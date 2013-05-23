package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.ArrayList;
import java.util.List;

public enum DictionaryEntryType {
	
	WORD_GREETING("zwrot grzecznościowy"),
		
	WORD_NUMBER("liczba"),
	
	WORD_TIME("godzina"),
	
	WORD_AGE("wiek"),
	
	WORD_NOUN("rzeczownik"),
	
	WORD_VERB_U("u-czasownik"),
	
	WORD_VERB_RU("ru-czasownik"),
	
	WORD_VERB_TE("te-czasownik"),
	
	WORD_VERB_IRREGULAR("czasownik nieregularny"),
	
	WORD_ADJECTIVE_I("i-przymiotnik"),
	
	WORD_ADJECTIVE_NA("na-przymiotnik"),
	
	WORD_KANJI_READING("kanji czytanie"),
	
	WORD_EXPRESSION("wyrażenia"),
	
	WORD_THAT_POINT("wskazywanie punktu"),
	
	WORD_ASK("słówka pytające"),
	
	WORD_ADVERB("przysłówek"),
	
	WORD_DAY_NUMBER("numer dnia"),
	
	WORD_DAY_WEEK("dzień tygodnia"),
	
	WORD_MONTH("miesiąc"),
	
	WORD_LOCATION("lokalizacja"),
	
	WORD_PEOPLE_NUMBER("liczenie ludzi"),
	
	WORD_COUNT_DAY_NUMBER("liczenie dni"),
	
	WORD_COUNTERS("klasyfikatory"),
	
	WORD_PRONOUN("zaimki"),
	
	WORD_NAME("imię"),
	
	WORD_MALE_NAME("imię męskie"),
	
	WORD_FEMALE_NAME("imię żeńskie"),
	
	WORD_SURNAME_NAME("nazwisko"),
	
	UNKNOWN("nieznany");
	
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
		addableDictionaryEntryList.add(WORD_ADVERB);
		addableDictionaryEntryList.add(WORD_NAME);
		addableDictionaryEntryList.add(WORD_MALE_NAME);
		addableDictionaryEntryList.add(WORD_FEMALE_NAME);
		addableDictionaryEntryList.add(WORD_SURNAME_NAME);		
	}
	
	public static boolean isAddableDictionaryEntryTypeInfo(DictionaryEntryType dictionaryEntryType) {
		return addableDictionaryEntryList.contains(dictionaryEntryType);
	}

	public static List<DictionaryEntryType> getAddableDictionaryEntryList() {
		return addableDictionaryEntryList;
	}
	
	public static List<DictionaryEntryType> getOtherDictionaryEntryList() {
		
		List<DictionaryEntryType> otherDictionaryEntryList = new ArrayList<DictionaryEntryType>();
		
		DictionaryEntryType[] allValues = values();
		
		for (DictionaryEntryType currentValues : allValues) {
			
			if (addableDictionaryEntryList.contains(currentValues) == false) {
				otherDictionaryEntryList.add(currentValues);
			}
		}
		
		return otherDictionaryEntryList;
	}
}