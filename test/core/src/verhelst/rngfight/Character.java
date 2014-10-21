package verhelst.rngfight;
import java.util.Random;

/**
 * Created by Orion on 10/16/2014.
 */
public class Character {
    private int health;
    private int min_dmg;
    private int max_dmg;
    private String name;


    public Character(String name, int initial_health){
        this.name = name;
        this.setHealth(initial_health);
        this.min_dmg = 0;
        this.max_dmg = (int)(initial_health * 0.1);
    }

    public void applyDamageOrHealth(int dmg_hlth){
        this.health += dmg_hlth;
    }

    public int getHealth() {
        return health;
    }

    private void setHealth(int health){
        this.health = health;
    }

    public int getMin_dmg() {
        return min_dmg;
    }

    public int getMax_dmg() {
        return max_dmg;
    }
}
