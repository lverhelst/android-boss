package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;
import java.util.List;

import verhelst.Comp.LCoverFlow;
import verhelst.Comp.LTable;
import verhelst.bones.Model;

/**
 * Created by Orion on 11/11/2014.
 */
public class TestScreen implements Screen {

    Model m;
    LTable table;
    LCoverFlow lcf;
    RngFight game;

    public TestScreen(RngFight game){
        this.game = game;
        //m = new Model(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, false, 96);
        Character a = new Character("Enemy", Assets.getHeadSprite());
        a.setLevel(1);
        a.equipSuit();
        Character b = new Character("Enemy", Assets.getHeadSprite());
        b.setLevel(11);
        b.equipSuit();
        Character c = new Character("Enemy", Assets.getHeadSprite());
        c.setLevel(21);
        c.equipSuit();
        Character d = new Character("Enemy", Assets.getHeadSprite());
        d.setLevel(31);
        d.equipSuit();
        Character e = new Character("Enemy", Assets.getHeadSprite());
        e.setLevel(41);
        e.equipSuit();
        Character f = new Character("Enemy", Assets.getHeadSprite());
        f.setLevel(51);
        f.equipSuit();
        Character g = new Character("Enemy", Assets.getHeadSprite());
        g.setLevel(61);
        g.equipSuit();
        Character h = new Character("Enemy", Assets.getHeadSprite());
        h.setLevel(71);
        h.equipSuit();
        Character i2 = new Character("Enemy", Assets.getHeadSprite());
        i2.setLevel(81);
        i2.equipSuit();
        Character j = new Character("Enemy", Assets.getHeadSprite());
        j.setLevel(91);
        j.equipSuit();



        table = new LTable(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());



        table.addActor(a);
        table.addActor(b);
        table.addActor(c);
        table.addActor(d);
        table.addActor(e);
        table.addActor(f);
        table.addActor(g);
        table.addActor(h);
        table.addActor(i2);
        table.addActor(j);
        //table.addRow();
        int i = 0;
        for(Sprite s: Assets.weapons_sprites){
            if(i % 10 == 0){
                table.addRow();
            }
            Image img = new Image(s);
            img.setRotation(90);
            table.addActor(img,true);
            i++;
        }
        List<Actor> actor = new ArrayList<Actor>();
        actor.add(a);
        actor.add(b);
        actor.add(c);
        actor.add(d);
        actor.add(e);
        actor.add(f);
        actor.add(g);
        actor.add(h);
        actor.add(i2);
        actor.add(j);

       lcf = new LCoverFlow(actor, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RngFight.batch.begin();
      //  m.render(RngFight.batch);
       // table.draw(RngFight.batch, 1);
        lcf.draw(RngFight.batch, 1);
        RngFight.batch.end();
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void show() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
