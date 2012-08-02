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
		ExampleGroupTypeElements likeExample = new ExampleGroupTypeElements();
		
		likeExample.setExampleGroupType(ExampleGroupType.NOUN_LIKE);
		
		likeExample.getExampleResults().add(makeSukiExample(dictionaryEntry));
		
		result.add(likeExample);
		
		// dislike : kirai
		ExampleGroupTypeElements dislikeExample = new ExampleGroupTypeElements();
		
		dislikeExample.setExampleGroupType(ExampleGroupType.NOUN_DISLIKE);
		
		dislikeExample.getExampleResults().add(makeDislikeExample(dictionaryEntry));
		
		result.add(dislikeExample);
		
		return result;
	}

	private static ExampleResult makeSukiExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sが好き";
		final String templateKana = "%sがすき";
		final String templateRomaji = "%s ga suki";
		
		return makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}

	private static ExampleResult makeDislikeExample(DictionaryEntry dictionaryEntry) {
		
		final String templateKanji = "%sがきらい";
		final String templateKana = "%sがきらい";
		final String templateRomaji = "%s ga kirai";
		
		return makeSimpleTemplateExample(dictionaryEntry, templateKanji, templateKana, templateRomaji, true);
	}
	
	private static ExampleResult makeSimpleTemplateExample(DictionaryEntry dictionaryEntry,
			String templateKanji, String templateKana, String templateRomaji, boolean canAddPrefix) {
		
		ExampleResult result = new ExampleResult();
		
		result.setCanAddPrefix(canAddPrefix);
		
		String kanji = dictionaryEntry.getKanji();

		if (kanji != null) {		
			result.setKanji(String.format(templateKanji, kanji));
		}

		List<String> kanaList = dictionaryEntry.getKanaList();

		List<String> kanaListResult = new ArrayList<String>();

		for (String currentKana : kanaList) {			
			kanaListResult.add(String.format(templateKana, currentKana));
		}

		result.setKanaList(kanaListResult);		

		List<String> romajiList = dictionaryEntry.getRomajiList();

		List<String> romajiListResult = new ArrayList<String>();

		for (String currentRomaji : romajiList) {
			romajiListResult.add(String.format(templateRomaji, currentRomaji));
		}

		result.setRomajiList(romajiListResult);
		
		return result;		
	}
}
