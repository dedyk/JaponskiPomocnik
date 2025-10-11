package pl.idedyk.android.japaneselearnhelper.kanji;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

import android.text.Spanned;

public class KanjiEntryListItem {
	
	private ItemType itemType;
	
	private KanjiCharacterInfo kanjiEntry;
	
	private Spanned text;
	
	private Spanned radicalText;
	
	private String suggestion;

	private String historyValue;

	public static KanjiEntryListItem createKanjiEntryListItemAsKanjiEntry(KanjiCharacterInfo kanjiEntry, Spanned text, Spanned radicalText) {

		KanjiEntryListItem kanjiEntryListItem = new KanjiEntryListItem();

		kanjiEntryListItem.itemType = ItemType.KANJI_ENTRY;

		kanjiEntryListItem.kanjiEntry = kanjiEntry;

		kanjiEntryListItem.text = text;
		kanjiEntryListItem.radicalText = radicalText;

		return kanjiEntryListItem;
	}
	
	public static KanjiEntryListItem createKanjiEntryListItemAsSuggestionValue(String suggestion, Spanned text) {

		KanjiEntryListItem kanjiEntryListItem = new KanjiEntryListItem();

		kanjiEntryListItem.itemType = ItemType.SUGGESTION_VALUE;

		kanjiEntryListItem.suggestion = suggestion;
		kanjiEntryListItem.text = text;

		return kanjiEntryListItem;
	}

	public static KanjiEntryListItem createKanjiEntryListItemAsHistoryValue(String historyValue, Spanned text) {

		KanjiEntryListItem kanjiEntryListItem = new KanjiEntryListItem();

		kanjiEntryListItem.itemType = ItemType.SHOW_HISTORY_VALUE;

		kanjiEntryListItem.historyValue = historyValue;
		kanjiEntryListItem.text = text;

		return kanjiEntryListItem;
	}

	public static KanjiEntryListItem createKanjiEntryListItemAsTitle(Spanned text) {

		KanjiEntryListItem kanjiEntryListItem = new KanjiEntryListItem();

		kanjiEntryListItem.itemType = ItemType.TITLE;

		kanjiEntryListItem.text = text;

		return kanjiEntryListItem;
	}

	private KanjiEntryListItem() {
	}

	public KanjiCharacterInfo getKanjiEntry() {
		return kanjiEntry;
	}

	public Spanned getText() {
		return text;
	}

	public Spanned getRadicalText() {
		return radicalText;
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

		KANJI_ENTRY(R.layout.kanji_entry_simplerow, 0),

		TITLE(R.layout.kanji_entry_simplerow, 0),

		SUGGESTION_VALUE(R.layout.kanji_entry_suggestion_simplerow, 1),

		SHOW_HISTORY_VALUE(R.layout.kanji_entry_show_history_simplerow, 2);
		
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
