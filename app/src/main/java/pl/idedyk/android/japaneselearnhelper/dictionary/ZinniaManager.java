package pl.idedyk.android.japaneselearnhelper.dictionary;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.xdump.android.zinnia.Zinnia;

import pl.idedyk.japanese.dictionary.api.dto.KanjiRecognizerResultItem;

public class ZinniaManager {
	
	private File kanjiRecognizeModelDbFile = null;
	
	private Zinnia zinnia = new Zinnia();
	
	private long zinniaHandler = 0;
	
	ZinniaManager(File kanjiRecognizeModelDbFile) {
		this.kanjiRecognizeModelDbFile = kanjiRecognizeModelDbFile;
	}
	
	void copyKanjiRecognizeModelToData(InputStream kanjiRecognizeModelInputStream, ILoadWithProgress loadWithProgress) throws IOException {
		
		BufferedOutputStream kanjiRecognizerModelDbOutputStream = null;

		kanjiRecognizerModelDbOutputStream = new BufferedOutputStream(new FileOutputStream(kanjiRecognizeModelDbFile));

		byte[] buffer = new byte[8096];
		
		int read;  
		
		while ((read = kanjiRecognizeModelInputStream.read(buffer)) != -1) {  
			kanjiRecognizerModelDbOutputStream.write(buffer, 0, read);  
		}  

		kanjiRecognizeModelInputStream.close();
		kanjiRecognizerModelDbOutputStream.close();
		
		loadWithProgress.setCurrentPos(1);
	}

	public File getKanjiRecognizeModelDbFile() {
		return kanjiRecognizeModelDbFile;
	}
	
	public void open() {
		
		if (zinniaHandler == 0) {
			
			zinniaHandler = zinnia.zinnia_recognizer_new();
			
			int zinniaRecognizerOpenResult = zinnia.zinnia_recognizer_open(zinniaHandler, kanjiRecognizeModelDbFile.getAbsolutePath());
			
			if (zinniaRecognizerOpenResult == 0) {
				throw new RuntimeException("Can't open zinnia db: " + zinnia.zinnia_recognizer_strerror(zinniaHandler));
			}
		}
	}
	
	public Character createNewCharacter() {
		
		open();
		
		return new Character();		
	}
	
	public class Character {
		
		private long character;
		
		private Character() {
			character = zinnia.zinnia_character_new();
		}
		
		public void clear() {
			zinnia.zinnia_character_clear(character);
		}
		
		public void setWidth(int width) {
			zinnia.zinnia_character_set_width(character, width);
		}
		
		public void setHeight(int width) {
			zinnia.zinnia_character_set_height(character, width);
		}
		
		public void add(int strokeNo, int x, int y) {
			zinnia.zinnia_character_add(character, strokeNo, x, y);
		}
		
		public List<KanjiRecognizerResultItem> recognize(int limit) {
			
			List<KanjiRecognizerResultItem> result = new ArrayList<KanjiRecognizerResultItem>();
			
			long recognizerResult = zinnia.zinnia_recognizer_classify(zinniaHandler, character, limit);

			if (recognizerResult != 0) {
				for (int i = 0; i < zinnia.zinnia_result_size(recognizerResult); ++i) {	
					result.add(new KanjiRecognizerResultItem(zinnia.zinnia_result_value_from_int_array_to_string(recognizerResult, i), zinnia.zinnia_result_score(recognizerResult, i)));
				}
				
				zinnia.zinnia_result_destroy(recognizerResult);
			}
			
			return result;
		}
		
		public void destroy() {
			zinnia.zinnia_character_destroy(character);
		}
	}
}
