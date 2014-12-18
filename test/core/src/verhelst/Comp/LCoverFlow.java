package verhelst.Comp;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.List;

/**
 * Created by Orion on 12/1/2014.
 * TODO: Add keep aspect ratio
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
    int x_start, x_end, x_current, x_last;
    int velocity;
    boolean isScroll, isDrag, inBoundingBox, keepAspectRatio;
    int shiftedcount;



    public LCoverFlow(List<Actor> displayItems, int width, int height, boolean keepAspectRatio){
        this.items = displayItems;
        this.currentIndex = 0;
        this.visibleItems = 5;
        this.keepAspectRatio = keepAspectRatio;
        setSize(width, height);
        setPosition(0, 0);
        setBounds(0, 0, width, height);
        currentDirection = DIRECTION.STILL;
        //Gdx.input.setInputProcessor(this);
        shiftedcount = 0;
        inBoundingBox = false;
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

        items.get(index).setScale(calculateScale(offset));

        //Aspect ratio maybe
        //keep aspect ratio of actor
        if(keepAspectRatio) {
            float w = items.get(index).getWidth();
            float h = items.get(index).getHeight();

            float x_scale = (getWidth() / 5) / w;
            float y_scale = (getWidth() / 5) / h;

            float scale = Math.min(x_scale, y_scale);

            float left_edge_offset = (float) (getWidth()/10 + (items.get(index).getHeight()/2));
            //System.out.println(left_edge_offset + " " + items.get(index).getWidth());
            items.get(index).setSize(w * scale, h * scale);
            //items.get(index).setOrigin(w * scale/2,h * scale);
            items.get(index).setRotation(90);
            float y_offset = (float)(getHeight()/2.0 - (items.get(index).getWidth() * items.get(index).getScaleX())/2.0);
           // System.out.println("y off " + y_offset);

            items.get(index).setPosition((float)(0 + (((offset + visibleItems/2) / 5.0) * getWidth() + left_edge_offset) + calculatePosition()), getY() + y_offset);

            //  System.out.println("o " + offset + " " + calculateScale(offset));
        }else {
            items.get(index).setSize((float) (getWidth() / 5.0), getHeight());
            float y_offset = (float)(getHeight()/2.0 - (items.get(index).getHeight() * items.get(index).getScaleY())/2.0);
            items.get(index).setPosition((float)(0 + (((offset + visibleItems/2) / 5.0) * getWidth()) + calculatePosition()), getY());

        }

    }

    private float calculateScale(int offset){

        float thyme = target_time - current_time;
        float percent_offset = (move_anim_time_ms - thyme)/move_anim_time_ms;
        if(isDrag){
            percent_offset = Math.abs((float)((float)(x_start - x_current) / (float)(getWidth()/5.0)));
        }

        switch(currentDirection){
            case STILL: return (float)(1 - Math.abs(offset) * 0.25);
            case LEFT:
                if(offset == 0){
                    return (float)(1 - 0.25 * percent_offset);
                }
                else if(offset < 0 ){
                    return (float)(1 -  Math.abs(offset) * 0.25 - 0.25 * percent_offset);
                }else{
                    return (float)(1 -  Math.abs(offset) * 0.25 + 0.25 * percent_offset);
                }
            case RIGHT:
                if(offset == 0){
                    return (float)(1 - 0.25 * percent_offset);
                }else if (offset < 0 ){
                    return (float)(1 -  Math.abs(offset) * 0.25 + 0.25 * percent_offset);
                }else{
                    return (float)(1 -  Math.abs(offset) * 0.25 - 0.25 * percent_offset);
                }
        }


        return (float)1.0;
    }


    private float calculatePositionDuringAnimation(){
        float thyme = target_time - current_time;

     //   move_anim_time_ms -= Math.abs((float)((float)(x_start - x_current) / (float)(getWidth()/5.0)));


        float percent_offset = (move_anim_time_ms - thyme)/move_anim_time_ms;// + Math.abs((float)((x_current - x_start)/(getWidth()/5.0)));
      //  move_anim_time_ms = 1000;
      //  System.out.println("During anm " + target_time + ": " + percent_offset);

        float drag_offset = (float)((x_current - x_start)/(getWidth()/5.0));
        percent_offset += drag_offset;
        if(percent_offset > 1) {
            target_time = System.currentTimeMillis() - 10;
            return 0;
        }

      //  System.out.println("anuim " + (x_current - x_start) + " adj percent offset: " + drag_offset + " " + percent_offset);
        if(currentDirection == DIRECTION.LEFT){
            return (float)((getWidth()/5.0) * -percent_offset);

        }

        if(currentDirection == DIRECTION.RIGHT){
            return (float)((getWidth()/5.0) * percent_offset);

        }

        if(currentDirection == DIRECTION.STILL){
            return 0;
        }
        return 0;
    }

    private void snapToGrid(){
        if(x_current - x_start > getWidth()/10){
            moveIndex();
        }else if(x_current - x_start < -(getWidth()/10)){
            moveIndex();
        }
    }

    private float calculatePosition(){
        return  (isDrag? calculatePositionDuringDrag() : calculatePositionDuringAnimation());
    }

    private float calculatePositionDuringDrag(){
        return x_current - x_start;
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
        float y = Gdx.graphics.getHeight() - screenY;

        inBoundingBox = screenX > getX() && screenX < getX() + getWidth() && y > getY() && y < getY() + getHeight();

        System.out.println(inBoundingBox + " x" + screenX + " y "  + y );

        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if(inBoundingBox) {
            x_end = screenX;

            current_time = System.currentTimeMillis();
            end_drag = System.currentTimeMillis();

            if (end_drag - start_drag < 150) {
                isDrag = false;
            }


            if (!isDrag) {
                x_end = x_start = x_current = x_last = 0;
            }


            //velocity = (int)((x_end - x_start)/(float)((end_drag - start_drag)/100));
            //velocity = 0;
            /// isScroll = Math.abs(velocity) > 0;

            target_time = current_time + (move_anim_time_ms);// * (isDrag ? (long)((x_end - x_start) / (getWidth()/5)) : 1) );
            //Move by one unless we are scrolling
            if (isDrag) {
                isDrag = false;
                snapToGrid();
                currentDirection = DIRECTION.STILL;
            } else {
                if (screenX < getWidth() / 2) {
                    //Move right
                    currentDirection = DIRECTION.LEFT;
                }

                if (screenX > getWidth() / 2) {
                    //Move left
                    currentDirection = DIRECTION.RIGHT;
                }

            }
       /* else
        {
                if(velocity > 0)
                    currentDirection = DIRECTION.RIGHT;
                else
                    currentDirection = DIRECTION.LEFT;

         }*/
        }
        isDrag = false;
        inBoundingBox = false;
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if(inBoundingBox) {
            isDrag = true;
            x_last = x_current;
            x_current = screenX;
            if (x_last < x_current) {
                currentDirection = DIRECTION.RIGHT;
            } else if (x_last > x_current) {
                currentDirection = DIRECTION.LEFT;
            } else {
                currentDirection = DIRECTION.STILL;
            }


            if (x_current - x_start > getWidth() / 5) {
                x_start = x_current;

                //  currentDirection = DIRECTION.RIGHT;
                moveIndex();
            }
            if (x_current - x_start < -getWidth() / 5) {
                x_start = x_current;

                //currentDirection = DIRECTION.LEFT;
                moveIndex();
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
    public void draw(Batch batch, float parentAlpha) {


        current_time = System.currentTimeMillis();
        if(currentDirection != DIRECTION.STILL && !isDrag && !isScroll && current_time > target_time){

            moveIndex();
            currentDirection = DIRECTION.STILL;
        }
        //i is the offset
        int index;
        for(int i = (visibleItems + 2)/2 - (visibleItems % 2 == 0? 1:0) ; i >= -(visibleItems + 2)/2; i--){
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
        //super.draw(batch, parentAlpha);
    }

    private void moveIndex(){
        if (currentDirection == DIRECTION.RIGHT){
            if (currentIndex - 1 >= 0) {
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



