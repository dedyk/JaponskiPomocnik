package pl.idedyk.android.japaneselearnhelper.config;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.RangeTest;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.TestMode1;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.TestMode2;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiTestMode;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class ConfigManager {
		
	private SharedPreferences preferences;
	
	public ConfigManager(Activity activity) {
		
        // init config manager
        preferences = activity.getSharedPreferences("config", Context.MODE_PRIVATE);
	}
	
	public KanaTestConfig getKanaTestConfig() {
		return new KanaTestConfig();
	}
	
	public KanjiTestConfig getKanjiTestConfig() {
		return new KanjiTestConfig();
	}

	public SplashConfig getSplashConfig() {
		return new SplashConfig();
	}
	
	public WordDictionarySearchConfig getWordDictionarySearchConfig() {
		return new WordDictionarySearchConfig();
	}
	
	public DictionaryHearConfig getDictionaryHearConfig() {
		return new DictionaryHearConfig();
	}
	
	public WordTestConfig getWordTestConfig() {
		return new WordTestConfig();
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
	
	public class KanjiTestConfig {
		
		private final String kanjiTestConfigPrefix = "kanjiTestConfig_";
		
		private final String kanjiTestModePostfix = "kanjiTestMode";
		
		private final String kanjiTestChosenKanjiPostfix = "chosenKanji";
		
		private final String kanjiTestChosenKanjiGroupPostfix = "chosenKanjiGroup";
		
		private final String kanjiTestUntilSuccessPostfix = "untilSuccess";
		
		private final String kanjiTestDedicateExamplePostfix = "dedicateExample";

		public KanjiTestMode getKanjiTestMode() {
			
			String kanjiTestMode = preferences.getString(kanjiTestConfigPrefix + kanjiTestModePostfix, KanjiTestMode.DRAW_KANJI_FROM_MEANING.toString());
			
			if (kanjiTestMode.equals("DRAW_KANJI_IN_WORD_GROUP") == true) { // zabezpieczenie, przed wczesniejsza wartoscia
				return KanjiTestMode.DRAW_KANJI_IN_WORD;
			}
			
			return KanjiTestMode.valueOf(kanjiTestMode);
		}
		
		public void setKanjiTestMode(KanjiTestMode kanjiTestMode) {
			
			Editor editor = preferences.edit();
			
			editor.putString(kanjiTestConfigPrefix + kanjiTestModePostfix, kanjiTestMode.toString());
			
			editor.commit();
		}
		
		public Set<String> getChosenKanji() {
			
			Set<String> result = new HashSet<String>();
			
			String chosenKanjiString = preferences.getString(kanjiTestConfigPrefix + kanjiTestChosenKanjiPostfix, "");
			
			String[] chosenKanjiSplited = chosenKanjiString.split(",");
			
			for (String currentChosenKanji : chosenKanjiSplited) {
				result.add(currentChosenKanji);
			}
			
			return result;
		}

		public List<String> getChosenKanjiAsList() {
			
			List<String> result = new ArrayList<String>();
			
			String chosenKanjiString = preferences.getString(kanjiTestConfigPrefix + kanjiTestChosenKanjiPostfix, "");
			
			String[] chosenKanjiSplited = chosenKanjiString.split(",");
			
			for (String currentChosenKanji : chosenKanjiSplited) {
				result.add(currentChosenKanji);
			}
			
			return result;
		}
		
		public Set<String> getChosenKanjiGroup() {
			
			Set<String> result = new HashSet<String>();
			
			String chosenKanjiGroupString = preferences.getString(kanjiTestConfigPrefix + kanjiTestChosenKanjiGroupPostfix, "");
			
			String[] chosenKanjiGroupSplited = chosenKanjiGroupString.split(",");
			
			for (String currentChosenKanjiGroup : chosenKanjiGroupSplited) {
				result.add(currentChosenKanjiGroup);
			}
			
			return result;
		}
		
		public void setChosenKanji(List<String> chosenKanjiList) {
			
			StringBuffer chosenKanjiStringBuffer = new StringBuffer();
			
			for (int idx = 0; idx < chosenKanjiList.size(); ++idx) {
				
				if (idx != 0) {
					chosenKanjiStringBuffer.append(",");
				}
				
				chosenKanjiStringBuffer.append(chosenKanjiList.get(idx));
			}
			
			Editor editor = preferences.edit();
			
			editor.putString(kanjiTestConfigPrefix + kanjiTestChosenKanjiPostfix, chosenKanjiStringBuffer.toString());
			
			editor.commit();
		}

		public void setChosenKanjiGroup(List<String> chosenKanjiGroupList) {
			
			StringBuffer chosenKanjiGroupStringBuffer = new StringBuffer();
			
			for (int idx = 0; idx < chosenKanjiGroupList.size(); ++idx) {
				
				if (idx != 0) {
					chosenKanjiGroupStringBuffer.append(",");
				}
				
				chosenKanjiGroupStringBuffer.append(chosenKanjiGroupList.get(idx));
			}
			
			Editor editor = preferences.edit();
			
			editor.putString(kanjiTestConfigPrefix + kanjiTestChosenKanjiGroupPostfix, chosenKanjiGroupStringBuffer.toString());
			
			editor.commit();
		}
		
		public Boolean getUntilSuccess() {
			return preferences.getBoolean(kanjiTestConfigPrefix + kanjiTestUntilSuccessPostfix, true);
		}

		public void setUntilSuccess(boolean untilSuccess) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(kanjiTestConfigPrefix + kanjiTestUntilSuccessPostfix, untilSuccess);
			
			editor.commit();
		}
		
		public Boolean getDedicateExample() {
			return preferences.getBoolean(kanjiTestConfigPrefix + kanjiTestDedicateExamplePostfix, false);
		}

		public void setDedicateExample(boolean untilSuccess) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(kanjiTestConfigPrefix + kanjiTestDedicateExamplePostfix, untilSuccess);
			
			editor.commit();
		}
	}
	
	public class SplashConfig {
		
		private final String splashConfigPrefix = "splashConfig_";
		
		private final String dialogBoxSkipPostfix = "dialogBoxSkip";
		
		public Boolean getDialogBoxSkip() {
			return preferences.getBoolean(splashConfigPrefix + dialogBoxSkipPostfix, false);
		}

		public void setDialogBoxSkip(boolean dialogBoxSkip) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(splashConfigPrefix + dialogBoxSkipPostfix, dialogBoxSkip);
			
			editor.commit();
		}		
	}
	
	public class WordDictionarySearchConfig {
		
		private final String wordDictionarySearchConfigPrefix = "wordDictionarySearchConfig_";
		
		private final String eachChangeSearchPostfix = "eachChangeSearch";
		
		public Boolean getEachChangeSearch() {
			return preferences.getBoolean(wordDictionarySearchConfigPrefix + eachChangeSearchPostfix, false);
		}
		
		public void setEachChangeSearch(boolean eachChangeSearch) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordDictionarySearchConfigPrefix + eachChangeSearchPostfix, eachChangeSearch);
			
			editor.commit();			
		}
	}
	
	public class DictionaryHearConfig {
		
		private final String dictionaryHearConfigPrefix = "dictionaryHearConfig_";
		
		private final String repeatNumberPostfix = "repeatNumber";
		
		private final String delayNumberPostfix = "delayNumber";
		
		private final String wordGroupsPostfix = "wordGroups";
		
		private final String randomPostfix = "random";
		
		public Integer getRepeatNumber() {
			return preferences.getInt(dictionaryHearConfigPrefix + repeatNumberPostfix, 1);
		}
		
		public void setRepeatNumber(int repeatNumber) {
			
			Editor editor = preferences.edit();
			
			editor.putInt(dictionaryHearConfigPrefix + repeatNumberPostfix, repeatNumber);
			
			editor.commit();			
		}	

		public Integer getDelayNumber() {
			return preferences.getInt(dictionaryHearConfigPrefix + delayNumberPostfix, 3);
		}
		
		public void setDelayNumber(int repeatNumber) {
			
			Editor editor = preferences.edit();
			
			editor.putInt(dictionaryHearConfigPrefix + delayNumberPostfix, repeatNumber);
			
			editor.commit();			
		}
		
		public Set<String> getChosenWordGroups() {
			
			Set<String> result = new HashSet<String>();
			
			String chosenWordGroupString = preferences.getString(dictionaryHearConfigPrefix + wordGroupsPostfix, null);
			
			if (chosenWordGroupString == null) {
				return result;
			}
			
			String[] chosenWordGroupSplited = chosenWordGroupString.split(",");
			
			for (String currentChosenWordGroup : chosenWordGroupSplited) {
				result.add(currentChosenWordGroup);
			}
			
			return result;
		}
		
		public void setChosenWordGroups(List<String> chosenWordGroupsNumberList) {
			
			if (chosenWordGroupsNumberList == null) {
				return;
			}
			
			StringBuffer chosenWordGroupsNumberSb = new StringBuffer();
			
			for (int chosenWordGroupsNumberListIdx = 0; chosenWordGroupsNumberListIdx < chosenWordGroupsNumberList.size(); ++chosenWordGroupsNumberListIdx) {
				
				chosenWordGroupsNumberSb.append(chosenWordGroupsNumberList.get(chosenWordGroupsNumberListIdx));
				
				if (chosenWordGroupsNumberListIdx != chosenWordGroupsNumberList.size() - 1) {
					chosenWordGroupsNumberSb.append(",");	
				}
			}
			
			Editor editor = preferences.edit();
			
			editor.putString(dictionaryHearConfigPrefix + wordGroupsPostfix, chosenWordGroupsNumberSb.toString());
			
			editor.commit();
		}
		
		public Boolean getRandom() {
			return preferences.getBoolean(dictionaryHearConfigPrefix + randomPostfix, false);
		}

		public void setRandom(boolean random) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(dictionaryHearConfigPrefix + randomPostfix, random);
			
			editor.commit();
		}
	}

	public class WordTestConfig {
		
		private final String wordTestConfigPrefix = "wordTestConfig_";
		
		private final String repeatNumberPostfix = "repeatNumber";
				
		private final String wordGroupsPostfix = "wordGroups";
		
		private final String randomPostfix = "random";
		
		private final String untilSuccessPostfix = "untilSuccess";
		
		private final String showKanjiPostfix = "showKanji";
		
		private final String showTranslatePostfix = "showTranslate";
		
		public Integer getRepeatNumber() {
			return preferences.getInt(wordTestConfigPrefix + repeatNumberPostfix, 1);
		}
		
		public void setRepeatNumber(int repeatNumber) {
			
			Editor editor = preferences.edit();
			
			editor.putInt(wordTestConfigPrefix + repeatNumberPostfix, repeatNumber);
			
			editor.commit();			
		}	
		
		public Set<String> getChosenWordGroups() {
			
			Set<String> result = new HashSet<String>();
			
			String chosenWordGroupString = preferences.getString(wordTestConfigPrefix + wordGroupsPostfix, null);
			
			if (chosenWordGroupString == null) {
				return result;
			}
			
			String[] chosenWordGroupSplited = chosenWordGroupString.split(",");
			
			for (String currentChosenWordGroup : chosenWordGroupSplited) {
				result.add(currentChosenWordGroup);
			}
			
			return result;
		}
		
		public void setChosenWordGroups(List<String> chosenWordGroupsNumberList) {
			
			if (chosenWordGroupsNumberList == null) {
				return;
			}
			
			StringBuffer chosenWordGroupsNumberSb = new StringBuffer();
			
			for (int chosenWordGroupsNumberListIdx = 0; chosenWordGroupsNumberListIdx < chosenWordGroupsNumberList.size(); ++chosenWordGroupsNumberListIdx) {
				
				chosenWordGroupsNumberSb.append(chosenWordGroupsNumberList.get(chosenWordGroupsNumberListIdx));
				
				if (chosenWordGroupsNumberListIdx != chosenWordGroupsNumberList.size() - 1) {
					chosenWordGroupsNumberSb.append(",");	
				}
			}
			
			Editor editor = preferences.edit();
			
			editor.putString(wordTestConfigPrefix + wordGroupsPostfix, chosenWordGroupsNumberSb.toString());
			
			editor.commit();
		}
		
		public Boolean getRandom() {
			return preferences.getBoolean(wordTestConfigPrefix + randomPostfix, false);
		}

		public void setRandom(boolean random) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestConfigPrefix + randomPostfix, random);
			
			editor.commit();
		}

		public Boolean getUntilSuccess() {
			return preferences.getBoolean(wordTestConfigPrefix + untilSuccessPostfix, false);
		}

		public void setUntilSuccess(boolean random) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestConfigPrefix + untilSuccessPostfix, random);
			
			editor.commit();
		}
		
		public Boolean getShowKanji() {
			return preferences.getBoolean(wordTestConfigPrefix + showKanjiPostfix, true);
		}

		public void setShowKanji(boolean showKanji) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestConfigPrefix + showKanjiPostfix, showKanji);
			
			editor.commit();
		}

		public Boolean getShowTranslate() {
			return preferences.getBoolean(wordTestConfigPrefix + showTranslatePostfix, true);
		}

		public void setShowTranslate(boolean showTranslate) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestConfigPrefix + showTranslatePostfix, showTranslate);
			
			editor.commit();
		}
	}
}
