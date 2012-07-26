package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager.FindWordResult;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

public class WordDictionary extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_dictionary);
		
		final DictionaryManager dictionaryManager = DictionaryManager.getInstance();
		
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
		
		EditText searchValueEditText = (EditText)findViewById(R.id.word_dictionary_search_value);
		
		final Resources resources = getResources();
		
		searchValueEditText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				searchResultList.clear();
								
				final String findWord = s.toString();
				
				if (findWord != null && findWord.length() > 0) {
					
					final ProgressDialog progressDialog = ProgressDialog.show(WordDictionary.this, 
							getString(R.string.word_dictionary_searching1),
							getString(R.string.word_dictionary_searching2));
					
					class FindWordAsyncTask extends AsyncTask<Void, Void, FindWordResult> {
						
						@Override
						protected FindWordResult doInBackground(Void... params) {							
							return dictionaryManager.findWord(findWord);
						}
						
					    @Override
					    protected void onPostExecute(FindWordResult foundWord) {
					        super.onPostExecute(foundWord);
					        
							wordDictionarySearchElementsNoTextView.setText(resources.getString(R.string.word_dictionary_elements_no, "" + foundWord.result.size() +
									(foundWord.moreElemetsExists == true ? "+" : "" )));

							String findWordLowerCase = findWord.toLowerCase();
							
							for (DictionaryEntry currentFoundWord : foundWord.result) {
								
								String currentFoundWordFullText = currentFoundWord.getFullText(false);
								
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
					
					wordDictionarySearchElementsNoTextView.setText(resources.getString(R.string.word_dictionary_elements_no, 0));
				}
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			public void afterTextChanged(Editable s) {				
			}
		});	
		
		wordDictionarySearchElementsNoTextView.setText(resources.getString(R.string.word_dictionary_elements_no, 0));
		
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
								
				Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString()); 
				
				startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));
			}
		});
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
