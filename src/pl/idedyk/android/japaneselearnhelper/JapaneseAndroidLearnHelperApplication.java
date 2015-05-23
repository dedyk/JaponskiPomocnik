package pl.idedyk.android.japaneselearnhelper;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ILoadWithProgress;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryMissingWordQueue;
import android.app.Activity;
import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Typeface;

public class JapaneseAndroidLearnHelperApplication extends Application {
	
	private static JapaneseAndroidLearnHelperApplication singleton;
	
	public static JapaneseAndroidLearnHelperApplication getInstance() {
		return singleton;
	}
	
	private JapaneseAndroidLearnHelperContext context;
	
	private DictionaryManager dictionaryManager;
	
	private ConfigManager configManager;
	
	private WordDictionaryMissingWordQueue wordDictionaryMissingWordQueue;
	
	private Typeface babelStoneHanSubset = null;
	
	private Tracker tracker = null;
	
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
		
		if (dictionaryManager != null) {
			dictionaryManager.close();
		}
	}
	
	public JapaneseAndroidLearnHelperContext getContext() {
		
		if (context == null) {
			context = new JapaneseAndroidLearnHelperContext();
		}
		
		return context;
	}

	public DictionaryManager getDictionaryManager(final Activity activity) {
		
		Resources resources = activity.getResources();
		AssetManager assets = activity.getAssets();
				
		if (dictionaryManager == null) {
			
	        int versionCode = 0;
	        
	        try {
	        	PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
	        	
	            versionCode = packageInfo.versionCode;

	        } catch (NameNotFoundException e) {        	
	        }
	        
	        ILoadWithProgress loadWithProgress = new ILoadWithProgress() {
				
				@Override
				public void setMaxValue(int maxValue) {
				}
				
				@Override
				public void setError(String errorMessage) {					
					throw new RuntimeException(errorMessage);					
				}
				
				@Override
				public void setDescription(String desc) {
				}
				
				@Override
				public void setCurrentPos(int currentPos) {
				}
			};
						
			dictionaryManager = new DictionaryManager();
			
			dictionaryManager.init(loadWithProgress, resources, assets, getPackageName(), versionCode);
		}
		
		return dictionaryManager;
	}

	public void setDictionaryManager(DictionaryManager dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}

	public ConfigManager getConfigManager(Activity activity) {
		
		if (configManager == null) {
			configManager = new ConfigManager(activity);
		}
		
		return configManager;
	}

	public void setConfigManager(ConfigManager configManager) {
		this.configManager = configManager;
	}
	
	public WordDictionaryMissingWordQueue getWordDictionaryMissingWordQueue(Activity activity) {
		
		if (wordDictionaryMissingWordQueue == null) {
			wordDictionaryMissingWordQueue = new WordDictionaryMissingWordQueue(activity);
		}
		
		return wordDictionaryMissingWordQueue;
	}

	public void setWordDictionaryMissingWordQueue(WordDictionaryMissingWordQueue wordDictionaryMissingWordQueue) {
		this.wordDictionaryMissingWordQueue = wordDictionaryMissingWordQueue;
	}

	public Typeface getBabelStoneHanSubset(AssetManager assetManager) {
		
		if (babelStoneHanSubset != null) {
			return babelStoneHanSubset;
		}
		
		babelStoneHanSubset = Typeface.createFromAsset(getAssets(), "BabelStoneHan-subset.ttf");
		
		return babelStoneHanSubset;
	}
	
	public Tracker getTracker() {
		
		if (tracker != null) {
			return tracker;
		}
		
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

		tracker = analytics.newTracker(R.xml.app_tracker);

		tracker.enableAdvertisingIdCollection(true);
		
		return tracker;		
	}
	
	public void logScreen(String screenName) {
		
		Tracker tracker = getTracker();
		
		tracker.setScreenName(screenName);
		
		tracker.send(new HitBuilders.AppViewBuilder().build());		
	}
	
	public void logEvent(String screenName, String actionName, String label) {
		
		Tracker tracker = getTracker();
		
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory(screenName)
				.setAction(actionName).
				setLabel(label).
				build());		
	}
}
