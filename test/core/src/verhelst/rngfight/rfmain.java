package verhelst.rngfight;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;


import sun.rmi.runtime.Log;

//https://github.com/libgdx/libgdx/wiki/Threading ---LOOK AT THIS

public class rfmain extends ApplicationAdapter implements InputProcessor {
    SpriteBatch batch;
    Texture img;
    Texture img2;
    Random rng;
    ArrayList<DamageNumber> dnListA = new ArrayList<DamageNumber>();
    ArrayList<DamageNumber> dnListB = new ArrayList<DamageNumber>();
    ConcurrentLinkedQueue<List<Integer>> bswNumList = new ConcurrentLinkedQueue<List<Integer>>();

    BitmapFont bf;
    Character a  = new Character("Boss", 1000);
    Character b = new Character("NotBoss", 100);
    Battle btl;
    BattleRunnable bsw;

    String[] endmessage= new String[1];

    ShapeRenderer sr;
    int xA, xB, yA, yB, aH = 1000, bH = 100, hits = 0;

    boolean battling;


    @Override
    public void create () {
        batch = new SpriteBatch();
        img = new Texture("boss_sprite.png");
        img2 = new Texture("player_sprite.png");
        rng = new Random();
        sr = new ShapeRenderer();
        bf = new BitmapFont();

        xA = Gdx.graphics.getWidth()/3;
        xB = Gdx.graphics.getWidth()/3 * 2;
        yA = Gdx.graphics.getHeight() / 2;
        yB =  Gdx.graphics.getHeight() / 2;

        Gdx.input.setInputProcessor(this);
    }

    private int getRandomNumber(){
        return ((rng.nextInt(2) == 0)? -1 : 1) * rng.nextInt(11);
    }

    private void addDmgNum(int num, int x, int y, int whichlist){
        DamageNumber dn = new DamageNumber(num, x, y, batch);
        if(whichlist == 1)
            dnListA.add(dn);
        else
            dnListB.add(dn);
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

        //int value = this.getRandomNumber();
        //this.addDmgNum(value, screenX, Gdx.graphics.getHeight() - screenY, 1);
        a = new Character("Boss", 1000);
        b = new Character("NotBoss", 100);
        btl = new Battle(a, b);
        bsw = new BattleRunnable(btl, bswNumList, endmessage);

        if(!battling) {
            battling = true;
            bsw.run();
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

    List<Integer> lst;
    Iterator<DamageNumber> i;
    @Override
    public void render() {
        //Gdx.gl.glClearColor(1, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        if(!bswNumList.isEmpty()){
            lst = bswNumList.poll();

            addDmgNum(lst.get(0), xA + rng.nextInt(img.getWidth()), yA + 20, 1);
            addDmgNum(lst.get(1), xB + rng.nextInt(img2.getWidth()), yB + 20, 2);

            aH = lst.get(2);
            bH = lst.get(3);
            if(aH <= 0 || bH <= 0){
                battling = false;
            }
            hits = lst.get(4);
        }


        batch.begin();
        bf.draw(batch, "" + Math.max(aH,0) , 0,Gdx.graphics.getHeight() - 15);
        bf.draw(batch, "" + Math.max(bH,0) , 0, Gdx.graphics.getHeight() - 55);
        bf.draw(batch, "Hits: " + hits, Gdx.graphics.getWidth() - 100, 25);

        int  count = 0;
        for(i = dnListA.iterator(); i.hasNext(); ){
            DamageNumber dn = i.next();
            if(dn.isRemoveable()){
                i.remove();
            }else{
                dn.render();
            }
        }
        count = 0;
        for(i = dnListB.iterator(); i.hasNext();){
            DamageNumber dn = i.next();
            if(dn.isRemoveable()){
                i.remove();
            }else{
                dn.render();
            }

        }


        batch.draw(img, Gdx.graphics.getWidth()/3,Gdx.graphics.getHeight() / 2 - img.getHeight());
        batch.draw(img2, Gdx.graphics.getWidth()/3 * 2, Gdx.graphics.getHeight() / 2 - img.getHeight());

        if(!battling && endmessage[0] != null){
            bf.draw(batch, endmessage[0], Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/4);
        }

        batch.end();

        sr.begin(ShapeRenderer.ShapeType.Filled);
        sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        sr.rect(0, Gdx.graphics.getHeight() - 12, 100, 12);
        sr.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        sr.rect(1, Gdx.graphics.getHeight() - 10, Math.min(aH/10, 1000) , 8);

        sr.setColor(1.0f, 1.0f, 1.0f, 1.0f);
        sr.rect(0, Gdx.graphics.getHeight() - 52, 100, 12);
        sr.setColor(1.0f, 0.0f, 0.0f, 1.0f);
        sr.rect(1, Gdx.graphics.getHeight() - 50, Math.min(bH, 1000), 8);


        sr.end();

    }
}
