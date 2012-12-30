package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class WordDictionaryTab extends TabActivity {
	
	private List<TabHost.TabSpec> tagHostSpecList = new ArrayList<TabHost.TabSpec>();  
	
	private int counter = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, R.id.main_menu_word_dictionary_tab_add_tab, Menu.NONE, R.string.word_dictionary_tab_add_tab);
		menu.add(Menu.NONE, R.id.main_menu_word_dictionary_tab_del_tab, Menu.NONE, R.string.word_dictionary_tab_del_tab);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		int itemId = item.getItemId();

		if (itemId == R.id.main_menu_word_dictionary_tab_add_tab) { // add tab

			addTab(getTabHost());

			return true;
		} else if (itemId == R.id.main_menu_word_dictionary_tab_del_tab) { // del tab
			
			delCurrentTab(getTabHost());	
			
			return true;
		}

		return false;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.word_dictionary_tab);

		TabHost tabHost = getTabHost();
		
		addTab(tabHost);

		tabHost.setCurrentTab(0);
	}

	private void addTab(TabHost tabHost) {

		// word dictionary
		Intent wordDictionary = new Intent(getApplicationContext(), WordDictionary.class);

		TabSpec wordDictionaryTabSpec = tabHost.newTabSpec("WordDictionaryTabSpec." + counter)
				.setIndicator(getString(R.string.word_dictionary_tab_tab, counter))
				.setContent(wordDictionary);

		tabHost.addTab(wordDictionaryTabSpec);	
		tagHostSpecList.add(wordDictionaryTabSpec);
		
		tabHost.setCurrentTab(tagHostSpecList.size() - 1);
		
		counter++;
	}
	
	private void delCurrentTab(TabHost tabHost) {
		
		int currentTab = tabHost.getCurrentTab();
		
		tagHostSpecList.remove(currentTab);
		
		if (tagHostSpecList.size() == 0) {
			finish();
			
			return;
		}
		
		tabHost.clearAllTabs();  
		
		for(TabHost.TabSpec spec : tagHostSpecList) {  
			tabHost.addTab(spec); 
		}
		
		if (currentTab >= tagHostSpecList.size()) { 
			currentTab = tagHostSpecList.size();
		}
		
		tabHost.setCurrentTab(currentTab - 1);
	}
}
