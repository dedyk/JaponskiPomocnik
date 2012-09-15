package pl.idedyk.android.japaneselearnhelper.context;

import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;

public class JapaneseAndroidLearnHelperKanaTestContext {

	// from test
	private List<KanaEntry> allKanaEntries;
	
	private Map<String, List<KanaEntry>> allKanaEntriesGroupBy;
	
	private int allKanaEntriesIdx = 0;
	
	private boolean initialized = false;
	
	private String charTestValue;
	
	private String[][] buttonValues;
	
	private int correctAnswers = 0;
	
	private int incorrectAnswers = 0;
	
	// methods
	
	public void resetTest() {
		allKanaEntries = null;
		allKanaEntriesGroupBy = null;
		allKanaEntriesIdx = 0;
		initialized = false;
		charTestValue = null;
		buttonValues = null;
		correctAnswers = 0;
		incorrectAnswers = 0;
	}
	
	public List<KanaEntry> getAllKanaEntries() {
		return allKanaEntries;
	}

	public Map<String, List<KanaEntry>> getAllKanaEntriesGroupBy() {
		return allKanaEntriesGroupBy;
	}

	public int getAllKanaEntriesIdx() {
		return allKanaEntriesIdx;
	}

	public boolean isInitialized() {
		return initialized;
	}

	public void setAllKanaEntries(List<KanaEntry> allKanaEntries) {
		this.allKanaEntries = allKanaEntries;
	}

	public void setAllKanaEntriesGroupBy(
			Map<String, List<KanaEntry>> allKanaEntriesGroupBy) {
		this.allKanaEntriesGroupBy = allKanaEntriesGroupBy;
	}

	public void setAllKanaEntriesIdx(int allKanaEntriesIdx) {
		this.allKanaEntriesIdx = allKanaEntriesIdx;
	}

	public void setInitialized(boolean initialized) {
		this.initialized = initialized;
	}

	public String getCharTestValue() {
		return charTestValue;
	}

	public void setCharTestValue(String charTestValue) {
		this.charTestValue = charTestValue;
	}

	public String[][] getButtonValues() {
		return buttonValues;
	}

	public void setButtonValues(String[][] buttonValues) {
		this.buttonValues = buttonValues;
	}
	
	public void incrementCorrectAnswers() {
		correctAnswers++;
	}
	
	public void incrementIncorrectAnswers() {
		incorrectAnswers++;
	}

	public int getCorrectAnswers() {
		return correctAnswers;
	}

	public int getIncorrectAnswers() {
		return incorrectAnswers;
	}
	
	public static enum RangeTest {
		HIRAGANA,
		
		KATAKANA,
		
		HIRAGANA_KATAKANA;
	}
	
	public static enum TestMode1 {
		CHOOSE,
		
		INPUT;
	}
	
	public static enum TestMode2 {
		KANA_TO_ROMAJI,
		
		ROMAJI_TO_KANA;
	}
}
