package pl.idedyk.android.japaneselearnhelper.serverclient;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.japanese.dictionary.api.android.queue.event.IQueueEvent;
import pl.idedyk.japanese.dictionary.api.android.queue.event.QueueEventWrapper;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.WordPlaceSearch;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dto.AttributeList;
import pl.idedyk.japanese.dictionary.api.dto.AttributeType;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntryType;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

import com.google.gson.Gson;

public class ServerClient {

	// FM_FIXME: sprawdzic zdalna baze danych

	// trzeba ustawic android:usesCleartextTraffic="true" w AndroidManifest.xml
	// FM_FIXME: lokalny adres
	private static final String PREFIX_URL = "http://10.0.2.2:8080"; // dev

	// stary Android ponizej wersji 8 nie zna certyfikatu Let's Encrypt, wiec musi byc uzyte zwykle polaczenie
	private static final String PREFIX_PROTOCOL = android.os.Build.VERSION.SDK_INT >= 26 ? "https" : "http";

	// FM_FIXME: produkcyjny adres
	// private static final String PREFIX_URL = PREFIX_PROTOCOL + "://" + "www.japonski-pomocnik.pl"; // prod

	private static final String SEND_MISSING_WORD_URL = PREFIX_URL + "/android/sendMissingWord";
	private static final String SEARCH_URL = PREFIX_URL + "/android/search";
	private static final String GET_MESSAGE_URL = PREFIX_URL + "/android/getMessage";

	private static final String AUTOCOMPLETE_URL = PREFIX_URL + "/android/autocomplete";
	
	private static final String SPELL_CHECKER_SUGGESTION_URL = PREFIX_URL + "/android/spellCheckerSuggestion";

	private static final String SEND_QUEUE_EVENT_URL = PREFIX_URL + "/android/receiveQueueEvent";

	private static final String REMOTE_DATABASE_CONNECTOR_BASE_URL = PREFIX_URL + "/android/remoteDatabaseConnector/";

	private static final int TIMEOUT = 10000;
	
	public ServerClient() {
				
	}
	
