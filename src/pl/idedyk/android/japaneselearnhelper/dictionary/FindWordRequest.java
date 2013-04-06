package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.Serializable;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;

public class FindWordRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public String word;
	
	public boolean searchKanji = true;
	
	public boolean searchKana = true;
	
	public boolean searchRomaji = true;
	
	public boolean searchTranslate = true;
	
	public boolean searchInfo = true;
	
	public boolean searchGrammaFormAndExamples = false;
	
	public WordPlaceSearch wordPlaceSearch = WordPlaceSearch.ANY_PLACE;
	
	public List<DictionaryEntryType> dictionaryEntryList = null;
	
	public static enum WordPlaceSearch {
		ANY_PLACE,
		
		START_WITH,
		
		EXACT;
	}
}
