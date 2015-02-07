package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import verhelst.Comp.LCell;
import verhelst.Comp.LTable;

/**
 * Created by Leon I. Verhelst on 2/6/2015.
 */
public class LoadingScreen implements Screen {
    LTable table;

    Image[] sprites = new Image[6];
    Image spr;
    int sprInd;
    LCell t;
    long curtime;

    public LoadingScreen(){
        for(int i = 1; i <= 6; i++){
            sprites[i-1] = new Image(new Sprite(new Texture(Gdx.files.internal("In Development\\loading" + i +".png"))));
        }
        spr = sprites[0];
        sprInd = 0;
        table = new LTable(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        t = table.addActor(spr, true);
        curtime = 0;
    }


    @Override
    public void render(float delta) {
        if(System.currentTimeMillis() - curtime < 50){

        }else {
            curtime = System.currentTimeMillis();
            Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
            RngFight.batch.begin();
            if (sprInd + 1 > 5) {
                sprInd = 0;
            } else {
                sprInd++;
            }

            t.setActor(sprites[sprInd]);
            ;
            table.draw(RngFight.batch, 1.0f);
            RngFight.batch.end();
        }
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
