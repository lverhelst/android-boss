package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;

import verhelst.Craft.Inventory;
import verhelst.CustomActors.*;


/**
 * Created by Orion on 10/21/2014.
 */
public class RngFight extends com.badlogic.gdx.Game {

    public static final int VIRTUAL_WIDTH = 400;
    public static final int VITRUAL_HEIGHT = 800;

    private static final int MS_PER_RENDER = 22;
    private long renderstart;


    //Used by all screens
    public static SpriteBatch batch;

    static StatsScreen viewerAndStats;
    static BattleScreen gameScreen;
    static DressingScreen dressingScreen;
    static SettingScreen settingsScreen;
    static MainScreen mainScreen;
    static CraftingScreen craftingScreen;

    public static int lvl = 1;
    private int currentscreen;
    SaveGame sg;

    public static ActionResolver actionResolver;

    private Assets assets;

    verhelst.CustomActors.Character player;

    public RngFight(ActionResolver ar){
        this.actionResolver = ar;
    }


    @Override
    public void create() {
        batch = new SpriteBatch();
        setScreen(new LoadingScreen());
        assets = new Assets();
    }

    //TODO: Put loading of screens when needed here
    //May require loading screen

    public void switchScreens(int screen) {
        switch(screen) {
            case 0:
                gameScreen.updateWeps();
                setScreen(gameScreen);
                Gdx.input.setInputProcessor(gameScreen);
                break;
            case 1:
                if(viewerAndStats == null)
                    viewerAndStats = new StatsScreen(this);
                viewerAndStats.updateLabels(gameScreen.brh.max_hitcount, gameScreen.brh.min_hitcount, gameScreen.brh.player2wins, gameScreen.brh.player2losses, gameScreen.brh.draws, gameScreen.brh.games, gameScreen.brh.max_level_reached, gameScreen.brh.getMax_score());
                setScreen(viewerAndStats);
                Gdx.input.setInputProcessor(viewerAndStats.im);
                break;
            case 2:
                if(dressingScreen == null)
                    dressingScreen = new DressingScreen(this, player);
                dressingScreen.reload();
                setScreen(dressingScreen);
                Gdx.input.setInputProcessor(dressingScreen.getInputProcessor());
                break;
            case 3:
                if(settingsScreen == null)
                    settingsScreen = new SettingScreen(this);
                settingsScreen.setMax_level(gameScreen.brh.max_level_reached);
                setScreen(settingsScreen);
                Gdx.input.setInputProcessor(settingsScreen);
                break;
            case 5:
                mainScreen.updateHighscore(gameScreen.brh.getMax_score(), gameScreen.brh.max_hitcount, gameScreen.brh.max_level_reached);
                setScreen(mainScreen);
                Gdx.input.setInputProcessor(mainScreen);
                break;
            case 4:
                if(craftingScreen == null)
                    craftingScreen = new CraftingScreen(this);
                craftingScreen.refreshChosenItems();
                setScreen(craftingScreen);
                Gdx.input.setInputProcessor(craftingScreen);
                break;

        }
        currentscreen = screen;
    }

    public void reload() {
        try {
            sg.readGameSave();
            Assets.unclocks = SaveGame.unclocks;
            Assets.weaponUnlocks = SaveGame.weapon_unlocks;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }

     //   viewerAndStats = new StatsScreen(this);

        //gameScreen = new BattleScreen(this);
        RngFight.lvl = 1;
        gameScreen.reload();
        if(viewerAndStats == null)
            viewerAndStats = new StatsScreen(this);
        else
            viewerAndStats.reload();

        dressingScreen = new DressingScreen(this, gameScreen.a2);
        Inventory.reset();
        player = gameScreen.a2;
        if(craftingScreen == null)
            craftingScreen = new CraftingScreen(this);
        else
            craftingScreen.reset();
        switchScreens(currentscreen);

    }

    public static void SaveGame(){
        SaveGame.saveGame(gameScreen.a, gameScreen.a2, gameScreen.brh.stats);
    }


    //this is where we will control the render speed

    private void loadShit(){
        final RngFight f = this;
        Gdx.app.postRunnable(new Runnable(){
            @Override
            public void run(){
                try {
                    sg = new SaveGame(f);
                    sg.readGameSave();
                    if(SaveGame.unclocks != null)
                        Assets.unclocks = SaveGame.unclocks;
                    if(SaveGame.weapon_unlocks != null)
                        Assets.weaponUnlocks = SaveGame.weapon_unlocks;
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    e.printStackTrace();
                    Gdx.app.error("Load Game error: ", e.getMessage(), e);
                }
                mainScreen = new MainScreen(f);
                gameScreen = new BattleScreen(f);
                player = gameScreen.a2;
                 switchScreens(5);
            }
        });

    }

    @Override
    public void render() {
        if(assets.managerUpdate() && (assets.state == Assets.STATE.LOADINGTEXTUREATLASES)){
            assets.setSuitsAndWeapons();
            assets.loadOtherAssets();
            System.out.println("loading shit");
        }else if (assets.managerUpdate() && (assets.state == Assets.STATE.LOADINGOTHERTEXTURES)){
            assets.setOtherAssets();
            System.out.println("Settingshit");
        }else if (assets.state == Assets.STATE.ALLDONE){
            System.out.println("Switch screen");
            assets.state = Assets.STATE.DONESKI;
            loadShit();
        }





        renderstart = System.currentTimeMillis();
        super.render();
        if(System.currentTimeMillis() - renderstart < MS_PER_RENDER)
            try {
                Thread.sleep(renderstart + MS_PER_RENDER - System.currentTimeMillis());
            }catch(InterruptedException ie){
                //Do nothing
            }
    }

}
