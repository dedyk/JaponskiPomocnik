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
import java.util.Set;

import android.content.res.AssetManager;
import android.content.res.Resources;

import com.csvreader.CsvReader;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.RadicalInfo;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;
import pl.idedyk.android.japaneselearnhelper.example.ExampleManager;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.GrammaConjugaterManager;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;

public class DictionaryManager {
	
	private static int GROUP_SIZE = 10;
	
	private static final String WORD_FILE = "word.csv";
	
	private static final String KANJI_FILE = "kanji.csv";
	
	private static final String RADICAL_FILE = "radical.csv";
	
	private static final String KANA_FILE = "kana.csv";

	private static DictionaryManager instance;
	
	public static DictionaryManager getInstance() {
		
		if (instance == null) {
			throw new RuntimeException("No dictionary manager");
		}
		
		return instance;
	}
	
	private SQLiteConnector sqliteConnector;
		
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

			// wczytywanie slow
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_words));
			
			if (needInsertData == true) {

				InputStream fileWordInputStream = assets.open(WORD_FILE);

				int wordFileSize = getWordSize(fileWordInputStream);

				loadWithProgress.setMaxValue(wordFileSize);

				fileWordInputStream = assets.open(WORD_FILE);

				readDictionaryFile(fileWordInputStream, loadWithProgress);
			} else {
				fakeProgress(loadWithProgress);
			}
			
			// wczytanie informacji o pisaniu znakow kana
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kana));
							
			InputStream kanaFileInputStream = assets.open(KANA_FILE);

			int kanaFileSize = getWordSize(kanaFileInputStream);
			
			loadWithProgress.setMaxValue(kanaFileSize);

			kanaFileInputStream = assets.open(KANA_FILE);
			
			readKanaFile(kanaFileInputStream, loadWithProgress);
			
			// wczytywanie informacji o znakach podstawowych
			InputStream radicalInputStream = assets.open(RADICAL_FILE);

			int radicalFileSize = getWordSize(radicalInputStream);

			loadWithProgress.setCurrentPos(0);
			loadWithProgress.setMaxValue(radicalFileSize);

			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_radical));

			radicalInputStream = assets.open(RADICAL_FILE);

			readRadicalEntriesFromCsv(radicalInputStream, loadWithProgress);			

			// wczytywanie kanji
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kanji));
			
			if (needInsertData == true) {

				InputStream kanjiInputStream = assets.open(KANJI_FILE);

				int kanjiFileSize = getWordSize(kanjiInputStream);

				loadWithProgress.setCurrentPos(0);
				loadWithProgress.setMaxValue(kanjiFileSize);

				kanjiInputStream = assets.open(KANJI_FILE);

				readKanjiDictionaryFile(kanjiInputStream, loadWithProgress);
			} else {
				fakeProgress(loadWithProgress);
			}

			// obliczanie form (tutaj)
			if (needInsertData == true) {
				int dictionaryEntriesSize = sqliteConnector.getDictionaryEntriesSize();
			
				loadWithProgress.setCurrentPos(0);
				loadWithProgress.setMaxValue(dictionaryEntriesSize);
				loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_count_word_forms));
			
				countForm(loadWithProgress);
			} else {
				fakeProgress(loadWithProgress);
			}
			
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
		
		int transactionCounter = 0;
		
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
				
				transactionCounter++;
				
				if (transactionCounter >= 1000) {
					transactionCounter = 0;
					
					try {
						sqliteConnector.commitTransaction();
					} finally {
						sqliteConnector.endTransaction();
					}
					
					sqliteConnector.beginTransaction();
				}
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
	
	public FindWordResult findWord(FindWordRequest findWordRequest) {
				
		FindWordResult findWordResult = null;
		
		try {
			findWordResult = sqliteConnector.findDictionaryEntries(findWordRequest);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}
		
		final Map<String, KanaEntry> kanaCache = KanaHelper.getInstance().getKanaCache();
		
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
					String lhsRomaji = KanaHelper.getInstance().createRomajiString(KanaHelper.getInstance().convertKanaStringIntoKanaWord(lhsString, kanaCache));
					String rhsRomaji = KanaHelper.getInstance().createRomajiString(KanaHelper.getInstance().convertKanaStringIntoKanaWord(rhsString, kanaCache));
					
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
	
	private void countForm(ILoadWithProgress loadWithProgress) throws DictionaryException {
		
		final Map<String, KanaEntry> kanaCache = KanaHelper.getInstance().getKanaCache();
		
		int counter = 1;
		
		int transactionCounter = 0;
		
		int dictionaryEntriesSize = sqliteConnector.getDictionaryEntriesSize();

		sqliteConnector.beginTransaction();
		
		try {
			
			for (int dictionaryEntriesSizeIdx = 0; dictionaryEntriesSizeIdx < dictionaryEntriesSize; ++dictionaryEntriesSizeIdx) {
				loadWithProgress.setCurrentPos(counter);
				
				DictionaryEntry nthDictionaryEntry = sqliteConnector.getNthDictionaryEntry(dictionaryEntriesSizeIdx);
	
				List<GrammaFormConjugateGroupTypeElements> grammaConjufateResult = GrammaConjugaterManager.getGrammaConjufateResult(nthDictionaryEntry);
				
				if (grammaConjufateResult != null) {
					for (GrammaFormConjugateGroupTypeElements grammaFormConjugateGroupTypeElements : grammaConjufateResult) {
						sqliteConnector.insertGrammaFormConjugateGroupTypeElements(nthDictionaryEntry, grammaFormConjugateGroupTypeElements);
					}					
				}
				
				List<ExampleGroupTypeElements> examples = ExampleManager.getExamples(nthDictionaryEntry);
				
				if (examples != null) {
					for (ExampleGroupTypeElements exampleGroupTypeElements : examples) {
						sqliteConnector.insertExampleGroupTypeElements(nthDictionaryEntry, exampleGroupTypeElements);
					}
				}
				
				List<String> kanaList = nthDictionaryEntry.getKanaList();
				
				for (String currentKana : kanaList) {
					KanaHelper.getInstance().createRomajiString(KanaHelper.getInstance().convertKanaStringIntoKanaWord(currentKana, kanaCache));
				}
											
				counter++;
				
				transactionCounter++;
				
				if (transactionCounter >= 500) {
					transactionCounter = 0;
					
					try {
						sqliteConnector.commitTransaction();
					} finally {
						sqliteConnector.endTransaction();
					}
					
					sqliteConnector.beginTransaction();
				}
			}
		} finally {
			sqliteConnector.endTransaction();
		}
	}
	
	private void readKanjiDictionaryFile(InputStream kanjiInputStream, ILoadWithProgress loadWithProgress) throws IOException, DictionaryException {
				
		Map<String, RadicalInfo> radicalListMapCache = new HashMap<String, RadicalInfo>();
		
		for (RadicalInfo currentRadicalInfo : radicalList) {
			
			String radical = currentRadicalInfo.getRadical();
			
			radicalListMapCache.put(radical, currentRadicalInfo);
		}
		
		CsvReader csvReader = new CsvReader(new InputStreamReader(kanjiInputStream), ',');
		
		int currentPos = 1;
		
		int transactionCounter = 0;
		
		sqliteConnector.beginTransaction();
		
		try {
			while(csvReader.readRecord()) {

				currentPos++;

				loadWithProgress.setCurrentPos(currentPos);

				String idString = csvReader.get(0);

				String kanjiString = csvReader.get(1);

				String strokeCountString = csvReader.get(2);

				String radicalsString = csvReader.get(3);

				String onReadingString = csvReader.get(4);

				String kunReadingString = csvReader.get(5);

				String strokePathString = csvReader.get(6);

				String polishTranslateListString = csvReader.get(7);
				String infoString = csvReader.get(8);

				KanjiEntry entry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, 
						radicalsString, onReadingString, kunReadingString, strokePathString, 
						polishTranslateListString, infoString);

				// update radical info
				if (entry.getKanjiDic2Entry() != null) {
					updateRadicalInfoUse(radicalListMapCache, entry.getKanjiDic2Entry().getRadicals());	
				}
				
				// insert
				sqliteConnector.insertKanjiEntry(entry);
				
				transactionCounter++;
				
				if (transactionCounter >= 1000) {
					transactionCounter = 0;
					
					try {
						sqliteConnector.commitTransaction();
					} finally {
						sqliteConnector.endTransaction();
					}
					
					sqliteConnector.beginTransaction();
				}
			}
			
			sqliteConnector.commitTransaction();
			
		} finally {
			sqliteConnector.endTransaction();
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
			
			KanjiEntry kanjiEntry = null;
			try {
				kanjiEntry = sqliteConnector.getKanjiEntry(currentChar);
			} catch (DictionaryException e) {
				throw new RuntimeException(e);
			}
			
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
		
		List<KanjiEntry> result = null;
		
		try {
			result = sqliteConnector.findKanjiFromRadicals(radicals);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
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
				
		try {
			return sqliteConnector.findAllAvailableRadicals(radicals);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void fakeProgress(ILoadWithProgress loadWithProgress) {
		
		int max = 50;
		
		loadWithProgress.setMaxValue(max);
		
		for (int pos = 0; pos <= max; ++pos) {
			loadWithProgress.setCurrentPos(pos);
			
			try {
				Thread.sleep(5);
			} catch (InterruptedException e) {
			}
		}
	}
	
	public void close() {
		sqliteConnector.close();
	}
	
	private void readKanaFile(InputStream kanaFileInputStream, ILoadWithProgress loadWithProgress) throws IOException {
		
		CsvReader csvReader = new CsvReader(new InputStreamReader(kanaFileInputStream), ',');
		
		int currentPos = 1;
		
		Map<String, List<List<String>>> kanaAndStrokePaths = new HashMap<String, List<List<String>>>();
		
		while(csvReader.readRecord()) {			
			
			currentPos++;
			
			loadWithProgress.setCurrentPos(currentPos);
			
			//int id = Integer.parseInt(csvReader.get(0));
			
			String kana = csvReader.get(1);
			
			String strokePath1String = csvReader.get(2);
			
			String strokePath2String = csvReader.get(3);
			
			List<List<String>> strokePaths = new ArrayList<List<String>>();
			
			strokePaths.add(Utils.parseStringIntoList(strokePath1String, false));
			
			if (strokePath2String == null || strokePath2String.equals("") == false) {
				strokePaths.add(Utils.parseStringIntoList(strokePath2String, false));
			}
			
			kanaAndStrokePaths.put(kana, strokePaths);
		}
		
		new KanaHelper(kanaAndStrokePaths);
		
		csvReader.close();		
	}
	
	public List<List<String>> getStrokePathsForWord(String word) {
		
		List<List<String>> result = new ArrayList<List<String>>();
		
		if (word == null) {
			return result;
		}
		
		Map<String, KanaEntry> kanaCache = KanaHelper.getInstance().getKanaCache();
		
		for (int idx = 0; idx < word.length(); ++idx) {
			
			String currentChar = String.valueOf(word.charAt(idx));
			
			KanjiEntry kanjiEntry = null;
			try {
				kanjiEntry = sqliteConnector.getKanjiEntry(currentChar);
			} catch (DictionaryException e) {
				throw new RuntimeException(e);
			}
			
			if (kanjiEntry != null) {
				result.add(kanjiEntry.getStrokePaths());
			} else {
				KanaEntry kanaEntry = kanaCache.get(currentChar);
				
				if (kanaEntry != null) {
					result.addAll(kanaEntry.getStrokePaths());
				}
			}
		}
		
		return result;
	}
}
