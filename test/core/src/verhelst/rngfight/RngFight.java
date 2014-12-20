package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by Orion on 10/21/2014.
 */
public class RngFight extends com.badlogic.gdx.Game {

    public static final int VIRTUAL_WIDTH = 400;
    public static final int VITRUAL_HEIGHT = 800;
    //Used by all screens
    public static SpriteBatch batch;

    static StatsScreen viewerAndStats;
    static BattleScreen gameScreen;

    private int currentscreen;
    SaveGame sg;
    //input multiplexor?

    @Override
    public void create() {
        Assets assets = new Assets();
        assets.loadAssets();
        try {
            sg = new SaveGame(this);
            sg.readGameSave();

            Assets.unclocks = SaveGame.unclocks;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            Gdx.app.error("Load Game error: ", e.getMessage(), e);

        }
        batch = new SpriteBatch();
        gameScreen = new BattleScreen(this);
        viewerAndStats = new StatsScreen(this);
        switchScreens(0);
    }


    public void switchScreens(int screen) {
        if (screen == 1) {

           // viewerAndStats = new StatsScreen(this);
            viewerAndStats.updateLabels(gameScreen.brh.max_hitcount, gameScreen.brh.min_hitcount, gameScreen.brh.player2wins, gameScreen.brh.player2losses, gameScreen.brh.draws, gameScreen.brh.games, gameScreen.brh.max_level_reached);

            setScreen(viewerAndStats);
            Gdx.input.setInputProcessor(viewerAndStats.im);
        } else {
            setScreen(gameScreen);
            Gdx.input.setInputProcessor(gameScreen);
        }
        currentscreen = screen;
    }

    public void reload() {
        try {
            sg.readGameSave();
            Assets.unclocks = SaveGame.unclocks;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

     //   viewerAndStats = new StatsScreen(this);

        //gameScreen = new BattleScreen(this);
        gameScreen.reload();
        viewerAndStats.reload();

        switchScreens(currentscreen);

    }

    @Override
    public void render() {
        super.render();
    }

}
