package pl.idedyk.android.japaneselearnhelper.common.queue.event;

import java.util.HashMap;
import java.util.Map;

public class StatLogEventEvent extends QueryEventCommon {

    private String screenName;
    private String actionName;
    private String label;

    public StatLogEventEvent(String screenName, String actionName, String label) {

        super();

        this.screenName = screenName;
        this.actionName = actionName;
        this.label = label;
    }

    @Override
    public QueryEventOperation getQueryEventOperation() {
        return QueryEventOperation.STAT_LOG_EVENT_EVENT;
    }

    @Override
    public Map<String, String> getParams() {

        Map<String, String> result = new HashMap<>();

        result.put("screenName", screenName);
        result.put("actionName", actionName);
        result.put("label", label);

        return result;
    }
}