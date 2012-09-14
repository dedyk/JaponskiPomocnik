package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class DictionaryEntry implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private int id;
		
	private DictionaryEntryType dictionaryEntryType;
	
	private String prefixKana;
	
	private String kanji;
	
	private List<String> kanaList;
	
	private String prefixRomaji;
	
	private List<String> romajiList;
		
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
			result.add(prefixKana + currentKanaList);
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
		
		if (DictionaryEntryType.isAddableDictionaryEntryTypeInfo(dictionaryEntryType) == true) {
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

	public List<String> getRomajiList() {
		return romajiList;
	}

	public void setRomajiList(List<String> romajiList) {
		this.romajiList = romajiList;
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

	public String getPrefixKana() {
		return prefixKana;
	}

	public void setPrefixKana(String prefixKana) {
		this.prefixKana = prefixKana;
	}
	
	public String getFullKanji() {
		
		if (isKanjiExists() == true) {
			return prefixKana + kanji;	
		} else {
			return null;
		}
	}
	
	public boolean isKanjiExists() {
		if (kanji != null && kanji.equals("-") == false) {
			return true;
		} else {
			return false;
		}
	}
		
	public int getId() {
		return id;
	}

	public List<String> getKanaList() {
		return kanaList;
	}

	public String getInfo() {
		return info;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPrefixRomaji() {
		return prefixRomaji;
	}

	public void setPrefixRomaji(String prefixRomaji) {
		this.prefixRomaji = prefixRomaji;
	}
}
