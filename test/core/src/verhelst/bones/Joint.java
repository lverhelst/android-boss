package verhelst.bones;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

import verhelst.rngfight.Assets;

/**
 * Created by Orion on 11/8/2014.
 */
public class Joint {

        Sprite testS;

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
            testS = new Sprite(Assets.findSpriteForName(name));
            if(!name.equals("head"))
                testS.setSize((int) length, 8);
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
                sr.rectLine(x, y, offx, offy, 8);

            }else{

                sr.rect(x, y, 2 , 2);
            }
            for(Joint j : children) {

                j.renderSkeleton(sr, offx, offy);
            }
        }

        public void renderWithSprites(Batch batch, float x, float y){

            float offx = x + (float)(Math.cos(Math.toRadians(this.angle)) * length);
            float offy = y + (float)(Math.sin(Math.toRadians(this.angle)) * length);

            this.testS.setOrigin(0,0);
            this.testS.setPosition(x, y);


            this.testS.setRotation((float)angle);

            this.testS.draw(batch);

            System.out.println(x + " " + y + " " + offx + " " + offy + " " + angle) ;

            for(Joint j : children) {

                j.renderWithSprites(batch, offx, offy);
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
          //  System.out.println("adj: " + adjustment);
            this.adjustAngle(adjustment);

        }
}
