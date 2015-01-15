package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Locale;
import java.util.Random;

import verhelst.Comp.LCell;
import verhelst.Comp.LLabel;
import verhelst.Comp.LTable;
import verhelst.CustomActors.DamageNumber;
import verhelst.CustomActors.SpriteActor;

/**
 * Created by Leon I. Verhelst on 1/3/2015.
 */
public class MainScreen implements Screen, InputProcessor {

    LTable mt, st, lt, entrances;
    LLabel highscore;
    Image butterbeaver;
    RngFight game;

    ArrayList<DamageNumber> stringlist = new ArrayList<DamageNumber>();
    Iterator<DamageNumber> i;

    String[] thingsToSay = { "ACHIEVMENT UNLOCKED!","QUIKFITE", "QuikFite", "quikfite", "ioi", "get a job", "you lose", "you win", "yes", "no", "That's ridiculous", "Well I say!", "Uplifting Message!", "CHEN SMASH", ",", "i++", "Subliminal Suggestions!",
        "Namtab: MY PARENT'S ARE ALIVE!", "Namtab: I'll fight your tax return!", "The fire rises", "Looking good!", "Please like and subscribe!",
        "It really helps!", "It puts the lotion on the skin", "MATH.PI", "LIBGDX", "Wowee", "Call me: 1-800-not a real number.com", "Get rekt", "Tyranasauras Rekt",
        "Heeey there", "Butterbeaver", "Pee gee!", "Android", "Fighting Game", "P ZEN Q", "P -> Q"};

    Image achieve, leaders, submitscore;
    SpriteActor signin;

    int skore, maxhits, maxlvl;



    public MainScreen(RngFight fight){
        this.game = fight;
        mt = new LTable(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        lt = new LTable(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        st = new LTable(0,0,Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
        butterbeaver = new Image(Assets.butterBeaver);
        lt.addActor(butterbeaver, true);
        lt.addRow();


        highscore = new LLabel("Highscore", RngFight.skin);
        highscore.setIsHUD(true);


        lt.addActor(highscore);
        lt.addRow();
        st.addRow();
        st.addActor(new Image(Assets.game_controller), true);
        submitscore = new Image(Assets.submitscore);
        submitscore.setVisible(false);
        st.addActor(submitscore);
        st.addRow();
        //TODO: Make into SIGN IN, ACHIEVEMENTS, LEADERBOARD buttons
        signin = new SpriteActor(Assets.signin);
        st.addActor(signin,true);
        achieve = new Image(Assets.game_achieve);
        achieve.setVisible(false);
        st.addActor(achieve, true);
        leaders = new Image(Assets.game_leader);
        leaders.setVisible(false);
        st.addActor(leaders,true);

        lt.addActor(st);


        entrances = new LTable(Gdx.graphics.getWidth()/2, 0, Gdx.graphics.getWidth()/2, Gdx.graphics.getHeight());
        //Fight screen
        Image img0 = new Image(Assets.FITE_SIGN);
        img0.setName("img0");
        entrances.addActor(img0,true);
        entrances.addRow();
        //Stats Screen

        Image img1 = new Image(Assets.STATS);
        img1.setName("img1");
        entrances.addActor(img1,true);
        entrances.addRow();
        //Dressing Room Screen

        Image img3 = new Image(Assets.DRES);
        img3.setName("img2");
        entrances.addActor(img3, true);
        entrances.addRow();
        //Settings/Info screen
        Image img4 = new Image(Assets.INDEV);
        img4.setName("img3");
        entrances.addActor(img4,true);


        mt.addActor(lt);
        mt.addActor(entrances);
    }

    public void updateHighscore(int score, int maxhits, int maxlvl){
        skore = score;
        this.maxhits = maxhits;
        this.maxlvl = maxlvl;
        highscore.setText("Highscore: " + NumberFormat.getNumberInstance(Locale.US).format(score));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if(RngFight.actionResolver.isSignedIn()) {
            leaders.setVisible(true);
            achieve.setVisible(true);
            submitscore.setVisible(true);
            signin.setDisplaysprite(Assets.signout);
        }else{
            leaders.setVisible(false);
            achieve.setVisible(false);
            submitscore.setVisible(false);
            signin.setDisplaysprite(Assets.signin);
        }
        if(RngFight.actionResolver.isSigningIn()){
            signin.setVisible(false);
        }else{
            signin.setVisible(true);
        }


        RngFight.batch.begin();
        renderDamageNumbers(RngFight.batch);
        mt.draw(RngFight.batch, 1);

        RngFight.batch.end();
    }

    public void renderDamageNumbers(Batch batch) {

        //Add Damage Numbers to screen
        synchronized (stringlist) {

            for (i = stringlist.iterator(); i.hasNext(); ) {

                DamageNumber dn = i.next();
                if (dn.isRemoveable()) {
                    i.remove();
                } else {
                    Assets.wepNumFnt.setColor(dn.getRed(), dn.getGreen(), dn.getBlue(), dn.getAlpha());
                    Assets.wepNumFnt.draw(batch, dn.getCs(), dn.getX(), dn.getY());
                    dn.update();
                }
            }
        }

    }


    //Adds a new damage number to the appropriate damage list
    private void addDmgNum(String num, int x, int y) {
        DamageNumber dn = new DamageNumber(num, x, y);
        synchronized (stringlist) {
            stringlist.add(dn);
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


        if(screenX > submitscore.getX() && screenX <  submitscore.getX() +  submitscore.getWidth() &&
                actualY > submitscore.getY() && actualY < submitscore.getY() + submitscore.getHeight()) {
            if(RngFight.actionResolver.isSignedIn()){
                RngFight.actionResolver.submitHighScore(skore, maxhits, maxlvl);
            }
        }

        if(screenX < Gdx.graphics.getWidth()/2){
            if(actualY > signin.getY() && actualY < signin.getY() + signin.getHeight()) {
                if (screenX > signin.getX() && screenX < signin.getX() + signin.getWidth()) {
                    if (RngFight.actionResolver.isSignedIn()) {
                        leaders.setVisible(false);
                        achieve.setVisible(false);
                        submitscore.setVisible(false);
                        signin.setDisplaysprite(Assets.signin);
                        RngFight.actionResolver.signOut();
                    } else {
                        RngFight.actionResolver.signIn();
                    }
                }else if(screenX > achieve.getX() && screenX < achieve.getX() + achieve.getWidth()){
                    if (RngFight.actionResolver.isSignedIn()) {
                        RngFight.actionResolver.showAchievements();
                    }
                }else if(screenX > leaders.getX() && screenX < leaders.getX() + leaders.getWidth()){
                    if (RngFight.actionResolver.isSignedIn()) {
                        RngFight.actionResolver.showLeaderBoard();
                    }
                }
            }else{
                int index = new Random().nextInt(thingsToSay.length);
                if(index == 0)
                    RngFight.actionResolver.unlockAchievement("achievement_unlocked");
                addDmgNum(thingsToSay[index], screenX, actualY);
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
