package pl.idedyk.android.japaneselearnhelper.context;

public class JapaneseAndroidLearnHelperKanaTestContext {

	private RangeTest rangeTest;
	
	private TestMode1 testMode1;
	
	private TestMode2 testMode2;
	
	private Boolean untilSuccess;
	
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
