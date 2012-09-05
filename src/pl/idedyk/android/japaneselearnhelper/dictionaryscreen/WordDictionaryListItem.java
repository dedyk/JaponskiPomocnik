package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import android.text.Spanned;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordResult.ResultItem;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;

public class WordDictionaryListItem {
	
	private ResultItem resultItem;
	
	private Spanned text;

	public WordDictionaryListItem(ResultItem resultItem, Spanned text) {
		this.resultItem = resultItem;
		
		this.text = text;
	}

	public DictionaryEntry getDictionaryEntry() {
		return resultItem.getDictionaryEntry();
	}

	public Spanned getText() {
		return text;
	}
}
