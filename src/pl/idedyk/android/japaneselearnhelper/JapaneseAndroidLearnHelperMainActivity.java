package pl.idedyk.android.japaneselearnhelper;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionary;
import pl.idedyk.android.japaneselearnhelper.info.InfoActivity;
import pl.idedyk.android.japaneselearnhelper.kana.Kana;
import pl.idedyk.android.japaneselearnhelper.kana.KanaTestOptions;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiSearch;
import pl.idedyk.android.japaneselearnhelper.kanji.sod.SodActivity;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;

import android.app.Activity;
import android.content.Intent;
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
    			getString(R.string.main_menu_dictionary_kanji),
    			getString(R.string.main_menu_dictionary_text)));
    	
    	/*
    	mainMenuListItems.add(new MainMenuItem(
    			getString(R.string.main_menu_word_test_kanji),
    			getString(R.string.main_menu_word_test_text)));
    	
    	mainMenuListItems.add(new MainMenuItem(
    			getString(R.string.main_menu_kanji_test_kanji),
    			getString(R.string.main_menu_kanji_test_text)));
    	*/

    	mainMenuListItems.add(new MainMenuItem(
    			getString(R.string.main_menu_kanji_kanji),
    			getString(R.string.main_menu_kanji_text)));
    	
    	mainMenuListItems.add(new MainMenuItem(
    			getString(R.string.main_menu_suggestion_kanji),
    			getString(R.string.main_menu_suggestion_text)));
    	
    	mainMenuListItems.add(new MainMenuItem(
    			getString(R.string.main_menu_information_kanji),
    			getString(R.string.main_menu_information_text)));    	

    	mainMenuListItems.add(new MainMenuItem(
    			"TT",
    			"TTTTT2"));    	

    	
    	MainMenuListItemAdapter mainMenuListItemsAdapter = new MainMenuListItemAdapter(this, R.layout.main_menu_simplerow, mainMenuListItems);
    	
    	mainMenuListView.setAdapter(mainMenuListItemsAdapter);
    }
	
    private void initMenuActions() {
    	
    	final ListView mainMenuListView = (ListView)findViewById(R.id.mainMenuList);
    	
    	mainMenuListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if (position == 0) { // kana selected
					Intent intent = new Intent(getApplicationContext(), Kana.class);
					
					startActivity(intent);
				} else if (position == 1) { // kana test selected 
					Intent intent = new Intent(getApplicationContext(), KanaTestOptions.class);
					
					startActivity(intent);
				} else if (position == 2) { // dictionary selected
					Intent intent = new Intent(getApplicationContext(), WordDictionary.class);
					
					startActivity(intent);
				} 
				/*
				else if (position == X) { // word test selected		
					Intent intent = new Intent(getApplicationContext(), WordTestGroup.class);
					
					startActivity(intent);
				}
					*/
				else if (position == 3) { // kanji search
					Intent intent = new Intent(getApplicationContext(), KanjiSearch.class);
					
					startActivity(intent);
				} 
				else if (position == 4) { // suggestion
					String chooseEmailClientTitle = getString(R.string.choose_email_client);
					
					String mailSubject = getString(R.string.main_menu_email_subject);
					
					String mailBody = getString(R.string.main_menu_email_body);				
									
					Intent reportSuggestionIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody); 
					
					startActivity(Intent.createChooser(reportSuggestionIntent, chooseEmailClientTitle));
				} else if (position == 5) { // info
					Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
					
					startActivity(intent);
				} else if (position == 6) { // test
					Intent intent = new Intent(getApplicationContext(), SodActivity.class);
					
					startActivity(intent);					
				}
			}
		});
	}
}