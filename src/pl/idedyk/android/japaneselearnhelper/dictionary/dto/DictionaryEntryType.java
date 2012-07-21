package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

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
}