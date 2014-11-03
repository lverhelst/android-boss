package verhelst.rngfight;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.FillViewport;
import com.badlogic.gdx.utils.viewport.ScalingViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;

//Levels
//1 - Easy -> New level each win
//2 - Normal -> New level every 2 wins
//3 - Hard -> New level every 3 wins
//4 - Extreme -> New level every 3 wins + boss hp scaling with level

public class rfmain extends ApplicationAdapter implements InputProcessor, ApplicationListener {
    SpriteBatch batch;

    Random rng;
    ArrayList<DamageNumber> dnListA = new ArrayList<DamageNumber>();
    ArrayList<DamageNumber> dnListB = new ArrayList<DamageNumber>();
    ConcurrentLinkedQueue<List<Integer>> bswNumList = new ConcurrentLinkedQueue<List<Integer>>();


    BitmapFont bf;
    BitmapFont dmgNumFnt;
    BitmapFont wepNumFnt;
    Character a  = new Character("Enemy", null);
    Character b = new Character("You", null);
    Battle btl;

    Weapon loot;
    //for use in Render()
    List<Integer> lst;
    Iterator<DamageNumber> i;

    ShapeRenderer sr;
    int xA, xB, yA, yB, aH = 200, bH = 200, hits = 0;
    int wx, wy;
    int health_bar_height = 12;
    int text_height_adjust = 0;
    int health_bar_padding = 2;


    int scale = 1;


    boolean battling;

    OrthographicCamera camera;
    Viewport viewport;
    boolean showloot;

    public static float statetime;

    private int max_boss_level = 1;
    private int max_boss_level_wins = 0;

    float h;
    float w;

    Sprite sprite, sprite2, swurd, wdi;
    boolean line_debug;
    Assets assets;
    BattleResultHandler brh;
    BattleResult[] results;

    boolean isDmgNumOverridden = false;
    int dmgNumberOverride = 0;

