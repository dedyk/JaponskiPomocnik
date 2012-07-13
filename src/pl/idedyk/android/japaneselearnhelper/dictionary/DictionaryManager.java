package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;

public class DictionaryManager {
	
	private static int GROUP_SIZE = 10;
	
	private static int MAX_LIST_SIZE = 4;
	
	private static int MAX_SEARCH_RESULT = 50;
	
	private static final String FILE_WORD = "/word.csv";

	private static DictionaryManager instance;
	
	public static DictionaryManager getInstance() {
		
		if (instance == null) {
			instance = new DictionaryManager();
		}
		
		return instance;
	}
	
	private List<DictionaryEntry> wordDictionaryEntries = null;
	
	private DictionaryManager() {
		
		// read word csv file
		wordDictionaryEntries = new ArrayList<DictionaryEntry>();
		
		InputStream fileWordInputStream = DictionaryManager.class.getResourceAsStream(FILE_WORD);
		
		try {
			readDictionaryFile(fileWordInputStream, wordDictionaryEntries);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private void readDictionaryFile(InputStream dictionaryInputStream, List<DictionaryEntry> dictionary) throws IOException, DictionaryException {
				
		CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');
		
		while(csvReader.readRecord()) {
			
			String dictionaryEntryType = csvReader.get(0);
			String prefixString = csvReader.get(2);
			String kanjiString = csvReader.get(3);
			
			if (kanjiString.equals("") == true || kanjiString.equals("-") == true) {
				kanjiString = null;
			}
			
			String kanaListString = csvReader.get(5);
			String romajiListString = csvReader.get(6);
			String translateListString = csvReader.get(7);
			String infoString = csvReader.get(8);
			
			DictionaryEntry entry = new DictionaryEntry();
			
			entry.setDictionaryEntryType(DictionaryEntryType.valueOf(dictionaryEntryType));
			entry.setPrefix(prefixString);
			entry.setKanji(kanjiString);
			entry.setRomajiList(parseStringIntoList(romajiListString));
			entry.setKanaList(parseStringIntoList(kanaListString));
			entry.setTranslates(parseStringIntoList(translateListString));
			
			entry.setInfo(infoString);
			
			dictionary.add(entry);
		}
		
		csvReader.close();
	}
	
	private List<String> parseStringIntoList(String text) {
		
		List<String> result = new ArrayList<String>();
		
		String[] splitedText = text.split("\n");
		
		for (String currentSplitedText : splitedText) {			
			result.add(currentSplitedText);
		}
		
		if (result.size() > MAX_LIST_SIZE) {
			throw new RuntimeException("parseStringIntoList max list size");
		}
		
		return result;		
	}
	
	public int getWordGroupsNo() {		
		int result = wordDictionaryEntries.size() / GROUP_SIZE;
		
		if (wordDictionaryEntries.size() % GROUP_SIZE > 0) {
			result++;
		}
		
		return result;
	}

	public List<DictionaryEntry> getWordsGroup(int groupNo) {
		
		List<DictionaryEntry> result = new ArrayList<DictionaryEntry>();
		
		for (int idx = groupNo * GROUP_SIZE; idx < (groupNo + 1) * GROUP_SIZE && idx < wordDictionaryEntries.size(); ++idx) {
			DictionaryEntry currentDictionaryEntry = wordDictionaryEntries.get(idx);
			
			result.add(currentDictionaryEntry);
		}
		
		return result;
	}
	
	public List<DictionaryEntry> findWord(String word) {
		
		List<DictionaryEntry> result = new ArrayList<DictionaryEntry>();
		
		for (DictionaryEntry currentWordDictionaryEntry : wordDictionaryEntries) {
			
			if (matchWord(currentWordDictionaryEntry, word) == true) {
				result.add(currentWordDictionaryEntry);
			}
			
			if (result.size() >= MAX_SEARCH_RESULT) {
				break;
			}
		}
		
		return result;
	}
	
	public boolean matchWord(DictionaryEntry dictionaryEntry, String word) {
		
		String fullKanji = dictionaryEntry.getFullKanji();
		
		if (fullKanji != null && fullKanji.contains(word) == true) {
			return true;
		}
		
		List<String> fullKanaList = dictionaryEntry.getFullKanaList();
		
		for (String currentFullKanaList : fullKanaList) {
			
			if (currentFullKanaList.toLowerCase().contains(word) == true) {
				return true;
			}
		}
		
		String wordLowerCase = word.toLowerCase();
		
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		for (String currentRomaji : romajiList) {
			if (currentRomaji.toLowerCase().contains(wordLowerCase) == true) {
				return true;
			}
		}
		
		List<String> translates = dictionaryEntry.getTranslates();
		
		
		for (String currentTranslate : translates) {
			if (currentTranslate.toLowerCase().contains(wordLowerCase) == true) {
				return true;
			}
		}
		
		return false;
	}
}
