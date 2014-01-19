package pl.idedyk.android.japaneselearnhelper.dictionary.dto;

import java.util.ArrayList;
import java.util.List;

public enum DictionaryEntryType {

	WORD_GREETING("zwrot grzecznościowy"),

	WORD_NUMBER("liczba"),

	WORD_TIME("godzina"),

	WORD_AGE("wiek"),

	WORD_NOUN("rzeczownik"),

	WORD_TEMPORAL_NOUN("rzeczownik czasowy"),

	WORD_VERB_U("u-czasownik"),

	WORD_VERB_RU("ru-czasownik"),

	WORD_VERB_TE("te-czasownik"),

	WORD_VERB_IRREGULAR("czasownik nieregularny"),

	WORD_VERB_ZURU("czasownik zuru"),

	WORD_VERB_AUX("czasownik pomocniczy"),

	WORD_ADJECTIVE_I("i-przymiotnik"),

	WORD_AUX_ADJECTIVE_I("i-przymiotnik (pomocniczy)"),

	WORD_ADJECTIVE_NA("na-przymiotnik"),

	WORD_ADJECTIVE_TARU("taru-przymiotnik"),

	WORD_EXPRESSION("wyrażenia"),

	WORD_THAT_POINT("wskazywanie punktu"),

	WORD_ASK("słówka pytające"),

	WORD_ADVERB("przysłówek"),

	WORD_ADVERB_TO("przysłówek z partykułą to"),

	WORD_ADVERBIAL_NOUN("rzeczownik przysłówkowy"),

	WORD_PRE_NOUN_ADVERBIAL("pre rzeczownik przysłówkowy"),

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

	WORD_CONJUNCTION("spójnik"),

	WORD_PARTICULE("partykuła"),

	WORD_INTERJECTION("wykrzyknik"),

	WORD_AUX("słówko pomocnicze"),

	WORD_EMPTY("pusty"),

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
		addableDictionaryEntryList.add(WORD_PARTICULE);
		addableDictionaryEntryList.add(WORD_COUNTERS);
		addableDictionaryEntryList.add(WORD_NOUN);
		addableDictionaryEntryList.add(WORD_TEMPORAL_NOUN);
		addableDictionaryEntryList.add(WORD_ADJECTIVE_I);
		addableDictionaryEntryList.add(WORD_AUX_ADJECTIVE_I);
		addableDictionaryEntryList.add(WORD_ADJECTIVE_NA);
		addableDictionaryEntryList.add(WORD_ADJECTIVE_TARU);
		addableDictionaryEntryList.add(WORD_VERB_U);
		addableDictionaryEntryList.add(WORD_VERB_RU);
		addableDictionaryEntryList.add(WORD_VERB_IRREGULAR);
		addableDictionaryEntryList.add(WORD_VERB_ZURU);
		addableDictionaryEntryList.add(WORD_VERB_TE);
		addableDictionaryEntryList.add(WORD_VERB_AUX);
		addableDictionaryEntryList.add(WORD_ADVERB);
		addableDictionaryEntryList.add(WORD_ADVERB_TO);
		addableDictionaryEntryList.add(WORD_ADVERBIAL_NOUN);
		addableDictionaryEntryList.add(WORD_PRE_NOUN_ADVERBIAL);
		addableDictionaryEntryList.add(WORD_PRONOUN);
		addableDictionaryEntryList.add(WORD_CONJUNCTION);
		addableDictionaryEntryList.add(WORD_INTERJECTION);
		addableDictionaryEntryList.add(WORD_AUX);
		addableDictionaryEntryList.add(WORD_EXPRESSION);
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

	public static List<DictionaryEntryType> convertToListDictionaryEntryType(List<String> values) {

		if (values == null) {
			return null;
		}

		List<DictionaryEntryType> dictionaryEntryTypeList = new ArrayList<DictionaryEntryType>();

		for (String currentValue : values) {

			DictionaryEntryType dictionaryEntryType = getDictionaryEntryType(currentValue);

			if (dictionaryEntryType != null) {
				dictionaryEntryTypeList.add(dictionaryEntryType);
			}
		}

		return dictionaryEntryTypeList;
	}

	public static DictionaryEntryType getDictionaryEntryType(String value) {

		if (value == null || value.equals("") == true) {
			return null;
		}

		return DictionaryEntryType.valueOf(value);
	}

	public static List<String> convertToValues(List<DictionaryEntryType> dictionaryEntryTypeList) {

		if (dictionaryEntryTypeList == null) {
			return null;
		}

		List<String> values = new ArrayList<String>();

		for (DictionaryEntryType currentDictionaryEntryTypeList : dictionaryEntryTypeList) {
			values.add(currentDictionaryEntryTypeList.toString());
		}

		return values;
	}
}