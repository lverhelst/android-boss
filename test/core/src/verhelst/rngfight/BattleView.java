package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;


import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

/**
 * Created by Orion on 11/2/2014.
 */
public class BattleView {

    enum DmgListSide{
        LEFT,
        RIGHT
    }

    Random rng;
    Stage battleStage;
    OrthographicCamera camera;
    Label r3c2;
    Battle b;
    final Weapon lootWep, aWep, bWep;
    Table rootTable;

    //Damage Numbers during battle
    ArrayList<DamageNumber> dnListA = new ArrayList<DamageNumber>();
    ArrayList<DamageNumber> dnListB = new ArrayList<DamageNumber>();

    //For rendering damage numbers
    Iterator<DamageNumber> i;

    public BattleView(Battle battle, Weapon dummA, Weapon dummB, Weapon dummC){
        rng = new Random();
        b= battle;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        Label r2c1 = new Label("", skin); //Right - bottom alight, left half
        Label r2c2 = new Label("", skin); //Empty Right half

        r3c2  = new Label("R3C2", skin); //Left stars
       // Label r3c3 = new Label("R3C3", skin); //Left Char
        Label r3c4 = new Label("R3C4", skin); //Middle space
        Label r3c5 = new Label("", skin); //Right char

        Label r5c2 = new Label("R5C2", skin); //Left weapon
        Label r5c3 = new Label("", skin); //Middle Space
        Label r5c4 = new Label("R5C4", skin); //Right weapon

        Label r6c1 = new Label("R6C1", skin); //Centre- top align, loot weapon & bottom row

        rootTable = new Table();

        Table row1 = new Table();
        row1.add(r2c1).expand().align(Align.bottomRight); //To SHow Level Number
        row1.add(r2c2).expand(); //Blank
        rootTable.add(row1).expand().fill();
        rootTable.row();
        Table row2 = new Table();
        row2.add(r3c2).right().expand(); //Stars A
        row2.add(battle.getLeftside()).expand().fill().left();  //Char A
        row2.add(battle.getRightside()).expand().fill().right(); //Char B
        row2.add(r3c5).left().expand(); //Stars B
        row2.setDebug(true);
        rootTable.add(row2).expand().fill();
        rootTable.row();

        Table row3 = new Table();
        aWep = Weapon.generateRandomWeapon(10, Assets.getWeaponSprite(), Weapon.POSITION.LEFT_POSITION);
       // aWep.copyWeapon(dummA, Weapon.POSITION.LEFT_POSITION);
        aWep.setVisible(false);
        row3.add(r5c3).expand().fill();
        row3.add(aWep).right().expand().fill(); //A Weapon

        row3.add(r5c3).expand();         //Space
        bWep = Weapon.generateRandomWeapon(10, Assets.getWeaponSprite(), Weapon.POSITION.RIGHT_POSITION);
       // bWep.copyWeapon(dummB, Weapon.POSITION.RIGHT_POSITION);
        bWep.setVisible(false);
        row3.add(bWep).left().expand().fill(); //B Weapon
        row3.add(r5c3).expand().fill();

        rootTable.add(row3).expand().fill();
        rootTable.row();

        Table row4 = new Table();
        lootWep = dummC;
        row4.add(new Label("", skin)).expand().fill();
        row4.add(new Label("", skin)).expand().fill();
        row4.add(lootWep).center().top().expand().fill(); //LOOT
        row4.add(new Label("", skin)).expand().fill();
        row4.add(new Label("", skin)).expand().fill();
        rootTable.add(row4).expand().fill();

        rootTable.setDebug(true);
        rootTable.setFillParent(true);

        battleStage = new Stage(new ScalingViewport(Scaling.fit, camera.viewportWidth, camera.viewportHeight, camera), RngFight.batch);
        battleStage.addActor(rootTable);

    }

    public Stage getStage(){
        return battleStage;
    }

    public OrthographicCamera getCamera(){
        return camera;
    }


    //Adds a new damage number to the appropriate damage list
    private void addDmgNum(int num, int x, int y, DmgListSide side){
        DamageNumber dn = new DamageNumber(num, x, y);
        if(side == DmgListSide.LEFT)
            synchronized (dnListA) {
                dnListA.add(dn);
            }
        else
            synchronized (dnListB) {
                dnListB.add(dn);
            }
    }

    public void consumeDmgNumPost(int num, DmgListSide side){

        float y =  b.getRightside().otherY + b.getLeftside().getHeight() + (rng.nextBoolean() ? - 1 : 1) * rng.nextInt(10);
        float x;
        if(side == DmgListSide.RIGHT){
            x = b.getRightside().getX() + b.getRightside().getWidth()/2 + (rng.nextBoolean() ? - 1 : 1) * rng.nextInt((int)b.getRightside().getWidth()/4);

        }else{
            x = b.getLeftside().getX() + b.getLeftside().getWidth()/2 + (rng.nextBoolean() ? - 1 : 1) * rng.nextInt((int)b.getLeftside().getWidth()/4);
        }
        addDmgNum(num, (int)x,(int)y, side);
    }

    public void renderDamageNumbers(SpriteBatch batch){
        batch.begin();
        //Add Damage Numbers to screen
        synchronized (dnListA) {

            for (i = dnListA.iterator(); i.hasNext(); ) {

                DamageNumber dn = i.next();
                if (dn.isRemoveable()) {
                    i.remove();
                } else {
                    Assets.dmgNumFnt.setColor(dn.getRed(), dn.getGreen(), dn.getBlue(), dn.getAlpha());
                    Assets.dmgNumFnt.draw(batch, dn.getCs(), dn.getX(), dn.getY());
                    dn.update(1);
                }
            }
        }
        //Add damage Numbers to Screen
        synchronized (dnListB) {
            for (i = dnListB.iterator(); i.hasNext(); ) {
                DamageNumber dn = i.next();
                if (dn.isRemoveable()) {
                    i.remove();
                } else {
                    Assets.dmgNumFnt.setColor(dn.getRed(), dn.getGreen(), dn.getBlue(), dn.getAlpha());
                    Assets.dmgNumFnt.draw(batch, dn.getCs(), dn.getX(), dn.getY());
                    dn.update(1);
                }
            }
        }
        batch.end();
    }


    public void setLoot(Weapon copy){
        System.out.println("Set Loot");
        lootWep.copyWeapon(copy, Weapon.POSITION.LOOT_POSITION);
    }

    public void updateCharacterWeapons(Weapon aWeapon, Weapon bWeapon){

        if(b.getLeftside().isWeaponEquipped()) {

          aWep.copyWeapon(aWeapon, Weapon.POSITION.LEFT_POSITION);
          aWep.setVisible(true);
        }else{
            aWep.setVisible(false);
        }

        if(b.getRightside().isWeaponEquipped()){
          bWep.setVisible(true);
          bWep.copyWeapon(bWeapon, Weapon.POSITION.RIGHT_POSITION);
        }else{
            bWep.setVisible(false);
        }
    }


    public void update(int lefthp, int righthp, boolean showLoot){
        b.getLeftside().setHealth(lefthp);
        b.getRightside().setHealth(righthp);
        lootWep.setVisible(showLoot);
        String line = "";
        for(int i = 0; i < b.getLeftside().getLose_streak() % 3; i++ ){
            line += "*";
        }
        r3c2.setText(line);
    }



}
