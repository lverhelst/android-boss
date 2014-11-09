package verhelst.rngfight;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.Random;

/**
 * Created by Orion on 10/16/2014.
 */
public class Character extends Actor {
    private int health;

    private final int base_mindmg =0;
    private final int base_maxdmg;

    private int min_dmg;
    private int max_dmg;
    int max_level, max_wtnl;
    private static final int BASE_HEALTH = 200;
    private int initial_health;
    private int level;

    private String name;
    private Sprite sprite;

    private Weapon equipped_weapon = null;
    private Random rng;


    int wins_to_level = 2;
    private int win_streak = 0;
    private int lose_streak = 0;

    //GetY() wasn't working in the BattleView
    //GetHeight() and getX() both worked...wierd.
    //Use otherY
    public int otherY = 0;

    public Character(String name, Sprite sprite){
        rng = new Random();
        this.name = name;
        this.health = BASE_HEALTH;
        this.min_dmg = 0;
        this.base_maxdmg = 10;
        this.max_dmg = this.base_maxdmg;
        level = 1;
        max_level = 1;
        max_wtnl = 0;
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
        //System.out.println("Weapon equipped " + name);
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
            sprite.setRegion(Assets.face_anim.getKeyFrame(BattleScreen.statetime, true));
        }else {
            sprite.setRegion((hp <= 0 ? Assets.dead_face: Assets.faces[Math.max(Math.min(hp/(BASE_HEALTH * (isWeaponEquipped()? equipped_weapon.getHp_multiplier() : 1)/4), 7), 2)]));
        }
        return sprite;
    }


    public void incrementWins()
    {
        this.win_streak++;
        if(win_streak % (wins_to_level + 2) == wins_to_level + 1){
            level--;
            level = Math.max(1, level);
        }

    }
    public void resetWins(){
        this.win_streak = 0;
    }
    public void incrementLosses()
    {
        this.lose_streak++;
        if(lose_streak % wins_to_level == 0){
            level++;
            System.out.println(name + " lvl up");
            if(level > max_level)
                max_wtnl = 0;
            max_level = Math.max(max_level, level);
        }
        if(level == max_level){
            max_wtnl = Math.max(max_wtnl, lose_streak % wins_to_level);
        }
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


    @Override
    public void draw(Batch batch, float parentAlpha) {
        Sprite spr = getSpriteForHP(health);
        otherY = (int)getY();
        spr.setPosition(getX(), getY());
        spr.setSize(getWidth(), getHeight());
        spr.setScale(getScaleX(), getScaleY());
        spr.draw(batch);

        if(BattleScreen.battling)
            this.drawAttack(batch, rotation += (Math.PI * 4));
        else
            this.drawAttack(batch, rotation = 0);




    }

    double rotation = 0;

    private void drawAttack(Batch batch, double rot){

        Sprite test = Assets.arm;
        test.setSize(100,10);

        int joint1x = (int)(getX() + getWidth()/2);
        int joint1y = (int)(getY());
        int joint1rotation = 180 + (int)(Math.sin(Math.toRadians(rot)) * 20);

        test.setPosition(joint1x,joint1y);
        test.setRotation(joint1rotation);
        int joint2x = (int)(joint1x + Math.cos(Math.toRadians(joint1rotation)) * test.getWidth());
        int joint2y = (int)(joint1y + Math.sin(Math.toRadians(joint1rotation)) * test.getWidth());

        if(isWeaponEquipped()) {

            int joint2rotation = 270 + joint1rotation;

            Sprite test2 = new Sprite(this.getEquipped_weapon().getSprite());
            test2.setSize(32,32);
          //  test2.setScale((float)0.5, (float)0.5);
          //  test2.setOrigin(test.getWidth()/4, test.getHeight()/2);
            test2.setPosition(joint2x + test2.getWidth()/2, joint2y);
            test2.setRotation(270 + joint2rotation);
            test2.draw(batch);
        }
         test.draw(batch);



        batch.begin();

    }
 }
