package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;


/**
 * Created by Orion on 10/21/2014.
 */
public class RngFight extends com.badlogic.gdx.Game{

    public static final int VIRTUAL_WIDTH = 400;
    public static final int VITRUAL_HEIGHT = 400;
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
     //   gameScreen = new BattleScreen(this);

       // setScreen(new BattleScreen());
      //  setScreen(new TestScreen());
        switchScreens(1);
    }


    public void switchScreens(int screen){
        if(screen == 1){
            setScreen(viewerAndStats);

        }else{
            setScreen(gameScreen);
        }
    }

    @Override
    public void render(){
        super.render();
    }

}
