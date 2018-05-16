package alexstelzig.randomizer.database.list;

import java.util.List;

import alexstelzig.randomizer.model.RandomList;
import alexstelzig.randomizer.model.RandomListItem;

/**
 * Created by alex on 2018-03-12.
 */

public interface IListDao {

    List<RandomList> fetchAllList();
    RandomList fetchListById(int listId);
    int createList(String name);
    boolean updateListName(int listId, String name);
    boolean deleteList(int listId);

}
