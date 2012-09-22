package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import android.app.Activity;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class KanjiTest extends Activity {
	
	private TextView kanjiInfoTextView;

	private TextView kanjiInfoCorrectTextView;

	private KanjiDrawView drawView;
	
	private TextView kanjiTestState;
	
	private JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext;
	
	private KanjiTestConfig kanjiTestConfig;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.kanji_test);
		
		kanjiTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanjiTestContext();
		
		kanjiTestConfig = ConfigManager.getInstance().getKanjiTestConfig();
		
		kanjiInfoTextView = (TextView)findViewById(R.id.kanji_test_info_textview);
		kanjiInfoCorrectTextView = (TextView)findViewById(R.id.kanji_test_info_correct_textview);

		drawView = (KanjiDrawView) findViewById(R.id.kanji_test_recognizer_draw_view);
		
		kanjiTestState = (TextView) findViewById(R.id.kanji_test_state);

		Button undoButton = (Button) findViewById(R.id.kanji_test_recognizer_undo_button);

		undoButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				drawView.removeLastStroke();
			}
		});

		Button clearButton = (Button) findViewById(R.id.kanji_test_recognizer_clear_button);

		clearButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				drawView.clear();
			}
		});
		
		Button checkButton = (Button) findViewById(R.id.kanji_test_recognizer_check_button);
		
		checkButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				final List<Stroke> strokes = drawView.getStrokes();
				
				if (strokes.size() == 0) {
					
					Toast toast = Toast.makeText(KanjiTest.this, getString(R.string.kanji_test_recognizer_please_draw), Toast.LENGTH_SHORT);
					
					toast.show();
					
					return;
				}
				
				DictionaryManager dictionaryManager = DictionaryManager.getInstance();
				
				ZinniaManager zinniaManager = dictionaryManager.getZinniaManager();
				
				zinniaManager.open();
				
				pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManager.Character zinniaCharacter = zinniaManager.createNewCharacter();
				
				//strokesStringBuffer.append("Height: " + drawView.getHeight()).append("\n");
				//strokesStringBuffer.append("Width: " + drawView.getWidth()).append("\n\n");
				
				zinniaCharacter.clear();
				zinniaCharacter.setHeight(drawView.getHeight());
				zinniaCharacter.setWidth(drawView.getWidth());
				
				for (int idx = 0; idx < strokes.size(); ++idx) {
					
					//strokesStringBuffer.append(String.valueOf((idx + 1))).append(":");
					
					Stroke currentStroke = strokes.get(idx);
					
					List<PointF> currentStrokePoints = currentStroke.getPoints();
					
					for (PointF currentStrokeCurrentPoint : currentStrokePoints) {
						zinniaCharacter.add(idx, (int)currentStrokeCurrentPoint.x, (int)currentStrokeCurrentPoint.y);
						
						//strokesStringBuffer.append(currentStrokeCurrentPoint.x).append(" ").append(currentStrokeCurrentPoint.y).append(";");
					}
					
					// strokesStringBuffer.append("\n\n");
				}
				
				List<KanjiRecognizerResultItem> recognizeResult = zinniaCharacter.recognize(50);
				
				zinniaCharacter.destroy();
				
				KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
				
				String correctKanji = getCurrentTestPosCorrectKanji(kanjiTestContext, kanjiTestMode);
				int correctKanjiStrokeNo = getCurrentTestPosCorrectStrokeNo(kanjiTestContext, kanjiTestMode);
				
				boolean correctAnswer = false;
				
				for (int recognizeResultIdx = 0; recognizeResultIdx < recognizeResult.size() && recognizeResultIdx < 5; ++recognizeResultIdx) {
					
					KanjiRecognizerResultItem currentKanjiRecognizerResultItem = recognizeResult.get(recognizeResultIdx);
					
					String currentKanjiRecognizerResultItemKanji = currentKanjiRecognizerResultItem.getKanji();
					
					if (correctKanji.equals(currentKanjiRecognizerResultItemKanji) == true && strokes.size() == correctKanjiStrokeNo) {
						correctAnswer = true;
					}
				}
				
				if (correctAnswer == true) { // correct
					
					Toast toast = Toast.makeText(KanjiTest.this, getString(R.string.kanji_test_recognizer_correct_answer), Toast.LENGTH_SHORT);
					
					toast.show();
					
					kanjiTestContext.setCurrentPos(kanjiTestContext.getCurrentPos() + 1);
					
					setScreen();
				} else { // incorrect
					
					setErrorScreen();
				}
			}
		});
		
		setScreen();
	}
	
	private void setScreen() {
				
		// if finish
		if (isFinish(kanjiTestContext, kanjiTestConfig.getKanjiTestMode()) == true) {
			
			// FIXME !!!
			
			finish();
			
			return;
		}
		
		
		// set info value
		setInfoValue(kanjiTestContext, kanjiTestConfig);
		
		// draw view clear
		drawView.clear();
	}
	
	private void setErrorScreen() {
		kanjiInfoTextView.setVisibility(View.GONE);
		
		kanjiInfoCorrectTextView.setVisibility(View.VISIBLE);

		
	}
	
	private void setInfoValue(JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext,
			KanjiTestConfig kanjiTestConfig) {
		
		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
		
		Object currentTestPosObject = getCurrentTestPosObject(kanjiTestContext, kanjiTestMode);
		
		kanjiTestState.setText(getString(R.string.kanji_test_state, kanjiTestContext.getCurrentPos() + 1, getMaxTestPos(kanjiTestContext, kanjiTestMode)));
		
		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			
			KanjiEntry currentTestKanjiEntry = (KanjiEntry)currentTestPosObject;
			
			List<String> polishTranslates = currentTestKanjiEntry.getPolishTranslates();
			String info = currentTestKanjiEntry.getInfo();
			
			StringBuffer kanjiInfoSb = new StringBuffer();
			
			kanjiInfoSb.append("<b><big>").append(polishTranslates.toString()).append("</big></b>");
			
			if (info != null && info.equals("") == false) {
				kanjiInfoSb.append(" - ").append(info);
			}
			
			StringBuffer kanjiInfoCorrectSb = new StringBuffer();
			
			kanjiInfoCorrectSb.append("<b><big>").append(currentTestKanjiEntry.getKanji()).append("</big></b>");
			
			kanjiInfoTextView.setText(Html.fromHtml(getString(R.string.kanji_test_info_meaning, kanjiInfoSb.toString())), TextView.BufferType.SPANNABLE);
			kanjiInfoCorrectTextView.setText(Html.fromHtml(getString(R.string.kanji_test_info_correct_meaning, kanjiInfoSb.toString(), kanjiInfoCorrectSb.toString())), TextView.BufferType.SPANNABLE);			
			
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			
			DictionaryEntryWithRemovedKanji currentDictionaryEntryWithRemovedKanji = (DictionaryEntryWithRemovedKanji)currentTestPosObject;
			
			String kanjiWithRemovedKanji = currentDictionaryEntryWithRemovedKanji.getKanjiWithRemovedKanji();
			DictionaryEntry dictionaryEntry = currentDictionaryEntryWithRemovedKanji.getDictionaryEntry();
			
			String kanji = dictionaryEntry.getKanji();
			List<String> kanaList = dictionaryEntry.getKanaList();
			List<String> translates = dictionaryEntry.getTranslates();
			String info = dictionaryEntry.getInfo();
			
			StringBuffer kanjiInfoSb = new StringBuffer();
			StringBuffer kanjiInfoCorrectSb = new StringBuffer();
			
			kanjiInfoSb.append(getString(R.string.kanji_test_info_kanji_in_word)).append("<br/><br/>");
			kanjiInfoCorrectSb.append(getString(R.string.kanji_test_info_correct_kanji_in_word)).append("<br/><br/>");
			
			kanjiInfoSb.append("<b><big>").append(kanjiWithRemovedKanji).append("</big></b>");
			kanjiInfoCorrectSb.append("<b><big>").append(kanji).append("</big></b>");
			
			kanjiInfoSb.append(" ").append(kanaList).append(" - ");
			kanjiInfoCorrectSb.append(" ").append(kanaList).append(" - ");
			
			kanjiInfoSb.append(translates);
			kanjiInfoCorrectSb.append(translates);
			
			if (info != null && info.equals("") == false) {
				kanjiInfoSb.append(" - ").append(info);
				kanjiInfoCorrectSb.append(" - ").append(info);
			}					
			
			kanjiInfoTextView.setText(Html.fromHtml(kanjiInfoSb.toString()), TextView.BufferType.SPANNABLE);
			kanjiInfoCorrectTextView.setText(Html.fromHtml(kanjiInfoCorrectSb.toString()), TextView.BufferType.SPANNABLE);
			
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");			
		}
	}
	
	private Object getCurrentTestPosObject(JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext,
			KanjiTestMode kanjiTestMode) {
		
		int testCurrentPos = kanjiTestContext.getCurrentPos();
		
		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			
			List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();
			
			return kanjiEntryList.get(testCurrentPos);			
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			
			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();
			
			return dictionaryEntryWithRemovedKanji.get(testCurrentPos);			
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");			
		}		
	}
	
	private int getMaxTestPos(JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext,
			KanjiTestMode kanjiTestMode) {
				
		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			
			return kanjiTestContext.getKanjiEntryList().size();			
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			
			return kanjiTestContext.getDictionaryEntryWithRemovedKanji().size();
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");			
		}		
	}
	
	private String getCurrentTestPosCorrectKanji(JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext,
			KanjiTestMode kanjiTestMode) {
		
		int testCurrentPos = kanjiTestContext.getCurrentPos();
		
		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			
			List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();
			
			return kanjiEntryList.get(testCurrentPos).getKanji();		
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			
			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();
			
			return dictionaryEntryWithRemovedKanji.get(testCurrentPos).getRemovedKanji();	
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");			
		}		
	}

	private int getCurrentTestPosCorrectStrokeNo(JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext,
			KanjiTestMode kanjiTestMode) {
		
		int testCurrentPos = kanjiTestContext.getCurrentPos();
		
		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			
			List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();
			
			return kanjiEntryList.get(testCurrentPos).getStrokePaths().size();	
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			
			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();
			
			return DictionaryManager.getInstance().findKanji(dictionaryEntryWithRemovedKanji.get(testCurrentPos).getRemovedKanji()).getStrokePaths().size();
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");			
		}		
	}
	
	private boolean isFinish(JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext,
			KanjiTestMode kanjiTestMode) {
		
		int testCurrentPos = kanjiTestContext.getCurrentPos();
		
		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			
			List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();
			
			if (testCurrentPos >= kanjiEntryList.size()) {
				return true;
			} else {
				return false;
			}
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			
			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();
			
			if (testCurrentPos >= dictionaryEntryWithRemovedKanji.size()) {
				return true;
			} else {
				return false;
			}
			
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");			
		}
	}
}
