package verhelst.rngfight;

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



public class rfmain extends ApplicationAdapter implements InputProcessor, ApplicationListener {
    SpriteBatch batch;
    Texture img;
    Texture img2;
    Random rng;
    ArrayList<DamageNumber> dnListA = new ArrayList<DamageNumber>();
    ArrayList<DamageNumber> dnListB = new ArrayList<DamageNumber>();
    ConcurrentLinkedQueue<List<Integer>> bswNumList = new ConcurrentLinkedQueue<List<Integer>>();


    BitmapFont bf;
    BitmapFont dmgNumFnt;
    Character a  = new Character("Enemy", 200);
    Character b = new Character("You", 200);
    Battle btl;
    BattleRunnable bsw;

    Weapon loot;
    //for use in Render()
    List<Integer> lst;
    Iterator<DamageNumber> i;

    String[] endmessage= new String[1];

    ShapeRenderer sr;
    int xA, xB, yA, yB, aH = 200, bH = 200, hits = 0;

    boolean battling;

    OrthographicCamera camera;
    Viewport viewport;
    private Sprite sprite;
    private Sprite sprite2;
    private Sprite swurd;
    boolean showloot;


    TextureAtlas ta;
    Animation face_anim;
    float statetime;

    private int boss_level = 1;
    private int win_streak = 0;
    float h;
    float w;

