package pl.idedyk.android.japaneselearnhelper.testsm2;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.WordTestSM2Manager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.WordTestSM2WordStat;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class WordTestSM2 extends Activity {

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuShorterHelper.onCreateOptionsMenu(menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_test_sm2);
		
		// testy !!!!!!
		
		DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);
		
		WordTestSM2Manager wordTestSM2Manager = dictionaryManager.getWordTestSM2Manager();
		
		//wordTestSM2Manager.getCurrentDateStat();
		
		WordTestSM2WordStat nextNewWordStat = wordTestSM2Manager.getNextNewWordStat(20);
		
		Log.d("AAAAAAAA:", "BBBB: " + nextNewWordStat);
		

	}
}
