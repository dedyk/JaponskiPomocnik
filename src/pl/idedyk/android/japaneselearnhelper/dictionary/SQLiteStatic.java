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
