package pl.idedyk.android.japaneselearnhelper.dictionary;

public class SQLiteStatic {
	
	public static int MAX_SEARCH_RESULT = 51;
	
	public static int MAX_KANJI_STROKE_COUNT_RESULT = 201;

	public static final String databaseName = "JapaneseAndroidLearnHelperDb";
	
	public static final String listEntriesTableName = "ListEntries";
	
	public static final String listEntriesTable_id = "id";
	public static final String listEntriesTable_type = "type";
	public static final String listEntriesTable_subType = "subType";
	public static final String listEntriesTable_key = "key";
	public static final String listEntriesTable_value = "value";
	public static final String listEntriesTable_special = "special";
	
	public static final String dictionaryEntriesTableName = "DictionaryEntries";
	
	public static final String dictionaryEntriesTable_id = "id";
	public static final String dictionaryEntriesTable_dictionaryEntryType = "dictionaryEntryType";
	public static final String dictionaryEntriesTable_attributeList = "attributeList";
	public static final String dictionaryEntriesTable_groups = "groups";
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
	public static final String kanjiEntriesTable_generated = "generated";
	public static final String kanjiEntriesTable_groups = "groups";
	
	public static final String grammaFormConjugateGroupTypeEntriesTableName = "GrammaFormConjugateGroupTypeEntries";
	
	public static final String grammaFormConjugateGroupTypeEntriesTable_id = "id";
	public static final String grammaFormConjugateGroupTypeEntriesTable_dictionaryEntryId = "dictionaryEntryId";
	public static final String grammaFormConjugateGroupTypeEntriesTable_grammaFormConjugateGroupType = "grammaFormConjugateGroupType";	
	
	public static final String grammaFormConjugateResultEntriesTableName = "GrammaFormConjugateResultEntries";
	
	public static final String grammaFormConjugateResultEntriesTable_id = "id";
	public static final String grammaFormConjugateResultEntriesTable_grammaFormConjugateGroupTypeEntriesId = "grammaFormConjugateGroupTypeEntriesId";
	public static final String grammaFormConjugateResultEntriesTable_grammaFormConjugateResultEntriesParentId = "grammaFormConjugateResultEntriesParentId";
	public static final String grammaFormConjugateResultEntriesTable_resultType = "resultType";
	public static final String grammaFormConjugateResultEntriesTable_prefixKana = "prefixKana";
	public static final String grammaFormConjugateResultEntriesTable_kanji = "kanji";
	public static final String grammaFormConjugateResultEntriesTable_kanaList = "kanaList";
	public static final String grammaFormConjugateResultEntriesTable_prefixRomaji = "prefixRomaji";
	public static final String grammaFormConjugateResultEntriesTable_romajiList = "romajiList";
	
	public static final String exampleGroupTypeEntriesTableName = "ExampleGroupTypeEntries";
	
	public static final String exampleGroupTypeEntriesTable_id = "id";
	public static final String exampleGroupTypeEntriesTable_dictionaryEntryId = "dictionaryEntryId";
	public static final String exampleGroupTypeEntriesTable_exampleGroupType = "exampleGroupType";
	
	public static final String exampleResultEntriesTableName = "ExampleResultEntries";
	
	public static final String exampleResultEntriesTable_id = "id";
	public static final String exampleResultEntriesTable_exampleGroupTypeEntriesId = "exampleGroupTypeEntriesId";
	public static final String exampleResultEntriesTable_exampleResultEntriesParentId = "exampleResultEntriesParentId";
	public static final String exampleResultEntriesTable_prefixKana = "prefixKana";
	public static final String exampleResultEntriesTable_kanji = "kanji";
	public static final String exampleResultEntriesTable_kanaList = "kanaList";
	public static final String exampleResultEntriesTable_prefixRomaji = "prefixRomaji";
	public static final String exampleResultEntriesTable_romajiList = "romajiList";
	
