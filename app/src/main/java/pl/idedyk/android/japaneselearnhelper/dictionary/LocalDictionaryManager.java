package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.data.DataManager;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupItemEntity;
import pl.idedyk.android.japaneselearnhelper.data.exception.DataManagerException;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.TestSM2ManagerException;
import pl.idedyk.japanese.dictionary.api.dictionary.DictionaryManagerAbstract;
import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.WordPowerList;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;
import pl.idedyk.japanese.dictionary.api.dto.RadicalInfo;
import pl.idedyk.japanese.dictionary.api.dto.TransitiveIntransitivePair;
import pl.idedyk.japanese.dictionary.api.dto.TransitiveIntransitivePairWithDictionaryEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary.api.keigo.KeigoHelper;
import pl.idedyk.japanese.dictionary.api.tools.KanaHelper;
import pl.idedyk.japanese.dictionary.lucene.LuceneDatabase;

import android.app.Activity;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Environment;

import com.csvreader.CsvReader;

public class LocalDictionaryManager extends DictionaryManagerCommon {

	private static final String KANJI_RECOGNIZE_MODEL_DB_FILE = "kanji_recognizer.model.db";

	private static final String TRANSITIVE_INTRANSTIVE_PAIRS_FILE = "transitive_intransitive_pairs.csv";

	private static final String WORD_POWER_FILE = "word-power.csv";

	private static final String DATABASE_FILE = "dictionary.db";
	
	private static final String LUCENE_ZIP_FILE = "dictionary.zip";
	
	private static final String LUCENE_DIR = "db-lucene";
	
	private File lucenePath = null;

	// private AndroidSqliteDatabase androidSqliteDatabase;

	private List<TransitiveIntransitivePair> transitiveIntransitivePairsList = null;

	//

	private LuceneDatabase luceneDatabase;

	private AssetManager assets;

	public LocalDictionaryManager() {
		
		super();
		
		//databaseConnector = sqliteConnector = new SQLiteConnector();
		//sqliteConnector = new SQLiteConnector();

		// keigoHeper = new KeigoHelper();
	}
	
	@Override
	public void waitForDatabaseReady() {
		// noop		
	}

	@Override
	public void init2(Activity activity, ILoadWithProgress loadWithProgress, Resources resources, AssetManager assets, String packageName, int versionCode) {

		this.assets = assets;

		try {
			try {
				// delete old database file
				deleteOldDatabaseFile(packageName);

			} catch (IOException e) {
				loadWithProgress.setError(resources.getString(R.string.dictionary_manager_generic_ioerror, e.toString()));

				return;
			}

			File sqliteDatabaseFile = new File(databaseDir, DATABASE_FILE);
			
			lucenePath = new File(baseDir, LUCENE_DIR);
			
			// create lucene dir
			lucenePath.mkdirs();			
			
			// delete old sqlite database directory
			sqliteDatabaseFile.delete();			
			
			File databaseVersionFile = new File(sqliteDatabaseFile.getAbsolutePath() + "-version");

			File databaseRecognizeModelFile = new File(databaseDir, KANJI_RECOGNIZE_MODEL_DB_FILE);

			// androidSqliteDatabase = new AndroidSqliteDatabase(lucenePath.getAbsolutePath());
			
			databaseConnector = luceneDatabase = new LuceneDatabase(lucenePath.getAbsolutePath());
			
			// get database version
			int databaseVersion = getDatabaseVersion(databaseVersionFile);
			
			if (versionCode != databaseVersion) {

				try {
					// copy dictionary file
					//copyDatabaseFileToDatabaseDir(assets.open(DATABASE_FILE), databaseFile);

					// delete files from lucene dir					
					File[] lucenePathListFile = lucenePath.listFiles();
					
					for (File file : lucenePathListFile) {
						file.delete();
					}
					
					// unzip lucene database
					unpackZip(assets.open(LUCENE_ZIP_FILE), lucenePath);					
					
					// save database version
					saveDatabaseVersion(databaseVersionFile, versionCode);

				} catch (IOException e) {
					loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

					return;
				}
			}

			// open database			
			try {				
				luceneDatabase.open();

			} catch (Exception e) {
				loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

				return;
			}

			// wczytywanie slow
			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_words));
			loadWithProgress.setCurrentPos(0);

			fakeProgress(loadWithProgress);

			// wczytanie informacji o pisaniu znakow kana
			if (initKana(activity, loadWithProgress, resources, assets) == false) {
				return;
			}

			// wczytywanie informacji o znakach podstawowych
			if (initRadical(activity, loadWithProgress, resources, assets) == false) {
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
			zinniaManager = new LocalZinniaManager(databaseRecognizeModelFile);

			loadWithProgress.setDescription(resources.getString(R.string.dictionary_manager_load_kanji_recognize));
			loadWithProgress.setCurrentPos(0);
			loadWithProgress.setMaxValue(1);

			try {
				InputStream kanjiRecognizeModelInputStream = assets.open(KANJI_RECOGNIZE_MODEL_DB_FILE);

				((LocalZinniaManager)zinniaManager).copyKanjiRecognizeModelToData(kanjiRecognizeModelInputStream, loadWithProgress);
			} catch (IOException e) {
				loadWithProgress.setError(resources.getString(R.string.dictionary_manager_ioerror));

				return;
			}

			// create word test sm2 manager
			if (initWordTestSM2Manager(activity, loadWithProgress, resources) == false) {
				return;
			}

			// create data manager
			if (initDataManager(activity, loadWithProgress, resources) == false) {
				return;
			}

			//

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

	private void saveDatabaseVersion(File databaseVersionFile, int versionCode) throws IOException {

		BufferedWriter writer = new BufferedWriter(new FileWriter(databaseVersionFile));

		writer.write(String.valueOf(versionCode));

		writer.close();
	}

	private int getDatabaseVersion(File databaseVersionFile) {

		int version = 0;

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
			luceneDatabase.open();
		} catch (Exception e) {
			return 0;
		} finally {
			try {
				luceneDatabase.close();
			} catch (Exception e) {
				return 0;
			}
		}

		return version;
	}
	
