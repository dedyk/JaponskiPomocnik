package pl.idedyk.android.japaneselearnhelper.data.entity;

import java.io.Serializable;

public class UserGroupItemEntity implements Serializable {

    private Integer id;

    private Integer userGroupId;

    private Type type;

    private Integer itemId;

    public UserGroupItemEntity(Integer id, Integer userGroupId, Type type, Integer itemId) {
        this.id = id;
        this.userGroupId = userGroupId;
        this.type = type;
        this.itemId = itemId;
    }

    public Integer getId() {
        return id;
    }

    public Integer getUserGroupId() {
        return userGroupId;
    }

    public Type getType() {
        return type;
    }

    public Integer getItemId() {
        return itemId;
    }

    public static enum Type {

        DICTIONARY_ENTRY,

        KANJI_ENTRY;
    }
}
