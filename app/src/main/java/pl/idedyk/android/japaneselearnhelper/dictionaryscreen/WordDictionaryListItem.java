package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import android.text.Spanned;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult.ResultItem;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;

public class WordDictionaryListItem {
	
	private ItemType itemType;
	
	private ResultItem resultItem;
	
	private String suggestion;

	private String historyValue;

	private Spanned text;

	public static WordDictionaryListItem createWordDictionaryListItemAsResultItem(ResultItem resultItem, Spanned text) {

		WordDictionaryListItem wordDictionaryListItem = new WordDictionaryListItem();

		wordDictionaryListItem.itemType = ItemType.RESULT_ITEM;

		wordDictionaryListItem.resultItem = resultItem;
		wordDictionaryListItem.text = text;

		return wordDictionaryListItem;
	}
	
	public static WordDictionaryListItem createWordDictionaryListItemAsSuggestionValue(String suggestion, Spanned text) {

		WordDictionaryListItem wordDictionaryListItem = new WordDictionaryListItem();

		wordDictionaryListItem.itemType = ItemType.SUGGESTION_VALUE;

		wordDictionaryListItem.suggestion = suggestion;
		wordDictionaryListItem.text = text;

		return wordDictionaryListItem;
	}

	public static WordDictionaryListItem createWordDictionaryListItemAsHistoryValue(String historyValue, Spanned text) {

		WordDictionaryListItem wordDictionaryListItem = new WordDictionaryListItem();

		wordDictionaryListItem.itemType = ItemType.SHOW_HISTORY_VALUE;

		wordDictionaryListItem.historyValue = historyValue;
		wordDictionaryListItem.text = text;

		return wordDictionaryListItem;
	}

	public static WordDictionaryListItem createWordDictionaryListItemAsTitle(Spanned text) {

		WordDictionaryListItem wordDictionaryListItem = new WordDictionaryListItem();

		wordDictionaryListItem.itemType = ItemType.TITLE;

		wordDictionaryListItem.text = text;

		return wordDictionaryListItem;
	}

	private WordDictionaryListItem() {
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

	public String getHistoryValue() {
		return historyValue;
	}

	public ItemType getItemType() {
		return itemType;
	}

	public static enum ItemType {
		
		RESULT_ITEM(R.layout.word_dictionary_simplerow, 0),
		
		TITLE(R.layout.word_dictionary_simplerow, 0),
		
		SUGGESTION_VALUE(R.layout.word_dictionary_suggestion_simplerow, 1),

		SHOW_HISTORY_VALUE(R.layout.word_dictionary_show_history_simplerow, 2);
		
		private int layoutResourceId;
		
		private int viewTypeId;
		
		// ten sam layoutResourceId musi sie rownac tego samemu viewTypeId !!!
		ItemType(int layoutResourceId, int viewTypeId) {
			this.layoutResourceId = layoutResourceId;
			this.viewTypeId = viewTypeId;
		}

		public int getViewTypeId() {
			return viewTypeId;
		}

		public static int getLayoutResourceId(int itemViewTypeId) {
			
			ItemType[] values = values();
			
			for (ItemType itemType : values) {
				
				if (itemType.getViewTypeId() == itemViewTypeId) {
					return itemType.layoutResourceId;
				}
			}			
			
			throw new RuntimeException("Unknown itemViewTypeId: " + itemViewTypeId);
		}
	}
}
