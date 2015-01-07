package verhelst.rngfight;

/**
 * Created by Orion on 1/6/2015.
 */
public interface ActionResolver {

    public void showAchievements();
    public void showLeaderBoard();
    public boolean submitHighScore(int score);
    public boolean unlockAchievement(String achievement);
    public void signIn();
    public void signOut();
    public boolean isSignedIn();

}
