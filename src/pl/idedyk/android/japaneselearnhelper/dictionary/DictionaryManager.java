package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import com.csvreader.CsvReader;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;

public class DictionaryManager {
	
	private static final String FILE_WORD = "/word.csv";

	private static DictionaryManager instance;
	
	public static DictionaryManager getInstance() {
		
		if (instance == null) {
			instance = new DictionaryManager();
		}
		
		return instance;
	}
	
	private List<DictionaryEntry> wordDictionaryEntries = null;
	
	private DictionaryManager() {
		
		// read word csv file
		wordDictionaryEntries = new ArrayList<DictionaryEntry>();
		
		InputStream fileWordInputStream = DictionaryManager.class.getResourceAsStream(FILE_WORD);
		
		try {
			readDictionaryFile(fileWordInputStream, wordDictionaryEntries);
		} catch (IOException e) {
			throw new RuntimeException(e);
		} catch (DictionaryException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	private void readDictionaryFile(InputStream dictionaryInputStream, List<DictionaryEntry> dictionary) throws IOException, DictionaryException {
				
		CsvReader csvReader = new CsvReader(new InputStreamReader(dictionaryInputStream), ',');
		
		while(csvReader.readRecord()) {
			
			String dictionaryEntryType = csvReader.get(0);
			String prefixString = csvReader.get(2);
			String kanjiString = csvReader.get(3);
			
			if (kanjiString.equals("") == true) {
				throw new DictionaryException("Empty kanji!");
			}
			
			String kanaListString = csvReader.get(5);
			String translateListString = csvReader.get(7);
			String infoString = csvReader.get(8);
			
			DictionaryEntry entry = new DictionaryEntry();
			
			entry.setDictionaryEntryType(DictionaryEntryType.valueOf(dictionaryEntryType));
			entry.setPrefix(prefixString);
			entry.setKanji(kanjiString);
			entry.setKanaList(parseStringIntoList(kanaListString));
			entry.setTranslates(parseStringIntoList(translateListString));
			
			entry.setInfo(infoString);
			
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
		
		return result;		
	}
	
	public List<DictionaryEntry> getWordDictionaryEntries() {
		return wordDictionaryEntries;
	}
}
