package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.File;

import pl.idedyk.android.japaneselearnhelper.dictionary.exception.TestSM2ManagerException;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class TestSM2Manager {
	
	private static final String DATABASE_FILE = "test_sm2_dictionary.db";
	
	private File databaseDir;
	
	private SQLiteDatabase sqliteDatabase;
	
	TestSM2Manager(File databaseDir) {
		this.databaseDir = databaseDir;
	}
	
	public void open() throws TestSM2ManagerException {
		
		try {
			File databaseFile = new File(databaseDir, DATABASE_FILE);
			
			sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(databaseFile, null);
			
			// create table if needed
			if (isObjectExists("table", SQLiteStatic.wordStatTableName) == false) {
				
				sqliteDatabase.execSQL(SQLiteStatic.wordStatTableCreate);
				
				sqliteDatabase.execSQL(SQLiteStatic.wordStatTableCreateDictionaryIdKeyIndex);
				sqliteDatabase.execSQL(SQLiteStatic.wordStatTableCreateNextRepetitionsKeyIndex);
			}

		} catch (SQLException e) {
			throw new TestSM2ManagerException(e.toString());
		}
	}
	
	private boolean isObjectExists(String type, String name) {
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.countObjectSql, new String[] { type, name });
			
			cursor.moveToFirst();
			
			int result = cursor.getInt(0);
			
			if (result > 0) {
				return true;
				
			} else {
				return false;
			}
			
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public void close() {
		
		if (sqliteDatabase != null) {
			sqliteDatabase.close();
		}
	}
	
	private static class SQLiteStatic {
		
		/*
		private float easiness_factor = 2.5f;

		private int number_repetitions = 0;

		private int quality_of_last_recall = 0;

		private int repetition_interval = 0;

		private Date next_repetition = null;
		private Date last_studied = null;
		*/
		
		public static final String wordStatTableName = "WordStat";
		
		public static final String wordStatTable_id = "id";
		public static final String wordStatTable_dictionaryId = "dictionaryId";
		public static final String wordStatTable_easinessFactor = "easinessFactor";
		public static final String wordStatTable_repetitions = "repetitions";
		public static final String wordStatTable_interval = "interval";
		public static final String wordStatTable_nextRepetitions = "nextRepetitions";
		public static final String wordStatTable_lastStudied = "lastStudied";
		
		public static final String wordStatTableCreate =
				"create table " + wordStatTableName + "(" +
				wordStatTable_id + " integer primary key autoincrement, " +
				wordStatTable_dictionaryId + " integer not null, " +
				wordStatTable_easinessFactor + " real not null, " +
				wordStatTable_repetitions + " integer not null, " +
				wordStatTable_interval + " integer not null, " +
				wordStatTable_nextRepetitions + " date not null, " +
				wordStatTable_lastStudied + " date not null);";

		public static final String wordStatTableCreateDictionaryIdKeyIndex = 
				"create index " + wordStatTableName + "DictionaryIdKeyIdx on " +
				wordStatTableName + "(" + wordStatTable_dictionaryId + ")";
		
		public static final String wordStatTableCreateNextRepetitionsKeyIndex = 
				"create index " + wordStatTableName + "NextRepetitionsKeyIdx on " +
				wordStatTableName + "(" + wordStatTable_nextRepetitions + ")";
		
		public static final String countObjectSql = 
				"select count(*) from sqlite_master where type = ? and name = ?";
	}
}
