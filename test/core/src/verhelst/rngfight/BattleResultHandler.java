package verhelst.rngfight;

import java.util.ArrayList;
import java.util.List;

import javax.rmi.CORBA.Tie;

/**
 * Created by Orion on 10/29/2014.
 */
public class BattleResultHandler {

    private int max_hitcount, min_hitcount;


    public BattleResultHandler(){
        max_hitcount = -1;
        min_hitcount = -1;
    }


    public BattleResult[] getResults(Character a, Character b, int hitcount){
        List<BattleResult> resultsList = new ArrayList<BattleResult>();
        //Decide winner


        int aoriglvl = a.getLevel();

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

            if(hitcount % 3  == 0 ) {
               resultsList.add(BattleResult.ShowStaticLoot);
            }else if(hitcount % 7 == 0) {
                resultsList.add(BattleResult.HeadLoot);
            }

        }else{
            resultsList.add(BattleResult.Tie);
            a.resetWins();
            b.resetWins();
        }

          if (hitcount % 4 == 1 || aoriglvl != a.getLevel())
                resultsList.add(BattleResult.Player1GetsLoot);


        System.out.println("BOSSSSUIT : " + aoriglvl + " max: " + a.max_level + " ");
        if((int)(aoriglvl / 10) != (int)(a.getLevel() /10)){

            resultsList.add(BattleResult.Player1NewSuit);

        }
        //Deal with hitcount
        if(hitcount == 101)
            resultsList.add(BattleResult.CustomMode_ioi);

        if(max_hitcount == -1 || hitcount > max_hitcount)
        {
            resultsList.add(BattleResult.NewMaxHitCount);
            max_hitcount = hitcount;
        }
        if(min_hitcount == -1 || hitcount < min_hitcount){
            resultsList.add(BattleResult.NewMinHitCount);
            min_hitcount = hitcount;
        }


        System.out.print(resultsList);
       // System.out.println("        CL " + a.getLevel() + " ws " + a.getWin_streak() + " ml" + a.max_level + " mwtl" + a.max_wtnl + " ls " + a.getLose_streak() + " lscheck" + a.getLose_streak() % (a.wins_to_level + 1));
        return resultsList.toArray(new BattleResult[resultsList.size()]);
    }

}
