package verhelst.bones;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

/**
 * Created by Orion on 11/11/2014.
 */
public class Model {

    Joint root;

    ShapeRenderer sr = new ShapeRenderer();

    public Model(int abs_x, int abs_y){
        //Pseudo human torso, side view
        root = new Joint(abs_x,abs_y, null);
        root.addChild(new Joint(10,-10, root));
        root.children.get(0).addChild(new Joint(0,-10,root.children.get(0)));
        root.addChild(new Joint(0, -20, root));
        root.children.get(1).addChild(new Joint(0,-20, root.children.get(1)));
        root.print();
    }

    public void render(Batch batch){
        sr.begin(ShapeRenderer.ShapeType.Line);
        root.render(sr);
        sr.end();

    }

}
