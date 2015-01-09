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

    public boolean start(){
        mGoogleApiClient.connect();
        return true;
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
           makeToast("Sign In to view Achievments");
        }
    }

    public void showLeaderBoard() {
        if (isSignedIn()) {
            AndroidLauncher.instance.startActivityForResult(Games.Leaderboards.getAllLeaderboardsIntent(mGoogleApiClient), RC_UNUSED);
        } else {
            makeToast("Sign In to view LeaderBoards");
        }
    }

    public boolean submitHighScore(int score){
        if (isSignedIn()) {
            Games.Leaderboards.submitScore(mGoogleApiClient, ctx.getResources().getString(R.string.leaderboard_leaderboard), score);
        } else {
            makeToast("Sign In to view LeaderBoards");
        }
        return true;
    }


    private void makeToast(final String message){
        System.out.println("Toast: " + message);

        AndroidLauncher.instance.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(ctx, message, Toast.LENGTH_LONG);
            }
        });
    }


    public boolean unlockAchievement(String achievement){

        if(!isSignedIn()){

            makeToast("Not Signed In. Would have unlocked: " + achievement);

            return false;
        }else{
            if(achievement.equalsIgnoreCase("its_over_9000")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_its_over_9000) );
                makeToast("Achievement unlocked: It's Over 9000!");
            }else if(achievement.equalsIgnoreCase("millennial")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_millennial) );
                makeToast("Achievement unlocked: Millennial!");
            }else if(achievement.equalsIgnoreCase("lucky")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_lucky) );

                makeToast( "Achievement unlocked: Lucky!");
            }else if(achievement.equalsIgnoreCase("quikfite")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_quikfite) );
                makeToast("Achievement unlocked: QuikFite!");
            }else if(achievement.equalsIgnoreCase("achievement_unlocked")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_achievement_unlocked) );
                makeToast("Achievement unlocked: Achievement Unlocked!");
            }else if(achievement.equalsIgnoreCase("equipsuit")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_well_put_together) );
                makeToast("Achievement unlocked: Well Put Together!");
            }else if(achievement.equalsIgnoreCase("milkbutt")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_i_came_here_to_drink_milk_and_kick_butt) );
                makeToast( "Achievement unlocked: I cam here to drink milk and kick butt...");
            }else if(achievement.equalsIgnoreCase("loser")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_lehuesaheer) );
                makeToast("Achievement unlocked: Le-hue-sa-heer");
            }else if(achievement.equalsIgnoreCase("fifty")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_level_50) );
                makeToast("Achievement unlocked: Level 50");
            }else if(achievement.equalsIgnoreCase("missiles")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_launch_the_missiles) );
                makeToast("Achievement unlocked: Launch the missiles!!!");
            }else if(achievement.equalsIgnoreCase("millionaire")){
                Games.Achievements.unlock(mGoogleApiClient, ctx.getResources().getString(R.string.achievement_millionaire) );
                makeToast("Achievement unlocked: Millionaire!");
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

        makeToast("Welcome " + displayName + "!");
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        makeToast("Sign In Failed");
    }

    public void signOut(){
        isAutoSignIn = false;
        Games.signOut(mGoogleApiClient);
        stop();
    }

    public boolean signIn(){
        return start();
    }
}



