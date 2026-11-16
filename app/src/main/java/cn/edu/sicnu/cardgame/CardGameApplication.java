package cn.edu.sicnu.cardgame;

import android.app.Application;
import cn.edu.sicnu.cardgame.model.PlayerProfile;

public class CardGameApplication extends Application {
    private PlayerProfile profile;
    private static final String PROFILE_FILE = "profile.dat";

    @Override public void onCreate() {
        super.onCreate();
        profile = StateStore.read(this, PROFILE_FILE, PlayerProfile.class);
        if (profile == null) {
            profile = new PlayerProfile(getString(R.string.empty));
            profile.name = getString(R.string.owner_name);
            profile.student = getString(R.string.owner_student);
        }
        profile.restoreAvatar();
    }

    boolean saveProfile() {
        return StateStore.write(this, PROFILE_FILE, profile);
    }
    public PlayerProfile getProfile() { return profile; }
}
