package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.Random;

/**
 * Created by Leon I. Verhelst on 10/19/2014.
 */
public class DamageNumber {
    private float x;
    private float y;
    private float angle;
    private float alpha;
    private int value, render_iterations;
    private final SpriteBatch spritebatch;
    private CharSequence cs;
    private BitmapFont bf;
    private boolean isRemoveable;
    private Random random;

    public DamageNumber(int amount, int screenx, int screeny, SpriteBatch sb){
        this.value = amount;
        this.x= screenx;
        this.y=screeny;
        this.render_iterations = 0;
        this.alpha = 1.0f;
        this.spritebatch = sb;
        this.isRemoveable = false;
        bf = new BitmapFont();
        cs = "" +  Math.abs(this.value);
        //set angle in degrees, convert to radians
        this.angle = (float)Math.toRadians(new Random().nextInt(120) + 30);//+ 30;
    }

    public void update(){

        this.x += Math.cos(this.angle);  //CAH
        //use the alpha value to pull the number down, this gives a nice curve without complicated computations
        //the * 1.1 is just to adjust the intensity of the (-1 + alpha) portion
        this.y += (Math.sin(this.angle) + (-1 + this.alpha) * 1.1); //SOH
        this.alpha += -0.01f;
        //at an alpha < 0, the random number can be removed from where it is displayed
        //since it will be invisible
        if(this.alpha <= 0){
            this.isRemoveable = true;
        }
    }

    public void render(){
        //Damage done = red colour
        if(this.value > 0){
            bf.setColor(1.0f, 0.0f, 0.0f, this.alpha);
        }
        //healing done = green colour
        else if(this.value < 0){
            bf.setColor(0.0f, 1.0f, 0.0f, this.alpha);
        }
        //0 done = white color
        else{
            bf.setColor(1.0f, 1.0f, 1.0f, this.alpha);
        }
        bf.draw(spritebatch, cs, getX(), getY());
        //calculate next location for next render
        this.update();
    }

    public int getRender_iterations(){
        return this.render_iterations;
    }

    public boolean isRemoveable(){
        return isRemoveable;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }
}
