package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
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

        NinePatch bg = new NinePatch(skinAtlas.findRegion("default-round"), 5, 5, 4, 4);

        bg.setColor(Color.WHITE);
        NinePatch fg = new NinePatch(skinAtlas.findRegion("default-round"), 5, 5, 4, 4);
        fg.setColor(Color.RED);
        background = new NinePatchDrawable(bg);
        foreground = new NinePatchDrawable(fg);


    }

    public void resetHP(){
        this.health_value = initial_value;

    }

    public void setHealth_value(int health_value){
        this.health_value = health_value;

    }

    public void setInitial_value(int initial_value){
        this.initial_value = initial_value;
        this.health_value = initial_value;
        this.health_scale  = initial_value/BASE;

    }

    private void updateNameAndHealth(){
        nameAndHealth.setText(this.name + ": " + this.health_value);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        background.draw(batch, getX(), getY(), BASE * getScaleX(),  getHeight() * getScaleY());
        foreground.draw(batch,getX(), getY(), health_value / health_scale, getHeight() * getScaleY());
    }

    @Override
    public float getHeight() {
        return background.getMinHeight();
    }
}
