package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;

import java.util.Random;

/**
 * Created by Orion on 10/23/2014.
 */


public class Weapon {

    enum DAMAGETYPE{
        NORMAL,
        FIRE,
        ICE,
        POISON
    }


    private Sprite sprite;

    private DAMAGETYPE extra_type;
    private int max_damage, min_damage, hp_multiplier;
    private float life_steal;

    private final static Random rng = new Random();

    public static Weapon generateRandomWeapon(int lvl, Sprite tempSprite)
    {
        int a_roll = (int)rng.nextInt(lvl);
        int b_roll = rng.nextInt(lvl * 2) + 1;
        return new Weapon(Math.min(a_roll, b_roll), Math.max(a_roll,b_roll), DAMAGETYPE.NORMAL, Math.max(rng.nextInt(lvl)/3,1),(float)0.1,tempSprite);

    }


    public Weapon( int min_damage,int max_damage, float life_steal, Sprite sprite){
        this.max_damage = max_damage;
        this.min_damage = min_damage;
        this.life_steal = life_steal;
        this.sprite  = new Sprite(sprite);
    }

    public Weapon(int mindmg, int maxdmg, DAMAGETYPE dmg_type, int hp_multiplier, float life_steal, Sprite sprite){
        this.min_damage = mindmg;
        this.max_damage = maxdmg;
        this.extra_type = dmg_type;
        this.hp_multiplier = hp_multiplier;
        this.life_steal = life_steal;
        //Copy the sprite so we aren't dependant on the external sprite
        this.sprite = new Sprite(sprite);

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
}
