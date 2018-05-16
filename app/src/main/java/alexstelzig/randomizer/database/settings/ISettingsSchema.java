package alexstelzig.randomizer.database.settings;

/**
 * Created by alex on 2018-03-12.
 */

public interface ISettingsSchema {

    // COLUMNS NAME
    String SETTINGS_TABLE = "settings_table";
    String SETTINGS_ID = "settings_table";
    String COLUMN_LAST_LIST_ID = "last_list_id";

    // ON CREATE
    String SQL_CREATE_TABLE_SETTINGS = "CREATE TABLE "
            + SETTINGS_TABLE + " ("
            + SETTINGS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_LAST_LIST_ID + " INTEGER" + ");";

    // ON DELETE
    String SQL_DELETE_TABLE_SETTINGS = "DROP TABLE IF EXISTS " + SETTINGS_TABLE;
}
