package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;

public class Utils {
	
	private static int MAX_LIST_SIZE = 5;
	
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
			throw new RuntimeException("parseStringIntoList max list size");
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
			String prefixKanaString,
			String kanjiString,
			String kanaListString,
			String prefixRomajiString,
			String romajiListString,
			String translateListString,
			String infoString) throws DictionaryException {
				
		if (kanjiString.equals("") == true || kanjiString.equals("-") == true) {
			kanjiString = null;
		}
		
		if (prefixRomajiString.equals("") == true || prefixRomajiString.equals("-") == true) {
			prefixRomajiString = null;
		}
				
		DictionaryEntryType dictionaryEntryType = DictionaryEntryType.valueOf(dictionaryEntryTypeString);
		
		List<String> romajiList = parseStringIntoList(romajiListString, true);
		List<String> kanaList = parseStringIntoList(kanaListString, true);
		
		if (romajiList.size() != kanaList.size()) {
			throw new DictionaryException("Parse parseStringIntoList size exception");
		}
		
		DictionaryEntry entry = new DictionaryEntry();
		
		entry.setId(Integer.parseInt(idString));
		entry.setDictionaryEntryType(dictionaryEntryType);
		entry.setPrefixKana(prefixKanaString);
		entry.setKanji(kanjiString);
		entry.setPrefixRomaji(prefixRomajiString);
		entry.setRomajiList(romajiList);
		entry.setKanaList(kanaList);
		entry.setTranslates(parseStringIntoList(translateListString, true));
		
		entry.setInfo(infoString);
		
		return entry;
	}
	
	public static KanjiEntry parseKanjiEntry(String idString,
			String kanjiString,
			String strokeCountString,
			String radicalsString,
			String onReadingString,
			String kunReadingString,
			String strokePathString,
			String polishTranslateListString,
			String infoString,
			String generatedString,
			String groupString) throws DictionaryException {
		
		int id = Integer.parseInt(idString);
		
		if (kanjiString.equals("") == true) {
			throw new DictionaryException("Empty kanji!");
		}
		
		KanjiDic2Entry kanjiDic2Entry = null;
		
		if (strokeCountString != null && strokeCountString.equals("") == false) {
			
			kanjiDic2Entry = new KanjiDic2Entry();
			
			int strokeCount = Integer.parseInt(strokeCountString);
			
			List<String> radicals = parseStringIntoList(radicalsString, false);
		
			List<String> onReading = parseStringIntoList(onReadingString, false);
		
			List<String> kunReading = parseStringIntoList(kunReadingString, false);
			
			kanjiDic2Entry.setKanji(kanjiString);
			kanjiDic2Entry.setStrokeCount(strokeCount);
			kanjiDic2Entry.setRadicals(radicals);
			kanjiDic2Entry.setKunReading(kunReading);
			kanjiDic2Entry.setOnReading(onReading);
		}
				
		KanjiEntry entry = new KanjiEntry();
		
		entry.setId(id);
		entry.setKanji(kanjiString);
		entry.setStrokePaths(parseStringIntoList(strokePathString, false));
		entry.setPolishTranslates(parseStringIntoList(polishTranslateListString, false));
		entry.setInfo(infoString);
		
		entry.setGenerated(Boolean.parseBoolean(generatedString));
		
		entry.setGroups(parseStringIntoList(groupString, false));
					
		entry.setKanjiDic2Entry(kanjiDic2Entry);
		
		return entry;
	}
}