    @Override
    public void create () {


        batch = new SpriteBatch();


        ta = new TextureAtlas(Gdx.files.internal("face_sprites\\sprite.pack"));
        //TextureAtlas.AtlasRegion region = ta.findRegion("sprite_a1");
       /// Sprite sp = new Sprite(region);

        TextureRegion[] hairframes = {new TextureRegion(ta.findRegion("face11")),new TextureRegion(ta.findRegion("face12")),new TextureRegion(ta.findRegion("face13")),new TextureRegion(ta.findRegion("face14"))};
        face_anim = new Animation(0.075f, hairframes);
        statetime = 0f;



        //img = region.getTexture();// new Texture(region);
        img = new Texture("swurd.png");
        img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
       // img2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);





        camera = new OrthographicCamera(w,h);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        viewport = new ScalingViewport(Scaling.stretch,w,h,camera);

        rng = new Random();
        sr = new ShapeRenderer();

        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("Mecha_Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(36 * Gdx.graphics.getDensity() );
        bf = ftfg.generateFont(ftfp);
        ftfp.size = Math.round(20 * Gdx.graphics.getDensity());
        dmgNumFnt = ftfg.generateFont(ftfp);

        ftfg.dispose();
        xA = (int)w/3;
        xB = (int)w/3 * 2;
        yA = (int)h / 2;
        yB = (int)h / 2;


        System.out.println(xA + " " + xB);

        sprite = new Sprite(ta.findRegion("face4"));
        sprite.setOrigin(0,0);
        //sprite.setSize(img.getWidth(), img.getWidth());
        System.out.println(scale);
        scale = Math.min(scale, 2);
        sprite.setSize(128 * scale,128 * scale);
        sprite.setPosition(xA - sprite.getWidth()/2 ,yB - sprite.getHeight()/2);


        sprite2 = new Sprite(ta.findRegion("face4"));

        sprite2.setOrigin(0,0);
        //sprite2.setSize(img.getHeight(), img.getWidth());

        sprite2.setSize(128 * scale, 128 * scale);
        sprite2.setPosition(xB - sprite2.getWidth()/2, yB - sprite2.getHeight()/2);
        sprite2.setColor(0.8f,1.0f,0.8f,1.0f);


        swurd = new Sprite(img);

        swurd.setSize(64 * scale,64 * scale);
        swurd.setPosition( w/2 - swurd.getWidth()/2, h/5);


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
            if(swurd.getBoundingRectangle().contains(screenX, Gdx.graphics.getHeight() - screenY)){
                b.setEquipped_weapon(loot);
            }
            showloot = false;
        }
        if(!battling) {
            Weapon boss_wep = null;
            if(win_streak % 3 == 2){
                boss_level++;
                win_streak = 0;
                if(a.isWeaponEquipped())
                    boss_wep = a.getEquipped_weapon();
                a = null;
            }


            showloot = false;
            battling = true;
            if (a == null) {
                a = new Character("Enemy", 200 * boss_level);
                if(boss_wep != null)
                    a.setEquipped_weapon(boss_wep);
            }else
                a.reset();

            if (b == null)
                b = new Character("You", 200);
            else
                b.reset();

            btl = new Battle(a, b);


            bswNumList = new ConcurrentLinkedQueue<List<Integer>>();
            bsw = new BattleRunnable(btl, bswNumList, endmessage);

            bsw.run();

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

                            addDmgNum(lst.get(0), (int) sprite.getX() + rng.nextInt((int) sprite.getWidth()), (int) sprite.getY() + (int) sprite.getHeight() * (int) sprite.getScaleY() - rng.nextInt(20), 1);
                            addDmgNum(lst.get(1), (int) sprite2.getX() + rng.nextInt((int) sprite2.getWidth()), (int) sprite2.getY() + (int) sprite2.getHeight() * (int) sprite2.getScaleY() - rng.nextInt(20), 2);

                            aH = lst.get(2);
                            bH = lst.get(3);
                            hits = lst.get(4);
                            if (aH <= 0 || bH <= 0) {
                                battling = false;
                                if(aH <= 0 && bH <= 0){
                                    win_streak = 0;
                                }else {

                                    //generate weapon
                                    if (aH <= 0 && bH > 0) {
                                        win_streak++;
                                        if (hits % 3 == 0) {
                                            loot = new Weapon(0, rng.nextInt(2 * boss_level), (float) 0.1, swurd);
                                            showloot = true;
                                        }
                                    }
                                    if (aH > 0 && bH < 0) {
                                        win_streak = 0;
                                        if (hits % 3 == 0) {
                                            loot = new Weapon(0, rng.nextInt(2 * boss_level - 1), (float) 0.1, swurd);
                                            if (!a.isWeaponEquipped() || loot.getMax_damage() > a.getEquipped_weapon().getMax_damage())
                                                a.setEquipped_weapon(loot);
                                        }
                                    }
                                }

                                //select next boss


                            }

                        }

                    }
                }
            }).start();


            return true;
        }
        else
            return false;

    }

    int wx, wy;

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


    int health_bar_height = 12;
    int text_height_adjust = 0;
    int health_bar_padding = 2;


    int scale = 1;
    @Override
    public void render() {
        //Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        statetime += Gdx.graphics.getDeltaTime();
        scale = (int)Math.min(( Gdx.graphics.getDensity()/0.6),2);
        h = Gdx.graphics.getHeight();
        w = Gdx.graphics.getWidth();

        health_bar_height = scale * 12;
        sprite.setSize(128 * scale,128 * scale);
        sprite2.setSize(128 * scale, 128 * scale);
        swurd.setSize(64 * scale,64 * scale);
        sprite.setPosition(xA - sprite.getWidth()/2 ,yB - sprite.getHeight()/2);
        sprite2.setPosition(xB - sprite2.getWidth()/2, yB - sprite2.getHeight()/2);
        swurd.setPosition(w/2 - swurd.getWidth()/2,  h/5);

        Matrix4 normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        batch.setProjectionMatrix(normalProjection);
        batch.begin();
        bf.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        text_height_adjust = (int)bf.getBounds("1000").height;

        bf.draw(batch, a.getName() + ": " + Math.max(aH,0) , 0, Gdx.graphics.getHeight() -(health_bar_height + (2 * health_bar_padding)));

        if(aH/50 > 10) {
            sprite.setRegion(face_anim.getKeyFrame(statetime + 1, true));
        }else {
            sprite.setRegion(ta.findRegion("face" + (aH <= 0 ? 1 : Math.max(Math.min(aH / 50, 10), 2))));
        }
        bf.draw(batch, b.getName() + ": " + Math.max(bH,0) , 0, Gdx.graphics.getHeight() - ((health_bar_height + (2 * health_bar_padding)) * 2 + text_height_adjust));
        if(bH/50 > 10){
            sprite2.setRegion(face_anim.getKeyFrame(statetime, true));
        }else {
            sprite2.setRegion(ta.findRegion("face" + (bH <= 0 ? 1 : Math.max(Math.min(bH / 50, 10), 2))));
        }
        bf.draw(batch, "Hits: " + hits, 0, text_height_adjust + 5);


        synchronized (dnListA) {
            for (i = dnListA.iterator(); i.hasNext(); ) {

                DamageNumber dn = i.next();
                if (dn.isRemoveable()) {
                    i.remove();
                } else {
                    dmgNumFnt.setColor(dn.getRed(), dn.getGreen(), dn.getBlue(), dn.getAlpha());
                    dmgNumFnt.draw(batch, dn.getCs(), dn.getX(), dn.getY() +  bf.getBounds(dn.getCs()).height);
                    dn.update();
                }
            }
        }
        synchronized (dnListB) {

            for (i = dnListB.iterator(); i.hasNext(); ) {

                DamageNumber dn = i.next();
                if (dn.isRemoveable()) {
                    i.remove();
                } else {
                    dmgNumFnt.setColor(dn.getRed(), dn.getGreen(), dn.getBlue(), dn.getAlpha());
                    dmgNumFnt.draw(batch, dn.getCs(), dn.getX(), dn.getY() + bf.getBounds(dn.getCs()).height);
                    dn.update();
                }

            }
        }


        sprite.draw(batch);
        bf.draw(batch, a.getName(), sprite.getX(), sprite.getY());
        sprite2.draw(batch);
        bf.draw(batch, b.getName(), sprite2.getX(), sprite2.getY());

        if(showloot) {
            swurd.draw(batch);
            bf.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            bf.draw(batch, "Loot", swurd.getX() - scale * 20, swurd.getY() + swurd.getHeight() + text_height_adjust + 4);
            bf.draw(batch, "" + loot.getMin_damage(), swurd.getX() + swurd.getWidth() + 10, swurd.getY() + swurd.getHeight() - 10 + text_height_adjust);
            bf.draw(batch, "" + loot.getMax_damage(), swurd.getX() + swurd.getWidth() + 10, swurd.getY() + swurd.getHeight() - 40 - text_height_adjust);
        }

        if(a.isWeaponEquipped()){
            Sprite awep =  a.getEquipped_weapon().getSprite();
            awep.setPosition(sprite.getX() - 20-  awep.getWidth(), sprite.getY() + sprite.getHeight()/2 - awep.getHeight()/2);
            awep.draw(batch);

            bf.draw(batch, "" + a.getEquipped_weapon().getMin_damage(), awep.getX() - 20 * scale, awep.getY() + awep.getHeight() - 10 + text_height_adjust);
            bf.draw(batch, "" + a.getEquipped_weapon().getMax_damage(), awep.getX() - 20 * scale, awep.getY() + awep.getHeight() - 40 - text_height_adjust);
        }
        if(b.isWeaponEquipped()){
            Sprite bwep =  b.getEquipped_weapon().getSprite();
            bwep.setPosition(sprite2.getX() + sprite2.getWidth() + 20, sprite2.getY() + sprite2.getHeight()/2 - bwep.getHeight()/2);
            bwep.draw(batch);

            bf.draw(batch, "" + b.getEquipped_weapon().getMin_damage(), bwep.getX() + bwep.getWidth() + 10, bwep.getY() + bwep.getHeight() - 10 + text_height_adjust);
            bf.draw(batch, "" + b.getEquipped_weapon().getMax_damage(), bwep.getX() + bwep.getWidth() + 10, bwep.getY() + bwep.getHeight() - 40 - text_height_adjust);
        }

        if(!battling && endmessage[0] != null){

            bf.drawWrapped(batch, endmessage[0], Gdx.graphics.getWidth()/2 - bf.getWrappedBounds(endmessage[0],Gdx.graphics.getWidth() - 10).width/2, Gdx.graphics.getHeight()/4 * 3, Gdx.graphics.getWidth() - 10);

        }
        bf.draw(batch, "Win Streak: " + win_streak, 0, text_height_adjust*3 + 15);
        bf.draw(batch, "Boss Level: " + boss_level, 0, text_height_adjust*2 + 10);

        batch.end();


        sr.setProjectionMatrix(normalProjection);

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        sr.rect(0, Gdx.graphics.getHeight() - health_bar_height - health_bar_padding, 200, health_bar_height );
        sr.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        sr.rect(1, Gdx.graphics.getHeight() - health_bar_height, aH , health_bar_height - 4);

        sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        sr.rect(0, Gdx.graphics.getHeight() - (text_height_adjust + ((health_bar_height + health_bar_padding) * 2) + health_bar_padding), 200, health_bar_height);
        sr.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        sr.rect(1, Gdx.graphics.getHeight() - (text_height_adjust + (health_bar_height + health_bar_padding) * 2), bH,  health_bar_height - 4);



        sr.end();
        if(showloot) {
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
            sr.rect(swurd.getX(), swurd.getY(), swurd.getWidth(), swurd.getHeight());
            sr.end();
        }
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


        sprite.setPosition(xA - sprite.getWidth()/2 ,yB - sprite.getHeight()/2);
        sprite2.setPosition(xB - sprite2.getWidth()/2, yB - sprite2.getHeight()/2);
        swurd.setPosition(w/2 - swurd.getWidth()/2,  h/5);



        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();
        camera.update();
        viewport.update((int)w,(int)h,true);
    }
}
