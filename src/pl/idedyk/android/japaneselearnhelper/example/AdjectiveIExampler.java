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
		
		return result;
	}

	private static ExampleResult makeAdjectiveINaru(DictionaryEntry dictionaryEntry) {
		
		GrammaFormConjugateResult virtualForm = AdjectiveIGrammaConjugater.makeVirtualForm(dictionaryEntry);
		
		final String templateKanji = "%sくなる";
		final String templateKana = "%sくなる";
		final String templateRomaji = "%sku naru";
		
		return ExampleHelper.makeSimpleTemplateExample(virtualForm, templateKanji, templateKana, templateRomaji, true);
	}
}
