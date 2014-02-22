package pl.idedyk.android.japaneselearnhelper.kanji;

import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import android.text.Spanned;

public class KanjiEntryListItem {
	
	private KanjiEntry kanjiEntry;
	
	private Spanned text;

	public KanjiEntryListItem(KanjiEntry kanjiEntry, Spanned text) {
		this.kanjiEntry = kanjiEntry;
		
		this.text = text;
	}

	public KanjiEntry getKanjiEntry() {
		return kanjiEntry;
	}

	public Spanned getText() {
		return text;
	}
}
