package verhelst.CustomActors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

import verhelst.rngfight.Assets;

/**
 * Created by Leon I. Verhelst on 11/17/2014.
 */
public class BodyPartActor extends Actor {

    public enum BodyPartType {
        HEAD,
        TORSO,
        LEGS,
        SHOULDER,
        ELBOW

    }

    Sprite partSprite;
    Sprite drawSprite;
    public int part_index;
    Random rng = new Random();


    BodyPartType btype;

    public BodyPartActor() {

    }


    public BodyPartActor(BodyPartType type, int lvl) {
        partSprite = null;
        lvl = (lvl + 15)/5;
        switch (type) {
            case HEAD:
                part_index = rng.nextInt(Math.min(Assets.faces.length,lvl));
                partSprite = new Sprite(Assets.faces[part_index]);
                break;
            case SHOULDER:
                part_index = rng.nextInt(Math.min(Assets.shoulders.length,lvl));
                partSprite = new Sprite(Assets.shoulders[part_index]);
                break;
            case ELBOW:
                part_index = rng.nextInt(Math.min(Assets.arms.length,lvl));
                partSprite = new Sprite(Assets.arms[part_index]);
                break;
            case TORSO:
                part_index = rng.nextInt(Math.min(Assets.shirts.length,lvl));
                partSprite = new Sprite(Assets.shirts[part_index]);
                break;
            case LEGS:
                part_index = rng.nextInt(Math.min(Assets.pants.length, lvl));
                partSprite = new Sprite(Assets.pants[part_index]);
                break;
        }
        drawSprite = new Sprite(partSprite);
        setName("HSA" + btype + System.currentTimeMillis());
        this.btype = type;
        System.out.println("lvl " + lvl + " " + part_index) ;

        updateSize();

    }

    public Sprite getSprite() {
        return partSprite;
    }


    public void copyHSA(BodyPartActor hsa) {
        this.partSprite = new Sprite(hsa.getSprite());
        this.drawSprite = new Sprite(partSprite);
        setName(hsa.getName());
        setBtype(hsa.getBtype());
        this.part_index = hsa.part_index;
        updateSize();
    }

    public void updateSize() {
        switch (btype) {
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
        drawSprite.setPosition(x, y);
        super.setPosition(x, y);
    }
}

