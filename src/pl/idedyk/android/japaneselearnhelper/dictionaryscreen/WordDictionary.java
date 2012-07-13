package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
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
		
		final List<String> searchResultList = new ArrayList<String>();
		final ArrayAdapter<String> searchResultArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchResultList);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
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
					
					class FindWordAsyncTask extends AsyncTask<Void, Void, List<DictionaryEntry>> {
						
						@Override
						protected List<DictionaryEntry> doInBackground(Void... params) {							
							return dictionaryManager.findWord(findWord);
						}
						
					    @Override
					    protected void onPostExecute(List<DictionaryEntry> foundWord) {
					        super.onPostExecute(foundWord);
					        
							wordDictionarySearchElementsNoTextView.setText(resources.getString(R.string.word_dictionary_elements_no, foundWord.size()));

							for (DictionaryEntry currentFoundWord : foundWord) {
								searchResultList.add("FIXME: " + currentFoundWord.getFullKanji()); // FIXME !!!
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
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
