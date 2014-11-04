package verhelst.rngfight;

import java.util.ArrayList;
import java.util.List;

import javax.rmi.CORBA.Tie;

/**
 * Created by Orion on 10/29/2014.
 */
public class BattleResultHandler {

    int playerAmaxLvl, playerBmaxLvl;

    public BattleResultHandler(){
        playerAmaxLvl = 0;
        playerBmaxLvl = 0;
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

            if(hitcount % 2 == 1) {
                resultsList.add(BattleResult.Player1GetsLoot);
            }
        }else if(a.getHealth() <= 0 && b.getHealth() > 0){
            resultsList.add(BattleResult.Player2Win);
            a.incrementLosses();
            a.resetWins();
            b.incrementWins();
            b.resetLosses();

            if(hitcount % 2 == 0) {
                resultsList.add(BattleResult.ShowStaticLoot);
            }
            else if(hitcount % 13 == 0) {
                resultsList.add(BattleResult.ShowRandomLoot);
            }
        }else{
            resultsList.add(BattleResult.Tie);
            a.resetWins();
            b.resetWins();
        }

        playerAmaxLvl = Math.max(a.getLevel(), playerAmaxLvl);
        playerBmaxLvl = Math.max(b.getLevel(), playerBmaxLvl);

        //Deal with hitcount
        if(hitcount == 101)
            resultsList.add(BattleResult.CustomMode_ioi);

        System.out.println(resultsList);
        return resultsList.toArray(new BattleResult[resultsList.size()]);
    }

    public Weapon getLoot(Character left){
        return Weapon.generateRandomWeapon(left.getLevel(), Assets.getWeaponSprite(), Weapon.POSITION.LOOT_POSITION);
    }




}
