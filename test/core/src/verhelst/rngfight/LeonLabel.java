package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

/**
 * Created by Orion on 11/4/2014.
 */
public class LeonLabel extends Widget {

    String text;
    Skin skin;


    public LeonLabel(String text, Skin skin) {
        this.text = text;
        this.skin = skin;
    }

    public void setText(String newText) {
        this.text = newText;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        Assets.bf.draw(batch, text, getX() - Assets.bf.getBounds(text).width, getY());
    }
}
