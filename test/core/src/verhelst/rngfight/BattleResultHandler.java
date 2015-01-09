package verhelst.rngfight;

import java.util.ArrayList;
import java.util.List;

import verhelst.CustomActors.Character;

/**
 * Created by Orion on 10/29/2014.
 */
public class BattleResultHandler {

    protected int max_hitcount, min_hitcount, max_level_reached, games, player2wins, player2losses, draws, score, max_score;
    public int[] stats = new int[8];

    /*
        <string name="app_id">225240267219</string>
          <string name="achievement_its_over_9000">CgkI0_P2iscGEAIQAg</string>
          <string name="achievement_millennial">CgkI0_P2iscGEAIQAw</string>
          <string name="achievement_lucky">CgkI0_P2iscGEAIQBA</string>
          <string name="achievement_quikfite">CgkI0_P2iscGEAIQBQ</string>
          <string name="achievement_achievement_unlocked">CgkI0_P2iscGEAIQBg</string>
          <string name="leaderboard_leaderboard">CgkI0_P2iscGEAIQAA</string>
    

     */

    public BattleResultHandler() {
        reset();
    }


    public BattleResult[] getResults(verhelst.CustomActors.Character a, Character b, int hitcount) {
        games++;
        List<BattleResult> resultsList = new ArrayList<BattleResult>();
        //Decide winner
        System.out.println("BRH: " + a.getHealth() + " " + b.getHealth() + " Hits " + hitcount);

        int aoriglvl = a.getLevel();
        double winscaler;
        if (a.getHealth() > 0 && b.getHealth() <= 0) {
            resultsList.add(BattleResult.Player1Win);
            a.incrementWins();
            b.incrementLosses();
            player2losses++;
            winscaler = 0.5;

        } else if (a.getHealth() <= 0 && b.getHealth() > 0) {
            resultsList.add(BattleResult.Player2Win);
            a.incrementLosses();
            b.incrementWins();
            player2wins++;
            if (hitcount % 3 == 0) {
                resultsList.add(BattleResult.ShowStaticLoot);
            } else if (hitcount % 7 == 0) {
                resultsList.add(BattleResult.HeadLoot);
            }
            winscaler = 1.0;
        } else {
            resultsList.add(BattleResult.Tie);
            a.resetWins();
            b.resetWins();
            draws++;
            winscaler = 0.55;
        }
        if (hitcount % 4 == 1 || (aoriglvl != a.getLevel() && a.getLevel() % 2 == 0))
            resultsList.add(BattleResult.Player1GetsLoot);


        if ((int) (aoriglvl /5) != (int) (a.getLevel() / 5)) {

            resultsList.add(BattleResult.Player1NewSuit);

        }
        //Deal with hitcount
        if (hitcount % 101 == 0)
            resultsList.add(BattleResult.CustomMode_ioi);

        if (max_hitcount == -1 || hitcount > max_hitcount) {
            resultsList.add(BattleResult.NewMaxHitCount);
            max_hitcount = hitcount;
        }
        if (min_hitcount == -1 || hitcount < min_hitcount) {
            resultsList.add(BattleResult.NewMinHitCount);
            min_hitcount = hitcount;
        }

        max_level_reached = a.getMax_level();

        System.out.println(resultsList);
        // System.out.println("        CL " + a.getLevel() + " ws " + a.getWin_streak() + " ml" + a.max_level + " mwtl" + a.max_wtnl + " ls " + a.getLose_streak() + " lscheck" + a.getLose_streak() % (a.wins_to_level + 1));
        score = (int)(winscaler * hitcount * (1 + player2wins/(games)) * a.getLevel()/a.getMax_level() * a.getMax_level()/25);
        if(score > max_score){
            max_score = score;
            resultsList.add(BattleResult.NewHighScore);
        }

        stats[0] = max_hitcount;
        stats[1] = min_hitcount;
        stats[2] = player2wins;
        stats[3] = player2losses;
        stats[4] = draws;
        stats[5] = games;
        stats[6] = max_level_reached;
        stats[7] = max_score;



        if(score > 9000)
            RngFight.actionResolver.unlockAchievement("its_over_9000");


        if(games == 1000)
            RngFight.actionResolver.unlockAchievement("millennial");
        if(games == 500 && ((float)player2wins/player2losses >= 1.0))
            RngFight.actionResolver.unlockAchievement("lucky");
        if(hitcount < 25)
            RngFight.actionResolver.unlockAchievement("quickfite");


        return resultsList.toArray(new BattleResult[resultsList.size()]);
    }


    public void reset(){
        try {

            max_hitcount = SaveGame.stats[0];
            min_hitcount = SaveGame.stats[1];
            player2wins = SaveGame.stats[2];
            player2losses = SaveGame.stats[3];
            draws = SaveGame.stats[4];
            games = SaveGame.stats[5];
            max_level_reached = SaveGame.stats[6];
            max_score = SaveGame.stats[7];

        } catch (Exception e) {
            System.out.println("Could not load stats");

            max_hitcount = -1;
            min_hitcount = -1;
            games = 0;
            player2wins = 0;
            player2losses = 0;
            draws = 0;
            max_level_reached = 0;
            max_score = 0;
        }
        stats[0] = max_hitcount;
        stats[1] = min_hitcount;
        stats[2] = player2wins;
        stats[3] = player2losses;
        stats[4] = draws;
        stats[5] = games;
        stats[6] = max_level_reached;
        stats[7] = max_score;
    }

    public int getScore(int hits, Character a){
        return (int)(0.5 * hits * (1 + player2wins/(games == 0 ? 1: games)) * a.getLevel()/a.getMax_level() * a.getMax_level()/25);
    }

    public int getMax_score(){
        return max_score;
    }

}
