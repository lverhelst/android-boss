package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.util.Random;

import verhelst.Comp.LTable;

/**
 * Created by Orion on 10/23/2014.
 */


public class Weapon extends Actor {

    enum DAMAGETYPE {
        NORMAL,
        FIRE,
        ICE,
        POISON
    }

    enum POSITION {
        LEFT_POSITION,
        RIGHT_POSITION,
        LOOT_POSITION
    }


    private Sprite sprite, displaysprite;
    private Label llmindmg, llmaxdmg, llhrt;
    private String mindmgstring, maxdmgstring, heartstring;

    private DAMAGETYPE extra_type;
    private POSITION posi;
    private int max_damage, min_damage, hp_multiplier;
    private float life_steal;

    public int spriteindex;

    int wdi_initWidth, sprite_initWidth;


    private final static Random rng = new Random();

    private String nm;
    public Skin skin;

    //glowy stuff here
    int glow_type = 0;
    boolean glow;



    public static Weapon generateRandomWeapon(int lvl, POSITION position) {
        int a_roll = rng.nextInt(lvl);
        int b_roll = rng.nextInt((int)(lvl * 1.75)) + 1;

        return new Weapon(Math.min(a_roll, b_roll), Math.max(a_roll, b_roll), DAMAGETYPE.NORMAL, Math.max(rng.nextInt(lvl) / 3, 1), (float) 0.1, lvl, position);

    }

    public static Weapon generateScaledWeapon(int lvl, POSITION position) {
        double average_dmg = Math.pow(lvl, 1.05); //(mindmg + max_damage )/2.0;
        int offset_roll = rng.nextInt((int) average_dmg);//(int)average_dmg - mindmg;
        int min_dmg = (int) Math.min(lvl, average_dmg - offset_roll);
        int max_dmg = (int) Math.min(lvl * 2, average_dmg + offset_roll);

       // System.out.println("A,O,A-O,A+O: " + average_dmg + " " + offset_roll + " " + min_dmg + " " + max_dmg);
        return new Weapon(min_dmg, max_dmg, DAMAGETYPE.NORMAL, Math.max(rng.nextInt(lvl) / 3, Math.max((int)(lvl / (3.75)), 1)), (float) (0.1), lvl, position);
    }

    //Dummy Constructor
    public Weapon() {
        this.setName("DummyWeap" + System.currentTimeMillis());
        this.spriteindex = rng.nextInt(Math.min(5, Assets.weapons_sprites.size()));
        this.sprite = new Sprite(Assets.weapons_sprites.get(spriteindex));
        this.sprite_initWidth = (int) sprite.getWidth();

        this.wdi_initWidth = (int) Assets.weapon_data_icon.getWidth();
        this.posi = POSITION.LOOT_POSITION;
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        llmindmg = new Label(mindmgstring, skin);
        llmaxdmg = new Label(maxdmgstring, skin);
        //lldmg.setText(dmgstring);
        llhrt = new Label(heartstring, skin);
        //llhrt.setText(heartstring);
        root = getTable();
    }

    public Weapon(int mindmg, int maxdmg, DAMAGETYPE dmg_type, int hp_multiplier, float life_steal, int level, POSITION position) {
        this.setName("Weap" + System.currentTimeMillis());
        this.nm = this.getName();
        this.min_damage = mindmg;
        this.max_damage = maxdmg;
        this.extra_type = dmg_type;
        this.hp_multiplier = hp_multiplier;
        this.life_steal = life_steal;
        this.posi = position;
        //Copy the sprite so we aren't dependant on the external sprite
        this.spriteindex = rng.nextInt(Math.min(level + 5, Assets.weapons_sprites.size()));
        this.sprite = new Sprite(Assets.weapons_sprites.get(spriteindex));

        this.sprite_initWidth = (int) sprite.getWidth();
        this.wdi_initWidth = (int) Assets.weapon_data_icon.getWidth();
        this.mindmgstring = "" + min_damage;
        this.maxdmgstring = "" + max_damage;
        this.heartstring = hp_multiplier * 200 + "";
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        llmindmg = new Label(mindmgstring, skin);
        llmindmg.setText(mindmgstring);
        llmaxdmg = new Label(maxdmgstring, skin);
        llmaxdmg.setText(maxdmgstring);
        llhrt = new Label(heartstring, skin);
        llhrt.setText(heartstring);
        root = getTable();
    }

