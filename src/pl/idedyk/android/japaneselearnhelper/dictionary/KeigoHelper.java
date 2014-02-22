package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntryType;

public class KeigoHelper {

	// high
	private final List<KeigoEntry> keigoHighEntryList = new ArrayList<KeigoEntry>();

	private final Map<String, KeigoEntry> wordToKeigoEntryHighMap = new HashMap<String, KeigoEntry>();
	private final Map<String, KeigoEntry> keigoWordToKeigoEntryHighMap = new HashMap<String, KeigoEntry>();

	// low
	private final List<KeigoEntry> keigoLowEntryList = new ArrayList<KeigoEntry>();

	private final Map<String, KeigoEntry> wordToKeigoEntryLowMap = new HashMap<String, KeigoEntry>();
	private final Map<String, KeigoEntry> keigoWordToKeigoEntryLowMap = new HashMap<String, KeigoEntry>();

	public KeigoHelper() {

		// *** high ***

		// iru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, null, "いる", "iru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, null, null, "いらっしゃる", null, "irassharu", null, null, "いらっしゃい",
				"irasshai");

		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, "居る", "いる", "iru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, null, null, "いらっしゃる", null, "irassharu", null, null, "いらっしゃい",
				"irasshai");

		// iku
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_U, "行く", "いく", "iku", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, null, null, "いらっしゃる", null, "irassharu", null, null, "いらっしゃい",
				"irasshai");

		// kuru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_IRREGULAR, "来る", "くる", "kuru",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, null, null, "いらっしゃる", null,
				"irassharu", null, null, "いらっしゃい", "irasshai");

		// miru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, "見る", "みる", "miru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "ご覧", "になる", "ごらん", "になる", "goran", "ni naru", null, null, null);

		// iu
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_U, "言う", "いう", "iu", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, null, null, "おっしゃる", null, "ossharu", null, null, "おっしゃい", "osshai");

		// suru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_IRREGULAR, null, "する", "suru",
				KeigoEntryFindMatchType.END_WITH, DictionaryEntryType.WORD_VERB_U, "為さる", null, "なさる", null, "nasaru",
				null, "為さい", "なさい", "nasai");

		// taberu
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, "食べる", "たべる", "taberu", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "召し上がる", null, "めしあがる", null, "meshiagaru", null, null, null, null);

		// nomu
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_U, "飲む", "のむ", "nomu", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "召し上がる", null, "めしあがる", null, "meshiagaru", null, null, null, null);

		// kureru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, null, "くれる", "kureru", KeigoEntryFindMatchType.EXACT,
				DictionaryEntryType.WORD_VERB_U, "下さる", null, "くださる", null, "kudasaru", null, "下さい", "ください", "kudasai");

		// neru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, "寝る", "ねる", "neru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "お休み", "になる", "おやすみ", "になる", "oyasumi", "ni naru", null, null, null);

		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, null, "ている", "te iru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, null, null, "ていらっしゃる", null, "te irassharu", null, null, "ていらっしゃい",
				"te irasshai");

		// *** low ***

		// iru
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_RU, null, "いる", "iru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, null, null, "おる", null, "oru", null, null, null, null);

		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_RU, "居る", "いる", "iru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, null, null, "おる", null, "oru", null, null, null, null);

		// iku
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_U, "行く", "いく", "iku", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "参る", null, "まいる", null, "mairu", null, null, null, null);

		// kuru
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_IRREGULAR, "来る", "くる", "kuru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "参る", null, "まいる", null, "mairu", null, null, null, null);

		// iu
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_U, "言う", "いう", "iu", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "申す", null, "もうす", null, "mousu", null, null, null, null);

		// suru
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_IRREGULAR, null, "する", "suru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "致す", null, "いたす", null, "itasu", null, null, null, null);

		// taberu
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_RU, "食べる", "たべる", "taberu", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "頂く", null, "いただく", null, "itadaku", null, null, null, null);

		// nomu
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_U, "飲む", "のむ", "nomu", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "頂く", null, "いただく", null, "itadaku", null, null, null, null);

		// aru
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_U, null, "ある", "aru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, null, null, "ござる", null, "gozaru", null, null, "ござい", "gozai");

		// morau
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_U, "貰う", "もらう", "morau", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, "頂く", null, "いただく", null, "itadaku", null, null, null, null);

		// ageru
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_RU, null, "あげる", "ageru", KeigoEntryFindMatchType.EXACT,
				DictionaryEntryType.WORD_VERB_RU, "差し上げる", null, "さしあげる", null, "sashiageru", null, null, null, null);

		// desu - special
		addKeigoLowEntry(DictionaryEntryType.UNKNOWN, null, "です", "desu", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, null, null, "でござる", null, "de gozaru", null, null, "でござい", "de gozai");

		// te iru - special
		addKeigoLowEntry(DictionaryEntryType.WORD_VERB_RU, null, "ている", "te iru", KeigoEntryFindMatchType.END_WITH,
				DictionaryEntryType.WORD_VERB_U, null, null, "ておる", null, "te oru", null, null, null, null);

	}

	// *** high ***
	private void addKeigoHighEntry(DictionaryEntryType dictionaryEntryType, String kanji, String kana, String romaji,

	KeigoEntryFindMatchType findMatchType, DictionaryEntryType keigoDictionaryEntryType, String keigoKanji,
			String keigoKanjiPostfix, String keigoKana, String keigoKanaPostfix, String keigoRomaji,
			String keigoRomajiPostfix,

			String keigoLongFormWithoutMasuKanji, String keigoLongFormWithoutMasuKana,
			String keigoLongFormWithoutMasuRomaji) {

		//////

		KeigoEntry keigoEntry = new KeigoEntry(dictionaryEntryType, kanji, kana, romaji,

		findMatchType, keigoDictionaryEntryType, keigoKanji, keigoKanjiPostfix, keigoKana, keigoKanaPostfix,
				keigoRomaji, keigoRomajiPostfix,

				keigoLongFormWithoutMasuKanji, keigoLongFormWithoutMasuKana, keigoLongFormWithoutMasuRomaji);

		keigoHighEntryList.add(keigoEntry);

		wordToKeigoEntryHighMap.put(getKey(kanji, null, kana, null), keigoEntry);
		keigoWordToKeigoEntryHighMap.put(getKey(keigoKanji, keigoKanaPostfix, keigoKana, keigoKanaPostfix), keigoEntry);
	}

	public KeigoEntry getKeigoHighEntryFromWord(String kanji, String kana) {
		return wordToKeigoEntryHighMap.get(getKey(kanji, null, kana, null));
	}

	public List<KeigoEntry> getKeigoHighEntryList() {
		return keigoHighEntryList;
	}

	public KeigoEntry getKeigoHighEntryFromKeigoWord(String keigoKanji, String keigoKanjiPostfix, String keigoKana,
			String keigoKanaPostfix) {
		return keigoWordToKeigoEntryHighMap.get(getKey(keigoKanji, keigoKanjiPostfix, keigoKana, keigoKanaPostfix));
	}

	public List<KeigoEntry> getKeigoLowEntryList() {
		return keigoLowEntryList;
	}

	public KeigoEntry findKeigoHighEntry(DictionaryEntryType dictionaryEntryType, String kanji, List<String> kanaList,
			List<String> romajiList) {

		for (KeigoEntry keigoEntry : keigoHighEntryList) {

			DictionaryEntryType keigoEntryDictionaryEntryType = keigoEntry.getDictionaryEntryType();

			String keigoEntryKanji = keigoEntry.getKanji();
			String keigoEntryKana = keigoEntry.getKana();
			String keigoEntryRomaji = keigoEntry.getRomaji();

			for (int idx = 0; idx < kanaList.size(); ++idx) {

				if (dictionaryEntryType == keigoEntryDictionaryEntryType) {

					KeigoEntryFindMatchType findMatchType = keigoEntry.getFindMatchType();

					if (findMatchType == KeigoEntryFindMatchType.END_WITH) {

						if (stringEndWith(kanji, keigoEntryKanji) == true
								&& stringEndWith(kanaList.get(idx), keigoEntryKana) == true
								&& stringEndWith(romajiList.get(idx), keigoEntryRomaji) == true) {

							return keigoEntry;
						}

					} else if (findMatchType == KeigoEntryFindMatchType.EXACT) {

						if (equals(kanji, keigoEntryKanji) == true && equals(kanaList.get(idx), keigoEntryKana) == true
								&& equals(romajiList.get(idx), keigoEntryRomaji) == true) {

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

	/// *** low ***
	private void addKeigoLowEntry(DictionaryEntryType dictionaryEntryType, String kanji, String kana, String romaji,

	KeigoEntryFindMatchType findMatchType, DictionaryEntryType keigoDictionaryEntryType, String keigoKanji,
			String keigoKanjiPostfix, String keigoKana, String keigoKanaPostfix, String keigoRomaji,
			String keigoRomajiPostfix,

			String keigoLongFormWithoutMasuKanji, String keigoLongFormWithoutMasuKana,
			String keigoLongFormWithoutMasuRomaji) {

		//////

		KeigoEntry keigoEntry = new KeigoEntry(dictionaryEntryType, kanji, kana, romaji,

		findMatchType, keigoDictionaryEntryType, keigoKanji, keigoKanjiPostfix, keigoKana, keigoKanaPostfix,
				keigoRomaji, keigoRomajiPostfix,

				keigoLongFormWithoutMasuKanji, keigoLongFormWithoutMasuKana, keigoLongFormWithoutMasuRomaji);

		keigoLowEntryList.add(keigoEntry);

		wordToKeigoEntryLowMap.put(getKey(kanji, null, kana, null), keigoEntry);
		keigoWordToKeigoEntryLowMap.put(getKey(keigoKanji, keigoKanaPostfix, keigoKana, keigoKanaPostfix), keigoEntry);
	}

	public KeigoEntry getKeigoLowEntryFromWord(String kanji, String kana) {
		return wordToKeigoEntryLowMap.get(getKey(kanji, null, kana, null));
	}

	public KeigoEntry getKeigoLowEntryFromKeigoWord(String keigoKanji, String keigoKanjiPostfix, String keigoKana,
			String keigoKanaPostfix) {
		return keigoWordToKeigoEntryLowMap.get(getKey(keigoKanji, keigoKanjiPostfix, keigoKana, keigoKanaPostfix));
	}

	public KeigoEntry findKeigoLowEntry(DictionaryEntryType dictionaryEntryType, String kanji, List<String> kanaList,
			List<String> romajiList) {

		for (KeigoEntry keigoEntry : keigoLowEntryList) {

			DictionaryEntryType keigoEntryDictionaryEntryType = keigoEntry.getDictionaryEntryType();

			String keigoEntryKanji = keigoEntry.getKanji();
			String keigoEntryKana = keigoEntry.getKana();
			String keigoEntryRomaji = keigoEntry.getRomaji();

			for (int idx = 0; idx < kanaList.size(); ++idx) {

				if (dictionaryEntryType == keigoEntryDictionaryEntryType) {

					KeigoEntryFindMatchType findMatchType = keigoEntry.getFindMatchType();

					if (findMatchType == KeigoEntryFindMatchType.END_WITH) {

						if (stringEndWith(kanji, keigoEntryKanji) == true
								&& stringEndWith(kanaList.get(idx), keigoEntryKana) == true
								&& stringEndWith(romajiList.get(idx), keigoEntryRomaji) == true) {

							return keigoEntry;
						}

					} else if (findMatchType == KeigoEntryFindMatchType.EXACT) {

						if (equals(kanji, keigoEntryKanji) == true && equals(kanaList.get(idx), keigoEntryKana) == true
								&& equals(romajiList.get(idx), keigoEntryRomaji) == true) {

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

	private String getKey(String kanji, String kanjiPostfix, String kana, String kanaPostfix) {

		if (kanji == null) {
			kanji = "-";
		}

		return kanji + (kanjiPostfix != null ? kanjiPostfix : "") + "." + kana
				+ (kanaPostfix != null ? kanaPostfix : "");
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

		private final DictionaryEntryType dictionaryEntryType;

		private final String kanji;
		private final String kana;
		private final String romaji;

		private final DictionaryEntryType keigoDictionaryEntryType;

		private final KeigoEntryFindMatchType findMatchType;

		private final String keigoKanji;
		private final String keigoKanjiPostfix;

		private final String keigoKana;
		private final String keigoKanaPostfix;

		private final String keigoRomaji;
		private final String keigoRomajiPostfix;

		private final String keigoLongFormWithoutMasuKanji;
		private final String keigoLongFormWithoutMasuKana;
		private final String keigoLongFormWithoutMasuRomaji;

		public KeigoEntry(DictionaryEntryType dictionaryEntryType, String kanji, String kana, String romaji,

		KeigoEntryFindMatchType findMatchType, DictionaryEntryType keigoDictionaryEntryType, String keigoKanji,
				String keigoKanjiPostfix, String keigoKana, String keigoKanaPostfix, String keigoRomaji,
				String keigoRomajiPostfix,

				String keigoLongFormWithoutMasuKanji, String keigoLongFormWithoutMasuKana,
				String keigoLongFormWithoutMasuRomaji) {

			this.dictionaryEntryType = dictionaryEntryType;

			this.kanji = kanji;
			this.kana = kana;
			this.romaji = romaji;

			this.findMatchType = findMatchType;

			this.keigoDictionaryEntryType = keigoDictionaryEntryType;

			this.keigoKanji = keigoKanji;
			this.keigoKanjiPostfix = keigoKanjiPostfix;

			this.keigoKana = keigoKana;
			this.keigoKanaPostfix = keigoKanaPostfix;

			this.keigoRomaji = keigoRomaji;
			this.keigoRomajiPostfix = keigoRomajiPostfix;

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

		public String getKeigoKanji(boolean full) {

			if (keigoKanji == null) {
				return null;
			}

			StringBuffer sb = new StringBuffer();

			sb.append(keigoKanji);

			if (full == true && keigoKanjiPostfix != null) {
				sb.append(keigoKanjiPostfix);
			}

			return sb.toString();
		}

		public String getKeigoKana(boolean full) {

			StringBuffer sb = new StringBuffer();

			sb.append(keigoKana);

			if (full == true && keigoKanaPostfix != null) {
				sb.append(keigoKanaPostfix);
			}

			return sb.toString();
		}

		public String getKeigoRomaji(boolean full) {

			StringBuffer sb = new StringBuffer();

			sb.append(keigoRomaji);

			if (full == true && keigoRomajiPostfix != null) {
				sb.append(" ").append(keigoRomajiPostfix);
			}

			return sb.toString();
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
