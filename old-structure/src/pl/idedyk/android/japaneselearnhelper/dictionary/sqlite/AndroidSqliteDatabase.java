package pl.idedyk.android.japaneselearnhelper.dictionary.sqlite;

import java.util.Iterator;
import java.util.Map;

import android.content.ContentValues;
import pl.idedyk.japanese.dictionary.api.dictionary.sqlite.Cursor;
import pl.idedyk.japanese.dictionary.api.dictionary.sqlite.SQLiteDatabase;

public class AndroidSqliteDatabase implements SQLiteDatabase {
	
	private String path;
	
	private android.database.sqlite.SQLiteDatabase sqliteDatabase;
	
	public AndroidSqliteDatabase(String path) {
		this.path = path;
	}
		
	@Override
	public void open() {
		sqliteDatabase = android.database.sqlite.SQLiteDatabase.openDatabase(path, null, android.database.sqlite.SQLiteDatabase.OPEN_READWRITE);
	}

	@Override
	public void close() {
		sqliteDatabase.close();
	}
	
	@Override
	public void beginTransaction() {
		sqliteDatabase.beginTransaction();
	}

	@Override
	public void setTransactionSuccessful() {
		sqliteDatabase.setTransactionSuccessful();
	}

	@Override
	public void endTransaction() {
		sqliteDatabase.endTransaction();
	}

	@Override
	public Cursor rawQuery(String query, String[] params) {
		
		android.database.Cursor cursor = sqliteDatabase.rawQuery(query, params);
		
		return new AndroidCursor(cursor);
	}

	@Override
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy) {
		
		android.database.Cursor cursor = sqliteDatabase.query(
				table, columns, selection, selectionArgs, groupBy, having, orderBy);
		
		return new AndroidCursor(cursor);
	}
	
	@Override
	public Cursor query(String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		
		android.database.Cursor cursor = sqliteDatabase.query(
				table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		
		return new AndroidCursor(cursor);
	}
	
	@Override
	public Cursor query(boolean distinct, String table, String[] columns, String selection, String[] selectionArgs, String groupBy, String having, String orderBy, String limit) {
		
		android.database.Cursor cursor = sqliteDatabase.query(
				distinct, table, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
		
		return new AndroidCursor(cursor);
	}
	
	@Override
	public void execSQL(String sql) {
		sqliteDatabase.execSQL(sql);
	}

	@Override
	public long insertOrThrow(String table, String nullColumnHack, Map<String, Object> values) {
		
		ContentValues contentValues = new ContentValues();
		
		Iterator<String> valuesKeySetIterator = values.keySet().iterator();
		
		while (valuesKeySetIterator.hasNext()) {
			
			String currentKey = valuesKeySetIterator.next();
			
			Object currentValue = values.get(currentKey);
			
			if (currentValue instanceof Boolean) {
				contentValues.put(currentKey, (Boolean)currentValue);
				
			} else if (currentValue instanceof Byte) {
				contentValues.put(currentKey, (Byte)currentValue);
				
			} else if (currentValue instanceof byte[]) {
				contentValues.put(currentKey, (byte[])currentValue);
				
			} else if (currentValue instanceof Double) {
				contentValues.put(currentKey, (Double)currentValue);
				
			} else if (currentValue instanceof Float) {
				contentValues.put(currentKey, (Float)currentValue);
				
			} else if (currentValue instanceof Integer) {
				contentValues.put(currentKey, (Integer)currentValue);
				
			} else if (currentValue instanceof Long) {
				contentValues.put(currentKey, (Long)currentValue);
				
			} else if (currentValue instanceof Short) {
				contentValues.put(currentKey, (Short)currentValue);
				
			} else if (currentValue instanceof String) {
				contentValues.put(currentKey, (String)currentValue);
				
			} else {
				throw new RuntimeException("Unknown object type: " + currentValue.getClass());
			}
		}		
		
		return sqliteDatabase.insertOrThrow(table, nullColumnHack, contentValues);
	}
}

