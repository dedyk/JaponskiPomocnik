package pl.idedyk.android.japaneselearnhelper.dbgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.lucene.analysis.ja.JapaneseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.IntField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.TopScoreDocCollector;
import org.apache.lucene.search.similarities.PerFieldSimilarityWrapper;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.MMapDirectory;
import org.apache.lucene.store.RAMDirectory;
import org.apache.lucene.util.Version;

import pl.idedyk.android.japaneselearnhelper.dictionary.Utils;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.AttributeType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.GroupEnum;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.RadicalInfo;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;

import com.csvreader.CsvReader;

public class LuceneGenerator {
	
	// test

	public static void main(String[] args) throws Exception {

		// init db driver
		Class.forName("org.sqlite.JDBC");

		// file params
		final String dictionaryFilePath = "db/word.csv";
		final String kanjiFilePath = "db/kanji.csv";
		final String radicalFilePath = "db/radical.csv";

		final String dbOutputFilePath = "assets/dictionary.db";
		
		// file db output file
		new File(dbOutputFilePath).delete();
		
		// open db
		JapaneseAnalyzer analyzer = new JapaneseAnalyzer(Version.LUCENE_40);

		// 1. create the index
		Directory index = new MMapDirectory(new File("lucene-db-test"));

		IndexWriterConfig indexWriterConfig = new IndexWriterConfig(Version.LUCENE_40, analyzer);

		IndexWriter indexWriter = new IndexWriter(index, indexWriterConfig);

		
		
		try {


			// dictionary file input stream
			FileInputStream dictionaryInputStream = new FileInputStream(dictionaryFilePath);
			
			// read and write dictionary
			readDictionaryFile(indexWriter, dictionaryInputStream);
			
			dictionaryInputStream.close();
			
			indexWriter.close(); // FIXME !!!!!!
			
			String querystr = "おか"; //"lucene";

			// the "title" arg specifies the default field to use
			// when no field is explicitly specified in the query.
			Query q = new QueryParser(Version.LUCENE_40, "kana", analyzer).parse(querystr);

			// 3. search
			int hitsPerPage = 10;
			IndexReader reader = DirectoryReader.open(index);
			IndexSearcher searcher = new IndexSearcher(reader);
			TopScoreDocCollector collector = TopScoreDocCollector.create(hitsPerPage, true);
			searcher.search(q, collector);
			ScoreDoc[] hits = collector.topDocs().scoreDocs;

			// 4. display results
			System.out.println("Found " + hits.length + " hits.");
			for(int i=0;i<hits.length;++i) {
				int docId = hits[i].doc;
				Document d = searcher.doc(docId);
				//System.out.println((i + 1) + ". " + d.get("isbn") + "\t" + d.getFields("title")[0].stringValue() + "\t" + d.getFields("title")[1].stringValue());
				
				System.out.println(d);
			}

			// reader can only be closed when there
			// is no need to access the documents any more.
			reader.close();
			
			/*
			// radical file input stream
			FileInputStream radicalInputStream = new FileInputStream(radicalFilePath);
			
			// read radical entries
			List<RadicalInfo> radicalInfoList = readRadicalEntriesFromCsv(radicalInputStream);
			
			radicalInputStream.close();
			
			// kanji file input stream
			FileInputStream kanjiInputStream = new FileInputStream(kanjiFilePath);
			
			// read kanji and write
			readKanjiDictionaryFile(statement, radicalInfoList, kanjiInputStream);
			
			kanjiInputStream.close();
			
			// commit			
			connection.commit();
			
			connection.setAutoCommit(true);
			
			// vacuum
			statement.execute("vacuum");
			*/
			
		} finally {
			/*
			if (statement != null) {
				statement.close();
			}
			
			if (connection != null) {
				connection.close();
			}
			*/		
		}	
		
		System.out.println("Lucene Generator - done");
	}
		
