package pl.idedyk.android.japaneselearnhelper.dbgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.lucene.analysis.SimpleAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.Field.Index;
import org.apache.lucene.document.NumericField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.Version;

import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dictionary.lucene.LuceneStatic;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntryType;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import pl.idedyk.japanese.dictionary.api.dto.KanjiDic2Entry;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;
import pl.idedyk.japanese.dictionary.api.dto.RadicalInfo;
import pl.idedyk.japanese.dictionary.api.example.ExampleManager;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary.api.gramma.GrammaConjugaterManager;
import pl.idedyk.japanese.dictionary.api.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.japanese.dictionary.api.gramma.dto.GrammaFormConjugateResultType;
import pl.idedyk.japanese.dictionary.api.keigo.KeigoHelper;

import com.csvreader.CsvReader;

public class Lucene3DBGenerator {

	public static void main(String[] args) throws Exception {

		// parametry pliku
		final String dictionaryFilePath = "db/word.csv";
		final String kanjiFilePath = "db/kanji.csv";
		final String radicalFilePath = "db/radical.csv";
				
		final File dbOutDirFile = new File("db-lucene");
		
		if (dbOutDirFile.exists() == false) {
			dbOutDirFile.mkdir();
		}
		
		if (dbOutDirFile.isDirectory() == true) {
			
			File[] dbOutDirFileListFiles = dbOutDirFile.listFiles();
			
			for (File file : dbOutDirFileListFiles) {
				file.delete();
			}
		}		
		
		// tworzenie indeksu lucene
		Directory index = FSDirectory.open(dbOutDirFile);
		
		// tworzenie analizatora lucene
		SimpleAnalyzer analyzer = new SimpleAnalyzer(Version.LUCENE_36);
		
		// tworzenie zapisywacza konfiguracji
		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_36, analyzer);
		indexWriterConfig.setOpenMode(OpenMode.CREATE);
		
		IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig);
		
		// otwarcie pliku ze slownikiem
		FileInputStream dictionaryInputStream = new FileInputStream(dictionaryFilePath);

		// wczytywanie slownika
		readDictionaryFile(indexWriter, dictionaryInputStream);

		dictionaryInputStream.close();
		
		// otwarcie pliku ze znakami podstawowymi
		FileInputStream radicalInputStream = new FileInputStream(radicalFilePath);

		// wczytywanie pliku ze znakami podstawowymi
		List<RadicalInfo> radicalInfoList = readRadicalEntriesFromCsv(radicalInputStream);

		radicalInputStream.close();

		// otwarcie pliku ze znakami kanji
		FileInputStream kanjiInputStream = new FileInputStream(kanjiFilePath);

		// wczytywanie pliku ze znakami kanji
		readKanjiDictionaryFile(indexWriter, radicalInfoList, kanjiInputStream);

		kanjiInputStream.close();
		
		// zakonczenie zapisywania indeksu
		indexWriter.close();
				
		System.out.println("DB Generator - done");
	}

	private static void readDictionaryFile(IndexWriter indexWriter, InputStream dictionaryInputStream) throws IOException,
			DictionaryException, SQLException {

		KeigoHelper keigoHelper = new KeigoHelper();

		CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');

		while (csvReader.readRecord()) {

			String idString = csvReader.get(0);
			String dictionaryEntryTypeString = csvReader.get(1);
			String attributesString = csvReader.get(2);
			String groupsString = csvReader.get(4);
			String prefixKanaString = csvReader.get(5);
			String kanjiString = csvReader.get(6);

			String kanaListString = csvReader.get(7);
			String prefixRomajiString = csvReader.get(8);

			String romajiListString = csvReader.get(9);
			String translateListString = csvReader.get(10);
			String infoString = csvReader.get(11);

			DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, attributesString,
					groupsString, prefixKanaString, kanjiString, kanaListString, prefixRomajiString, romajiListString,
					translateListString, infoString);

			// count form for dictionary entry
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache = new HashMap<GrammaFormConjugateResultType, GrammaFormConjugateResult>();

			GrammaConjugaterManager.getGrammaConjufateResult(keigoHelper, entry, grammaFormCache, null);

			for (DictionaryEntryType currentDictionaryEntryType : entry.getDictionaryEntryTypeList()) {
				GrammaConjugaterManager.getGrammaConjufateResult(keigoHelper, entry, grammaFormCache,
						currentDictionaryEntryType);
			}

			ExampleManager.getExamples(keigoHelper, entry, grammaFormCache, null);

			for (DictionaryEntryType currentDictionaryEntryType : entry.getDictionaryEntryTypeList()) {
				ExampleManager.getExamples(keigoHelper, entry, grammaFormCache, currentDictionaryEntryType);
			}

			addDictionaryEntry(indexWriter, entry);
		}

		csvReader.close();
	}

	private static void addDictionaryEntry(IndexWriter indexWriter, DictionaryEntry dictionaryEntry) throws IOException {
		
		Document document = new Document();
		
		// object type
		document.add(new Field(LuceneStatic.objectType, LuceneStatic.dictionaryEntry_objectType, Field.Store.YES, Index.NOT_ANALYZED));
		
		// id
		NumericField idField = new NumericField(LuceneStatic.dictionaryEntry_id, Field.Store.YES, true);
		idField.setIntValue(dictionaryEntry.getId());
		
		document.add(idField);
		
		// dictionary entry type list
		List<String> dictionaryEntryTypeStringList = DictionaryEntryType.convertToValues(dictionaryEntry.getDictionaryEntryTypeList());
		
		for (String dictionaryEntryTypeString : dictionaryEntryTypeStringList) {
			document.add(new Field(LuceneStatic.dictionaryEntry_dictionaryEntryTypeList, dictionaryEntryTypeString, Field.Store.YES, Index.NOT_ANALYZED));
		}
				
		// attributeList
		List<String> attributeStringList = dictionaryEntry.getAttributeList().convertAttributeListToListString();
		
		for (String currentAttribute : attributeStringList) {
			document.add(new Field(LuceneStatic.dictionaryEntry_attributeList, currentAttribute, Field.Store.YES, Index.NOT_ANALYZED));
		}
		
		// groupsList
		List<String> groupsList = GroupEnum.convertToValues(dictionaryEntry.getGroups());
		
		for (String currentGroup : groupsList) {
			document.add(new Field(LuceneStatic.dictionaryEntry_groupsList, currentGroup, Field.Store.YES, Index.NOT_ANALYZED));
		}
		
		// prefixKana
		document.add(new Field(LuceneStatic.dictionaryEntry_prefixKana, emptyIfNull(dictionaryEntry.getPrefixKana()), Field.Store.YES, Index.NOT_ANALYZED));
		
		// kanji
		document.add(new Field(LuceneStatic.dictionaryEntry_kanji, emptyIfNull(dictionaryEntry.getKanji()), Field.Store.YES, Index.ANALYZED));
		
		// kanaList
		List<String> kanaList = dictionaryEntry.getKanaList();
		
		for (String currentKana : kanaList) {
			document.add(new Field(LuceneStatic.dictionaryEntry_kanaList, currentKana, Field.Store.YES, Index.ANALYZED));
		}
		
		// prefixRomaji
		document.add(new Field(LuceneStatic.dictionaryEntry_prefixRomaji, emptyIfNull(dictionaryEntry.getPrefixRomaji()), Field.Store.YES, Index.NOT_ANALYZED));
		
		// romajiList
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		for (String currentRomaji : romajiList) {
			document.add(new Field(LuceneStatic.dictionaryEntry_romajiList, currentRomaji, Field.Store.YES, Index.ANALYZED));
		}
		
		// translatesList
		List<String> translates = dictionaryEntry.getTranslates();
		
		for (String currentTranslate : translates) {
			
			document.add(new Field(LuceneStatic.dictionaryEntry_translatesList, currentTranslate, Field.Store.YES, Index.ANALYZED));
			
			if (Utils.containsPolishChars(currentTranslate) == true) {
				String currentTranslateWithoutPolishChars = Utils.removePolishChars(currentTranslate);
				
				document.add(new Field(LuceneStatic.dictionaryEntry_translatesListWithoutPolishChars, currentTranslateWithoutPolishChars, Field.Store.YES, Index.ANALYZED));
			}
		}
		
		// info
		String info = emptyIfNull(dictionaryEntry.getInfo());
		
		document.add(new Field(LuceneStatic.dictionaryEntry_info, info, Field.Store.YES, Index.ANALYZED));
		
		if (Utils.containsPolishChars(info) == true) {
			
			String infoWithoutPolishChars = Utils.removePolishChars(info);
			
			document.add(new Field(LuceneStatic.dictionaryEntry_infoWithoutPolishChars, infoWithoutPolishChars, Field.Store.YES, Index.ANALYZED));
		}
		
		indexWriter.addDocument(document);
	}

	private static List<RadicalInfo> readRadicalEntriesFromCsv(InputStream radicalInputStream) throws IOException,
			DictionaryException {

		List<RadicalInfo> radicalList = new ArrayList<RadicalInfo>();

		CsvReader csvReader = new CsvReader(new InputStreamReader(radicalInputStream), ',');

		while (csvReader.readRecord()) {

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

		return radicalList;
	}

	private static void readKanjiDictionaryFile(IndexWriter indexWriter, List<RadicalInfo> radicalInfoList,
			InputStream kanjiInputStream) throws IOException, DictionaryException, SQLException {

		Map<String, RadicalInfo> radicalListMapCache = new HashMap<String, RadicalInfo>();

		for (RadicalInfo currentRadicalInfo : radicalInfoList) {

			String radical = currentRadicalInfo.getRadical();

			radicalListMapCache.put(radical, currentRadicalInfo);
		}

		CsvReader csvReader = new CsvReader(new InputStreamReader(kanjiInputStream), ',');

		while (csvReader.readRecord()) {

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
					Utils.parseStringIntoList(radicalsString, false),
					Utils.parseStringIntoList(onReadingString, false),
					Utils.parseStringIntoList(kunReadingString, false), strokePathString,
					Utils.parseStringIntoList(polishTranslateListString, false), infoString, generatedString,
					Utils.parseStringIntoList(groupString, false));

			// update radical info
			if (entry.getKanjiDic2Entry() != null) {
				updateRadicalInfoUse(radicalListMapCache, entry.getKanjiDic2Entry().getRadicals());
			}

			// add
			addKanjiEntry(indexWriter, entry);
		}

		csvReader.close();
	}

	private static void updateRadicalInfoUse(Map<String, RadicalInfo> radicalListMapCache, List<String> radicals) {

		for (String currentRadical : radicals) {

			RadicalInfo currentRadicalInfo = radicalListMapCache.get(currentRadical);

			if (currentRadicalInfo == null) {
				throw new RuntimeException("currentRadicalInfo == null: " + currentRadical);
			}

			//currentRadicalInfo.incrementUse();			
		}
	}

	public static void addKanjiEntry(IndexWriter indexWriter, KanjiEntry kanjiEntry) throws IOException {

		Document document = new Document();
		
		// object type
		document.add(new Field(LuceneStatic.objectType, LuceneStatic.kanjiEntry_objectType, Field.Store.YES, Index.NOT_ANALYZED));
		
		// id
		NumericField idField = new NumericField(LuceneStatic.kanjiEntry_id, Field.Store.YES, true);
		idField.setIntValue(kanjiEntry.getId());
		
		document.add(idField);

		// kanji
		document.add(new Field(LuceneStatic.kanjiEntry_kanji, emptyIfNull(kanjiEntry.getKanji()), Field.Store.YES, Index.ANALYZED));
		
		// polishTranslatesList
		List<String> polishtranslatesList = kanjiEntry.getPolishTranslates();
		
		for (String currentTranslate : polishtranslatesList) {
			
			document.add(new Field(LuceneStatic.kanjiEntry_polishTranslatesList, currentTranslate, Field.Store.YES, Index.ANALYZED));
			
			if (Utils.containsPolishChars(currentTranslate) == true) {
				String currentTranslateWithoutPolishChars = Utils.removePolishChars(currentTranslate);
				
				document.add(new Field(LuceneStatic.kanjiEntry_infoWithoutPolishChars, currentTranslateWithoutPolishChars, Field.Store.YES, Index.ANALYZED));
			}
		}
		
		// info
		String info = emptyIfNull(kanjiEntry.getInfo());
		
		document.add(new Field(LuceneStatic.kanjiEntry_info, info, Field.Store.YES, Index.ANALYZED));
		
		if (Utils.containsPolishChars(info) == true) {
			
			String infoWithoutPolishChars = Utils.removePolishChars(info);
			
			document.add(new Field(LuceneStatic.kanjiEntry_infoWithoutPolishChars, infoWithoutPolishChars, Field.Store.YES, Index.ANALYZED));
		}

		// generated
		document.add(new Field(LuceneStatic.kanjiEntry_generated, String.valueOf(kanjiEntry.isGenerated()), Field.Store.YES, Index.NOT_ANALYZED));
				
		// groupsList
		List<String> groupsList = GroupEnum.convertToValues(kanjiEntry.getGroups());
		
		for (String currentGroup : groupsList) {
			document.add(new Field(LuceneStatic.kanjiEntry_groupsList, currentGroup, Field.Store.YES, Index.NOT_ANALYZED));
		}
		
		KanjiDic2Entry kanjiDic2Entry = kanjiEntry.getKanjiDic2Entry();

		if (kanjiDic2Entry != null) {

			// kanjiDic2Entry_strokeCount
			NumericField strokeCountField = new NumericField(LuceneStatic.kanjiEntry_kanjiDic2Entry_strokeCount, Field.Store.YES, true);
			strokeCountField.setIntValue(kanjiDic2Entry.getStrokeCount());
			
			document.add(strokeCountField);
			
			// kanjiDic2Entry_onReadingList
			List<String> onReadingList = kanjiDic2Entry.getOnReading();
			
			for (String currentOnReading : onReadingList) {
				document.add(new Field(LuceneStatic.kanjiEntry_kanjiDic2Entry_onReadingList, currentOnReading, Field.Store.YES, Index.ANALYZED));
			}

			// kanjiDic2Entry_kunReadingList
			List<String> kunReadingList = kanjiDic2Entry.getKunReading();
			
			for (String currentKunReading : kunReadingList) {
				document.add(new Field(LuceneStatic.kanjiEntry_kanjiDic2Entry_kunReadingList, currentKunReading, Field.Store.YES, Index.ANALYZED));
			}
			
			// kanjiDic2Entry_radicalsList
			List<String> radicalsList = kanjiDic2Entry.getRadicals();
			
			for (String currentRadical : radicalsList) {
				document.add(new Field(LuceneStatic.kanjiEntry_kanjiDic2Entry_radicalsList, currentRadical, Field.Store.YES, Index.ANALYZED));
			}
			
			// kanjiDic2Entry_jlpt
			Integer jlpt = kanjiDic2Entry.getJlpt();
			
			if (jlpt != null) {
				NumericField jlptField = new NumericField(LuceneStatic.kanjiEntry_kanjiDic2Entry_jlpt, Field.Store.YES, true);
				jlptField.setIntValue(jlpt);
				
				document.add(jlptField);
			}			
			
			// kanjiDic2Entry_freq
			Integer freq = kanjiDic2Entry.getFreq();
			
			if (freq != null) {
				NumericField freqField = new NumericField(LuceneStatic.kanjiEntry_kanjiDic2Entry_freq, Field.Store.YES, true);
				freqField.setIntValue(freq);

				document.add(freqField);
			}
		}
		
		KanjivgEntry kanjivgEntry = kanjiEntry.getKanjivgEntry();
		
		if (kanjivgEntry != null) {
			
			// kanjivgEntry_strokePaths
			List<String> strokePaths = kanjivgEntry.getStrokePaths();
						
			for (String currentStrokePath : strokePaths) {
				document.add(new Field(LuceneStatic.kanjiEntry_kanjivgEntry_strokePaths, currentStrokePath, Field.Store.YES, Index.NOT_ANALYZED));
			}
		}

		indexWriter.addDocument(document);		
	}
	
	private static String emptyIfNull(String text) {
		if (text == null) {
			return "";
		}

		return text;
	}
}
