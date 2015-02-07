
package verhelst.Craft;

import java.util.Random;
import java.util.Stack;

import verhelst.CustomActors.BodyPartActor;
import verhelst.rngfight.RngFight;


/*
 * @author Leon I. Verhelst
 */
public class VM {
    
    static Stack vmStack = new Stack();
    
    public static Item executeScript(Item item, String code){
        String[] instructions = code.split(";");
        
        for(int i = 0; i < instructions.length; i++){
            if(instructions[i].equals("LIT")){
                vmStack.push(Integer.parseInt(instructions[++i]));
            }
            if(instructions[i].equals("STR")){
                vmStack.push(instructions[++i]);
            }
            if(instructions[i].equals("DIV")){
                int a = Integer.parseInt(vmStack.pop().toString());
                int b = Integer.parseInt(vmStack.pop().toString());
                vmStack.push(b/a);
            }
            if(instructions[i].equals("ADD")){
                int a = Integer.parseInt(vmStack.pop().toString());
                int b = Integer.parseInt(vmStack.pop().toString());
                vmStack.push(b + a);
            }
            if(instructions[i].equals("SUB")){
                int a = Integer.parseInt(vmStack.pop().toString());
                int b = Integer.parseInt(vmStack.pop().toString());
                vmStack.push(b - a);
            }
            if(instructions[i].equals("MULT")){
                int a = Integer.parseInt(vmStack.pop().toString());
                int b = Integer.parseInt(vmStack.pop().toString());
                vmStack.push(b * a);
            }
            if(instructions[i].equals("MIN")){
                int a = Integer.parseInt(vmStack.pop().toString());
                int b = Integer.parseInt(vmStack.pop().toString());
                vmStack.push(Math.min(a,b));
            }
            if(instructions[i].equals("MAX")){
                int a = Integer.parseInt(vmStack.pop().toString());
                int b = Integer.parseInt(vmStack.pop().toString());
                vmStack.push(Math.max(a,b));
            }
            if(instructions[i].equals("SET")){
                String c = (String)vmStack.pop();
                int val = Integer.parseInt(vmStack.pop().toString());
                item.setIntegerValue(c, val);
            }
            if(instructions[i].equals("GET")){
                String c = (String)vmStack.pop();
                vmStack.push(item.getIntegerValue(c));
            }
            if(instructions[i].equals("GETLEVEL")){
                vmStack.push(RngFight.lvl);
            }
            if(instructions[i].equals("ROLL")){
                int val = Integer.parseInt(vmStack.pop().toString());
                vmStack.push(new Random().nextInt(val) + 1);
            }
            if(instructions[i].equals("BODYPART")){
                int val = Integer.parseInt(vmStack.pop().toString());
                //TODO: return bodypart for integer
                item = BodyPartActor.generateRandomBodyPart();
            }
            System.out.println(instructions[i] + ": " + vmStack.toString());
        }
        return item;
    }
    
    
    
    
    

}
