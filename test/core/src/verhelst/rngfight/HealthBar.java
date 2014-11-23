package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;

/**
 * Created by Leon I. Verhelst on 10/30/2014.
 */
public class HealthBar extends Actor {

    private int health_value;
    private int initial_value; //for health scaling
    private final int BASE = 200;
    private int health_scale;
    private String name;

    private NinePatchDrawable background;
    private NinePatchDrawable foreground;

    private Label nameAndHealth;
    private final Character character;


    public HealthBar(Character character){
        this.character = character;
        this.name = character.getName();
        this.health_value = character.getBase_health();


        this.initial_value = character.getInitial_health();
        health_scale = (initial_value/BASE == 0 ? 1 : initial_value/BASE);
        TextureAtlas skinAtlas = new TextureAtlas(Gdx.files.internal("data/uiskin.atlas"));
        float height = Assets.HUDbf.getBounds("100").height;

        NinePatch bg = new NinePatch(skinAtlas.findRegion("default-round"), 5, 5, 4, 4);
        bg.setMiddleHeight(height);
        bg.setColor(Color.DARK_GRAY);

        NinePatch fg = new NinePatch(skinAtlas.findRegion("default-round"), 5, 5, 4, 4);


        fg.setColor(Color.RED);
        fg.setMiddleHeight(height);
        background = new NinePatchDrawable(bg);
        foreground = new NinePatchDrawable(fg);

    }



    public void setHealth_value(int health_value){
        this.health_value = health_value;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {


        //background.draw(batch, getX(), getY(), getWidth(), character.getInitial_health()/character.getBase_health() * getScaleX());
        if(health_value > 0 ) {
            //This gets the health value AS IF it waas the base health
            float hh =  (character.getHealth() / (character.getInitial_health() / character.getBase_health()));
            //We need to scale the base health to getHeight
            float scaler = getHeight()/character.getBase_health();

            //System.out.println(scaler + " bh: " + character.getBase_health() + "  height:" + getHeight());


            foreground.draw(batch, getX(), getY(), getWidth(), hh * scaler);
        }
        Assets.HUDbf.draw(batch, this.name + ": " + ( character.getHealth()  > 0 ?  character.getHealth()  : 0), getX() + getWidth()/4, getY() + Assets.HUDbf.getBounds( character.getName()).height + 1);
    }

    @Override
    public float getHeight() {
        return background.getMinHeight();
    }
}
