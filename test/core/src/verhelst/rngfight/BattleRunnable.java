package verhelst.rngfight;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Orion on 10/20/2014.
 */
public class BattleRunnable implements Runnable {

    private Battle btl;
    public boolean battling;

    private final ConcurrentLinkedQueue<List<Integer>> dmgNumListA;
    private final String[] results;


    public BattleRunnable(Battle b,ConcurrentLinkedQueue<List<Integer>> dmgNumListA, String[] results){
        btl = b;
        this.dmgNumListA = dmgNumListA;
        this.results = results;
    }

    @Override
    public void run() {
        String results2 = "";
        Character rightside = btl.getRightside();
        Character leftside = btl.getLeftside();
            Random rng = new Random();
            int dmgtoboss = 0;
            int dmgtoplayer = 0;
            int hitcount = 0;

            do {
                dmgtoplayer += leftside.attack(rightside);
                dmgtoboss += rightside.attack(leftside);

                if(hitcount % 13 == 0) {
                    final List<Integer> chunk = new ArrayList<Integer>();
                    chunk.add(dmgtoboss);
                    chunk.add(dmgtoplayer);
                    chunk.add(leftside.getHealth());
                    chunk.add(rightside.getHealth());
                    chunk.add(hitcount);
                    Gdx.app.postRunnable(new Runnable(){
                        @Override
                        public void run(){
                           dmgNumListA.add(chunk);
                        }

                    });
                    dmgtoboss = 0;
                    dmgtoplayer = 0;
                }

                hitcount++;
            } while (leftside.getHealth() > 0 && rightside.getHealth() > 0);


            final List<Integer> chunk = new ArrayList<Integer>();
            chunk.add(dmgtoboss);
            chunk.add(dmgtoplayer);
            chunk.add(leftside.getHealth());
            chunk.add(rightside.getHealth());
            chunk.add(hitcount);
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run(){
                        dmgNumListA.add(chunk);
                }

            });



            if(leftside.getHealth() <= 0 && rightside.getHealth() <= 0){
                results2 = "D-D-D-DOUBLE KILL!!!";
            }
            else if (leftside.getHealth() <= 0) {
                results2 += "VICTORY! " + leftside.getName() + " defeated";
            } else {
                results2 +=  "You died, too bad.";
            }


            final String  r2 = results2;
            Gdx.app.postRunnable(new Runnable(){
                @Override
                public void run(){
                    synchronized (results) {
                        results[0] = r2;
                    }
                }

            });
            System.out.println(results2 + "\r\nhits: " + hitcount);

        //return resultString;
    }


}
