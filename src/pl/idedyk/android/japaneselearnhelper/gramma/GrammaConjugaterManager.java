package pl.idedyk.android.japaneselearnhelper.gramma;

import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;

public class GrammaConjugaterManager {
	
	public static List<GrammaFormConjugateGroupTypeElements> getGrammaConjufateResult(DictionaryManager dictionaryManager, DictionaryEntry dictionaryEntry, 
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {
		
		DictionaryEntryType dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		
		if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_I) {
			return AdjectiveIGrammaConjugater.makeAll(dictionaryEntry, grammaFormCache);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_NA) {
			return AdjectiveNaGrammaConjugater.makeAll(dictionaryEntry, grammaFormCache);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_NOUN) {
			return NounGrammaConjugater.makeAll(dictionaryEntry, grammaFormCache);
		
		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U ||
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU ||
				dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {
			
			return VerbGrammaConjugater.makeAll(dictionaryManager, dictionaryEntry, grammaFormCache);
		}

		return null;
	}
}
