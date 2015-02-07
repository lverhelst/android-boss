package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import verhelst.Craft.cTOKEN;

/**
 * Created by Leon I. Verhelst on 10/25/2014.
 */
public class Assets {

    public enum STATE {
        LOADINGTEXTUREATLASES,
        LOADINGOTHERTEXTURES,
        ALLDONE,
        DONESKI;
    }

    public static Animation face_anim;
    public static Skin skin;
    public static List<Sprite> weapons_sprites;
    public static Sprite weapon_data_icon;
    public static Sprite resting_face;
    public static Sprite dead_face;
    public static Sprite craft_btn;
    public static Sprite[] arms, pants, shirts, shoulders, faces;
    public static BitmapFont wepNumFnt;
    public static BitmapFont dmgNumFnt;
    public static BitmapFont bf;
    public static BitmapFont HUDbf;
    public static Texture dmgIconTxture, dmgIconSmallTxture, hrtIconTxture, glow, glow_ylw;
    public static Sprite dmgIcon, hrtIcon;
    public static Sprite butterBeaver, landing_pad, landing_pad_glow, back_btn, reset_closed, reset_opened, reset_pressed, mystery_sprite, FITE_SIGN, STATS, DRES, INDEV, game_controller, game_leader, game_achieve, signin, signout, submitscore, bbCraft;
    public static Sprite IRON, DUST, CLOTH, BONE;
    private static int armcount, pantcount, shirtcount, shouldercount, facecount, weaponcount;
    private static Random rng = new Random();


    private static boolean debug;

    //values: 1 = unlocked, 0 = locked
    //0 -> head/face, 1-> shirts/torso, 2 -> legs/pants, 3->shoulder, 4->elbow/arms
    static int[][] unclocks = new int[5][100];
    //weapons
    static int[] weaponUnlocks = new int [100];

    public static int[] lastSuitGiven = new int[5];

    AssetManager manager;

    public Assets() {
        manager = new AssetManager();
        weapons_sprites = new ArrayList<Sprite>();
        facecount = 19;
        armcount = 19;
        pantcount = 19;
        shirtcount = 19;
        shouldercount = 19;
        weaponcount = 50;

        faces = new Sprite[facecount];
        pants = new Sprite[pantcount];
        shirts = new Sprite[shirtcount];
        arms = new Sprite[armcount];
        shoulders = new Sprite[shouldercount];
        //unlock first suit
        for (int i = 0; i < 5; i++) {
            unclocks[i][0] = 1;
        }

        debug = false;

        loadTextureAtlases();
    }

    //Load graphical assets
    public void loadAssets() {
        //skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        //Load character images & animation
        //TextureAtlas ta = new TextureAtlas(Gdx.files.internal("face_sprites\\sprite.pack"));
        //TextureRegion[] hairframes = {new TextureRegion(ta.findRegion("face11")),new TextureRegion(ta.findRegion("face12")),new TextureRegion(ta.findRegion("face13")),new TextureRegion(ta.findRegion("face14"))};
        //face_anim = new Animation(0.075f, hairframes);


        //Load Weapon Sprites
        //TODO: Make more weapon sprites

    }

    public static STATE state;

    public void loadTextureAtlases(){
        state = STATE.LOADINGTEXTUREATLASES;
        manager.load("face_sprites\\sprite.pack", TextureAtlas.class);
        manager.load("In Development\\weaponFullSize.pack", TextureAtlas.class);

    }

    public void setSuitsAndWeapons(){
        manager.finishLoading();
        TextureAtlas ta = manager.get("face_sprites/sprite.pack");
        resting_face = new Sprite(ta.findRegion("face1"));
        dead_face = new Sprite(ta.findRegion("face1"));

        //faces

        for (int i = 1; i <= facecount; i++) {
            //  System.out.println(i);
            faces[i - 1] = new Sprite(ta.findRegion("face" + i));
        }
        //torsos
        for (int i = 1; i <= shirtcount; i++) {
            shirts[i - 1] = new Sprite(ta.findRegion("torso" + i));
        }
        //legs
        for (int i = 1; i <= pantcount; i++) {
            pants[i - 1] = new Sprite(ta.findRegion("legs" + i));
        }
        //shoulders
        for (int i = 1; i <= armcount; i++) {
            arms[i - 1] = new Sprite(ta.findRegion("elbow" + i));
        }
        //elbows
        for (int i = 1; i <= shouldercount; i++) {
            shoulders[i - 1] = new Sprite(ta.findRegion("shoulder" + i));
        }

        ta = manager.get("In Development/weaponFullSize.pack");
        for (int i = 1; i <= weaponcount; i++) {
            weapons_sprites.add(new Sprite(ta.findRegion("weapon" + String.format("%02d", i))));
        }

    }



