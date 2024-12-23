package pl.idedyk.android.japaneselearnhelper;

//import com.google.android.gms.analytics.GoogleAnalytics;
//import com.google.android.gms.analytics.HitBuilders;
//import com.google.android.gms.analytics.Tracker;

import pl.idedyk.android.japaneselearnhelper.common.queue.QueueEventThread;
import pl.idedyk.japanese.dictionary.api.android.queue.event.IQueueEvent;
import pl.idedyk.japanese.dictionary.api.android.queue.event.StatLogEventEvent;
import pl.idedyk.japanese.dictionary.api.android.queue.event.StatLogScreenEvent;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.dictionary.ILoadWithProgress;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryMissingWordQueue;
import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.AssetManager;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.content.res.TypedArray;
import androidx.databinding.DataBindingUtil;
import androidx.multidex.MultiDexApplication;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;

public class JapaneseAndroidLearnHelperApplication extends MultiDexApplication {

	public static final ThemeType defaultThemeType = ThemeType.BLACK;
	
	private static JapaneseAndroidLearnHelperApplication singleton;

	public static JapaneseAndroidLearnHelperApplication getInstance() {
		return singleton;
	}
	
	private JapaneseAndroidLearnHelperContext context;
	
	private DictionaryManagerCommon dictionaryManager;
	
	private ConfigManager configManager;
	
	private WordDictionaryMissingWordQueue wordDictionaryMissingWordQueue;

	private QueueEventThread queueEventThread;
	
	private Typeface babelStoneHanSubset = null;
	
	// private Tracker tracker = null;

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

		stopQueueThread();

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

	public DictionaryManagerCommon getDictionaryManager(final Activity activity) {
		
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
						
			dictionaryManager = DictionaryManagerCommon.getDictionaryManager();
			
			dictionaryManager.init(activity, loadWithProgress, resources, assets, getPackageName(), versionCode);
		}
		
