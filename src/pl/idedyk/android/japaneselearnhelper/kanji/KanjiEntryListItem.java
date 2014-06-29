package pl.idedyk.android.japaneselearnhelper.kanji;

import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import android.text.Spanned;

public class KanjiEntryListItem {
	
	private KanjiEntry kanjiEntry;
	
	private Spanned text;
	
	private Spanned radicalText;

	public KanjiEntryListItem(KanjiEntry kanjiEntry, Spanned text, Spanned radicalText) {
		this.kanjiEntry = kanjiEntry;
		
		this.text = text;
		this.radicalText = radicalText;
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
}
