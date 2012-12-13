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
		
		// motto
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_II_GRADATION, makeMotto(dictionaryEntry));

		// mottomo
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_III_GRADATION, makeMottomo(dictionaryEntry));
		
		// ku naru
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_NARU, makeAdjectiveINaru(dictionaryEntry));
		
		// n desu
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_N_DESU, makeNDesuExample(dictionaryEntry));
		
		// sugiru
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_SUGIRU, makeSugiruExample(dictionaryEntry));
		
		// deshou
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_DESHOU, makeDeshouExample(dictionaryEntry));
		
		// sou desu look like
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_SOU_DESU_LOOKS_LIKE, makeSouDesuLooksLikeExample(dictionaryEntry));		
		
		// kamoshi remasen
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_KAMOSHI_REMASEN, makeKamoshiRemasenExample(dictionaryEntry));
		
		// to ii to others
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_TO_II_TO_OTHERS, makeToIIToOthers(dictionaryEntry));

		// to ii to me
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_TO_II_TO_ME, makeToIIToMe(dictionaryEntry));
		
		// toki
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_TOKI, makeToki(dictionaryEntry));
		
		// sou desu hear
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_SOU_DESU_HEAR, makeSouDesuHearExample(dictionaryEntry));		

		// tte
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_TTE, makeTteExample(dictionaryEntry));		
		
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
	
	private static ExampleResult makeSouDesuLooksLikeExample(DictionaryEntry dictionaryEntry) {
		
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
	
	private static ExampleResult makeToIIToOthers(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sといいですね";
		final String templateKana = "%sといいですね";
		final String templateRomaji = "%s to ii desu ne";
		
		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = AdjectiveIGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
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
		
		ExampleResult exampleResult1 = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		ExampleResult exampleResult2 = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true);
		
		exampleResult1.setAlternative(exampleResult2);
		
		GrammaFormConjugateResult informalPresentNegativeForm = AdjectiveIGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		exampleResult2.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji1, templateKana1, templateRomaji1, true));
		exampleResult2.getAlternative().setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult1;
	}
	
	private static ExampleResult makeToki(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%s時、...";
		final String templateKana = "%sとき、...";
		final String templateRomaji = "%s toki, ...";
		
		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPastForm = AdjectiveIGrammaConjugater.makeInformalPastForm(dictionaryEntry);
		
		exampleResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeSouDesuHearExample(DictionaryEntry dictionaryEntry) {
		
		String templateKanji = "%sそうです";
		String templateKana = "%sそうです";
		String templateRomaji = "%s sou desu";	
		
		ExampleResult souDesuResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = AdjectiveIGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		souDesuResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(
				informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
		return souDesuResult;
	}	

	private static ExampleResult makeTteExample(DictionaryEntry dictionaryEntry) {
		
		String templateKanji = "%sって";
		String templateKana = "%sって";
		String templateRomaji = "%stte";	
		
		ExampleResult souDesuResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = AdjectiveIGrammaConjugater.makeInformalPresentNegativeForm(dictionaryEntry);
		
		souDesuResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(
				informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
		return souDesuResult;
	}	
}
