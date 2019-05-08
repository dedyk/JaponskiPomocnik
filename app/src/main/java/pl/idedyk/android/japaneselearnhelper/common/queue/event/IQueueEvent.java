package pl.idedyk.android.japaneselearnhelper.common.queue.event;

import java.util.Date;
import java.util.Map;

public interface IQueueEvent {

    public static String dateFormat = "yyyy-MM-dd HH:mm:ss";

    public String getUUID();

    public QueueEventOperation getQueryEventOperation();

    public Date getCreateDate();

    public String getCreateDateAsString();

    public Map<String, String> getParams();

    public String getParamsAsString();
}
