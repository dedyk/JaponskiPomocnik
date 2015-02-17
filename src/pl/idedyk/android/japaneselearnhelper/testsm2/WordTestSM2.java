package pl.idedyk.android.japaneselearnhelper.testsm2;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.WordTestSM2Config;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.WordTestSM2Manager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.WordTestSM2WordStat;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
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
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class WordTestSM2 extends Activity {

	private DictionaryManager dictionaryManager = null;
	private WordTestSM2Manager wordTestSM2Manager = null;

	private WordTestSM2WordStat currentNextWordStat = null;
	private DictionaryEntry currentWordDictionaryEntry = null;

	private boolean inputWasCorrectAnswer = false;

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

			final WordTestSM2Config wordTestSM2Config = JapaneseAndroidLearnHelperApplication.getInstance()
					.getConfigManager(WordTestSM2.this).getWordTestSM2Config();

			// config
			WordTestSM2Mode wordTestSM2Mode = wordTestSM2Config.getWordTestSM2Mode();

			Integer maxNewWords = wordTestSM2Config.getMaxNewWords();

			Boolean showKanji = wordTestSM2Config.getShowKanji();
			Boolean showKana = wordTestSM2Config.getShowKana();
			Boolean showTranslate = wordTestSM2Config.getShowTranslate();
			Boolean showAdditionalInfo = wordTestSM2Config.getShowAdditionalInfo();

			// details report
			StringBuffer detailsSb = new StringBuffer();

			detailsSb.append(" *** config ***\n\n");
			detailsSb.append("maxNewWords: " + maxNewWords).append("\n\n");
			detailsSb.append("wordTestSM2Mode: " + wordTestSM2Mode).append("\n\n");
			detailsSb.append("showKanji: " + showKanji).append("\n\n");
			detailsSb.append("showKana: " + showKana).append("\n\n");
			detailsSb.append("showTranslate: " + showTranslate).append("\n\n");
			detailsSb.append("showAdditionalInfo: " + showAdditionalInfo).append("\n\n");

			String chooseEmailClientTitle = getString(R.string.choose_email_client);

			String mailSubject = getString(R.string.word_test_sm2_report_problem_email_subject);

			String mailBody = getString(R.string.word_test_sm2_report_problem_email_body, detailsSb.toString());

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

		builder.setMessage(getString(R.string.word_test_sm2_quit_question))
				.setPositiveButton(getString(R.string.word_test_sm2_quit_question_yes), dialogClickListener)
				.setNegativeButton(getString(R.string.word_test_sm2_quit_question_no), dialogClickListener).show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);
		wordTestSM2Manager = dictionaryManager.getWordTestSM2Manager();

		requestWindowFeature(Window.FEATURE_NO_TITLE);

		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		super.onCreate(savedInstanceState);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_word_test_sm2));

		setContentView(R.layout.word_test_sm2);

		fillScreen();

		Button nextButton = (Button) findViewById(R.id.word_test_sm2_next_button);

		nextButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				checkUserAnswer();
			}
		});
	}

	private void checkUserAnswer() {

		final WordTestSM2Config wordTestSM2Config = JapaneseAndroidLearnHelperApplication.getInstance()
				.getConfigManager(WordTestSM2.this).getWordTestSM2Config();

		WordTestSM2Mode wordTestSM2Mode = wordTestSM2Config.getWordTestSM2Mode();

		if (wordTestSM2Mode == WordTestSM2Mode.INPUT) {

			// check user answer
			int correctAnswersNo = getCorrectAnswersNo();

			@SuppressWarnings("deprecation")
			List<String> kanaList = currentWordDictionaryEntry.getKanaList();

			View stateInfoView = findViewById(R.id.word_test_sm2_state_info);

			int[] stateInfoLocation = new int[2];
			stateInfoView.getLocationOnScreen(stateInfoLocation);

			if (correctAnswersNo == kanaList.size()) {
				Toast toast = Toast.makeText(WordTestSM2.this, getString(R.string.word_test_sm2_correct),
						Toast.LENGTH_SHORT);

				toast.setGravity(Gravity.TOP, 0, stateInfoLocation[1] + stateInfoView.getHeight() + 40);

				toast.show();

				showFullAnswer();

				inputWasCorrectAnswer = true;

				showSM2Buttons();

			} else {

				Toast toast = Toast.makeText(WordTestSM2.this, getString(R.string.word_test_sm2_incorrect),
						Toast.LENGTH_SHORT);

				toast.setGravity(Gravity.TOP, 0, stateInfoLocation[1] + stateInfoView.getHeight() + 40);

				toast.show();

				showFullAnswer();

				inputWasCorrectAnswer = false;

				showSM2Buttons();
			}

		} else if (wordTestSM2Mode == WordTestSM2Mode.CHOOSE) {

			showFullAnswer();

			showSM2Buttons();

		} else {
			throw new RuntimeException("Unknown wordTestSM2Mode: " + wordTestSM2Config.getWordTestSM2Mode());
		}
	}

	private void showFullAnswer() {

		// show kanji
		String kanji = currentWordDictionaryEntry.getKanji();

		if (kanji != null) {

			TextView kanjiLabel = (TextView) findViewById(R.id.word_test_sm2_kanji_label);
			EditText kanjiPrefix = (EditText) findViewById(R.id.word_test_sm2_kanji_prefix);
			EditText kanjiInput = (EditText) findViewById(R.id.word_test_sm2_kanji_input);

			kanjiLabel.setVisibility(View.VISIBLE);
			kanjiPrefix.setVisibility(View.VISIBLE);
			kanjiInput.setVisibility(View.VISIBLE);
		}

		@SuppressWarnings("deprecation")
		List<String> kanaList = currentWordDictionaryEntry.getKanaList();

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

				currentTextViewAndEditText.editText.setText(currentKana);
				currentTextViewAndEditText.editText.setEnabled(false);

				currentTextViewAndEditText.editText.setSingleLine(false);
				currentTextViewAndEditText.editText.setInputType(InputType.TYPE_CLASS_TEXT
						| InputType.TYPE_TEXT_FLAG_MULTI_LINE);
			}
		}

		// show translate
		TextView translateLabel = (TextView) findViewById(R.id.word_test_sm2_translate_label);
		EditText translateInput = (EditText) findViewById(R.id.word_test_sm2_translate_input);

		translateLabel.setVisibility(View.VISIBLE);
		translateInput.setVisibility(View.VISIBLE);

		// show additional info
		TextView additionalInfoLabel = (TextView) findViewById(R.id.word_test_sm2_additional_info_label);
		EditText additionalInfoInput = (EditText) findViewById(R.id.word_test_sm2_additional_info_input);

		String additionalInfo = currentWordDictionaryEntry.getFullInfo();

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

	private int getCorrectAnswersNo() {

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

	@SuppressWarnings("deprecation")
	private void fillScreen() {

		final WordTestSM2Config wordTestSM2Config = JapaneseAndroidLearnHelperApplication.getInstance()
				.getConfigManager(WordTestSM2.this).getWordTestSM2Config();

		TextView titleTextView = (TextView) findViewById(R.id.word_test_sm2_title);

		Button nextButton = (Button) findViewById(R.id.word_test_sm2_next_button);

		TableRow sm2QualityTitleTableRow = (TableRow) findViewById(R.id.word_test_sm2_quality_title_table_row);
		TableRow sm2IncorrectButtonsTableRow = (TableRow) findViewById(R.id.word_test_sm2_incorrect_sm2_buttons);
		TableRow sm2CorrectButtonsTableRow = (TableRow) findViewById(R.id.word_test_sm2_correct_sm2_buttons);

		nextButton.setVisibility(View.VISIBLE);

		sm2QualityTitleTableRow.setVisibility(View.GONE);
		sm2IncorrectButtonsTableRow.setVisibility(View.GONE);
		sm2CorrectButtonsTableRow.setVisibility(View.GONE);

		if (wordTestSM2Config.getWordTestSM2Mode() == WordTestSM2Mode.INPUT) {

			titleTextView.setText(getResources().getString(R.string.word_test_sm2_view_input_label));

		} else if (wordTestSM2Config.getWordTestSM2Mode() == WordTestSM2Mode.CHOOSE) {

			titleTextView.setText(getResources().getString(R.string.word_test_sm2_view_choose_think_label));

		} else {
			throw new RuntimeException("Unknown wordTestSM2Mode: " + wordTestSM2Config.getWordTestSM2Mode());
		}

		currentNextWordStat = wordTestSM2Manager.getNextWordStat(wordTestSM2Config.getMaxNewWords());

		if (currentNextWordStat == null) {

			AlertDialog alertDialog = new AlertDialog.Builder(WordTestSM2.this).create();

			alertDialog.setMessage(getString(R.string.word_test_sm2_no_more_words));

			alertDialog.setCancelable(false);
			alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

					finish();
				}
			});

			alertDialog.show();

		} else {

			currentWordDictionaryEntry = dictionaryManager.getDictionaryEntryById(currentNextWordStat.getId());

			String kanji = currentWordDictionaryEntry.getKanji();
			String prefixKana = currentWordDictionaryEntry.getPrefixKana();

			TextView kanjiLabel = (TextView) findViewById(R.id.word_test_sm2_kanji_label);
			EditText kanjiPrefix = (EditText) findViewById(R.id.word_test_sm2_kanji_prefix);
			EditText kanjiInput = (EditText) findViewById(R.id.word_test_sm2_kanji_input);

			if (kanji != null) {
				kanjiInput.setText(kanji);
				kanjiPrefix.setText(prefixKana);
			} else {
				kanjiInput.setText("");
				kanjiPrefix.setText("");
			}

			if (kanji != null && wordTestSM2Config.getShowKanji() != null
					&& wordTestSM2Config.getShowKanji().equals(Boolean.TRUE) == true) {

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

					if (wordTestSM2Config.getWordTestSM2Mode() == WordTestSM2Mode.INPUT
							|| (wordTestSM2Config.getShowKana() != null && wordTestSM2Config.getShowKana().equals(
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

					if (wordTestSM2Config.getWordTestSM2Mode() == WordTestSM2Mode.INPUT) {

						currentTextViewAndEditText.editPrefix.setFocusable(true);
						currentTextViewAndEditText.textView.setFocusable(true);
						currentTextViewAndEditText.editText.setFocusable(true);

						currentTextViewAndEditText.editText.setText("");

						currentTextViewAndEditText.editText.setEnabled(true);

						currentTextViewAndEditText.editText.setSingleLine(true);
						currentTextViewAndEditText.editText.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);

						if (kanaListIdx == 0) {
							currentTextViewAndEditText.editText.requestFocus();
						}

					} else if (wordTestSM2Config.getWordTestSM2Mode() == WordTestSM2Mode.CHOOSE) {

						currentTextViewAndEditText.editPrefix.setFocusable(false);
						currentTextViewAndEditText.textView.setFocusable(false);
						currentTextViewAndEditText.editText.setFocusable(false);

						currentTextViewAndEditText.editText.setText(currentKana);

						currentTextViewAndEditText.editText.setEnabled(false);

						currentTextViewAndEditText.editText.setSingleLine(false);
						currentTextViewAndEditText.editText.setInputType(InputType.TYPE_CLASS_TEXT
								| InputType.TYPE_TEXT_FLAG_MULTI_LINE);

					} else {
						throw new RuntimeException("Unknown wordTestSM2Mode: " + wordTestSM2Config.getWordTestSM2Mode());
					}

				} else {
					currentTextViewAndEditText.textView.setVisibility(View.GONE);

					currentTextViewAndEditText.editPrefix.setVisibility(View.GONE);
					currentTextViewAndEditText.editText.setVisibility(View.GONE);
					currentTextViewAndEditText.editText.setText("");
				}
			}

			TextView translateLabel = (TextView) findViewById(R.id.word_test_sm2_translate_label);
			EditText translateInput = (EditText) findViewById(R.id.word_test_sm2_translate_input);

			translateInput.setText(ListUtil.getListAsString(currentWordDictionaryEntry.getTranslates(), "\n"));
			translateInput.setEnabled(false);

			if (wordTestSM2Config.getShowTranslate() != null
					&& wordTestSM2Config.getShowTranslate().equals(Boolean.TRUE) == true) {
				translateLabel.setVisibility(View.VISIBLE);
				translateInput.setVisibility(View.VISIBLE);
			} else {
				translateLabel.setVisibility(View.GONE);
				translateInput.setVisibility(View.GONE);
			}

			TextView additionalInfoLabel = (TextView) findViewById(R.id.word_test_sm2_additional_info_label);
			EditText additionalInfoInput = (EditText) findViewById(R.id.word_test_sm2_additional_info_input);

			String additionalInfo = currentWordDictionaryEntry.getFullInfo();

			if (additionalInfo != null && wordTestSM2Config.getShowAdditionalInfo() != null
					&& wordTestSM2Config.getShowAdditionalInfo().equals(Boolean.TRUE) == true) {
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

			TextView state = (TextView) findViewById(R.id.word_test_sm2_state_info);

			Resources resources = getResources();

			state.setText(resources.getString(R.string.word_test_sm2_state,
					wordTestSM2Manager.getNextWordSize(wordTestSM2Config.getMaxNewWords())));
		}
	}

	private void showSM2Buttons() {

		Button nextButton = (Button) findViewById(R.id.word_test_sm2_next_button);

		TableRow sm2QualityTitleTableRow = (TableRow) findViewById(R.id.word_test_sm2_quality_title_table_row);
		TableRow sm2IncorrectButtonsTableRow = (TableRow) findViewById(R.id.word_test_sm2_incorrect_sm2_buttons);
		TableRow sm2CorrectButtonsTableRow = (TableRow) findViewById(R.id.word_test_sm2_correct_sm2_buttons);

		TextView qualityTitleTextView = (TextView) findViewById(R.id.word_test_sm2_quality_title);

		nextButton.setVisibility(View.GONE);
		sm2QualityTitleTableRow.setVisibility(View.VISIBLE);

		final WordTestSM2Config wordTestSM2Config = JapaneseAndroidLearnHelperApplication.getInstance()
				.getConfigManager(WordTestSM2.this).getWordTestSM2Config();

		WordTestSM2Mode wordTestSM2Mode = wordTestSM2Config.getWordTestSM2Mode();

		// buttons
		Button incorrectSM2ButtonQuality0 = (Button) findViewById(R.id.word_test_sm2_incorrect_sm2_buttons_quality0);
		Button incorrectSM2ButtonQuality1 = (Button) findViewById(R.id.word_test_sm2_incorrect_sm2_buttons_quality1);
		Button incorrectSM2ButtonQuality2 = (Button) findViewById(R.id.word_test_sm2_incorrect_sm2_buttons_quality2);

		Button correctSM2ButtonQuality3 = (Button) findViewById(R.id.word_test_sm2_correct_sm2_buttons_quality3);
		Button correctSM2ButtonQuality4 = (Button) findViewById(R.id.word_test_sm2_correct_sm2_buttons_quality4);
		Button correctSM2ButtonQuality5 = (Button) findViewById(R.id.word_test_sm2_correct_sm2_buttons_quality5);

		if (wordTestSM2Mode == WordTestSM2Mode.INPUT) {

			if (inputWasCorrectAnswer == false) {

				qualityTitleTextView.setText(getString(R.string.word_test_sm2_view_input_incorrect_label));

				sm2IncorrectButtonsTableRow.setVisibility(View.VISIBLE);
				sm2CorrectButtonsTableRow.setVisibility(View.VISIBLE);

				correctSM2ButtonQuality4.setEnabled(false);
				correctSM2ButtonQuality5.setEnabled(false);

			} else {

				qualityTitleTextView.setText(getString(R.string.word_test_sm2_view_input_correct_label));

				sm2IncorrectButtonsTableRow.setVisibility(View.VISIBLE);
				sm2CorrectButtonsTableRow.setVisibility(View.VISIBLE);

				correctSM2ButtonQuality4.setEnabled(true);
				correctSM2ButtonQuality5.setEnabled(true);
			}

		} else if (wordTestSM2Mode == WordTestSM2Mode.CHOOSE) {

			qualityTitleTextView.setText(getString(R.string.word_test_sm2_view_choose_think_result_label));

			sm2IncorrectButtonsTableRow.setVisibility(View.VISIBLE);
			sm2CorrectButtonsTableRow.setVisibility(View.VISIBLE);

		} else {
			throw new RuntimeException("Unknown wordTestSM2Mode: " + wordTestSM2Config.getWordTestSM2Mode());
		}

		// actions
		incorrectSM2ButtonQuality0.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processSM2RecallResult(0);
			}
		});

		incorrectSM2ButtonQuality1.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processSM2RecallResult(1);
			}
		});

		incorrectSM2ButtonQuality2.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processSM2RecallResult(2);
			}
		});

		correctSM2ButtonQuality3.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processSM2RecallResult(3);
			}
		});

		correctSM2ButtonQuality4.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processSM2RecallResult(4);
			}
		});

		correctSM2ButtonQuality5.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				processSM2RecallResult(5);
			}
		});
	}

	private void processSM2RecallResult(int quality) {

		currentNextWordStat.processRecallResult(quality);

		wordTestSM2Manager.updateWordStat(currentNextWordStat);

		fillScreen();
	}

	private void createTextViewAndEditTextForWordAsArray(final int lastAnswerIdx) {

		TextView wordLabel1 = (TextView) findViewById(R.id.word_test_sm2_word_label1);
		EditText wordPrefix1 = (EditText) findViewById(R.id.word_test_sm2_word_prefix1);
		EditText wordInput1 = (EditText) findViewById(R.id.word_test_sm2_word_input1);

		TextView wordLabel2 = (TextView) findViewById(R.id.word_test_sm2_word_label2);
		EditText wordPrefix2 = (EditText) findViewById(R.id.word_test_sm2_word_prefix2);
		EditText wordInput2 = (EditText) findViewById(R.id.word_test_sm2_word_input2);

		TextView wordLabel3 = (TextView) findViewById(R.id.word_test_sm2_word_label3);
		EditText wordPrefix3 = (EditText) findViewById(R.id.word_test_sm2_word_prefix3);
		EditText wordInput3 = (EditText) findViewById(R.id.word_test_sm2_word_input3);

		TextView wordLabel4 = (TextView) findViewById(R.id.word_test_sm2_word_label4);
		EditText wordPrefix4 = (EditText) findViewById(R.id.word_test_sm2_word_prefix4);
		EditText wordInput4 = (EditText) findViewById(R.id.word_test_sm2_word_input4);

		TextView wordLabel5 = (TextView) findViewById(R.id.word_test_sm2_word_label5);
		EditText wordPrefix5 = (EditText) findViewById(R.id.word_test_sm2_word_prefix5);
		EditText wordInput5 = (EditText) findViewById(R.id.word_test_sm2_word_input5);

		TextView wordLabel6 = (TextView) findViewById(R.id.word_test_sm2_word_label6);
		EditText wordPrefix6 = (EditText) findViewById(R.id.word_test_sm2_word_prefix6);
		EditText wordInput6 = (EditText) findViewById(R.id.word_test_sm2_word_input6);

		TextView wordLabel7 = (TextView) findViewById(R.id.word_test_sm2_word_label7);
		EditText wordPrefix7 = (EditText) findViewById(R.id.word_test_sm2_word_prefix7);
		EditText wordInput7 = (EditText) findViewById(R.id.word_test_sm2_word_input7);

		TextView wordLabel8 = (TextView) findViewById(R.id.word_test_sm2_word_label8);
		EditText wordPrefix8 = (EditText) findViewById(R.id.word_test_sm2_word_prefix8);
		EditText wordInput8 = (EditText) findViewById(R.id.word_test_sm2_word_input8);

		TextView wordLabel9 = (TextView) findViewById(R.id.word_test_sm2_word_label9);
		EditText wordPrefix9 = (EditText) findViewById(R.id.word_test_sm2_word_prefix9);
		EditText wordInput9 = (EditText) findViewById(R.id.word_test_sm2_word_input9);

		TextView wordLabel10 = (TextView) findViewById(R.id.word_test_sm2_word_label10);
		EditText wordPrefix10 = (EditText) findViewById(R.id.word_test_sm2_word_prefix10);
		EditText wordInput10 = (EditText) findViewById(R.id.word_test_sm2_word_input10);

		TextView wordLabel11 = (TextView) findViewById(R.id.word_test_sm2_word_label11);
		EditText wordPrefix11 = (EditText) findViewById(R.id.word_test_sm2_word_prefix11);
		EditText wordInput11 = (EditText) findViewById(R.id.word_test_sm2_word_input11);

		TextView wordLabel12 = (TextView) findViewById(R.id.word_test_sm2_word_label12);
		EditText wordPrefix12 = (EditText) findViewById(R.id.word_test_sm2_word_prefix12);
		EditText wordInput12 = (EditText) findViewById(R.id.word_test_sm2_word_input12);

		TextView wordLabel13 = (TextView) findViewById(R.id.word_test_sm2_word_label13);
		EditText wordPrefix13 = (EditText) findViewById(R.id.word_test_sm2_word_prefix13);
		EditText wordInput13 = (EditText) findViewById(R.id.word_test_sm2_word_input13);

		TextView wordLabel14 = (TextView) findViewById(R.id.word_test_sm2_word_label14);
		EditText wordPrefix14 = (EditText) findViewById(R.id.word_test_sm2_word_prefix14);
		EditText wordInput14 = (EditText) findViewById(R.id.word_test_sm2_word_input14);

		TextView wordLabel15 = (TextView) findViewById(R.id.word_test_sm2_word_label15);
		EditText wordPrefix15 = (EditText) findViewById(R.id.word_test_sm2_word_prefix15);
		EditText wordInput15 = (EditText) findViewById(R.id.word_test_sm2_word_input15);

		TextView wordLabel16 = (TextView) findViewById(R.id.word_test_sm2_word_label16);
		EditText wordPrefix16 = (EditText) findViewById(R.id.word_test_sm2_word_prefix16);
		EditText wordInput16 = (EditText) findViewById(R.id.word_test_sm2_word_input16);

		TextView wordLabel17 = (TextView) findViewById(R.id.word_test_sm2_word_label17);
		EditText wordPrefix17 = (EditText) findViewById(R.id.word_test_sm2_word_prefix17);
		EditText wordInput17 = (EditText) findViewById(R.id.word_test_sm2_word_input17);

		TextView wordLabel18 = (TextView) findViewById(R.id.word_test_sm2_word_label18);
		EditText wordPrefix18 = (EditText) findViewById(R.id.word_test_sm2_word_prefix18);
		EditText wordInput18 = (EditText) findViewById(R.id.word_test_sm2_word_input18);

		TextView wordLabel19 = (TextView) findViewById(R.id.word_test_sm2_word_label19);
		EditText wordPrefix19 = (EditText) findViewById(R.id.word_test_sm2_word_prefix19);
		EditText wordInput19 = (EditText) findViewById(R.id.word_test_sm2_word_input19);

		TextView wordLabel20 = (TextView) findViewById(R.id.word_test_sm2_word_label20);
		EditText wordPrefix20 = (EditText) findViewById(R.id.word_test_sm2_word_prefix20);
		EditText wordInput20 = (EditText) findViewById(R.id.word_test_sm2_word_input20);

		TextView wordLabel21 = (TextView) findViewById(R.id.word_test_sm2_word_label21);
		EditText wordPrefix21 = (EditText) findViewById(R.id.word_test_sm2_word_prefix21);
		EditText wordInput21 = (EditText) findViewById(R.id.word_test_sm2_word_input21);

		TextView wordLabel22 = (TextView) findViewById(R.id.word_test_sm2_word_label22);
		EditText wordPrefix22 = (EditText) findViewById(R.id.word_test_sm2_word_prefix22);
		EditText wordInput22 = (EditText) findViewById(R.id.word_test_sm2_word_input22);

		TextView wordLabel23 = (TextView) findViewById(R.id.word_test_sm2_word_label23);
		EditText wordPrefix23 = (EditText) findViewById(R.id.word_test_sm2_word_prefix23);
		EditText wordInput23 = (EditText) findViewById(R.id.word_test_sm2_word_input23);

		TextView wordLabel24 = (TextView) findViewById(R.id.word_test_sm2_word_label24);
		EditText wordPrefix24 = (EditText) findViewById(R.id.word_test_sm2_word_prefix24);
		EditText wordInput24 = (EditText) findViewById(R.id.word_test_sm2_word_input24);

		TextView wordLabel25 = (TextView) findViewById(R.id.word_test_sm2_word_label25);
		EditText wordPrefix25 = (EditText) findViewById(R.id.word_test_sm2_word_prefix25);
		EditText wordInput25 = (EditText) findViewById(R.id.word_test_sm2_word_input25);
		
		TextView wordLabel26 = (TextView) findViewById(R.id.word_test_sm2_word_label26);
		EditText wordPrefix26 = (EditText) findViewById(R.id.word_test_sm2_word_prefix26);
		EditText wordInput26 = (EditText) findViewById(R.id.word_test_sm2_word_input26);

		TextView wordLabel27 = (TextView) findViewById(R.id.word_test_sm2_word_label27);
		EditText wordPrefix27 = (EditText) findViewById(R.id.word_test_sm2_word_prefix27);
		EditText wordInput27 = (EditText) findViewById(R.id.word_test_sm2_word_input27);

		TextView wordLabel28 = (TextView) findViewById(R.id.word_test_sm2_word_label28);
		EditText wordPrefix28 = (EditText) findViewById(R.id.word_test_sm2_word_prefix28);
		EditText wordInput28 = (EditText) findViewById(R.id.word_test_sm2_word_input28);

		TextView wordLabel29 = (TextView) findViewById(R.id.word_test_sm2_word_label29);
		EditText wordPrefix29 = (EditText) findViewById(R.id.word_test_sm2_word_prefix29);
		EditText wordInput29 = (EditText) findViewById(R.id.word_test_sm2_word_input29);

		TextView wordLabel30 = (TextView) findViewById(R.id.word_test_sm2_word_label30);
		EditText wordPrefix30 = (EditText) findViewById(R.id.word_test_sm2_word_prefix30);
		EditText wordInput30 = (EditText) findViewById(R.id.word_test_sm2_word_input30);
		
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
