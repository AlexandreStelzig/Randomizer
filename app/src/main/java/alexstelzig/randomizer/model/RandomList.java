package alexstelzig.randomizer.model;

public class RandomList {

    int mRandomListId;
    String mName;

    public RandomList(int randomListId, String name) {
        this.mRandomListId = randomListId;
        this.mName = name;
    }

    public int getRandomListId() {
        return mRandomListId;
    }

    public void setRandomListId(int randomListId) {
        this.mRandomListId = randomListId;
    }

    public String getName() {
        return mName;
    }

    public void setName(String name) {
        this.mName = name;
    }
}
