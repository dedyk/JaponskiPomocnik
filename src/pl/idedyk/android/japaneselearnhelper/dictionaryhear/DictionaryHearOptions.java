package pl.idedyk.android.japaneselearnhelper.dictionaryhear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.DictionaryHearConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperDictionaryHearContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.tts.TtsConnector;
import pl.idedyk.android.japaneselearnhelper.tts.TtsLanguage;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class DictionaryHearOptions extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dictionary_hear_options);
		
		final DictionaryHearConfig dictionaryHearConfig = ConfigManager.getInstance().getDictionaryHearConfig();
		
		final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.dictionary_hear_options_main_layout);
		
		// get repeat number
		final EditText repeatNumberEditText = (EditText)findViewById(R.id.dictionary_hear_options_repeat_number_edit_text);
		
		repeatNumberEditText.setText(String.valueOf(dictionaryHearConfig.getRepeatNumber()));
		
		final CheckBox randomCheckBox = (CheckBox)findViewById(R.id.dictionary_hear_options_random);
		
		randomCheckBox.setChecked(dictionaryHearConfig.getRandom());
		
		// loading word groups
		final List<CheckBox> wordGroupCheckBoxList = new ArrayList<CheckBox>();
		
		final int groupSize = 20;
		
		int wordGroupsNo = DictionaryManager.getInstance().getWordGroupsNo(groupSize);
		
		Set<Integer> chosenWordGroups = dictionaryHearConfig.getChosenWordGroups();

		for (int currentGroupNo = 0; currentGroupNo < wordGroupsNo; ++currentGroupNo) {
			
			CheckBox currentWordGroupCheckBox = new CheckBox(this);
			
			currentWordGroupCheckBox.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

			currentWordGroupCheckBox.setTextSize(12);
			
			int startPosition = currentGroupNo * groupSize + 1;
			int endPosition = 0;
			
			if (currentGroupNo != wordGroupsNo - 1) {
				endPosition = (currentGroupNo + 1) * groupSize;
			} else {
				List<DictionaryEntry> lastWordsGroup = DictionaryManager.getInstance().getWordsGroup(groupSize, currentGroupNo);
				
				endPosition = startPosition + lastWordsGroup.size();
			}
			
			currentWordGroupCheckBox.setText(getString(R.string.dictionary_hear_options_group_position, (currentGroupNo + 1), 
					startPosition, endPosition));
			
			if (chosenWordGroups != null && chosenWordGroups.contains(Integer.valueOf(currentGroupNo))) {
				currentWordGroupCheckBox.setChecked(true);
			}
						
			wordGroupCheckBoxList.add(currentWordGroupCheckBox);
			
			mainLayout.addView(currentWordGroupCheckBox, mainLayout.getChildCount());
		}
		
		final Button startButton = (Button)findViewById(R.id.dictionary_hear_start);
		
		// check TTS
		final ProgressDialog progressDialog = ProgressDialog.show(this, 
				getString(R.string.dictionary_hear_options_check_tts1),
				getString(R.string.dictionary_hear_options_check_tts2));
		
		class TtsInitResult {
			
			public boolean japaneseTtsResult;
			
			public boolean polishTtsResult;
		}
		
		class PrepareAsyncTask extends AsyncTask<Void, Void, TtsInitResult> {

			@Override
			protected TtsInitResult doInBackground(Void... params) {
				
				TtsInitResult ttsInitResult = new TtsInitResult();
				
				TtsConnector japanaeseTtsConnector = new TtsConnector(DictionaryHearOptions.this, TtsLanguage.JAPANESE);
				TtsConnector polishTtsConnector = new TtsConnector(DictionaryHearOptions.this, TtsLanguage.POLISH);
								
				boolean japaneseInitialized = isTtsConnectorInitialized(japanaeseTtsConnector);
				boolean polishInitialized = isTtsConnectorInitialized(polishTtsConnector);
				
				ttsInitResult.japaneseTtsResult = japaneseInitialized;
				ttsInitResult.polishTtsResult = polishInitialized;
								
				japanaeseTtsConnector.stop();
				polishTtsConnector.stop();
				
				return ttsInitResult;
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
			
			@Override
			protected void onPostExecute(TtsInitResult ttsInitResult) {
				super.onPostExecute(ttsInitResult);

				progressDialog.dismiss();
				
				// INFO: Tylko do testow
				// ttsInitResult.japaneseTtsResult = true;
				// ttsInitResult.polishTtsResult = true;
								
				if (ttsInitResult.japaneseTtsResult == true && ttsInitResult.polishTtsResult == true) {
					return;
				}
				
				AlertDialog alertDialog = new AlertDialog.Builder(DictionaryHearOptions.this).create();
				
				String message = null;
				
				if (ttsInitResult.japaneseTtsResult == false && ttsInitResult.polishTtsResult == false) {
					message = getString(R.string.tts_japanese_polish_error);
				} else if (ttsInitResult.japaneseTtsResult == true && ttsInitResult.polishTtsResult == false) {
					message = getString(R.string.tts_polish_error);
				} else if (ttsInitResult.japaneseTtsResult == false && ttsInitResult.polishTtsResult == true) {
					message = getString(R.string.tts_japanese_error);
				} else {
					throw new RuntimeException();
				}
				
				alertDialog.setMessage(message);
				alertDialog.setCancelable(false);

				alertDialog.setButton(getString(R.string.tts_error_ok), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// noop
					}
				});
				
				if (ttsInitResult.japaneseTtsResult == false) {
					alertDialog.setButton2(getString(R.string.tts_svox_google_play_go), new DialogInterface.OnClickListener() {
	
						public void onClick(DialogInterface dialog, int which) {
							
							Uri marketUri = Uri.parse(getString(R.string.tts_svox_market_url));
							
							Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
							
							startActivity(marketIntent);
						}
					});
				}
				
				if (ttsInitResult.polishTtsResult == false) {
					alertDialog.setButton3(getString(R.string.tts_ivona_google_play_go), new DialogInterface.OnClickListener() {
	
						public void onClick(DialogInterface dialog, int which) {
							
							Uri marketUri = Uri.parse(getString(R.string.tts_ivona_market_url));
							
							Intent marketIntent = new Intent(Intent.ACTION_VIEW, marketUri);
							
							startActivity(marketIntent);
						}
					});
				}
				
				alertDialog.show();	
				
				startButton.setEnabled(false);
			}
		}
		
		new PrepareAsyncTask().execute();
		
		// start action		
		startButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
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
					
					Toast toast = Toast.makeText(DictionaryHearOptions.this, getString(R.string.dictionary_hear_options_repeat_number_invalid), Toast.LENGTH_SHORT);

					toast.show();

					return;					
				}
				
				dictionaryHearConfig.setRepeatNumber(repeatNumber);
				
				boolean random = randomCheckBox.isChecked();
				
				dictionaryHearConfig.setRandom(random);
				
				List<DictionaryEntry> chosenAllDictionaryEntryList = new ArrayList<DictionaryEntry>();
				List<Integer> chosenWordGroupsNumberList = new ArrayList<Integer>();
				
				for (int wordGroupCheckBoxListIdx = 0; wordGroupCheckBoxListIdx < wordGroupCheckBoxList.size(); ++wordGroupCheckBoxListIdx) {
					
					CheckBox currentWordGroupCheckBox = wordGroupCheckBoxList.get(wordGroupCheckBoxListIdx);
					
					if (currentWordGroupCheckBox.isChecked() == true) {
						
						List<DictionaryEntry> currentWordsGroupDictionaryEntryList = DictionaryManager.getInstance().getWordsGroup(groupSize, wordGroupCheckBoxListIdx);
						
						for (int repeatIdx = 0; repeatIdx < repeatNumber; ++repeatIdx) {
							chosenAllDictionaryEntryList.addAll(currentWordsGroupDictionaryEntryList);
						}
						
						chosenWordGroupsNumberList.add(wordGroupCheckBoxListIdx);
					}
				}
				
				if (chosenAllDictionaryEntryList.size() == 0) {
					
					Toast toast = Toast.makeText(DictionaryHearOptions.this, getString(R.string.dictionary_hear_options_word_group_no_chosen), Toast.LENGTH_SHORT);

					toast.show();

					return;
				}
				
				dictionaryHearConfig.setChosenWordGroups(chosenWordGroupsNumberList);
				
				if (random == true) {
					Collections.shuffle(chosenAllDictionaryEntryList);
				}
				
				JapaneseAndroidLearnHelperDictionaryHearContext dictionaryHearContext = 
						JapaneseAndroidLearnHelperApplication.getInstance().getContext().getDictionaryHearContext();
				
				dictionaryHearContext.reset();
				
				dictionaryHearContext.setDictionaryEntryList(chosenAllDictionaryEntryList);
				
				finish();
				
				Intent intent = new Intent(getApplicationContext(), DictionaryHear.class);

				startActivity(intent);
			}
		});
		
		// report problem button
		Button reportProblemButton = (Button)findViewById(R.id.dictionary_hear_options_report_problem_button);

		reportProblemButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				StringBuffer detailsSb = new StringBuffer();
				
				TextView optionsRepeat = (TextView)findViewById(R.id.dictionary_hear_options_repeat);
				TextView optionsOther = (TextView)findViewById(R.id.dictionary_hear_options_other);
				TextView optionsGroup = (TextView)findViewById(R.id.dictionary_hear_group);

				detailsSb.append("***" + optionsRepeat.getText() + "***\n\n");
				detailsSb.append(repeatNumberEditText.getText().toString()).append("\n\n");

				detailsSb.append("***" + optionsOther.getText() + "***\n\n");
				detailsSb.append(randomCheckBox.isChecked() + " - " + randomCheckBox.getText()).append("\n\n");

				detailsSb.append("***" + optionsGroup.getText() + "***\n\n");
		
				for (CheckBox currentWordGroupCheckBox : wordGroupCheckBoxList) {

					String currentWordGroupCheckBoxText = currentWordGroupCheckBox.getText().toString();

					detailsSb.append(currentWordGroupCheckBox.isChecked() + " - " + currentWordGroupCheckBoxText).append("\n");
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

				Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString(), versionName, versionCode); 

				startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));
			}
		});
	}
}
