package pl.idedyk.android.japaneselearnhelper.example;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;

public class ExampleManager {
	
	public static List<ExampleGroupTypeElements> getExamples(DictionaryEntry dictionaryEntry) {
		
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_I) {
			return AdjectiveIExampler.makeAll(dictionaryEntry);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_NA) {
			return AdjectiveNaExampler.makeAll(dictionaryEntry);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_NOUN) {
			return NounExampler.makeAll(dictionaryEntry);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U ||
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU ||
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			return VerbExampler.makeAll(dictionaryEntry);
		}
		
		return null;
	}
}
