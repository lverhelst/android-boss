
package verhelst.Craft;

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
                     break;
                 case WEAPON:
                     item = Weapon.generateRandomWeapon(100, Weapon.POSITION.LOOT_POSITION);
                     break;
                 case BODYPART:
                     break;
                 case HEAD:
                     break;
                 case TORSO:
                     break;
                 case LEGS:
                     break;
                 case UPPERARM:
                     break;
                 case LOWERARM:
                     break;
             }
             return item;
        }
 
        
        
        @Override
        public String toString(){
            return l.toString()+ "+" + r.toString() + "="+o.toString();
        }
}
