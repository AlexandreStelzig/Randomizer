package alexstelzig.randomizer.database.list;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import alexstelzig.randomizer.database.Database;
import alexstelzig.randomizer.database.DbContentProvider;
import alexstelzig.randomizer.model.RandomList;
import alexstelzig.randomizer.model.RandomListItem;

public class ListDao extends DbContentProvider implements IListDao, IListSchema {
    public ListDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public List<RandomList> fetchAllList() {
        List<RandomList> randomLists = new ArrayList<>();
        Cursor cursor = super.rawQuery("SELECT * FROM " + LIST_TABLE, null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                RandomList list = cursorToEntity(cursor);
                randomLists.add(list);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return randomLists;
    }

    @Override
    public RandomList fetchListById(int listId) {
        RandomList randomList = null;
        Cursor cursor = super.rawQuery("SELECT * FROM " + LIST_TABLE + " WHERE "
                + COLUMN_LIST_ID + "=" + listId, null);

        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                randomList = cursorToEntity(cursor);
            }
            cursor.close();
        }

        return randomList;
    }

    @Override
    public int createList(String name) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);

        try {
            return super.insert(LIST_TABLE, values);
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return -1;
        }
    }

    @Override
    public boolean updateListName(int listId, String name) {

        ContentValues values = new ContentValues();

        try {
            values.put(COLUMN_NAME, name);

            return super.update(LIST_TABLE, values,
                    COLUMN_LIST_ID + "=" + listId, null) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteList(int listId) {

        List<RandomListItem> randomListItems = Database.listItemDao.fetchAllListItemForListId(listId);

        for(RandomListItem randomListItem: randomListItems){
            Database.listItemDao.deleteListItem(randomListItem.getRandomListItemId());
        }

        return super.delete(LIST_TABLE, COLUMN_LIST_ID + "=" + listId, null) > 0;
    }

    @Override
    protected RandomList cursorToEntity(Cursor cursor) {
        int randomListId = cursor.getInt(cursor.getColumnIndex(COLUMN_LIST_ID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));

        return new RandomList(randomListId, name);
    }

}
