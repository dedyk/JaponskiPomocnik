package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.utils.EntryOrderList;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult.ResultItem;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.GroupEnum;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TextView.BufferType;
import android.widget.Toast;

public class KanjiTestOptionsActivity extends Activity {

	private List<CheckBox> kanjiGroupList;
	private KanjiList kanjiList;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, R.id.kanji_test_options_input_kanji_to_test, Menu.NONE,
				R.string.kanji_test_options_input_kanji_to_test);
		
		menu.add(Menu.NONE, R.id.kanji_test_options_clear_selected_kanji, Menu.NONE,
				R.string.kanji_test_options_clear_selected_kanji);

		menu.add(Menu.NONE, R.id.kanji_test_options_save_current_kanji_list_as_own_group, Menu.NONE,
				R.string.kanji_test_options_save_current_kanji_list_as_own_group);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		if (item.getItemId() == R.id.kanji_test_options_input_kanji_to_test) {

			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(getString(R.string.kanji_test_options_input_kanji_to_test));
			alert.setMessage(getString(R.string.kanji_test_options_input_kanji_to_test2));

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {

					String kanjiValue = input.getText().toString();

					List<KanjiEntry> knownKanjiList = JapaneseAndroidLearnHelperApplication.getInstance()
							.getDictionaryManager(KanjiTestOptionsActivity.this).findKnownKanji(kanjiValue);

					List<String> allKanjisInGroup = new ArrayList<String>();

					StringBuffer allKanjiTest = new StringBuffer();

					for (KanjiEntry currentKanjiEntry : knownKanjiList) {

						if (currentKanjiEntry.isGenerated() == false
								&& allKanjisInGroup.contains(currentKanjiEntry.getKanji()) == false) {
							allKanjisInGroup.add(currentKanjiEntry.getKanji());

							allKanjiTest.append(currentKanjiEntry.getKanji());
						}
					}

					if (allKanjisInGroup.size() == 0) {
						Toast.makeText(KanjiTestOptionsActivity.this,
								getString(R.string.kanji_test_options_input_no_kanji), Toast.LENGTH_SHORT).show();

						return;
					}

					for (CheckBox currentCheckBox : kanjiGroupList) {
						currentCheckBox.setChecked(false);
					}

					for (String currentInputedKanji : allKanjisInGroup) {
						kanjiList.setKanjiChecked(currentInputedKanji, true);
					}

					Toast.makeText(KanjiTestOptionsActivity.this,
							getString(R.string.kanji_test_options_input_detected_kanji, allKanjiTest.toString()),
							Toast.LENGTH_SHORT).show();

					showSelectedKanji();
				}
			});

			alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					// noop
				}
			});

			alert.show();

			return true;

		} else if (item.getItemId() == R.id.kanji_test_options_clear_selected_kanji) {

			for (CheckBox currentCheckBox : kanjiGroupList) {
				currentCheckBox.setChecked(false);
			}

			kanjiList.cleatUserSelectedKanjiList();

			showSelectedKanji();

			Toast.makeText(KanjiTestOptionsActivity.this,
					getString(R.string.kanji_test_options_clear_selected_kanji_all), Toast.LENGTH_SHORT).show();

			return true;
			
		} else if (item.getItemId() == R.id.kanji_test_options_save_current_kanji_list_as_own_group) {
			
			final List<KanjiEntry> userSelectedKanjiList = kanjiList.getUserSelectedKanjiList();
						
			if (userSelectedKanjiList.size() == 0) {
				
				Toast.makeText(KanjiTestOptionsActivity.this,
						getString(R.string.kanji_test_options_save_current_kanji_list_as_own_group_please_choose_kanji),
						Toast.LENGTH_SHORT).show();
								
				return true;
			}
			
			AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle(getString(R.string.kanji_test_options_save_current_kanji_list_as_own_group_dialog_title));

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			
			input.setSingleLine(true);
			
			alert.setView(input);

			alert.setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					
					String groupName = input.getText().toString();
					
					boolean isCorrectGroupName = true;
										
					if (groupName == null || groupName.trim().equals("") == true) {						
						isCorrectGroupName = false;						
					}
					
					if (isCorrectGroupName == true) {
						
						if (groupName.matches("([A-Z]|[a-z]| |[0-9])*") == false) {
							isCorrectGroupName = false;	
						}						
					}
					
					if (isCorrectGroupName == false) {
						
						Toast.makeText(KanjiTestOptionsActivity.this,
								getString(R.string.kanji_test_options_save_current_kanji_list_as_own_group_incorrect_group_name),
								Toast.LENGTH_SHORT).show();
						
						return;
					}					
					
					final KanjiTestConfig kanjiTestConfig = JapaneseAndroidLearnHelperApplication.getInstance()
							.getConfigManager(KanjiTestOptionsActivity.this).getKanjiTestConfig();

					// jesli grupa nieistnieje, dodaj ja
					if (kanjiTestConfig.isOwnGroupExists(groupName) == false) {
						
						// dodanie grupy
						kanjiTestConfig.addOwnGroup(groupName);
					}					
					
					// zapisanie znakow do grupy
					List<String> userSelectedKanjiStringList = new ArrayList<String>();
					
					for (KanjiEntry kanjiEntry : userSelectedKanjiList) {
						userSelectedKanjiStringList.add(kanjiEntry.getKanji());
					}
					
					kanjiTestConfig.setOwnGroupKanjiList(groupName, userSelectedKanjiStringList);
					
					// wyswietlenie listy grup
					int fixme = 1;
					
				}
			});

			alert.setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int whichButton) {
					// noop
				}
			});

			alert.show();

			return true;			
		}
		
		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_kanji_test_options));

		final KanjiTestConfig kanjiTestConfig = JapaneseAndroidLearnHelperApplication.getInstance()
				.getConfigManager(this).getKanjiTestConfig();

		setContentView(R.layout.kanji_test_options);

		final TextView testModeTextView = (TextView) findViewById(R.id.kanji_test_options_test_mode);

		final RadioButton testModeDrawKanjiFromMeaningRadioButton = (RadioButton) findViewById(R.id.kanji_test_options_test_mode_draw_kanji_from_meaning);
		final RadioButton testModeChooseKanjiFromMeaningRadioButton = (RadioButton) findViewById(R.id.kanji_test_options_test_mode_choose_kanji_from_meaning);
		final RadioButton testModeDrawKanjiFromInWord = (RadioButton) findViewById(R.id.kanji_test_options_test_mode_draw_kanji_in_word);
		final RadioButton testModeChooseKanjiFromInWord = (RadioButton) findViewById(R.id.kanji_test_options_test_mode_choose_kanji_in_word);

		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();

		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			testModeDrawKanjiFromMeaningRadioButton.setChecked(true);
		} else if (kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_FROM_MEANING) {
			testModeChooseKanjiFromMeaningRadioButton.setChecked(true);
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			testModeDrawKanjiFromInWord.setChecked(true);
		} else if (kanjiTestMode == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {
			testModeChooseKanjiFromInWord.setChecked(true);
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode: " + kanjiTestMode);
		}

		final TextView otherOptionsTextView = (TextView) findViewById(R.id.kanji_test_options_other_options);

		// until success
		final CheckBox untilSuccessCheckBox = (CheckBox) findViewById(R.id.kanji_test_options_until_success);

		untilSuccessCheckBox.setChecked(kanjiTestConfig.getUntilSuccess());

		// until success new word limit
		final CheckBox untilSuccessNewWordLimitCheckBox = (CheckBox) findViewById(R.id.kanji_test_options_until_success_new_word_limit);

		untilSuccessNewWordLimitCheckBox.setChecked(kanjiTestConfig.getUntilSuccessNewWordLimitPostfix());

		setUntilSuccessNewWordLimitCheckBoxEnabled(untilSuccessCheckBox, untilSuccessNewWordLimitCheckBox);
		
		// max test size
		final EditText maxTestSizeEditText = (EditText)findViewById(R.id.kanji_test_options_max_test_size_edit_text);
		
		maxTestSizeEditText.setText(String.valueOf(kanjiTestConfig.getMaxTestSize()));

		// actions
		untilSuccessCheckBox.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				setUntilSuccessNewWordLimitCheckBoxEnabled(untilSuccessCheckBox, untilSuccessNewWordLimitCheckBox);
			}
		});

		// dedicate example
		final CheckBox dedicateExampleCheckBox = (CheckBox) findViewById(R.id.kanji_test_options_dedicate_example);

		dedicateExampleCheckBox.setChecked(kanjiTestConfig.getDedicateExample());

		final TextView chooseKanjiGroupTextView = (TextView) findViewById(R.id.kanji_test_options_choose_kanji_group);

		final TextView chosenKanjiTextView = (TextView) findViewById(R.id.kanji_test_options_chosen_kanji);

		final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.kanji_test_options_main_layout);

		kanjiList = new KanjiList();
		kanjiGroupList = new ArrayList<CheckBox>();

		Button startTestButton = (Button) findViewById(R.id.kanji_test_options_start_test);

		startTestButton.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (testModeDrawKanjiFromMeaningRadioButton.isChecked() == true) {
					kanjiTestConfig.setKanjiTestMode(KanjiTestMode.DRAW_KANJI_FROM_MEANING);
				} else if (testModeChooseKanjiFromMeaningRadioButton.isChecked() == true) {
					kanjiTestConfig.setKanjiTestMode(KanjiTestMode.CHOOSE_KANJI_FROM_MEANING);
				} else if (testModeDrawKanjiFromInWord.isChecked() == true) {
					kanjiTestConfig.setKanjiTestMode(KanjiTestMode.DRAW_KANJI_IN_WORD);
				} else if (testModeChooseKanjiFromInWord.isChecked() == true) {
					kanjiTestConfig.setKanjiTestMode(KanjiTestMode.CHOOSE_KANJI_IN_WORD);
				} else {
					throw new RuntimeException("KanjiTestMode kanjiTestMode");
				}

				kanjiTestConfig.setUntilSuccess(untilSuccessCheckBox.isChecked());

				kanjiTestConfig.setUntilSuccessNewWordLimitPostfix(untilSuccessNewWordLimitCheckBox.isChecked());

				kanjiTestConfig.setDedicateExample(dedicateExampleCheckBox.isChecked());

				final List<KanjiEntry> kanjiEntryList = new ArrayList<KanjiEntry>();

				List<String> chosenKanjiList = new ArrayList<String>();

				for (KanjiEntry currentCheckBoxKanjiEntry : kanjiList.getUserSelectedKanjiList()) {

					// get kanji with details
					currentCheckBoxKanjiEntry = JapaneseAndroidLearnHelperApplication.getInstance()
							.getDictionaryManager(KanjiTestOptionsActivity.this)
							.findKanji(currentCheckBoxKanjiEntry.getKanji());

					chosenKanjiList.add(currentCheckBoxKanjiEntry.getKanji());
					kanjiEntryList.add(currentCheckBoxKanjiEntry);

				}

				kanjiTestConfig.setChosenKanji(chosenKanjiList);

				final List<String> chosenKanjiGroupList = new ArrayList<String>();

				for (CheckBox currentKanjiGroupListCheckBox : kanjiGroupList) {

					if (currentKanjiGroupListCheckBox.isChecked() == true) {
						chosenKanjiGroupList.add(currentKanjiGroupListCheckBox.getText().toString());
					}
				}

				kanjiTestConfig.setChosenKanjiGroup(chosenKanjiGroupList);

				if (chosenKanjiList.size() == 0) {

					Toast toast = Toast.makeText(KanjiTestOptionsActivity.this,
							getString(R.string.kanji_test_options_choose_kanji_info), Toast.LENGTH_SHORT);

					toast.show();

					return;
				}

				if ((kanjiTestConfig.getKanjiTestMode() == KanjiTestMode.CHOOSE_KANJI_IN_WORD || kanjiTestConfig
						.getKanjiTestMode() == KanjiTestMode.DRAW_KANJI_IN_WORD)
						&& dedicateExampleCheckBox.isChecked() == true) {

					if (chosenKanjiGroupList.size() == 0) {

						Toast toast = Toast.makeText(KanjiTestOptionsActivity.this,
								getString(R.string.kanji_test_options_choose_kanji_group_info), Toast.LENGTH_SHORT);

						toast.show();

						return;
					}

					int dictionaryEntrySize = 0;

					for (String currentKanjiGroup : chosenKanjiGroupList) {

						List<DictionaryEntry> currentWordsGroupDictionaryEntryList = JapaneseAndroidLearnHelperApplication
								.getInstance().getDictionaryManager(KanjiTestOptionsActivity.this)
								.getGroupDictionaryEntries(GroupEnum.getGroupEnum(currentKanjiGroup));

						dictionaryEntrySize += currentWordsGroupDictionaryEntryList.size();
					}

					if (dictionaryEntrySize == 0) {
						Toast toast = Toast.makeText(KanjiTestOptionsActivity.this,
								getString(R.string.kanji_test_options_choose_kanji_group_no_words), Toast.LENGTH_SHORT);

						toast.show();

						return;
					}
				}
				
				// max test size
				boolean maxTestSizeError = false;
				
				int maxTestSize = -1;
				
				String maxTestSizeString = maxTestSizeEditText.getText().toString();
				
				if (maxTestSizeString == null) {
					maxTestSizeError = true;
					
				} else {
					
					try {
						maxTestSize = Integer.parseInt(maxTestSizeString);
						
					} catch (NumberFormatException e) {
						maxTestSizeError = true;
					}					
				}
				
				if (maxTestSizeError == true || maxTestSize <= 0) {
					Toast toast = Toast.makeText(KanjiTestOptionsActivity.this,
							getString(R.string.kanji_test_options_max_test_size_error), Toast.LENGTH_SHORT);

					toast.show();

					return;					
				}
				
				kanjiTestConfig.setMaxTestSize(maxTestSize);				

				// prepare test
				final ProgressDialog progressDialog = ProgressDialog.show(KanjiTestOptionsActivity.this,
						getString(R.string.kanji_test_options_prepare1),
						getString(R.string.kanji_test_options_prepare2));

				class PrepareAsyncTask extends AsyncTask<Void, Void, Void> {

					@Override
					protected Void doInBackground(Void... arg) {

						// get kanji test context
						JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext = JapaneseAndroidLearnHelperApplication
								.getInstance().getContext().getKanjiTestContext();

						// reset test
						kanjiTestContext.resetTest();
						
						List<KanjiEntry> kanjiEntryList2 = kanjiEntryList;

						Collections.shuffle(kanjiEntryList2);
						
						// max test size filter
						int maxTestSize = kanjiTestConfig.getMaxTestSize();
						
						if (kanjiEntryList2.size() > maxTestSize) {
							
							List<KanjiEntry> newKanjiEntryList = new ArrayList<KanjiEntry>();
							
							for (int kanjiEntryListIdx = 0; kanjiEntryListIdx < maxTestSize; ++kanjiEntryListIdx) {
								newKanjiEntryList.add(kanjiEntryList2.get(kanjiEntryListIdx));
							}
							
							kanjiEntryList2 = newKanjiEntryList;
						}						

						EntryOrderList<KanjiEntry> kanjiEntryListEntryOrderList = null;

						if (untilSuccessCheckBox.isChecked() == true
								&& untilSuccessNewWordLimitCheckBox.isChecked() == true) {
							kanjiEntryListEntryOrderList = new EntryOrderList<KanjiEntry>(kanjiEntryList2, 10);
						} else {
							kanjiEntryListEntryOrderList = new EntryOrderList<KanjiEntry>(kanjiEntryList2,
									kanjiEntryList2.size());
						}

						// set kanji entry list in context
						kanjiTestContext.setKanjiEntryList(kanjiEntryListEntryOrderList);

						if (kanjiTestConfig.getKanjiTestMode() == KanjiTestMode.DRAW_KANJI_IN_WORD
								|| kanjiTestConfig.getKanjiTestMode() == KanjiTestMode.CHOOSE_KANJI_IN_WORD) {

							List<JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanjiList = new ArrayList<JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji>();

							if (dedicateExampleCheckBox.isChecked() == false) {

								FindWordRequest findWordRequest = new FindWordRequest();

								findWordRequest.searchKanji = true;
								findWordRequest.searchKana = false;
								findWordRequest.searchRomaji = false;
								findWordRequest.searchTranslate = false;
								findWordRequest.searchInfo = false;
								findWordRequest.searchGrammaFormAndExamples = false;
								findWordRequest.wordPlaceSearch = FindWordRequest.WordPlaceSearch.START_WITH;
								findWordRequest.dictionaryEntryTypeList = null;

								for (KanjiEntry currentKanjiEntry : kanjiEntryList2) {

									findWordRequest.word = currentKanjiEntry.getKanji();

									// find word with this kanji
									FindWordResult findWordResult = JapaneseAndroidLearnHelperApplication.getInstance()
											.getDictionaryManager(KanjiTestOptionsActivity.this)
											.findWord(findWordRequest);

									List<ResultItem> findWordResultResult = findWordResult.result;

									for (ResultItem currentFindWordResultResult : findWordResultResult) {

										JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji currentDictionaryEntryWithRemovedKanji = new JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji(
												currentFindWordResultResult.getDictionaryEntry(), currentKanjiEntry
														.getKanji());

										dictionaryEntryWithRemovedKanjiList.add(currentDictionaryEntryWithRemovedKanji);
									}
								}

								for (KanjiEntry currentKanjiEntry : kanjiEntryList2) {

									// find word with this kanji
									FindWordResult findWordResult = JapaneseAndroidLearnHelperApplication.getInstance()
											.getDictionaryManager(KanjiTestOptionsActivity.this)
											.findWord(findWordRequest);

									List<ResultItem> findWordResultResult = findWordResult.result;

									for (ResultItem currentFindWordResultResult : findWordResultResult) {

										DictionaryEntry currentDictionaryEntry = currentFindWordResultResult
												.getDictionaryEntry();

										String currentDictionaryEntryKanji = currentDictionaryEntry.getKanji();

										if (currentDictionaryEntryKanji.contains(currentKanjiEntry.getKanji()) == false) {
											continue;
										}

										JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji currentDictionaryEntryWithRemovedKanji = new JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji(
												currentFindWordResultResult.getDictionaryEntry(), currentKanjiEntry
														.getKanji());

										dictionaryEntryWithRemovedKanjiList.add(currentDictionaryEntryWithRemovedKanji);

									}
								}

							} else {

								for (String currentKanjiGroup : chosenKanjiGroupList) {

									List<DictionaryEntry> currentWordsGroupDictionaryEntryList = JapaneseAndroidLearnHelperApplication
											.getInstance().getDictionaryManager(KanjiTestOptionsActivity.this)
											.getGroupDictionaryEntries(GroupEnum.getGroupEnum(currentKanjiGroup));

									for (KanjiEntry currentKanjiEntry : kanjiEntryList2) {

										for (DictionaryEntry currentDictionaryEntry : currentWordsGroupDictionaryEntryList) {

											String currentDictionaryEntryKanji = currentDictionaryEntry.getKanji();

											if (currentDictionaryEntryKanji.contains(currentKanjiEntry.getKanji()) == false) {
												continue;
											}

											JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji currentDictionaryEntryWithRemovedKanji = new JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji(
													currentDictionaryEntry, currentKanjiEntry.getKanji());

											dictionaryEntryWithRemovedKanjiList
													.add(currentDictionaryEntryWithRemovedKanji);
										}
									}
								}
							}

							Collections.shuffle(dictionaryEntryWithRemovedKanjiList);

							EntryOrderList<JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji> entryOrderList = null;

							if (untilSuccessCheckBox.isChecked() == true
									&& untilSuccessNewWordLimitCheckBox.isChecked() == true) {
								entryOrderList = new EntryOrderList<JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji>(
										dictionaryEntryWithRemovedKanjiList, 10);
							} else {
								entryOrderList = new EntryOrderList<JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji>(
										dictionaryEntryWithRemovedKanjiList, dictionaryEntryWithRemovedKanjiList.size());
							}

							kanjiTestContext.setDictionaryEntryWithRemovedKanji(entryOrderList);
						}

						return null;
					}

					@SuppressWarnings("deprecation")
					@Override
					protected void onPostExecute(Void arg) {
						super.onPostExecute(arg);

						progressDialog.dismiss();

						AlertDialog alertDialog = new AlertDialog.Builder(KanjiTestOptionsActivity.this).create();

						alertDialog.setCancelable(false);

						alertDialog.setTitle(getString(R.string.kanji_test_options_info_title));
						alertDialog.setMessage(getString(R.string.kanji_test_options_info_message));

						alertDialog.setButton(getString(R.string.kanji_test_options_info_ok),
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog, int which) {

										Intent intent = new Intent(getApplicationContext(), KanjiTest.class);

										startActivity(intent);

										finish();
									}
								});

						alertDialog.show();
					}
				}

				new PrepareAsyncTask().execute();
			}
		});

		Button reportProblemButton = (Button) findViewById(R.id.kanji_test_options_report_problem_button);

		reportProblemButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {

				StringBuffer detailsSb = new StringBuffer();

				detailsSb.append("***" + testModeTextView.getText() + "***\n\n");

				detailsSb.append(
						testModeDrawKanjiFromMeaningRadioButton.isChecked() + " - "
								+ testModeDrawKanjiFromMeaningRadioButton.getText()).append("\n\n");
				detailsSb.append(
						testModeChooseKanjiFromMeaningRadioButton.isChecked() + " - "
								+ testModeChooseKanjiFromMeaningRadioButton.getText()).append("\n\n");
				detailsSb.append(
						testModeDrawKanjiFromInWord.isChecked() + " - " + testModeDrawKanjiFromInWord.getText())
						.append("\n\n");
				detailsSb.append(
						testModeChooseKanjiFromInWord.isChecked() + " - " + testModeChooseKanjiFromInWord.getText())
						.append("\n\n");

				detailsSb.append("***" + otherOptionsTextView.getText() + "***\n\n");

				detailsSb.append(untilSuccessCheckBox.isChecked() + " - " + untilSuccessCheckBox.getText()).append(
						"\n\n");

				detailsSb.append(
						untilSuccessNewWordLimitCheckBox.isChecked() + " - "
								+ untilSuccessNewWordLimitCheckBox.getText()).append("\n\n");

				detailsSb.append(dedicateExampleCheckBox.isChecked() + " - " + dedicateExampleCheckBox.getText())
						.append("\n\n");

				detailsSb.append("***" + chooseKanjiGroupTextView.getText() + "***\n\n"); // tutaj

				for (CheckBox currentKanjiGroupList : kanjiGroupList) {

					String currentKanjiGroupListText = currentKanjiGroupList.getText().toString();

					detailsSb.append(currentKanjiGroupList.isChecked() + " - " + currentKanjiGroupListText)
							.append("\n");
				}

				detailsSb.append("\n***" + chosenKanjiTextView.getText() + "***\n\n");

				for (KanjiEntry currentCheckBoxKanjiEntry : kanjiList.getAllKanjiList()) {

					if (kanjiList.isChecked(currentCheckBoxKanjiEntry) == true) {
						detailsSb.append(
								"true - " + currentCheckBoxKanjiEntry.getKanji() + " - "
										+ currentCheckBoxKanjiEntry.getPolishTranslates()).append("\n");
					}
				}

				String chooseEmailClientTitle = getString(R.string.choose_email_client);

				String mailSubject = getString(R.string.kanji_test_options_report_problem_email_subject);

				String mailBody = getString(R.string.kanji_test_options_report_problem_email_body, detailsSb.toString());

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

		// loading kanji

		final ProgressDialog progressDialog = ProgressDialog.show(this, getString(R.string.kanji_test_options_loading),
				getString(R.string.kanji_test_options_loading2));

		class PrepareAsyncTask extends AsyncTask<Void, Void, List<KanjiEntry>> {

			@Override
			protected List<KanjiEntry> doInBackground(Void... arg) {

				return JapaneseAndroidLearnHelperApplication.getInstance()
						.getDictionaryManager(KanjiTestOptionsActivity.this).getAllKanjis(false, false, true);
			}

			@Override
			protected void onPostExecute(List<KanjiEntry> allKanjis) {

				Map<GroupEnum, Set<String>> kanjiGroups = new TreeMap<GroupEnum, Set<String>>();

				for (int allKanjisIdx = 0; allKanjisIdx < allKanjis.size(); ++allKanjisIdx) {

					KanjiEntry currentKanjiEntry = allKanjis.get(allKanjisIdx);

					kanjiList.addKanjiEntry(currentKanjiEntry);

					List<GroupEnum> currentKanjiEntryGroups = currentKanjiEntry.getGroups();

					if (currentKanjiEntryGroups != null && currentKanjiEntryGroups.size() > 0) {

						for (GroupEnum currentCurrentKanjiEntryGroup : currentKanjiEntryGroups) {

							Set<String> groupSet = kanjiGroups.get(currentCurrentKanjiEntryGroup);

							if (groupSet == null) {
								groupSet = new HashSet<String>();
							}

							groupSet.add(currentKanjiEntry.getKanji());

							kanjiGroups.put(currentCurrentKanjiEntryGroup, groupSet);
						}
					}
				}

				List<String> chosenKanji = kanjiTestConfig.getChosenKanjiAsList();

				for (String currentChoosenKanji : chosenKanji) {
					kanjiList.setKanjiChecked(currentChoosenKanji, true);
				}

				GroupEnum[] kanjiGroupsKeysArray = new GroupEnum[kanjiGroups.size()];

				kanjiGroups.keySet().toArray(kanjiGroupsKeysArray);

				GroupEnum.sortGroups(kanjiGroupsKeysArray);

				// add to main layout

				Set<String> chosenKanjiGroup = kanjiTestConfig.getChosenKanjiGroup();

				for (GroupEnum currentKanjiGroup : kanjiGroupsKeysArray) {

					CheckBox currentKanjiGroupCheckBox = new CheckBox(KanjiTestOptionsActivity.this);

					currentKanjiGroupCheckBox.setLayoutParams(new LinearLayout.LayoutParams(
							LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

					currentKanjiGroupCheckBox.setTextSize(12);

					currentKanjiGroupCheckBox.setText(currentKanjiGroup.getValue());

					currentKanjiGroupCheckBox.setChecked(chosenKanjiGroup.contains(currentKanjiGroup.getValue()));

					currentKanjiGroupCheckBox.setTag(kanjiGroups.get(currentKanjiGroup));

					kanjiGroupList.add(currentKanjiGroupCheckBox);

					mainLayout.addView(currentKanjiGroupCheckBox, mainLayout.getChildCount() - 2);

					currentKanjiGroupCheckBox.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {

							Set<String> allKanjisInGroup = new HashSet<String>();

							for (CheckBox currentKanjiGroupListCheckBox : kanjiGroupList) {

								if (currentKanjiGroupListCheckBox.isChecked() == false) {
									continue;
								}

								@SuppressWarnings("unchecked")
								Set<String> currentKanjiGroupListCheckBoxTag = (Set<String>) currentKanjiGroupListCheckBox
										.getTag();

								allKanjisInGroup.addAll(currentKanjiGroupListCheckBoxTag);
							}

							for (KanjiEntry currentCheckBoxKanjiEntry : kanjiList.getAllKanjiList()) {

								String currentCheckBoxKanjiEntryKanji = currentCheckBoxKanjiEntry.getKanji();

								kanjiList.setKanjiChecked(currentCheckBoxKanjiEntry,
										allKanjisInGroup.contains(currentCheckBoxKanjiEntryKanji));
							}

							showSelectedKanji();
						}
					});
				}

				showSelectedKanji();

				progressDialog.dismiss();
			}
		}

		new PrepareAsyncTask().execute();
	}

	private void showSelectedKanji() {

		final LinearLayout mainLayout = (LinearLayout) findViewById(R.id.kanji_test_options_main_layout);
		final TextView chosenKanjiInfoTextView = (TextView) findViewById(R.id.kanji_test_options_chosen_kanji_info);

		boolean startDelete = false;

		// clear end of main layout
		for (int idx = 0; idx < mainLayout.getChildCount(); ++idx) {

			View currentMainLayoutChildView = mainLayout.getChildAt(idx);

			if (currentMainLayoutChildView == chosenKanjiInfoTextView) {
				startDelete = true;

				continue;
			}

			if (startDelete == true) {
				mainLayout.removeView(currentMainLayoutChildView);

				idx--;
			}
		}

		for (final KanjiEntry currentCheckBoxKanjiEntry : kanjiList.getUserSelectedKanjiList()) {

			LinearLayout linearLayout = new LinearLayout(this);

			LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT,
					LayoutParams.WRAP_CONTENT);
			linearLayout.setLayoutParams(layoutParams);

			// kanji
			TextView kanjiTextView = new TextView(this);

			LinearLayout.LayoutParams kanjiLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);

			kanjiLayoutParams.leftMargin = 10;

			kanjiTextView.setLayoutParams(kanjiLayoutParams);

			StringBuffer currentKanjiEntryText = new StringBuffer();

			currentKanjiEntryText.append("<big>").append(currentCheckBoxKanjiEntry.getKanji()).append("</big> - ")
					.append(currentCheckBoxKanjiEntry.getPolishTranslates().toString()).append("\n\n");

			kanjiTextView.setTextSize(15.0f);
			kanjiTextView.setText(Html.fromHtml(currentKanjiEntryText.toString()), BufferType.SPANNABLE);

			// delete icon
			ImageView deleteImageView = new ImageView(this);

			LinearLayout.LayoutParams imageLayoutParams = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT,
					LayoutParams.WRAP_CONTENT);
			imageLayoutParams.gravity = Gravity.CENTER;

			imageLayoutParams.leftMargin = 10;

			deleteImageView.setLayoutParams(imageLayoutParams);

			deleteImageView.setImageDrawable(getResources().getDrawable(R.drawable.delete));

			linearLayout.addView(kanjiTextView);
			linearLayout.addView(deleteImageView);

			// actions: delete
			View.OnClickListener deleteOnClickListener = new View.OnClickListener() {

				@Override
				public void onClick(View v) {

					for (CheckBox currentKanjiGroupListCheckBox : kanjiGroupList) {
						currentKanjiGroupListCheckBox.setChecked(false);
					}

					kanjiList.setKanjiChecked(currentCheckBoxKanjiEntry, false);

					showSelectedKanji();
				}
			};

			kanjiTextView.setOnClickListener(deleteOnClickListener);
			deleteImageView.setOnClickListener(deleteOnClickListener);

			mainLayout.addView(linearLayout);
		}

	}

	private void setUntilSuccessNewWordLimitCheckBoxEnabled(CheckBox untilSuccessCheckBox,
			CheckBox untilSuccessNewWordLimitCheckBox) {

		if (untilSuccessCheckBox.isChecked() == true) {
			untilSuccessNewWordLimitCheckBox.setEnabled(true);
		} else {
			untilSuccessNewWordLimitCheckBox.setEnabled(false);
		}
	}

	private static class KanjiList {

		private final List<KanjiEntry> allKanjiList = new ArrayList<KanjiEntry>();

		private final List<KanjiEntry> userSelectedKanjiList = new ArrayList<KanjiEntry>();

		public List<KanjiEntry> getAllKanjiList() {
			return allKanjiList;
		}

		public List<KanjiEntry> getUserSelectedKanjiList() {
			return userSelectedKanjiList;
		}

		public void cleatUserSelectedKanjiList() {
			userSelectedKanjiList.clear();
		}

		public void addKanjiEntry(KanjiEntry kanjiEntry) {
			allKanjiList.add(kanjiEntry);
		}

		public void setKanjiChecked(String kanji, boolean checked) {

			KanjiEntry foundKanjiEntry = null;

			for (KanjiEntry currentKanjiEntry : allKanjiList) {

				if (currentKanjiEntry.getKanji().equals(kanji) == true) {

					foundKanjiEntry = currentKanjiEntry;

					break;
				}
			}

			if (foundKanjiEntry != null) {
				setKanjiChecked(foundKanjiEntry, checked);
			}
		}

		public void setKanjiChecked(KanjiEntry kanjiEntry, boolean checked) {

			if (checked == false) {
				userSelectedKanjiList.remove(kanjiEntry);

			} else if (userSelectedKanjiList.contains(kanjiEntry) == false) {
				userSelectedKanjiList.add(kanjiEntry);
			}
		}

		public boolean isChecked(KanjiEntry kanjiEntry) {
			return userSelectedKanjiList.contains(kanjiEntry);
		}
	}
}
