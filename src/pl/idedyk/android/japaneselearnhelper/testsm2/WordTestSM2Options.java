package pl.idedyk.android.japaneselearnhelper.testsm2;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.WordTestSM2Config;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
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
				showKanaCheckBox.setChecked(true);
				
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
				
				
			}
		});
	}	
}
