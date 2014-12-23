package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import verhelst.Comp.LCoverFlow;
import verhelst.Comp.LLabel;
import verhelst.Comp.LTable;
import verhelst.CustomActors.Character;
import verhelst.CustomActors.SpriteActor;
import verhelst.bones.Model;

/**
 * Created by Orion on 11/11/2014.
 */
public class StatsScreen implements Screen {

    Model m;
    LTable table;
    LTable t;
    LCoverFlow lcf, wCF ;
    final RngFight game2;
    LLabel maxlbl, minlbl, wlrg, maxlvl, battles;
    InputMultiplexer im;
    SpriteActor backBtn, resetButton;

    int reset_state = 0; //0 closed, 1 open, 2 pressed
    long reset_time = -1;
    List<Actor> characterSuitList = new ArrayList<Actor>();
    List<Actor> weaponsImgList;

    public StatsScreen(RngFight game) {
        this.game2 = game;
        characterSuitList = new ArrayList<Actor>();

        Character chara;
        for (int k = 0; k < Assets.faces.length; k++) {
            chara = new Character("Enemy");
            chara.setLevel(10 * k + 1);
            chara.equipSuit();
            characterSuitList.add(chara);
        }


        lcf = new LCoverFlow(characterSuitList, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2, false);

        weaponsImgList = new ArrayList<Actor>();

        for (Sprite s : Assets.getWeaponsList()) {
            Image img = new Image(s);
            // img.setOrigin(img.getWidth()/2, img.getHeight()/2);
            // img.setRotation(45);
            weaponsImgList.add(img);
        }


        wCF = new LCoverFlow(weaponsImgList, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2, true);

        t = new LTable(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        t.addActor(lcf);
        t.addRow();
        t.addActor(wCF);
        t.addRow();
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

        LTable statsTable = new LTable(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2);
        maxlbl = new LLabel(" Max Hits: 0", skin);
        maxlbl.setIsHUD(true);
        statsTable.addActor(maxlbl);
        statsTable.addRow();
        minlbl = new LLabel(" Min Hit: 0", skin);
        minlbl.setIsHUD(true);
        statsTable.addActor(minlbl);
        statsTable.addRow();
        wlrg = new LLabel("   0 wins, 0 losses, 0 draws, 0 games", skin);
        wlrg.setIsHUD(true);
        statsTable.addActor(wlrg);
        statsTable.addRow();
        battles = new LLabel("  0 games ", skin);
        battles.setIsHUD(true);
        statsTable.addActor(battles);
        statsTable.addRow();
        maxlvl = new LLabel(" Max Level Reached: 1", skin);
        maxlvl.setIsHUD(true);
        statsTable.addActor(maxlvl);
        resetButton = new SpriteActor(Assets.reset_closed);
        //   statsTable.addRow();

        //statsTable.addActor(new Image(Assets.back_btn)); //TODO: Back button (need proper graphics)
        t.addActor(statsTable);
        backBtn = new SpriteActor(Assets.back_btn);
        t.addActor(resetButton, true);
        t.getLCellForActor(resetButton).setDraw_position(2);

        //t.addActor(new Label("",skin));
        t.addActor(backBtn, 2);


        im = new InputMultiplexer();
        im.addProcessor(lcf);
        im.addProcessor(wCF);
        im.addProcessor(new InputAdapter() {
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                System.out.println(screenX + " " + (Gdx.graphics.getHeight() - screenY));
                if (screenX > backBtn.getX() && (Gdx.graphics.getHeight() - screenY) < backBtn.getY() + backBtn.getHeight()) {
                    game2.switchScreens(0);
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
        });
        Gdx.input.setInputProcessor(im);
    }

    public void reload(){
        Character chara;
        characterSuitList.clear();
        for (int k = 0; k < Assets.faces.length; k++) {
            chara = new Character("Enemy");
            chara.setLevel(10 * k + 1);
            chara.equipSuit();
            characterSuitList.add(chara);
        }

        weaponsImgList.clear();

        for (Sprite s : Assets.getWeaponsList()) {
            Image img = new Image(s);
            // img.setOrigin(img.getWidth()/2, img.getHeight()/2);
            // img.setRotation(45);
            weaponsImgList.add(img);
        }
       // updateLabels(-1,-1,0,0,0,0,1);
        wCF.setCurrentIndex(0);
        lcf.setCurrentIndex(0);
    }

    public void updateLabels(int maxhits, int minhits, int wins, int losses, int draws, int games, int maxlevel) {
        maxlbl.setText("    Max hits: " + NumberFormat.getNumberInstance(Locale.US).format(maxhits));
        minlbl.setText("    Min hits: " +  NumberFormat.getNumberInstance(Locale.US).format(minhits));
        wlrg.setText("  W/L/D:  " +  NumberFormat.getNumberInstance(Locale.US).format(wins) + "/" +  NumberFormat.getNumberInstance(Locale.US).format(losses) + "/" +  NumberFormat.getNumberInstance(Locale.US).format(draws));
        battles.setText("   Games: " +  NumberFormat.getNumberInstance(Locale.US).format(games));
        maxlvl.setText("    Max Lvl: " + maxlevel);

        //rebuild character list
         reload();
        //rebuild weapon list

    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RngFight.batch.begin();
        if (reset_state == 2 && (reset_time != -1 && System.currentTimeMillis() > reset_time)) {
            reset_state = 0;
            SaveGame.reset();
            resetButton.setDisplaysprite(new Sprite(Assets.reset_closed));
        }
        //  m.render(RngFight.batch);
        t.draw(RngFight.batch, 1);
        // lcf.draw(RngFight.batch, 1);
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
