package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.PopupMenu;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;

public class UserGroupActivity extends Activity {

    private ListView userGroupListView;

    private UserGroupListItemAdapter userGroupListAdapter;

    private List<UserGroupListItem> userGroupList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, R.id.user_group_add_group, Menu.NONE, R.string.user_group_add_group);

        MenuShorterHelper.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        super.onSaveInstanceState(bundle);

        // FIXME
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {

        super.onRestoreInstanceState(bundle);

        // FIXME
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_user_group));

        setContentView(R.layout.user_group);

        //

        createUseerGroupListView();

        //

        loadUserGroups();
    }

    private void createUseerGroupListView() {

        userGroupListView = (ListView)findViewById(R.id.user_group_list);

        userGroupList = new ArrayList<>();
        userGroupListAdapter = new UserGroupListItemAdapter(this, userGroupList);

        userGroupListView.setAdapter(userGroupListAdapter);

        userGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                UserGroupListItem userGroupListItem = (UserGroupListItem)userGroupListAdapter.getItem(position);

                UserGroupListItem.ItemType itemType = userGroupListItem.getItemType();

                // tworzenie menu podrecznego
                PopupMenu popupMenu = new PopupMenu(UserGroupActivity.this, view);

                popupMenu.getMenu().add(Menu.NONE, R.id.user_group_popup_open_group, Menu.NONE, R.string.user_group_popup_open_group);

                if (itemType == UserGroupListItem.ItemType.USER_GROUP) {
                    popupMenu.getMenu().add(Menu.NONE, R.id.user_group_popup_change_group_name, Menu.NONE, R.string.user_group_popup_change_group_name);
                    popupMenu.getMenu().add(Menu.NONE, R.id.user_group_popup_delete_group, Menu.NONE, R.string.user_group_popup_delete_group);
                }

                //

                popupMenu.show();
            }
        });
    }

    private void loadUserGroups() {

        DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

        DataManager dataManager = dictionaryManager.getDataManager();

        userGroupList.clear();

        List<UserGroupEntity> allUserGroupsList = dataManager.getAllUserGroupList();

        for (UserGroupEntity currentUserGroupEntity : allUserGroupsList) {
            userGroupList.add(new UserGroupListItem(currentUserGroupEntity, getResources()));
        }

        userGroupListAdapter.notifyDataSetChanged();
    }
}