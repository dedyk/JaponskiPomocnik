package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.csvreader.CsvReader;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;

public class DictionaryManager {
	
	private static int GROUP_SIZE = 10;
	
	private static int MAX_LIST_SIZE = 5;
	
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
			
			String idString = csvReader.get(0);
			String dictionaryEntryTypeString = csvReader.get(2);
			String prefixString = csvReader.get(4);
			String kanjiString = csvReader.get(5);
			
			if (kanjiString.equals("") == true || kanjiString.equals("-") == true) {
				kanjiString = null;
			}
			
			String kanaListString = csvReader.get(7);
			String romajiListString = csvReader.get(8);
			String translateListString = csvReader.get(9);
			String infoString = csvReader.get(10);
			
			DictionaryEntryType dictionaryEntryType = DictionaryEntryType.valueOf(dictionaryEntryTypeString);
			
			if (dictionaryEntryType == DictionaryEntryType.WORD_KANJI_READING) {
				continue;
			} // FIXME
			
			List<String> romajiList = parseStringIntoList(romajiListString);
			List<String> kanaList = parseStringIntoList(kanaListString);
			
			if (romajiList.size() != kanaList.size()) {
				throw new DictionaryException("Parse parseStringIntoList size exception");
			}
			
			DictionaryEntry entry = new DictionaryEntry();
			
			entry.setId(Integer.parseInt(idString));
			entry.setDictionaryEntryType(dictionaryEntryType);
			entry.setPrefix(prefixString);
			entry.setKanji(kanjiString);
			entry.setRomajiList(romajiList);
			entry.setKanaList(kanaList);
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
	
	public FindWordResult findWord(String word) {
		
		FindWordResult findWordResult = new FindWordResult();
		
		findWordResult.result = new ArrayList<DictionaryEntry>();
		
		for (DictionaryEntry currentWordDictionaryEntry : wordDictionaryEntries) {
			
			if (matchWord(currentWordDictionaryEntry, word) == true) {
				findWordResult.result.add(currentWordDictionaryEntry);
			}
			
			if (findWordResult.result.size() >= MAX_SEARCH_RESULT) {
				findWordResult.moreElemetsExists = true;
				
				break;
			}
		}
		
		final Map<String, KanaEntry> kanaCache = KanaHelper.getKanaCache();
		
		Collections.sort(findWordResult.result, new Comparator<DictionaryEntry>() {

			public int compare(DictionaryEntry lhs, DictionaryEntry rhs) {
				
				List<String> lhsKanaList = lhs.getKanaList();
				List<String> rhsKanaList = rhs.getKanaList();
				
				/*
				if (lhsKanaList.size() < rhsKanaList.size()) {
					return -1;
				} else if (lhsKanaList.size() > rhsKanaList.size()) {
					return 1;
				}
				
				for (int idx = 0; idx < lhsKanaList.size(); ++idx) {
					
					String currentLhsKanaList = lhsKanaList.get(idx);
					String currentRhsKanaList = rhsKanaList.get(idx);
					
					int compareResult = currentLhsKanaList.compareToIgnoreCase(currentRhsKanaList);
					
					if (compareResult != 0) {
						return compareResult;
					}
				}
				*/
				
				int maxArraySize = lhsKanaList.size();
				
				if (maxArraySize < rhsKanaList.size()) {
					maxArraySize = rhsKanaList.size();
				}
				
				for (int idx = 0; idx < maxArraySize; ++idx) {
					int compareResult = compare(lhsKanaList, rhsKanaList, idx);
					
					if (compareResult != 0) {
						return compareResult;
					}
				}
				
				return 0;
			}
			
			private int compare(List<String> lhsKanaList, List<String> rhsKanaList, int idx) {
				
				String lhsString = getString(lhsKanaList, idx);
				
				String rhsString = getString(rhsKanaList, idx);
				
				if (lhsString == null && rhsString == null) {
					return 0;
				} else if (lhsString != null && rhsString == null) {
					return -1;
				} else if (lhsString == null && rhsString != null) {
					return 1;
				} else {
					String lhsRomaji = KanaHelper.createRomajiString(KanaHelper.convertKanaStringIntoKanaWord(lhsString, kanaCache));
					String rhsRomaji = KanaHelper.createRomajiString(KanaHelper.convertKanaStringIntoKanaWord(rhsString, kanaCache));
					
					return lhsRomaji.compareToIgnoreCase(rhsRomaji);
				}
			}
			
			private String getString(List<String> kanaList, int kanaListIdx) {
				if (kanaListIdx < kanaList.size()) {
					return kanaList.get(kanaListIdx);
				} else {
					return null;
				}
			}
		});
		
		return findWordResult;
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
	
	public static class FindWordResult {
		
		public List<DictionaryEntry> result;
		
		public boolean moreElemetsExists = false;		
	}
}
