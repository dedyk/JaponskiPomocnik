package pl.idedyk.android.japaneselearnhelper.dictionary;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.remote.RemoteLuceneConnector;
import pl.idedyk.japanese.dictionary.api.dto.RadicalInfo;
import pl.idedyk.japanese.dictionary.api.dto.TransitiveIntransitivePair;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary.api.keigo.KeigoHelper;
import pl.idedyk.japanese.dictionary.api.tools.KanaHelper;
import pl.idedyk.japanese.dictionary.lucene.LuceneDatabase;

public class RemoteDictionaryManager extends DictionaryManagerCommon {

    public RemoteDictionaryManager() {
        super();
    }

    @Override
    public void init2(Activity activity, ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets, String packageName, int versionCode) {

        // get package info
        PackageInfo packageInfo = null;

        try {
            packageInfo = activity.getPackageManager().getPackageInfo(activity.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e) {
        }

        //

        // wczytanie informacji o pisaniu znakow kana
        if (initKana(activity, loadWithProgress, resources, assets) == false) {
            return;
        }

        // wczytywanie informacji o znakach podstawowych
        try {

            if (initRadical(activity, loadWithProgress, resources, assets) == false) {
                return;
            }

        } catch (DictionaryException e) {
            throw new RuntimeException(e);
        }

        // create word test sm2 manager
        if (initWordTestSM2Manager(activity, loadWithProgress, resources) == false) {
            return;
        }

        // create data manager
        if (initDataManager(activity, loadWithProgress, resources) == false) {
            return;
        }

        databaseConnector = new RemoteLuceneConnector(packageInfo);

        // init zinnia manager
        zinniaManager = new RemoteZinniaManager(packageInfo);

        //

        loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_ready));

        return;

        /*
        try {
            try {
                // delete old database file
                deleteOldDatabaseFile(packageName);

            } catch (IOException e) {
                loadWithProgress.setError(resources.getString(R.string.dictionary_manager_generic_ioerror, e.toString()));

                return;
            }

            File sqliteDatabaseFile = new File(databaseDir, DATABASE_FILE);

            lucenePath = new File(baseDir, LUCENE_DIR);

            // create lucene dir
            lucenePath.mkdirs();

            // delete old sqlite database directory
            sqliteDatabaseFile.delete();

            File databaseVersionFile = new File(sqliteDatabaseFile.getAbsolutePath() + "-version");

            File databaseRecognizeModelFile = new File(databaseDir, KANJI_RECOGNIZE_MODEL_DB_FILE);

            // androidSqliteDatabase = new AndroidSqliteDatabase(lucenePath.getAbsolutePath());

            databaseConnector = luceneDatabase = new LuceneDatabase(lucenePath.getAbsolutePath());

            // get database version
            int databaseVersion = getDatabaseVersion(databaseVersionFile);

            if (versionCode != databaseVersion) {

                try {
                    // copy dictionary file
                    //copyDatabaseFileToDatabaseDir(assets.open(DATABASE_FILE), databaseFile);

                    // delete files from lucene dir
                    File[] lucenePathListFile = lucenePath.listFiles();

                    for (File file : lucenePathListFile) {
                        file.delete();
                    }

                    // unzip lucene database
                    unpackZip(assets.open(LUCENE_ZIP_FILE), lucenePath);

                    // save database version
                    saveDatabaseVersion(databaseVersionFile, versionCode);

                } catch (IOException e) {
                    loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

                    return;
                }
            }

            // open database
            try {
                luceneDatabase.open();

            } catch (Exception e) {
                loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

                return;
            }

            // wczytywanie slow
            loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_words));
            loadWithProgress.setCurrentPos(0);

            fakeProgress(loadWithProgress);

            // wczytanie informacji o pisaniu znakow kana
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

                return;
            }

            // wczytywanie informacji o znakach podstawowych
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

                return;
            }

            // wczytywanie informacji o parach czasownikow przechodnich i nieprzechodnich
            loadWithProgress.setDescription(resources
                    .getString(R.string.dictionary_manager_load_transitive_intransitive_pairs));
            loadWithProgress.setCurrentPos(0);

            try {
                InputStream transitiveIntransitivePairsInputStream = assets.open(TRANSITIVE_INTRANSTIVE_PAIRS_FILE);
                int transitiveIntransitivePairsFileSize = getWordSize(transitiveIntransitivePairsInputStream);
                loadWithProgress.setMaxValue(transitiveIntransitivePairsFileSize);

                transitiveIntransitivePairsInputStream = assets.open(TRANSITIVE_INTRANSTIVE_PAIRS_FILE);
                readTransitiveIntransitivePairsFromCsv(transitiveIntransitivePairsInputStream, loadWithProgress);

            } catch (IOException e) {
                loadWithProgress
                        .setError(resources.getString(R.string.dictionary_manager_generic_ioerror, e.toString()));

                return;
            }

            // wczytywanie kanji
            loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kanji));
            loadWithProgress.setCurrentPos(0);

            fakeProgress(loadWithProgress);

            // copy kanji recognize model db data
            zinniaManager = new LocalZinniaManager(databaseRecognizeModelFile);

            loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kanji_recognize));
            loadWithProgress.setCurrentPos(0);
            loadWithProgress.setMaxValue(1);

            try {
                InputStream kanjiRecognizeModelInputStream = assets.open(KANJI_RECOGNIZE_MODEL_DB_FILE);

                ((LocalZinniaManager)zinniaManager).copyKanjiRecognizeModelToData(kanjiRecognizeModelInputStream, loadWithProgress);
            } catch (IOException e) {
                loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

                return;
            }

        } catch (DictionaryException e) {
            throw new RuntimeException(e);
        }
        */
    }

    @Override
    public void close() {
        // noop
    }

    @Override
    public List<TransitiveIntransitivePair> getTransitiveIntransitivePairsList() {
        return ((RemoteLuceneConnector)databaseConnector).getTransitiveIntransitivePairsList();
    }

    @Override
    public void waitForDatabaseReady() {
       // noop
    }
}