    public void copyWeapon(Weapon to_copy, POSITION posit) {
        this.max_damage = to_copy.getMax_damage();
        this.min_damage = to_copy.getMin_damage();
        this.setName(to_copy.getName() + "1");
        this.extra_type = to_copy.getExtra_type();
        this.hp_multiplier = to_copy.getHp_multiplier();
        this.life_steal = to_copy.life_steal;
        this.posi = posit;
        this.sprite = new Sprite(to_copy.getSprite());
        this.spriteindex = to_copy.spriteindex;


        this.sprite_initWidth = (int) to_copy.sprite_initWidth;

        this.wdi_initWidth = (int) Assets.weapon_data_icon.getWidth();
        this.mindmgstring = "" + min_damage;
        this.maxdmgstring = "" + max_damage;
        this.heartstring = hp_multiplier * 200 + "";
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        llmindmg = new Label(mindmgstring, skin);
        llmindmg.setText(mindmgstring);
        llmaxdmg = new Label(maxdmgstring, skin);
        llmaxdmg.setText(maxdmgstring);
        llhrt = new Label(heartstring, skin);
        llhrt.setText(heartstring);
        root = getTable();

    }

    private void rebuildUI() {
        this.mindmgstring = "" + min_damage;
        this.maxdmgstring = "" + max_damage;
        this.heartstring = hp_multiplier * 200 + "";
        skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        llmindmg = new Label(mindmgstring, skin);
        llmindmg.setText(mindmgstring);
        llmaxdmg = new Label(maxdmgstring, skin);
        llmaxdmg.setText(maxdmgstring);
        llhrt = new Label(heartstring, skin);
        llhrt.setText(heartstring);
        root = getTable();
    }

    public int getMax_damage() {
        return max_damage;
    }

    public int getMin_damage() {
        return min_damage;
    }

    public float getLife_steal() {
        return life_steal;
    }

    public void setSprite(Sprite sprite) {
        this.sprite = new Sprite(sprite);
        this.displaysprite = new Sprite(sprite);
        root = getTable();
    }


    public Sprite getSprite() {
        return this.sprite;
    }

    public int getHp_multiplier() {
        return hp_multiplier;
    }

    public DAMAGETYPE getExtra_type() {
        return extra_type;
    }

    public int dragx = 0, dragy = 0;

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //System.out.println(this.getName());


        Sprite spr = sprite;

        float padding = Assets.bf.getBounds("|||").width + 10;


        spr.setPosition(getX(), getY());

        spr.setSize(getWidth() - padding, getHeight() - padding);

        dragx = (int) getWidth();
        dragy = (int) getHeight();

        Sprite wdi = new Sprite(Assets.weapon_data_icon);

        wdi.setSize(wdi_initWidth * (getWidth() / sprite_initWidth), getHeight());
        float wdi_offset = 0;
        int text_x_offset = 0;
        switch (this.posi) {
            case RIGHT_POSITION:
            case LOOT_POSITION:
                wdi_offset += getWidth();
                text_x_offset += (wdi_offset + wdi.getWidth() + 5);
                break;
            case LEFT_POSITION:
                wdi_offset -= wdi.getWidth();
                text_x_offset *= -1;
                text_x_offset -= (wdi.getWidth() + (int) Assets.wepNumFnt.getBounds(getMin_damage() + "-" + getMax_damage()).width + 5);
                break;
        }


        wdi.setPosition(getX() + wdi_offset, getY());
        wdi.draw(batch);

        //draw min-max damage in upper right corner
        Assets.wepNumFnt.setColor(Color.WHITE);

