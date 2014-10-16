package verhelst.bossfight;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.Random;
import java.util.LinkedList;
import android.os.Handler;



public class TextGame extends Activity {
    private static Boolean battling = false;
    private LinkedList<String> bosslist;
    private LinkedList<String> playerlist;
    private final int QUEUE_LENGTH = 6;


    private int mInterval = 50;
    private Handler mHandler;
    private TextView bossText;
    private TextView playerText;
    private ScrollView bossScrollView;
    private ScrollView playerScrollView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text_game);
        //Get items for updates
        bossText = (TextView) findViewById(R.id.bosstextarea);
        playerText = (TextView) findViewById(R.id.playertextarea);
        bossScrollView = (ScrollView) findViewById(R.id.bossscrollview);
        playerScrollView = (ScrollView) findViewById(R.id.playerscrollview);
        bossScrollView.setForegroundGravity(Gravity.BOTTOM);
        mHandler = new Handler();

        bosslist = new LinkedList<String>();
        playerlist = new LinkedList<String>();


        bossScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("onTouch", "onTouchActivated!!! isBattling? " + battling);
                battle();
                return true;
            }
        });

        playerScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                Log.d("onTouch", "onTouchActivated!!! isBattling? " + battling);
                battle();
                return true;
            }
        });

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.text_game, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }




    private void battle(){
        if(!battling) {
            battling = true;
            int bosslife = 1000;
            int playerlife = 100;
            Random rng = new Random();
            int dmgtoboss = 0;
            int dmgtoplayer = 0;
            int hitcount = 0;
            String bossline;
            String playerline;
            do {
                dmgtoboss = (rng.nextInt(2) == 0 ? -1 : 1) * rng.nextInt(11);
                bosslife += dmgtoboss;
                dmgtoplayer = (rng.nextInt(2) == 0 ? -1 : 1) * rng.nextInt(11);
                playerlife += dmgtoplayer;

                bossline = "Boss life: " + String.format("%05d", bosslife) + " dmgtoboss " + (dmgtoboss < 0 ? "" : " ") + String.format("%03d", dmgtoboss);
                playerline =  " Player life: " + String.format("%05d", playerlife) + " dmgtoplayer " + (dmgtoplayer < 0 ? "" : " ") + String.format("%02d", dmgtoplayer);
                Log.d("ToPost: ", bossline);
                Log.d("ToPost: ", playerline);
                //add queue
                if(bosslist.size() > QUEUE_LENGTH){
                    bosslist.removeLast();
                }
                bosslist.add(bossline);
                //add queue
                if(playerlist.size() > QUEUE_LENGTH){
                    playerlist.removeLast();
                }
                playerlist.add(playerline);

                updatetext(bossText, bosslist);
                updatetext(playerText, playerlist);
                bossScrollView.fullScroll(View.FOCUS_DOWN);
                playerScrollView.fullScroll(View.FOCUS_DOWN);
                hitcount++;
            } while (bosslife > 0 && playerlife > 0);
            if (bosslife < 0) {
                bossText.append("\r\n" + "VICTORY! Boss defeated");
            } else {
                bossText.append("\r\n" + "DEFEAT! You died");
            }
            bossText.append("\r\n" + hitcount + " hits");

            bossScrollView.fullScroll(View.FOCUS_DOWN);
            playerScrollView.fullScroll(View.FOCUS_DOWN);
            battling = false;

        }
    }

    private void updatetext(TextView tv, LinkedList<String> lls){
        final TextView tv2 = tv;
        final LinkedList<String> lls2 = lls;
        //write line on a new thread
        new Thread(new Runnable() {
            @Override
            public void run() {
                mHandler.post(printQueueIntoTextArea(lls2, tv2);

            }
        });
    }

    private void printQueueIntoTextArea(LinkedList<String> t, TextView tv){
        String line = "";
        for(int i = 0; i < t.size(); i++){
            line += "\r\n" + t.get(i);
        }
        tv.setText(line);
    }


}
