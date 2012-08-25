package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
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
	
	public void insertDictionaryEntry(DictionaryEntry dictionaryEntry) {
		
		ContentValues values = new ContentValues();
		
		values.put(SQLiteStatic.dictionaryEntriesTable_id, dictionaryEntry.getId());		
		values.put(SQLiteStatic.dictionaryEntriesTable_dictionaryEntryType, emptyIfNull(dictionaryEntry.getDictionaryEntryType().toString()));
		values.put(SQLiteStatic.dictionaryEntriesTable_prefixKana, emptyIfNull(dictionaryEntry.getPrefixKana()));
		values.put(SQLiteStatic.dictionaryEntriesTable_kanji, emptyIfNull(dictionaryEntry.getKanji()));
		values.put(SQLiteStatic.dictionaryEntriesTable_kanaList, emptyIfNull(Utils.convertListToString(dictionaryEntry.getKanaList())));
		values.put(SQLiteStatic.dictionaryEntriesTable_prefixRomaji, emptyIfNull(dictionaryEntry.getPrefixRomaji()));
		values.put(SQLiteStatic.dictionaryEntriesTable_romajiList, emptyIfNull(Utils.convertListToString(dictionaryEntry.getRomajiList())));
		values.put(SQLiteStatic.dictionaryEntriesTable_translates, emptyIfNull(Utils.convertListToString(dictionaryEntry.getTranslates())));
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
		
		Cursor cursor = sqliteDatabase.rawQuery(SQLiteStatic.dictionaryEntriesTableCreateCount, null);
		
		cursor.moveToFirst();
		
		int result = cursor.getInt(0);
		
		cursor.close();
		
		return result;
	}
	
	public DictionaryEntry getNthDictionaryEntry(int nth) throws DictionaryException {
		
		Cursor cursor = sqliteDatabase.rawQuery(
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

		cursor.close();
		
		return entry;
	}
	
	public FindWordResult findDictionaryEntries(String word) throws DictionaryException {
		
		FindWordResult findWordResult = new FindWordResult();
		
		findWordResult.result = new ArrayList<DictionaryEntry>();
		
		String wordWithPercent = "%" + word + "%"; 
		
		String wordLowerCase = word.toLowerCase();
		
		String wordLowerCaseWithPercent = "%" + wordLowerCase + "%";
		
		Cursor cursor = sqliteDatabase.rawQuery(
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

	    cursor.close();
	    
		if (findWordResult.result.size() >= SQLiteStatic.MAX_SEARCH_RESULT) {
			findWordResult.moreElemetsExists = true;
		}
		
		return findWordResult;
	}
}
