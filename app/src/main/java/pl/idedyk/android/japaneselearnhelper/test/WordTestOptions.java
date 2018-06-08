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
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupItemEntity;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.utils.EntryOrderList;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
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
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_word_test_options));
		
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
		
		// show additional info check box
		final CheckBox showAdditionalInfoCheckBox = (CheckBox)findViewById(R.id.word_test_options_show_additional_info);
		
		showAdditionalInfoCheckBox.setChecked(wordTestConfig.getShowAdditionalInfo());
		
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
		
		final CheckBox untilSuccessNewWordLimitCheckBox = (CheckBox)findViewById(R.id.word_test_options_new_word_limit);
		
		untilSuccessCheckBox.setChecked(wordTestConfig.getUntilSuccess());
		untilSuccessNewWordLimitCheckBox.setChecked(wordTestConfig.getUntilSuccessNewWordLimit());
		
		setUntilSuccessNewWordLimitCheckBoxEnabled(untilSuccessCheckBox, untilSuccessNewWordLimitCheckBox);
		
		// actions
		untilSuccessCheckBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setUntilSuccessNewWordLimitCheckBoxEnabled(untilSuccessCheckBox, untilSuccessNewWordLimitCheckBox);				
			}
		});
		
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

		// groups
		final List<CheckBox> wordGroupCheckBoxList = new ArrayList<CheckBox>();

		// loading word groups
		final List<GroupEnum> groupsNames = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this).getDictionaryEntryGroupTypes();
		
		Set<String> chosenWordGroups = wordTestConfig.getChosenWordGroups();

		for (int groupsNamesIdx = 0; groupsNamesIdx < groupsNames.size(); ++groupsNamesIdx) {

			GroupEnum groupEnum = groupsNames.get(groupsNamesIdx);

			if (groupEnum == GroupEnum.OTHER) {
				continue;
			}

			CheckBox checkbox = createGroupCheckBox(groupEnum, null, groupEnum.getValue(),
					chosenWordGroups != null && chosenWordGroups.contains(groupsNames.get(groupsNamesIdx).getValue()));

			wordGroupCheckBoxList.add(checkbox);
			
			mainLayout.addView(checkbox, mainLayout.getChildCount() - 1);
		}

		// loading user word groups
		List<UserGroupEntity> allUserGroupList = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this).getDataManager().getAllUserGroupList();

		Set<Integer> chosenWordUserGroups = wordTestConfig.getChosenWordUserGroups();

		for (int allUserGroupListIdx = 0; allUserGroupListIdx < allUserGroupList.size(); ++allUserGroupListIdx) {

			UserGroupEntity userGroupEntity = allUserGroupList.get(allUserGroupListIdx);

			String checkboxText = userGroupEntity.getType() == UserGroupEntity.Type.USER_GROUP ? userGroupEntity.getName() :
					getString(R.string.user_group_star_group);

			CheckBox checkbox = createGroupCheckBox(null, userGroupEntity, checkboxText,
					chosenWordUserGroups != null && chosenWordUserGroups.contains(userGroupEntity.getId()));

			wordGroupCheckBoxList.add(checkbox);

			mainLayout.addView(checkbox);
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
				
				// until success new word limit
				boolean untilSuccessNewWordLimit = untilSuccessNewWordLimitCheckBox.isChecked();
				
				wordTestConfig.setUntilSuccessNewWordLimit(untilSuccessNewWordLimit);
				
				// show kanji
				boolean showKanji = showKanjiCheckBox.isChecked();
								
				wordTestConfig.setShowKanji(showKanji);
				
				// show kana
				boolean showKana = showKanaCheckBox.isChecked();
				
				wordTestConfig.setShowKana(showKana);
				
				// show translate
				boolean showTranslate = showTranslateCheckBox.isChecked();
				
				wordTestConfig.setShowTranslate(showTranslate);
				
				// show additional info
				boolean showAdditionalInfo = showAdditionalInfoCheckBox.isChecked();
				
				wordTestConfig.setAdditionalInfoTranslate(showAdditionalInfo);
				
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
				List<Integer> chosenWordUserGroupsNumberList = new ArrayList<Integer>();
				
				boolean wasFilteredWords = false;

				DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(WordTestOptions.this);

				for (int wordGroupCheckBoxListIdx = 0; wordGroupCheckBoxListIdx < wordGroupCheckBoxList.size(); ++wordGroupCheckBoxListIdx) {
					
					CheckBox currentWordGroupCheckBox = wordGroupCheckBoxList.get(wordGroupCheckBoxListIdx);
					
					if (currentWordGroupCheckBox.isChecked() == true) {

						CheckBoxTag currentWordGroupCheckBoxTag = (CheckBoxTag)currentWordGroupCheckBox.getTag();

						List<DictionaryEntry> currentWordsGroupDictionaryEntryList = null;

						if (currentWordGroupCheckBoxTag.groupType == CheckBoxGroupType.INTERNAL_GROUP) { // grupa wbudowana

							currentWordsGroupDictionaryEntryList = dictionaryManager.getGroupDictionaryEntries(currentWordGroupCheckBoxTag.groupEnum);

						} else if (currentWordGroupCheckBoxTag.groupType == CheckBoxGroupType.USER_GROUP) { // grupa uzytkownika

							List<UserGroupItemEntity> userGroupItemListForUserEntity = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(WordTestOptions.this).getDataManager().getUserGroupItemListForUserEntity(currentWordGroupCheckBoxTag.userGroupEntity);

							if (userGroupItemListForUserEntity == null) {
								userGroupItemListForUserEntity = new ArrayList<>();
							}

							currentWordsGroupDictionaryEntryList = new ArrayList<>();

							for (UserGroupItemEntity userGroupItemEntity : userGroupItemListForUserEntity) {

								if (userGroupItemEntity.getType() == UserGroupItemEntity.Type.DICTIONARY_ENTRY) {

									DictionaryEntry dictionaryEntry = dictionaryManager.getDictionaryEntryById(userGroupItemEntity.getItemId());

									if (dictionaryEntry != null) {
										currentWordsGroupDictionaryEntryList.add(dictionaryEntry);
									}
								}
							}
						}

						for (int repeatIdx = 0; repeatIdx < repeatNumber; ++repeatIdx) {

							for (DictionaryEntry currentDictionaryEntry : currentWordsGroupDictionaryEntryList) {

								if (chosenWordTestMode == WordTestMode.INPUT) {

									if (showTranslate == true || currentDictionaryEntry.isKanjiExists() == true) {
										chosenAllDictionaryEntryList.add(currentDictionaryEntry);
									} else {
										wasFilteredWords = true;
									}

								} else if (chosenWordTestMode == WordTestMode.OVERVIEW) {

									if (showKanji == true && showKana == false && showTranslate == false && currentDictionaryEntry.isKanjiExists() == false) {
										wasFilteredWords = true;
									} else if (showKanji == false && showKana == true && showTranslate == true && currentDictionaryEntry.isKanjiExists() == false) {
										wasFilteredWords = true;
									} else {
										chosenAllDictionaryEntryList.add(currentDictionaryEntry);
									}

								} else {
									throw new RuntimeException("WordTestConfig wordTestConfig: " + chosenWordTestMode);
								}
							}
						}

						if (currentWordGroupCheckBoxTag.groupType == CheckBoxGroupType.INTERNAL_GROUP) { // grupa wbudowana

							chosenWordGroupsNumberList.add(currentWordGroupCheckBoxTag.groupEnum.getValue());

						} else if (currentWordGroupCheckBoxTag.groupType == CheckBoxGroupType.USER_GROUP) { // grupa uzytkownika

							chosenWordUserGroupsNumberList.add(currentWordGroupCheckBoxTag.userGroupEntity.getId());

						}
					}
				}
								
				if (chosenAllDictionaryEntryList.size() == 0) {
					
					Toast toast = null;
					
					if (wasFilteredWords == false && chosenWordGroupsNumberList.size() == 0 && chosenWordUserGroupsNumberList.size() == 0) {
						toast = Toast.makeText(WordTestOptions.this, getString(R.string.word_test_options_word_group_no_chosen), Toast.LENGTH_SHORT);	
					} else {
						toast = Toast.makeText(WordTestOptions.this, getString(R.string.word_test_options_word_filtered), Toast.LENGTH_SHORT);
					}

					toast.show();

					return;
				}
				
				wordTestConfig.setChosenWordGroups(chosenWordGroupsNumberList);
				wordTestConfig.setChosenWordUserGroups(chosenWordUserGroupsNumberList);
				
				if (random == true) {
					Collections.shuffle(chosenAllDictionaryEntryList);
				}
				
				JapaneseAndroidLearnHelperWordTestContext wordTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getWordTestContext();
				
				EntryOrderList<DictionaryEntry> entryOrderList = null;
				
				if (untilSuccess == true && untilSuccessNewWordLimit == true) {
					entryOrderList = new EntryOrderList<DictionaryEntry>(chosenAllDictionaryEntryList, 10);
				} else {
					entryOrderList = new EntryOrderList<DictionaryEntry>(chosenAllDictionaryEntryList, chosenAllDictionaryEntryList.size());
				}				
				
				wordTestContext.setWordsTest(entryOrderList);
				
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
				TextView optionsGroupPleaseChoose = (TextView)findViewById(R.id.word_test_options_group_please_choose);
				TextView optionsGroupInternal = (TextView)findViewById(R.id.word_test_options_group_internal);
				TextView optionsGroupUser = (TextView)findViewById(R.id.word_test_options_group_user);

				detailsSb.append("*** " + optionsRepeat.getText() + " ***\n\n");
				detailsSb.append(repeatNumberEditText.getText().toString()).append("\n\n");

				detailsSb.append("*** " + optionsTestMode.getText() + " ***\n\n");
				detailsSb.append(testModeInputRadioButton.isChecked() + " - " + testModeInputRadioButton.getText()).append("\n\n");
				detailsSb.append(testModeOverviewRadioButton.isChecked() + " - " + testModeOverviewRadioButton.getText()).append("\n\n");
				
				detailsSb.append("*** " + optionsShow.getText() + " ***\n\n");
				detailsSb.append(showKanjiCheckBox.isChecked() + " - " + showKanjiCheckBox.getText()).append("\n\n");
				detailsSb.append(showKanaCheckBox.isChecked() + " - " + showKanaCheckBox.getText()).append("\n\n");
				detailsSb.append(showTranslateCheckBox.isChecked() + " - " + showTranslateCheckBox.getText()).append("\n\n");
				detailsSb.append(showAdditionalInfoCheckBox.isChecked() + " - " + showAdditionalInfoCheckBox.getText()).append("\n\n");
				
				detailsSb.append("*** " + optionsOther.getText() + " ***\n\n");
				detailsSb.append(randomCheckBox.isChecked() + " - " + randomCheckBox.getText()).append("\n\n");
				detailsSb.append(untilSuccessCheckBox.isChecked() + " - " + untilSuccessCheckBox.getText()).append("\n\n");
				detailsSb.append(untilSuccessNewWordLimitCheckBox.isChecked() + " - " + untilSuccessNewWordLimitCheckBox.getText()).append("\n\n");

				detailsSb.append("*** " + optionsGroupPleaseChoose.getText() + " ***\n\n");
				detailsSb.append("*** " + optionsGroupInternal.getText() + " ***\n\n");

				for (CheckBox currentWordGroupCheckBox : wordGroupCheckBoxList) {

					CheckBoxTag currentWordGroupCheckBoxTag = (CheckBoxTag)currentWordGroupCheckBox.getTag();

					if (currentWordGroupCheckBoxTag.groupType != CheckBoxGroupType.INTERNAL_GROUP) {
						continue;
					}

					String currentWordGroupCheckBoxText = currentWordGroupCheckBox.getText().toString();

					detailsSb.append(currentWordGroupCheckBox.isChecked() + " - " + currentWordGroupCheckBoxText).append("\n");
				}

				detailsSb.append("*** " + optionsGroupUser.getText() + " ***\n\n");

				for (CheckBox currentWordGroupCheckBox : wordGroupCheckBoxList) {

					CheckBoxTag currentWordGroupCheckBoxTag = (CheckBoxTag)currentWordGroupCheckBox.getTag();

					if (currentWordGroupCheckBoxTag.groupType != CheckBoxGroupType.USER_GROUP) {
						continue;
					}

					String currentWordGroupCheckBoxText = currentWordGroupCheckBox.getText().toString();

					detailsSb.append(currentWordGroupCheckBox.isChecked() + " - " + currentWordGroupCheckBoxText).append("\n");
				}

				//

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
	
	private void setUntilSuccessNewWordLimitCheckBoxEnabled(CheckBox untilSuccessCheckBox, CheckBox untilSuccessNewWordLimitCheckBox) {
		
		if (untilSuccessCheckBox.isChecked() == true) {
			untilSuccessNewWordLimitCheckBox.setEnabled(true);
		} else {
			untilSuccessNewWordLimitCheckBox.setEnabled(false);
		}
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private CheckBox createGroupCheckBox(GroupEnum groupEnum, UserGroupEntity userGroupEntity, String text, boolean checked) {

		CheckBox checkbox = new CheckBox(this);

		if ( 	(groupEnum == null && userGroupEntity == null) ||
				(groupEnum != null && userGroupEntity != null)) {
			throw new RuntimeException();
		}

		if (groupEnum != null) {
			checkbox.setTag(new CheckBoxTag(groupEnum));

		} else if (userGroupEntity != null) {
			checkbox.setTag(new CheckBoxTag(userGroupEntity));
		}

		checkbox.setLayoutParams(new LinearLayout.LayoutParams(
				LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

		checkbox.setTextSize(12);
		checkbox.setText(text);

		checkbox.setChecked(checked);

		return checkbox;
	}

	private static class CheckBoxTag {

		private CheckBoxGroupType groupType;

		private GroupEnum groupEnum;

		private UserGroupEntity userGroupEntity;

		public CheckBoxTag(GroupEnum groupEnum) {
			this.groupType = CheckBoxGroupType.INTERNAL_GROUP;
			this.groupEnum = groupEnum;
		}

		public CheckBoxTag(UserGroupEntity userGroupEntity) {
			this.groupType = CheckBoxGroupType.USER_GROUP;
			this.userGroupEntity = userGroupEntity;
		}
	}

	private static enum CheckBoxGroupType {

		INTERNAL_GROUP,

		USER_GROUP;
	}
}
