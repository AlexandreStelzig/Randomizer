package alexstelzig.randomizer.database.listitem;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import alexstelzig.randomizer.database.DbContentProvider;
import alexstelzig.randomizer.model.RandomListItem;

public class ListItemDao extends DbContentProvider implements IListItemSchema, IListItemDao {

    public ListItemDao(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public List<RandomListItem> fetchAllListItemForListId(int listId) {
        List<RandomListItem> randomListItems = new ArrayList<>();
        Cursor cursor = super.rawQuery("SELECT * FROM " + LIST_ITEM_TABLE + " WHERE "
                + COLUMN_LIST_REF_ID + "=" + listId +
                " ORDER BY "+ COLUMN_POSITION + " ASC", null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                RandomListItem listItem = cursorToEntity(cursor);
                randomListItems.add(listItem);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return randomListItems;
    }

    @Override
    public List<RandomListItem> fetchAllActiveListItemForListId(int listId) {
        List<RandomListItem> randomListItems = new ArrayList<>();
        Cursor cursor = super.rawQuery("SELECT * FROM " + LIST_ITEM_TABLE + " WHERE "
                + COLUMN_LIST_REF_ID + "=" + listId + " AND " + COLUMN_ACTIVE + "=" + 1 +
                " ORDER BY "+ COLUMN_POSITION + " ASC", null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                RandomListItem listItem = cursorToEntity(cursor);
                randomListItems.add(listItem);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return randomListItems;
    }

    @Override
    public List<RandomListItem> fetchAllInactiveListItemForListId(int listId) {
        List<RandomListItem> randomListItems = new ArrayList<>();
        Cursor cursor = super.rawQuery("SELECT * FROM " + LIST_ITEM_TABLE + " WHERE "
                + COLUMN_LIST_REF_ID + "=" + listId + " AND " + COLUMN_ACTIVE + "=" + 0 +
                " ORDER BY "+ COLUMN_POSITION + " ASC", null);

        if (cursor != null) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                RandomListItem listItem = cursorToEntity(cursor);
                randomListItems.add(listItem);
                cursor.moveToNext();
            }
            cursor.close();
        }

        return randomListItems;
    }

    @Override
    public RandomListItem fetchListItemById(int listItemId) {
        RandomListItem randomListItem = null;
        Cursor cursor = super.rawQuery("SELECT * FROM " + LIST_ITEM_TABLE + " WHERE "
                + COLUMN_LIST_ITEM_ID + "=" + listItemId, null);

        if (cursor != null) {
            cursor.moveToFirst();
            if (!cursor.isAfterLast()) {
                randomListItem = cursorToEntity(cursor);
            }
            cursor.close();
        }

        return randomListItem;
    }

    public int fetchLastItemPosition(int listItemId){
        List<RandomListItem> randomListItem = fetchAllListItemForListId(listItemId);

        // todo less ugly way of getter last position
//        Cursor cursor = super.rawQuery("SELECT * FROM " + LIST_ITEM_TABLE + " WHERE "
//                + COLUMN_LIST_ITEM_ID + "=" + listItemId +
//                " ORDER BY "+ COLUMN_POSITION + " DESC", null);
//
//        if (cursor != null) {
//            cursor.moveToFirst();
//            if (!cursor.isAfterLast()) {
//                randomListItem = cursorToEntity(cursor);
//            }
//            cursor.close();
//        }

        return randomListItem.get(randomListItem.size() - 1).getListPosition();
    }

    @Override
    public int createListItem(String name, int weight, int position, int listRefId, boolean isActive) {
        ContentValues values = new ContentValues();

        values.put(COLUMN_NAME, name);
        values.put(COLUMN_WEIGHT, weight);
        values.put(COLUMN_POSITION, position);
        values.put(COLUMN_LIST_REF_ID, listRefId);
        values.put(COLUMN_ACTIVE, isActive);

        try {
            return super.insert(LIST_ITEM_TABLE, values);
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return -1;
        }
    }

    @Override
    public boolean updateListItemName(int listItemId, String name) {

        ContentValues values = new ContentValues();

        try {
            values.put(COLUMN_NAME, name);

            return super.update(LIST_ITEM_TABLE, values,
                    COLUMN_LIST_ITEM_ID + "=" + listItemId, null) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateListItemPosition(List<Pair<Long, RandomListItem>> listItems, int fromPosition, int toPosition) {

        int position = fromPosition;
        boolean success = true;
        for (Pair<Long, RandomListItem> pair : listItems) {
            int listItemId = pair.second.getRandomListItemId();
            boolean successTemp = updateListItemPosition(listItemId, position);
            if(!successTemp)
                success = false;
            position++;
        }



        return success;
    }

    @Override
    public boolean updateListItemPosition(int listItemId, int position) {
        ContentValues values = new ContentValues();

        try {
            values.put(COLUMN_POSITION, position);

            return super.update(LIST_ITEM_TABLE, values,
                    COLUMN_LIST_ITEM_ID + "=" + listItemId, null) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateListItemWeight(int listItemId, int weight) {
        ContentValues values = new ContentValues();

        try {
            values.put(COLUMN_WEIGHT, weight);

            return super.update(LIST_ITEM_TABLE, values,
                    COLUMN_LIST_ITEM_ID + "=" + listItemId, null) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean updateIsActive(int listItemId, boolean active) {
        ContentValues values = new ContentValues();

        try {
            values.put(COLUMN_ACTIVE, active);

            return super.update(LIST_ITEM_TABLE, values,
                    COLUMN_LIST_ITEM_ID + "=" + listItemId, null) > 0;
        } catch (SQLiteConstraintException ex) {
            Log.w("Database", ex.getMessage());
            return false;
        }
    }

    @Override
    public boolean deleteListItem(int listItemId) {
        return super.delete(LIST_ITEM_TABLE, COLUMN_LIST_ITEM_ID + "=" + listItemId, null) > 0;
    }

    @Override
    protected RandomListItem cursorToEntity(Cursor cursor) {

        int randomListItemId = cursor.getInt(cursor.getColumnIndex(COLUMN_LIST_ITEM_ID));
        String name = cursor.getString(cursor.getColumnIndex(COLUMN_NAME));
        int weight = cursor.getInt(cursor.getColumnIndex(COLUMN_WEIGHT));
        int listPosition = cursor.getInt(cursor.getColumnIndex(COLUMN_POSITION));
        int listItemRefId = cursor.getInt(cursor.getColumnIndex(COLUMN_LIST_REF_ID));
        boolean active = cursor.getInt(cursor.getColumnIndex(COLUMN_ACTIVE)) > 0;


        return new RandomListItem(randomListItemId, name, weight, listPosition, listItemRefId, active);
    }

}
