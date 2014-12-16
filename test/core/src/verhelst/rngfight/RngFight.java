package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by Orion on 10/21/2014.
 */
public class RngFight extends com.badlogic.gdx.Game{

    public static final int VIRTUAL_WIDTH = 400;
    public static final int VITRUAL_HEIGHT = 800;
    //Used by all screens
    public static SpriteBatch batch;

    TestScreen viewerAndStats;
    BattleScreen gameScreen;

    //input multiplexor?

    @Override
    public void create() {
        Assets assets = new Assets();
        batch = new SpriteBatch();

        viewerAndStats = new TestScreen(this);
        gameScreen = new BattleScreen(this);

       // setScreen(new BattleScreen());
      //  setScreen(new TestScreen());
        switchScreens(0);
    }


    public void switchScreens(int screen){
        if(screen == 1){
            viewerAndStats.updateLabels(gameScreen.brh.max_hitcount, gameScreen.brh.min_hitcount, gameScreen.brh.player2wins,
                    gameScreen.brh.player2losses, gameScreen.brh.draws, gameScreen.brh.games, gameScreen.brh.max_level_reached);
                    setScreen(viewerAndStats);
            Gdx.input.setInputProcessor(viewerAndStats.im);
        }else{
            setScreen(gameScreen);
            Gdx.input.setInputProcessor(gameScreen);
        }
    }

    @Override
    public void render(){
        super.render();
    }

}
