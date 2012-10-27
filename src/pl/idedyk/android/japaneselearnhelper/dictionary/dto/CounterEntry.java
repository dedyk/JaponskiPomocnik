package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.ArrayList;
import java.util.List;

public class CounterEntry {

	private String kanji;
	
	private String kana;
	
	private String romaji;
	
	private List<Entry> entries;
	
	private String description;
	
	private List<String> exampleUse;
	
	public CounterEntry(String kanji, String kana, String romaji, String description) {
		this.kanji = kanji;
		this.kana = kana;
		this.romaji = romaji;
		
		entries = new ArrayList<CounterEntry.Entry>();
		
		this.description = description;
		
		exampleUse = new ArrayList<String>();
	}
	
	public String getKanji() {
		return kanji;
	}

	public String getKana() {
		return kana;
	}

	public String getRomaji() {
		return romaji;
	}

	public List<Entry> getEntries() {
		return entries;
	}

	public String getDescription() {
		return description;
	}

	public List<String> getExampleUse() {
		return exampleUse;
	}

	public static class Entry {
		
		private String key;
		
		private String kanji;
		
		private String kana;
		
		private String romaji;

		public Entry(String key, String kanji, String kana, String romaji) {
			this.key = key;
			this.kanji = kanji;
			this.kana = kana;
			this.romaji = romaji;
		}

		public String getKey() {
			return key;
		}

		public String getKanji() {
			return kanji;
		}

		public String getKana() {
			return kana;
		}

		public String getRomaji() {
			return romaji;
		}
	}
}
