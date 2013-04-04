package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.WordTestSM2DayStat;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.WordTestSM2WordStat;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.TestSM2ManagerException;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class WordTestSM2Manager {
	
	private static final String DATABASE_FILE = "word_test_sm2.db";
	
	private File databaseDir;
	
	private SQLiteDatabase sqliteDatabase;
	
	WordTestSM2Manager(File databaseDir) {
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
			
			if (isObjectExists("table", SQLiteStatic.dayStatTableName) == false) {
				
				sqliteDatabase.execSQL(SQLiteStatic.dayStatTableCreate);
				
				sqliteDatabase.execSQL(SQLiteStatic.dayStatTableCreateDateStatKeyIndex);				
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
		sqliteDatabase.execSQL(SQLiteStatic.updateWordStatPowerSql, new Object[] { dictionaryEntry.getGroups().get(0).getPower(), dictionaryEntry.getId() });		
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
	
	public WordTestSM2DayStat getCurrentDayStat() {
		
		WordTestSM2DayStat currentDayStat = getCurrentDayStatPriv();
		
		if (currentDayStat != null) {
			return currentDayStat;			
		}
		
		sqliteDatabase.execSQL(SQLiteStatic.insertNewCurrentDateStatSql);
		
		currentDayStat = getCurrentDayStatPriv();
		
		if (currentDayStat == null) {
			throw new RuntimeException("Empty: " + currentDayStat);
		}
		
		return currentDayStat;
	}
	
	private WordTestSM2DayStat getCurrentDayStatPriv() {
		
		Cursor cursor = null;
				
		try {
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.selectCurrentDateStatSql, new String[] { });
			
			boolean moveToFirstResult = cursor.moveToFirst();
			
			if (moveToFirstResult == false) {
				return null;
			}
			
			WordTestSM2DayStat result = new WordTestSM2DayStat();
			
			int id = cursor.getInt(0);
			String dateStatString = cursor.getString(1);
			int newWords = cursor.getInt(2);
			
			result.setId(id);
			result.setDateStat(getDateFromString(dateStatString));
			result.setNewWords(newWords);
						
			return result;
			
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	private Date getDateFromString(String dateString) {

		if (dateString == null) {
			return null;
		}
		
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

		try {
			return dateFormat.parse(dateString);

		} catch (ParseException e) {
			throw new RuntimeException(e);
		}	
	}
	
	private Date getDatetimeFromString(String datetimeString) {

		if (datetimeString == null) {
			return null;
		}

		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());

		try {
			return datetimeFormat.parse(datetimeString);

		} catch (ParseException e) {
			throw new RuntimeException(e);
		}	
	}
	
	private String getDatetimeFromDate(Date date) {
		
		if (date == null) {
			return null;
		}
		
		SimpleDateFormat datetimeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
		
		return datetimeFormat.format(date);
	}
	
	public WordTestSM2WordStat getNextWordStat(int maxNewWordsLimit) {
				
		boolean canGetNextWordStat = canGetNextWordStat(maxNewWordsLimit);
		
		WordTestSM2WordStat wordStat = null;
		
		if (canGetNextWordStat == true) {			
			wordStat = getNextNewWordStat(maxNewWordsLimit);
			
		} else {			
			wordStat = getNextRepeatWordStat();
		}
		
		return wordStat;
	}
	
	public int getNextWordSize(int maxNewWordsLimit) {		
		return countNextNewWordSize(maxNewWordsLimit) + countNextRepeatWordSize();
	}
	
	public int countNextNewWordSize(int maxNewWordsLimit) {
		
		boolean canGetNextWordStat = canGetNextWordStat(maxNewWordsLimit);
		
		if (canGetNextWordStat == false) {
			return 0;
		}
		
		WordTestSM2DayStat currentDayStat = getCurrentDayStat();
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.countNextNewWordStatSql, new String[] { });
			
			cursor.moveToFirst();
			
			int resultFromCount = cursor.getInt(0);
			
			if (resultFromCount > (maxNewWordsLimit - currentDayStat.getNewWords())) {
				return maxNewWordsLimit - currentDayStat.getNewWords();
				
			} else {
				return resultFromCount;
			}			
			
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	public int countNextRepeatWordSize() {
		
		Cursor cursor = null;
		
		try {
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.countNextRepeatWordStatSql, new String[] { });
			
			cursor.moveToFirst();
			
			return cursor.getInt(0);			
			
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	private boolean canGetNextWordStat(int maxNewWordsLimit) {
		
		WordTestSM2DayStat currentDateStat = getCurrentDayStat();
		
		if (currentDateStat.getNewWords() >= maxNewWordsLimit) {
			return false;
		}
		
		return true;
	}
	
	private WordTestSM2WordStat getNextNewWordStat(int maxNewWordsLimit) {
		
		Cursor cursor = null;
		
		try {			
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.selectNextNewWordStatSql, new String[] { });
			
			boolean moveToFirstResult = cursor.moveToFirst();
			
			if (moveToFirstResult == false) {
				return null;
			}			
			
			return getWordTestSM2WordStatFromCursor(cursor, true);

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}

	private WordTestSM2WordStat getNextRepeatWordStat() {
				
		Cursor cursor = null;
		
		try {			
			cursor = sqliteDatabase.rawQuery(SQLiteStatic.selectNextRepeatWordStatSql, new String[] { });
			
			boolean moveToFirstResult = cursor.moveToFirst();
			
			if (moveToFirstResult == false) {
				return null;
			}			
			
			return getWordTestSM2WordStatFromCursor(cursor, false);

		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
	}
	
	private WordTestSM2WordStat getWordTestSM2WordStatFromCursor(Cursor cursor, boolean wasNew) {
		
		int id = cursor.getInt(0);
		int power = cursor.getInt(1);
		float easinessFactor = cursor.getFloat(2);
		int repetitions = cursor.getInt(3);
		int interval = cursor.getInt(4);
		String nextRepetitions = cursor.getString(5);
		String lastStudied = cursor.getString(6);
		
		WordTestSM2WordStat wordStat = new WordTestSM2WordStat();
		
		wordStat.setId(id);
		wordStat.setPower(power);
		wordStat.setEasinessFactor(easinessFactor);
		wordStat.setRepetitions(repetitions);
		wordStat.setInterval(interval);
		wordStat.setNextRepetitions(getDatetimeFromString(nextRepetitions));
		wordStat.setLastStudied(getDatetimeFromString(lastStudied));
		wordStat.setWasNew(wasNew);
		
		return wordStat;
	}
	
	public void updateWordStat(WordTestSM2WordStat wordStat) {
		
		sqliteDatabase.execSQL(SQLiteStatic.updateWordStatSql, 
				new Object[] { wordStat.getEasinessFactor(), wordStat.getRepetitions(),
					wordStat.getInterval(), getDatetimeFromDate(wordStat.getNextRepetitions()),
					getDatetimeFromDate(wordStat.getLastStudied()), wordStat.getId() });
		
		if (wordStat.isWasNew() == true) {
			
			sqliteDatabase.execSQL(SQLiteStatic.updateDayStatNewRepeatWordsSql,
				new Object[] { 1 });
			
		}
	}
	
	public void updateCurrentDayStat(WordTestSM2DayStat dayStat) {		
		sqliteDatabase.execSQL(SQLiteStatic.updateDayStatSql, new Object[] { dayStat.getNewWords() } );		
	}
	
	public void setNextDay() {
		sqliteDatabase.execSQL(SQLiteStatic.reverseWordStatNextRepetionsOneDaySql);
		
		sqliteDatabase.execSQL(SQLiteStatic.resetDayStatSql);
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
		
		public static final String dayStatTableName = "DayStat";
		
		public static final String dayStatTable_id = "id";
		public static final String dayStatTable_dateStat = "dateStat";
		public static final String dayStatTable_newWords = "newWords";
				
		public static final String wordStatTableCreate =
				"create table " + wordStatTableName + "(" +
				wordStatTable_id + " integer primary key, " +
				wordStatTable_power + " integer not null, " +
				wordStatTable_easinessFactor + " real not null, " +
				wordStatTable_repetitions + " integer not null, " +
				wordStatTable_interval + " integer not null, " +
				wordStatTable_nextRepetitions + " datetime null, " +
				wordStatTable_lastStudied + " datetime null);";
		
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
		
		public static final String dayStatTableCreate =
				"create table " + dayStatTableName + "(" +
				dayStatTable_id + " integer primary key, " +
				dayStatTable_dateStat + " date not null, " +
				dayStatTable_newWords + " integer not null);";
		
		public static final String dayStatTableCreateDateStatKeyIndex = 
				"create unique index " + dayStatTableName + "DateStatKeyIdx on " +
				dayStatTableName + "(" + dayStatTable_dateStat + ")";
		
		// update DayStat set newWords = newWords + 0, repeatWords = repeatWords + 1 where dateStat = date('now');
		
		public static final String updateDayStatNewRepeatWordsSql =
				"update " + dayStatTableName + " set " +
				dayStatTable_newWords + " = " + dayStatTable_newWords + " + ? " +
				" where " +
				dayStatTable_dateStat + " = date('now');";

		public static final String updateDayStatSql =
				"update " + dayStatTableName + " set " +
				dayStatTable_newWords + " = ? " +
				" where " +
				dayStatTable_dateStat + " = date('now');";

		public static final String countObjectSql = 
				"select count(*) from sqlite_master where type = ? and name = ?";
		
		public static final String countWordStatSql =
				"select count(*) from " + wordStatTableName + " where " + wordStatTable_id + " = ? ";
		
		public static final String insertWordStatSql =
				"insert into " + wordStatTableName + " values(?, ?, '2.5', '0', '0', NULL, NULL);";
		
		public static final String updateWordStatPowerSql =
				"update " + wordStatTableName + " set " + wordStatTable_power + " = ? where " + wordStatTable_id + " = ? ";
		
		public static final String updateWordStatSql =
				"update " + wordStatTableName + " set " + wordStatTable_easinessFactor + " = ?, " +
				wordStatTable_repetitions + " = ?, " +
				wordStatTable_interval + " = ?, " + 
				wordStatTable_nextRepetitions + " = datetime(?), " +
				wordStatTable_lastStudied + " = datetime(?) where " + wordStatTable_id + " = ? ";				
		
		public static final String selectConfigSql =
				"select " + configTable_value + " from " + configTableName + " where " + configTable_name + " = ?";
		
		public static final String insertOrReplaceConfigSql =
				"insert or replace into " + configTableName + " ( " + configTable_name + " , " + configTable_value + " ) values (?, ?)";
		
		public static final String selectCurrentDateStatSql =
				"select " +
				" " + dayStatTable_id + ", " +
				" datetime( " + dayStatTable_dateStat + "), " +
				" " + dayStatTable_newWords + " " + 
				" from " + dayStatTableName + " where dateStat = date('now');";
		
		public static final String insertNewCurrentDateStatSql =
				"insert into " + dayStatTableName + " (" + dayStatTable_dateStat + " , " + dayStatTable_newWords + " ) " + 
				" values (date('now'), 0);";
		
		public static final String selectNextNewWordStatSql =
				"select " + wordStatTable_id + ", " + wordStatTable_power + " , " + wordStatTable_easinessFactor  + " , " + 
				" " + wordStatTable_repetitions + " , " + wordStatTable_interval + " , " + 
				" datetime( " + wordStatTable_nextRepetitions + " ) , " +
				" datetime( " + wordStatTable_lastStudied + " ) from " + wordStatTableName + " " +
				" where " + wordStatTable_nextRepetitions + " IS NULL order by " + wordStatTable_power + " , " + wordStatTable_id + " " +				
				" limit 1";
		
		public static final String countNextNewWordStatSql =
				"select count(*) from " + wordStatTableName + " " +
				" where " + wordStatTable_nextRepetitions + " IS NULL order by " + wordStatTable_power + " , " + wordStatTable_id + " ";
		
		public static final String selectNextRepeatWordStatSql =
				"select " + wordStatTable_id + ", " + wordStatTable_power + " , " + wordStatTable_easinessFactor  + " , " + 
				" " + wordStatTable_repetitions + " , " + wordStatTable_interval + " , " + 
				" datetime( " + wordStatTable_nextRepetitions + " ) , " +
				" datetime( " + wordStatTable_lastStudied + " ) from " + wordStatTableName + " " +
				" where " + wordStatTable_nextRepetitions + " IS NOT NULL and " +
				wordStatTable_nextRepetitions + " < date('now', '+1 day') order by random() limit 1 ";
		
		public static final String countNextRepeatWordStatSql = 
				"select count(*) from " + wordStatTableName + " " +
				" where " + wordStatTable_nextRepetitions + " IS NOT NULL and " +
				wordStatTable_nextRepetitions + " < date('now', '+1 day') order by " + wordStatTable_nextRepetitions + " , " +
				wordStatTable_power + " , " + wordStatTable_id + " ";
		
		public static final String reverseWordStatNextRepetionsOneDaySql =
				"update " + wordStatTableName + " set " + wordStatTable_nextRepetitions + " = datetime( " + 
				wordStatTable_nextRepetitions + ", '-1 day') where " + wordStatTable_nextRepetitions + " IS NOT NULL;";
		
		public static final String resetDayStatSql = 
				"update " + dayStatTableName + " set " + dayStatTable_newWords + " = 0 where " + dayStatTable_dateStat + " = date('now');";
	}
}
