package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import verhelst.Comp.LTable;
import verhelst.bones.Model;

/**
 * Created by Orion on 11/11/2014.
 */
public class TestScreen implements Screen {

    Model m;
    LTable table;


    public TestScreen(){
        //m = new Model(Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight()/2, false, 96);
        Character a = new Character("name", Assets.getHeadSprite());
        a.setLevel(1);
        a.equipSuit();
        Character b = new Character("name", Assets.getHeadSprite());
        b.setLevel(11);
        b.equipSuit();
        Character c = new Character("name", Assets.getHeadSprite());
        c.setLevel(21);
        c.equipSuit();
        Character d = new Character("name", Assets.getHeadSprite());
        d.setLevel(31);
        d.equipSuit();
        Character e = new Character("name", Assets.getHeadSprite());
        e.setLevel(41);
        e.equipSuit();
        Character f = new Character("name", Assets.getHeadSprite());
        f.setLevel(51);
        f.equipSuit();
        Character g = new Character("name", Assets.getHeadSprite());
        g.setLevel(61);
        g.equipSuit();
        Character h = new Character("name", Assets.getHeadSprite());
        h.setLevel(71);
        h.equipSuit();
        Character i2 = new Character("name", Assets.getHeadSprite());
        i2.setLevel(81);
        i2.equipSuit();
        Character j = new Character("name", Assets.getHeadSprite());
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



    }



    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RngFight.batch.begin();
      //  m.render(RngFight.batch);
        table.draw(RngFight.batch, 1);
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
