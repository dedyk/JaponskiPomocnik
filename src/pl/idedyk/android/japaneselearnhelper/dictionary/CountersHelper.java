package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.CounterEntry;

public class CountersHelper {
	
	public List<CounterEntry> getAllCounters(Resources resources) {
		
		List<CounterEntry> result = new ArrayList<CounterEntry>();
		
		result.add(createKoCounter(resources));
		
		return result;
	}
	
	private CounterEntry createKoCounter(Resources resources) {
		
		CounterEntry counterEntry = new CounterEntry("個", "こ", "ko", resources.getString(R.string.counter_ko_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一個", "いっこ", "ikko"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二個", "にこ", "niko"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三個", "さんこ", "sanko"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四個", "よんこ", "yonko"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五個", "ごこ", "goko"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六個", "ろっこ", "rokko"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七個", "ななこ", "nanako"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八個", "はっこ", "hakko"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九個", "きゅうこ", "kyuuko"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十個", "じゅっこ", "jukko"));
		
		counterEntry.getEntries().add(null);
		
		counterEntry.getEntries().add(new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何個", "なんこ", "nanko"));
		counterEntry.getEntries().add(new CounterEntry.Entry(resources.getString(R.string.counters_how_many), null, "いくつ", "ikutsu"));
		
		String[] examplesArray = resources.getStringArray(R.array.counter_ko_examples);
		
		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}
		
		return counterEntry;
	}	
}
