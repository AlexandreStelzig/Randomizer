package alexstelzig.randomizer.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import alexstelzig.randomizer.database.list.IListSchema;
import alexstelzig.randomizer.database.list.ListDao;
import alexstelzig.randomizer.database.listitem.IListItemSchema;
import alexstelzig.randomizer.database.listitem.ListItemDao;
import alexstelzig.randomizer.database.settings.ISettingsSchema;
import alexstelzig.randomizer.database.settings.SettingsDao;
import alexstelzig.randomizer.model.Settings;

/**
 * Created by alex on 2018-03-07.
 */

public class Database {
    private static final String TAG = "LearningGoalsDatabase";
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "LearningGoals.db";
    private static Database database = null;
    private static DatabaseHelper mDatabaseHelper;

    // !!! for testing purposes only !!!
    private static final boolean RESET_DATABASE_ON_OPEN = false;

    // tables
    public static ListDao listDao;
    public static ListItemDao listItemDao;
    public static SettingsDao settingsDao;


    private Database(Context context) {
        mDatabaseHelper = new DatabaseHelper(context);
        SQLiteDatabase sqLiteDatabase = mDatabaseHelper.getWritableDatabase();

        // init tables
        listDao = new ListDao(sqLiteDatabase);
        listItemDao = new ListItemDao(sqLiteDatabase);
        settingsDao = new SettingsDao(sqLiteDatabase);

        Settings settings = settingsDao.fetchSettings();

        if(settings == null)
            settingsDao.createSettings();
    }


    public static void open(Context context) {
        if (database == null) {
            if (RESET_DATABASE_ON_OPEN)
                context.deleteDatabase(Database.DATABASE_NAME);
            database = new Database(context);

        } else {
            Log.e(TAG, "Cannot open database - database is already opened");
        }
    }

    public void close() {
        if (database != null) {
            mDatabaseHelper.close();
            database = null;
        } else {
            Log.e(TAG, "Cannot close database - database is not opened");
        }
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {


        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(IListSchema.SQL_CREATE_TABLE_LIST);
            db.execSQL(IListItemSchema.SQL_CREATE_TABLE_LIST_ITEM);
            db.execSQL(ISettingsSchema.SQL_CREATE_TABLE_SETTINGS);
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            deleteDatabase(db);
            onCreate(db);
        }

        public void deleteDatabase(SQLiteDatabase db) {
            db.execSQL(IListSchema.SQL_DELETE_TABLE_LIST);
            db.execSQL(IListItemSchema.SQL_DELETE_TABLE_LIST_ITEM);
            db.execSQL(ISettingsSchema.SQL_DELETE_TABLE_SETTINGS);
        }

        public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            onUpgrade(db, oldVersion, newVersion);
        }

    }
}
