package pl.idedyk.android.japaneselearnhelper.dictionary;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import java.io.File;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupItemEntity;
import pl.idedyk.android.japaneselearnhelper.data.exception.DataManagerException;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.TestSM2ManagerException;
import pl.idedyk.japanese.dictionary.api.dictionary.DictionaryManagerAbstract;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;

public abstract class DictionaryManagerCommon extends DictionaryManagerAbstract {

    protected File baseDir;
    protected File databaseDir;

    //

    private WordTestSM2Manager wordTestSM2Manager;

    private DataManager dataManager;

    protected ZinniaManagerCommon zinniaManager;

    public final void init(Activity activity, ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets, String packageName, int versionCode) {

        // init
        loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_init));

        // check external storage state
        if (checkExternalStorageState(loadWithProgress, resources) == false) {
            return;
        }

        // create base dir in external storage
        File externalStorageDirectory = Environment.getExternalStorageDirectory();

        // create base dir
        baseDir = new File(externalStorageDirectory, "JaponskiPomocnik");

        if (baseDir.isDirectory() == false) {

            if (baseDir.mkdirs() == false) {
                loadWithProgress.setError(resources.getString(R.string.dictionary_manager_create_directories_error));

                return;
            }
        }

        // create directory dir
        databaseDir = new File(baseDir, "db");

        if (databaseDir.isDirectory() == false) {

            if (databaseDir.mkdirs() == false) {
                loadWithProgress.setError(resources.getString(R.string.dictionary_manager_create_directories_error));

                return;
            }
        }

        // dalsze czynnosci
        init2(activity, loadWithProgress, resources, assets, packageName, versionCode);
    }

    protected boolean initDataManager(Activity activity, ILoadWithProgress loadWithProgress, Resources resources) {

        // create data manager
        dataManager = new DataManager(databaseDir);

        // open data manager
        try {
            dataManager.open();
        } catch (DataManagerException e) {
            loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

            return false;
        }

        // migrate old own group from kanji test
        loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_migrate_old_own_groups_from_kanji_test));
        loadWithProgress.setCurrentPos(0);

        final ConfigManager.KanjiTestConfig kanjiTestConfig = JapaneseAndroidLearnHelperApplication.getInstance()
                .getConfigManager(activity).getKanjiTestConfig();

        List<String> kanjiTestOldOwnGroupList = kanjiTestConfig.getOwnGroupList();

        if (kanjiTestOldOwnGroupList != null && kanjiTestOldOwnGroupList.size() > 0) {

            for (String kanjiTestOldOwnGroupName : kanjiTestOldOwnGroupList) {

                String kanjiTestOldOwnGroupNameMigrate = kanjiTestOldOwnGroupName + " " + resources.getString(R.string.dictionary_manager_migrate_old_own_groups_from_kanji_test_group_postfix);

                //

                List<UserGroupEntity> userGroupEntityList = dataManager.findUserGroupEntity(UserGroupEntity.Type.USER_GROUP, kanjiTestOldOwnGroupNameMigrate);

                UserGroupEntity userGroupEntity = null;

                if (userGroupEntityList == null || userGroupEntityList.size() == 0) {

                    userGroupEntity = new UserGroupEntity(null, UserGroupEntity.Type.USER_GROUP, kanjiTestOldOwnGroupNameMigrate);

                    dataManager.addUserGroup(userGroupEntity);

                    userGroupEntityList = dataManager.findUserGroupEntity(UserGroupEntity.Type.USER_GROUP, kanjiTestOldOwnGroupNameMigrate);
                }

                userGroupEntity = userGroupEntityList.get(0);

                //

                Set<String> ownGroupKanjiList = kanjiTestConfig.getOwnGroupKanjiList(kanjiTestOldOwnGroupName);

                for (String kanji : ownGroupKanjiList) {

                    KanjiEntry kanjiEntry = findKanji(kanji);

                    if (kanjiEntry != null) {

                        boolean isItemIdExists = dataManager.isItemIdExistsInUserGroup(userGroupEntity, UserGroupItemEntity.Type.KANJI_ENTRY, kanjiEntry.getId());

                        if (isItemIdExists == false) {
                            dataManager.addItemIdToUserGroup(userGroupEntity, UserGroupItemEntity.Type.KANJI_ENTRY, kanjiEntry.getId());
                        }
                    }
                }

                kanjiTestConfig.deleteOwnGroup(kanjiTestOldOwnGroupName);
            }
        }

        return true;
    }

    protected boolean initWordTestSM2Manager(Activity activity, ILoadWithProgress loadWithProgress, Resources resources) {

        wordTestSM2Manager = new WordTestSM2Manager(databaseDir);

        // open word test sm2 manager
        try {
            wordTestSM2Manager.open();
        } catch (TestSM2ManagerException e) {
            loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

            return false;
        }

        return true;
    }

    private boolean checkExternalStorageState(ILoadWithProgress loadWithProgress, Resources resources) {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state) == false) {
            loadWithProgress.setError(resources.getString(R.string.dictionary_manager_bad_external_storage_state));

            return false;
        }

        return true;
    }


    public abstract void init2(Activity activity, ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets, String packageName, int versionCode);

    public abstract void close();

    public DataManager getDataManager() {
        return dataManager;
    }

    public WordTestSM2Manager getWordTestSM2Manager() {
        return wordTestSM2Manager;
    }

    public ZinniaManagerCommon getZinniaManager() {
        return zinniaManager;
    }

    @Override
    protected void finalize() throws Throwable {

        super.finalize();

        dataManager.close();

        wordTestSM2Manager.close();
    }

    public static DictionaryManagerCommon getDictionaryManager() {

        // FIXME !!!!!!!!!!!!!!!!!!!!!!!
        return new RemoteDictionaryManager();

        //return new LocalDictionaryManager();
    }
}
