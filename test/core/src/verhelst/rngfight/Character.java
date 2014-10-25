package verhelst.rngfight;
import java.util.Random;

/**
 * Created by Orion on 10/16/2014.
 */
public class Character {
    private int health;

    private final int base_mindmg =0;
    private final int base_maxdmg;

    private int min_dmg;
    private int max_dmg;
    private int base_health;

    private String name;


    private Weapon equipped_weapon = null;
    private Random rng;


    public Character(String name, int initial_health){
        rng = new Random();
        this.name = name;
        this.base_health = initial_health;
        this.health = initial_health;
        this.min_dmg = 0;
        this.base_maxdmg = 10;
        this.max_dmg = this.base_maxdmg;
    }

    public void applyDamageOrHealth(int dmg_hlth){
        this.health -= dmg_hlth;
    }

    public int attack(Character victim){
        int dmgOrHealth = (rng.nextInt(2) == 0 ? -1 : 1) * rng.nextInt(max_dmg) +  min_dmg;
        victim.applyDamageOrHealth(dmgOrHealth);
        return dmgOrHealth;
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

    public Weapon getEquipped_weapon() {
        return equipped_weapon;
    }

    public void setEquipped_weapon(Weapon weapon) {
        this.equipped_weapon = weapon;
        this.min_dmg = base_mindmg + weapon.getMin_damage();
        this.max_dmg = base_maxdmg + weapon.getMax_damage();
        this.base_health *= weapon.getHp_multiplier();
    }

    public boolean isWeaponEquipped()
    {
        return this.equipped_weapon != null;
    }

    public String getName() {
        return name;
    }

    public void reset(){
        this.health = base_health;

    }

    public int getBase_health() {
        return base_health;
    }
}
