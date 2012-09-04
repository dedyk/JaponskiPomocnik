package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordRequest;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordResult;
import pl.idedyk.android.japaneselearnhelper.dictionary.ILoadWithProgress;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.exception.DictionaryException;
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
import android.widget.RadioButton;
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
		final CheckBox searchOptionsGrammaExampleSearchCheckbox = (CheckBox)findViewById(R.id.word_dictionary_search_options_search_gramma_examples_checkbox);
		
		final RadioButton searchOptionsAnyPlaceRadioButton = (RadioButton)findViewById(R.id.word_dictionary_search_options_search_any_place_radiobutton);
		final RadioButton searchOptionsStartWithPlaceRadioButton = (RadioButton)findViewById(R.id.word_dictionary_search_options_search_startwith_radiobutton);
		
		OnClickListener searchOptionsOnClick = new OnClickListener() {			
			public void onClick(View view) {
				performSearch(searchValueEditText.getText().toString(), searchResultList, searchResultArrayAdapter, searchOptionsKanjiCheckbox, 
						searchOptionsKanaCheckbox, searchOptionsRomajiCheckbox, searchOptionsTranslateCheckbox, 
						searchOptionsInfoCheckbox, searchOptionsGrammaExampleSearchCheckbox, 
						searchOptionsAnyPlaceRadioButton, searchOptionsStartWithPlaceRadioButton, 
						wordDictionarySearchElementsNoTextView);
			}
		};
		
		searchOptionsKanjiCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsKanaCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsRomajiCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsTranslateCheckbox.setOnClickListener(searchOptionsOnClick);
		searchOptionsInfoCheckbox.setOnClickListener(searchOptionsOnClick);	
		searchOptionsGrammaExampleSearchCheckbox.setOnClickListener(searchOptionsOnClick);
		
		searchOptionsAnyPlaceRadioButton.setOnClickListener(searchOptionsOnClick);
		searchOptionsStartWithPlaceRadioButton.setOnClickListener(searchOptionsOnClick);
		
		searchValueEditText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				performSearch(s.toString(), searchResultList, searchResultArrayAdapter, searchOptionsKanjiCheckbox, 
						searchOptionsKanaCheckbox, searchOptionsRomajiCheckbox, searchOptionsTranslateCheckbox, 
						searchOptionsInfoCheckbox, searchOptionsGrammaExampleSearchCheckbox,
						searchOptionsAnyPlaceRadioButton, searchOptionsStartWithPlaceRadioButton,
						wordDictionarySearchElementsNoTextView);
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
			final CheckBox searchOptionsGrammaExampleSearchCheckbox,
			final RadioButton searchAnyPlaceRadioButton,
			final RadioButton searchStartWithRadioButton,
			final TextView wordDictionarySearchElementsNoTextView) {
		
		// sprawdzic, czy nalezy przliczyc wszystkie formy i je zapisac do bazy danych
		if (searchOptionsGrammaExampleSearchCheckbox.isChecked() == true) {
			
			final DictionaryManager dictionaryManager = DictionaryManager.getInstance();
			
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

						public void setMaxValue(int maxValue) {					
							ProgressInfo progressInfo = new ProgressInfo();
							
							progressInfo.progressBarMaxValue = maxValue;
							
							publishProgress(progressInfo);
						}
						
						public void setCurrentPos(int currentPos) {
							ProgressInfo progressInfo = new ProgressInfo();
							
							progressInfo.progressBarValue = currentPos;
							
							publishProgress(progressInfo);
						}

						public void setDescription(String desc) {
						}
		        	}
		        	
					@Override
					protected Void doInBackground(Void... params) {
										
						LoadWithProgress loadWithProgress = new LoadWithProgress();
						
						try {
							dictionaryManager.countForm(loadWithProgress);
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
				    	
				    	performRealSearch(findWord, searchResultList, searchResultArrayAdapter, searchOptionsKanjiCheckbox, 
				    			searchOptionsKanaCheckbox, searchOptionsRomajiCheckbox, searchOptionsTranslateCheckbox, 
				    			searchOptionsInfoCheckbox, searchOptionsGrammaExampleSearchCheckbox, searchAnyPlaceRadioButton, 
				    			searchStartWithRadioButton, wordDictionarySearchElementsNoTextView);
				    }
		        }
		        
		        CountFormAsyncTask countFormAsyncTask = new CountFormAsyncTask();
		        
		        countFormAsyncTask.execute();
		        
			} else {
				
				performRealSearch(findWord, searchResultList, searchResultArrayAdapter, searchOptionsKanjiCheckbox, 
						searchOptionsKanaCheckbox, searchOptionsRomajiCheckbox, searchOptionsTranslateCheckbox, 
						searchOptionsInfoCheckbox, searchOptionsGrammaExampleSearchCheckbox, searchAnyPlaceRadioButton, 
						searchStartWithRadioButton, wordDictionarySearchElementsNoTextView);
			}
		} else {
			performRealSearch(findWord, searchResultList, searchResultArrayAdapter, searchOptionsKanjiCheckbox, 
					searchOptionsKanaCheckbox, searchOptionsRomajiCheckbox, searchOptionsTranslateCheckbox, 
					searchOptionsInfoCheckbox, searchOptionsGrammaExampleSearchCheckbox, searchAnyPlaceRadioButton, 
					searchStartWithRadioButton, wordDictionarySearchElementsNoTextView);			
		}		
	}
	
	private void performRealSearch(final String findWord, final List<WordDictionaryListItem> searchResultList,
			final WordDictionaryListItemAdapter searchResultArrayAdapter,
			final CheckBox searchOptionsKanjiCheckbox,
			final CheckBox searchOptionsKanaCheckbox,
			final CheckBox searchOptionsRomajiCheckbox,
			final CheckBox searchOptionsTranslateCheckbox,
			final CheckBox searchOptionsInfoCheckbox,
			final CheckBox searchOptionsGrammaExampleSearchCheckbox,
			final RadioButton searchAnyPlaceRadioButton,
			final RadioButton searchStartWithRadioButton,
			final TextView wordDictionarySearchElementsNoTextView) {
		
		searchResultList.clear();
						
		final FindWordRequest findWordRequest = new FindWordRequest();
		
		findWordRequest.word = findWord;
		
		findWordRequest.searchKanji = searchOptionsKanjiCheckbox.isChecked();
		findWordRequest.searchKana = searchOptionsKanaCheckbox.isChecked();
		findWordRequest.searchRomaji = searchOptionsRomajiCheckbox.isChecked();
		findWordRequest.searchTranslate = searchOptionsTranslateCheckbox.isChecked();
		findWordRequest.searchInfo = searchOptionsInfoCheckbox.isChecked();
		
		findWordRequest.searchGrammaFormAndExamples = searchOptionsGrammaExampleSearchCheckbox.isChecked();
		
		if (searchAnyPlaceRadioButton.isChecked() == true) {
			findWordRequest.wordPlaceSearch = FindWordRequest.WordPlaceSearch.ANY_PLACE;
		} else if (searchStartWithRadioButton.isChecked() == true) {
			findWordRequest.wordPlaceSearch = FindWordRequest.WordPlaceSearch.START_WITH;
		}
		
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
					
					for (DictionaryEntry currentFoundWord : foundWord.result) {
						
						String currentFoundWordFullTextWithMark = getWordFullTextWithMark(currentFoundWord, findWord, findWordRequest);
																				
						searchResultList.add(new WordDictionaryListItem(currentFoundWord, Html.fromHtml(currentFoundWordFullTextWithMark.replaceAll("\n", "<br/>"))));								
					}

					searchResultArrayAdapter.notifyDataSetChanged();
			        
			        progressDialog.dismiss();
			    }
			    
			    private String getWordFullTextWithMark(DictionaryEntry dictionaryEntry, String findWord, FindWordRequest findWordRequest) {

			    	String kanji = dictionaryEntry.getKanji();
			    	String prefixKana = dictionaryEntry.getPrefixKana();
			    	List<String> kanaList = dictionaryEntry.getKanaList();
			    	String prefixRomaji = dictionaryEntry.getPrefixRomaji();
			    	List<String> romajiList = dictionaryEntry.getRomajiList();
			    	List<String> translates = dictionaryEntry.getTranslates();
			    	String info = dictionaryEntry.getInfo();
			    	
			    	StringBuffer result = new StringBuffer();
			    	
			    	String tempPrefixKana = prefixKana != null && prefixKana.equals("") == false ? prefixKana : null;
			    	String tempPrefixRomaji = prefixRomaji != null && prefixRomaji.equals("") == false ? prefixRomaji : null;

			    	if (dictionaryEntry.isKanjiExists() == true) {

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
			    	
			    	String findWordLowerCase = findWord.toLowerCase();
			    	
					StringBuffer texStringBuffer = new StringBuffer(text);								
					StringBuffer textLowerCaseStringBuffer = new StringBuffer(text.toLowerCase());
													
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
}