    @Override
    public void create () {
        //Loads assets
        assets = new Assets();

        batch = new SpriteBatch();
        statetime = 0f;

        camera = new OrthographicCamera(w,h);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();

        viewport = new ScalingViewport(Scaling.stretch,w,h,camera);


        rng = new Random();
        sr = new ShapeRenderer();

        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("game_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(35 * Gdx.graphics.getDensity() );
        bf = ftfg.generateFont(ftfp);


        ftfp.size = Math.round(20 * Gdx.graphics.getDensity());
        dmgNumFnt = ftfg.generateFont(ftfp);
        
        int siz = (Gdx.app.getType() == Application.ApplicationType.Android ? (int) Assets.weapon_data_icon.getHeight() * scale / 4 : 26);
        ftfp.size = Math.round(siz * Gdx.graphics.getDensity());
        wepNumFnt = ftfg.generateFont(ftfp);

        ftfg.dispose();
        xA = (int)w/3;
        xB = (int)w/3 * 2;
        yA = (int)h / 2;
        yB = (int)h / 2;


        System.out.println(xA + " " + xB);

        sprite = new Sprite(Assets.resting_face);
        sprite.setOrigin(0,0);
        //sprite.setSize(img.getWidth(), img.getWidth());
        System.out.println(scale);
        scale = Math.min(scale, 2);
        sprite.setSize(128 * scale,128 * scale);
        sprite.setPosition(xA - sprite.getWidth()/2 ,yB - sprite.getHeight()/2);


        sprite2 = new Sprite(Assets.resting_face);

        sprite2.setOrigin(0,0);
        //sprite2.setSize(img.getHeight(), img.getWidth());

        sprite2.setSize(128 * scale, 128 * scale);
        sprite2.setPosition(xB - sprite2.getWidth()/2, yB - sprite2.getHeight()/2);
        sprite2.setColor(0.8f,1.0f,0.8f,1.0f);


        swurd = new Sprite(assets.getWeaponSprite());

        swurd.setSize(64 * scale,64 * scale);
        swurd.setPosition( w/2 - (swurd.getWidth()*100/66/2), h/5);

        wdi = Assets.weapon_data_icon;

        a  = new Character("Enemy", sprite);
        b = new Character("You", sprite2);
        brh = new BattleResultHandler();
        results = new BattleResult[0];
        Gdx.input.setInputProcessor(this);
    }


    private void addDmgNum(int num, int x, int y, int whichlist){
        DamageNumber dn = new DamageNumber(num, x, y);
        if(whichlist == 1)
            synchronized (dnListA) {
                dnListA.add(dn);
            }
        else
            synchronized (dnListB) {
                dnListB.add(dn);
            }
    }




    @Override
    public boolean keyDown(int keycode) {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {



        if (showloot){

            wx = screenX;
            wy = screenY;
            float ls = (float)( Math.pow(wx - w/2,2) + Math.pow((h-wy) - h/4,2));
            float r = (float) Math.pow(h/3 - h/4,2);
            if(ls <= r){
                b.setEquipped_weapon(loot);
            }
        }
        if(!battling) {
            if(b.getWin_streak() % 3 == 2){
                a.setLevel(a.getLevel() + 1);
                if(a.getLevel() > max_boss_level){
                    max_boss_level_wins = 0;
                }
                max_boss_level = Math.max(max_boss_level, a.getLevel());
                if(b.isWeaponEquipped())
                    a.setEquipped_weapon(Weapon.generateScaledWeapon(b.getEquipped_weapon().getMin_damage(), b.getEquipped_weapon().getMax_damage(), a.getLevel(),assets.getWeaponSprite()));
                else
                    a.setEquipped_weapon(Weapon.generateRandomWeapon(a.getLevel(),assets.getWeaponSprite()));

                b.resetWins();
            }

            if(b.getLose_streak() % 4 == 3){
                a.setLevel(Math.max(a.getLevel() - 1,1));
                b.resetLosses();
                if(b.isWeaponEquipped())
                    a.setEquipped_weapon(Weapon.generateScaledWeapon(b.getEquipped_weapon().getMin_damage(), b.getEquipped_weapon().getMax_damage(), a.getLevel(),assets.getWeaponSprite()));
                else
                    a.setEquipped_weapon(Weapon.generateRandomWeapon(a.getLevel(),assets.getWeaponSprite()));

            }
            showloot = false;
            battling = true;
            if (a == null) {
                a = new Character("Enemy", sprite);
            }else
                a.reset();

            if (b == null)
                b = new Character("You", sprite2);
            else
                b.reset();


            bswNumList = new ConcurrentLinkedQueue<List<Integer>>();
            btl = new Battle(a, b, bswNumList);

            btl.run();

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (battling) {
                        try {
                            Thread.currentThread().sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!bswNumList.isEmpty()) {

                            lst = bswNumList.poll();

                            int width = (int)a.getSprite().getWidth();
                            if(isDmgNumOverridden){
                                addDmgNum(dmgNumberOverride, (int) xA - width / 2 + rng.nextInt(width), yA + rng.nextInt(30), 1);
                                addDmgNum(dmgNumberOverride, (int) xB - width / 2 + rng.nextInt(width), yB + rng.nextInt(30), 2);
                            }
                            else {
                                addDmgNum(lst.get(0), (int) xA - width / 2 + rng.nextInt(width), yA + rng.nextInt(30), 1);
                                addDmgNum(lst.get(1), (int) xB - width / 2 + rng.nextInt(width), yB + rng.nextInt(30), 2);
                            }
                            aH = lst.get(2);
                            bH = lst.get(3);
                            hits = lst.get(4);
                            if (aH <= 0 || bH <= 0) {
                                battling = false;
                                loot = Weapon.generateRandomWeapon(a.getLevel(),assets.getWeaponSprite());
                                break;
                            }

                        }

                    }
                    isDmgNumOverridden = false;
                    results = brh.getResults(a,b,hits);
                }
            }).start();


            return true;
        }
        else
            return false;

    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }


    @Override
    public void render() {
        //Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);


        statetime += Gdx.graphics.getDeltaTime();
        scale = (int)Math.min(( Gdx.graphics.getDensity()/0.6),2.5);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();




        health_bar_height = scale * 12;
        Matrix4 normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());

        batch.setProjectionMatrix(normalProjection);
        batch.begin();

        //Draw Healths.
        bf.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        text_height_adjust = (int)bf.getBounds("1000").height;
        bf.draw(batch, a.getName() + ": " + Math.max(aH,0) , 0, Gdx.graphics.getHeight() -(health_bar_height + (2 * health_bar_padding)));
        bf.draw(batch, b.getName() + ": " + Math.max(bH,0) , 0, Gdx.graphics.getHeight() - ((health_bar_height + (2 * health_bar_padding)) * 2 + text_height_adjust));

        //Add Damage Numbers to screen
        synchronized (dnListA) {
            for (i = dnListA.iterator(); i.hasNext(); ) {

                DamageNumber dn = i.next();
                if (dn.isRemoveable()) {
                    i.remove();
                } else {
                    dmgNumFnt.setColor(dn.getRed(), dn.getGreen(), dn.getBlue(), dn.getAlpha());
                    dmgNumFnt.draw(batch, dn.getCs(), dn.getX(), dn.getY() +  bf.getBounds(dn.getCs()).height);
                    dn.update(1);
                }
            }
        }
        //Add damage Numbers to Screen
        synchronized (dnListB) {
            for (i = dnListB.iterator(); i.hasNext(); ) {
                DamageNumber dn = i.next();
                if (dn.isRemoveable()) {
                    i.remove();
                } else {
                    dmgNumFnt.setColor(dn.getRed(), dn.getGreen(), dn.getBlue(), dn.getAlpha());
                    dmgNumFnt.draw(batch, dn.getCs(), dn.getX(), dn.getY() + bf.getBounds(dn.getCs()).height);
                    dn.update(1);
                }
            }
        }

        //Render character
        if(a != null && a.getSprite() != null)
            renderCharacter(a, xA, yA, aH);
        if(b != null && b.getSprite() != null)
            renderCharacter(b, xB, yB, bH);

        //Show character weapon
        if(a.isWeaponEquipped()){
            renderWeapon(a.getEquipped_weapon(),(int)(w/3),(int)(h/2), -1);
        }
        if(b.isWeaponEquipped()){
            //System.out.println("Weapon requipped");
            renderWeapon(b.getEquipped_weapon(),(int)(2 * w / 3),(int)(h/2), 1);
        }

        //show results if applicable
        renderBattleResults();

        //Show Stats
        String line = "";
        if(a.getLevel() == max_boss_level) {
            for (int i = 0; i < max_boss_level_wins; i++) {
                line += "*";
            }
        }
        bf.draw(batch, "Highscore: " + max_boss_level + "" + line , 0,  text_height_adjust*2 + 10);
        bf.draw(batch, "Hits: " + hits, 0, text_height_adjust + 5);

        //Show starting Healths
        //bf.draw(batch, a.getName() + ": " + a.getInitial_health(), w - 5 - bf.getBounds(a.getName() + "BHP: " + a.getBase_health()).width, text_height_adjust *2 + 10);
        //bf.draw(batch, b.getName() + ": " + b.getInitial_health(), w - 5 - bf.getBounds(a.getName() + "BHP: " + b.getBase_health()).width, text_height_adjust + 5);

        batch.end();

        sr.setProjectionMatrix(normalProjection);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        //Render Health Bars
        int a_scale = (a.getInitial_health()/a.getBase_health());
        sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        sr.rect(0, Gdx.graphics.getHeight() - health_bar_height - health_bar_padding, 200, health_bar_height );
        sr.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        sr.rect(1, Gdx.graphics.getHeight() - health_bar_height, aH/a_scale, health_bar_height - 4);

        int b_scale = (b.getInitial_health()/b.getBase_health());
        sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        sr.rect(0, Gdx.graphics.getHeight() - (text_height_adjust + ((health_bar_height + health_bar_padding) * 2) + health_bar_padding), 200, health_bar_height);
        sr.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        sr.rect(1, Gdx.graphics.getHeight() - (text_height_adjust + (health_bar_height + health_bar_padding) * 2), bH/b_scale,  health_bar_height - 4);


        sr.end();

        sr.begin(ShapeRenderer.ShapeType.Line);

        if(line_debug){

            sr.circle(w/2, h/4, h/3-h/4);
            //verticle middle line
            sr.rect(w/2, 0,1,h);
            //horizontal
            sr.rect(0, h/2,w,1);

            sr.setColor(0.0f,1.0f,0.0f,1.0f);
            sr.rect(w/3, 0,1,h);
            sr.rect(w*2/3, 0,1,h);

            sr.rect(0, h/4,w,1);

        }
        sr.end();
    }

    @Override
    public void resize(int width, int height) {
        //move sprites to middle
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();
        xA = (int)w/3;
        xB = (int)w/3 * 2;
        yA = (int)h / 2;
        yB = (int)h / 2;

        //Reset Sprite positions
        //sprite.setPosition(xA - sprite.getWidth()/2 ,yB - sprite.getHeight()/2);
        //sprite2.setPosition(xB - sprite2.getWidth()/2, yB - sprite2.getHeight()/2);
        //swurd.setPosition(w/2 - (swurd.getWidth()*100/66/2),  h/5);
        //Update Camera
        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();
        camera.update();
        viewport.update((int)w,(int)h,true);
    }

    //wep_pos = -1 left, 0 loot, 1 right
    private void renderWeapon(Weapon weapon, int x, int y, int wep_pos){
        //System.out.println("weapon render " + wep_pos);
        Sprite wepSprite = weapon.getSprite();
        wepSprite.setSize(64 * scale, 64 * scale);
        if(wep_pos != 0)
            y -= ((wepSprite.getHeight()) + a.getSprite().getWidth()/2);
        else
            y -= wepSprite.getHeight()/2;


        int text_x_offset = (int)wepNumFnt.getBounds(weapon.getMin_damage() + "-" + weapon.getMax_damage()).width;

        int field_x_offset = (int)wepSprite.getWidth();
        int data_x_offset = field_x_offset + (int)(a.getSprite().getWidth() * 14/100);

        if(wep_pos == -1){
            x -= wepSprite.getWidth();
            int temp = text_x_offset;
            field_x_offset = (temp + (int)(a.getSprite().getWidth() * 14/100)) * -1;
            data_x_offset = -1 * temp;
        }


        //actual draw wep
        if(wep_pos == 0) {
            x -= ((wepSprite.getWidth() + wdi.getWidth() + text_x_offset)/2);
        }
        wepSprite.setPosition(x, y);
        wepSprite.draw(batch);
        //draw icons
        wdi.setSize(5 * scale,wepSprite.getHeight());
        wdi.setPosition(x + field_x_offset, y);
        wdi.draw(batch);

        //draw min-max damage in upper right corner
        wepNumFnt.setColor(Color.WHITE);
        y = y-1;
        //Damage Numbers
        wepNumFnt.draw(batch, weapon.getMin_damage() + "-" + weapon.getMax_damage(), x + data_x_offset,y + wepSprite.getHeight());
        //Health Mutlipler
        wepNumFnt.draw(batch, "" + weapon.getHp_multiplier(), x + data_x_offset,y + wepSprite.getHeight()/4*3);
        //Dmg Type
        wepNumFnt.draw(batch, weapon.getExtra_type().name().charAt(0) + "", x + data_x_offset,y + wepSprite.getHeight()/2);
        //Life Steal
        wepNumFnt.draw(batch, "" + (int)weapon.getLife_steal(), x + data_x_offset,y + wepSprite.getHeight()/4);
        //
        if(wep_pos == 0) {
            int text_y_offset = (int) wepNumFnt.getBounds("Tap to equip").height;
            wepNumFnt.draw(batch, "Tap to equip", wepSprite.getX(), wepSprite.getY() + wepSprite.getHeight() + text_y_offset + 10);
        }

    }

    public void renderCharacter(Character chr, int x, int y, int hp){

        Sprite spr = chr.getSpriteForHP(hp);
        spr.setSize(128 * scale, 128 * scale);
        spr.setPosition(x - spr.getWidth()/2,y - spr.getHeight()/2);
        spr.draw(batch);

        if(chr == a) {
            String line = "";
            for (int i = 0; i < a.getWin_streak(); i++) {
                    line += "*";
            }
            float offset = 5 + bf.getBounds("" + chr.getLevel() + "**").width;

            bf.draw(batch, chr.getLevel() + line, spr.getX() + (chr.equals(a) ? -offset : offset + spr.getWidth()), spr.getY() + spr.getHeight() / 2);
        }
        //TODO: Find location for name &  better sizing
        // bf.draw(batch, a.getName(), sprite.getX(), sprite.getY());
    }

    public void renderBattleResults(){
        if(!battling && results.length > 0){
            //This can probably be done without a switch, I just don't know how yet.
            for(BattleResult br : results){
                switch(br){
                    case Player1Win:
                        this.renderWhoWon("You lost. Too bad.");
                        break;
                    case Player2Win:
                        this.renderWhoWon("Victory! " + a.getName() + " defeated!");
                        break;
                    case Tie:
                        this.renderWhoWon("D-D-D-Double Kill!");
                        break;
                    case ShowStaticLoot:
                        showloot = true;
                        renderWeapon(loot, (int) w/2, (int) h/4, 0);
                        break;
                    case ShowRandomLoot:
                        renderWeapon(loot = Weapon.generateRandomWeapon(a.getLevel(),assets.getWeaponSprite()), (int) w/2, (int) h/4, 0);
                        break;
                    case CustomMode_ioi:
                        isDmgNumOverridden = true;
                        dmgNumberOverride = 101;
                        break;
                }
            }
        }
    }

    private void renderWhoWon(String message){
        bf.drawWrapped(batch,message, Gdx.graphics.getWidth() / 2 - bf.getWrappedBounds(message, Gdx.graphics.getWidth() - 10).width / 2, Gdx.graphics.getHeight() / 4 * 3, Gdx.graphics.getWidth() - 10);
    }


}
