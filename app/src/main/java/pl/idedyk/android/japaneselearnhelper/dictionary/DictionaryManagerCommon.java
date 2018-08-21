package pl.idedyk.android.japaneselearnhelper.dictionary;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;

import pl.idedyk.japanese.dictionary.api.dictionary.DictionaryManagerAbstract;

public abstract class DictionaryManagerCommon extends DictionaryManagerAbstract {

    public abstract void init(Activity activity, ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets, String packageName, int versionCode);

    public abstract void close();

    public static DictionaryManagerCommon getDictionaryManager() {
        return new LocalDictionaryManager();
    }
}
