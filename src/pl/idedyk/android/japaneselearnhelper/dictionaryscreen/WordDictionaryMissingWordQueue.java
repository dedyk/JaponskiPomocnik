package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import org.json.JSONArray;
import org.json.JSONObject;

import pl.idedyk.japanese.dictionary.api.dictionary.dto.WordPlaceSearch;
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class WordDictionaryMissingWordQueue {
	
	private SharedPreferences preferences;

	public WordDictionaryMissingWordQueue(Activity activity) {
		
        // init config manager
        preferences = activity.getSharedPreferences("wordDictionaryMissingWordQueue", Context.MODE_PRIVATE);

	}
	
	public synchronized boolean addMissingWordToQueue(QueueEntry queueEntry) {
				
		try {	
			// pobranie danych
			JSONObject jsonQueue = getJSONQueue();
			
			JSONArray queueArray = getQueueArray(jsonQueue);
			
			// dodanie nowego wpisu
			JSONObject queryEntryObject = new JSONObject();
			
			queryEntryObject.put("word", queueEntry.getWord());
			queryEntryObject.put("wordPlaceSearch", queueEntry.getWordPlaceSearch().toString());
						
			queueArray.put(queryEntryObject);
						
			jsonQueue.put("queue", queueArray);
			
			// zapisanie kolejki
			saveQueue(jsonQueue);
			
			return true;
			
		} catch (Exception e) {						
			return false;
		}
	}
	
	public synchronized QueueEntry getNextQueueEntryFromQueue() {
		
		try {			
			// pobranie danych
			JSONObject jsonQueue = getJSONQueue();
			
			JSONArray queueArray = getQueueArray(jsonQueue);
			
			if (queueArray.length() > 0) {
				
				// pobranie pierwszego elementu
				JSONObject queueEntryJsonObject = queueArray.getJSONObject(0);
				
				return new QueueEntry(queueEntryJsonObject.getString("word"), WordPlaceSearch.valueOf(queueEntryJsonObject.getString("wordPlaceSearch")));				
			}
			
			return null;
			
		} catch (Exception e) {
			return null;
		}		
	}
	
	public synchronized boolean removeFirstQueueEntryFromQueue() {
		
		try {			
			// pobranie danych
			JSONObject jsonQueue = getJSONQueue();
			
			JSONArray queueArray = getQueueArray(jsonQueue);
			
			if (queueArray.length() > 0) {
								
				// utworz nowa  liste bez pierwszego elementu
				JSONArray newQueueArray = new JSONArray();
				
				for (int queueArrayIdx = 1; queueArrayIdx < queueArray.length(); ++queueArrayIdx) {
					
					JSONObject queueEntryJsonObject = queueArray.getJSONObject(queueArrayIdx);
					
					newQueueArray.put(queueEntryJsonObject);					
				}
				
				jsonQueue.put("queue", newQueueArray);
								
				// zapisanie kolejki
				saveQueue(jsonQueue);
			}		
			
			return true;
			
		} catch (Exception e) {
			return false;
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
		
		Editor preferencesEditor = preferences.edit();
		
		preferencesEditor.putString("queue", jsonQueue.toString());
		
		preferencesEditor.commit();
	}
	
	public static class QueueEntry {
		
		private String word;
		
		private WordPlaceSearch wordPlaceSearch;

		public QueueEntry(String word, WordPlaceSearch wordPlaceSearch) {
			this.word = word;
			this.wordPlaceSearch = wordPlaceSearch;
		}

		public String getWord() {
			return word;
		}

		public WordPlaceSearch getWordPlaceSearch() {
			return wordPlaceSearch;
		}		
	}
}
