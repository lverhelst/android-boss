package verhelst.Comp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.List;

/**
 * Created by Orion on 12/1/2014.
 */
public class LCoverFlow extends Actor implements InputProcessor {
    /**
     *  Rudimentary: "Planning" stage
     *
     *  What we need is a list of items that scroll left and right based on A) a tap or B) a swipe
     *
     *
     *
     *
     *
     *
     */

    //Different sizes should be okay in the final build...might never get there.
    List<Actor> items;
    int currentIndex;
    int visibleItems;



    public LCoverFlow(List<Actor> displayItems, int width, int height){
        this.items = displayItems;
        this.currentIndex = displayItems.size()/2;
        this.visibleItems = 5;
        setSize(width, height);
        setPosition(0,0);
        setBounds(0,0, width, height);
        Gdx.input.setInputProcessor(this);
    }

/*
    public LCoverFlow(List<Actor> displayItems, int visibleItems){
        this.items = displayItems;
        this.currentIndex = displayItems.size()/2;
        this.visibleItems = visibleItems;
    }
*/

    private void computeSizeAndPosition(int offset, int index){

        if(index <  0  || index >= items.size())
            return;



        switch(offset){
            case -2:
                //60%
                //centered in first 1/5th
                //from x: 0 + ((1/5) * width)/2 <-- that's center of the item
                items.get(index).setSize((float) (getWidth() / 5.0), (float) (getHeight()));
                items.get(index).setScale((float)0.6);
                items.get(index).setPosition((float)0, 0);

                break;
            case -1:
                //80%
                //centered in 2/5th
                //from x: (1/5) * width + ((1/5) * width)/2
                items.get(index).setSize((float) (getWidth() / 5 ), (float) (getHeight() ));
                items.get(index).setScale((float)0.8);
                items.get(index).setPosition((float)(0 + ((1.0 / 5.0) * getWidth()) ), 0);
                break;
            case 0:
                //Full size
                //Position = centered
                //from x: width/2
                items.get(index).setSize((float) (getWidth() / 5), (float) (getHeight()));
                items.get(index).setScale((float)1.0);
                items.get(index).setPosition((float)(0 + ((2.0 / 5.0) * getWidth())), 0);
                break;
            case 1:
                //80% size
                //Centered in 4/5th
                //from X: (3/5) * width + ((1/5) * width)/2
                items.get(index).setSize((float) (getWidth() / 5), (float) (getHeight()));
                items.get(index).setScale((float)0.8);
                items.get(index).setPosition((float)(0 + ((3.0 / 5.0) * getWidth())), 0);
                break;

            case 2:
                //50
                //Centered in last fifth
                //from X: (4/5) * width + ((1/5) * width)/2
                items.get(index).setSize((float) (getWidth() / 5), (float) (getHeight()));
                items.get(index).setScale((float)0.6);
                items.get(index).setPosition((float)(0 + ((4.0 / 5.0) * getWidth())), 0);
                break;
        }
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
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        System.out.println("Touchup @ " + screenX);
        if(screenX < getWidth()/5){
            if(currentIndex - 1 > 0){
                currentIndex--;
            }else{
                currentIndex = items.size() - 1;
            }
        }

        if(screenX > 4 * (getWidth()/5)){
            if(currentIndex + 1 < items.size()){
                currentIndex++;
            }else{
                currentIndex = 0;
            }
        }


        return true;
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

    @Override
    public void draw(Batch batch, float parentAlpha) {
        //i is the offset
        int index;
        for(int i = visibleItems/2 - (visibleItems % 2 == 0? 1:0) ; i >= -visibleItems/2; i--){
            index = currentIndex + i;
            if(index < 0){
                index = items.size() + index;
            }
            if(index >= items.size()){
                index = index - items.size();
            }


            computeSizeAndPosition(i,index);
            items.get(index).draw(batch, parentAlpha);

        }
        System.out.println(currentIndex);
        //super.draw(batch, parentAlpha);
    }
}



