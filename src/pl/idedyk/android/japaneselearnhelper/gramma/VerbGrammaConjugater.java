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
	
	private static Map<String, String> lastKanaCharsMapperToAChar;
	private static Map<String, String> lastRomajiCharsMapperToAChar;
	
	private static Map<String, String> lastKanaCharsMapperToEChar;
	private static Map<String, String> lastRomajiCharsMapperToEChar;

	private static Map<String, String> lastKanaCharsMapperToCharForVolitionalForm;
	private static Map<String, String> lastRomajiCharsMapperToCharForVolitionalForm;
	
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
		
		lastKanaCharsMapperToAChar = new HashMap<String, String>();
		
		lastKanaCharsMapperToAChar.put("う", "わ");
		lastKanaCharsMapperToAChar.put("く", "か");
		lastKanaCharsMapperToAChar.put("す", "さ");
		lastKanaCharsMapperToAChar.put("つ", "た");
		lastKanaCharsMapperToAChar.put("ぬ", "な");
		lastKanaCharsMapperToAChar.put("む", "ま");
		lastKanaCharsMapperToAChar.put("る", "ら");
		lastKanaCharsMapperToAChar.put("ぐ", "が");
		lastKanaCharsMapperToAChar.put("ぶ", "ば");

		lastRomajiCharsMapperToAChar = new HashMap<String, String>();
		
		lastRomajiCharsMapperToAChar.put("u", "wa");
		lastRomajiCharsMapperToAChar.put("ku", "ka");
		lastRomajiCharsMapperToAChar.put("su", "sa");
		lastRomajiCharsMapperToAChar.put("tsu", "ta");
		lastRomajiCharsMapperToAChar.put("nu", "na");
		lastRomajiCharsMapperToAChar.put("mu", "ma");
		lastRomajiCharsMapperToAChar.put("ru", "ra");
		lastRomajiCharsMapperToAChar.put("gu", "ga");
		lastRomajiCharsMapperToAChar.put("bu", "ba");
		
		lastKanaCharsMapperToEChar = new HashMap<String, String>();
		
		lastKanaCharsMapperToEChar.put("う", "え");
		lastKanaCharsMapperToEChar.put("く", "け");
		lastKanaCharsMapperToEChar.put("す", "せ");
		lastKanaCharsMapperToEChar.put("つ", "て");
		lastKanaCharsMapperToEChar.put("ぬ", "ね");
		lastKanaCharsMapperToEChar.put("む", "め");
		lastKanaCharsMapperToEChar.put("る", "れ");
		lastKanaCharsMapperToEChar.put("ぐ", "げ");
		lastKanaCharsMapperToEChar.put("ぶ", "べ");
		
		lastRomajiCharsMapperToEChar = new HashMap<String, String>();
		
		lastRomajiCharsMapperToEChar.put("u", "e");
		lastRomajiCharsMapperToEChar.put("ku", "ke");
		lastRomajiCharsMapperToEChar.put("su", "se");
		lastRomajiCharsMapperToEChar.put("tsu", "te");
		lastRomajiCharsMapperToEChar.put("nu", "ne");
		lastRomajiCharsMapperToEChar.put("mu", "me");
		lastRomajiCharsMapperToEChar.put("ru", "re");
		lastRomajiCharsMapperToEChar.put("gu", "ge");
		lastRomajiCharsMapperToEChar.put("bu", "be");
		
		lastKanaCharsMapperToCharForVolitionalForm = new HashMap<String, String>();
		
		lastKanaCharsMapperToCharForVolitionalForm.put("う", "おう");
		lastKanaCharsMapperToCharForVolitionalForm.put("く", "こう");
		lastKanaCharsMapperToCharForVolitionalForm.put("す", "そう");
		lastKanaCharsMapperToCharForVolitionalForm.put("つ", "とう");
		lastKanaCharsMapperToCharForVolitionalForm.put("ぬ", "のう");
		lastKanaCharsMapperToCharForVolitionalForm.put("む", "もう");
		lastKanaCharsMapperToCharForVolitionalForm.put("る", "ろう");
		lastKanaCharsMapperToCharForVolitionalForm.put("ぐ", "ごう");
		lastKanaCharsMapperToCharForVolitionalForm.put("ぶ", "ぼう");

		lastRomajiCharsMapperToCharForVolitionalForm = new HashMap<String, String>();
		
		lastRomajiCharsMapperToCharForVolitionalForm.put("u", "ou");
		lastRomajiCharsMapperToCharForVolitionalForm.put("ku", "kou");
		lastRomajiCharsMapperToCharForVolitionalForm.put("su", "sou");
		lastRomajiCharsMapperToCharForVolitionalForm.put("tsu", "tou");
		lastRomajiCharsMapperToCharForVolitionalForm.put("nu", "nou");
		lastRomajiCharsMapperToCharForVolitionalForm.put("mu", "mou");
		lastRomajiCharsMapperToCharForVolitionalForm.put("ru", "rou");
		lastRomajiCharsMapperToCharForVolitionalForm.put("gu", "gou");
		lastRomajiCharsMapperToCharForVolitionalForm.put("bu", "bou");
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
		
		// forma nieformalna (prosta)
		GrammaFormConjugateGroupTypeElements informal = new GrammaFormConjugateGroupTypeElements();
		
		informal.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.VERB_INFORMAL);
		
		informal.getGrammaFormConjugateResults().add(makeInformalPresentForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPresentNegativeForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPastForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPastNegativeForm(dictionaryEntry));
		
		result.add(informal);
		
		// stem
		GrammaFormConjugateGroupTypeElements stemForm = new GrammaFormConjugateGroupTypeElements();
		
		stemForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.VERB_STEM);
		
		stemForm.getGrammaFormConjugateResults().add(makeStemForm(dictionaryEntry));
		
		result.add(stemForm);
		
		// mashou
		GrammaFormConjugateGroupTypeElements mashouForm = new GrammaFormConjugateGroupTypeElements();
		
		mashouForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.VERB_MASHOU);
		
		mashouForm.getGrammaFormConjugateResults().add(makeMashouForm(dictionaryEntry));
		
		result.add(mashouForm);
		
		// forma te
		GrammaFormConjugateGroupTypeElements teForm = new GrammaFormConjugateGroupTypeElements();
		
		teForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.VERB_TE);
		
		teForm.getGrammaFormConjugateResults().add(makeTeForm(dictionaryEntry));
		
		result.add(teForm);
		
		// forma potecjalna
		GrammaFormConjugateGroupTypeElements potentialInformalForm = new GrammaFormConjugateGroupTypeElements();
		potentialInformalForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.VERB_POTENTIAL_INFORMAL);
		
		GrammaFormConjugateResult potentialFormInformalPresentForm = makePotentialFormInformalPresentForm(dictionaryEntry);
		
		// prosta
		potentialInformalForm.getGrammaFormConjugateResults().add(potentialFormInformalPresentForm);
		potentialInformalForm.getGrammaFormConjugateResults().add(makePotentialFormInformalPresentNegativeForm(potentialFormInformalPresentForm));
		potentialInformalForm.getGrammaFormConjugateResults().add(makePotentialFormInformalPastForm(potentialFormInformalPresentForm));
		potentialInformalForm.getGrammaFormConjugateResults().add(makePotentialFormInformalPastNegativeForm(potentialFormInformalPresentForm));
		
		result.add(potentialInformalForm);
		
		GrammaFormConjugateGroupTypeElements potentialFormalForm = new GrammaFormConjugateGroupTypeElements();
		potentialFormalForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.VERB_POTENTIAL_FORMAL);
		
		// formalna
		potentialFormalForm.getGrammaFormConjugateResults().add(makePotentialFormFormalPresentForm(potentialFormInformalPresentForm));
		potentialFormalForm.getGrammaFormConjugateResults().add(makePotentialFormFormalPresentNegativeForm(potentialFormInformalPresentForm));
		potentialFormalForm.getGrammaFormConjugateResults().add(makePotentialFormFormalPastForm(potentialFormInformalPresentForm));
		potentialFormalForm.getGrammaFormConjugateResults().add(makePotentialFormFormalPastNegativeForm(potentialFormInformalPresentForm));
		
		result.add(potentialFormalForm);
		
		// forma te
		GrammaFormConjugateGroupTypeElements potentialTeForm = new GrammaFormConjugateGroupTypeElements();
		
		potentialTeForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.VERB_POTENTIAL_TE);
		
		potentialTeForm.getGrammaFormConjugateResults().add(makePotentialTeForm(potentialFormInformalPresentForm));
		
		result.add(potentialTeForm);
		
		// forma wolicjonalna
		GrammaFormConjugateGroupTypeElements volitionalForm = new GrammaFormConjugateGroupTypeElements();
		
		volitionalForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.VERB_VOLITIONAL);
		
		volitionalForm.getGrammaFormConjugateResults().add(makeVolitionalForm(dictionaryEntry));
		
		result.add(volitionalForm);
				
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
				throw new RuntimeException("getStemForKanji 1: " + kanji);	
			}
		} else {
			throw new RuntimeException("getStemForKanji 2: " + dictionaryEntryType);
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
				throw new RuntimeException("getStemForKanji 1: " + kana);
			}
		} else {
			throw new RuntimeException("getStemForKanji 2: " + dictionaryEntryType);
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
			
			throw new RuntimeException("getStemForRomaji 3: " + romaji);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (romaji.endsWith("kuru") == true) {
				return removeChars(romaji, 4) + "ki";
			} else if (romaji.endsWith("suru") == true) {
				return removeChars(romaji, 4) + "shi";
			} else {
				throw new RuntimeException("getStemForRomaji 1: " + romaji);
			}
		} else {
			throw new RuntimeException("getStemForRomaji 2: " + dictionaryEntryType);
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

		result.setPrefixKana(dictionaryEntry.getPrefixKana());
		result.setPrefixRomaji(dictionaryEntry.getPrefixRomaji());

		return result;
	}

	private static void  validateDictionaryEntry(DictionaryEntry dictionaryEntry) {
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();

		if (dictionaryEntryType != DictionaryEntryType.WORD_VERB_U &&
				dictionaryEntryType != DictionaryEntryType.WORD_VERB_RU &&
				dictionaryEntryType != DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			throw new RuntimeException("dictionaryEntryType != DictionaryEntryType.WORD_VERB_U/RU/IRREGULAR: " + dictionaryEntryType);
		}
		
		String kanji = dictionaryEntry.getKanji();
		
		if (kanji != null) {
			if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && kanji.endsWith("る") == false) {
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && kanji.endsWith(る) == false): " + kanji);
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && getLastCharConvertedToI(kanji) == null) {	
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && getLastCharConvertedToI(kanji) == null" + kanji);
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
				
				if (kanji.endsWith("来る") == false && kanji.endsWith("くる") == false && kanji.endsWith("する") == false) {
					throw new RuntimeException("kanji.endsWith(来る) == false && kanji.endsWith(くる) == false && kanji.endsWith(する) == false: " + kanji);
				}				
			}
		}
		
		List<String> kanaList = dictionaryEntry.getKanaList();

		for (String currentKana : kanaList) {
			if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && currentKana.endsWith("る") == false) {
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && kanji.endsWith(る) == false): " + currentKana);
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && getLastCharConvertedToI(currentKana) == null) {
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && getLastCharConvertedToI(kanji) == null: " + currentKana);
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
				
				if (currentKana.endsWith("する") == false && currentKana.endsWith("くる") == false) {
					throw new RuntimeException("currentKana.endsWith(する) == false && currentKana.endsWith(くる) == false: " + currentKana);
				}
			}
		}
		
		List<String> romajiList = dictionaryEntry.getRomajiList();

		for (String currentRomaji : romajiList) {
			if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && currentRomaji.endsWith("ru") == false) {
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU && kanji.endsWith(ru) == false): " + currentRomaji);
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && currentRomaji.endsWith("u") == false) {
				throw new RuntimeException("dictionaryEntryType == DictionaryEntryType.WORD_VERB_U && kanji.endsWith(u) == false): " + currentRomaji);
			
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
				
				if (currentRomaji.endsWith("suru") == false && currentRomaji.endsWith("kuru") == false) {
					throw new RuntimeException("currentRomaji.endsWith(suru) == false && currentRomaji.endsWith(kuru) == false: " + currentRomaji);
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
	
	public static GrammaFormConjugateResult makeTeForm(DictionaryEntry dictionaryEntry) {
		
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
				throw new RuntimeException("tePostfix == null: " + text);
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
				throw new RuntimeException("makeTeFormForKanjiOrKana 1: " + text);	
			}
		}
		
		throw new RuntimeException("makeTeFormForKanjiOrKana 2: " + dictionaryEntryType);
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
				
				throw new RuntimeException("makeTeFormForRomaji 1: " + romaji);
			} else {
				return removeChars(romaji, 2) + "tte";
			}
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (romaji.endsWith("kuru") == true) {
				return removeChars(romaji, 4) + "kite";
			} else if (romaji.endsWith("suru") == true) {
				return removeChars(romaji, 4) + "shite";
			} else {
				throw new RuntimeException("makeTeFormForRomaji 2: " + romaji);
			}
		} else {
			throw new RuntimeException("makeTeFormForRomaji 3: " + dictionaryEntryType);
		}
	}
	
	public static GrammaFormConjugateResult makeInformalPresentForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, twierdzenie, forma nieformalna (prosta)
		
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);

		result.setResultType(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT);
		
		result.setKanji(dictionaryEntry.getKanji());
		
		result.setKanaList(dictionaryEntry.getKanaList());
		
		result.setRomajiList(dictionaryEntry.getRomajiList());		
		
		return result;
	}
	
	public static GrammaFormConjugateResult makeInformalPresentNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma nieformalna (prosta)
		
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);

		result.setResultType(GrammaFormConjugateResultType.VERB_INFORMAL_PRESENT_NEGATIVE);

		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		
		String kanji = dictionaryEntry.getKanji();
		
		if (kanji != null) {
			String informalKanji = makeInformalPresentNegativeFormForKanjiOrKana(kanji, dictionaryEntryType);
						
			result.setKanji(informalKanji);
		}
		
		List<String> kanaList = dictionaryEntry.getKanaList();

		List<String> kanaListResult = new ArrayList<String>();

		for (String currentKana : kanaList) {			
			String informalKana = makeInformalPresentNegativeFormForKanjiOrKana(currentKana, dictionaryEntryType);

			kanaListResult.add(informalKana);
		}

		result.setKanaList(kanaListResult);		

		List<String> romajiList = dictionaryEntry.getRomajiList();

		List<String> romajiListResult = new ArrayList<String>();

		for (String currentRomaji : romajiList) {
			String informalRomaji = makeInformalPresentNegativeFormForRomaji(currentRomaji, dictionaryEntryType);
			
			romajiListResult.add(informalRomaji);
		}

		result.setRomajiList(romajiListResult);		
		
		return result;
	}
	
	private static String makeInformalPresentNegativeFormForKanjiOrKana(String text, DictionaryEntryType dictionaryEntryType) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			return removeLastChar(text) + "ない";
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			
			boolean aruException = false;
			
			if (text.equals("有る") == true || text.endsWith("有る") == true ||
					text.equals("ある") == true || text.endsWith("ある") == true) {

				aruException = true;
			}
			
			if (aruException == false) {
				
				String lastChar = getLastChars(text, 1);
				
				String postfix = lastKanaCharsMapperToAChar.get(lastChar);
				
				if (postfix == null) {
					throw new RuntimeException("postfix == null: " + text);
				}
				
				return removeLastChar(text) + postfix + "ない";
				
			} else {
				return removeChars(text, 2) + "ない";
			}
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (text.endsWith("来る") == true) {
				return removeChars(text, 2) + "来ない";
				
			} else if (text.endsWith("くる") == true) {
				return removeChars(text, 2) + "こない";
				
			} else if (text.endsWith("する") == true) {
				return removeChars(text, 2) + "しない";
			} else {
				throw new RuntimeException("makeInformalPresentNegativeFormForKanjiOrKana 1: " + text);	
			}
		}
		
		throw new RuntimeException("makeInformalPresentNegativeFormForKanjiOrKana 2: " + dictionaryEntryType);
	}
	
	private static String makeInformalPresentNegativeFormForRomaji(String romaji, DictionaryEntryType dictionaryEntryType) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			
			return removeChars(romaji, 2) + "nai";
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			
			boolean aruException = false;
			
			if (romaji.equals("aru") == true || romaji.endsWith(" aru") == true) {
				aruException = true;
			}
			
			if (aruException == false) {
				
				String romajiLastThreeChars = getLastChars(romaji, 3);
				
				if (romajiLastThreeChars != null && lastRomajiCharsMapperToAChar.containsKey(romajiLastThreeChars) == true) {
					return removeChars(romaji, 3) + lastRomajiCharsMapperToAChar.get(romajiLastThreeChars) + "nai";
				}
				
				String romajiLastTwoChars = getLastChars(romaji, 2);
				
				if (romajiLastTwoChars != null && lastRomajiCharsMapperToAChar.containsKey(romajiLastTwoChars) == true) {
					return removeChars(romaji, 2) + lastRomajiCharsMapperToAChar.get(romajiLastTwoChars) + "nai";
				}
				
				String romajiLastOneChar = getLastChars(romaji, 1);
				
				if (romajiLastOneChar != null && lastRomajiCharsMapperToAChar.containsKey(romajiLastOneChar) == true) {
					return removeChars(romaji, 1) + lastRomajiCharsMapperToAChar.get(romajiLastOneChar) + "nai";
				}
				
				throw new RuntimeException("makeInformalPresentNegativeFormForRomaji 1: " + romaji);

			} else {				
				return removeChars(romaji, 3) + "nai";
			}
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (romaji.endsWith("kuru") == true) {
				return removeChars(romaji, 4) + "konai";
			} else if (romaji.endsWith("suru") == true) {
				return removeChars(romaji, 4) + "shinai";
			} else {
				throw new RuntimeException("makeInformalPresentNegativeFormForRomaji 2: " + romaji);
			}
		} else {
			throw new RuntimeException("makeInformalPresentNegativeFormForRomaji 3: " + dictionaryEntryType);
		}
	}
	
	public static GrammaFormConjugateResult makeInformalPastForm(DictionaryEntry dictionaryEntry) {
		// czas przeszly, twierdzenie, forma nieformalna (prosta)
		
		GrammaFormConjugateResult teForm = makeTeForm(dictionaryEntry);
		
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);

		result.setResultType(GrammaFormConjugateResultType.VERB_INFORMAL_PAST);
		
		String teKanji = teForm.getKanji();
		
		if (teKanji != null) {
			String taKanji = convertTeFormToTaFormForKanjiOrKana(teKanji);
			
			result.setKanji(taKanji);
		}
		
		List<String> teKanaList = teForm.getKanaList();

		List<String> kanaListResult = new ArrayList<String>();

		for (String teCurrentKana : teKanaList) {			
			String taCurrentKana = convertTeFormToTaFormForKanjiOrKana(teCurrentKana);

			kanaListResult.add(taCurrentKana);
		}

		result.setKanaList(kanaListResult);
		
		List<String> teRomajiList = teForm.getRomajiList();

		List<String> romajiListResult = new ArrayList<String>();

		for (String teCurrentRomaji : teRomajiList) {
			String taCurrentRomaji = convertTeFormToTaFormForRomaji(teCurrentRomaji);
			
			romajiListResult.add(taCurrentRomaji);
		}

		result.setRomajiList(romajiListResult);		
		
		return result;
	}
	
	private static String convertTeFormToTaFormForKanjiOrKana(String text) {
		
		String lastChars = getLastChars(text, 1);
		
		String postfix = null;
		
		if (lastChars.equals("て") == true) {
			postfix = "た";
		} else if (lastChars.equals("で") == true) {
			postfix = "だ";
		} else {
			throw new RuntimeException("convertTeFormToTaFormKanjiOrKana 1: " + text);
		}
		
		return removeChars(text, 1) + postfix;
	}

	private static String convertTeFormToTaFormForRomaji(String text) {
		
		String lastChars = getLastChars(text, 1);
		
		String postfix = null;
		
		if (lastChars.equals("e") == true) {
			postfix = "a";
		} else {
			throw new RuntimeException("convertTeFormToTaFormForRomaji 1: " + text);
		}
		
		return removeChars(text, 1) + postfix;
	}
	
	public static GrammaFormConjugateResult makeInformalPastNegativeForm(DictionaryEntry dictionaryEntry) {
		
		// czas przeszly, przeczenie, forma nieformalna (prosta)
		
		GrammaFormConjugateResult informalPresentNegativeForm = makeInformalPresentNegativeForm(dictionaryEntry);
		
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);

		result.setResultType(GrammaFormConjugateResultType.VERB_INFORMAL_PAST_NEGATIVE);
		
		String informalPresentNegativeFormKanji = informalPresentNegativeForm.getKanji();
		
		if (informalPresentNegativeFormKanji != null) {
			result.setKanji(removeLastChar(informalPresentNegativeFormKanji) + "かった");
		}
		
		List<String> informalPresentNegativeFormKanjiKanaList = informalPresentNegativeForm.getKanaList();
		
		List<String> kanaListResult = new ArrayList<String>();
		
		for (String currentInformalPresentNegativeFormKanjiKanaList : informalPresentNegativeFormKanjiKanaList) {
			kanaListResult.add(removeLastChar(currentInformalPresentNegativeFormKanjiKanaList) + "かった");
		}
		
		result.setKanaList(kanaListResult);
		
		List<String> informalPresentNegativeFormKanjiRomajiList = informalPresentNegativeForm.getRomajiList();
		
		List<String> romajiListResult = new ArrayList<String>();
		
		for (String currentInformalPresentNegativeFormKanjiRomajiList : informalPresentNegativeFormKanjiRomajiList) {
			romajiListResult.add(removeLastChar(currentInformalPresentNegativeFormKanjiRomajiList) + "katta");
		}
		
		result.setRomajiList(romajiListResult);
		
		return result;
	}
	
	public static GrammaFormConjugateResult makeStemForm(DictionaryEntry dictionaryEntry) {
		
		// stem

		return makeVerbGrammaConjugateFormalForm(dictionaryEntry, GrammaFormConjugateResultType.VERB_STEM,
				"", "");
	}
	
	public static GrammaFormConjugateResult makeMashouForm(DictionaryEntry dictionaryEntry) {
		
		// mashou
		
		GrammaFormConjugateResult mashouForm = makeVerbGrammaConjugateFormalForm(dictionaryEntry, GrammaFormConjugateResultType.VERB_MASHOU,
				"ましょう", "mashou");
		
		mashouForm.setAlternative(
				makeVerbGrammaConjugateFormalForm(dictionaryEntry, GrammaFormConjugateResultType.VERB_MASHOU,
						"ましょうか", "mashou ka"));
		
		return mashouForm;
	}
	
	private static GrammaFormConjugateResult makePotentialFormInformalPresentForm(DictionaryEntry dictionaryEntry) {
		
		final String ruPostfixKana = "られる";
		final String ruPostfixRomaji = "rareru";
		
		GrammaFormConjugateResult potentialForm = makePotentialForm(dictionaryEntry, ruPostfixKana, ruPostfixRomaji);
		
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		String kanji = dictionaryEntry.getKanji();
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU ||
			(dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR && 
			kanji != null && kanji.endsWith("来る") == true)) {
			
			final String ruPostfixKana2 = "れる";
			final String ruPostfixRomaji2 = "reru";
			
			GrammaFormConjugateResult potentialFormAlternative = makePotentialForm(dictionaryEntry, ruPostfixKana2, ruPostfixRomaji2);
			
			potentialForm.setAlternative(potentialFormAlternative);
		}
		
		return potentialForm;
	}
	
	private static GrammaFormConjugateResult makePotentialFormInformalPresentNegativeForm(GrammaFormConjugateResult potentialFormInformalPresentForm) {
		
		// convert to dictionary entry
		DictionaryEntry potentialFormInformalPresentFormAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(potentialFormInformalPresentForm, DictionaryEntryType.WORD_VERB_RU);
		
		// make form
		GrammaFormConjugateResult potentialFormInformalPresentNegativeForm = makeInformalPresentNegativeForm(potentialFormInformalPresentFormAsDictionaryEntry);
		
		// replace result type
		potentialFormInformalPresentNegativeForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_INFORMAL_PRESENT_NEGATIVE);
		
		GrammaFormConjugateResult alternative = potentialFormInformalPresentForm.getAlternative();
		
		if (alternative != null) {
			
			DictionaryEntry alternativeAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(alternative, DictionaryEntryType.WORD_VERB_RU);
			
			GrammaFormConjugateResult alternativeInformalPresentNegativeForm = makeInformalPresentNegativeForm(alternativeAsDictionaryEntry);
			
			alternativeInformalPresentNegativeForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_INFORMAL_PRESENT_NEGATIVE);
			
			potentialFormInformalPresentNegativeForm.setAlternative(alternativeInformalPresentNegativeForm);
		}
		
		return potentialFormInformalPresentNegativeForm;
	}
	
	private static GrammaFormConjugateResult makePotentialFormInformalPastForm(GrammaFormConjugateResult potentialFormInformalPresentForm) {
		
		// convert to dictionary entry
		DictionaryEntry potentialFormInformalPresentFormAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(potentialFormInformalPresentForm, DictionaryEntryType.WORD_VERB_RU);
		
		// make form
		GrammaFormConjugateResult potentialFormInformalPastForm = makeInformalPastForm(potentialFormInformalPresentFormAsDictionaryEntry);
		
		// replace result type
		potentialFormInformalPastForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_INFORMAL_PAST);
		
		GrammaFormConjugateResult alternative = potentialFormInformalPresentForm.getAlternative();
		
		if (alternative != null) {
			
			DictionaryEntry alternativeAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(alternative, DictionaryEntryType.WORD_VERB_RU);
			
			GrammaFormConjugateResult alternativeInformalPastForm = makeInformalPastForm(alternativeAsDictionaryEntry);
			
			alternativeInformalPastForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_INFORMAL_PAST);
			
			potentialFormInformalPastForm.setAlternative(alternativeInformalPastForm);
		}
		
		return potentialFormInformalPastForm;
	}
	
	private static GrammaFormConjugateResult makePotentialFormInformalPastNegativeForm(GrammaFormConjugateResult potentialFormInformalPresentForm) {
		
		// convert to dictionary entry
		DictionaryEntry potentialFormInformalPresentFormAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(potentialFormInformalPresentForm, DictionaryEntryType.WORD_VERB_RU);
		
		// make form
		GrammaFormConjugateResult potentialFormInformalPastNegativeForm = makeInformalPastNegativeForm(potentialFormInformalPresentFormAsDictionaryEntry);
		
		// replace result type
		potentialFormInformalPastNegativeForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_INFORMAL_PAST_NEGATIVE);
		
		GrammaFormConjugateResult alternative = potentialFormInformalPresentForm.getAlternative();
		
		if (alternative != null) {
			
			DictionaryEntry alternativeAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(alternative, DictionaryEntryType.WORD_VERB_RU);
			
			GrammaFormConjugateResult alternativeInformalPastNegativeForm = makeInformalPastNegativeForm(alternativeAsDictionaryEntry);
			
			alternativeInformalPastNegativeForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_INFORMAL_PAST_NEGATIVE);
			
			potentialFormInformalPastNegativeForm.setAlternative(alternativeInformalPastNegativeForm);
		}
		
		return potentialFormInformalPastNegativeForm;
	}
	
	private static GrammaFormConjugateResult makePotentialFormFormalPresentForm(GrammaFormConjugateResult potentialFormInformalPresentForm) {
		
		// convert to dictionary entry
		DictionaryEntry potentialFormInformalPresentFormAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(potentialFormInformalPresentForm, DictionaryEntryType.WORD_VERB_RU);
		
		// make form
		GrammaFormConjugateResult potentialFormFormalPresentForm = makeFormalPresentForm(potentialFormInformalPresentFormAsDictionaryEntry);
		
		// replace result type
		potentialFormFormalPresentForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_FORMAL_PRESENT);
		
		GrammaFormConjugateResult alternative = potentialFormInformalPresentForm.getAlternative();
		
		if (alternative != null) {
			
			DictionaryEntry alternativeAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(alternative, DictionaryEntryType.WORD_VERB_RU);
			
			GrammaFormConjugateResult alternativeFormalPresentForm = makeFormalPresentForm(alternativeAsDictionaryEntry);
			
			alternativeFormalPresentForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_FORMAL_PRESENT);
			
			potentialFormFormalPresentForm.setAlternative(alternativeFormalPresentForm);
		}
		
		return potentialFormFormalPresentForm;
	}

	private static GrammaFormConjugateResult makePotentialFormFormalPresentNegativeForm(GrammaFormConjugateResult potentialFormInformalPresentForm) {
		
		// convert to dictionary entry
		DictionaryEntry potentialFormInformalPresentNegativeFormAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(potentialFormInformalPresentForm, DictionaryEntryType.WORD_VERB_RU);
		
		// make form
		GrammaFormConjugateResult potentialFormFormalPresentNegativeForm = makeFormalPresentNegativeForm(potentialFormInformalPresentNegativeFormAsDictionaryEntry);
		
		// replace result type
		potentialFormFormalPresentNegativeForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_FORMAL_PRESENT_NEGATIVE);
		
		GrammaFormConjugateResult alternative = potentialFormInformalPresentForm.getAlternative();
		
		if (alternative != null) {
			
			DictionaryEntry alternativeAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(alternative, DictionaryEntryType.WORD_VERB_RU);
			
			GrammaFormConjugateResult alternativeFormalPresentNegativeForm = makeFormalPresentNegativeForm(alternativeAsDictionaryEntry);
			
			alternativeFormalPresentNegativeForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_FORMAL_PRESENT_NEGATIVE);
			
			potentialFormFormalPresentNegativeForm.setAlternative(alternativeFormalPresentNegativeForm);
		}
		
		return potentialFormFormalPresentNegativeForm;
	}

	private static GrammaFormConjugateResult makePotentialFormFormalPastForm(GrammaFormConjugateResult potentialFormInformalPresentForm) {
		
		// convert to dictionary entry
		DictionaryEntry potentialFormInformalPresentNegativeFormAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(potentialFormInformalPresentForm, DictionaryEntryType.WORD_VERB_RU);
		
		// make form
		GrammaFormConjugateResult potentialFormFormalPastForm = makeFormalPastForm(potentialFormInformalPresentNegativeFormAsDictionaryEntry);
		
		// replace result type
		potentialFormFormalPastForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_FORMAL_PAST);
		
		GrammaFormConjugateResult alternative = potentialFormInformalPresentForm.getAlternative();
		
		if (alternative != null) {
			
			DictionaryEntry alternativeAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(alternative, DictionaryEntryType.WORD_VERB_RU);
			
			GrammaFormConjugateResult alternativeFormalPastForm = makeFormalPastForm(alternativeAsDictionaryEntry);
			
			alternativeFormalPastForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_FORMAL_PAST);
			
			potentialFormFormalPastForm.setAlternative(alternativeFormalPastForm);
		}
		
		return potentialFormFormalPastForm;
	}

	private static GrammaFormConjugateResult makePotentialFormFormalPastNegativeForm(GrammaFormConjugateResult potentialFormInformalPresentForm) {
		
		// convert to dictionary entry
		DictionaryEntry potentialFormInformalPresentNegativeFormAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(potentialFormInformalPresentForm, DictionaryEntryType.WORD_VERB_RU);
		
		// make form
		GrammaFormConjugateResult potentialFormFormalPastNegativeForm = makeFormalPastNegativeForm(potentialFormInformalPresentNegativeFormAsDictionaryEntry);
		
		// replace result type
		potentialFormFormalPastNegativeForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_FORMAL_PAST_NEGATIVE);
		
		GrammaFormConjugateResult alternative = potentialFormInformalPresentForm.getAlternative();
		
		if (alternative != null) {
			
			DictionaryEntry alternativeAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(alternative, DictionaryEntryType.WORD_VERB_RU);
			
			GrammaFormConjugateResult alternativeFormalPastNegativeForm = makeFormalPastNegativeForm(alternativeAsDictionaryEntry);
			
			alternativeFormalPastNegativeForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_FORMAL_PAST_NEGATIVE);
			
			potentialFormFormalPastNegativeForm.setAlternative(alternativeFormalPastNegativeForm);
		}
		
		return potentialFormFormalPastNegativeForm;
	}
	
	private static GrammaFormConjugateResult makePotentialForm(DictionaryEntry dictionaryEntry, String ruPostfixKana, String ruPostfixRomaji) {
		
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);
		
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		
		result.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_INFORMAL_PRESENT);
		
		String prefixKana = dictionaryEntry.getPrefixKana();
		
		if (prefixKana != null && prefixKana.equals("を") == true) {
			result.setPrefixKana("が");
		}
		
		String kanji = dictionaryEntry.getKanji();
		
		if (kanji != null) { 
			kanji = kanji.replaceAll("を", "が");
			
			result.setKanji(makePotentialFormForKanjiOrKana(kanji, dictionaryEntryType, ruPostfixKana));
		}
		
		List<String> kanaList = dictionaryEntry.getKanaList();
			
		List<String> kanaListResult = new ArrayList<String>();
		
		for (String currentKanaList : kanaList) {
			
			currentKanaList = currentKanaList.replaceAll("を", "が");
			
			kanaListResult.add(makePotentialFormForKanjiOrKana(currentKanaList, dictionaryEntryType, ruPostfixKana));
		}
		
		result.setKanaList(kanaListResult);
				
		if (prefixKana != null && prefixKana.equals("を") == true) {
			result.setPrefixRomaji("ga");
		}
		
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		List<String> romajiListResult = new ArrayList<String>();
		
		for (String currentRomajiList : romajiList) {
			currentRomajiList = currentRomajiList.replaceAll(" o ", " ga ");
			
			romajiListResult.add(makePotentialFormForRomaji(currentRomajiList, dictionaryEntryType, ruPostfixRomaji));
		}
		
		result.setRomajiList(romajiListResult);
		
		return result;
	}
	
	private static GrammaFormConjugateResult makePotentialTeForm(GrammaFormConjugateResult potentialFormInformalPresentForm) {
		
		// convert to dictionary entry
		DictionaryEntry potentialFormInformalPresentFormAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(potentialFormInformalPresentForm, DictionaryEntryType.WORD_VERB_RU);
		
		// make form
		GrammaFormConjugateResult potentialTeForm = makeTeForm(potentialFormInformalPresentFormAsDictionaryEntry);
		
		// replace result type
		potentialTeForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_TE);
		
		GrammaFormConjugateResult alternative = potentialFormInformalPresentForm.getAlternative();
		
		if (alternative != null) {
			
			DictionaryEntry alternativeAsDictionaryEntry = convertGrammaFormConjugateResultToDictionaryEntry(alternative, DictionaryEntryType.WORD_VERB_RU);
			
			GrammaFormConjugateResult alternativeTeForm = makeTeForm(alternativeAsDictionaryEntry);
			
			alternativeTeForm.setResultType(GrammaFormConjugateResultType.VERB_POTENTIAL_TE);
			
			potentialTeForm.setAlternative(alternativeTeForm);
		}
		
		return potentialTeForm;
	}

	private static String makePotentialFormForKanjiOrKana(String kana, DictionaryEntryType dictionaryEntryType, String ruPostfixKana) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			return removeLastChar(kana) + ruPostfixKana;
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			
			String kanaWithoutLastChar = removeLastChar(kana);
			String lastChar = getLastChars(kana, 1);
			
			String lastCharMappedToE = lastKanaCharsMapperToEChar.get(lastChar);
			
			if (lastCharMappedToE == null) {
				throw new RuntimeException("lastCharMappedToE == null: " + kana);
			}
			
			return kanaWithoutLastChar + lastCharMappedToE + "る";
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (kana.endsWith("来る") == true) {
				return removeChars(kana, 1) + ruPostfixKana;
				
			} else if (kana.endsWith("くる") == true) {
				return removeChars(kana, 2) + "こ" + ruPostfixKana;
				
			} else if (kana.endsWith("する") == true) {
				return removeChars(kana, 2) + "できる";
			} else {
				throw new RuntimeException("makePotentialFormForKanjiOrKana 1: " + kana);	
			}
		}
		
		throw new RuntimeException("makePotentialFormForKanjiOrKana 2: " + dictionaryEntryType);
	}
	
	private static String makePotentialFormForRomaji(String romaji, DictionaryEntryType dictionaryEntryType, String ruPostfixRomaji) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			return removeChars(romaji, 2) + ruPostfixRomaji;
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			
			String romajiLastThreeChars = getLastChars(romaji, 3);
			
			if (romajiLastThreeChars != null && lastRomajiCharsMapperToEChar.containsKey(romajiLastThreeChars) == true) {
				return removeChars(romaji, 3) + lastRomajiCharsMapperToEChar.get(romajiLastThreeChars) + "ru";
			}
			
			String romajiLastTwoChars = getLastChars(romaji, 2);
			
			if (romajiLastTwoChars != null && lastRomajiCharsMapperToEChar.containsKey(romajiLastTwoChars) == true) {
				return removeChars(romaji, 2) + lastRomajiCharsMapperToEChar.get(romajiLastTwoChars) + "ru";
			}
			
			String romajiLastOneChar = getLastChars(romaji, 1);
			
			if (romajiLastOneChar != null && lastRomajiCharsMapperToEChar.containsKey(romajiLastOneChar) == true) {
				return removeChars(romaji, 1) + lastRomajiCharsMapperToEChar.get(romajiLastOneChar) + "ru";
			}
			
			throw new RuntimeException("makePotentialFormForRomaji 1: " + romaji);
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (romaji.endsWith("kuru") == true) {
				return removeChars(romaji, 4) + "ko" + ruPostfixRomaji;
			} else if (romaji.endsWith("suru") == true) {
				return removeChars(romaji, 4) + "dekiru";
			} else {
				throw new RuntimeException("makePotentialFormForRomaji 2: " + romaji);
			}
		}
		
		throw new RuntimeException("makePotentialFormForRomaji 3: " + dictionaryEntryType);
	}
	
	public static GrammaFormConjugateResult makeVolitionalForm(DictionaryEntry dictionaryEntry) {
				
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);
		
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		
		result.setResultType(GrammaFormConjugateResultType.VERB_VOLITIONAL);
		
		String kanji = dictionaryEntry.getKanji();
		
		if (kanji != null) {
			result.setKanji(makeVolitionalFormForKanjiOrKana(kanji, dictionaryEntryType));
		}
		
		List<String> kanaList = dictionaryEntry.getKanaList();
		
		List<String> kanaListResult = new ArrayList<String>();
		
		for (String currentKanaList : kanaList) {
			kanaListResult.add(makeVolitionalFormForKanjiOrKana(currentKanaList, dictionaryEntryType));
		}
		
		result.setKanaList(kanaListResult);
		
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		List<String> romajiListResult = new ArrayList<String>();
		
		for (String currentRomajiList : romajiList) {
			romajiListResult.add(makeVolitionalFormForRomaji(currentRomajiList, dictionaryEntryType));
		}
		
		result.setRomajiList(romajiListResult);
		
		return result;
	}
	
	private static String makeVolitionalFormForKanjiOrKana(String kana, DictionaryEntryType dictionaryEntryType) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			return removeLastChar(kana) + "よう";
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			
			String kanaWithoutLastChar = removeLastChar(kana);
			String lastChar = getLastChars(kana, 1);
			
			String lastCharMappedToYou = lastKanaCharsMapperToCharForVolitionalForm.get(lastChar);
			
			if (lastCharMappedToYou == null) {
				throw new RuntimeException("lastCharMappedToYou == null: " + kana);
			}
			
			return kanaWithoutLastChar + lastCharMappedToYou;
			
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (kana.endsWith("来る") == true) {
				return removeChars(kana, 1) + "よう";
				
			} else if (kana.endsWith("くる") == true) {
				return removeChars(kana, 2) + "こ" + "よう";
				
			} else if (kana.endsWith("する") == true) {
				return removeChars(kana, 2) + "しよう";
			} else {
				throw new RuntimeException("makeVolitionalFormForKanjiOrKana 1: " + kana);	
			}
		}
		
		throw new RuntimeException("makeVolitionalFormForKanjiOrKana 2: " + dictionaryEntryType);
	}
	
	private static String makeVolitionalFormForRomaji(String romaji, DictionaryEntryType dictionaryEntryType) {
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU) {
			return removeChars(romaji, 2) + "you";
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U) {
			
			String romajiLastThreeChars = getLastChars(romaji, 3);
			
			if (romajiLastThreeChars != null && lastRomajiCharsMapperToCharForVolitionalForm.containsKey(romajiLastThreeChars) == true) {
				return removeChars(romaji, 3) + lastRomajiCharsMapperToCharForVolitionalForm.get(romajiLastThreeChars);
			}
			
			String romajiLastTwoChars = getLastChars(romaji, 2);
			
			if (romajiLastTwoChars != null && lastRomajiCharsMapperToCharForVolitionalForm.containsKey(romajiLastTwoChars) == true) {
				return removeChars(romaji, 2) + lastRomajiCharsMapperToCharForVolitionalForm.get(romajiLastTwoChars);
			}
			
			String romajiLastOneChar = getLastChars(romaji, 1);
			
			if (romajiLastOneChar != null && lastRomajiCharsMapperToCharForVolitionalForm.containsKey(romajiLastOneChar) == true) {
				return removeChars(romaji, 1) + lastRomajiCharsMapperToCharForVolitionalForm.get(romajiLastOneChar);
			}
			
			throw new RuntimeException("makeVolitionalFormForRomaji 1: " + romaji);
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			if (romaji.endsWith("kuru") == true) {
				return removeChars(romaji, 4) + "ko" + "you";
			} else if (romaji.endsWith("suru") == true) {
				return removeChars(romaji, 4) + "shiyou";
			} else {
				throw new RuntimeException("makeVolitionalFormForRomaji 2: " + romaji);
			}
		}
		
		throw new RuntimeException("makeVolitionalFormForRomaji 3: " + dictionaryEntryType);
	}
	
	private static DictionaryEntry convertGrammaFormConjugateResultToDictionaryEntry(GrammaFormConjugateResult grammaFormConjugateResult, DictionaryEntryType dictionaryEntryType) {
		
		DictionaryEntry dictionaryEntry = new DictionaryEntry();
		
		dictionaryEntry.setDictionaryEntryType(dictionaryEntryType);
		dictionaryEntry.setPrefixKana(grammaFormConjugateResult.getPrefixKana());
		dictionaryEntry.setKanji(grammaFormConjugateResult.getKanji());
		dictionaryEntry.setKanaList(grammaFormConjugateResult.getKanaList());
		dictionaryEntry.setPrefixRomaji(grammaFormConjugateResult.getPrefixRomaji());
		dictionaryEntry.setRomajiList(grammaFormConjugateResult.getRomajiList());
		
		return dictionaryEntry;		
	}
}
