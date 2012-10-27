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
		
		// tsu - klasyfikator ogólny
		result.add(createTsuCounter(resources));
		
		// ko - małe przedmioty
		result.add(createKoCounter(resources));
		
		// satsu - wolumeny
		result.add(createSatsuCounter(resources));
		
		// hiki - małe zwierzęta
		result.add(createHikiCounter(resources));
		
		// hon - długie przedmioty
		result.add(createHonCounter(resources));
		
		// dai - urządzenia
		result.add(createDaiCounter(resources));
		
		// mai - płaskie przedmioty
		result.add(createMaiCounter(resources));
		
		return result;
	}
	
	private CounterEntry createTsuCounter(Resources resources) {
		
		CounterEntry counterEntry = new CounterEntry("つ", "つ", "tsu", resources.getString(R.string.counter_tsu_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一つ", "ひとつ", "hitotsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二つ", "ふたつ", "futatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三つ", "みっつ", "mittsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四つ", "よっつ", "yottsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五つ", "いつつ", "itsutsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六つ", "むっつ", "muttsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七つ", "ななつ", "nanatsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八つ", "やっつ", "yattsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九つ", "ここのつ", "kokonotsu"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十", "とお", "too"));
		
		return counterEntry;
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
	
	private CounterEntry createHonCounter(Resources resources) {
		
		CounterEntry counterEntry = new CounterEntry("本", "ほん", "hon", resources.getString(R.string.counter_hon_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一本", "いっぽん", "ippon"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二本", "にほん", "nihon"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三本", "さんぼん", "sanbon"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四本", "よんほん", "yonhon"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五本", "ごほん", "gohon"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六本", "ろっぽん", "roppon"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七本", "ななほん", "nanahon"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八本", "はっぽん", "happon"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九本", "きゅうほん", "kyuuhon"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十本", "じゅっぽん", "juppon"));
		
		counterEntry.getEntries().add(null);
				
		counterEntry.getEntries().add(new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何本", "なんぼん", "nanbon"));
		
		String[] examplesArray = resources.getStringArray(R.array.counter_hon_examples);
		
		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}
		
		return counterEntry;
	}
	
	private CounterEntry createDaiCounter(Resources resources) {
		
		CounterEntry counterEntry = new CounterEntry("台", "だい", "dai", resources.getString(R.string.counter_dai_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一台", "いちだい", "ichidai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二台", "にだい", "nidai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三台", "さんだい", "sandai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四台", "よんだい", "yondai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五台", "ごだい", "godai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六台", "ろくだい", "rokudai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七台", "ななだい", "nanadai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八台", "はちだい", "hachidai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九台", "きゅうだい", "kyuudai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十台", "じゅうだい", "juudai"));
		
		counterEntry.getEntries().add(null);
		
		counterEntry.getEntries().add(new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何台", "なんだい", "nandai"));
		
		String[] examplesArray = resources.getStringArray(R.array.counter_dai_examples);
		
		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}
		
		return counterEntry;
	}
	
	private CounterEntry createMaiCounter(Resources resources) {
		
		CounterEntry counterEntry = new CounterEntry("枚", "まい", "mai", resources.getString(R.string.counter_mai_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一枚", "いちまい", "ichimai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二枚", "にまい", "nimai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三枚", "さんまい", "sanmai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四枚", "よんまい", "yonmai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五枚", "ごまい", "gomai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六枚", "ろくまい", "rokumai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七枚", "ななまい", "nanamai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八枚", "はちまい", "hachimai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九枚", "きゅうまい", "kyuumai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十枚", "じゅうまい", "juumai"));
		
		counterEntry.getEntries().add(null);
		
		counterEntry.getEntries().add(new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何枚", "なんまい", "nanmai"));
		
		String[] examplesArray = resources.getStringArray(R.array.counter_mai_examples);
		
		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}
		
		return counterEntry;
	}
}
