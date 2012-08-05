package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.AdjectiveIGrammaConjugater;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;

public class AdjectiveIExampler {

	public static List<ExampleGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry) {

		List<ExampleGroupTypeElements> result = new ArrayList<ExampleGroupTypeElements>();
		
		// ku naru
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_NARU, makeAdjectiveINaru(dictionaryEntry));
		
		// n desu
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_N_DESU, makeNDesuExample(dictionaryEntry));
		
		// sugiru
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_SUGIRU, makeSugiruExample(dictionaryEntry));
		
		// deshou
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_DESHOU, makeDeshouExample(dictionaryEntry));
		
		// sou desu
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_SOU_DESU, makeSouDesuExample(dictionaryEntry));		
		
		// kamoshi remasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_KAMOSHI_REMASEN, makeKamoshiRemasenExample(dictionaryEntry));
		
		return result;
	}

	private static ExampleResult makeAdjectiveINaru(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult virtualForm = AdjectiveIGrammaConjugater.makeVirtualForm(dictionaryEntry);
		
		final String templateKanji = "%sくなる";
		final String templateKana = "%sくなる";
		final String templateRomaji = "%sku naru";
		
		return ExampleHelper.makeSimpleTemplateExample(virtualForm, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeNDesuExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sんです";
		final String templateKana = "%sんです";
		final String templateRomaji = "%sn desu";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeSugiruExample(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult virtualForm = AdjectiveIGrammaConjugater.makeVirtualForm(dictionaryEntry);
		
		final String templateKanji = "%sすぎる";
		final String templateKana = "%sすぎる";
		final String templateRomaji = "%s sugiru";
		
		return ExampleHelper.makeSimpleTemplateExample(virtualForm, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeDeshouExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sでしょう";
		final String templateKana = "%sでしょう";
		final String templateRomaji = "%s deshou";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeSouDesuExample(DictionaryEntry dictionaryEntry) {
		
		boolean isIiAdjective = false;
		
		List<String> kanaList = dictionaryEntry.getKanaList();
		
		for (String currentKana : kanaList) {
			if (currentKana.endsWith("いい") == true) {
				isIiAdjective = true;
				
				break;
			}
		}
		
		GrammaFormConjugateResult virtualForm = AdjectiveIGrammaConjugater.makeVirtualForm(dictionaryEntry);
		
		String templateKanji1 = "%sそうです";
		String templateKana1 = "%sそうです";
		String templateRomaji1 = "%s sou desu";	
		
		String templateKanji2 = "%sさそうです";
		String templateKana2 = "%sさそうです";
		String templateRomaji2 = "%s sasou desu";
		
		ExampleResult souDesuResult = null;
		
		if (isIiAdjective == false) {
			souDesuResult = ExampleHelper.makeSimpleTemplateExample(virtualForm, templateKanji1, templateKana1, templateRomaji1, true);		
		} else {
			souDesuResult = ExampleHelper.makeSimpleTemplateExample(virtualForm, templateKanji2, templateKana2, templateRomaji2, true);
		}
		
		GrammaFormConjugateResult informalPresentNegativeForm = AdjectiveIGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		souDesuResult.setAlternative(ExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(
				informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return souDesuResult;
	}
	
	private static ExampleResult makeKamoshiRemasenExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sかもしれません";
		final String templateKana = "%sかもしれません";
		final String templateRomaji = "%s kamoshi remasen";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
}
