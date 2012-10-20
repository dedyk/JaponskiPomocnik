package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.ArrayList;
import java.util.List;

public class FuriganaEntry {
	
	private String kanji;
	
	private String kana;
	
	private String currentKanaState;
	
	private List<String> kanjiPart;
	
	private List<String> kanaPart;
	
	public FuriganaEntry(String kanji, String kana) {
		this.kanji = kanji;
		this.kana = kana;
		
		this.currentKanaState = kana;
		
		kanjiPart = new ArrayList<String>();
		kanaPart = new ArrayList<String>();
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

	@Override
	public String toString() {
		return "FuriganaEntry [kanji=" + kanji + ", kana=" + kana
				+ ", currentKanaState=" + currentKanaState + ", kanjiPart="
				+ kanjiPart + ", kanaPart=" + kanaPart + "]";
	}
}
