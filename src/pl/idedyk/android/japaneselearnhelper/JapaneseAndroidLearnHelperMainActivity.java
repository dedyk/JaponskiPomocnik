package pl.idedyk.android.japaneselearnhelper;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionary;
import pl.idedyk.android.japaneselearnhelper.test.WordTestGroup;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
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
    	
    	List<String> mainMenuListItems = new ArrayList<String>();
    	
    	mainMenuListItems.add(getResources().getString(R.string.main_menu_dictionary));
    	
    	mainMenuListItems.add(getResources().getString(R.string.main_menu_word_test));
    	mainMenuListItems.add(getResources().getString(R.string.main_menu_kanji_test));
    	
    	ArrayAdapter<String> mainMenuListItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, mainMenuListItems);
    	
    	mainMenuListView.setAdapter(mainMenuListItemsAdapter);
    }
	
    private void initMenuActions() {
		JapaneseAndroidLearnHelperContext context = new JapaneseAndroidLearnHelperContext();
		
		// FIXME
		JapaneseAndroidLearnHelperApplication.getInstance().setContext(context);
    	
    	final ListView mainMenuListView = (ListView)findViewById(R.id.mainMenuList);
    	
    	mainMenuListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				if (position == 0) { // dictionary selected
					Intent intent = new Intent(getApplicationContext(), WordDictionary.class);
					
					startActivity(intent);					
				} else if (position == 1) { // word test selected					
					Intent intent = new Intent(getApplicationContext(), WordTestGroup.class);
					
					startActivity(intent);
				}
			}
		});
	}
}