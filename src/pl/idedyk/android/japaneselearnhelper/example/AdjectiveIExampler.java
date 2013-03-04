package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;
import pl.idedyk.android.japaneselearnhelper.grammaexample.GrammaExampleHelper;

public class AdjectiveIExampler {

	public static List<ExampleGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry, 
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {

		List<ExampleGroupTypeElements> result = new ArrayList<ExampleGroupTypeElements>();
		
		// motto
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_II_GRADATION, makeMotto(dictionaryEntry));

		// mottomo
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_III_GRADATION, makeMottomo(dictionaryEntry));
		
		// ku naru
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_NARU, makeAdjectiveINaru(dictionaryEntry, grammaFormCache));
		
		// n desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_N_DESU, makeNDesuExample(dictionaryEntry));
		
		// sugiru
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_SUGIRU, makeSugiruExample(dictionaryEntry, grammaFormCache));
		
		// deshou
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_DESHOU, makeDeshouExample(dictionaryEntry));
		
		// sou desu look like
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_SOU_DESU_LOOKS_LIKE, makeSouDesuLooksLikeExample(dictionaryEntry, grammaFormCache));		
		
		// kamoshi remasen
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_KAMOSHI_REMASEN, makeKamoshiRemasenExample(dictionaryEntry));
		
		// to ii to others
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_TO_II_TO_OTHERS, makeToIIToOthers(dictionaryEntry, grammaFormCache));

		// to ii to me
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_TO_II_TO_ME, makeToIIToMe(dictionaryEntry, grammaFormCache));
		
		// toki
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_TOKI, makeToki(dictionaryEntry, grammaFormCache));
		
		// sou desu hear
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_SOU_DESU_HEAR, makeSouDesuHearExample(dictionaryEntry, grammaFormCache));		

		// tte
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_TTE, makeTteExample(dictionaryEntry, grammaFormCache));		
		
		// tara
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_TARA, makeTaraExample(dictionaryEntry, grammaFormCache));
		
		// nakute mo ii desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_NAKUTE_MO_II_DESU, makeNakuteMoIiDesu(dictionaryEntry, grammaFormCache));

		// mitai desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_MITAI_DESU, makeMitaiDesuExample(dictionaryEntry, grammaFormCache));

		// to
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_TO, makeToExample(dictionaryEntry, grammaFormCache));
		
		// hazu desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_I_HAZU_DESU, makeHazuDesu(dictionaryEntry, grammaFormCache));
		
		return result;
	}
	
	private static ExampleResult makeMotto(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "もっと%s";
		final String templateKana = "もっと%s";
		final String templateRomaji = "motto %s";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeMottomo(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji1 = "最も%s";
		final String templateKana1 = "もっとも%s";
		final String templateRomaji1 = "mottomo %s";
		
		ExampleResult mottomoExample = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		
		final String templateKanji2 = "一番%s";
		final String templateKana2 = "いちばん%s";
		final String templateRomaji2 = "ichiban %s";
		
		mottomoExample.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true));
		
		return mottomoExample;
	}
	
	private static ExampleResult makeAdjectiveINaru(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult virtualForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_VIRTUAL);
		
		final String templateKanji = "%sくなる";
		final String templateKana = "%sくなる";
		final String templateRomaji = "%sku naru";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(virtualForm, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeNDesuExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sんです";
		final String templateKana = "%sんです";
		final String templateRomaji = "%sn desu";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeSugiruExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult virtualForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_VIRTUAL);
		
		final String templateKanji = "%sすぎる";
		final String templateKana = "%sすぎる";
		final String templateRomaji = "%s sugiru";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(virtualForm, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeDeshouExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sでしょう";
		final String templateKana = "%sでしょう";
		final String templateRomaji = "%s deshou";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeSouDesuLooksLikeExample(DictionaryEntry dictionaryEntry, 
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		boolean isIiAdjective = false;
		
		List<String> kanaList = dictionaryEntry.getKanaList();
		
		for (String currentKana : kanaList) {
			if (currentKana.endsWith("いい") == true) {
				isIiAdjective = true;
				
				break;
			}
		}
		
		GrammaFormConjugateResult virtualForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_VIRTUAL);
		
		String templateKanji1 = "%sそうです";
		String templateKana1 = "%sそうです";
		String templateRomaji1 = "%s sou desu";	
		
		String templateKanji2 = "%sさそうです";
		String templateKana2 = "%sさそうです";
		String templateRomaji2 = "%s sasou desu";
		
		ExampleResult souDesuResult = null;
		
		if (isIiAdjective == false) {
			souDesuResult = GrammaExampleHelper.makeSimpleTemplateExample(virtualForm, templateKanji1, templateKana1, templateRomaji1, true);		
		} else {
			souDesuResult = GrammaExampleHelper.makeSimpleTemplateExample(virtualForm, templateKanji2, templateKana2, templateRomaji2, true);
		}
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE);
		
		souDesuResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(
				informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return souDesuResult;
	}
	
	private static ExampleResult makeKamoshiRemasenExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sかもしれません";
		final String templateKana = "%sかもしれません";
		final String templateRomaji = "%s kamoshi remasen";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeToIIToOthers(DictionaryEntry dictionaryEntry, 
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sといいですね";
		final String templateKana = "%sといいですね";
		final String templateRomaji = "%s to ii desu ne";
		
		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE);
		
		exampleResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeToIIToMe(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji1 = "%sといいんですが";
		final String templateKana1 = "%sといいんですが";
		final String templateRomaji1 = "%s to ii n desu ga";

		final String templateKanji2 = "%sといいんですけど";
		final String templateKana2 = "%sといいんですけど";
		final String templateRomaji2 = "%s to ii n desu kedo";
		
		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		ExampleResult exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true);
		
		exampleResult1.setAlternative(exampleResult2);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE);
		
		exampleResult2.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji1, templateKana1, templateRomaji1, true));
		exampleResult2.getAlternative().setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult1;
	}
	
	private static ExampleResult makeToki(DictionaryEntry dictionaryEntry, 
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%s時、...";
		final String templateKana = "%sとき、...";
		final String templateRomaji = "%s toki, ...";
		
		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PAST);
		
		exampleResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji, templateKana, templateRomaji, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeSouDesuHearExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String templateKanji = "%sそうです";
		String templateKana = "%sそうです";
		String templateRomaji = "%s sou desu";	
		
		ExampleResult souDesuResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE);
		
		souDesuResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(
				informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
		return souDesuResult;
	}	

	private static ExampleResult makeTteExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String templateKanji = "%sって";
		String templateKana = "%sって";
		String templateRomaji = "%stte";	
		
		ExampleResult souDesuResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE);
		
		souDesuResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(
				informalPresentNegativeForm, templateKanji, templateKana, templateRomaji, true));
		
		return souDesuResult;
	}	
	
	private static ExampleResult makeTaraExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PAST);

		final String templateKanji1 = "%sら、...";
		final String templateKana1 = "%sら、...";
		final String templateRomaji1 = "%sra, ...";
		
		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji1, templateKana1, templateRomaji1, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji2 = "%sかったら、...";
		final String templateKana2 = "%sかったら、...";
		final String templateRomaji2 = "%skattara, ...";		
		
		exampleResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(
				informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeNakuteMoIiDesu(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.ADJECTIVE_I_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji2 = "%sくてもいいです";
		final String templateKana2 = "%sくてもいいです";
		final String templateRomaji2 = "%skute mo ii desu";
		
		return GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true);
	}
	
	private static ExampleResult makeMitaiDesuExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji1 = "%sみたいです";
		final String templateKana1 = "%sみたいです";
		final String templateRomaji1 = "%s mitai desu";

		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);

		final String templateKanji2 = "%sみたいな";
		final String templateKana2 = "%sみたいな";
		final String templateRomaji2 = "%s mitai na";
		
		ExampleResult alternative2 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true);
		
		alternative2.setInfo("Zachowuje się, jak na-przymiotnik");
		
		exampleResult.setAlternative(alternative2);
	
		return exampleResult;
	}
	
	private static ExampleResult makeToExample(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		ExampleResult nounNiNaru = makeAdjectiveINaru(dictionaryEntry, grammaFormCache);
		
		final String templateKanji = "%sと, ...";
		final String templateKana = "%sと, ...";
		final String templateRomaji = "%s to, ...";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(nounNiNaru, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeHazuDesu(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
	
		final String templateKanji1 = "%sはずです";
		final String templateKana1 = "%sはずです";
		final String templateRomaji1 = "%s hazu desu";
		
		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		
		final String templateKanji2 = "%sはずでした";
		final String templateKana2 = "%sはずでした";
		final String templateRomaji2 = "%s hazu deshita";
		
		ExampleResult alternative1 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true);
		
		exampleResult1.setAlternative(alternative1);
		
		return exampleResult1;
	}
}
