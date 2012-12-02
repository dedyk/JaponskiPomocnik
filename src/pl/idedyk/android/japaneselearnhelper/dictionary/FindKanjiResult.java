package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;

public class FindKanjiResult {

	public List<KanjiEntry> result;
	
	public boolean moreElemetsExists = false;

	public List<KanjiEntry> getResult() {
		return result;
	}

	public boolean isMoreElemetsExists() {
		return moreElemetsExists;
	}

	public void setResult(List<KanjiEntry> result) {
		this.result = result;
	}

	public void setMoreElemetsExists(boolean moreElemetsExists) {
		this.moreElemetsExists = moreElemetsExists;
	}
}
