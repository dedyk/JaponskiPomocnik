package pl.idedyk.android.japaneselearnhelper.common.queue.event;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.UUID;

public abstract class QueueEventCommon implements IQueueEvent {

    //

    protected String uuid;

    protected Date createDate;

    protected QueueEventCommon() {
        this.uuid = UUID.randomUUID().toString();
        this.createDate = new Date();
    }

    protected QueueEventCommon(String uuid, Date createDate) {
        this.uuid = uuid;
        this.createDate = createDate;
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
    public String getCreateDateAsString() {

        if (createDate == null) {
            return null;
        }

        SimpleDateFormat sdf = new SimpleDateFormat(IQueueEvent.dateFormat);

        return sdf.format(createDate);
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null || obj instanceof QueueEventCommon == false) {
            return false;
        }

        QueueEventCommon objAsQueueEventCommon = (QueueEventCommon)obj;

        return uuid.equals(objAsQueueEventCommon.uuid);
    }

    @Override
    public String getParamsAsString() {

        Map<String, String> params = getParams();

        if (params == null) {
            return null;
        }

        JSONObject paramsJSONObject = new JSONObject();

        //

        Iterator<String> paramsKeyIterator = params.keySet().iterator();

        while (paramsKeyIterator.hasNext() == true) {

            String key = paramsKeyIterator.next();
            String value = params.get(key);

            //

            try {
                paramsJSONObject.put(key, value);

            } catch (JSONException e) {
                throw new RuntimeException((e));
            }
        }

        return paramsJSONObject.toString();
    }

    protected Map<String, String> getParamsFromString(String params) {

        Map<String, String> result = new HashMap<>();

        if (params == null) {
            return result;
        }

        JSONObject jsonObject = null;

        try {
            jsonObject = new JSONObject(params);

            Iterator<String> jsonObjectKeyIterator = jsonObject.keys();

            while (jsonObjectKeyIterator.hasNext() == true) {

                String key = jsonObjectKeyIterator.next();
                String value = jsonObject.getString(key);

                //

                result.put(key, value);
            }

        } catch (JSONException e) {
            return result;
        }

        return result;
    }
}
