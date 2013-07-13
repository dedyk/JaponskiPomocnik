package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiSearchMeaningConfig;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.WordDictionarySearchConfig;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordRequest;
import pl.idedyk.android.japaneselearnhelper.dictionary.FindWordRequest.WordPlaceSearch;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryDetails;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryListItem;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryListItemAdapter;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
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
		
		int fixme2 = 1;
		// FIXME: wyszukac word dictionary
		
		setContentView(R.layout.kanji_search_meaning);
		
		//////////
		kanjiSearchMeaningElementsNoTextView = (TextView)findViewById(R.id.kanji_search_meaning_elements_no);
		
		searchResultListView = (ListView)findViewById(R.id.kanji_search_meaning_result_list);
		
		searchResultList = new ArrayList<KanjiEntryListItem>();
		searchResultArrayAdapter = new KanjiEntryListItemAdapter(this, R.layout.kanji_entry_simplerow, searchResultList);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				int fixme = 1;
				
				/*
				
				WordDictionaryListItem wordDictionaryListItem = searchResultArrayAdapter.getItem(position);
				
				Intent intent = new Intent(getApplicationContext(), WordDictionaryDetails.class);
				
				intent.putExtra("item", wordDictionaryListItem.getDictionaryEntry());
				
				startActivity(intent);
				*/
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
				
				int fixme = 1;
				
				/*
				
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
				*/
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
		
		///////////
		
		//Button reportProblemButton = (Button)findViewById(R.id.kanji_search_meaning_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				int fixme = 1;
				
				/*
				
				StringBuffer detailsSb = new StringBuffer();
				
				int fixme = 1;
				
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kanji_search_meaning_report_problem_email_subject);
				
				String mailBody = getString(R.string.kanji_search_report_problem_email_body,
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
				*/
			}
		});
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
		
		int fixme = 1;
		
	}
}
