package pl.idedyk.android.japaneselearnhelper.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.WordTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperWordTestContext;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.utils.EntryOrderList;
import pl.idedyk.android.japaneselearnhelper.utils.ListUtil;
import pl.idedyk.japanese.dictionary.api.dictionary.Utils;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WordTest extends Activity {

	private TextViewAndEditText[] textViewAndEditTextForWordAsArray;

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

			final JapaneseAndroidLearnHelperContext context = JapaneseAndroidLearnHelperApplication.getInstance()
					.getContext();

			final JapaneseAndroidLearnHelperWordTestContext wordTestContext = context.getWordTestContext();

			final WordTestConfig wordTestConfig = JapaneseAndroidLearnHelperApplication.getInstance()
					.getConfigManager(WordTest.this).getWordTestConfig();

			// config
			WordTestMode wordTestMode = wordTestConfig.getWordTestMode();

			Set<String> chosenWordGroups = wordTestConfig.getChosenWordGroups();
			Boolean random = wordTestConfig.getRandom();
			Boolean untilSuccess = wordTestConfig.getUntilSuccess();
			Integer repeatNumber = wordTestConfig.getRepeatNumber();
			Boolean showKanji = wordTestConfig.getShowKanji();
			Boolean showKana = wordTestConfig.getShowKana();
			Boolean showTranslate = wordTestConfig.getShowTranslate();
			Boolean showAdditionalInfo = wordTestConfig.getShowAdditionalInfo();

			// context
			EntryOrderList<DictionaryEntry> wordsTest = wordTestContext.getWordsTest();
			int wordsTestIdx = wordsTest.getCurrentPos();
			int wordTestAnswers = wordTestContext.getWordTestAnswers();
			int wordTestCorrectAnswers = wordTestContext.getWordTestCorrectAnswers();
			int wordTestIncorrentAnswers = wordTestContext.getWordTestIncorrentAnswers();

			// details report
			StringBuffer detailsSb = new StringBuffer();

			detailsSb.append(" *** config ***\n\n");
			detailsSb.append("wordTestMode: " + wordTestMode).append("\n\n");
			detailsSb.append("chosenWordGroups: " + chosenWordGroups).append("\n\n");
			detailsSb.append("random: " + random).append("\n\n");
			detailsSb.append("untilSuccess: " + untilSuccess).append("\n\n");
			detailsSb.append("repeatNumber: " + repeatNumber).append("\n\n");
			detailsSb.append("showKanji: " + showKanji).append("\n\n");
			detailsSb.append("showKana: " + showKana).append("\n\n");
			detailsSb.append("showTranslate: " + showTranslate).append("\n\n");
			detailsSb.append("showAdditionalInfo: " + showAdditionalInfo).append("\n\n");

			detailsSb.append(" *** context ***\n\n");

			detailsSb.append("wordsTestIdx: " + wordsTestIdx).append("\n\n");
			detailsSb.append("wordTestAnswers: " + wordTestAnswers).append("\n\n");
			detailsSb.append("wordTestCorrectAnswers: " + wordTestCorrectAnswers).append("\n\n");
			detailsSb.append("wordTestIncorrentAnswers: " + wordTestIncorrentAnswers).append("\n\n");

			detailsSb.append("wordsTest:\n");

			for (int idx = 0; idx < wordsTest.size(); ++idx) {
				detailsSb.append("\t").append(idx).append(": ").append(wordsTest.getEntry(idx).toString()).append("\n");
			}

			String chooseEmailClientTitle = getString(R.string.choose_email_client);

			String mailSubject = getString(R.string.word_test_report_problem_email_subject);

			String mailBody = getString(R.string.word_test_report_problem_email_body, detailsSb.toString());

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

		builder.setMessage(getString(R.string.word_test_quit_question))
				.setPositiveButton(getString(R.string.word_test_quit_question_yes), dialogClickListener)
				.setNegativeButton(getString(R.string.word_test_quit_question_no), dialogClickListener).show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		super.onCreate(savedInstanceState);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_word_test));

		setContentView(R.layout.word_test);

		fillScreen();

		Button nextButton = (Button) findViewById(R.id.word_test_next_button);

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				checkUserAnswer();
			}
		});
	}

	@SuppressWarnings("deprecation")
	private void checkUserAnswer() {

		final JapaneseAndroidLearnHelperContext context = JapaneseAndroidLearnHelperApplication.getInstance()
				.getContext();
		final JapaneseAndroidLearnHelperWordTestContext wordTestContext = context.getWordTestContext();
		final WordTestConfig wordTestConfig = JapaneseAndroidLearnHelperApplication.getInstance()
				.getConfigManager(WordTest.this).getWordTestConfig();

		final EntryOrderList<DictionaryEntry> wordDictionaryEntries = wordTestContext.getWordsTest();

		final DictionaryEntry currentWordDictionaryEntry = wordDictionaryEntries.getNext();

		List<String> kanaList = currentWordDictionaryEntry.getKanaList();
		List<String> translateList = currentWordDictionaryEntry.getTranslates();

		WordTestMode wordTestMode = wordTestConfig.getWordTestMode();
		Boolean showTranslate = wordTestConfig.getShowTranslate();

		if (wordTestMode == WordTestMode.INPUT) {

			// check user answer
			int correctAnswersNo = getCorrectAnswersNo(context);

			wordTestContext.addWordTestAnswers(kanaList.size());
			wordTestContext.addWordTestCorrectAnswers(correctAnswersNo);
			wordTestContext.addWordTestIncorrentAnswers(kanaList.size() - correctAnswersNo);

			if (correctAnswersNo == kanaList.size()) {

				Toast toast = null;

				if (showTranslate == true) {
					toast = Toast.makeText(WordTest.this, getString(R.string.word_test_correct_without_translate),
							Toast.LENGTH_SHORT);
				} else {
					toast = Toast.makeText(
							WordTest.this,
							getString(R.string.word_test_correct_with_translate,
									ListUtil.getListAsString(translateList, "\n")), Toast.LENGTH_SHORT);
				}

				toast.setGravity(Gravity.TOP, 0, 110);

				toast.show();

				wordDictionaryEntries.currentPositionOk();

				fillScreen();
			} else {

				showFullAnswer(currentWordDictionaryEntry);

				AlertDialog alertDialog = new AlertDialog.Builder(WordTest.this).create();

				if (showTranslate == true) {
					alertDialog.setMessage(getString(R.string.word_test_incorrect_without_translate,
							ListUtil.getListAsString(kanaList, "\n")));
				} else {
					alertDialog.setMessage(getString(R.string.word_test_incorrect_with_translate,
							ListUtil.getListAsString(kanaList, "\n"), ListUtil.getListAsString(translateList, "\n")));
				}

				alertDialog.setCancelable(false);
				alertDialog.setButton(getString(R.string.word_test_incorrect_ok),
						new DialogInterface.OnClickListener() {

							@Override
							public void onClick(DialogInterface dialog, int which) {

								if (wordTestConfig.getUntilSuccess() != null
										&& wordTestConfig.getUntilSuccess().equals(Boolean.TRUE)) {

									wordDictionaryEntries.currentPositionBad();

								} else {
									wordDictionaryEntries.currentPositionOk();
								}

								fillScreen();
							}
						});

				alertDialog.show();
			}
		} else if (wordTestMode == WordTestMode.OVERVIEW) {

			boolean showAnswer = wordTestContext.getAndSwitchWordTestOverviewShowAnswer();

			boolean full = isFull(wordTestConfig);

			if (full == true || showAnswer == true) {

				wordTestContext.addWordTestAnswers(kanaList.size());
				wordTestContext.addWordTestCorrectAnswers(kanaList.size());
				wordTestContext.addWordTestIncorrentAnswers(0);

				wordDictionaryEntries.currentPositionOk();

				fillScreen();

			} else {
				showFullAnswer(currentWordDictionaryEntry);
			}

		} else {
			throw new RuntimeException("Unknown wordTestMode: " + wordTestConfig.getWordTestMode());
		}
	}

	private void showFullAnswer(DictionaryEntry dictionaryEntry) {

		// show kanji
		String kanji = dictionaryEntry.getKanji();

		if (kanji != null) {

			TextView kanjiLabel = (TextView) findViewById(R.id.word_test_kanji_label);
			EditText kanjiPrefix = (EditText) findViewById(R.id.word_test_kanji_prefix);
			EditText kanjiInput = (EditText) findViewById(R.id.word_test_kanji_input);

			kanjiLabel.setVisibility(View.VISIBLE);
			kanjiPrefix.setVisibility(View.VISIBLE);
			kanjiInput.setVisibility(View.VISIBLE);
		}

		@SuppressWarnings("deprecation")
		List<String> kanaList = dictionaryEntry.getKanaList();

		// show kana
		for (int kanaListIdx = 0; kanaListIdx < textViewAndEditTextForWordAsArray.length; ++kanaListIdx) {

			TextViewAndEditText currentTextViewAndEditText = textViewAndEditTextForWordAsArray[kanaListIdx];

			String currentKana = null;

			if (kanaListIdx < kanaList.size()) {
				currentKana = kanaList.get(kanaListIdx);
			}

			if (currentKana != null) {

				currentTextViewAndEditText.editPrefix.setVisibility(View.VISIBLE);
				currentTextViewAndEditText.textView.setVisibility(View.VISIBLE);
				currentTextViewAndEditText.editText.setVisibility(View.VISIBLE);
			}
		}

		// show translate
		TextView translateLabel = (TextView) findViewById(R.id.word_test_translate_label);
		EditText translateInput = (EditText) findViewById(R.id.word_test_translate_input);

		translateLabel.setVisibility(View.VISIBLE);
		translateInput.setVisibility(View.VISIBLE);

		// show additional info
		TextView additionalInfoLabel = (TextView) findViewById(R.id.word_test_additional_info_label);
		EditText additionalInfoInput = (EditText) findViewById(R.id.word_test_additional_info_input);

		String additionalInfo = dictionaryEntry.getFullInfo();

		if (additionalInfo != null) {
			additionalInfoInput.setText(additionalInfo);
			additionalInfoInput.setEnabled(false);

			additionalInfoLabel.setVisibility(View.VISIBLE);
			additionalInfoInput.setVisibility(View.VISIBLE);
		} else {
			additionalInfoInput.setText("");
			additionalInfoInput.setEnabled(false);

			additionalInfoLabel.setVisibility(View.GONE);
			additionalInfoInput.setVisibility(View.GONE);
		}
	}

	private int getCorrectAnswersNo(JapaneseAndroidLearnHelperContext context) {

		JapaneseAndroidLearnHelperWordTestContext wordTestContext = context.getWordTestContext();

		EntryOrderList<DictionaryEntry> wordDictionaryEntries = wordTestContext.getWordsTest();

		DictionaryEntry currentWordDictionaryEntry = wordDictionaryEntries.getNext();

		@SuppressWarnings("deprecation")
		List<String> kanaList = currentWordDictionaryEntry.getKanaList();

		List<String> kanaListToRemove = new ArrayList<String>(kanaList);

		for (int kanaListIdx = 0; kanaListIdx < kanaList.size(); ++kanaListIdx) {

			String currentUserAnswer = textViewAndEditTextForWordAsArray[kanaListIdx].editText.getText().toString();

			kanaListToRemove.remove(currentUserAnswer);
		}

		return kanaList.size() - kanaListToRemove.size();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);

		fillScreen();
	}

	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
		super.onRestoreInstanceState(savedInstanceState);

		fillScreen();
	}

	private void fillScreen() {
		JapaneseAndroidLearnHelperContext context = JapaneseAndroidLearnHelperApplication.getInstance().getContext();

		final WordTestConfig wordTestConfig = JapaneseAndroidLearnHelperApplication.getInstance()
				.getConfigManager(WordTest.this).getWordTestConfig();

		JapaneseAndroidLearnHelperWordTestContext wordTestContext = context.getWordTestContext();

		EntryOrderList<DictionaryEntry> wordDictionaryEntries = wordTestContext.getWordsTest();

		TextView titleTextView = (TextView) findViewById(R.id.word_test_title);

		if (wordTestConfig.getWordTestMode() == WordTestMode.INPUT) {

			titleTextView.setText(getResources().getString(R.string.word_test_view_input_label));

		} else if (wordTestConfig.getWordTestMode() == WordTestMode.OVERVIEW) {

			boolean full = isFull(wordTestConfig);

			if (full == true) {

				titleTextView.setText(getResources().getString(R.string.word_test_view_overview_full_label));

			} else {

				titleTextView.setText(getResources().getString(R.string.word_test_view_overview_think_label));
			}

		} else {
			throw new RuntimeException("Unknown wordTestMode: " + wordTestConfig.getWordTestMode());
		}

		if (wordDictionaryEntries == null) {

			Intent intent = new Intent(getApplicationContext(), WordTestSummary.class);

			startActivity(intent);

			finish();
		}
		
		DictionaryEntry currentWordDictionaryEntry = wordDictionaryEntries.getNext();

		if (currentWordDictionaryEntry == null) {

			Intent intent = new Intent(getApplicationContext(), WordTestSummary.class);

			startActivity(intent);

			finish();

		} else {
			String kanji = currentWordDictionaryEntry.getKanji();
			String prefixKana = currentWordDictionaryEntry.getPrefixKana();

			TextView kanjiLabel = (TextView) findViewById(R.id.word_test_kanji_label);
			EditText kanjiPrefix = (EditText) findViewById(R.id.word_test_kanji_prefix);
			EditText kanjiInput = (EditText) findViewById(R.id.word_test_kanji_input);

			if (kanji != null) {
				kanjiInput.setText(kanji);
				kanjiPrefix.setText(prefixKana);
			} else {
				kanjiInput.setText("");
				kanjiPrefix.setText("");
			}

			if (kanji != null && wordTestConfig.getShowKanji() != null
					&& wordTestConfig.getShowKanji().equals(Boolean.TRUE) == true) {

				kanjiLabel.setVisibility(View.VISIBLE);
				kanjiPrefix.setVisibility(View.VISIBLE);
				kanjiInput.setVisibility(View.VISIBLE);

				kanjiInput.setEnabled(false);
			} else {
				kanjiLabel.setVisibility(View.GONE);
				kanjiPrefix.setVisibility(View.GONE);
				kanjiInput.setVisibility(View.GONE);

				kanjiInput.setEnabled(false);
			}

			@SuppressWarnings("deprecation")
			List<String> kanaList = currentWordDictionaryEntry.getKanaList();

			if (kanaList.size() >= Utils.MAX_LIST_SIZE) {
				throw new RuntimeException("Kana list size: " + kanaList);
			}

			createTextViewAndEditTextForWordAsArray(kanaList.size() - 1);

			for (int kanaListIdx = 0; kanaListIdx < textViewAndEditTextForWordAsArray.length; ++kanaListIdx) {

				TextViewAndEditText currentTextViewAndEditText = textViewAndEditTextForWordAsArray[kanaListIdx];

				String currentKana = null;

				if (kanaListIdx < kanaList.size()) {
					currentKana = kanaList.get(kanaListIdx);
				}

				if (currentKana != null) {

					if (wordTestConfig.getWordTestMode() == WordTestMode.INPUT
							|| (wordTestConfig.getShowKana() != null && wordTestConfig.getShowKana().equals(
									Boolean.TRUE) == true)) {

						currentTextViewAndEditText.editPrefix.setVisibility(View.VISIBLE);
						currentTextViewAndEditText.textView.setVisibility(View.VISIBLE);
						currentTextViewAndEditText.editText.setVisibility(View.VISIBLE);

					} else {
						currentTextViewAndEditText.editPrefix.setVisibility(View.GONE);
						currentTextViewAndEditText.textView.setVisibility(View.GONE);
						currentTextViewAndEditText.editText.setVisibility(View.GONE);
					}

					currentTextViewAndEditText.editPrefix.setText(prefixKana);

					if (wordTestConfig.getWordTestMode() == WordTestMode.INPUT) {

						currentTextViewAndEditText.editPrefix.setFocusable(true);
						currentTextViewAndEditText.textView.setFocusable(true);
						currentTextViewAndEditText.editText.setFocusable(true);

						currentTextViewAndEditText.editText.setText("");

						currentTextViewAndEditText.editText.setEnabled(true);

						if (kanaListIdx == 0) {
							currentTextViewAndEditText.editText.requestFocus();
						}

					} else if (wordTestConfig.getWordTestMode() == WordTestMode.OVERVIEW) {

						currentTextViewAndEditText.editPrefix.setFocusable(false);
						currentTextViewAndEditText.textView.setFocusable(false);
						currentTextViewAndEditText.editText.setFocusable(false);

						currentTextViewAndEditText.editText.setText(currentKana);

						currentTextViewAndEditText.editText.setEnabled(false);

						currentTextViewAndEditText.editText.setSingleLine(false);
						currentTextViewAndEditText.editText.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_FLAG_MULTI_LINE);

					} else {
						throw new RuntimeException("Unknown wordTestMode: " + wordTestConfig.getWordTestMode());
					}

				} else {
					currentTextViewAndEditText.textView.setVisibility(View.GONE);

					currentTextViewAndEditText.editPrefix.setVisibility(View.GONE);
					currentTextViewAndEditText.editText.setVisibility(View.GONE);
					currentTextViewAndEditText.editText.setText("");
				}
			}

			TextView translateLabel = (TextView) findViewById(R.id.word_test_translate_label);
			EditText translateInput = (EditText) findViewById(R.id.word_test_translate_input);

			translateInput.setText(ListUtil.getListAsString(currentWordDictionaryEntry.getTranslates(), "\n"));
			translateInput.setEnabled(false);

			if (wordTestConfig.getShowTranslate() != null
					&& wordTestConfig.getShowTranslate().equals(Boolean.TRUE) == true) {
				translateLabel.setVisibility(View.VISIBLE);
				translateInput.setVisibility(View.VISIBLE);
			} else {
				translateLabel.setVisibility(View.GONE);
				translateInput.setVisibility(View.GONE);
			}

			TextView additionalInfoLabel = (TextView) findViewById(R.id.word_test_additional_info_label);
			EditText additionalInfoInput = (EditText) findViewById(R.id.word_test_additional_info_input);

			String additionalInfo = currentWordDictionaryEntry.getFullInfo();

			if (additionalInfo != null && wordTestConfig.getShowAdditionalInfo() != null
					&& wordTestConfig.getShowAdditionalInfo().equals(Boolean.TRUE) == true) {
				additionalInfoInput.setText(additionalInfo);
				additionalInfoInput.setEnabled(false);

				additionalInfoLabel.setVisibility(View.VISIBLE);
				additionalInfoInput.setVisibility(View.VISIBLE);
			} else {
				additionalInfoInput.setText("");
				additionalInfoInput.setEnabled(false);

				additionalInfoLabel.setVisibility(View.GONE);
				additionalInfoInput.setVisibility(View.GONE);
			}

			TextView state = (TextView) findViewById(R.id.word_test_state_info);

			Resources resources = getResources();

			state.setText(resources.getString(R.string.word_test_state, (wordDictionaryEntries.getCurrentPos() + 1),
					wordDictionaryEntries.size()));
		}
	}

	private void createTextViewAndEditTextForWordAsArray(final int lastAnswerIdx) {

		TextView wordLabel1 = (TextView) findViewById(R.id.word_test_word_label1);
		EditText wordPrefix1 = (EditText) findViewById(R.id.word_test_word_prefix1);
		EditText wordInput1 = (EditText) findViewById(R.id.word_test_word_input1);

		TextView wordLabel2 = (TextView) findViewById(R.id.word_test_word_label2);
		EditText wordPrefix2 = (EditText) findViewById(R.id.word_test_word_prefix2);
		EditText wordInput2 = (EditText) findViewById(R.id.word_test_word_input2);

		TextView wordLabel3 = (TextView) findViewById(R.id.word_test_word_label3);
		EditText wordPrefix3 = (EditText) findViewById(R.id.word_test_word_prefix3);
		EditText wordInput3 = (EditText) findViewById(R.id.word_test_word_input3);

		TextView wordLabel4 = (TextView) findViewById(R.id.word_test_word_label4);
		EditText wordPrefix4 = (EditText) findViewById(R.id.word_test_word_prefix4);
		EditText wordInput4 = (EditText) findViewById(R.id.word_test_word_input4);

		TextView wordLabel5 = (TextView) findViewById(R.id.word_test_word_label5);
		EditText wordPrefix5 = (EditText) findViewById(R.id.word_test_word_prefix5);
		EditText wordInput5 = (EditText) findViewById(R.id.word_test_word_input5);

		TextView wordLabel6 = (TextView) findViewById(R.id.word_test_word_label6);
		EditText wordPrefix6 = (EditText) findViewById(R.id.word_test_word_prefix6);
		EditText wordInput6 = (EditText) findViewById(R.id.word_test_word_input6);

		TextView wordLabel7 = (TextView) findViewById(R.id.word_test_word_label7);
		EditText wordPrefix7 = (EditText) findViewById(R.id.word_test_word_prefix7);
		EditText wordInput7 = (EditText) findViewById(R.id.word_test_word_input7);

		TextView wordLabel8 = (TextView) findViewById(R.id.word_test_word_label8);
		EditText wordPrefix8 = (EditText) findViewById(R.id.word_test_word_prefix8);
		EditText wordInput8 = (EditText) findViewById(R.id.word_test_word_input8);

		TextView wordLabel9 = (TextView) findViewById(R.id.word_test_word_label9);
		EditText wordPrefix9 = (EditText) findViewById(R.id.word_test_word_prefix9);
		EditText wordInput9 = (EditText) findViewById(R.id.word_test_word_input9);

		TextView wordLabel10 = (TextView) findViewById(R.id.word_test_word_label10);
		EditText wordPrefix10 = (EditText) findViewById(R.id.word_test_word_prefix10);
		EditText wordInput10 = (EditText) findViewById(R.id.word_test_word_input10);

		TextView wordLabel11 = (TextView) findViewById(R.id.word_test_word_label11);
		EditText wordPrefix11 = (EditText) findViewById(R.id.word_test_word_prefix11);
		EditText wordInput11 = (EditText) findViewById(R.id.word_test_word_input11);

		TextView wordLabel12 = (TextView) findViewById(R.id.word_test_word_label12);
		EditText wordPrefix12 = (EditText) findViewById(R.id.word_test_word_prefix12);
		EditText wordInput12 = (EditText) findViewById(R.id.word_test_word_input12);

		TextView wordLabel13 = (TextView) findViewById(R.id.word_test_word_label13);
		EditText wordPrefix13 = (EditText) findViewById(R.id.word_test_word_prefix13);
		EditText wordInput13 = (EditText) findViewById(R.id.word_test_word_input13);

		TextView wordLabel14 = (TextView) findViewById(R.id.word_test_word_label14);
		EditText wordPrefix14 = (EditText) findViewById(R.id.word_test_word_prefix14);
		EditText wordInput14 = (EditText) findViewById(R.id.word_test_word_input14);

		TextView wordLabel15 = (TextView) findViewById(R.id.word_test_word_label15);
		EditText wordPrefix15 = (EditText) findViewById(R.id.word_test_word_prefix15);
		EditText wordInput15 = (EditText) findViewById(R.id.word_test_word_input15);

		TextView wordLabel16 = (TextView) findViewById(R.id.word_test_word_label16);
		EditText wordPrefix16 = (EditText) findViewById(R.id.word_test_word_prefix16);
		EditText wordInput16 = (EditText) findViewById(R.id.word_test_word_input16);

		TextView wordLabel17 = (TextView) findViewById(R.id.word_test_word_label17);
		EditText wordPrefix17 = (EditText) findViewById(R.id.word_test_word_prefix17);
		EditText wordInput17 = (EditText) findViewById(R.id.word_test_word_input17);

		TextView wordLabel18 = (TextView) findViewById(R.id.word_test_word_label18);
		EditText wordPrefix18 = (EditText) findViewById(R.id.word_test_word_prefix18);
		EditText wordInput18 = (EditText) findViewById(R.id.word_test_word_input18);

		TextView wordLabel19 = (TextView) findViewById(R.id.word_test_word_label19);
		EditText wordPrefix19 = (EditText) findViewById(R.id.word_test_word_prefix19);
		EditText wordInput19 = (EditText) findViewById(R.id.word_test_word_input19);

		TextView wordLabel20 = (TextView) findViewById(R.id.word_test_word_label20);
		EditText wordPrefix20 = (EditText) findViewById(R.id.word_test_word_prefix20);
		EditText wordInput20 = (EditText) findViewById(R.id.word_test_word_input20);

		TextView wordLabel21 = (TextView) findViewById(R.id.word_test_word_label21);
		EditText wordPrefix21 = (EditText) findViewById(R.id.word_test_word_prefix21);
		EditText wordInput21 = (EditText) findViewById(R.id.word_test_word_input21);

		TextView wordLabel22 = (TextView) findViewById(R.id.word_test_word_label22);
		EditText wordPrefix22 = (EditText) findViewById(R.id.word_test_word_prefix22);
		EditText wordInput22 = (EditText) findViewById(R.id.word_test_word_input22);

		TextView wordLabel23 = (TextView) findViewById(R.id.word_test_word_label23);
		EditText wordPrefix23 = (EditText) findViewById(R.id.word_test_word_prefix23);
		EditText wordInput23 = (EditText) findViewById(R.id.word_test_word_input23);

		TextView wordLabel24 = (TextView) findViewById(R.id.word_test_word_label24);
		EditText wordPrefix24 = (EditText) findViewById(R.id.word_test_word_prefix24);
		EditText wordInput24 = (EditText) findViewById(R.id.word_test_word_input24);

		TextView wordLabel25 = (TextView) findViewById(R.id.word_test_word_label25);
		EditText wordPrefix25 = (EditText) findViewById(R.id.word_test_word_prefix25);
		EditText wordInput25 = (EditText) findViewById(R.id.word_test_word_input25);
		
		TextView wordLabel26 = (TextView) findViewById(R.id.word_test_word_label26);
		EditText wordPrefix26 = (EditText) findViewById(R.id.word_test_word_prefix26);
		EditText wordInput26 = (EditText) findViewById(R.id.word_test_word_input26);

		TextView wordLabel27 = (TextView) findViewById(R.id.word_test_word_label27);
		EditText wordPrefix27 = (EditText) findViewById(R.id.word_test_word_prefix27);
		EditText wordInput27 = (EditText) findViewById(R.id.word_test_word_input27);

		TextView wordLabel28 = (TextView) findViewById(R.id.word_test_word_label28);
		EditText wordPrefix28 = (EditText) findViewById(R.id.word_test_word_prefix28);
		EditText wordInput28 = (EditText) findViewById(R.id.word_test_word_input28);

		TextView wordLabel29 = (TextView) findViewById(R.id.word_test_word_label29);
		EditText wordPrefix29 = (EditText) findViewById(R.id.word_test_word_prefix29);
		EditText wordInput29 = (EditText) findViewById(R.id.word_test_word_input29);

		TextView wordLabel30 = (TextView) findViewById(R.id.word_test_word_label30);
		EditText wordPrefix30 = (EditText) findViewById(R.id.word_test_word_prefix30);
		EditText wordInput30 = (EditText) findViewById(R.id.word_test_word_input30);
		
		textViewAndEditTextForWordAsArray = new TextViewAndEditText[Utils.MAX_LIST_SIZE];

		textViewAndEditTextForWordAsArray[0] = new TextViewAndEditText(wordLabel1, wordPrefix1, wordInput1);
		textViewAndEditTextForWordAsArray[1] = new TextViewAndEditText(wordLabel2, wordPrefix2, wordInput2);
		textViewAndEditTextForWordAsArray[2] = new TextViewAndEditText(wordLabel3, wordPrefix3, wordInput3);
		textViewAndEditTextForWordAsArray[3] = new TextViewAndEditText(wordLabel4, wordPrefix4, wordInput4);
		textViewAndEditTextForWordAsArray[4] = new TextViewAndEditText(wordLabel5, wordPrefix5, wordInput5);
		textViewAndEditTextForWordAsArray[5] = new TextViewAndEditText(wordLabel6, wordPrefix6, wordInput6);
		textViewAndEditTextForWordAsArray[6] = new TextViewAndEditText(wordLabel7, wordPrefix7, wordInput7);
		textViewAndEditTextForWordAsArray[7] = new TextViewAndEditText(wordLabel8, wordPrefix8, wordInput8);
		textViewAndEditTextForWordAsArray[8] = new TextViewAndEditText(wordLabel9, wordPrefix9, wordInput9);
		textViewAndEditTextForWordAsArray[9] = new TextViewAndEditText(wordLabel10, wordPrefix10, wordInput10);
		textViewAndEditTextForWordAsArray[10] = new TextViewAndEditText(wordLabel11, wordPrefix11, wordInput11);
		textViewAndEditTextForWordAsArray[11] = new TextViewAndEditText(wordLabel12, wordPrefix12, wordInput12);
		textViewAndEditTextForWordAsArray[12] = new TextViewAndEditText(wordLabel13, wordPrefix13, wordInput13);
		textViewAndEditTextForWordAsArray[13] = new TextViewAndEditText(wordLabel14, wordPrefix14, wordInput14);
		textViewAndEditTextForWordAsArray[14] = new TextViewAndEditText(wordLabel15, wordPrefix15, wordInput15);		
		textViewAndEditTextForWordAsArray[15] = new TextViewAndEditText(wordLabel16, wordPrefix16, wordInput16);
		textViewAndEditTextForWordAsArray[16] = new TextViewAndEditText(wordLabel17, wordPrefix17, wordInput17);
		textViewAndEditTextForWordAsArray[17] = new TextViewAndEditText(wordLabel18, wordPrefix18, wordInput18);
		
		textViewAndEditTextForWordAsArray[18] = new TextViewAndEditText(wordLabel19, wordPrefix19, wordInput19);
		textViewAndEditTextForWordAsArray[19] = new TextViewAndEditText(wordLabel20, wordPrefix20, wordInput20);
		textViewAndEditTextForWordAsArray[20] = new TextViewAndEditText(wordLabel21, wordPrefix21, wordInput21);
		textViewAndEditTextForWordAsArray[21] = new TextViewAndEditText(wordLabel22, wordPrefix22, wordInput22);
		textViewAndEditTextForWordAsArray[22] = new TextViewAndEditText(wordLabel23, wordPrefix23, wordInput23);
		textViewAndEditTextForWordAsArray[23] = new TextViewAndEditText(wordLabel24, wordPrefix24, wordInput24);
		textViewAndEditTextForWordAsArray[24] = new TextViewAndEditText(wordLabel25, wordPrefix25, wordInput25);

		textViewAndEditTextForWordAsArray[25] = new TextViewAndEditText(wordLabel26, wordPrefix26, wordInput26);
		textViewAndEditTextForWordAsArray[26] = new TextViewAndEditText(wordLabel27, wordPrefix27, wordInput27);
		textViewAndEditTextForWordAsArray[27] = new TextViewAndEditText(wordLabel28, wordPrefix28, wordInput28);
		textViewAndEditTextForWordAsArray[28] = new TextViewAndEditText(wordLabel29, wordPrefix29, wordInput29);
		textViewAndEditTextForWordAsArray[29] = new TextViewAndEditText(wordLabel30, wordPrefix30, wordInput30);
		
		for (int idx = 0; idx < textViewAndEditTextForWordAsArray.length; ++idx) {

			final EditText currentEditText = textViewAndEditTextForWordAsArray[idx].editText;

			if (idx == lastAnswerIdx) {

				currentEditText.setOnClickListener(new View.OnClickListener() {

					@Override
					public void onClick(View v) {

						String currentEditTextText = currentEditText.getText().toString();

						if (currentEditTextText != null && currentEditTextText.equals("") == false) {
							checkUserAnswer();
						}
					}
				});

			} else {
				currentEditText.setOnClickListener(null);
			}
		}
	}

	private boolean isFull(WordTestConfig wordTestConfig) {

		Boolean showKanji = wordTestConfig.getShowKanji();
		Boolean showKana = wordTestConfig.getShowKana();
		Boolean showTranslate = wordTestConfig.getShowTranslate();

		if (showKanji != null && showKanji.booleanValue() == true && showKana != null
				&& showKana.booleanValue() == true && showTranslate != null && showTranslate.booleanValue() == true) {

			return true;
		} else {

			return false;
		}
	}

	private static class TextViewAndEditText {
		TextView textView;

		EditText editPrefix;
		EditText editText;

		public TextViewAndEditText(TextView textView, EditText editPrefix, EditText editText) {
			this.textView = textView;

			this.editPrefix = editPrefix;
			this.editText = editText;
		}
	}
}