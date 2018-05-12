package pl.idedyk.android.japaneselearnhelper.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import pl.idedyk.android.japaneselearnhelper.dictionary.WordTestSM2Manager;

public class SQLiteDatabaseHelper {

    private static final String countObjectSql =
            "select count(*) from sqlite_master where type = ? and name = ?";

    public static boolean isObjectExists(SQLiteDatabase sqliteDatabase, String type, String name) {

        Cursor cursor = null;

        try {
            cursor = sqliteDatabase.rawQuery(countObjectSql, new String[] { type, name });

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
}
