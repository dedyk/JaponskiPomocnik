package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.TestSM2ManagerException;
import pl.idedyk.android.japaneselearnhelper.dictionary.sqlite.AndroidSqliteDatabase;
import pl.idedyk.japanese.dictionary.api.dictionary.DictionaryManagerAbstract;
import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dictionary.sqlite.SQLiteConnector;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;
import pl.idedyk.japanese.dictionary.api.dto.RadicalInfo;
import pl.idedyk.japanese.dictionary.api.dto.TransitiveIntransitivePair;
import pl.idedyk.japanese.dictionary.api.example.ExampleManager;
import pl.idedyk.japanese.dictionary.api.example.dto.ExampleGroupTypeElements;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary.api.gramma.GrammaConjugaterManager;
import pl.idedyk.japanese.dictionary.api.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.japanese.dictionary.api.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.japanese.dictionary.api.gramma.dto.GrammaFormConjugateResultType;
import pl.idedyk.japanese.dictionary.api.keigo.KeigoHelper;
import pl.idedyk.japanese.dictionary.api.tools.KanaHelper;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import com.csvreader.CsvReader;

public class DictionaryManager extends DictionaryManagerAbstract {

	private static final String KANJI_RECOGNIZE_MODEL_DB_FILE = "kanji_recognizer.model.db";

	private static final String RADICAL_FILE = "radical.csv";

	private static final String TRANSITIVE_INTRANSTIVE_PAIRS_FILE = "transitive_intransitive_pairs.csv";

	private static final String KANA_FILE = "kana.csv";

	private static final String DATABASE_FILE = "dictionary.db";

	private final SQLiteConnector sqliteConnector;
	private AndroidSqliteDatabase androidSqliteDatabase;

	private ZinniaManager zinniaManager;

	private List<RadicalInfo> radicalList = null;

	private List<TransitiveIntransitivePair> transitiveIntransitivePairsList = null;

	private KanaHelper kanaHelper;

	private final KeigoHelper keigoHeper;

	private WordTestSM2Manager wordTestSM2Manager;

	public DictionaryManager() {
		
		super(new SQLiteConnector());

		sqliteConnector = (SQLiteConnector)getDatabaseConnector();
		
		keigoHeper = new KeigoHelper();
	}

	public void init(ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets, String packageName,
			int versionCode) {

		try {
			// init
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_init));

			try {
				// delete old database file
				deleteOldDatabaseFile(packageName);

			} catch (IOException e) {
				loadWithProgress
						.setError(resources.getString(R.string.dictionary_manager_generic_ioerror, e.toString()));

				return;
			}

			// check external storage state
			if (checkExternalStorageState(loadWithProgress, resources) == false) {
				return;
			}

			// create base dir in external storage
			File externalStorageDirectory = Environment.getExternalStorageDirectory();

			// create base dir
			File baseDir = new File(externalStorageDirectory, "JaponskiPomocnik");

			if (baseDir.isDirectory() == false) {

				if (baseDir.mkdirs() == false) {
					loadWithProgress
							.setError(resources.getString(R.string.dictionary_manager_create_directories_error));

					return;
				}
			}

			// create directory dir
			File databaseDir = new File(baseDir, "db");

			if (databaseDir.isDirectory() == false) {

				if (databaseDir.mkdirs() == false) {
					loadWithProgress
							.setError(resources.getString(R.string.dictionary_manager_create_directories_error));

					return;
				}
			}

			File databaseFile = new File(databaseDir, DATABASE_FILE);
			File databaseVersionFile = new File(databaseFile.getAbsolutePath() + "-version");

			File databaseRecognizeModelFile = new File(databaseDir, KANJI_RECOGNIZE_MODEL_DB_FILE);

			androidSqliteDatabase = new AndroidSqliteDatabase(databaseFile.getAbsolutePath());
			
			// get database version
			int databaseVersion = getDatabaseVersion(databaseFile, databaseVersionFile);

			if (versionCode != databaseVersion) {

				try {
					// copy dictionary file
					copyDatabaseFileToDatabaseDir(assets.open(DATABASE_FILE), databaseFile);

					// save database version
					saveDatabaseVersion(databaseVersionFile, versionCode);

				} catch (IOException e) {
					loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

					return;
				}
			}

			// open database			
			try {
				sqliteConnector.open(androidSqliteDatabase);

			} catch (SQLException e) {
				loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

				return;
			}

