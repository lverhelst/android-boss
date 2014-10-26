package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import java.util.ArrayList;
import java.util.List;

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
        Texture wepimg = new Texture("swurd.png");
        wepimg.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        Sprite weptemp = new Sprite(wepimg);
        weapons_sprites.add(weptemp);
        //Weapon Data Icon (For UI)
        weapon_data_icon = new Sprite(new Texture("wepdataicon.png"));



    }
}
