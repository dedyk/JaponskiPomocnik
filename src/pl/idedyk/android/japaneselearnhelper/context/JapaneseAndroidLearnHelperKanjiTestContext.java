package pl.idedyk.android.japaneselearnhelper.context;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;

public class JapaneseAndroidLearnHelperKanjiTestContext {

	private List<KanjiEntry> kanjiEntryList;
	
	private List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji;
	
	// methods
	
	public void resetTest() {
		kanjiEntryList = null;
		dictionaryEntryWithRemovedKanji = null;
		
	}

	public List<KanjiEntry> getKanjiEntryList() {
		return kanjiEntryList;
	}

	public void setKanjiEntryList(List<KanjiEntry> kanjiEntryList) {
		this.kanjiEntryList = kanjiEntryList;
	}
	
	public List<DictionaryEntryWithRemovedKanji> getDictionaryEntryWithRemovedKanji() {
		return dictionaryEntryWithRemovedKanji;
	}

	public void setDictionaryEntryWithRemovedKanji(List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji) {
		this.dictionaryEntryWithRemovedKanji = dictionaryEntryWithRemovedKanji;
	}

	public static class DictionaryEntryWithRemovedKanji {
		
		private DictionaryEntry dictionaryEntry;
		
		private String removedKanji;

		public DictionaryEntryWithRemovedKanji(DictionaryEntry dictionaryEntry, String removedKanji) {
			this.dictionaryEntry = dictionaryEntry;
			this.removedKanji = removedKanji;
		}

		public DictionaryEntry getDictionaryEntry() {
			return dictionaryEntry;
		}

		public String getRemovedKanji() {
			return removedKanji;
		}
		
		public String getKanjiWithRemovedKanji() {
			return dictionaryEntry.getKanji().replaceAll(removedKanji, "_");
		}
	}
}
