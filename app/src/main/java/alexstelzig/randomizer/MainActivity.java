package alexstelzig.randomizer;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.Toolbar;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.woxthebox.draglistview.DragListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import alexstelzig.randomizer.components.CustomYesNoDialog;
import alexstelzig.randomizer.components.EditTextDialog;
import alexstelzig.randomizer.components.RandomItemDialog;
import alexstelzig.randomizer.components.ResultsDialog;
import alexstelzig.randomizer.database.Database;
import alexstelzig.randomizer.model.RandomList;
import alexstelzig.randomizer.model.RandomListItem;
import alexstelzig.randomizer.model.Settings;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Toolbar mToolbar;

    // menu
    private MenuItem mPreviousMenuItem;
    private DrawerLayout mDrawer;
    private NavigationView mNavigationView;
    private SubMenu mSubMenu;
    private MenuItem groupMenuItem;

    private final int NO_LIST_SELECTED = -1;
    private int currentListPosition = NO_LIST_SELECTED;

    private TextView noListItemTextView;
    private Button createListButton;
    private FloatingActionButton floatingActionButton;

    private List<RandomList> randomLists = new ArrayList<>();
    private ListItemAdapter listAdapter;
    private DragListView mDragListView;
    private ArrayList<Pair<Long, RandomListItem>> randomListItems = new ArrayList<>();

    private boolean firstTimeOpening = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // open database
        Database.open(this);


        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
        mNavigationView = (NavigationView) findViewById(R.id.nav_view);
        mNavigationView.setNavigationItemSelectedListener(this);
        groupMenuItem = mNavigationView.getMenu().findItem(R.id.list_submenu);
        mSubMenu = groupMenuItem.getSubMenu();


        noListItemTextView = (TextView) findViewById(R.id.no_list_item_text_view);
        mDragListView = (DragListView) findViewById(R.id.drag_list_view);

        floatingActionButton = (FloatingActionButton) findViewById(R.id.fab);

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentListPosition == NO_LIST_SELECTED) {
                    Toast.makeText(MainActivity.this, "Please select a list to create an item", Toast.LENGTH_SHORT).show();
                } else {
                    new RandomItemDialog(MainActivity.this, "Create new Item", false, null,
                            -1, true) {
                        @Override
                        public void positiveButtonClicked(String itemName, int weight, boolean active) {
                            createItem(itemName, weight, active);
                        }

                        @Override
                        public void deleteButtonClicked() {

                        }
                    }.show();

                }
            }
        });

        createListButton = ((Button) findViewById(R.id.create_list_button));
        createListButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newListDialog();
            }
        });

        initListView();
        refreshActivity();
    }

    private void createItem(String itemName, int weight, boolean active) {

        int position = 0;

        if (!randomListItems.isEmpty())
            position = Database.listItemDao.fetchLastItemPosition(randomLists.get(currentListPosition).getRandomListId()) + 1;

        Database.listItemDao.createListItem(itemName, weight, position, randomLists.get(currentListPosition).getRandomListId(), active);
        refreshActivity();
    }

    private void refreshActivity() {
        refreshDrawer();
        refreshContent();
        invalidateOptionsMenu();
    }

    private void refreshDrawer() {
        randomLists = Database.listDao.fetchAllList();

        mSubMenu.clear();

        if (randomLists.isEmpty()) {
            mSubMenu.getItem().setVisible(false);
            currentListPosition = NO_LIST_SELECTED;
        } else {
            mSubMenu.getItem().setVisible(true);
            for (int i = 0; i < randomLists.size(); i++) {
                mSubMenu.add(Menu.NONE, Menu.NONE, i, randomLists.get(i).getName());
            }


            if (mPreviousMenuItem == null) {

                if (firstTimeOpening) {
                    firstTimeOpening = false;
                    int lastListId = Database.settingsDao.fetchSettings().getLastOpenedListId();

                    int position = 0;

                    for (int counter = 0; counter < randomLists.size(); counter++) {
                        RandomList randomList = randomLists.get(counter);
                        if (randomList.getRandomListId() == lastListId)
                            position = counter;
                    }

                    currentListPosition = position;
                    mPreviousMenuItem = mSubMenu.getItem(position);

                } else {
                    currentListPosition = 0;
                    mPreviousMenuItem = mSubMenu.getItem(0);
                }
            }

            selectMenuItem(mPreviousMenuItem);
        }

        groupMenuItem.setTitle("Lists (" + randomLists.size() + ")");
    }

    private void refreshContent() {
        randomListItems.clear();

        if (randomLists.isEmpty()) {
            noListItemTextView.setVisibility(View.GONE);
            createListButton.setVisibility(View.VISIBLE);
            mDragListView.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.GONE);
            getSupportActionBar().setTitle("No List selected");
            return;
        }

        int currentListId = randomLists.get(currentListPosition).getRandomListId();


        List<RandomListItem> randomListItemsTemp = Database.listItemDao.fetchAllListItemForListId(currentListId);

        for (int i = 0; i < randomListItemsTemp.size(); i++) {
            randomListItems.add(new Pair<>((long) i, randomListItemsTemp.get(i)));
        }

        getSupportActionBar().setTitle(randomLists.get(currentListPosition).getName() + " (" + randomListItems.size() + ")");

        if (randomListItems.isEmpty()) {
            noListItemTextView.setVisibility(View.VISIBLE);
            createListButton.setVisibility(View.GONE);
            mDragListView.setVisibility(View.GONE);
            floatingActionButton.setVisibility(View.VISIBLE);
            noListItemTextView.setText("The List is empty");
            return;
        }


        listAdapter.updateAdapterData(randomListItems);
        listAdapter.notifyDataSetChanged();

        noListItemTextView.setVisibility(View.GONE);
        createListButton.setVisibility(View.GONE);
        floatingActionButton.setVisibility(View.VISIBLE);
        mDragListView.setVisibility(View.VISIBLE);

    }

    private void initListView() {
        mDragListView.getRecyclerView().setVerticalScrollBarEnabled(true);
        mDragListView.setDragListListener(new DragListView.DragListListenerAdapter() {
            @Override
            public void onItemDragStarted(int position) {

            }

            @Override
            public void onItemDragEnded(int fromPosition, int toPosition) {
                if (fromPosition != toPosition) {


                    if (fromPosition > toPosition) {
                        int tempFromPosition = fromPosition;
                        fromPosition = toPosition;
                        toPosition = tempFromPosition;
                    }

                    ArrayList<Pair<Long, RandomListItem>> pairs;
                    if (toPosition - fromPosition != randomListItems.size()) {
                        pairs = new ArrayList<Pair<Long, RandomListItem>>(
                                randomListItems.subList(fromPosition, Math.min(toPosition + 1, randomListItems.size())));
                    } else {
                        pairs = randomListItems;
                    }


                    Database.listItemDao.updateListItemPosition(pairs, fromPosition, toPosition);
                }
            }
        });

        mDragListView.setLayoutManager(new LinearLayoutManager(this));
        listAdapter = new ListItemAdapter(this);
        mDragListView.setAdapter(listAdapter, true);
        mDragListView.setCanDragHorizontally(false);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);


        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem deleteItem = menu.findItem(R.id.action_delete_list);
        MenuItem editNameItem = menu.findItem(R.id.action_edit_list_name);
        if (randomLists.isEmpty()) {
            deleteItem.setEnabled(false);
            editNameItem.setEnabled(false);
            deleteItem.getIcon().setAlpha(80);
            editNameItem.getIcon().setAlpha(80);
        } else {
            deleteItem.setEnabled(true);
            editNameItem.setEnabled(true);
            deleteItem.getIcon().setAlpha(255);
            editNameItem.getIcon().setAlpha(255);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();


        switch (id) {
            case R.id.action_edit_list_name:
                new EditTextDialog(this, "Edit List", null, randomLists.get(currentListPosition).getName()) {
                    @Override
                    public void positiveButtonClicked(String listName) {
                        updateList(listName);
                    }
                }.show();
                break;
            case R.id.action_delete_list:
                final RandomList randomList = randomLists.get(currentListPosition);
                new CustomYesNoDialog(this, "Delete List", "Are you sure you want to delete the list '" +
                        randomList.getName() + "'?") {
                    @Override
                    protected void onNegativeButtonClick() {
                    }

                    @Override
                    protected void onPositiveButtonClick() {
                        deleteList(randomList.getRandomListId());

                    }
                }.show();
                break;
            case R.id.action_random:
                if (randomListItems.isEmpty()) {
                    Toast.makeText(this, "Cannot randomize an empty list", Toast.LENGTH_SHORT).show();
                } else if (Database.listItemDao.
                        fetchAllActiveListItemForListId(randomLists.get(currentListPosition).getRandomListId()).isEmpty()) {
                    Toast.makeText(this, "Cannot randomize a list with no active items", Toast.LENGTH_SHORT).show();

                } else {
                    getRandomResults();

                }
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    private void updateList(String listName) {
        Database.listDao.updateListName(randomLists.get(currentListPosition).getRandomListId(), listName);
        int currentSelectedTemp = currentListPosition;
        refreshActivity();

        selectMenuItem(mSubMenu.getItem(currentSelectedTemp));


    }

    public void updateItem(int itemId, String itemName, int weight, boolean active) {
        Database.listItemDao.updateListItemName(itemId, itemName);
        Database.listItemDao.updateListItemWeight(itemId, weight);
        Database.listItemDao.updateIsActive(itemId, active);

        refreshContent();
    }

    private void getRandomResults() {


        List<RandomListItem> results = new ArrayList<>();
        List<RandomListItem> randomListItemsRandomList = new ArrayList<>();

        List<RandomListItem> randomListItemsTemp = Database.listItemDao.
                fetchAllActiveListItemForListId(randomLists.get(currentListPosition).getRandomListId());

        while (!randomListItemsTemp.isEmpty()) {
            randomListItemsRandomList.clear();
            for (int i = 0; i < randomListItemsTemp.size(); i++) {
                RandomListItem randomListItemTemp = randomListItemsTemp.get(i);
                for (int nbWeight = 0; nbWeight < randomListItemTemp.getRandomWeight(); nbWeight++) {
                    randomListItemsRandomList.add(randomListItemTemp);
                }
            }

            RandomListItem randomListItem = randomListItemsRandomList.get(new Random().nextInt(randomListItemsRandomList.size()));
            results.add(randomListItem);
            randomListItemsTemp.remove(randomListItem);
        }


        List<RandomListItem> inactives = Database.listItemDao.
                fetchAllInactiveListItemForListId(randomLists.get(currentListPosition).getRandomListId());

        Collections.shuffle(inactives);

        results.addAll(inactives);

        final ResultsDialog resultsDialog = new ResultsDialog(this, results) {
            @Override
            public void onNeutralButtonClicked() {
                getRandomResults();
            }
        };
        resultsDialog.show();

//        Toast.makeText(this, randomListItem.getName() + ", " + percentage, Toast.LENGTH_SHORT).show();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem menuItem) {
        // Handle navigation view item clicks here.
        int id = menuItem.getItemId();

        if (id == R.id.create_menu_item) {
            newListDialog();
        } else {
            selectMenuItem(menuItem);
            mDrawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    private void newListDialog() {
        new EditTextDialog(this, "Create new List", null, null) {
            @Override
            public void positiveButtonClicked(String listName) {

                createList(listName);
                if (mDrawer.isDrawerOpen(GravityCompat.START))
                    mDrawer.closeDrawer(GravityCompat.START);
            }
        }.show();
    }

    private void createList(String listName) {
        Database.listDao.createList(listName);
        refreshActivity();

        selectMenuItem(mSubMenu.getItem(mSubMenu.size() - 1));
    }

    private void deleteList(int listId) {
        Database.listDao.deleteList(listId);
        mPreviousMenuItem = null;
        currentListPosition = NO_LIST_SELECTED;
        refreshActivity();
    }

    public void deleteListItem(int listItemId) {
        Database.listItemDao.deleteListItem(listItemId);
        refreshContent();
    }

    private void selectMenuItem(MenuItem menuItem) {
        menuItem.setCheckable(true);
        menuItem.setChecked(true);
        if (mPreviousMenuItem != null && menuItem != mPreviousMenuItem) {
            mPreviousMenuItem.setChecked(false);
        }
        mPreviousMenuItem = menuItem;

        currentListPosition = menuItem.getOrder();

        Database.settingsDao.updateLastListOpened(randomLists.get(currentListPosition).getRandomListId());

        Settings settings = Database.settingsDao.fetchSettings();

        refreshContent();
    }

    public List<Pair<Long, RandomListItem>> fetchItems() {
        return randomListItems;
    }

    public int getTotalWeight() {
        int weight = 0;
        List<RandomListItem> randomListItemsTemp = Database.listItemDao.
                fetchAllActiveListItemForListId(randomLists.get(currentListPosition).getRandomListId());
        for (int i = 0; i < randomListItemsTemp.size(); i++) {
            RandomListItem randomListItemTemp = randomListItemsTemp.get(i);
            weight += randomListItemTemp.getRandomWeight();
        }
        return weight;
    }

}
