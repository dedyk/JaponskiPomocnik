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
		
		// random check box
		final CheckBox randomCheckBox = (CheckBox)findViewById(R.id.word_test_options_random);
		
		randomCheckBox.setChecked(wordTestConfig.getRandom());

		// until sucess check box
		final CheckBox untilSuccessCheckBox = (CheckBox)findViewById(R.id.word_test_options_until_success);
		
		untilSuccessCheckBox.setChecked(wordTestConfig.getUntilSuccess());
		
		// show kanji check box
		final CheckBox showKanjiCheckBox = (CheckBox)findViewById(R.id.word_test_options_show_kanji);
		
		showKanjiCheckBox.setChecked(wordTestConfig.getUntilSuccess());
		
		// loading word groups
		final List<CheckBox> wordGroupCheckBoxList = new ArrayList<CheckBox>();
		
		final List<String> groupsNames = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(getResources(), getAssets()).getDictionaryEntryGroupTypes();
		
		Set<String> chosenWordGroups = wordTestConfig.getChosenWordGroups();
				
		for (int groupsNamesIdx = 0; groupsNamesIdx < groupsNames.size(); ++groupsNamesIdx) {
			
			CheckBox currentWordGroupCheckBox = new CheckBox(this);
			
			currentWordGroupCheckBox.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

			currentWordGroupCheckBox.setTextSize(12);
			
			currentWordGroupCheckBox.setText(groupsNames.get(groupsNamesIdx));
			
			if (chosenWordGroups != null && chosenWordGroups.contains(groupsNames.get(groupsNamesIdx))) {
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
								
				// random
				boolean random = randomCheckBox.isChecked();
				
				wordTestConfig.setRandom(random);
				
				// until success
				boolean untilSuccess = untilSuccessCheckBox.isChecked();
				
				wordTestConfig.setUntilSuccess(untilSuccess);
				
				// show kanji
				boolean showKanji = showKanjiCheckBox.isChecked();
				
				wordTestConfig.setShowKanji(showKanji);
				
				// groups
				List<DictionaryEntry> chosenAllDictionaryEntryList = new ArrayList<DictionaryEntry>();
				
				List<String> chosenWordGroupsNumberList = new ArrayList<String>();
				
				for (int wordGroupCheckBoxListIdx = 0; wordGroupCheckBoxListIdx < wordGroupCheckBoxList.size(); ++wordGroupCheckBoxListIdx) {
					
					CheckBox currentWordGroupCheckBox = wordGroupCheckBoxList.get(wordGroupCheckBoxListIdx);
					
					if (currentWordGroupCheckBox.isChecked() == true) {
						
						List<DictionaryEntry> currentWordsGroupDictionaryEntryList = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(getResources(), getAssets()).
								getGroupDictionaryEntries(groupsNames.get(wordGroupCheckBoxListIdx));
												
						for (int repeatIdx = 0; repeatIdx < repeatNumber; ++repeatIdx) {
							chosenAllDictionaryEntryList.addAll(currentWordsGroupDictionaryEntryList);
						}
						
						chosenWordGroupsNumberList.add(groupsNames.get(wordGroupCheckBoxListIdx));
					}
				}
				
				if (chosenAllDictionaryEntryList.size() == 0) {
					
					Toast toast = Toast.makeText(WordTestOptions.this, getString(R.string.word_test_options_word_group_no_chosen), Toast.LENGTH_SHORT);

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
				TextView optionsOther = (TextView)findViewById(R.id.word_test_options_other);
				TextView optionsGroup = (TextView)findViewById(R.id.word_test_options_group);

				detailsSb.append("***" + optionsRepeat.getText() + "***\n\n");
				detailsSb.append(repeatNumberEditText.getText().toString()).append("\n\n");

				detailsSb.append("***" + optionsOther.getText() + "***\n\n");
				detailsSb.append(randomCheckBox.isChecked() + " - " + randomCheckBox.getText()).append("\n\n");
				detailsSb.append(untilSuccessCheckBox.isChecked() + " - " + untilSuccessCheckBox.getText()).append("\n\n");
				detailsSb.append(showKanjiCheckBox.isChecked() + " - " + showKanjiCheckBox.getText()).append("\n\n");

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
