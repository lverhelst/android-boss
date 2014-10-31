package verhelst.rngfight;

import java.util.ArrayList;
import java.util.List;

import javax.rmi.CORBA.Tie;

/**
 * Created by Orion on 10/29/2014.
 */
public class BattleResultHandler {

    

    public BattleResultHandler(){

    }


    public BattleResult[] getResults(Character a, Character b, int hitcount){
        List<BattleResult> resultsList = new ArrayList<BattleResult>();
        //Decide winner
        if(a.getHealth() > 0 && b.getHealth() <= 0){
            resultsList.add(BattleResult.Player1Win);
            a.incrementWins();
            a.resetLosses();
            b.resetWins();
         
        }else if(a.getHealth() <= 0 && b.getHealth() > 0){
            resultsList.add(BattleResult.Player2Win);
            a.resetWins();
            b.incrementWins();
            b.resetLosses();
        }else{
            resultsList.add(BattleResult.Tie);
            a.resetWins();
            b.resetWins();
        }

        //Deal with hitcount
        if(hitcount == 101)
            resultsList.add(BattleResult.CustomMode_ioi);
        if(hitcount % 2 == 0)
            resultsList.add(BattleResult.ShowStaticLoot);
        else if(hitcount % 13 == 0)
            resultsList.add(BattleResult.ShowRandomLoot);

        return resultsList.toArray(new BattleResult[resultsList.size()]);
    }






}
