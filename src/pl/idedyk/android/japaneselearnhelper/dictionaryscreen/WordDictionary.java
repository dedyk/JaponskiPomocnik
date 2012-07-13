package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

public class WordDictionary extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_dictionary);
		
		final DictionaryManager dictionaryManager = DictionaryManager.getInstance();
		
		ListView searchResultListView = (ListView)findViewById(R.id.word_dictionary_search_result_list);
		
		final List<String> searchResultList = new ArrayList<String>();
		final ArrayAdapter<String> searchResultArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchResultList);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
		EditText searchValueEditText = (EditText)findViewById(R.id.word_dictionary_search_value);
		
		searchValueEditText.addTextChangedListener(new TextWatcher() {
			
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				
				List<DictionaryEntry> foundWord = dictionaryManager.findWord(s.toString());
				
				searchResultList.clear();
				
				for (DictionaryEntry currentFoundWord : foundWord) {
					searchResultList.add("FIXME: " + currentFoundWord.getFullKanji()); // FIXME !!!
				}
				
				searchResultArrayAdapter.notifyDataSetChanged();				
			}
			
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {
			}
			
			public void afterTextChanged(Editable s) {				
			}
		});		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
