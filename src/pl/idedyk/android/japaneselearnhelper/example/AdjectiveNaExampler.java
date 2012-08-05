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
		
		// ni naru
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_NARU, makeAdjectiveNaNaru(dictionaryEntry));
		
		// na desu
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_NA_DESU, makeNaDesuExample(dictionaryEntry));
		
		// sugiru
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_SUGIRU, makeSugiruExample(dictionaryEntry));
		
		// deshou
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_DESHOU, makeDeshouExample(dictionaryEntry));
		
		// sou desu
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_SOU_DESU, makeSouDesuExample(dictionaryEntry));
		
		// kamoshi remasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_KAMOSHI_REMASEN, makeKamoshiRemasenExample(dictionaryEntry));
		
		return result;
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
	
	private static ExampleResult makeSouDesuExample(DictionaryEntry dictionaryEntry) {
		
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
}
