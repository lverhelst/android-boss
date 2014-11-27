package verhelst.rngfight;

import com.badlogic.gdx.Gdx;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * Created by Orion on 10/16/2014.
 */
public class Battle implements Runnable {
    private Character leftside;
    private Character rightside;
    private boolean battling = false;

    private final ConcurrentLinkedQueue<List<Integer>> dmgNumListA;

    public Battle(Character challenger, Character challengee,ConcurrentLinkedQueue<List<Integer>> dmgNumListA){
        this.leftside = challenger;
        this.rightside = challengee;
        this.dmgNumListA = dmgNumListA;
    }


    public Character getLeftside() {
        return leftside;
    }

    public Character getRightside() {
        return rightside;
    }



    @Override
    public void run() {
        String results2 = "";
        //reset fighters (can be put into battle maybe?)
        rightside.reset();
        leftside.reset();
        //Clear Battle-Consuming Queue
        dmgNumListA.clear();
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
            if(hitcount > 2147483647){
                if(leftside.getHealth() <= rightside.getHealth()){
                    leftside.setHealth(0);
                }else{
                    rightside.setHealth(0);
                }
            }

            System.out.println(hitcount);
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
    }
}
