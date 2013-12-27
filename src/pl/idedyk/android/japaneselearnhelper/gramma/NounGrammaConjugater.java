package pl.idedyk.android.japaneselearnhelper.gramma;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;

public class NounGrammaConjugater {

	public static List<GrammaFormConjugateGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {

		// validate DictionaryEntry
		validateDictionaryEntry(dictionaryEntry);

		List<GrammaFormConjugateGroupTypeElements> result = new ArrayList<GrammaFormConjugateGroupTypeElements>();

		// forma formalna
		GrammaFormConjugateGroupTypeElements formal = new GrammaFormConjugateGroupTypeElements();

		formal.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.NOUN_FORMAL);

		formal.getGrammaFormConjugateResults().add(makeFormalPresentForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPresentNegativeForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPastForm(dictionaryEntry));
		formal.getGrammaFormConjugateResults().add(makeFormalPastNegativeForm(dictionaryEntry));

		result.add(formal);

		// forma nieformalna (prosta)
		GrammaFormConjugateGroupTypeElements informal = new GrammaFormConjugateGroupTypeElements();

		informal.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.NOUN_INFORMAL);

		informal.getGrammaFormConjugateResults().add(makeInformalPresentForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPresentNegativeForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPastForm(dictionaryEntry));
		informal.getGrammaFormConjugateResults().add(makeInformalPastNegativeForm(dictionaryEntry));

		result.add(informal);

		// forma te
		GrammaFormConjugateGroupTypeElements teForm = new GrammaFormConjugateGroupTypeElements();

		teForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.NOUN_TE);

		teForm.getGrammaFormConjugateResults().add(makeTeForm(dictionaryEntry));

		result.add(teForm);

		// forma honoryfikatywna
		GrammaFormConjugateGroupTypeElements keigoForm = new GrammaFormConjugateGroupTypeElements();

		keigoForm.setGrammaFormConjugateGroupType(GrammaFormConjugateGroupType.NOUN_KEIGO);

		keigoForm.getGrammaFormConjugateResults().add(makeKeigoLowForm(dictionaryEntry));

		result.add(keigoForm);

		// caching
		for (GrammaFormConjugateGroupTypeElements grammaFormConjugateGroupTypeElements : result) {

			List<GrammaFormConjugateResult> grammaFormConjugateResults = grammaFormConjugateGroupTypeElements
					.getGrammaFormConjugateResults();

			for (GrammaFormConjugateResult grammaFormConjugateResult : grammaFormConjugateResults) {
				grammaFormCache.put(grammaFormConjugateResult.getResultType(), grammaFormConjugateResult);
			}
		}

		return result;
	}

	private static GrammaFormConjugateResult makeFormalPresentForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, twierdzenie, forma formalna, -desu

		final String postfixKana = "です";
		final String postfixRomaji = " desu";

		return makeNounGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.NOUN_FORMAL_PRESENT,
				postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeFormalPresentNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma formalna (prosta), -dewa arimasen

		final String postfixKana = "でわありません";
		final String postfixRomaji = " dewa arimasen";

		GrammaFormConjugateResult grammaFormConjugateResult = makeNounGrammaConjugateForm(dictionaryEntry,
				GrammaFormConjugateResultType.NOUN_FORMAL_PRESENT_NEGATIVE, postfixKana, postfixRomaji);

		// alternative
		grammaFormConjugateResult.setAlternative(makeFormalPresentNegativeForm2(dictionaryEntry));

		return grammaFormConjugateResult;
	}

	private static GrammaFormConjugateResult makeFormalPresentNegativeForm2(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma formalna (prosta), -ja arimasen

		final String postfixKana = "じゃありません";
		final String postfixRomaji = " ja arimasen";

		return makeNounGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.NOUN_FORMAL_PRESENT_NEGATIVE,
				postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeFormalPastForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, twierdzenie, forma formalna, -deshita

		final String postfixKana = "でした";
		final String postfixRomaji = " deshita";

		return makeNounGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.NOUN_FORMAL_PAST,
				postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeFormalPastNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, przeczenie, forma formalna, -dewa arimasen deshita

		final String postfixKana = "でわありませんでした";
		final String postfixRomaji = " dewa arimasen deshita";

		GrammaFormConjugateResult grammaFormConjugateResult = makeNounGrammaConjugateForm(dictionaryEntry,
				GrammaFormConjugateResultType.NOUN_FORMAL_PAST_NEGATIVE, postfixKana, postfixRomaji);

		// alternative
		grammaFormConjugateResult.setAlternative(makeFormalPastNegativeForm2(dictionaryEntry));

		return grammaFormConjugateResult;
	}

	private static GrammaFormConjugateResult makeFormalPastNegativeForm2(DictionaryEntry dictionaryEntry) {
		// czas przesly, przeczenie, forma formalna, -ja arimasen deshita

		final String postfixKana = "じゃありませんでした";
		final String postfixRomaji = " ja arimasen deshita";

		return makeNounGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.NOUN_FORMAL_PAST_NEGATIVE,
				postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeInformalPresentForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, twierdzenie, forma nieformalna (prosta), -da

		final String postfixKana = "だ";
		final String postfixRomaji = " da";

		return makeNounGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.NOUN_INFORMAL_PRESENT,
				postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeInformalPresentNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas terazniejszy, przeczenie, forma nieformalna (prosta), -ja nai

		final String postfixKana = "じゃない";
		final String postfixRomaji = " ja nai";

		return makeNounGrammaConjugateForm(dictionaryEntry,
				GrammaFormConjugateResultType.NOUN_INFORMAL_PRESENT_NEGATIVE, postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeInformalPastForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, twierdzenie, forma nieformalna (prosta), -datta

		final String postfixKana = "だった";
		final String postfixRomaji = " datta";

		return makeNounGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.NOUN_INFORMAL_PAST,
				postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeInformalPastNegativeForm(DictionaryEntry dictionaryEntry) {
		// czas przesly, przeczenie, forma nieformalna (prosta), -ja nakatta

		final String postfixKana = "じゃなかった";
		final String postfixRomaji = " ja nakatta";

		return makeNounGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.NOUN_INFORMAL_PAST_NEGATIVE,
				postfixKana, postfixRomaji);
	}

	private static GrammaFormConjugateResult makeNounGrammaConjugateForm(DictionaryEntry dictionaryEntry,
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

		// create result
		GrammaFormConjugateResult result = new GrammaFormConjugateResult();

		result.setPrefixKana(dictionaryEntry.getPrefixKana());
		result.setPrefixRomaji(dictionaryEntry.getPrefixRomaji());

		return result;
	}

	private static void validateDictionaryEntry(DictionaryEntry dictionaryEntry) {

		boolean isAdjectiveNounResult = dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_NOUN);

		if (isAdjectiveNounResult == false) {
			throw new RuntimeException("dictionaryEntryType != DictionaryEntryType.WORD_NOUN: "
					+ dictionaryEntry.getDictionaryEntryTypeList());
		}
	}

	private static GrammaFormConjugateResult makeTeForm(DictionaryEntry dictionaryEntry) {

		// forma te

		final String postfixKana = "で";
		final String postfixRomaji = " de";

		return makeNounGrammaConjugateForm(dictionaryEntry, GrammaFormConjugateResultType.NOUN_TE, postfixKana,
				postfixRomaji);
	}

	private static GrammaFormConjugateResult makeKeigoLowForm(DictionaryEntry dictionaryEntry) {

		// keigo low

		final String postfixKana1 = "でございます";
		final String postfixRomaji1 = " de gozaimasu";

		GrammaFormConjugateResult result = makeNounGrammaConjugateForm(dictionaryEntry,
				GrammaFormConjugateResultType.NOUN_KEIGO_LOW, postfixKana1, postfixRomaji1);

		final String postfixKana2 = "でござる";
		final String postfixRomaji2 = " de gozaru";

		result.setAlternative(makeNounGrammaConjugateForm(dictionaryEntry,
				GrammaFormConjugateResultType.NOUN_KEIGO_LOW, postfixKana2, postfixRomaji2));

		return result;
	}
}
