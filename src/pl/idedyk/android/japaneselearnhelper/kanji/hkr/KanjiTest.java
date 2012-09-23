package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext.TestAnswer;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.sod.SodActivity;
import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.PointF;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class KanjiTest extends Activity {
	
	private TextView kanjiInfoTextView;
	
	private Button testUndoButton;
	private Button testClearButton;
	private Button testCheckButton;
	
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

		drawView = (KanjiDrawView) findViewById(R.id.kanji_test_recognizer_draw_view);
				
		kanjiTestState = (TextView) findViewById(R.id.kanji_test_state);

		testUndoButton = (Button) findViewById(R.id.kanji_test_recognizer_undo_button);
		testClearButton = (Button) findViewById(R.id.kanji_test_recognizer_clear_button);
		testCheckButton = (Button) findViewById(R.id.kanji_test_recognizer_check_button);
				
		testUndoButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				drawView.removeLastStroke();
			}
		});

		testClearButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				drawView.clear();
			}
		});
		
		testCheckButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				
				final List<Stroke> strokes = drawView.getStrokes();
				
				if (strokes.size() == 0) {
					
					Toast toast = Toast.makeText(KanjiTest.this, getString(R.string.kanji_test_recognizer_please_draw), Toast.LENGTH_SHORT);
					
					toast.show();
					
					return;
				}
				
				final DictionaryManager dictionaryManager = DictionaryManager.getInstance();
				
				ZinniaManager zinniaManager = dictionaryManager.getZinniaManager();
				
				zinniaManager.open();
				
				pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManager.Character zinniaCharacter = zinniaManager.createNewCharacter();

				TestAnswer currentTestAnswer = new TestAnswer();
				
				currentTestAnswer.setWidth(drawView.getWidth());
				currentTestAnswer.setHeight(drawView.getHeight());
				
				zinniaCharacter.clear();
				zinniaCharacter.setWidth(drawView.getWidth());
				zinniaCharacter.setHeight(drawView.getHeight());
				
				final StringBuffer strokesStringBuffer = new StringBuffer();
				
				for (int idx = 0; idx < strokes.size(); ++idx) {
					
					strokesStringBuffer.append(String.valueOf((idx + 1))).append(":");
					
					Stroke currentStroke = strokes.get(idx);
					
					List<PointF> currentStrokePoints = currentStroke.getPoints();
					
					for (PointF currentStrokeCurrentPoint : currentStrokePoints) {
						zinniaCharacter.add(idx, (int)currentStrokeCurrentPoint.x, (int)currentStrokeCurrentPoint.y);
						
						strokesStringBuffer.append(currentStrokeCurrentPoint.x).append(" ").append(currentStrokeCurrentPoint.y).append(";");
					}
					
					strokesStringBuffer.append("\n\n");
				}
				
				currentTestAnswer.setDrawStrokesStrings(strokesStringBuffer.toString());
				
				final int maxRecognizeResult = 5;
				
				List<KanjiRecognizerResultItem> recognizeResult = zinniaCharacter.recognize(maxRecognizeResult);
				
				zinniaCharacter.destroy();
				
				currentTestAnswer.setRecognizeResult(recognizeResult);
				
				final String correctKanji = getCurrentTestPosCorrectKanji();
				int correctKanjiStrokeNo = getCurrentTestPosCorrectStrokeNo();
				
				currentTestAnswer.setKanji(correctKanji);
				currentTestAnswer.setKanjiStrokeNo(correctKanjiStrokeNo);
				
				boolean correctAnswer = false;
				
				for (int recognizeResultIdx = 0; recognizeResultIdx < recognizeResult.size() && recognizeResultIdx < maxRecognizeResult; ++recognizeResultIdx) {
					
					KanjiRecognizerResultItem currentKanjiRecognizerResultItem = recognizeResult.get(recognizeResultIdx);
					
					String currentKanjiRecognizerResultItemKanji = currentKanjiRecognizerResultItem.getKanji();
					
					if (correctKanji.equals(currentKanjiRecognizerResultItemKanji) == true && strokes.size() == correctKanjiStrokeNo) {
						correctAnswer = true;
					}
				}
				
				currentTestAnswer.setCorrectAnswer(correctAnswer);
				
				kanjiTestContext.getTestAnswers().add(currentTestAnswer);
				
				if (correctAnswer == true) { // correct
					
					Toast toast = Toast.makeText(KanjiTest.this, getString(R.string.kanji_test_recognizer_correct_answer), Toast.LENGTH_SHORT);
					
					toast.show();
					
					kanjiTestContext.setCurrentPos(kanjiTestContext.getCurrentPos() + 1);
					
					setScreen();
				} else { // incorrect
					
					// FIXME powtarzaj az do sukcesu
					
					AlertDialog alertDialog = new AlertDialog.Builder(KanjiTest.this).create();
					
					alertDialog.setMessage(getString(R.string.kanji_test_correct_answer, correctKanji));
					
					alertDialog.setCancelable(false);
										
					alertDialog.setButton(getString(R.string.kanji_test_incorrect_ok), new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							
							Boolean untilSuccess = kanjiTestConfig.getUntilSuccess();
							
							if (untilSuccess == true) {
								addCurrentPosKanjiTestEntry();
							}
							
							kanjiTestContext.setCurrentPos(kanjiTestContext.getCurrentPos() + 1);
							
							setScreen();
						}
					});
					
					alertDialog.setButton2(getString(R.string.kanji_test_incorrect_show_sod), new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							
							StrokePathInfo strokePathInfo = new StrokePathInfo();
														
							List<List<String>> strokePathsList = new ArrayList<List<String>>();
							strokePathsList.add(dictionaryManager.findKanji(correctKanji).getStrokePaths());
							strokePathInfo.setStrokePaths(strokePathsList);
							
							Intent intent = new Intent(getApplicationContext(), SodActivity.class);
												
							intent.putExtra("strokePathsInfo", strokePathInfo);
							
							startActivity(intent);
							
							Boolean untilSuccess = kanjiTestConfig.getUntilSuccess();
							
							if (untilSuccess == true) {
								addCurrentPosKanjiTestEntry();
							}
							
							kanjiTestContext.setCurrentPos(kanjiTestContext.getCurrentPos() + 1);
							
							setScreen();							
						}
					});
					
					alertDialog.show();					
				}
			}
		});
		
		Button reportProblemButton = (Button)findViewById(R.id.kanji_test_report_problem_button);
		
		reportProblemButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View view) {

				StringBuffer detailsSb = new StringBuffer();
				
				KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
				
				int testCurrentPos = kanjiTestContext.getCurrentPos();
								
				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_kanji)).append(" ").append(kanjiTestConfig.getChosenKanjiAsList()).append("\n\n");
				
				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_kanjiTestMode)).append(" ").append(kanjiTestMode).append("\n\n");
				
				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_untilSuccess)).append(" ").append(kanjiTestConfig.getUntilSuccess()).append("\n\n");
				
				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_currentPos)).append(" ").append((testCurrentPos + 1)).append("\n\n");
				
				List<String> kanjiInTestList = new ArrayList<String>();
				
				if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
					
					List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();
					
					for (KanjiEntry currentKanjiEntry : kanjiEntryList) {
						kanjiInTestList.add(currentKanjiEntry.getKanji());
					}					
						
				} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
					
					List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();
					
					for (DictionaryEntryWithRemovedKanji currentDictionaryEntryWithRemovedKanji : dictionaryEntryWithRemovedKanji) {
						kanjiInTestList.add(currentDictionaryEntryWithRemovedKanji.getDictionaryEntry().getKanji());
					}
					
				} else {
					throw new RuntimeException("KanjiTestMode kanjiTestMode");			
				}
				
				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_kanji2)).append(" ").append(kanjiInTestList).append("\n\n");
				
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);

				String mailSubject = getString(R.string.kanji_test_report_problem_email_subject);

				String mailBody = getString(R.string.kanji_test_report_problem_email_body,
						detailsSb.toString());

				String versionName = "";
				int versionCode = 0;

				try {
					PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

					versionName = packageInfo.versionName;
					versionCode = packageInfo.versionCode;

				} catch (NameNotFoundException e) {        	
				}

				Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString(), versionName, versionCode); 

				startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));				
			}
		});
				
		setScreen();
	}
		
	private void setScreen() {
				
		// if finish
		if (isFinish() == true) {
			
			// FIXME !!!
			
			finish();
			
			return;
		}
		
		// set info value
		setInfoValue();
		
		// draw view clear
		drawView.clear();
	}
		
	private void setInfoValue() {
		
		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
		
		Object currentTestPosObject = getCurrentTestPosObject();
		
		kanjiTestState.setText(getString(R.string.kanji_test_state, kanjiTestContext.getCurrentPos() + 1, getMaxTestPos()));
		
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
			
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			
			DictionaryEntryWithRemovedKanji currentDictionaryEntryWithRemovedKanji = (DictionaryEntryWithRemovedKanji)currentTestPosObject;
			
			String kanjiWithRemovedKanji = currentDictionaryEntryWithRemovedKanji.getKanjiWithRemovedKanji();
			DictionaryEntry dictionaryEntry = currentDictionaryEntryWithRemovedKanji.getDictionaryEntry();
			
			List<String> kanaList = dictionaryEntry.getKanaList();
			List<String> translates = dictionaryEntry.getTranslates();
			String info = dictionaryEntry.getInfo();
			
			StringBuffer kanjiInfoSb = new StringBuffer();
			
			kanjiInfoSb.append(getString(R.string.kanji_test_info_kanji_in_word)).append("<br/><br/>");			
			kanjiInfoSb.append("<b><big>").append(kanjiWithRemovedKanji).append("</big></b>");
			kanjiInfoSb.append(" ").append(kanaList).append(" - ");
			
			kanjiInfoSb.append(translates);
			
			if (info != null && info.equals("") == false) {
				kanjiInfoSb.append(" - ").append(info);
			}					
			
			kanjiInfoTextView.setText(Html.fromHtml(kanjiInfoSb.toString()), TextView.BufferType.SPANNABLE);			
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");			
		}
	}
	
	private Object getCurrentTestPosObject() {
		
		int testCurrentPos = kanjiTestContext.getCurrentPos();
		
		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
		
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
	
	private int getMaxTestPos() {
		
		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
				
		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			
			return kanjiTestContext.getKanjiEntryList().size();			
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			
			return kanjiTestContext.getDictionaryEntryWithRemovedKanji().size();
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");			
		}		
	}
	
	private String getCurrentTestPosCorrectKanji() {
		
		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
		
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

	private int getCurrentTestPosCorrectStrokeNo() {
		
		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
		
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
	
	private boolean isFinish() {
		
		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
		
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
	
	private void addCurrentPosKanjiTestEntry() {
		
		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
		
		int testCurrentPos = kanjiTestContext.getCurrentPos();
		
		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			
			List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();
			
			kanjiEntryList.add(kanjiEntryList.get(testCurrentPos));	
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			
			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();
			
			dictionaryEntryWithRemovedKanji.add(dictionaryEntryWithRemovedKanji.get(testCurrentPos));
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");			
		}		
	}
}
