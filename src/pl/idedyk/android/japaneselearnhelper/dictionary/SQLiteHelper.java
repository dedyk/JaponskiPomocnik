package pl.idedyk.android.japaneselearnhelper.dictionary;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLiteHelper extends SQLiteOpenHelper {
		
	private boolean needInsertData = false;

	public SQLiteHelper(Context context, int version) {
		super(context, SQLiteStatic.databaseName, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		
		db.execSQL(SQLiteStatic.dictionaryEntriesTableCreate);
		
		needInsertData = true;
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				
	    db.execSQL("DROP TABLE IF EXISTS " + SQLiteStatic.dictionaryEntriesTableName);
	    
	    onCreate(db);
	}

	public boolean isNeedInsertData() {
		return needInsertData;
	}
}
