package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.Serializable;

public class FindKanjiRequest implements Serializable {
	
	private static final long serialVersionUID = 1L;

	public String word;
		
	public WordPlaceSearch wordPlaceSearch = WordPlaceSearch.ANY_PLACE;
		
	public static enum WordPlaceSearch {
		ANY_PLACE,
		
		START_WITH,
		
		EXACT;
	}
}
