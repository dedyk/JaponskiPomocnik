package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.File;
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
import java.util.TreeSet;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.csvreader.CsvReader;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordResult.ResultItem;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.FuriganaEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.RadicalInfo;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;
import pl.idedyk.android.japaneselearnhelper.example.ExampleManager;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.GrammaConjugaterManager;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;

public class DictionaryManager {

	private static final String WORD_FILE = "word.csv";

	private static final String KANJI_FILE = "kanji.csv";

	private static final String KANJI_RECOGNIZE_MODEL_DB_FILE = "kanji_recognizer.model.db";

	private static final String RADICAL_FILE = "radical.csv";

	private static final String KANA_FILE = "kana.csv";

	private SQLiteConnector sqliteConnector;

	private ZinniaManager zinniaManager;

	private List<RadicalInfo> radicalList = null;
	
	private KanaHelper kanaHelper;

	public DictionaryManager(SQLiteConnector sqliteConnector) {
		this.sqliteConnector = sqliteConnector;
	}

	public void init(ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets, String packageName) {

		if (loadWithProgress == null) {
			
			// create fake
			loadWithProgress = new ILoadWithProgress() {
				
				public void setMaxValue(int maxValue) {					
				}
				
				public void setDescription(String desc) {
				}
				
				public void setCurrentPos(int currentPos) {
				}
			};
		}
		
		try {			
			// init
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_init));

			sqliteConnector.open();

			boolean needInsertData = sqliteConnector.isNeedInsertData();

