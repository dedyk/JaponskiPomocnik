package pl.idedyk.android.japaneselearnhelper.dictionary;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.content.res.Resources;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.remote.RemoteLuceneConnector;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.WordPowerList;
import pl.idedyk.japanese.dictionary.api.dto.TransitiveIntransitivePairWithDictionaryEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;

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
    }

    @Override
    public void close() {
        // noop
    }

    @Override
    public List<TransitiveIntransitivePairWithDictionaryEntry> getTransitiveIntransitivePairsList() throws DictionaryException {
        return ((RemoteLuceneConnector)databaseConnector).getTransitiveIntransitivePairsList();
    }

    @Override
    public void waitForDatabaseReady() {
       // noop
    }

    @Override
    public WordPowerList getWordPowerList() throws DictionaryException {
        return ((RemoteLuceneConnector)databaseConnector).getWordPowerList();
    }
}
