package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordRequest;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordResult;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

public class WordDictionary extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_dictionary);
		
		final TextView wordDictionarySearchElementsNoTextView = (TextView)findViewById(R.id.word_dictionary_elements_no);
		
		ListView searchResultListView = (ListView)findViewById(R.id.word_dictionary_search_result_list);
		
		final List<WordDictionaryListItem> searchResultList = new ArrayList<WordDictionaryListItem>();
		final WordDictionaryListItemAdapter searchResultArrayAdapter = new WordDictionaryListItemAdapter(this, 
				R.layout.word_dictionary_simplerow, searchResultList);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				WordDictionaryListItem wordDictionaryListItem = searchResultArrayAdapter.getItem(position);
				
				Intent intent = new Intent(getApplicationContext(), WordDictionaryDetails.class);
				
				intent.putExtra("item", wordDictionaryListItem.getDictionaryEntry());
				
				startActivity(intent);
				
			}
		});
		
		final EditText searchValueEditText = (EditText)findViewById(R.id.word_dictionary_search_value);		
		
		final CheckBox searchOptionsKanjiCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_kanji_checkbox);
		final CheckBox searchOptionsKanaCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_kana_checkbox);
		final CheckBox searchOptionsRomajiCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_romaji_checkbox);
		final CheckBox searchOptionsTranslateCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_translate_checkbox);
		final CheckBox searchOptionsInfoCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_info_checkbox);
		
		OnClickListener searchOptionsOnClick = new OnClickListener() {			
			public void onClick(View view) {
				performSearch(searchValueEditText.getText().toString(), searchResultList, searchResultArrayAdapter, searchOptionsKanjiCheckbox, 
						searchOptionsKanaCheckbox, searchOptionsRomajiCheckbox, searchOptionsTranslateCheckbox, 
						searchOptionsInfoCheckbox, wordDictionarySearchElementsNoTextView);
			}
		};
		
		searchOptionsKanjiCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsKanaCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsRomajiCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsTranslateCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsInfoCheckbox.setOnClickListener(searchOptionsOnClick);		
		
		searchValueEditText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				performSearch(s.toString(), searchResultList, searchResultArrayAdapter, searchOptionsKanjiCheckbox, 
						searchOptionsKanaCheckbox, searchOptionsRomajiCheckbox, searchOptionsTranslateCheckbox, 
						searchOptionsInfoCheckbox, wordDictionarySearchElementsNoTextView);
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
		
		String inputFindWord = (String)getIntent().getSerializableExtra("find");
		
		searchValueEditText.setText(inputFindWord);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
	
	private void performSearch(final String findWord, final List<WordDictionaryListItem> searchResultList,
			final WordDictionaryListItemAdapter searchResultArrayAdapter,
			final CheckBox searchOptionsKanjiCheckbox,
			final CheckBox searchOptionsKanaCheckbox,
			final CheckBox searchOptionsRomajiCheckbox,
			final CheckBox searchOptionsTranslateCheckbox,
			final CheckBox searchOptionsInfoCheckbox,
			final TextView wordDictionarySearchElementsNoTextView) {
		
		searchResultList.clear();
						
		final FindWordRequest findWordRequest = new FindWordRequest();
		
		findWordRequest.word = findWord;
		
		findWordRequest.searchKanji = searchOptionsKanjiCheckbox.isChecked();
		findWordRequest.searchKana = searchOptionsKanaCheckbox.isChecked();
		findWordRequest.searchRomaji = searchOptionsRomajiCheckbox.isChecked();
		findWordRequest.searchTranslate = searchOptionsTranslateCheckbox.isChecked();
		findWordRequest.searchInfo = searchOptionsInfoCheckbox.isChecked();
		
		boolean searchOptionsChoose = true;
		
		if (findWordRequest.searchKanji == false &&
				findWordRequest.searchKana == false &&
				findWordRequest.searchRomaji == false &&
				findWordRequest.searchTranslate == false &&
				findWordRequest.searchInfo == false) {
			
			Toast toast = Toast.makeText(WordDictionary.this, getString(R.string.word_dictionary_no_search_options_info), Toast.LENGTH_SHORT);
								
			toast.show();
			
			searchOptionsChoose = false;
		}
		
		if (findWord != null && findWord.length() > 0 && searchOptionsChoose == true) {
			
			
			final ProgressDialog progressDialog = ProgressDialog.show(WordDictionary.this, 
					getString(R.string.word_dictionary_searching1),
					getString(R.string.word_dictionary_searching2));
			
			class FindWordAsyncTask extends AsyncTask<Void, Void, FindWordResult> {
				
				@Override
				protected FindWordResult doInBackground(Void... params) {
					
					final DictionaryManager dictionaryManager = DictionaryManager.getInstance();
					
					return dictionaryManager.findWord(findWordRequest);
				}
				
			    @Override
			    protected void onPostExecute(FindWordResult foundWord) {
			        super.onPostExecute(foundWord);
			        
					wordDictionarySearchElementsNoTextView.setText(getString(R.string.word_dictionary_elements_no, "" + foundWord.result.size() +
							(foundWord.moreElemetsExists == true ? "+" : "" )));

					String findWordLowerCase = findWord.toLowerCase();
					
					for (DictionaryEntry currentFoundWord : foundWord.result) {
						
						String currentFoundWordFullText = currentFoundWord.getFullText(true);
						
						StringBuffer currentFoundWordFullTexStringBuffer = new StringBuffer(currentFoundWordFullText);								
						StringBuffer currentFoundWordFullTextLowerCase = new StringBuffer(currentFoundWordFullText.toLowerCase());
														
						int idxStart = 0;
						
						final String fontBegin = "<font color='red'>";
						final String fontEnd = "</font>";
						
						while(true) {
							
							int idx1 = currentFoundWordFullTextLowerCase.indexOf(findWordLowerCase, idxStart);
							
							if (idx1 == -1) {
								break;
							}
							
							currentFoundWordFullTexStringBuffer.insert(idx1, fontBegin);
							currentFoundWordFullTextLowerCase.insert(idx1, fontBegin);
							
							currentFoundWordFullTexStringBuffer.insert(idx1 + findWordLowerCase.length() + fontBegin.length(), fontEnd);
							currentFoundWordFullTextLowerCase.insert(idx1 + findWordLowerCase.length() + fontBegin.length(), fontEnd);

							idxStart = idx1 + findWordLowerCase.length() + fontBegin.length() + fontEnd.length();
						}
														
						searchResultList.add(new WordDictionaryListItem(currentFoundWord, Html.fromHtml(currentFoundWordFullTexStringBuffer.toString().replaceAll("\n", "<br/>"))));								
					}

					searchResultArrayAdapter.notifyDataSetChanged();
			        
			        progressDialog.dismiss();
			    }
			}
			
			new FindWordAsyncTask().execute();
			
		} else {					
			searchResultArrayAdapter.notifyDataSetChanged();
			
			wordDictionarySearchElementsNoTextView.setText(getString(R.string.word_dictionary_elements_no, 0));
		}		
	}
}
