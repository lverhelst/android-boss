package verhelst.CustomActors;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Queue;
import java.util.Random;

import verhelst.bones.Model;
import verhelst.rngfight.Assets;
import verhelst.rngfight.BattleScreen;
import verhelst.rngfight.Weapon;

/**
 * Created by Orion on 10/16/2014.
 */
public class Character extends Actor {
    private static final int BASE_HEALTH = 200;
    private final int base_mindmg = 0;
    private final int base_maxdmg;
    public int glow_type; // 0 = none, 1 = yellow, 2 = green
    //GetY() wasn't working in the BattleView
    //GetHeight() and getX() both worked...wierd.
    //Use otherY
    public int otherY = 0;
    int max_level, max_wtnl;
    int display_hp;
    int wins_to_level = 2;
    int win_streak = 0;
    int lose_streak = 0;
    //Damage Numbers during battle
    ArrayList<DamageNumber> dnListA = new ArrayList<DamageNumber>();
    Queue<DamageNumber> availablepool = new ArrayDeque<DamageNumber>();
    //For rendering damage numbers
    Iterator<DamageNumber> i;
    int[] spriteindices = new int[5];
    double rotation = 0;
    private int health;
    private int min_dmg;
    private int max_dmg;
    private int initial_health;
    private int level;
    private String name;
    private Sprite sprite;
    private Weapon equipped_weapon = null;
    private Random rng;
    private boolean glow;
    private Model m;


    public Character(String name) {
        rng = new Random();
        this.name = name;
        this.health = BASE_HEALTH;
        this.min_dmg = 0;
        this.base_maxdmg = 10;
        this.max_dmg = this.base_maxdmg;
        level = 1;
        max_level = 1;
        max_wtnl = 0;
        this.sprite = new Sprite(Assets.resting_face);

        if (name.equals("Enemy")) {
            m = new Model((int) (getX() + getWidth() / 2), (int) (getY() + getHeight()), true, getHeight());
            m.isFlipped = true;
            m.flip();
        } else
            m = new Model((int) (getX() + getWidth() / 2), (int) (getY() + getHeight()), false, getHeight());
            m.hideWeapon();

        for (int i = 0; i < 1000; i++) {
            availablepool.add(new DamageNumber(-9999, -100, -100));
        }
    }


    public void applyDamageOrHealth(int dmg_hlth) {
        this.health -= dmg_hlth;

    }


    public int attack(Character victim, float healing_scaler) {
        //  int dmgOrHealth = (int)(rng.nextGaussian() * (rng.nextInt(max_dmg +min_dmg)));
        int dmgOrHealth = (int) ((rng.nextBoolean() ? -healing_scaler : 1) * (rng.nextInt(max_dmg + min_dmg)));

        //Heal Self
        //if(dmgOrHealth < 0) {
        //     this.applyDamageOrHealth(dmgOrHealth);
        //}
        //Damage victim
        //else{
        victim.applyDamageOrHealth(dmgOrHealth);
        //simulate lifesteal
        //0.011
        this.applyDamageOrHealth(-1 * (int) ((Math.abs(dmgOrHealth)) * 0.011));
        //}
        return dmgOrHealth;
    }


    public void equipSuit() {
        Sprite[] sprites = Assets.getSuitForLevel(level);
        Sprite head = new Sprite(sprites[0]);
        head.rotate90(true);
        head.flip(true, true);
        m.isFlipped = true;
        m.updateSprite("head", head);
        m.updateSprite("torso", sprites[1]);
        m.updateSprite("leg", sprites[2]);
        m.updateSprite("shoulder", sprites[3]);
        m.updateSprite("elbow", sprites[4]);
        for (int i = 0; i < 5; i++) {
            spriteindices[1] = (int) level / 10;
        }
    }

    public void equipSuitNoCheck() {
        Sprite[] sprites = Assets.getSuitForLevelNoCheck(level);
        Sprite head = new Sprite(sprites[0]);
        head.rotate90(true);
        head.flip(true, true);
        m.isFlipped = true;
        m.updateSprite("head", head);
        m.updateSprite("torso", sprites[1]);
        m.updateSprite("leg", sprites[2]);
        m.updateSprite("shoulder", sprites[3]);
        m.updateSprite("elbow", sprites[4]);
        for (int i = 0; i < 5; i++) {
            spriteindices[1] = (int) level / 10;
        }
    }

