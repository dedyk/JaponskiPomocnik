package pl.idedyk.android.japaneselearnhelper.context;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;

public class JapaneseAndroidLearnHelperWordTestContext {
	
	private List<DictionaryEntry> wordsTest;
	private int wordsTestIdx = 0;

	private int wordTestAnswers = 0;
	private int wordTestCorrectAnswers = 0;
	private int wordTestIncorrentAnswers = 0;
	
	private Set<Integer> lastWordsGroupChecked = null;
	
	public Set<Integer> getLastWordsGroupChecked() {
		if (lastWordsGroupChecked == null) {
			lastWordsGroupChecked = new HashSet<Integer>();
		}
		
		return lastWordsGroupChecked;
	}
	
	public void setWordsTest(List<DictionaryEntry> wordsTest) {
		this.wordsTest = wordsTest;	
		this.wordsTestIdx = 0;
		this.wordTestAnswers = 0;
		this.wordTestCorrectAnswers = 0;
		this.wordTestIncorrentAnswers = 0;
	}
	
	public List<DictionaryEntry> getWordsTest() {
		return wordsTest;
	}

	public int getWordsTestIdx() {
		return wordsTestIdx;
	}
	
	public void incrementWordsTestIdx() {
		wordsTestIdx++;
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
}
