package pl.idedyk.android.japaneselearnhelper.context;

public class JapaneseAndroidLearnHelperContext {
	
	private JapaneseAndroidLearnHelperWordTestContext wordTestContext;
	
	private JapaneseAndroidLearnHelperKanaTestContext kanaTestContext;

	public JapaneseAndroidLearnHelperContext() {
		this.wordTestContext = new JapaneseAndroidLearnHelperWordTestContext();
		this.kanaTestContext = new JapaneseAndroidLearnHelperKanaTestContext();
	}

	public JapaneseAndroidLearnHelperWordTestContext getWordTestContext() {
		return wordTestContext;
	}
	
	public JapaneseAndroidLearnHelperKanaTestContext getKanaTestContext() {
		return kanaTestContext;
	}
}
