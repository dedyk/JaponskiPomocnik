package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.ArrayList;
import java.util.List;

public enum DictionaryEntryType {
	
	WORD_GREETING("Zwrot grzecznościowy"),
		
	WORD_NUMBER("Liczba"),
	
	WORD_TIME("Godzina"),
	
	WORD_AGE("Wiek"),
	
	WORD_NOUN("rzeczownik"),
	
	WORD_VERB_U("u-czasownik"),
	
	WORD_VERB_RU("ru-czasownik"),
	
	WORD_VERB_TE("te-czasownik"),
	
	WORD_VERB_IRREGULAR("czasownik nieregularny"),
	
	WORD_ADJECTIVE_I("i-przymiotnik"),
	
	WORD_ADJECTIVE_NA("na-przymiotnik"),
	
	WORD_KANJI_READING("kanji czytanie"),
	
	WORD_EXPRESSION("Wyrażenia"),
	
	WORD_THAT_POINT("Wskazywanie punktu"),
	
	WORD_ASK("Słówka pytające"),
	
	WORD_ADVERB("Przysłówek"),
	
	WORD_DAY_NUMBER("Numer dnia"),
	
	WORD_DAY_WEEK("Dzień tygodnia"),
	
	WORD_MONTH("Miesiąc"),
	
	WORD_LOCATION("Lokalizacja"),
	
	WORD_PEOPLE_NUMBER("Liczenie ludzi"),
	
	WORD_COUNT_DAY_NUMBER("Liczenie dni"),
	
	WORD_COUNTERS("Klasyfikatory"),
	
	WORD_PRONOUN("Zaimki"),
	
	UNKNOWN("unknown");
	
	private String name;
	
	DictionaryEntryType(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	// static
	
	private static List<DictionaryEntryType> addableDictionaryEntryList;
	
	static {
		addableDictionaryEntryList = new ArrayList<DictionaryEntryType>();
		
		addableDictionaryEntryList.add(WORD_GREETING);
		addableDictionaryEntryList.add(WORD_NOUN);
		addableDictionaryEntryList.add(WORD_ADJECTIVE_I);
		addableDictionaryEntryList.add(WORD_ADJECTIVE_NA);
		addableDictionaryEntryList.add(WORD_VERB_U);
		addableDictionaryEntryList.add(WORD_VERB_RU);
		addableDictionaryEntryList.add(WORD_VERB_IRREGULAR);
	}
	
	public static boolean isAddableDictionaryEntryTypeInfo(DictionaryEntryType dictionaryEntryType) {
		return addableDictionaryEntryList.contains(dictionaryEntryType);
	}
	
	
}