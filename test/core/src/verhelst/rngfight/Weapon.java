package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import java.util.Random;

/**
 * Created by Orion on 10/23/2014.
 */


public class Weapon extends Actor {

    enum DAMAGETYPE{
        NORMAL,
        FIRE,
        ICE,
        POISON
    }

    enum POSITION{
        LEFT_POSITION,
        RIGHT_POSITION,
        LOOT_POSITION
    }


    private Sprite sprite;
    private Label lldmg, llhrt;
    private String dmgstring, heartstring;

    private DAMAGETYPE extra_type;
    private POSITION posi;
    private int max_damage, min_damage, hp_multiplier;
    private float life_steal;

    int wdi_initWidth, sprite_initWidth;


    private final static Random rng = new Random();

    public static Weapon generateRandomWeapon(int lvl, Sprite tempSprite, POSITION position)
    {
        int a_roll = rng.nextInt(lvl);
        int b_roll = rng.nextInt(lvl * 2) + 1;

        return new Weapon(Math.min(a_roll, b_roll), Math.max(a_roll,b_roll), DAMAGETYPE.NORMAL, Math.max(rng.nextInt(lvl)/3,1),(float)0.1,tempSprite, position);

    }

    public static Weapon generateScaledWeapon(int lvl, Sprite tempSprite, POSITION position)
    {
        double average_dmg = Math.pow(lvl,1.11); //(mindmg + max_damage )/2.0;
        int offset_roll = rng.nextInt((int)average_dmg);//(int)average_dmg - mindmg;
        int min_dmg = (int)Math.min(lvl, average_dmg - offset_roll);
        int max_dmg = (int)Math.min(lvl * 2, average_dmg + offset_roll);

        System.out.println("A,O,A-O,A+O: " + average_dmg + " " + offset_roll + " " +  min_dmg + " " +  max_dmg);


        return new Weapon(min_dmg, max_dmg, DAMAGETYPE.NORMAL, Math.max(rng.nextInt(lvl)/3, Math.max(lvl/5,1)), (float)(0.1), tempSprite, position);
    }

    //Dummy Constructor
    public Weapon(){
        this.setName("DummyWeap" + System.currentTimeMillis());
        this.sprite = Assets.getWeaponSprite();
        this.sprite_initWidth = (int)sprite.getWidth();
        this.wdi_initWidth = (int)Assets.weapon_data_icon.getWidth();
        this.posi = POSITION.LOOT_POSITION;
    }



    public Weapon( int min_damage,int max_damage, float life_steal, Sprite sprite, POSITION position){
        this.setName("Weap" + System.currentTimeMillis());
        this.max_damage = max_damage;
        this.min_damage = min_damage;
        this.life_steal = life_steal;
        this.sprite  = new Sprite(sprite);
        this.sprite_initWidth = (int)sprite.getWidth();
        this.wdi_initWidth = (int)Assets.weapon_data_icon.getWidth();
        this.posi = position;
    }

