package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.Sprite;

/**
 * Created by Orion on 10/23/2014.
 */


public class Weapon {
    private Sprite sprite;
    private int max_damage, min_damage;
    private float life_steal;

    public Weapon( int min_damage,int max_damage, float life_steal, Sprite sprite){
        this.max_damage = max_damage;
        this.min_damage = min_damage;
        this.life_steal = life_steal;
        this.sprite  = new Sprite(sprite);
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
}