	private static void readDictionaryFile(IndexWriter indexWriter, InputStream dictionaryInputStream) throws IOException, DictionaryException, Exception { // FIXME !!!!!!!!!!!

		CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');

		while(csvReader.readRecord()) {

			String idString = csvReader.get(0);
			String dictionaryEntryTypeString = csvReader.get(2);
			String attributesString = csvReader.get(3);
			String groupsString = csvReader.get(5);
			String prefixKanaString = csvReader.get(6);
			String kanjiString = csvReader.get(7);

			String kanaListString = csvReader.get(8);
			String prefixRomajiString = csvReader.get(9);

			String romajiListString = csvReader.get(10);
			String translateListString = csvReader.get(11);
			String infoString = csvReader.get(12);

			DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, attributesString, groupsString,
					prefixKanaString, kanjiString, kanaListString, prefixRomajiString,
					romajiListString, translateListString, infoString);
			
			insertDictionaryEntry(indexWriter, entry);
		}
		
		csvReader.close();		
	}
	
	/*
	private static void addDoc(IndexWriter w, String title, String isbn) throws IOException {
		
		doc.add(new TextField("title", title, Field.Store.YES));

		doc.add(new TextField("title", title + " aaaa", Field.Store.YES));
		
		// use a string field for isbn because we don't want it tokenized
		doc.add(new StringField("isbn", isbn, Field.Store.YES));
		w.addDocument(doc);
	}
	*/
	
	private static void insertDictionaryEntry(IndexWriter indexWriter, DictionaryEntry dictionaryEntry) throws Exception { // FIXME !!!!!!!!!!!
		
		Document document = new Document();
				
		document.add(new IntField("id", dictionaryEntry.getId(), Field.Store.YES));
		
		List<String> translates = dictionaryEntry.getTranslates();
		
		for (String currentTranslate : translates) {
			document.add(new TextField("translate", currentTranslate, Field.Store.YES));
		}
		
		List<String> kanaList = dictionaryEntry.getKanaList();

		for (String currentKana : kanaList) {
			document.add(new TextField("kana", currentKana, Field.Store.YES));
		}

		
		indexWriter.addDocument(document);
		
		/*
		Map<String, String> values = new TreeMap<String, String>();
		
		!!!!!!!! id !!!!!!!
		values.put(SQLiteStatic.dictionaryEntriesTable_dictionaryEntryType, dictionaryEntry.getDictionaryEntryType().toString());
		values.put(SQLiteStatic.dictionaryEntriesTable_prefixKana, emptyIfNull(dictionaryEntry.getPrefixKana()));
		values.put(SQLiteStatic.dictionaryEntriesTable_kanji, emptyIfNull(dictionaryEntry.getKanji()));
		values.put(SQLiteStatic.dictionaryEntriesTable_prefixRomaji, emptyIfNull(dictionaryEntry.getPrefixRomaji()));
		values.put(SQLiteStatic.dictionaryEntriesTable_info, emptyIfNull(dictionaryEntry.getInfo()));		
		
		insert(statement, SQLiteStatic.dictionaryEntriesTableName, values);
		
		insertListEntry(statement, AttributeType.convertToValues(dictionaryEntry.getAttributeList()), SQLiteStatic.dictionaryEntriesTableName, 
				SQLiteStatic.dictionaryEntriesTable_attributeList, String.valueOf(dictionaryEntry.getId()));
		
		insertListEntry(statement, GroupEnum.convertToValues(dictionaryEntry.getGroups()), SQLiteStatic.dictionaryEntriesTableName, 
				SQLiteStatic.dictionaryEntriesTable_groups, String.valueOf(dictionaryEntry.getId()));
		
		insertListEntry(statement, dictionaryEntry.getKanaList(), SQLiteStatic.dictionaryEntriesTableName, 
				SQLiteStatic.dictionaryEntriesTable_kanaList, String.valueOf(dictionaryEntry.getId()));

		insertListEntry(statement, dictionaryEntry.getRomajiList(), SQLiteStatic.dictionaryEntriesTableName, 
				SQLiteStatic.dictionaryEntriesTable_romajiList, String.valueOf(dictionaryEntry.getId()));
		
		insertListEntry(statement, dictionaryEntry.getTranslates(), SQLiteStatic.dictionaryEntriesTableName, 
				SQLiteStatic.dictionaryEntriesTable_translates, String.valueOf(dictionaryEntry.getId()));
		
		insertListEntryWithPolishCharsRemove(statement, dictionaryEntry.getTranslates(), SQLiteStatic.dictionaryEntriesTableName, 
				SQLiteStatic.dictionaryEntriesTable_translates, String.valueOf(dictionaryEntry.getId()));

		String info = dictionaryEntry.getInfo();
		
		if (info != null && info.equals("") == false) {
			
			List<String> infoList = new ArrayList<String>();
			
			infoList.add(info);

			insertListEntry(statement, infoList, SQLiteStatic.dictionaryEntriesTableName, 
					SQLiteStatic.dictionaryEntriesTable_info, String.valueOf(dictionaryEntry.getId()));
			
			insertListEntryWithPolishCharsRemove(statement, infoList, SQLiteStatic.dictionaryEntriesTableName, 
					SQLiteStatic.dictionaryEntriesTable_info, String.valueOf(dictionaryEntry.getId()));					
		}
		*/
	}
	
	/*
	private static void insert(Statement statement, String tableName, Map<String, String> values) throws SQLException {
		
		StringBuffer sqlStart = new StringBuffer();
		
		sqlStart.append("INSERT INTO ").append(tableName).append(" ");
		
		StringBuffer columns = new StringBuffer();
		StringBuffer columnsValue = new StringBuffer();
		
		Iterator<String> keySetIterator = values.keySet().iterator();
		
		boolean isFirstColumn = true;
		
		while(keySetIterator.hasNext()) {
			
			String currentColumn = keySetIterator.next();
			
			if (isFirstColumn == true) {
				columns.append("(");
				columnsValue.append("(");
				
				isFirstColumn = false;
				
			} else {
				columns.append(", ");
				columnsValue.append(", ");
			}
			
			columns.append(currentColumn);
			
			String currentColumnValue = values.get(currentColumn);
			
			currentColumnValue = currentColumnValue.replaceAll("\\'", "''");
			
			columnsValue.append("'").append(currentColumnValue).append("'");			
		}
		
		columns.append(")");
		columnsValue.append(")");
		
		String sql = sqlStart.toString() + columns.toString() + " values " + columnsValue.toString();
		
		statement.execute(sql);		
	}
	*/
	
	/*
	private static void insertListEntry(Statement statement, List<String> list, String type, String subType, String key) throws SQLException {
		
		if (list == null || list.size() == 0) {
			return;
		}
		
		for (String currentListValue : list) {
			Map<String, String> values = new TreeMap<String, String>();
			
			values.put(SQLiteStatic.listEntriesTable_type, type);
			values.put(SQLiteStatic.listEntriesTable_subType, subType);
			values.put(SQLiteStatic.listEntriesTable_key, key);
			values.put(SQLiteStatic.listEntriesTable_value, currentListValue);
			values.put(SQLiteStatic.listEntriesTable_special, "0");
			
			insert(statement, SQLiteStatic.listEntriesTableName, values);
		}
	}
	
	private static void insertListEntryWithPolishCharsRemove(Statement statement, List<String> list, String type, String subType, String key) throws SQLException {
		
		if (list == null || list.size() == 0) {
			return;
		}
		
		for (String currentListValue : list) {
			
			if (Utils.containsPolishChars(currentListValue) == false) {
				continue;
			}			
			
			Map<String, String> values = new TreeMap<String, String>();
			
			values.put(SQLiteStatic.listEntriesTable_type, type);
			values.put(SQLiteStatic.listEntriesTable_subType, subType);
			values.put(SQLiteStatic.listEntriesTable_key, key);
			values.put(SQLiteStatic.listEntriesTable_value, Utils.removePolishChars(currentListValue));
			values.put(SQLiteStatic.listEntriesTable_special, "1");
			
			insert(statement, SQLiteStatic.listEntriesTableName, values);
		}
	}
	*/
	
	/*
	private static List<RadicalInfo> readRadicalEntriesFromCsv(InputStream radicalInputStream) throws IOException, DictionaryException {

		List<RadicalInfo> radicalList = new ArrayList<RadicalInfo>();

		CsvReader csvReader = new CsvReader(new InputStreamReader(radicalInputStream), ',');

		while(csvReader.readRecord()) {			

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
	
	private static void readKanjiDictionaryFile(Statement statement, List<RadicalInfo> radicalInfoList, InputStream kanjiInputStream) throws IOException, DictionaryException, SQLException {

		Map<String, RadicalInfo> radicalListMapCache = new HashMap<String, RadicalInfo>();

		for (RadicalInfo currentRadicalInfo : radicalInfoList) {

			String radical = currentRadicalInfo.getRadical();

			radicalListMapCache.put(radical, currentRadicalInfo);
		}

		CsvReader csvReader = new CsvReader(new InputStreamReader(kanjiInputStream), ',');
		
		while(csvReader.readRecord()) {
			
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
			insertKanjiEntry(statement, entry);
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
	
	public static void insertKanjiEntry(KanjiEntry kanjiEntry) throws Exception { // FIXME !!!!!!!!!!!!!!!!!!!
				
		Map<String, String> values = new TreeMap<String, String>();
		
		values.put(SQLiteStatic.kanjiEntriesTable_id, String.valueOf(kanjiEntry.getId()));
		values.put(SQLiteStatic.kanjiEntriesTable_kanji, kanjiEntry.getKanji());
		
		KanjiDic2Entry kanjiDic2Entry = kanjiEntry.getKanjiDic2Entry();
		
		if (kanjiDic2Entry != null) {
			values.put(SQLiteStatic.kanjiEntriesTable_strokeCount, String.valueOf(kanjiDic2Entry.getStrokeCount()));
			values.put(SQLiteStatic.kanjiEntriesTable_radicals, Utils.convertListToString(kanjiDic2Entry.getRadicals()));
			values.put(SQLiteStatic.kanjiEntriesTable_onReading, Utils.convertListToString(kanjiDic2Entry.getOnReading()));
			values.put(SQLiteStatic.kanjiEntriesTable_kunReading, Utils.convertListToString(kanjiDic2Entry.getKunReading()));
		} else {
			values.put(SQLiteStatic.kanjiEntriesTable_strokeCount, "");
			values.put(SQLiteStatic.kanjiEntriesTable_radicals, "");
			values.put(SQLiteStatic.kanjiEntriesTable_onReading, "");
			values.put(SQLiteStatic.kanjiEntriesTable_kunReading, "");			
		}
		
		values.put(SQLiteStatic.kanjiEntriesTable_strokePaths, Utils.convertListToString(kanjiEntry.getStrokePaths()));
		values.put(SQLiteStatic.kanjiEntriesTable_polishTranslates, Utils.convertListToString(kanjiEntry.getPolishTranslates()));
		values.put(SQLiteStatic.kanjiEntriesTable_info, emptyIfNull(kanjiEntry.getInfo()));
		values.put(SQLiteStatic.kanjiEntriesTable_generated, String.valueOf(kanjiEntry.isGenerated()));
		values.put(SQLiteStatic.kanjiEntriesTable_groups, Utils.convertListToString(GroupEnum.convertToValues(kanjiEntry.getGroups())));
		
		insert(statement, SQLiteStatic.kanjiEntriesTableName, values);
	}
	*/
	
	private static String emptyIfNull(String text) {
		if (text == null) {
			return "";
		}
		
		return text;
	}	
}
