package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Leon I. Verhelst on 11/17/2014.
 */
public class LootActor extends Actor {
    Actor act;

    public LootActor(Actor loot){
        this.act = loot;
    }

    public Actor getActor(){
        return act;
    }

    public void setActor(Actor loot){
        this.act = loot;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        if(act != null) {
            act.setPosition(getX(), getY());
            act.setSize(getWidth(), getHeight());
            act.setScale(getScaleX(), getScaleY());
            act.draw(batch, parentAlpha);
        }
    }
}
