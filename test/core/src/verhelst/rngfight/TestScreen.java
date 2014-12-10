package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Gdx2DPixmap;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

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
    LTable t;
    LCoverFlow lcf;
    final RngFight game2;
    LeonLabel maxlbl, minlbl, wlrg, maxlvl, battles;
    InputMultiplexer im;


    public TestScreen(RngFight game){
        this.game2 = game;
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

            Image img = new Image(s);
            img.setRotation(90);
            table.addActor(img,true);
            i++;
        }





        List<Actor> actor = new ArrayList<Actor>();
        Character chara;
        for(int k = 0; k < Assets.faces.length; k++){
            chara = new Character("Enemy", Assets.getHeadSprite());
            chara.setLevel(10 * k + 1);
            chara.equipSuit();
            actor.add(chara);
        }


       lcf = new LCoverFlow(actor, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2,false);

       List<Actor> las = new ArrayList<Actor>();

        for(Sprite s: Assets.weapons_sprites){

            Image img = new Image(s);
           // img.setOrigin(img.getWidth()/2, img.getHeight()/2);
           // img.setRotation(45);
            las.add(img);
            i++;
        }


       LCoverFlow wCF = new LCoverFlow(las, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2, true);

       t = new LTable(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
       t.addActor(lcf);
       t.addRow();
       t.addActor(wCF);
       t.addRow();
        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));

       LTable statsTable = new LTable(0,0,Gdx.graphics.getWidth(), Gdx.graphics.getHeight()/2);
       maxlbl = new LeonLabel(" Max Hits: 0", skin);
       maxlbl.isHUD = true;
       statsTable.addActor(maxlbl);
       statsTable.addRow();
       minlbl = new LeonLabel(" Min Hit: 0", skin);
       minlbl.isHUD = true;
       statsTable.addActor(minlbl);
       statsTable.addRow();
       wlrg = new LeonLabel("   0 wins, 0 losses, 0 draws, 0 games", skin);
       wlrg.isHUD = true;
       statsTable.addActor(wlrg);
       statsTable.addRow();
       battles = new LeonLabel("  0 games ", skin);
       battles.isHUD = true;
       statsTable.addActor(battles);
       statsTable.addRow();
       maxlvl = new LeonLabel(" Max Level Reached: 1", skin);
       maxlvl.isHUD = true;
       statsTable.addActor(maxlvl);
       statsTable.addActor(new Image(Assets.weapon_data_icon), true); //TODO: Back button
       t.addActor(statsTable);



        im = new InputMultiplexer();
        im.addProcessor(lcf);
        im.addProcessor(wCF);
        im.addProcessor(new InputAdapter(){
            @Override
            public boolean touchUp(int screenX, int screenY, int pointer, int button) {
                System.out.println(screenX + " " + (Gdx.graphics.getHeight() - screenY));
                if(screenX > 3 * Gdx.graphics.getWidth()/4 && (Gdx.graphics.getHeight() - screenY) < Gdx.graphics.getHeight()/4){
                    game2.switchScreens(0);
                }
                return false;
            }
        });
        Gdx.input.setInputProcessor(im);
    }

    public void updateLabels(int maxhits, int minhits, int wins, int losses, int draws, int games, int maxlevel){
        maxlbl.setText("    Max hits: " + maxhits);
        minlbl.setText("    Min hits: " + minhits);
        wlrg.setText("  Wins/Losses/Draws:  " +  wins + "/" +  losses +  "/" + draws);
        battles.setText("   Games: " + games );
        maxlvl.setText("    Highest level reached: " + maxlevel);
    }


    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RngFight.batch.begin();
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
