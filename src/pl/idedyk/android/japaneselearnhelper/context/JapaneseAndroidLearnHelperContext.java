package pl.idedyk.android.japaneselearnhelper.context;

public class JapaneseAndroidLearnHelperContext {
	
	private JapaneseAndroidLearnHelperWordTestContext wordTestContext;

	public JapaneseAndroidLearnHelperContext() {
		this.wordTestContext = new JapaneseAndroidLearnHelperWordTestContext();
	}

	public JapaneseAndroidLearnHelperWordTestContext getWordTestContext() {
		return wordTestContext;
	}
}
