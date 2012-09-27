package pl.idedyk.android.japaneselearnhelper;

import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import android.app.Application;
import android.content.res.Configuration;

public class JapaneseAndroidLearnHelperApplication extends Application {
	
	private static JapaneseAndroidLearnHelperApplication singleton;
	
	public static JapaneseAndroidLearnHelperApplication getInstance() {
		return singleton;
	}
	
	private JapaneseAndroidLearnHelperContext context;
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	@Override
	public void onCreate() {
		super.onCreate();
		
		singleton = this;
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
	}

	@Override
	public void onTerminate() {
		super.onTerminate();
		
		DictionaryManager.getInstance().close();
	}
	
	public void setContext(JapaneseAndroidLearnHelperContext context) {
		this.context = context;
	}

	public JapaneseAndroidLearnHelperContext getContext() {
		return context;
	}
}
