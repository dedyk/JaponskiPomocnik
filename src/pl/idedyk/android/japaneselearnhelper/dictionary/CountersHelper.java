package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.List;

import android.content.res.Resources;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.CounterEntry;

public class CountersHelper {
	
	public List<CounterEntry> getAllCounters(Resources resources) {
		
		List<CounterEntry> result = new ArrayList<CounterEntry>();
		
		// =ZŁĄCZ.TEKSTY(G1, E1, H1,A1,I1,B1, J1, D1,K1)
		// counterEntry.getEntries().add(new CounterEntry.Entry("	", "	", "	", "	"));
		
		// ko - małe przedmioty
		result.add(createKoCounter(resources));
		
		// satsu - wolumeny
		result.add(createSatsuCounter(resources));
		
		// hiki - małe zwierzęta
		result.add(createHikiCounter(resources));
		
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

	private CounterEntry createSatsuCounter(Resources resources) {
		
		CounterEntry counterEntry = new CounterEntry("冊", "さつ", "satsu", resources.getString(R.string.counter_satsu_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一冊", "いっさつ", "issatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二冊", "にさつ", "nisatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三冊", "さんさつ", "sansatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四冊", "よんさつ", "yonsatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五冊", "ごさつ", "gosatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六冊", "ろくさつ", "rokusatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七冊", "ななさつ", "nanasatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八冊", "はっさつ", "hassatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九冊", "きゅうさつ", "kyuusatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十冊", "じゅっさつ", "jussatsu"));
		
		counterEntry.getEntries().add(null);
		
		counterEntry.getEntries().add(new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何冊", "なんさつ", "nansatsu"));
		
		String[] examplesArray = resources.getStringArray(R.array.counter_satsu_examples);
		
		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}
		
		return counterEntry;
	}	
	
	private CounterEntry createHikiCounter(Resources resources) {
		
		CounterEntry counterEntry = new CounterEntry("匹", "ひき", "hiki", resources.getString(R.string.counter_hiki_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一匹", "いっぴき", "ippiki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二匹", "にひき", "nihiki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三匹", "さんびき", "sanbiki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四匹", "よんひき", "yonhiki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五匹", "ごひき", "gohiki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六匹", "ろっぴき", "roppiki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七匹", "ななひき", "nanahiki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八匹", "はっぴき", "happiki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九匹", "きゅうひき", "kyuuhiki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十匹", "じゅっぴき", "juppiki"));
		
		counterEntry.getEntries().add(null);
		
		counterEntry.getEntries().add(new CounterEntry.Entry("jak wiele", "何匹", "なんびき", "nanbiki"));
		
		String[] examplesArray = resources.getStringArray(R.array.counter_hiki_examples);
		
		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}
		
		return counterEntry;
	}
}
