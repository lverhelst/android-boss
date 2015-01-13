package verhelst.rngfight.desktop;

import verhelst.rngfight.ActionResolver;

/**
 * Created by Orion on 1/6/2015.
 */
public class DesktopActionResolver implements ActionResolver {
    boolean isSignedIn;

    @Override
    public void showAchievements() {
        System.out.println("Show achievments screen in Android");
    }

    @Override
    public void showLeaderBoard() {
        System.out.println("Show leaderboard screen in Android");
    }

    @Override
    public boolean submitHighScore(int score) {
        System.out.println("Submit score " + score);
        return false;
    }

    @Override
    public boolean unlockAchievement(String achievement) {
        System.out.println("***** UNLOCK ACHEIVEMENT : " + achievement);

        return false;
    }

    @Override
    public boolean signIn() {
        isSignedIn = true;
        System.out.println("Sign In");
        return true;
    }

    @Override
    public void signOut() {
        isSignedIn = false;
        System.out.println("Sign Out");
    }

    @Override
    public boolean isSignedIn() {
        return isSignedIn;
    }

    @Override
    public boolean isSigningIn() {
        return false;
    }

    @Override
    public void postMessage(String message) {
        System.out.println(message);
    }
}
