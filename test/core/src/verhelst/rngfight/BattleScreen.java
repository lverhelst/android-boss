package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Leon I. Verhelst on 10/30/2014.
 */
public class BattleScreen implements Screen, InputProcessor {

    BattleHUD bhud;
    BattleView bView;
    Random rng;
    ConcurrentLinkedQueue<List<Integer>> bswNumList = new ConcurrentLinkedQueue<List<Integer>>();


    Battle btl;
    BattleResultHandler brh;
    BattleResult[] results;
    //A list of itegers for consuming the posts from the battle
    List<Integer> lst;

    Character a;
    Character a2;
    String message;
    boolean showLoot,battling;
    public static float statetime;
    int hits = 0, anim_h1, anim_h2;



    public BattleScreen(){
        a = new Character("Enemy", Assets.resting_face);
        a2 = new Character("Enemy2", Assets.resting_face);
        anim_h1 = a.getHealth();
        anim_h2 = a2.getHealth();
        message = "";
        btl = new Battle(a,a2,bswNumList);
        brh = new BattleResultHandler();
        results = new BattleResult[0];
        rng = new Random();
        statetime = 0f;

        bhud = new BattleHUD(btl);


        bView = new BattleView(btl, new Weapon(), new Weapon(), new Weapon());

        Gdx.input.setInputProcessor(this);
    }

    private void handleBattleResults(){
        if(!battling && results.length > 0){
            //This can probably be done without a switch, I just don't know how yet.
            for(BattleResult br : results){
                switch(br){
                    case Player1Win:
                        message = "You lost. Too bad.";
                        break;
                    case Player2Win:
                        message = "Victory! " + a.getName() + " defeated!";
                        break;
                    case Tie:
                     message = "D-D-D-Double Kill!";
                        break;
                    case ShowStaticLoot:
                        showLoot = true;
                        Weapon weapon = Weapon.generateRandomWeapon(10, Assets.getWeaponSprite(), Weapon.POSITION.LOOT_POSITION);
                        bView.setLoot(weapon);
                        System.out.println("Showloot");
                        break;
                    case ShowRandomLoot:
                        //renderWeapon(loot = Weapon.generateRandomWeapon(a.getLevel(),assets.getWeaponSprite(), Weapon.POSITION.LOOT_POSITION), (int) w/2, (int) h/4, 0);
                        break;
                    case CustomMode_ioi:
                        //isDmgNumOverridden = true;
                        //dmgNumberOverride = 101;
                        break;
                    case Player1GetsLoot:
                        btl.getLeftside().setEquipped_weapon(brh.getLoot(btl.getLeftside()));
                        break;
                }
            }
            results = new BattleResult[0];
        }
    }

    /*
        Screen Methods
     */

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        statetime += Gdx.graphics.getDeltaTime();
        if(!battling)
            handleBattleResults();

        bhud.update(hits, anim_h1, anim_h2);
        bhud.getStage().draw();


        Weapon aWep = btl.getLeftside().getEquipped_weapon();
        Weapon bWep = btl.getRightside().getEquipped_weapon();

        bView.updateCharacterWeapons(aWep, bWep);
        bView.update(anim_h1, anim_h2, showLoot, brh.playerAmaxLvl);
        bView.renderDamageNumbers(RngFight.batch);
        bView.getStage().draw();
        bView.renderMessage(message, RngFight.batch);
    }

    @Override
    public void resize(int width, int height) {


        //Update Camera
        bhud.getCamera().viewportWidth = width;
        bhud.getCamera().viewportHeight = height;
        bhud.getCamera().update();
        bhud.getStage().getViewport().update(width, height, true);

        bView.getCamera().viewportWidth = width;
        bView.getCamera().viewportHeight = height;
        bView.getCamera().update();
        bView.getStage().getViewport().update(width, height, true);

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

    //

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
        if(showLoot){
            //check to see if touch aligns with displayed loot, if so, equip on player
            Vector2 vec = bView.getStage().screenToStageCoordinates(new Vector2(screenX, screenY));
            Actor tActor = bView.getStage().hit(vec.x, vec.y, true);
            if(tActor != null && tActor.getName() != null) {
                System.out.println(tActor.getName() + " " + bView.lootWep.getName());
                if (tActor.getName().equals(bView.lootWep.getName())) {
                    //Copy the weapon so that we aren't passing the reference
                    Weapon newWep = new Weapon();
                    newWep.copyWeapon(bView.lootWep, Weapon.POSITION.RIGHT_POSITION);
                    btl.getRightside().setEquipped_weapon(newWep);
                }
            }
        }
        if(!battling){
            message = "";
            battling = true;
            showLoot = false;
            //reset fighters (can be put into battle maybe?)
            btl.getRightside().reset();
            btl.getLeftside().reset();
            //Clear Battle-Consuming Queue
            bswNumList.clear();
            //execute the battle();
            btl.run();
            //Create a new thread to consume the battle
            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (battling) {
                        try {
                            //Slow down consuming the battle to AT LEAST visible
                            Thread.currentThread().sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        if (!bswNumList.isEmpty()) {

                            lst = bswNumList.poll();

                            bView.consumeDmgNumPost(lst.get(0), BattleView.DmgListSide.LEFT);
                            bView.consumeDmgNumPost(lst.get(1), BattleView.DmgListSide.RIGHT);
                            anim_h1 = lst.get(2);
                            anim_h2 = lst.get(3);
                            hits = lst.get(4);
                            if (lst.get(2) <= 0 || lst.get(3) <= 0) {
                                btl.getLeftside().setHealth(lst.get(2));
                                btl.getRightside().setHealth(lst.get(3));
                                battling = false;
                                break;
                            }

                        }

                    }
                    results = brh.getResults(btl.getLeftside(),btl.getRightside(),hits);
                }
            }).start();


            return true;
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


