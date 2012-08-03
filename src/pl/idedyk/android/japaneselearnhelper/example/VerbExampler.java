package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.VerbGrammaConjugater;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;

public class VerbExampler {
	public static List<ExampleGroupTypeElements> makeAll(DictionaryEntry dictionaryEntry) {
		
		List<ExampleGroupTypeElements> result = new ArrayList<ExampleGroupTypeElements>();
		
		// te iru
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_IRU, makeTeIruExample(dictionaryEntry));
		
		// te kudasai
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_KUDASAI, makeTeKudasaiExample(dictionaryEntry));
		
		// te mo ii desu
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_MO_II, makeTeMoIiExample(dictionaryEntry));
		
		// te wa ikemasen
		ExampleHelper.addExample(result, ExampleGroupType.VERB_TE_HA_IKEMASEN, makeTeHaIkemasenExample(dictionaryEntry));
		
		// like : suki		
		ExampleHelper.addExample(result, ExampleGroupType.VERB_LIKE, makeSukiExample(dictionaryEntry));
		
		// dislike : kirai		
		ExampleHelper.addExample(result, ExampleGroupType.VERB_DISLIKE, makeKiraiExample(dictionaryEntry));
		
		return result;
	}

	private static ExampleResult makeTeIruExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sいる";
		final String templateKana = "%sいる";
		final String templateRomaji = "%s iru";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeTeKudasaiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sください";
		final String templateKana = "%sください";
		final String templateRomaji = "%s kudasai";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		return ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);		
	}
	
	private static ExampleResult makeTeMoIiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sもいいです";
		final String templateKana = "%sもいいです";
		final String templateRomaji = "%s mo ii desu";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		ExampleResult moIidesu = ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
		
		moIidesu.setAlternative(
				ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji + "か", templateKana + "か", templateRomaji + " ka", true));
		
		return moIidesu;
	}
	
	private static ExampleResult makeTeHaIkemasenExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sはいけません";
		final String templateKana = "%sはいけません";
		final String templateRomaji = "%s wa ikemasen";
		
		GrammaFormConjugateResult teForm = VerbGrammaConjugater.makeTeForm(dictionaryEntry);
		
		ExampleResult moIidesu = ExampleHelper.makeSimpleTemplateExample(teForm, templateKanji, templateKana, templateRomaji, true);
				
		return moIidesu;
	}

	private static ExampleResult makeSukiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sのが好き";
		final String templateKana = "%sのがすき";
		final String templateRomaji = "%s no ga suki";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeKiraiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sのがきらい";
		final String templateKana = "%sのがきらい";
		final String templateRomaji = "%s no ga kirai";
		
		return ExampleHelper.makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
}
