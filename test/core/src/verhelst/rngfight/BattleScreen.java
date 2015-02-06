package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import verhelst.Comp.LCell;
import verhelst.CustomActors.BodyPartActor;
import verhelst.CustomActors.Character;
import verhelst.CustomActors.DamageNumber;
import verhelst.CustomActors.SpriteActor;

/**
 * Created by Leon I. Verhelst on 10/30/2014.
 */
public class BattleScreen implements Screen, InputProcessor {

    BattleView bView;
    Random rng;
    ConcurrentLinkedQueue<int[]> bswNumList = new ConcurrentLinkedQueue<int[]>();


    Battle btl;
    BattleResultHandler brh;
    BattleResult[] results;
    //A list of itegers for consuming the posts from the battle
    int[] lst;

    verhelst.CustomActors.Character a;
    Character a2;
    String message;
    boolean showLoot;
    public static boolean battling, interrupted;
    public static float statetime;
    int hits = 0, score = 0, anim_h1, anim_h2;

    int display_cap = Integer.MAX_VALUE;
    Actor dragme, butterDragMe;

    ArrayList<DamageNumber> soloMessage = new ArrayList<DamageNumber>();
    Iterator<DamageNumber> it;

    boolean custom_mode_on;
    String custom_mode_string;

    RngFight fight;

    public BattleScreen(RngFight rf) {
        this.fight = rf;
        a = SaveGame.getLeftside();
        a2 = SaveGame.getRightside();


        anim_h1 = a.getHealth();
        anim_h2 = a2.getHealth();
        message = "";

        brh = new BattleResultHandler();
        results = new BattleResult[0];
        rng = new Random();
        statetime = 0f;
        btl = new Battle(a, a2, bswNumList);
        bView = new BattleView(btl);

        Weapon aWep = btl.getLeftside().getEquipped_weapon();
        Weapon bWep = btl.getRightside().getEquipped_weapon();

        bView.updateCharacterWeapons(aWep, bWep);

        //Gdx.input.setInputProcessor(this);
    }

    public void reload(){
         bswNumList.clear();
         battling = false;
         interrupted = true;
         showLoot = false;
         a = SaveGame.getLeftside();
         a2 = SaveGame.getRightside();
         a.reload();
         a2.reload();
         anim_h1 = a.getHealth();
         anim_h2 = a.getHealth();
         message = "";

         showLoot = false;
         hits = 0;
         score = 0;

         Weapon aWep = btl.getLeftside().getEquipped_weapon();
         Weapon bWep = btl.getRightside().getEquipped_weapon();

         bView.updateCharacterWeapons(aWep, bWep);

         brh.reset();
         bView.update(anim_h1, anim_h2, showLoot, 0, message, hits, 0);
        btl = new Battle(a, a2, bswNumList);
        bView = new BattleView(btl);
    }


    private void handleBattleResults() {
        custom_mode_on = false;
        custom_mode_string = "";
        if (!battling && results.length > 0) {
            //This can probably be done without a switch, I just don't know how yet.
            for (BattleResult br : results) {
                switch (br) {
                    case NewHighScore:
                        soloMessage.add(new DamageNumber("NEW HIGHSCORE!", Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight() * 3/4 ));
                        break;
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
                        Weapon weapon = Weapon.generateRandomWeapon(a.getMax_level(), Weapon.POSITION.LOOT_POSITION);

                        Assets.unlockItem(-1, weapon.spriteindex);
                        bView.setLoot(weapon);
                        soloMessage.add(new DamageNumber("LOOT!", Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight() * 3/4 ));
                        // System.out.println("Showloot");
                        break;
                    case ShowRandomLoot:
                        //renderWeapon(loot = Weapon.generateRandomWeapon(a.getLevel(),assets.getWeaponSprite(), Weapon.POSITION.LOOT_POSITION), (int) w/2, (int) h/4, 0);
                        break;
                    case CustomMode_ioi:
                        custom_mode_on = true;
                        custom_mode_string = "ioi";
                        break;
                    case Player1GetsLoot:
                        btl.getLeftside().setEquipped_weapon(Weapon.generateScaledWeapon(a.getLevel(), Weapon.POSITION.LEFT_POSITION));
                        Weapon aWep = btl.getLeftside().getEquipped_weapon();
                        Weapon bWep = btl.getRightside().getEquipped_weapon();

                        bView.updateCharacterWeapons(aWep, bWep);


                        break;
                    case HeadLoot:
                        showLoot = true;

                        int types = BodyPartActor.BodyPartType.values().length;

                        int loooooot = rng.nextInt(types);


                        BodyPartActor hsa = new BodyPartActor(BodyPartActor.BodyPartType.values()[loooooot], btl.getLeftside().getMax_level());
                        Assets.unlockItem(loooooot, hsa.part_index);
                        bView.setLoot(hsa);
                        soloMessage.add(new DamageNumber("LOOT!", Gdx.graphics.getWidth()/3, Gdx.graphics.getHeight() * 3/4 ));

                        break;
                    case Player1NewSuit:
                        btl.getLeftside().equipSuitNoCheck();
                        break;
                }
            }
            results = new BattleResult[0];
            SaveGame.saveGame(a, a2, brh.stats);
        }
    }

