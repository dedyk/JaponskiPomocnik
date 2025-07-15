package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupItemEntity;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.utils.WordKanjiDictionaryUtils;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

public class UserGroupActivity extends Activity {

    private ListView userGroupListView;

    private UserGroupListItemAdapter userGroupListAdapter;

    private List<UserGroupListItem> userGroupList;

    //

    private DictionaryEntry dictionaryEntryToAdd = null;

    private KanjiCharacterInfo kanjiEntryToAdd = null;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuShorterHelper.onCreateOptionsMenu(menu);

        menu.add(Menu.NONE, R.id.user_group_menu_create_new_group, Menu.NONE, R.string.user_group_menu_create_new_group);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        int itemId = item.getItemId();

        if (itemId == R.id.user_group_menu_create_new_group) { // tworzenie nowej grupy

            createOrUpdateUserGroup(null);

            return true;

        } else {
            return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
        }
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

        JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.id.rootView, R.layout.user_group);

        JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_user_group));

        //

        createUserGroupListView();

        //

        loadUserGroups();

        //

        checkItemToAdd();

        //

        // createReportProblemButton();
    }

    @Override
    public void onBackPressed() {

        final Intent mIntent = new Intent();

        setResult(RESULT_OK, mIntent);

        finish();
    }

    private void createUserGroupListView() {

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
    }

    private void loadUserGroups() {

        DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

        DataManager dataManager = dictionaryManager.getDataManager();

        userGroupList.clear();

        List<UserGroupEntity> allUserGroupsList = dataManager.getAllUserGroupList();

        for (UserGroupEntity currentUserGroupEntity : allUserGroupsList) {
            userGroupList.add(new UserGroupListItem(currentUserGroupEntity, getResources()));
        }

        userGroupListAdapter.notifyDataSetChanged();
    }

    private void createOrUpdateUserGroup(final UserGroupEntity userGroupEntityToUpdate) {

        final EditText groupNameEditText = new EditText(this);

        groupNameEditText.setInputType(InputType.TYPE_CLASS_TEXT);

        if (userGroupEntityToUpdate != null) {
            groupNameEditText.setText(userGroupEntityToUpdate.getName());
        }

        //

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(getString(userGroupEntityToUpdate == null ? R.string.user_group_new_group_dialog_title : R.string.user_group_update_group_dialog_title));
        alertDialog.setMessage(getString(userGroupEntityToUpdate == null ? R.string.user_group_new_group_dialog_message : R.string.user_group_update_group_dialog_message));

        alertDialog.setView(groupNameEditText);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.user_group_ok_button), (DialogInterface.OnClickListener)null);
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.user_group_cancel_button), (DialogInterface.OnClickListener)null);

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {

                Button okButton = alertDialog.getButton(AlertDialog.BUTTON_POSITIVE);

                okButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {

                        DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(UserGroupActivity.this);

                        DataManager dataManager = dictionaryManager.getDataManager();

                        //

                        String newGroupName = groupNameEditText.getText().toString();

                        if (newGroupName == null || newGroupName.length() < 1) {

                            Toast.makeText(UserGroupActivity.this,
                                    getString(R.string.user_group_new_group_name_too_short_error), Toast.LENGTH_SHORT).show();

                            return;
                        }

                        if (newGroupName.length() > 90) {
                            Toast.makeText(UserGroupActivity.this,
                                    getString(R.string.user_group_new_group_name_too_long_error), Toast.LENGTH_SHORT).show();

                            return;
                        }

                        // sprawdzamy, czy grupa o takiej nazwie juz istnieje
                        List<UserGroupEntity> findUserGroupEntityResultList = dataManager.findUserGroupEntity(null, newGroupName);

                        if (findUserGroupEntityResultList != null && findUserGroupEntityResultList.size() > 0) { // grupa o podanej nazwie juz istnieje

                            Toast.makeText(UserGroupActivity.this,
                                    getString(R.string.user_group_new_group_already_exists_error), Toast.LENGTH_SHORT).show();

                            return;
                        }

                        if (userGroupEntityToUpdate == null) { // jesli to nowa grupa, tworzymy ja

                            // nie znaleziono, tworzymy nowa grupe
                            UserGroupEntity newUserGroupEntity = new UserGroupEntity(null, UserGroupEntity.Type.USER_GROUP, newGroupName);

                            dataManager.addUserGroup(newUserGroupEntity);

                        } else { // uaktualnienie nazwy grupy

                            userGroupEntityToUpdate.setName(newGroupName);

                            dataManager.updateUserGroup(userGroupEntityToUpdate);
                        }

                        alertDialog.dismiss();

                        Toast.makeText(UserGroupActivity.this,
                                getString(userGroupEntityToUpdate == null ? R.string.user_group_new_group_created : R.string.user_group_update_group_done), Toast.LENGTH_SHORT).show();

                        // zaladowanie nowe listy grup
                        loadUserGroups();
                    }
                });

                Button cancelButton = alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE);

                cancelButton.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        alertDialog.dismiss();
                    }
                });
            }
        });

        if (isFinishing() == false) {
            alertDialog.show();
        }
    }

    private void checkItemToAdd() {

        Serializable itemToAdd = getIntent().getSerializableExtra("itemToAdd");

        if (itemToAdd != null) {

            TextView itemToAddValueTextView = (TextView)findViewById(R.id.user_group_item_to_add_value);
            TextView itemToAddValueLabelTextView = (TextView)findViewById(R.id.user_group_item_to_add_value_label);
            //View itemToAddTextViewLine1 = (View)findViewById(R.id.user_group_item_to_add_line1);
            View itemToAddTextViewLine2 = (View)findViewById(R.id.user_group_item_to_add_line2);

            //

            CharSequence itemToAddToCharSequence = null;

            if (itemToAdd instanceof DictionaryEntry) {
                dictionaryEntryToAdd = (DictionaryEntry)itemToAdd;

                itemToAddToCharSequence = WordKanjiDictionaryUtils.getWordFullTextWithMark(dictionaryEntryToAdd);

            } else if (itemToAdd instanceof KanjiCharacterInfo) {
                kanjiEntryToAdd = (KanjiCharacterInfo)itemToAdd;

                String itemToAddToString = WordKanjiDictionaryUtils.getKanjiFullTextWithMark(kanjiEntryToAdd);

                itemToAddToCharSequence = Html.fromHtml(itemToAddToString.replaceAll("\n", "<br/>"));

            } else {
                throw new RuntimeException("Unknown itemToAdd: " + itemToAdd);
            }

            itemToAddValueTextView.setText(itemToAddToCharSequence);

            itemToAddValueTextView.setVisibility(View.VISIBLE);
            itemToAddValueLabelTextView.setVisibility(View.VISIBLE);
            //itemToAddTextViewLine1.setVisibility(View.VISIBLE);
            itemToAddTextViewLine2.setVisibility(View.VISIBLE);
        }
    }

    public void addItemIdToUserGroupEntity(final UserGroupEntity userGroupEntity) {

        DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

        final DataManager dataManager = dictionaryManager.getDataManager();

        //

        final Integer itemId;
        final UserGroupItemEntity.Type itemIdUserGroupItemEntityType;

        if (dictionaryEntryToAdd != null) {
            itemId = dictionaryEntryToAdd.getId();
            itemIdUserGroupItemEntityType = UserGroupItemEntity.Type.DICTIONARY_ENTRY;

        } else if (kanjiEntryToAdd != null) {
            itemId = kanjiEntryToAdd.getId();
            itemIdUserGroupItemEntityType = UserGroupItemEntity.Type.KANJI_ENTRY;

        } else {
            throw new RuntimeException("dictionaryEntryToAdd and kanjiEntryToAdd is null");
        }

        boolean itemIdAlreadyExists = dataManager.isItemIdExistsInUserGroup(userGroupEntity, itemIdUserGroupItemEntityType, itemId);

        if (itemIdAlreadyExists == true) {

            Toast.makeText(UserGroupActivity.this,
                    getString(R.string.user_group_item_to_add_user_group_entity_already_exists), Toast.LENGTH_SHORT).show();

            return;
        }

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(getString(R.string.user_group_item_to_add_user_group_entity_title));
        alertDialog.setMessage(getString(R.string.user_group_item_to_add_user_group_entity_message,
                userGroupEntity.getType() == UserGroupEntity.Type.USER_GROUP ? userGroupEntity.getName() :
                getString(R.string.user_group_star_group)));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.user_group_ok_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // dodanie do grupy
                dataManager.addItemIdToUserGroup(userGroupEntity, itemIdUserGroupItemEntityType, itemId);

                // komunikat
                Toast.makeText(UserGroupActivity.this,
                        getString(R.string.user_group_item_to_add_user_group_entity_toast, userGroupEntity.getType() == UserGroupEntity.Type.USER_GROUP ? userGroupEntity.getName() :
                                getString(R.string.user_group_star_group)), Toast.LENGTH_SHORT).show();

                //

                /*
                dictionaryEntryToAdd = null;
                kanjiEntryToAdd = null;

                //

                TextView itemToAddValueTextView = (TextView) findViewById(R.id.user_group_item_to_add_value);
                TextView itemToAddValueLabelTextView = (TextView) findViewById(R.id.user_group_item_to_add_value_label);
                View itemToAddTextViewLine1 = (View) findViewById(R.id.user_group_item_to_add_line1);
                View itemToAddTextViewLine2 = (View) findViewById(R.id.user_group_item_to_add_line2);

                itemToAddValueTextView.setVisibility(View.GONE);
                itemToAddValueLabelTextView.setVisibility(View.GONE);
                itemToAddTextViewLine1.setVisibility(View.GONE);
                itemToAddTextViewLine2.setVisibility(View.GONE);
                */
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.user_group_cancel_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();

            }
        });

        if (isFinishing() == false) {
            alertDialog.show();
        }
    }

    private void deleteUserGroup(final UserGroupEntity userGroupEntity) {

        DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

        final DataManager dataManager = dictionaryManager.getDataManager();

        //

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(getString(R.string.user_group_delete_user_group_entity_title));
        alertDialog.setMessage(getString(R.string.user_group_delete_user_group_entity_message, userGroupEntity.getName()));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.user_group_ok_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // usuniecie grupy
                dataManager.deleteUserGroup(userGroupEntity);

                // komunikat
                Toast.makeText(UserGroupActivity.this,
                        getString(R.string.user_group_delete_user_group_entity_toast, userGroupEntity.getName()), Toast.LENGTH_SHORT).show();

                // zaladowanie nowe listy grup
                loadUserGroups();
            }
        });

        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, getString(R.string.user_group_cancel_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();

            }
        });

        if (isFinishing() == false) {
            alertDialog.show();
        }
    }

    /*
    private void createReportProblemButton() {

        Button reportProblemButton = (Button)findViewById(R.id.user_group_report_problem_button);

        reportProblemButton.setOnClickListener(new OnClickListener() {

            public void onClick(View view) {

                StringBuffer userGroupListText = new StringBuffer();

                for (int userGroupListAdapterIdx = 0; userGroupListAdapterIdx < userGroupListAdapter.size(); ++userGroupListAdapterIdx) {
                    userGroupListText.append(((UserGroupListItem)userGroupListAdapter.getItem(userGroupListAdapterIdx)).getText().toString()).append("\n--\n");
                }

                String chooseEmailClientTitle = getString(R.string.choose_email_client);

                String mailSubject = getString(R.string.user_group_report_problem_email_subject);

                String mailBody = getString(R.string.user_group_report_problem_email_body, userGroupListText.toString());

                String versionName = "";
                int versionCode = 0;

                try {
                    PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

                    versionName = packageInfo.versionName;
                    versionCode = packageInfo.versionCode;

                } catch (PackageManager.NameNotFoundException e) {
                }

                Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString(), versionName, versionCode);

                startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));
            }
        });
    }
    */
}