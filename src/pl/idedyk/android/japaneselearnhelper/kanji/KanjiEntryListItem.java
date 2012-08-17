package pl.idedyk.android.japaneselearnhelper.kanji;

import android.text.Spanned;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;

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
