package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import verhelst.Comp.LCell;
import verhelst.Comp.LTable;

/**
 * Created by Leon I. Verhelst on 1/3/2015.
 */
public class MainScreen implements Screen, InputProcessor {

    LTable lt, entrances;
    Image butterbeaver;
    RngFight game;

    public MainScreen(RngFight fight){
        this.game = fight;
        lt = new LTable(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        butterbeaver = new Image(Assets.butterBeaver);
        lt.addActor(butterbeaver, true);

        entrances = new LTable(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
        //Fight screen
        Image img0 = new Image(Assets.landing_pad);
        img0.setName("img0");
        entrances.addActor(img0,true);
        entrances.addRow();
        //Stats Screen

        Image img1 = new Image(Assets.landing_pad);
        img1.setName("img1");
        entrances.addActor(img1,true);
        entrances.addRow();
        //Dressing Room Screen

        Image img3 = new Image(Assets.landing_pad);
        img3.setName("img2");
        entrances.addActor(img3, true);
        entrances.addRow();
        //Settings/Info screen
        Image img4 = new Image(Assets.landing_pad);
        img4.setName("img3");
        entrances.addActor(img4,true);
        lt.addActor(entrances);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RngFight.batch.begin();
        lt.draw(RngFight.batch, 1);
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
        int actualY = Gdx.graphics.getHeight() - screenY;

        for(int i = 0; i < 4; i++){
            LCell b = entrances.getLCellForActorName("img" + i);

            if(screenX > b.getX() && screenX < b.getX() + b.getWidth() &&
                    actualY > b.getY() && actualY < b.getY() + b.getHeight()){
                game.switchScreens(i);
                break;
            }
        }






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
}
