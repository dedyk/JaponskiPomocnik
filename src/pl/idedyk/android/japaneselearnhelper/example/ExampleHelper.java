package pl.idedyk.android.japaneselearnhelper.example;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;

public class ExampleHelper {
	
	public static void addExample(List<ExampleGroupTypeElements> result, ExampleGroupType exampleGroupType, ExampleResult exampleResult) {
		
		ExampleGroupTypeElements exampleGroup = new ExampleGroupTypeElements();
		
		exampleGroup.setExampleGroupType(exampleGroupType);
		
		exampleGroup.getExampleResults().add(exampleResult);
		
		result.add(exampleGroup);
	}
	
	public static ExampleResult makeSimpleTemplateExample(DictionaryEntry dictionaryEntry,
			String templateKanji, String templateKana, String templateRomaji, boolean canAddPrefix) {
		
		String kanji = dictionaryEntry.getKanji();
		List<String> kanaList = dictionaryEntry.getKanaList();
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		return makeSimpleTemplateExample(kanji, kanaList, romajiList, templateKanji, templateKana, templateRomaji, canAddPrefix);
	}

	public static ExampleResult makeSimpleTemplateExample(GrammaFormConjugateResult grammaFormConjugateResult,
			String templateKanji, String templateKana, String templateRomaji, boolean canAddPrefix) {
		
		String kanji = grammaFormConjugateResult.getKanji();
		List<String> kanaList = grammaFormConjugateResult.getKanaList();
		List<String> romajiList = grammaFormConjugateResult.getRomajiList();
		
		return makeSimpleTemplateExample(kanji, kanaList, romajiList, templateKanji, templateKana, templateRomaji, canAddPrefix);
	}
	
	private static ExampleResult makeSimpleTemplateExample(String kanji, List<String> kanaList, List<String> romajiList,
			String templateKanji, String templateKana, String templateRomaji, boolean canAddPrefix) {
		
		ExampleResult result = new ExampleResult();
		
		result.setCanAddPrefix(canAddPrefix);
		

		if (kanji != null) {		
			result.setKanji(String.format(templateKanji, kanji));
		}

		List<String> kanaListResult = new ArrayList<String>();

		for (String currentKana : kanaList) {			
			kanaListResult.add(String.format(templateKana, currentKana));
		}

		result.setKanaList(kanaListResult);

		List<String> romajiListResult = new ArrayList<String>();

		for (String currentRomaji : romajiList) {
			romajiListResult.add(String.format(templateRomaji, currentRomaji));
		}

		result.setRomajiList(romajiListResult);
		
		return result;		
	}

}