	public static final String[] kanjiEntriesTableAllColumns = new String [] {
			SQLiteStatic.kanjiEntriesTable_id,
			SQLiteStatic.kanjiEntriesTable_kanji,
			SQLiteStatic.kanjiEntriesTable_strokeCount,
			//SQLiteStatic.kanjiEntriesTable_radicals,
			//SQLiteStatic.kanjiEntriesTable_onReading,
			//SQLiteStatic.kanjiEntriesTable_kunReading,
			SQLiteStatic.kanjiEntriesTable_strokePaths,
			//SQLiteStatic.kanjiEntriesTable_polishTranslates,
			//SQLiteStatic.kanjiEntriesTable_info,
			SQLiteStatic.kanjiEntriesTable_generated,
			//SQLiteStatic.kanjiEntriesTable_groups
	};
	
	public static final String listEntriesTableCreate =
			"create virtual table " + listEntriesTableName + " using fts3(" +
			listEntriesTable_id + " integer primary key autoincrement, " +
			listEntriesTable_type + " text not null, " +
			listEntriesTable_subType + " text not null, " +
			listEntriesTable_key + " text not null, " +
			listEntriesTable_value + " text not null, " +
			listEntriesTable_special + " boolean not null);";

	public static final String listEntriesTableCreateAllIndex = 
			"create index " + listEntriesTableName + "AllIdx on " +
			listEntriesTableName + "(" + listEntriesTable_type + ", " + listEntriesTable_subType + ", " + listEntriesTable_key + ")";

	public static final String listEntriesTableCreateTypeKeyIndex = 
			"create index " + listEntriesTableName + "TypeKeyIdx on " +
			listEntriesTableName + "(" + listEntriesTable_type + ", " + listEntriesTable_key + ")";

	/*
	public static final String listEntriesTableCreateTypeIndex = 
			"create index " + listEntriesTableName + listEntriesTable_type.substring(0, 1).toUpperCase() + listEntriesTable_type.substring(1) + "Idx on " +
			listEntriesTableName + "(" + listEntriesTable_type + ")";

	public static final String listEntriesTableCreateSubTypeIndex = 
			"create index " + listEntriesTableName + listEntriesTable_subType.substring(0, 1).toUpperCase() + listEntriesTable_subType.substring(1) + "Idx on " +
			listEntriesTableName + "(" + listEntriesTable_subType + ")";
	
	public static final String listEntriesTableCreateKeyIndex = 
			"create index " + listEntriesTableName + listEntriesTable_key.substring(0, 1).toUpperCase() + listEntriesTable_key.substring(1) + "Idx on " +
			listEntriesTableName + "(" + listEntriesTable_key + ")";

	public static final String listEntriesTableCreateValueIndex = 
			"create index " + listEntriesTableName + listEntriesTable_value.substring(0, 1).toUpperCase() + listEntriesTable_value.substring(1) + "Idx on " +
			listEntriesTableName + "(" + listEntriesTable_value + ")";
	*/
	
	public static final String dictionaryEntriesTableCreate = 
			"create table " + dictionaryEntriesTableName + "(" +
			dictionaryEntriesTable_id + " integer primary key, " +
			dictionaryEntriesTable_dictionaryEntryType + " text not null, " +
			dictionaryEntriesTable_prefixKana + " text not null, " +
			dictionaryEntriesTable_kanji + " text not null, " +
			//dictionaryEntriesTable_kanaList + " text not null, " +
			dictionaryEntriesTable_prefixRomaji + " text not null, " +
			//dictionaryEntriesTable_romajiList + " text not null, " +
			//dictionaryEntriesTable_translates + " text not null, " +
			dictionaryEntriesTable_info + " text not null);";
	
	public static final String kanjiEntriesTableCreate =
			"create table " + kanjiEntriesTableName + "(" +
			kanjiEntriesTable_id + " integer primary key, " +
			kanjiEntriesTable_kanji + " text unique not null, " +
			kanjiEntriesTable_strokeCount + " int not null, " +
			//kanjiEntriesTable_radicals + " text not null, " +
			//kanjiEntriesTable_onReading + " text not null, " +
			//kanjiEntriesTable_kunReading + " text not null, " +
			kanjiEntriesTable_strokePaths + " text not null, " +
			//kanjiEntriesTable_polishTranslates + " text not null, " +
			//kanjiEntriesTable_info + " text not null, " +
			kanjiEntriesTable_generated + " text not null);";
			//kanjiEntriesTable_groups + " text null);";
	
