package pl.idedyk.android.japaneselearnhelper.context;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;

public class JapaneseAndroidLearnHelperDictionaryHearContext {

	private List<DictionaryEntry> dictionaryEntryList;

	private int dictionaryEntryListIdx = 0;

	public void reset() {
		dictionaryEntryList = null;
		dictionaryEntryListIdx = 0;
	}

	public List<DictionaryEntry> getDictionaryEntryList() {

		if (dictionaryEntryList == null) {
			return new ArrayList<DictionaryEntry>();
		}

		return dictionaryEntryList;
	}

	public int getDictionaryEntryListIdx() {
		return dictionaryEntryListIdx;
	}

	public void setDictionaryEntryList(List<DictionaryEntry> dictionaryEntryList) {
		this.dictionaryEntryList = dictionaryEntryList;
	}

	public void setDictionaryEntryListIdx(int dictionaryEntryListIdx) {
		this.dictionaryEntryListIdx = dictionaryEntryListIdx;
	}
}
