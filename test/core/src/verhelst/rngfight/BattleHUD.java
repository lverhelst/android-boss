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
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.viewport.ScalingViewport;


/**
 * Created by Leon I. Verhelst on 10/31/2014.
 */
public class BattleHUD {

    HealthBar one, two;
    Stage stage;
    Label highscore, hitcount, oneLbl, twoLbl;
    OrthographicCamera camera;
    Battle b;

    public BattleHUD(Battle b){
        this.b = b;
        camera = new OrthographicCamera(Gdx.graphics.getWidth(),Gdx.graphics.getHeight());

        Skin skin = new Skin(Gdx.files.internal("data/uiskin.json"));


        one = new HealthBar(b.getLeftside());
        one.setHealth_value(10);

        oneLbl = new Label(b.getLeftside().getName() + 200, skin);

        two = new HealthBar(b.getRightside());

        twoLbl = new Label(b.getRightside().getName() + 200, skin);


        FreeTypeFontGenerator ftfg = new FreeTypeFontGenerator(Gdx.files.internal("game_font.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter ftfp = new FreeTypeFontGenerator.FreeTypeFontParameter();
        ftfp.size = Math.round(35 * Gdx.graphics.getDensity() );
        BitmapFont bf = ftfg.generateFont(ftfp);
        highscore = new Label("Win Streak: 0", skin);
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
        stage = new Stage(new ScalingViewport(Scaling.fit, camera.viewportWidth, camera.viewportHeight, camera), RngFight.batch);
        stage.addActor(table);


    }

    public Stage getStage(){
        return stage;
    }

    public OrthographicCamera getCamera(){
        return camera;
    }

    public void update(int hits, int hbval1, int hbval2){
        one.setHealth_value(hbval1);
        oneLbl.setText(b.getLeftside().getName() + ": " + (hbval1 > 0 ? hbval1 : 0));

        two.setHealth_value(hbval2);
        twoLbl.setText(b.getRightside().getName() + ": " + (hbval2 > 0 ? hbval2 : 0));

        highscore.setText("Win Streak: " + b.getRightside().getWin_streak());
        hitcount.setText("Hitcount: " + hits);
    }
}