		return dictionaryManager;
	}

	public void setDictionaryManager(DictionaryManagerCommon dictionaryManager) {
		this.dictionaryManager = dictionaryManager;
	}

	public DataManager getDataManager() {

		if (dictionaryManager == null) {
			return null;
		}

		return dictionaryManager.getDataManager();
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

	public void setContentViewAndTheme(Activity activity, int contentViewId) {
		activity.setTheme(getThemeType(activity).styleId);

		DataBindingUtil.setContentView(activity, contentViewId);
	}

	public ThemeType getThemeType() {
		return getThemeType(null);
	}

	private ThemeType getThemeType(Activity activity) {

		if (configManager != null) {
			return configManager.getCommonConfig().getThemeType(defaultThemeType);
		}

		if (activity == null) {
			return defaultThemeType;
		}

		getConfigManager(activity);

		if (configManager != null) {
			return configManager.getCommonConfig().getThemeType(defaultThemeType);
		}

		return defaultThemeType;
	}

	public int getLinkColor() {

		TypedArray typedArray = getTheme().obtainStyledAttributes(
				getThemeType().getStyleId(),
				new int[] { android.R.attr.textColorLink });

		int intColor = typedArray.getColor(0, 0);

		typedArray.recycle();

		return intColor;
	}

	/*
	public Tracker getTracker() {
		
		if (tracker != null) {
			return tracker;
		}
		
		GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);

		tracker = analytics.newTracker(R.xml.app_tracker);

		tracker.enableAdvertisingIdCollection(true);
		
		return tracker;		
	}
	*/

	public void logScreen(Activity activity, String screenName) {

	    /*
		Tracker tracker = getTracker();
		
		tracker.setScreenName(screenName);
		
		tracker.send(new HitBuilders.AppViewBuilder().build());
		*/

		addQueueEvent(activity, new StatLogScreenEvent(getConfigManager(activity).getCommonConfig().getOrGenerateUniqueUserId(), screenName));
	}
	
	public void logEvent(Activity activity, String screenName, String actionName, String label) {

	    /*
		Tracker tracker = getTracker();
		
		tracker.send(new HitBuilders.EventBuilder()
				.setCategory(screenName)
				.setAction(actionName).
				setLabel(label).
				build());
		*/

		addQueueEvent(activity, new StatLogEventEvent(getConfigManager(activity).getCommonConfig().getOrGenerateUniqueUserId(), screenName, actionName, label));
	}

	public synchronized void startQueueThread(Activity activity) {

		if (queueEventThread == null || queueEventThread.isAlive() == false) {

            queueEventThread = new QueueEventThread(activity.getPackageManager(), activity.getPackageName());

            queueEventThread.start();
		}
	}

	public synchronized void stopQueueThread() {

		if (queueEventThread != null && queueEventThread.isAlive() == true) {

            queueEventThread.requestStop();

			try {
                queueEventThread.join(11000);

			} catch (InterruptedException e) {
				// noop
			}
		}
	}

	public synchronized void addQueueEvent(Activity activity, IQueueEvent queueEvent) {

		startQueueThread(activity);

		if (queueEventThread != null) {

			ThemeType themeType = getThemeType(activity);

			queueEventThread.addQueueEvent(activity, themeType, queueEvent);
		}
	}

	public enum ThemeType {

		BLACK(	R.style.JapaneseDictionaryBlackStyle,
				android.R.color.white,
				R.color.title_background_for_black,
				R.color.white,
				Color.DKGRAY,
				R.drawable.delete_for_black,
				R.drawable.user_group_list_for_black,
				R.drawable.listening_for_black),

		WHITE(	R.style.JapaneseDictionaryWhiteStyle,
				android.R.color.black,
				R.color.title_background_for_white,
				R.color.black,
				Color.WHITE,
				R.drawable.delete_for_white,
				R.drawable.user_group_list_for_white,
				R.drawable.listening_for_white);

		private int styleId;

		private int textColorId;

		private int titleItemBackgroundColorId;

		private int kanjiStrokeColorId;

		private int kanjiSearchRadicalInactiveColorId;

		private int deleteIconId;

		private int userGroupListIconId;

		private int listenIconId;

		private ThemeType(int styleId, int textColorId, int titleItemBackgroundColorId, int kanjiStrokeColorId, int kanjiSearchRadicalInactiveColorId, int deleteIconId, int userGroupListIconId, int listenIconId) {
			this.styleId = styleId;
			this.textColorId = textColorId;
			this.titleItemBackgroundColorId = titleItemBackgroundColorId;
			this.kanjiStrokeColorId = kanjiStrokeColorId;
			this.kanjiSearchRadicalInactiveColorId = kanjiSearchRadicalInactiveColorId;
			this.deleteIconId = deleteIconId;
			this.userGroupListIconId = userGroupListIconId;
			this.listenIconId = listenIconId;
		}

		public int getStyleId() {
			return styleId;
		}

		public int getTextColorId() {
			return textColorId;
		}

		public int getTextColorAsColor() {
			return getInstance().getResources().getColor(textColorId);
		}

		public int getTitleItemBackgroundColorAsColor() {
			return getInstance().getResources().getColor(titleItemBackgroundColorId);
		}

		public Drawable getTitleItemBackgroundColorAsDrawable() {
			ColorDrawable colorDrawable = new ColorDrawable(getTitleItemBackgroundColorAsColor());

			return colorDrawable;
		}

		public int getKanjiStrokeColorAsColor() {
			return getInstance().getResources().getColor(kanjiStrokeColorId);
		}

		public int getKanjiSearchRadicalInactiveColorId() {
			return kanjiSearchRadicalInactiveColorId;
		}

		public int getDeleteIconId() {
			return deleteIconId;
		}

		public int getUserGroupListIconId() {
			return userGroupListIconId;
		}

		public Drawable getUserGroupListIconAsDrawable() {
			return getInstance().getResources().getDrawable(userGroupListIconId);
		}

		public int getListenIconId() {
			return listenIconId;
		}
	}
}
