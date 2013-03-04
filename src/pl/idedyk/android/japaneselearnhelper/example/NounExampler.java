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

public class NounExampler {
	public static List<ExampleGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry, 
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		List<ExampleGroupTypeElements> result = new ArrayList<ExampleGroupTypeElements>();
		
		// like : suki		
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_LIKE, makeSukiExample(dictionaryEntry));
		
		// dislike : kirai		
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_DISLIKE, makeKiraiExample(dictionaryEntry));
		
		// ni naru
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_NI_NARU, makeNounNiNaru(dictionaryEntry));
		
		// na desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_NA_DESU, makeNaDesuExample(dictionaryEntry));
		
		// deshou
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_DESHOU, makeDeshouExample(dictionaryEntry));

		// hoshii
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_HOSHII, makeHoshiiExample(dictionaryEntry));

		// hoshigatte iru
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_HOSHIGATE_IRU, makeHoshigatteIruExample(dictionaryEntry));
		
		// kamoshi remasen
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_KAMOSHI_REMASEN, makeKamoshiRemasenExample(dictionaryEntry));
		
		// ageru
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_AGERU, makeAgeruExample(dictionaryEntry));
		
		// kureru
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_KURERU, makeKureruExample(dictionaryEntry));
		
		// morau
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_MORAU, makeMorauExample(dictionaryEntry));
		
		// to ii to others
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_TO_II_TO_OTHERS, makeToIIToOthers(dictionaryEntry, grammaFormCache));

		// to ii to me
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_TO_II_TO_ME, makeToIIToMe(dictionaryEntry, grammaFormCache));
		
		// toki
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_TOKI, makeToki(dictionaryEntry, grammaFormCache));
		
		// sou desu hear
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_SOU_DESU_HEAR, makeSouDesuHearExample(dictionaryEntry, grammaFormCache));

		// tte
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_TTE, makeTteExample(dictionaryEntry, grammaFormCache));
		
		// tara
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_TARA, makeTaraExample(dictionaryEntry, grammaFormCache));
		
		// mitai desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_MITAI_DESU, makeMitaiDesuExample(dictionaryEntry, grammaFormCache));
		
		// to
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_TO, makeToExample(dictionaryEntry));
		
		// hazu desu
		GrammaExampleHelper.addExample(result, ExampleGroupType.NOUN_HAZU_DESU, makeHazuDesu(dictionaryEntry, grammaFormCache));
		
		return result;
	}

	private static ExampleResult makeSukiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sが好き";
		final String templateKana = "%sがすき";
		final String templateRomaji = "%s ga suki";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeKiraiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sがきらい";
		final String templateKana = "%sがきらい";
		final String templateRomaji = "%s ga kirai";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeNounNiNaru(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sになる";
		final String templateKana = "%sになる";
		final String templateRomaji = "%s ni naru";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeNaDesuExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sなんです";
		final String templateKana = "%sなんです";
		final String templateRomaji = "%s nan desu";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeDeshouExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sでしょう";
		final String templateKana = "%sでしょう";
		final String templateRomaji = "%s deshou";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeHoshiiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sがほしい";
		final String templateKana = "%sがほしい";
		final String templateRomaji = "%s ga hoshii";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeHoshigatteIruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sをほしがっている";
		final String templateKana = "%sをほしがっている";
		final String templateRomaji = "%s o hoshigatte iru";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeKamoshiRemasenExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sかもしれません";
		final String templateKana = "%sかもしれません";
		final String templateRomaji = "%s kamoshi remasen";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeAgeruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "私 [は/が] [odbiorca] に%sをあげる";
		final String templateKana = "わたし [は/が] [odbiorca] に%sをあげる";
		final String templateRomaji = "watashi [wa/ga] [odbiorca] ni %s o ageru";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeKureruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "[dający] [は/が] %sをくれる";
		final String templateKana = "[dający] [は/が] %sをくれる";
		final String templateRomaji = "[dający] [wa/ga] %s o kureru";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeMorauExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "[odbiorca] [は/が] [dający] [に/から] %sをもらう";
		final String templateKana = "[odbiorca] [は/が] [dający] [に/から] %sをもらう";
		final String templateRomaji = "[odbiorca] [wa/ga] [dający] [ni/kara] %s o morau";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeToIIToOthers(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji = "%sといいですね";
		final String templateKana = "%sといいですね";
		final String templateRomaji = "%s to ii desu ne";
		
		GrammaFormConjugateResult informalPresentForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PRESENT);
		
		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PRESENT_NEGATIVE);
		
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
		
		GrammaFormConjugateResult informalPresentForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PRESENT);
		
		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji1, templateKana1, templateRomaji1, true);
		ExampleResult exampleResult2 = GrammaExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji2, templateKana2, templateRomaji2, true);
		
		exampleResult1.setAlternative(exampleResult2);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PRESENT_NEGATIVE);
		
		exampleResult2.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji1, templateKana1, templateRomaji1, true));
		exampleResult2.getAlternative().setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult1;
	}
	
	private static ExampleResult makeToki(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji1 = "%sの時、...";
		final String templateKana1 = "%sのとき、...";
		final String templateRomaji1 = "%s no toki, ...";
		
		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PAST);

		final String templateKanji2 = "%s時、...";
		final String templateKana2 = "%sとき、...";
		final String templateRomaji2 = "%s toki, ...";
		
		exampleResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult;
	}
	
	private static ExampleResult makeSouDesuHearExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String templateKanji = "%sそうです";
		String templateKana = "%sそうです";
		String templateRomaji = "%s sou desu";	
		
		GrammaFormConjugateResult informalPresentForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PRESENT);
		
		ExampleResult souDesuResult = GrammaExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PAST);
		
		souDesuResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(
				informalPastForm, templateKanji, templateKana, templateRomaji, true));		
		
		return souDesuResult;
	}

	private static ExampleResult makeTteExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		String templateKanji = "%sって";
		String templateKana = "%sって";
		String templateRomaji = "%stte";	
		
		GrammaFormConjugateResult informalPresentForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PRESENT);
		
		ExampleResult souDesuResult = GrammaExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PAST);
		
		souDesuResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExample(
				informalPastForm, templateKanji, templateKana, templateRomaji, true));		
		
		return souDesuResult;
	}	
	
	private static ExampleResult makeTaraExample(DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		GrammaFormConjugateResult informalPastForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PAST);

		final String templateKanji1 = "%sら、...";
		final String templateKana1 = "%sら、...";
		final String templateRomaji1 = "%sra, ...";
		
		ExampleResult exampleResult = GrammaExampleHelper.makeSimpleTemplateExample(informalPastForm, templateKanji1, templateKana1, templateRomaji1, true);
		
		GrammaFormConjugateResult informalPresentNegativeForm = grammaFormCache.get(GrammaFormConjugateResultType.NOUN_INFORMAL_PRESENT_NEGATIVE);
		
		final String templateKanji2 = "%sかったら、...";
		final String templateKana2 = "%sかったら、...";
		final String templateRomaji2 = "%skattara, ...";		
		
		exampleResult.setAlternative(GrammaExampleHelper.makeSimpleTemplateExampleWithLastCharRemove(
				informalPresentNegativeForm, templateKanji2, templateKana2, templateRomaji2, true));
		
		return exampleResult;
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
		
		final String templateKanji3 = "%sみたいに [przymiotnik, czasownik]";
		final String templateKana3 = "%sみたいに [przymiotnik, czasownik]";
		final String templateRomaji3 = "%s mitai ni [przymiotnik, czasownik]";
		
		ExampleResult alternative3 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji3, templateKana3, templateRomaji3, true);

		alternative2.setAlternative(alternative3);
	
		return exampleResult;
	}
	
	private static ExampleResult makeToExample(DictionaryEntry dictionaryEntry) {
		
		ExampleResult nounNiNaru = makeNounNiNaru(dictionaryEntry);
		
		final String templateKanji = "%sと, ...";
		final String templateKana = "%sと, ...";
		final String templateRomaji = "%s to, ...";
		
		return GrammaExampleHelper.makeSimpleTemplateExample(nounNiNaru, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeHazuDesu(DictionaryEntry dictionaryEntry, Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		final String templateKanji1 = "%sのはずです";
		final String templateKana1 = "%sなのはずです";
		final String templateRomaji1 = "%s no hazu desu";
		
		ExampleResult exampleResult1 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		
		final String templateKanji2 = "%sのはずでした";
		final String templateKana2 = "%sのはずでした";
		final String templateRomaji2 = "%s no hazu deshita";
		
		ExampleResult alternative1 = GrammaExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji2, templateKana2, templateRomaji2, true);
		
		exampleResult1.setAlternative(alternative1);
		
		return exampleResult1;
	}
}
