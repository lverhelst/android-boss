package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Cell;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
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



    HealthBar one, two;
    Stage stage;
    LeonLabel highscore, hitcount, oneLbl, twoLbl;


    Random rng;
    Stage battleStage;
    OrthographicCamera camera;
    LeonLabel r2c1, r3c2, endMessageLbl;
    Battle b;
    final Weapon lootWep, aWep, bWep;
    Table rootTable;
    public final float PADDING;

    //Damage Numbers during battle
    ArrayList<DamageNumber> dnListA = new ArrayList<DamageNumber>();
    ArrayList<DamageNumber> dnListB = new ArrayList<DamageNumber>();

    //For rendering damage numbers
    Iterator<DamageNumber> i;

    boolean debug = false;

    public BattleView(Battle battle){
        rng = new Random();
        b= battle;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        PADDING = Assets.bf.getBounds("|||").width + 10;


        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        /***  HUD ***/

        one = new HealthBar(b.getLeftside());
        one.setHealth_value(10);

        oneLbl = new LeonLabel(b.getLeftside().getName() + 200, skin);
        oneLbl.isHUD = true;

        two = new HealthBar(b.getRightside());

        twoLbl = new LeonLabel(b.getRightside().getName() + 200, skin);
        twoLbl.isHUD = true;

        highscore = new LeonLabel("Streak: 0", skin);
        highscore.isHUD = true;
        hitcount = new LeonLabel("Hits: 0",skin);
        hitcount.isHUD = true;


        /*** Battle Peices ***/
        r2c1 = new LeonLabel("ABCD", skin); //Right - bottom alight, left half
        Label r2c2 = new Label("", skin); //Empty Right half

        endMessageLbl = new LeonLabel("End Message Here", skin);
        endMessageLbl.setWrap(true);

        r3c2  = new LeonLabel("R3C2", skin); //Left stars

       // Label r3c3 = new Label("R3C3", skin); //Left Char
        Label r3c4 = new Label("R3C4", skin); //Middle space
        Label r3c5 = new Label("", skin); //Right char

        Label r5c2 = new Label("R5C2", skin); //Left weapon
        Label r5c3 = new Label("", skin); //Middle Space
        Label r5c4 = new Label("R5C4", skin); //Right weapon

        Label r6c1 = new Label("R6C1", skin); //Centre- top align, loot weapon & bottom row

        rootTable = new Table();
        Table healthbarstable = new Table();
        healthbarstable.add(one).expand().fill().align(Align.left);
        healthbarstable.row();
       // healthbarstable.add(oneLbl).expand().fill().align(Align.left);
       // healthbarstable.row();
        healthbarstable.add(two).expand().fill().align(Align.left);
        healthbarstable.row();
        //healthbarstable.add(twoLbl).expand().fill().align(Align.left);
       // healthbarstable.row();
        healthbarstable.setDebug(debug);
        rootTable.add(healthbarstable).expandX().fillX();
        rootTable.row();


        Table row1 = new Table();
        row1.add(r2c2).expand().bottom(); //To SHow Level Number
        row1.add(r2c2).expand().fill(); //Blank
        row1.add(endMessageLbl).expand().center();
        row1.add(r2c2).expand().fill(); //Blank
        row1.add(r2c2).expand().fill(); //Blank
        row1.setDebug(debug);
        row1.row();

        rootTable.add(row1).expand().fillX();
        rootTable.row();

        Table row1point5 = new Table();

        row1point5.row();
        //rootTable.add(row1point5).expandX().fill();
        //rootTable.row();

        Table row2 = new Table();
            Table row2pt2 = new Table();
            row2pt2.add(r2c1).expand().top().right();
            row2pt2.row();
            row2pt2.add(r3c2).expand().right();
            row2pt2.row();
            row2pt2.setDebug(debug);
        row2.add(row2pt2).expand().fill(); //Stars A
        row2.add(battle.getLeftside()).expand().fill().left();  //Char A
        row2.add(battle.getRightside()).expand().fill().right(); //Char B
        row2.add(r3c5).expand().left(); //Stars B
        row2.setDebug(debug);
        rootTable.add(row2).expand().fill();
        rootTable.row();

        Table row3 = new Table();
        aWep = Weapon.generateRandomWeapon(10, Assets.getWeaponSprite(), Weapon.POSITION.LEFT_POSITION);
        aWep.setVisible(false);
        row3.add(r5c3).expand().fill();
        row3.add(aWep).right().expand().fill().pad(PADDING); //A Weapon

        row3.add(r5c3).expand();         //Space
        bWep = Weapon.generateRandomWeapon(10, Assets.getWeaponSprite(), Weapon.POSITION.RIGHT_POSITION);
        bWep.setVisible(false);
        row3.add(bWep).left().expand().fill().pad(PADDING); //B Weapon
        row3.add(r5c3).expand().fill();

        rootTable.add(row3).expand().fill();
        rootTable.row();

        Table row4 = new Table();
        lootWep = Weapon.generateRandomWeapon(10, Assets.getWeaponSprite(), Weapon.POSITION.LOOT_POSITION);
        lootWep.setVisible(false);
        Table statsTable = new Table();
        statsTable.add(highscore).expand().align(Align.left);
        statsTable.row();
        statsTable.add(hitcount).expand().align(Align.left).top();
        statsTable.row();
        statsTable.setDebug(debug);


        row4.add(statsTable).expand().fill();
        row4.add(new Label("", skin)).expand().fill();
        row4.add(lootWep).center().top().expand().fill().pad(PADDING); //LOOT
        row4.add(new Label("", skin)).expand().fill();
        row4.add(new Label("", skin)).expand().fill();
        rootTable.add(row4).expand().fill();
        rootTable.row();

        rootTable.setDebug(debug);
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

        float y =  b.getRightside().otherY + b.getLeftside().getHeight()/2;// + (rng.nextBoolean() ? - 1 : 1) * rng.nextInt((int)b.getLeftside().getHeight()/2);
        float x;
        if(side == DmgListSide.RIGHT){
            x = b.getRightside().getX() + b.getRightside().getWidth()/2;// + (rng.nextBoolean() ? - 1 : 1) * rng.nextInt((int)b.getRightside().getWidth()/4);

        }else{
            x = b.getLeftside().getX() + b.getLeftside().getWidth()/2;// + (rng.nextBoolean() ? - 1 : 1) * rng.nextInt((int)b.getLeftside().getWidth()/4);
        }
        addDmgNum(num, (int) x, (int) y, side);
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
                    dn.update();
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
                    dn.update();
                }
            }
        }
        batch.end();
    }


    public void setLoot(Weapon copy){
      //  System.out.println("Set Loot");
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


    public void update(int lefthp, int righthp, boolean showLoot, int aMax, String message, int hits){
        b.getLeftside().setHealth(lefthp);
        b.getRightside().setHealth(righthp);



        lootWep.setVisible(showLoot);
        String line = "";
        for(int i = 0; i < b.getRightside().getWin_streak() % b.getRightside().wins_to_level; i++ ){
            line += "*";
        }

        String str = String.format("%3s", line + b.getLeftside().getLevel()) ;
        r3c2.setText(str);
        line = "";
        for(int i = 0; i < b.getLeftside().max_wtnl; i++ ){
            line += "*";
        }
        String str2  = String.format("%3s", line + b.getLeftside().max_level) ;
        r2c1.setText(str2);
        endMessageLbl.setText(message);

        one.setHealth_value(lefthp);
        //oneLbl.setText(b.getLeftside().getName() + ": " + (lefthp > 0 ? lefthp : 0));

        two.setHealth_value(righthp);
        //twoLbl.setText(b.getRightside().getName() + ": " + (righthp > 0 ? righthp : 0));


        highscore.setText("Streak: " + b.getRightside().getWin_streak());
        hitcount.setText("Hits: " + hits);

    }
}
