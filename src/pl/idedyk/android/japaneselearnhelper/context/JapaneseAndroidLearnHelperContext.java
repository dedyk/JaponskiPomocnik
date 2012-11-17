package pl.idedyk.android.japaneselearnhelper.context;

public class JapaneseAndroidLearnHelperContext {
	
	private JapaneseAndroidLearnHelperWordTestContext wordTestContext;
	
	private JapaneseAndroidLearnHelperKanaTestContext kanaTestContext;

	private JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext;
	
	private JapaneseAndroidLearnHelperDictionaryHearContext dictionaryHearContext;

	public JapaneseAndroidLearnHelperContext() {
		this.wordTestContext = new JapaneseAndroidLearnHelperWordTestContext();
		this.kanaTestContext = new JapaneseAndroidLearnHelperKanaTestContext();
		this.kanjiTestContext = new JapaneseAndroidLearnHelperKanjiTestContext();
		this.dictionaryHearContext = new JapaneseAndroidLearnHelperDictionaryHearContext();
	}

	public JapaneseAndroidLearnHelperWordTestContext getWordTestContext() {
		return wordTestContext;
	}
	
	public JapaneseAndroidLearnHelperKanaTestContext getKanaTestContext() {
		return kanaTestContext;
	}

	public JapaneseAndroidLearnHelperKanjiTestContext getKanjiTestContext() {
		return kanjiTestContext;
	}

	public JapaneseAndroidLearnHelperDictionaryHearContext getDictionaryHearContext() {
		return dictionaryHearContext;
	}
}
