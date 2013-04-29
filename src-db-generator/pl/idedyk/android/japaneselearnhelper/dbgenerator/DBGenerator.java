package pl.idedyk.android.japaneselearnhelper.dbgenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import pl.idedyk.android.japaneselearnhelper.dictionary.KeigoHelper;
import pl.idedyk.android.japaneselearnhelper.dictionary.SQLiteStatic;
import pl.idedyk.android.japaneselearnhelper.dictionary.Utils;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.AttributeType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.GroupEnum;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.RadicalInfo;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;
import pl.idedyk.android.japaneselearnhelper.example.ExampleManager;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.GrammaConjugaterManager;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResultType;

import com.csvreader.CsvReader;

public class DBGenerator {

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
		Connection connection = null;
		Statement statement = null;
		
		try {
			connection = DriverManager.getConnection("jdbc:sqlite:" + dbOutputFilePath);
			
			connection.setAutoCommit(false);
			
			// create statement
			statement = connection.createStatement();
			statement.setQueryTimeout(30); // set timeout to 30 sec.

			// create db objects
			statement.execute(SQLiteStatic.listEntriesTableCreate);
			// statement.execute(SQLiteStatic.listEntriesTableCreateAllIndex);
			// statement.execute(SQLiteStatic.listEntriesTableCreateTypeKeyIndex);
			// statement.execute(SQLiteStatic.listEntriesTableCreateTypeIndex);
			// statement.execute(SQLiteStatic.listEntriesTableCreateSubTypeIndex);
			// statement.execute(SQLiteStatic.listEntriesTableCreateKeyIndex);
			// statement.execute(SQLiteStatic.listEntriesTableCreateValueIndex);
			statement.execute(SQLiteStatic.dictionaryEntriesTableCreate);
			statement.execute(SQLiteStatic.kanjiEntriesTableCreate);
			statement.execute(SQLiteStatic.grammaFormConjugateGroupTypeEntriesTableCreate);
			statement.execute(SQLiteStatic.grammaFormConjugateResultEntriesTableNameCreate);
			statement.execute(SQLiteStatic.exampleGroupTypeEntriesTableCreate);
			statement.execute(SQLiteStatic.exampleResultEntriesTableCreate);

			// dictionary file input stream
			FileInputStream dictionaryInputStream = new FileInputStream(dictionaryFilePath);
			
			// read and write dictionary
			readDictionaryFile(statement, dictionaryInputStream);
			
			dictionaryInputStream.close();
			
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
			
		} finally {
			
			if (statement != null) {
				statement.close();
			}
			
			if (connection != null) {
				connection.close();
			}			
		}	
		
		System.out.println("DB Generator - done");
	}
		
	private static void readDictionaryFile(Statement statement, InputStream dictionaryInputStream) throws IOException, DictionaryException, SQLException {

		KeigoHelper keigoHelper = new KeigoHelper();
		
		CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');

		while(csvReader.readRecord()) {

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

			DictionaryEntry entry = Utils.parseDictionaryEntry(idString, dictionaryEntryTypeString, attributesString, groupsString,
					prefixKanaString, kanjiString, kanaListString, prefixRomajiString,
					romajiListString, translateListString, infoString);
			
			// count form for dictionary entry
			Map<GrammaFormConjugateResultType, GrammaFormConjugateResult> grammaFormCache = new HashMap<GrammaFormConjugateResultType, GrammaFormConjugateResult>();
			
			List<GrammaFormConjugateGroupTypeElements> grammaConjufateResult = GrammaConjugaterManager.getGrammaConjufateResult(keigoHelper, entry, grammaFormCache);

			if (grammaConjufateResult != null) {
				
				/*
				for (GrammaFormConjugateGroupTypeElements grammaFormConjugateGroupTypeElements : grammaConjufateResult) {
					System.out.println(grammaFormConjugateGroupTypeElements.getGrammaFormConjugateResults().get(0).getKanji());
				}
				*/					
			}

			List<ExampleGroupTypeElements> examples = ExampleManager.getExamples(keigoHelper, entry, grammaFormCache);

			if (examples != null) {
				
				/*
				for (ExampleGroupTypeElements exampleGroupTypeElements : examples) {
					System.out.println(exampleGroupTypeElements.getExampleResults().get(0).getKanji());
				}
				*/
			}
			
			insertDictionaryEntry(statement, entry);
		}
		
		csvReader.close();		
	}
	
	private static void insertDictionaryEntry(Statement statement, DictionaryEntry dictionaryEntry) throws SQLException {
		
		Map<String, String> values = new TreeMap<String, String>();
		
		values.put(SQLiteStatic.dictionaryEntriesTable_id, String.valueOf(dictionaryEntry.getId()));		
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
	}
	
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
	
	public static void insertKanjiEntry(Statement statement, KanjiEntry kanjiEntry) throws SQLException {
				
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
	
	private static String emptyIfNull(String text) {
		if (text == null) {
			return "";
		}
		
		return text;
	}	
}
