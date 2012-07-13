package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Dictionary extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		/*
		LinearLayout linearLayout = new LinearLayout(this);
		
		linearLayout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		linearLayout.setOrientation(LinearLayout.HORIZONTAL);
				
		TextView searchTextView = new TextView(this);
		
		LinearLayout.LayoutParams searchTextViewLayoutParams = new LinearLayout.LayoutParams(MarginLayoutParams.WRAP_CONTENT, MarginLayoutParams.WRAP_CONTENT);
		searchTextViewLayoutParams.setMargins(0, 0, 10, 00);
		searchTextView.setLayoutParams(searchTextViewLayoutParams);
		
		searchTextView.setText(getString(R.string.word_dictionary_search_label));
		
		linearLayout.addView(searchTextView);
		
		EditText searchEditText = new EditText(this);
		searchEditText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		
		linearLayout.addView(searchEditText);
		
		ListView searchResultListView = new ListView(this);
		searchResultListView.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		
		List<String> searchResultList = new ArrayList<String>();
		searchResultList.add("AAAA");
		searchResultList.add("AAAA222");
		
		ArrayAdapter<String> searchResultArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, searchResultList);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
		linearLayout.addView(searchResultListView);
		*/
		
		setContentView(linearLayout);
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
