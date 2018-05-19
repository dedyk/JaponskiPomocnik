package pl.idedyk.android.japaneselearnhelper.data;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupItemEntity;
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

            if (SQLiteDatabaseHelper.isObjectExists(sqliteDatabase, "table", SQLiteStatic.user_groups_table_name) == false) {
                sqliteDatabase.execSQL(SQLiteStatic.user_groups_sql_create);

                UserGroupEntity userGroupEntity = new UserGroupEntity(null, UserGroupEntity.Type.STAR_GROUP, "StarGroup");

                addUserGroup(userGroupEntity);
            }

            if (SQLiteDatabaseHelper.isObjectExists(sqliteDatabase, "table", SQLiteStatic.user_groups_items_table_name) == false) {
                sqliteDatabase.execSQL(SQLiteStatic.user_groups_items_sql_create);

                sqliteDatabase.execSQL(SQLiteStatic.user_groups_items_sql_index);
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

    public void addUserGroup(UserGroupEntity userGroupEntity) {
        sqliteDatabase.execSQL(SQLiteStatic.user_group_sql_insert,
                new Object[] { userGroupEntity.getType().name(), userGroupEntity.getName() });
    }

    public List<UserGroupEntity> getAllUserGroupList() {

        List<UserGroupEntity> result = new ArrayList<>();

        Cursor cursor = null;

        try {
            cursor = sqliteDatabase.rawQuery(SQLiteStatic.user_group_sql_select_all, null);

            boolean moveToFirst = cursor.moveToFirst();

            if (moveToFirst == false) {
                return result;
            }

            do {
                Integer id = cursor.getInt(0);
                UserGroupEntity.Type type = UserGroupEntity.Type.valueOf(cursor.getString(1));
                String name = cursor.getString(2);

                result.add(new UserGroupEntity(id, type, name));

            } while (cursor.moveToNext());

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return result;
    }

    public List<UserGroupEntity> findUserGroupEntity(UserGroupEntity.Type type, String name) {

        List<UserGroupEntity> allUserGroupList = getAllUserGroupList();

        List<UserGroupEntity> result = new ArrayList<>();

        for (UserGroupEntity currentUserGroupEntity : allUserGroupList) {

            if (type != null && currentUserGroupEntity.getType() != type) {
                continue;
            }

            if (name != null && currentUserGroupEntity.getName().equals(name) == false) {
                continue;
            }

            result.add(currentUserGroupEntity);
        }

        return result;
    }

    public boolean isItemIdExistsInUserGroup(UserGroupEntity userGroupEntity, UserGroupItemEntity.Type type, Integer itemId) {
        return isExists(SQLiteStatic.user_groups_items_sql_count_group_id_type_item_id, new String[] {
                String.valueOf(userGroupEntity.getId()), type.name(), String.valueOf(itemId) });
    }

    //

    public void addDictionaryEntryToFavouriteList(DictionaryEntry dictionaryEntry) {

        /*
        boolean alreadyExists = isDictionaryEntryExistsInFavouriteList(dictionaryEntry);

        if (alreadyExists == false) { // add
            sqliteDatabase.execSQL(SQLiteStatic.insertFavouriteDictionaryEntryDictionaryEntryIdSql, new Object[] { dictionaryEntry.getId() });
        }
        */
    }

    public void deleteDictionaryEntryFromFavouriteList(DictionaryEntry dictionaryEntry) {

        /*
        boolean alreadyExists = isDictionaryEntryExistsInFavouriteList(dictionaryEntry);

        if (alreadyExists == true) { // delete
            sqliteDatabase.execSQL(SQLiteStatic.deleteFavouriteDictionaryEntryDictionaryEntryIdSql, new Object[] { dictionaryEntry.getId() });
        }
        */
    }

    public boolean isDictionaryEntryExistsInFavouriteList(DictionaryEntry dictionaryEntry) {
        //return isExistsInFavouriteList(SQLiteStatic.countFavouriteDictionaryEntryDictionaryEntryIdSql, dictionaryEntry.getId());
        return false;
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
        //return isExistsInFavouriteList(SQLiteStatic.countFavouriteKanjiEntryKanjiEntryIdSql, kanjiEntry.getId());
        return false;
    }

    //

    private boolean isExists(String sql, String[] params) {

        Cursor cursor = null;

        try {
            cursor = sqliteDatabase.rawQuery(sql, params);

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

        public static final String user_groups_table_name = "user_groups";

        public static final String user_groups_column_id = "id";
        public static final String user_groups_column_type = "type";
        public static final String user_groups_column_name = "name";

        //

        public static final String user_groups_items_table_name = "user_groups_items";

        public static final String user_groups_items_column_id = "id";
        public static final String user_groups_items_column_user_group_id = "user_group_id";
        public static final String user_groups_items_column_type = "type";
        public static final String user_groups_items_column_item_id = "item_id";

        //

        public static final String user_groups_sql_create =
                "create table " + user_groups_table_name + "(" +
                        user_groups_column_id + " integer primary key, " +
                        user_groups_column_type + " varchar(30) not null, " +
                        user_groups_column_name + " varchar(100) not null);";

        //

        public static final String user_groups_items_sql_create =
                "create table " + user_groups_items_table_name + "(" +
                        user_groups_items_column_id + " integer primary key, " +
                        user_groups_items_column_user_group_id + " integer not null, " +
                        user_groups_items_column_type + " varchar(30) not null, " +
                        user_groups_items_column_item_id + " integer not null, " +
                        "foreign key (" + user_groups_items_column_user_group_id + ") references " + user_groups_table_name + "(" + user_groups_column_id + "));";

        public static final String user_groups_items_sql_index =
                String.format("create index %s_%s_%s_%s_idx on %s(%s,%s,%s)", user_groups_items_table_name,
                        user_groups_items_column_user_group_id, user_groups_items_column_type, user_groups_items_column_item_id, user_groups_items_table_name,
                        user_groups_items_column_user_group_id, user_groups_items_column_type, user_groups_items_column_item_id);

        //

        public static final String user_group_sql_insert =
                "insert into " + user_groups_table_name + "(" +
                        user_groups_column_type + ", " +   user_groups_column_name + ") " +
                        "values (?, ?)";

        public static final String user_group_sql_select_all =
                "select * from " + user_groups_table_name + ";";

        //

        public static final String user_groups_items_sql_count_group_id_type_item_id =
            String.format("select count(*) from %s where %s = ? and %s = ? and %s = ?", user_groups_items_table_name,
                    user_groups_items_column_user_group_id, user_groups_items_column_type, user_groups_items_column_item_id);

        // FIXME do usuniecia

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