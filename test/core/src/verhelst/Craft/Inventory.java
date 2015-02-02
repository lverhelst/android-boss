package verhelst.Craft;

/**
 * Created by Orion on 2/1/2015.
 */
public class Inventory {

    //Index
    //0 -> IRON, 1 -> CLOTH, 2->DUST, 3-BONE
    static int[] counts = new int[4];

    public static void addItem(int index){
        if(index >= 0 && index <= 3)
            counts[index]++;
    }

    public static void removeItem(int index){
        if(index >= 0 && index <= 3)
            counts[index]++;
    }
}
