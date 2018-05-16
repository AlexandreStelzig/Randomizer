package alexstelzig.randomizer.model;

public class RandomListItem {


    // add optional

    private int mRandomListItemId;
    private String mName;
    private int mRandomWeight;
    private int mListPosition;
    private int mListItemRefId;
    private boolean mActive;

    public RandomListItem(int randomListItemId, String name, int randomWeight, int listPosition, int listItemRefId, boolean active) {
        this.mRandomListItemId = randomListItemId;
        this.mName = name;
        this.mRandomWeight = randomWeight;
        this.mListPosition = listPosition;
        this.mListItemRefId = listItemRefId;
        this.mActive = active;
    }

    public boolean isActive() {
        return mActive;
    }

    public void setActive(boolean active) {
        this.mActive = active;
    }

    public int getListItemRefId() {
        return mListItemRefId;
    }

    public void setListItemRefId(int listItemRefId) {
        this.mListItemRefId = listItemRefId;
    }

    public int getRandomListItemId() {
        return mRandomListItemId;
    }

    public void setRandomListItemId(int randomListItemId) {
        this.mRandomListItemId = randomListItemId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }

    public int getRandomWeight() {
        return mRandomWeight;
    }

    public void setRandomWeight(int randomWeight) {
        this.mRandomWeight = randomWeight;
    }

    public int getListPosition() {
        return mListPosition;
    }

    public void setListPosition(int listPosition) {
        this.mListPosition = listPosition;
    }
}
