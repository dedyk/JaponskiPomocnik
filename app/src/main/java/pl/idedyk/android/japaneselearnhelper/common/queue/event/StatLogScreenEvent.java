package pl.idedyk.android.japaneselearnhelper.common.queue.event;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class StatLogScreenEvent extends QueryEventCommon {

    private String screenName;

    public StatLogScreenEvent(String screenName) {

        super();

        this.screenName = screenName;
    }

    public String getUUID() {
        return uuid;
    }

    @Override
    public QueryEventOperation getQueryEventOperation() {
        return QueryEventOperation.STAT_LOG_SCREEN_EVENT;
    }

    @Override
    public Map<String, String> getParams() {

        Map<String, String> result = new HashMap<>();

        result.put("screenName", screenName);

        return result;
    }
}
