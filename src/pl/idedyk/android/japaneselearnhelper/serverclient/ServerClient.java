package pl.idedyk.android.japaneselearnhelper.serverclient;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest.WordPlaceSearch;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ServerClient {
	
	private static final String SEND_MISSING_WORD_URL = "http://10.0.2.2:8080/android/sendMissingWord";
	//private static final String SEND_MISSING_WORD_URL = "http://japonski-pomocnik.idedyk.pl/android/sendMissingWord";

	public ServerClient() {
				
	}
	
	public void sendMissingWord(String word, WordPlaceSearch wordPlaceSearch) {
		
		boolean connected = isConnected();
		
		if (connected == false) {			
			return;
		}
		
		try {
			// parametry do polaczenia
			HttpParams httpParams = new BasicHttpParams();
			
			HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
			HttpConnectionParams.setSoTimeout(httpParams, 5000);
			
			// klient do http
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			
			HttpPost httpPost = new HttpPost(SEND_MISSING_WORD_URL);
			
			// przygotuj dane wejsciowe
			Map<String, Object> requestDataMap = new HashMap<String, Object>();
			
			requestDataMap.put("word", word);
			requestDataMap.put("wordPlaceSearch", wordPlaceSearch.toString());
			
			StringEntity stringEntity = new StringEntity(convertMapToJSONObject(requestDataMap).toString(), "UTF-8");
			
			httpPost.setEntity(stringEntity);
			
			// ustaw naglowki
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			
			// wywolaj serwer
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			// sprawdz odpowiedz
			StatusLine statusLine = httpResponse.getStatusLine();
			
			int statusCode = statusLine.getStatusCode();			
			
			if (statusCode < 200 || statusCode >= 300) {
				Log.e("ServerClient", "Error send missing word: " + statusLine.getStatusCode() + " - " + statusLine.getReasonPhrase());
			}			
			
		} catch (Exception e) {
			Log.e("ServerClient", "Error send missing word: ", e);
		}
	}
	
	private boolean isConnected() {
		
		ConnectivityManager cm = (ConnectivityManager)JapaneseAndroidLearnHelperApplication.getInstance().
				getSystemService(Context.CONNECTIVITY_SERVICE);
		
		NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
		
		if (activeNetwork == null) {
			return false;
		}
		
		return activeNetwork.isConnected();		
	}
	
	private static JSONObject convertMapToJSONObject(Map<String, Object> params) throws JSONException {

	    Iterator<Map.Entry<String, Object>> paramsEntrySetIterator = params.entrySet().iterator();

	    JSONObject result = new JSONObject();
	    
	    while (paramsEntrySetIterator.hasNext() == true) { 
	    	
	    	Map.Entry<String, Object> currentPair = (Map.Entry<String, Object>)paramsEntrySetIterator.next();

	        String key = (String)currentPair.getKey();	        
	        Object value = currentPair.getValue();
	        
	        result.put(key, value);
	    }
	    
	    return result;
	}	
}
