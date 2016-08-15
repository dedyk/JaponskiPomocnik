package pl.idedyk.android.japaneselearnhelper.context;

import pl.idedyk.android.japaneselearnhelper.utils.EntryOrderList;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;

public class JapaneseAndroidLearnHelperWordTestContext {
	
	private EntryOrderList<DictionaryEntry> wordsTest;
	
	private int wordTestAnswers = 0;
	private int wordTestCorrectAnswers = 0;
	private int wordTestIncorrentAnswers = 0;
	
	private boolean wordTestOverviewShowAnswer = false;
		
	public void setWordsTest(EntryOrderList<DictionaryEntry> wordsTest) {
		this.wordsTest = wordsTest;
		
		this.wordTestAnswers = 0;
		this.wordTestCorrectAnswers = 0;
		this.wordTestIncorrentAnswers = 0;
		
		this.wordTestOverviewShowAnswer = false;
	}
	
	public EntryOrderList<DictionaryEntry> getWordsTest() {
		return wordsTest;
	}

	public int getWordTestAnswers() {
		return wordTestAnswers;
	}

	public int getWordTestCorrectAnswers() {
		return wordTestCorrectAnswers;
	}

	public int getWordTestIncorrentAnswers() {
		return wordTestIncorrentAnswers;
	}

	public void addWordTestAnswers(int addValue) {
		this.wordTestAnswers += addValue;
	}

	public void addWordTestCorrectAnswers(int addValue) {
		this.wordTestCorrectAnswers += addValue;
	}

	public void addWordTestIncorrentAnswers(int addValue) {
		this.wordTestIncorrentAnswers += addValue;
	}
	
	public boolean getAndSwitchWordTestOverviewShowAnswer() {
		
		boolean result = wordTestOverviewShowAnswer;
		
		wordTestOverviewShowAnswer = !wordTestOverviewShowAnswer;
		
		return result;
	}
}
