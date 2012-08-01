package pl.idedyk.android.japaneselearnhelper.gramma;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;

public class VerbGrammaConjugater {
	
	private static Map<String, String> lastKanaCharsMapperToIChar;
	private static Map<String, String> lastRomajiCharsMapperToIChar;
	
	private static Map<String, String> lastKanaCharsMapperToTeFormChar;
	private static Map<String, String> lastRomajiCharsMapperToTeFormChar;
	
	static {
		lastKanaCharsMapperToIChar = new HashMap<String, String>();
		
		lastKanaCharsMapperToIChar.put("う", "い");
		lastKanaCharsMapperToIChar.put("く", "き");
		lastKanaCharsMapperToIChar.put("す", "し");
		lastKanaCharsMapperToIChar.put("つ", "ち");
		lastKanaCharsMapperToIChar.put("ぬ", "に");
		lastKanaCharsMapperToIChar.put("む", "み");
		lastKanaCharsMapperToIChar.put("る", "り");
		lastKanaCharsMapperToIChar.put("ぐ", "ぎ");
		lastKanaCharsMapperToIChar.put("ぶ", "び");

		lastRomajiCharsMapperToIChar = new HashMap<String, String>();
		
		lastRomajiCharsMapperToIChar.put("u", "i");
		lastRomajiCharsMapperToIChar.put("ku", "ki");
		lastRomajiCharsMapperToIChar.put("su", "shi");
		lastRomajiCharsMapperToIChar.put("tsu", "chi");
		lastRomajiCharsMapperToIChar.put("nu", "ni");
		lastRomajiCharsMapperToIChar.put("mu", "mi");
		lastRomajiCharsMapperToIChar.put("ru", "ri");
		lastRomajiCharsMapperToIChar.put("gu", "gi");
		lastRomajiCharsMapperToIChar.put("bu", "bi");
		
		lastKanaCharsMapperToTeFormChar = new HashMap<String, String>();
		
		lastKanaCharsMapperToTeFormChar.put("う", "って");
		lastKanaCharsMapperToTeFormChar.put("つ", "って");
		lastKanaCharsMapperToTeFormChar.put("る", "って");
		
		lastKanaCharsMapperToTeFormChar.put("む", "んで");
		lastKanaCharsMapperToTeFormChar.put("ぶ", "んで");
		lastKanaCharsMapperToTeFormChar.put("ぬ", "んで");
		
		lastKanaCharsMapperToTeFormChar.put("く", "いて");
		lastKanaCharsMapperToTeFormChar.put("ぐ", "いで");
		lastKanaCharsMapperToTeFormChar.put("す", "して");

		lastRomajiCharsMapperToTeFormChar = new HashMap<String, String>();
		
		lastRomajiCharsMapperToTeFormChar.put("u", "tte");
		lastRomajiCharsMapperToTeFormChar.put("tsu", "tte");
		lastRomajiCharsMapperToTeFormChar.put("ru", "tte");
		
		lastRomajiCharsMapperToTeFormChar.put("mu", "nde");
		lastRomajiCharsMapperToTeFormChar.put("bu", "nde");
		lastRomajiCharsMapperToTeFormChar.put("nu", "nde");
		
		lastRomajiCharsMapperToTeFormChar.put("ku", "ite");
		lastRomajiCharsMapperToTeFormChar.put("gu", "ide");
		lastRomajiCharsMapperToTeFormChar.put("su", "shite");
	}

