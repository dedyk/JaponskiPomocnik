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
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem.ItemType;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient;
import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient.AutoCompleteSuggestionType;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiRequest;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiResult;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.WordPlaceSearch;
import pl.idedyk.japanese.dictionary.api.dto.KanjiDic2Entry;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.TextWatcher;
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

public class KanjiSearchMeaning extends Activity {
	
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
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_kanji_search_meaning));
		
		setContentView(R.layout.kanji_search_meaning);
		
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
		JapaneseAndroidLearnHelperApplication.getInstance().logEvent(getString(R.string.logs_kanji_search_meaning), getString(R.string.logs_kanji_search_meaning_search_event), findWord);
			
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
					
					final DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(KanjiSearchMeaning.this);
					
					FindKanjiResult findKanjiResult = dictionaryManager.findKanji(findKanjiRequest);
					
					// jesli nic nie znaleziono, proba pobrania znakow z napisu
					if (findKanjiResult.getResult().size() == 0 && findKanjiRequest.word != null && findKanjiRequest.word.trim().equals("") == false) {
						
						List<KanjiEntry> findKnownKanjiResult = dictionaryManager.findKnownKanji(findKanjiRequest.word);
						
						findKanjiResult.setResult(findKnownKanjiResult);
					}
					
					//
					
					FindKanjiResultAndSuggestionList findKanjiResultAndSuggestionList = new FindKanjiResultAndSuggestionList();
										
					findKanjiResultAndSuggestionList.findKanjiResult = findKanjiResult;
					
					// szukanie sugestii
					if (findKanjiResult.result.size() == 0 && searchOptionsUseSuggestionCheckBox.isChecked() == true) {
						
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
			        
			        FindKanjiResult findKanjiResult = findKanjiResultAndSuggestionList.findKanjiResult;
			        List<String> suggestionList = findKanjiResultAndSuggestionList.suggestionList;
			        
			        kanjiSearchMeaningElementsNoTextView.setText(getString(R.string.kanji_entry_elements_no, "" + findKanjiResult.result.size() +
							(findKanjiResult.moreElemetsExists == true ? "+" : "" )));
					
					for (KanjiEntry currentKanjiEntry : findKanjiResult.result) {
						
						String currentFoundKanjiFullTextWithMarks = getKanjiFullTextWithMark(currentKanjiEntry, findWord);
						String currentFoundKanjiRadicalTextWithMarks = getKanjiRadicalTextWithMark(currentKanjiEntry, findWord);														
						
						searchResultList.add(new KanjiEntryListItem(currentKanjiEntry,
								Html.fromHtml(currentFoundKanjiFullTextWithMarks.replaceAll("\n", "<br/>")),
								Html.fromHtml(currentFoundKanjiRadicalTextWithMarks.toString())));								
					}
					
					if (suggestionList != null && suggestionList.size() > 0) { // pokazywanie sugestii

						searchResultList.add(new KanjiEntryListItem(Html.fromHtml("<big><b>" + getString(R.string.kanji_search_meaning_suggestion_title) + "</b></big>")));

						for (String currentSuggestion : suggestionList) {							
							searchResultList.add(new KanjiEntryListItem(currentSuggestion, Html.fromHtml("<big>" + currentSuggestion + "</big>")));
						}
					}

					searchResultArrayAdapter.notifyDataSetChanged();
			        
					if (progressDialog != null && progressDialog.isShowing()) {
						progressDialog.dismiss();
					}
			    }
			    
			    private String getKanjiFullTextWithMark(KanjiEntry kanjiEntry, String findWord) {

			    	
			    	String kanji = kanjiEntry.getKanji();
			    	List<String> polishTranslates = kanjiEntry.getPolishTranslates();
			    				    				    	
			    	String info = kanjiEntry.getInfo();
			    	
			    	StringBuffer result = new StringBuffer();
			    	
					result.append("<big>").append(getStringWithMark(kanji, findWord)).append("</big> - ");
					result.append(getStringWithMark(toString(polishTranslates, null), findWord));
					
					if (info != null && info.equals("") == false) {
						
						result.append("\n");
						
						result.append(getStringWithMark(info, findWord));
					}
					
					result.append("\n");
					
			    	return result.toString();
			    }

			    private String getKanjiRadicalTextWithMark(KanjiEntry kanjiEntry, String findWord) {
			    	
			    	KanjiDic2Entry kanjiDic2Entry = kanjiEntry.getKanjiDic2Entry();
			    				    	
			    	List<String> radicals = null;
			    	
			    	if (kanjiDic2Entry != null) {
			    		radicals = kanjiDic2Entry.getRadicals();
			    		
			    		if (radicals != null && radicals.size() == 0) {
			    			radicals = null;
			    		}
			    	}
			    				    	
			    	StringBuffer result = new StringBuffer();
			    						
					if (radicals != null) {
						result.append(getStringWithMark(toString(radicals, null), findWord));
					}

			    	return result.toString();
			    }			    
			    
			    private String getStringWithMark(String text, String findWord) {
			    				    	
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
			
			kanjiSearchMeaningElementsNoTextView.setText(getString(R.string.kanji_entry_elements_no, 0));
		}		
	}
	
	class FindKanjiResultAndSuggestionList {

		public FindKanjiResult findKanjiResult;

		public List<String> suggestionList;		
	}
}
