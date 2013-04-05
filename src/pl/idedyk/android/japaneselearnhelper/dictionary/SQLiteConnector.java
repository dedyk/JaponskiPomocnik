package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordResult.ResultItem;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.GroupEnum;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupType;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteConnector {
	
	private SQLiteDatabase sqliteDatabase;

	public void open(String path) throws SQLException {
		
		if (sqliteDatabase == null) {
			sqliteDatabase = SQLiteDatabase.openDatabase(path, null, SQLiteDatabase.OPEN_READWRITE);
		}
	}

	public void close() {
		if (sqliteDatabase != null) {
			sqliteDatabase.close();
		}
		
		sqliteDatabase = null;
	}
	
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		close();
	}
	
	public void beginTransaction() {
		sqliteDatabase.beginTransaction();
	}
	
	public void commitTransaction() {
		sqliteDatabase.setTransactionSuccessful();
	}
	
	public void endTransaction() {
		sqliteDatabase.endTransaction();
	}
	
	public int getDictionaryEntriesSize() {
		
		return countTableSize(SQLiteStatic.dictionaryEntriesTableName);		
	}
	
	public DictionaryEntry getDictionaryEntryById(String id) throws DictionaryException {
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(
					SQLiteStatic.dictionaryEntriesTableIdElement, new String[] { id });
			
			cursor.moveToFirst();
			
			String idString = cursor.getString(0);
			String dictionaryEntryTypeString = cursor.getString(1);
			String prefixKanaString = cursor.getString(2);
			String kanjiString = cursor.getString(3);
			
			Map<String, List<String>> mapListEntryValues = getMapListEntryValues(SQLiteStatic.dictionaryEntriesTableName, idString);
			
			List<String> attributeList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_attributeList);
			List<String> groupsList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_groups);
			List<String> kanaList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_kanaList);
			List<String> romajiList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_romajiList);
			List<String> translateList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_translates);
			List<String> infoStringList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_info);

			String prefixRomajiString = cursor.getString(4);
						
			String infoString = null;
			
			if (infoStringList != null && infoStringList.size() > 0) {
				infoString = infoStringList.get(0);
			}
			
			DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, attributeList, groupsList,
					prefixKanaString, kanjiString, kanaList, prefixRomajiString,
					romajiList, translateList, infoString);
			
			return entry;
		} finally {
			cursor.close();
		}
	}
	
	public DictionaryEntry getNthDictionaryEntry(int nth) throws DictionaryException {
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(
					SQLiteStatic.dictionaryEntriesTableNthElement, new String[] { String.valueOf(nth) });
			
			cursor.moveToFirst();
			
			String idString = cursor.getString(0);
			String dictionaryEntryTypeString = cursor.getString(1);
			String prefixKanaString = cursor.getString(2);
			String kanjiString = cursor.getString(3);
			
			Map<String, List<String>> mapListEntryValues = getMapListEntryValues(SQLiteStatic.dictionaryEntriesTableName, idString);
			
			List<String> attributeList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_attributeList);
			List<String> groupsList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_groups);
			List<String> kanaList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_kanaList);
			List<String> romajiList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_romajiList);
			List<String> translateList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_translates);
			List<String> infoStringList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_info);
			
			String prefixRomajiString = cursor.getString(4);
						
			String infoString = null;
			
			if (infoStringList != null && infoStringList.size() > 0) {
				infoString = infoStringList.get(0);
			}
						
			DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, attributeList, groupsList,
					prefixKanaString, kanjiString, kanaList, prefixRomajiString,
					romajiList, translateList, infoString);
			
			return entry;
		} finally {
			cursor.close();
		}
	}
	
	public FindWordResult findDictionaryEntries(FindWordRequest findWordRequest) throws DictionaryException {
		
		FindWordResult findWordResult = new FindWordResult();
		
		findWordResult.result = new ArrayList<ResultItem>();
				
		String wordArgument = null;
		String wordKanjiArgument = null;
				
		if (findWordRequest.wordPlaceSearch == FindWordRequest.WordPlaceSearch.ANY_PLACE) {
			
			wordArgument = "%" + findWordRequest.word + "%";
			wordKanjiArgument = wordArgument;
						
		} else if (findWordRequest.wordPlaceSearch == FindWordRequest.WordPlaceSearch.START_WITH) {
			 
			wordArgument = findWordRequest.word + "*";
			wordKanjiArgument =  findWordRequest.word + "%";
						
		} else if (findWordRequest.wordPlaceSearch == FindWordRequest.WordPlaceSearch.EXACT) {
			
			wordArgument = findWordRequest.word; 
			wordKanjiArgument =  findWordRequest.word;
						
		} else {
			throw new RuntimeException(String.valueOf(findWordRequest.wordPlaceSearch));
		}
				
		StringBuffer sql = new StringBuffer(SQLiteStatic.dictionaryEntriesTableSelectElements);
		List<String> arguments = new ArrayList<String>();
		
		boolean addedWhere = false;
		
		if (findWordRequest.searchKanji == true) {
			
			if (addedWhere == false) {
				sql.append(" where ( ");
				
				addedWhere = true;
			}
			
			sql.append(SQLiteStatic.dictionaryEntriesTableSelectElements_kanji);
			
			arguments.add(wordKanjiArgument);
		}
		
		if (findWordRequest.searchKana == true) {
			
			if (addedWhere == false) {
				sql.append(" where ( ");
				
				addedWhere = true;
			} else {
				sql.append(" or ");
			}
			
			if (findWordRequest.wordPlaceSearch == FindWordRequest.WordPlaceSearch.ANY_PLACE) {
				sql.append(SQLiteStatic.dictionaryEntriesTableSelectElements_like_kana);
			} else {
				sql.append(SQLiteStatic.dictionaryEntriesTableSelectElements_match_kana);
			}
			
			arguments.add(wordArgument);
		}
		
		if (findWordRequest.searchRomaji == true) {
			
			if (addedWhere == false) {
				sql.append(" where ( ");
				
				addedWhere = true;
			} else {
				sql.append(" or ");
			}
			
			if (findWordRequest.wordPlaceSearch == FindWordRequest.WordPlaceSearch.ANY_PLACE) {
				sql.append(SQLiteStatic.dictionaryEntriesTableSelectElements_like_romaji);
			} else {
				sql.append(SQLiteStatic.dictionaryEntriesTableSelectElements_match_romaji);
			}
			
			//arguments.add(operation);
			arguments.add(wordArgument);
		}

		if (findWordRequest.searchTranslate == true) {
			
			if (addedWhere == false) {
				sql.append(" where ( ");
				
				addedWhere = true;
			} else {
				sql.append(" or ");
			}
			
			if (findWordRequest.wordPlaceSearch == FindWordRequest.WordPlaceSearch.ANY_PLACE) {
				sql.append(SQLiteStatic.dictionaryEntriesTableSelectElements_like_translate);
			} else {
				sql.append(SQLiteStatic.dictionaryEntriesTableSelectElements_match_translate);
			}
			
			// arguments.add(operation);
			arguments.add(wordArgument);
		}
		
		if (findWordRequest.searchInfo == true) {
			
			if (addedWhere == false) {
				sql.append(" where ( ");
				
				addedWhere = true;
			} else {
				sql.append(" or ");
			}
			
			if (findWordRequest.wordPlaceSearch == FindWordRequest.WordPlaceSearch.ANY_PLACE) {
				sql.append(SQLiteStatic.dictionaryEntriesTableSelectElements_like_info);
			} else {
				sql.append(SQLiteStatic.dictionaryEntriesTableSelectElements_match_info);
			}
			
			//arguments.add(operation);
			arguments.add(wordArgument);
		}
		
		if (addedWhere == true) {
			sql.append(" )");
		}
		
		List<DictionaryEntryType> findWordRequestDictionaryEntryList = findWordRequest.dictionaryEntryList;
		
		if (findWordRequestDictionaryEntryList != null && findWordRequestDictionaryEntryList.size() != 0 &&
				findWordRequestDictionaryEntryList.size() != DictionaryEntryType.values().length) {
			
			if (addedWhere == false) {
				sql.append(" where ");
				
				addedWhere = true;
			} else {
				sql.append(" and ");
			}
			
			sql.append(" ( ");
			
			for (int findWordRequestDictionaryEntryListIdx = 0; findWordRequestDictionaryEntryListIdx < findWordRequestDictionaryEntryList.size();
					findWordRequestDictionaryEntryListIdx++) {
				
				if (findWordRequestDictionaryEntryListIdx != 0) {
					sql.append(" or ");
				}
				
				DictionaryEntryType currentFindWordRequestDictionaryEntry = findWordRequestDictionaryEntryList.get(findWordRequestDictionaryEntryListIdx);
				
				sql.append(" ").append(SQLiteStatic.dictionaryEntriesTableSelectElements_dictionaryEntryType).append(" ");
				
				arguments.add(currentFindWordRequestDictionaryEntry.toString());
			}
			
			sql.append(" ) ");			
		}
		
		sql.append(SQLiteStatic.dictionaryEntriesTableSelectElements_limit);

		String[] argumentsStringArray = new String[arguments.size()];
		
		arguments.toArray(argumentsStringArray);
		
		Cursor cursor = null;
				
		try {			
			cursor = sqliteDatabase.rawQuery(sql.toString(), argumentsStringArray);
			
		    cursor.moveToFirst();
		    
		    while (!cursor.isAfterLast()) {
		    	
				String idString = cursor.getString(0);
				String dictionaryEntryTypeString = cursor.getString(1);
				String prefixKanaString = cursor.getString(2);
				String kanjiString = cursor.getString(3);
				
				Map<String, List<String>> mapListEntryValues = getMapListEntryValues(SQLiteStatic.dictionaryEntriesTableName, idString);
							
				List<String> attributeList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_attributeList);
				List<String> groupsList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_groups);
				List<String> kanaList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_kanaList);
				List<String> romajiList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_romajiList);
				List<String> translateList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_translates);
				List<String> infoStringList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_info);
				
				String prefixRomajiString = cursor.getString(4);
								
				String infoString = null;
				
				if (infoStringList != null && infoStringList.size() > 0) {
					infoString = infoStringList.get(0);
				}
							
				DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, attributeList, groupsList,
						prefixKanaString, kanjiString, kanaList, prefixRomajiString,
						romajiList, translateList, infoString);
				
				findWordResult.result.add(new ResultItem(entry));
				
				cursor.moveToNext();
		    }
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
			    
		if (findWordResult.result.size() >= SQLiteStatic.MAX_SEARCH_RESULT) {
			findWordResult.moreElemetsExists = true;
			
			findWordResult.result.remove(findWordResult.result.size() - 1);
		}
		
		return findWordResult;
	}
	
	private Map<String, List<String>> getMapListEntryValues(String type, String key) {
		
		Map<String, List<String>> result = new HashMap<String, List<String>>();
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.listEntriesTableSelectValues, new String[] { type, key, "0" }); 
			
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) {
				
				String subType = cursor.getString(0);
				String value = cursor.getString(1);
				
				List<String> listForSubType = result.get(subType);
				
				if (listForSubType == null) {
					listForSubType = new ArrayList<String>();
				}
				
				listForSubType.add(value);
				
				result.put(subType, listForSubType);
				
				cursor.moveToNext();
			}
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}		
		
		return result;
	}
		
	public List<KanjiEntry> getAllKanjis(boolean withDetails, boolean addGenerated) throws DictionaryException {
		
		KanjiEntry kanjiEntry = null;
		
		Cursor cursor = null;
				
		List<KanjiEntry> result = new ArrayList<KanjiEntry>();
		
		try {
			
			if (addGenerated == false) {
				cursor = sqliteDatabase.query(SQLiteStatic.kanjiEntriesTableName, SQLiteStatic.kanjiEntriesTableAllColumns, SQLiteStatic.kanjiEntriesTable_generated + " = ?",
						new String[] { "false" }, null, null, null);				
			} else {
				cursor = sqliteDatabase.query(SQLiteStatic.kanjiEntriesTableName, SQLiteStatic.kanjiEntriesTableAllColumns, null,
						null, null, null, null);
			}			
			
		    cursor.moveToFirst();
		    
		    while (!cursor.isAfterLast()) {
				
				String idString = cursor.getString(0);

				String kanjiString = cursor.getString(1);
				
				String strokeCountString = null;
				String radicalsString = null;
				String onReadingString = null;
				String kunReadingString = null;
				String strokePathString = null;
				
				if (withDetails == true) {
					strokeCountString = cursor.getString(2);

					radicalsString = cursor.getString(3);

					onReadingString = cursor.getString(4);

					kunReadingString = cursor.getString(5);

					strokePathString = cursor.getString(6);
				}

				String polishTranslateListString = cursor.getString(7);
				String infoString = cursor.getString(8);
				
				String generated = cursor.getString(9);
				
				String groups = cursor.getString(10);

				kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, 
						radicalsString, onReadingString, kunReadingString, strokePathString, 
						polishTranslateListString, infoString, generated, groups);	
				
				result.add(kanjiEntry);
				
				cursor.moveToNext();
			}
		
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return result;
	}
	
	public KanjiEntry getKanjiEntry(String kanji) throws DictionaryException {
		
		KanjiEntry kanjiEntry = null;
				
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.query(SQLiteStatic.kanjiEntriesTableName, SQLiteStatic.kanjiEntriesTableAllColumns, SQLiteStatic.kanjiEntriesTable_kanji + " = ?" ,
					new String[] { kanji }, null, null, null);
			
			if (cursor.moveToFirst() == true) {
				
				String idString = cursor.getString(0);

				String kanjiString = cursor.getString(1);

				String strokeCountString = cursor.getString(2);

				String radicalsString = cursor.getString(3);

				String onReadingString = cursor.getString(4);

				String kunReadingString = cursor.getString(5);

				String strokePathString = cursor.getString(6);

				String polishTranslateListString = cursor.getString(7);
				String infoString = cursor.getString(8);
				
				String generated = cursor.getString(9);
				
				String groups = cursor.getString(10);

				kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, 
						radicalsString, onReadingString, kunReadingString, strokePathString, 
						polishTranslateListString, infoString, generated, groups);				
			}
		
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return kanjiEntry;
	}
	
	public List<KanjiEntry> findKanjiFromRadicals(String[] radicals) throws DictionaryException {
		
		KanjiEntry kanjiEntry = null;
				
		Cursor cursor = null;
		
		StringBuffer selectionArgs = new StringBuffer();
		
		for (int radicalsIdx = 0; radicalsIdx < radicals.length; ++radicalsIdx) {
			selectionArgs.append(SQLiteStatic.kanjiEntriesTable_radicals).append(" like ?");
			
			if (radicalsIdx != radicals.length - 1) {
				selectionArgs.append(" and ");	
			}
		}
		
		String[] radicalsWithPercent = new String[radicals.length];
		
		for (int radicalsIdx = 0; radicalsIdx < radicals.length; ++radicalsIdx) {
			radicalsWithPercent[radicalsIdx] = "%" + radicals[radicalsIdx] + "%";
		}
		
		List<KanjiEntry> result = new ArrayList<KanjiEntry>();
		
		try {
			cursor = sqliteDatabase.query(SQLiteStatic.kanjiEntriesTableName, SQLiteStatic.kanjiEntriesTableAllColumns, 
					selectionArgs.toString(),
					radicalsWithPercent, null, null, null);
			
		    cursor.moveToFirst();
		    
		    while (!cursor.isAfterLast()) {
				
				String idString = cursor.getString(0);

				String kanjiString = cursor.getString(1);

				String strokeCountString = cursor.getString(2);

				String radicalsString = cursor.getString(3);

				String onReadingString = cursor.getString(4);

				String kunReadingString = cursor.getString(5);

				String strokePathString = cursor.getString(6);

				String polishTranslateListString = cursor.getString(7);
				String infoString = cursor.getString(8);
				
				String generated = cursor.getString(9);
				
				String groups = cursor.getString(10);

				kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, 
						radicalsString, onReadingString, kunReadingString, strokePathString, 
						polishTranslateListString, infoString, generated, groups);	
				
				result.add(kanjiEntry);
				
				cursor.moveToNext();
			}
		
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return result;
	}

	public Set<String> findAllAvailableRadicals(String[] radicals) throws DictionaryException {
		
		KanjiEntry kanjiEntry = null;
				
		Cursor cursor = null;
		
		StringBuffer selectionArgs = new StringBuffer();
		
		for (int radicalsIdx = 0; radicalsIdx < radicals.length; ++radicalsIdx) {
			selectionArgs.append(SQLiteStatic.kanjiEntriesTable_radicals).append(" like ?");
			
			if (radicalsIdx != radicals.length - 1) {
				selectionArgs.append(" and ");	
			}
		}
		
		String[] radicalsWithPercent = new String[radicals.length];
		
		for (int radicalsIdx = 0; radicalsIdx < radicals.length; ++radicalsIdx) {
			radicalsWithPercent[radicalsIdx] = "%" + radicals[radicalsIdx] + "%";
		}
		
		Set<String> result = new HashSet<String>();
		
		try {
			cursor = sqliteDatabase.query(SQLiteStatic.kanjiEntriesTableName, SQLiteStatic.kanjiEntriesTableAllColumns, 
					selectionArgs.toString(),
					radicalsWithPercent, null, null, null);
			
		    cursor.moveToFirst();
		    
		    while (!cursor.isAfterLast()) {
				
				String idString = cursor.getString(0);

				String kanjiString = cursor.getString(1);

				String strokeCountString = cursor.getString(2);

				String radicalsString = cursor.getString(3);

				String onReadingString = cursor.getString(4);

				String kunReadingString = cursor.getString(5);

				String strokePathString = cursor.getString(6);

				String polishTranslateListString = cursor.getString(7);
				String infoString = cursor.getString(8);
				
				String generated = cursor.getString(9);
				
				String groups = cursor.getString(10);

				kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, 
						radicalsString, onReadingString, kunReadingString, strokePathString, 
						polishTranslateListString, infoString, generated, groups);	
				
				KanjiDic2Entry kanjiDic2Entry = kanjiEntry.getKanjiDic2Entry();
				
				if (kanjiDic2Entry != null) {
					result.addAll(kanjiDic2Entry.getRadicals());
				}
				
				cursor.moveToNext();
			}
		
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		return result;
	}
	
	public void insertGrammaFormConjugateGroupTypeElements(DictionaryEntry dictionaryEntry, GrammaFormConjugateGroupTypeElements grammaFormConjugateGroupTypeElements) {
		
		ContentValues values = new ContentValues();

		values.put(SQLiteStatic.grammaFormConjugateGroupTypeEntriesTable_dictionaryEntryId, dictionaryEntry.getId());
		values.put(SQLiteStatic.grammaFormConjugateGroupTypeEntriesTable_grammaFormConjugateGroupType, grammaFormConjugateGroupTypeElements.getGrammaFormConjugateGroupType().toString());

		long grammaFormConjugateGroupTypeElementsId = sqliteDatabase.insertOrThrow(SQLiteStatic.grammaFormConjugateGroupTypeEntriesTableName, null, values);
		
		List<GrammaFormConjugateResult> grammaFormConjugateResults = grammaFormConjugateGroupTypeElements.getGrammaFormConjugateResults();
		
		for (GrammaFormConjugateResult currentGrammaFormConjugateResults : grammaFormConjugateResults) {
			
			insertGrammaFormConjugateResult(grammaFormConjugateGroupTypeElementsId, null, currentGrammaFormConjugateResults);
		}
	}
	
	private void insertGrammaFormConjugateResult(long grammaFormConjugateGroupTypeElementsId, Long grammaFormConjugateResultEntriesParentId, 
			GrammaFormConjugateResult grammaFormConjugateResult) {
		
		ContentValues values = new ContentValues();
		
		values.put(SQLiteStatic.grammaFormConjugateResultEntriesTable_grammaFormConjugateGroupTypeEntriesId, grammaFormConjugateGroupTypeElementsId);
		values.put(SQLiteStatic.grammaFormConjugateResultEntriesTable_grammaFormConjugateResultEntriesParentId, grammaFormConjugateResultEntriesParentId);
		values.put(SQLiteStatic.grammaFormConjugateResultEntriesTable_resultType, grammaFormConjugateResult.getResultType().toString());
		values.put(SQLiteStatic.grammaFormConjugateResultEntriesTable_prefixKana, grammaFormConjugateResult.getPrefixKana());
		values.put(SQLiteStatic.grammaFormConjugateResultEntriesTable_kanji, grammaFormConjugateResult.getKanji());
		values.put(SQLiteStatic.grammaFormConjugateResultEntriesTable_kanaList, Utils.convertListToString(grammaFormConjugateResult.getKanaList()));
		values.put(SQLiteStatic.grammaFormConjugateResultEntriesTable_prefixRomaji, grammaFormConjugateResult.getPrefixRomaji());
		values.put(SQLiteStatic.grammaFormConjugateResultEntriesTable_romajiList, Utils.convertListToString(grammaFormConjugateResult.getRomajiList()));
		
		long grammaFormConjugateResultEntriesId = sqliteDatabase.insertOrThrow(SQLiteStatic.grammaFormConjugateResultEntriesTableName, null, values);
		
		GrammaFormConjugateResult alternative = grammaFormConjugateResult.getAlternative();
		
		if (alternative != null) {
			insertGrammaFormConjugateResult(grammaFormConjugateGroupTypeElementsId, grammaFormConjugateResultEntriesId, alternative);
		}
	}
	
	public void insertExampleGroupTypeElements(DictionaryEntry dictionaryEntry, ExampleGroupTypeElements exampleGroupTypeElements) {
		
		ContentValues values = new ContentValues();

		values.put(SQLiteStatic.exampleGroupTypeEntriesTable_dictionaryEntryId, dictionaryEntry.getId());
		values.put(SQLiteStatic.exampleGroupTypeEntriesTable_exampleGroupType, exampleGroupTypeElements.getExampleGroupType().toString());

		long exampleGroupTypeEntriesId = sqliteDatabase.insertOrThrow(SQLiteStatic.exampleGroupTypeEntriesTableName, null, values);
		
		List<ExampleResult> exampleResults = exampleGroupTypeElements.getExampleResults();
		
		for (ExampleResult currentExampleResult : exampleResults) {
			
			insertExampleResult(exampleGroupTypeEntriesId, null, currentExampleResult);
		}
	}
	
	private void insertExampleResult(long exampleGroupTypeEntriesId, Long exampleResultEntriesParentId, 
			ExampleResult exampleResult) {
		
		ContentValues values = new ContentValues();
		
		values.put(SQLiteStatic.exampleResultEntriesTable_exampleGroupTypeEntriesId, exampleGroupTypeEntriesId);
		values.put(SQLiteStatic.exampleResultEntriesTable_exampleResultEntriesParentId, exampleResultEntriesParentId);
		values.put(SQLiteStatic.exampleResultEntriesTable_prefixKana, exampleResult.getPrefixKana());
		values.put(SQLiteStatic.exampleResultEntriesTable_kanji, exampleResult.getKanji());
		values.put(SQLiteStatic.exampleResultEntriesTable_kanaList, Utils.convertListToString(exampleResult.getKanaList()));
		values.put(SQLiteStatic.exampleResultEntriesTable_prefixRomaji, exampleResult.getPrefixRomaji());
		values.put(SQLiteStatic.exampleResultEntriesTable_romajiList, Utils.convertListToString(exampleResult.getRomajiList()));
		
		long exampleResultEntriesId = sqliteDatabase.insertOrThrow(SQLiteStatic.exampleResultEntriesTableName, null, values);
		
		ExampleResult alternative = exampleResult.getAlternative();
		
		if (alternative != null) {
			insertExampleResult(exampleGroupTypeEntriesId, exampleResultEntriesId, alternative);
		}
	}
	
	public int getGrammaFormAndExamplesEntriesSize() {
		
		return countTableSize(SQLiteStatic.grammaFormConjugateGroupTypeEntriesTableName) +
				countTableSize(SQLiteStatic.grammaFormConjugateResultEntriesTableName) + 
				countTableSize(SQLiteStatic.exampleGroupTypeEntriesTableName) +
				countTableSize(SQLiteStatic.exampleResultEntriesTableName);
	}
	
	private int countTableSize(String tableName) {
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.countTableSql + tableName, null);
			
			cursor.moveToFirst();
			
			int result = cursor.getInt(0);
			
			return result;			
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public void findDictionaryEntriesInGrammaFormAndExamples(FindWordRequest findWordRequest, FindWordResult findWordResult) throws DictionaryException {
		
		if (findWordRequest.searchGrammaFormAndExamples == false) {
			return;
		}
		
		if (findWordResult.moreElemetsExists == true) {
			return;
		}
		
		int limit = SQLiteStatic.MAX_SEARCH_RESULT - findWordResult.result.size();
		
		String wordWithPercent = null; 
		String wordLowerCaseWithPercent = null;
		
		if (findWordRequest.wordPlaceSearch == FindWordRequest.WordPlaceSearch.ANY_PLACE) {
			
			wordWithPercent = "%" + findWordRequest.word + "%"; 
			wordLowerCaseWithPercent = "%" + findWordRequest.word.toLowerCase(Locale.getDefault()) + "%";
			
		} else if (findWordRequest.wordPlaceSearch == FindWordRequest.WordPlaceSearch.START_WITH) {
			
			wordWithPercent = findWordRequest.word + "%"; 
			wordLowerCaseWithPercent = findWordRequest.word.toLowerCase(Locale.getDefault()) + "%";
			
		} else if (findWordRequest.wordPlaceSearch == FindWordRequest.WordPlaceSearch.EXACT) {
			
			wordWithPercent = findWordRequest.word; 
			wordLowerCaseWithPercent = findWordRequest.word.toLowerCase(Locale.getDefault());
			
		} else {
			throw new RuntimeException(String.valueOf(findWordRequest.wordPlaceSearch));
		}
		
		List<String> arguments = new ArrayList<String>();
		
		StringBuffer sql = new StringBuffer(SQLiteStatic.grammaFormSelectElements);

		boolean addedAnd = false;
		
		if (findWordRequest.searchKanji == true) {
			
			if (addedAnd == false) {
				sql.append(" and ( ");
				
				addedAnd = true;
			}
			
			sql.append(SQLiteStatic.grammaFormSelectElements_kanji);
			
			arguments.add(wordWithPercent);
		}
		
		if (findWordRequest.searchKana == true) {
			
			if (addedAnd == false) {
				sql.append(" and ( ");
				
				addedAnd = true;
			} else {
				sql.append(" or ");
			}
			
			sql.append(SQLiteStatic.grammaFormSelectElements_kana);
			
			arguments.add(wordWithPercent);
		}
		
		if (findWordRequest.searchRomaji == true) {
			
			if (addedAnd == false) {
				sql.append(" and ( ");
				
				addedAnd = true;
			} else {
				sql.append(" or ");
			}
			
			sql.append(SQLiteStatic.grammaFormSelectElements_romaji);
			
			arguments.add(wordLowerCaseWithPercent);
		}
		
		if (addedAnd == true) {
			sql.append(" ) ");
		} else {
			return;
		}
		
		sql.append(" union ");
		
		sql.append(SQLiteStatic.exampleResultSelectElements);

		addedAnd = false;
		
		if (findWordRequest.searchKanji == true) {
			
			if (addedAnd == false) {
				sql.append(" and ( ");
				
				addedAnd = true;
			}
			
			sql.append(SQLiteStatic.exampleResultSelectElements_kanji);
			
			arguments.add(wordWithPercent);
		}
		
		if (findWordRequest.searchKana == true) {
			
			if (addedAnd == false) {
				sql.append(" and ( ");
				
				addedAnd = true;
			} else {
				sql.append(" or ");
			}
			
			sql.append(SQLiteStatic.exampleResultSelectElements_kana);
			
			arguments.add(wordWithPercent);
		}
		
		if (findWordRequest.searchRomaji == true) {
			
			if (addedAnd == false) {
				sql.append(" and ( ");
				
				addedAnd = true;
			} else {
				sql.append(" or ");
			}
			
			sql.append(SQLiteStatic.exampleResultSelectElements_romaji);
			
			arguments.add(wordLowerCaseWithPercent);
		}
		
		if (addedAnd == true) {
			sql.append(" ) ");
		} else {
			return;
		}
		
		sql.append(SQLiteStatic.grammaFormExampleSelectElements_limit).append(" " + limit);
		
		Cursor cursor = null;
		
		String[] argumentsStringArray = new String[arguments.size()];
		
		arguments.toArray(argumentsStringArray);
		
		try {
			cursor = sqliteDatabase.rawQuery(sql.toString(), argumentsStringArray);
			
			cursor.moveToFirst();
			
			while (!cursor.isAfterLast()) {
				
				String grammaFormOrExample = cursor.getString(0);
				String dictionaryEntryId = cursor.getString(1);
				String resultTypeString = cursor.getString(2);
				
				String prefixKana = cursor.getString(3);
				String kanjiString = cursor.getString(4);
				String kanaListString = cursor.getString(5);				
				String prefixRomaji = cursor.getString(6);
				String romajiListString = cursor.getString(7);
				
				if (grammaFormOrExample.equals("GrammaForm") == true) {
					
					GrammaFormConjugateResult grammaFormConjugateResult = new GrammaFormConjugateResult();
					
					grammaFormConjugateResult.setPrefixKana(prefixKana);
					grammaFormConjugateResult.setKanji(kanjiString);
					grammaFormConjugateResult.setResultType(GrammaFormConjugateResultType.valueOf(resultTypeString));
					grammaFormConjugateResult.setKanaList(Utils.parseStringIntoList(kanaListString, false));
					grammaFormConjugateResult.setPrefixRomaji(prefixRomaji);
					grammaFormConjugateResult.setRomajiList(Utils.parseStringIntoList(romajiListString , false));
					
					DictionaryEntry relatedDictionaryEntryById = getDictionaryEntryById(dictionaryEntryId);
					
					if (relatedDictionaryEntryById == null) {
						throw new RuntimeException("relatedDictionaryEntryById == null: " + dictionaryEntryId);
					}
					
					findWordResult.result.add(new ResultItem(grammaFormConjugateResult, relatedDictionaryEntryById));
					
				} else if (grammaFormOrExample.equals("ExampleResult") == true) {
					
					ExampleResult exampleResult = new ExampleResult();
					
					exampleResult.setPrefixKana(prefixKana);
					exampleResult.setKanji(kanjiString);
					exampleResult.setKanaList(Utils.parseStringIntoList(kanaListString, false));
					exampleResult.setPrefixRomaji(prefixRomaji);
					exampleResult.setRomajiList(Utils.parseStringIntoList(romajiListString , false));

					DictionaryEntry relatedDictionaryEntryById = getDictionaryEntryById(dictionaryEntryId);
					
					if (relatedDictionaryEntryById == null) {
						throw new RuntimeException("relatedDictionaryEntryById == null: " + dictionaryEntryId);
					}
					
					findWordResult.result.add(new ResultItem(exampleResult, ExampleGroupType.valueOf(resultTypeString), relatedDictionaryEntryById));
				
				} else {
					throw new RuntimeException("grammaFormOrExampe: " + grammaFormOrExample);
				}
				
				cursor.moveToNext();
			}
			
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		if (findWordResult.result.size() >= SQLiteStatic.MAX_SEARCH_RESULT) {
			findWordResult.moreElemetsExists = true;
			
			findWordResult.result.remove(findWordResult.result.size() - 1);
		}
	}

	public FindKanjiResult findKanjisFromStrokeCount(int from, int to) throws DictionaryException {
		
		KanjiEntry kanjiEntry = null;
		
		Cursor cursor = null;
				
		List<KanjiEntry> resultList = new ArrayList<KanjiEntry>();
		
		try {
			cursor = sqliteDatabase.query(SQLiteStatic.kanjiEntriesTableName, SQLiteStatic.kanjiEntriesTableAllColumns, 
					SQLiteStatic.kanjiEntriesTable_strokeCount + " >= ? and " + SQLiteStatic.kanjiEntriesTable_strokeCount + " <= ?",
					new String[] { String.valueOf(from), String.valueOf(to) }, null, null, null, String.valueOf(SQLiteStatic.MAX_KANJI_STROKE_COUNT_RESULT));
			
		    cursor.moveToFirst();
		    
		    while (!cursor.isAfterLast()) {
				
				String idString = cursor.getString(0);

				String kanjiString = cursor.getString(1);

				String strokeCountString = cursor.getString(2);

				String radicalsString = cursor.getString(3);

				String onReadingString = cursor.getString(4);

				String kunReadingString = cursor.getString(5);

				String strokePathString = cursor.getString(6);

				String polishTranslateListString = cursor.getString(7);
				String infoString = cursor.getString(8);
				
				String generated = cursor.getString(9);
				
				String groups = cursor.getString(10);

				kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, 
						radicalsString, onReadingString, kunReadingString, strokePathString, 
						polishTranslateListString, infoString, generated, groups);	
				
				resultList.add(kanjiEntry);
				
				cursor.moveToNext();
			}
		
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		FindKanjiResult findKanjiResult = new FindKanjiResult();
		
		if (resultList.size() == SQLiteStatic.MAX_KANJI_STROKE_COUNT_RESULT) {
			resultList.remove(resultList.size() - 1);
			
			findKanjiResult.setMoreElemetsExists(true);
		}

		findKanjiResult.setResult(resultList);
		
		return findKanjiResult;
	}

	public List<GroupEnum> getDictionaryEntryGroupTypes() {
		
		List<GroupEnum> result = new ArrayList<GroupEnum>();
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.query( true, SQLiteStatic.listEntriesTableName, new String[] { SQLiteStatic.listEntriesTable_value },  
					SQLiteStatic.listEntriesTable_type + " = ? and " + SQLiteStatic.listEntriesTable_subType + " = ? ",
					new String[] { SQLiteStatic.dictionaryEntriesTableName, SQLiteStatic.dictionaryEntriesTable_groups },
					null, null, null, null);
			
		    cursor.moveToFirst();
		    
		    while (!cursor.isAfterLast()) {
				
				String groupName = cursor.getString(0);

				result.add(GroupEnum.getGroupEnum(groupName));
				
				cursor.moveToNext();
			}
		
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		
		GroupEnum.sortGroups(result);
		
		return result;
	}

	public List<DictionaryEntry> getGroupDictionaryEntries(GroupEnum groupName) throws DictionaryException {
		
		List<DictionaryEntry> result = new ArrayList<DictionaryEntry>();
		
		Cursor cursor = null;
		
		try {			
			cursor = sqliteDatabase.rawQuery(
					"select * from " + SQLiteStatic.dictionaryEntriesTableName + " de where " +
					" de." + SQLiteStatic.dictionaryEntriesTable_id + " in ( " +
					" select " + SQLiteStatic.listEntriesTable_key + " from " + SQLiteStatic.listEntriesTableName + " " + 
					" where " + SQLiteStatic.listEntriesTable_type + " = ? and " + SQLiteStatic.listEntriesTable_subType + " = ? and " +
					" " + SQLiteStatic.listEntriesTable_value + " = ? " +
					")", new String[] { SQLiteStatic.dictionaryEntriesTableName, SQLiteStatic.dictionaryEntriesTable_groups, groupName.getValue() });
			
		    cursor.moveToFirst();
		    
		    while (!cursor.isAfterLast()) {
		    	
				String idString = cursor.getString(0);
				String dictionaryEntryTypeString = cursor.getString(1);
				String prefixKanaString = cursor.getString(2);
				String kanjiString = cursor.getString(3);
				
				Map<String, List<String>> mapListEntryValues = getMapListEntryValues(SQLiteStatic.dictionaryEntriesTableName, idString);
							
				List<String> attributeList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_attributeList);
				List<String> groupsList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_groups);
				List<String> kanaList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_kanaList);
				List<String> romajiList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_romajiList);
				List<String> translateList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_translates);
				List<String> infoStringList = mapListEntryValues.get(SQLiteStatic.dictionaryEntriesTable_info);
				
				String prefixRomajiString = cursor.getString(4);
								
				String infoString = null;
				
				if (infoStringList != null && infoStringList.size() > 0) {
					infoString = infoStringList.get(0);
				}
							
				DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, attributeList, groupsList,
						prefixKanaString, kanjiString, kanaList, prefixRomajiString,
						romajiList, translateList, infoString);
				
				result.add(entry);
				
				cursor.moveToNext();
		    }
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}		
		
		return result;
	}

	public void vacuum() {
		sqliteDatabase.execSQL("vacuum");
	}
}
