package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Leon I. Verhelst on 11/17/2014.
 */
public class HeadSpriteActor extends Actor {

    Sprite headSprite;


    public HeadSpriteActor(){
        headSprite = new Sprite(Assets.getHeadSprite());

    }

    public Sprite getSprite(){
        return headSprite;
    }

    public void copyHSA(HeadSpriteActor hsa){
        this.headSprite = new Sprite(hsa.getSprite());

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

