package verhelst.bones;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import verhelst.rngfight.BattleScreen;

/**
 * Created by Orion on 11/11/2014.
 */
public class Model {


    //TODO: Loadable animations
    //But seriously, the stabbing is grand
    Joint root;

    ShapeRenderer sr = new ShapeRenderer();
    public int originx, originy;
    public boolean isFlipped;
    public static float scale;

    public Model(int abs_x, int abs_y, boolean flip, float height){

        scale = (float) Math.max(height/96, 1);
        System.out.println("Scale" + scale);


        isFlipped = false;
        //Pseudo human torso, side view
        this.originx = abs_x;
        this.originy = abs_y;

        root = new Joint(0,0 * scale, "root", flip);
        root.addChild(new Joint(-90, 32 * scale, "torso", flip));
        root.children.get(0).addChild(new Joint(-90,32 * scale, "leg", flip));
        root.addChild(new Joint(-90,32 * scale, "shoulder", flip));
        root.children.get(1).addChild(new Joint(180,32 * scale,"elbow", flip));
        root.children.get(1).children.get(0).addChild(new Joint(180,32 * scale, "wrist", flip));

        root.addChild(new Joint(90,32 * scale, "head",flip));
        root.print();
    }

    public void render(Batch batch){
       /* batch.end();
        sr.begin(ShapeRenderer.ShapeType.Line);

    */

        Joint shoulder = root.children.get(1);
        if(!isFlipped){
            if(Math.abs(shoulder.getAngle()) < 180 && BattleScreen.battling){
                shoulder.adjustAngle(-30);
            }else{
                shoulder.setAngle(-90);
            }
            Joint elbow = shoulder.children.get(0);
          //  System.out.println(elbow.getAngle());
            if(Math.abs(elbow.getAngle()) < 270 && BattleScreen.battling){
               // elbow.adjustAngle(10);
            }else
            {
                elbow.setAngle(180);
            }
        }else{
            if(shoulder.getAngle() < 0 && BattleScreen.battling){
                shoulder.adjustAngle(30);

            }else{

                shoulder.setAngle(-90);
            }
            Joint elbow = shoulder.children.get(0);
            //System.out.println(shoulder.getAngle());
          //    System.out.println(elbow.getAngle());
            if(Math.abs(elbow.getAngle()) < 90 && BattleScreen.battling){
                // elbow.adjustAngle(10);
            }else
            {
                elbow.setAngle(0);
            }
        }

        /*root.renderSkeleton(sr, originx, originy);


        sr.end();
        batch.begin();*/
        root.renderWithSprites(batch, originx, originy);
    }

    public void flip(){
        flip(root);
    }

    private void flip(Joint j){
        j.flip();
        for(Joint j2 : j.children){
            flip(j2);
        }
    }


    public void updateSprite(String name, Sprite sprite){
        //search for name
        Joint j = searchForJointByName(name, root);
        if(j != null) {
            float jw = j.testS.getWidth();
            float jh = j.testS.getHeight();

            Sprite temp = new Sprite(sprite);
            temp.setSize(jw,jh);
            j.testS = temp;
        }

    }

    private Joint searchForJointByName(String name, Joint joint){
        if(joint.name.equals(name)){
            return joint;
        }
        else{
            Joint jj = null;
            for(Joint j : joint.children){
                jj = searchForJointByName(name, j);
                if(jj!= null)
                    break;
            }
            return jj;
        }
    }

}