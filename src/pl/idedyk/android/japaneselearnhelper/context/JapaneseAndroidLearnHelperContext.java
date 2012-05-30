package pl.idedyk.android.japaneselearnhelper.context;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;

public class JapaneseAndroidLearnHelperContext {
	
	private List<DictionaryEntry> wordsTest;
	private int currentWordsTextIdx = 0;
	
	public void setWordsTest(List<DictionaryEntry> wordsTest) {
		this.wordsTest = wordsTest;	
		this.currentWordsTextIdx = 0;
	}

	public List<DictionaryEntry> getWordsTest() {
		return wordsTest;
	}

	public int getCurrentWordsTextIdx() {
		return currentWordsTextIdx;
	}
	
	public void incrementCurrentWordsTextIdx() {
		currentWordsTextIdx++;
	}
}