        //Damage Numbers
        Assets.wepNumFnt.draw(batch, getMin_damage() + "-" + getMax_damage(), getX() + text_x_offset, getY() - 2 + getHeight());
        //Health Mutlipler
        Assets.wepNumFnt.draw(batch, "" + getHp_multiplier(), getX() + text_x_offset, getY() - 2 + getHeight() / 4 * 3);
        //Dmg Type
        Assets.wepNumFnt.draw(batch, getExtra_type().name().charAt(0) + "", getX() + text_x_offset, getY() - 4 + getHeight() / 2);
        //Life Steal
        Assets.wepNumFnt.draw(batch, "" + (int) getLife_steal(), getX() + text_x_offset, getY() - 6 + getHeight() / 4);
        //
        if (posi == POSITION.LOOT_POSITION) {
            int text_y_offset = (int) Assets.wepNumFnt.getBounds("Drag to equip").height;
            Assets.wepNumFnt.draw(batch, "Drag to equip", getX(), getY() + getHeight() + text_y_offset + 10);
        }

        spr.draw(batch);

        //System.out.println("weapon draw");


        //if(root != null) {
        //root.setPosition(getX(), getY());
        //  root.draw(batch, parentAlpha);
        //       System.out.println("Drawing table.");
//        /}

    }

    //TODO: move to a view class
    LTable root;

    public LTable getTable() {
        if (root == null) {
            root = new LTable(0, 0, 100, 100);
            root.setName("WeaponRoot" + System.currentTimeMillis());
        } else {
            root.removeChildren();
        }

        //else
        //    root.clearChildren();

        LTable dataTable = new LTable(0, 0, 100, 100);
        //dataTable.columnDefaults(1).width(32);
        //dataTable.setDebug(true);
        llmindmg.setFontScale((float) (Gdx.graphics.getDensity() * 1.25));
        llmaxdmg.setFontScale((float) (Gdx.graphics.getDensity() * 1.25));

        dataTable.addActor(new Image(Assets.dmgIconSmallTxture), true);//.center().pad(5).fill().expand();
        dataTable.addActor(llmindmg);//.center().pad(5).expand().fill();
        dataTable.addRow();
        dataTable.addActor(new Image(Assets.dmgIconTxture), true);//.center().pad(5).fill().expand();
        dataTable.addActor(llmaxdmg);//.center().pad(5).expand().fill();
        dataTable.addRow();
        llhrt.setFontScale((float) (Gdx.graphics.getDensity() * 1.25));
        dataTable.addActor(new Image(Assets.hrtIconTxture), true);//.center().pad(5).fill().expand();
        dataTable.addActor(llhrt);//.center().pad(5).expand().fill();

        //if(posi != POSITION.RIGHT_POSITION) {

        root.addActor(new WepSpriteActor(), true);//.uniform().expand().fill();
        root.addActor(dataTable);//.uniform().expand().fill();
        // }else{
        //   root.addActor(new WepSpriteActor(), true);//.uniform().expand().fill();
        // root.addActor(dataTable);//.uniform().expand().fill();
        //}
        return root;
    }

    public void setMax_damage(int max_damage) {
        this.max_damage = max_damage;
        rebuildUI();
    }

    public void setMin_damage(int min_damage) {
        this.min_damage = min_damage;
        rebuildUI();
    }

    public void setHp_multiplier(int hp_multiplier) {
        this.hp_multiplier = hp_multiplier;
        rebuildUI();
    }

    public class WepSpriteActor extends Actor {


        public WepSpriteActor() {
            displaysprite = new Sprite(sprite);
            setSize(displaysprite.getWidth(), displaysprite.getHeight());
            displaysprite.setFlip(false, true);
            displaysprite.setRotation(45);

        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            displaysprite.draw(batch);
        }

        @Override
        public void setSize(float width, float height) {
            displaysprite.setSize(width, height);
            displaysprite.setOrigin(width / 2, height / 2);
            super.setSize(width, height);
        }

        @Override
        public void setPosition(float x, float y) {
            displaysprite.setPosition(x, y);

            super.setPosition(x, y);
        }
    }

    @Override
    public void setVisible(boolean visible) {
        if (root != null)
            root.setVisible(visible);
        super.setVisible(visible);
    }


    @Override
    public float getY() {
        if (root != null)
            return root.getY();
        return super.getY();
    }

    @Override
    public float getX() {
        if (root != null)
            return root.getX();
        return super.getX();
    }

    @Override
    public float getWidth() {
        if (root != null)
            return root.getWidth();
        return super.getWidth();
    }

    @Override
    public float getHeight() {
        if (root != null)
            return root.getHeight();
        return super.getHeight();
    }



}
