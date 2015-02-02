
package verhelst.Craft;

/*
 * @author Leon I. Verhelst
 */
public class Mat implements Item {
        String name;
        cTOKEN type;

       
        public Mat(String nm, cTOKEN type){
           this.name = nm;
           this.type = type;
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
}
