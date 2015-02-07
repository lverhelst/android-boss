package verhelst.bones;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
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

    public Model(int abs_x, int abs_y, boolean flip, float height) {
        if (Gdx.app.getType() == Application.ApplicationType.Android) {

            scale = (float) Math.max(height / 96, 2.75);

        } else {

            scale = (float) Math.max(height / 96, 1.00);
        }
      //  System.out.println("Scale" + scale);

        isFlipped = false;
        //Pseudo human torso, side view
        this.originx = abs_x;
        this.originy = abs_y;

        root = new Joint(0, 0 * scale, "root", flip, 0, 0);
        root.addChild(new Joint(-90, 32 * scale, "torso", flip, 0, 0));
        root.children.get(0).addChild(new Joint(-90, 32 * scale, "leg", flip, 0, 0));
        root.addChild(new Joint(90, 32 * scale, "head", flip, 0, 0));
        root.addChild(new Joint(-90, 32 * scale, "shoulder", flip, 0, (32 * scale) / 2));
        root.children.get(2).addChild(new Joint(180, 32 * scale, "elbow", flip, 32 / 2 * scale, 0));
        root.children.get(2).children.get(0).addChild(new Joint(180, 32 * scale, "wrist", flip, 32 / 5 * scale, -(16 * scale / 2)));
        root.children.get(2).children.get(0).isRenderChildrenFirst = true; //make sure weapon is drawn under elbow sprite

        //root.print();

    }

    public void render(Batch batch, float parentAlpha) {


        Joint shoulder = root.children.get(2);
        if (!isFlipped) {
            if (Math.abs(shoulder.getAngle()) < 180 && BattleScreen.battling) {
                shoulder.adjustAngle(-30);
            } else {
                shoulder.setAngle(-90);
            }
            Joint elbow = shoulder.children.get(0);
            Joint wrist = elbow.children.get(0);
            //  System.out.println(elbow.getAngle());
            if (Math.abs(elbow.getAngle()) < 270 && BattleScreen.battling) {
                // elbow.adjustAngle(10);
            } else {
                elbow.setAngle(180);

            }
            if (Math.abs(elbow.getAngle()) == 180 && BattleScreen.battling) {
                //  System.out.print(" wrist to def ");
                wrist.setAngle(180);
            }


            if (!BattleScreen.battling) {
                wrist.setAngle(115);
            } else {

            }


        } else {
            if (shoulder.getAngle() < 0 && BattleScreen.battling) {
                shoulder.adjustAngle(30);

            } else {

                shoulder.setAngle(-90);
            }
            Joint elbow = shoulder.children.get(0);
            Joint wrist = elbow.children.get(0);
            //System.out.println(shoulder.getAngle());
            //  System.out.println(elbow.getAngle());
            if (Math.abs(elbow.getAngle()) < 45 && BattleScreen.battling) {
                // elbow.adjustAngle(5);

            } else {
                elbow.setAngle(0);
                wrist.setAngle(0);
            }
            if (!BattleScreen.battling) {
                wrist.setAngle(65);
            } else {

            }
        }

        //root.renderWithSprites(batch, originx, originy);
        if (false) {
            batch.end();
            sr.begin(ShapeRenderer.ShapeType.Line);
            root.renderSkeleton(sr, originx, originy);
            sr.end();
            batch.begin();
        } else {

            root.renderWithSprites(batch, originx, originy, parentAlpha);
        }

    }

    public void flip() {
        flip(root);
    }

    private void flip(Joint j) {
        j.flip();
        for (Joint j2 : j.children) {
            flip(j2);
        }
    }


    public void updateSprite(String name, Sprite sprite) {
        //search for name
        Joint j = searchForJointByName(name, root);
        if (j != null) {
            float jw = j.testS.getWidth();
            float jh = j.testS.getHeight();


            Sprite temp = new Sprite(sprite);


            if (name.equals("wrist")) {
                temp.flip(false, isFlipped);
                temp.setSize(temp.getWidth() * scale, temp.getHeight() * scale);
                //centre according to weapon sprite
                j.y_adj = 0;//(-temp.getHeight()/2);
            } else {
                temp.flip(false, isFlipped);
                temp.setSize(jw, jh);

            }

            j.testS = temp;

        }

    }

    private Joint searchForJointByName(String name, Joint joint) {
        if (joint.name.equals(name)) {
            return joint;
        } else {
            Joint jj = null;
            for (Joint j : joint.children) {
                jj = searchForJointByName(name, j);
                if (jj != null)
                    break;
            }
            return jj;
        }
    }

    public void showWeapon() {
        Joint writ = searchForJointByName("wrist", root);
        writ.setIsVisible(true);
    }

    public void hideWeapon() {

        Joint writ = searchForJointByName("wrist", root);
        writ.setIsVisible(false);
    }

    public void modifyScale(float scaleVal) {
        modifyJointScale(root, scaleVal);
    }

    private void modifyJointScale(Joint j, float scaleval) {
        j.multiplyScale(scaleval);
        for (Joint j2 : j.children) {
            modifyJointScale(j2, scaleval);
        }
    }


}
