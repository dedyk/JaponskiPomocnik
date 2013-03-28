package pl.idedyk.android.japaneselearnhelper.testsm2;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.WordTestSM2Config;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.WordTestSM2Manager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class WordTestSM2Options extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuShorterHelper.onCreateOptionsMenu(menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_test_sm2_options);
		
		final WordTestSM2Config wordTestSM2Config = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getWordTestSM2Config();
		
		// get max new number
		final EditText maxNewWordsNumberEditText = (EditText)findViewById(R.id.word_test_sm2_options_max_new_words_edit_text);
		
		maxNewWordsNumberEditText.setText(String.valueOf(wordTestSM2Config.getMaxNewWords()));

		// get max repeat number
		final EditText maxRepeatWordsNumberEditText = (EditText)findViewById(R.id.word_test_sm2_options_max_repeat_words_edit_text);
		
		maxRepeatWordsNumberEditText.setText(String.valueOf(wordTestSM2Config.getMaxRepeatWords()));
		
		// show kanji check box
		final CheckBox showKanjiCheckBox = (CheckBox)findViewById(R.id.word_test_sm2_options_show_kanji);
		
		showKanjiCheckBox.setChecked(wordTestSM2Config.getShowKanji());

		// show kana check box
		final CheckBox showKanaCheckBox = (CheckBox)findViewById(R.id.word_test_sm2_options_show_kana);
		
		showKanaCheckBox.setChecked(wordTestSM2Config.getShowKana());
		
		// show translate check box
		final CheckBox showTranslateCheckBox = (CheckBox)findViewById(R.id.word_test_sm2_options_show_translate);
		
		showTranslateCheckBox.setChecked(wordTestSM2Config.getShowTranslate());
		
		// show additional info check box
		final CheckBox showAdditionalInfoCheckBox = (CheckBox)findViewById(R.id.word_test_sm2_options_show_additional_info);
		
		showAdditionalInfoCheckBox.setChecked(wordTestSM2Config.getShowAdditionalInfo());
		
		// test mode
		final RadioButton testModeInputRadioButton = (RadioButton)findViewById(R.id.word_test_sm2_options_test_mode_radiogroup_input);
		final RadioButton testModeChooseRadioButton = (RadioButton)findViewById(R.id.word_test_sm2_options_test_mode_radiogroup_choose);
		
		WordTestSM2Mode wordTestSM2Mode = wordTestSM2Config.getWordTestSM2Mode();
		
		if (wordTestSM2Mode == WordTestSM2Mode.INPUT) {
			testModeInputRadioButton.setChecked(true);
			
			showKanaCheckBox.setEnabled(false);
			showKanaCheckBox.setChecked(false);
			
		} else if (wordTestSM2Mode == WordTestSM2Mode.CHOOSE) {
			testModeChooseRadioButton.setChecked(true);
			
			showKanaCheckBox.setEnabled(true);
			showKanaCheckBox.setChecked(true);
			
			showTranslateCheckBox.setEnabled(false);
			showTranslateCheckBox.setChecked(false);
			
		} else {
			throw new RuntimeException("WordTestSM2Mode wordTestSM2Mode: " + wordTestSM2Mode);
		}	
		
		// actions
		
		testModeInputRadioButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showKanaCheckBox.setEnabled(false);
				showKanaCheckBox.setChecked(false);
				
				showTranslateCheckBox.setEnabled(true);
			}
		});

		testModeChooseRadioButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showKanaCheckBox.setEnabled(true);
				
				showTranslateCheckBox.setEnabled(false);
				showTranslateCheckBox.setChecked(false);
			}
		});
		
		// start button
		final Button startButton = (Button)findViewById(R.id.word_test_sm2_options_start);
		
		// start action		
		startButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				// max new words number
				String maxNewWordsNumberString = maxNewWordsNumberEditText.getText().toString();
				
				boolean maxNewWordsNumberError = false;
				
				int maxNewWordsNumber = -1;
				
				if (maxNewWordsNumberString == null) {
					maxNewWordsNumberError = true;
				} else {
					
					try {
						maxNewWordsNumber = Integer.parseInt(maxNewWordsNumberString);
					} catch (NumberFormatException e) {
						maxNewWordsNumberError = true;
					}
				}
				
				if (maxNewWordsNumberError == false && maxNewWordsNumber <= 0) {
					maxNewWordsNumberError = true;
				}
				
				if (maxNewWordsNumberError == true) {
					
					Toast toast = Toast.makeText(WordTestSM2Options.this, getString(R.string.word_test_sm2_options_max_new_words_number_invalid), Toast.LENGTH_SHORT);

					toast.show();

					return;					
				}
				
				wordTestSM2Config.setMaxNewWords(maxNewWordsNumber);

				// max repeat words number
				String maxRepeatWordsNumberString = maxRepeatWordsNumberEditText.getText().toString();
				
				boolean maxRepeatWordsNumberError = false;
				
				int maxRepeatWordsNumber = -1;
				
				if (maxRepeatWordsNumberString == null) {
					maxRepeatWordsNumberError = true;
				} else {
					
					try {
						maxRepeatWordsNumber = Integer.parseInt(maxRepeatWordsNumberString);
					} catch (NumberFormatException e) {
						maxRepeatWordsNumberError = true;
					}
				}
				
				if (maxRepeatWordsNumberError == false && maxRepeatWordsNumber <= 0) {
					maxRepeatWordsNumberError = true;
				}
				
				if (maxRepeatWordsNumberError == true) {
					
					Toast toast = Toast.makeText(WordTestSM2Options.this, getString(R.string.word_test_sm2_options_max_repeat_words_number_invalid), Toast.LENGTH_SHORT);

					toast.show();

					return;					
				}
				
				wordTestSM2Config.setMaxRepeatWords(maxRepeatWordsNumber);
				
				// test mode
				WordTestSM2Mode chosenWordTestSM2Mode = null;
				
				if (testModeInputRadioButton.isChecked() == true) {
					chosenWordTestSM2Mode = WordTestSM2Mode.INPUT;
				} else if (testModeChooseRadioButton.isChecked() == true) {
					chosenWordTestSM2Mode = WordTestSM2Mode.CHOOSE;
				} else {
					throw new RuntimeException("WordTestSM2Mode wordTestSM2Mode");
				}
				
				wordTestSM2Config.setWordTestSM2Mode(chosenWordTestSM2Mode);
								
				// show kanji
				boolean showKanji = showKanjiCheckBox.isChecked();
								
				wordTestSM2Config.setShowKanji(showKanji);
				
				// show kana
				boolean showKana = showKanaCheckBox.isChecked();
				
				wordTestSM2Config.setShowKana(showKana);
				
				// show translate
				boolean showTranslate = showTranslateCheckBox.isChecked();
				
				wordTestSM2Config.setShowTranslate(showTranslate);
				
				// show additional info
				boolean showAdditionalInfo = showAdditionalInfoCheckBox.isChecked();
				
				wordTestSM2Config.setAdditionalInfoTranslate(showAdditionalInfo);
				
				if (chosenWordTestSM2Mode == WordTestSM2Mode.INPUT && showKanji == false && showTranslate == false) {
					
					Toast toast = Toast.makeText(WordTestSM2Options.this, getString(R.string.word_test_sm2_options_no_kanji_translate), Toast.LENGTH_SHORT);

					toast.show();
					
					return;
				}

				if (chosenWordTestSM2Mode == WordTestSM2Mode.CHOOSE && showKanji == false && showKana == false) {
					
					Toast toast = Toast.makeText(WordTestSM2Options.this, getString(R.string.word_test_sm2_options_no_kanji_kana), Toast.LENGTH_SHORT);

					toast.show();
					
					return;
				}
				
				// prepare test
				final ProgressDialog progressDialog = ProgressDialog.show(WordTestSM2Options.this, 
						getString(R.string.word_test_sm2_options_prepare1),
						getString(R.string.word_test_sm2_options_prepare2));

				class PrepareAsyncTask extends AsyncTask<Void, Void, Void> {

					@Override
					protected Void doInBackground(Void... arg) {
						
						int versionCode = 0;

						try {
							PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

							versionCode = packageInfo.versionCode;

						} catch (NameNotFoundException e) {        	
						}
						
						DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(WordTestSM2Options.this);
						
						WordTestSM2Manager wordTestSM2Manager = dictionaryManager.getWordTestSM2Manager();
						
						Integer dbVersion = wordTestSM2Manager.getVersion();
						
						if (dbVersion == null || dbVersion.intValue() != versionCode) { // update db
							
							try {		
								wordTestSM2Manager.beginTransaction();
								
								int dictionaryEntriesSize = dictionaryManager.getDictionaryEntriesSize();
								
								for (int currentDictionaryEntryIdx = 1; currentDictionaryEntryIdx <= dictionaryEntriesSize; ++currentDictionaryEntryIdx) {
									
									boolean dictionaryEntryExistsInWordStat = wordTestSM2Manager.isDictionaryEntryExistsInWordStat(currentDictionaryEntryIdx);
									
									if (dictionaryEntryExistsInWordStat == false) {
										
										DictionaryEntry dictionaryEntry = dictionaryManager.getDictionaryEntryById(currentDictionaryEntryIdx);
										
										wordTestSM2Manager.insertDictionaryEntry(dictionaryEntry);
										
									} else {
										
										DictionaryEntry dictionaryEntry = dictionaryManager.getDictionaryEntryById(currentDictionaryEntryIdx);
										
										wordTestSM2Manager.updateDictionaryEntry(dictionaryEntry);
									}
								}
								
								wordTestSM2Manager.setVersion(versionCode);
								
								wordTestSM2Manager.commitTransaction();
								
							} finally {
								wordTestSM2Manager.endTransaction();
							}							
						}

						return null;
					}

					@Override
					protected void onPostExecute(Void arg) {
						super.onPostExecute(arg);

						progressDialog.dismiss();
						
						Intent intent = new Intent(getApplicationContext(), WordTestSM2.class);

						startActivity(intent);						
					}
				};
				
				new PrepareAsyncTask().execute();				
			}
		});
		
		// report problem button
		Button reportProblemButton = (Button)findViewById(R.id.word_test_sm2_options_report_problem_button);

		reportProblemButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				StringBuffer detailsSb = new StringBuffer();
				
				TextView optionsMaxNewWords = (TextView)findViewById(R.id.word_test_sm2_options_max_new_words);
				TextView optionsMaxRepeatWords = (TextView)findViewById(R.id.word_test_sm2_options_max_repeat_words);
				
				TextView optionsTestMode = (TextView)findViewById(R.id.word_test_sm2_options_test_mode);
				
				TextView optionsShow = (TextView)findViewById(R.id.word_test_sm2_options_show);

				detailsSb.append("*** " + optionsMaxNewWords.getText() + " ***\n\n");
				detailsSb.append(maxNewWordsNumberEditText.getText().toString()).append("\n\n");

				detailsSb.append("*** " + optionsMaxRepeatWords.getText() + " ***\n\n");
				detailsSb.append(maxRepeatWordsNumberEditText.getText().toString()).append("\n\n");

				detailsSb.append("*** " + optionsTestMode.getText() + " ***\n\n");
				detailsSb.append(testModeInputRadioButton.isChecked() + " - " + testModeInputRadioButton.getText()).append("\n\n");
				detailsSb.append(testModeChooseRadioButton.isChecked() + " - " + testModeChooseRadioButton.getText()).append("\n\n");
				
				detailsSb.append("*** " + optionsShow.getText() + " ***\n\n");
				detailsSb.append(showKanjiCheckBox.isChecked() + " - " + showKanjiCheckBox.getText()).append("\n\n");
				detailsSb.append(showKanaCheckBox.isChecked() + " - " + showKanaCheckBox.getText()).append("\n\n");
				detailsSb.append(showTranslateCheckBox.isChecked() + " - " + showTranslateCheckBox.getText()).append("\n\n");
				detailsSb.append(showAdditionalInfoCheckBox.isChecked() + " - " + showAdditionalInfoCheckBox.getText()).append("\n\n");
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);

				String mailSubject = getString(R.string.word_test_sm2_options_report_problem_email_subject);

				String mailBody = getString(R.string.word_test_sm2_options_report_problem_email_body,
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
	}	
}
