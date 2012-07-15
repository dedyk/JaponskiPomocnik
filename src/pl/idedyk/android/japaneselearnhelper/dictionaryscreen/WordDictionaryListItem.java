package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import android.text.Spanned;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;

public class WordDictionaryListItem {
	
	private DictionaryEntry dictionaryEntry;
	
	private Spanned text;

	public WordDictionaryListItem(DictionaryEntry dictionaryEntry, Spanned text) {
		this.dictionaryEntry = dictionaryEntry;
		
		this.text = text;
	}

	public DictionaryEntry getDictionaryEntry() {
		return dictionaryEntry;
	}

	public Spanned getText() {
		return text;
	}
}
