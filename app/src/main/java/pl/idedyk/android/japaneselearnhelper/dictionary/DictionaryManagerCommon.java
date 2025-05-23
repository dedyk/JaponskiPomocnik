package pl.idedyk.android.japaneselearnhelper.dictionary;

import android.app.Activity;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import com.csvreader.CsvReader;
//import com.google.android.gms.wearable.Asset;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;
import pl.idedyk.japanese.dictionary.api.dto.RadicalInfo;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary.api.keigo.KeigoHelper;
import pl.idedyk.japanese.dictionary.api.tools.KanaHelper;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

public abstract class DictionaryManagerCommon extends DictionaryManagerAbstract {

    protected static final String KANA_FILE = "kana.csv";

    protected static final String RADICAL_FILE = "radical.csv";

    protected File baseDir;
    protected File databaseDir;

    //

    private WordTestSM2Manager wordTestSM2Manager;

    private DataManager dataManager;

    protected ZinniaManagerCommon zinniaManager;

    //

    private List<RadicalInfo> radicalList = null;

    private KanaHelper kanaHelper;

    protected final KeigoHelper keigoHeper;

    protected DictionaryManagerCommon() {
        keigoHeper = new KeigoHelper();
    }

    public final void init(Activity activity, ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets, String packageName, int versionCode) {

        // init
        loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_init));

        // detect external storage legacy
        boolean externalStorageLegacy = true;

        try {
            Method isExternalStorageLegacyMethod = Environment.class.getMethod("isExternalStorageLegacy");

            externalStorageLegacy = (Boolean)isExternalStorageLegacyMethod.invoke(null);

        } catch (Exception e) {
            externalStorageLegacy = true;

        }

        // check external storage state
        if (checkExternalStorageState(loadWithProgress, resources) == false) {
            return;
        }

        File externalStorageDirectory;

        if (externalStorageLegacy == true) { // stary sposob

            // pobranie glownego katalogu to storage
            File oldStorageDirFile = Environment.getExternalStorageDirectory();
            File oldBaseDir = calculateBaseDir(oldStorageDirFile);

            // pobranie nowej lokalizacji
            File newStorageDirFile = getNewStorageDirFile(activity);
            File newBaseDir = calculateBaseDir(newStorageDirFile);

            if (oldBaseDir.exists() == true) { // jezeli stara lokalizacja istnieje to przenies to w nowe miejsce

                boolean moveStorageResult = moveFile(oldBaseDir, newBaseDir);

                if (moveStorageResult == false) { // operacja nie udala sie

                    loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

                    return;
                }
            }

            // ustawienie nowej glownej lokalizacji
            externalStorageDirectory = newStorageDirFile;

        } else { // new way Android 11+
            externalStorageDirectory = getNewStorageDirFile(activity);
        }

        if (externalStorageDirectory == null) {
            loadWithProgress.setError(resources.getString(R.string.dictionary_manager_null_external_storage));

            return;
        }

        // create base dir
        baseDir = calculateBaseDir(externalStorageDirectory);

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

    private File calculateBaseDir(File dir) {
        return new File(dir, "JaponskiPomocnik");
    }

    private File getNewStorageDirFile(Activity activity) {

        File newStorageDirFile = activity.getExternalFilesDir(null);

        if (newStorageDirFile == null) { // to chyba nie powinno zadarzyc sie

            newStorageDirFile = new File(Environment.getExternalStorageDirectory(), "/Android/data/" + activity.getPackageName() + "/files");

            newStorageDirFile.mkdirs();

            //

            //newStorageDirFile = activity.getExternalFilesDir(null);
        }

        return newStorageDirFile;
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

                    KanjiCharacterInfo kanjiEntry = null;

                    try {
                        kanjiEntry = findKanji(kanji);

                    } catch (DictionaryException e) {
                        loadWithProgress.setError(resources.getString(R.string.dictionary_manager_generic_ioerror, e.getMessage()));

                        return false;
                    }

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

    protected boolean initKana(Activity activity, ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets) {

        loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kana));
        loadWithProgress.setCurrentPos(0);

        try {
            InputStream kanaFileInputStream = assets.open(KANA_FILE);
            int kanaFileSize = getWordSize(kanaFileInputStream);
            loadWithProgress.setMaxValue(kanaFileSize);

            kanaFileInputStream = assets.open(KANA_FILE);
            readKanaFile(kanaFileInputStream, loadWithProgress);

        } catch (IOException e) {
            loadWithProgress
                    .setError(resources.getString(R.string.dictionary_manager_generic_ioerror, e.toString()));

            return false;
        }

        return true;
    }

    private void readKanaFile(InputStream kanaFileInputStream, ILoadWithProgress loadWithProgress) throws IOException {

        CsvReader csvReader = new CsvReader(new InputStreamReader(kanaFileInputStream), ',');

        int currentPos = 1;

        Map<String, List<KanjivgEntry>> kanaAndStrokePaths = new HashMap<String, List<KanjivgEntry>>();

        while (csvReader.readRecord()) {

            currentPos++;

            loadWithProgress.setCurrentPos(currentPos);

            //int id = Integer.parseInt(csvReader.get(0));

            String kana = csvReader.get(1);

            String strokePath1String = csvReader.get(2);

            String strokePath2String = csvReader.get(3);

            List<KanjivgEntry> strokePaths = new ArrayList<KanjivgEntry>();

            strokePaths.add(new KanjivgEntry(Utils.parseStringIntoList(strokePath1String /*, false */)));

            if (strokePath2String == null || strokePath2String.equals("") == false) {
                strokePaths.add(new KanjivgEntry(Utils.parseStringIntoList(strokePath2String /*, false */)));
            }

            kanaAndStrokePaths.put(kana, strokePaths);
        }

        kanaHelper = new KanaHelper(kanaAndStrokePaths);

        csvReader.close();
    }

    protected boolean initRadical(Activity activity, ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets) throws DictionaryException {

        loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_radical));
        loadWithProgress.setCurrentPos(0);

        try {
            InputStream radicalInputStream = assets.open(RADICAL_FILE);
            int radicalFileSize = getWordSize(radicalInputStream);
            loadWithProgress.setMaxValue(radicalFileSize);

            radicalInputStream = assets.open(RADICAL_FILE);
            readRadicalEntriesFromCsv(radicalInputStream, loadWithProgress);

        } catch (IOException e) {
            loadWithProgress
                    .setError(resources.getString(R.string.dictionary_manager_generic_ioerror, e.toString()));

            return false;
        }

        return true;
    }

    private void readRadicalEntriesFromCsv(InputStream radicalInputStream, ILoadWithProgress loadWithProgress)
            throws IOException, DictionaryException {

        radicalList = new ArrayList<RadicalInfo>();

        CsvReader csvReader = new CsvReader(new InputStreamReader(radicalInputStream), ',');

        int currentPos = 1;

        while (csvReader.readRecord()) {

            currentPos++;

            loadWithProgress.setCurrentPos(currentPos);

            int id = Integer.parseInt(csvReader.get(0));

            String radical = csvReader.get(1);

            if (radical.equals("") == true) {
                throw new DictionaryException("Empty radical: " + radical);
            }

            String strokeCountString = csvReader.get(2);

            int strokeCount = Integer.parseInt(strokeCountString);

            RadicalInfo entry = new RadicalInfo();

            entry.setId(id);
            entry.setRadical(radical);
            entry.setStrokeCount(strokeCount);

            radicalList.add(entry);
        }

        csvReader.close();
    }

    private boolean checkExternalStorageState(ILoadWithProgress loadWithProgress, Resources resources) {

        String state = Environment.getExternalStorageState();

        if (Environment.MEDIA_MOUNTED.equals(state) == false) {
            loadWithProgress.setError(resources.getString(R.string.dictionary_manager_bad_external_storage_state));

            return false;
        }

        return true;
    }

    private static boolean moveFile(File sourceFile, File destinationFile) {

        if (sourceFile.isFile() == true) {

            // kopiujemy zawartosc
            byte[] buffer = new byte[1024];
            int count;

            FileInputStream sourceFileInputStream = null;
            FileOutputStream destinationFileOutputStream = null;

            try {
                sourceFileInputStream = new FileInputStream(sourceFile);
                destinationFileOutputStream = new FileOutputStream(destinationFile);

                while ((count = sourceFileInputStream.read(buffer)) != -1) {
                    destinationFileOutputStream.write(buffer, 0, count);
                }

            } catch (IOException e) {
                return false;

            } finally {

                if (sourceFileInputStream != null) {

                    try {
                        sourceFileInputStream.close();
                    } catch (IOException e) {
                        //noop
                    }
                }

                if (destinationFileOutputStream != null) {
                    try {
                        destinationFileOutputStream.close();
                    } catch (IOException e) {
                        //noop
                    }
                }

                if (sourceFile.delete() == false) {
                    return false;
                }
            }

        } else if (sourceFile.isDirectory() == true) {

            if (destinationFile.isDirectory() == false) {

                if (destinationFile.mkdir() == false) {
                    return false;
                }
            }

            File[] sourceDirFileList = sourceFile.listFiles();

            for (File currentSourceDirFile : sourceDirFileList) {

                if (moveFile(currentSourceDirFile, new File(destinationFile, currentSourceDirFile.getName())) == false) {
                    return false;
                }
            }

            if (sourceFile.delete() == false) {
                return false;
            }

        } else {
            throw new RuntimeException();
        }

        return true;
    }

    protected int getWordSize(InputStream dictionaryInputStream) throws IOException {

        int size = 0;

        CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');

        while (csvReader.readRecord()) {
            size++;
        }

        dictionaryInputStream.close();

        return size;
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
    public KanaHelper getKanaHelper() {
        return kanaHelper;
    }

    @Override
    public KeigoHelper getKeigoHelper() {
        return keigoHeper;
    }

    @Override
    public List<RadicalInfo> getRadicalList() {
        return radicalList;
    }

    @Override
    protected void finalize() throws Throwable {

        super.finalize();

        dataManager.close();

        wordTestSM2Manager.close();
    }

    public static DictionaryManagerCommon getDictionaryManager() {

        // wersja zdalna
        //return new RemoteDictionaryManager();

        // wersja lokalna
        return new LocalDictionaryManager();
    }
}
