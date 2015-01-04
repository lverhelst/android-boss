package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.ui.Image;

import java.util.ArrayList;

import verhelst.Comp.LCell;
import verhelst.Comp.LCoverFlow;
import verhelst.Comp.LTable;
import verhelst.CustomActors.Character;

/**
 * Created by Leon I. Verhelst on 1/1/2015.
 */
public class DressingScreen implements Screen, InputProcessor {


    private final Character c;
    private LCoverFlow currentGearFlow;
    private Image[] gearTypes = new Image[6];

    RngFight game;

    //0 -> Heads
    private int geartype = 0;
    private int partindex = 0;
    private LTable mainT, subT;
    private InputMultiplexer im;

    private Actor dragme;


    public DressingScreen(RngFight rfight, Character c){
        this.game = rfight;
        this.c = c;
        Sprite[] sprs = Assets.getSuitForLevel(1);
        for(int i = 0; i < 5; i++) {

            gearTypes[i] = new Image(sprs[i]);
        }
        gearTypes[5] = new Image(Assets.getWeaponSprite());

        //Load CoverFlow Heads Here.
        loadCoverFlow(geartype);


        mainT = new LTable(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainT.addActor(c);
        mainT.addRow();
        subT = new LTable(0,0,0,0);
        subT.addActor(currentGearFlow);
        subT.addRow();
        int i = 0;
        for(Image img : gearTypes){
            img.setName("gt" +  i++);
            if(i != 1 && i != 6)
                img.rotateBy(270);
            if(i == 6)
                img.rotateBy(90);
            subT.addActor(img, true);
        }
        Image bk = new Image(Assets.back_btn);
        bk.setName("gt" + i);
        subT.addActor(bk, true);
        mainT.addActor(subT);

        im = new InputMultiplexer();
        im.addProcessor(currentGearFlow);
        im.addProcessor(this);
    }

    public InputMultiplexer getInputProcessor(){
        return im;
    }

    private void loadCoverFlow(int type) {
        geartype = type;
        ArrayList<Actor> acts = new ArrayList<Actor>();
        Sprite[] t = new Sprite[0];
        switch (type) {
            case 0:
                t = Assets.faces;
                break;
            case 1:
                t = Assets.shirts;
                break;
            case 2:
                t = Assets.pants;
                break;
            case 3:
                t = Assets.shoulders;
                break;
            case 4:
                t = Assets.arms;
                break;
            case 5:
                t = new Sprite[Assets.weapons_sprites.size()];
                for (int i = 0; i < t.length; i++) {
                    t[i] = Assets.weapons_sprites.get(i);
                }
                break;
        }
        int rotateby = 0;
        for (int i = 0; i < t.length; i++) {

            Image img = new Image(t[i]);
            switch(type){
                case 0:
                    rotateby = 0;
                    break;
                case 1:
                case 2:
                case 3:
                case 4:
                    rotateby = 270;
                    break;
                case 5:
                    rotateby = 90;
                    break;
            }
            acts.add(img);
        }
        for(int i = 0; i < t.length; i++){
            if(geartype == 5){
                if(Assets.weaponUnlocks[i] == 0){
                    acts.set(i, new Image(Assets.mystery_sprite));
                }
            }
            else{
                if(Assets.unclocks[geartype][i] == 0){
                    acts.set(i, new Image(Assets.mystery_sprite));
                }
            }
        }
        if (t.length > 0) {
            if(currentGearFlow == null)
                currentGearFlow = new LCoverFlow(acts, Gdx.graphics.getWidth(), Gdx.graphics.getHeight() / 2, true);

            else {
                currentGearFlow.setItems(acts);
                currentGearFlow.setCurrentIndex(0);
            }
            currentGearFlow.setRotate_items_by(rotateby);
        }
    }

    @Override
    public void render(float delta) {

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RngFight.batch.begin();
        mainT.draw(RngFight.batch, 1);
        if(dragme != null) {
            dragme.draw(RngFight.batch, 1);

            System.out.println("DM: x " + dragme.getX() + " y " + dragme.getY() + " w " + dragme.getWidth() + " h " + dragme.getHeight() + " v " + dragme.isVisible());

        }
        RngFight.batch.end();
    }

    @Override
    public void resize(int width, int height) {

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
        int actualY = Gdx.graphics.getHeight() - screenY;
        //If drag started on the coverflow, take the middle item and put it on the drag
        LCell b = subT.getLCellForActor(currentGearFlow);
        if(screenX > b.getX() && screenX < b.getX() + b.getWidth() &&
                actualY > b.getY() && actualY < b.getY() + b.getHeight()) {
                partindex = currentGearFlow.getCurrentIndex();

                if(geartype == 5){
                        if(Assets.weaponUnlocks[partindex] == 0){
                            return false;
                        }
                    }
                    else{
                        if(Assets.unclocks[geartype][partindex] == 0){
                            return false;
                        }
                    }
                switch (geartype) {
                    case 0:
                        dragme = new Image(Assets.faces[partindex]);
                        break;
                    case 1:
                        dragme = new Image(Assets.shirts[partindex]);
                        dragme.setRotation(270);
                        break;
                    case 2:
                        dragme = new Image(Assets.pants[partindex]);
                        dragme.setRotation(270);
                        break;
                    case 3:
                        dragme = new Image(Assets.shoulders[partindex]);
                        dragme.setRotation(270);
                        break;
                    case 4:
                        dragme = new Image(Assets.arms[partindex]);
                        dragme.setRotation(270);
                        break;
                    case 5:

                        if (c.getEquipped_weapon() != null) {
                            dragme = new Image(Assets.weapons_sprites.get(partindex));
                            dragme.setRotation(90);
                        }
                        break;
                }
                if(dragme != null) {
                    Actor sizes = currentGearFlow.getInFocusActor();
                    dragme.setPosition(screenX - sizes.getWidth() / 2, actualY - sizes.getHeight() / 2);
                    dragme.setSize(sizes.getWidth(), sizes.getHeight());
                    dragme.setOrigin(sizes.getWidth() / 2, sizes.getHeight() / 2);
                }
            System.out.println("Added new dragme");
        }

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        int actualY = Gdx.graphics.getHeight() - screenY;
        LCell b = mainT.getLCellForActor(c);
        b.setGlow_type(0);
        b.setGlow(false);

        //dragme chek
        if(dragme != null) {

            if (screenX > b.getX() && screenX < b.getX() + b.getWidth() &&
                    actualY > b.getY() && actualY < b.getY() + b.getHeight()) {

                switch (geartype) {
                    case 0:
                        c.setBodyPart("head", Assets.faces[partindex], partindex);
                        break;
                    case 1:
                        c.setBodyPart("torso", Assets.shirts[partindex], partindex);
                        break;
                    case 2:
                        c.setBodyPart("leg", Assets.pants[partindex], partindex);
                        break;
                    case 3:
                        c.setBodyPart("shoulder", Assets.shoulders[partindex], partindex);
                        break;
                    case 4:
                        c.setBodyPart("elbow", Assets.arms[partindex], partindex);
                        break;
                    case 5:
                        if (c.getEquipped_weapon() != null) {
                            c.getEquipped_weapon().setSprite(Assets.weapons_sprites.get(partindex));
                            c.updateWeaponSprite();
                        }
                        break;
                }
            }
        }
        else{

            //Switch Gear Types
            for(int i = 0; i < 7; i++) {
                b = subT.getLCellForActorName("gt" + i);

                if(screenX > b.getX() && screenX < b.getX() + b.getWidth() &&
                        actualY > b.getY() && actualY < b.getY() + b.getHeight()){
                    if(i == 6)
                        game.switchScreens(4); //go to main screen
                    else{
                        dragme = null;
                        loadCoverFlow(i);
                    }
                    break;
                }
            }
        }
        dragme = null;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {


        if(dragme != null) {
            int actualY = Gdx.graphics.getHeight() - screenY;

            dragme.setPosition(screenX - dragme.getWidth()/2, actualY - dragme.getHeight()/2);
            if(dragme != null) {
                LCell b = mainT.getLCellForActor(c);
                if (screenX > b.getX() && screenX < b.getX() + b.getWidth() &&
                        actualY > b.getY() && actualY < b.getY() + b.getHeight()) {
                    b.setGlow_type(2);
                    b.setGlow(true);
                }else{
                    b.setGlow_type(0);
                    b.setGlow(false);
                }
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
