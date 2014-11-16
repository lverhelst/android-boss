package verhelst.bones;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

/**
 * Created by Orion on 11/8/2014.
 */
public class Joint {



    private double angle;
        double length;
        String name;
        Joint parent;
        ArrayList<Joint> children;

        public Joint(double angle_degrees, double length, String name){

            this.children = new ArrayList<Joint>();
            this.angle = angle_degrees;
            this.length = length;
            this.name = name;
        }

        public void addChild(Joint b){
            b.setParent(this);
            children.add(b);

        }

        public void setParent(Joint par){
            this.parent = par;

        }

        public void adjustAngle(double deg){
            this.angle = ((angle + deg) % 360);
            for(Joint j : children)
                j.adjustAngle(deg);
        }


        public void renderSkeleton(ShapeRenderer sr, float x, float y){


            float offx = x + (float)(Math.cos(Math.toRadians(this.angle)) * length);
            float offy = y + (float)(Math.sin(Math.toRadians(this.angle)) * length);

            if(parent != null){
                //System.out.println(x + " " + y + " " + offx + " " + offy) ;
                sr.line(x, y, offx, offy);

            }else{

                sr.rect(x, y, 2 , 2);
            }
            for(Joint j : children) {

                j.renderSkeleton(sr, offx, offy);
            }
        }

        public String toString(){
            return this.name;
        }

        public void print(){
            System.out.println((this.parent != null? this.parent.toString() : "Root: ") + " " + this.toString());
            for(Joint j : children){
                j.print();
            }
        }

        public double getAngle() {
            return angle;
        }

        public void setAngle(double angle) {

            int adjustment = (int)(angle - this.angle);
            System.out.println("adj: " + adjustment);
            this.adjustAngle(adjustment);

        }
}
