package verhelst.Comp;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Widget;

import verhelst.rngfight.Assets;

/**
 * Created by Orion on 11/4/2014.
 */
public class LLabel extends Widget {

    String text;
    Skin skin;
    Boolean wrap = false;
    Boolean isHUD = false;


    public LLabel(String text, Skin skin) {
        this.text = text;
        this.skin = skin;

    }

    public void setText(String newText) {
        this.text = newText;
    }

    public void setWrap(Boolean isWrap) {
        this.wrap = isWrap;
    }

    public Boolean getIsHUD() {
        return isHUD;
    }

    public void setIsHUD(Boolean isHUD) {
        this.isHUD = isHUD;
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        setScale(getScaleX(), getScaleY());
        if (!isHUD && !wrap)
            Assets.bf.draw(batch, text, getX() - Assets.bf.getBounds(text).width, getY());
        else if (!isHUD && wrap)
            Assets.bf.drawWrapped(batch, text, getX() - Assets.bf.getBounds(text).width / 2, getY() + Assets.bf.getBounds(text).height, getWidth() - 10);
        else
            Assets.HUDbf.draw(batch, text, getX(), getY() + Assets.bf.getBounds(text).height - 2);
    }


}
