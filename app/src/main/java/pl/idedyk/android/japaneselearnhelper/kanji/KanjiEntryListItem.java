package pl.idedyk.android.japaneselearnhelper.kanji;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import android.text.Spanned;

public class KanjiEntryListItem {
	
	private ItemType itemType;
	
	private KanjiEntry kanjiEntry;
	
	private Spanned text;
	
	private Spanned radicalText;
	
	private String suggestion;

	public KanjiEntryListItem(KanjiEntry kanjiEntry, Spanned text, Spanned radicalText) {
		
		this.itemType = ItemType.KANJI_ENTRY;
		
		this.kanjiEntry = kanjiEntry;
		
		this.text = text;
		this.radicalText = radicalText;
	}
	
	public KanjiEntryListItem(String suggestion, Spanned text) {

		this.itemType = ItemType.SUGGESTION_VALUE;

		this.suggestion = suggestion;		
		this.text = text;
	}

	public KanjiEntryListItem(Spanned text) {

		this.itemType = ItemType.TITLE;

		this.text = text;
	}

	public KanjiEntry getKanjiEntry() {
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
	
	public ItemType getItemType() {
		return itemType;
	}
	
	public static enum ItemType {

		KANJI_ENTRY(R.layout.kanji_entry_simplerow, 0),

		TITLE(R.layout.kanji_entry_simplerow, 0),

		SUGGESTION_VALUE(R.layout.kanji_entry_suggestion_simplerow, 1);
		
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
