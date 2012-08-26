package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import android.content.res.AssetManager;
import android.content.res.Resources;

import com.csvreader.CsvReader;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.RadicalInfo;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;

public class DictionaryManager {
	
	private static int GROUP_SIZE = 10;
	
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
	
	private SQLiteConnector sqliteConnector;
	
	private Map<String, KanjiEntry> kanjiEntriesMap = null;
	
	private List<RadicalInfo> radicalList = null;
	
	public DictionaryManager(SQLiteConnector sqliteConnector) {
		this.sqliteConnector = sqliteConnector;
	}
	
	public void init(ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets) {
				
		try {			
			// init
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_init));
			
			sqliteConnector.open();
			
			boolean needInsertData = sqliteConnector.isNeedInsertData();
			
			if (needInsertData == true) {

				InputStream fileWordInputStream = new GZIPInputStream(assets.open(FILE_WORD));

				int wordFileSize = getWordSize(fileWordInputStream);

				loadWithProgress.setMaxValue(wordFileSize);

				// wczytywanie slow
				fileWordInputStream = new GZIPInputStream(assets.open(FILE_WORD));

				loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_words));

				readDictionaryFile(fileWordInputStream, loadWithProgress);
			}
			
			// wczytywanie informacji o znakach podstawowych
			InputStream radicalInputStream = new GZIPInputStream(assets.open(RADICAL_WORD));

			int radicalFileSize = getWordSize(radicalInputStream);

			loadWithProgress.setCurrentPos(0);
			loadWithProgress.setMaxValue(radicalFileSize);

			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_radical));

			radicalInputStream = new GZIPInputStream(assets.open(RADICAL_WORD));

			readRadicalEntriesFromCsv(radicalInputStream, loadWithProgress);			

			// wczytywanie kanji
			InputStream kanjiInputStream = new GZIPInputStream(assets.open(KANJI_WORD));

			int kanjiFileSize = getWordSize(kanjiInputStream);

			loadWithProgress.setCurrentPos(0);
			loadWithProgress.setMaxValue(kanjiFileSize);

			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kanji));

			kanjiInputStream = new GZIPInputStream(assets.open(KANJI_WORD));

			readKanjiDictionaryFile(kanjiInputStream, loadWithProgress);			

			// obliczanie form (tutaj)
			// loadWithProgress.setCurrentPos(0);
			// loadWithProgress.setMaxValue(wordDictionaryEntries.size());
			// loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_count_word_forms));
			//
			// countForm(loadWithProgress);
			
			
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
		
		CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');
		
		int currentPos = 1;
		
		sqliteConnector.beginTransaction();
		
		try {
			while(csvReader.readRecord()) {
				
				currentPos++;
				
				loadWithProgress.setCurrentPos(currentPos);
				
				String idString = csvReader.get(0);
				String dictionaryEntryTypeString = csvReader.get(2);
				String prefixKanaString = csvReader.get(4);
				String kanjiString = csvReader.get(5);
							
				String kanaListString = csvReader.get(6);
				String prefixRomajiString = csvReader.get(7);
				
				String romajiListString = csvReader.get(8);
				String translateListString = csvReader.get(9);
				String infoString = csvReader.get(10);
							
				DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, 
						prefixKanaString, kanjiString, kanaListString, prefixRomajiString,
						romajiListString, translateListString, infoString);
				
				sqliteConnector.insertDictionaryEntry(entry);
			}
			
			sqliteConnector.commitTransaction();
		} finally {
			sqliteConnector.endTransaction();
		}
		
		csvReader.close();
	}
		
	public int getWordGroupsNo() {
		
		int dictionaryEntriesSize = sqliteConnector.getDictionaryEntriesSize();
		
		int result = dictionaryEntriesSize / GROUP_SIZE;
		
		if (dictionaryEntriesSize % GROUP_SIZE > 0) {
			result++;
		}
		
		return result;
	}

	public List<DictionaryEntry> getWordsGroup(int groupNo) throws DictionaryException {
		
		int dictionaryEntriesSize = sqliteConnector.getDictionaryEntriesSize();
		
		List<DictionaryEntry> result = new ArrayList<DictionaryEntry>();
		
		for (int idx = groupNo * GROUP_SIZE; idx < (groupNo + 1) * GROUP_SIZE && idx < dictionaryEntriesSize; ++idx) {
			DictionaryEntry currentDictionaryEntry = sqliteConnector.getNthDictionaryEntry(idx);
			
			result.add(currentDictionaryEntry);
		}
		
		return result;
	}
	
	public FindWordResult findWord(String word) {
				
		FindWordResult findWordResult = null;
		
		try {
			findWordResult = sqliteConnector.findDictionaryEntries(word);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
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
		
		String info = dictionaryEntry.getInfo();
		
		if (info != null) {
			if (info.toLowerCase().contains(wordLowerCase) == true) {
				return true;
			}			
		}
		
		return false;
	}
	
	/*
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
	*/
	
	private void readKanjiDictionaryFile(InputStream kanjiInputStream, ILoadWithProgress loadWithProgress) throws IOException, DictionaryException {
				
		Map<String, RadicalInfo> radicalListMapCache = new HashMap<String, RadicalInfo>();
		
		for (RadicalInfo currentRadicalInfo : radicalList) {
			
			String radical = currentRadicalInfo.getRadical();
			
			radicalListMapCache.put(radical, currentRadicalInfo);
		}
		
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
				List<String> radicals = Utils.parseStringIntoList(radicalsString, false);
			
				String onReadingString = csvReader.get(4);
				List<String> onReading = Utils.parseStringIntoList(onReadingString, false);
			
				String kunReadingString = csvReader.get(5);
				List<String> kunReading = Utils.parseStringIntoList(kunReadingString, false);
				
				kanjiDic2Entry.setKanji(kanjiString);
				kanjiDic2Entry.setStrokeCount(strokeCount);
				kanjiDic2Entry.setRadicals(radicals);
				kanjiDic2Entry.setKunReading(kunReading);
				kanjiDic2Entry.setOnReading(onReading);
				
				// update radical info
				updateRadicalInfoUse(radicalListMapCache, radicals);
			}
			
			String strokePathString = csvReader.get(6);
			
			String polishTranslateListString = csvReader.get(7);
			String infoString = csvReader.get(8);
			
			KanjiEntry entry = new KanjiEntry();
			
			entry.setId(id);
			entry.setKanji(kanjiString);
			entry.setStrokePaths(Utils.parseStringIntoList(strokePathString, false));
			entry.setPolishTranslates(Utils.parseStringIntoList(polishTranslateListString, false));
			entry.setInfo(infoString);
						
			entry.setKanjiDic2Entry(kanjiDic2Entry);
			
			kanjiEntriesMap.put(kanjiString, entry);
		}
		
		csvReader.close();
	}
	
	private void updateRadicalInfoUse(Map<String, RadicalInfo> radicalListMapCache, List<String> radicals) {
		
		for (String currentRadical : radicals) {
			
			RadicalInfo currentRadicalInfo = radicalListMapCache.get(currentRadical);
			
			if (currentRadicalInfo == null) {
				throw new RuntimeException("currentRadicalInfo == null");
			}
			
			//currentRadicalInfo.incrementUse();			
		}
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
	
	public List<RadicalInfo> getRadicalList() {
		return radicalList;
	}

	public List<KanjiEntry> findKnownKanjiFromRadicals(String[] radicals) {
		
		List<KanjiEntry> result = new ArrayList<KanjiEntry>();
		
		Iterator<String> kanjiEntriesMapKeySetIterator = kanjiEntriesMap.keySet().iterator();
		
		while(kanjiEntriesMapKeySetIterator.hasNext()) {
			
			String currentKanji = kanjiEntriesMapKeySetIterator.next();
			
			KanjiEntry currentKanjiKanjiEntry = kanjiEntriesMap.get(currentKanji);
			
			if (hasAllRadicals(currentKanjiKanjiEntry, radicals) == true) {				
				result.add(currentKanjiKanjiEntry);
			}
		}
		
		Collections.sort(result, new Comparator<KanjiEntry>() {

			public int compare(KanjiEntry lhs, KanjiEntry rhs) {
				
				int lhsId = lhs.getId();
				int rhsId = rhs.getId();
				
				if (lhsId < rhsId) {
					return -1;
				} else if (lhsId > rhsId) {
					return 1;
				} else {
					String lhsKanji = lhs.getKanji();
					String rhsKanji = rhs.getKanji();
					
					return lhsKanji.compareTo(rhsKanji);
				}
			}
		});
		
		return result;
	}

	public Set<String> findAllAvailableRadicals(String[] radicals) {
		
		Set<String> result = new HashSet<String>();
		
		Iterator<String> kanjiEntriesMapKeySetIterator = kanjiEntriesMap.keySet().iterator();
		
		while(kanjiEntriesMapKeySetIterator.hasNext()) {
			
			String currentKanji = kanjiEntriesMapKeySetIterator.next();
			
			KanjiEntry currentKanjiKanjiEntry = kanjiEntriesMap.get(currentKanji);
			
			if (hasAllRadicals(currentKanjiKanjiEntry, radicals) == true) {				
				result.addAll(currentKanjiKanjiEntry.getKanjiDic2Entry().getRadicals());
			}
		}
				
		return result;
	}
	
	private boolean hasAllRadicals(KanjiEntry kanjiEntry, String[] radicals) {
		
		KanjiDic2Entry kanjiDic2Entry = kanjiEntry.getKanjiDic2Entry();
		
		if (kanjiDic2Entry == null) {
			return false;
		}
		
		List<String> kanjiEntryRadicals = kanjiDic2Entry.getRadicals();
		
		Set<String> kanjiEntryRadicalsSet = new HashSet<String>(kanjiEntryRadicals);
		
		for (String currentRadical : radicals) {
			
			boolean containsResult = kanjiEntryRadicalsSet.contains(currentRadical);
			
			if (containsResult == false) {				
				return false;
			}
		}
		
		return true;
	}
}
