package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;

public class FindWordResult {
	
	public List<ResultItem> result;
	
	public boolean moreElemetsExists = false;
	
	public static class ResultItem {
		
		private DictionaryEntry dictionaryEntry;
		
		public ResultItem(DictionaryEntry dictionaryEntry) {
			this.dictionaryEntry = dictionaryEntry;
		}
		
		public DictionaryEntry getDictionaryEntry() {
			if (dictionaryEntry != null) {
				return dictionaryEntry;
			}
			
			throw new RuntimeException("getDictionaryEntry");
		}

		public boolean isKanjiExists() {
			
			if (dictionaryEntry != null) {
				return dictionaryEntry.isKanjiExists();
			}
			
			throw new RuntimeException("isKanjiExists");
		}

		public String getKanji() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getKanji();
			}
			
			throw new RuntimeException("getKanji");
		}

		public String getPrefixKana() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getPrefixKana();
			}
			
			throw new RuntimeException("getPrefixKana");
		}

		public List<String> getKanaList() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getKanaList();
			}
			
			throw new RuntimeException("getKanaList");
		}

		public String getPrefixRomaji() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getPrefixRomaji();
			}
			
			throw new RuntimeException("getPrefixRomaji");
		}

		public List<String> getRomajiList() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getRomajiList();
			}
			
			throw new RuntimeException("getRomajiList");
		}

		public List<String> getTranslates() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getTranslates();
			}
			
			throw new RuntimeException("getTranslates");
		}

		public String getInfo() {
			if (dictionaryEntry != null) {
				return dictionaryEntry.getInfo();
			}
			
			throw new RuntimeException("getInfo");
		}
		
		
		
		
		
		
	}
}
