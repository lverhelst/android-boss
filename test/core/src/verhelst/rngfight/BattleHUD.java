package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.VerticalGroup;
import com.badlogic.gdx.scenes.scene2d.utils.Align;


/**
 * Created by Leon I. Verhelst on 10/31/2014.
 */
public class BattleHUD {

    HealthBar one, two;
    Stage stage;
    Label highscore, hitcount, oneLbl, twoLbl;
    OrthographicCamera camera;


    public BattleHUD(){

        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));


        one = new HealthBar(200, "Temp1");
        one.setHealth_value(10);

        oneLbl = new Label("Temp1: " + 200, skin);

        two = new HealthBar(200, "Temp2");

        twoLbl = new Label("Temp2: " + 200, skin);


        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("game_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(35 * Gdx.graphics.getDensity() );
        BitmapFont bf = ftfg.generateFont(ftfp);
        highscore = new Label("HighScore: 1", skin);
        hitcount = new Label("HitCount: 0",skin);

        Table table = new Table();
        table.add(one).align(Align.left);
        table.row();
        table.add(oneLbl).align(Align.left);
        table.row();
        table.add(two).align(Align.left);
        table.row();
        table.add(twoLbl).align(Align.left);
        table.row();
        //new row to separate top & bottom
        table.row().expand();
        table.add(new Label("", skin));
        table.row();
        table.add(highscore).align(Align.left);
        table.row();
        table.add(hitcount).align(Align.left);
        table.setDebug(true);
        table.setFillParent(true);
        stage = new Stage();
        stage.addActor(table);


    }

    public Stage getStage(){
        return stage;
    }


}
