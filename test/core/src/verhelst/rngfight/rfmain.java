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
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
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
    Character a  = new Character("Boss", 1000);
    Character b = new Character("NotBoss", 100);
    Battle btl;
    BattleRunnable bsw;

    //for use in Render()
    List<Integer> lst;
    Iterator<DamageNumber> i;

    String[] endmessage= new String[1];

    ShapeRenderer sr;
    int xA, xB, yA, yB, aH = 1000, bH = 100, hits = 0;

    boolean battling;

    OrthographicCamera camera;
    Viewport viewport;
    private Sprite sprite;
    private Sprite sprite2;

    TextureAtlas ta;
    @Override
    public void create () {
        batch = new SpriteBatch();


        ta = new TextureAtlas(Gdx.files.internal("testsprite.asset"));
        //TextureAtlas.AtlasRegion region = ta.findRegion("sprite_a1");
       /// Sprite sp = new Sprite(region);


        //img = region.getTexture();// new Texture(region);
       // img2 = new Texture("player_sprite.png");
       // img.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
       // img2.setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);

        float h = Gdx.graphics.getHeight();
        float w = Gdx.graphics.getWidth();



        camera = new OrthographicCamera(w,h);
        camera.position.set(camera.viewportWidth / 2f, camera.viewportHeight / 2f, 0);
        camera.update();
        viewport = new ScalingViewport(Scaling.stretch,w,h,camera);

        rng = new Random();
        sr = new ShapeRenderer();

        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("Mecha_Bold.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(40 * Gdx.graphics.getDensity() );
        bf = ftfg.generateFont(ftfp);
        ftfp.size = Math.round(20 * Gdx.graphics.getDensity());
        dmgNumFnt = ftfg.generateFont(ftfp);

        ftfg.dispose();
        xA = (int)w/3;
        xB = (int)w/3 * 2;
        yA = (int)h / 2;
        yB = (int)h / 2;


        System.out.println(xA + " " + xB);

        sprite = new Sprite(ta.findRegion("sprite_a2"));
        sprite.setOrigin(0,0);
        //sprite.setSize(img.getWidth(), img.getWidth());
        sprite.setSize(128,128);
        sprite.setPosition(xA - sprite.getWidth()/2 ,yB - sprite.getHeight()/2);

        sprite2 = new Sprite(ta.findRegion("sprite_a2"));

        sprite2.setOrigin(0,0);
        //sprite2.setSize(img.getHeight(), img.getWidth());
        sprite2.setSize(128,128);
        sprite2.setPosition(xB - sprite2.getWidth()/2, yB - sprite2.getHeight()/2);
        sprite2.setColor(Color.RED);


        Gdx.input.setInputProcessor(this);
    }

    private int getRandomNumber(){
        return ((rng.nextInt(2) == 0)? -1 : 1) * rng.nextInt(11);
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

        if(!battling) {

            battling = true;
            a = new Character("Boss", 1000);
            b = new Character("NotBoss", 100);
            btl = new Battle(a, b);
            bswNumList = new ConcurrentLinkedQueue<List<Integer>>();
            bsw = new BattleRunnable(btl, bswNumList, endmessage);

            bsw.run();

            new Thread(new Runnable(){
                @Override
                public void run() {
                    while(battling){
                        try {
                            Thread.currentThread().sleep(20);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!bswNumList.isEmpty()) {

                            lst = bswNumList.poll();

                            addDmgNum(lst.get(0), (int)sprite.getX() + rng.nextInt((int)sprite.getWidth()), (int)sprite.getY() + (int)sprite.getHeight() * (int)sprite.getScaleY() - rng.nextInt(20), 1);
                            addDmgNum(lst.get(1), (int)sprite2.getX() + rng.nextInt((int)sprite2.getWidth()), (int)sprite2.getY() + (int)sprite2.getHeight() * (int)sprite2.getScaleY() - rng.nextInt(20), 2);

                            aH = lst.get(2);
                            bH = lst.get(3);
                            if (aH <= 0 || bH <= 0) {
                                battling = false;
                            }
                            hits = lst.get(4);
                        }

                    }
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


    int health_bar_height = 12;
    int text_height_adjust = 0;
    int health_bar_padding = 2;


    @Override
    public void render() {
        //Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        health_bar_height = (int)( Gdx.graphics.getHeight()/480) * 12;

        Matrix4 normalProjection = new Matrix4().setToOrtho2D(0, 0, Gdx.graphics.getWidth(),  Gdx.graphics.getHeight());
        batch.setProjectionMatrix(normalProjection);
        batch.begin();
        bf.setColor(1.0f, 1.0f, 1.0f, 1.0f);

        text_height_adjust = (int)bf.getBounds("1000").height;

        bf.draw(batch, "" + Math.max(aH,0) , 0, Gdx.graphics.getHeight() -(health_bar_height + (2 * health_bar_padding)));
        if (aH <= 0){
            sprite.setRegion(ta.findRegion("sprite_a4"));
        }else if(aH/10 < 51){
            sprite.setRegion(ta.findRegion("sprite_a1"));
        }else if(aH/10 < 101){
            sprite.setRegion(ta.findRegion("sprite_a2"));
        }else {
            sprite.setRegion(ta.findRegion("sprite_a3"));
        }



        bf.draw(batch, "" + Math.max(bH,0) , 0, Gdx.graphics.getHeight() - ((health_bar_height + (2 * health_bar_padding)) * 2 + text_height_adjust));
        if (bH <= 0){
            sprite2.setRegion(ta.findRegion("sprite_a4"));
        }else if(bH < 51){
            sprite2.setRegion(ta.findRegion("sprite_a1"));
        }else if(bH < 101){
            sprite2.setRegion(ta.findRegion("sprite_a2"));
        }else {
            sprite2.setRegion(ta.findRegion("sprite_a3"));
        }
        bf.draw(batch, "Hits: " + hits, Gdx.graphics.getWidth() - (50 + bf.getBounds("Hits: " + hits).width), 10 + text_height_adjust);



        if(!battling && endmessage[0] != null){

            bf.drawWrapped(batch, endmessage[0], Gdx.graphics.getWidth()/2 - bf.getWrappedBounds(endmessage[0],Gdx.graphics.getWidth() - 10).width/2, Gdx.graphics.getHeight()/4, Gdx.graphics.getWidth() - 10);
        }

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
        sprite2.draw(batch);

//        batch.draw(sprite, Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight() / 2 - img.getHeight());
  //      batch.draw(sprite2, Gdx.graphics.getWidth()/3 * 2, Gdx.graphics.getHeight() / 2 - img.getHeight());

        batch.end();


        sr.setProjectionMatrix(normalProjection);
        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        sr.rect(0, Gdx.graphics.getHeight() - health_bar_height - health_bar_padding, 100, health_bar_height );
        sr.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        sr.rect(1, Gdx.graphics.getHeight() - health_bar_height, aH/10 /*Math.min(aH/10, 1000)*/ , health_bar_height - 4);

        sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        sr.rect(0, Gdx.graphics.getHeight() - (text_height_adjust + ((health_bar_height + health_bar_padding) * 2) + health_bar_padding), 100, health_bar_height);
        sr.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        sr.rect(1, Gdx.graphics.getHeight() - (text_height_adjust + (health_bar_height + health_bar_padding) * 2), bH /* Math.min(bH, 1000)*/,  health_bar_height - 4);
        sr.end();

    }

    @Override
    public void resize(int width, int height) {
        //move sprites to middle
        float h = Gdx.graphics.getHeight();
        float w = Gdx.graphics.getWidth();
        xA = (int)w/3;
        xB = (int)w/3 * 2;
        yA = (int)h / 2;
        yB = (int)h / 2;
        sprite.setPosition(xA - sprite.getWidth()/2 ,yB - sprite.getHeight()/2);
        sprite2.setPosition(xB - sprite2.getWidth()/2, yB - sprite2.getHeight()/2);
        System.out.println(width + " " + height);

        camera.viewportWidth = Gdx.graphics.getWidth();
        camera.viewportHeight = Gdx.graphics.getHeight();
        camera.update();
        viewport.update((int)w,(int)h,true);
    }
}
