package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiSearchMeaningConfig;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindKanjiRequest;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindKanjiResult;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class KanjiSearchMeaning extends Activity {
	
	private TextView kanjiSearchMeaningElementsNoTextView;
	
	private ListView searchResultListView;
	
	private List<KanjiEntryListItem> searchResultList;
	
	private KanjiEntryListItemAdapter searchResultArrayAdapter;
	
	private EditText searchValueEditText;
	
	private Button searchButton;
	
	private CheckBox seachOptionsEachChangeCheckBox;
	
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
		
		setContentView(R.layout.kanji_search_meaning);
		
		kanjiSearchMeaningElementsNoTextView = (TextView)findViewById(R.id.kanji_search_meaning_elements_no);
		
		searchResultListView = (ListView)findViewById(R.id.kanji_search_meaning_result_list);
		
		searchResultList = new ArrayList<KanjiEntryListItem>();
		searchResultArrayAdapter = new KanjiEntryListItemAdapter(this, R.layout.kanji_entry_simplerow, searchResultList);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
								
				KanjiEntryListItem kanjiEntryListItem = searchResultArrayAdapter.getItem(position);
				
				Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);
				
				intent.putExtra("item", kanjiEntryListItem.getKanjiEntry());
				
				startActivity(intent);
			}
		});
		
		searchValueEditText = (EditText)findViewById(R.id.kanji_search_meaning_search_value);		
		
		searchButton = (Button)findViewById(R.id.kanji_search_meaning_search_button);
		
		searchButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				performSearch(searchValueEditText.getText().toString());
			}
		});
		
		seachOptionsEachChangeCheckBox = (CheckBox)findViewById(R.id.kanji_search_meaning_options_search_each_change_checkbox);
		
		KanjiSearchMeaningConfig kanjiSearchMeaningConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getKanjiSearchMeaningConfig();
		
		seachOptionsEachChangeCheckBox.setChecked(kanjiSearchMeaningConfig.getEachChangeSearch());
		
		seachOptionsEachChangeCheckBox.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				
				setSearchButtonVisible();
			}
		});
				
		searchOptionsAnyPlaceRadioButton = (RadioButton)findViewById(R.id.kanji_search_meaning_options_search_any_place_radiobutton);
		searchOptionsStartWithPlaceRadioButton = (RadioButton)findViewById(R.id.kanji_search_meaning_options_search_startwith_radiobutton);
		searchOptionsExactPlaceRadioButton = (RadioButton)findViewById(R.id.kanji_search_meaning_options_search_exact_radiobutton);
		
		OnClickListener searchOptionsOnClick = new OnClickListener() {			
			public void onClick(View view) {
				
				if (seachOptionsEachChangeCheckBox.isChecked() == true) {
					performSearch(searchValueEditText.getText().toString());
				}
			}
		};
				
		searchOptionsAnyPlaceRadioButton.setOnClickListener(searchOptionsOnClick);
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
				
		kanjiSearchMeaningElementsNoTextView.setText(getString(R.string.kanji_search_meaning_elements_no, 0));
		
		Button reportProblemButton = (Button)findViewById(R.id.kanji_search_meaning_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
								
				KanjiEntryListItemAdapter searchResultListViewAdapter = (KanjiEntryListItemAdapter)searchResultListView.getAdapter();				
				
				StringBuffer searchListText = new StringBuffer();
				
				for (int searchResultListViewAdapterIdx = 0; searchResultListViewAdapterIdx < searchResultListViewAdapter.size(); ++searchResultListViewAdapterIdx) {
					searchListText.append(searchResultListViewAdapter.getItem(searchResultListViewAdapterIdx).getText().toString()).append("\n--\n");
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
		
		if (seachOptionsEachChangeCheckBox.isChecked() == false) {
			searchButton.setVisibility(View.VISIBLE);
		} else {
			searchButton.setVisibility(View.GONE);
			
			performSearch(searchValueEditText.getText().toString());
		}
		
		KanjiSearchMeaningConfig kanjiSearchMeaningConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getKanjiSearchMeaningConfig();
		
		kanjiSearchMeaningConfig.setEachChangeSearch(seachOptionsEachChangeCheckBox.isChecked());
	}
	
	private void performSearch(final String findWord) {
			
		searchResultList.clear();
						
		final FindKanjiRequest findKanjiRequest = new FindKanjiRequest();
		
		findKanjiRequest.word = findWord;
				
		if (searchOptionsAnyPlaceRadioButton.isChecked() == true) {
			findKanjiRequest.wordPlaceSearch = FindKanjiRequest.WordPlaceSearch.ANY_PLACE;
		} else if (searchOptionsStartWithPlaceRadioButton.isChecked() == true) {
			findKanjiRequest.wordPlaceSearch = FindKanjiRequest.WordPlaceSearch.START_WITH;
		} else if (searchOptionsExactPlaceRadioButton.isChecked() == true) {
			findKanjiRequest.wordPlaceSearch = FindKanjiRequest.WordPlaceSearch.EXACT;
		}
				
		if (findWord != null && findWord.length() > 0) {
			
			final ProgressDialog progressDialog = ProgressDialog.show(KanjiSearchMeaning.this, 
					getString(R.string.kanji_search_meaning_searching1),
					getString(R.string.kanji_search_meaning_searching2));
			
			class FindWordAsyncTask extends AsyncTask<Void, Void, FindKanjiResult> {
				
				@Override
				protected FindKanjiResult doInBackground(Void... params) {
					
					final DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(KanjiSearchMeaning.this);
					
					return dictionaryManager.findKanji(findKanjiRequest);
				}
				
			    @Override
			    protected void onPostExecute(FindKanjiResult findKanjiResult) {
			    	
			        super.onPostExecute(findKanjiResult);
			        
			        kanjiSearchMeaningElementsNoTextView.setText(getString(R.string.kanji_entry_elements_no, "" + findKanjiResult.result.size() +
							(findKanjiResult.moreElemetsExists == true ? "+" : "" )));
					
					for (KanjiEntry currentKanjiEntry : findKanjiResult.result) {
						
						String currentFoundKanjiFullTextWithMarks = getKanjiFullTextWithMark(currentKanjiEntry, findWord);
																				
						searchResultList.add(new KanjiEntryListItem(currentKanjiEntry, Html.fromHtml(currentFoundKanjiFullTextWithMarks.replaceAll("\n", "<br/>"))));								
					}

					searchResultArrayAdapter.notifyDataSetChanged();
			        
			        progressDialog.dismiss();
			    }
			    
			    private String getKanjiFullTextWithMark(KanjiEntry kanjiEntry, String findWord) {

			    	
			    	String kanji = kanjiEntry.getKanji();
			    	List<String> polishTranslates = kanjiEntry.getPolishTranslates();
			    	
			    	KanjiDic2Entry kanjiDic2Entry = kanjiEntry.getKanjiDic2Entry();
			    	
			    	List<String> radicals = null;
			    	
			    	if (kanjiDic2Entry != null) {
			    		radicals = kanjiDic2Entry.getRadicals();
			    	}
			    	
			    	String info = kanjiEntry.getInfo();
			    	
			    	StringBuffer result = new StringBuffer();
			    	
					result.append("<big>").append(getStringWithMark(kanji, findWord)).append("</big> - ");
					result.append(getStringWithMark(toString(polishTranslates, null), findWord));
					
					if (info != null && info.equals("") == false) {
						
						result.append("\n");
						
						result.append(getStringWithMark(info, findWord));
					}
					
					result.append("\n\n");
					
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
}