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

    @Override
    public void create() {
        Assets assets = new Assets();
        batch = new SpriteBatch();

        setScreen(new BattleScreen());
    }

    @Override
    public void render(){
        super.render();
    }
}