	public static final String grammaFormConjugateGroupTypeEntriesTableCreate =
			"create table " + grammaFormConjugateGroupTypeEntriesTableName + "(" +
			grammaFormConjugateGroupTypeEntriesTable_id + " integer primary key, " +
			grammaFormConjugateGroupTypeEntriesTable_dictionaryEntryId + " integer not null, " +
			grammaFormConjugateGroupTypeEntriesTable_grammaFormConjugateGroupType + " text not null);";
	
	public static final String grammaFormConjugateResultEntriesTableNameCreate =
			"create table " + grammaFormConjugateResultEntriesTableName + "(" +
			grammaFormConjugateResultEntriesTable_id + " integer primary key, " +
			grammaFormConjugateResultEntriesTable_grammaFormConjugateGroupTypeEntriesId + " integer not null, " +
			grammaFormConjugateResultEntriesTable_grammaFormConjugateResultEntriesParentId + " integer null, " +
			grammaFormConjugateResultEntriesTable_resultType + " text not null, " +
			grammaFormConjugateResultEntriesTable_prefixKana + " text null, " +
			grammaFormConjugateResultEntriesTable_kanji + " text null, " +
			grammaFormConjugateResultEntriesTable_kanaList + " text not null, " +
			grammaFormConjugateResultEntriesTable_prefixRomaji + " text null, " +
			grammaFormConjugateResultEntriesTable_romajiList + " text not null);";
	
	public static final String exampleGroupTypeEntriesTableCreate =
			"create table " + exampleGroupTypeEntriesTableName + "(" +
			exampleGroupTypeEntriesTable_id + " integer primary key, " +
			exampleGroupTypeEntriesTable_dictionaryEntryId + " integer not null, " +
			exampleGroupTypeEntriesTable_exampleGroupType + " text not null);";
	
	public static final String exampleResultEntriesTableCreate =
			"create table " + exampleResultEntriesTableName + "(" +
			exampleResultEntriesTable_id + " integer primary key, " +
			exampleResultEntriesTable_exampleGroupTypeEntriesId + " integer not null, " +
			exampleResultEntriesTable_exampleResultEntriesParentId + " integer null, " +
			exampleResultEntriesTable_prefixKana + " text null, " +
			exampleResultEntriesTable_kanji + " text null, " +
			exampleResultEntriesTable_kanaList + " text not null, " +
			exampleResultEntriesTable_prefixRomaji + " text null, " +
			exampleResultEntriesTable_romajiList + " text not null);";
	
	public static final String listEntriesTableSelectValues =
			"select " + listEntriesTable_subType + ", " + listEntriesTable_value + " from " + listEntriesTableName + " " + 
			"where " + listEntriesTable_type + " = ? and " +
			listEntriesTable_key + " match ? and " + listEntriesTable_special + " = ? ";
	
	public static final String dictionaryEntriesTableIdElement = 
			"select " + 
			dictionaryEntriesTable_id + ", " +
			dictionaryEntriesTable_dictionaryEntryType + ", " +
			dictionaryEntriesTable_prefixKana + ", " +
			dictionaryEntriesTable_kanji + ", " +
			//dictionaryEntriesTable_kanaList + ", " +
			dictionaryEntriesTable_prefixRomaji + " " +
			//dictionaryEntriesTable_romajiList + ", " +
			//dictionaryEntriesTable_translates + ", " +
			//dictionaryEntriesTable_info + " " +
			"from " + dictionaryEntriesTableName + " where " + dictionaryEntriesTable_id + " = ?";
		
