package pl.idedyk.android.japaneselearnhelper.context;

public class JapaneseAndroidLearnHelperContext {
	
	private JapaneseAndroidLearnHelperWordTestContext wordTestContext;
	
	private JapaneseAndroidLearnHelperKanaTestContext kanaTestContext;

	public JapaneseAndroidLearnHelperContext() {
		this.wordTestContext = new JapaneseAndroidLearnHelperWordTestContext();
	}

	public JapaneseAndroidLearnHelperWordTestContext getWordTestContext() {
		return wordTestContext;
	}

	public JapaneseAndroidLearnHelperKanaTestContext createKanaTestContext() {
		this.kanaTestContext = new JapaneseAndroidLearnHelperKanaTestContext();
		
		return kanaTestContext;
	}
	
	public JapaneseAndroidLearnHelperKanaTestContext getKanaTestContext() {
		return kanaTestContext;
	}
}
