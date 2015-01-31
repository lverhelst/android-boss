package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Random;

import verhelst.Comp.LCell;
import verhelst.Comp.LLabel;
import verhelst.Comp.LTable;
import verhelst.CustomActors.HealthBar;
import verhelst.CustomActors.SpriteActor;

/**
 * Created by Orion on 11/2/2014.
 */
public class BattleView {

    HealthBar one, two;
    Stage stage;
    LLabel highscorelbl, scorelbl, streaklbl, hitcount, oneLbl, twoLbl;


    Random rng;
    Stage battleStage;
    OrthographicCamera camera;
    LLabel r2c1, r3c2, endMessageLbl;
    Battle b;
    Actor lootActor;
    public SpriteActor butterbeaver, landingpad;
    Weapon aWep, bWep;
    LTable rootTable, row3, row4, butterTable;
    Table statsTable;
    public final float PADDING;


    boolean debug = true;

    public BattleView(Battle battle) {
        rng = new Random();
        b = battle;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        PADDING = Assets.bf.getBounds("|||").width + 10;


        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        /***  HUD ***/

        one = new HealthBar(b.getLeftside());
        one.setHealth_value(10);

        oneLbl = new LLabel(b.getLeftside().getName() + 200, skin);
        oneLbl.setIsHUD(true);

        two = new HealthBar(b.getRightside());

        twoLbl = new LLabel(b.getRightside().getName() + 200, skin);
        twoLbl.setIsHUD(true);

        highscorelbl = new LLabel("Highscore: 0", skin);
        highscorelbl.setIsHUD(true);
        scorelbl = new LLabel("Score: 0", skin);
        scorelbl.setIsHUD(true);
        streaklbl = new LLabel("Streak: 0", skin);
        streaklbl.setIsHUD(true);
        hitcount = new LLabel("Hits: 0", skin);
        hitcount.setIsHUD(true);


        /*** Battle Peices ***/
        r2c1 = new LLabel("ABCD", skin); //Right - bottom alight, left half
        r2c1.setIsHUD(true);
        Label r2c2 = new Label("", skin); //Empty Right half

        endMessageLbl = new LLabel("End Message Here", skin);
        endMessageLbl.setWrap(true);

        r3c2 = new LLabel("R3C2", skin); //Left stars
        r3c2.setIsHUD(true);

        // Label r3c3 = new Label("R3C3", skin); //Left Char
        Label r3c4 = new Label("R3C4", skin); //Middle space
        Label r3c5 = new Label("", skin); //Right char

        Label r5c2 = new Label("R5C2", skin); //Left weapon
        Label r5c3 = new Label("", skin); //Middle Space
        Label r5c4 = new Label("R5C4", skin); //Right weapon

        Label r6c1 = new Label("R6C1", skin); //Centre- top align, loot weapon & bottom row

        rootTable = new LTable(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        /*Table healthbarstable = new Table();
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
        */


        Table row1 = new Table();
        row1.add(r2c2).expand().bottom(); //To SHow Level Number
        row1.add(r2c2).expand().fill(); //Blank
        row1.add(endMessageLbl).expand().center();
        row1.add(r2c2).expand().fill(); //Blank
        row1.add(r2c2).expand().fill(); //Blank
        row1.setDebug(debug);
        row1.row();

        rootTable.addActor(row1);
        rootTable.addRow();

        Table row1point5 = new Table();

        row1point5.row();
        //rootTable.add(row1point5).expandX().fill();
        //rootTable.row();

        LTable row2 = new LTable(0, 0, 100, 100);

        row2.addActor(one, 2);//.expand().fill(); //Stars A

        row2.addActor(battle.getLeftside());//.expand().fill().left();  //Char A
        row2.addActor(battle.getRightside());//.expand().fill().right(); //Char B
        row2.addActor(two, 2);//.expand().fill(); //Stars B
        // row2.setDebug(debug);
        rootTable.addActor(row2, 2);
        rootTable.addRow();

        row3 = new LTable(0, 0, 100, 100);
        aWep = Weapon.generateRandomWeapon(10, Weapon.POSITION.LEFT_POSITION);
        aWep.setVisible(false);

        row3.addActor(r5c3).setWidth_percent(10);//.expand().fill();
        row3.addActor(aWep.getTable()).setWidth_percent(35);//.right().expand().fill();//.pad(PADDING); //A Weapon

        row3.addActor(r5c3).setWidth_percent(10);//.expand();         //Space
        bWep = Weapon.generateRandomWeapon(10, Weapon.POSITION.RIGHT_POSITION);
        bWep.setVisible(false);
        row3.addActor(bWep.getTable()).setWidth_percent(35);//.left().expand().fill();//.pad(PADDING); //B Weapon
        row3.addActor(r5c3).setWidth_percent(10);//.expand().fill();

        rootTable.addActor(row3);
        rootTable.addRow();
        //rootTable.addRow();
        row4 = new LTable(0, 0, 100, 100);

        statsTable = new Table();


        statsTable.add(r2c1).expand().top().left();
        statsTable.row();
        statsTable.add(r3c2).expand().top().left();
        statsTable.row();
        statsTable.add(streaklbl).expand().align(Align.left).top();
        statsTable.row();
        statsTable.add(hitcount).expand().align(Align.left).top();
        statsTable.row();
        statsTable.add(scorelbl).expand().top().left();
        statsTable.row();
        statsTable.add(highscorelbl).expand().top().left();
        statsTable.row();
        /*Table row2pt2 = new Table();
        row2pt2.add(r2c1).expand().top().right();
        row2pt2.row();
        row2pt2.add(r3c2).expand().right();
        row2pt2.row();
        row2pt2.setDebug(debug);*/

        statsTable.setDebug(debug);

        lootActor = Weapon.generateRandomWeapon(10, Weapon.POSITION.LOOT_POSITION);
        lootActor.setVisible(false);


        row4.addActor(statsTable);
        // row4.addActor(new Label("", skin));
        //.getTable(skin)
        row4.addActor(((Weapon) lootActor).getTable(), true);//.center().top().expand().fill();//.pad(PADDING); //LOOT
        // row4.addActor(new Label("", skin));//.expand().fill();

        butterTable = new LTable(10, 10, 10, 10);

        butterTable.addActor(new Label("", skin));
        butterbeaver = new SpriteActor(Assets.butterBeaver);
        butterTable.addActor(butterbeaver, true);
        landingpad = new SpriteActor(Assets.landing_pad);
        butterTable.addActor(landingpad, true);
        butterTable.addRow();
        row4.addActor(butterTable);//.expand().fill();


        rootTable.addActor(row4);//.expand().fill();

        //rootTable.setDebug(debug);
        //rootTable.setFillParent(true);


        battleStage = new Stage(new ScalingViewport(Scaling.fit, camera.viewportWidth, camera.viewportHeight, camera), RngFight.batch);
        battleStage.addActor(rootTable);

    }

    public Stage getStage() {
        return battleStage;
    }

    public OrthographicCamera getCamera() {
        return camera;
    }

    public LCell getCellForLoot() {
        LCell c = row4.getLCellForActor(lootActor);
        if (lootActor instanceof Weapon) {
            System.out.println("Searching for weapon table");
            c = row4.getLCellForActorName(((Weapon) lootActor).getTable().getName());
        }
        return c;
    }


    public void setLoot(Actor newloot) {
        LCell c = row4.getLCellForActor(lootActor);
        if (lootActor instanceof Weapon) {
            //System.out.println("Searching for weapon table");
            c = row4.getLCellForActorName(((Weapon) lootActor).getTable().getName());
        }

       // System.out.println(lootActor);
        if (c != null) {
            //System.out.println("Cell found");
            if (newloot instanceof Weapon) {
                c.setActor(((Weapon) newloot).getTable());
                c.setKeep_aspect_ratio(false);
                //   System.out.println("Weapon max dmaage" + ((Weapon)newloot).getMax_damage());

            } else {
                c.setActor(newloot);
                c.setKeep_aspect_ratio(true);


            }
            lootActor = newloot;
        }
        //System.out.println(lootActor);
    }

    public void updateCharacterWeapons(Weapon aWeapon, Weapon bWeapon) {


        //System.out.println("Update Character Weapons");
        if (aWeapon != null && b.getLeftside().isWeaponEquipped()) {
            //aWep.copyWeapon(aWeapon, Weapon.POSITION.LEFT_POSITION);
            LCell c = row3.getLCellForActor(aWep.getTable());
            if (c != null) {
               // System.out.println("AWeaponFound");
                c.setActor(aWeapon.getTable());
                aWep = aWeapon;
            }
            aWep.setVisible(true);
        } else {
            aWep.setVisible(false);
        }

        if (bWeapon != null && b.getRightside().isWeaponEquipped()) {


            //bWep.copyWeapon(bWeapon, Weapon.POSITION.RIGHT_POSITION);
            LCell c = row3.getLCellForActor(bWep.getTable());
            if (c != null) {
                c.setActor(bWeapon.getTable());
                bWep = bWeapon;
            }
            bWep.setVisible(true);
        } else {
            bWep.setVisible(false);
        }
    }


    public void update(int lefthp, int righthp, boolean showLoot, int highscore, String message, int hits, int score) {
        b.getLeftside().setDisplay_hp(lefthp);
        b.getRightside().setDisplay_hp(righthp);

        lootActor.setVisible(showLoot);

        String line = "";
        for (int i = 0; i < b.getRightside().getWin_streak() % b.getRightside().getWins_to_level(); i++) {
            line += "*";
        }

        String str = String.format("%3s", line + b.getLeftside().getLevel());
        r3c2.setText("Cur Lvl: " + str);
        line = "";
        for (int i = 0; i < b.getLeftside().getMax_wtnl(); i++) {
            line += "*";
        }
        String str2 = String.format("%3s", line + b.getLeftside().getMax_level());
        r2c1.setText("Max Lvl: " + str2);
        endMessageLbl.setText(message);

        one.setHealth_value(lefthp);
        //oneLbl.setText(b.getLeftside().getName() + ": " + (lefthp > 0 ? lefthp : 0));

        two.setHealth_value(righthp);
        //twoLbl.setText(b.getRightside().getName() + ": " + (righthp > 0 ? righthp : 0));

        streaklbl.setText("Streak: " + b.getRightside().getWin_streak());
        hitcount.setText("Hits: " +  NumberFormat.getNumberInstance(Locale.US).format(hits));

        if(score > highscore){
            highscore = score;
        }
        highscorelbl.setText("Highscore: " +  NumberFormat.getNumberInstance(Locale.US).format(highscore));
        line = "";
        if(message.contains("Victory")){
               score *= 2;
        }
        scorelbl.setText("Score: " +  NumberFormat.getNumberInstance(Locale.US).format(score) + line);
        scorex = (int)(Assets.wepNumFnt.getBounds("Score: " +  NumberFormat.getNumberInstance(Locale.US).format(score)).width);
        scorey = (int) (scorelbl.getY() + Assets.wepNumFnt.getBounds("Score: " +  NumberFormat.getNumberInstance(Locale.US).format(score)).height * 2);

    }

    int scorex, scorey;

    public int getScoreX(){
        return scorex;
    }

    public int getScoreY(){
        return scorey;
    }

    public void landingPadGlow(boolean pad) {
        if (pad) {
            landingpad.setDisplaysprite(Assets.landing_pad_glow);
        } else {
            landingpad.setDisplaysprite(Assets.landing_pad);
        }
    }

    public void setPlayerWeaponGlow(boolean glow, int glow_type){
        LCell c = row3.getLCellForActorName(((Weapon)bWep).getTable().getName());
        if(c!= null){
            c.setGlow(glow);
            c.setGlow_type(glow_type);
        }
        else{
            System.out.println("Set Player Weapon Glow, cell not found!");
        }
    }
}
