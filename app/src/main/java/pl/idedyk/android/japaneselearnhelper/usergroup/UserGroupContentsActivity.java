package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.io.Serializable;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;

public class UserGroupContentsActivity extends Activity {

    private UserGroupEntity userGroupEntity;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        super.onCreateOptionsMenu(menu);

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
    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {

        super.onRestoreInstanceState(bundle);
    }

    @Override
    public void onCreate(Bundle bundle) {

        super.onCreate(bundle);

        JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_user_group_contents));

        setContentView(R.layout.user_group_contents);

        //

        userGroupEntity = (UserGroupEntity) getIntent().getSerializableExtra("userGroupEntity");

        //

        /*
        //

        createUserGroupListView();

        //

        loadUserGroups();

        //

        checkItemToAdd();

        //

        createReportProblemButton();
        */
    }

    private void createUserGroupListView() {

        /*

        userGroupListView = (ListView)findViewById(R.id.user_group_list);

        userGroupList = new ArrayList<>();
        userGroupListAdapter = new UserGroupListItemAdapter(this, userGroupList);

        userGroupListView.setAdapter(userGroupListAdapter);

        userGroupListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final UserGroupListItem userGroupListItem = (UserGroupListItem)userGroupListAdapter.getItem(position);

                UserGroupListItem.ItemType itemType = userGroupListItem.getItemType();

                // tworzenie menu podrecznego
                PopupMenu popupMenu = new PopupMenu(UserGroupActivity.this, view);

                if (dictionaryEntryToAdd != null || kanjiEntryToAdd != null) {
                    popupMenu.getMenu().add(Menu.NONE, R.id.user_group_popup_add_to_this_group, Menu.NONE, R.string.user_group_popup_add_to_this_group);
                }

                popupMenu.getMenu().add(Menu.NONE, R.id.user_group_popup_open_group, Menu.NONE, R.string.user_group_popup_open_group);

                if (itemType == UserGroupListItem.ItemType.USER_GROUP) {
                    popupMenu.getMenu().add(Menu.NONE, R.id.user_group_popup_change_group_name, Menu.NONE, R.string.user_group_popup_change_group_name);
                    popupMenu.getMenu().add(Menu.NONE, R.id.user_group_popup_delete_group, Menu.NONE, R.string.user_group_popup_delete_group);
                }

                //

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int itemId = item.getItemId();

                        if (itemId == R.id.user_group_popup_add_to_this_group) { // dodanie elementu do grupy

                            UserGroupEntity userGroupEntity = userGroupListItem.getUserGroupEntity();

                            addItemIdToUserGroupEntity(userGroupEntity);

                            return true;

                        } else if (itemId == R.id.user_group_popup_open_group) { // otwarcie grupy

                            UserGroupEntity userGroupEntity = userGroupListItem.getUserGroupEntity();

                            Intent intent = new Intent(getApplicationContext(), UserGroupContentsActivity.class);

                            intent.putExtra("userGroupEntity", userGroupEntity);

                            startActivity(intent);

                            return true;

                        } else if (itemId == R.id.user_group_popup_change_group_name) { // zmiana nazwy grupy

                            UserGroupEntity userGroupEntityToUpdate = userGroupListItem.getUserGroupEntity();

                            createOrUpdateUserGroup(userGroupEntityToUpdate);

                            return true;

                        } else if (itemId == R.id.user_group_popup_delete_group) { // usuniecie nazwy grupy

                            UserGroupEntity userGroupEntityToDelete = userGroupListItem.getUserGroupEntity();

                            deleteUserGroup(userGroupEntityToDelete);

                            return true;

                        } else {
                            return false;
                        }
                    }
                });

                //

                popupMenu.show();
            }
        });
        */
    }

    private void loadUserGroups() {

        /*
        DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

        DataManager dataManager = dictionaryManager.getDataManager();

        userGroupList.clear();

        List<UserGroupEntity> allUserGroupsList = dataManager.getAllUserGroupList();

        // sortowanie
        Collections.sort(allUserGroupsList, new Comparator<UserGroupEntity>() {

            @Override
            public int compare(UserGroupEntity o1, UserGroupEntity o2) {

                UserGroupEntity.Type o1Type = o1.getType();
                UserGroupEntity.Type o2Type = o2.getType();

                if (o1Type == UserGroupEntity.Type.STAR_GROUP && o2Type != UserGroupEntity.Type.STAR_GROUP) {
                    return -1;

                } else if (o1Type != UserGroupEntity.Type.STAR_GROUP && o2Type == UserGroupEntity.Type.STAR_GROUP) {
                    return 1;
                }

                return o1.getName().compareTo(o2.getName());
            }
        });

        for (UserGroupEntity currentUserGroupEntity : allUserGroupsList) {
            userGroupList.add(new UserGroupListItem(currentUserGroupEntity, getResources()));
        }

        userGroupListAdapter.notifyDataSetChanged();
        */
    }

}
