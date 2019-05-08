package pl.idedyk.android.japaneselearnhelper.common.queue.event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatLogScreenEvent extends QueueEventCommon {

    private String screenName;

    public StatLogScreenEvent(String screenName) {

        super();

        this.screenName = screenName;
    }

    public StatLogScreenEvent(String uuid, Date createDate, String params) {

        super(uuid, createDate);

        Map<String, String> paramsMap = getParamsFromString(params);

        this.screenName = paramsMap.get("screenName");
    }

    public String getUUID() {
        return uuid;
    }

    @Override
    public QueueEventOperation getQueryEventOperation() {
        return QueueEventOperation.STAT_LOG_SCREEN_EVENT;
    }

    @Override
    public Map<String, String> getParams() {

        Map<String, String> result = new HashMap<>();

        result.put("screenName", screenName);

        return result;
    }
}
