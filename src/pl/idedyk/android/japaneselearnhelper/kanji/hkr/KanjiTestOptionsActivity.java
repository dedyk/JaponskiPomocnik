package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordRequest;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordResult;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordResult.ResultItem;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class KanjiTestOptionsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		final KanjiTestConfig kanjiTestConfig = ConfigManager.getInstance().getKanjiTestConfig();

		setContentView(R.layout.kanji_test_options);

		final TextView testModeTextView = (TextView)findViewById(R.id.kanji_test_options_test_mode);

		final RadioButton testModeDrawKanjiFromMeaningRadioButton = (RadioButton)findViewById(R.id.kanji_test_options_test_mode_draw_kanji_from_meaning);
		final RadioButton testModeDrawKanjiFromInWord = (RadioButton)findViewById(R.id.kanji_test_options_test_mode_draw_kanji_in_word);

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			testModeDrawKanjiFromMeaningRadioButton.setChecked(true);
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			testModeDrawKanjiFromInWord.setChecked(true);
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");
		}

		final TextView otherOptionsTextView = (TextView)findViewById(R.id.kanji_test_options_other_options);

		final CheckBox untilSuccessCheckBox = (CheckBox)findViewById(R.id.kanji_test_options_until_success);

		untilSuccessCheckBox.setChecked(kanjiTestConfig.getUntilSuccess());

		final TextView chooseKanjiTextView = (TextView)findViewById(R.id.kanji_test_options_choose_kanji);

		final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.kanji_test_options_main_layout);
		
		final List<CheckBox> kanjiCheckBoxList = new ArrayList<CheckBox>();

		Button startTestButton = (Button)findViewById(R.id.kanji_test_options_start_test);

		startTestButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {

				if (testModeDrawKanjiFromMeaningRadioButton.isChecked() == true) {
					kanjiTestConfig.setKanjiTestMode(KanjiTestMode.DRAW_KANJI_FROM_MEANING);
				} else if (testModeDrawKanjiFromInWord.isChecked() == true) {
					kanjiTestConfig.setKanjiTestMode(KanjiTestMode.DRAW_KANJI_IN_WORD);
				} else {
					throw new RuntimeException("KanjiTestMode kanjiTestMode");
				}

				kanjiTestConfig.setUntilSuccess(untilSuccessCheckBox.isChecked());

				final List<KanjiEntry> kanjiEntryList = new ArrayList<KanjiEntry>();

				List<String> chosenKanjiList = new ArrayList<String>();

				for (CheckBox currentCheckBox : kanjiCheckBoxList) {

					if (currentCheckBox.isChecked() == true) {

						KanjiEntry currentCheckBoxKanjiEntry = (KanjiEntry)currentCheckBox.getTag();

						chosenKanjiList.add(currentCheckBoxKanjiEntry.getKanji());
						kanjiEntryList.add(currentCheckBoxKanjiEntry);						
					}
				}

				kanjiTestConfig.setChosenKanji(chosenKanjiList);

				if (chosenKanjiList.size() == 0) {

					Toast toast = Toast.makeText(KanjiTestOptionsActivity.this, getString(R.string.kanji_test_options_choose_kanji), Toast.LENGTH_SHORT);

					toast.show();

					return;
				}

				// prepare test
				final ProgressDialog progressDialog = ProgressDialog.show(KanjiTestOptionsActivity.this, 
						getString(R.string.kanji_test_options_prepare1),
						getString(R.string.kanji_test_options_prepare2));

				class PrepareAsyncTask extends AsyncTask<Void, Void, Void> {

					@Override
					protected Void doInBackground(Void... arg) {

						// get kanji test context
						JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanjiTestContext();

						// reset test
						kanjiTestContext.resetTest();

						Collections.shuffle(kanjiEntryList);

						// set kanji entry list in context
						kanjiTestContext.setKanjiEntryList(kanjiEntryList);

						if (kanjiTestConfig.getKanjiTestMode() == KanjiTestMode.DRAW_KANJI_IN_WORD) {

							FindWordRequest findWordRequest = new FindWordRequest();

							findWordRequest.searchKanji = true;
							findWordRequest.searchKana = false;
							findWordRequest.searchRomaji = false;
							findWordRequest.searchTranslate = false;
							findWordRequest.searchInfo = false;
							findWordRequest.searchGrammaFormAndExamples = false;
							findWordRequest.wordPlaceSearch = FindWordRequest.WordPlaceSearch.ANY_PLACE;
							findWordRequest.dictionaryEntryList = null;

							List<JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanjiList = 
									new ArrayList<JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji>();

							for (KanjiEntry currentKanjiEntry : kanjiEntryList) {

								findWordRequest.word = currentKanjiEntry.getKanji();

								// find word with this kanji
								FindWordResult findWordResult = DictionaryManager.getInstance().findWord(findWordRequest);

								List<ResultItem> findWordResultResult = findWordResult.result;

								for (ResultItem currentFindWordResultResult : findWordResultResult) {

									JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji currentDictionaryEntryWithRemovedKanji = 
											new JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji(currentFindWordResultResult.getDictionaryEntry(), currentKanjiEntry.getKanji());

									dictionaryEntryWithRemovedKanjiList.add(currentDictionaryEntryWithRemovedKanji);
								}	
							}

							Collections.shuffle(dictionaryEntryWithRemovedKanjiList);

							kanjiTestContext.setDictionaryEntryWithRemovedKanji(dictionaryEntryWithRemovedKanjiList);
						}

						return null;
					}

					@Override
					protected void onPostExecute(Void arg) {
						super.onPostExecute(arg);

						progressDialog.dismiss();

						Intent intent = new Intent(getApplicationContext(), KanjiTest.class);

						startActivity(intent);

						finish();
					}
				}

				new PrepareAsyncTask().execute();				
			}
		});

		Button reportProblemButton = (Button)findViewById(R.id.kanji_test_options_report_problem_button);

		reportProblemButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {

				StringBuffer detailsSb = new StringBuffer();

				detailsSb.append("***" + testModeTextView.getText() + "***\n\n");

				detailsSb.append(testModeDrawKanjiFromMeaningRadioButton.isChecked() + " - " + testModeDrawKanjiFromMeaningRadioButton.getText()).append("\n\n");
				detailsSb.append(testModeDrawKanjiFromInWord.isChecked() + " - " + testModeDrawKanjiFromInWord.getText()).append("\n\n");

				detailsSb.append("***" + otherOptionsTextView.getText() + "***\n\n");

				detailsSb.append(untilSuccessCheckBox.isChecked() + " - " + untilSuccessCheckBox.getText()).append("\n\n");

				detailsSb.append("***" + chooseKanjiTextView.getText() + "***\n\n");

				for (CheckBox currentCheckBox : kanjiCheckBoxList) {

					KanjiEntry currentCheckBoxKanjiEntry = (KanjiEntry)currentCheckBox.getTag();

					detailsSb.append(currentCheckBox.isChecked() + " - " + currentCheckBoxKanjiEntry.getKanji() + " - " + currentCheckBoxKanjiEntry.getPolishTranslates()).append("\n");
				}

				String chooseEmailClientTitle = getString(R.string.choose_email_client);

				String mailSubject = getString(R.string.kanji_test_options_report_problem_email_subject);

				String mailBody = getString(R.string.kanji_test_options_report_problem_email_body,
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
		
		// loading kanji
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, 
				getString(R.string.kanji_test_options_loading),
				getString(R.string.kanji_test_options_loading2));

		class PrepareAsyncTask extends AsyncTask<Void, Void, List<KanjiEntry>> {

			@Override
			protected List<KanjiEntry> doInBackground(Void... arg) {
		
				return DictionaryManager.getInstance().getAllKanjis();
			}

			@Override
			protected void onPostExecute(List<KanjiEntry> allKanjis) {
				
				Set<String> chosenKanji = kanjiTestConfig.getChosenKanji();
				
				for (int allKanjisIdx = 0; allKanjisIdx < allKanjis.size(); ++allKanjisIdx) {

					KanjiEntry currentKanjiEntry = allKanjis.get(allKanjisIdx);
					
					CheckBox currentKanjiCheckBox = new CheckBox(KanjiTestOptionsActivity.this);
					
					currentKanjiCheckBox.setChecked(chosenKanji.contains(currentKanjiEntry.getKanji()));

					currentKanjiCheckBox.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

					currentKanjiCheckBox.setTextSize(12);

					StringBuffer currentKanjiEntryText = new StringBuffer();

					currentKanjiEntryText.append("<big>").append(currentKanjiEntry.getKanji()).append("</big> - ").append(currentKanjiEntry.getPolishTranslates().toString()).append("\n\n");

					currentKanjiCheckBox.setText(Html.fromHtml(currentKanjiEntryText.toString()), BufferType.SPANNABLE);

					currentKanjiCheckBox.setTag(currentKanjiEntry);
					
					kanjiCheckBoxList.add(currentKanjiCheckBox);

					mainLayout.addView(currentKanjiCheckBox);			
				}
				
				progressDialog.dismiss();
			}
		}

		new PrepareAsyncTask().execute();
	}
}
