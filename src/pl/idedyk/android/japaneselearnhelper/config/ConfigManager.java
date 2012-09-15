package pl.idedyk.android.japaneselearnhelper.config;

import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.RangeTest;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.TestMode1;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.TestMode2;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConfigManager {

	private static ConfigManager instance;
	
	public static ConfigManager getInstance() {
		
		if (instance == null) {
			throw new RuntimeException("No config manager");
		}
		
		return instance;
	}
	
	private SharedPreferences preferences;
	
	public ConfigManager(SharedPreferences preferences) {
		instance = this;
		
		this.preferences = preferences;
	}
	
	public KanaTestConfig getKanaTestConfig() {
		return new KanaTestConfig();
	}
	
	public class KanaTestConfig {
		
		private final String kanaTestConfigPrefix = "kanaTestConfig_";
		
		private final String rangeTestPostfix = "rangeTest";
		
		private final String testMode1Postfix = "testMode1";
		private final String testMode2Postfix = "testMode2";
		
		private final String untilSuccessPostfix = "untilSuccess";
		
		private final String gojuuonPostfix = "gojuuon";
		private final String dakutenHandakutenPostfix = "dakutenHandakuten";
		private final String youonPostfix = "youon";
				
		public RangeTest getRangeTest() {
			
			String rangeTest = preferences.getString(kanaTestConfigPrefix + rangeTestPostfix, RangeTest.HIRAGANA_KATAKANA.toString());
			
			return RangeTest.valueOf(rangeTest);
		}

		public TestMode1 getTestMode1() {
			
			String testMode1 = preferences.getString(kanaTestConfigPrefix + testMode1Postfix, TestMode1.CHOOSE.toString());
			
			return TestMode1.valueOf(testMode1);
		}

		public TestMode2 getTestMode2() {
			
			String testMode2 = preferences.getString(kanaTestConfigPrefix + testMode2Postfix, TestMode2.KANA_TO_ROMAJI.toString());
			
			return TestMode2.valueOf(testMode2);
		}

		public void setRangeTest(RangeTest rangeTest) {
			
			Editor editor = preferences.edit();
			
			editor.putString(kanaTestConfigPrefix + rangeTestPostfix, rangeTest.toString());
			
			editor.commit();
		}

		public void setTestMode1(TestMode1 testMode1) {
			
			Editor editor = preferences.edit();
			
			editor.putString(kanaTestConfigPrefix + testMode1Postfix, testMode1.toString());
			
			editor.commit();
		}

		public void setTestMode2(TestMode2 testMode2) {
			
			Editor editor = preferences.edit();
			
			editor.putString(kanaTestConfigPrefix + testMode2Postfix, testMode2.toString());
			
			editor.commit();
		}

		public Boolean getUntilSuccess() {
			return preferences.getBoolean(kanaTestConfigPrefix + untilSuccessPostfix, true);
		}

		public void setUntilSuccess(boolean untilSuccess) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(kanaTestConfigPrefix + untilSuccessPostfix, untilSuccess);
			
			editor.commit();
		}

		public Boolean getGojuuon() {
			return preferences.getBoolean(kanaTestConfigPrefix + gojuuonPostfix, true);
		}

		public Boolean getDakutenHandakuten() {
			return preferences.getBoolean(kanaTestConfigPrefix + dakutenHandakutenPostfix, true);
		}

		public Boolean getYouon() {
			return preferences.getBoolean(kanaTestConfigPrefix + youonPostfix, true);
		}

		public void setGojuuon(Boolean gojuuon) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(kanaTestConfigPrefix + gojuuonPostfix, gojuuon);
			
			editor.commit();
		}

		public void setDakutenHandakuten(boolean dakutenHandakuten) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(kanaTestConfigPrefix + dakutenHandakutenPostfix, dakutenHandakuten);
			
			editor.commit();
		}

		public void setYouon(boolean youon) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(kanaTestConfigPrefix + youonPostfix, youon);
			
			editor.commit();
		}		
	}
}