    public void reload(){
        System.out.println("Character reload");
        this.health = BASE_HEALTH;
        this.display_hp = BASE_HEALTH;
        this.min_dmg = 0;
        this.max_dmg = this.base_maxdmg;
        level = 1;
        max_level = 1;
        max_wtnl = 0;
        win_streak = 0;
        lose_streak = 0;

        availablepool.clear();
        dnListA.clear();
        for (int i = availablepool.size(); i < 1000 - availablepool.size(); i++) {
            availablepool.add(new DamageNumber(-9999, -100, -100));
        }


        Sprite[] sprites = new Sprite[5];
        sprites[0] = Assets.findSpriteForName("head", spriteindices[0]);
        sprites[1] = Assets.findSpriteForName("torso", spriteindices[1]);
        sprites[2] = Assets.findSpriteForName("leg", spriteindices[2]);
        sprites[3] = Assets.findSpriteForName("shoulder", spriteindices[3]);
        sprites[4] = Assets.findSpriteForName("elbow", spriteindices[4]);




        Sprite head = new Sprite(sprites[0]);
        head.rotate90(m.isFlipped);
        head.flip(m.isFlipped, true);
        m.updateSprite("head", head);
        m.updateSprite("torso", sprites[1]);
        m.updateSprite("leg", sprites[2]);
        m.updateSprite("shoulder", sprites[3]);
        m.updateSprite("elbow", sprites[4]);
        m.hideWeapon();
        this.equipped_weapon = null;
    }


    public boolean isWeaponEquipped() {
        return this.equipped_weapon != null;
    }

    public String getName() {
        return name;
    }

    public void reset() {
        this.health = (isWeaponEquipped() ? equipped_weapon.getHp_multiplier() : 1) * this.BASE_HEALTH;// * level;

    }

    public int getBase_health() {
        return BASE_HEALTH;
    }

    public int getInitial_health() {
        return BASE_HEALTH * (isWeaponEquipped() ? equipped_weapon.getHp_multiplier() : 1);// * level;
    }

    public Sprite getSprite() {
        return sprite;
    }

    private void updateSprite() {
        sprite.setRegion(getSpriteForHP(health));
    }

    public Sprite getSpriteForHP(int hp) {
        if (hp / (BASE_HEALTH * (isWeaponEquipped() ? equipped_weapon.getHp_multiplier() : 1) / 4) > 10) {
            sprite.setRegion(Assets.face_anim.getKeyFrame(BattleScreen.statetime, true));
        } else {
            sprite.setRegion((hp <= 0 ? Assets.dead_face : Assets.faces[Math.max(Math.min(hp / (BASE_HEALTH * (isWeaponEquipped() ? equipped_weapon.getHp_multiplier() : 1) / 4), 7), 2)]));
        }
        return sprite;
    }


    public void incrementWins() {
        if(lose_streak > 0) {
            lose_streak = 0;
            win_streak = 0;
        }else {
            this.win_streak++;
            this.lose_streak--;
        }


        if (win_streak % (wins_to_level + 2) == wins_to_level + 1) {
            level--;
            level = Math.max(1, level);
        }
        System.out.println("Lose Streak: " + lose_streak + " ws " + win_streak);
    }

    public void resetWins() {
        this.win_streak = 0;
    }

    public void incrementLosses() {
        if(win_streak > 0){
            win_streak = 0;
            lose_streak =0;
        }else {
            this.lose_streak++;
            this.win_streak--;
        }
        if (lose_streak % wins_to_level == 0) {
            level++;
            System.out.println(name + " lvl up");
            if (level > max_level)
                max_wtnl = 0;
            max_level = Math.max(max_level, level);
        }
        if (level == max_level) {
            max_wtnl = Math.max(max_wtnl, lose_streak % wins_to_level);
        }
    }

    public void resetLosses() {
        this.lose_streak = 0;
    }

    public int getWin_streak() {
        return win_streak;
    }

    public void setWin_streak(int win_streak) {
        this.win_streak = win_streak;
    }

    public int getLose_streak() {
        return lose_streak;
    }

