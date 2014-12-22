package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Leon I. Verhelst on 10/25/2014.
 */
public class Assets {

    public static Animation face_anim;
    public static List<Sprite> weapons_sprites;
    public static Sprite weapon_data_icon;
    public static Sprite resting_face;
    public static Sprite dead_face;
    public static Sprite[] arms, pants, shirts, shoulders, faces;
    public static BitmapFont wepNumFnt;
    public static BitmapFont dmgNumFnt;
    public static BitmapFont bf;
    public static BitmapFont HUDbf;
    public static Texture dmgIconTxture, hrtIconTxture, glow, glow_ylw;
    public static Sprite dmgIcon, hrtIcon;
    public static Sprite butterBeaver, landing_pad, landing_pad_glow, back_btn, reset_closed, reset_opened, reset_pressed, mystery_sprite;

    private static int armcount, pantcount, shirtcount, shouldercount, facecount, weaponcount;
    private static Random rng = new Random();


    private static boolean debug;

    //values: 1 = unlocked, 0 = locked
    //0 -> weps, 1-> head/face, 2-> shirts/torso, 3 -> legs/pants, 4->shoulder, 5->elbow/arms
    static int[][] unclocks = new int[6][35];

    public Assets() {
        weapons_sprites = new ArrayList<Sprite>();
        facecount = 27;
        armcount = 14;
        pantcount = 14;
        shirtcount = 14;
        shouldercount = 14;
        weaponcount = 35;

        faces = new Sprite[facecount];
        pants = new Sprite[pantcount];
        shirts = new Sprite[shirtcount];
        arms = new Sprite[armcount];
        shoulders = new Sprite[shouldercount];

        for (int i = 1; i < 6; i++) {
            unclocks[i][0] = 1;
        }

        debug = false;


    }

    //Load graphical assets
    public void loadAssets() {

        //Load character images & animation
        TextureAtlas ta = new TextureAtlas(Gdx.files.internal("face_sprites\\sprite.pack"));
        //TextureRegion[] hairframes = {new TextureRegion(ta.findRegion("face11")),new TextureRegion(ta.findRegion("face12")),new TextureRegion(ta.findRegion("face13")),new TextureRegion(ta.findRegion("face14"))};
        //face_anim = new Animation(0.075f, hairframes);
        resting_face = new Sprite(ta.findRegion("face1"));
        dead_face = new Sprite(ta.findRegion("face1"));
        //faces


        for (int i = 1; i <= facecount; i++) {
            System.out.println(i);
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


        //Load Weapon Sprites
        //TODO: Make more weapon sprites

        ta = new TextureAtlas(Gdx.files.internal("In Development\\weaponFullSize.pack"));
        for (int i = 1; i <= weaponcount; i++) {
            weapons_sprites.add(new Sprite(ta.findRegion("weapon" + String.format("%02d", i))));
        }
        weapon_data_icon = new Sprite(new Texture(Gdx.files.internal("wepdataicon.png")));

        dmgIcon = new Sprite(new Texture(Gdx.files.internal("In Development\\DmgIcon.png")));
        dmgIconTxture = new Texture(Gdx.files.internal("In Development\\DmgIcon.png"));
        hrtIcon = new Sprite(new Texture(Gdx.files.internal("In Development\\HrtIcon.png")));
        hrtIconTxture = new Texture(Gdx.files.internal("In Development\\HrtIcon.png"));

        glow = new Texture(Gdx.files.internal("In Development\\glow.png"));
        glow_ylw = new Texture(Gdx.files.internal("In Development\\glow_ylw.png"));

        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("game_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(32 * Gdx.graphics.getDensity());
        bf = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(28 * Gdx.graphics.getDensity());
        wepNumFnt = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(12 * Gdx.graphics.getDensity());
        dmgNumFnt = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(24 * Gdx.graphics.getDensity());
        HUDbf = ftfg.generateFont(ftfp);

        butterBeaver = new Sprite(new Texture(Gdx.files.internal("In Development\\repBB.png")));
        landing_pad = new Sprite(new Texture(Gdx.files.internal("In Development\\repOG.png")));
        landing_pad_glow = new Sprite(new Texture(Gdx.files.internal("In Development\\repOG_hover.png")));
        back_btn = new Sprite(new Texture(Gdx.files.internal("In Development\\repARW.png")));
        reset_closed = new Sprite(new Texture(Gdx.files.internal("In Development\\ResetButtonClosed.png")));
        reset_opened = new Sprite(new Texture(Gdx.files.internal("In Development\\ResetButtonOpen.png")));
        reset_pressed = new Sprite(new Texture(Gdx.files.internal("In Development\\ResetButtonDepressed.png")));
        mystery_sprite = new Sprite(new Texture(Gdx.files.internal("In Development\\mystery_sprite.png")));
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

    public static Sprite getHeadSprite() {
        return faces[rng.nextInt(faces.length)];
    }

    public static Sprite getShoulderSprite() {
        return shoulders[rng.nextInt(shoulders.length)];
    }

    public static Sprite getLegSprite() {
        return pants[rng.nextInt(pants.length)];
    }

    public static Sprite getTorsoSprite() {
        return shirts[rng.nextInt(shirts.length)];
    }

    public static Sprite getElbowSprite() {
        return arms[rng.nextInt(arms.length)];
    }


    public static Sprite[] getSuitForLevelNoCheck(int lvl) {
        int index = lvl / 10;

        Sprite[] suit = new Sprite[5];
        //head
        suit[0] = faces[Math.min(index, facecount - 1)];
        //torso
        suit[1] = shirts[Math.min(index, shirtcount - 1)];
        //leg
        suit[2] = pants[Math.min(index, pantcount - 1)];
        //arm
        suit[3] = shoulders[Math.min(index, shouldercount - 1)];
        //elbow
        suit[4] = arms[Math.min(index, armcount - 1)];
        return suit;
    }

    public static Sprite[] getSuitForLevel(int lvl) {
        int index = lvl / 10;

        Sprite[] suit = new Sprite[5];
        //head

        if (debug || unclocks[1][Math.min(index, facecount - 1)] == 1)
            suit[0] = faces[Math.min(index, facecount - 1)];
        else
            suit[0] = mystery_sprite;

        //torso
        if (debug || unclocks[2][Math.min(index, shirtcount - 1)] == 1) {
            suit[1] = shirts[Math.min(index, shirtcount - 1)];
        } else {
            suit[1] = mystery_sprite;
        }
        //leg
        if (debug || unclocks[3][Math.min(index, pantcount - 1)] == 1) {
            suit[2] = pants[Math.min(index, pantcount - 1)];
        } else {
            suit[2] = mystery_sprite;
        }
        //arm
        if (debug || unclocks[4][Math.min(index, shouldercount - 1)] == 1) {
            suit[3] = shoulders[Math.min(index, shouldercount - 1)];
        } else {
            suit[3] = mystery_sprite;
        }

        //elbow
        if (debug || unclocks[5][Math.min(index, armcount - 1)] == 1) {
            suit[4] = arms[Math.min(index, armcount - 1)];
        } else {
            suit[4] = mystery_sprite;
        }

        return suit;
    }

    public static void unlockItem(int type, int index) {
        unclocks[type][index] = 1;
    }

    public static List<Sprite> getWeaponsList() {
        ArrayList<Sprite> las = new ArrayList<Sprite>();
        for (int i = 0; i < weapons_sprites.size(); i++) {
            if (debug || unclocks[0][i] == 1) {
                las.add(weapons_sprites.get(i));
            } else {
                las.add(mystery_sprite);
            }
        }
        return las;
    }
}
