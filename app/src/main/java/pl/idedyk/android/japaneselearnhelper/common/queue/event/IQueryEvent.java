package pl.idedyk.android.japaneselearnhelper.common.queue.event;

import java.util.Date;
import java.util.Map;

public interface IQueryEvent {

    public String getUUID();

    public QueryEventOperation getQueryEventOperation();

    public Date getCreateDate();

    public Map<String, String> getParams();
}
