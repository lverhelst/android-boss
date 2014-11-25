package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Leon I. Verhelst on 11/17/2014.
 */
public class BodyPartActor extends Actor {

    public enum BodyPartType {
        HEAD,
        SHOULDER,
        ELBOW,
        TORSO,
        LEGS
    }

    Sprite partSprite;
    Sprite drawSprite;



    BodyPartType btype;

    public BodyPartActor(){

    }


    public BodyPartActor(BodyPartType type){
        partSprite = null;
        switch (type){
            case HEAD:
                partSprite = new Sprite(Assets.getHeadSprite());
                break;
            case SHOULDER:
                partSprite = new Sprite(Assets.getShoulderSprite());
                break;
            case ELBOW:
                partSprite = new Sprite(Assets.getElbowSprite());
                break;
            case TORSO:
                partSprite = new Sprite(Assets.getTorsoSprite());
                break;
            case LEGS:
                partSprite = new Sprite(Assets.getLegSprite());
                break;
        }
        drawSprite = new Sprite(partSprite);
        setName("HSA" + btype + System.currentTimeMillis());
        this.btype = type;
        updateSize();

    }

    public Sprite getSprite(){
        return partSprite;
    }



    public void copyHSA(BodyPartActor hsa){
        this.partSprite = new Sprite(hsa.getSprite());
        this.drawSprite = new Sprite(partSprite);
        setName(hsa.getName());
        setBtype(hsa.getBtype());
         updateSize();
    }

    public void updateSize(){
        switch (btype){
            case HEAD:
                //   partSprite.rotate90(true);
                break;
            case SHOULDER:
                //drawSprite.rotate90(true);
                break;
            case ELBOW:
                drawSprite.rotate90(true);
                drawSprite.rotate90(true);
                break;
            case TORSO:
                drawSprite.rotate90(true);
                break;
            case LEGS:
                drawSprite.rotate90(true);
                break;
        }
        this.setSize(partSprite.getWidth(), partSprite.getHeight());
    }

    public BodyPartType getBtype() {
        return btype;
    }

    public void setBtype(BodyPartType btype) {
        this.btype = btype;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        drawSprite.draw(batch);
    }


    @Override
    public void setSize(float width, float height) {
        drawSprite.setSize(width, height);
        super.setSize(width, height);
    }

    @Override
    public void setPosition(float x, float y) {
        drawSprite.setPosition(x,y);
        super.setPosition(x, y);
    }
}

