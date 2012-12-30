package pl.idedyk.android.japaneselearnhelper;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.counters.CountersActivity;
import pl.idedyk.android.japaneselearnhelper.dictionaryhear.DictionaryHearOptions;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionary;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryTab;
import pl.idedyk.android.japaneselearnhelper.info.InfoActivity;
import pl.idedyk.android.japaneselearnhelper.kana.Kana;
import pl.idedyk.android.japaneselearnhelper.kana.KanaTestOptions;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiSearch;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiRecognizeActivity;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiTestOptionsActivity;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.test.WordTestOptions;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class JapaneseAndroidLearnHelperMainActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.main);

		// create menu
		createMenuList();

		// init menu actions
		initMenuActions();
	}

	private void createMenuList() {
		ListView mainMenuListView = (ListView)findViewById(R.id.mainMenuList);

		List<MainMenuItem> mainMenuListItems = new ArrayList<MainMenuItem>();

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_kana_kanji),
				getString(R.string.main_menu_kana_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_kana_test_kanji),
				getString(R.string.main_menu_kana_test_text)));    	

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_counters_kanji),
				getString(R.string.main_menu_counters_text)));    	

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_dictionary_kanji),
				getString(R.string.main_menu_dictionary_text)));

		if (android.os.Build.VERSION.SDK_INT >= 14) { // if Android 4+
			mainMenuListItems.add(new MainMenuItem(
					getString(R.string.main_menu_dictionary_hear_kanji),
					getString(R.string.main_menu_dictionary_hear_text)));
		}
		
    	mainMenuListItems.add(new MainMenuItem(
    			getString(R.string.main_menu_word_test_kanji),
    			getString(R.string.main_menu_word_test_text)));
    	
    	/*
    	mainMenuListItems.add(new MainMenuItem(
    			getString(R.string.main_menu_kanji_test_kanji),
    			getString(R.string.main_menu_kanji_test_text)));
		 */

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_kanji_kanji),
				getString(R.string.main_menu_kanji_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_kanji_recognizer_kanji),
				getString(R.string.main_menu_kanji_recognizer_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_kanji_test_kanji),
				getString(R.string.main_menu_kanji_test_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_suggestion_kanji),
				getString(R.string.main_menu_suggestion_text)));

		mainMenuListItems.add(new MainMenuItem(
				getString(R.string.main_menu_information_kanji),
				getString(R.string.main_menu_information_text)));

		MainMenuListItemAdapter mainMenuListItemsAdapter = new MainMenuListItemAdapter(this, R.layout.main_menu_simplerow, mainMenuListItems);

		mainMenuListView.setAdapter(mainMenuListItemsAdapter);
	}

	private void initMenuActions() {

		final ListView mainMenuListView = (ListView)findViewById(R.id.mainMenuList);

		mainMenuListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				MainMenuItem mainMenuChosenItem = (MainMenuItem)mainMenuListView.getAdapter().getItem(position);
				
				String mainMenuChosenItemText = mainMenuChosenItem.getText();
				
				if (mainMenuChosenItemText.equals(getString(R.string.main_menu_kana_text)) == true) { // kana selected
					
					Intent intent = new Intent(getApplicationContext(), Kana.class);

					startActivity(intent);
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_kana_test_text)) == true) { // kana test selected
					
					Intent intent = new Intent(getApplicationContext(), KanaTestOptions.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_counters_text)) == true) { // counter
					
					Intent intent = new Intent(getApplicationContext(), CountersActivity.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_dictionary_text)) == true) { // dictionary selected
					
					Intent intent = new Intent(getApplicationContext(), WordDictionaryTab.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_dictionary_hear_text)) == true) { // dictionary hear

					Intent intent = new Intent(getApplicationContext(), DictionaryHearOptions.class);

					startActivity(intent);					
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_word_test_text)) == true) { // word test selected
					
					Intent intent = new Intent(getApplicationContext(), WordTestOptions.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_kanji_text)) == true) { // kanji search
					
					Intent intent = new Intent(getApplicationContext(), KanjiSearch.class);

					startActivity(intent);
					
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_kanji_recognizer_text)) == true) { // kanji recognizer

					Intent intent = new Intent(getApplicationContext(), KanjiRecognizeActivity.class);

					startActivity(intent);					

				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_kanji_test_text)) == true) { // kanji test

					Intent intent = new Intent(getApplicationContext(), KanjiTestOptionsActivity.class);

					startActivity(intent);					

				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_suggestion_text)) == true) { // suggestion
					String chooseEmailClientTitle = getString(R.string.choose_email_client);

					String mailSubject = getString(R.string.main_menu_email_subject);

					String mailBody = getString(R.string.main_menu_email_body);

					String versionName = "";
					int versionCode = 0;

					try {
						PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);

						versionName = packageInfo.versionName;
						versionCode = packageInfo.versionCode;

					} catch (NameNotFoundException e) {        	
					}

					Intent reportSuggestionIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody, versionName, versionCode); 

					startActivity(Intent.createChooser(reportSuggestionIntent, chooseEmailClientTitle));
				} else if (mainMenuChosenItemText.equals(getString(R.string.main_menu_information_text)) == true) { // info
					
					Intent intent = new Intent(getApplicationContext(), InfoActivity.class);

					startActivity(intent);
					
				}
			}
		});
	}
}