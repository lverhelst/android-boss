
package verhelst.Craft;

import verhelst.CustomActors.BodyPartActor;
import verhelst.rngfight.BattleResultHandler;
import verhelst.rngfight.RngFight;
import verhelst.rngfight.Weapon;

/*
 * @author Leon I. Verhelst
 */
public class Rule {        
        /*
         * Rule evaluate
         *   Check:
         *      IF a Generated on LeftSide then modify (First) generated
         *      
         *      if(generated in leftside){
         *          rightside.generated = leftside.generated;
         *          rightside.evaluate;
         *      }
         * 
         */
        cTOKEN l, r, o;
        
        public Rule(cTOKEN ls, cTOKEN rs, cTOKEN result){
            
           l = ls;
           r = rs;
           o = result;
        }
        
        
        
        public Item evaluate(){
             Item item = null;
             switch(o){
                 case IRON:
                 case CLOTH:
                 case BONE:
                 case DUST:
                     item = new Mat(o.name(),o);
                     break;
                 case WEAPON:
                     item = Weapon.generateRandomWeapon(RngFight.lvl, Weapon.POSITION.LOOT_POSITION);
                     break;
                 case BODYPART:
                     item = BodyPartActor.generateRandomBodyPart();
                     break;
                 case HEAD:
                     item = BodyPartActor.generateBodyPartForType(BodyPartActor.BodyPartType.HEAD);
                     break;
                 case TORSO:
                     item = BodyPartActor.generateBodyPartForType(BodyPartActor.BodyPartType.TORSO);
                     break;
                 case LEGS:
                     item = BodyPartActor.generateBodyPartForType(BodyPartActor.BodyPartType.LEGS);
                     break;
                 case UPPERARM:
                     item = BodyPartActor.generateBodyPartForType(BodyPartActor.BodyPartType.UPPERARM);
                     break;
                 case LOWERARM:
                     item = BodyPartActor.generateBodyPartForType(BodyPartActor.BodyPartType.LOWERARM);
                     break;
             }
             return item;
        }
 
        
        
        @Override
        public String toString(){
            return l.toString()+ "+" + r.toString() + "="+o.toString();
        }
}
