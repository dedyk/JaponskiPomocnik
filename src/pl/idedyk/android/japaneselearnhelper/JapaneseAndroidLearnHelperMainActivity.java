package pl.idedyk.android.japaneselearnhelper;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.test.WordTest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class JapaneseAndroidLearnHelperMainActivity extends Activity {
    /** Called when the activity is first created. */
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
    	
    	List<String> mainMenuListItems = new ArrayList<String>();
    	
    	mainMenuListItems.add(getResources().getString(R.string.main_menu_word_test));
    	mainMenuListItems.add(getResources().getString(R.string.main_menu_kanji_test));
    	
    	ArrayAdapter<String> mainMenuListItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mainMenuListItems);
    	
    	mainMenuListView.setAdapter(mainMenuListItemsAdapter);
    }
	
    private void initMenuActions() {
    	final ListView mainMenuListView = (ListView)findViewById(R.id.mainMenuList);
    	
    	mainMenuListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				JapaneseAndroidLearnHelperContext context = new JapaneseAndroidLearnHelperContext();
				
				// FIXME
				JapaneseAndroidLearnHelperApplication.getInstance().setContext(context);
				
				DictionaryManager dictionaryManager = DictionaryManager.getInstance();
				
				// word test selected
				if (position == 0) {
					
					List<DictionaryEntry> wordsTest = new ArrayList<DictionaryEntry>();
					
					wordsTest.addAll(dictionaryManager.getWordsGroup(50));
					wordsTest.addAll(dictionaryManager.getWordsGroup(51));
					wordsTest.addAll(dictionaryManager.getWordsGroup(52));
					
					context.setWordsTest(wordsTest);
					
					
					Intent intent = new Intent(getApplicationContext(), WordTest.class);	
					
					 // FIXME !!!
					
					//MyObject value = MyApplication.getInstance().getGlobalStateValue();
					//MyApplication.getInstance().setGlobalStateValue(myObjectValue);
					
					

					startActivity(intent);
				}
			}
		});
		
	}
}