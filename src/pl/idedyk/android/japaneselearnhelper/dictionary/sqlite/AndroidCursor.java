package pl.idedyk.android.japaneselearnhelper.dictionary.sqlite;

import pl.idedyk.japanese.dictionary.api.dictionary.sqlite.Cursor;

public class AndroidCursor implements Cursor {
	
	private android.database.Cursor cursor;
	
	public AndroidCursor(android.database.Cursor cursor) {
		this.cursor = cursor;
	}

	@Override
	public boolean moveToFirst() {
		return cursor.moveToFirst();
	}
	
	@Override
	public boolean moveToNext() {
		return cursor.moveToNext();
	}
	
	@Override
	public boolean isAfterLast() {
		return cursor.isAfterLast();
	}
	
	@Override
	public String getString(int idx) {
		return cursor.getString(idx);
	}
	
	@Override
	public int getInt(int idx) {
		return cursor.getInt(idx);
	}

	@Override
	public void close() {
		cursor.close();
	}
}