			// wczytywanie slow
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_words));
			loadWithProgress.setCurrentPos(0);

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
			loadWithProgress.setCurrentPos(0);

			InputStream kanaFileInputStream = assets.open(KANA_FILE);

			int kanaFileSize = getWordSize(kanaFileInputStream);

			loadWithProgress.setMaxValue(kanaFileSize);

			kanaFileInputStream = assets.open(KANA_FILE);

			readKanaFile(kanaFileInputStream, loadWithProgress);

			// wczytywanie informacji o znakach podstawowych
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_radical));
			loadWithProgress.setCurrentPos(0);

			InputStream radicalInputStream = assets.open(RADICAL_FILE);

			int radicalFileSize = getWordSize(radicalInputStream);

			loadWithProgress.setMaxValue(radicalFileSize);			

			radicalInputStream = assets.open(RADICAL_FILE);

			readRadicalEntriesFromCsv(radicalInputStream, loadWithProgress);			

			// wczytywanie kanji
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kanji));
			loadWithProgress.setCurrentPos(0);

			if (needInsertData == true) {

				InputStream kanjiInputStream = assets.open(KANJI_FILE);

				int kanjiFileSize = getWordSize(kanjiInputStream);

				loadWithProgress.setMaxValue(kanjiFileSize);

				kanjiInputStream = assets.open(KANJI_FILE);

				readKanjiDictionaryFile(kanjiInputStream, loadWithProgress);
			} else {
				fakeProgress(loadWithProgress);
			}

			// kopiowanie kanji recognize model do data
			zinniaManager = new ZinniaManager(new File("/data/data/" + packageName + "/" + KANJI_RECOGNIZE_MODEL_DB_FILE));

			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kanji_recognize));
			loadWithProgress.setCurrentPos(0);
			loadWithProgress.setMaxValue(1);

			InputStream kanjiRecognizeModelInputStream = assets.open(KANJI_RECOGNIZE_MODEL_DB_FILE);

			zinniaManager.copyKanjiRecognizeModelToData(kanjiRecognizeModelInputStream, loadWithProgress);			

			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_ready));

			// if emulator
			/*
			if (new File("/data/data/" + packageName + "/emulator").exists() == true) {
				// validate
				
				// countForm(loadWithProgress);
				// getFuriganaForAll();
			}
			*/

		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}
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
				String groupsString = csvReader.get(4);
				String prefixKanaString = csvReader.get(5);
				String kanjiString = csvReader.get(6);

				String kanaListString = csvReader.get(7);
				String prefixRomajiString = csvReader.get(8);

				String romajiListString = csvReader.get(9);
				String translateListString = csvReader.get(10);
				String infoString = csvReader.get(11);

				DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, groupsString,
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

	public int getWordGroupsNo(int groupSize) {

		int dictionaryEntriesSize = sqliteConnector.getDictionaryEntriesSize();

		int result = dictionaryEntriesSize / groupSize;

		if (dictionaryEntriesSize % groupSize > 0) {
			result++;
		}

		return result;
	}

	public List<DictionaryEntry> getWordsGroup(int groupSize, int groupNo) {

		try {
			int dictionaryEntriesSize = sqliteConnector.getDictionaryEntriesSize();

			List<DictionaryEntry> result = new ArrayList<DictionaryEntry>();

			for (int idx = groupNo * groupSize; idx < (groupNo + 1) * groupSize && idx < dictionaryEntriesSize; ++idx) {
				DictionaryEntry currentDictionaryEntry = sqliteConnector.getNthDictionaryEntry(idx);

				result.add(currentDictionaryEntry);
			}

			return result;
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}
	}

	public FindWordResult findWord(FindWordRequest findWordRequest) {

		FindWordResult findWordResult = null;

		try {
			findWordResult = sqliteConnector.findDictionaryEntries(findWordRequest);

			sqliteConnector.findDictionaryEntriesInGrammaFormAndExamples(findWordRequest, findWordResult);

		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}		

		final Map<String, KanaEntry> kanaCache = kanaHelper.getKanaCache();

		Collections.sort(findWordResult.result, new Comparator<ResultItem>() {

			public int compare(ResultItem lhs, ResultItem rhs) {

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
					String lhsRomaji = kanaHelper.createRomajiString(kanaHelper.convertKanaStringIntoKanaWord(lhsString, kanaCache, true));
					String rhsRomaji = kanaHelper.createRomajiString(kanaHelper.convertKanaStringIntoKanaWord(rhsString, kanaCache, true));

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

	public void countForm(ILoadWithProgress loadWithProgress) throws DictionaryException {

		int counter = 1;

		int transactionCounter = 0;

		int dictionaryEntriesSize = sqliteConnector.getDictionaryEntriesSize();

		loadWithProgress.setCurrentPos(0);
		loadWithProgress.setMaxValue(dictionaryEntriesSize);

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

				counter++;

				transactionCounter++;

				if (transactionCounter >= 400) {
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

				String generatedString = csvReader.get(9);

				String groupString = csvReader.get(10);

				KanjiEntry entry = Utils.parseKanjiEntry(idString, kanjiString, strokeCountString, 
						radicalsString, onReadingString, kunReadingString, strokePathString, 
						polishTranslateListString, infoString, generatedString, groupString);

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
				throw new RuntimeException("currentRadicalInfo == null: " + currentRadical);
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

	public KanjiEntry findKanji(String kanji) {

		KanjiEntry kanjiEntry = null;

		try {
			kanjiEntry = sqliteConnector.getKanjiEntry(kanji);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}

		return kanjiEntry;		
	}

	public List<KanjiEntry> getAllKanjis(boolean withDetails, boolean addGenerated) {
		try {
			return sqliteConnector.getAllKanjis(withDetails, addGenerated);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}
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
				throw new DictionaryException("Empty radical: " + radical);
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

	public FindKanjiResult findKanjisFromStrokeCount(int from, int to) {

		FindKanjiResult result = null;

		try {
			result = sqliteConnector.findKanjisFromStrokeCount(from, to);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}

		Collections.sort(result.getResult(), new Comparator<KanjiEntry>() {

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

		kanaHelper = new KanaHelper(kanaAndStrokePaths);

		csvReader.close();		
	}

	public List<List<String>> getStrokePathsForWord(String word) {

		List<List<String>> result = new ArrayList<List<String>>();

		if (word == null) {
			return result;
		}

		Map<String, KanaEntry> kanaCache = kanaHelper.getKanaCache();

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

	public List<FuriganaEntry> getFurigana(DictionaryEntry dictionaryEntry) {

		if (dictionaryEntry == null) {
			return null;
		}

		String kanji = dictionaryEntry.getKanji();

		if (kanji == null) {
			return null;
		}

		List<String> kana = dictionaryEntry.getKanaList();

		List<FuriganaEntry> result = new ArrayList<FuriganaEntry>();

		for (String currentKana : kana) {
			List<FuriganaEntry> currentFurigana = getFurigana(kanji, currentKana);

			if (currentFurigana != null) {
				result.addAll(currentFurigana);
			} else {
				Log.d("FuriganaError", kanji + " - " + currentKana);
			}
		}
		
		List<FuriganaEntry> newResult = new ArrayList<FuriganaEntry>();
		
		for (FuriganaEntry currentFuriganaEntry : result) {
			if (currentFuriganaEntry.matchKanaWithKanaPart() == true) {
				newResult.add(currentFuriganaEntry);
			}
		}

		result = newResult;
		
		if (result.size() == 0) {			
			return null;
		}

		return result;
	}
	
	/*
	private void getFuriganaForAll() throws DictionaryException {

		int dictionaryEntriesSize = sqliteConnector.getDictionaryEntriesSize();

		for (int dictionaryEntriesSizeIdx = 0; dictionaryEntriesSizeIdx < dictionaryEntriesSize; ++dictionaryEntriesSizeIdx) {

			DictionaryEntry nthDictionaryEntry = sqliteConnector.getNthDictionaryEntry(dictionaryEntriesSizeIdx);

			getFurigana(nthDictionaryEntry);
		}
	}
	*/

	private List<FuriganaEntry> getFurigana(String kanji, String kana) {

		List<FuriganaEntry> furiganaEntries = new ArrayList<FuriganaEntry>();

		// start furigana
		FuriganaEntry startFuriganaEntry = new FuriganaEntry(kanji, kana);

		furiganaEntries.add(startFuriganaEntry);

		for (int idx = 0; idx < kanji.length(); ++idx) {

			String currentChar = String.valueOf(kanji.charAt(idx));

			KanjiEntry kanjiEntry = null;
			try {
				kanjiEntry = sqliteConnector.getKanjiEntry(currentChar);
			} catch (DictionaryException e) {
				throw new RuntimeException(e);
			}

			if (kanjiEntry == null) { // if hiragana

				List<FuriganaEntry> newFuriganaEntries = new ArrayList<FuriganaEntry>();

				for (FuriganaEntry furiganaEntry : furiganaEntries) {

					FuriganaEntry newFuriganaEntry = furiganaEntry.createCopy();

					boolean removeCharsFromCurrentKanaStateResult = newFuriganaEntry.removeCharsFromCurrentKanaState(1);

					if (removeCharsFromCurrentKanaStateResult == true) {
						newFuriganaEntry.addHiraganaChar(currentChar);

						newFuriganaEntries.add(newFuriganaEntry);
					}
				}

				furiganaEntries = newFuriganaEntries;

				continue;
			}

			List<String> kanjiReading = normalizeKanjiReading(kanjiEntry);

			List<FuriganaEntry> newFuriganaEntries = new ArrayList<FuriganaEntry>();

			for (String currentKanjiReading : kanjiReading) {

				for (FuriganaEntry currentFuriganaEntry : furiganaEntries) {

					String currentKanaState = currentFuriganaEntry.getCurrentKanaState();

					if (currentKanaState.startsWith(currentKanjiReading) == true) { // match

						FuriganaEntry newCurrentFuriganaEntry = currentFuriganaEntry.createCopy();

						boolean removeCharsFromCurrentKanaStateResult = newCurrentFuriganaEntry.removeCharsFromCurrentKanaState(currentKanjiReading.length());

						if (removeCharsFromCurrentKanaStateResult == true) {
							newCurrentFuriganaEntry.addReading(kanjiEntry.getKanji(), currentKanjiReading);

							newFuriganaEntries.add(newCurrentFuriganaEntry);
						}
					}
				}
			}

			furiganaEntries = newFuriganaEntries;
		}

		if (furiganaEntries.size() == 0) {
			
			FuriganaEntry furiganaEntry = new FuriganaEntry(kanji, kana);
			
			furiganaEntry.addReading(kanji, kana);
			
			furiganaEntries.add(furiganaEntry);
		}

		return furiganaEntries;
	}

	private List<String> normalizeKanjiReading(KanjiEntry kanjiEntry) {

		List<String> result = new ArrayList<String>();

		KanjiDic2Entry kanjiDic2Entry = kanjiEntry.getKanjiDic2Entry();

		if (kanjiDic2Entry == null) {
			return result;
		}

		List<String> kunReading = kanjiDic2Entry.getKunReading();

		if (kunReading != null && kunReading.size() > 0) {

			for (String currentKunReading : kunReading) {

				List<String> readingVariantList = getReadingVariant(currentKunReading);

				if (readingVariantList != null) {
					result.addAll(readingVariantList);
				}
			}
		}

		List<String> onReading = kanjiDic2Entry.getOnReading();

		if (onReading != null && onReading.size() > 0) {

			for (String currentOnReading : onReading) {

				List<String> readingVariantList = getReadingVariant(currentOnReading);

				List<String> readingVariantList2 = new ArrayList<String>();

				if (readingVariantList != null) {
					for (String currentReadingVariant : readingVariantList) {
						String hiraganaString = kanaHelper.convertKatakanaToHiragana(currentReadingVariant);

						if (hiraganaString != null) {
							readingVariantList2.add(hiraganaString);
						}
					}
				}

				if (readingVariantList2 != null) {
					result.addAll(readingVariantList2);
				}
			}
		}
		
		// generate additional
		
		Map<String, String[]> generateAdditionalMap = new HashMap<String, String[]>();
		
		generateAdditionalMap.put("か", new String[] { "が" });
		generateAdditionalMap.put("き", new String[] { "ぎ" });
		generateAdditionalMap.put("く", new String[] { "ぐ" });
		generateAdditionalMap.put("け", new String[] { "げ" });
		generateAdditionalMap.put("こ", new String[] { "ご" });

		generateAdditionalMap.put("さ", new String[] { "ざ" });
		generateAdditionalMap.put("し", new String[] { "じ" });
		generateAdditionalMap.put("す", new String[] { "ず" });
		generateAdditionalMap.put("せ", new String[] { "ぜ" });
		generateAdditionalMap.put("そ", new String[] { "ぞ" });

		generateAdditionalMap.put("た", new String[] { "だ" });
		generateAdditionalMap.put("ち", new String[] { "ぢ" });
		generateAdditionalMap.put("つ", new String[] { "づ" });
		generateAdditionalMap.put("て", new String[] { "で" });
		generateAdditionalMap.put("と", new String[] { "ど" });

		generateAdditionalMap.put("は", new String[] { "ば", "ぱ" });
		generateAdditionalMap.put("ひ", new String[] { "び", "ぴ" });
		generateAdditionalMap.put("ふ", new String[] { "ぶ", "ぷ" });
		generateAdditionalMap.put("へ", new String[] { "べ", "ぺ" });
		generateAdditionalMap.put("ほ", new String[] { "ぼ", "ぽ" });
		
		List<String> newResult = new ArrayList<String>();
		
		for (String currentResult : result) {
			
			if (currentResult.length() == 0) {
				continue;
			}
			
			String currentResultFirstCgar = String.valueOf(currentResult.charAt(0));
			
			String[] additionalValues = generateAdditionalMap.get(currentResultFirstCgar);
			
			if (additionalValues == null) {
				newResult.add(currentResult);
			} else {
				newResult.add(currentResult);
				
				for (String currentAdditionalValue : additionalValues) {
					newResult.add(currentAdditionalValue + currentResult.substring(1));
				}
			}
		}

		TreeSet<String> treeSet = new TreeSet<String>(newResult);

		return new ArrayList<String>(treeSet);
	}

	private List<String> getReadingVariant(String reading) {

		if (reading == null) {
			return null;
		}

		boolean nextStep = true;

		List<String> result = new ArrayList<String>();

		{
			String reading1 = reading.trim();

			if (reading1.length() == 0) {
				nextStep = false;
			}

			if (nextStep == true) {

				int dotIdx = reading1.indexOf(".");

				if (dotIdx != -1) {
					reading1 = reading1.substring(0, dotIdx);
				}

				reading1 = reading1.trim();

				if (reading1.startsWith("-") == true) {
					reading1 = reading1.substring(1);
				}

				reading1 = reading1.trim();

				if (reading1.endsWith("-") == true) {
					reading1 = reading1.substring(0, reading1.length() - 1);
				}

				reading1 = reading1.trim();

				if (reading1.length() == 0) {
					nextStep = false;
				}

				if (nextStep == true) {
					result.add(reading1);
				}
			}
		}

		{
			String reading2 = reading.trim();

			reading2 = reading2.replaceAll("\\.", "");

			reading2 = reading2.trim();

			if (reading2.startsWith("-") == true) {
				reading2 = reading2.substring(1);
			}

			reading2 = reading2.trim();

			result.add(reading2);
		}
		
		return result;
	}

	public int getGrammaFormAndExamplesEntriesSize() {
		return sqliteConnector.getGrammaFormAndExamplesEntriesSize();
	}

	public ZinniaManager getZinniaManager() {
		return zinniaManager;
	}

	public KanaHelper getKanaHelper() {
		return kanaHelper;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		close();
	}
}
