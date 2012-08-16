package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import android.content.res.AssetManager;
import android.content.res.Resources;

import com.csvreader.CsvReader;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.RadicalInfo;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;
import pl.idedyk.android.japaneselearnhelper.example.ExampleManager;
import pl.idedyk.android.japaneselearnhelper.gramma.GrammaConjugaterManager;
import pl.idedyk.android.japaneselearnhelper.utils.XorInputStream;

public class DictionaryManager {
	
	private static int GROUP_SIZE = 10;
	
	private static int MAX_LIST_SIZE = 5;
	
	private static int MAX_SEARCH_RESULT = 50;
	
	private static final String FILE_WORD = "word.csv";
	
	private static final String KANJI_WORD = "kanji.csv";
	
	private static final String RADICAL_WORD = "radical.csv";

	private static DictionaryManager instance;
	
	public static DictionaryManager getInstance() {
		
		if (instance == null) {
			throw new RuntimeException("No dictionary manager");
		}
		
		return instance;
	}
	
	private List<DictionaryEntry> wordDictionaryEntries = null;
	
	private Map<String, KanjiEntry> kanjiEntriesMap = null;
	
	private List<RadicalInfo> radicalList = null;
	
	public DictionaryManager() {
	}
	
	public void init(ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets) {
		
		final int k = 23;		
		
		try {
			// init
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_init));
			
			InputStream fileWordInputStream = new GZIPInputStream(new XorInputStream(assets.open(FILE_WORD), k));
			
			int wordFileSize = getWordSize(fileWordInputStream);
			
			loadWithProgress.setMaxValue(wordFileSize);
			
			// wczytywanie slow
			fileWordInputStream = new GZIPInputStream(new XorInputStream(assets.open(FILE_WORD), k));
			
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_words));
			
			readDictionaryFile(fileWordInputStream, loadWithProgress);
			
			// wczytywanie informacji o znakach podstawowych
			InputStream radicalInputStream = new GZIPInputStream(new XorInputStream(assets.open(RADICAL_WORD), k));
			
			int radicalFileSize = getWordSize(radicalInputStream);
			
			loadWithProgress.setCurrentPos(0);
			loadWithProgress.setMaxValue(radicalFileSize);
			
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_radical));
			
			radicalInputStream = new GZIPInputStream(new XorInputStream(assets.open(RADICAL_WORD), k));
			
			readRadicalEntriesFromCsv(radicalInputStream, loadWithProgress);			
			
			// wczytywanie kanji
			InputStream kanjiInputStream = new GZIPInputStream(new XorInputStream(assets.open(KANJI_WORD), k));
			
			int kanjiFileSize = getWordSize(kanjiInputStream);
			
			loadWithProgress.setCurrentPos(0);
			loadWithProgress.setMaxValue(kanjiFileSize);
			
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kanji));
			
			kanjiInputStream = new GZIPInputStream(new XorInputStream(assets.open(KANJI_WORD), k));
			
			readKanjiDictionaryFile(kanjiInputStream, loadWithProgress);			
			
			// obliczanie form (tutaj)
