package pl.idedyk.android.japaneselearnhelper.utils;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchHistoryHelper {

    private static final int MAX_ELEMENTS = 50;

    private SharedPreferences preferences;

    public SearchHistoryHelper(Activity activity, String searchField) {
        preferences = activity.getSharedPreferences(searchField, Context.MODE_PRIVATE);
    }

    public void addEntry(Entry newEntry) {

        // pobieramy aktualna zawartosc listy
        List<Entry> entryList = getEntryList();

        // szukamy, czy wpis, ktory chcemy dodac juz wystepuje na liscie
        Entry theSameEntryInEntryList = null;

        for (Entry currentEntry: entryList) {

            // mamy taki sam element
            if (currentEntry.text.equals(newEntry.text) == true) {
                theSameEntryInEntryList = currentEntry;

                break;
            }
        }

        // znalezlismy ten sam wpis, uaktualniamy go (w tej chwili to troche bez sensu, ale moze w przyszlosci to zmieni sie
        if (theSameEntryInEntryList != null) {
            theSameEntryInEntryList.text = newEntry.text;

            // usuwamy wpis z tej listy, za chwilke dodamy go na poczatku listy
            entryList.remove(theSameEntryInEntryList);

            entryList.add(0, theSameEntryInEntryList);

        } else {

            // dodajemy wpis na poczatku listy
            entryList.add(0, newEntry);
        }

        if (entryList.size() > MAX_ELEMENTS) { // usuwamy najstarszy wpis
            entryList.remove(entryList.size() - 1);
        }

        // zapisujemy liste
        try {
            saveEntryList(entryList);

        } catch (Exception e) {
            // noop
        }
    }

    public List<Entry> getEntryList() {

        List<Entry> result = new ArrayList<>();

        try {
            // pobranie danych
            JSONObject jsonObject = getJSONObject();

            JSONArray jsonArray = getJSSONArray(jsonObject);

            // utworzenie listy wynikowej
            for (int idx = 0; idx < jsonArray.length(); ++idx) {

                JSONObject jsonArrayEntryObject = jsonArray.getJSONObject(idx);

                String entryText = jsonArrayEntryObject.getString("text");

                result.add(new Entry(entryText));
            }

        } catch (Exception e) {
            // noop
        }

        return result;
    }

    private JSONObject getJSONObject() throws Exception {

        String jsonString = preferences.getString("history", null);

        JSONObject queueJson = null;

        if (jsonString == null) {
            queueJson = new JSONObject();

        } else {
            queueJson = new JSONObject(jsonString);
        }

        return queueJson;
    }

    private JSONArray getJSSONArray(JSONObject jsonObject) throws Exception {

        JSONArray jsonArray = null;

        if (jsonObject.isNull("array") == false) {
            jsonArray = jsonObject.getJSONArray("array");

        } else {
            jsonArray = new JSONArray();
        }

        return jsonArray;
    }

    private void saveEntryList(List<Entry> entryList) throws Exception {

        JSONObject jsonObject = getJSONObject();

        JSONArray jsonArray = new JSONArray();

        for (Entry entry : entryList) {

            JSONObject jsonEntryObject = new JSONObject();

            jsonEntryObject.put("text", entry.getText());

            jsonArray.put(jsonEntryObject);
        }

        jsonObject.put("array", jsonArray);

        // zapisujemy
        SharedPreferences.Editor preferencesEditor = preferences.edit();

        preferencesEditor.putString("history", jsonObject.toString());

        preferencesEditor.commit();
    }

    public static class Entry {

        private String text;

        public Entry(String text) {
            this.text = text;
        }

        public String getText() {
            return text;
        }

        @Override
        public boolean equals(Object o) {

            if (this == o) return true;

            if (o == null || getClass() != o.getClass()) return false;

            Entry entry = (Entry) o;

            return text != null ? text.equals(entry.text) : entry.text == null;
        }

        @Override
        public int hashCode() {
            return text != null ? text.hashCode() : 0;
        }
    }
}
