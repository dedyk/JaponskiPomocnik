package pl.idedyk.android.japaneselearnhelper;

import android.app.Application;
import android.content.res.Configuration;
import android.util.Log;

public class JapaneseAndroidLearnHelperApplication extends Application {
	
	private static JapaneseAndroidLearnHelperApplication singleton;
	
	public static JapaneseAndroidLearnHelperApplication getInstance() {
		return singleton;
	}
	
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
	}

}