    public Weapon(int mindmg, int maxdmg, DAMAGETYPE dmg_type, int hp_multiplier, float life_steal, Sprite sprite, POSITION position){
        this.setName("Weap" + System.currentTimeMillis());
        this.min_damage = mindmg;
        this.max_damage = maxdmg;
        this.extra_type = dmg_type;
        this.hp_multiplier = hp_multiplier;
        this.life_steal = life_steal;
        this.posi = position;
        //Copy the sprite so we aren't dependant on the external sprite
        this.sprite = new Sprite(sprite);
        this.sprite_initWidth = (int)sprite.getWidth();
        this.wdi_initWidth = (int)Assets.weapon_data_icon.getWidth();
        this.dmgstring = min_damage + "-" + max_damage;
        this.heartstring = hp_multiplier +"";
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));
        lldmg = new Label(dmgstring, skin);
        lldmg.setText(dmgstring);
        llhrt = new Label(heartstring, skin);
        llhrt.setText(heartstring);
    }

    public void copyWeapon(Weapon to_copy, POSITION posit){
        this.max_damage = to_copy.getMax_damage();
        this.min_damage = to_copy.getMin_damage();
        this.setName(to_copy.getName() + "1");
        this.extra_type = to_copy.getExtra_type();
        this.hp_multiplier = to_copy.getHp_multiplier();
        this.life_steal = to_copy.life_steal;
        this.posi = posit;
        this.sprite = new Sprite(to_copy.getSprite());
        this.sprite_initWidth = (int)to_copy.sprite_initWidth;
        this.wdi_initWidth = (int)Assets.weapon_data_icon.getWidth();
        this.dmgstring = min_damage + "-" + max_damage;
        this.heartstring = hp_multiplier +"";

        lldmg.setText(dmgstring);

        llhrt.setText(heartstring);

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

    public Sprite getSprite(){
        return this.sprite;
    }

    public int getHp_multiplier() {
        return hp_multiplier;
    }

    public DAMAGETYPE getExtra_type() {
        return extra_type;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
         //System.out.println(this.getName());


        Sprite spr = sprite;

        float padding = Assets.bf.getBounds("|||").width + 10;


        spr.setPosition(getX(), getY());
        spr.setSize(getWidth() - padding, getHeight() - padding);

        Sprite wdi = new Sprite(Assets.weapon_data_icon);

        wdi.setSize( wdi_initWidth * (getWidth()/sprite_initWidth), getHeight());
        float wdi_offset = 0;
        int text_x_offset = 0;
        switch(this.posi){
            case RIGHT_POSITION:
            case LOOT_POSITION: wdi_offset += getWidth();
                                text_x_offset += (wdi_offset + wdi.getWidth() + 5);
                 break;
            case LEFT_POSITION:
                    wdi_offset -= wdi.getWidth();
                    text_x_offset *= -1;
                    text_x_offset -= (wdi.getWidth() + (int)Assets.wepNumFnt.getBounds(getMin_damage() + "-" + getMax_damage()).width + 5);
                break;
        }


        wdi.setPosition(getX() + wdi_offset, getY());
        wdi.draw(batch);

        //draw min-max damage in upper right corner
        Assets.wepNumFnt.setColor(Color.WHITE);

        //Damage Numbers
        Assets.wepNumFnt.draw(batch, getMin_damage() + "-" + getMax_damage(), getX() + text_x_offset, getY() - 2 + getHeight());
        //Health Mutlipler
        Assets.wepNumFnt.draw(batch, "" + getHp_multiplier(),getX() + text_x_offset,getY() - 2 + getHeight()/4*3);
        //Dmg Type
        Assets.wepNumFnt.draw(batch, getExtra_type().name().charAt(0) + "", getX() + text_x_offset,getY() - 4 + getHeight()/2);
        //Life Steal
        Assets.wepNumFnt.draw(batch, "" + (int)getLife_steal(), getX() + text_x_offset, getY() - 6 + getHeight()/4);
        //
        if(posi == POSITION.LOOT_POSITION) {
            int text_y_offset = (int) Assets.wepNumFnt.getBounds("Drag to equip").height;
            Assets.wepNumFnt.draw(batch, "Drag to equip", getX(), getY() + getHeight() + text_y_offset + 10);
        }

        spr.draw(batch);
    }


    //TODO: move to a view class
    Table root;
    public Table getTable(Skin skin){
        root = new Table();

        root.setDebug(true);
        Table dataTable = new Table(skin);
        dataTable.columnDefaults(1).width(32);
        dataTable.setDebug(false);

        dataTable.add(lldmg).center().pad(5);
        dataTable.add(new WDIActor(1)).center().pad(5).fill().expand();
        dataTable.row();

        dataTable.add(llhrt).center().pad(5);
        dataTable.add(new WDIActor(2)).center().pad(5).fill().expand();
        if(posi != POSITION.RIGHT_POSITION) {
            root.add(dataTable).uniform();
            root.add(new WepSpriteActor()).uniform().fill();
        }else{
            root.add(new WepSpriteActor()).uniform().fill();
            root.add(dataTable).uniform();
        }
        return root;
    }

    private class WDIActor extends Actor{
        Sprite wdi;

        public WDIActor(int icon){
            if(icon == 1)
                wdi = new Sprite(Assets.dmgIcon);
            else if(icon == 2)
                wdi = new Sprite(Assets.hrtIcon);
            else
                wdi = new Sprite(Assets.weapon_data_icon);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            wdi.setSize(getWidth(), getHeight());
            wdi.setPosition(getX(), getY());

            wdi.draw(batch);
        }
    }

    private class WepSpriteActor extends Actor{

        @Override
        public void draw(Batch batch, float parentAlpha){
            sprite.setSize(getWidth(), getHeight());
            sprite.setPosition(getX(), getY());
            sprite.draw(batch);
        }

    }

    @Override
    public void setVisible(boolean visible) {
        if(root != null)
            root.setVisible(visible);
        super.setVisible(visible);
    }


}
