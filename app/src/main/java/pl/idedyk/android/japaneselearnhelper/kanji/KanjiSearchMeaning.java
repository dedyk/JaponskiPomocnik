package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.common.adapter.AutoCompleteAdapter;
import pl.idedyk.android.japaneselearnhelper.common.view.DelayAutoCompleteTextView;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiSearchMeaningConfig;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem.ItemType;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient;
import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient.AutoCompleteSuggestionType;
import pl.idedyk.android.japaneselearnhelper.utils.SearchHistoryHelper;
import pl.idedyk.android.japaneselearnhelper.utils.WordKanjiDictionaryUtils;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiResult;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.WordPlaceSearch;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class KanjiSearchMeaning extends Activity {

	private static final String kanjiSearchMeaningSearchHistoryFieldName = "kanjiSearchMeaningSearchHistory";
	
	private TextView kanjiSearchMeaningElementsNoTextView;
	
	private ListView searchResultListView;
	
	private List<KanjiEntryListItem> searchResultList;
	
	private KanjiEntryListItemAdapter searchResultArrayAdapter;
	
	private DelayAutoCompleteTextView searchValueEditText;
	
	private Button searchButton;
	
	private CheckBox searchOptionsEachChangeCheckBox;
	private CheckBox searchOptionsUseAutocompleteCheckBox;
	private CheckBox searchOptionsUseSuggestionCheckBox;
	
	private RadioButton searchOptionsAnyPlaceRadioButton;
	private RadioButton searchOptionsStartWithPlaceRadioButton;
	private RadioButton searchOptionsExactPlaceRadioButton;

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

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.kanji_search_meaning);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_kanji_search_meaning));

		kanjiSearchMeaningElementsNoTextView = (TextView)findViewById(R.id.kanji_search_meaning_elements_no);
		
		searchResultListView = (ListView)findViewById(R.id.kanji_search_meaning_result_list);
		
		searchResultList = new ArrayList<KanjiEntryListItem>();
		Typeface babelStoneHanTypeface = JapaneseAndroidLearnHelperApplication.getInstance().getBabelStoneHanSubset(getAssets());
		
		searchResultArrayAdapter = new KanjiEntryListItemAdapter(this, searchResultList, babelStoneHanTypeface);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								
				KanjiEntryListItem kanjiEntryListItem = (KanjiEntryListItem)searchResultArrayAdapter.getItem(position);
				
				ItemType itemType = kanjiEntryListItem.getItemType();
				
				if (itemType == ItemType.KANJI_ENTRY) {
					
					Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);
					
					intent.putExtra("item", kanjiEntryListItem.getKanjiEntry());
					
					startActivity(intent);
					
				} else if (itemType == ItemType.SUGGESTION_VALUE) { // klikniecie w sugestie
					
					// wstawienie napisu
					searchValueEditText.setText(kanjiEntryListItem.getSuggestion());

					// resetowanie ustawien wyszukiwania
					searchOptionsStartWithPlaceRadioButton.setChecked(true);
					
					// wykonanie wyszukiwania
					performSearch(searchValueEditText.getText().toString());

				} else if (itemType == ItemType.SHOW_HISTORY_VALUE) { // klikniecie w historie wyszukiwania

					// wstawienie napisu
					searchValueEditText.setText(kanjiEntryListItem.getHistoryValue());

					// resetowanie ustawien wyszukiwania
					searchOptionsStartWithPlaceRadioButton.setChecked(true);

					// wykonanie wyszukiwania
					performSearch(searchValueEditText.getText().toString());
				}
			}
		});
		
		searchValueEditText = (DelayAutoCompleteTextView)findViewById(R.id.kanji_search_meaning_search_value);		
		
		searchButton = (Button)findViewById(R.id.kanji_search_meaning_search_button);
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				performSearch(searchValueEditText.getText().toString());
			}
		});
		
		searchOptionsEachChangeCheckBox = (CheckBox)findViewById(R.id.kanji_search_meaning_options_search_each_change_checkbox);
		
		final KanjiSearchMeaningConfig kanjiSearchMeaningConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getKanjiSearchMeaningConfig();
		
		searchOptionsEachChangeCheckBox.setChecked(kanjiSearchMeaningConfig.getEachChangeSearch());
		
		searchOptionsEachChangeCheckBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				setSearchButtonVisible();
			}
		});
		
		//
		
		searchOptionsUseAutocompleteCheckBox = (CheckBox)findViewById(R.id.kanji_search_meaning_options_search_use_autocomplete_checkbox);
		
		searchOptionsUseAutocompleteCheckBox.setChecked(kanjiSearchMeaningConfig.getUseAutocomplete());
		
		searchValueEditText.setUseAutocompleteCheckBox(searchOptionsUseAutocompleteCheckBox);
		
		searchOptionsUseAutocompleteCheckBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				kanjiSearchMeaningConfig.setUseAutocomplete(searchOptionsUseAutocompleteCheckBox.isChecked());
				
			}
		});
		
		//
		
		searchOptionsUseSuggestionCheckBox = (CheckBox)findViewById(R.id.kanji_search_meaning_options_search_use_suggestion_checkbox);
		
		searchOptionsUseSuggestionCheckBox.setChecked(kanjiSearchMeaningConfig.getUseSuggestion());
		
		searchOptionsUseSuggestionCheckBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {

				kanjiSearchMeaningConfig.setUseSuggestion(searchOptionsUseSuggestionCheckBox.isChecked());
				
			}
		});
		
		//
				
		searchOptionsAnyPlaceRadioButton = (RadioButton)findViewById(R.id.kanji_search_meaning_options_search_any_place_radiobutton);
		searchOptionsStartWithPlaceRadioButton = (RadioButton)findViewById(R.id.kanji_search_meaning_options_search_startwith_radiobutton);
		searchOptionsExactPlaceRadioButton = (RadioButton)findViewById(R.id.kanji_search_meaning_options_search_exact_radiobutton);
		
		OnClickListener searchOptionsOnClick = new OnClickListener() {			
			public void onClick(View view) {
				
				if (searchOptionsEachChangeCheckBox.isChecked() == true) {
					performSearch(searchValueEditText.getText().toString());
				}
			}
		};
				
		searchOptionsAnyPlaceRadioButton.setOnClickListener(searchOptionsOnClick);
		searchOptionsStartWithPlaceRadioButton.setOnClickListener(searchOptionsOnClick);
		searchOptionsExactPlaceRadioButton.setOnClickListener(searchOptionsOnClick);
		
		searchValueEditText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				if (searchOptionsEachChangeCheckBox.isChecked() == true) {
					performSearch(s.toString());
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			public void afterTextChanged(Editable s) {				
			}
		});

		searchValueEditText.setOnKeyListener(new View.OnKeyListener() {
			public boolean onKey(View view, int keyCode, KeyEvent keyevent) {

				// nacisniecie entera
				if ((keyevent.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {

					if (searchOptionsEachChangeCheckBox.isChecked() == false) {
						performSearch(searchValueEditText.getText().toString());
					}

					return true;
				}
				return false;
			}
		});

		final Button showHistoryButton = (Button)findViewById(R.id.kanji_search_meaning_show_history_button);

		showHistoryButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {

				// pokazywanie histori wyszukiwania

				searchResultList.clear();

				searchResultList.add(KanjiEntryListItem.createKanjiEntryListItemAsTitle(Html.fromHtml("<big><b>" + getString(R.string.kanji_search_meaning_show_history_title) + "</b></big>")));

				SearchHistoryHelper searchHistoryHelper = new SearchHistoryHelper(KanjiSearchMeaning.this, kanjiSearchMeaningSearchHistoryFieldName);

				List<SearchHistoryHelper.Entry> historyEntryList = searchHistoryHelper.getEntryList();

				for (SearchHistoryHelper.Entry entry : historyEntryList) {
					searchResultList.add(KanjiEntryListItem.createKanjiEntryListItemAsHistoryValue(entry.getText(), Html.fromHtml("<big>" + entry.getText() + "</big>")));
				}

				kanjiSearchMeaningElementsNoTextView.setText(getString(R.string.kanji_entry_elements_no, historyEntryList.size()));

				searchResultArrayAdapter.notifyDataSetChanged();
				searchResultListView.setSelection(0);
			}
		});

		final Button pasteFromCliboardButton = (Button)findViewById(R.id.kanji_search_meaning_paste_from_clipboard_button);

		pasteFromCliboardButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				String textFromClipboard = null;

				// pobranie managera schowka
				ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);

				if (clipboard.hasPrimaryClip() == false) {
					Toast toast = Toast.makeText(KanjiSearchMeaning.this, getString(R.string.kanji_search_meaning_paste_from_clipboard_no_clipboard_data), Toast.LENGTH_SHORT);

					toast.show();

					return;
					
				} else {
					// schowek zawiera tekst, pobranie go
					ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);

					textFromClipboard = item.getText() != null ? item.getText().toString() : null;
				}

				if (textFromClipboard != null && textFromClipboard.length() > 0) {
					// wstawienie napisu
					searchValueEditText.setText(textFromClipboard);

					// resetowanie ustawien wyszukiwania
					searchOptionsStartWithPlaceRadioButton.setChecked(true);

					// wykonanie wyszukiwania
					performSearch(searchValueEditText.getText().toString());
				}
			}
		});

		searchValueEditText.setAdapter(new AutoCompleteAdapter(this, AutoCompleteSuggestionType.KANJI_DICTIONARY));
				
		kanjiSearchMeaningElementsNoTextView.setText(getString(R.string.kanji_search_meaning_elements_no, 0));
		
		Button reportProblemButton = (Button)findViewById(R.id.kanji_search_meaning_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
								
				KanjiEntryListItemAdapter searchResultListViewAdapter = (KanjiEntryListItemAdapter)searchResultListView.getAdapter();				
				
				StringBuffer searchListText = new StringBuffer();
				
				for (int searchResultListViewAdapterIdx = 0; searchResultListViewAdapterIdx < searchResultListViewAdapter.size(); ++searchResultListViewAdapterIdx) {
					searchListText.append(((KanjiEntryListItem)searchResultListViewAdapter.getItem(searchResultListViewAdapterIdx)).getText().toString()).append("\n--\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kanji_search_meaning_report_problem_email_subject);
				
				String mailBody = getString(R.string.kanji_search_report_problem_email_body,
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
		
		final ScrollView searchOptionsScrollView = (ScrollView)findViewById(R.id.kanji_search_meaning_options_scrollview);
		
		final Button searchOptionsButton = (Button)findViewById(R.id.kanji_search_meaning_options_button);
		
		searchOptionsButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				int searchOptionsButtonVisibility = searchOptionsScrollView.getVisibility();
				
				if (searchOptionsButtonVisibility == View.GONE) {
					searchOptionsScrollView.setVisibility(View.VISIBLE);
					
					searchOptionsButton.setText(getString(R.string.kanji_search_meaning_options_button_hide));
				} else {
					searchOptionsScrollView.setVisibility(View.GONE);
					
					searchOptionsButton.setText(getString(R.string.kanji_search_meaning_options_button));
				}
			}
		});
		
		setSearchButtonVisible();
	}

	public void onBackPressed() {
		super.onBackPressed();

		finish();
	}

	private void setSearchButtonVisible() {
		
		if (searchOptionsEachChangeCheckBox.isChecked() == false) {
			searchButton.setVisibility(View.VISIBLE);
		} else {
			searchButton.setVisibility(View.GONE);
			
			performSearch(searchValueEditText.getText().toString());
		}
		
		KanjiSearchMeaningConfig kanjiSearchMeaningConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getKanjiSearchMeaningConfig();
		
		kanjiSearchMeaningConfig.setEachChangeSearch(searchOptionsEachChangeCheckBox.isChecked());
	}
	
	private void performSearch(final String findWord) {
		
		// logowanie
		JapaneseAndroidLearnHelperApplication.getInstance().logEvent(this, getString(R.string.logs_kanji_search_meaning), getString(R.string.logs_kanji_search_meaning_search_event), findWord);
			
		searchResultList.clear();
						
		final FindKanjiRequest findKanjiRequest = new FindKanjiRequest();
		
		findKanjiRequest.word = findWord;
		
		if (searchOptionsAnyPlaceRadioButton.isChecked() == true) {
			findKanjiRequest.wordPlaceSearch = WordPlaceSearch.ANY_PLACE;
			
		} else if (searchOptionsStartWithPlaceRadioButton.isChecked() == true) {
			findKanjiRequest.wordPlaceSearch = WordPlaceSearch.START_WITH;
			
		} else if (searchOptionsExactPlaceRadioButton.isChecked() == true) {
			findKanjiRequest.wordPlaceSearch = WordPlaceSearch.EXACT;
		}
				
		if (findWord != null && findWord.length() > 0) {
			
			final ProgressDialog progressDialog = ProgressDialog.show(KanjiSearchMeaning.this, 
					getString(R.string.kanji_search_meaning_searching1),
					getString(R.string.kanji_search_meaning_searching2));
			
			class FindWordAsyncTask extends AsyncTask<Void, Void, FindKanjiResultAndSuggestionList> {
				
				@Override
				protected FindKanjiResultAndSuggestionList doInBackground(Void... params) {
					
					final DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(KanjiSearchMeaning.this);

					FindKanjiResultAndSuggestionList findKanjiResultAndSuggestionList = new FindKanjiResultAndSuggestionList();

					FindKanjiResult findKanjiResult = null;

					try {
						findKanjiResult = dictionaryManager.findKanji(findKanjiRequest);

					} catch (DictionaryException e) {
						findKanjiResultAndSuggestionList.dictionaryException = e;

						findKanjiResult = new FindKanjiResult();

						findKanjiResult.setResult(new ArrayList<KanjiCharacterInfo>());
					}

					// jesli nic nie znaleziono, proba pobrania znakow z napisu
					if (findKanjiResult.getResult().size() == 0 && findKanjiRequest.word != null && findKanjiRequest.word.trim().equals("") == false && findKanjiResultAndSuggestionList.dictionaryException == null) {

						try {
							List<KanjiCharacterInfo> findKnownKanjiResult = dictionaryManager.findKnownKanji(findKanjiRequest.word);

							findKanjiResult.setResult(findKnownKanjiResult);

						} catch (DictionaryException e) {
							findKanjiResultAndSuggestionList.dictionaryException = e;
						}
					}
					
					//
										
					findKanjiResultAndSuggestionList.findKanjiResult = findKanjiResult;
					
					// szukanie sugestii
					if (findKanjiResult.result.size() == 0 && searchOptionsUseSuggestionCheckBox.isChecked() == true && findKanjiResultAndSuggestionList.dictionaryException == null) {
						
						ServerClient serverClient = new ServerClient();

						PackageInfo packageInfo = null;

						try {
							packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

						} catch (NameNotFoundException e) {        	
						}
						
						List<String> suggestionList = serverClient.getSuggestionList(packageInfo, findKanjiRequest.word, AutoCompleteSuggestionType.KANJI_DICTIONARY);

						findKanjiResultAndSuggestionList.suggestionList = suggestionList;
					}
					
					return findKanjiResultAndSuggestionList;
				}
				
			    @Override
			    protected void onPostExecute(FindKanjiResultAndSuggestionList findKanjiResultAndSuggestionList) {
			    	
			        super.onPostExecute(findKanjiResultAndSuggestionList);

					if (findKanjiResultAndSuggestionList.dictionaryException != null) {

						Toast.makeText(KanjiSearchMeaning.this, getString(R.string.dictionary_exception_common_error_message, findKanjiResultAndSuggestionList.dictionaryException.getMessage()), Toast.LENGTH_LONG).show();
					}

					FindKanjiResult findKanjiResult = findKanjiResultAndSuggestionList.findKanjiResult;
			        List<String> suggestionList = findKanjiResultAndSuggestionList.suggestionList;
			        
			        kanjiSearchMeaningElementsNoTextView.setText(getString(R.string.kanji_entry_elements_no, "" + findKanjiResult.result.size() +
							(findKanjiResult.moreElemetsExists == true ? "+" : "" )));
					
					for (KanjiCharacterInfo currentKanjiEntry : findKanjiResult.result) {
						
						String currentFoundKanjiFullTextWithMarks = WordKanjiDictionaryUtils.getKanjiFullTextWithMark(currentKanjiEntry, findWord);
						String currentFoundKanjiRadicalTextWithMarks = WordKanjiDictionaryUtils.getKanjiRadicalTextWithMark(currentKanjiEntry, findWord);
						
						searchResultList.add(KanjiEntryListItem.createKanjiEntryListItemAsKanjiEntry(currentKanjiEntry,
								Html.fromHtml(currentFoundKanjiFullTextWithMarks.replaceAll("\n", "<br/>")),
								Html.fromHtml(currentFoundKanjiRadicalTextWithMarks.toString())));								
					}
					
					if (suggestionList != null && suggestionList.size() > 0) { // pokazywanie sugestii

						searchResultList.add(KanjiEntryListItem.createKanjiEntryListItemAsTitle(Html.fromHtml("<big><b>" + getString(R.string.kanji_search_meaning_suggestion_title) + "</b></big>")));

						for (String currentSuggestion : suggestionList) {							
							searchResultList.add(KanjiEntryListItem.createKanjiEntryListItemAsSuggestionValue(currentSuggestion, Html.fromHtml("<big>" + currentSuggestion + "</big>")));
						}
					}

					searchResultArrayAdapter.notifyDataSetChanged();
					searchResultListView.setSelection(0);
			        
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}

					// zapisywanie do historii wyszukiwania
					SearchHistoryHelper searchHistoryHelper = new SearchHistoryHelper(KanjiSearchMeaning.this, kanjiSearchMeaningSearchHistoryFieldName);

					searchHistoryHelper.addEntry(new SearchHistoryHelper.Entry(findKanjiRequest.word));
				}
			}
			
			new FindWordAsyncTask().execute();
			
		} else {					
			searchResultArrayAdapter.notifyDataSetChanged();
			searchResultListView.setSelection(0);
			
			kanjiSearchMeaningElementsNoTextView.setText(getString(R.string.kanji_entry_elements_no, 0));
		}		
	}
	
	class FindKanjiResultAndSuggestionList {

		public FindKanjiResult findKanjiResult;

		public List<String> suggestionList;

		public DictionaryException dictionaryException;
	}
}
