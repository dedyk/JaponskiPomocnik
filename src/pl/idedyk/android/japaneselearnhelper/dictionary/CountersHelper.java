package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.CounterEntry;
import android.content.res.Resources;

public class CountersHelper {

	public List<CounterEntry> getAllCounters(Resources resources) {

		List<CounterEntry> result = new ArrayList<CounterEntry>();

		// kolumny:
		// A1 = counterEntry.getEntries().add(new CounterEntry.Entry("
		// B1 = ", "
		// C1 = "));

		// kolumny:
		// A2 - liczba (numer) 1, 2, 3 ...
		// B2 - kanji
		// C2 - kana
		// D2 - romaji
		// E2 - liczba (słownie)
		// F2 - opis

		// wzór dla komorki G2: =ZŁĄCZ.TEKSTY($A$1, A2, $B$1,B2 ,$B$1,C2, $B$1, D2, $C$1)

		// tsu - klasyfikator ogólny
		result.add(createTsuCounter(resources));

		// ji - godziny
		result.add(createJiCounter(resources));

		// fun - minuty
		result.add(createFunCounter(resources));

		// sai - lata
		result.add(createSaiCounter(resources));

		// nichi - dzień miesiąca
		result.add(createNichiCounter(resources));

		// nin - ludzi
		result.add(createNinCounter(resources));

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

		// dan - stopnie, rangi, poziomy
		result.add(createDanCounter(resources));

		// kai - numer piętra
		result.add(createKaiCounter(resources));

		// ken - numer domu, budynku
		result.add(createKenCounter(resources));

		// ho - kroki
		result.add(createHoCounter(resources));

		// hai - łyżki, kubki, miski, szklanki
		result.add(createHaiCounter(resources));

		// ki - samoloty
		result.add(createKiCounter(resources));

		return result;
	}

	private CounterEntry createTsuCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("つ", "つ", "tsu",
				resources.getString(R.string.counter_tsu_description));

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

