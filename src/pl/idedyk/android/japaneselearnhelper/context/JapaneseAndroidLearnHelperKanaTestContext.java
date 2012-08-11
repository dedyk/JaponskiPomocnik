package pl.idedyk.android.japaneselearnhelper.context;

import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;

public class JapaneseAndroidLearnHelperKanaTestContext {

	private RangeTest rangeTest;
	
	private TestMode1 testMode1;
	
	private TestMode2 testMode2;
	
	private Boolean untilSuccess;
	
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
	
	public RangeTest getRangeTest() {
		return rangeTest;
	}

	public TestMode1 getTestMode1() {
		return testMode1;
	}

	public TestMode2 getTestMode2() {
		return testMode2;
	}

	public void setRangeTest(RangeTest rangeTest) {
		this.rangeTest = rangeTest;
	}

	public void setTestMode1(TestMode1 testMode1) {
		this.testMode1 = testMode1;
	}

	public void setTestMode2(TestMode2 testMode2) {
		this.testMode2 = testMode2;
	}

	public Boolean isUntilSuccess() {
		return untilSuccess;
	}

	public void setUntilSuccess(Boolean untilSuccess) {
		this.untilSuccess = untilSuccess;
	}

	public Boolean getUntilSuccess() {
		return untilSuccess;
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
