package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Leon I. Verhelst on 12/11/2014.
 */
public class SpriteActor extends Actor {
    Sprite displaysprite;

    public SpriteActor(Sprite sprite){
        displaysprite = new Sprite(sprite);
        setSize(displaysprite.getWidth(),displaysprite.getHeight());
    }

    @Override
    public void draw(Batch batch, float parentAlpha){
        displaysprite.draw(batch);
    }

    @Override
    public void setSize(float width, float height) {
        displaysprite.setSize(width, height);
        displaysprite.setOrigin(width / 2, height/ 2);
        super.setSize(width, height);
    }

    @Override
    public void setPosition(float x, float y) {
        displaysprite.setPosition(x, y);

        super.setPosition(x, y);
    }
}
