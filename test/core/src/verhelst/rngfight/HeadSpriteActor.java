package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Leon I. Verhelst on 11/17/2014.
 */
public class HeadSpriteActor extends Actor implements Loot {

    Sprite headSprite;
    Actor actor;

    public HeadSpriteActor(){
        headSprite = new Sprite(Assets.getHeadSprite());
        actor = this;
    }

    public Sprite getSprite(){
        return headSprite;
    }

    public void copyHSA(HeadSpriteActor hsa){
        this.headSprite = new Sprite(hsa.getSprite());
        actor = this;
    }

    @Override
    public Actor getActor() {
        return actor;
    }

    @Override
    public void setActor(Actor actor) {
        this.actor = actor;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //super.draw(batch, parentAlpha);
        //System.out.println("Drawing " + this.getName());
        headSprite.setPosition(getX(), getY());
        headSprite.setSize(getWidth(), getHeight());
        headSprite.draw(batch);
    }
}

