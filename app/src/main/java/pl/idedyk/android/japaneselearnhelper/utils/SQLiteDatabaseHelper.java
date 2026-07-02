package pl.idedyk.android.japaneselearnhelper.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.WordTestSM2Manager;

public class SQLiteDatabaseHelper {

    private static final String countObjectSql = "select count(*) from sqlite_master where type = ? and name = ?";

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

    public static List<String> getColumnNames(SQLiteDatabase sqliteDatabase, String tableName) {
        Cursor cursor = null;

        List<String> result = new ArrayList<>();

        try {
            cursor = sqliteDatabase.rawQuery("PRAGMA table_info(" + tableName + ")", new String[] { });

            if (cursor.moveToFirst() == false) {
                return result;
            }

            do {
                int nameIdx = cursor.getColumnIndex("name");

                String currentColumnName = cursor.getString(nameIdx);

                result.add(currentColumnName);

            } while (cursor.moveToNext());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return result;
    }
}
