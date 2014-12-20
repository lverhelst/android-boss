package verhelst.rngfight;

import java.util.concurrent.ConcurrentLinkedQueue;

import verhelst.CustomActors.Character;

/**
 * Created by Orion on 10/16/2014.
 */
public class Battle implements Runnable {
    private verhelst.CustomActors.Character leftside;
    private Character rightside;
    private boolean battling = false;

    private final ConcurrentLinkedQueue<int[]> dmgNumListA;

    public Battle(Character challenger, Character challengee, ConcurrentLinkedQueue<int[]> dmgNumListA) {
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
        float healing_scaler = (float) 1.0;

        int[] chunk;
        do {
            /*if(dmgNumListA.size() > 500){
                try {

                    Thread.currentThread().sleep(1);
                }catch(Exception e){
                    //don't do shit all
                }
                continue;
            }*/


            dmgtoplayer += leftside.attack(rightside, healing_scaler);
            dmgtoboss += rightside.attack(leftside, healing_scaler);
            /*leftside.setHealth(1);
            rightside.setHealth(1);
            if(hitcount > 30000000){
                leftside.setHealth(0);
                rightside.setHealth(0);
            }*/


            if (hitcount % Math.max(1, (int) Math.log(hitcount)) == 0) {
                //System.out.println("Hitcount " + hitcount + " " + (int)Math.log(hitcount));

                chunk = new int[5];
                chunk[0] = dmgtoboss;
                chunk[1] = dmgtoplayer;
                chunk[2] = leftside.getHealth();
                chunk[3] = rightside.getHealth();
                chunk[4] = hitcount;
                //    Gdx.app.postRunnable(new Runnable(){
                //        @Override
                //       public void run(){

                dmgNumListA.add(chunk);

                //     }

                // });
                dmgtoboss = 0;
                dmgtoplayer = 0;
            }

            hitcount++;
            /*if(hitcount > Integer.MAX_VALUE - 1){
                if(leftside.getHealth() <= rightside.getHealth()){
                    leftside.setHealth(0);
                }else{
                    rightside.setHealth(0);
                }
            }*/
            //reduce lifesteal to ensure games don't run too long
            if (hitcount % 500000 == 0 && healing_scaler > 0.0) {
                healing_scaler -= 0.001;
                if (healing_scaler < 0)
                    healing_scaler = 0;
                System.out.println("Healing scaler" + " " + healing_scaler);
            }

        } while (leftside.getHealth() > 0 && rightside.getHealth() > 0);
        System.out.println(hitcount);
        chunk = new int[5];
        chunk[0] = dmgtoboss;
        chunk[1] = dmgtoplayer;
        chunk[2] = leftside.getHealth();
        chunk[3] = rightside.getHealth();
        chunk[4] = hitcount;
        //Gdx.app.postRunnable(new Runnable(){
        //  @Override
        //  public void run(){
        dmgNumListA.add(chunk);

        //  }

        //});
    }
}
