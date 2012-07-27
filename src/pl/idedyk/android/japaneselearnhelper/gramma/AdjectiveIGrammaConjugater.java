package pl.idedyk.android.japaneselearnhelper.gramma;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;

public class AdjectiveIGrammaConjugater {
	
	public static List<GrammaFormConjugateGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry) {
		
		List<GrammaFormConjugateGroupTypeElements> result = new ArrayList<GrammaFormConjugateGroupTypeElements>();
		
		// FIXME: formal
		
		// forma nieformalna (prosta)
		GrammaFormConjugateGroupTypeElements informal = new GrammaFormConjugateGroupTypeElements();
		
		informal.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.ADJECTIVE_I_INFORMAL);
		
		informal.getGrammaFormConjugateResults().add(makeInformalPresentForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPresentNegativeForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPastForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPastNegativeForm(dictionaryEntry));
		
		result.add(informal);
		
		return result;		
	}

	public static GrammaFormConjugateResult makeInformalPresentForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, twierdzenie, forma nieformalna (prosta), -katta
		
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);

		result.setResultType(GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT);
		
		@SuppressWarnings("unused") final String postfixKana = "い";
		@SuppressWarnings("unused") final String postfixRomaji = "i";
		
		result.setKanji(dictionaryEntry.getKanji());
		result.setKanaList(dictionaryEntry.getKanaList());
		result.setRomajiList(dictionaryEntry.getRomajiList());
		
		return result;
	}
	
	public static GrammaFormConjugateResult makeInformalPresentNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma nieformalna (prosta), -katta
		
		final String postfixKana = "くない";
		final String postfixRomaji = "kunai";
		
		return makeInformalForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE,
				postfixKana, postfixRomaji);
	}
	
	public static GrammaFormConjugateResult makeInformalPastForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, twierdzenie, forma nieformalna (prosta), -katta
		
		final String postfixKana = "かった";
		final String postfixRomaji = "katta";
		
		return makeInformalForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PAST,
				postfixKana, postfixRomaji);
	}

	public static GrammaFormConjugateResult makeInformalPastNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, przeczenie, forma nieformalna (prosta), -ku nakatta
		
		final String postfixKana = "くなかった";
		final String postfixRomaji = "kunakatta";
		
		return makeInformalForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PAST_NEGATIVE,
				postfixKana, postfixRomaji);
	}
	
	private static GrammaFormConjugateResult makeInformalForm(DictionaryEntry dictionaryEntry, 
			GrammaFormConjugateResultType grammaFormConjugateResultType,String postfixKana, String postfixRomaji) {
		
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);
		
		result.setResultType(grammaFormConjugateResultType);
		
		String kanji = dictionaryEntry.getKanji();
		
		if (kanji != null) {
			result.setKanji(removeLastChar(kanji) + postfixKana);
		}
		
		List<String> kanaList = dictionaryEntry.getKanaList();
		
		List<String> kanaListResult = new ArrayList<String>();
		
		for (String currentKana : kanaList) {
			
			if (currentKana.equals("いい") == true) {
				currentKana = "よい";
			}
			
			kanaListResult.add(removeLastChar(currentKana) + postfixKana);
		}
		
		result.setKanaList(kanaListResult);		
		
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		List<String> romajiListResult = new ArrayList<String>();
		
		for (String currentRomaji : romajiList) {
			
			if (currentRomaji.equals("ii") == true) {
				currentRomaji = "yoi";
			}

			romajiListResult.add(removeLastChar(currentRomaji) + postfixRomaji);
		}
		
		result.setRomajiList(romajiListResult);
		
		// http://japanese.about.com/library/weekly/aa040101b.htm
		
		return result; 
	}
	
	private static GrammaFormConjugateResult makeCommon(DictionaryEntry dictionaryEntry) {
		
		// validate DictionaryEntry
		validateDictionaryEntry(dictionaryEntry);
		
		// create result
		GrammaFormConjugateResult result = new GrammaFormConjugateResult();
		
		return result;
	}
	
	private static String removeLastChar(String text) {
		return text.substring(0, text.length() - 1);
	}
	
	private static void  validateDictionaryEntry(DictionaryEntry dictionaryEntry) {
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		
		if (dictionaryEntryType != DictionaryEntryType.WORD_ADJECTIVE_I) {
			throw new RuntimeException("dictionaryEntryType != DictionaryEntryType.WORD_ADJECTIVE_I");
		}
		
		String kanji = dictionaryEntry.getKanji();
		
		if (kanji != null && kanji.endsWith("い") == false) {
			throw new RuntimeException("kanji.endsWith(い) == false");
		}
		
		List<String> kanaList = dictionaryEntry.getKanaList();
		
		for (String currentKana : kanaList) {
			if (currentKana.endsWith("い") == false) {
				throw new RuntimeException("currentKana.endsWith(い) == false");
			}			
		}
		
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		for (String currentRomaji : romajiList) {
			if (currentRomaji.endsWith("i") == false) {
				throw new RuntimeException("currentRomaji.endsWith(i) == false");
			}
		}		
	}
}
