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
import pl.idedyk.android.japaneselearnhelper.gramma.AdjectiveIGrammaConjugater;
import pl.idedyk.android.japaneselearnhelper.gramma.AdjectiveNaGrammaConjugater;

public class DictionaryManager {
	
	private static int GROUP_SIZE = 10;
	
	private static int MAX_LIST_SIZE = 5;
	
	private static int MAX_SEARCH_RESULT = 50;
	
	private static final String FILE_WORD = "/word.csv";

	private static DictionaryManager instance;
	
	public static DictionaryManager getInstance() {
		
		if (instance == null) {
			throw new RuntimeException("No dictionary manager");
		}
		
		return instance;
	}
	
	private List<DictionaryEntry> wordDictionaryEntries = null;
	
	public DictionaryManager() {
	}
	
	public void init(ILoadWithProgress loadWithProgress) {
		
		// read word csv file
		wordDictionaryEntries = new ArrayList<DictionaryEntry>();
		
		InputStream fileWordInputStream = DictionaryManager.class.getResourceAsStream(FILE_WORD);
		
		try {
			int wordFileSize = getWordSize(fileWordInputStream);
			
			loadWithProgress.setMaxValue(wordFileSize);
			
			fileWordInputStream = DictionaryManager.class.getResourceAsStream(FILE_WORD);
			
			readDictionaryFile(fileWordInputStream, loadWithProgress, wordDictionaryEntries);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}
		
		instance = this;
	}
	
	private int getWordSize(InputStream dictionaryInputStream) throws IOException {
		
		int size = 0;
		
		CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');
		
		while(csvReader.readRecord()) {
			size++;			
		}
		
		dictionaryInputStream.close();
		
		return size;		
	}
	
	private void readDictionaryFile(InputStream dictionaryInputStream, ILoadWithProgress loadWithProgress, List<DictionaryEntry> dictionary) throws IOException, DictionaryException {
		
		CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');
		
		int currentPos = 1;
		
		while(csvReader.readRecord()) {
			
			currentPos++;
			
			loadWithProgress.setCurrentPos(currentPos);
			
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
			
			if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_I) {
				entry.setGrammaFormConjugateGroupTypeElementsList(AdjectiveIGrammaConjugater.makeAll(entry));
			} else if (dictionaryEntryType == DictionaryEntryType.WORD_ADJECTIVE_NA) {
				entry.setGrammaFormConjugateGroupTypeElementsList(AdjectiveNaGrammaConjugater.makeAll(entry));
			}
			
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
