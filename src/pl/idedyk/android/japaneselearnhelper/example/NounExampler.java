package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.NounGrammaConjugater;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;

public class NounExampler {
	public static List<ExampleGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry) {
		
		List<ExampleGroupTypeElements> result = new ArrayList<ExampleGroupTypeElements>();
		
		// like : suki		
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_LIKE, makeSukiExample(dictionaryEntry));
		
		// dislike : kirai		
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_DISLIKE, makeKiraiExample(dictionaryEntry));
		
		// ni naru
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_NARU, makeNounNaru(dictionaryEntry));
		
		// na desu
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_NA_DESU, makeNaDesuExample(dictionaryEntry));
		
		// deshou
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_DESHOU, makeDeshouExample(dictionaryEntry));

		// hoshii
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_HOSHII, makeHoshiiExample(dictionaryEntry));

		// hoshigatte iru
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_HOSHIGATE_IRU, makeHoshigatteIruExample(dictionaryEntry));
		
		// kamoshi remasen
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_KAMOSHI_REMASEN, makeKamoshiRemasenExample(dictionaryEntry));
		
		// ageru
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_AGERU, makeAgeruExample(dictionaryEntry));
		
		// kureru
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_KURERU, makeKureruExample(dictionaryEntry));
		
		// morau
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_MORAU, makeMorauExample(dictionaryEntry));
		
		// toki
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_TOKI, makeToki(dictionaryEntry));
		
		// sou desu hear
		ExampleHelper.addExample(result, ExampleGroupType.NOUN_SOU_DESU_HEAR, makeSouDesuHearExample(dictionaryEntry));
		
		return result;
	}

	private static ExampleResult makeSukiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sが好き";
		final String templateKana = "%sがすき";
		final String templateRomaji = "%s ga suki";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeKiraiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sがきらい";
		final String templateKana = "%sがきらい";
		final String templateRomaji = "%s ga kirai";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeNounNaru(DictionaryEntry dictionaryEntry) {
		
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
	
	private static ExampleResult makeDeshouExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sでしょう";
		final String templateKana = "%sでしょう";
		final String templateRomaji = "%s deshou";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeHoshiiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sがほしい";
		final String templateKana = "%sがほしい";
		final String templateRomaji = "%s ga hoshii";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeHoshigatteIruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sをほしがっている";
		final String templateKana = "%sをほしがっている";
		final String templateRomaji = "%s o hoshigatte iru";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeKamoshiRemasenExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sかもしれません";
		final String templateKana = "%sかもしれません";
		final String templateRomaji = "%s kamoshi remasen";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeAgeruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "私 [は/が] [odbiorca] に%sをあげる";
		final String templateKana = "わたし [は/が] [odbiorca] に%sをあげる";
		final String templateRomaji = "watashi [wa/ga] [odbiorca] ni %s o ageru";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeKureruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "[dający] [は/が] %sをくれる";
		final String templateKana = "[dający] [は/が] %sをくれる";
		final String templateRomaji = "[dający] [wa/ga] %s o kureru";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeMorauExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "[odbiorca] [は/が] [dający] [に/から] %sをもらう";
		final String templateKana = "[odbiorca] [は/が] [dający] [に/から] %sをもらう";
		final String templateRomaji = "[odbiorca] [wa/ga] [dający] [ni/kara] %s o morau";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, false);
	}
	
	private static ExampleResult makeToki(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji1 = "%sの時、...";
		final String templateKana1 = "%sのとき、...";
		final String templateRomaji1 = "%s no toki, ...";
		
		ExampleResult exampleResult = ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji1, templateKana1, templateRomaji1, true);
		
		GrammaFormConjugateResult informalPastForm = NounGrammaConjugater.makeInformalPastForm(dictionaryEntry);

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
		
		GrammaFormConjugateResult informalPresentForm = NounGrammaConjugater.makeInformalPresentForm(dictionaryEntry);
		
		ExampleResult souDesuResult = ExampleHelper.makeSimpleTemplateExample(informalPresentForm, templateKanji, templateKana, templateRomaji, true);
		
		GrammaFormConjugateResult makeInformalPastForm = NounGrammaConjugater.makeInformalPastForm(dictionaryEntry);
		
		souDesuResult.setAlternative(ExampleHelper.makeSimpleTemplateExample(
				makeInformalPastForm, templateKanji, templateKana, templateRomaji, true));		
		
		return souDesuResult;
	}

}