	public boolean sendMissingWord(PackageInfo packageInfo, String word, WordPlaceSearch wordPlaceSearch) {
		
		boolean connected = isConnected();
		
		if (connected == false) {			
			return false;
		}

		HttpURLConnection httpURLConnection = null;
		
		try {
			// przygotuj dane wejsciowe
			Map<String, Object> requestDataMap = new HashMap<String, Object>();

			requestDataMap.put("word", word);
			requestDataMap.put("wordPlaceSearch", wordPlaceSearch.toString());

			httpURLConnection = callRemoteService(packageInfo, SEND_MISSING_WORD_URL, convertMapToJSONObject(requestDataMap).toString());

			// sprawdz odpowiedz
			int statusCode = httpURLConnection.getResponseCode();

			if (statusCode < 200 || statusCode >= 300) {

				Log.e("ServerClient", "Error send missing word: " + httpURLConnection.getResponseCode() + " - " + httpURLConnection.getResponseMessage());

				return false;
			}

			return true;

		} catch (Exception e) {
			Log.e("ServerClient", "Error send missing word: ", e);
			
			return false;

		} finally {

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
	}
	
	private String createUserAgent(PackageInfo packageInfo) {
		
		StringBuffer sb = new StringBuffer("JapaneseAndroidLearnHelper");
		
		if (packageInfo != null) {			
			sb.append("/" + packageInfo.versionCode + "/" + packageInfo.versionName);
		}
				
		return sb.toString();
	}

	public FindWordResult search(PackageInfo packageInfo, FindWordRequest findWordRequest) {
		
		boolean connected = isConnected();
		
		if (connected == false) {			
			return null;
		}

		HttpURLConnection httpURLConnection = null;

		BufferedReader contentInputStreamReader = null;

		try {
			// przygotuj dane wejsciowe
			Map<String, Object> requestDataMap = createMapFromFindWordRequest(findWordRequest);

			httpURLConnection = callRemoteService(packageInfo, SEARCH_URL, convertMapToJSONObject(requestDataMap).toString());

			// sprawdz odpowiedz
			int statusCode = httpURLConnection.getResponseCode();

			if (statusCode < 200 || statusCode >= 300) {

				Log.e("ServerClient", "Error search: " + httpURLConnection.getResponseCode() + " - " + httpURLConnection.getResponseMessage());
				
				return null;
			}

			// pobierz odpowiedz
			contentInputStreamReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

			StringBuffer jsonResponseSb = new StringBuffer();
			
			while (true) {		
				String readLine = contentInputStreamReader.readLine();
				
				if (readLine == null) {
					break;
				}
				
				jsonResponseSb.append(readLine);			
			}

			if (jsonResponseSb.length() == 0) {
				return null;
			}

			Gson gson = new Gson();

			return gson.fromJson(jsonResponseSb.toString(), FindWordResult.class);

			/*
			JSONObject responseJSON = new JSONObject(jsonResponseSb.toString());
			
			return createFindWordResultFromJSON(responseJSON);
			 */
			
		} catch (Exception e) {
			Log.e("ServerClient", "Error search: ", e);
			
			return null;

		} finally {

			if (contentInputStreamReader != null) {

				try {
					contentInputStreamReader.close();

				} catch (IOException e) {
					// noop
				}
			}

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
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
	        
	        if (value instanceof List<?>) {
	        	
	        	JSONArray jsonArray = new JSONArray();
	        	
	        	List<?> valueList =  (List<?>)value;
	        	
	        	for (Object currentValue : valueList) {					
	        		jsonArray.put(currentValue.toString());	        		
	        	}
	        	
	        	result.put(key, jsonArray);
	        	
	        } else {
	        	result.put(key, value);
	        }	        
	    }
	    
	    return result;
	}
	
	private Map<String, Object> createMapFromFindWordRequest(FindWordRequest findWordRequest) {
				
		Map<String, Object> requestDataMap =  new HashMap<String, Object>();
		
		requestDataMap.put("searchMainDictionary", findWordRequest.searchMainDictionary);
		requestDataMap.put("searchGrammaFormAndExamples", findWordRequest.searchGrammaFormAndExamples);
		requestDataMap.put("searchName", findWordRequest.searchName);
		
		requestDataMap.put("word", findWordRequest.word);
		
		requestDataMap.put("searchKanji", findWordRequest.searchKanji);
		requestDataMap.put("searchKana", findWordRequest.searchKana);
		requestDataMap.put("searchRomaji", findWordRequest.searchRomaji);
		requestDataMap.put("searchTranslate", findWordRequest.searchTranslate);
		requestDataMap.put("searchInfo", findWordRequest.searchInfo);
		requestDataMap.put("searchOnlyCommonWord", findWordRequest.searchOnlyCommonWord);
		
		requestDataMap.put("wordPlaceSearch", findWordRequest.wordPlaceSearch);
		
		requestDataMap.put("dictionaryEntryTypeList", findWordRequest.dictionaryEntryTypeList);
				
		return requestDataMap;
	}

	/*
	private FindWordResult createFindWordResultFromJSON(JSONObject responseJSON) throws JSONException {

		// FM_FIXME: do poprawy

		FindWordResult findWordResult = new FindWordResult();
		
		findWordResult.moreElemetsExists = responseJSON.getBoolean("moreElemetsExists");
		findWordResult.foundGrammaAndExamples = responseJSON.getBoolean("foundGrammaAndExamples");
		findWordResult.foundNames = responseJSON.getBoolean("foundNames");
		
		findWordResult.result = new ArrayList<FindWordResult.ResultItem>();
		
		JSONArray resultJsonArray = responseJSON.getJSONArray("result");
		
		for (int resultIdx = 0; resultIdx < resultJsonArray.length(); ++resultIdx) {
			
			JSONObject currentJsonObjectResult = resultJsonArray.getJSONObject(resultIdx);
						
			DictionaryEntry dictionaryEntry = new DictionaryEntry();
			
			// id
			int id = currentJsonObjectResult.getInt("id");
			
			dictionaryEntry.setId(id);
			
			// dictionaryEntryTypeList
			JSONArray dictionaryEntryTypeListJsonArray = currentJsonObjectResult.getJSONArray("dictionaryEntryTypeList");
			
			List<DictionaryEntryType> dictionaryEntryTypeList = new ArrayList<DictionaryEntryType>();
			
			for (int dictionaryEntryTypeListJsonArrayIdx = 0; 
					dictionaryEntryTypeListJsonArrayIdx < dictionaryEntryTypeListJsonArray.length(); ++dictionaryEntryTypeListJsonArrayIdx) {
				
				dictionaryEntryTypeList.add(DictionaryEntryType.getDictionaryEntryType(
						dictionaryEntryTypeListJsonArray.getString(dictionaryEntryTypeListJsonArrayIdx)));
			}
			
			dictionaryEntry.setDictionaryEntryTypeList(dictionaryEntryTypeList);
			
			// attributeList
			AttributeList attributeList = new AttributeList();
			
			if (currentJsonObjectResult.has("attributeList") == true) {
				
				JSONArray attributeListJsonArray = currentJsonObjectResult.getJSONArray("attributeList");
				
				for (int attributeListJsonArrayIdx = 0; attributeListJsonArrayIdx < attributeListJsonArray.length();
						attributeListJsonArrayIdx++) {
					
					JSONObject currentAttributeJsonObject = attributeListJsonArray.getJSONObject(attributeListJsonArrayIdx);
					
					AttributeType attributeType = AttributeType.valueOf(currentAttributeJsonObject.getString("attributeType"));					
					List<String> attributeValueList = convertJSONArrayToListString(currentAttributeJsonObject.getJSONArray("attributeValue"));
					
					attributeList.addAttributeValue(attributeType, attributeValueList);
				}				
			}
			
			dictionaryEntry.setAttributeList(attributeList);
			
			// groups
			JSONArray groupsEnumListJsonArray = currentJsonObjectResult.getJSONArray("groups");
			
			List<GroupEnum> groupsEnumList = new ArrayList<GroupEnum>();
			
			for (int groupsEnumListJsonArrayIdx = 0; 
					groupsEnumListJsonArrayIdx < groupsEnumListJsonArray.length(); ++groupsEnumListJsonArrayIdx) {
				
				groupsEnumList.add(GroupEnum.valueOf(groupsEnumListJsonArray.getString(groupsEnumListJsonArrayIdx)));
			}
			
			dictionaryEntry.setGroups(groupsEnumList);

			// prefixKana
			if (currentJsonObjectResult.has("prefixKana") == true) {
				dictionaryEntry.setPrefixKana(currentJsonObjectResult.getString("prefixKana"));
			}
			
			// kanji
			if (currentJsonObjectResult.has("kanji") == true) {
				dictionaryEntry.setKanji(currentJsonObjectResult.getString("kanji"));
			}
			
			// kanaList
			dictionaryEntry.setKana(currentJsonObjectResult.getString("kana"));
			
			// prefixRomaji
			if (currentJsonObjectResult.has("prefixRomaji") == true) {
				dictionaryEntry.setPrefixRomaji(currentJsonObjectResult.getString("prefixRomaji"));
			}
			
			// romajiList
			dictionaryEntry.setRomaji(currentJsonObjectResult.getString("romaji"));
			
			// translates
			dictionaryEntry.setTranslates(convertJSONArrayToListString(currentJsonObjectResult.getJSONArray("translates")));
			
			// info
			if (currentJsonObjectResult.has("info") == true) {
				dictionaryEntry.setInfo(currentJsonObjectResult.getString("info"));
			}
			
			// name
			dictionaryEntry.setName(currentJsonObjectResult.getBoolean("name"));

			// FM_FIXME: do poprawy
			findWordResult.result.add(new FindWordResult.ResultItem(dictionaryEntry, dictionaryEntry.isName(), false));
		}
		
		return findWordResult;
	}
	
	private List<String> convertJSONArrayToListString(JSONArray jsonArray) throws JSONException {
		
		List<String> result = new ArrayList<String>();
		
		for (int idx = 0; idx < jsonArray.length(); ++idx) {			
			result.add(jsonArray.getString(idx));			
		}
		
		return result;
	}
	 */
	
	public List<String> getAutoComplete(PackageInfo packageInfo, String word, AutoCompleteSuggestionType autoCompleteSuggestionType) {
		
		boolean connected = isConnected();
		
		if (connected == false) {			
			return new ArrayList<>();
		}
		
		if (word == null || word.length() < 2) {
			return new ArrayList<>();
		}

		HttpURLConnection httpURLConnection = null;

		BufferedReader contentInputStreamReader = null;
		
		try {
			// przygotuj dane wejsciowe
			Map<String, Object> requestDataMap = new HashMap<String, Object>();

			requestDataMap.put("word", word);
			requestDataMap.put("type", autoCompleteSuggestionType.getType());

			httpURLConnection = callRemoteService(packageInfo, AUTOCOMPLETE_URL, convertMapToJSONObject(requestDataMap).toString());

			// sprawdz odpowiedz
			int statusCode = httpURLConnection.getResponseCode();

			if (statusCode < 200 || statusCode >= 300) {

				Log.e("ServerClient", "Error send missing word: " + httpURLConnection.getResponseCode() + " - " + httpURLConnection.getResponseMessage());

				return new ArrayList<>();
			}
			
			// pobranie odpowiedzi
			contentInputStreamReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

			StringBuffer jsonResponseSb = new StringBuffer();
			
			while (true) {		
				String readLine = contentInputStreamReader.readLine();
				
				if (readLine == null) {
					break;
				}
				
				jsonResponseSb.append(readLine);			
			}

			if (jsonResponseSb.length() == 0) {
				return null;
			}

			JSONArray responseJSON = new JSONArray(jsonResponseSb.toString());

			List<String> result = new ArrayList<String>();
			
			for (int idx = 0; idx < responseJSON.length(); ++idx) {
				
				JSONObject jsonObject = responseJSON.getJSONObject(idx);
				
				result.add(jsonObject.getString("value"));
			}			
			
			return result;
			
		} catch (Exception e) {
			Log.e("ServerClient", "Error send missing word: ", e);
			
			return null;

		} finally {

			if (contentInputStreamReader != null) {

				try {
					contentInputStreamReader.close();

				} catch (IOException e) {
					// noop
				}
			}

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
	}
	
	public List<String> getSuggestionList(PackageInfo packageInfo, String word, AutoCompleteSuggestionType autoCompleteSuggestionType) {
		
		boolean connected = isConnected();
		
		if (connected == false) {			
			return null;
		}
		
		if (word == null || word.length() < 2) {
			return null;
		}

		HttpURLConnection httpURLConnection = null;

		BufferedReader contentInputStreamReader = null;
		
		try {
			// przygotuj dane wejsciowe
			Map<String, Object> requestDataMap = new HashMap<String, Object>();

			requestDataMap.put("word", word);
			requestDataMap.put("type", autoCompleteSuggestionType.getType());

			httpURLConnection = callRemoteService(packageInfo, SPELL_CHECKER_SUGGESTION_URL, convertMapToJSONObject(requestDataMap).toString());

			// sprawdz odpowiedz
			int statusCode = httpURLConnection.getResponseCode();

			if (statusCode < 200 || statusCode >= 300) {
				Log.e("ServerClient", "Error send missing word: " + httpURLConnection.getResponseCode() + " - " + httpURLConnection.getResponseMessage());
				
				return null;
			}
			
			// pobranie odpowiedzi
			contentInputStreamReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

			StringBuffer jsonResponseSb = new StringBuffer();
			
			while (true) {		
				String readLine = contentInputStreamReader.readLine();
				
				if (readLine == null) {
					break;
				}
				
				jsonResponseSb.append(readLine);			
			}

			if (jsonResponseSb.length() == 0) {
				return null;
			}

			JSONArray responseJSON = new JSONArray(jsonResponseSb.toString());

			List<String> result = new ArrayList<String>();
			
			for (int idx = 0; idx < responseJSON.length(); ++idx) {
				
				JSONObject jsonObject = responseJSON.getJSONObject(idx);
				
				result.add(jsonObject.getString("value"));
			}			
			
			return result;
			
		} catch (Exception e) {
			Log.e("ServerClient", "Error send missing word: ", e);
			
			return null;

		} finally {

			if (contentInputStreamReader != null) {

				try {
					contentInputStreamReader.close();

				} catch (IOException e) {
					// noop
				}
			}

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
	}

    public String callRemoteDictionaryConnectorMethod(PackageInfo packageInfo, String methodName, String jsonRequest) throws Exception {

        boolean connected = isConnected();

        if (connected == false) {
            throw new IOException("Not connected");
        }

		HttpURLConnection httpURLConnection = null;

		BufferedReader contentInputStreamReader = null;

        try {
			httpURLConnection = callRemoteService(packageInfo, REMOTE_DATABASE_CONNECTOR_BASE_URL + methodName, jsonRequest);

			// sprawdz odpowiedz
			int statusCode = httpURLConnection.getResponseCode();

			if (statusCode < 200 || statusCode >= 300) {
                Log.e("ServerClient", "Error callRemoteDictionaryConnectorMethod: " + httpURLConnection.getResponseCode() + " - " + httpURLConnection.getResponseMessage());

                throw new IOException(httpURLConnection.getResponseCode() + " - " + httpURLConnection.getResponseMessage());
            }

			// pobranie odpowiedzi
            contentInputStreamReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

            StringBuffer jsonResponseSb = new StringBuffer();

            while (true) {
                String readLine = contentInputStreamReader.readLine();

                if (readLine == null) {
                    break;
                }

                jsonResponseSb.append(readLine);
            }

			if (jsonResponseSb.length() == 0) {
				throw new IOException();
			}

			return jsonResponseSb.toString();

        } catch (Exception e) {
            Log.e("ServerClient", "Error callRemoteDictionaryConnectorMethod: ", e);

            throw e;

		} finally {

			if (contentInputStreamReader != null) {

				try {
					contentInputStreamReader.close();

				} catch (IOException e) {
					// noop
				}
			}

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
    }

	public static <T> T callInServerThread(Callable<Object> callable, Class<T> resultClass) throws DictionaryException {

		ExecutorService executorService = null;

		try {
			executorService = Executors.newFixedThreadPool(1);

			Future<Object> resultFuture = executorService.submit(callable);

			Object resultObject = null;

			try {
				resultObject = resultFuture.get();

			} catch (Exception e) {
				throw new DictionaryException(e);
			}

			if (resultObject == null) {
				return null;

			} else if (resultObject instanceof DictionaryException) {
				throw (DictionaryException) resultObject;

			} else if (resultObject instanceof Exception) {
				throw new DictionaryException((Exception) resultObject);

			} else if (resultClass.isInstance(resultObject) == true) {
				return (T) resultObject;

			} else {
				throw new RuntimeException("Unknown object: " + resultObject);
			}

		} finally {

			if (executorService != null) {
				executorService.shutdown();
			}
		}
	}

	private HttpURLConnection callRemoteService(PackageInfo packageInfo, String urlString, String requestPostString) throws Exception {

		// url
		URL url = new URL(urlString);

		// tworzymy polaczenie
		HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();

		httpURLConnection.setRequestMethod("POST");

		// parametry do polaczenia
		httpURLConnection.setUseCaches(false);
		httpURLConnection.setAllowUserInteraction(false);
		httpURLConnection.setConnectTimeout(TIMEOUT);
		httpURLConnection.setReadTimeout(TIMEOUT);

		httpURLConnection.setDoInput(true);
		httpURLConnection.setDoOutput(true);

		// ustaw naglowki
		httpURLConnection.setRequestProperty("Accept", "application/json");
		httpURLConnection.setRequestProperty("Content-type", "application/json");
		httpURLConnection.setRequestProperty("User-Agent", createUserAgent(packageInfo));

		httpURLConnection.setRequestProperty("Content-Type", "application/json; utf-8");

		//

		// wywolaj serwer
		httpURLConnection.connect();

		// dane wejsciowe
		BufferedOutputStream bufferedOutputStream = null;

		try {
			bufferedOutputStream = new BufferedOutputStream(httpURLConnection.getOutputStream());

			bufferedOutputStream.write(requestPostString.getBytes("UTF-8"));

			bufferedOutputStream.flush();

		} catch(IOException exception) {
			throw exception;

		} finally {

			if (bufferedOutputStream != null) {

				try {
					bufferedOutputStream.close();

				} catch (IOException e) {
					// noop
				}
			}
		}

		return httpURLConnection;
	}

	public boolean sendQueueEvent(PackageInfo packageInfo, IQueueEvent queueEvent) {

		boolean connected = isConnected();

		if (connected == false) {
			return false;
		}

		HttpURLConnection httpURLConnection = null;

		BufferedReader contentInputStreamReader = null;

		try {
			String jsonRequest = new Gson().toJson(new QueueEventWrapper(queueEvent.getId(), queueEvent.getUserId(), queueEvent.getQueryEventOperation(), queueEvent.getCreateDateAsString(), queueEvent.getParams()));

			httpURLConnection = callRemoteService(packageInfo, SEND_QUEUE_EVENT_URL, jsonRequest);

			// sprawdz odpowiedz
			int statusCode = httpURLConnection.getResponseCode();

			if (statusCode < 200 || statusCode >= 300) {
				Log.e("ServerClient", "Error sendQueueEvent: " + httpURLConnection.getResponseCode() + " - " + httpURLConnection.getResponseMessage());

				return false;
			}

			return true;

		} catch (Exception e) {
			Log.e("ServerClient", "Error sendQueueEvent: ", e);

			return false;

		} finally {

			if (contentInputStreamReader != null) {

				try {
					contentInputStreamReader.close();

				} catch (IOException e) {
					// noop
				}
			}

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
	}

	public static class GetMessageResult {

		public String message;

		public String timestamp;
	}

	public GetMessageResult getMessage(PackageInfo packageInfo) {

		boolean connected = isConnected();

		if (connected == false) {
			return null;
		}

		HttpURLConnection httpURLConnection = null;

		BufferedReader contentInputStreamReader = null;

		try {
			httpURLConnection = callRemoteService(packageInfo, GET_MESSAGE_URL, new JSONObject().toString());

			// sprawdz odpowiedz
			int statusCode = httpURLConnection.getResponseCode();

			if (statusCode < 200 || statusCode >= 300) {

				Log.e("ServerClient", "Error search: " + httpURLConnection.getResponseCode() + " - " + httpURLConnection.getResponseMessage());

				return null;
			}

			// pobierz odpowiedz
			contentInputStreamReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

			StringBuffer jsonResponseSb = new StringBuffer();

			while (true) {
				String readLine = contentInputStreamReader.readLine();

				if (readLine == null) {
					break;
				}

				jsonResponseSb.append(readLine);
			}

			if (jsonResponseSb.length() == 0) {
				return null;
			}

			JSONObject responseJSON = new JSONObject(jsonResponseSb.toString());

			String message = responseJSON.optString("message");
			String timestamp = responseJSON.optString("timestamp");

			// mamy jakis komunikat
			if (message != null && message.trim().equals("") == false && timestamp != null && timestamp.trim().equals("") == false) {

				GetMessageResult getMessageResult = new GetMessageResult();

				getMessageResult.message = message.trim();
				getMessageResult.timestamp = timestamp.trim();

				return getMessageResult;

			} else {
				return null;
			}

		} catch (Exception e) {
			Log.e("ServerClient", "Error search: ", e);

			return null;

		} finally {

			if (contentInputStreamReader != null) {

				try {
					contentInputStreamReader.close();

				} catch (IOException e) {
					// noop
				}
			}

			if (httpURLConnection != null) {
				httpURLConnection.disconnect();
			}
		}
	}

	public static enum AutoCompleteSuggestionType {
		
		WORD_DICTIONARY("wordDictionaryEntry"),
		
		KANJI_DICTIONARY("kanjiDictionaryEntry");
		
		private String type;
		
		AutoCompleteSuggestionType(String type) {
			this.type = type;
		}

		public String getType() {
			return type;
		}
	}
}
