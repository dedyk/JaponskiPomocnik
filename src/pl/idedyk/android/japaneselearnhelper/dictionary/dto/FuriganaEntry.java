package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.ArrayList;
import java.util.List;

public class FuriganaEntry {
	
	private String kanji;
	
	private String kana;
	
	private String currentKanaState;
	
	private List<String> kanjiPart;
	
	private List<String> kanaPart;
	
	private FuriganaEntry() {
	}
	
	public FuriganaEntry(String kanji, String kana) {
		this.kanji = kanji;
		this.kana = kana;
		
		this.currentKanaState = kana;
		
		kanjiPart = new ArrayList<String>();
		kanaPart = new ArrayList<String>();
	}
	
	public FuriganaEntry createCopy() {
		
		FuriganaEntry newFuriganaEntry = new FuriganaEntry();
		
		newFuriganaEntry.kanji = kanji;
		newFuriganaEntry.kana = kana;
		newFuriganaEntry.currentKanaState = currentKanaState;
		
		newFuriganaEntry.kanjiPart = new ArrayList<String>();
		
		for (String currentKanjiPart : kanjiPart) {
			newFuriganaEntry.kanjiPart.add(currentKanjiPart);
		}
		
		newFuriganaEntry.kanaPart = new ArrayList<String>();
		
		for (String currentKanaPart : kanaPart) {
			newFuriganaEntry.kanaPart.add(currentKanaPart);
		}
		
		return newFuriganaEntry;
	}

	public String getKanji() {
		return kanji;
	}

	public String getKana() {
		return kana;
	}

	public List<String> getKanjiPart() {
		return kanjiPart;
	}

	public List<String> getKanaPart() {
		return kanaPart;
	}

	public String getCurrentKanaState() {
		return currentKanaState;
	}

	public boolean removeCharsFromCurrentKanaState(int length) {
		
		if (currentKanaState.length() < length) {
			return false;
		}
		
		currentKanaState = currentKanaState.substring(length);
		
		return true;
	}

	public void addHiraganaChar(String hiraganaChar) {
		kanjiPart.add(hiraganaChar);
		kanaPart.add(hiraganaChar);
	}
	
	public void addReading(String kanji, String hiragana) {
		kanjiPart.add(kanji);
		kanaPart.add(hiragana);
	}
	
	public boolean matchKanaWithKanaPart() {
		
		StringBuffer kanaPartJointed = new StringBuffer();
		
		for (String currentKanaPath : kanaPart) {
			kanaPartJointed.append(currentKanaPath);
		}
		
		return kanaPartJointed.toString().equals(kana);
	}

	@Override
	public String toString() {
		return "FuriganaEntry [kanji=" + kanji + ", kana=" + kana
				+ ", currentKanaState=" + currentKanaState + ", kanjiPart="
				+ kanjiPart + ", kanaPart=" + kanaPart + "]";
	}
}
