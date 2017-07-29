package pl.idedyk.android.japaneselearnhelper.dictionaryhear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.DictionaryHearConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperDictionaryHearContext;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.tts.TtsConnector;
import pl.idedyk.android.japaneselearnhelper.tts.TtsLanguage;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.AsyncTask;
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

public class DictionaryHearOptions extends Activity {
	
	private TtsConnector japanaeseTtsConnector;
	
	private TtsConnector polishTtsConnector;

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
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_dictionary_hear_options));

		setContentView(R.layout.dictionary_hear_options);

		final DictionaryHearConfig dictionaryHearConfig = JapaneseAndroidLearnHelperApplication.getInstance()
				.getConfigManager(this).getDictionaryHearConfig();

		final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.dictionary_hear_options_main_layout);

		// get repeat number
		final EditText repeatNumberEditText = (EditText) findViewById(R.id.dictionary_hear_options_repeat_number_edit_text);

		repeatNumberEditText.setText(String.valueOf(dictionaryHearConfig.getRepeatNumber()));

		final EditText delayNumberEditText = (EditText) findViewById(R.id.dictionary_hear_options_delay_number_edit_text);

		delayNumberEditText.setText(String.valueOf(dictionaryHearConfig.getDelayNumber()));

		final CheckBox randomCheckBox = (CheckBox) findViewById(R.id.dictionary_hear_options_random);

		randomCheckBox.setChecked(dictionaryHearConfig.getRandom());

		// loading word groups
		final List<CheckBox> wordGroupCheckBoxList = new ArrayList<CheckBox>();

		final List<GroupEnum> groupsNames = JapaneseAndroidLearnHelperApplication.getInstance()
				.getDictionaryManager(this).getDictionaryEntryGroupTypes();

		Set<String> chosenWordGroups = dictionaryHearConfig.getChosenWordGroups();

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

		final Button startButton = (Button) findViewById(R.id.dictionary_hear_start);

		// check TTS
		if (japanaeseTtsConnector != null) {
			japanaeseTtsConnector.stop();
		}
		
		japanaeseTtsConnector = new TtsConnector(DictionaryHearOptions.this, TtsLanguage.JAPANESE);

		if (polishTtsConnector != null) {
			polishTtsConnector.stop();
		}
		
		polishTtsConnector = new TtsConnector(DictionaryHearOptions.this, TtsLanguage.POLISH);
			
		// start action		
		startButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				
				final ProgressDialog progressDialog = ProgressDialog.show(DictionaryHearOptions.this,
						getString(R.string.dictionary_hear_options_check_tts1),
						getString(R.string.dictionary_hear_options_check_tts2));

				class PrepareAsyncTask extends AsyncTask<Void, Void, Void> {

					private boolean japaneseInitialized = false;
					private boolean polishInitialized = false;
					
					@Override
					protected Void doInBackground(Void... params) {
						
						japaneseInitialized = isTtsConnectorInitialized(japanaeseTtsConnector);
						polishInitialized = isTtsConnectorInitialized(polishTtsConnector);
						
						return null;
					}

					private boolean isTtsConnectorInitialized(TtsConnector ttsConnector) {

						for (int idx = 0; idx < 10; ++idx) {

							Boolean onInitResult = ttsConnector.getOnInitResult();

							if (onInitResult == null) {
								try {
									Thread.sleep(1000);
								} catch (InterruptedException e) {
								}

							} else {

								return onInitResult.booleanValue();
							}
						}

						return false;
					}

					@SuppressWarnings("deprecation")
					@Override
					protected void onPostExecute(Void voidNull) {
						
						super.onPostExecute(voidNull);

						progressDialog.dismiss();

						// INFO: Tylko do testow
						//				int warning = 0;
						//				japaneseInitialized = true;
						//				polishInitialized = true;
						
						if (japaneseInitialized == false || polishInitialized == false) { // wystapil blad
							
							AlertDialog alertDialog = new AlertDialog.Builder(DictionaryHearOptions.this).create();

							String message = null;

							if (japaneseInitialized == false && polishInitialized == false) {
								message = getString(R.string.tts_japanese_polish_error);
							} else if (japaneseInitialized == true && polishInitialized == false) {
								message = getString(R.string.tts_polish_error);
							} else if (japaneseInitialized == false && polishInitialized == true) {
								message = getString(R.string.tts_japanese_error);
							} else {
								throw new RuntimeException();
							}

							alertDialog.setMessage(message);
							alertDialog.setCancelable(false);

							alertDialog.setButton(getString(R.string.tts_error_ok), new DialogInterface.OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog, int which) {
									// noop
								}
							});

							if (japaneseInitialized == false || polishInitialized == false) {
								alertDialog.setButton2(getString(R.string.tts_google_android_tts),
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {

												Uri marketUri = Uri.parse(getString(R.string.tts_google_android_tts_url));

												Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);

												startActivity(marketIntent);
											}
										});
							}
							
							/*
							if (polishInitialized == false) {
								alertDialog.setButton3(getString(R.string.tts_ivona_google_play_go),
										new DialogInterface.OnClickListener() {

											@Override
											public void onClick(DialogInterface dialog, int which) {

												Uri marketUri = Uri.parse(getString(R.string.tts_ivona_market_url));

												Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);

												startActivity(marketIntent);
											}
										});
							}
							*/

							alertDialog.show();							
							
						} else { // ok
							
							// przygotowanie danych
							
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

								Toast toast = Toast.makeText(DictionaryHearOptions.this,
										getString(R.string.dictionary_hear_options_repeat_number_invalid), Toast.LENGTH_SHORT);

								toast.show();

								return;
							}

							dictionaryHearConfig.setRepeatNumber(repeatNumber);

							String delayNumberString = delayNumberEditText.getText().toString();

							boolean delayNumberError = false;

							int delayNumber = -1;

							if (delayNumberString == null) {
								delayNumberError = true;
							} else {

								try {
									delayNumber = Integer.parseInt(delayNumberString);
								} catch (NumberFormatException e) {
									delayNumberError = true;
								}
							}

							if (delayNumberError == false && delayNumber < 0) {
								delayNumberError = true;
							}

							if (delayNumberError == true) {

								Toast toast = Toast.makeText(DictionaryHearOptions.this,
										getString(R.string.dictionary_hear_options_delay_number_invalid), Toast.LENGTH_SHORT);

								toast.show();

								return;
							}

							dictionaryHearConfig.setDelayNumber(delayNumber);

							boolean random = randomCheckBox.isChecked();

							dictionaryHearConfig.setRandom(random);

							List<DictionaryEntry> chosenAllDictionaryEntryList = new ArrayList<DictionaryEntry>();

							List<GroupEnum> chosenWordGroupsNumberList = new ArrayList<GroupEnum>();

							for (int wordGroupCheckBoxListIdx = 0; wordGroupCheckBoxListIdx < wordGroupCheckBoxList.size(); ++wordGroupCheckBoxListIdx) {

								CheckBox currentWordGroupCheckBox = wordGroupCheckBoxList.get(wordGroupCheckBoxListIdx);

								if (currentWordGroupCheckBox.isChecked() == true) {

									List<DictionaryEntry> currentWordsGroupDictionaryEntryList = JapaneseAndroidLearnHelperApplication
											.getInstance().getDictionaryManager(DictionaryHearOptions.this)
											.getGroupDictionaryEntries(groupsNames.get(wordGroupCheckBoxListIdx));

									for (int repeatIdx = 0; repeatIdx < repeatNumber; ++repeatIdx) {
										chosenAllDictionaryEntryList.addAll(currentWordsGroupDictionaryEntryList);
									}

									chosenWordGroupsNumberList.add(groupsNames.get(wordGroupCheckBoxListIdx));
								}
							}

							if (chosenAllDictionaryEntryList.size() == 0) {

								Toast toast = Toast.makeText(DictionaryHearOptions.this,
										getString(R.string.dictionary_hear_options_word_group_no_chosen), Toast.LENGTH_SHORT);

								toast.show();

								return;
							}

							dictionaryHearConfig.setChosenWordGroups(GroupEnum.convertToValues(chosenWordGroupsNumberList));

							if (random == true) {
								Collections.shuffle(chosenAllDictionaryEntryList);
							}

							JapaneseAndroidLearnHelperDictionaryHearContext dictionaryHearContext = JapaneseAndroidLearnHelperApplication
									.getInstance().getContext().getDictionaryHearContext();

							dictionaryHearContext.reset();

							dictionaryHearContext.setDictionaryEntryList(chosenAllDictionaryEntryList);

							finish();

							Intent intent = new Intent(getApplicationContext(), DictionaryHear.class);

							startActivity(intent);							
						}						
					}
				}

				new PrepareAsyncTask().execute();
			}
		});

		// report problem button
		Button reportProblemButton = (Button) findViewById(R.id.dictionary_hear_options_report_problem_button);

		reportProblemButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				StringBuffer detailsSb = new StringBuffer();

				TextView optionsRepeat = (TextView) findViewById(R.id.dictionary_hear_options_repeat);
				TextView optionsOther = (TextView) findViewById(R.id.dictionary_hear_options_other);
				TextView optionsGroup = (TextView) findViewById(R.id.dictionary_hear_group);

				detailsSb.append("***" + optionsRepeat.getText() + "***\n\n");
				detailsSb.append(repeatNumberEditText.getText().toString()).append("\n\n");

				detailsSb.append("***" + optionsOther.getText() + "***\n\n");
				detailsSb.append(randomCheckBox.isChecked() + " - " + randomCheckBox.getText()).append("\n\n");

				detailsSb.append("***" + optionsGroup.getText() + "***\n\n");

				for (CheckBox currentWordGroupCheckBox : wordGroupCheckBoxList) {

					String currentWordGroupCheckBoxText = currentWordGroupCheckBox.getText().toString();

					detailsSb.append(currentWordGroupCheckBox.isChecked() + " - " + currentWordGroupCheckBoxText)
							.append("\n");
				}

				String chooseEmailClientTitle = getString(R.string.choose_email_client);

				String mailSubject = getString(R.string.dictionary_hear_options_report_problem_email_subject);

				String mailBody = getString(R.string.dictionary_hear_options_report_problem_email_body,
						detailsSb.toString());

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
			}
		});
	}
	
	@Override
	protected void onDestroy() {

		if (japanaeseTtsConnector != null) {
			japanaeseTtsConnector.stop();
		}

		if (polishTtsConnector != null) {
			polishTtsConnector.stop();
		}
		
		super.onDestroy();
	}

}
