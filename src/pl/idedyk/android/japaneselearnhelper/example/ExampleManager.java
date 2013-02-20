package pl.idedyk.android.japaneselearnhelper.example;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;

public class ExampleManager {
	
	public static List<ExampleGroupTypeElements> getExamples(DictionaryManager dictionaryManager, DictionaryEntry dictionaryEntry, 
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		
		List<ExampleGroupTypeElements> result = null;
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_I) {
			result =  AdjectiveIExampler.makeAll(dictionaryEntry, grammaFormCache);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_NA) {
			result =  AdjectiveNaExampler.makeAll(dictionaryEntry, grammaFormCache);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_NOUN) {
			result =  NounExampler.makeAll(dictionaryEntry, grammaFormCache);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U ||
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU ||
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			result = VerbExampler.makeAll(dictionaryManager, dictionaryEntry, grammaFormCache);
		}
		
		if (result != null) {
			
			Collections.sort(result, new Comparator<ExampleGroupTypeElements>() {

				public int compare(ExampleGroupTypeElements lhs, ExampleGroupTypeElements rhs) {
					return lhs.getExampleGroupType().getName().compareTo(rhs.getExampleGroupType().getName());
				}
			});		
		}
		
		return result;
	}
}
