package pl.idedyk.android.japaneselearnhelper.gramma;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;

public class GrammaConjugaterManager {
	
	public static List<GrammaFormConjugateGroupTypeElements> getGrammaConjufateResult(DictionaryEntry dictionaryEntry) {
		
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_I) {
			return AdjectiveIGrammaConjugater.makeAll(dictionaryEntry);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_NA) {
			return AdjectiveNaGrammaConjugater.makeAll(dictionaryEntry);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_NOUN) {
			return NounGrammaConjugater.makeAll(dictionaryEntry);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U ||
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU ||
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			return VerbGrammaConjugater.makeAll(dictionaryEntry);
		}

		return null;
	}
}