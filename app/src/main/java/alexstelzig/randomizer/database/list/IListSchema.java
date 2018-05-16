package alexstelzig.randomizer.database.list;

/**
 * Created by alex on 2018-03-12.
 */

public interface IListSchema {

    // COLUMNS NAME
    String LIST_TABLE = "list_table";
    String COLUMN_LIST_ID = "list_id";
    String COLUMN_NAME = "list_item_name";

    // ON CREATE
    String SQL_CREATE_TABLE_LIST = "CREATE TABLE "
            + LIST_TABLE + " ("
            + COLUMN_LIST_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT" + ");";

    // ON DELETE
    String SQL_DELETE_TABLE_LIST = "DROP TABLE IF EXISTS " + LIST_TABLE;
}
