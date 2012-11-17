package pl.idedyk.android.japaneselearnhelper.context;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;

public class JapaneseAndroidLearnHelperDictionaryHearContext {

	private List<DictionaryEntry> dictionaryEntryList;
	
	private int dictionaryEntryListIdx = 0;
	
	public void reset() {
		dictionaryEntryList = null;
		dictionaryEntryListIdx = 0;
	}

	public List<DictionaryEntry> getDictionaryEntryList() {
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