//			loadWithProgress.setCurrentPos(0);
//			loadWithProgress.setMaxValue(wordDictionaryEntries.size());
//			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_count_word_forms));
//			
//			countForm(loadWithProgress);
			
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_ready));
			
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
	
	private void readDictionaryFile(InputStream dictionaryInputStream, ILoadWithProgress loadWithProgress) throws IOException, DictionaryException {
		
		wordDictionaryEntries = new ArrayList<DictionaryEntry>();
		
		CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');
		
		int currentPos = 1;
		
		while(csvReader.readRecord()) {
			
			currentPos++;
			
			loadWithProgress.setCurrentPos(currentPos);
			
			String idString = csvReader.get(0);
			String dictionaryEntryTypeString = csvReader.get(2);
			String prefixKanaString = csvReader.get(4);
			String kanjiString = csvReader.get(5);
			
			if (kanjiString.equals("") == true || kanjiString.equals("-") == true) {
				kanjiString = null;
			}
			
			String kanaListString = csvReader.get(6);
			String prefixRomajiString = csvReader.get(7);

			if (prefixRomajiString.equals("") == true || prefixRomajiString.equals("-") == true) {
				prefixRomajiString = null;
			}
			
			String romajiListString = csvReader.get(8);
			String translateListString = csvReader.get(9);
			String infoString = csvReader.get(10);
			
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
						
			wordDictionaryEntries.add(entry);
		}
		
		csvReader.close();
	}
	
	private List<String> parseStringIntoList(String text, boolean limitSize) {
		
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
	
	@SuppressWarnings("unused")
	private void countForm(ILoadWithProgress loadWithProgress) {
		
		final Map<String, KanaEntry> kanaCache = KanaHelper.getKanaCache();
		
		int counter = 1;
		
		for (DictionaryEntry currentDictionaryEntry : wordDictionaryEntries) {
			
			GrammaConjugaterManager.getGrammaConjufateResult(currentDictionaryEntry);
			ExampleManager.getExamples(currentDictionaryEntry);
			
			List<String> kanaList = currentDictionaryEntry.getKanaList();
			
			for (String currentKana : kanaList) {
				KanaHelper.createRomajiString(KanaHelper.convertKanaStringIntoKanaWord(currentKana, kanaCache));
			}
			
			loadWithProgress.setCurrentPos(counter);
			
			counter++;
		}
	}
	
	private void readKanjiDictionaryFile(InputStream kanjiInputStream, ILoadWithProgress loadWithProgress) throws IOException, DictionaryException {
		
		kanjiEntriesMap = new HashMap<String, KanjiEntry>();
		
		CsvReader csvReader = new CsvReader(new InputStreamReader(kanjiInputStream), ',');
		
		int currentPos = 1;
		
		while(csvReader.readRecord()) {
			
			currentPos++;
			
			loadWithProgress.setCurrentPos(currentPos);
			
			int id = Integer.parseInt(csvReader.get(0));
			
			String kanjiString = csvReader.get(1);
			
			if (kanjiString.equals("") == true) {
				throw new DictionaryException("Empty kanji!");
			}
			
			String strokeCountString = csvReader.get(2);
			
			KanjiDic2Entry kanjiDic2Entry = null;
			
			if (strokeCountString.equals("") == false) {
				
				kanjiDic2Entry = new KanjiDic2Entry();
				
				int strokeCount = Integer.parseInt(strokeCountString);
			
				String radicalsString = csvReader.get(3);
				List<String> radicals = parseStringIntoList(radicalsString, false);
			
				String onReadingString = csvReader.get(4);
				List<String> onReading = parseStringIntoList(onReadingString, false);
			
				String kunReadingString = csvReader.get(5);
				List<String> kunReading = parseStringIntoList(kunReadingString, false);
				
				kanjiDic2Entry.setKanji(kanjiString);
				kanjiDic2Entry.setStrokeCount(strokeCount);
				kanjiDic2Entry.setRadicals(radicals);
				kanjiDic2Entry.setKunReading(kunReading);
				kanjiDic2Entry.setOnReading(onReading);			
			}
			
			String polishTranslateListString = csvReader.get(6);
			String infoString = csvReader.get(7);
			
			KanjiEntry entry = new KanjiEntry();
			
			entry.setId(id);
			entry.setKanji(kanjiString);
			entry.setPolishTranslates(parseStringIntoList(polishTranslateListString, false));
			entry.setInfo(infoString);
						
			entry.setKanjiDic2Entry(kanjiDic2Entry);
			
			kanjiEntriesMap.put(kanjiString, entry);
		}
		
		csvReader.close();
	}
	
	public List<KanjiEntry> findKnownKanji(String text) {
		
		List<KanjiEntry> result = new ArrayList<KanjiEntry>();
		
		for (int idx = 0; idx < text.length(); ++idx) {
			
			String currentChar = String.valueOf(text.charAt(idx));
			
			KanjiEntry kanjiEntry = kanjiEntriesMap.get(currentChar);
			
			if (kanjiEntry != null) {
				result.add(kanjiEntry);
			}
		}
		
		return result;
	}
	
	private void readRadicalEntriesFromCsv(InputStream radicalInputStream, ILoadWithProgress loadWithProgress) throws IOException, DictionaryException {
		
		radicalList = new ArrayList<RadicalInfo>();
		
		CsvReader csvReader = new CsvReader(new InputStreamReader(radicalInputStream), ',');
		
		int currentPos = 1;
		
		while(csvReader.readRecord()) {			
			
			currentPos++;
			
			loadWithProgress.setCurrentPos(currentPos);
			
			int id = Integer.parseInt(csvReader.get(0));
					
			String radical = csvReader.get(1);
			
			if (radical.equals("") == true) {
				throw new DictionaryException("Empty radical!");
			}
			
			String strokeCountString = csvReader.get(2);
			
			int strokeCount = Integer.parseInt(strokeCountString);
			
			RadicalInfo entry = new RadicalInfo();
			
			entry.setId(id);
			entry.setRadical(radical);
			entry.setStrokeCount(strokeCount);
			
			radicalList.add(entry);
		}
		
		csvReader.close();
	}
	
	public static class FindWordResult {
		
		public List<DictionaryEntry> result;
		
		public boolean moreElemetsExists = false;		
	}
}
