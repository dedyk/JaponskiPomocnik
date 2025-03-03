package pl.idedyk.android.japaneselearnhelper.context;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.utils.EntryOrderList;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjiRecognizerResultItem;

public class JapaneseAndroidLearnHelperKanjiTestContext {

	private EntryOrderList<KanjiEntry> kanjiEntryList;
	
	private EntryOrderList<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji;
		
	private List<TestAnswer> testAnswers;
	
	private int correctAnswers = 0;
	
	private int incorrectAnswers = 0;
	
	// methods
	
	public void resetTest() {
		kanjiEntryList = null;
		dictionaryEntryWithRemovedKanji = null;
				
		testAnswers = new ArrayList<TestAnswer>();
		
		correctAnswers = 0;
		incorrectAnswers = 0;
	}

	public EntryOrderList<KanjiEntry> getKanjiEntryList() {
		return kanjiEntryList;
	}

	public void setKanjiEntryList(EntryOrderList<KanjiEntry> kanjiEntryList) {
		this.kanjiEntryList = kanjiEntryList;
	}
	
	public EntryOrderList<DictionaryEntryWithRemovedKanji> getDictionaryEntryWithRemovedKanji() {
		return dictionaryEntryWithRemovedKanji;
	}

	public void setDictionaryEntryWithRemovedKanji(EntryOrderList<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji) {
		this.dictionaryEntryWithRemovedKanji = dictionaryEntryWithRemovedKanji;
	}

	public List<TestAnswer> getTestAnswers() {
		return testAnswers;
	}
	
	public void incrementCorrectAnswers() {
		correctAnswers++;
	}
	
	public void incrementIncorrectAnswers() {
		incorrectAnswers++;
	}

	public int getCorrectAnswers() {
		return correctAnswers;
	}

	public int getIncorrectAnswers() {
		return incorrectAnswers;
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

		@Override
		public String toString() {
			return "DictionaryEntryWithRemovedKanji [dictionaryEntry="
					+ dictionaryEntry + ", removedKanji=" + removedKanji + "]";
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
		
		private String chosenKanji;

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

		public String getChosenKanji() {
			return chosenKanji;
		}

		public void setChosenKanji(String chosenKanji) {
			this.chosenKanji = chosenKanji;
		}
	}
}
