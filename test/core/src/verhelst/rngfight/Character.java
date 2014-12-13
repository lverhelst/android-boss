package verhelst.rngfight;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Random;

import verhelst.bones.Model;

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
    int display_hp;
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
    private boolean glow;
    public int glow_type; // 0 = none, 1 = yellow, 2 = green

    //GetY() wasn't working in the BattleView
    //GetHeight() and getX() both worked...wierd.
    //Use otherY
    public int otherY = 0;

    private Model m;

    enum DmgListSide{
        LEFT,
        RIGHT
    }

    //Damage Numbers during battle
    ArrayList<DamageNumber> dnListA = new ArrayList<DamageNumber>();
    Queue<DamageNumber> availablepool = new ArrayDeque<DamageNumber>();

    //For rendering damage numbers
    Iterator<DamageNumber> i;


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

        if(name.equals("Enemy")) {
            m = new Model((int)(getX() + getWidth()/2), (int)(getY() + getHeight()), true, getHeight());
            m.isFlipped = true;
            m.flip();
        }else
            m = new Model((int)(getX() + getWidth()/2), (int)(getY() + getHeight()), false, getHeight());
        m.hideWeapon();

        for(int i = 0; i < 1000; i++){
            availablepool.add(new DamageNumber(-9999,-100,-100));
        }
    }

    public void setSprite(Sprite sp){
        this.sprite = sp;
    }

    public void applyDamageOrHealth(int dmg_hlth){
        this.health -= dmg_hlth;

    }



    public int attack(Character victim, float lifesteal){
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
            //0.011
            this.applyDamageOrHealth(-1 * Math.abs((int)(dmgOrHealth * lifesteal)));
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


        m.updateSprite("wrist", new Sprite(weapon.getSprite()));
        m.showWeapon();

    }

    public void setBodyPart(BodyPartActor hsa){
        Sprite toequip = new Sprite(hsa.getSprite());
        switch(hsa.getBtype()){
            case HEAD:
                toequip.rotate90(!name.equals("Enemy"));
                m.updateSprite("head", toequip);
                break;
            case SHOULDER:
                m.updateSprite( "shoulder" , toequip);
                break;
            case TORSO:
                m.updateSprite("torso", toequip);
                break;
            case LEGS:
                m.updateSprite("leg", toequip);
                break;
            case ELBOW:
                m.updateSprite("elbow", toequip);
                break;
        }
    }

    public void equipSuit(){
        System.out.println("EQUIP SUUUUUIT *********************");
        Sprite[] sprites = Assets.getSuitForLevel(level);
        Sprite head = new Sprite(sprites[0]);
        head.rotate90(true);
        head.flip(true, true);
        m.isFlipped = true;
        m.updateSprite("head", head);
        m.updateSprite("torso", sprites[1]);
        m.updateSprite("leg", sprites[2]);
        m.updateSprite( "shoulder" , sprites[3]);
        m.updateSprite("elbow", sprites[4]);
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

    public int getDisplay_hp(){
        return  display_hp;
    }

    public void setDisplay_hp(int display_hp){
        this.display_hp = display_hp;
    }


    @Override
    public void draw(Batch batch, float parentAlpha) {
        /*Sprite spr = getSpriteForHP(health);
        otherY = (int)getY();
        spr.setPosition(getX(), getY());
        spr.setSize(getWidth(), getHeight());
        spr.setScale(getScaleX(), getScaleY());
        spr.draw(batch);

        if(BattleScreen.battling)
            this.drawAttack(batch, rotation += (Math.PI * 4));
        else
            this.drawAttack(batch, rotation = 0);

        */


        if(glow){
            if(glow_type == 1)
                batch.draw(Assets.glow_ylw, getX(), getY(), getWidth(), getHeight());
            if(glow_type == 2)
                batch.draw(Assets.glow, getX(), getY(), getWidth(), getHeight());
        }

        otherY = (int)getY();
        m.originx = (int)(getX() + getWidth()/2);
        m.originy = (int)(getY() + getHeight()/2);
        renderDamageNumbers(batch);

        m.render(batch);

    }

    public void setGlow(boolean glow){
        this.glow = glow;
    }

    double rotation = 0;

    private void drawAttack(Batch batch, double rot)        {

            Sprite test = Assets.dead_face;
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
        }




    //Adds a new damage number to the appropriate damage list
    private void addDmgNum(String num, int x, int y, DmgListSide side){
            DamageNumber dn;
            if(!availablepool.isEmpty()) {
                synchronized (availablepool) {
                    dn = availablepool.poll();
                }
                if (isInteger(num)) {
                    dn.setValue(Integer.parseInt(num));

                } else {
                    dn.setCs(num);
                }
                dn.setX(x);
                dn.setY(y);
                synchronized (dnListA) {
                    dnListA.add(dn);
                }
            }
            else {
                if (isInteger(num)) {
                    dn = new DamageNumber(Integer.parseInt(num), x, y);
                } else {
                    dn = new DamageNumber(num, x, y);
                }
            }


    }

    //FROM: http://stackoverflow.com/questions/237159/whats-the-best-way-to-check-to-see-if-a-string-represents-an-integer-in-java (for speed)
    public boolean isInteger(String str) {
        if (str == null) {
            return false;
        }
        int length = str.length();
        if (length == 0) {
            return false;
        }
        int i = 0;
        if (str.charAt(0) == '-') {
            if (length == 1) {
                return false;
            }
            i = 1;
        }
        for (; i < length; i++) {
            char c = str.charAt(i);
            if (c <= '/' || c >= ':') {
                return false;
            }
        }
        return true;
    }

    public void consumeDmgNumPost(String num, DmgListSide side){

        float y = otherY + getHeight()/2;// + (rng.nextBoolean() ? - 1 : 1) * rng.nextInt((int)b.getLeftside().getHeight()/2);
        float x = getX() + getWidth()/2;// + (rng.nextBoolean() ? - 1 : 1) * rng.nextInt((int)b.getRightside().getWidth()/4);
        addDmgNum(num, (int) x, (int) y, side);
    }

    public void renderDamageNumbers(Batch batch){

        //Add Damage Numbers to screen
        synchronized (dnListA) {

            for (i = dnListA.iterator(); i.hasNext(); ) {

                DamageNumber dn = i.next();
                if (dn.isRemoveable()) {
                    synchronized (availablepool) {
                        availablepool.add(dn.reset());
                    }
                    i.remove();
                } else {
                    Assets.dmgNumFnt.setColor(dn.getRed(), dn.getGreen(), dn.getBlue(), dn.getAlpha());
                    Assets.dmgNumFnt.draw(batch, dn.getCs(), dn.getX(), dn.getY());
                    dn.update();
                }
            }

            //    System.out.print("dnlistsize: " + dnListA.size());
             //   System.out.println("    availablepool: " + availablepool.size());


        }

    }

    @Override
    public void setScale(float scaleXY) {
        m.modifyScale(scaleXY);
        super.setScale(scaleXY);
    }
}
