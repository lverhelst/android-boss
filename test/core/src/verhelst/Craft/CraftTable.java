package verhelst.Craft;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 *
 * @author Leon Verhelst
 */
public class CraftTable {

     HashMap<String, Mat> mats = new HashMap<String,Mat>();
     HashMap<String, String> instructions = new HashMap<String, String>();
     ArrayList<Rule> rules = new ArrayList<Rule>();
     ArrayList<ScriptRule> scriptrules = new ArrayList<ScriptRule>();

     String fileAsString;
     String[] fileAsLines;
    
    
    public CraftTable() {
        loadRules();
    }

    public Item craftItem(LinkedList<Item> items){
        while(items.size() > 1){
            Item a = items.removeFirst();
            Item b = items.removeFirst();

            System.out.println(a.getCTOKEN() + "  " + b.getCTOKEN());

            if(a.getCraftableType() == CraftableType.COMPOUND){
                items.addFirst(getScriptRuleForMats(a.getCTOKEN(),b.getCTOKEN()).evaluate(a));
            }else{
                items.addFirst(getRuleForMats(a.getCTOKEN(),b.getCTOKEN()).evaluate());
            }
        }
        return items.get(0);
    }
    
    
    /**
     * TODO: Make a proper tokenizer && parser
     */
    private void loadRules(){
       // try{
            
                //BufferedReader br = new BufferedReader(new FileReader("rules.txt"));
                FileHandle fh = Gdx.files.internal("rules.txt");


                //Load the file into our string buffer
                //String line;
                //while((line = br.readLine()) != null)
                 //   fileAsString += "\n" + line;
                //fileAsString += '\u001a';
                fileAsLines = fh.readString().split("\n");
                
                //state: -1 = Started, 0 = reading rules, 1 = reading instructions
                int state = -1;
                 
                //Load instructions
                for(String ln : fileAsLines){
                    if(ln.startsWith("INSTRUCTIONS")){
                        state = 0;
                        continue;
                    }
                    if(state == 0 && !ln.startsWith("}")){
                        String[] inst = ln.split(":");
                        instructions.put(inst[0],inst[1]);
                    }else{
                        state = -1;
                    }
                }
                //Load rules
                
                state = -1;
                for(String ln : fileAsLines){
                    //skip line comments
                    if(ln.startsWith("--"))
                        continue;
                    
                    
                    if(ln.startsWith("RULES")){
                        state = 0;
                        continue;
                    }
                    if(ln.startsWith("INSTRUCTIONS")){
                        state = 1;
                        continue;
                    }
                    
                    if(state == 0 && !ln.startsWith("}"))
                    {
                        //parse a rule
                        String[] sides = ln.split("=");
                        
                        String[] ls = sides[0].split("\\+");
                        mats.get(ls[0]);
                        mats.get(ls[1]);
                        
                        String[] rss = ls[1].split("\\|");
                        for(String sec : rss){
                            if(sides[1].startsWith("INST")){
                                String instKey = sides[1].replace("\r","").replace("INST(","").replace(");","");
                                scriptrules.add(new ScriptRule(cTOKEN.valueOf(ls[0]), cTOKEN.valueOf(sec), instructions.get(instKey)));
                            }else{

                                rules.add(new Rule(cTOKEN.valueOf(ls[0]), cTOKEN.valueOf(sec), cTOKEN.valueOf(sides[1].substring(0,sides[1].indexOf(';')))));
                            }
                        }
                    }

                }
       // }catch(IOException e){
         //      System.out.print("Administrative Console: " + e.toString());
       // }
    }
    
    
    public Rule getRuleForMats(cTOKEN m, cTOKEN m2){
        for(Rule r : rules){
            if(r.l.equals(m)
                    && r.r.equals(m2)){
                return r;
            }
        }
        return null;
    }

    public ScriptRule getScriptRuleForMats(cTOKEN m, cTOKEN m2){
        if(m.equals(cTOKEN.HEAD) || m.equals(cTOKEN.TORSO) ||
                m.equals(cTOKEN.LEGS) || m.equals(cTOKEN.UPPERARM) || m.equals(cTOKEN.LOWERARM))
            m = cTOKEN.BODYPART;




        for(ScriptRule r : scriptrules){
            if(r.ls.equals(m)
                    && r.rs.equals(m2)) {
                return r;
            }
        }
        return null;
    }
}

