package verhelst.rngfight.android;


import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;
import com.google.example.games.basegameutils.BaseGameUtils;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import verhelst.rngfight.ActionResolver;
import verhelst.rngfight.RngFight;

public class AndroidLauncher extends AndroidApplication implements ActionResolver, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String AD_UNIT_ID = "ca-app-pub-5013489833070081/3540762654";
    protected AdView adView;
    protected View gameView;
    protected TextView logView;

    public static AndroidLauncher instance;

    private GoogleApiClient mGoogleApiClient;
    private static int RC_SIGN_IN = 9001;
    private static final int RC_UNUSED = 5001;
    private boolean mResolvingConnectionFailure = false;
    private boolean mAutoStartSignInflow = true;
    private boolean mSignInClicked = false;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // Create the Google Api Client with access to Games
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        instance = this;

        AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
        cfg.useAccelerometer = false;
        cfg.useCompass = false;


        //TODO: Replace with Wake Lock (Screen only needs to stay on in battle, even then it doesn't really)
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        //AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        //initialize(new RngFight(), config);
        RelativeLayout layout = new RelativeLayout(this);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
        layout.setLayoutParams(params);

        AdView admobView = createAdView();
        layout.addView(admobView);
        View gameView = createGameView(cfg);
        layout.addView(gameView);
        //TextView lg = createTextView();
        //layout.addView(lg);





        setContentView(layout);
        if(false)
            startAdvertising(admobView);


    }



    private TextView createTextView(){
        logView = new TextView(this) {
            {
                setVerticalScrollBarEnabled(true);
                setMovementMethod(ScrollingMovementMethod.getInstance());
                setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);

                // Force scrollbars to be displayed.
                TypedArray a = this.getContext().getTheme().obtainStyledAttributes(new int[0]);
                initializeScrollbars(a);
                a.recycle();
            }
        };
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, gameView.getId());
        logView.setLayoutParams(params);

        return logView;
    }


    private View createGameView(AndroidApplicationConfiguration cfg) {
        gameView = initializeForView(new RngFight(this), cfg);
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.BELOW, adView.getId());
        gameView.setLayoutParams(params);
        return gameView;
    }


    private AdView createAdView() {
        adView = new AdView(this);
        adView.setAdSize(AdSize.SMART_BANNER);
        adView.setAdUnitId(AD_UNIT_ID);
        adView.setId(adView.generateViewId());
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
        params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
        adView.setLayoutParams(params);
        adView.setBackgroundColor(Color.BLACK);
        return adView;
    }

    private void startAdvertising(AdView adView) {
        AdRequest adRequest = new AdRequest.Builder().addTestDevice(AdRequest.DEVICE_ID_EMULATOR).build();
        adView.loadAd(adRequest);
    }

    @Override
    public void onResume() {
        super.onResume();
        if (adView != null) adView.resume();
    }

    @Override
    public void onPause() {
        if (adView != null) adView.pause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (adView != null) adView.destroy();
        super.onDestroy();
    }

    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {
        if (requestCode == RC_SIGN_IN) {
                mSignInClicked = false;
                mResolvingConnectionFailure = false;
            if (resultCode == RESULT_OK) {
                mGoogleApiClient.connect();
            } else {
                // Bring up an error dialog to alert the user that sign-in
                // failed. The R.string.signin_failure should reference an error
                // string in your strings.xml file that tells the user they
                // could not be signed in, such as "Unable to sign in."
                BaseGameUtils.showActivityResultError(this,
                        requestCode, resultCode, R.string.signin_failure);
            }
        }
       // showTextView();

    }

    public void showTextView(){
        try {
            Process process = Runtime.getRuntime().exec("logcat -d");
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(process.getInputStream()));

            StringBuilder log=new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                log.append(line);
            }

            logView.setText(log.toString());
        } catch (IOException e) {
        }
    }




    public static void saveLogcatToFile(Context context) {
        String fileName = "logcat_"+System.currentTimeMillis()+".txt";
        File outputFile = new File(context.getExternalCacheDir(),fileName);

        try {
            @SuppressWarnings("unused")
            Process process = Runtime.getRuntime().exec("logcat -f " + outputFile.getAbsolutePath());
        }catch(Exception e){

        }
    }



    @Override
    protected void onStart() {
        super.onStart();
     //   mGoogleApiClient.connect();

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }


    @Override
    public void onConnected(Bundle bundle) {
        System.out.println("ON CONNECTED");
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        String displayName;
        if (p == null) {
            displayName = "???";
        } else {
            displayName = p.getDisplayName();
        }

        makeToast("Welcome " + displayName + "!");
    }

    private void makeToast(final String message){
        System.out.println("Toast: " + message);

        AndroidLauncher.instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onConnectionSuspended(int i) {
        // Attempt to reconnect
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        if (mResolvingConnectionFailure) {
            // already resolving
            return;
        }

        // if the sign-in button was clicked or if auto sign-in is enabled,
        // launch the sign-in flow
        if (mSignInClicked || mAutoStartSignInflow) {
            mAutoStartSignInflow = false;
            mSignInClicked = false;
            mResolvingConnectionFailure = true;

            // Attempt to resolve the connection failure using BaseGameUtils.
            // The R.string.signin_other_error value should reference a generic
            // error string in your strings.xml file, such as "There was
            // an issue with sign-in, please try again later."
            if (!BaseGameUtils.resolveConnectionFailure(this,
                    mGoogleApiClient, connectionResult,
                    RC_SIGN_IN, getResources().getString(R.string.signin_other_error))) {
                mResolvingConnectionFailure = false;
            }
        }

        // Put code here to display the sign-in button
    }

    // Call when the sign-in button is clicked
    private void signInClicked() {
        mSignInClicked = true;
        mGoogleApiClient.connect();
    }

    // Call when the sign-out button is clicked
    private void signOutclicked() {
        mSignInClicked = false;
        Games.signOut(mGoogleApiClient);
    }


    /***
     *
     * Action Resolver
     */

    @Override
    public void showAchievements() {
        if (isSignedIn()) {
            AndroidLauncher.instance.startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), RC_UNUSED);
        } else {
            makeToast("Sign In to view Achievements");
        }
    }

    @Override
    public void showLeaderBoard() {
        if (isSignedIn()) {
            AndroidLauncher.instance.startActivityForResult(Games.Leaderboards.getLeaderboardIntent(mGoogleApiClient, getApplicationContext().getString(R.string.leaderboard_leaderboard)), 100);
        } else {
            makeToast("Sign In to view LeaderBoards");
        }
    }

    @Override
    public boolean submitHighScore(int score) {
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, getApplicationContext().getString(R.string.leaderboard_leaderboard), score);
        } else {
            makeToast("Sign In to view LeaderBoards");
        }
        return true;
    }

    @Override
    public boolean unlockAchievement(String achievement) {

        if(!isSignedIn()){

            makeToast("Not Signed In. Would have unlocked: " + achievement);

            return false;
        }else{
            if(achievement.equalsIgnoreCase("its_over_9000")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_its_over_9000) );
                makeToast("Achievement unlocked: It's Over 9000!");
            }else if(achievement.equalsIgnoreCase("millennial")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_millennial) );
                makeToast("Achievement unlocked: Millennial!");
            }else if(achievement.equalsIgnoreCase("lucky")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_lucky) );

                makeToast( "Achievement unlocked: Lucky!");
            }else if(achievement.equalsIgnoreCase("quikfite")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_quikfite) );
                makeToast("Achievement unlocked: QuikFite!");
            }else if(achievement.equalsIgnoreCase("achievement_unlocked")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_achievement_unlocked) );
                makeToast("Achievement unlocked: Achievement Unlocked!");
            }else if(achievement.equalsIgnoreCase("equipsuit")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_well_put_together) );
                makeToast("Achievement unlocked: Well Put Together!");
            }else if(achievement.equalsIgnoreCase("milkbutt")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_i_came_here_to_drink_milk_and_kick_butt) );
                makeToast( "Achievement unlocked: I cam here to drink milk and kick butt...");
            }else if(achievement.equalsIgnoreCase("loser")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_lehuesaheer) );
                makeToast("Achievement unlocked: Le-hue-sa-heer");
            }else if(achievement.equalsIgnoreCase("fifty")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_level_50) );
                makeToast("Achievement unlocked: Level 50");
            }else if(achievement.equalsIgnoreCase("missiles")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_launch_the_missiles) );
                makeToast("Achievement unlocked: Launch the missiles!!!");
            }else if(achievement.equalsIgnoreCase("millionaire")){
                Games.Achievements.unlock(mGoogleApiClient, getApplicationContext().getString(R.string.achievement_millionaire) );
                makeToast("Achievement unlocked: Millionaire!");
            }
        }
        return true;
    }

    @Override
    public boolean signIn() {
        signInClicked();
        return true;
    }

    @Override
    public void signOut() {
        signOutclicked();
    }

    @Override
    public boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }

    @Override
    public boolean isSigningIn() {
        return mGoogleApiClient.isConnecting();
    }


}
