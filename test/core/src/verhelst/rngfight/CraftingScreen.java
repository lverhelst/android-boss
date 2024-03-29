package verhelst.rngfight;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;

import java.util.LinkedList;

import verhelst.Comp.LCell;
import verhelst.Comp.LLabel;
import verhelst.Comp.LTable;
import verhelst.Craft.CraftTable;
import verhelst.Craft.Inventory;
import verhelst.Craft.Item;
import verhelst.Craft.Mat;
import verhelst.Craft.cTOKEN;
import verhelst.CustomActors.*;

/**
 * Created by Orion on 2/2/2015.
 */
public class CraftingScreen implements Screen, InputProcessor {

    /*
        MainTable
        -LeftSide
            -TableOfChosenItems
            -Button
            -Output
        -RightSide
            -TableOfInventory


    */
    LTable mainTable;

    LTable tableOfRightSide;
    LTable tableOfLeftSide;

    LTable tableOfInventory;
    LTable tableOfCounts;
    LTable tableOfChosenItems;
    Image craftButtonImage, backButtonImage, mysteryImage;
    Item output;
    verhelst.CustomActors.Character chara;

    RngFight game;
    CraftTable craftTable;
    LinkedList<Item> chosenItems = new LinkedList<Item>();

    LLabel[] inventoryLabels = new LLabel[4];

    Actor dragme;
    int screenHeight = Gdx.graphics.getHeight();
    LCell characterCell, characterWeaponCell, outputCell;

