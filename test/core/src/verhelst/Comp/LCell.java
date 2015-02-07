package verhelst.Comp;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;

import verhelst.CustomActors.BodyPartActor;
import verhelst.rngfight.Assets;

import static verhelst.CustomActors.BodyPartActor.BodyPartType.LOWERARM;
import static verhelst.CustomActors.BodyPartActor.BodyPartType.UPPERARM;

/**
 * Created by Leon I. Verhelst on 11/22/2014.
 */
public class LCell extends Actor {

    int x_coord, y_coord;
    int width, height;
    int draw_position;
    int width_percent;
    boolean keep_aspect_ratio, glow;
    int glow_type;
    Actor actor;

    public LCell() {
        this.draw_position = 1;
    }

    public void setActor(Actor actor) {
        this.actor = actor;
    }

    public Actor getActor() {
        return actor;
    }

    public void setKeep_aspect_ratio(boolean keep_ratio) {
        this.keep_aspect_ratio = keep_ratio;
    }

    public void setDraw_position(int draw_position) {
        this.draw_position = draw_position;
    }

    public int getDraw_position() {
        return draw_position;
    }

    public void setWidth_percent(int width_percent) {
        this.width_percent = width_percent;
    }

    public int getWidth_percent() {
        return this.width_percent;
    }

    public void setBounds(int x, int y, int width, int height) {
        this.x_coord = x;
        this.y_coord = y;
        this.width = width;
        this.height = height;


    }

    @Override
    public void draw(Batch batch, float parentAlpha) {

        if (glow) {
            if (glow_type == 1)
                batch.draw(Assets.glow_ylw, getX(), getY(), getWidth(), getHeight());
            if (glow_type == 2)
                batch.draw(Assets.glow, getX(), getY(), getWidth(), getHeight());
        }


        if (!keep_aspect_ratio) {
            actor.setSize(width, height);
            actor.setPosition(x_coord, y_coord);
        } else {
            //keep aspect ratio of actor
            float w = actor.getWidth();
            float h = actor.getHeight();
            float x_scale = width / w;
            float y_scale = height / h;

            float scale = Math.min(x_scale, y_scale);
            if (actor instanceof BodyPartActor) {
                //   System.out.println("w " + w + " h " + h + " xs " + x_scale + " ys " + y_scale + " s " + scale + "\r\n " + "x_coor " + x_coord + " witdh" + width + "ycoord " + y_coord + " " + height +
                // "\r\n w" + (w * scale) + " h" + (h * scale));
                scale *= 0.75;
                if (((BodyPartActor) actor).getBtype() == UPPERARM || ((BodyPartActor) actor).getBtype() == LOWERARM) {
                    scale *= 0.55;
                }
            }

            actor.setSize(w * scale, h * scale);
            actor.setOrigin(w * scale / 2, h * scale / 2);
            actor.setPosition(x_coord + (width - (w * scale)) / 2, y_coord + (height - (h * scale)) / 2);

        }

        if (actor.isVisible())
            actor.draw(batch, parentAlpha);
        //   System.out.println(actor.toString() + " x: " + x_coord + " y: " + y_coord + " w: " + width + " h: " + height);
        //TODO: Add debug
        if (false) {
            batch.end();
            if (sr == null)
                sr = new ShapeRenderer();
            sr.begin(ShapeRenderer.ShapeType.Line);
            sr.setColor(Color.WHITE);
            sr.rect(x_coord, y_coord, width, height);
            sr.setColor(Color.RED);
            sr.rect(actor.getX(), actor.getY(), actor.getWidth(), actor.getHeight());

            sr.end();
            batch.begin();
        }
    }

    ShapeRenderer sr;

    @Override
    public void setVisible(boolean visible) {
        this.actor.setVisible(visible);
        super.setVisible(visible);
    }

    @Override
    public float getX() {
        return x_coord;
    }

    @Override
    public float getY() {
        return y_coord;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    /***
     *
     * Glow shit
     */
    public int getGlow_type() {
        return glow_type;
    }

    public void setGlow_type(int glow_type) {
        this.glow_type = glow_type;
    }

    public boolean isGlow() {
        return glow;
    }

    public void setGlow(boolean glow) {
        this.glow = glow;
    }

    @Override
    public void setColor(Color color){
        this.actor.setColor(color);
    }
}
