package pl.idedyk.android.japaneselearnhelper;

import pl.idedyk.android.japaneselearnhelper.counters.CountersActivity;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionary;
import pl.idedyk.android.japaneselearnhelper.kana.Kana;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiSearch;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiRecognizeActivity;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;

public class MenuShorterHelper {

	public static void onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, R.id.main_menu_kana_text_menu_item, Menu.NONE, R.string.main_menu_kana_text);
		menu.add(Menu.NONE, R.id.main_menu_counters_text_menu_item, Menu.NONE, R.string.main_menu_counters_text);
		menu.add(Menu.NONE, R.id.main_menu_dictionary_text_menu_item, Menu.NONE, R.string.main_menu_dictionary_text);
		menu.add(Menu.NONE, R.id.main_menu_kanji_text_menu_item, Menu.NONE, R.string.main_menu_kanji_text);
		menu.add(Menu.NONE, R.id.main_menu_kanji_recognizer_text_menu_item, Menu.NONE, R.string.main_menu_kanji_recognizer_text);		
	}	
	
	public static boolean onOptionsItemSelected(MenuItem item, Context applicationContext, Activity activity) {
		
		int itemId = item.getItemId();
		
		if (itemId == R.id.main_menu_kana_text_menu_item) { // kana table
			
			Intent intent = new Intent(applicationContext, Kana.class);

			activity.startActivity(intent);
			
			return true;
		} else if (itemId == R.id.main_menu_counters_text_menu_item) { // counter table
			
			Intent intent = new Intent(applicationContext, CountersActivity.class);

			activity.startActivity(intent);
			
			return true;
		} else if (itemId == R.id.main_menu_dictionary_text_menu_item) { // dictionary
			
			Intent intent = new Intent(applicationContext, WordDictionary.class);

			activity.startActivity(intent);
			
			return true;
		} else if (itemId == R.id.main_menu_kanji_text_menu_item) { // kanji search
			
			Intent intent = new Intent(applicationContext, KanjiSearch.class);

			activity.startActivity(intent);
			
			return true;
		} else if (itemId == R.id.main_menu_kanji_recognizer_text_menu_item) { // kanji recognize
			
			Intent intent = new Intent(applicationContext, KanjiRecognizeActivity.class);

			activity.startActivity(intent);	
			
			return true;
		}
		
		return false;
	}
}