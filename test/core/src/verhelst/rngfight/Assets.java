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
    public static Sprite[] faces;
    public static BitmapFont wepNumFnt;
    public static BitmapFont dmgNumFnt;
    public static BitmapFont bf;

    public Assets(){
        weapons_sprites = new ArrayList<Sprite>();
        faces = new Sprite[8];
        loadAssets();
    }
    //Load graphical assets
    public void loadAssets(){

        //Load character images & animation
        TextureAtlas ta = new TextureAtlas(Gdx.files.internal("face_sprites\\sprite.pack"));
        TextureRegion[] hairframes = {new TextureRegion(ta.findRegion("face11")),new TextureRegion(ta.findRegion("face12")),new TextureRegion(ta.findRegion("face13")),new TextureRegion(ta.findRegion("face14"))};
        face_anim = new Animation(0.075f, hairframes);
        resting_face = new Sprite(ta.findRegion("face4"));
        dead_face = new Sprite(ta.findRegion("face1"));
        for(int i = 2; i <= 9; i++){
            faces[i-2] = new Sprite(ta.findRegion("face" + i));
        }

        //Load Weapon Sprites
        //TODO: Make more weapon sprites, loop over sprite sheet
        Texture wepspritepack = new Texture("In Development\\Weapon_Sprites_PaintNet.png");
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

        Texture wepimg = new Texture("swurd.png");
        wepimg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Sprite weptemp = new Sprite(wepimg);
        weapons_sprites.add(weptemp);
        //Weapon Data Icon (For UI)
        weapon_data_icon = new Sprite(new Texture("wepdataicon.png"));

        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("game_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(35 * Gdx.graphics.getDensity() );
        bf = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(35 * Gdx.graphics.getDensity());
        wepNumFnt = ftfg.generateFont(ftfp);

        ftfp.size = Math.round(28 * Gdx.graphics.getDensity());
        dmgNumFnt = ftfg.generateFont(ftfp);
    }

    public static Sprite getWeaponSprite(){
        Random rng = new Random();
        return weapons_sprites.get(rng.nextInt(weapons_sprites.size()));
    }
}