    public CraftingScreen(RngFight fight){
        game = fight;
        chara = fight.player;
        mainTable = new LTable(0,0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        tableOfRightSide = new LTable();
        tableOfLeftSide = new LTable();
        tableOfInventory = new LTable();
        tableOfCounts = new LTable();
        tableOfChosenItems = new LTable();

        craftButtonImage = new Image(Assets.craft_btn);
        backButtonImage = new Image(Assets.back_btn);
        mysteryImage = new Image(Assets.mystery_sprite);




        for(int i = 0; i < 6; i++){
            SpriteActor chosenItem = new SpriteActor(Assets.mystery_sprite);
            tableOfChosenItems.addActor(chosenItem,true);
            if(i == 2)
                tableOfChosenItems.addRow();
        }


        tableOfLeftSide.addActor(tableOfChosenItems);
        tableOfLeftSide.addRow();
        output = new Mat("PLACEHOLDER", cTOKEN.BODYPART);

        outputCell  = tableOfLeftSide.addActor(output.getActor(), true);
        tableOfLeftSide.addRow();
        tableOfLeftSide.addActor(craftButtonImage, true);

        tableOfLeftSide.addRow();
        // tableOfLeftSide.addActor(new Image(Assets.butterBeaver), true);
       characterCell =  tableOfLeftSide.addActor(chara);
        if(chara.getEquipped_weapon() != null)
            characterWeaponCell = tableOfLeftSide.addActor(chara.getEquipped_weapon().getActor());
        else
            characterWeaponCell = tableOfLeftSide.addActor(new Image(Assets.mystery_sprite));


        for(int i = 0; i < 4; i++){
            inventoryLabels[i] = new LLabel(Inventory.getCount(i)+ "", Assets.skin);
            inventoryLabels[i].setIsHUD(true);
            tableOfCounts.addActor(inventoryLabels[i], true);
            tableOfCounts.addRow();
        }
        tableOfInventory.addActor(new Image(Assets.IRON), true);

        tableOfInventory.addRow();

        tableOfInventory.addActor(new Image(Assets.CLOTH), true);
        tableOfInventory.addRow();


        tableOfInventory.addActor(new Image(Assets.DUST), true);
        tableOfInventory.addRow();

        tableOfInventory.addActor(new Image(Assets.BONE), true);
        tableOfInventory.addRow();

        tableOfRightSide.addActor(tableOfInventory);
        //tableOfRightSide.addActor(tableOfCounts);
        tableOfRightSide.addRow();
        tableOfRightSide.addActor(new Image(Assets.glow) ,true).setVisible(false);
        tableOfRightSide.addActor(backButtonImage, true);
        mainTable.addActor(tableOfLeftSide);
        mainTable.addActor(tableOfRightSide);


        craftTable = new CraftTable();
    }

    public void craft(){
        if(chosenItems.size() >= 2) {

            output = craftTable.craftItem(chosenItems);
            System.out.println("Crafted: " + (output != null ? output.toString() : "NOTHING"));


            outputCell.setActor(output.getActor());
            if(output.getCTOKEN() == cTOKEN.WEAPON) {
                outputCell.setKeep_aspect_ratio(false);
                Assets.unlockItem(-1, ((Weapon)output).spriteindex);
            }
            else {
                outputCell.setKeep_aspect_ratio(true);
                if(output instanceof  BodyPartActor) {
                    Assets.unlockItem(((BodyPartActor)output).getBtype().ordinal(), ((BodyPartActor)output).part_index);
                }
            }
            chosenItems.clear();
            RngFight.SaveGame();
        }
    }

    public void chooseItem(cTOKEN type){
        int index = 0;
        switch(type){
            case IRON: index = 0;
                break;
            case CLOTH: index = 1;
                break;
            case BONE: index = 2;
                break;
            case DUST: index = 3;
                break;
        }
        if(!Inventory.removeItem(index))
            return;


        if(chosenItems == null)
            chosenItems = new LinkedList<Item>();
        chosenItems.add(new Mat(type.name(), type));
    }

    public void chooseItem(int index){
        cTOKEN token = cTOKEN.IRON;
        switch(index){
            case 0: token = cTOKEN.IRON;
                break;
            case 1: token = cTOKEN.CLOTH;
                break;
            case 2: token = cTOKEN.DUST;
                break;
            case 3: token = cTOKEN.BONE;
                break;
        }

        if(!Inventory.removeItem(index))
            return;



        if(chosenItems == null)
            chosenItems = new LinkedList<Item>();
        chosenItems.add(new Mat(token.name(), token));
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
        /*
            if(output visible)
                dragme = output;
         */
        int actualY = screenHeight - screenY;

        if(actorHit(output.getActor(), screenX, actualY)){
            if (output instanceof Weapon) {
                dragme = new Weapon();
                ((Weapon) dragme).copyWeapon((Weapon)output, Weapon.POSITION.RIGHT_POSITION);
                dragme = ((Weapon) dragme).getTable();
                dragme.setSize(((Weapon) output).getWidth() / 2, ((Weapon) output).getHeight() / 2);
            }
            if (output instanceof BodyPartActor) {
                dragme = new BodyPartActor();
                ((BodyPartActor) dragme).copyHSA((BodyPartActor)output);
                dragme.setSize(output.getActor().getWidth() / 2, output.getActor().getHeight() / 2);

            }
            if(dragme != null)
                dragme.setPosition(screenX + dragme.getX() - dragme.getWidth() / 2, actualY + dragme.getY() - dragme.getHeight() / 2);
            //dragme = output.getActor();
        }



        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {

        int actualY = screenHeight - screenY;
        characterCell.setGlow_type(0);
        characterCell.setGlow(false);
        characterWeaponCell.setGlow_type(0);
        characterWeaponCell.setGlow(false);

        //check touch on the inventory buttons
        /**
         *  PUT Item matchin to button into the selected item
         *  if not full
         */
        int i = 0;
        for(LCell cell : tableOfInventory.getLCells()){
            if(actorHit(cell.getActor(), screenX, actualY)){
                if(chosenItems.size() < 6) {
                    chooseItem(i);
                    refreshChosenItems();
                }
            }
            i++;
        }


        //check touch on chosen items...if so remove
        /**
         * Remove item at touched index.
         * Increase inventory count by one for that item
         */
        i = 0;
        for(LCell cell : tableOfChosenItems.getLCells()){
            if(actorHit(cell.getActor(), screenX, actualY)){
                if(chosenItems.size() > i) {
                   // if(cell.getActor() instanceof  Item)
                    Inventory.addItem(((Item)chosenItems.get(i)).getCTOKEN().ordinal());
                    chosenItems.remove(i);
                    refreshChosenItems();
                }
            }
            i++;
        }


        /*
            craft touched && chosenitems >= 2

            craft item
         */
        if(actorHit(craftButtonImage, screenX, actualY)){
            if(chosenItems.size() >= 2) {
                craft();
                refreshChosenItems();
            }
        }


        //check touch on back (if so return items to inventory && go back).
        /**
         * return all items in chosenitems to the inventory
         */
        if(actorHit(backButtonImage, screenX, actualY)){
            for(Item it : chosenItems){
               Inventory.addItem(it.getCTOKEN().ordinal());
            }
            chosenItems.clear();
            refreshChosenItems();
            game.switchScreens(5);
        }

        /*
            Touchup on character
            if(dragme != null)
                character.equip(item);
         */
        if(dragme != null) {
            if (screenX > characterCell.getX() && screenX < characterCell.getX() + characterCell.getWidth() + characterWeaponCell.getWidth() &&
                    actualY > characterCell.getY() && actualY < characterCell.getY() + characterCell.getHeight()) {
                if(output instanceof  Weapon){
                    chara.setEquipped_weapon((Weapon)output);
                    characterWeaponCell.setActor(chara.getEquipped_weapon().getActor());
                    RngFight.SaveGame();
                }
                if(dragme instanceof  BodyPartActor){
                    chara.setBodyPart((BodyPartActor)dragme);
                    RngFight.SaveGame();
                }

            }
            //Show explosion!!!
            output =  new Mat("PLACEHOLDER", cTOKEN.BODYPART);
            outputCell.setActor(output.getActor());
            outputCell.setKeep_aspect_ratio(true);
        }


        dragme = null;

        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {

        if(dragme != null) {

            int actualY = screenHeight - screenY;

            dragme.setPosition(screenX - dragme.getWidth()/2, actualY - dragme.getHeight()/2);

            if (screenX > characterCell.getX() && screenX < characterCell.getX() + characterCell.getWidth() + characterWeaponCell.getWidth() &&
                    actualY > characterCell.getY() && actualY < characterCell.getY() + characterCell.getHeight()) {
                characterCell.setGlow_type(2);
                characterCell.setGlow(true);
                characterWeaponCell.setGlow_type(2);
                characterWeaponCell.setGlow(true);
            }else{
                characterCell.setGlow_type(1);
                characterCell.setGlow(true);
                characterWeaponCell.setGlow_type(1);
                characterWeaponCell.setGlow(true);
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

    @Override
    public void render(float delta) {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        RngFight.batch.begin();
        mainTable.draw(RngFight.batch, 1.0f);
        tableOfCounts.setX(tableOfInventory.getX());
        tableOfCounts.setY(tableOfInventory.getY());
        tableOfCounts.setSize(tableOfInventory.getWidth(), tableOfInventory.getHeight());
        tableOfCounts.draw(RngFight.batch, 1.0f);
        if(dragme != null) {
            dragme.draw(RngFight.batch, 1.0f);
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


    public boolean actorHit(Actor actor, int x, int y){
        return actor.getX() <= x && actor.getX() + actor.getWidth() >= x &&
                 actor.getY() <= y && actor.getY() + actor.getHeight()>= y;
    }

    public void refreshChosenItems(){
        for(int i = 0; i < 6; i++){
            if(i >= chosenItems.size()) {
                tableOfChosenItems.getLCells()[i].setActor(mysteryImage);
            }
             else {
                Item c = chosenItems.get(i);
                tableOfChosenItems.getLCells()[i].setActor(c.getActor());
            }
        }
        if(chosenItems.size() < 2)
            craftButtonImage.setColor(Color.DARK_GRAY);
        else
            craftButtonImage.setColor(Color.WHITE);

        refreshInventoryLabels();
    }

    private void refreshInventoryLabels(){
        int i = 0;
        for(LLabel l : inventoryLabels){
            if(Inventory.getCount(i) == 0){
                tableOfInventory.getLCells()[i].setColor(Color.DARK_GRAY);
            }else{
                tableOfInventory.getLCells()[i].setColor(Color.WHITE);
            }
            l.setText(Inventory.getCount(i++) + " ");


        }
        resetCharacterWeapon();
    }

    private void resetCharacterWeapon(){
        if(chara.getEquipped_weapon() != null)
            characterWeaponCell.setActor(chara.getEquipped_weapon().getActor());
    }

    public void reset(){
        chosenItems.clear();
        refreshChosenItems();
        output =  new Mat("PLACEHOLDER", cTOKEN.BODYPART);
        outputCell.setActor(output.getActor());
        outputCell.setKeep_aspect_ratio(true);
        chara = game.player;
        characterCell.setActor(chara);
        characterWeaponCell.setActor(new Image(Assets.mystery_sprite));
        System.out.println("Well shit");
    }
}
