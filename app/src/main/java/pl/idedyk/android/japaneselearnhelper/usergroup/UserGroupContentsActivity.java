package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupItemEntity;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;

public class UserGroupContentsActivity extends Activity {

    private UserGroupEntity userGroupEntity;

    //

    private ListView userGroupContentsListView;

    private UserGroupContentsListItemAdapter userGroupContentsListAdapter;

    private List<UserGroupContentsListItem> userGroupContentsList;

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

        createUserGroupContentsListView();

        //

        loadUserGroupContents();

        /*
        //

        //

        checkItemToAdd();

        //

        createReportProblemButton();
        */
    }

    private void createUserGroupContentsListView() {

        userGroupContentsListView = (ListView)findViewById(R.id.user_group_contents_list);

        userGroupContentsList = new ArrayList<>();
        userGroupContentsListAdapter = new UserGroupContentsListItemAdapter(this, userGroupContentsList);

        userGroupContentsListView.setAdapter(userGroupContentsListAdapter);

        /*
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

    private void loadUserGroupContents() {

        DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

        DataManager dataManager = dictionaryManager.getDataManager();

        userGroupContentsList.clear();

        List<UserGroupItemEntity> userGroupItemEntityList = dataManager.getUserGroupItemListForUserEntity(userGroupEntity);

        class UserGroupItemEntityAndObject {

            UserGroupItemEntity userGroupItemEntity;

            DictionaryEntry dictionaryEntry;
            KanjiEntry kanjiEntry;

            public UserGroupItemEntityAndObject(UserGroupItemEntity userGroupItemEntity, DictionaryEntry dictionaryEntry) {
                this.userGroupItemEntity = userGroupItemEntity;
                this.dictionaryEntry = dictionaryEntry;
            }

            public UserGroupItemEntityAndObject(UserGroupItemEntity userGroupItemEntity, KanjiEntry kanjiEntry) {
                this.userGroupItemEntity = userGroupItemEntity;
                this.kanjiEntry = kanjiEntry;
            }
        }

        // podzielenie grupy na liste DICTIONARY_ENTRY i KANJI_ENTRY
        List<UserGroupItemEntityAndObject> dictionaryEntryList = new ArrayList<>();
        List<UserGroupItemEntityAndObject> kanjiEntryList = new ArrayList<>();

        for (UserGroupItemEntity userGroupItemEntity : userGroupItemEntityList) {

            Integer itemId = userGroupItemEntity.getItemId();

            UserGroupItemEntity.Type type = userGroupItemEntity.getType();

            switch (type) {

                case DICTIONARY_ENTRY:

                    DictionaryEntry dictionaryEntry = dictionaryManager.getDictionaryEntryById(itemId);

                    if (dictionaryEntry == null) {
                        continue;
                    }

                    dictionaryEntryList.add(new UserGroupItemEntityAndObject(userGroupItemEntity, dictionaryEntry));

                    break;

                case KANJI_ENTRY:

                    KanjiEntry kanjiEntry = dictionaryManager.getKanjiEntryById(itemId);

                    if (kanjiEntry == null) {
                        continue;
                    }

                    kanjiEntryList.add(new UserGroupItemEntityAndObject(userGroupItemEntity, kanjiEntry));

                    break;

                    default:
                        throw new RuntimeException("Unknown UserGroupItemEntity.Type");
            }
        }

        // sortowanie
        Collections.sort(dictionaryEntryList, new Comparator<UserGroupItemEntityAndObject>() {
            @Override
            public int compare(UserGroupItemEntityAndObject o1, UserGroupItemEntityAndObject o2) {

                String o1Romaji = o1.dictionaryEntry.getRomaji();
                String o2Romaji = o2.dictionaryEntry.getRomaji();

                if (o1Romaji == null && o2Romaji == null) {
                    return 0;

                } else if (o1Romaji == null && o2Romaji != null) {
                    return -1;

                } else if (o1Romaji != null && o2Romaji == null) {
                    return 1;

                } else {
                    return o1Romaji.compareTo(o2Romaji);
                }
            }
        });

        Collections.sort(kanjiEntryList, new Comparator<UserGroupItemEntityAndObject>() {
            @Override
            public int compare(UserGroupItemEntityAndObject o1, UserGroupItemEntityAndObject o2) {

                Integer o1Power = getMinPower(o1.kanjiEntry.getGroups());
                Integer o2Power = getMinPower(o2.kanjiEntry.getGroups());

                int comparePower = o1Power.compareTo(o2Power);

                if (comparePower != 0) {
                    return comparePower;
                }

                Integer o1Id = o1.kanjiEntry.getId();
                Integer o2Id = o2.kanjiEntry.getId();

                return o1Id.compareTo(o2Id);
            }

            private int getMinPower(List<GroupEnum> groupEnumList) {

                int power = Integer.MAX_VALUE;

                for (GroupEnum groupEnum : groupEnumList) {

                    if (groupEnum.getPower() < power) {
                        power = groupEnum.getPower();
                    }
                }

                return power;
            }
        });

        // dodajemy liste dictionary
        userGroupContentsList.add(new UserGroupContentsListItem(getString(R.string.user_group_contents_list_title_dictionary_entry)));

        for (UserGroupItemEntityAndObject userGroupItemEntityAndObject : dictionaryEntryList) {

            userGroupContentsList.add(new UserGroupContentsListItem(userGroupItemEntityAndObject.userGroupItemEntity,
                    userGroupItemEntityAndObject.dictionaryEntry));
        }

        // dodajemy liste kanji
        userGroupContentsList.add(new UserGroupContentsListItem(getString(R.string.user_group_contents_list_title_kanji_entry)));

        for (UserGroupItemEntityAndObject userGroupItemEntityAndObject : kanjiEntryList) {

            userGroupContentsList.add(new UserGroupContentsListItem(userGroupItemEntityAndObject.userGroupItemEntity,
                    userGroupItemEntityAndObject.kanjiEntry));
        }

        userGroupContentsListAdapter.notifyDataSetChanged();
    }
}
