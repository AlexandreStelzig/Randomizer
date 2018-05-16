package alexstelzig.randomizer.database.settings;

import android.util.Pair;

import java.util.List;

import alexstelzig.randomizer.model.RandomListItem;
import alexstelzig.randomizer.model.Settings;

/**
 * Created by alex on 2018-03-12.
 */

public interface ISettingsDao {

    Settings fetchSettings();
    boolean updateLastListOpened(int lastListOpenedId);
    int createSettings();
}