    public void setLose_streak(int lose_streak) {
        this.lose_streak = lose_streak;
    }

    public int getDisplay_hp() {
        return display_hp;
    }

    public void setDisplay_hp(int display_hp) {
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


        if (glow) {
            if (glow_type == 1)
                batch.draw(Assets.glow_ylw, getX(), getY(), getWidth(), getHeight());
            if (glow_type == 2)
                batch.draw(Assets.glow, getX(), getY(), getWidth(), getHeight());
        }

        otherY = (int) getY();
        m.originx = (int) (getX() + getWidth() / 2);
        m.originy = (int) (getY() + getHeight() / 2);
        renderDamageNumbers(batch);
        //  System.out.println(this.name);
        m.render(batch);

    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }

    //Adds a new damage number to the appropriate damage list
    private void addDmgNum(String num, int x, int y, DmgListSide side) {
        DamageNumber dn;
        if (!availablepool.isEmpty()) {
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

    public void consumeDmgNumPost(String num, DmgListSide side) {

        float y = otherY + getHeight() / 2;// + (rng.nextBoolean() ? - 1 : 1) * rng.nextInt((int)b.getLeftside().getHeight()/2);
        float x = getX() + getWidth() / 2;// + (rng.nextBoolean() ? - 1 : 1) * rng.nextInt((int)b.getRightside().getWidth()/4);
        addDmgNum(num, (int) x, (int) y, side);
    }

    public void renderDamageNumbers(Batch batch) {

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
        }

    }

    @Override
    public void setScale(float scaleXY) {
        m.modifyScale(scaleXY);
        super.setScale(scaleXY);
    }

    /**
     * Getters and Setters
     */

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
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
        //System.out.println("Weapon equipped " + name);
        this.equipped_weapon = weapon;
        this.min_dmg = base_mindmg + weapon.getMin_damage();
        this.max_dmg = base_maxdmg + weapon.getMax_damage();
        this.health = this.BASE_HEALTH * weapon.getHp_multiplier();// * this.level;
        m.updateSprite("wrist", new Sprite(weapon.getSprite()));
        m.showWeapon();

    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setBodyPart(BodyPartActor hsa) {

        Sprite toequip = new Sprite(hsa.getSprite());
        switch (hsa.getBtype()) {
            case HEAD:
                spriteindices[0] = hsa.part_index;
                toequip.rotate90(!m.isFlipped);
                m.updateSprite("head", toequip);
                break;
            case TORSO:
                spriteindices[1] = hsa.part_index;
                m.updateSprite("torso", toequip);
                break;
            case LEGS:
                spriteindices[2] = hsa.part_index;
                m.updateSprite("leg", toequip);
                break;
            case SHOULDER:
                spriteindices[3] = hsa.part_index;
                m.updateSprite("shoulder", toequip);
                break;
            case ELBOW:
                spriteindices[4] = hsa.part_index;
                m.updateSprite("elbow", toequip);
                break;
        }
    }

    public void setBodyPart(String part, Sprite sprite, int index) {

        Sprite toequip = new Sprite(sprite);


        if (part.equalsIgnoreCase("head")) {
            toequip.rotate90(!m.isFlipped);
            spriteindices[0] = index;
        }
        if (part.equalsIgnoreCase("torso"))
            spriteindices[1] = index;
        if (part.equalsIgnoreCase("leg"))
            spriteindices[2] = index;
        if (part.equalsIgnoreCase("shoulder"))
            spriteindices[3] = index;
        if (part.equalsIgnoreCase("elbow"))
            spriteindices[4] = index;

        m.updateSprite(part, toequip);
    }

    public int getMax_level() {
        return max_level;
    }

    public void setMax_level(int max_level) {
        this.max_level = max_level;
    }

    public int getMax_wtnl() {
        return max_wtnl;
    }

    public void setMax_wtnl(int max_wtnl) {
        this.max_wtnl = max_wtnl;
    }

    public int getWins_to_level() {
        return wins_to_level;
    }

    public int[] getSpriteindices() {
        return spriteindices;
    }

    public void setSpriteindices(int[] spriteindices) {
        this.spriteindices = spriteindices;
    }

    public enum DmgListSide {
        LEFT,
        RIGHT
    }
}
