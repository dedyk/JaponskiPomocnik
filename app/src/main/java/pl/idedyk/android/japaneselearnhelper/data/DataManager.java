package pl.idedyk.android.japaneselearnhelper.data;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.idedyk.japanese.dictionary.api.android.queue.event.IQueueEvent;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupItemEntity;
import pl.idedyk.android.japaneselearnhelper.data.exception.DataManagerException;
import pl.idedyk.android.japaneselearnhelper.utils.SQLiteDatabaseHelper;
import pl.idedyk.japanese.dictionary.api.android.queue.factory.IQueueEventFactory;

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

            if (SQLiteDatabaseHelper.isObjectExists(sqliteDatabase, "table", SQLiteStatic.queue_events_table_name) == false) {
                sqliteDatabase.execSQL(SQLiteStatic.queue_events_sql_create);
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

    public synchronized void addUserGroup(UserGroupEntity userGroupEntity) {
        sqliteDatabase.execSQL(SQLiteStatic.user_group_sql_insert,
                new Object[] { userGroupEntity.getType().name(), userGroupEntity.getName() });
    }

    public synchronized void updateUserGroup(UserGroupEntity userGroupEntity) {
        sqliteDatabase.execSQL(SQLiteStatic.user_group_sql_name_update, new Object[] { userGroupEntity.getName(), userGroupEntity.getId() });
    }

    public synchronized void deleteUserGroup(UserGroupEntity userGroupEntity) {

        for (String sql : SQLiteStatic.user_group_sql_delete) {
            sqliteDatabase.execSQL(sql, new Object[] { userGroupEntity.getId() });
        }
    }

    public synchronized List<UserGroupEntity> getAllUserGroupList() {

        Cursor cursor = null;

        try {
            cursor = sqliteDatabase.rawQuery(SQLiteStatic.user_group_sql_select_all, null);

            List<UserGroupEntity> allUserGroupsList = createUserGroupEntityListFromCursor(cursor);

            // sortowanie
            Collections.sort(allUserGroupsList, new Comparator<UserGroupEntity>() {

                @Override
                public int compare(UserGroupEntity o1, UserGroupEntity o2) {

                    UserGroupEntity.Type o1Type = o1.getType();
                    UserGroupEntity.Type o2Type = o2.getType();

                    if (o1Type == UserGroupEntity.Type.STAR_GROUP && o2Type != UserGroupEntity.Type.STAR_GROUP) {
                        return -1;

                    } else if (o1Type != UserGroupEntity.Type.STAR_GROUP && o2Type == UserGroupEntity.Type.STAR_GROUP) {
                        return 1;
                    }

                    return o1.getName().compareTo(o2.getName());
                }
            });

            return allUserGroupsList;

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private List<UserGroupEntity> createUserGroupEntityListFromCursor(Cursor cursor) {

        List<UserGroupEntity> result = new ArrayList<>();

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

        return result;
    }

    public synchronized List<UserGroupEntity> findUserGroupEntity(UserGroupEntity.Type type, String name) {

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

    public synchronized UserGroupEntity getStarUserGroup() throws DataManagerException {

        List<UserGroupEntity> allUserGroupList = getAllUserGroupList();

        for (UserGroupEntity currentUserGroupEntity : allUserGroupList) {

            if (currentUserGroupEntity.getType() == UserGroupEntity.Type.STAR_GROUP) {
                return currentUserGroupEntity;
            }
        }

        throw new DataManagerException("No star user group");
    }

    public synchronized List<UserGroupEntity> getUserGroupEntityListForItemId(UserGroupEntity.Type userGroupEntityType, UserGroupItemEntity.Type userGroupItemEntityType, Integer itemId) {

        Cursor cursor = null;

        try {
            cursor = sqliteDatabase.rawQuery(SQLiteStatic.user_group_sql_select_for_item_id, new String[] { userGroupItemEntityType.name(), String.valueOf(itemId), userGroupEntityType.name() });

            return createUserGroupEntityListFromCursor(cursor);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public synchronized boolean isItemIdExistsInUserGroup(UserGroupEntity userGroupEntity, UserGroupItemEntity.Type type, Integer itemId) {
        return isExists(SQLiteStatic.user_groups_items_sql_count_group_id_type_item_id, new String[] {
                String.valueOf(userGroupEntity.getId()), type.name(), String.valueOf(itemId) });
    }

    public synchronized void addItemIdToUserGroup(UserGroupEntity userGroupEntity, UserGroupItemEntity.Type type, Integer itemId) {

        boolean alreadyExists = isItemIdExistsInUserGroup(userGroupEntity, type, itemId);

        if (alreadyExists == false) { // add
            sqliteDatabase.execSQL(SQLiteStatic.user_groups_items_sql_insert_group_id_type_item_id, new Object[] { String.valueOf(userGroupEntity.getId()), type.name(), String.valueOf(itemId)  });
        }
    }

    public synchronized void deleteItemIdFromUserGroup(UserGroupEntity userGroupEntity, UserGroupItemEntity.Type type, Integer itemId) {

        boolean alreadyExists = isItemIdExistsInUserGroup(userGroupEntity, type, itemId);

        if (alreadyExists == true) { // delete
            sqliteDatabase.execSQL(SQLiteStatic.user_groups_items_sql_delete_group_id_type_item_id, new Object[] { String.valueOf(userGroupEntity.getId()), type.name(), String.valueOf(itemId)  });
        }
    }

    public synchronized List<UserGroupItemEntity> getUserGroupItemListForUserEntity(UserGroupEntity userGroupEntity) {

        Cursor cursor = null;

        try {
            cursor = sqliteDatabase.rawQuery(SQLiteStatic.user_groups_items_sql_get_list_for_user_group,
                    new String[] { String.valueOf(userGroupEntity.getId()) });

            return createUserGroupItemEntityFromCursor(cursor);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private List<UserGroupItemEntity> createUserGroupItemEntityFromCursor(Cursor cursor) {

        List<UserGroupItemEntity> result = new ArrayList<>();

        boolean moveToFirst = cursor.moveToFirst();

        if (moveToFirst == false) {
            return result;
        }

        do {
            Integer id = cursor.getInt(0);
            Integer userGroupId = cursor.getInt(1);
            UserGroupItemEntity.Type type = UserGroupItemEntity.Type.valueOf(cursor.getString(2));
            Integer itemId = cursor.getInt(3);

            result.add(new UserGroupItemEntity(id, userGroupId, type, itemId));

        } while (cursor.moveToNext());

        return result;
    }

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

    public synchronized void addQueueEvent(IQueueEvent queueEvent) {
        sqliteDatabase.execSQL(SQLiteStatic.queue_events_sql_insert, new Object[] { queueEvent.getUUID(), queueEvent.getQueryEventOperation().toString(), queueEvent.getCreateDateAsString(), queueEvent.getParamsAsString() });
    }

    public synchronized List<IQueueEvent> getQueueEventList(IQueueEventFactory queueEventFactory) {

        if (sqliteDatabase.isOpen() == false) {
            return new ArrayList<>();
        }

        Cursor cursor = null;

        try {
            cursor = sqliteDatabase.rawQuery(SQLiteStatic.queue_events_sql_select, new String[] { });

            return createQueueEventListFromCursor(queueEventFactory, cursor);

        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    private List<IQueueEvent> createQueueEventListFromCursor(IQueueEventFactory queueEventFactory, Cursor cursor) {

        List<IQueueEvent> result = new ArrayList<>();

        boolean moveToFirst = cursor.moveToFirst();

        if (moveToFirst == false) {
            return result;
        }

        do {
            //Integer id = cursor.getInt(0);
            String uuid = cursor.getString(1);
            String operation = cursor.getString(2);
            String createDate = cursor.getString(3);
            String params = cursor.getString(4);

            IQueueEvent queueEvent = queueEventFactory.createQueueEvent(uuid, operation, createDate, params);

            if (queueEvent != null) {
                result.add(queueEvent);
            }

        } while (cursor.moveToNext());

        return result;
    }

    public synchronized void deleteQueueEvent(String uuid) {

        if (sqliteDatabase.isOpen() == false) {
            return;
        }

        sqliteDatabase.execSQL(SQLiteStatic.queue_events_sql_delete_uuid, new Object[] { uuid  });
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

        public static final String queue_events_table_name = "queue_events";

        public static final String queue_events_column_id = "id";
        public static final String queue_events_column_uuid = "uuid";
        public static final String queue_events_column_operation = "operation";
        public static final String queue_events_column_createDate = "createDate";
        public static final String queue_events_column_params = "params";

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

        public static final String queue_events_sql_create =
                "create table " + queue_events_table_name + "(" +
                        queue_events_column_id + " integer primary key, " +
                        queue_events_column_uuid + " varchar(40) not null, " +
                        queue_events_column_operation + " varchar(50) not null, " +
                        queue_events_column_createDate + " text not null, " +
                        queue_events_column_params + " text null);";


        public static final String user_groups_items_sql_index =
                String.format("create index %s_%s_%s_%s_idx on %s(%s,%s,%s)", user_groups_items_table_name,
                        user_groups_items_column_user_group_id, user_groups_items_column_type, user_groups_items_column_item_id, user_groups_items_table_name,
                        user_groups_items_column_user_group_id, user_groups_items_column_type, user_groups_items_column_item_id);

        //

        public static final String user_group_sql_insert =
                "insert into " + user_groups_table_name + "(" +
                        user_groups_column_type + ", " +   user_groups_column_name + ") " +
                        "values (?, ?)";

        public static final String user_group_sql_name_update =
                String.format("update %s set %s = ? where %s = ?", user_groups_table_name, user_groups_column_name, user_groups_items_column_id);

        public static final String[] user_group_sql_delete =
                new String[] {
                        String.format("delete from %s where %s = ?", user_groups_items_table_name, user_groups_items_column_user_group_id),
                        String.format("delete from %s where %s = ?", user_groups_table_name, user_groups_column_id),
                };

        public static final String user_group_sql_select_all =
                "select * from " + user_groups_table_name + ";";

        public static final String user_group_sql_select_for_item_id =
                String.format("select * from %s ug where ug.%s in (select %s from %s ugi where ugi.%s = ? and ugi.%s = ?) and ug.%s = ? order by %s",
                        user_groups_table_name, user_groups_column_id, user_groups_items_column_user_group_id,
                        user_groups_items_table_name, user_groups_items_column_type, user_groups_items_column_item_id, user_groups_column_type,
                        user_groups_column_name);

        //

        public static final String user_groups_items_sql_count_group_id_type_item_id =
            String.format("select count(*) from %s where %s = ? and %s = ? and %s = ?", user_groups_items_table_name,
                    user_groups_items_column_user_group_id, user_groups_items_column_type, user_groups_items_column_item_id);

        public static final String user_groups_items_sql_insert_group_id_type_item_id =
                String.format("insert into %s (%s, %s, %s) values (?, ?, ?)", user_groups_items_table_name,
                        user_groups_items_column_user_group_id, user_groups_items_column_type, user_groups_items_column_item_id);

        public static final String user_groups_items_sql_delete_group_id_type_item_id =
                String.format("delete from %s where %s = ? and %s = ? and %s = ?", user_groups_items_table_name,
                        user_groups_items_column_user_group_id, user_groups_items_column_type, user_groups_items_column_item_id);

        public static final String user_groups_items_sql_get_list_for_user_group =
                String.format("select * from %s where %s = ?", user_groups_items_table_name, user_groups_items_column_user_group_id);

        //

        public static final String queue_events_sql_insert =
                String.format("insert into %s (%s, %s, %s, %s) values (?, ?, ?, ?)", queue_events_table_name,
                        queue_events_column_uuid, queue_events_column_operation, queue_events_column_createDate, queue_events_column_params);

        public static final String queue_events_sql_select =
                String.format("select * from %s order by %s limit 10", queue_events_table_name, queue_events_column_id);

        public static final String queue_events_sql_delete_uuid =
                String.format("delete from %s where %s = ?", queue_events_table_name, queue_events_column_uuid);
    }
}