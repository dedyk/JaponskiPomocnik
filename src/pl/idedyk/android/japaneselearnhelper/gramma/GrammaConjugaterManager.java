package pl.idedyk.android.japaneselearnhelper.gramma;

import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.KeigoHelper;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;

public class GrammaConjugaterManager {

	public static List<GrammaFormConjugateGroupTypeElements> getGrammaConjufateResult(KeigoHelper keigoHelper,
			DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {

		if (dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_ADJECTIVE_I) == true) {
			return AdjectiveIGrammaConjugater.makeAll(dictionaryEntry, grammaFormCache);

		} else if (dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_ADJECTIVE_NA) == true) {
			return AdjectiveNaGrammaConjugater.makeAll(dictionaryEntry, grammaFormCache);

		} else if (dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_NOUN) == true) {
			return NounGrammaConjugater.makeAll(dictionaryEntry, grammaFormCache);

		} else if (dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_VERB_U) == true
				|| dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_VERB_RU) == true
				|| dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_VERB_IRREGULAR) == true) {

			return VerbGrammaConjugater.makeAll(keigoHelper, dictionaryEntry, grammaFormCache);
		}

		return null;
	}
}
