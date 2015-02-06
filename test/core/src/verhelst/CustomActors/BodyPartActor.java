package verhelst.CustomActors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

import verhelst.Craft.CraftableType;
import verhelst.Craft.Inventory;
import verhelst.Craft.Item;
import verhelst.Craft.cTOKEN;
import verhelst.rngfight.Assets;

/**
 * Created by Leon I. Verhelst on 11/17/2014.
 */
public class BodyPartActor extends Actor implements Item{

    public enum BodyPartType {
        HEAD,
        TORSO,
        LEGS,
        UPPERARM,
        LOWERARM
    }

    @Override
    public CraftableType getCraftableType() {
        return CraftableType.COMPOUND;
    }

    @Override
    public cTOKEN getCTOKEN() {
        return cTOKEN.valueOf(btype.name());
    }

    @Override
    public Integer getIntegerValue(String attribute) {
        return 0;
    }

    @Override
    public void setIntegerValue(String attribute, Integer value) {
    }

    @Override
    public Actor getActor() {
        return this;
    }

    public String decompose(){
        int itemindex = 1 + rng.nextInt(3);
        int number = rng.nextInt(4) + 1;
        int j = 0;
        for(int i = 0; i < number; i++) {
            if (Inventory.addItem(itemindex))
                j++;
        }
        if(j > 0)
            return cTOKEN.values()[itemindex] + " + " + j;
        else
            return "Max " + cTOKEN.values()[itemindex] + "  Reached";

    }

    Sprite partSprite;
    Sprite drawSprite;
    public int part_index;
    Random rng = new Random();


    BodyPartType btype;

    public static BodyPartActor generateRandomBodyPart(){
        int types = BodyPartActor.BodyPartType.values().length;
        int loooooot = new Random().nextInt(types);
        return new BodyPartActor(BodyPartActor.BodyPartType.values()[loooooot], 200);

    }

    public static BodyPartActor generateBodyPartForType(BodyPartType type){
        return new BodyPartActor(type, 200);
    }


    public BodyPartActor() {

    }


    public BodyPartActor(BodyPartType type, int lvl) {
        partSprite = null;
        lvl = (lvl + 25)/5;
        switch (type) {
            case HEAD:
                part_index = rng.nextInt(Math.min(Assets.faces.length,lvl));
                partSprite = new Sprite(Assets.faces[part_index]);
                break;
            case UPPERARM:
                part_index = rng.nextInt(Math.min(Assets.shoulders.length,lvl));
                partSprite = new Sprite(Assets.shoulders[part_index]);
                break;
            case LOWERARM:
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
            case UPPERARM:
                //drawSprite.rotate90(true);
                break;
            case LOWERARM:
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