			// wczytywanie slow
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_words));
			loadWithProgress.setCurrentPos(0);

			fakeProgress(loadWithProgress);

			// wczytanie informacji o pisaniu znakow kana
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kana));
			loadWithProgress.setCurrentPos(0);

			try {
				InputStream kanaFileInputStream = assets.open(KANA_FILE);
				int kanaFileSize = getWordSize(kanaFileInputStream);
				loadWithProgress.setMaxValue(kanaFileSize);

				kanaFileInputStream = assets.open(KANA_FILE);
				readKanaFile(kanaFileInputStream, loadWithProgress);

			} catch (IOException e) {
				loadWithProgress
						.setError(resources.getString(R.string.dictionary_manager_generic_ioerror, e.toString()));

				return;
			}

			// wczytywanie informacji o znakach podstawowych
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_radical));
			loadWithProgress.setCurrentPos(0);

			try {
				InputStream radicalInputStream = assets.open(RADICAL_FILE);
				int radicalFileSize = getWordSize(radicalInputStream);
				loadWithProgress.setMaxValue(radicalFileSize);

				radicalInputStream = assets.open(RADICAL_FILE);
				readRadicalEntriesFromCsv(radicalInputStream, loadWithProgress);

			} catch (IOException e) {
				loadWithProgress
						.setError(resources.getString(R.string.dictionary_manager_generic_ioerror, e.toString()));

				return;
			}

			// wczytywanie informacji o parach czasownikow przechodnich i nieprzechodnich
			loadWithProgress.setDescription(resources
					.getString(R.string.dictionary_manager_load_transitive_intransitive_pairs));
			loadWithProgress.setCurrentPos(0);

			try {
				InputStream transitiveIntransitivePairsInputStream = assets.open(TRANSITIVE_INTRANSTIVE_PAIRS_FILE);
				int transitiveIntransitivePairsFileSize = getWordSize(transitiveIntransitivePairsInputStream);
				loadWithProgress.setMaxValue(transitiveIntransitivePairsFileSize);

				transitiveIntransitivePairsInputStream = assets.open(TRANSITIVE_INTRANSTIVE_PAIRS_FILE);
				readTransitiveIntransitivePairsFromCsv(transitiveIntransitivePairsInputStream, loadWithProgress);

			} catch (IOException e) {
				loadWithProgress
						.setError(resources.getString(R.string.dictionary_manager_generic_ioerror, e.toString()));

				return;
			}

			// wczytywanie kanji
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kanji));
			loadWithProgress.setCurrentPos(0);

			fakeProgress(loadWithProgress);

			// copy kanji recognize model db data
			zinniaManager = new ZinniaManager(databaseRecognizeModelFile);

			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kanji_recognize));
			loadWithProgress.setCurrentPos(0);
			loadWithProgress.setMaxValue(1);

			try {
				InputStream kanjiRecognizeModelInputStream = assets.open(KANJI_RECOGNIZE_MODEL_DB_FILE);

				zinniaManager.copyKanjiRecognizeModelToData(kanjiRecognizeModelInputStream, loadWithProgress);
			} catch (IOException e) {
				loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

				return;
			}

			// create word test sm2 manager
			wordTestSM2Manager = new WordTestSM2Manager(databaseDir);

			// open word test sm2 manager
			try {
				wordTestSM2Manager.open();
			} catch (TestSM2ManagerException e) {
				loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

				return;
			}

			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_ready));

			return;

		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}
	}

	private void deleteOldDatabaseFile(String packageName) throws IOException {

		String packageBaseDir = Environment.getDataDirectory().getAbsolutePath() + "/data/" + packageName;

		//  delete old kanji recognize mode db data
		new File(packageBaseDir + "/" + KANJI_RECOGNIZE_MODEL_DB_FILE).delete();

		// delete all from database directory

		String databaseDir = packageBaseDir + "/databases/";

		File databaseDirFile = new File(databaseDir);

		if (databaseDirFile.isDirectory() == true) {

			File[] databaseDirListFiles = databaseDirFile.listFiles();

			for (File currentDatabaseDirListFiles : databaseDirListFiles) {
				currentDatabaseDirListFiles.delete();
			}

			databaseDirFile.delete();
		}
	}

	private boolean checkExternalStorageState(ILoadWithProgress loadWithProgress, Resources resources) {

		String state = Environment.getExternalStorageState();

		if (Environment.MEDIA_MOUNTED.equals(state) == false) {
			loadWithProgress.setError(resources.getString(R.string.dictionary_manager_bad_external_storage_state));

			return false;
		}

		return true;
	}

	private void saveDatabaseVersion(File databaseVersionFile, int versionCode) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(databaseVersionFile));

		writer.write(String.valueOf(versionCode));

		writer.close();
	}

	private int getDatabaseVersion(File databaseFile, File databaseVersionFile) {

		int version = 0;

		if (databaseFile.exists() == false) {
			return version;
		}

		if (databaseVersionFile.exists() == false) {
			return version;
		}

		BufferedReader reader = null;

		try {
			reader = new BufferedReader(new FileReader(databaseVersionFile));

			String line = reader.readLine();

			if (line == null) {
				return version;
			}

			try {
				version = Integer.parseInt(line);
			} catch (NumberFormatException e) {
				return version;
			}

		} catch (IOException e) {
			return version;

		} finally {

			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e1) {
				}
			}
		}

		// testing open database
		try {
			sqliteConnector.open(androidSqliteDatabase);
		} catch (SQLException e) {
			return 0;
		} finally {
			sqliteConnector.close();
		}

		return version;
	}

	private void copyDatabaseFileToDatabaseDir(InputStream databaseInputStream, File databaseOutputFile)
			throws IOException {

		BufferedOutputStream databaseOutputStream = null;

		databaseOutputStream = new BufferedOutputStream(new FileOutputStream(databaseOutputFile));

		byte[] buffer = new byte[8096];

		int read;

		while ((read = databaseInputStream.read(buffer)) != -1) {
			databaseOutputStream.write(buffer, 0, read);
		}

		databaseInputStream.close();
		databaseOutputStream.close();
	}

	private int getWordSize(InputStream dictionaryInputStream) throws IOException {

		int size = 0;

		CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');

		while (csvReader.readRecord()) {
			size++;
		}

		dictionaryInputStream.close();

		return size;
	}

	public void countForm(ILoadWithProgress loadWithProgress, Resources resources) throws DictionaryException {

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

				Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache = new HashMap<GrammaFormConjugateResultType, GrammaFormConjugateResult>();

				List<GrammaFormConjugateGroupTypeElements> grammaConjufateResult = GrammaConjugaterManager
						.getGrammaConjufateResult(keigoHeper, nthDictionaryEntry, grammaFormCache, null);

				if (grammaConjufateResult != null) {
					for (GrammaFormConjugateGroupTypeElements grammaFormConjugateGroupTypeElements : grammaConjufateResult) {
						sqliteConnector.insertGrammaFormConjugateGroupTypeElements(nthDictionaryEntry,
								grammaFormConjugateGroupTypeElements);
					}
				}

				List<ExampleGroupTypeElements> examples = ExampleManager.getExamples(keigoHeper, nthDictionaryEntry,
						grammaFormCache, null);

				if (examples != null) {
					for (ExampleGroupTypeElements exampleGroupTypeElements : examples) {
						sqliteConnector.insertExampleGroupTypeElements(nthDictionaryEntry, exampleGroupTypeElements);
					}
				}

				counter++;

				transactionCounter++;

				if (transactionCounter >= 200) {
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

	private void readRadicalEntriesFromCsv(InputStream radicalInputStream, ILoadWithProgress loadWithProgress)
			throws IOException, DictionaryException {

		radicalList = new ArrayList<RadicalInfo>();

		CsvReader csvReader = new CsvReader(new InputStreamReader(radicalInputStream), ',');

		int currentPos = 1;

		while (csvReader.readRecord()) {

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

	@Override
	public List<RadicalInfo> getRadicalList() {
		return radicalList;
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

		Map<String, List<KanjivgEntry>> kanaAndStrokePaths = new HashMap<String, List<KanjivgEntry>>();

		while (csvReader.readRecord()) {

			currentPos++;

			loadWithProgress.setCurrentPos(currentPos);

			//int id = Integer.parseInt(csvReader.get(0));

			String kana = csvReader.get(1);

			String strokePath1String = csvReader.get(2);

			String strokePath2String = csvReader.get(3);

			List<KanjivgEntry> strokePaths = new ArrayList<KanjivgEntry>();

			strokePaths.add(new KanjivgEntry(Utils.parseStringIntoList(strokePath1String, false)));

			if (strokePath2String == null || strokePath2String.equals("") == false) {
				strokePaths.add(new KanjivgEntry(Utils.parseStringIntoList(strokePath2String, false)));
			}

			kanaAndStrokePaths.put(kana, strokePaths);
		}

		kanaHelper = new KanaHelper(kanaAndStrokePaths);

		csvReader.close();
	}

	private void readTransitiveIntransitivePairsFromCsv(InputStream transitiveIntransitivePairsInputStream,
			ILoadWithProgress loadWithProgress) throws IOException {

		CsvReader csvReader = new CsvReader(new InputStreamReader(transitiveIntransitivePairsInputStream), ',');

		int currentPos = 1;

		transitiveIntransitivePairsList = new ArrayList<TransitiveIntransitivePair>();

		while (csvReader.readRecord()) {

			currentPos++;

			loadWithProgress.setCurrentPos(currentPos);

			Integer transitiveId = Integer.valueOf(csvReader.get(0));
			Integer intransitiveId = Integer.valueOf(csvReader.get(1));

			TransitiveIntransitivePair transitiveIntransitivePair = new TransitiveIntransitivePair();

			transitiveIntransitivePair.setTransitiveId(transitiveId);
			transitiveIntransitivePair.setIntransitiveId(intransitiveId);

			transitiveIntransitivePairsList.add(transitiveIntransitivePair);
		}

		csvReader.close();
	}

	public int getGrammaFormAndExamplesEntriesSize() {
		return sqliteConnector.getGrammaFormAndExamplesEntriesSize();
	}

	public ZinniaManager getZinniaManager() {
		return zinniaManager;
	}

	public WordTestSM2Manager getWordTestSM2Manager() {
		return wordTestSM2Manager;
	}

	public KanaHelper getKanaHelper() {
		return kanaHelper;
	}

	public KeigoHelper getKeigoHelper() {
		return keigoHeper;
	}

	@Override
	protected void finalize() throws Throwable {
		super.finalize();

		wordTestSM2Manager.close();

		close();
	}

	public List<TransitiveIntransitivePair> getTransitiveIntransitivePairsList() {
		return transitiveIntransitivePairsList;
	}
}
