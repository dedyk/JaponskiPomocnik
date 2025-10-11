package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.usergroup.UserGroupActivity;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class WordDictionaryTab extends TabActivity {

	private final List<TabHost.TabSpec> tagHostSpecList = new ArrayList<TabHost.TabSpec>();

	private int counter = 1;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);

		menu.add(Menu.NONE, R.id.main_menu_word_dictionary_tab_add_tab, Menu.NONE, R.string.word_dictionary_tab_add_tab);
		menu.add(Menu.NONE, R.id.main_menu_word_dictionary_tab_del_tab, Menu.NONE, R.string.word_dictionary_tab_del_tab);

		MenuShorterHelper.onCreateOptionsMenu(menu);
		
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		int itemId = item.getItemId();

		if (itemId == R.id.main_menu_word_dictionary_tab_add_tab) { // add tab

			addTab(getTabHost(), false);

			return true;
		} else if (itemId == R.id.main_menu_word_dictionary_tab_del_tab) { // del tab

			delCurrentTab(getTabHost());

			return true;
			
		} else {
			return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, android.R.id.tabhost, R.layout.word_dictionary_tab);

		TabHost tabHost = getTabHost();

		addTab(tabHost, true);

		tabHost.setCurrentTab(0);
	}

	private void addTab(TabHost tabHost, boolean addFindExtra) {

		// word dictionary
		Intent wordDictionary = new Intent(getApplicationContext(), WordDictionary.class);

		if (addFindExtra == true) {
			wordDictionary.putExtra("findWordRequest", getIntent().getSerializableExtra("findWordRequest"));
		}

		TabSpec wordDictionaryTabSpec = tabHost.newTabSpec("WordDictionaryTabSpec." + counter)
				.setIndicator(getString(R.string.word_dictionary_tab_tab, counter)).setContent(wordDictionary);

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

		for (TabHost.TabSpec spec : tagHostSpecList) {
			tabHost.addTab(spec);
		}

		if (currentTab >= tagHostSpecList.size()) {
			currentTab = tagHostSpecList.size();
		}

		currentTab = currentTab - 1;

		if (currentTab < 0) {
			currentTab = 0;
		}

		tabHost.setCurrentTab(currentTab);
	}
}
