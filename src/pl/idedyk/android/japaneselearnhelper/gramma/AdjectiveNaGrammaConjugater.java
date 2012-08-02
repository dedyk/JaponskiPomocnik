package pl.idedyk.android.japaneselearnhelper.gramma;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;

public class AdjectiveNaGrammaConjugater {

	public static List<GrammaFormConjugateGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry) {

		List<GrammaFormConjugateGroupTypeElements> result = new ArrayList<GrammaFormConjugateGroupTypeElements>();

		// forma formalna
		GrammaFormConjugateGroupTypeElements formal = new GrammaFormConjugateGroupTypeElements();

		formal.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.ADJECTIVE_NA_FORMAL);
		
		formal.getGrammaFormConjugateResults().add(makeFormalPresentForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPresentNegativeForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPastForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPastNegativeForm(dictionaryEntry));
		
		result.add(formal);
		
		// forma nieformalna (prosta)
		GrammaFormConjugateGroupTypeElements informal = new GrammaFormConjugateGroupTypeElements();

		informal.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.ADJECTIVE_NA_INFORMAL);

		informal.getGrammaFormConjugateResults().add(makeInformalPresentForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPresentNegativeForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPastForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPastNegativeForm(dictionaryEntry));

		result.add(informal);
		
		// forma te
		GrammaFormConjugateGroupTypeElements teForm = new GrammaFormConjugateGroupTypeElements();
		
		teForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.ADJECTIVE_NA_TE);
		
		teForm.getGrammaFormConjugateResults().add(makeTeForm(dictionaryEntry));
		
		result.add(teForm);
		
		return result;
	}

	public static GrammaFormConjugateResult makeFormalPresentForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, twierdzenie, forma formalna, -desu

		final String postfixKana = "です";
		final String postfixRomaji = " desu";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_FORMAL_PRESENT,
				postfixKana, postfixRomaji);
	}

	public static GrammaFormConjugateResult makeFormalPresentNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma formalna (prosta), -dewa arimasen

		final String postfixKana = "でわありません";
		final String postfixRomaji = " dewa arimasen";

		GrammaFormConjugateResult grammaFormConjugateResult = makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_FORMAL_PRESENT_NEGATIVE,
				postfixKana, postfixRomaji);
		
		// alternative
		grammaFormConjugateResult.setAlternative(makeFormalPresentNegativeForm2(dictionaryEntry));
		
		return grammaFormConjugateResult;
	}

	private static GrammaFormConjugateResult makeFormalPresentNegativeForm2(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma formalna (prosta), -ja arimasen

		final String postfixKana = "じゃありません";
		final String postfixRomaji = " ja arimasen";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_FORMAL_PRESENT_NEGATIVE,
				postfixKana, postfixRomaji);
	}
	
	public static GrammaFormConjugateResult makeFormalPastForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, twierdzenie, forma formalna, -deshita

		final String postfixKana = "でした";
		final String postfixRomaji = " deshita";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_FORMAL_PAST,
				postfixKana, postfixRomaji);
	}
	
	public static GrammaFormConjugateResult makeFormalPastNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, przeczenie, forma formalna, -dewa arimasen deshita

		final String postfixKana = "でわありませんでした";
		final String postfixRomaji = " dewa arimasen deshita";

		GrammaFormConjugateResult grammaFormConjugateResult = makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_FORMAL_PAST_NEGATIVE,
				postfixKana, postfixRomaji);
		
		// alternative
		grammaFormConjugateResult.setAlternative(makeFormalPastNegativeForm2(dictionaryEntry));
		
		return grammaFormConjugateResult;
	}
	
	private static GrammaFormConjugateResult makeFormalPastNegativeForm2(DictionaryEntry dictionaryEntry) {
		// czas przesly, przeczenie, forma formalna, -ja arimasen deshita
		
		final String postfixKana = "じゃありませんでした";
		final String postfixRomaji = " ja arimasen deshita";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_FORMAL_PAST_NEGATIVE,
				postfixKana, postfixRomaji);
	}
	
	public static GrammaFormConjugateResult makeInformalPresentForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, twierdzenie, forma nieformalna (prosta), -da

		final String postfixKana = "だ";
		final String postfixRomaji = " da";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_INFORMAL_PRESENT,
				postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeInformalPresentNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma nieformalna (prosta), -ja nai

		final String postfixKana = "じゃない";
		final String postfixRomaji = " ja nai";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_INFORMAL_PRESENT_NEGATIVE,
				postfixKana, postfixRomaji);
	}
	
	public static GrammaFormConjugateResult makeInformalPastForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, twierdzenie, forma nieformalna (prosta), -datta

		final String postfixKana = "だった";
		final String postfixRomaji = " datta";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_INFORMAL_PAST,
				postfixKana, postfixRomaji);
	}
		
	private static GrammaFormConjugateResult makeInformalPastNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, przeczenie, forma nieformalna (prosta), -ja nakatta
		
		final String postfixKana = "じゃなかった";
		final String postfixRomaji = " ja nakatta";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_INFORMAL_PAST_NEGATIVE,
				postfixKana, postfixRomaji);
	}
	
	private static GrammaFormConjugateResult makeAdjectiveGrammaConjugateForm(DictionaryEntry dictionaryEntry, 
			GrammaFormConjugateResultType grammaFormConjugateResultType, String postfixKana, String postfixRomaji) {

		// make common
		GrammaFormConjugateResult result = makeCommon(dictionaryEntry);

		result.setResultType(grammaFormConjugateResultType);

		String kanji = dictionaryEntry.getKanji();

		if (kanji != null) {		
			result.setKanji(kanji + postfixKana);
		}

		List<String> kanaList = dictionaryEntry.getKanaList();

		List<String> kanaListResult = new ArrayList<String>();

		for (String currentKana : kanaList) {			
			kanaListResult.add(currentKana + postfixKana);
		}

		result.setKanaList(kanaListResult);		

		List<String> romajiList = dictionaryEntry.getRomajiList();

		List<String> romajiListResult = new ArrayList<String>();

		for (String currentRomaji : romajiList) {
			romajiListResult.add(currentRomaji + postfixRomaji);
		}

		result.setRomajiList(romajiListResult);

		return result; 
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

		if (dictionaryEntryType != DictionaryEntryType.WORD_ADJECTIVE_NA) {
			throw new RuntimeException("dictionaryEntryType != DictionaryEntryType.WORD_ADJECTIVE_NA");
		}		
	}
	
	private static GrammaFormConjugateResult makeTeForm(DictionaryEntry dictionaryEntry) {
		// forma te
		
		final String postfixKana = "で";
		final String postfixRomaji = " de";

		return makeAdjectiveGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.ADJECTIVE_NA_TE,
				postfixKana, postfixRomaji);
	}
}
