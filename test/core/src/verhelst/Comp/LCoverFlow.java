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

    enum DIRECTION {
        LEFT,
        RIGHT,
        STILL
    }

    //Different sizes should be okay in the final build...might never get there.
    List<Actor> items;
    int currentIndex;
    int visibleItems;

    long move_anim_time_ms = 100;
    long current_time;
    long target_time;
    long start_drag, end_drag;
    DIRECTION currentDirection;
    int x_start, x_end;
    int velocity;
    boolean isScroll;
    int shiftedcount;



    public LCoverFlow(List<Actor> displayItems, int width, int height){
        this.items = displayItems;
        this.currentIndex = displayItems.size()/2;
        this.visibleItems = 5;
        setSize(width, height);
        setPosition(0, 0);
        setBounds(0, 0, width, height);
        currentDirection = DIRECTION.STILL;
        Gdx.input.setInputProcessor(this);
        shiftedcount = 0;
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



       items.get(index).setSize((float) (getWidth() / 5.0), (float) (getHeight()));
        items.get(index).setScale(calculateScale(offset));
        switch(offset){
            case -2:
                //60%
                //centered in first 1/5th
                //from x: 0 + ((1/5) * width)/2 <-- that's center of the item



                items.get(index).setPosition((float)0 + calculatePositionDuringAnimation(), 0);


                break;
            case -1:
                //80%
                //centered in 2/5th
                //from x: (1/5) * width + ((1/5) * width)/2

                items.get(index).setPosition((float)(0 + ((1.0 / 5.0) * getWidth()) + calculatePositionDuringAnimation()), 0);
                break;
            case 0:
                //Full size
                //Position = centered
                //from x: width/2

                items.get(index).setPosition((float)(0 + ((2.0 / 5.0) * getWidth())+ calculatePositionDuringAnimation()), 0);
                break;
            case 1:
                //80% size
                //Centered in 4/5th
                //from X: (3/5) * width + ((1/5) * width)/2

                items.get(index).setPosition((float)(0 + ((3.0 / 5.0) * getWidth())+ calculatePositionDuringAnimation()), 0);
                break;

            case 2:
                //50
                //Centered in last fifth
                //from X: (4/5) * width + ((1/5) * width)/2

                items.get(index).setPosition((float)(0 + ((4.0 / 5.0) * getWidth())+ calculatePositionDuringAnimation()), 0);
                break;
        }
    }

    private float calculateScale(int offset){
        float thyme = target_time - current_time;
        float percent_offset = (move_anim_time_ms - thyme)/move_anim_time_ms;


        switch(offset){
            case -2:
                switch (currentDirection){
                    case STILL: return (float)0.6;
                    case LEFT: return (float)0.0;//(float)0.6 - (float)(0.2 * percent_offset);
                    case RIGHT: return (float)0.6 + (float)(0.2 * percent_offset);
                }
                break;
            case -1:
                switch (currentDirection){
                    case STILL: return (float)0.8;
                    case LEFT: return (float)0.8 - (float)(0.2 * percent_offset);
                    case RIGHT: return (float)0.8 + (float)(0.2 * percent_offset);
                }
                break;
            case 0:
                switch (currentDirection){
                    case STILL: return (float)1;
                    case LEFT: return (float)1- (float)(0.2 * percent_offset);
                    case RIGHT: return (float)1- (float)(0.2 * percent_offset);
                }
                break;
            case 1:
                switch (currentDirection){
                    case STILL: return (float)0.8;
                    case LEFT: return (float)0.8 + (float)(0.2 * percent_offset);
                    case RIGHT: return (float)0.8 - (float)(0.2 * percent_offset);
                }
                break;
            case 2:
                switch (currentDirection){
                    case STILL: return (float)0.6;
                    case LEFT: return (float)0.6 + (float)(0.2 * percent_offset);
                    case RIGHT: return (float)0.0;// (float)0.6 - (float)(0.2 * percent_offset);
                }
                break;
        }
        return (float)1.0;
    }


    private float calculatePositionDuringAnimation(){
        float thyme = target_time - current_time;
        float percent_offset = (move_anim_time_ms - thyme)/move_anim_time_ms;
        if(currentDirection == DIRECTION.LEFT){
            return (float)(getWidth()/5.0 * -percent_offset);

        }
        if(currentDirection == DIRECTION.RIGHT){
            return (float)(getWidth()/5.0 * percent_offset);

        }
        if(currentDirection == DIRECTION.STILL){
            return 0;
        }
        return 0;
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
        x_start = screenX;
        start_drag = System.currentTimeMillis();
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        x_end = screenX;




        current_time = System.currentTimeMillis();
        end_drag = System.currentTimeMillis();



        velocity = (int)((x_end - x_start)/(float)((end_drag - start_drag)/100));
        isScroll = Math.abs(velocity) > 0;
        target_time = current_time + move_anim_time_ms;
        //Move by one unless we are scrolling
        if(!isScroll) {
            if (screenX < getWidth() / 2) {
                //Move right
                currentDirection = DIRECTION.RIGHT;

            }

            if (screenX > getWidth() / 2) {
                //Move left
                currentDirection = DIRECTION.LEFT;


            }
        }else{
            if(velocity > 0)
                currentDirection = DIRECTION.RIGHT;
            else
                currentDirection = DIRECTION.LEFT;
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        x_end = screenX;
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
        current_time = System.currentTimeMillis();
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
            computeSizeAndPosition(i, index);
            items.get(index).draw(batch, parentAlpha);
        }

        if(!isScroll && current_time > target_time){

            moveIndex();
            currentDirection = DIRECTION.STILL;
        }

        velocity/=1.1;
        if(Math.abs(velocity) > 0 && current_time > target_time){

            target_time = current_time + move_anim_time_ms;// * ++shiftedcount;

            moveIndex();

        }else{
            isScroll = Math.abs(velocity) > 0;
            if(!isScroll){
                shiftedcount = 0;
                move_anim_time_ms = 100;
            }
        }




        //super.draw(batch, parentAlpha);
    }

    private void moveIndex(){
        if (currentDirection == DIRECTION.RIGHT){
            if (currentIndex - 1 > 0) {
                currentIndex--;
            } else {
                currentIndex = items.size() - 1;
            }
        }

        if (currentDirection == DIRECTION.LEFT) {

            if (currentIndex + 1 < items.size()) {
                currentIndex++;
            } else {
                currentIndex = 0;
            }
        }
    }

}



