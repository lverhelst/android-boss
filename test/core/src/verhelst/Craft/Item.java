package verhelst.Craft;

import com.badlogic.gdx.scenes.scene2d.Actor;

/**
 * Created by Orion on 2/1/2015.
 */
public interface Item {
    public CraftableType getCraftableType();
    public cTOKEN getCTOKEN();
    public Integer getIntegerValue(String attribute);
    public void setIntegerValue(String attribute, Integer value);
    public Actor getActor();
}