    /*
        Screen Methods
     */

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        if (!battling)
            handleBattleResults();
        bView.update(anim_h1, anim_h2, showLoot, brh.getMax_score(), message, hits, score);
        bView.getStage().draw();
        renderDamageNumbers(RngFight.batch);

    }

    public void renderDamageNumbers(Batch batch) {

        //Add Damage Numbers to screen
        synchronized (soloMessage) {

            for (it = soloMessage.iterator(); it.hasNext(); ) {

                DamageNumber dn = it.next();
                if (dn.isRemoveable()) {
                    it.remove();
                } else {
                    batch.begin();
                    Assets.wepNumFnt.setColor(dn.getRed(), dn.getGreen(), dn.getBlue(), dn.getAlpha());
                    Assets.wepNumFnt.draw(batch, dn.getCs(), dn.getX(), dn.getY());
                    batch.end();
                    dn.update();
                }
            }
        }

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
        Vector2 vec = bView.getStage().screenToStageCoordinates(new Vector2(screenX, screenY));

        if (showLoot) {
            //check to see if touch aligns with displayed loot, if so, equip on player

            System.out.println("Touch down before actor check");
            //Get width * 2 accounts for the extra text in the weapon actor
            LCell lCell = bView.getCellForLoot();
            if (vec.x >= lCell.getX() && vec.x <= lCell.getX() + lCell.getWidth()
                    && vec.y >= lCell.getY() && vec.y <= lCell.getY() + lCell.getHeight()) {
                //Copy the weapon so that we aren't passing the reference
                System.out.println("Touch down in loot actor");

                if (bView.lootActor instanceof Weapon) {
                    dragme = new Weapon();

                    ((Weapon) dragme).copyWeapon((Weapon) bView.lootActor, Weapon.POSITION.RIGHT_POSITION);
                    dragme = ((Weapon) dragme).getTable();


                    dragme.setSize(((Weapon) bView.lootActor).getWidth() / 2, ((Weapon) bView.lootActor).getHeight() / 2);
                    System.out.println(((Weapon) bView.lootActor).getMax_damage() + " " + ((Weapon) bView.lootActor).dragy);
                }
                if (bView.lootActor instanceof BodyPartActor) {
                    dragme = new BodyPartActor();
                    ((BodyPartActor) dragme).copyHSA((BodyPartActor) bView.lootActor);
                    dragme.setSize(bView.lootActor.getWidth() / 2, bView.lootActor.getHeight() / 2);

                }

                dragme.setPosition(vec.x + dragme.getX() - dragme.getWidth() / 2, vec.y + dragme.getY() - dragme.getHeight() / 2);
                bView.getStage().addActor(dragme);

            }
        }
        if (vec.x >= bView.butterbeaver.getX() && vec.x <= bView.butterbeaver.getX() + bView.butterbeaver.getWidth()
                && vec.y >= bView.butterbeaver.getY() && vec.y <= bView.butterbeaver.getY() + bView.butterbeaver.getHeight()) {
            butterDragMe = new SpriteActor(Assets.butterBeaver);
            butterDragMe.setSize(bView.butterbeaver.getWidth(), bView.butterbeaver.getHeight());
            butterDragMe.setPosition(vec.x + butterDragMe.getX() - butterDragMe.getWidth() / 2, vec.y + butterDragMe.getY() - butterDragMe.getHeight() / 2);
            bView.getStage().addActor(butterDragMe);
            bView.butterbeaver.setVisible(false);
        }


        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        bView.landingPadGlow(false);
        boolean canDecompose = showLoot;
        if (butterDragMe != null) {

            float rectax11 = bView.landingpad.getX();

            float rectbx21 = butterDragMe.getX() + butterDragMe.getWidth();

            float rectax21 = bView.landingpad.getX() + bView.landingpad.getWidth();
            float rectbx11 = butterDragMe.getX();

            float rectay11 = bView.landingpad.getY();
            float rectby21 = butterDragMe.getY() + butterDragMe.getHeight();

            float rectay21 = bView.landingpad.getY() + bView.landingpad.getHeight();
            float rectby11 = butterDragMe.getY();
            if (rectax11 <= rectbx21 && rectax21 >= rectbx11
                    && rectay11 <= rectby21 && rectay21 >= rectby11) {
                fight.switchScreens(5);
            }
            if (butterDragMe != null)
                butterDragMe.remove();
            butterDragMe = null;
            bView.butterbeaver.setVisible(true);
            return false;

        }
        if (butterDragMe != null)
            butterDragMe.remove();
        butterDragMe = null;
        bView.butterbeaver.setVisible(true);
        btl.getRightside().setGlow(false);
        bView.setPlayerWeaponGlow(false, 0);
        if (showLoot) {

            if (dragme != null) {
                float rectax1 = btl.getRightside().getX();
                float rectbx2 = dragme.getX() + dragme.getWidth();

                float rectax2 = btl.getRightside().getX() + btl.getRightside().getWidth();
                float rectbx1 = dragme.getX();

                float rectay1 = btl.getRightside().otherY;
                float rectby2 = dragme.getY() + dragme.getHeight();

                float rectay2 = btl.getRightside().otherY + btl.getRightside().getHeight();
                float rectby1 = dragme.getY();


                if(bView.lootActor instanceof Weapon) {
                    float rectcx1 = bView.bWep.getX();
                    float rectcx2 = bView.bWep.getX() + btl.getRightside().getWidth();
                    float rectcy1 = bView.bWep.getY() + bView.bWep.getHeight()/2;
                    float rectcy2 = bView.bWep.getY() + bView.bWep.getHeight();

                    if ((rectcx1 <= rectbx2 && rectcx2 >= rectbx1
                            && rectcy1 <= rectby2 && rectcy2 >= rectby1)|| (rectax1 <= rectbx2 && rectax2 >= rectbx1
                            && rectay1 <= rectby2 && rectay2 >= rectby1)) {
                        //Overlapping weapon.
                        Weapon newWep = new Weapon();
                        newWep.copyWeapon((Weapon) bView.lootActor, Weapon.POSITION.RIGHT_POSITION);
                        btl.getRightside().setEquipped_weapon(newWep);
                        Weapon aWep = btl.getLeftside().getEquipped_weapon();
                        Weapon bWep = btl.getRightside().getEquipped_weapon();

                        bView.updateCharacterWeapons(aWep, bWep);
                    }
                    canDecompose = false;
                }
                else
                if (rectax1 <= rectbx2 && rectax2 >= rectbx1
                            && rectay1 <= rectby2 && rectay2 >= rectby1) {

                    if (bView.lootActor instanceof BodyPartActor) {

                        btl.getRightside().setBodyPart((BodyPartActor) bView.lootActor);
                    }
                    canDecompose = false;
                }





            }
        }


        if(canDecompose){
            if (bView.lootActor instanceof Weapon) {
                soloMessage.add(new DamageNumber(((Weapon)bView.lootActor).decompose(), (int)(bView.lootActor.getX() + bView.lootActor.getWidth()/2), (int)( bView.lootActor.getHeight() + bView.lootActor.getY()) ));
            }
            if (bView.lootActor instanceof BodyPartActor) {
                soloMessage.add(new DamageNumber(((BodyPartActor)bView.lootActor).decompose(),  (int)(bView.lootActor.getX() + bView.lootActor.getWidth()/2), (int)( bView.lootActor.getHeight() + bView.lootActor.getY()) ));
            }
        }
        if (dragme != null)
            dragme.remove();
        dragme = null;

        if (!battling) {
            message = "";
            battling = true;
            showLoot = false;

            //execute the battle
            new Thread(btl).start();
            //Create a new thread to consume the battle
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //Slow down consuming the battle to AT LEAST visible
                        Thread.currentThread().sleep(10);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    while (battling) {
                        try {
                            //Slow down consuming the battle to AT LEAST visible
                            Thread.currentThread().sleep(1);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                     //   if (!bswNumList.isEmpty()) {

                            lst = bswNumList.poll();
                            if(lst != null) {

                                btl.getLeftside().consumeDmgNumPost((custom_mode_on ? custom_mode_string : "" + lst[0]), Character.DmgListSide.LEFT);
                                btl.getRightside().consumeDmgNumPost((custom_mode_on ? custom_mode_string : "" + lst[1]), Character.DmgListSide.RIGHT);

                                anim_h1 = lst[2];
                                anim_h2 = lst[3];
                                hits = lst[4];
                            }
                            score = brh.getScore(hits, btl.getLeftside());

                            if (bswNumList.isEmpty()) {
                                if(lst != null) {
                                    btl.getLeftside().setDisplay_hp(lst[2]);
                                    btl.getRightside().setDisplay_hp(lst[3]);
                                    anim_h1 = lst[2];
                                    anim_h2 = lst[3];
                                    hits = lst[4];
                                }
                                battling = false;
                                break;
                            }

                       // }

                    }
                    if(!interrupted)
                        results = brh.getResults(btl.getLeftside(), btl.getRightside(), hits);
                    return;
                }
            }).start();


            //return true;
        }
        if(!interrupted)
            SaveGame.saveGame(a, a2, brh.stats);
        interrupted = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {


        //  System.out.println(screenX + " h:" + screenY +  " dragme not null? " + (dragme != null));
        if (dragme != null) {

            dragme.setPosition(screenX - dragme.getWidth() / 2, Gdx.graphics.getHeight() - screenY - dragme.getHeight() / 2);

            float rectax1 = btl.getRightside().getX();

            float rectbx2 = dragme.getX() + dragme.getWidth();

            float rectax2 = btl.getRightside().getX() + btl.getRightside().getWidth();
            float rectbx1 = dragme.getX();

            float rectay1 = btl.getRightside().otherY;
            float rectby2 = dragme.getY() + dragme.getHeight();

            float rectay2 = btl.getRightside().otherY + btl.getRightside().getHeight();
            float rectby1 = dragme.getY();

            if(bView.lootActor instanceof Weapon) {
                float rectcx1 = bView.bWep.getX();
                float rectcx2 = bView.bWep.getX() + btl.getRightside().getWidth();
                float rectcy1 = bView.bWep.getY() + bView.bWep.getHeight()/2;
                float rectcy2 = bView.bWep.getY() + bView.bWep.getHeight();

                if (rectcx1 <= rectbx2 && rectcx2 >= rectbx1
                        && rectcy1 <= rectby2 && rectcy2 >= rectby1) {
                    //Overlapping weapon.
                    bView.setPlayerWeaponGlow(true, 2);
                } else {

                    bView.setPlayerWeaponGlow(true, 1);
                }
            }


            if (rectax1 <= rectbx2 && rectax2 >= rectbx1
                    && rectay1 <= rectby2 && rectay2 >= rectby1) {
                btl.getRightside().glow_type = 2;
                btl.getRightside().setGlow(true);
            } else {
                btl.getRightside().glow_type = 1;
                btl.getRightside().setGlow(true);
            }
        }
        if (butterDragMe != null) {
            butterDragMe.setPosition(screenX - butterDragMe.getWidth() / 2, Gdx.graphics.getHeight() - screenY - butterDragMe.getHeight() / 2);

            float rectax1 = bView.landingpad.getX();

            float rectbx2 = butterDragMe.getX() + butterDragMe.getWidth();

            float rectax2 = bView.landingpad.getX() + bView.landingpad.getWidth();
            float rectbx1 = butterDragMe.getX();

            float rectay1 = bView.landingpad.getY();
            float rectby2 = butterDragMe.getY() + butterDragMe.getHeight();

            float rectay2 = bView.landingpad.getY() + bView.landingpad.getHeight();
            float rectby1 = butterDragMe.getY();


            if (rectax1 <= rectbx2 && rectax2 >= rectbx1
                    && rectay1 <= rectby2 && rectay2 >= rectby1) {
                //Set some big glowy thing here
                bView.landingPadGlow(true);
            } else {
                //Maybe a differeny glowy thing here
                bView.landingPadGlow(false);
            }
        } else {
            bView.landingPadGlow(false);
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

    public void updateWeps(){
        Weapon aWep = btl.getLeftside().getEquipped_weapon();
        Weapon bWep = btl.getRightside().getEquipped_weapon();
        bView.updateCharacterWeapons(aWep, bWep);
    }
}


