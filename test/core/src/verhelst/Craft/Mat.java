
package verhelst.Craft;

import com.badlogic.gdx.scenes.scene2d.Actor;

import verhelst.CustomActors.SpriteActor;
import verhelst.rngfight.Assets;

/*
 * @author Leon I. Verhelst
 */
public class Mat extends Actor implements Item {
        String name;
        cTOKEN type;
        SpriteActor actor;


       
        public Mat(String nm, cTOKEN type){
           this.name = nm;
           this.type = type;
            actor = new SpriteActor(Assets.getMatSprite(type));
        }

        public String getSName(){
            return name;
        }

        @Override
        public cTOKEN getCTOKEN() {
            return type;
        }

        public cTOKEN getType(){
                return type;
         }
        
        @Override
        public String toString(){
            return name;
        }

        @Override
        public CraftableType getCraftableType() {
            return CraftableType.SIMPLE;
        }

        @Override
        public Integer getIntegerValue(String attribute) {
            return null;
        }

        @Override
        public void setIntegerValue(String attribute, Integer value) {
            //Do Nothing
        }

        @Override
        public Actor getActor() {
            return actor;
        }


}
