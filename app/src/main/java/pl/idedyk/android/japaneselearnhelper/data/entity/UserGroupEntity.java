package pl.idedyk.android.japaneselearnhelper.data.entity;

import java.io.Serializable;

public class UserGroupEntity implements Serializable {

    private Integer id;

    private Type type;

    private String name;

    public UserGroupEntity(Integer id, Type type, String name) {
        this.id = id;
        this.type = type;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public Type getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static enum Type {
        STAR_GROUP,

        USER_GROUP;
    }
}
