package pl.idedyk.android.japaneselearnhelper.example;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.dictionary.KeigoHelper;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;

public class ExampleManager {

	public static List<ExampleGroupTypeElements> getExamples(KeigoHelper keigoHelper, DictionaryEntry dictionaryEntry,
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache) {

		List<ExampleGroupTypeElements> result = null;

		if (dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_ADJECTIVE_I) == true) {
			result = AdjectiveIExampler.makeAll(dictionaryEntry, grammaFormCache);

		} else if (dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_ADJECTIVE_NA) == true) {
			result = AdjectiveNaExampler.makeAll(dictionaryEntry, grammaFormCache);

		} else if (dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_NOUN) == true) {
			result = NounExampler.makeAll(dictionaryEntry, grammaFormCache);

		} else if (dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_VERB_U) == true
				|| dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_VERB_RU) == true
				|| dictionaryEntry.isDictionaryEntryType(DictionaryEntryType.WORD_VERB_IRREGULAR) == true) {

			result = VerbExampler.makeAll(keigoHelper, dictionaryEntry, grammaFormCache);
		}

		if (result != null) {

			Collections.sort(result, new Comparator<ExampleGroupTypeElements>() {

				private final Collator collator = Collator.getInstance(Locale.getDefault());

				@Override
				public int compare(ExampleGroupTypeElements lhs, ExampleGroupTypeElements rhs) {
					return collator.compare(lhs.getExampleGroupType().getName(), rhs.getExampleGroupType().getName());
				}
			});
		}

		return result;
	}
}