    public void loadOtherAssets(){
        state = STATE.LOADINGOTHERTEXTURES;
        manager.load("data/uiskin.json", Skin.class);
        manager.load("wepdataicon.png", Texture.class);

        manager.load("wepdataicon.png", Texture.class);

        manager.load("In Development\\DmgIcon.png", Texture.class);
        manager.load("In Development\\DmgIcon.png", Texture.class);
        manager.load("In Development\\DmgIconSmall.png", Texture.class);

        manager.load("In Development\\HrtIcon.png", Texture.class);
        manager.load("In Development\\HrtIcon.png", Texture.class);

        manager.load("In Development\\glow.png", Texture.class);
        manager.load("In Development\\glow_ylw.png", Texture.class);

        manager.load("In Development\\repBB.png", Texture.class);
        manager.load("In Development\\bbFITE.png", Texture.class);
        manager.load("In Development\\bbSTAT.png", Texture.class);
        manager.load("In Development\\bbDRES.png", Texture.class);
        manager.load("In Development\\bbOTHE.png", Texture.class);
        manager.load("In Development\\bbCraft.png", Texture.class);


        manager.load("In Development\\repOG.png", Texture.class);
        manager.load("In Development\\repOG_hover.png", Texture.class);
        manager.load("In Development\\craft.png", Texture.class);
        manager.load("In Development\\repARW.png", Texture.class);
        manager.load("In Development\\ResetButtonClosed.png", Texture.class);
        manager.load("In Development\\ResetButtonOpen.png", Texture.class);
        manager.load("In Development\\ResetButtonDepressed.png", Texture.class);
        manager.load("In Development\\mystery_sprite.png", Texture.class);
        manager.load("games_controller.png", Texture.class);
        manager.load("bb_leaderboards.png", Texture.class);
        manager.load("bb_achievements.png", Texture.class);
        manager.load("bb_signin.png", Texture.class);
        manager.load("bb_signout.png", Texture.class);
        manager.load("submitscore.png", Texture.class);


        manager.load("In Development\\Materials\\IRON.png", Texture.class);
        manager.load("In Development\\Materials\\DUST.png", Texture.class);
        manager.load("In Development\\Materials\\BONE.png", Texture.class);
        manager.load("In Development\\Materials\\CLOTH.png", Texture.class);


    }

