package pl.idedyk.android.japaneselearnhelper.data;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import pl.idedyk.android.japaneselearnhelper.data.exception.DataManagerException;
import pl.idedyk.android.japaneselearnhelper.data.exception.DataManagerException;
import pl.idedyk.android.japaneselearnhelper.utils.SQLiteDatabaseHelper;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;

public class DataManager {

    private static final String DATABASE_FILE = "data.db";

    private File databaseDir;

    private SQLiteDatabase sqliteDatabase;

    public DataManager(File databaseDir) {
        this.databaseDir = databaseDir;
    }

    public void open() throws DataManagerException {

        try {
            File databaseFile = new File(databaseDir, DATABASE_FILE);

            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(databaseFile, null);

            // create table if needed
            if (SQLiteDatabaseHelper.isObjectExists(sqliteDatabase, "table", SQLiteStatic.favouriteDictionaryEntryTableName) == false) {

                sqliteDatabase.execSQL(SQLiteStatic.favouriteDictionaryEntryTableCreate);

                sqliteDatabase.execSQL(SQLiteStatic.favouriteDictionaryEntryTableCreateDictionaryEntryIdIndex);
            }

            if (SQLiteDatabaseHelper.isObjectExists(sqliteDatabase, "table", SQLiteStatic.favouriteKanjiEntryTableName) == false) {

                sqliteDatabase.execSQL(SQLiteStatic.favouriteKanjiEntryTableCreate);

                sqliteDatabase.execSQL(SQLiteStatic.favouriteKanjiEntryTableCreateKanjiEntryIdIndex);
            }

        } catch (SQLException e) {
            throw new DataManagerException(e.toString());
        }
    }

    public void close() {

        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }
    }

    public void addDictionaryEntryToFavouriteList(DictionaryEntry dictionaryEntry) {

        boolean alreadyExists = isDictionaryEntryExistsInFavouriteList(dictionaryEntry);

        if (alreadyExists == false) { // add
            sqliteDatabase.execSQL(SQLiteStatic.insertFavouriteDictionaryEntryDictionaryEntryIdSql, new Object[] { dictionaryEntry.getId() });
        }
    }

    public void deleteDictionaryEntryFromFavouriteList(DictionaryEntry dictionaryEntry) {

        boolean alreadyExists = isDictionaryEntryExistsInFavouriteList(dictionaryEntry);

        if (alreadyExists == true) { // delete
            sqliteDatabase.execSQL(SQLiteStatic.deleteFavouriteDictionaryEntryDictionaryEntryIdSql, new Object[] { dictionaryEntry.getId() });
        }
    }

    public boolean isDictionaryEntryExistsInFavouriteList(DictionaryEntry dictionaryEntry) {
        return isExistsInFavouriteList(SQLiteStatic.countFavouriteDictionaryEntryDictionaryEntryIdSql, dictionaryEntry.getId());
    }

    //

    public void addKanjiEntryToFavouriteList(KanjiEntry kanjiEntry) {

        boolean alreadyExists = isKanjiEntryExistsInFavouriteList(kanjiEntry);

        if (alreadyExists == false) { // add
            sqliteDatabase.execSQL(SQLiteStatic.insertFavouriteKanjiEntryKanjiEntryIdSql, new Object[] { kanjiEntry.getId() });
        }
    }

    public void deleteKanjiEntryFromFavouriteList(KanjiEntry kanjiEntry) {

        boolean alreadyExists = isKanjiEntryExistsInFavouriteList(kanjiEntry);

        if (alreadyExists == true) { // delete
            sqliteDatabase.execSQL(SQLiteStatic.deleteFavouriteKanjiEntryKanjiEntryIdSql, new Object[] { kanjiEntry.getId() });
        }
    }

    public boolean isKanjiEntryExistsInFavouriteList(KanjiEntry kanjiEntry) {
        return isExistsInFavouriteList(SQLiteStatic.countFavouriteKanjiEntryKanjiEntryIdSql, kanjiEntry.getId());
    }

    //

    private boolean isExistsInFavouriteList(String sql, Integer id) {

        Cursor cursor = null;

        try {
            cursor = sqliteDatabase.rawQuery(sql, new String[] { String.valueOf(id) });

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

    private static class SQLiteStatic {

        public static final String favouriteDictionaryEntryTableName = "favourite_dictionary_entry";
        public static final String favouriteKanjiEntryTableName = "favourite_kanji_entry";

        public static final String favouriteDictionaryEntry_id = "id";
        public static final String favouriteDictionaryEntry_dictionary_entry_id = "dictionary_entry_id";

        public static final String favouriteKanjiEntry_id = "id";
        public static final String favouriteKanjiEntry_kanji_entry_id = "kanji_entry_id";

        //

        public static final String favouriteDictionaryEntryTableCreate =
                "create table " + favouriteDictionaryEntryTableName + "(" +
                        favouriteDictionaryEntry_id + " integer primary key, " +
                        favouriteDictionaryEntry_dictionary_entry_id + " integer not null);";

        public static final String favouriteDictionaryEntryTableCreateDictionaryEntryIdIndex =
                "create index " + favouriteDictionaryEntryTableName + "_dictionary_entry_id_idx on " +
                        favouriteDictionaryEntryTableName + "(" + favouriteDictionaryEntry_dictionary_entry_id + ")";
        //

        public static final String favouriteKanjiEntryTableCreate =
                "create table " + favouriteKanjiEntryTableName + "(" +
                        favouriteKanjiEntry_id + " integer primary key, " +
                        favouriteKanjiEntry_kanji_entry_id + " integer not null);";

        public static final String favouriteKanjiEntryTableCreateKanjiEntryIdIndex =
                "create index " + favouriteKanjiEntryTableName + "_kanji_entry_id_idx on " +
                        favouriteKanjiEntryTableName + "(" + favouriteKanjiEntry_kanji_entry_id + ")";

        //

        public static final String countFavouriteDictionaryEntryDictionaryEntryIdSql =
                "select count(*) from " + favouriteDictionaryEntryTableName + " where " + favouriteDictionaryEntry_dictionary_entry_id + " = ? ";

        public static final String insertFavouriteDictionaryEntryDictionaryEntryIdSql =
                "insert into " + favouriteDictionaryEntryTableName + "(" + favouriteDictionaryEntry_dictionary_entry_id + ") values(?);";

        public static final String deleteFavouriteDictionaryEntryDictionaryEntryIdSql =
                "delete from " + favouriteDictionaryEntryTableName + " where " + favouriteDictionaryEntry_dictionary_entry_id + " = ?;";

        //

        public static final String countFavouriteKanjiEntryKanjiEntryIdSql =
                "select count(*) from " + favouriteKanjiEntryTableName + " where " + favouriteKanjiEntry_kanji_entry_id + " = ? ";

        public static final String insertFavouriteKanjiEntryKanjiEntryIdSql =
                "insert into " + favouriteKanjiEntryTableName + "(" + favouriteKanjiEntry_kanji_entry_id + ") values(?);";

        public static final String deleteFavouriteKanjiEntryKanjiEntryIdSql =
                "delete from " + favouriteKanjiEntryTableName + " where " + favouriteKanjiEntry_kanji_entry_id + " = ?;";
    }
}