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
            b.incrementLosses();
        }else if(a.getHealth() <= 0 && b.getHealth() > 0){
            resultsList.add(BattleResult.Player2Win);
            a.incrementLosses();
            a.resetWins();
            b.incrementWins();
            b.resetLosses();

            if(hitcount % 3  == 0) {
                resultsList.add(BattleResult.ShowStaticLoot);
            }else if(hitcount % 7 == 0) {
                resultsList.add(BattleResult.HeadLoot);
            }

        }else{
            resultsList.add(BattleResult.Tie);
            a.resetWins();
            b.resetWins();
        }

        if(hitcount % 2 == 1) {
            resultsList.add(BattleResult.Player1GetsLoot);
        }



        //Deal with hitcount
        if(hitcount == 101)
            resultsList.add(BattleResult.CustomMode_ioi);

        System.out.print(resultsList);
        System.out.println("        CL " + a.getLevel() + " ws " + a.getWin_streak() + " ml" + a.max_level + " mwtl" + a.max_wtnl + " ls " + a.getLose_streak() + " lscheck" + a.getLose_streak() % (a.wins_to_level + 1));
        return resultsList.toArray(new BattleResult[resultsList.size()]);
    }

}
