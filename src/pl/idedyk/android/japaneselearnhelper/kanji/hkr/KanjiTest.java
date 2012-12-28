package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext.TestAnswer;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
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
import android.view.Menu;
import android.view.MenuItem;
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
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(Menu.NONE, R.id.report_problem_menu_item, Menu.NONE, R.string.report_problem);
		
		MenuShorterHelper.onCreateOptionsMenu(menu);
				
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		// report problem
		if (item.getItemId() == R.id.report_problem_menu_item) {

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

			} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD || kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD_GROUP) {

				List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();

				for (DictionaryEntryWithRemovedKanji currentDictionaryEntryWithRemovedKanji : dictionaryEntryWithRemovedKanji) {
					kanjiInTestList.add(currentDictionaryEntryWithRemovedKanji.getDictionaryEntry().getKanji());
				}

			} else {
				throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);			
			}

			detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_kanji2)).append(" ").append(kanjiInTestList).append("\n\n");

			detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_answers)).append("\n\n----------\n\n");

			List<TestAnswer> testAnswers = kanjiTestContext.getTestAnswers();

			for (int testAnswersIdx = 0; testAnswersIdx < testAnswers.size(); ++testAnswersIdx) {

				TestAnswer currentTestAnswer = testAnswers.get(testAnswersIdx);

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_position)).append(" ").append((testAnswersIdx + 1)).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_kanji3)).append(" ").append(currentTestAnswer.getKanji()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_correct_strokeNo)).append(" ").append(currentTestAnswer.getKanjiCorrectStrokeNo()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_width)).append(" ").append(currentTestAnswer.getWidth()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_height)).append(" ").append(currentTestAnswer.getHeight()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_correct_answer)).append(" ").append(currentTestAnswer.isCorrectAnswer()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_user_strokeNo)).append(" ").append(currentTestAnswer.getKanjiUserStrokeNo()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_user_draw)).append("\n\n").append(						
						currentTestAnswer.getDrawStrokesStrings()).append("\n");

				List<KanjiRecognizerResultItem> recognizeResult = currentTestAnswer.getRecognizeResult();

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_recognize_result)).append("\n\n");

				for (KanjiRecognizerResultItem currentRecognizerResultItem : recognizeResult) {
					detailsSb.append(currentRecognizerResultItem.getKanji() + " - " + currentRecognizerResultItem.getScore()).append("\n");
				}

				detailsSb.append("\n----------\n\n");
			}

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

			return true;
		} else {
			return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.kanji_test);

		kanjiTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanjiTestContext();

		kanjiTestConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getKanjiTestConfig();

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

				final DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(getResources(), getAssets());

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
				currentTestAnswer.setKanjiCorrectStrokeNo(correctKanjiStrokeNo);
				currentTestAnswer.setKanjiUserStrokeNo(strokes.size());

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

					kanjiTestContext.incrementCorrectAnswers();

					setScreen();
				} else { // incorrect

					kanjiTestContext.incrementIncorrectAnswers();

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

		setScreen();
		
		// set report problem info
		Toast toast = Toast.makeText(KanjiTest.this, getString(R.string.kanji_test_report_problem_info), Toast.LENGTH_LONG);

		toast.show();
	}

	private void setScreen() {

		// if finish
		if (isFinish() == true) {

			Intent intent = new Intent(getApplicationContext(), KanjiTestSummary.class);

			startActivity(intent);

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

			KanjiDic2Entry kanjiDic2Entry = currentTestKanjiEntry.getKanjiDic2Entry();

			if (kanjiDic2Entry != null) {

				boolean addedSpace = false;

				List<String> kunReading = kanjiDic2Entry.getKunReading();

				if (kunReading != null && kunReading.size() > 0) {

					if (addedSpace == false) {
						kanjiInfoSb.append("<br/><br/>");

						addedSpace = true;
					}

					kanjiInfoSb.append("<small><b>").append(getString(R.string.kanji_test_info_kunyomi)).append("</b>: ").append(kunReading.toString()).append("</small>");				
				}

				List<String> onReading = kanjiDic2Entry.getOnReading();

				if (onReading != null && onReading.size() > 0) {

					if (addedSpace == false) {
						kanjiInfoSb.append("<br/><br/>");

						addedSpace = true;
					} else {
						kanjiInfoSb.append("<br/>");
					}

					kanjiInfoSb.append("<small><b>").append(getString(R.string.kanji_test_info_onyomi)).append("</b>: ").append(onReading.toString()).append("</small>");		
				}				
			}

			kanjiInfoTextView.setText(Html.fromHtml(getString(R.string.kanji_test_info_meaning, kanjiInfoSb.toString())), TextView.BufferType.SPANNABLE);		

		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD || kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD_GROUP) {

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
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);			
		}
	}

	private Object getCurrentTestPosObject() {

		int testCurrentPos = kanjiTestContext.getCurrentPos();

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {

			List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();

			return kanjiEntryList.get(testCurrentPos);			
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD || kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD_GROUP) {

			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();

			return dictionaryEntryWithRemovedKanji.get(testCurrentPos);			
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);			
		}		
	}

	private int getMaxTestPos() {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {

			return kanjiTestContext.getKanjiEntryList().size();			
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD || kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD_GROUP) {

			return kanjiTestContext.getDictionaryEntryWithRemovedKanji().size();
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);			
		}		
	}

	private String getCurrentTestPosCorrectKanji() {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		int testCurrentPos = kanjiTestContext.getCurrentPos();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {

			List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();

			return kanjiEntryList.get(testCurrentPos).getKanji();		
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD || kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD_GROUP) {

			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();

			return dictionaryEntryWithRemovedKanji.get(testCurrentPos).getRemovedKanji();	
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);			
		}		
	}

	private int getCurrentTestPosCorrectStrokeNo() {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		int testCurrentPos = kanjiTestContext.getCurrentPos();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {

			List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();

			return kanjiEntryList.get(testCurrentPos).getStrokePaths().size();	
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD || kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD_GROUP) {

			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();

			return JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(getResources(), getAssets()).findKanji(dictionaryEntryWithRemovedKanji.get(testCurrentPos).getRemovedKanji()).getStrokePaths().size();
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);			
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
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD || kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD_GROUP) {

			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();

			if (testCurrentPos >= dictionaryEntryWithRemovedKanji.size()) {
				return true;
			} else {
				return false;
			}

		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);			
		}
	}

	private void addCurrentPosKanjiTestEntry() {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		int testCurrentPos = kanjiTestContext.getCurrentPos();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {

			List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();

			kanjiEntryList.add(kanjiEntryList.get(testCurrentPos));	
			
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD || kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD_GROUP) {

			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();

			dictionaryEntryWithRemovedKanji.add(dictionaryEntryWithRemovedKanji.get(testCurrentPos));
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);			
		}		
	}
}
