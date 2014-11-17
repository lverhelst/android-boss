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
        boolean isFlipped;

        public Joint(double angle_degrees, double length, String name, boolean flip){

            this.children = new ArrayList<Joint>();
            this.angle = angle_degrees;
            this.length = length;
            this.name = name;

            testS = new Sprite(Assets.findSpriteForName(name));
            testS.flip(false, flip);
            testS.setSize(testS.getWidth() * Model.scale, testS.getHeight() * Model.scale);
            this.isFlipped = flip;
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
                sr.rectLine(x, y, offx, offy, 1);



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

            this.testS.setOrigin(0,0 + testS.getHeight()/2);
            if(name.equals("shoulder")){
               // y -= testS.getHeight()/2;
                //offy -= testS.getHeight()/2;
                if(!isFlipped)
                x += testS.getWidth()/4;
            }
            if(name.equals("elbow")){
                if(!isFlipped) {
                    x += testS.getWidth() / 4;
                    offx += testS.getWidth() / 4;
                }
            }
            if(name.equals("wrist") ){

                    y -= testS.getHeight() / 4;
                    offy -= testS.getHeight() / 4;
                if(!isFlipped) {
                x += testS.getWidth()/4;
                offx += testS.getWidth()/4;}
            }

            this.testS.setPosition(x, y);


            this.testS.setRotation((float)angle);

            if(!name.equals("root"))
                this.testS.draw(batch);



            for(Joint j : children) {

                j.renderWithSprites(batch, offx, offy);
            }

        }


        public void flip(){
            //fliiiip
            angle = 180 - angle;
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
