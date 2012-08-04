package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;

public class AdjectiveNaExampler {

	public static List<ExampleGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry) {

		List<ExampleGroupTypeElements> result = new ArrayList<ExampleGroupTypeElements>();
		
		// ni naru
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_NARU, makeAdjectiveNaNaru(dictionaryEntry));
		
		// na desu
		ExampleHelper.addExample(result, ExampleGroupType.ADJECTIVE_NA_NA_DESU, makeNaDesuExample(dictionaryEntry));
		
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
}