	private CounterEntry createJiCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("時", "じ", "ji",
				resources.getString(R.string.counter_ji_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一時", "いちじ", "ichiji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二時", "にじ", "niji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三時", "さんじ", "sanji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四時", "よじ", "yoji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五時", "ごじ", "goji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六時", "ろくじ", "rokuji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七時", "しちじ", "shichiji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八時", "はちじ", "hachiji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九時", "くじ", "kuji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十時", "じゅうじ", "juuji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("11", "十一時", "じゅういちじ", "juuichiji"));
		counterEntry.getEntries().add(new CounterEntry.Entry("12", "十二時", "じゅうにじ", "juuniji"));

		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counter_ji_which_time), "何時", "なんじ", "nanji"));

		return counterEntry;
	}

	private CounterEntry createFunCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("分", "ふん", "fun",
				resources.getString(R.string.counter_fun_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一分", "いっぷん", "ippun"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二分", "にふん", "nifun"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三分", "さんぷん", "sanpun"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四分", "よんぷん", "yonpun"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五分", "ごふん", "gofun"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六分", "ろっぷん", "roppun"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七分", "ななふん", "nanafun"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八分", "はっぷん", "happun"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八分", "はちふん", "hachifun"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九分", "きゅうふん", "kyuufun"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十分", "じゅっぷん", "juppun"));

		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counter_fun_which_minute), "何分", "なんぷん", "nanpun"));

		return counterEntry;
	}

	private CounterEntry createSaiCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("歳", "さい", "sai",
				resources.getString(R.string.counter_sai_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一歳", "いっさい", "issai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二歳", "にさい", "nisai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三歳", "さんさい", "sansai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四歳", "よんさい", "yonsai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五歳", "ごさい", "gosai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六歳", "ろくさい", "rokusai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七歳", "ななさい", "nanasai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八歳", "はっさい", "hassai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九歳", "きゅうさい", "kyuusai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十歳", "じゅっさい", "jussai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("11", "十一歳", "じゅういっさい", "juuissai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("20", "二十歳", "はたち", "hatachi"));

		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counter_sai_howOld), "何歳", "なんさい", "nansai"));

		return counterEntry;
	}

	private CounterEntry createNichiCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("日", "にち", "nichi",
				resources.getString(R.string.counter_nichi_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一日", "ついたち", "tsuitachi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二日", "ふつか", "futsuka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三日", "みっか", "mikka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四日", "よっか", "yokka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五日", "いつか", "itsuka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六日", "むいか", "muika"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七日", "なのか", "nanoka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八日", "ようか", "youka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九日", "ここのか", "kokonoka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十日", "とおか", "tooka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("11", "十一日", "じゅういちにち", "juuichinichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("12", "十二日", "じゅうににち", "juuninichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("13", "十三日", "じゅうさんにち", "juusannichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("14", "十四日", "じゅうよっか", "juuyokka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("15", "十五日", "じゅうごにち", "juugonichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("16", "十六日", "じゅうろくにち", "juurokunichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("17", "十七日", "じゅうしちにち", "juushichinichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("18", "十八日", "じゅうはちにち", "juuhachinichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("19", "十九日", "じゅうくにち", "juukunichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("20", "二十日", "はつか", "hatsuka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("21", "二十一日", "にじゅういちにち", "nijuuichinichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("22", "二十二日", "にじゅうににち", "nijuuninichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("23", "二十三日", "にじゅうさんにち", "nijuusannichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("24", "二十四日", "にじゅうよっか", "nijuuyokka"));
		counterEntry.getEntries().add(new CounterEntry.Entry("25", "二十五日", "にじゅうごにち", "nijuugonichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("26", "二十六日", "にじゅうろくにち", "nijuurokunichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("27", "二十七日", "にじゅうしちにち", "nijuushichinichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("28", "二十八日", "にじゅうはちにち", "nijuuhachinichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("29", "二十九日", "にじゅうくにち", "nijuukunichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("30", "三十日", "さんじゅうにち", "sanjuunichi"));
		counterEntry.getEntries().add(new CounterEntry.Entry("31", "三十一日", "さんじゅういちにち", "sanjuuichinichi"));

		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counter_nichi_which), "何日", "なんにち", "nannichi"));

		return counterEntry;
	}

	private CounterEntry createNinCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("人", "にん", "nin",
				resources.getString(R.string.counter_nin_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一人", "ひとり", "hitori"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二人", "ふたり", "futari"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三人", "さんにん", "sannin"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四人", "よにん", "yonin"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五人", "ごにん", "gonin"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六人", "ろくにん", "rokunin"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七人", "しちにん", "shichinin"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七人", "ななにん", "nananin"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八人", "はちにん", "hachinin"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九人", "きゅうにん", "kyuunin"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十人", "じゅうにん", "juunin"));

		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counter_nin_how_many), "何人", "なんにん", "nan'nin"));

		return counterEntry;
	}

	private CounterEntry createKoCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("個", "こ", "ko",
				resources.getString(R.string.counter_ko_description));

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

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何個", "なんこ", "nanko"));
		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "", "いくつ", "ikutsu"));

		String[] examplesArray = resources.getStringArray(R.array.counter_ko_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}

	private CounterEntry createSatsuCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("冊", "さつ", "satsu",
				resources.getString(R.string.counter_satsu_description));

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

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何冊", "なんさつ", "nansatsu"));

		String[] examplesArray = resources.getStringArray(R.array.counter_satsu_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}

	private CounterEntry createHikiCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("匹", "ひき", "hiki",
				resources.getString(R.string.counter_hiki_description));

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

		CounterEntry counterEntry = new CounterEntry("本", "ほん", "hon",
				resources.getString(R.string.counter_hon_description));

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

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何本", "なんぼん", "nanbon"));

		String[] examplesArray = resources.getStringArray(R.array.counter_hon_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}

	private CounterEntry createDaiCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("台", "だい", "dai",
				resources.getString(R.string.counter_dai_description));

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

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何台", "なんだい", "nandai"));

		String[] examplesArray = resources.getStringArray(R.array.counter_dai_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}

	private CounterEntry createMaiCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("枚", "まい", "mai",
				resources.getString(R.string.counter_mai_description));

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

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何枚", "なんまい", "nanmai"));

		String[] examplesArray = resources.getStringArray(R.array.counter_mai_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}

	private CounterEntry createDanCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("段", "だん", "dan",
				resources.getString(R.string.counter_dan_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一段", "いちだん ", "ichidan"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二段", "にだん", "nidan"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三段", "さんだん", "sandan"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四段", "よだん", "yodan"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五段", "ごだん", "godan"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六段", "ろくだん", "rokudan"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七段", "しちだん", "shichidan"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八段", "はったん", "hattan"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九段", "くだん ", "kudan"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十段", "じゅうだん", "juudan"));

		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_which), "何段", "なんだん", "nandan"));

		String[] examplesArray = resources.getStringArray(R.array.counter_dan_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}

	private CounterEntry createKaiCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("階", "かい", "kai",
				resources.getString(R.string.counter_kai_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一階", "いっかい", "ikkai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二階", "にかい", "nikai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三階", "さんかい", "sankai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三階", "さんがい", "sangai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四階", "よんかい", "yonkai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五階", "ごかい", "gokai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六階", "ろっかい", "rokkai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七階", "ななかい", "nanakai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八階", "はちかい", "hachikai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九階", "きゅうかい", "kyuukai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十階", "じゅっかい", "jukkai"));

		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_which2), "何階", "なんかい", "nankai"));

		String[] examplesArray = resources.getStringArray(R.array.counter_kai_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}

	private CounterEntry createKenCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("軒", "けん", "ken",
				resources.getString(R.string.counter_ken_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一軒", "いっけん ", "ikken"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二軒", "にけん", "niken"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三軒", "さんげん ", "sangen"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四軒", "よんけん", "yonken"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五軒", "ごけん ", "goken"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六軒", "ろっけん", "rokken"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七軒", "ななけん", "nanaken"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八軒", "はっけん", "hakken"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九軒", "きゅうけん", "kyuuken"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十軒", "じゅっけん", "jukken"));
		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何軒", "なんけん", "nanken"));

		String[] examplesArray = resources.getStringArray(R.array.counter_ken_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}

	private CounterEntry createHoCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("歩", "ぼ", "bo",
				resources.getString(R.string.counter_bo_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一歩", "いっぽ", "ippo"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二歩", "にほ", "niho"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三歩", "さんぽ", "sanpo"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四歩", "よんほ", "yonho"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五歩", "ごほ", "goho"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六歩", "ろっぽ", "roppo"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七歩", "ななほ", "nanaho"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八歩", "はっぽ", "happo"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九歩", "きゅうほ", "kyuuho"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十歩", "じっぽ ", "jippo"));

		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何歩", "なんぽ", "nanpo"));

		String[] examplesArray = resources.getStringArray(R.array.counter_bo_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}

	private CounterEntry createHaiCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("杯", "はい", "hai",
				resources.getString(R.string.counter_hai_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一杯", "いっぱい", "ippai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二杯", "にはい", "nihai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三杯", "さんばい", "sanbai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四杯", "よんはい", "yonhai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五杯", "ごはい", "gohai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六杯", "ろっぱい", "roppai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七杯", "ななはい", "nanahai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八杯", "はっぱい", "happai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九杯", "きゅうはい", "kyuuhai"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十杯", "じっぱい", "jippai"));

		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何杯", "なんはい", "nanhai"));

		String[] examplesArray = resources.getStringArray(R.array.counter_hai_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}

	private CounterEntry createKiCounter(Resources resources) {

		CounterEntry counterEntry = new CounterEntry("機", "き", "ki",
				resources.getString(R.string.counter_ki_description));

		counterEntry.getEntries().add(new CounterEntry.Entry("1", "一機", "いっき", "ikki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("2", "二機", "にき", "niki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("3", "三機", "さんき", "sanki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("4", "四機", "よんき", "yonki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("5", "五機", "ごき", "goki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("6", "六機", "ろっき", "rokki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("7", "七機", "ななき", "nanaki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("8", "八機", "はっき", "hakki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("9", "九機", "きゅうき", "kyuuki"));
		counterEntry.getEntries().add(new CounterEntry.Entry("10", "十機", "じゅっき", "jukki"));

		counterEntry.getEntries().add(null);

		counterEntry.getEntries().add(
				new CounterEntry.Entry(resources.getString(R.string.counters_how_many), "何機", "なんき", "nanki"));

		String[] examplesArray = resources.getStringArray(R.array.counter_ki_examples);

		for (String currentExample : examplesArray) {
			counterEntry.getExampleUse().add(currentExample);
		}

		return counterEntry;
	}
}