	private boolean unpackZip(InputStream zipInputFile, File dbLuceneFile) {   
		
	     ZipInputStream zis = null;
	     
	     try {
	         zis = new ZipInputStream(new BufferedInputStream(zipInputFile));          
	         
	         ZipEntry ze = null;
	         
	         byte[] buffer = new byte[1024];
	         int count;

	         while ((ze = zis.getNextEntry()) != null) {
	             // get file name
	             String filename = ze.getName();
	             
	             if (ze.isDirectory() == true) {
	            	 throw new IOException("Zip input directory file is not supported");
	             }

	             FileOutputStream fout = new FileOutputStream(new File(dbLuceneFile, filename));

	             while ((count = zis.read(buffer)) != -1) {
	                 fout.write(buffer, 0, count);             
	             }

	             fout.close();               
	             zis.closeEntry();
	         }

	         zis.close();
	         
	     } catch(IOException e) {
	         return false;
	     }

	    return true;
	}

	/*
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
	*/

	public void countForm(ILoadWithProgress loadWithProgress, Resources resources) throws DictionaryException {

		throw new UnsupportedOperationException();
		
		/*
		
		int counter = 1;

		int transactionCounter = 0;

		int dictionaryEntriesSize = lucene3Database.getDictionaryEntriesSize();

		loadWithProgress.setCurrentPos(0);
		loadWithProgress.setMaxValue(dictionaryEntriesSize);

		lucene3Database.beginTransaction();

		try {

			for (int dictionaryEntriesSizeIdx = 0; dictionaryEntriesSizeIdx < dictionaryEntriesSize; ++dictionaryEntriesSizeIdx) {
				loadWithProgress.setCurrentPos(counter);

				DictionaryEntry nthDictionaryEntry = lucene3Database.getNthDictionaryEntry(dictionaryEntriesSizeIdx);

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
		*/
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

	@Override
	public void close() {
		try {
			luceneDatabase.close();

			getDataManager().close();

			getWordTestSM2Manager().close();

		} catch (Exception e) {
			throw new RuntimeException(e);
		}
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
		
		throw new UnsupportedOperationException();
		
		//return lucene3Database.getGrammaFormAndExamplesEntriesSize();
	}

	@Override
	public List<TransitiveIntransitivePairWithDictionaryEntry> getTransitiveIntransitivePairsList() throws DictionaryException {

		List<TransitiveIntransitivePairWithDictionaryEntry> result = new ArrayList<>();

		for (TransitiveIntransitivePair currentTransitiveIntransitivePair : transitiveIntransitivePairsList) {

			Integer transitiveId = currentTransitiveIntransitivePair.getTransitiveId();
			Integer intransitiveId = currentTransitiveIntransitivePair.getIntransitiveId();

			//

			TransitiveIntransitivePairWithDictionaryEntry newTransitiveIntransitivePairWithDictionaryEntry = new TransitiveIntransitivePairWithDictionaryEntry();

			newTransitiveIntransitivePairWithDictionaryEntry.setTransitiveId(transitiveId);
			newTransitiveIntransitivePairWithDictionaryEntry.setTransitiveDictionaryEntry(getDictionaryEntryById(transitiveId));

			newTransitiveIntransitivePairWithDictionaryEntry.setIntransitiveId(intransitiveId);
			newTransitiveIntransitivePairWithDictionaryEntry.setIntransitiveDictionaryEntry(getDictionaryEntryById(intransitiveId));

			//

			result.add(newTransitiveIntransitivePairWithDictionaryEntry);
		}

		return result;
	}

	@Override
	public WordPowerList getWordPowerList() throws DictionaryException {

		CsvReader wordPowerInputStreamCsvReader = null;

		try {
			WordPowerList wordPowerList = new WordPowerList();

			wordPowerInputStreamCsvReader = new CsvReader(new InputStreamReader(assets.open(WORD_POWER_FILE)), ',');

			while (wordPowerInputStreamCsvReader.readRecord()) {

				int columnCount = wordPowerInputStreamCsvReader.getColumnCount();

				int power = Integer.parseInt(wordPowerInputStreamCsvReader.get(0));

				for (int columnNo = 1; columnNo < columnCount; ++columnNo) {

					int currentDictionaryEntryIdx = Integer.parseInt(wordPowerInputStreamCsvReader.get(columnNo));

					wordPowerList.addPower(power, currentDictionaryEntryIdx);
				}
			}

			return wordPowerList;

		} catch (IOException e) {
			throw new DictionaryException(e);

		} finally {
			if (wordPowerInputStreamCsvReader != null) {
				wordPowerInputStreamCsvReader.close();
			}
		}
	}

	@Override
	protected void finalize() throws Throwable {

		super.finalize();

		luceneDatabase.close();

		close();
	}
}
