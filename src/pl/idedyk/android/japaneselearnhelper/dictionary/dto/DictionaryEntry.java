package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.ArrayList;
import java.util.List;

public class DictionaryEntry {
	
	private DictionaryEntryType dictionaryEntryType;
	
	private String prefix;
	
	private String kanji;
	
	private List<String> kanaList;
		
	private List<String> translates;
	
	private String info;

	public DictionaryEntryType getDictionaryEntryType() {
		return dictionaryEntryType;
	}

	public String getKanji() {
		return kanji;
	}

	public List<String> getFullKanaList() {
		List<String> result = new ArrayList<String>();
		
		for (String currentKanaList : kanaList) {
			result.add(prefix + currentKanaList);
		}
		
		return result;
	}

	public List<String> getTranslates() {
		return translates;
	}

	public String getFullInfo() {
		
		StringBuffer result = new StringBuffer();
		
		if (info != null) {
			result.append(info);
		}
		
		if (isAddDictionaryEntryTypeInfo() == true) {
			if (result.length() > 0) {
				result.append(", ");
			}
			
			result.append(dictionaryEntryType.getName());
		}
		
		if (result.length() > 0) {
			return result.toString();	
		} else {
			return null;
		}	
	}
	
	private boolean isAddDictionaryEntryTypeInfo() {
		if (	dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU || 
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_U ||
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR ||
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_TE ||
				dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_I ||
				dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_NA ||
				dictionaryEntryType == DictionaryEntryType.WORD_KANJI_READING) {
			
			return true;
		} else {
			return false;
		}
	}

	public void setDictionaryEntryType(DictionaryEntryType dictionaryEntryType) {
		this.dictionaryEntryType = dictionaryEntryType;
	}

	public void setKanji(String kanji) {
		this.kanji = kanji;
	}

	public void setKanaList(List<String> kanaList) {
		this.kanaList = kanaList;
	}

	public void setTranslates(List<String> translates) {
		this.translates = translates;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getFullKanji() {
		return kanji.equals("-") == false ? prefix + kanji : kanji;
	}
}
