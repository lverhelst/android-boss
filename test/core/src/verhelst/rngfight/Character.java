package verhelst.rngfight;
import com.badlogic.gdx.graphics.g2d.Sprite;

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
    private static final int BASE_HEALTH = 200;
    private int initial_health;
    private int level;


    private String name;
    private Sprite sprite;

    private Weapon equipped_weapon = null;
    private Random rng;

    private int win_streak = 0;
    private int lose_streak = 0;

    public Character(String name, Sprite sprite){
        rng = new Random();
        this.name = name;
        this.health = BASE_HEALTH;
        this.min_dmg = 0;
        this.base_maxdmg = 10;
        this.max_dmg = this.base_maxdmg;
        level = 1;
        if(sprite != null)
            this.sprite = new Sprite(sprite);
    }

    public void setSprite(Sprite sp){
        this.sprite = sp;
    }

    public void applyDamageOrHealth(int dmg_hlth){
        this.health -= dmg_hlth;

    }



    public int attack(Character victim){
      //  int dmgOrHealth = (int)(rng.nextGaussian() * (rng.nextInt(max_dmg +min_dmg)));
        int dmgOrHealth = (int)((rng.nextBoolean() ? -1 : 1) * (rng.nextInt(max_dmg +min_dmg)));

        //Heal Self
        //if(dmgOrHealth < 0) {
        //     this.applyDamageOrHealth(dmgOrHealth);
        //}
        //Damage victim
        //else{
            victim.applyDamageOrHealth(dmgOrHealth);
            //simulate lifesteal
            this.applyDamageOrHealth(-1 * Math.abs((int)(dmgOrHealth * 0.01)));
        //}
        return dmgOrHealth;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health){
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setEquipped_weapon(Weapon weapon) {
        System.out.println("set weapon");
        this.equipped_weapon = weapon;
        this.min_dmg = base_mindmg + weapon.getMin_damage();
        this.max_dmg = base_maxdmg + weapon.getMax_damage();
        this.health = this.BASE_HEALTH * weapon.getHp_multiplier();// * this.level;
    }

    public boolean isWeaponEquipped()
    {
        return this.equipped_weapon != null;
    }

    public String getName() {
        return name;
    }

    public void reset(){
        this.health = (isWeaponEquipped()? equipped_weapon.getHp_multiplier() : 1) * this.BASE_HEALTH;// * level;
        resetWins();
        resetLosses();
    }

    public int getBase_health() {
        return BASE_HEALTH;
    }

    public int getInitial_health() {return BASE_HEALTH * (isWeaponEquipped() ? equipped_weapon.getHp_multiplier() : 1);// * level;
    }

    public Sprite getSprite() {
        return sprite;
    }

    private void updateSprite(){
        sprite.setRegion(getSpriteForHP(health));
    }

    public Sprite getSpriteForHP(int hp){
        if(hp/(BASE_HEALTH * (isWeaponEquipped()? equipped_weapon.getHp_multiplier() : 1)/4) > 10){
            sprite.setRegion(Assets.face_anim.getKeyFrame(rfmain.statetime, true));
        }else {
            sprite.setRegion((hp <= 0 ? Assets.dead_face: Assets.faces[Math.max(Math.min(hp/(BASE_HEALTH * (isWeaponEquipped()? equipped_weapon.getHp_multiplier() : 1)/4), 7), 2)]));
        }
        return sprite;
    }


    public void incrementWins()
    {
        this.win_streak ++;
    }
    public void resetWins(){
        this.win_streak = 0;
    }
    public void incrementLosses(){
        this.lose_streak++;
    }
    public void resetLosses(){
        this.lose_streak = 0;
    }
    public int getWin_streak() {
        return win_streak;
    }
    public int getLose_streak() {
        return lose_streak;
    }
}
