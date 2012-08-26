package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class SQLiteConnector {
	
	private SQLiteDatabase sqliteDatabase;

	private SQLiteHelper sqliteHelper;

	public SQLiteConnector(Context context, int version) {
		sqliteHelper = new SQLiteHelper(context, version);
	}

	public void open() throws SQLException {
		
		if (sqliteDatabase == null) {
			sqliteDatabase = sqliteHelper.getWritableDatabase();
		}
	}

	public void close() {
		if (sqliteDatabase != null) {
			sqliteDatabase.close();
		}
		
		sqliteDatabase = null;
	}
	
	public boolean isNeedInsertData() {
		return sqliteHelper.isNeedInsertData();
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
	
	public void insertDictionaryEntry(DictionaryEntry dictionaryEntry) {
		
		ContentValues values = new ContentValues();
		
		values.put(SQLiteStatic.dictionaryEntriesTable_id, dictionaryEntry.getId());		
		values.put(SQLiteStatic.dictionaryEntriesTable_dictionaryEntryType, dictionaryEntry.getDictionaryEntryType().toString());
		values.put(SQLiteStatic.dictionaryEntriesTable_prefixKana, emptyIfNull(dictionaryEntry.getPrefixKana()));
		values.put(SQLiteStatic.dictionaryEntriesTable_kanji, emptyIfNull(dictionaryEntry.getKanji()));
		values.put(SQLiteStatic.dictionaryEntriesTable_kanaList, Utils.convertListToString(dictionaryEntry.getKanaList()));
		values.put(SQLiteStatic.dictionaryEntriesTable_prefixRomaji, emptyIfNull(dictionaryEntry.getPrefixRomaji()));
		values.put(SQLiteStatic.dictionaryEntriesTable_romajiList, Utils.convertListToString(dictionaryEntry.getRomajiList()));
		values.put(SQLiteStatic.dictionaryEntriesTable_translates, Utils.convertListToString(dictionaryEntry.getTranslates()));
		values.put(SQLiteStatic.dictionaryEntriesTable_info, emptyIfNull(dictionaryEntry.getInfo()));		
		
		sqliteDatabase.insertOrThrow(SQLiteStatic.dictionaryEntriesTableName, null, values);
	}
	
	private String emptyIfNull(String text) {
		if (text == null) {
			return "";
		}
		
		return text;
	}
	
	public int getDictionaryEntriesSize() {
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.dictionaryEntriesTableCreateCount, null);
			
			cursor.moveToFirst();
			
			int result = cursor.getInt(0);
			
			return result;			
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public DictionaryEntry getNthDictionaryEntry(int nth) throws DictionaryException {
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(
					SQLiteStatic.dictionaryEntriesTableNthElement, new String[] { String.valueOf(nth - 1) });
			
			cursor.moveToFirst();
			
			String idString = cursor.getString(0);
			String dictionaryEntryTypeString = cursor.getString(1);
			String prefixKanaString = cursor.getString(2);
			String kanjiString = cursor.getString(3);
						
			String kanaListString = cursor.getString(4);
			String prefixRomajiString = cursor.getString(5);
			
			String romajiListString = cursor.getString(6);
			String translateListString = cursor.getString(7);
			String infoString = cursor.getString(8);
						
			DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, 
					prefixKanaString, kanjiString, kanaListString, prefixRomajiString,
					romajiListString, translateListString, infoString);
			
			return entry;
		} finally {
			cursor.close();
		}
	}
	
	public FindWordResult findDictionaryEntries(String word) throws DictionaryException {
		
		FindWordResult findWordResult = new FindWordResult();
		
		findWordResult.result = new ArrayList<DictionaryEntry>();
		
		String wordWithPercent = "%" + word + "%"; 
		
		String wordLowerCase = word.toLowerCase();
		
		String wordLowerCaseWithPercent = "%" + wordLowerCase + "%";
		
		Cursor cursor = null;
		try {
			cursor = sqliteDatabase.rawQuery(
					SQLiteStatic.dictionaryEntriesTableSelectElements, new String[] { 
							wordWithPercent, wordWithPercent, wordLowerCaseWithPercent,
							wordLowerCaseWithPercent, wordLowerCaseWithPercent
					});
			
		    cursor.moveToFirst();
		    
		    while (!cursor.isAfterLast()) {
		    	
				String idString = cursor.getString(0);
				String dictionaryEntryTypeString = cursor.getString(1);
				String prefixKanaString = cursor.getString(2);
				String kanjiString = cursor.getString(3);
							
				String kanaListString = cursor.getString(4);
				String prefixRomajiString = cursor.getString(5);
				
				String romajiListString = cursor.getString(6);
				String translateListString = cursor.getString(7);
				String infoString = cursor.getString(8);
							
				DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, 
						prefixKanaString, kanjiString, kanaListString, prefixRomajiString,
						romajiListString, translateListString, infoString);
				
				findWordResult.result.add(entry);
				
				cursor.moveToNext();
		    }
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	    
		if (findWordResult.result.size() >= SQLiteStatic.MAX_SEARCH_RESULT) {
			findWordResult.moreElemetsExists = true;
		}
		
		return findWordResult;
	}
		
	public void insertKanjiEntry(KanjiEntry kanjiEntry) {
				
		ContentValues values = new ContentValues();
		
		values.put(SQLiteStatic.kanjiEntriesTable_id, kanjiEntry.getId());
		values.put(SQLiteStatic.kanjiEntriesTable_kanji, kanjiEntry.getKanji());
		
		KanjiDic2Entry kanjiDic2Entry = kanjiEntry.getKanjiDic2Entry();
		
		if (kanjiDic2Entry != null) {
			values.put(SQLiteStatic.kanjiEntriesTable_strokeCount, kanjiDic2Entry.getStrokeCount());
			values.put(SQLiteStatic.kanjiEntriesTable_radicals, Utils.convertListToString(kanjiDic2Entry.getRadicals()));
			values.put(SQLiteStatic.kanjiEntriesTable_onReading, Utils.convertListToString(kanjiDic2Entry.getOnReading()));
			values.put(SQLiteStatic.kanjiEntriesTable_kunReading, Utils.convertListToString(kanjiDic2Entry.getKunReading()));
		} else {
			values.put(SQLiteStatic.kanjiEntriesTable_strokeCount, "");
			values.put(SQLiteStatic.kanjiEntriesTable_radicals, "");
			values.put(SQLiteStatic.kanjiEntriesTable_onReading, "");
			values.put(SQLiteStatic.kanjiEntriesTable_kunReading, "");			
		}
		
		values.put(SQLiteStatic.kanjiEntriesTable_strokePaths, Utils.convertListToString(kanjiEntry.getStrokePaths()));
		values.put(SQLiteStatic.kanjiEntriesTable_polishTranslates, Utils.convertListToString(kanjiEntry.getPolishTranslates()));
		values.put(SQLiteStatic.kanjiEntriesTable_info, emptyIfNull(kanjiEntry.getInfo()));
		
		sqliteDatabase.insertOrThrow(SQLiteStatic.kanjiEntriesTableName, null, values);
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

				kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, 
						radicalsString, onReadingString, kunReadingString, strokePathString, 
						polishTranslateListString, infoString);				
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

				kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, 
						radicalsString, onReadingString, kunReadingString, strokePathString, 
						polishTranslateListString, infoString);	
				
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

				kanjiEntry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, 
						radicalsString, onReadingString, kunReadingString, strokePathString, 
						polishTranslateListString, infoString);	
				
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
}
