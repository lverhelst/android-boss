package verhelst.rngfight;

import java.util.Random;
import java.util.concurrent.BlockingQueue;

/**
 * Created by Orion on 10/16/2014.
 */
public class Battle  {
    private Character leftside;
    private Character rightside;
    private boolean battling = false;

    public Battle(Character challenger, Character challengee){
        this.leftside = challenger;
        this.rightside = challengee;
    }


    public Character getLeftside() {
        return leftside;
    }

    public Character getRightside() {
        return rightside;
    }
}
