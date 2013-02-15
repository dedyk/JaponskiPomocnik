package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;

public class KeigoHelper {

	private List<KeigoEntry> keigoHighEntryList = new ArrayList<KeigoEntry>();

	private Map<String, KeigoEntry> wordToKeigoEntryHighMap = new HashMap<String, KeigoEntry>();
	private Map<String, KeigoEntry> keigoWordToKeigoEntryHighMap = new HashMap<String, KeigoEntry>();


	KeigoHelper() {

		// iru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, null, "いる", "iru",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, null,  "いらっしゃる", "irassharu", null, "いらっしゃい", "irasshai");

		// iku
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_U, "行く", "いく", "iku",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, null,  "いらっしゃる", "irassharu", null, "いらっしゃい", "irasshai");

		// kuru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_IRREGULAR, "来る", "くる", "kuru",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, null,  "いらっしゃる", "irassharu", null, "いらっしゃい", "irasshai");

		// miru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, "見る", "みる", "miru",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, "ご覧になる", "ごらんになる", "goran ni naru", null, null, null);

		// iu
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_U, "言う", "いう", "iu",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, null, "おっしゃる", "ossharu", null, "おっしゃい", "osshai");

		// suru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_IRREGULAR, null, "する", "suru",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, null, "なさる", "nasaru", null, "なさい", "nasai");

		// taberu
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, "食べる", "たべる", "taberu",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, "召し上がる", "めしあがる", "meshiagaru", null, null, null);

		// nomu
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_U, "飲む", "のむ", "nomu",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, "召し上がる", "めしあがる", "meshiagaru", null, null, null);

		// kureru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, null, "くれる", "kureru",
				KeigoEntryFindMatchType.EXACT, DictionaryEntryType.WORD_VERB_U, "下さる", "くださる", "kudasaru", "下さい", "ください", "kudasai");

		// neru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, "寝る", "ねる", "neru",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, "お休みになる", "おやすみになる", "oyasumi ni naru", null, null, null);
	}

	private void addKeigoHighEntry(DictionaryEntryType dictionaryEntryType, String kanji, String kana, String romaji,
			KeigoEntryFindMatchType findMatchType, DictionaryEntryType keigoDictionaryEntryType, String keigoKanji, String keigoKana, String keigoRomaji,
			String keigoLongFormWithoutMasuKanji, String keigoLongFormWithoutMasuKana, String keigoLongFormWithoutMasuRomaji) {

		KeigoEntry keigoEntry = new KeigoEntry(dictionaryEntryType, kanji, kana, romaji,

				findMatchType, keigoDictionaryEntryType, keigoKanji, keigoKana, keigoRomaji, 

				keigoLongFormWithoutMasuKanji, keigoLongFormWithoutMasuKana, keigoLongFormWithoutMasuRomaji);

		keigoHighEntryList.add(keigoEntry);

		wordToKeigoEntryHighMap.put(getKey(kanji, kana), keigoEntry);
		keigoWordToKeigoEntryHighMap.put(getKey(keigoKanji, keigoKana), keigoEntry);
	}

	public KeigoEntry getKeigoEntryFromWord(String kanji, String kana) {
		return wordToKeigoEntryHighMap.get(getKey(kanji, kana));
	}

	public KeigoEntry getKeigoEntryFromKeigoWord(String keigoKanji, String keigoKana) {
		return keigoWordToKeigoEntryHighMap.get(getKey(keigoKanji, keigoKana));
	}
	
    private String getKey(String kanji, String kana) {
        
        if (kanji == null) {
                kanji = "-";
        }
       
        return kanji + "." + kana;              
    }

	public KeigoEntry findKeigoEntry(DictionaryEntryType dictionaryEntryType, String kanji, List<String> kanaList, List<String> romajiList) {

		for (KeigoEntry keigoEntry : keigoHighEntryList) {

			DictionaryEntryType keigoEntryDictionaryEntryType = keigoEntry.getDictionaryEntryType();

			String keigoEntryKanji = keigoEntry.getKanji();
			String keigoEntryKana = keigoEntry.getKana();
			String keigoEntryRomaji = keigoEntry.getRomaji();

			for (int idx = 0; idx < kanaList.size(); ++idx) {

				if (dictionaryEntryType == keigoEntryDictionaryEntryType) {

					KeigoEntryFindMatchType findMatchType = keigoEntry.getFindMatchType();

					if (findMatchType == KeigoEntryFindMatchType.END_WITH) {

						if (	stringEndWith(kanji, keigoEntryKanji) == true &&
								stringEndWith(kanaList.get(idx), keigoEntryKana) == true &&
								stringEndWith(romajiList.get(idx), keigoEntryRomaji) == true) {

							return keigoEntry;
						}

					} else if (findMatchType == KeigoEntryFindMatchType.EXACT) {

						if (	equals(kanji, keigoEntryKanji) == true &&
								equals(kanaList.get(idx), keigoEntryKana) == true &&
								equals(romajiList.get(idx), keigoEntryRomaji) == true) {

							return keigoEntry;
						}
					} else {
						throw new RuntimeException("Unknown find match type: " + findMatchType);
					}
				}
			}			
		}

		return null;		
	}

	private boolean stringEndWith(String string1, String string2) {

		if (string1 == null && string2 == null) {
			return true;
		} else if (string1 != null && string2 == null) {
			return true; // dla kanji
		} else if (string1 == null && string2 != null) {
			return false;
		} else {
			return string1.endsWith(string2);
		}
	}

	private boolean equals(String string1, String string2) {

		if (string1 == null && string2 == null) {
			return true;
		} else if (string1 != null && string2 == null) {
			return false;
		} else if (string1 == null && string2 != null) {
			return false;
		} else {
			return string1.equals(string2);
		}
	}


	public static class KeigoEntry {

		private DictionaryEntryType dictionaryEntryType;

		private String kanji;
		private String kana;
		private String romaji;

		private DictionaryEntryType keigoDictionaryEntryType;

		private KeigoEntryFindMatchType findMatchType;

		private String keigoKanji;		
		private String keigoKana;		
		private String keigoRomaji;

		private String keigoLongFormWithoutMasuKanji;
		private String keigoLongFormWithoutMasuKana;
		private String keigoLongFormWithoutMasuRomaji;

		public KeigoEntry(DictionaryEntryType dictionaryEntryType, String kanji, String kana, String romaji,

				KeigoEntryFindMatchType findMatchType, DictionaryEntryType keigoDictionaryEntryType, String keigoKanji, String keigoKana, String keigoRomaji,

				String keigoLongFormWithoutMasuKanji, String keigoLongFormWithoutMasuKana, String keigoLongFormWithoutMasuRomaji) {

			this.dictionaryEntryType = dictionaryEntryType;

			this.kanji = kanji;
			this.kana = kana;
			this.romaji = romaji;

			this.findMatchType = findMatchType;

			this.keigoDictionaryEntryType = keigoDictionaryEntryType;

			this.keigoKanji = keigoKanji;
			this.keigoKana = keigoKana;
			this.keigoRomaji = keigoRomaji;

			this.keigoLongFormWithoutMasuKanji = keigoLongFormWithoutMasuKanji;
			this.keigoLongFormWithoutMasuKana = keigoLongFormWithoutMasuKana;
			this.keigoLongFormWithoutMasuRomaji = keigoLongFormWithoutMasuRomaji;
		}

		public DictionaryEntryType getDictionaryEntryType() {
			return dictionaryEntryType;
		}

		public String getKanji() {
			return kanji;
		}
		public String getKana() {
			return kana;
		}
		public String getRomaji() {
			return romaji;
		}

		public KeigoEntryFindMatchType getFindMatchType() {
			return findMatchType;
		}

		public DictionaryEntryType getKeigoDictionaryEntryType() {
			return keigoDictionaryEntryType;
		}

		public String getKeigoKanji() {
			return keigoKanji;
		}
		public String getKeigoKana() {
			return keigoKana;
		}
		public String getKeigoRomaji() {
			return keigoRomaji;
		}

		public String getKeigoLongFormWithoutMasuKanji() {
			return keigoLongFormWithoutMasuKanji;
		}
		public String getKeigoLongFormWithoutMasuKana() {
			return keigoLongFormWithoutMasuKana;
		}
		public String getKeigoLongFormWithoutMasuRomaji() {
			return keigoLongFormWithoutMasuRomaji;
		}		
	}

	public static enum KeigoEntryFindMatchType {

		END_WITH,

		EXACT;		
	}
}
