package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.WordDictionarySearchConfig;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.ILoadWithProgress;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordRequest.WordPlaceSearch;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindWordResult.ResultItem;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntryType;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class WordDictionary extends Activity {
	
	private ListView searchResultListView;
	
	private WordDictionaryListItemAdapter searchResultArrayAdapter;
	
	private List<WordDictionaryListItem> searchResultList;
	
	private EditText searchValueEditText;
	
	private CheckBox seachOptionsEachChangeCheckBox;
	
	private CheckBox automaticSendMissingWordCheckBox;
	
	private Button searchButton;
	
	private CheckBox searchOptionsOnlyCommonWordsCheckbox;
	private CheckBox searchOptionsKanjiCheckbox;
	private CheckBox searchOptionsKanaCheckbox;
	private CheckBox searchOptionsRomajiCheckbox;
	private CheckBox searchOptionsTranslateCheckbox;
	private CheckBox searchOptionsInfoCheckbox;
	private CheckBox searchOptionsGrammaExampleSearchCheckbox;
	
	//private RadioButton searchOptionsAnyPlaceRadioButton;
	private RadioButton searchOptionsStartWithPlaceRadioButton;
	private RadioButton searchOptionsExactPlaceRadioButton;
	
	private TextView wordDictionarySearchElementsNoTextView;
	
	private CheckBox[] searchDictionaryEntryListCheckBox;
			
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
	protected void onSaveInstanceState(Bundle bundle) {
		
		super.onSaveInstanceState(bundle);
		
		bundle.putString("searchValueEditText", searchValueEditText.getText().toString());
		
		bundle.putBoolean("seachOptionsEachChangeCheckBox", seachOptionsEachChangeCheckBox.isChecked());
		
		bundle.putBoolean("automaticSendMissingWordCheckBox", automaticSendMissingWordCheckBox.isChecked());
		
		bundle.putBoolean("searchOptionsOnlyCommonWordsCheckbox", searchOptionsOnlyCommonWordsCheckbox.isChecked());
		bundle.putBoolean("searchOptionsKanjiCheckbox", searchOptionsKanjiCheckbox.isChecked());
		bundle.putBoolean("searchOptionsKanaCheckbox", searchOptionsKanaCheckbox.isChecked());
		bundle.putBoolean("searchOptionsRomajiCheckbox", searchOptionsRomajiCheckbox.isChecked());
		bundle.putBoolean("searchOptionsTranslateCheckbox", searchOptionsTranslateCheckbox.isChecked());
		bundle.putBoolean("searchOptionsInfoCheckbox", searchOptionsInfoCheckbox.isChecked());
		bundle.putBoolean("searchOptionsGrammaExampleSearchCheckbox", searchOptionsGrammaExampleSearchCheckbox.isChecked());
		
		//bundle.putBoolean("searchOptionsAnyPlaceRadioButton", searchOptionsAnyPlaceRadioButton.isChecked());
		bundle.putBoolean("searchOptionsStartWithPlaceRadioButton", searchOptionsStartWithPlaceRadioButton.isChecked());
		bundle.putBoolean("searchOptionsExactPlaceRadioButton", searchOptionsExactPlaceRadioButton.isChecked());
		
		boolean[] searchDictionaryEntryListCheckBoxBooleanValue = new boolean[searchDictionaryEntryListCheckBox.length];
		
		for (int idx = 0; idx < searchDictionaryEntryListCheckBox.length; ++idx) {
			searchDictionaryEntryListCheckBoxBooleanValue[idx] = searchDictionaryEntryListCheckBox[idx].isChecked();
		}
		
		bundle.putBooleanArray("searchDictionaryEntryListCheckBox", searchDictionaryEntryListCheckBoxBooleanValue);
	}

	@Override
	protected void onRestoreInstanceState(Bundle bundle) {
			
		super.onRestoreInstanceState(bundle);
					   
		seachOptionsEachChangeCheckBox.setChecked(bundle.getBoolean("seachOptionsEachChangeCheckBox"));
		
		automaticSendMissingWordCheckBox.setChecked(bundle.getBoolean("automaticSendMissingWordCheckBox"));
		
		searchOptionsOnlyCommonWordsCheckbox.setChecked(bundle.getBoolean("searchOptionsOnlyCommonWordsCheckbox"));
		searchOptionsKanjiCheckbox.setChecked(bundle.getBoolean("searchOptionsKanjiCheckbox"));
		searchOptionsKanaCheckbox.setChecked(bundle.getBoolean("searchOptionsKanaCheckbox"));
		searchOptionsRomajiCheckbox.setChecked(bundle.getBoolean("searchOptionsRomajiCheckbox"));
		searchOptionsTranslateCheckbox.setChecked(bundle.getBoolean("searchOptionsTranslateCheckbox"));
		searchOptionsInfoCheckbox.setChecked(bundle.getBoolean("searchOptionsInfoCheckbox"));
		searchOptionsGrammaExampleSearchCheckbox.setChecked(bundle.getBoolean("searchOptionsGrammaExampleSearchCheckbox"));
		
		//searchOptionsAnyPlaceRadioButton.setChecked(bundle.getBoolean("searchOptionsAnyPlaceRadioButton"));
		searchOptionsStartWithPlaceRadioButton.setChecked(bundle.getBoolean("searchOptionsStartWithPlaceRadioButton"));
		searchOptionsExactPlaceRadioButton.setChecked(bundle.getBoolean("searchOptionsExactPlaceRadioButton"));

		boolean[] searchDictionaryEntryListCheckBoxBooleanValue = bundle.getBooleanArray("searchDictionaryEntryListCheckBox");
		
		for (int idx = 0; idx < searchDictionaryEntryListCheckBox.length; ++idx) {
			searchDictionaryEntryListCheckBox[idx].setChecked(searchDictionaryEntryListCheckBoxBooleanValue[idx]);
		}
		
		searchValueEditText.setText(bundle.getString("searchValueEditText"));
				
		
		
		searchValueEditText.post(new Runnable() {
			
			public void run() {
				performSearch(searchValueEditText.getText().toString());
			}
		});
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
				
		super.onCreate(savedInstanceState);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_word_dictionary));
		
		setContentView(R.layout.word_dictionary);
		
		wordDictionarySearchElementsNoTextView = (TextView)findViewById(R.id.word_dictionary_elements_no);
		
		searchResultListView = (ListView)findViewById(R.id.word_dictionary_search_result_list);
		
		searchResultList = new ArrayList<WordDictionaryListItem>();
		searchResultArrayAdapter = new WordDictionaryListItemAdapter(this, R.layout.word_dictionary_simplerow, searchResultList);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				WordDictionaryListItem wordDictionaryListItem = searchResultArrayAdapter.getItem(position);
				
				Intent intent = new Intent(getApplicationContext(), WordDictionaryDetails.class);
				
				intent.putExtra("item", wordDictionaryListItem.getDictionaryEntry());
				
				startActivity(intent);
				
			}
		});
		
		searchValueEditText = (EditText)findViewById(R.id.word_dictionary_search_value);		
		
		searchButton = (Button)findViewById(R.id.word_dictionary_search_search_button);
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				performSearch(searchValueEditText.getText().toString());
			}
		});
		
		final WordDictionarySearchConfig wordDictionarySearchConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getWordDictionarySearchConfig();
		
		seachOptionsEachChangeCheckBox = (CheckBox)findViewById(R.id.word_dictionary_search_options_search_each_change_checkbox);		
		
		seachOptionsEachChangeCheckBox.setChecked(wordDictionarySearchConfig.getEachChangeSearch());		
		seachOptionsEachChangeCheckBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				setSearchButtonVisible();
			}
		});
		
		automaticSendMissingWordCheckBox = (CheckBox)findViewById(R.id.word_dictionary_search_options_improve_dict_automatic_send_missing_word_checkbox);
	
		automaticSendMissingWordCheckBox.setChecked(wordDictionarySearchConfig.getAutomaticSendMissingWord());
		automaticSendMissingWordCheckBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
		
				wordDictionarySearchConfig.setAutomaticSendMissingWord(automaticSendMissingWordCheckBox.isChecked());
		
			}
		});		
		
		searchOptionsOnlyCommonWordsCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_only_common_words_checkbox);
		searchOptionsKanjiCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_kanji_checkbox);
		searchOptionsKanaCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_kana_checkbox);
		searchOptionsRomajiCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_romaji_checkbox);
		searchOptionsTranslateCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_translate_checkbox);
		searchOptionsInfoCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_info_checkbox);
		searchOptionsGrammaExampleSearchCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_search_gramma_examples_checkbox);
		
		//searchOptionsAnyPlaceRadioButton = (RadioButton)findViewById(R.id.word_dictionary_search_options_search_any_place_radiobutton);
		searchOptionsStartWithPlaceRadioButton = (RadioButton)findViewById(R.id.word_dictionary_search_options_search_startwith_radiobutton);
		searchOptionsExactPlaceRadioButton = (RadioButton)findViewById(R.id.word_dictionary_search_options_search_exact_radiobutton);
		
		OnClickListener searchOptionsOnClick = new OnClickListener() {			
			public void onClick(View view) {
				
				if (seachOptionsEachChangeCheckBox.isChecked() == true) {
					performSearch(searchValueEditText.getText().toString());
				}
			}
		};
		
		searchOptionsOnlyCommonWordsCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsKanjiCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsKanaCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsRomajiCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsTranslateCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsInfoCheckbox.setOnClickListener(searchOptionsOnClick);	
		searchOptionsGrammaExampleSearchCheckbox.setOnClickListener(searchOptionsOnClick);
		
		//searchOptionsAnyPlaceRadioButton.setOnClickListener(searchOptionsOnClick);
		searchOptionsStartWithPlaceRadioButton.setOnClickListener(searchOptionsOnClick);
		searchOptionsExactPlaceRadioButton.setOnClickListener(searchOptionsOnClick);
		
		searchValueEditText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if (seachOptionsEachChangeCheckBox.isChecked() == true) {
					performSearch(s.toString());
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			public void afterTextChanged(Editable s) {				
			}
		});
				
		wordDictionarySearchElementsNoTextView.setText(getString(R.string.word_dictionary_elements_no, 0));
		
		Button reportProblemButton = (Button)findViewById(R.id.word_dictionary_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				EditText searchValueEditText = (EditText)findViewById(R.id.word_dictionary_search_value);
				ListView searchResultListView = (ListView)findViewById(R.id.word_dictionary_search_result_list);
				
				WordDictionaryListItemAdapter searchResultListViewAdapter = (WordDictionaryListItemAdapter)searchResultListView.getAdapter();				
				
				StringBuffer searchListText = new StringBuffer();
				
				for (int searchResultListViewAdapterIdx = 0; searchResultListViewAdapterIdx < searchResultListViewAdapter.size(); ++searchResultListViewAdapterIdx) {
					searchListText.append(searchResultListViewAdapter.getItem(searchResultListViewAdapterIdx).getText().toString()).append("\n--\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.word_dictionary_search_report_problem_email_subject);
				
				String mailBody = getString(R.string.word_dictionary_search_report_problem_email_body,
						searchValueEditText.getText(), searchListText.toString());
				
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
		
		final ScrollView searchOptionsScrollView = (ScrollView)findViewById(R.id.word_dictionary_search_options_scrollview);
		
		final Button searchOptionsButton = (Button)findViewById(R.id.word_dictionary_search_options_button);
		
		searchOptionsButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				int searchOptionsButtonVisibility = searchOptionsScrollView.getVisibility();
				
				if (searchOptionsButtonVisibility == View.GONE) {
					searchOptionsScrollView.setVisibility(View.VISIBLE);
					
					searchOptionsButton.setText(getString(R.string.word_dictionary_search_options_button_hide));
				} else {
					searchOptionsScrollView.setVisibility(View.GONE);
					
					searchOptionsButton.setText(getString(R.string.word_dictionary_search_options_button));
				}
			}
		});
		
		setSearchButtonVisible();
		
		LinearLayout searchOptionsLinearLayout = (LinearLayout)findViewById(R.id.word_dictionary_search_options_scrollview_linearlayout);
		
		List<DictionaryEntryType> addableDictionaryEntryList = DictionaryEntryType.getAddableDictionaryEntryList();
		
		searchDictionaryEntryListCheckBox = new CheckBox[addableDictionaryEntryList.size() + 1];
		
		for (int addableDictionaryEntryListIdx = 0; addableDictionaryEntryListIdx < addableDictionaryEntryList.size(); ++addableDictionaryEntryListIdx) {
			
			searchDictionaryEntryListCheckBox[addableDictionaryEntryListIdx] = 
					createCheckBox(addableDictionaryEntryList.get(addableDictionaryEntryListIdx).getName(), true);
			
			searchDictionaryEntryListCheckBox[addableDictionaryEntryListIdx].setTag(
					addableDictionaryEntryList.get(addableDictionaryEntryListIdx));
			
			searchDictionaryEntryListCheckBox[addableDictionaryEntryListIdx].setOnClickListener(searchOptionsOnClick);
			
			searchOptionsLinearLayout.addView(searchDictionaryEntryListCheckBox[addableDictionaryEntryListIdx], 
					searchOptionsLinearLayout.getChildCount() - 1);
		}		
		
		searchDictionaryEntryListCheckBox[searchDictionaryEntryListCheckBox.length - 1] = 
				createCheckBox(getString(R.string.word_dictionary_search_options_search_word_entry_type_other), true);
		
		searchDictionaryEntryListCheckBox[searchDictionaryEntryListCheckBox.length - 1].setOnClickListener(searchOptionsOnClick);
		
		searchOptionsLinearLayout.addView(searchDictionaryEntryListCheckBox[searchDictionaryEntryListCheckBox.length - 1], 
				searchOptionsLinearLayout.getChildCount() - 1);
		
		// search from other activity
		FindWordRequest findWordRequest = (FindWordRequest)getIntent().getSerializableExtra("findWordRequest");
		
		String inputFindWord = null;
		
		if (findWordRequest != null) { // dla parametrow wyszukiwania
			
			WordPlaceSearch wordPlaceSearch = findWordRequest.wordPlaceSearch;
			
			if (wordPlaceSearch == WordPlaceSearch.START_WITH) {
				searchOptionsStartWithPlaceRadioButton.setChecked(true);
				
			} /*else if (wordPlaceSearch == WordPlaceSearch.ANY_PLACE) {
				searchOptionsAnyPlaceRadioButton.setChecked(true);
				
			} */ else if (wordPlaceSearch == WordPlaceSearch.EXACT) {
				searchOptionsExactPlaceRadioButton.setChecked(true);
				
			} else {
				throw new RuntimeException("Unknown wordplaceSearch: " + wordPlaceSearch);
			}
			
			searchOptionsOnlyCommonWordsCheckbox.setChecked(findWordRequest.searchOnlyCommonWord);
			searchOptionsKanjiCheckbox.setChecked(findWordRequest.searchKanji);
			searchOptionsKanaCheckbox.setChecked(findWordRequest.searchKana);
			searchOptionsRomajiCheckbox.setChecked(findWordRequest.searchRomaji);
			searchOptionsTranslateCheckbox.setChecked(findWordRequest.searchTranslate);
			searchOptionsInfoCheckbox.setChecked(findWordRequest.searchInfo);
			searchOptionsGrammaExampleSearchCheckbox.setChecked(findWordRequest.searchGrammaFormAndExamples);
			
			inputFindWord = findWordRequest.word;
		}
		
		searchValueEditText.setText(inputFindWord);
		
		if (seachOptionsEachChangeCheckBox.isChecked() == false) {
			performSearch(inputFindWord);
		}
	}
	
	private void setSearchButtonVisible() {
		
		if (seachOptionsEachChangeCheckBox.isChecked() == false) {
			searchButton.setVisibility(View.VISIBLE);
		} else {
			searchButton.setVisibility(View.GONE);
			
			performSearch(searchValueEditText.getText().toString());
		}
		
		WordDictionarySearchConfig wordDictionarySearchConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getWordDictionarySearchConfig();
		
		wordDictionarySearchConfig.setEachChangeSearch(seachOptionsEachChangeCheckBox.isChecked());
	}
	
	private CheckBox createCheckBox(String text, boolean checked) {
		
		CheckBox newCheckBox = new CheckBox(this);
		
		newCheckBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		newCheckBox.setChecked(checked);
		
		newCheckBox.setTextSize(12.0f);
		newCheckBox.setText(text);

		return newCheckBox;
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	private void performSearch(final String findWord) {
		
		// sprawdzic, czy nalezy przliczyc wszystkie formy i je zapisac do bazy danych
		if (searchOptionsGrammaExampleSearchCheckbox.isChecked() == true) {
			
			final DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);
			
			int grammaFormAndExamplesEntriesSize = dictionaryManager.getGrammaFormAndExamplesEntriesSize();
			
			// przelic
			if (grammaFormAndExamplesEntriesSize == 0) {
				
				final ProgressDialog progressDialog = new ProgressDialog(WordDictionary.this);
				
				progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
				
				progressDialog.setMessage(getString(R.string.word_dictionary_search_count_form));
				progressDialog.setCancelable(false);
				
				progressDialog.setProgress(5);
				progressDialog.setMax(10);
				
				progressDialog.show();
				
		    	class ProgressInfo {
		    		Integer progressBarMaxValue;
		    		
		    		Integer progressBarValue;  		
		    	}
		        
		        class CountFormAsyncTask extends AsyncTask<Void, ProgressInfo, Void> {
		        	        	
		        	class LoadWithProgress implements ILoadWithProgress {

		        		@Override
						public void setMaxValue(int maxValue) {					
							ProgressInfo progressInfo = new ProgressInfo();
							
							progressInfo.progressBarMaxValue = maxValue;
							
							publishProgress(progressInfo);
						}
						
						@Override
						public void setCurrentPos(int currentPos) {
							ProgressInfo progressInfo = new ProgressInfo();
							
							progressInfo.progressBarValue = currentPos;
							
							publishProgress(progressInfo);
						}

						@Override
						public void setDescription(String desc) {
						}

						@Override
						public void setError(String errorMessage) {
						}
		        	}
		        	
					@Override
					protected Void doInBackground(Void... params) {
										
						LoadWithProgress loadWithProgress = new LoadWithProgress();
						
						try {
							dictionaryManager.countForm(loadWithProgress, getResources());
						} catch (DictionaryException e) {
							throw new RuntimeException(e);
						}						
						
						return null;
					}
					
					@Override
					protected void onProgressUpdate(ProgressInfo... values) {
						super.onProgressUpdate(values);
						
						ProgressInfo progressInfo = values[0];
						
						if (progressInfo.progressBarMaxValue != null) {
							progressDialog.setMax(progressInfo.progressBarMaxValue);
						}

						if (progressInfo.progressBarValue != null) {
							progressDialog.setProgress(progressInfo.progressBarValue);
						}		
					}
					
				    @Override
				    protected void onPostExecute(Void o) {
				    	
				    	progressDialog.dismiss();				    	
				    	
				    	performRealSearch(findWord);
				    }
		        }
		        
		        CountFormAsyncTask countFormAsyncTask = new CountFormAsyncTask();
		        
		        countFormAsyncTask.execute();
		        
			} else {
				
				performRealSearch(findWord);
			}
		} else {
			performRealSearch(findWord);			
		}		
	}
	
	private void performRealSearch(final String findWord) {
		
		// logowanie
		JapaneseAndroidLearnHelperApplication.getInstance().logEvent(getString(R.string.logs_word_dictionary), getString(R.string.logs_word_dictionary_search_event));
			
		searchResultList.clear();
						
		final FindWordRequest findWordRequest = new FindWordRequest();
		
		findWordRequest.word = findWord;
		
		findWordRequest.searchOnlyCommonWord = searchOptionsOnlyCommonWordsCheckbox.isChecked();
		findWordRequest.searchKanji = searchOptionsKanjiCheckbox.isChecked();
		findWordRequest.searchKana = searchOptionsKanaCheckbox.isChecked();
		findWordRequest.searchRomaji = searchOptionsRomajiCheckbox.isChecked();
		findWordRequest.searchTranslate = searchOptionsTranslateCheckbox.isChecked();
		findWordRequest.searchInfo = searchOptionsInfoCheckbox.isChecked();
		
		findWordRequest.searchGrammaFormAndExamples = searchOptionsGrammaExampleSearchCheckbox.isChecked();
		
		/*
		if (searchOptionsAnyPlaceRadioButton.isChecked() == true) {
			findWordRequest.wordPlaceSearch = FindWordRequest.WordPlaceSearch.ANY_PLACE;
		} else*/
		if (searchOptionsStartWithPlaceRadioButton.isChecked() == true) {
			findWordRequest.wordPlaceSearch = FindWordRequest.WordPlaceSearch.START_WITH;
		} else if (searchOptionsExactPlaceRadioButton.isChecked() == true) {
			findWordRequest.wordPlaceSearch = FindWordRequest.WordPlaceSearch.EXACT;
		}
		
		boolean performSearch = true;
		
		if (searchDictionaryEntryListCheckBox != null) {
			
			List<DictionaryEntryType> searchDictionaryEntryList = new ArrayList<DictionaryEntryType>();
			
			for (CheckBox currentDictionaryEntryListCheckBox : searchDictionaryEntryListCheckBox) {
				
				if (currentDictionaryEntryListCheckBox.isChecked() == true) {
					DictionaryEntryType currentDictionaryEntryForCheckBox = (DictionaryEntryType)currentDictionaryEntryListCheckBox.getTag();
					
					if (currentDictionaryEntryForCheckBox != null) {
						searchDictionaryEntryList.add(currentDictionaryEntryForCheckBox);
					} else {
						searchDictionaryEntryList.addAll(DictionaryEntryType.getOtherDictionaryEntryList());
					}
				}
			}
						
			if (searchDictionaryEntryList.size() == 0) {
				Toast toast = Toast.makeText(WordDictionary.this, getString(R.string.word_dictionary_search_options_search_word_no_entries), Toast.LENGTH_SHORT);
				
				toast.show();
				
				performSearch = false;
			}
			
			if (searchDictionaryEntryList.size() == DictionaryEntryType.values().length) {
				searchDictionaryEntryList = null;
			}
			
			findWordRequest.dictionaryEntryTypeList = searchDictionaryEntryList;
		}		
		
		if (performSearch == true && findWordRequest.searchKanji == false &&
				findWordRequest.searchKana == false &&
				findWordRequest.searchRomaji == false &&
				findWordRequest.searchTranslate == false &&
				findWordRequest.searchInfo == false) {
			
			Toast toast = Toast.makeText(WordDictionary.this, getString(R.string.word_dictionary_no_search_options_info), Toast.LENGTH_SHORT);
								
			toast.show();
			
			performSearch = false;
		}
		
		if (findWord != null && findWord.length() > 0 && performSearch == true) {
			
			final ProgressDialog progressDialog = ProgressDialog.show(WordDictionary.this, 
					getString(R.string.word_dictionary_searching1),
					getString(R.string.word_dictionary_searching2));
			
			class FindWordAsyncTask extends AsyncTask<Void, Void, FindWordResult> {
				
				@Override
				protected FindWordResult doInBackground(Void... params) {
					
					final DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(WordDictionary.this);
					
					FindWordResult findWordResult = dictionaryManager.findWord(findWordRequest);
					
					int fixme = 1;
					ServerClient serverClient = new ServerClient();
					
					serverClient.search(findWordRequest);
					
					return findWordResult;
				}
				
			    @Override
			    protected void onPostExecute(FindWordResult foundWord) {
			    	
			        super.onPostExecute(foundWord);
			        
					wordDictionarySearchElementsNoTextView.setText(getString(R.string.word_dictionary_elements_no, "" + foundWord.result.size() +
							(foundWord.moreElemetsExists == true ? "+" : "" )));
					
					for (ResultItem currentFoundWord : foundWord.result) {
						
						String currentFoundWordFullTextWithMark = getWordFullTextWithMark(currentFoundWord, findWord, findWordRequest);
																				
						searchResultList.add(new WordDictionaryListItem(currentFoundWord, Html.fromHtml(currentFoundWordFullTextWithMark.replaceAll("\n", "<br/>"))));								
					}

					searchResultArrayAdapter.notifyDataSetChanged();
			        
			        progressDialog.dismiss();
			        
			        if (foundWord.result.size() == 0 && automaticSendMissingWordCheckBox.isChecked() == true) {			        
			        	sendMissingWord(findWordRequest);
			        }
			    }
			    
			    private String getWordFullTextWithMark(ResultItem resultItem, String findWord, FindWordRequest findWordRequest) {

			    	String kanji = resultItem.getKanji();
			    	String prefixKana = resultItem.getPrefixKana();
			    	List<String> kanaList = resultItem.getKanaList();
			    	String prefixRomaji = resultItem.getPrefixRomaji();
			    	List<String> romajiList = resultItem.getRomajiList();
			    	List<String> translates = resultItem.getTranslates();
			    	String info = resultItem.getInfo();
			    	
			    	StringBuffer result = new StringBuffer();
			    	
			    	String tempPrefixKana = prefixKana != null && prefixKana.equals("") == false ? prefixKana : null;
			    	String tempPrefixRomaji = prefixRomaji != null && prefixRomaji.equals("") == false ? prefixRomaji : null;

			    	if (resultItem.isKanjiExists() == true) {

			    		if (tempPrefixKana != null) {
			    			result.append("(").append(getStringWithMark(tempPrefixKana, findWord, false)).append(") ");
			    		}

			    		result.append(getStringWithMark(kanji, findWord, findWordRequest.searchKanji)).append(" ");
			    	}

			    	if (kanaList != null && kanaList.size() > 0) {
			    		result.append(getStringWithMark(toString(kanaList, tempPrefixKana), findWord, findWordRequest.searchKana)).append(" - ");
			    	}

			    	if (romajiList != null && romajiList.size() > 0) {
			    		result.append(getStringWithMark(toString(romajiList, tempPrefixRomaji), findWord, findWordRequest.searchRomaji));
			    	}

			    	if (translates != null && translates.size() > 0) {
			    		result.append("\n\n");
			    		result.append(getStringWithMark(toString(translates, null), findWord, findWordRequest.searchTranslate));
			    	}

			    	if (info != null && info.equals("") == false) {
			    		result.append(" - ").append(getStringWithMark(info, findWord, findWordRequest.searchInfo));
			    	}

			    	return result.toString();
			    }
			    
			    private String getStringWithMark(String text, String findWord, boolean mark) {
			    	
			    	if (mark == false) {
			    		return text;
			    	}
			    	
			    	String findWordLowerCase = findWord.toLowerCase(Locale.getDefault());
			    	
					StringBuffer texStringBuffer = new StringBuffer(text);								
					StringBuffer textLowerCaseStringBuffer = new StringBuffer(text.toLowerCase(Locale.getDefault()));
													
					int idxStart = 0;
					
					final String fontBegin = "<font color='red'>";
					final String fontEnd = "</font>";
					
					while(true) {
						
						int idx1 = textLowerCaseStringBuffer.indexOf(findWordLowerCase, idxStart);
						
						if (idx1 == -1) {
							break;
						}
						
						texStringBuffer.insert(idx1, fontBegin);
						textLowerCaseStringBuffer.insert(idx1, fontBegin);
						
						texStringBuffer.insert(idx1 + findWordLowerCase.length() + fontBegin.length(), fontEnd);
						textLowerCaseStringBuffer.insert(idx1 + findWordLowerCase.length() + fontBegin.length(), fontEnd);

						idxStart = idx1 + findWordLowerCase.length() + fontBegin.length() + fontEnd.length();
					}
					
					return texStringBuffer.toString();
			    }
			    
				private String toString(List<String> listString, String prefix) {
					
					StringBuffer sb = new StringBuffer();
					
					sb.append("[");
					
					for (int idx = 0; idx < listString.size(); ++idx) {
						if (prefix != null) {
							sb.append("(").append(prefix).append(") ");
						}
						
						sb.append(listString.get(idx));
						
						if (idx != listString.size() - 1) {
							sb.append(", ");
						}
					}
					
					sb.append("]");
					
					return sb.toString();
				}
			}
			
			new FindWordAsyncTask().execute();
			
		} else {					
			searchResultArrayAdapter.notifyDataSetChanged();
			
			wordDictionarySearchElementsNoTextView.setText(getString(R.string.word_dictionary_elements_no, 0));
		}		
	}
	
	private void sendMissingWord(final FindWordRequest findWordRequest) {
		
		class SendMissingWordTask extends AsyncTask<Void, Void, Void> {

			@Override
			protected Void doInBackground(Void... params) {
				
				try {				
					ServerClient serverClient = new ServerClient();
										
					serverClient.sendMissingWord(findWordRequest.word, findWordRequest.wordPlaceSearch);
					
				} catch (Exception e) {
					// noop
				}
				
				return null;
			}		
		}
		
		new SendMissingWordTask().execute();
	}
}
