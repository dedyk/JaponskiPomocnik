package pl.idedyk.android.japaneselearnhelper.context;

public class JapaneseAndroidLearnHelperContext {
	
	private JapaneseAndroidLearnHelperWordTestContext wordTestContext;
	
	private JapaneseAndroidLearnHelperKanaTestContext kanaTestContext;

	private JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext;

	public JapaneseAndroidLearnHelperContext() {
		this.wordTestContext = new JapaneseAndroidLearnHelperWordTestContext();
		this.kanaTestContext = new JapaneseAndroidLearnHelperKanaTestContext();
		this.kanjiTestContext = new JapaneseAndroidLearnHelperKanjiTestContext();
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
}
