package pl.idedyk.android.japaneselearnhelper.dictionary;

public class SQLiteStatic {
	
	public static int MAX_SEARCH_RESULT = 51;

	public static final String databaseName = "JapaneseAndroidLearnHelperDb";
	
	public static final String dictionaryEntriesTableName = "DictionaryEntries";
	
	public static final String dictionaryEntriesTable_id = "id";
	public static final String dictionaryEntriesTable_dictionaryEntryType = "dictionaryEntryType";
	public static final String dictionaryEntriesTable_prefixKana = "prefixKana";
	public static final String dictionaryEntriesTable_kanji = "kanji";
	public static final String dictionaryEntriesTable_kanaList = "kanaList";
	public static final String dictionaryEntriesTable_prefixRomaji = "prefixRomaji";
	public static final String dictionaryEntriesTable_romajiList = "romajiList";
	public static final String dictionaryEntriesTable_translates = "translates";
	public static final String dictionaryEntriesTable_info = "info";
	
	public static final String kanjiEntriesTableName = "KanjiEntries";
	
	public static final String kanjiEntriesTable_id = "id";
	public static final String kanjiEntriesTable_kanji = "kanji";
	public static final String kanjiEntriesTable_strokeCount = "strokeCount";
	public static final String kanjiEntriesTable_radicals = "radicals";
	public static final String kanjiEntriesTable_onReading = "onReading";
	public static final String kanjiEntriesTable_kunReading = "kunReading";
	public static final String kanjiEntriesTable_strokePaths = "strokePaths";
	public static final String kanjiEntriesTable_polishTranslates = "polishTranslates";
	public static final String kanjiEntriesTable_info = "info";
	
	public static final String[] kanjiEntriesTableAllColumns = new String [] {
			SQLiteStatic.kanjiEntriesTable_id,
			SQLiteStatic.kanjiEntriesTable_kanji,
			SQLiteStatic.kanjiEntriesTable_strokeCount,
			SQLiteStatic.kanjiEntriesTable_radicals,
			SQLiteStatic.kanjiEntriesTable_onReading,
			SQLiteStatic.kanjiEntriesTable_kunReading,
			SQLiteStatic.kanjiEntriesTable_strokePaths,
			SQLiteStatic.kanjiEntriesTable_polishTranslates,
			SQLiteStatic.kanjiEntriesTable_info
	};
	
	public static final String dictionaryEntriesTableCreate = 
			"create table " + dictionaryEntriesTableName + "(" +
			dictionaryEntriesTable_id + " integer primary key, " +
			dictionaryEntriesTable_dictionaryEntryType + " text not null, " +
			dictionaryEntriesTable_prefixKana + " text not null, " +
			dictionaryEntriesTable_kanji + " text not null, " +
			dictionaryEntriesTable_kanaList + " text not null, " +
			dictionaryEntriesTable_prefixRomaji + " text not null, " +
			dictionaryEntriesTable_romajiList + " text not null, " +
			dictionaryEntriesTable_translates + " text not null, " +
			dictionaryEntriesTable_info + " text not null);";
	
	public static final String kanjiEntriesTableCreate =
			"create table " + kanjiEntriesTableName + "(" +
			kanjiEntriesTable_id + " integer primary key, " +
			kanjiEntriesTable_kanji + " text unique not null, " +
			kanjiEntriesTable_strokeCount + " text not null, " +
			kanjiEntriesTable_radicals + " text not null, " +
			kanjiEntriesTable_onReading + " text not null, " +
			kanjiEntriesTable_kunReading + " text not null, " +
			kanjiEntriesTable_strokePaths + " text not null, " +
			kanjiEntriesTable_polishTranslates + " text not null, " +
			kanjiEntriesTable_info + " text not null);";
	
	public static final String dictionaryEntriesTableCreateCount = 
			"select count(*) from " + dictionaryEntriesTableName;
	
	public static final String dictionaryEntriesTableNthElement = 
			"select " + 
			dictionaryEntriesTable_id + ", " +
			dictionaryEntriesTable_dictionaryEntryType + ", " +
			dictionaryEntriesTable_prefixKana + ", " +
			dictionaryEntriesTable_kanji + ", " +
			dictionaryEntriesTable_kanaList + ", " +
			dictionaryEntriesTable_prefixRomaji + ", " +
			dictionaryEntriesTable_romajiList + ", " +
			dictionaryEntriesTable_translates + ", " +
			dictionaryEntriesTable_info + " " +
			"from " + dictionaryEntriesTableName + " limit 1 offset ?";
	
	public static final String dictionaryEntriesTableSelectElements = 
			"select " + 
			dictionaryEntriesTable_id + ", " +
			dictionaryEntriesTable_dictionaryEntryType + ", " +
			dictionaryEntriesTable_prefixKana + ", " +
			dictionaryEntriesTable_kanji + ", " +
			dictionaryEntriesTable_kanaList + ", " +
			dictionaryEntriesTable_prefixRomaji + ", " +
			dictionaryEntriesTable_romajiList + ", " +
			dictionaryEntriesTable_translates + ", " +
			dictionaryEntriesTable_info + " " +
			"from " + dictionaryEntriesTableName + " " +
			" where " + 
			dictionaryEntriesTable_kanji + " like ? or " +
			dictionaryEntriesTable_kanaList + " like ? or" +
			" lower(" + dictionaryEntriesTable_romajiList + ") like ? or" +
			" lower(" + dictionaryEntriesTable_translates + ") like ? or" +
			" lower(" + dictionaryEntriesTable_info + ") like ? limit " + (MAX_SEARCH_RESULT - 1);					
}