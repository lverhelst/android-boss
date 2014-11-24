package verhelst.Comp;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Leon I. Verhelst on 11/22/2014.
 */
public class LTable extends Actor {

    ArrayList<ArrayList<LCell>> table;

    //Array List to draw cells in draw order
    ArrayList<LCell> sortedCells;


    int x, y, w, h;


    public LTable(int x, int y, int w, int h){
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        table = new ArrayList<ArrayList<LCell>>();
        table.add(new ArrayList<LCell>());
     }


    public void addRow(){
        table.add(new ArrayList<LCell>());
        computeSizes();
    }

    public LCell addActor(Actor actor){
        int last_row_index = table.size() -1;
        ArrayList<LCell> row = table.get(last_row_index);
        LCell cell = new LCell();
        cell.setActor(actor);
        row.add(cell);
        computeSizes();
        return cell;
    }

    public LCell addActor(Actor actor, int draworder){
        int last_row_index = table.size() -1;
        ArrayList<LCell> row = table.get(last_row_index);
        LCell cell = new LCell();
        cell.setActor(actor);
        cell.setDraw_position(draworder);
        row.add(cell);
        computeSizes();
        return cell;
    }

    public LCell addActor(Actor actor, boolean keep_aspect_ratio){
        int last_row_index = table.size() -1;
        ArrayList<LCell> row = table.get(last_row_index);
        LCell cell = new LCell();
        cell.setActor(actor);
        cell.setKeep_aspect_ratio(keep_aspect_ratio);
        row.add(cell);
        computeSizes();
        return cell;
    }

    //Iterate over table list and compute sizes
    private void computeSizes(){
        //Uniform row heights (for now)
        int rowheight = h/table.size();
        int columnWidth;
        int rowindex = 1;




        for(ArrayList<LCell> row : table){
            //uniform cell sizes for now

            //check if widthpercent is set
            boolean widthp = false;
            for(LCell lc : row){
               // System.out.println(lc.toString() + " " +lc.getWidth_percent());
                 widthp |= (lc.getWidth_percent() != 0);
            }
            //System.out.println("All percents set? " + widthp);

            if(!widthp) {
                if (row.size() > 0)
                    columnWidth = w / row.size();
                else
                    columnWidth = w;

                for (int i = 0; i < row.size(); i++) {
                    LCell lc = row.get(i);
                    lc.setBounds(x + (columnWidth * i), y + h - (rowheight * rowindex), columnWidth, rowheight);
                }
            }else{
                int currentw = x;
                for (int i = 0; i < row.size(); i++) {
                    LCell lc = row.get(i);

                    columnWidth = (int)((lc.getWidth_percent()/100.0) * w);
                    System.out.println("CW: " + columnWidth + " actual width: " + w + " percent: " + lc.getWidth_percent()/100);
                    lc.setBounds(currentw, y + h - (rowheight * rowindex), columnWidth, rowheight);
                    currentw += columnWidth;
                }
            }
            rowindex++;
        }
        sortCells();
    }

    private void sortCells(){
        if(sortedCells == null)
            sortedCells = new ArrayList<LCell>();
        else
            sortedCells.clear();

        for(ArrayList<LCell> row : table){
            for(LCell cell : row){
                sortedCells.add(cell);
            }
        }

       Collections.sort(sortedCells, new Comparator<LCell>() {
           @Override
           public int compare(LCell o1, LCell o2) {
               return Integer.compare(o2.getDraw_position(), o1.getDraw_position());
           }
       });
    }

    public LCell getLCellForActor(Actor a){
        for(LCell cell : sortedCells){
            if(cell.getActor().equals(a)){
                return cell;
            }
        }
        return null;
    }

    public LCell getLCellForActorName(String name){
        for(LCell cell : sortedCells){
            if(cell.getActor().getName() != null){
                System.out.println("Search names " + name + " " + cell.getActor().getName());
                if(cell.getActor().getName().equals(name)) {
                    return cell;
                }
            }
        }
        return null;
    }


    public void removeChildren(){
        table.clear();
        table.add(new ArrayList<LCell>());

        sortedCells.clear();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        for(LCell cell : sortedCells){
            cell.draw(batch, parentAlpha);
        }
    }

    @Override
    public void setX(float x) {
        if(this.x != (int)x) {
            this.x = (int) x;
            super.setX(x);
        }
    }

    @Override
    public void setY(float y) {
        if(this.y != (int)y) {
            this.y = (int) y;
            super.setY(y);
        }
    }

    @Override
    public void setPosition(float x, float y) {
        if(this.x != (int)x || this.y != (int)y) {
            setX(x);
            setY(y);
            computeSizes();
            super.setPosition(x, y);
        }
    }

    @Override
    public void setSize(float width, float height) {
        if(w != (int)width || h != (int)height) {
            w = (int) width;
            h = (int) height;
            computeSizes();
            super.setSize(width, height);
        }

    }

    @Override
    public void setVisible(boolean visible) {
        for(ArrayList<LCell> row : table){


            for(int i = 0; i < row.size(); i++) {
                LCell lc = row.get(i);
                lc.setVisible(visible);
            }

        }

        super.setVisible(visible);
    }
}
