package alexstelzig.randomizer.database.listitem;

/**
 * Created by alex on 2018-03-12.
 */

public interface IListItemSchema {

    // COLUMNS NAME
    String LIST_ITEM_TABLE = "list_item_table";
    String COLUMN_LIST_ITEM_ID = "list_item_id";
    String COLUMN_NAME = "list_item_name";
    String COLUMN_WEIGHT = "list_item_weight";
    String COLUMN_POSITION = "list_item_position";
    String COLUMN_LIST_REF_ID = "list_item_list_ref_id";
    String COLUMN_ACTIVE = "list_item_list_active";

    // ON CREATE
    String SQL_CREATE_TABLE_LIST_ITEM = "CREATE TABLE "
            + LIST_ITEM_TABLE + " ("
            + COLUMN_LIST_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + COLUMN_NAME + " TEXT,"
            + COLUMN_WEIGHT + " INTEGER,"
            + COLUMN_POSITION + " INTEGER,"
            + COLUMN_LIST_REF_ID + " INTEGER,"
            + COLUMN_ACTIVE + " BOOLEAN" + ");";

    // ON DELETE
    String SQL_DELETE_TABLE_LIST_ITEM = "DROP TABLE IF EXISTS " + LIST_ITEM_TABLE;
}
