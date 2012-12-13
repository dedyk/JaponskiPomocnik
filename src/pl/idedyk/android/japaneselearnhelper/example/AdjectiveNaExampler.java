package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.AdjectiveNaGrammaConjugater;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;

public class AdjectiveNaExampler {

	public static List<ExampleGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry) {

		List<ExampleGroupTypeElements> result = new ArrayList<ExampleGroupTypeElements>();
		
		// motto
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_II_GRADATION, makeMotto(dictionaryEntry));

		// mottomo
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_III_GRADATION, makeMottomo(dictionaryEntry));
		
		// ni naru
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_NARU, makeAdjectiveNaNaru(dictionaryEntry));
		
		// na desu
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_NA_DESU, makeNaDesuExample(dictionaryEntry));
		
		// sugiru
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_SUGIRU, makeSugiruExample(dictionaryEntry));
		
		// deshou
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_DESHOU, makeDeshouExample(dictionaryEntry));
		
		// sou desu looks like
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_SOU_DESU_LOOKS_LIKE, makeSouDesuLooksLikeExample(dictionaryEntry));
		
		// kamoshi remasen
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_KAMOSHI_REMASEN, makeKamoshiRemasenExample(dictionaryEntry));
		
		// to ii to others
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_TO_II_TO_OTHERS, makeToIIToOthers(dictionaryEntry));

		// to ii to me
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_TO_II_TO_ME, makeToIIToMe(dictionaryEntry));
		
		// toki
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_TOKI, makeToki(dictionaryEntry));
		
		// sou desu hear
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_SOU_DESU_HEAR, makeSouDesuHearExample(dictionaryEntry));

		// tte
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_TTE, makeTteExample(dictionaryEntry));
		
		// tara
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_TARA, makeTaraExample(dictionaryEntry));
		
		return result;
	}
	
	private static ExampleResult makeMotto(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "もっと%s";
		final String templateKana = "もっと%s";
		final String templateRomaji = "motto %s";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeMottomo(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji1 = "最も%s";
		final String templateKana1 = "もっとも%s";
		final String templateRomaji1 = "mottomo %s";
		
		ExampleResult mottomoExample = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		
		final String templateKanji2 = "一番%s";
		final String templateKana2 = "いちばん%s";
		final String templateRomaji2 = "ichiban %s";
		
		mottomoExample.setAlternative(ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true));
		
		return mottomoExample;
	}

	private static ExampleResult makeAdjectiveNaNaru(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sになる";
		final String templateKana = "%sになる";
		final String templateRomaji = "%s ni naru";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeNaDesuExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sなんです";
		final String templateKana = "%sなんです";
		final String templateRomaji = "%s nan desu";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeSugiruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sすぎる";
		final String templateKana = "%sすぎる";
		final String templateRomaji = "%s sugiru";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeDeshouExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sでしょう";
		final String templateKana = "%sでしょう";
		final String templateRomaji = "%s deshou";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeSouDesuLooksLikeExample(DictionaryEntry dictionaryEntry) {
		
		String templateKanji1 = "%sそうです";
		String templateKana1 = "%sそうです";
		String templateRomaji1 = "%s sou desu";	
		
		String templateKanji2 = "%sさそうです";
		String templateKana2 = "%sさそうです";
		String templateRomaji2 = "%s sasou desu";

		
		ExampleResult souDesuResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		
		GrammaFormConjugateResult makeInformalPresentNegativeForm = AdjectiveNaGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		souDesuResult.setAlternative(ExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(
				makeInformalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));		
		
		return souDesuResult;
	}
	
	private static ExampleResult makeKamoshiRemasenExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sかもしれません";
		final String templateKana = "%sかもしれません";
		final String templateRomaji = "%s kamoshi remasen";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeToIIToOthers(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sといいですね";
		final String templateKana = "%sといいですね";
		final String templateRomaji = "%s to ii desu ne";
		
		GrammaFormConjugateResult informalPresentForm = AdjectiveNaGrammaConjugater.makeInformalPresentForm(dictionaryEntry);
		
		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = AdjectiveNaGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		exampleResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeToIIToMe(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji1 = "%sといいんですが";
		final String templateKana1 = "%sといいんですが";
		final String templateRomaji1 = "%s to ii n desu ga";

		final String templateKanji2 = "%sといいんですけど";
		final String templateKana2 = "%sといいんですけど";
		final String templateRomaji2 = "%s to ii n desu kedo";
		
		GrammaFormConjugateResult informalPresentForm = AdjectiveNaGrammaConjugater.makeInformalPresentForm(dictionaryEntry);
		
		ExampleResult exampleResult1 = ExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji1, templateKana1, templateRomaji1, true);
		ExampleResult exampleResult2 = ExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji2, templateKana2, templateRomaji2, true);
		
		exampleResult1.setAlternative(exampleResult2);
		
		GrammaFormConjugateResult informalPresentNegativeForm = AdjectiveNaGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		exampleResult2.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji1, templateKana1, templateRomaji1, true));
		exampleResult2.getAlternative().setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult1;
	}
	
	private static ExampleResult makeToki(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji1 = "%sな時、...";
		final String templateKana1 = "%sなとき、...";
		final String templateRomaji1 = "%s na toki, ...";
		
		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		
		GrammaFormConjugateResult informalPastForm = AdjectiveNaGrammaConjugater.makeInformalPastForm(dictionaryEntry);

		final String templateKanji2 = "%s時、...";
		final String templateKana2 = "%sとき、...";
		final String templateRomaji2 = "%s toki, ...";
		
		exampleResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeSouDesuHearExample(DictionaryEntry dictionaryEntry) {
		
		String templateKanji = "%sそうです";
		String templateKana = "%sそうです";
		String templateRomaji = "%s sou desu";	
		
		GrammaFormConjugateResult informalPresentForm = AdjectiveNaGrammaConjugater.makeInformalPresentForm(dictionaryEntry);
		
		ExampleResult souDesuResult = ExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult makeInformalPastForm = AdjectiveNaGrammaConjugater.makeInformalPastForm(dictionaryEntry);
		
		souDesuResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(
				makeInformalPastForm, templateKanji, templateKana, templateRomaji, true));		
		
		return souDesuResult;
	}

	private static ExampleResult makeTteExample(DictionaryEntry dictionaryEntry) {
		
		String templateKanji = "%sって";
		String templateKana = "%sって";
		String templateRomaji = "%stte";	
		
		GrammaFormConjugateResult informalPresentForm = AdjectiveNaGrammaConjugater.makeInformalPresentForm(dictionaryEntry);
		
		ExampleResult souDesuResult = ExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult makeInformalPastForm = AdjectiveNaGrammaConjugater.makeInformalPastForm(dictionaryEntry);
		
		souDesuResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(
				makeInformalPastForm, templateKanji, templateKana, templateRomaji, true));		
		
		return souDesuResult;
	}
	
	private static ExampleResult makeTaraExample(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult informalPastForm = AdjectiveNaGrammaConjugater.makeInformalPastForm(dictionaryEntry);

		final String templateKanji1 = "%sら、...";
		final String templateKana1 = "%sら、...";
		final String templateRomaji1 = "%sra, ...";
		
		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji1, templateKana1, templateRomaji1, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = AdjectiveNaGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		final String templateKanji2 = "%sかったら、...";
		final String templateKana2 = "%sかったら、...";
		final String templateRomaji2 = "%skattara, ...";		
		
		exampleResult.setAlternative(ExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(
				informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult;
	}
}
