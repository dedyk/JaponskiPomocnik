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
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache,
			DictionaryEntryType forceDictionaryEntryType) {

		DictionaryEntryType dictionaryEntryType = null;

		if (forceDictionaryEntryType == null) {
			dictionaryEntryType = dictionaryEntry.getDictionaryEntryType();
		} else {
			dictionaryEntryType = forceDictionaryEntryType;
		}

		List<ExampleGroupTypeElements> result = null;

		if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_I) {
			result = AdjectiveIExampler.makeAll(dictionaryEntry, grammaFormCache);

		} else if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_NA) {
			result = AdjectiveNaExampler.makeAll(dictionaryEntry, grammaFormCache);

		} else if (dictionaryEntryType == DictionaryEntryType.WORD_NOUN) {
			result = NounExampler.makeAll(dictionaryEntry, grammaFormCache);

		} else if (dictionaryEntryType == DictionaryEntryType.WORD_VERB_U
				|| dictionaryEntryType == DictionaryEntryType.WORD_VERB_RU
				|| dictionaryEntryType == DictionaryEntryType.WORD_VERB_IRREGULAR) {

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
