package verhelst.rngfight.android;

import android.content.Intent;
import android.content.Context;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.games.Games;
import com.google.android.gms.games.Player;

import verhelst.rngfight.ActionResolver;




/**
 * Created by Orion on 1/6/2015.
 */
public class AndroidActionResolver implements ActionResolver, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    // Client used to interact with Google APIs
    private GoogleApiClient mGoogleApiClient;

    private boolean isSignInClicked = false;
    private boolean isAutoSignIn = false;

    private Context ctx;

    // request codes we use when invoking an external activity
    private static final int RC_RESOLVE = 5000;
    private static final int RC_UNUSED = 5001;
    private static final int RC_SIGN_IN = 9001;

    public AndroidActionResolver(Context context){
        this.ctx = context;

        // Create the Google API Client with access to Plus and Games
        mGoogleApiClient = new GoogleApiClient.Builder(ctx)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(Games.API).addScope(Games.SCOPE_GAMES)
                .build();

        if(isAutoSignIn){
            start();
        }
    }

    public void start(){
        mGoogleApiClient.connect();
    }

    public void stop(){
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    public void showAchievements() {
        if (isSignedIn()) {
            AndroidLauncher.instance.startActivityForResult(Games.Achievements.getAchievementsIntent(mGoogleApiClient), RC_UNUSED);
        } else {
            Toast.makeText(ctx, "Sign In to view Achievments", Toast.LENGTH_LONG);
        }
    }

    public void showLeaderBoard() {
        if (isSignedIn()) {
            AndroidLauncher.instance.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient), RC_UNUSED);
        } else {
            Toast.makeText(ctx, "Sign In to view LeaderBoards", Toast.LENGTH_LONG);
        }
    }

    public boolean submitHighScore(int score){
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, ctx.getResources().getString(R.string.leaderboard_leaderboard), score);
        } else {
            Toast.makeText(ctx, "Sign In to view LeaderBoards", Toast.LENGTH_LONG);
        }
        return true;
    }


    /*
        <string name="app_id">225240267219</string>
          <string name="achievement_its_over_9000">CgkI0_P2iscGEAIQAg</string>
          <string name="achievement_millennial">CgkI0_P2iscGEAIQAw</string>
          <string name="achievement_lucky">CgkI0_P2iscGEAIQBA</string>
          <string name="achievement_quikfite">CgkI0_P2iscGEAIQBQ</string>
          <string name="achievement_achievement_unlocked">CgkI0_P2iscGEAIQBg</string>
          <string name="leaderboard_leaderboard">CgkI0_P2iscGEAIQAA</string>


     */
    public boolean unlockAchievement(String achievement){
        if(!isSignedIn()){
            Toast.makeText(ctx, "Not Signed In", Toast.LENGTH_LONG);
            return false;
        }else{
            if(achievement.equalsIgnoreCase("its_over_9000")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_its_over_9000) );
                Toast.makeText(ctx, "Achievement unlocked: It's Over 9000!", Toast.LENGTH_LONG);
            }else if(achievement.equalsIgnoreCase("millennial")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_millennial) );
                Toast.makeText(ctx, "Achievement unlocked: Millennial!", Toast.LENGTH_LONG);
            }else if(achievement.equalsIgnoreCase("lucky")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_lucky) );
                Toast.makeText(ctx, "Achievement unlocked: Lucky!", Toast.LENGTH_LONG);
            }else if(achievement.equalsIgnoreCase("quikfite")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_quikfite) );
                Toast.makeText(ctx, "Achievement unlocked: QuikFite!", Toast.LENGTH_LONG);
            }else if(achievement.equalsIgnoreCase("achievement_unlocked")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_achievement_unlocked) );
                Toast.makeText(ctx, "Achievement unlocked: Achievement Unlocked!", Toast.LENGTH_LONG);
            }
        }
        return true;
    }

    public boolean isSignedIn() {
        return (mGoogleApiClient != null && mGoogleApiClient.isConnected());
    }


    @Override
    public void onConnected(Bundle bundle) {
        Player p = Games.Players.getCurrentPlayer(mGoogleApiClient);
        String displayName;
        if (p == null) {
            displayName = "???";
        } else {
            displayName = p.getDisplayName();
        }

        Toast.makeText(ctx, "Welcome " + displayName + "!", Toast.LENGTH_LONG);
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Toast.makeText(ctx, "Sign In Failed", Toast.LENGTH_LONG);
    }

    public void signOut(){
        isAutoSignIn = false;
        Games.signOut(mGoogleApiClient);
        stop();
    }

    public void signIn(){
        start();
    }
}



