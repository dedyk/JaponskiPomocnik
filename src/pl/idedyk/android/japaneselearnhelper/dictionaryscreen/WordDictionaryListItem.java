package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import android.text.Spanned;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult.ResultItem;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;

public class WordDictionaryListItem {
	
	private ItemType itemType;
	
	private ResultItem resultItem;
	
	private String suggestion;
	
	private Spanned text;

	public WordDictionaryListItem(ResultItem resultItem, Spanned text) {
		
		this.itemType = ItemType.RESULT_ITEM;
		
		this.resultItem = resultItem;		
		this.text = text;
	}
	
	public WordDictionaryListItem(String suggestion, Spanned text) {
		
		this.itemType = ItemType.SUGGESTION_VALUE;
		
		this.suggestion = suggestion;		
		this.text = text;
	}

	public WordDictionaryListItem(Spanned text) {
		
		this.itemType = ItemType.TITLE;
		
		this.text = text;
	}
	
	public DictionaryEntry getDictionaryEntry() {
		return resultItem.getDictionaryEntry();
	}

	public Spanned getText() {
		return text;
	}
	
	public String getSuggestion() {
		return suggestion;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public static enum ItemType {
		
		RESULT_ITEM,
		
		TITLE,
		
		SUGGESTION_VALUE;		
	}
}
