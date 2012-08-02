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

		// forma formalna
		GrammaFormConjugateGroupTypeElements formal = new GrammaFormConjugateGroupTypeElements();

		formal.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.ADJECTIVE_I_FORMAL);

		formal.getGrammaFormConjugateResults().add(makeFormalPresentForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPresentNegativeForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPastForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPastNegativeForm(dictionaryEntry));

		result.add(formal);

		// forma nieformalna (prosta)
		GrammaFormConjugateGroupTypeElements informal = new GrammaFormConjugateGroupTypeElements();

		informal.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.ADJECTIVE_I_INFORMAL);

		informal.getGrammaFormConjugateResults().add(makeInformalPresentForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPresentNegativeForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPastForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPastNegativeForm(dictionaryEntry));

		result.add(informal);
		
		// forma te
		GrammaFormConjugateGroupTypeElements teForm = new GrammaFormConjugateGroupTypeElements();
		
		teForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.ADJECTIVE_I_TE);
		
		teForm.getGrammaFormConjugateResults().add(makeTeForm(dictionaryEntry));
		
		result.add(teForm);

		return result;		
	}

	public static GrammaFormConjugateResult makeFormalPresentForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, twierdzenie, forma formalna, -i desu

		final String postfixKana = "いです";
		final String postfixRomaji = "i desu";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_FORMAL_PRESENT,
				postfixKana, postfixRomaji);
	}

	public static GrammaFormConjugateResult makeFormalPresentNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma formalna (prosta), -kunai desu

		final String postfixKana = "くないです";
		final String postfixRomaji = "kunai desu";

		GrammaFormConjugateResult grammaFormConjugateResult = makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_FORMAL_PRESENT_NEGATIVE,
				postfixKana, postfixRomaji);
		
		// alternative
		grammaFormConjugateResult.setAlternative(makeFormalPresentNegativeForm2(dictionaryEntry));
		
		return grammaFormConjugateResult;
	}

	private static GrammaFormConjugateResult makeFormalPresentNegativeForm2(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma formalna (prosta), -ku arimasen

		final String postfixKana = "くありません";
		final String postfixRomaji = "ku arimasen";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_FORMAL_PRESENT_NEGATIVE,
				postfixKana, postfixRomaji);
	}
	
	public static GrammaFormConjugateResult makeFormalPastForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, twierdzenie, forma formalna, -katta desu

		final String postfixKana = "かったです";
		final String postfixRomaji = "katta desu";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_FORMAL_PAST,
				postfixKana, postfixRomaji);
	}

	public static GrammaFormConjugateResult makeFormalPastNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, przeczenie, forma formalna, -ku nakatta desu

		final String postfixKana = "くなかったです";
		final String postfixRomaji = "kunakatta desu";

		GrammaFormConjugateResult grammaFormConjugateResult = makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_FORMAL_PAST_NEGATIVE,
				postfixKana, postfixRomaji);
		
		// alternative
		grammaFormConjugateResult.setAlternative(makeFormalPastNegativeForm2(dictionaryEntry));
		
		return grammaFormConjugateResult;
	}

	private static GrammaFormConjugateResult makeFormalPastNegativeForm2(DictionaryEntry dictionaryEntry) {
		// czas przesly, przeczenie, forma formalna, -ku arimasen deshita
		
		final String postfixKana = "くありませんでした";
		final String postfixRomaji = "ku arimasen deshita";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_FORMAL_PAST_NEGATIVE,
				postfixKana, postfixRomaji);
	}
	
	public static GrammaFormConjugateResult makeInformalPresentForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, twierdzenie, forma nieformalna (prosta), -i

		final String postfixKana = "い";
		final String postfixRomaji = "i";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT,
				postfixKana, postfixRomaji);
	}

	public static GrammaFormConjugateResult makeInformalPresentNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma nieformalna (prosta), -kunai

		final String postfixKana = "くない";
		final String postfixRomaji = "kunai";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE,
				postfixKana, postfixRomaji);
	}

	public static GrammaFormConjugateResult makeInformalPastForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, twierdzenie, forma nieformalna (prosta), -katta

		final String postfixKana = "かった";
		final String postfixRomaji = "katta";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PAST,
				postfixKana, postfixRomaji);
	}

	public static GrammaFormConjugateResult makeInformalPastNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, przeczenie, forma nieformalna (prosta), -ku nakatta

		final String postfixKana = "くなかった";
		final String postfixRomaji = "kunakatta";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PAST_NEGATIVE,
				postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeAdjectiveGrammaConjugateForm(DictionaryEntry dictionaryEntry, 
			GrammaFormConjugateResultType grammaFormConjugateResultType, String postfixKana, String postfixRomaji) {

		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);

		result.setResultType(grammaFormConjugateResultType);

		String kanji = dictionaryEntry.getKanji();

		if (kanji != null) {
			kanji = getKanaToConjugate(kanji, grammaFormConjugateResultType);
			
			result.setKanji(removeLastChar(kanji) + postfixKana);
		}

		List<String> kanaList = dictionaryEntry.getKanaList();

		List<String> kanaListResult = new ArrayList<String>();

		for (String currentKana : kanaList) {			
			currentKana = getKanaToConjugate(currentKana, grammaFormConjugateResultType);

			kanaListResult.add(removeLastChar(currentKana) + postfixKana);
		}

		result.setKanaList(kanaListResult);		

		List<String> romajiList = dictionaryEntry.getRomajiList();

		List<String> romajiListResult = new ArrayList<String>();

		for (String currentRomaji : romajiList) {
			currentRomaji = getRomajiToConjugate(currentRomaji, grammaFormConjugateResultType);

			romajiListResult.add(removeLastChar(currentRomaji) + postfixRomaji);
		}

		result.setRomajiList(romajiListResult);

		return result; 
	}

	private static String getKanaToConjugate(String kana, GrammaFormConjugateResultType grammaFormConjugateResultType) {

		if (grammaFormConjugateResultType != GrammaFormConjugateResultType.ADJECTIVE_I_FORMAL_PRESENT && 
				grammaFormConjugateResultType != GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT) {

			if (kana.endsWith("いい") == true) {
				return kana.substring(0, kana.length() - 2) + "よい";
			}
		}

		return kana;
	}

	private static String getRomajiToConjugate(String romaji, GrammaFormConjugateResultType grammaFormConjugateResultType) {

		if (grammaFormConjugateResultType != GrammaFormConjugateResultType.ADJECTIVE_I_FORMAL_PRESENT && 
				grammaFormConjugateResultType != GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT) {

			if (romaji.equals("ii") == true || romaji.endsWith(" ii") == true) {
				return romaji.substring(0, romaji.length() - 2) + "yoi";
			}
		}

		return romaji;
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
	
	private static GrammaFormConjugateResult makeTeForm(DictionaryEntry dictionaryEntry) {
		// forma te
		
		String postfixKana = "くて";
		String postfixRomaji = "kute";
		
		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);
		
		result.setResultType(GrammaFormConjugateResultType.ADJECTIVE_I_TE);
		
		String kanji = dictionaryEntry.getKanji();

		if (kanji != null) {
			kanji = getKanaToConjugate(kanji, GrammaFormConjugateResultType.ADJECTIVE_I_TE);
			
			result.setKanji(removeLastChar(kanji) + postfixKana);
		}

		List<String> kanaList = dictionaryEntry.getKanaList();

		List<String> kanaListResult = new ArrayList<String>();

		for (String currentKana : kanaList) {			
			currentKana = getKanaToConjugate(currentKana, GrammaFormConjugateResultType.ADJECTIVE_I_TE);

			kanaListResult.add(removeLastChar(currentKana) + postfixKana);
		}

		result.setKanaList(kanaListResult);		

		List<String> romajiList = dictionaryEntry.getRomajiList();

		List<String> romajiListResult = new ArrayList<String>();

		for (String currentRomaji : romajiList) {
			currentRomaji = getRomajiToConjugate(currentRomaji, GrammaFormConjugateResultType.ADJECTIVE_I_TE);

			romajiListResult.add(removeLastChar(currentRomaji) + postfixRomaji);
		}

		result.setRomajiList(romajiListResult);		
		
		return result;
	}
}
