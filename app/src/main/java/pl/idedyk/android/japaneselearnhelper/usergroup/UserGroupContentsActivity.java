package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Toast;

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
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryDetails;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiDetails;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;

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


        //

        createReportProblemButton();
    }

    private void createUserGroupContentsListView() {

        userGroupContentsListView = (ListView)findViewById(R.id.user_group_contents_list);

        userGroupContentsList = new ArrayList<>();
        userGroupContentsListAdapter = new UserGroupContentsListItemAdapter(this, userGroupContentsList);

        userGroupContentsListView.setAdapter(userGroupContentsListAdapter);

        userGroupContentsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                final UserGroupContentsListItem userGroupContentsListItem = (UserGroupContentsListItem)userGroupContentsListAdapter.getItem(position);

                // tworzenie menu podrecznego
                PopupMenu popupMenu = new PopupMenu(UserGroupContentsActivity.this, view);

                popupMenu.getMenu().add(Menu.NONE, R.id.user_group_contents_popup_open, Menu.NONE, R.string.user_group_contents_popup_open);
                popupMenu.getMenu().add(Menu.NONE, R.id.user_group_contents_popup_delete, Menu.NONE, R.string.user_group_contents_popup_delete);

                //

                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int itemId = item.getItemId();

                        if (itemId == R.id.user_group_contents_popup_open) { // otwarcie elementu z listy

                            UserGroupContentsListItem.ItemType type = userGroupContentsListItem.getItemType();

                            switch (type) {

                                case DICTIONARY_ENTRY:

                                    DictionaryEntry dictionaryEntry = userGroupContentsListItem.getDictionaryEntry();

                                    Intent intent = new Intent(getApplicationContext(), WordDictionaryDetails.class);

                                    intent.putExtra("item", dictionaryEntry);

                                    startActivity(intent);

                                    break;


                                case KANJI_ENTRY:

                                    KanjiEntry kanjiEntry = userGroupContentsListItem.getKanjiEntry();

                                    Intent intent2 = new Intent(getApplicationContext(), KanjiDetails.class);

                                    intent2.putExtra("item", kanjiEntry);

                                    startActivity(intent2);

                                    break;

                                    default:
                                        throw new RuntimeException("Unknwon type: " + type);

                            }

                            return true;

                        } else if (itemId == R.id.user_group_contents_popup_delete) { // usuniecie elementu z grupy

                            deleteUserGroupItemEntity(userGroupContentsListItem.getUserGroupItemEntity());

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

    private void loadUserGroupContents() {

        DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

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

        BEFORE_FOR:
        for (UserGroupItemEntity userGroupItemEntity : userGroupItemEntityList) {

            Integer itemId = userGroupItemEntity.getItemId();

            UserGroupItemEntity.Type type = userGroupItemEntity.getType();

            switch (type) {

                case DICTIONARY_ENTRY:

                    DictionaryEntry dictionaryEntry = null;

                    try {
                        dictionaryEntry = dictionaryManager.getDictionaryEntryById(itemId);

                    } catch (DictionaryException e) {
                        Toast.makeText(this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();

                        break BEFORE_FOR;
                    }

                    if (dictionaryEntry == null) {
                        continue;
                    }

                    dictionaryEntryList.add(new UserGroupItemEntityAndObject(userGroupItemEntity, dictionaryEntry));

                    break;

                case KANJI_ENTRY:

                    KanjiEntry kanjiEntry = null;

                    try {
                        kanjiEntry = dictionaryManager.getKanjiEntryById(itemId);

                    } catch (DictionaryException e) {
                        Toast.makeText(this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();

                        break BEFORE_FOR;
                    }

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

    private void deleteUserGroupItemEntity(final UserGroupItemEntity userGroupItemEntity) {

        DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

        final DataManager dataManager = dictionaryManager.getDataManager();

        //

        final AlertDialog alertDialog = new AlertDialog.Builder(this).create();

        alertDialog.setTitle(getString(R.string.user_group_delete_user_group_item_entity_title));
        alertDialog.setMessage(getString(R.string.user_group_delete_user_group_item_entity_message));

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getString(R.string.user_group_ok_button), new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // usuniecie elementu listy
                dataManager.deleteItemIdFromUserGroup(userGroupEntity, userGroupItemEntity.getType(), userGroupItemEntity.getItemId());

                // komunikat
                Toast.makeText(UserGroupContentsActivity.this,
                        getString(R.string.user_group_delete_user_group_item_entity_toast), Toast.LENGTH_SHORT).show();

                // zaladowanie nowej zawartosci grupy
                loadUserGroupContents();
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

    private void createReportProblemButton() {

        Button reportProblemButton = (Button)findViewById(R.id.user_group_contents_report_problem_button);

        reportProblemButton.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {

                StringBuffer userGroupContentsListText = new StringBuffer();

                for (int userGroupContentsListAdapterIdx = 0; userGroupContentsListAdapterIdx < userGroupContentsListAdapter.size(); ++userGroupContentsListAdapterIdx) {
                    userGroupContentsListText.append(((UserGroupContentsListItem)userGroupContentsListAdapter.getItem(userGroupContentsListAdapterIdx)).getText().toString()).append("\n--\n");
                }

                String chooseEmailClientTitle = getString(R.string.choose_email_client);

                String mailSubject = getString(R.string.user_group_contents_report_problem_email_subject);

                String mailBody = getString(R.string.user_group_contents_report_problem_email_body, userGroupContentsListText.toString());

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
}
