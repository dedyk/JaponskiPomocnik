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
				DictionaryEntryType.WORD_VERB_U, null,  "いらっしゃる", "irassharu", null, "いらっしゃい", "irasshai");

		// iku
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_U, "行く", "いく", "iku",
				DictionaryEntryType.WORD_VERB_U, null,  "いらっしゃる", "irassharu", null, "いらっしゃい", "irasshai");

		// kuru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_IRREGULAR, "来る", "くる", "kuru",
				DictionaryEntryType.WORD_VERB_U, null,  "いらっしゃる", "irassharu", null, "いらっしゃい", "irasshai");
		
		// miru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, "見る", "みる", "miru",
				DictionaryEntryType.WORD_VERB_U, "ご覧になる", "ごらんになる", "goran ni naru", null, null, null);
		
		// iu
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_U, "言う", "いう", "iu",
				DictionaryEntryType.WORD_VERB_U, null, "おっしゃる", "ossharu", null, "おっしゃい", "osshai");
		
		// suru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_IRREGULAR, null, "する", "suru",
				DictionaryEntryType.WORD_VERB_U, null, "なさる", "nasaru", null, "なさい", "nasai");
		
		// taberu
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, "食べる", "たべる", "taberu",
				DictionaryEntryType.WORD_VERB_U, "召し上がる", "めしあがる", "meshiagaru", null, null, null);
		
		// nomu
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_U, "飲む", "のむ", "nomu",
				DictionaryEntryType.WORD_VERB_U, "召し上がる", "めしあがる", "meshiagaru", null, null, null);
		
		// kureru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, null, "くれる", "kureru",
				DictionaryEntryType.WORD_VERB_U, null, "くださる", "kudasaru", null, "ください", "kudasai");
		
		// neru
		addKeigoHighEntry(DictionaryEntryType.WORD_VERB_RU, "寝る", "ねる", "neru",
				DictionaryEntryType.WORD_VERB_U, "お休みになる", "おやすみになる", "oyasumi ni naru", null, null, null);
	}
	
	private void addKeigoHighEntry(DictionaryEntryType dictionaryEntryType, String kanji, String kana, String romaji,
				DictionaryEntryType keigoDictionaryEntryType, String keigoKanji, String keigoKana, String keigoRomaji,
				String keigoLongFormWithoutMasuKanji, String keigoLongFormWithoutMasuKana, String keigoLongFormWithoutMasuRomaji) {
		
		KeigoEntry keigoEntry = new KeigoEntry(dictionaryEntryType, kanji, kana, romaji,
				
				keigoDictionaryEntryType, keigoKanji, keigoKana, keigoRomaji, 
				
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
	
	public List<KeigoEntry> getKeigoHighEntryList() {
		return keigoHighEntryList;
	}

	private String getKey(String kanji, String kana) {
		
		if (kanji == null) {
			kanji = "-";
		}
		
		return kanji + "." + kana;		
	}
	
	public static class KeigoEntry {
		
		private DictionaryEntryType dictionaryEntryType;
		
		private String kanji;
		private String kana;
		private String romaji;
		
		private DictionaryEntryType keigoDictionaryEntryType;
		
		private String keigoKanji;		
		private String keigoKana;		
		private String keigoRomaji;
		
		private String keigoLongFormWithoutMasuKanji;
		private String keigoLongFormWithoutMasuKana;
		private String keigoLongFormWithoutMasuRomaji;
		
		public KeigoEntry(DictionaryEntryType dictionaryEntryType, String kanji, String kana, String romaji,
				
				DictionaryEntryType keigoDictionaryEntryType, String keigoKanji, String keigoKana, String keigoRomaji,
				
				String keigoLongFormWithoutMasuKanji, String keigoLongFormWithoutMasuKana, String keigoLongFormWithoutMasuRomaji) {
			
			this.dictionaryEntryType = dictionaryEntryType;
			
			this.kanji = kanji;
			this.kana = kana;
			this.romaji = romaji;
			
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
}
