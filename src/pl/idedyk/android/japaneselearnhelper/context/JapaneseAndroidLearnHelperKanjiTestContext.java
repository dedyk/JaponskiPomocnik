package pl.idedyk.android.japaneselearnhelper.context;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiRecognizerResultItem;

public class JapaneseAndroidLearnHelperKanjiTestContext {

	private List<KanjiEntry> kanjiEntryList;
	
	private List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji;
	
	private int currentPos = 0;
	
	private List<TestAnswer> testAnswers;
	
	// methods
	
	public void resetTest() {
		kanjiEntryList = null;
		dictionaryEntryWithRemovedKanji = null;
		testAnswers = new ArrayList<TestAnswer>();
		
		currentPos = 0;
	}

	public List<KanjiEntry> getKanjiEntryList() {
		return kanjiEntryList;
	}

	public void setKanjiEntryList(List<KanjiEntry> kanjiEntryList) {
		this.kanjiEntryList = kanjiEntryList;
	}
	
	public List<DictionaryEntryWithRemovedKanji> getDictionaryEntryWithRemovedKanji() {
		return dictionaryEntryWithRemovedKanji;
	}

	public void setDictionaryEntryWithRemovedKanji(List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji) {
		this.dictionaryEntryWithRemovedKanji = dictionaryEntryWithRemovedKanji;
	}

	public int getCurrentPos() {
		return currentPos;
	}

	public void setCurrentPos(int currentPos) {
		this.currentPos = currentPos;
	}

	public List<TestAnswer> getTestAnswers() {
		return testAnswers;
	}

	public static class DictionaryEntryWithRemovedKanji {
		
		private DictionaryEntry dictionaryEntry;
		
		private String removedKanji;

		public DictionaryEntryWithRemovedKanji(DictionaryEntry dictionaryEntry, String removedKanji) {
			this.dictionaryEntry = dictionaryEntry;
			this.removedKanji = removedKanji;
		}

		public DictionaryEntry getDictionaryEntry() {
			return dictionaryEntry;
		}

		public String getRemovedKanji() {
			return removedKanji;
		}
		
		public String getKanjiWithRemovedKanji() {
			return dictionaryEntry.getKanji().replaceAll(removedKanji, "_");
		}
	}
	
	public static class TestAnswer {
		
		private String kanji;
		
		private int kanjiCorrectStrokeNo;

		private int width;
		
		private int height;
		
		private int kanjiUserStrokeNo;
		
		private String drawStrokesStrings;
		
		private List<KanjiRecognizerResultItem> recognizeResult;
		
		private boolean correctAnswer;

		public String getKanji() {
			return kanji;
		}

		public int getHeight() {
			return height;
		}

		public int getWidth() {
			return width;
		}

		public String getDrawStrokesStrings() {
			return drawStrokesStrings;
		}

		public List<KanjiRecognizerResultItem> getRecognizeResult() {
			return recognizeResult;
		}

		public void setKanji(String kanji) {
			this.kanji = kanji;
		}

		public void setHeight(int height) {
			this.height = height;
		}

		public void setWidth(int width) {
			this.width = width;
		}

		public void setDrawStrokesStrings(String drawStrokesStrings) {
			this.drawStrokesStrings = drawStrokesStrings;
		}

		public void setRecognizeResult(List<KanjiRecognizerResultItem> recognizeResult) {
			this.recognizeResult = recognizeResult;
		}

		public boolean isCorrectAnswer() {
			return correctAnswer;
		}

		public void setCorrectAnswer(boolean correctAnswer) {
			this.correctAnswer = correctAnswer;
		}

		public int getKanjiCorrectStrokeNo() {
			return kanjiCorrectStrokeNo;
		}

		public int getKanjiUserStrokeNo() {
			return kanjiUserStrokeNo;
		}

		public void setKanjiCorrectStrokeNo(int kanjiCorrectStrokeNo) {
			this.kanjiCorrectStrokeNo = kanjiCorrectStrokeNo;
		}

		public void setKanjiUserStrokeNo(int kanjiUserStrokeNo) {
			this.kanjiUserStrokeNo = kanjiUserStrokeNo;
		}
	}
}
