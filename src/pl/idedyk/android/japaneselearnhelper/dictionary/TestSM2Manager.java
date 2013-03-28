package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.File;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
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
				
				sqliteDatabase.execSQL(SQLiteStatic.wordStatTableCreateNextRepetitionsKeyIndex);
			}
			
			if (isObjectExists("table", SQLiteStatic.configTableName) == false) {
				
				sqliteDatabase.execSQL(SQLiteStatic.configTableNameCreate);
				
				sqliteDatabase.execSQL(SQLiteStatic.configTableNameCreateNameKeyIndex);				
			}

		} catch (SQLException e) {
			throw new TestSM2ManagerException(e.toString());
		}
	}
	
	public void beginTransaction() {
		sqliteDatabase.beginTransaction();
	}
	
	public void endTransaction() {
		sqliteDatabase.endTransaction();
	}
	
	public void commitTransaction() {
		sqliteDatabase.setTransactionSuccessful();
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
	
	public void insertDictionaryEntry(DictionaryEntry dictionaryEntry) {
		
		sqliteDatabase.execSQL(SQLiteStatic.insertWordStatSql, new Object[] { dictionaryEntry.getId(), dictionaryEntry.getGroups().get(0).getPower() });
	}
	
	public void updateDictionaryEntry(DictionaryEntry dictionaryEntry) {		
		sqliteDatabase.execSQL(SQLiteStatic.updateWordStatSql, new Object[] { dictionaryEntry.getGroups().get(0).getPower(), dictionaryEntry.getId() });		
	}
	
	public boolean isDictionaryEntryExistsInWordStat(int id) {
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.countWordStatSql, new String[] { String.valueOf(id) });
			
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
	
	public Integer getVersion() {
		
		String configValue = getConfigValue(SQLiteStatic.configName_version);
		
		if (configValue == null) {
			return null;
		}
		
		try {
			return Integer.parseInt(configValue);
			
		} catch (NumberFormatException e) {
			return null;
		}
	}
	
	public void setVersion(int version) {		
		setConfigValue(SQLiteStatic.configName_version, String.valueOf(version));		
	}
	
	private String getConfigValue(String name) {
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.selectConfigSql, new String[] { name });
			
			boolean moveToFirstResult = cursor.moveToFirst();
			
			if (moveToFirstResult == false) {
				return null;
			}
			
			return cursor.getString(0);
			
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}		
	}
	
	private void setConfigValue(String name, String value) {		
		sqliteDatabase.execSQL(SQLiteStatic.insertOrReplaceConfigSql, new Object[] { name, value });
	}
	
	private static class SQLiteStatic {
				
		public static final String wordStatTableName = "WordStat";
		
		public static final String wordStatTable_id = "id";
		public static final String wordStatTable_power = "power";
		public static final String wordStatTable_easinessFactor = "easinessFactor";
		public static final String wordStatTable_repetitions = "repetitions";
		public static final String wordStatTable_interval = "interval";
		public static final String wordStatTable_nextRepetitions = "nextRepetitions";
		public static final String wordStatTable_lastStudied = "lastStudied";
		
		public static final String configTableName = "Config";
		
		public static final String configTable_id = "id";
		public static final String configTable_name = "name";
		public static final String configTable_value = "value";
		
		public static final String configName_version = "version";
		
		public static final String wordStatTableCreate =
				"create table " + wordStatTableName + "(" +
				wordStatTable_id + " integer primary key, " +
				wordStatTable_power + " integer not null, " +
				wordStatTable_easinessFactor + " real not null, " +
				wordStatTable_repetitions + " integer not null, " +
				wordStatTable_interval + " integer not null, " +
				wordStatTable_nextRepetitions + " datetime not null, " +
				wordStatTable_lastStudied + " datetime not null);";
		
		public static final String wordStatTableCreateNextRepetitionsKeyIndex = 
				"create index " + wordStatTableName + "NextRepetitionsKeyIdx on " +
				wordStatTableName + "(" + wordStatTable_nextRepetitions + ")";
		
		public static final String configTableNameCreate =
				"create table " + configTableName + "(" +
				configTable_id + " integer primary key, " +
				configTable_name + " text not null, " +
				configTable_value + " text not null);";		

		public static final String configTableNameCreateNameKeyIndex = 
				"create unique index " + configTableName + "NameKeyIdx on " +
				configTableName + "(" + configTable_name + ")";
		
		public static final String countObjectSql = 
				"select count(*) from sqlite_master where type = ? and name = ?";
		
		public static final String countWordStatSql =
				"select count(*) from " + wordStatTableName + " where " + wordStatTable_id + " = ? ";
		
		public static final String insertWordStatSql =
				"insert into " + wordStatTableName + " values(?, ?, '2.5', '0', '0', datetime(0, 'unixepoch', 'localtime'), datetime(0, 'unixepoch', 'localtime'));";
		
		public static final String updateWordStatSql =
				"update " + wordStatTableName + " set " + wordStatTable_power + " = ? where " + wordStatTable_id + " = ? ";
		
		public static final String selectConfigSql =
				"select " + configTable_value + " from " + configTableName + " where " + configTable_name + " = ?";
		
		public static final String insertOrReplaceConfigSql =
				"insert or replace into " + configTableName + " ( " + configTable_name + " , " + configTable_value + " ) values (?, ?)";
	}
}
