package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
    public static Texture dmgIconTxture, hrtIconTxture, glow;
    public static Sprite dmgIcon, hrtIcon;

    private static int armcount, pantcount, shirtcount, shouldercount, facecount;
    private static Random rng = new Random();

    public Assets(){
        weapons_sprites = new ArrayList<Sprite>();
        facecount = 11;
        armcount = 3;
        pantcount = 3;
        shirtcount = 3;
        shouldercount = 3;

        faces = new Sprite[facecount];
        pants = new Sprite[pantcount];
        shirts = new Sprite[shirtcount];
        arms = new Sprite[armcount];
        shoulders = new Sprite[shouldercount];


        loadAssets();
    }
    //Load graphical assets
    public void loadAssets(){

        //Load character images & animation
        TextureAtlas ta = new TextureAtlas(Gdx.files.internal("face_sprites\\sprite.pack"));
        //TextureRegion[] hairframes = {new TextureRegion(ta.findRegion("face11")),new TextureRegion(ta.findRegion("face12")),new TextureRegion(ta.findRegion("face13")),new TextureRegion(ta.findRegion("face14"))};
        //face_anim = new Animation(0.075f, hairframes);
        resting_face = new Sprite(ta.findRegion("face1"));
        dead_face = new Sprite(ta.findRegion("face1"));
        //faces
        for(int i = 1; i <= facecount; i++){
            faces[i - 1] = new Sprite(ta.findRegion("face" + i));
        }
        //torsos
        for(int i = 1; i <= shirtcount; i++){
            shirts[i - 1] = new Sprite(ta.findRegion("torso" + i));
        }
        //legs
        for(int i = 1; i <= pantcount; i++){
            pants[i - 1] = new Sprite(ta.findRegion("legs" + i));
        }
        //shoulders
        for(int i = 1; i <= armcount; i++){
            arms[i - 1] = new Sprite(ta.findRegion("elbow" + i));
        }
        //elbows
        for(int i = 1; i <= shouldercount; i++){
            shoulders[i - 1] = new Sprite(ta.findRegion("shoulder" + i));
        }


        //Load Weapon Sprites
        //TODO: Make more weapon sprites
        Texture wepspritepack = new Texture(Gdx.files.internal("In Development\\Weapon_Sprites_PaintNet.png"));
        TextureRegion wep_temp = new TextureRegion(wepspritepack);
        TextureRegion[][] wep_sprites = wep_temp.split(33,34);
        int x_offset = 1;
        int y_offset = 1;
        int width = 32;
        int height = 32;
        for(int i = 0; i < 2; i++){
            for(int j = 0; j < 6; j++) {
                if(i == 1 && j > 1)
                    break;
                weapons_sprites.add(new Sprite(wep_sprites[i][j]));
            }
        }

        weapon_data_icon = new Sprite(new Texture(Gdx.files.internal("wepdataicon.png")));

        dmgIcon = new Sprite(new Texture(Gdx.files.internal("In Development\\DmgIcon.png")));
        dmgIconTxture = new Texture(Gdx.files.internal("In Development\\DmgIcon.png"));
        hrtIcon = new Sprite(new Texture(Gdx.files.internal("In Development\\HrtIcon.png")));
        hrtIconTxture = new Texture(Gdx.files.internal("In Development\\HrtIcon.png"));

        glow = new Texture(Gdx.files.internal("In Development\\glow.png"));

        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("game_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(32 * Gdx.graphics.getDensity() );
        bf = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(28 * Gdx.graphics.getDensity());
        wepNumFnt = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(12 * Gdx.graphics.getDensity());
        dmgNumFnt = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(24 * Gdx.graphics.getDensity());
        HUDbf = ftfg.generateFont(ftfp);
    }

    public static Sprite getWeaponSprite(){
        return weapons_sprites.get(rng.nextInt(weapons_sprites.size()));
    }

    public static Sprite findSpriteForName(String name){
        if(name.equals("head"))
            return resting_face;
        if(name.equals("shoulder"))
            return shoulders[0];
        if(name.equals("leg"))
            return  pants[0];
        if(name.equals("torso"))
            return shirts[0];
        if(name.equals("wrist"))
            return getWeaponSprite();
        return arms[0];
    }

    public static Sprite getHeadSprite(){
        return faces[rng.nextInt(faces.length)];
    }

    public static Sprite getShoulderSprite(){
        return shoulders[rng.nextInt(shoulders.length)];
    }

    public static Sprite getLegSprite(){
        return pants[rng.nextInt(pants.length)];
    }

    public static Sprite getTorsoSprite(){
        return shirts[rng.nextInt(shirts.length)];
    }

    public static Sprite getElbowSprite(){
        return arms[rng.nextInt(arms.length)];
    }

    public static Sprite[] getSuitForLevel(int lvl){
        int index = lvl/10;

        Sprite[] suit = new Sprite[5];
        //head
        suit[0] = faces[Math.min(index, facecount - 1)];
        //torso
        suit[1] = shirts[Math.min(index, shirtcount - 1)];
        //leg
        suit[2] = pants[Math.min(index, pantcount -1 )];
        //arm
        suit[3] = shoulders[Math.min(index, shouldercount - 1)];
        //elbow
        suit[4] = arms[Math.min(index, armcount -1 )];
        return suit;
    }
}
