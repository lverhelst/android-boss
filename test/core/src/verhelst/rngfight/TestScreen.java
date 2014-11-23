package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;

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

        Weapon aw = Weapon.generateRandomWeapon(10, Assets.getWeaponSprite(), Weapon.POSITION.LEFT_POSITION);

        table = new LTable(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        table.addActor(aw.getTable());
        table.addActor(aw.getTable());
        table.addRow();
        table.addActor(a);

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
