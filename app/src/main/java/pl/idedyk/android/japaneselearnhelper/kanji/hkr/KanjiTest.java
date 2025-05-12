package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext.TestAnswer;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.dictionary.ZinniaManagerCommon;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiDetails;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.sod.SodActivity;
import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;
import pl.idedyk.android.japaneselearnhelper.utils.EntryOrderList;
import pl.idedyk.android.japaneselearnhelper.utils.WordKanjiDictionaryUtils;
import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.KanjiRecognizerResultItem;
import pl.idedyk.japanese.dictionary.api.dto.KanjivgEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class KanjiTest extends Activity {

	private TextView kanjiInfoTextView;

	private Button testUndoButton;
	private Button testClearButton;
	private Button testCheckButton;

	private Button[] chooseAnswers;

	private KanjiDrawView drawView;

	private TextView kanjiTestState;

	private JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext;

	private KanjiTestConfig kanjiTestConfig;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		// menu.add(Menu.NONE, R.id.report_problem_menu_item, Menu.NONE, R.string.report_problem);

		MenuShorterHelper.onCreateOptionsMenu(menu);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		// report problem
		if (item.getItemId() == R.id.report_problem_menu_item) { // opcja ukryta (niedostepna)

			StringBuffer detailsSb = new StringBuffer();

			KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

			int testCurrentPos = getCurrentTestPos();

			detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_kanji)).append(" ")
					.append(kanjiTestConfig.getChosenKanjiAsList()).append("\n\n");

			detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_kanjiTestMode)).append(" ")
					.append(kanjiTestMode).append("\n\n");

			detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_untilSuccess)).append(" ")
					.append(kanjiTestConfig.getUntilSuccess()).append("\n\n");

			detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_currentPos)).append(" ")
					.append((testCurrentPos + 1)).append("\n\n");

			List<String> kanjiInTestList = new ArrayList<String>();

			if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING
					|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {

				EntryOrderList<KanjiCharacterInfo> kanjiEntryList = kanjiTestContext.getKanjiEntryList();

				for (int idx = 0; idx < kanjiEntryList.size(); ++idx) {
					kanjiInTestList.add(kanjiEntryList.getEntry(idx).getKanji());
				}

			} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD
					|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {

				EntryOrderList<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext
						.getDictionaryEntryWithRemovedKanji();

				for (int idx = 0; idx < dictionaryEntryWithRemovedKanji.size(); ++idx) {
					kanjiInTestList.add(dictionaryEntryWithRemovedKanji.getEntry(idx).getDictionaryEntry().getKanji());
				}

			} else {
				throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);
			}

			detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_kanji2)).append(" ")
					.append(kanjiInTestList).append("\n\n");

			detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_answers)).append(
					"\n\n----------\n\n");

			List<TestAnswer> testAnswers = kanjiTestContext.getTestAnswers();

			for (int testAnswersIdx = 0; testAnswersIdx < testAnswers.size(); ++testAnswersIdx) {

				TestAnswer currentTestAnswer = testAnswers.get(testAnswersIdx);

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_position)).append(" ")
						.append((testAnswersIdx + 1)).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_kanji3)).append(" ")
						.append(currentTestAnswer.getKanji()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_correct_strokeNo)).append(" ")
						.append(currentTestAnswer.getKanjiCorrectStrokeNo()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_width)).append(" ")
						.append(currentTestAnswer.getWidth()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_height)).append(" ")
						.append(currentTestAnswer.getHeight()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_correct_answer)).append(" ")
						.append(currentTestAnswer.isCorrectAnswer()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_user_strokeNo)).append(" ")
						.append(currentTestAnswer.getKanjiUserStrokeNo()).append("\n\n");

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_user_draw)).append("\n\n")
						.append(currentTestAnswer.getDrawStrokesStrings()).append("\n\n");

				List<KanjiRecognizerResultItem> recognizeResult = currentTestAnswer.getRecognizeResult();

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_recognize_result)).append(
						"\n\n");

				if (recognizeResult != null) {
					for (KanjiRecognizerResultItem currentRecognizerResultItem : recognizeResult) {
						detailsSb
								.append(currentRecognizerResultItem.getKanji() + " - "
										+ currentRecognizerResultItem.getScore()).append("\n");
					}

					detailsSb.append("\n");
				}

				detailsSb.append(getString(R.string.kanji_test_report_problem_email_body_chosenKanji)).append(" ")
						.append(currentTestAnswer.getChosenKanji()).append("\n\n");

				detailsSb.append("\n----------\n\n");
			}

			String chooseEmailClientTitle = getString(R.string.choose_email_client);

			String mailSubject = getString(R.string.kanji_test_report_problem_email_subject);

			String mailBody = getString(R.string.kanji_test_report_problem_email_body, detailsSb.toString());

			String versionName = "";
			int versionCode = 0;

			try {
				PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

				versionName = packageInfo.versionName;
				versionCode = packageInfo.versionCode;

			} catch (NameNotFoundException e) {
			}

			Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString(),
					versionName, versionCode);

			startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));

			return true;
		} else {
			return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
		}
	}

	@Override
	public void onBackPressed() {

		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				switch (which) {
					case DialogInterface.BUTTON_POSITIVE:

						finish();

						break;

					case DialogInterface.BUTTON_NEGATIVE:

						// noop

						break;
				}
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);

		builder.setMessage(getString(R.string.kanji_test_quit_question))
				.setPositiveButton(getString(R.string.kanji_test_quit_question_yes), dialogClickListener)
				.setNegativeButton(getString(R.string.kanji_test_quit_question_no), dialogClickListener).show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_kanji_test));

		kanjiTestConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this)
				.getKanjiTestConfig();

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {
            JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.kanji_test_choose);
		} else {
            JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.kanji_test_draw);
		}

		kanjiTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanjiTestContext();

		kanjiInfoTextView = (TextView) findViewById(R.id.kanji_test_info_textview);

		drawView = (KanjiDrawView) findViewById(R.id.kanji_test_recognizer_draw_view);

		kanjiTestState = (TextView) findViewById(R.id.kanji_test_state);

		testUndoButton = (Button) findViewById(R.id.kanji_test_recognizer_undo_button);
		testClearButton = (Button) findViewById(R.id.kanji_test_recognizer_clear_button);
		testCheckButton = (Button) findViewById(R.id.kanji_test_recognizer_check_button);

		if (testUndoButton != null) {
			testUndoButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					drawView.removeLastStroke();
				}
			});
		}

		if (testClearButton != null) {
			testClearButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					drawView.clear();
				}
			});
		}

		if (testCheckButton != null) {

			testCheckButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					final DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance()
							.getDictionaryManager(KanjiTest.this);

					final String correctKanji = getCurrentTestPosCorrectKanji();

					TestAnswer currentTestAnswer = new TestAnswer();

					boolean correctAnswer = false;

					final List<Stroke> strokes = drawView.getStrokes();

					if (strokes.size() == 0) {

						Toast toast = Toast.makeText(KanjiTest.this,
								getString(R.string.kanji_test_recognizer_please_draw), Toast.LENGTH_SHORT);

						toast.show();

						return;
					}

					ZinniaManagerCommon zinniaManager = dictionaryManager.getZinniaManager();

					// zinniaManager.open();

					ZinniaManagerCommon.IZinnaManagerCharacter zinniaCharacter = zinniaManager.createNewCharacter();

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
							zinniaCharacter.add(idx, (int) currentStrokeCurrentPoint.x,
									(int) currentStrokeCurrentPoint.y);

							strokesStringBuffer.append(currentStrokeCurrentPoint.x).append(" ")
									.append(currentStrokeCurrentPoint.y).append(";");
						}

						strokesStringBuffer.append("\n\n");
					}

					currentTestAnswer.setDrawStrokesStrings(strokesStringBuffer.toString());

					final int maxRecognizeResult = 5;

					List<KanjiRecognizerResultItem> recognizeResult = null;

					try {
						recognizeResult = zinniaCharacter.recognize(maxRecognizeResult);

					} catch (DictionaryException e) {

						Toast.makeText(KanjiTest.this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();

						return;

					} finally {
						zinniaCharacter.destroy();
					}

					currentTestAnswer.setRecognizeResult(recognizeResult);

					int correctKanjiStrokeNo = 0;

					try {
						correctKanjiStrokeNo = getCurrentTestPosCorrectStrokeNo();

					} catch (DictionaryException e) {

						Toast.makeText(KanjiTest.this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();

						return;
					}

					currentTestAnswer.setKanji(correctKanji);
					currentTestAnswer.setKanjiCorrectStrokeNo(correctKanjiStrokeNo);
					currentTestAnswer.setKanjiUserStrokeNo(strokes.size());

					for (int recognizeResultIdx = 0; recognizeResultIdx < recognizeResult.size()
							&& recognizeResultIdx < maxRecognizeResult; ++recognizeResultIdx) {

						KanjiRecognizerResultItem currentKanjiRecognizerResultItem = recognizeResult
								.get(recognizeResultIdx);

						String currentKanjiRecognizerResultItemKanji = currentKanjiRecognizerResultItem.getKanji();

						if (correctKanji.equals(currentKanjiRecognizerResultItemKanji) == true
								&& strokes.size() == correctKanjiStrokeNo) {
							correctAnswer = true;
						}
					}

					currentTestAnswer.setCorrectAnswer(correctAnswer);

					processAnswer(correctAnswer, correctKanji, currentTestAnswer);
				}
			});
		}

		setScreen();

		/*
		// set report problem info
		Toast toast = Toast.makeText(KanjiTest.this, getString(R.string.kanji_test_report_problem_info),
				Toast.LENGTH_LONG);

		toast.show();
		*/
	}

	private void processChooseAnswer(String chosenKanji) {

		final String correctKanji = getCurrentTestPosCorrectKanji();

		TestAnswer testAnswer = new TestAnswer();

		boolean correctAnswer = chosenKanji.equals(correctKanji);

		testAnswer.setKanji(correctKanji);
		testAnswer.setCorrectAnswer(correctAnswer);
		testAnswer.setChosenKanji(chosenKanji);

		processAnswer(correctAnswer, correctKanji, testAnswer);
	}

	private void processAnswer(boolean correctAnswer, final String correctKanji, TestAnswer testAnswer) {

		// logowanie
		JapaneseAndroidLearnHelperApplication.getInstance().logEvent(this, getString(R.string.logs_kanji_test), getString(R.string.logs_kanji_test_check), null);


		final DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance()
				.getDictionaryManager(this);

		kanjiTestContext.getTestAnswers().add(testAnswer);

		if (correctAnswer == true) { // correct

			Toast toast = Toast.makeText(KanjiTest.this, getString(R.string.kanji_test_recognizer_correct_answer),
					Toast.LENGTH_SHORT);

			toast.show();

			addCurrentPosKanjiTestEntry(true);

			kanjiTestContext.incrementCorrectAnswers();

			setScreen();

		} else { // incorrect

			kanjiTestContext.incrementIncorrectAnswers();
			
			/*
			Builder alertDialogBuilder = new AlertDialog.Builder(KanjiTest.this);
			
			alertDialogBuilder.setTitle(getString(R.string.kanji_test_correct_answer, correctKanji));
			
			alertDialogBuilder.setCancelable(false);
			*/
			
			final Dialog alertDialog = new Dialog(this);
			
			alertDialog.setContentView(R.layout.alert_dialog_three_buttons);			
			alertDialog.setTitle(getString(R.string.kanji_test_correct_answer_title, correctKanji));
			
			TextView message = (TextView)alertDialog.findViewById(R.id.message);
			
			message.setText(Html.fromHtml(getString(R.string.kanji_test_correct_answer_message) + " <b>" + correctKanji + "</b>"));
			
			Button alertDialogButton1 = (Button)alertDialog.findViewById(R.id.button1);
			Button alertDialogButton2 = (Button)alertDialog.findViewById(R.id.button2);
			Button alertDialogButton3 = (Button)alertDialog.findViewById(R.id.button3);
			
			alertDialogButton1.setText(getString(R.string.kanji_test_incorrect_show_kanji_details));
			alertDialogButton2.setText(getString(R.string.kanji_test_incorrect_show_sod));
			alertDialogButton3.setText(getString(R.string.kanji_test_incorrect_ok));
			
			alertDialogButton1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					try {
						Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);

						intent.putExtra("id", dictionaryManager.findKanji(correctKanji).getId());

						startActivity(intent);

					} catch (DictionaryException e) {

						Toast.makeText(KanjiTest.this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();

						return;
					}
				}
			});
			
			alertDialogButton2.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {

					try {
						StrokePathInfo strokePathInfo = new StrokePathInfo();

						List<KanjivgEntry> strokePathsList = new ArrayList<KanjivgEntry>();
						strokePathsList.add(WordKanjiDictionaryUtils.createKanjivgEntry(dictionaryManager.findKanji(correctKanji)));
						strokePathInfo.setStrokePaths(strokePathsList);

						Intent intent = new Intent(getApplicationContext(), SodActivity.class);

						intent.putExtra("strokePathsInfo", strokePathInfo);

						startActivity(intent);

					} catch (DictionaryException e) {

						Toast.makeText(KanjiTest.this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();

						return;
					}
				}
			});
			
			alertDialogButton3.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					
					alertDialog.cancel();

					Boolean untilSuccess = kanjiTestConfig.getUntilSuccess();

					if (untilSuccess == true) {
						addCurrentPosKanjiTestEntry(false);
					} else {
						addCurrentPosKanjiTestEntry(true);
					}

					setScreen();
				}
			});
						
			alertDialog.show();
		}
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
		if (drawView != null) {
			drawView.clear();
		}
	}

	private void setInfoValue() {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		Object currentTestPosObject = getCurrentTestPosObject();

		kanjiTestState.setText(getString(R.string.kanji_test_state, getCurrentTestPos() + 1, getMaxTestPos()));

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {

			KanjiCharacterInfo currentTestKanjiEntry = (KanjiCharacterInfo) currentTestPosObject;

			List<String> polishTranslates = Utils.getPolishTranslates(currentTestKanjiEntry);
			String info = Utils.getPolishAdditionalInfo(currentTestKanjiEntry);

			StringBuffer kanjiInfoSb = new StringBuffer();

			kanjiInfoSb.append("<b>").append(polishTranslates.toString()).append("</b>");

			if (info != null && info.equals("") == false) {
				kanjiInfoSb.append(" - ").append(info);
			}

			boolean addedSpace = false;

			List<String> kunReading = Utils.getKunReading(currentTestKanjiEntry);

			if (kunReading != null && kunReading.size() > 0) {

				if (addedSpace == false) {
					kanjiInfoSb.append("<br/><br/>");

					addedSpace = true;
				}

				kanjiInfoSb.append("<small><b>").append(getString(R.string.kanji_test_info_kunyomi))
						.append("</b>: ").append(toString(kunReading)).append("</small>");
			}

			List<String> onReading = Utils.getOnReading(currentTestKanjiEntry);

			if (onReading != null && onReading.size() > 0) {

				if (addedSpace == false) {
					kanjiInfoSb.append("<br/><br/>");

					addedSpace = true;
				} else {
					kanjiInfoSb.append("<br/>");
				}

				kanjiInfoSb.append("<small><b>").append(getString(R.string.kanji_test_info_onyomi))
						.append("</b>: ").append(toString(onReading)).append("</small>");
			}

			List<String> nanoriReading = Utils.getNanoriReading(currentTestKanjiEntry);

			if (nanoriReading != null && nanoriReading.size() > 0) {

				if (addedSpace == false) {
					kanjiInfoSb.append("<br/><br/>");

					addedSpace = true;
				} else {
					kanjiInfoSb.append("<br/>");
				}

				kanjiInfoSb.append("<small><b>").append(getString(R.string.kanji_test_info_nanori))
						.append("</b>: ").append(toString(nanoriReading)).append("</small>");
			}


			if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
				kanjiInfoTextView.setText(
						Html.fromHtml(getString(R.string.kanji_test_info_draw_meaning, kanjiInfoSb.toString())),
						TextView.BufferType.SPANNABLE);
			} else if (kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {
				kanjiInfoTextView.setText(
						Html.fromHtml(getString(R.string.kanji_test_info_choose_meaning, kanjiInfoSb.toString())),
						TextView.BufferType.SPANNABLE);
			}

			if (kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {
				generateChooseButtons(currentTestKanjiEntry.getKanji());
			}

		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {

			DictionaryEntryWithRemovedKanji currentDictionaryEntryWithRemovedKanji = (DictionaryEntryWithRemovedKanji) currentTestPosObject;

			String kanjiWithRemovedKanji = currentDictionaryEntryWithRemovedKanji.getKanjiWithRemovedKanji();
			DictionaryEntry dictionaryEntry = currentDictionaryEntryWithRemovedKanji.getDictionaryEntry();

			@SuppressWarnings("deprecation")
			List<String> kanaList = dictionaryEntry.getKanaList();
			
			List<String> translates = dictionaryEntry.getTranslates();
			String info = dictionaryEntry.getInfo();

			StringBuffer kanjiInfoSb = new StringBuffer();

			if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
				kanjiInfoSb.append(getString(R.string.kanji_test_info_draw_kanji_in_word)).append("<br/><br/>");
			} else if (kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {
				kanjiInfoSb.append(getString(R.string.kanji_test_info_choose_kanji_in_word)).append("<br/><br/>");
			}

			kanjiInfoSb.append("<b><big>").append(kanjiWithRemovedKanji).append("</big></b>");
			kanjiInfoSb.append(" ").append(kanaList).append(" - ");

			kanjiInfoSb.append(translates);

			if (info != null && info.equals("") == false) {
				kanjiInfoSb.append(" - ").append(info);
			}

			kanjiInfoTextView.setText(Html.fromHtml(kanjiInfoSb.toString()), TextView.BufferType.SPANNABLE);

			if (kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {
				generateChooseButtons(currentDictionaryEntryWithRemovedKanji.getRemovedKanji());
			}

		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);
		}
	}
	
	private String toString(List<String> listString) {
		
		StringBuffer sb = new StringBuffer();
		
		sb.append("[");
				
		for (int idx = 0; idx < listString.size(); ++idx) {
			
			sb.append(listString.get(idx));
			
			if (idx != listString.size() - 1) {
				sb.append(", &nbsp; &nbsp;");
			}
		}
		
		sb.append("]");
				
		return sb.toString();
	}

	private void generateChooseButtons(String correctKanji) {

		// set buttons

		chooseAnswers = new Button[12];

		chooseAnswers[0] = (Button) findViewById(R.id.kanji_test_choose_answer1);
		chooseAnswers[1] = (Button) findViewById(R.id.kanji_test_choose_answer2);
		chooseAnswers[2] = (Button) findViewById(R.id.kanji_test_choose_answer3);
		chooseAnswers[3] = (Button) findViewById(R.id.kanji_test_choose_answer4);
		chooseAnswers[4] = (Button) findViewById(R.id.kanji_test_choose_answer5);
		chooseAnswers[5] = (Button) findViewById(R.id.kanji_test_choose_answer6);
		chooseAnswers[6] = (Button) findViewById(R.id.kanji_test_choose_answer7);
		chooseAnswers[7] = (Button) findViewById(R.id.kanji_test_choose_answer8);
		chooseAnswers[8] = (Button) findViewById(R.id.kanji_test_choose_answer9);
		chooseAnswers[9] = (Button) findViewById(R.id.kanji_test_choose_answer10);
		chooseAnswers[10] = (Button) findViewById(R.id.kanji_test_choose_answer11);
		chooseAnswers[11] = (Button) findViewById(R.id.kanji_test_choose_answer12);

		// reset
		for (int chooseAnswerIdx = 0; chooseAnswerIdx < chooseAnswers.length; ++chooseAnswerIdx) {
			chooseAnswers[chooseAnswerIdx].setText("");
		}

		Random random = new Random();

		// set correct kanji
		int correctAnswerPos = random.nextInt(chooseAnswers.length);
		chooseAnswers[correctAnswerPos].setText(correctKanji);

		List<String> chosenKanjiSource = kanjiTestConfig.getChosenKanjiAsList();

		// create available kanji list
		List<String> availableKanji = new ArrayList<String>(chosenKanjiSource);

		availableKanji.remove(correctKanji);

		Collections.shuffle(availableKanji);

		for (int chooseAnswerIdx = 0; chooseAnswerIdx < chooseAnswers.length; ++chooseAnswerIdx) {

			final Button currentChooseAnswerButton = chooseAnswers[chooseAnswerIdx];

			if (availableKanji.size() == 0) {
				availableKanji = new ArrayList<String>(chosenKanjiSource);

				Collections.shuffle(availableKanji);
			}

			if (currentChooseAnswerButton.getText().equals("") == true) {
				currentChooseAnswerButton.setText(availableKanji.get(0));

				availableKanji.remove(0);
			}

			currentChooseAnswerButton.setOnClickListener(new View.OnClickListener() {

				@Override
				public void onClick(View v) {
					processChooseAnswer(currentChooseAnswerButton.getText().toString());
				}
			});
		}
	}

	private Object getCurrentTestPosObject() {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {

			return kanjiTestContext.getKanjiEntryList().getNext();

		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {

			return kanjiTestContext.getDictionaryEntryWithRemovedKanji().getNext();

		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);
		}
	}

	private int getCurrentTestPos() {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {

			return kanjiTestContext.getKanjiEntryList().getCurrentPos();

		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {

			return kanjiTestContext.getDictionaryEntryWithRemovedKanji().getCurrentPos();

		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);
		}
	}

	private int getMaxTestPos() {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {

			return kanjiTestContext.getKanjiEntryList().size();
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {

			return kanjiTestContext.getDictionaryEntryWithRemovedKanji().size();
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);
		}
	}

	private String getCurrentTestPosCorrectKanji() {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {

			return kanjiTestContext.getKanjiEntryList().getNext().getKanji();

		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {

			return kanjiTestContext.getDictionaryEntryWithRemovedKanji().getNext().getRemovedKanji();

		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);
		}
	}

	private int getCurrentTestPosCorrectStrokeNo() throws DictionaryException {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {

			return kanjiTestContext.getKanjiEntryList().getNext().getMisc2().getStrokePaths().size();

		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {

			DictionaryEntryWithRemovedKanji dictionaryEntryWithRemovedKanji = kanjiTestContext
					.getDictionaryEntryWithRemovedKanji().getNext();

			return JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this)
					.findKanji(dictionaryEntryWithRemovedKanji.getRemovedKanji()).getMisc2().getStrokePaths().size();
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);
		}
	}

	private boolean isFinish() {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {

			EntryOrderList<KanjiCharacterInfo> kanjiEntryList = kanjiTestContext.getKanjiEntryList();
			
			if (kanjiEntryList == null) {
				return true;
			}

			KanjiCharacterInfo kanjiEntry = kanjiEntryList.getNext();

			if (kanjiEntry == null) {
				return true;
			} else {
				return false;
			}
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {

			DictionaryEntryWithRemovedKanji dictionaryEntryWithRemovedKanji = kanjiTestContext
					.getDictionaryEntryWithRemovedKanji().getNext();

			if (dictionaryEntryWithRemovedKanji == null) {
				return true;
			} else {
				return false;
			}

		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);
		}
	}

	private void addCurrentPosKanjiTestEntry(boolean ok) {

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {

			EntryOrderList<KanjiCharacterInfo> kanjiEntryList = kanjiTestContext.getKanjiEntryList();

			if (ok == true) {
				kanjiEntryList.currentPositionOk();
			} else {
				kanjiEntryList.currentPositionBad();
			}

		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD
				|| kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {

			EntryOrderList<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext
					.getDictionaryEntryWithRemovedKanji();

			if (ok == true) {
				dictionaryEntryWithRemovedKanji.currentPositionOk();
			} else {
				dictionaryEntryWithRemovedKanji.currentPositionBad();
			}

		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);
		}
	}
}