    public void setOtherAssets(){

        weapon_data_icon = new Sprite((Texture)manager.get("wepdataicon.png"));

        dmgIcon = new Sprite((Texture)manager.get("In Development/DmgIcon.png"));
        dmgIconTxture = (Texture)manager.get("In Development/DmgIcon.png");
        dmgIconSmallTxture = (Texture)manager.get("In Development/DmgIconSmall.png");

        hrtIcon = new Sprite((Texture)manager.get("In Development/HrtIcon.png"));
        hrtIconTxture = (Texture)manager.get("In Development/HrtIcon.png");

        glow = (Texture)manager.get("In Development/glow.png");
        glow_ylw = (Texture)manager.get("In Development/glow_ylw.png");

        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("game_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(28 * Gdx.graphics.getDensity());
        bf = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(24 * Gdx.graphics.getDensity());
        wepNumFnt = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(12 * Gdx.graphics.getDensity());
        dmgNumFnt = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(20 * Gdx.graphics.getDensity());
        HUDbf = ftfg.generateFont(ftfp);

        butterBeaver = new Sprite((Texture)manager.get("In Development/repBB.png"));
        FITE_SIGN = new Sprite((Texture)manager.get("In Development/bbFITE.png"));
        STATS = new Sprite((Texture)manager.get("In Development/bbSTAT.png"));
        DRES = new Sprite((Texture)manager.get("In Development/bbDRES.png"));
        INDEV = new Sprite((Texture)manager.get("In Development/bbOTHE.png"));
        bbCraft = new Sprite((Texture)manager.get("In Development/bbCraft.png"));


        landing_pad = new Sprite((Texture)manager.get("In Development/repOG.png"));
        landing_pad_glow = new Sprite((Texture)manager.get("In Development/repOG_hover.png"));
        craft_btn = new Sprite((Texture)manager.get("In Development/craft.png"));
        back_btn = new Sprite((Texture)manager.get("In Development/repARW.png"));
        reset_closed = new Sprite((Texture)manager.get("In Development/ResetButtonClosed.png"));
        reset_opened = new Sprite((Texture)manager.get("In Development/ResetButtonOpen.png"));
        reset_pressed = new Sprite((Texture)manager.get("In Development/ResetButtonDepressed.png"));
        mystery_sprite = new Sprite((Texture)manager.get("In Development/mystery_sprite.png"));
        game_controller =  new Sprite((Texture)manager.get("games_controller.png"));
        game_leader =  new Sprite((Texture)manager.get("bb_leaderboards.png"));
        game_achieve =  new Sprite((Texture)manager.get("bb_achievements.png"));
        signin =  new Sprite((Texture)manager.get("bb_signin.png"));
        signout =  new Sprite((Texture)manager.get("bb_signout.png"));
        submitscore =  new Sprite((Texture)manager.get("submitscore.png"));


        IRON = new Sprite((Texture)manager.get("In Development/Materials/IRON.png"));
        DUST = new Sprite((Texture)manager.get("In Development/Materials/DUST.png"));
        BONE = new Sprite((Texture)manager.get("In Development/Materials/BONE.png"));
        CLOTH = new Sprite((Texture)manager.get("In Development/Materials/CLOTH.png"));



        state = STATE.ALLDONE;
    }

    public boolean managerUpdate(){
        return manager.update();
    }


    public static Sprite getWeaponSprite() {
        return weapons_sprites.get(rng.nextInt(weapons_sprites.size()));
    }

    public static Sprite findSpriteForName(String name) {
        if (name.equals("head"))
            return resting_face;
        if (name.equals("shoulder"))
            return shoulders[0];
        if (name.equals("leg"))
            return pants[0];
        if (name.equals("torso"))
            return shirts[0];
        if (name.equals("wrist"))
            return getWeaponSprite();
        return arms[0];
    }

    public static Sprite findSpriteForName(String name, int index) {
        if (name.equals("head"))
            return faces[index];
        if (name.equals("shoulder"))
            return shoulders[index];
        if (name.equals("leg"))
            return pants[index];
        if (name.equals("torso"))
            return shirts[index];
        if (name.equals("wrist"))
            return weapons_sprites.get(index);
        return arms[index];
    }




    public static Sprite[] getSuitForLevelNoCheck(int lvl) {
        int index = lvl / 5;

        Sprite[] suit = new Sprite[5];
        int i = index;
        //head
        if (index < facecount) {
            suit[0] = faces[index];
            i = index;
        } else {
            i = rng.nextInt(facecount);
            suit[0] = faces[i];
        }
        lastSuitGiven[0] = i;
        //torso
        if (index < shirtcount){
            suit[1] = shirts[index];
            i = index;
        }else {
            i = rng.nextInt(shirtcount);
            suit[1] = shirts[i];
        }
        lastSuitGiven[1] = i;
        //leg
        if(index  < pantcount) {
            i = index;
            suit[2] = pants[index];
        }else {
            i = rng.nextInt(pantcount);
            suit[2] = pants[i];
        }
        lastSuitGiven[2] = i;
        //arm
        if(index  < shouldercount) {
            i = index;
            suit[3] = shoulders[index];
        }else {
            i = rng.nextInt(shouldercount);
            suit[3] = shoulders[i];
        }
        lastSuitGiven[3] = i;
        //elbow
        if(index < armcount ) {
            i = index;
            suit[4] = arms[index];
        }else {
            i = rng.nextInt(armcount);
            suit[4] = arms[i];
        }
        lastSuitGiven[4] = i;
        return suit;
    }

    public static Sprite[] getSuitForLevel(int lvl) {
        int index = lvl / 10;

        Sprite[] suit = new Sprite[5];
        //head

        if (debug || unclocks[0][Math.min(index, facecount - 1)] == 1)
            suit[0] = faces[Math.min(index, facecount - 1)];
        else
            suit[0] = mystery_sprite;

        //torso
        if (debug || unclocks[1][Math.min(index, shirtcount - 1)] == 1) {
            suit[1] = shirts[Math.min(index, shirtcount - 1)];
        } else {
            suit[1] = mystery_sprite;
        }
        //leg
        if (debug || unclocks[2][Math.min(index, pantcount - 1)] == 1) {
            suit[2] = pants[Math.min(index, pantcount - 1)];
        } else {
            suit[2] = mystery_sprite;
        }
        //arm
        if (debug || unclocks[3][Math.min(index, shouldercount - 1)] == 1) {
            suit[3] = shoulders[Math.min(index, shouldercount - 1)];
        } else {
            suit[3] = mystery_sprite;
        }

        //elbow
        if (debug || unclocks[4][Math.min(index, armcount - 1)] == 1) {
            suit[4] = arms[Math.min(index, armcount - 1)];
        } else {
            suit[4] = mystery_sprite;
        }

        return suit;
    }

    public static void unlockItem(int type, int index) {
        boolean unlock = true;
        if(type == -1) {
            weaponUnlocks[index] = 1;

            for(int i = 0; i < weaponUnlocks.length; i++){
                unlock &= weaponUnlocks[i] == 1;
                if(!unlock)
                    break;
            }
            if(unlock)
                RngFight.actionResolver.unlockAchievement("weapon_master");

        }
        else {
            unclocks[type][index] = 1;

            for(int i = 0; i < unclocks.length; i++){
                for(int j = 0; j < unclocks[i].length; j++){
                    unlock &= unclocks[i][j] == 1;
                    if(!unlock)
                        break;
                }
                if(!unlock)
                    break;
            }
            if(unlock)
                RngFight.actionResolver.unlockAchievement("shopaholic");

        }
    }

    public static List<Sprite> getWeaponsList() {
        ArrayList<Sprite> las = new ArrayList<Sprite>();
        for (int i = 0; i < weapons_sprites.size(); i++) {
            if (debug || weaponUnlocks[i] == 1) {
                las.add(weapons_sprites.get(i));
            } else {
                las.add(mystery_sprite);
            }
        }
        return las;
    }

    public static Sprite getMatSprite(cTOKEN token){
        switch(token){
            case IRON: return IRON;
            case CLOTH: return CLOTH;
            case BONE: return BONE;
            case DUST: return DUST;
            default: return mystery_sprite;
        }
    }
}
