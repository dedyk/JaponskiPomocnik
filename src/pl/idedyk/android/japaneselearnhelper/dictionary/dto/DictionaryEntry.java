package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

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

	public List<String> getKanaList() {
		return kanaList;
	}

	public List<String> getTranslates() {
		return translates;
	}

	public String getInfo() {
		return info;
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
