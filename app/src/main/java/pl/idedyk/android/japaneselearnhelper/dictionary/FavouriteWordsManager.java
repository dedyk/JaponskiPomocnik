package pl.idedyk.android.japaneselearnhelper.dictionary;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;

import pl.idedyk.android.japaneselearnhelper.dictionary.exception.FavouriteWordsManagerException;
import pl.idedyk.android.japaneselearnhelper.utils.SQLiteDatabaseHelper;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;

public class FavouriteWordsManager {

    private static final String DATABASE_FILE = "words_favourite.db";

    private File databaseDir;

    private SQLiteDatabase sqliteDatabase;

    public FavouriteWordsManager(File databaseDir) {
        this.databaseDir = databaseDir;
    }

    public void open() throws FavouriteWordsManagerException {

        try {
            File databaseFile = new File(databaseDir, DATABASE_FILE);

            sqliteDatabase = SQLiteDatabase.openOrCreateDatabase(databaseFile, null);

            // create table if needed
            if (SQLiteDatabaseHelper.isObjectExists(sqliteDatabase, "table", SQLiteStatic.favouriteWordsTableName) == false) {

                sqliteDatabase.execSQL(SQLiteStatic.favouriteWordsTableCreate);

                sqliteDatabase.execSQL(SQLiteStatic.favouriteWordsTableCreateWordDictionaryIdIndex);
            }

        } catch (SQLException e) {
            throw new FavouriteWordsManagerException(e.toString());
        }
    }

    public void close() {

        if (sqliteDatabase != null) {
            sqliteDatabase.close();
        }
    }

    public void addDictionaryEntry(DictionaryEntry dictionaryEntry) {

        boolean alreadyExists = isDictionaryEntryExists(dictionaryEntry);

        if (alreadyExists == false) { // add
            sqliteDatabase.execSQL(SQLiteStatic.insertFavouriteWordsDictionaryIdSql, new Object[] { dictionaryEntry.getId() });
        }
    }

    public void deleteDictionaryEntry(DictionaryEntry dictionaryEntry) {

        boolean alreadyExists = isDictionaryEntryExists(dictionaryEntry);

        if (alreadyExists == true) { // delete
            sqliteDatabase.execSQL(SQLiteStatic.deleteFavouriteWordsDictionaryIdSql, new Object[] { dictionaryEntry.getId() });
        }
    }

    public boolean isDictionaryEntryExists(DictionaryEntry dictionaryEntry) {

        Cursor cursor = null;

        try {
            cursor = sqliteDatabase.rawQuery(SQLiteStatic.countFavouriteWordsWordDictionaryIdSql, new String[] { String.valueOf(dictionaryEntry.getId()) });

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

        public static final String favouriteWordsTableName = "favourite_words";

        public static final String favouriteWords_id = "id";
        public static final String favouriteWords_word_dictionary_id = "word_dictionary_id";

        //

        public static final String favouriteWordsTableCreate =
                "create table " + favouriteWordsTableName + "(" +
                        favouriteWords_id + " integer primary key, " +
                        favouriteWords_word_dictionary_id + " integer not null);";

        public static final String favouriteWordsTableCreateWordDictionaryIdIndex =
                "create index " + favouriteWordsTableName + "_word_dictionary_id_idx on " +
                        favouriteWordsTableName + "(" + favouriteWords_word_dictionary_id + ")";

        //

        public static final String countFavouriteWordsWordDictionaryIdSql =
                "select count(*) from " + favouriteWordsTableName + " where " + favouriteWords_word_dictionary_id + " = ? ";

        public static final String insertFavouriteWordsDictionaryIdSql =
                "insert into " + favouriteWordsTableName + "(" + favouriteWords_word_dictionary_id + ") values(?);";

        public static final String deleteFavouriteWordsDictionaryIdSql =
                "delete from " + favouriteWordsTableName + " where " + favouriteWords_word_dictionary_id + " = ?;";
    }
}