package pl.idedyk.android.japaneselearnhelper.serverclient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.WordPlaceSearch;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dto.AttributeList;
import pl.idedyk.japanese.dictionary.api.dto.AttributeType;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntryType;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;

public class ServerClient {
	
	//private static final String PREFIX_URL = "http://10.0.2.2:8080"; // dev
	private static final String PREFIX_URL = "http://japonski-pomocnik.idedyk.pl"; // prod
		
	private static final String SEND_MISSING_WORD_URL = PREFIX_URL + "/android/sendMissingWord";
	private static final String SEARCH_URL = PREFIX_URL + "/android/search";

	private static final int TIMEOUT = 5000;
	
	public ServerClient() {
				
	}
	
	public boolean sendMissingWord(PackageInfo packageInfo, String word, WordPlaceSearch wordPlaceSearch) {
		
		boolean connected = isConnected();
		
		if (connected == false) {			
			return false;
		}
		
		try {
			// parametry do polaczenia
			HttpParams httpParams = new BasicHttpParams();
			
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
			
			// klient do http
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			
			HttpPost httpPost = new HttpPost(SEND_MISSING_WORD_URL);
			
			// ustaw naglowki
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("User-Agent", createUserAgent(packageInfo));
			
			// przygotuj dane wejsciowe
			Map<String, Object> requestDataMap = new HashMap<String, Object>();
			
			requestDataMap.put("word", word);
			requestDataMap.put("wordPlaceSearch", wordPlaceSearch.toString());
			
			StringEntity stringEntity = new StringEntity(convertMapToJSONObject(requestDataMap).toString(), "UTF-8");
			
			httpPost.setEntity(stringEntity);			
			
			// wywolaj serwer
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			// sprawdz odpowiedz
			StatusLine statusLine = httpResponse.getStatusLine();
			
			int statusCode = statusLine.getStatusCode();			
			
			if (statusCode < 200 || statusCode >= 300) {
				Log.e("ServerClient", "Error send missing word: " + statusLine.getStatusCode() + " - " + statusLine.getReasonPhrase());
				
				return false;
			}			
			
			return true;
			
		} catch (Exception e) {
			Log.e("ServerClient", "Error send missing word: ", e);
			
			return false;
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

		try {			
			// parametry do polaczenia
			HttpParams httpParams = new BasicHttpParams();
			
			HttpConnectionParams.setConnectionTimeout(httpParams, TIMEOUT);
			HttpConnectionParams.setSoTimeout(httpParams, TIMEOUT);
			
			// klient do http
			DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
			
			HttpPost httpPost = new HttpPost(SEARCH_URL);
			
			// ustaw naglowki
			httpPost.setHeader("Accept", "application/json");
			httpPost.setHeader("Content-type", "application/json");
			httpPost.setHeader("User-Agent", createUserAgent(packageInfo));
			
			// przygotuj dane wejsciowe
			Map<String, Object> requestDataMap = createMapFromFindWordRequest(findWordRequest);
						
			StringEntity stringEntity = new StringEntity(convertMapToJSONObject(requestDataMap).toString(), "UTF-8");
			
			httpPost.setEntity(stringEntity);			
			
			// wywolaj serwer
			HttpResponse httpResponse = httpClient.execute(httpPost);
			
			// sprawdz odpowiedz
			StatusLine statusLine = httpResponse.getStatusLine();
			
			int statusCode = statusLine.getStatusCode();			
			
			if (statusCode != 200) {
				Log.e("ServerClient", "Error search: " + statusLine.getStatusCode() + " - " + statusLine.getReasonPhrase());
				
				return null;
			}
			
			HttpEntity entity = httpResponse.getEntity();
			
			if (entity == null) {
				return null;
			}
			
			InputStream contentInputStream = entity.getContent();
			
			BufferedReader contentInputStreamReader = new BufferedReader(new InputStreamReader(contentInputStream));
			
			String readLine = null;
			
			StringBuffer jsonResponseSb = new StringBuffer();
			
			while (true) {		
				readLine = contentInputStreamReader.readLine();
				
				if (readLine == null) {
					break;
				}
				
				jsonResponseSb.append(readLine);			
			}

			contentInputStreamReader.close();
			
			JSONObject responseJSON = new JSONObject(jsonResponseSb.toString());
			
			return createFindWordResultFromJSON(responseJSON);
			
		} catch (Exception e) {
			Log.e("ServerClient", "Error search: ", e);
			
			return null;
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
	
	private FindWordResult createFindWordResultFromJSON(JSONObject responseJSON) throws JSONException {
		
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
						
			findWordResult.result.add(new FindWordResult.ResultItem(dictionaryEntry));
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
}
