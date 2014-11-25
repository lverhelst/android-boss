package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Leon I. Verhelst on 10/30/2014.
 */
public class BattleScreen implements Screen, InputProcessor {

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
    boolean showLoot;
    public static boolean battling;
    public static float statetime;
    int hits = 0, anim_h1, anim_h2;

    Actor dragme;



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



        bView = new BattleView(btl);

        Weapon aWep = btl.getLeftside().getEquipped_weapon();
        Weapon bWep = btl.getRightside().getEquipped_weapon();

        bView.updateCharacterWeapons(aWep, bWep);

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
                        Weapon weapon = Weapon.generateRandomWeapon(a.max_level, Assets.getWeaponSprite(), Weapon.POSITION.LOOT_POSITION);
                        bView.setLoot(weapon);

                       // System.out.println("Showloot");
                        break;
                    case ShowRandomLoot:
                        //renderWeapon(loot = Weapon.generateRandomWeapon(a.getLevel(),assets.getWeaponSprite(), Weapon.POSITION.LOOT_POSITION), (int) w/2, (int) h/4, 0);
                        break;
                    case CustomMode_ioi:
                        //isDmgNumOverridden = true;
                        //dmgNumberOverride = 101;
                        break;
                    case Player1GetsLoot:
                        btl.getLeftside().setEquipped_weapon(Weapon.generateScaledWeapon(a.getLevel(), Assets.getWeaponSprite(), Weapon.POSITION.LEFT_POSITION));
                        Weapon aWep = btl.getLeftside().getEquipped_weapon();
                        Weapon bWep = btl.getRightside().getEquipped_weapon();

                        bView.updateCharacterWeapons(aWep, bWep);

                        break;
                    case HeadLoot:
                        showLoot = true;

                        int types= BodyPartActor.BodyPartType.values().length;

                        BodyPartActor hsa = new BodyPartActor(BodyPartActor.BodyPartType.values()[rng.nextInt(types)]);
                        bView.setLoot(hsa);
                        break;
                    case Player1NewSuit:
                        btl.getLeftside().equipSuit();
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



        bView.update(anim_h1, anim_h2, showLoot, a.max_level, message, hits);



        bView.getStage().draw();

    }

    @Override
    public void resize(int width, int height) {

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
        if(showLoot){
            //check to see if touch aligns with displayed loot, if so, equip on player
            Vector2 vec = bView.getStage().screenToStageCoordinates(new Vector2(screenX, screenY));
            Actor tActor = bView.getStage().hit(vec.x, vec.y, true);



            System.out.println("Touch down before actor check");
            //Get width * 2 accounts for the extra text in the weapon actor
            if(vec.x >= bView.lootActor.getX() && vec.x <= bView.lootActor.getX() + bView.lootActor.getWidth() * 2
                      && vec.y >= bView.lootActor.getY() && vec.y <= bView.lootActor.getY() + bView.lootActor.getHeight()) {
                    //Copy the weapon so that we aren't passing the reference
                    System.out.println("Touch down in loot actor");

                    if(bView.lootActor instanceof Weapon) {
                        dragme = new Weapon();

                        ((Weapon)dragme).copyWeapon((Weapon) bView.lootActor, Weapon.POSITION.RIGHT_POSITION);
                        dragme = ((Weapon) dragme).getTable();


                        dragme.setSize(((Weapon) bView.lootActor).getWidth()/2, ((Weapon) bView.lootActor).getHeight()/2);
                        System.out.println(((Weapon) bView.lootActor).getMax_damage() + " " +  ((Weapon) bView.lootActor).dragy);
                    }
                    if(bView.lootActor instanceof BodyPartActor){
                        dragme = new BodyPartActor();
                        ((BodyPartActor)dragme).copyHSA((BodyPartActor)bView.lootActor);
                        dragme.setSize(bView.lootActor.getWidth()/2, bView.lootActor.getHeight()/2);
                    }

                    dragme.setPosition(vec.x + dragme.getX() - dragme.getWidth()/2, vec.y + dragme.getY() - dragme.getHeight()/2);
                    bView.getStage().addActor(dragme);

                }
            }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        btl.getRightside().setGlow(false);
        if(showLoot){

            if(dragme != null) {
                float rectax1 = btl.getRightside().getX();
                float rectbx2 = dragme.getX() + dragme.getWidth();

                float rectax2 = btl.getRightside().getX() + btl.getRightside().getWidth();
                float rectbx1 = dragme.getX();

                float rectay1 = btl.getRightside().otherY;
                float rectby2 = dragme.getY() + dragme.getHeight();

                float rectay2 = btl.getRightside().otherY + btl.getRightside().getHeight();
                float rectby1 = dragme.getY();

                if (rectax1 <= rectbx2 && rectax2 >= rectbx1
                        && rectay1 <= rectby2 && rectay2 >= rectby1) {
                    System.out.println("DRAGME OVERLAPS CHARACHTER RIGHT");
                    //Copy the weapon so that we aren't passing the reference
                    if(bView.lootActor instanceof Weapon) {
                        Weapon newWep = new Weapon();
                        newWep.copyWeapon((Weapon)bView.lootActor, Weapon.POSITION.RIGHT_POSITION);
                        btl.getRightside().setEquipped_weapon(newWep);
                        Weapon aWep = btl.getLeftside().getEquipped_weapon();
                        Weapon bWep = btl.getRightside().getEquipped_weapon();

                        bView.updateCharacterWeapons(aWep, bWep);

                        System.out.println("QUEIROASDAS");
                    }
                    if(bView.lootActor instanceof BodyPartActor){
                        System.out.println("EQUIP HEAD HERE PLEASE");
                        btl.getRightside().setBodyPart((BodyPartActor) bView.lootActor);

                    }
                }
                System.out.println("Ax1 " + rectax1 + " x2 " + rectax2 + " y1 " + rectay1 + " y2  " + rectay2);

                System.out.println("Bx1 " + rectbx1 + " x2 " + rectbx2 + " y1 " + rectby1 + " y2  " + rectby2);
            }
        }
        if(dragme != null)
             dragme.remove();
        dragme = null;
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

                            btl.getLeftside().consumeDmgNumPost(lst.get(0), Character.DmgListSide.LEFT);
                            btl.getRightside().consumeDmgNumPost(lst.get(1), Character.DmgListSide.RIGHT);
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


      //  System.out.println(screenX + " h:" + screenY +  " dragme not null? " + (dragme != null));
        if(dragme != null) {

            dragme.setPosition(screenX - dragme.getWidth()/2, Gdx.graphics.getHeight() - screenY - dragme.getHeight()/2);

            float rectax1 = btl.getRightside().getX();

            float rectbx2 = dragme.getX() + dragme.getWidth();

            float rectax2 = btl.getRightside().getX() + btl.getRightside().getWidth();
            float rectbx1 = dragme.getX();

            float rectay1 = btl.getRightside().otherY;
            float rectby2 = dragme.getY() + dragme.getHeight();

            float rectay2 = btl.getRightside().otherY + btl.getRightside().getHeight();
            float rectby1 = dragme.getY();


            if (rectax1 <= rectbx2 && rectax2 >= rectbx1
                    && rectay1 <= rectby2 && rectay2 >= rectby1) {


                btl.getRightside().setGlow(true);
            }else{
                btl.getRightside().setGlow(false);
            }

        }
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


