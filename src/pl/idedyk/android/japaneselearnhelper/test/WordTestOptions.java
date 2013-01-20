package pl.idedyk.android.japaneselearnhelper.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.WordTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperWordTestContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.GroupEnum;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

public class WordTestOptions extends Activity {
	
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
		
		setContentView(R.layout.word_test_options);
		
		final WordTestConfig wordTestConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getWordTestConfig();
		
		final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.word_test_options_main_layout);
		
		// get repeat number
		final EditText repeatNumberEditText = (EditText)findViewById(R.id.word_test_options_repeat_number_edit_text);
		
		repeatNumberEditText.setText(String.valueOf(wordTestConfig.getRepeatNumber()));
						
		// show kanji check box
		final CheckBox showKanjiCheckBox = (CheckBox)findViewById(R.id.word_test_options_show_kanji);
		
		showKanjiCheckBox.setChecked(wordTestConfig.getShowKanji());

		// show kana check box
		final CheckBox showKanaCheckBox = (CheckBox)findViewById(R.id.word_test_options_show_kana);
		
		showKanaCheckBox.setChecked(wordTestConfig.getShowKana());
		
		// show translate check box
		final CheckBox showTranslateCheckBox = (CheckBox)findViewById(R.id.word_test_options_show_translate);
		
		showTranslateCheckBox.setChecked(wordTestConfig.getShowTranslate());
		
		// test mode
		final RadioButton testModeInputRadioButton = (RadioButton)findViewById(R.id.word_test_options_test_mode_radiogroup_input);
		final RadioButton testModeOverviewRadioButton = (RadioButton)findViewById(R.id.word_test_options_test_mode_radiogroup_overview);
		
		WordTestMode wordTestMode = wordTestConfig.getWordTestMode();
		
		if (wordTestMode == WordTestMode.INPUT) {
			testModeInputRadioButton.setChecked(true);
			showKanaCheckBox.setEnabled(false);			
		} else if (wordTestMode == WordTestMode.OVERVIEW) {
			testModeOverviewRadioButton.setChecked(true);
			showKanaCheckBox.setEnabled(true);
		} else {
			throw new RuntimeException("WordTestMode wordTestMode: " + wordTestMode);
		}
		
		// random check box
		final CheckBox randomCheckBox = (CheckBox)findViewById(R.id.word_test_options_random);
		
		randomCheckBox.setChecked(wordTestConfig.getRandom());

		// until sucess check box
		final CheckBox untilSuccessCheckBox = (CheckBox)findViewById(R.id.word_test_options_until_success);
		
		untilSuccessCheckBox.setChecked(wordTestConfig.getUntilSuccess());
		
		// actions
		testModeInputRadioButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showKanaCheckBox.setEnabled(false);
			}
		});

		testModeOverviewRadioButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				showKanaCheckBox.setEnabled(true);	
			}
		});
		
		// loading word groups
		final List<CheckBox> wordGroupCheckBoxList = new ArrayList<CheckBox>();
		
		final List<GroupEnum> groupsNames = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(getResources(), getAssets()).getDictionaryEntryGroupTypes();
		
		Set<String> chosenWordGroups = wordTestConfig.getChosenWordGroups();
				
		for (int groupsNamesIdx = 0; groupsNamesIdx < groupsNames.size(); ++groupsNamesIdx) {
			
			CheckBox currentWordGroupCheckBox = new CheckBox(this);
			
			currentWordGroupCheckBox.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

			currentWordGroupCheckBox.setTextSize(12);
			
			currentWordGroupCheckBox.setText(groupsNames.get(groupsNamesIdx).getValue());
			
			if (chosenWordGroups != null && chosenWordGroups.contains(groupsNames.get(groupsNamesIdx).getValue())) {
				currentWordGroupCheckBox.setChecked(true);
			}
						
			wordGroupCheckBoxList.add(currentWordGroupCheckBox);
			
			mainLayout.addView(currentWordGroupCheckBox);
		}
		
		// start button
		final Button startButton = (Button)findViewById(R.id.word_test_options_start);
		
		// start action		
		startButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				// repeat
				String repeatNumberString = repeatNumberEditText.getText().toString();
				
				boolean repeatNumberError = false;
				
				int repeatNumber = -1;
				
				if (repeatNumberString == null) {
					repeatNumberError = true;
				} else {
					
					try {
						repeatNumber = Integer.parseInt(repeatNumberString);
					} catch (NumberFormatException e) {
						repeatNumberError = true;
					}
				}
				
				if (repeatNumberError == false && repeatNumber <= 0) {
					repeatNumberError = true;
				}
				
				if (repeatNumberError == true) {
					
					Toast toast = Toast.makeText(WordTestOptions.this, getString(R.string.word_test_options_repeat_number_invalid), Toast.LENGTH_SHORT);

					toast.show();

					return;					
				}
				
				wordTestConfig.setRepeatNumber(repeatNumber);
				
				// test mode
				WordTestMode chosenWordTestMode = null;
				
				if (testModeInputRadioButton.isChecked() == true) {
					chosenWordTestMode = WordTestMode.INPUT;
				} else if (testModeOverviewRadioButton.isChecked() == true) {
					chosenWordTestMode = WordTestMode.OVERVIEW;
				} else {
					throw new RuntimeException("WordTestConfig wordTestConfig");
				}
				
				wordTestConfig.setWordTestMode(chosenWordTestMode);
								
				// random
				boolean random = randomCheckBox.isChecked();
				
				wordTestConfig.setRandom(random);
				
				// until success
				boolean untilSuccess = untilSuccessCheckBox.isChecked();
				
				wordTestConfig.setUntilSuccess(untilSuccess);
				
				// show kanji
				boolean showKanji = showKanjiCheckBox.isChecked();
								
				wordTestConfig.setShowKanji(showKanji);
				
				// show kana
				boolean showKana = showKanaCheckBox.isChecked();
				
				wordTestConfig.setShowKana(showKana);
				
				// show translate
				boolean showTranslate = showTranslateCheckBox.isChecked();
				
				wordTestConfig.setShowTranslate(showTranslate);
				
				if (chosenWordTestMode == WordTestMode.INPUT && showKanji == false && showTranslate == false) {
					
					Toast toast = Toast.makeText(WordTestOptions.this, getString(R.string.word_test_options_no_kanji_translate), Toast.LENGTH_SHORT);

					toast.show();
					
					return;
				}

				if (chosenWordTestMode == WordTestMode.OVERVIEW && showKanji == false && showKana == false && showTranslate == false) {
					
					Toast toast = Toast.makeText(WordTestOptions.this, getString(R.string.word_test_options_no_kanji_kana_translate), Toast.LENGTH_SHORT);

					toast.show();
					
					return;
				}
				
				// groups
				List<DictionaryEntry> chosenAllDictionaryEntryList = new ArrayList<DictionaryEntry>();
				
				List<String> chosenWordGroupsNumberList = new ArrayList<String>();
				
				boolean wasFilteredWords = false;
				
				for (int wordGroupCheckBoxListIdx = 0; wordGroupCheckBoxListIdx < wordGroupCheckBoxList.size(); ++wordGroupCheckBoxListIdx) {
					
					CheckBox currentWordGroupCheckBox = wordGroupCheckBoxList.get(wordGroupCheckBoxListIdx);
					
					if (currentWordGroupCheckBox.isChecked() == true) {
						
						List<DictionaryEntry> currentWordsGroupDictionaryEntryList = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(getResources(), getAssets()).
								getGroupDictionaryEntries(groupsNames.get(wordGroupCheckBoxListIdx));
												
						for (int repeatIdx = 0; repeatIdx < repeatNumber; ++repeatIdx) {
							
							for (DictionaryEntry currentDictionaryEntry : currentWordsGroupDictionaryEntryList) {
								
								if (showTranslate == true || currentDictionaryEntry.isKanjiExists() == true) {							
									chosenAllDictionaryEntryList.add(currentDictionaryEntry);
								} else {
									wasFilteredWords = true;
								}
							}
						}
						
						chosenWordGroupsNumberList.add(groupsNames.get(wordGroupCheckBoxListIdx).getValue());
					}
				}
								
				if (chosenAllDictionaryEntryList.size() == 0) {
					
					Toast toast = null;
					
					if (wasFilteredWords == false) {
						toast = Toast.makeText(WordTestOptions.this, getString(R.string.word_test_options_word_group_no_chosen), Toast.LENGTH_SHORT);	
					} else {
						toast = Toast.makeText(WordTestOptions.this, getString(R.string.word_test_options_word_filtered), Toast.LENGTH_SHORT);
					}

					toast.show();

					return;
				}
				
				wordTestConfig.setChosenWordGroups(chosenWordGroupsNumberList);
				
				if (random == true) {
					Collections.shuffle(chosenAllDictionaryEntryList);
				}
				
				JapaneseAndroidLearnHelperWordTestContext wordTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getWordTestContext();
								
				wordTestContext.setWordsTest(chosenAllDictionaryEntryList);
				
				finish();
				
				Intent intent = new Intent(getApplicationContext(), WordTest.class);

				startActivity(intent);
			}
		});
		
		// report problem button
		Button reportProblemButton = (Button)findViewById(R.id.word_test_options_report_problem_button);

		reportProblemButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				StringBuffer detailsSb = new StringBuffer();
				
				TextView optionsRepeat = (TextView)findViewById(R.id.word_test_options_repeat);
				TextView optionsTestMode = (TextView)findViewById(R.id.word_test_options_test_mode);
				TextView optionsShow = (TextView)findViewById(R.id.word_test_options_show);
				TextView optionsOther = (TextView)findViewById(R.id.word_test_options_other);
				TextView optionsGroup = (TextView)findViewById(R.id.word_test_options_group);

				detailsSb.append("***" + optionsRepeat.getText() + "***\n\n");
				detailsSb.append(repeatNumberEditText.getText().toString()).append("\n\n");

				detailsSb.append("***" + optionsTestMode.getText() + "***\n\n");
				detailsSb.append(testModeInputRadioButton.isChecked() + " - " + testModeInputRadioButton.getText()).append("\n\n");
				detailsSb.append(testModeOverviewRadioButton.isChecked() + " - " + testModeOverviewRadioButton.getText()).append("\n\n");
				
				detailsSb.append("***" + optionsShow.getText() + "***\n\n");
				detailsSb.append(showKanjiCheckBox.isChecked() + " - " + showKanjiCheckBox.getText()).append("\n\n");
				detailsSb.append(showKanaCheckBox.isChecked() + " - " + showKanaCheckBox.getText()).append("\n\n");
				detailsSb.append(showTranslateCheckBox.isChecked() + " - " + showTranslateCheckBox.getText()).append("\n\n");
				
				detailsSb.append("***" + optionsOther.getText() + "***\n\n");
				detailsSb.append(randomCheckBox.isChecked() + " - " + randomCheckBox.getText()).append("\n\n");
				detailsSb.append(untilSuccessCheckBox.isChecked() + " - " + untilSuccessCheckBox.getText()).append("\n\n");

				detailsSb.append("***" + optionsGroup.getText() + "***\n\n");
		
				for (CheckBox currentWordGroupCheckBox : wordGroupCheckBoxList) {

					String currentWordGroupCheckBoxText = currentWordGroupCheckBox.getText().toString();

					detailsSb.append(currentWordGroupCheckBox.isChecked() + " - " + currentWordGroupCheckBoxText).append("\n");
				}

				String chooseEmailClientTitle = getString(R.string.choose_email_client);

				String mailSubject = getString(R.string.word_test_options_report_problem_email_subject);

				String mailBody = getString(R.string.word_test_options_report_problem_email_body,
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

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
