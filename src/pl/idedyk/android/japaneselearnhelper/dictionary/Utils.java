package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.AttributeList;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.GroupEnum;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;

public class Utils {
	
	public static int MAX_LIST_SIZE = 8;
	
	public static List<String> parseStringIntoList(String text, boolean limitSize) {
		
		if (text == null) {
			return null;
		}
		
		List<String> result = new ArrayList<String>();
		
		String[] splitedText = text.split("\n");
		
		for (String currentSplitedText : splitedText) {			
			result.add(currentSplitedText);
		}
		
		if (limitSize == true && result.size() > MAX_LIST_SIZE) {
			throw new RuntimeException("parseStringIntoList max list size: " + text);
		}
		
		return result;		
	}
	
	public static String convertListToString(List<String> list) {
		StringBuffer sb = new StringBuffer();
		
		for (int idx = 0; idx < list.size(); ++idx) {
			sb.append(list.get(idx));
			
			if (idx != list.size() - 1) {
				sb.append("\n");
			}
		}
		
		return sb.toString();
	}
	
	public static DictionaryEntry parseDictionaryEntry(
			String idString,
			String dictionaryEntryTypeString,
			Object attributesObject,
			Object groupsObject,
			String prefixKanaString,
			String kanjiString,
			Object kanaListObject,
			String prefixRomajiString,
			Object romajiListObject,
			Object translateListObject,
			String infoString) throws DictionaryException {
				
		if (kanjiString.equals("") == true || kanjiString.equals("-") == true) {
			kanjiString = null;
		}
		
		if (prefixRomajiString.equals("") == true || prefixRomajiString.equals("-") == true) {
			prefixRomajiString = null;
		}
						
		DictionaryEntryType dictionaryEntryType = DictionaryEntryType.valueOf(dictionaryEntryTypeString);	
		
		DictionaryEntry entry = new DictionaryEntry();
		
		entry.setId(Integer.parseInt(idString));
		entry.setDictionaryEntryType(dictionaryEntryType);

		if (attributesObject instanceof String) {
			entry.setAttributeList(AttributeList.parseAttributesStringList(parseStringIntoList((String)attributesObject, false)));
		} else {
			entry.setAttributeList(AttributeList.parseAttributesStringList((convertToListString(attributesObject))));
		}
		
		if (groupsObject instanceof String) {
			entry.setGroups(GroupEnum.sortGroups(GroupEnum.convertToListGroupEnum(parseStringIntoList((String)groupsObject, false))));
		} else {
			entry.setGroups(GroupEnum.sortGroups(GroupEnum.convertToListGroupEnum(convertToListString(groupsObject))));
		}		
		
		entry.setPrefixKana(prefixKanaString);
		entry.setKanji(kanjiString);
		entry.setPrefixRomaji(prefixRomajiString);
		
		if (romajiListObject instanceof String) {
			entry.setRomajiList(parseStringIntoList((String)romajiListObject, true));
		} else {
			entry.setRomajiList(convertToListString(romajiListObject));
		}
				
		if (kanaListObject instanceof String) {
			entry.setKanaList(parseStringIntoList((String)kanaListObject, true));
		} else {
			entry.setKanaList(convertToListString(kanaListObject));			
		}
		
		if (translateListObject instanceof String) {
			entry.setTranslates(parseStringIntoList((String)translateListObject, true));
		} else {
			entry.setTranslates(convertToListString(translateListObject));
		}
		
		entry.setInfo(infoString);
		
		return entry;
	}
	
	private static List<String> convertToListString(Object object) {
		
		if (object == null) {
			return new ArrayList<String>();
		}
		
		List<?> listObject = (List<?>)object; 
		
		List<String> result = new ArrayList<String>();
		
		for (Object currentListObject : listObject) {
			result.add((String)currentListObject);
		}

		return result;
	}
	
	public static KanjiEntry parseKanjiEntry(String idString,
			String kanjiString,
			String strokeCountString,
			List<String> radicalsList,
			List<String> onReadingList,
			List<String> kunReadingList,
			String strokePathString,
			List<String> polishTranslateList,
			String infoString,
			String generatedString,
			List<String> groupsList) throws DictionaryException {
		
		int id = Integer.parseInt(idString);
		
		if (kanjiString.equals("") == true) {
			throw new DictionaryException("Empty kanji: " + idString);
		}
		
		KanjiDic2Entry kanjiDic2Entry = null;
		
		if (strokeCountString != null && strokeCountString.equals("") == false) {
			
			kanjiDic2Entry = new KanjiDic2Entry();
			
			int strokeCount = Integer.parseInt(strokeCountString);
						
			kanjiDic2Entry.setKanji(kanjiString);
			kanjiDic2Entry.setStrokeCount(strokeCount);
			kanjiDic2Entry.setRadicals(radicalsList);
			kanjiDic2Entry.setKunReading(kunReadingList);
			kanjiDic2Entry.setOnReading(onReadingList);
		}
				
		KanjiEntry entry = new KanjiEntry();
		
		entry.setId(id);
		entry.setKanji(kanjiString);
		entry.setStrokePaths(parseStringIntoList(strokePathString, false));
		entry.setPolishTranslates(polishTranslateList);
		entry.setInfo(infoString);
		
		entry.setGenerated(Boolean.parseBoolean(generatedString));
		
		entry.setGroups(GroupEnum.convertToListGroupEnum(groupsList));
					
		entry.setKanjiDic2Entry(kanjiDic2Entry);
		
		return entry;
	}
	
	public static String removePolishChars(String text) {
		
		text = text.replace('Ę', 'E');
		text = text.replace('ę', 'e');
		
		text = text.replace('Ó', 'O');
		text = text.replace('ó', 'o');
		
		text = text.replace('Ą', 'A');
		text = text.replace('ą', 'a');
		
		text = text.replace('Ś', 'S');
		text = text.replace('ś', 's');
		
		text = text.replace('Ł', 'L');
		text = text.replace('ł', 'l');
		
		text = text.replace('Ż', 'Z');
		text = text.replace('ż', 'z');
		
		text = text.replace('Ź', 'z');
		text = text.replace('ź', 'z');
		
		text = text.replace('Ć', 'C');
		text = text.replace('ć', 'c');
		
		text = text.replace('Ń', 'N');
		text = text.replace('ń', 'n');
		
		return text;
	}
	
	public static boolean containsPolishChars(String text) {
		
		Pattern pattern = Pattern.compile("(Ę|ę|Ó|ó|Ą|ą|Ś|ś|Ł|ł|Ż|ż|Ź|ź|Ć|ć|Ń|ń)");
		
		return pattern.matcher(text).find();
	}
}
