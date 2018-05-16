package alexstelzig.randomizer.model;

public class Settings {

    public final static int NO_LIST_SELECTED = -1;

    private int mSettingsId;
    private int mLastOpenedListId;

    public Settings(int settingsId, int lastOpenedListId) {
        this.mSettingsId = settingsId;
        this.mLastOpenedListId = lastOpenedListId;
    }

    public int getSettingsId() {
        return mSettingsId;
    }

    public void setSettingsId(int settingsId) {
        this.mSettingsId = settingsId;
    }

    public int getLastOpenedListId() {
        return mLastOpenedListId;
    }

    public void setLastOpenedListId(int lastOpenedListId) {
        this.mLastOpenedListId = lastOpenedListId;
    }
}