	public static List<GrammaFormConjugateGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry) {

		List<GrammaFormConjugateGroupTypeElements> result = new ArrayList<GrammaFormConjugateGroupTypeElements>();

		// forma formalna
		GrammaFormConjugateGroupTypeElements formal = new GrammaFormConjugateGroupTypeElements();

		formal.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.VERB_FORMAL);

		formal.getGrammaFormConjugateResults().add(makeFormalPresentForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPresentNegativeForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPastForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPastNegativeForm(dictionaryEntry));

		result.add(formal);
		
		// forma nieformalna (prosta) FIXME !!!
		
		// forma te
		GrammaFormConjugateGroupTypeElements teForm = new GrammaFormConjugateGroupTypeElements();
		
		teForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.VERB_TE);
		
		teForm.getGrammaFormConjugateResults().add(makeTeForm(dictionaryEntry));
		
		result.add(teForm);
		
		return result;		
	}

	public static GrammaFormConjugateResult makeFormalPresentForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, twierdzenie, forma formalna, -masu

		final String postfixKana = "ます";
		final String postfixRomaji = "masu";

		return makeVerbGrammaConjugateFormalForm(dictionaryEntry, GrammaFormConjugateResultType.VERB_FORMAL_PRESENT,
				postfixKana, postfixRomaji);
	}

	public static GrammaFormConjugateResult makeFormalPresentNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, twierdzenie, forma formalna, -masen

		final String postfixKana = "ません";
		final String postfixRomaji = "masen";

		return makeVerbGrammaConjugateFormalForm(dictionaryEntry, GrammaFormConjugateResultType.VERB_FORMAL_PRESENT_NEGATIVE,
				postfixKana, postfixRomaji);
	}

	public static GrammaFormConjugateResult makeFormalPastForm(DictionaryEntry dictionaryEntry) {
		// czas przeszly, przeczenie, forma formalna, -mashita

		final String postfixKana = "ました";
		final String postfixRomaji = "mashita";

		return makeVerbGrammaConjugateFormalForm(dictionaryEntry, GrammaFormConjugateResultType.VERB_FORMAL_PAST,
				postfixKana, postfixRomaji);
	}

	public static GrammaFormConjugateResult makeFormalPastNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas przeszly, twierdzenie, forma formalna, -masen deshita

		final String postfixKana = "ませんでした";
		final String postfixRomaji = "masen deshita";

		return makeVerbGrammaConjugateFormalForm(dictionaryEntry, GrammaFormConjugateResultType.VERB_FORMAL_PAST_NEGATIVE,
				postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeVerbGrammaConjugateFormalForm(DictionaryEntry dictionaryEntry,
			GrammaFormConjugateResultType grammaFormConjugateResultType, String postfixKana, String postfixRomaji) {
		
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);
		
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();

		result.setResultType(grammaFormConjugateResultType);
		
		String kanji = dictionaryEntry.getKanji();

		if (kanji != null) {
			String kanjiStem = getStemForKanji(kanji, dictionaryEntryType);
			
			result.setKanji(kanjiStem + postfixKana);
		}
		
		List<String> kanaList = dictionaryEntry.getKanaList();

		List<String> kanaListResult = new ArrayList<String>();

		for (String currentKana : kanaList) {			
			String kanaStem = getStemForKana(currentKana, dictionaryEntryType);

			kanaListResult.add(kanaStem + postfixKana);
		}

		result.setKanaList(kanaListResult);		

		List<String> romajiList = dictionaryEntry.getRomajiList();

		List<String> romajiListResult = new ArrayList<String>();

		for (String currentRomaji : romajiList) {
			String romajiStem = getStemForRomaji(currentRomaji, dictionaryEntryType);

			romajiListResult.add(romajiStem + postfixRomaji);
		}

		result.setRomajiList(romajiListResult);

		return result;
	}
	
	private static String getStemForKanji(String kanji, DictionaryEntryType dictionaryEntryType) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			return removeLastChar(kanji);
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			return removeLastChar(kanji) + getLastCharConvertedToI(kanji);
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (kanji.endsWith("来る") == true) {
				return removeChars(kanji, 1);
				
			} else if (kanji.endsWith("くる") == true) {
				return removeChars(kanji, 2) + "き";
				
			} else if (kanji.endsWith("する") == true) {
				return removeChars(kanji, 2) + "し";
				
			} else {
				throw new RuntimeException("getStemForKanji 1");	
			}
		} else {
			throw new RuntimeException("getStemForKanji 2");
		}
	}

	private static String getStemForKana(String kana, DictionaryEntryType dictionaryEntryType) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			return removeLastChar(kana);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			return removeLastChar(kana) + getLastCharConvertedToI(kana);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (kana.endsWith("くる") == true) {
				return removeChars(kana, 2) + "き";
				
			} else if (kana.endsWith("する") == true) {
				return removeChars(kana, 2) + "し";
			
			} else {
				throw new RuntimeException("getStemForKanji 1");
			}
		} else {
			throw new RuntimeException("getStemForKanji 2");
		}
	}

	private static String getStemForRomaji(String romaji, DictionaryEntryType dictionaryEntryType) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			return removeChars(romaji, 2);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			
			String romajiLastThreeChars = getLastChars(romaji, 3);
			
			if (romajiLastThreeChars != null && lastRomajiCharsMapperToIChar.containsKey(romajiLastThreeChars) == true) {
				return removeChars(romaji, 3) + lastRomajiCharsMapperToIChar.get(romajiLastThreeChars);
			}
			
			String romajiLastTwoChars = getLastChars(romaji, 2);
			
			if (romajiLastTwoChars != null && lastRomajiCharsMapperToIChar.containsKey(romajiLastTwoChars) == true) {
				return removeChars(romaji, 2) + lastRomajiCharsMapperToIChar.get(romajiLastTwoChars);
			}
			
			String romajiLastOneChar = getLastChars(romaji, 1);
			
			if (romajiLastOneChar != null && lastRomajiCharsMapperToIChar.containsKey(romajiLastOneChar) == true) {
				return removeChars(romaji, 1) + lastRomajiCharsMapperToIChar.get(romajiLastOneChar);
			}
			
			throw new RuntimeException("getStemForRomaji 3");
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (romaji.endsWith("kuru") == true) {
				return removeChars(romaji, 4) + "ki";
			} else if (romaji.endsWith("suru") == true) {
				return removeChars(romaji, 4) + "shi";
			} else {
				throw new RuntimeException("getStemForRomaji 1");
			}
		} else {
			throw new RuntimeException("getStemForRomaji 2");
		}
	}
	
	private static String removeLastChar(String text) {
		return removeChars(text, 1);
	}
	
	private static String removeChars(String text, int size) {
		return text.substring(0, text.length() - size);
	}
	
	private static String getLastChars(String text, int size) {
		if (text.length() - size < 0) { 
			return null;
		}
		
		return text.substring(text.length() - size);
	}

	private static GrammaFormConjugateResult makeCommon(DictionaryEntry dictionaryEntry) {

		// validate DictionaryEntry
		validateDictionaryEntry(dictionaryEntry);

		// create result
		GrammaFormConjugateResult result = new GrammaFormConjugateResult();

		return result;
	}

	private static void  validateDictionaryEntry(DictionaryEntry dictionaryEntry) {
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();

		if (dictionaryEntryType != DictionaryEntryType.WORD_VERB_U &&
				dictionaryEntryType != DictionaryEntryType.WORD_VERB_RU &&
				dictionaryEntryType != DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			throw new RuntimeException("dictionaryEntryType != DictionaryEntryType.WORD_VERB_U/RU/IRREGULAR");
		}
		
		String kanji = dictionaryEntry.getKanji();
		
		if (kanji != null) {
			if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && kanji.endsWith("る") == false) {
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && kanji.endsWith(る) == false)");
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && getLastCharConvertedToI(kanji) == null) {	
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && getLastCharConvertedToI(kanji) == null");
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
				
				if (kanji.endsWith("来る") == false && kanji.endsWith("くる") == false && kanji.endsWith("する") == false) {
					throw new RuntimeException("kanji.endsWith(来る) == false && kanji.endsWith(くる) == false && kanji.endsWith(する) == false");
				}				
			}
		}
		
		List<String> kanaList = dictionaryEntry.getKanaList();

		for (String currentKana : kanaList) {
			if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && currentKana.endsWith("る") == false) {
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && kanji.endsWith(る) == false)");
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && getLastCharConvertedToI(currentKana) == null) {
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && getLastCharConvertedToI(kanji) == null");
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
				
				if (currentKana.endsWith("する") == false && currentKana.endsWith("くる") == false) {
					throw new RuntimeException("currentKana.endsWith(する) == false && currentKana.endsWith(くる) == false");
				}
			}
		}
		
		List<String> romajiList = dictionaryEntry.getRomajiList();

		for (String currentRomaji : romajiList) {
			if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && currentRomaji.endsWith("ru") == false) {
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && kanji.endsWith(ru) == false)");
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && currentRomaji.endsWith("u") == false) {
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && kanji.endsWith(u) == false)");
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
				
				if (currentRomaji.endsWith("suru") == false && currentRomaji.endsWith("kuru") == false) {
					throw new RuntimeException("currentRomaji.endsWith(suru) == false && currentRomaji.endsWith(kuru) == false");
				}
			}
		}
		
	}
	
	private static String getLastCharConvertedToI(String word) {
		
		String lastChar = word.substring(word.length() - 1);
		
		String lastCharConvertedToI = lastKanaCharsMapperToIChar.get(lastChar);
		
		if (lastCharConvertedToI == null) {
			throw new RuntimeException("lastCharConvertedToI == null: " + word);
		}
		
		return lastCharConvertedToI;
	}
	
	private static GrammaFormConjugateResult makeTeForm(DictionaryEntry dictionaryEntry) {
		
		// forma te
		
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);
		
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();

		result.setResultType(GrammaFormConjugateResultType.VERB_TE);

		String kanji = dictionaryEntry.getKanji();
		
		if (kanji != null) {
			String teFormKanji = makeTeFormForKanjiOrKana(kanji, dictionaryEntryType);

			result.setKanji(teFormKanji);
		}
		
		List<String> kanaList = dictionaryEntry.getKanaList();

		List<String> kanaListResult = new ArrayList<String>();

		for (String currentKana : kanaList) {			
			String teFormKana = makeTeFormForKanjiOrKana(currentKana, dictionaryEntryType);
			
			kanaListResult.add(teFormKana);
		}

		result.setKanaList(kanaListResult);
		
		List<String> romajiList = dictionaryEntry.getRomajiList();

		List<String> romajiListResult = new ArrayList<String>();

		for (String currentRomaji : romajiList) {
			String teFormRomaji = makeTeFormForRomaji(currentRomaji, dictionaryEntryType);
			
			romajiListResult.add(teFormRomaji);
		}

		result.setRomajiList(romajiListResult);

		return result;
	}

	private static String makeTeFormForKanjiOrKana(String text, DictionaryEntryType dictionaryEntryType) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			return removeLastChar(text) + "て";
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			
			boolean ikuException = false;
			
			if (text.equals("行く") == true || text.endsWith("行く") == true ||
				text.equals("いく") == true || text.endsWith("いく") == true) {
				
				ikuException = true;
			}
			
			String lastChar = getLastChars(text, 1);
			
			String tePostfix = null;
			
			if (ikuException == false) {
				tePostfix = lastKanaCharsMapperToTeFormChar.get(lastChar);	
			} else {
				tePostfix = "って";
			}
			
			if (tePostfix == null) {
				throw new RuntimeException("tePostfix == null");
			}
			
			return removeLastChar(text) + tePostfix;
			
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (text.endsWith("来る") == true) {
				return removeChars(text, 1) + "て";
				
			} else if (text.endsWith("くる") == true) {
				return removeChars(text, 2) + "きて";
				
			} else if (text.endsWith("する") == true) {
				return removeChars(text, 2) + "して";
			} else {
				throw new RuntimeException("makeTeFormForKanjiOrKana 1");	
			}
		}
		
		throw new RuntimeException("makeTeFormForKanjiOrKana 2");
	}
	
	private static String makeTeFormForRomaji(String romaji, DictionaryEntryType dictionaryEntryType) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			return removeChars(romaji, 2) + "te";
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			
			boolean ikuException = false;
			
			if (romaji.equals("iku") == true || romaji.endsWith(" iku") == true) {
				ikuException = true;
			}
			
			if (ikuException == false) {
				String romajiLastThreeChars = getLastChars(romaji, 3);
				
				if (romajiLastThreeChars != null && lastRomajiCharsMapperToTeFormChar.containsKey(romajiLastThreeChars) == true) {
					return removeChars(romaji, 3) + lastRomajiCharsMapperToTeFormChar.get(romajiLastThreeChars);
				}
				
				String romajiLastTwoChars = getLastChars(romaji, 2);
				
				if (romajiLastTwoChars != null && lastRomajiCharsMapperToTeFormChar.containsKey(romajiLastTwoChars) == true) {
					return removeChars(romaji, 2) + lastRomajiCharsMapperToTeFormChar.get(romajiLastTwoChars);
				}
				
				String romajiLastOneChar = getLastChars(romaji, 1);
				
				if (romajiLastOneChar != null && lastRomajiCharsMapperToTeFormChar.containsKey(romajiLastOneChar) == true) {
					return removeChars(romaji, 1) + lastRomajiCharsMapperToTeFormChar.get(romajiLastOneChar);
				}
				
				throw new RuntimeException("makeTeFormForRomaji 1");
			} else {
				return removeChars(romaji, 2) + "tte";
			}
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (romaji.endsWith("kuru") == true) {
				return removeChars(romaji, 4) + "kite";
			} else if (romaji.endsWith("suru") == true) {
				return removeChars(romaji, 4) + "shite";
			} else {
				throw new RuntimeException("makeTeFormForRomaji 2");
			}
		} else {
			throw new RuntimeException("makeTeFormForRomaji 3");
		}
	}
}
