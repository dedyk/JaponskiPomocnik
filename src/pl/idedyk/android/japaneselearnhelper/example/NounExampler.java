package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;

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
}
