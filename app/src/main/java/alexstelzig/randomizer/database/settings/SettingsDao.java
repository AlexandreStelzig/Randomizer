package alexstelzig.randomizer.database.settings;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import alexstelzig.randomizer.database.DbContentProvider;
import alexstelzig.randomizer.model.RandomList;
import alexstelzig.randomizer.model.Settings;

public class SettingsDao extends DbContentProvider implements ISettingsDao, ISettingsSchema {
    public SettingsDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public Settings fetchSettings() {
        Settings settings = null;
        Cursor cursor = super.rawQuery("SELECT * FROM " + SETTINGS_TABLE, null);

        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                settings = cursorToEntity(cursor);
            }
            cursor.close();
        }

        return settings;
    }

    @Override
    public boolean updateLastListOpened(int lastListOpenedId) {
        ContentValues values = new ContentValues();

        int settingsId = fetchSettings().getSettingsId();

        try {
            values.put(COLUMN_LAST_LIST_ID, lastListOpenedId);

            return super.update(SETTINGS_TABLE, values,
                    SETTINGS_ID + "=" + settingsId, null) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    @Override
    public int createSettings() {
        if(fetchSettings() != null){
            return fetchSettings().getSettingsId();
        }

        ContentValues values = new ContentValues();

        values.put(COLUMN_LAST_LIST_ID, Settings.NO_LIST_SELECTED);

        try {
            return super.insert(SETTINGS_TABLE, values);
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return -1;
        }
    }

    @Override
    protected Settings cursorToEntity(Cursor cursor) {

        int settingsId = cursor.getInt(cursor.getColumnIndex(SETTINGS_ID));
        int lastListId = cursor.getInt(cursor.getColumnIndex(COLUMN_LAST_LIST_ID));


        return new Settings(settingsId, lastListId);
    }

}
