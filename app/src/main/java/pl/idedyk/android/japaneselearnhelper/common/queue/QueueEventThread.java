package pl.idedyk.android.japaneselearnhelper.common.queue;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.common.queue.event.IQueryEvent;

public class QueueEventThread extends Thread {

    private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    //

    private boolean stop = false;

    private Object syncObject = new Object();

    //

    private SharedPreferences preferences;

    private List<IQueryEvent> queryEventList;

    //

    public QueueEventThread(Activity activity) {

        queryEventList = new ArrayList<>();

        preferences = activity.getSharedPreferences("queueEventThread", Context.MODE_PRIVATE);

        int fixme = 1;
        // !!!!!!!!! zaladowanie kolejki !!!!
    }

    public void requestStop() {
        this.stop = true;

        Log.d("JapaneseAndroidLearnHel", "requestStop");
    }

    @Override
    public void run() {

        while(true) {

            if (stop == true) {
                break;
            }

            int fixme = 1;
            // !!!!!! nie mozemy blokowac listy na czas przetwarzania !!!!!!!!!!

            synchronized (syncObject) {
                Log.d("JapaneseAndroidLearnHel", "Processing queue items");


            }



            try {
                Thread.sleep(1000);

            } catch (InterruptedException e) {
                // noop
            }
        }
    }

    //

    public void addEvent(IQueryEvent queryEvent) {

        synchronized (syncObject) {

            // dodajemy zdarzenie do listy
            queryEventList.add(queryEvent);

            // zapisujemy stan do pliku
            saveQueryEventList();
        }
    }

    private void saveQueryEventList() { // ta metoda powinna byc wywolywana, gdy jestesmy synchronizowani z uzyciem syncObject

        try {
            // pobranie danych
            JSONObject jsonQueue = getJSONQueue();

            JSONArray queueArray = getQueueArray(jsonQueue);

            for (IQueryEvent queryEvent : queryEventList) {

                // dodanie nowego wpisu
                JSONObject queryEntryObject = new JSONObject();

                queryEntryObject.put("uuid", queryEvent.getUUID());
                queryEntryObject.put("operation", queryEvent.getQueryEventOperation().toString());
                queryEntryObject.put("createDate", sdf.format(queryEvent.getCreateDate()));

                JSONObject queryEntryObjectParams = new JSONObject();

                //

                Map<String, String> queryEventParams = queryEvent.getParams();
                Iterator<String> queryEventParamsKeyIterator = queryEventParams.keySet().iterator();

                while (queryEventParamsKeyIterator.hasNext() == true) {

                    String key = queryEventParamsKeyIterator.next();
                    String value = queryEventParams.get(key);

                    //

                    queryEntryObjectParams.put(key, value);
                }

                queryEntryObject.put("params", queryEntryObjectParams);

                //


                queueArray.put(queryEntryObject);

                jsonQueue.put("queue", queueArray);
            }

            // zapisanie kolejki
            saveQueue(jsonQueue);

        } catch (Exception e) {
            // noop
        }
    }

    private JSONObject getJSONQueue() throws Exception {

        String queueJsonString = preferences.getString("queue", null);

        JSONObject queueJson = null;

        if (queueJsonString == null) {
            queueJson = new JSONObject();

        } else {
            queueJson = new JSONObject(queueJsonString);
        }

        return queueJson;
    }

    private JSONArray getQueueArray(JSONObject jsonQueue) throws Exception {

        JSONArray queueArray = null;

        if (jsonQueue.isNull("queue") == false) {
            queueArray = jsonQueue.getJSONArray("queue");

        } else {
            queueArray = new JSONArray();

        }

        return queueArray;
    }

    private void saveQueue(JSONObject jsonQueue) {

        SharedPreferences.Editor preferencesEditor = preferences.edit();

        preferencesEditor.putString("queue", jsonQueue.toString());

        preferencesEditor.commit();
    }
}
