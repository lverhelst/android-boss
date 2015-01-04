package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;

import verhelst.Comp.LTable;
import verhelst.CustomActors.SpriteActor;

/**
 * Created by Orion on 1/4/2015.
 */
public class SettingScreen implements Screen, InputProcessor {

    LTable mainT;

    SpriteActor backBtn, resetButton;
    int reset_state = 0; //0 closed, 1 open, 2 pressed
    long reset_time;

    RngFight game2;

    public SettingScreen(RngFight game){
        this.game2 = game;
        mainT = new LTable(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        resetButton = new SpriteActor(Assets.reset_closed);
        backBtn = new SpriteActor(Assets.back_btn);
        mainT.addActor(resetButton, true);
        mainT.addActor(backBtn, true);
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
        System.out.println(screenX + " " + (Gdx.graphics.getHeight() - screenY));
        if (screenX > backBtn.getX() && (Gdx.graphics.getHeight() - screenY) < backBtn.getY() + backBtn.getHeight()) {
            game2.switchScreens(4);
        }
        if (screenX < resetButton.getX() + resetButton.getWidth() && (Gdx.graphics.getHeight() - screenY) < resetButton.getY() + resetButton.getHeight()) {
            switch (reset_state) {
                case 0:
                    resetButton.setDisplaysprite(new Sprite(Assets.reset_opened));
                    reset_state = 1;
                    break;
                case 1:
                    if ((Gdx.graphics.getHeight() - screenY) < resetButton.getY() + resetButton.getHeight() / 2) {
                        resetButton.setDisplaysprite(new Sprite(Assets.reset_pressed));
                        reset_state = 2;

                        reset_time = System.currentTimeMillis();
                    } else {
                        reset_state = 0;
                        resetButton.setDisplaysprite(new Sprite(Assets.reset_closed));
                    }
                    break;
                case 2:
                    //Do nothing
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RngFight.batch.begin();
        mainT.draw(RngFight.batch, 1);
        if (reset_state == 2 && (reset_time != -1 && System.currentTimeMillis() > reset_time)) {
            reset_state = 0;
            SaveGame.reset();
            resetButton.setDisplaysprite(new Sprite(Assets.reset_closed));
        }
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
