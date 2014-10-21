package verhelst.rngfight;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;
import javax.swing.SwingWorker;

/**
 * Created by Orion on 10/19/2014.
 */
public class BattleSwingWorker extends SwingWorker<String, List<Integer>> {
    private Battle btl;
    public boolean battling;

    private final ConcurrentLinkedQueue<List<Integer>> dmgNumListA;




    public BattleSwingWorker(Battle b,ConcurrentLinkedQueue<List<Integer>> dmgNumListA){
        btl = b;
        this.dmgNumListA = dmgNumListA;
    }


    @Override
    protected String doInBackground() throws Exception {
        String resultString = "Results:   ";
        Character rightside = btl.getRightside();
        Character leftside = btl.getLeftside();
        if(!battling) {
            battling = true;
            Random rng = new Random();
            int dmgtoboss = 0;
            int dmgtoplayer = 0;
            int hitcount = 0;

            String bossline = "LEFT";
            String playerline = "RIGHT";
            do {
                dmgtoplayer = (rng.nextInt(2) == 0 ? -1 : 1) * rng.nextInt(rightside.getMax_dmg()) + rightside.getMin_dmg();

                dmgtoboss = (rng.nextInt(2) == 0 ? -1 : 1) * rng.nextInt(leftside.getMax_dmg()) + leftside.getMin_dmg();
                rightside.applyDamageOrHealth(dmgtoplayer);
                leftside.applyDamageOrHealth(dmgtoboss);


                bossline += "\r\nBoss life: " + String.format("%05d", leftside.getHealth()) + " dmgtoboss " + (dmgtoboss < 0 ? "" : " ") + String.format("%03d", dmgtoboss);
                playerline +=  "\r\nPlayer life: " + String.format("%05d", rightside.getHealth()) + " dmgtoplayer " + (dmgtoplayer < 0 ? "" : " ") + String.format("%02d", dmgtoplayer);
                String[] str = {bossline, playerline};
                //Log.d("to post", bossline);
                //Log.d("to post", playerline);
               // if(hitcount % 10 == 9) {
                    List<Integer> chunk = new ArrayList<Integer>();
                    chunk.add(dmgtoboss);
                    chunk.add(dmgtoplayer);
                    chunk.add(leftside.getHealth());
                    chunk.add(rightside.getHealth());
                    chunk.add(hitcount);
                    publish(chunk);
                    bossline = "";
                    playerline = "";
                //}

                hitcount++;
            } while (leftside.getHealth() > 0 && rightside.getHealth() > 0);

            if (leftside.getHealth() < 0) {
                resultString += playerline += "\r\n" + "VICTORY! Boss defeated";
            } else {
                resultString += playerline += "\r\n" + "You died, too bad.";
            }
            System.out.println("hits: " + hitcount);
        }
        return resultString;
    }

    @Override
    protected void process(List<List<Integer>> chunks) {
        int dmgA = 0;
        int dmgB = 0;
        int split = 0;

        for(List<Integer> dmglist : chunks){
               synchronized (dmgNumListA) {
                   dmgNumListA.add(dmglist);
               }
        }

    }

    @Override
    protected void done() {
        System.out.println("Done");
        battling = false;
        super.done();
    }
}
