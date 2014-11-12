package verhelst.bones;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import java.util.ArrayList;

/**
 * Created by Orion on 11/8/2014.
 */
public class Joint {

        float offset_x, offset_y;
        double rotation;
        double length;

        Joint parent;
        ArrayList<Joint> children;

        public Joint(float xOffset, float yOffset, Joint parent){
            this.offset_x = xOffset;
            this.offset_y = yOffset;
            this.children = new ArrayList<Joint>();
            if(parent != null) {
                this.offset_x += parent.offset_x;
                this.offset_y += parent.offset_y;
            }
            this.parent = parent;
            //Pythagorous to get length
            this.length = Math.sqrt(xOffset * xOffset + yOffset * yOffset);
            //rotation
            if(yOffset != 0 && xOffset != 0)
                this.rotation = Math.atan(xOffset/yOffset);
            else
                this.rotation = 0;
            System.out.println("x" + offset_x + " y" + offset_y );
        }

        public void addChild(Joint b){
            children.add(b);
        }

        public void render(ShapeRenderer sr){
            if(parent != null){
                //System.out.println(parent.offset_x + " " + offset_x + " " + parent.offset_y + " " + offset_y) ;
                sr.line(parent.offset_x, parent.offset_y, offset_x, offset_y);

            }else{
                sr.rect(this.offset_x, this.offset_y, 2 , 2);
            }
            for(Joint j : children) {
                j.render(sr);
            }
        }

        public void print(){
            System.out.println((this.parent != null? this.parent.toString() : "Root: ") + " " + this.toString());
            for(Joint j : children){
                j.print();
            }
        }
}