	public static final String dictionaryEntriesTableNthElement = 
			"select " + 
			dictionaryEntriesTable_id + ", " +
			dictionaryEntriesTable_dictionaryEntryType + ", " +
			dictionaryEntriesTable_prefixKana + ", " +
			dictionaryEntriesTable_kanji + ", " +
			//dictionaryEntriesTable_kanaList + ", " +
			dictionaryEntriesTable_prefixRomaji + " " +
			//dictionaryEntriesTable_romajiList + ", " +
			//dictionaryEntriesTable_translates + ", " +
			//dictionaryEntriesTable_info + " " +
			"from " + dictionaryEntriesTableName + " limit 1 offset ?";
	
	public static final String dictionaryEntriesTableSelectElements = 
			"select " + 
			dictionaryEntriesTable_id + ", " +
			dictionaryEntriesTable_dictionaryEntryType + ", " +
			dictionaryEntriesTable_prefixKana + ", " +
			dictionaryEntriesTable_kanji + ", " +
			//dictionaryEntriesTable_kanaList + ", " +
			dictionaryEntriesTable_prefixRomaji + ", " +
			//dictionaryEntriesTable_romajiList + ", " +
			//dictionaryEntriesTable_translates + ", " +
			dictionaryEntriesTable_info + " " +
			"from " + dictionaryEntriesTableName + " ";
	
	// kanji
	
	public static final String dictionaryEntriesTableSelectElements_kanji =
			dictionaryEntriesTable_kanji + " like ? ";
	
	// kana
	
	public static final String dictionaryEntriesTableSelectElements_match_kana =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_kanaList + "' and " +
			listEntriesTable_value + " match ?) ";
	
	public static final String dictionaryEntriesTableSelectElements_match_exact_kana =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_kanaList + "' and " +
			listEntriesTable_value + " like ? and " + listEntriesTable_value + " match ?) ";	
	
	public static final String dictionaryEntriesTableSelectElements_like_kana =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_kanaList + "' and " +
			listEntriesTable_value + " like ?) ";
	
	// romaji
	
	public static final String dictionaryEntriesTableSelectElements_match_romaji =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_romajiList + "' and " +
			" " + listEntriesTable_value + " match ?) ";
	
	public static final String dictionaryEntriesTableSelectElements_match_exact_romaji =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_romajiList + "' and " +
			listEntriesTable_value + " like ? and " + listEntriesTable_value + " match ?) ";
	
	public static final String dictionaryEntriesTableSelectElements_like_romaji =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_romajiList + "' and " +
			" " + listEntriesTable_value + " like ?) ";
	
	// translate 
	
	public static final String dictionaryEntriesTableSelectElements_match_translate =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_translates + "' and " +
			" " + listEntriesTable_value + " match ?) ";
	
	public static final String dictionaryEntriesTableSelectElements_match_exact_translate =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_translates + "' and " +
			listEntriesTable_value + " like ? and " + listEntriesTable_value + " match ?) ";
	
	public static final String dictionaryEntriesTableSelectElements_like_translate =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_translates + "' and " +
			" " + listEntriesTable_value + " like ?) ";

	// info
	
	public static final String dictionaryEntriesTableSelectElements_match_info =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_info + "' and " +
			" " + listEntriesTable_value + " match ?) ";
	
	public static final String dictionaryEntriesTableSelectElements_match_exact_info =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_info + "' and " +
			listEntriesTable_value + " like ? and " + listEntriesTable_value + " match ?) ";
	
	public static final String dictionaryEntriesTableSelectElements_like_info =
			dictionaryEntriesTableName + "." + dictionaryEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + dictionaryEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + dictionaryEntriesTable_info + "' and " +
			" " + listEntriesTable_value + " match ?) ";
	
	public static final String dictionaryEntriesTableSelectElements_limit = 
			" limit " + MAX_SEARCH_RESULT;
	
	public static final String dictionaryEntriesTableSelectElements_dictionaryEntryType =
			dictionaryEntriesTable_dictionaryEntryType + " = ? ";
	
	public static final String countTableSql = 
			"select count(*) from ";
	
