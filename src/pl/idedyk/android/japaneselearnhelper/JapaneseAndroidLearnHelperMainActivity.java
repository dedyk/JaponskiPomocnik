package pl.idedyk.android.japaneselearnhelper;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionary;
import pl.idedyk.android.japaneselearnhelper.info.InfoActivity;
import pl.idedyk.android.japaneselearnhelper.kana.Kana;
import pl.idedyk.android.japaneselearnhelper.kana.KanaTest;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.PopupWindow;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TextView;

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
				
				if (position == 0) { // kana selected
					Intent intent = new Intent(getApplicationContext(), Kana.class);
					
					startActivity(intent);
				} else if (position == 1) { // kana test selected 
					//Intent intent = new Intent(getApplicationContext(), KanaTestOptions.class);
					
					//startActivity(intent);
					
					LayoutInflater inflater = (LayoutInflater) JapaneseAndroidLearnHelperMainActivity.this
							.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

					View layout = inflater.inflate(R.layout.word_kana_test_options,
							(ViewGroup) findViewById(R.id.word_kana_test_options_layout_id));
					
					Display defaultDisplay = getWindowManager().getDefaultDisplay();
					
					
					int width = defaultDisplay.getWidth();
					
					int height = defaultDisplay.getHeight();
					
					Log.d("AAAA", "BBBB: " + width);
					Log.d("AAAA", "BBBB: " + height);
					
					// create a 300px width and 470px height PopupWindow
					PopupWindow pw = new PopupWindow(layout, (int)(width * 0.8f), (int)(height * 0.85f), true);
					
					// display the popup in the center
					pw.showAtLocation(layout, Gravity.CENTER, 0, 0);

					
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
				else if (position == 3) { // suggestion
					String chooseEmailClientTitle = getString(R.string.choose_email_client);
					
					String mailSubject = getString(R.string.main_menu_email_subject);
					
					String mailBody = getString(R.string.main_menu_email_body);				
									
					Intent reportSuggestionIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody); 
					
					startActivity(Intent.createChooser(reportSuggestionIntent, chooseEmailClientTitle));
				} else if (position == 4) { // info
					Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
					
					startActivity(intent);
				}
			}
		});
	}
}