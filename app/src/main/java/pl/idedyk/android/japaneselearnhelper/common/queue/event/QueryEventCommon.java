package pl.idedyk.android.japaneselearnhelper.common.queue.event;

import java.util.Date;
import java.util.UUID;

public abstract class QueryEventCommon implements IQueryEvent {

    protected String uuid;

    protected Date createDate;

    protected QueryEventCommon() {
        this.uuid = UUID.randomUUID().toString();
        this.createDate = new Date();
    }

    @Override
    public String getUUID() {
        return uuid;
    }

    @Override
    public Date getCreateDate() {
        return createDate;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || obj instanceof QueryEventCommon == false) {
            return false;
        }

        QueryEventCommon objAsQueryEventCommon = (QueryEventCommon)obj;

        return uuid.equals(objAsQueryEventCommon.uuid);
    }
}
