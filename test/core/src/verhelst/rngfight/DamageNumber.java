package verhelst.rngfight;

import java.util.Random;

/**
 * Created by Leon I. Verhelst on 10/19/2014.
 */
public class DamageNumber {
    private float x;
    private float y;
    private float angle;
    private float alpha;
    private int value;
    private int render_iterations;
    private CharSequence cs;
    private boolean isRemoveable;
    private Random random;
    private double rand_force;
    private float red;
    private float green;
    private float blue;



    public DamageNumber(int amount, int screenx, int screeny){
        this.value = amount;
        this.x= screenx;
        this.y=screeny;
        this.render_iterations = 0;
        this.alpha = 1.0f;

        this.isRemoveable = false;
        cs = "" +  Math.abs(value);
        this.random = new Random();
        this.rand_force = 1 + random.nextDouble() + random.nextDouble();
        //set angle in degrees, convert to radians
        this.angle = (float)Math.toRadians(random.nextInt(120) + 30);//+ 30;

        //Damage done = red colour
        if(this.value > 0){
            this.red = 1.0f;
            this.green = 0.0f;
            this.blue = 0.0f;
        }
        //healing done = green colour
        else if(this.value < 0){
            this.red = 0.0f;
            this.green = 1.0f;
            this.blue = 0.0f;
        }
        //0 done = white color
        else{
            this.red = 1.0f;
            this.green = 1.0f;
            this.blue = 1.0f;
        }
    }

    public void update(int scale){

        this.x += Math.cos(this.angle) * scale;  //CAH
        //use the alpha value to pull the number down, this gives a nice curve without complicated computations
        //the * 1.1 is just to adjust the intensity of the (-1 + alpha) portion
        this.y += (Math.sin(this.angle) + (-1 + this.alpha) + 1/(value == 0? 1: value) + rand_force) * scale; //SOH
        this.alpha += -0.01f;
        //at an alpha < 0, the random number can be removed from where it is displayed
        //since it will be invisible
        if(this.alpha <= 0){
            this.isRemoveable = true;
        }
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

    public int getValue() {
        return value;
    }

    public CharSequence getCs() {
        return cs;
    }

    public float getRed() {
        return red;
    }

    public float getGreen() {
        return green;
    }

    public float getBlue() {
        return blue;
    }

    public float getAlpha(){
        return this.alpha;
    }
}