	public static final String grammaFormSelectElements = 
			"select 'GrammaForm', " + 
			"grammaFormGroup." + grammaFormConjugateGroupTypeEntriesTable_dictionaryEntryId + ", " +
			"grammaFormResult." + grammaFormConjugateResultEntriesTable_resultType + ", " +
			"grammaFormResult." + grammaFormConjugateResultEntriesTable_prefixKana + ", " +
			"grammaFormResult." + grammaFormConjugateResultEntriesTable_kanji + ", " +
			"grammaFormResult." + grammaFormConjugateResultEntriesTable_kanaList + ", " +
			"grammaFormResult." + grammaFormConjugateResultEntriesTable_prefixRomaji + ", " +
			"grammaFormResult." + grammaFormConjugateResultEntriesTable_romajiList + " " +
			" from " + 
			grammaFormConjugateGroupTypeEntriesTableName + " grammaFormGroup, " +
			grammaFormConjugateResultEntriesTableName + " grammaFormResult " +
			"where grammaFormGroup." + grammaFormConjugateGroupTypeEntriesTable_id + " " +
					" = grammaFormResult." + grammaFormConjugateResultEntriesTable_grammaFormConjugateGroupTypeEntriesId + " ";
	
	public static final String grammaFormSelectElements_kanji =
			grammaFormConjugateResultEntriesTable_kanji + " like ? ";
	
	public static final String grammaFormSelectElements_kana =
			grammaFormConjugateResultEntriesTable_kanaList + " like ? ";

	public static final String grammaFormSelectElements_romaji =
			" " + grammaFormConjugateResultEntriesTable_romajiList + " like ? ";
	
	public static final String exampleResultSelectElements =
			"select 'ExampleResult', " + 
			"exampleGroupType." + exampleGroupTypeEntriesTable_dictionaryEntryId + ", " +
			"exampleGroupType." + exampleGroupTypeEntriesTable_exampleGroupType + ", " +
			"exampleResult." + exampleResultEntriesTable_prefixKana + ", " +
			"exampleResult." + exampleResultEntriesTable_kanji + ", " +
			"exampleResult." + exampleResultEntriesTable_kanaList + ", " +
			"exampleResult." + exampleResultEntriesTable_prefixRomaji + ", " +
			"exampleResult." + exampleResultEntriesTable_romajiList + " " +
			" from " +
			exampleGroupTypeEntriesTableName + " exampleGroupType, " +
			exampleResultEntriesTableName + " exampleResult " +
			"where exampleGroupType." + exampleGroupTypeEntriesTable_id + " " + 
				" = exampleResult." + exampleResultEntriesTable_exampleGroupTypeEntriesId + " ";

	public static final String exampleResultSelectElements_kanji =
			exampleResultEntriesTable_kanji + " like ? ";
	
	public static final String exampleResultSelectElements_kana =
			exampleResultEntriesTable_kanaList + " like ? ";

	public static final String exampleResultSelectElements_romaji =
			" " + exampleResultEntriesTable_romajiList + " like ? ";
	
	public static final String grammaFormExampleSelectElements_limit = 
			" limit ";
	
	public static final String kanjiEntriesTableSelectFindKanjiFromRadicalsElementStart = 
			"select " + kanjiEntriesTable_id + " , " + kanjiEntriesTable_kanji + " , " + kanjiEntriesTable_strokeCount + " , " + kanjiEntriesTable_strokePaths + " , " + 
			kanjiEntriesTable_generated + " from " + kanjiEntriesTableName + " where " + kanjiEntriesTable_id + " in ( ";
	
	public static final String kanjiEntriesTableSelectFindKanjiFromRadicalsElement =
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " ln where ln." + listEntriesTable_type + " = '" + kanjiEntriesTableName + 
			"' and ln." + listEntriesTable_subType + " = '" + kanjiEntriesTable_radicals + "' ";
	
	public static final String kanjiEntriesTableSelectFindKanjiFromRadicalsFilter = 
			" and ln." + listEntriesTable_key + " in (select ln2." + listEntriesTable_key + " from " + listEntriesTableName + 
			" ln2 where ln2." + listEntriesTable_type + " = '" + kanjiEntriesTableName + "' and ln2." + listEntriesTable_subType + " = '" + kanjiEntriesTable_radicals + 
			"' and ln2." + listEntriesTable_value + " = ?) ";
	
