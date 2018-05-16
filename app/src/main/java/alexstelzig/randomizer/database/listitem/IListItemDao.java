package alexstelzig.randomizer.database.listitem;

import android.util.Pair;

import java.util.Date;
import java.util.List;

import alexstelzig.randomizer.model.RandomListItem;

/**
 * Created by alex on 2018-03-12.
 */

public interface IListItemDao {

    List<RandomListItem> fetchAllListItemForListId(int listId);
    List<RandomListItem> fetchAllActiveListItemForListId(int listId);

    List<RandomListItem> fetchAllInactiveListItemForListId(int listId);

    RandomListItem fetchListItemById(int listItemId);
    int createListItem(String name, int weight, int position, int listRefId, boolean isActive);
    boolean updateListItemName(int listItemId, String name);

    boolean updateListItemPosition(List<Pair<Long, RandomListItem>> listItems, int fromPosition, int toPosition);

    boolean updateListItemPosition(int listItemId, int position);
    boolean updateListItemWeight(int listItemId, int weight);
    boolean updateIsActive(int listItemId, boolean active);
    boolean deleteListItem(int listItemId);

}
