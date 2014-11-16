package verhelst.bones;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Orion on 11/11/2014.
 */
public class Model {


    //TODO: Loadable animations
    //But seriously, the stabbing doesn't need work.
    Joint root;

    ShapeRenderer sr = new ShapeRenderer();
    int originx, originy;

    public Model(int abs_x, int abs_y){
        //Pseudo human torso, side view
        this.originx = abs_x;
        this.originy = abs_y;

        root = new Joint(0,0, "root");
        root.addChild(new Joint(-90,10, "shoulder"));
        root.children.get(0).addChild(new Joint(180,10,"elbow"));
        root.children.get(0).children.get(0).addChild(new Joint(180,25, "wrist"));
        root.addChild(new Joint(-90, 20, "torso"));
        root.children.get(1).addChild(new Joint(-90,20, "leg"));
        root.addChild(new Joint(90,5, "head"));
        root.print();
    }

    public void render(Batch batch){

        sr.begin(ShapeRenderer.ShapeType.Line);

        Joint shoulder = root.children.get(0);
        if(Math.abs(shoulder.getAngle()) < 180){
            shoulder.adjustAngle(-30);
        }else{
            shoulder.setAngle(-90);
        }
        Joint elbow = shoulder.children.get(0);
        System.out.println(elbow.getAngle());
        if(Math.abs(elbow.getAngle()) < 270){
            elbow.adjustAngle(30);
        }else
        {
            elbow.setAngle(180);
        }

        root.renderSkeleton(sr, originx, originy);
        sr.end();
    }

}