	public static final String kanjiEntriesTableSelectFindKanjiFromRadicalsElementStop = " ) "; 
	
	public static final String kanjiEntriesTableSelectAllAvailableRadicalsElement =
			"select " + listEntriesTable_value + " from " + listEntriesTableName + " ln where ln." + listEntriesTable_type + " = '" + kanjiEntriesTableName + 
			"' and ln." + listEntriesTable_subType + " = '" + kanjiEntriesTable_radicals + "' ";
	
	public static final String kanjiEntriesTableSelectAllAvailableRadicalsElementFilter = 
			" and ln." + listEntriesTable_key + " in (select ln2." + listEntriesTable_key + " from " + listEntriesTableName + 
			" ln2 where ln2." + listEntriesTable_type + " = '" + kanjiEntriesTableName + "' and ln2." + listEntriesTable_subType + " = '" + kanjiEntriesTable_radicals + 
			"' and ln2." + listEntriesTable_value + " = ?) ";
	
	public static final String kanjiEntriesTableFindKanjiElementsStart = 
			"select " + kanjiEntriesTable_id + " , " + kanjiEntriesTable_kanji + " , " + kanjiEntriesTable_strokeCount + " , " + kanjiEntriesTable_strokePaths + " , " + 
			kanjiEntriesTable_generated + " from " + kanjiEntriesTableName + " ";
	
	// kanji
	
	public static final String kanjiEntriesTableFindKanjiElements_kanji =
			kanjiEntriesTable_kanji + " like ? ";
	
	// translate 
	
	public static final String kanjiEntriesTableFindKanjiElements_match_translate =
			kanjiEntriesTableName + "." + kanjiEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + kanjiEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + kanjiEntriesTable_polishTranslates + "' and " +
			" " + listEntriesTable_value + " match ?) ";
	
	public static final String kanjiEntriesTableFindKanjiElements_match_exact_translate =
			kanjiEntriesTableName + "." + kanjiEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + kanjiEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + kanjiEntriesTable_polishTranslates + "' and " +
			listEntriesTable_value + " like ? and " + listEntriesTable_value + " match ?) ";
	
	public static final String kanjiEntriesTableFindKanjiElements_like_translate =
			kanjiEntriesTableName + "." + kanjiEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + kanjiEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + kanjiEntriesTable_polishTranslates + "' and " +
			" " + listEntriesTable_value + " like ?) ";

	// info
	
	public static final String kanjiEntriesTableFindKanjiElements_match_info =
			kanjiEntriesTableName + "." + kanjiEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + kanjiEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + kanjiEntriesTable_info + "' and " +
			" " + listEntriesTable_value + " match ?) ";
	
	public static final String kanjiEntriesTableFindKanjiElements_match_exact_info =
			kanjiEntriesTableName + "." + kanjiEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + kanjiEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + kanjiEntriesTable_info + "' and " +
			listEntriesTable_value + " like ? and " + listEntriesTable_value + " match ?) ";
	
	public static final String kanjiEntriesTableFindKanjiElements_like_info =
			kanjiEntriesTableName + "." + kanjiEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + kanjiEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + kanjiEntriesTable_info + "' and " +
			" " + listEntriesTable_value + " match ?) ";
	
	// radicals
	public static final String kanjiEntriesTableFindKanjiElements_radicals =
			kanjiEntriesTableName + "." + kanjiEntriesTable_id + " in ( " +
			"select " + listEntriesTable_key + " from " + listEntriesTableName + " where " +
			listEntriesTable_type + " = '" + kanjiEntriesTableName + "' and " +
			listEntriesTable_subType + " = '" + kanjiEntriesTable_radicals + "' and " +
			" " + listEntriesTable_value + " like ?) ";
	
	public static final String kanjiEntriesTableFindKanjiElements_limit = 
			" limit " + MAX_SEARCH_RESULT;
}
