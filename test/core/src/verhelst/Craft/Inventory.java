package verhelst.Craft;

/**
 * Created by Orion on 2/1/2015.
 */
public class Inventory {

    //Index
    //0 -> IRON, 1 -> CLOTH, 2->DUST, 3-BONE
    static int[] counts = {0,0,0,0};

    public static boolean addItem(int index){
        if(index >= 0 && index <= 3 && counts[index] < 99) {
            counts[index] = ++counts[index];
            return true;
        }
        return false;
    }

    public static boolean setCount(int index, int count)
    {

        if(index >= 0 && index <= 3 && count < 100 && count >= 0) {
            counts[index] = count;
            return true;
        }
        return false;
    }

    public static boolean removeItem(int index){
        if(index >= 0 && index <= 3 && counts[index] > 0) {
            counts[index] = --counts[index];
            return true;
        }
        return false;
    }


    public static int getCount(int index){
        if(index >= 0 && index <= 3)
            return counts[index];
        return 0;
    }

    public static void reset(){
        for(int i : counts)
            counts[i] = 0;
    }
}
