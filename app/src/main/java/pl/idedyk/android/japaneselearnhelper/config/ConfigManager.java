package pl.idedyk.android.japaneselearnhelper.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.UUID;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.RangeTest;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.TestMode1;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.TestMode2;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiTestMode;
import pl.idedyk.android.japaneselearnhelper.test.WordTestMode;
import pl.idedyk.android.japaneselearnhelper.testsm2.WordTestSM2Mode;
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

	public CommonConfig getCommonConfig() {
		return new CommonConfig();
	};
	
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

	public KanjiSearchMeaningConfig getKanjiSearchMeaningConfig() {
		return new KanjiSearchMeaningConfig();
	}
	
	public DictionaryHearConfig getDictionaryHearConfig() {
		return new DictionaryHearConfig();
	}
	
	public WordTestConfig getWordTestConfig() {
		return new WordTestConfig();
	}

	public WordTestSM2Config getWordTestSM2Config() {
		return new WordTestSM2Config();
	}

	public class CommonConfig {

		private final String commonConfigPrefix = "commonConfig_";

		private final String userIdPostfix = "userId";

		private final String messageLastTimestampPostfix = "messageLastTimestamp";

		private final String themeTypePostfix = "themeType";

		public String getOrGenerateUniqueUserId() {

			String userId = preferences.getString(commonConfigPrefix + userIdPostfix, null);

			if (userId == null) { // generujemy losowy identyfikator

				userId = UUID.randomUUID().toString();

				// zapisujemy
				Editor editor = preferences.edit();

				editor.putString(commonConfigPrefix + userIdPostfix, userId);

				editor.commit();
			}

			return userId;
		}

		public String getMessageLastTimestamp() {
			return preferences.getString(commonConfigPrefix + messageLastTimestampPostfix, null);
		}

		public void setMessageLastTimestamp(String messageLastTimestamp) {

			Editor editor = preferences.edit();

			editor.putString(commonConfigPrefix + messageLastTimestampPostfix, messageLastTimestamp);

			editor.commit();
		}

		public JapaneseAndroidLearnHelperApplication.ThemeType getThemeType(JapaneseAndroidLearnHelperApplication.ThemeType defaultThemeType) {

			String themeTypeString = preferences.getString(commonConfigPrefix + themeTypePostfix, defaultThemeType.name());

			if (themeTypeString != null) {
				try {
					return JapaneseAndroidLearnHelperApplication.ThemeType.valueOf(themeTypeString);

				} catch (Exception e) {
					// noop, zostanie uzyta warotsc domyslna
				}
			}

			return defaultThemeType;
		}

		public void setThemeType(JapaneseAndroidLearnHelperApplication.ThemeType themeType) {

			Editor editor = preferences.edit();

			editor.putString(commonConfigPrefix + themeTypePostfix, themeType.name());

			editor.commit();
		}
	}
	
	public class KanaTestConfig {
		
		private final String kanaTestConfigPrefix = "kanaTestConfig_";
		
		private final String rangeTestPostfix = "rangeTest";
		
		private final String testMode1Postfix = "testMode1";
		private final String testMode2Postfix = "testMode2";
		
		private final String untilSuccessPostfix = "untilSuccess";
		private final String untilSuccessNewWordLimitPostfix = "untilSuccessNewWordLimit";
		
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

		public Boolean getUntilSuccessNewWordLimit() {
			return preferences.getBoolean(kanaTestConfigPrefix + untilSuccessNewWordLimitPostfix, false);
		}

		public void setUntilSuccessNewWordLimit(boolean untilSuccessNewWordLimit) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(kanaTestConfigPrefix + untilSuccessNewWordLimitPostfix, untilSuccessNewWordLimit);
			
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

		private final String kanjiTestChosenUserGroupPostfix = "chosenUserGroup";
		
		private final String kanjiTestUntilSuccessPostfix = "untilSuccess";
		
		private final String kanjiTestUntilSuccessNewWordLimitPostfix = "untilSuccessNewWordLimit";
		
		private final String kanjiTestDedicateExamplePostfix = "dedicateExample";
		
		private final String kanjiTestMaxTestSizePostfix = "maxTestSize";
		
		private final String kanjiTestOwnGroupListPostfix = "ownGroupList";

		private final String kanjiTestOwnGroupPostfix = "ownGroup";
		
		private final String kanjiTestChosenOwnGroupPostfix = "chosenOwnGroup";
		
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
		
		public List<String> getChosenKanjiAsList() {
			
			List<String> result = new ArrayList<String>();
			
			String chosenKanjiString = preferences.getString(kanjiTestConfigPrefix + kanjiTestChosenKanjiPostfix, null);

            if (chosenKanjiString == null || chosenKanjiString.trim().equals("") == true) {
                return result;
            }

            String[] chosenKanjiSplited = chosenKanjiString.split(",");
			
			for (String currentChosenKanji : chosenKanjiSplited) {
				result.add(currentChosenKanji);
			}
			
			return result;
		}
		
		public Set<String> getChosenKanjiGroup() {

			Set<String> result = new HashSet<String>();
			
			String chosenKanjiGroupString = preferences.getString(kanjiTestConfigPrefix + kanjiTestChosenKanjiGroupPostfix,  null);

            if (chosenKanjiGroupString == null || chosenKanjiGroupString.trim().equals("") == true) {
                return result;
            }

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

		public Set<Integer> getChosenUserGroups() {

			Set<Integer> result = new HashSet<Integer>();

			String chosenUserGroupString = preferences.getString(kanjiTestConfigPrefix + kanjiTestChosenUserGroupPostfix, null);

			if (chosenUserGroupString == null || chosenUserGroupString.trim().equals("") == true) {
				return result;
			}

			String[] chosenUserGroupSplited = chosenUserGroupString.split(",");

			for (String currentChosenUserGroupId : chosenUserGroupSplited) {
				result.add(new Integer(currentChosenUserGroupId));
			}

			return result;
		}


		public void setChosenUserGroups(List<Integer> chosenUserGroupsNumberList) {

			if (chosenUserGroupsNumberList == null) {
				return;
			}

			StringBuffer chosenUserGroupsNumberSb = new StringBuffer();

			for (int chosenUserGroupsNumberListIdx = 0; chosenUserGroupsNumberListIdx < chosenUserGroupsNumberList.size(); ++chosenUserGroupsNumberListIdx) {

				chosenUserGroupsNumberSb.append(chosenUserGroupsNumberList.get(chosenUserGroupsNumberListIdx));

				if (chosenUserGroupsNumberListIdx != chosenUserGroupsNumberList.size() - 1) {
					chosenUserGroupsNumberSb.append(",");
				}
			}

			Editor editor = preferences.edit();

			editor.putString(kanjiTestConfigPrefix + kanjiTestChosenUserGroupPostfix, chosenUserGroupsNumberSb.toString());

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

		public Boolean getUntilSuccessNewWordLimitPostfix() {
			return preferences.getBoolean(kanjiTestConfigPrefix + kanjiTestUntilSuccessNewWordLimitPostfix, false);
		}

		public void setUntilSuccessNewWordLimitPostfix(boolean untilSuccessNewWordLimit) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(kanjiTestConfigPrefix + kanjiTestUntilSuccessNewWordLimitPostfix, untilSuccessNewWordLimit);
			
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

		public Integer getMaxTestSize() {			
			return preferences.getInt(kanjiTestConfigPrefix + kanjiTestMaxTestSizePostfix, 1000);			
		}
		
		public void setMaxTestSize(int maxTestSize) {
			
			Editor editor = preferences.edit();
			
			editor.putInt(kanjiTestConfigPrefix + kanjiTestMaxTestSizePostfix, maxTestSize);
			
			editor.commit();			
		}
		
		public List<String> getOwnGroupList() {
			
			List<String> result = new ArrayList<String>();
			
			String ownGroupListString = preferences.getString(kanjiTestConfigPrefix + kanjiTestOwnGroupListPostfix, "");
			
			String[] ownGroupListStringSplited = ownGroupListString.split(",");
			
			for (String currentOwnGroupList : ownGroupListStringSplited) {
				
				if (currentOwnGroupList.trim().equals("") == false) {
					result.add(currentOwnGroupList);
				}				
			}
			
			return result;
		}
		
		public void setOwnGroupList(List<String> ownGroupList) {
			
			StringBuffer ownGroupListStringBuffer = new StringBuffer();
			
			for (int idx = 0; idx < ownGroupList.size(); ++idx) {
				
				if (idx != 0) {
					ownGroupListStringBuffer.append(",");
				}
				
				ownGroupListStringBuffer.append(ownGroupList.get(idx));
			}
			
			Editor editor = preferences.edit();
			
			editor.putString(kanjiTestConfigPrefix + kanjiTestOwnGroupListPostfix, ownGroupListStringBuffer.toString());
			
			editor.commit();
		}
		
		public void addOwnGroup(String groupName) {
			
			List<String> ownGroupList = getOwnGroupList();
			
			if (ownGroupList.contains(groupName) == true) {
				throw new RuntimeException("Try to add already exists group");
			}
			
			ownGroupList.add(groupName);
			
			Collections.sort(ownGroupList, new Comparator<String>() {

				@Override
				public int compare(String lhs, String rhs) {
					return lhs.compareToIgnoreCase(rhs);
				}
			});
			
			setOwnGroupList(ownGroupList);
		}

		public void deleteOwnGroup(String ownGroupName) {
			
			List<String> ownGroupList = getOwnGroupList();
			
			if (ownGroupList.contains(ownGroupName) == false) {
				throw new RuntimeException("Try to delete doesn't exist group");
			}
			
			ownGroupList.remove(ownGroupName);
			
			Collections.sort(ownGroupList, new Comparator<String>() {

				@Override
				public int compare(String lhs, String rhs) {
					return lhs.compareToIgnoreCase(rhs);
				}
			});
			
			setOwnGroupList(ownGroupList);
			
			Editor editor = preferences.edit();			
			editor.remove(kanjiTestConfigPrefix + kanjiTestOwnGroupPostfix + "_" + escapeOwnGroupName(ownGroupName));			
			editor.commit();
		}
		
		public boolean isOwnGroupExists(String groupName) {
			
			List<String> ownGroupList = getOwnGroupList();
			
			return ownGroupList.contains(groupName);
		}
		
		public void setOwnGroupKanjiList(String ownGroupName, Set<String> ownGroupKanjiList) {
			
			StringBuffer ownGroupKanjiListStringBuffer = new StringBuffer();
			
			Iterator<String> ownGroupKanjiListIterator = ownGroupKanjiList.iterator();
			
			int counter = 0;
			
			while (ownGroupKanjiListIterator.hasNext() == true) {
				
				if (counter != 0) {
					ownGroupKanjiListStringBuffer.append(",");
				}
											
				ownGroupKanjiListStringBuffer.append(ownGroupKanjiListIterator.next());
								
				counter++;
			}
			
			Editor editor = preferences.edit();
			
			editor.putString(kanjiTestConfigPrefix + kanjiTestOwnGroupPostfix + "_" + escapeOwnGroupName(ownGroupName), ownGroupKanjiListStringBuffer.toString());
			
			editor.commit();
		}
		
		public Set<String> getOwnGroupKanjiList(String ownGroupName) {
			
			Set<String> result = new TreeSet<String>();
			
			String ownGroupKanjiListString = preferences.getString(kanjiTestConfigPrefix + kanjiTestOwnGroupPostfix + "_" + escapeOwnGroupName(ownGroupName), "");
			
			String[] ownGroupKanjiListSplitted = ownGroupKanjiListString.split(",");
			
			for (String currentOwnGroupKanji : ownGroupKanjiListSplitted) {
				
				if (currentOwnGroupKanji.trim().equals("") == false) {
					result.add(currentOwnGroupKanji);
				}				
			}
			
			return result;			
		}

		private String escapeOwnGroupName(String ownGroupName) {
			return ownGroupName.replaceAll(" ", "_");			
		}
		
		public Set<String> getChosenOwnKanjiGroup() {
			
			Set<String> result = new HashSet<String>();
			
			String chosenOwnKanjiGroupString = preferences.getString(kanjiTestConfigPrefix + kanjiTestChosenOwnGroupPostfix, null);

            if (chosenOwnKanjiGroupString == null || chosenOwnKanjiGroupString.trim().equals("") == true) {
                return result;
            }

            String[] chosenOwnKanjiGroupSplited = chosenOwnKanjiGroupString.split(",");
			
			for (String currentChosenKanjiGroup : chosenOwnKanjiGroupSplited) {
				result.add(currentChosenKanjiGroup);
			}
			
			return result;
		}

		public void setChosenOwnKanjiGroup(List<String> chosenOwnKanjiGroupList) {
			
			StringBuffer chosenOwnKanjiGroupStringBuffer = new StringBuffer();
			
			for (int idx = 0; idx < chosenOwnKanjiGroupList.size(); ++idx) {
				
				if (idx != 0) {
					chosenOwnKanjiGroupStringBuffer.append(",");
				}
				
				chosenOwnKanjiGroupStringBuffer.append(chosenOwnKanjiGroupList.get(idx));
			}
			
			Editor editor = preferences.edit();
			
			editor.putString(kanjiTestConfigPrefix + kanjiTestChosenOwnGroupPostfix, chosenOwnKanjiGroupStringBuffer.toString());
			
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
		
		private final String automaticSendMissingWordPostfix = "automaticSendMissingWord";
		
		private final String useAutocompletePostfix = "useAutocomplete";
		
		private final String useSuggestionPostfix = "useSuggestion";
		
		public Boolean getEachChangeSearch() {
			return preferences.getBoolean(wordDictionarySearchConfigPrefix + eachChangeSearchPostfix, false);
		}
		
		public void setEachChangeSearch(boolean eachChangeSearch) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordDictionarySearchConfigPrefix + eachChangeSearchPostfix, eachChangeSearch);
			
			editor.commit();			
		}
		
		public Boolean getAutomaticSendMissingWord() {
			return preferences.getBoolean(wordDictionarySearchConfigPrefix + automaticSendMissingWordPostfix, true);
		}
		
		public void setAutomaticSendMissingWord(boolean automaticSendMissingWord) {

			Editor editor = preferences.edit();
			
			editor.putBoolean(wordDictionarySearchConfigPrefix + automaticSendMissingWordPostfix, automaticSendMissingWord);
			
			editor.commit();			
		}
		
		public Boolean getUseAutocomplete() {
			return preferences.getBoolean(wordDictionarySearchConfigPrefix + useAutocompletePostfix, true);
		}
		
		public void setUseAutocomplete(boolean useAutocomplete) {

			Editor editor = preferences.edit();
			
			editor.putBoolean(wordDictionarySearchConfigPrefix + useAutocompletePostfix, useAutocomplete);
			
			editor.commit();			
		}
		
		public Boolean getUseSuggestion() {
			return preferences.getBoolean(wordDictionarySearchConfigPrefix + useSuggestionPostfix, true);
		}
		
		public void setUseSuggestion(boolean useSuggestion) {

			Editor editor = preferences.edit();
			
			editor.putBoolean(wordDictionarySearchConfigPrefix + useSuggestionPostfix, useSuggestion);
			
			editor.commit();			
		}
	}

	public class KanjiSearchMeaningConfig {
		
		private final String kanjiSearchMeaningConfigPrefix = "kanjiSearchMeaningConfig_";
		
		private final String eachChangeSearchPostfix = "eachChangeSearch";
		
		private final String useAutocompletePostfix = "useAutocomplete";
		
		private final String useSuggestionPostfix = "useSuggestion";
		
		public Boolean getEachChangeSearch() {
			return preferences.getBoolean(kanjiSearchMeaningConfigPrefix + eachChangeSearchPostfix, false);
		}
		
		public void setEachChangeSearch(boolean eachChangeSearch) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(kanjiSearchMeaningConfigPrefix + eachChangeSearchPostfix, eachChangeSearch);
			
			editor.commit();			
		}
		
		public Boolean getUseAutocomplete() {
			return preferences.getBoolean(kanjiSearchMeaningConfigPrefix + useAutocompletePostfix, true);
		}
		
		public void setUseAutocomplete(boolean useAutocomplete) {

			Editor editor = preferences.edit();
			
			editor.putBoolean(kanjiSearchMeaningConfigPrefix + useAutocompletePostfix, useAutocomplete);
			
			editor.commit();			
		}
		
		public Boolean getUseSuggestion() {
			return preferences.getBoolean(kanjiSearchMeaningConfigPrefix + useSuggestionPostfix, true);
		}
		
		public void setUseSuggestion(boolean useSuggestion) {

			Editor editor = preferences.edit();
			
			editor.putBoolean(kanjiSearchMeaningConfigPrefix + useSuggestionPostfix, useSuggestion);
			
			editor.commit();			
		}
	}
	
	public class DictionaryHearConfig {
		
		private final String dictionaryHearConfigPrefix = "dictionaryHearConfig_";
		
		private final String repeatNumberPostfix = "repeatNumber";
		
		private final String delayNumberPostfix = "delayNumber";
		
		private final String wordGroupsPostfix = "wordGroups";

		private final String wordUserGroupsPostfix = "wordUserGroups";

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
			
			if (chosenWordGroupString == null || chosenWordGroupString.trim().equals("") == true) {
				return result;
			}
			
			String[] chosenWordGroupSplited = chosenWordGroupString.split(",");
			
			for (String currentChosenWordGroup : chosenWordGroupSplited) {
				result.add(currentChosenWordGroup);
			}
			
			return result;
		}

		public Set<Integer> getChosenWordUserGroups() {

			Set<Integer> result = new HashSet<Integer>();

			String chosenWordUserGroupString = preferences.getString(dictionaryHearConfigPrefix + wordUserGroupsPostfix, null);

			if (chosenWordUserGroupString == null || chosenWordUserGroupString.trim().equals("") == true) {
				return result;
			}

			String[] chosenWordUserGroupSplited = chosenWordUserGroupString.split(",");

			for (String currentChosenWordUserGroupId : chosenWordUserGroupSplited) {
				result.add(new Integer(currentChosenWordUserGroupId));
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

		public void setChosenWordUserGroups(List<Integer> chosenWordUserGroupsNumberList) {

			if (chosenWordUserGroupsNumberList == null) {
				return;
			}

			StringBuffer chosenWordUserGroupsNumberSb = new StringBuffer();

			for (int chosenWordUserGroupsNumberListIdx = 0; chosenWordUserGroupsNumberListIdx < chosenWordUserGroupsNumberList.size(); ++chosenWordUserGroupsNumberListIdx) {

				chosenWordUserGroupsNumberSb.append(chosenWordUserGroupsNumberList.get(chosenWordUserGroupsNumberListIdx));

				if (chosenWordUserGroupsNumberListIdx != chosenWordUserGroupsNumberList.size() - 1) {
					chosenWordUserGroupsNumberSb.append(",");
				}
			}

			Editor editor = preferences.edit();

			editor.putString(dictionaryHearConfigPrefix + wordUserGroupsPostfix, chosenWordUserGroupsNumberSb.toString());

			editor.commit();
		}

		public Boolean getRandom() {
			return preferences.getBoolean(dictionaryHearConfigPrefix + randomPostfix, true);
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

		private final String wordUserGroupsPostfix = "wordUserGroups";
		
		private final String randomPostfix = "random";
		
		private final String untilSuccessPostfix = "untilSuccess";
		
		private final String untilSuccessNewWordLimitPostfix = "untilSuccessNewWordLimit";
		
		private final String showKanjiPostfix = "showKanji";
		
		private final String showKanaPostfix = "showKana";
		
		private final String showTranslatePostfix = "showTranslate";
		
		private final String showAdditionalInfoPostfix = "showAdditionalInfo";
		
		private final String wordTestModePostfix = "wordTestMode";
		
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
			
			if (chosenWordGroupString == null || chosenWordGroupString.trim().equals("") == true) {
				return result;
			}
			
			String[] chosenWordGroupSplited = chosenWordGroupString.split(",");
			
			for (String currentChosenWordGroup : chosenWordGroupSplited) {
				result.add(currentChosenWordGroup);
			}
			
			return result;
		}

		public Set<Integer> getChosenWordUserGroups() {

			Set<Integer> result = new HashSet<Integer>();

			String chosenWordUserGroupString = preferences.getString(wordTestConfigPrefix + wordUserGroupsPostfix, null);

			if (chosenWordUserGroupString == null || chosenWordUserGroupString.trim().equals("") == true) {
				return result;
			}

			String[] chosenWordUserGroupSplited = chosenWordUserGroupString.split(",");

			for (String currentChosenWordUserGroupId : chosenWordUserGroupSplited) {
				result.add(new Integer(currentChosenWordUserGroupId));
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

		public void setChosenWordUserGroups(List<Integer> chosenWordUserGroupsNumberList) {

			if (chosenWordUserGroupsNumberList == null) {
				return;
			}

			StringBuffer chosenWordUserGroupsNumberSb = new StringBuffer();

			for (int chosenWordUserGroupsNumberListIdx = 0; chosenWordUserGroupsNumberListIdx < chosenWordUserGroupsNumberList.size(); ++chosenWordUserGroupsNumberListIdx) {

				chosenWordUserGroupsNumberSb.append(chosenWordUserGroupsNumberList.get(chosenWordUserGroupsNumberListIdx));

				if (chosenWordUserGroupsNumberListIdx != chosenWordUserGroupsNumberList.size() - 1) {
					chosenWordUserGroupsNumberSb.append(",");
				}
			}

			Editor editor = preferences.edit();

			editor.putString(wordTestConfigPrefix + wordUserGroupsPostfix, chosenWordUserGroupsNumberSb.toString());

			editor.commit();
		}

		public Boolean getRandom() {
			return preferences.getBoolean(wordTestConfigPrefix + randomPostfix, true);
		}

		public void setRandom(boolean random) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestConfigPrefix + randomPostfix, random);
			
			editor.commit();
		}

		public Boolean getUntilSuccess() {
			return preferences.getBoolean(wordTestConfigPrefix + untilSuccessPostfix, true);
		}

		public void setUntilSuccess(boolean untilSuccess) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestConfigPrefix + untilSuccessPostfix, untilSuccess);
			
			editor.commit();
		}

		public Boolean getUntilSuccessNewWordLimit() {
			return preferences.getBoolean(wordTestConfigPrefix + untilSuccessNewWordLimitPostfix, false);
		}

		public void setUntilSuccessNewWordLimit(boolean untilSuccessNewWordLimit) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestConfigPrefix + untilSuccessNewWordLimitPostfix, untilSuccessNewWordLimit);
			
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

		public Boolean getShowKana() {
			return preferences.getBoolean(wordTestConfigPrefix + showKanaPostfix, true);
		}

		public void setShowKana(boolean showKanji) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestConfigPrefix + showKanaPostfix, showKanji);
			
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

		public Boolean getShowAdditionalInfo() {
			return preferences.getBoolean(wordTestConfigPrefix + showAdditionalInfoPostfix, true);
		}

		public void setAdditionalInfoTranslate(boolean showAdditionalInfo) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestConfigPrefix + showAdditionalInfoPostfix, showAdditionalInfo);
			
			editor.commit();
		}
		
		public WordTestMode getWordTestMode() {
			
			String wordTestMode = preferences.getString(wordTestConfigPrefix + wordTestModePostfix, WordTestMode.INPUT.toString());
						
			return WordTestMode.valueOf(wordTestMode);
		}
		
		public void setWordTestMode(WordTestMode wordTestMode) {
			
			Editor editor = preferences.edit();
			
			editor.putString(wordTestConfigPrefix + wordTestModePostfix, wordTestMode.toString());
			
			editor.commit();
		}
	}
	
	public class WordTestSM2Config {
		
		private final String wordTestSM2ConfigPrefix = "wordTestSM2Config_";
		
		private final String maxNewWordsPostfix = "maxNewWords";
				
		private final String showKanjiPostfix = "showKanji";
		
		private final String showKanaPostfix = "showKana";
		
		private final String showTranslatePostfix = "showTranslate";
		
		private final String showAdditionalInfoPostfix = "showAdditionalInfo";
		
		private final String wordTestSM2ModePostfix = "wordTestSM2Mode";
		
		public Integer getMaxNewWords() {
			return preferences.getInt(wordTestSM2ConfigPrefix + maxNewWordsPostfix, 20);
		}
		
		public void setMaxNewWords(int maxNewWords) {
			
			Editor editor = preferences.edit();
			
			editor.putInt(wordTestSM2ConfigPrefix + maxNewWordsPostfix, maxNewWords);
			
			editor.commit();			
		}	
		
		public Boolean getShowKanji() {
			return preferences.getBoolean(wordTestSM2ConfigPrefix + showKanjiPostfix, true);
		}

		public void setShowKanji(boolean showKanji) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestSM2ConfigPrefix + showKanjiPostfix, showKanji);
			
			editor.commit();
		}

		public Boolean getShowKana() {
			return preferences.getBoolean(wordTestSM2ConfigPrefix + showKanaPostfix, true);
		}

		public void setShowKana(boolean showKanji) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestSM2ConfigPrefix + showKanaPostfix, showKanji);
			
			editor.commit();
		}
		
		public Boolean getShowTranslate() {
			return preferences.getBoolean(wordTestSM2ConfigPrefix + showTranslatePostfix, true);
		}

		public void setShowTranslate(boolean showTranslate) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestSM2ConfigPrefix + showTranslatePostfix, showTranslate);
			
			editor.commit();
		}

		public Boolean getShowAdditionalInfo() {
			return preferences.getBoolean(wordTestSM2ConfigPrefix + showAdditionalInfoPostfix, true);
		}

		public void setAdditionalInfoTranslate(boolean showAdditionalInfo) {
			
			Editor editor = preferences.edit();
			
			editor.putBoolean(wordTestSM2ConfigPrefix + showAdditionalInfoPostfix, showAdditionalInfo);
			
			editor.commit();
		}
		
		public WordTestSM2Mode getWordTestSM2Mode() {
			
			String wordTestSM2Mode = preferences.getString(wordTestSM2ConfigPrefix + wordTestSM2ModePostfix, WordTestSM2Mode.CHOOSE.toString());
						
			return WordTestSM2Mode.valueOf(wordTestSM2Mode);
		}
		
		public void setWordTestSM2Mode(WordTestSM2Mode wordTestSM2Mode) {
			
			Editor editor = preferences.edit();
			
			editor.putString(wordTestSM2ConfigPrefix + wordTestSM2ModePostfix, wordTestSM2Mode.toString());
			
			editor.commit();
		}
	}
}
