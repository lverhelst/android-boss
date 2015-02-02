package verhelst.Craft;

/**
 * Created by Orion on 2/1/2015.
 */
public class ScriptRule {
    cTOKEN ls, rs;
    String script;

    public ScriptRule(cTOKEN l, cTOKEN r, String code){
        ls = l;
        rs = r;
        script = code;
    }

    public Item evaluate(Item item){
        return VM.executeScript(item, script);
    }
}
