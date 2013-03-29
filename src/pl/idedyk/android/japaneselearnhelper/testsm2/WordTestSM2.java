package pl.idedyk.android.japaneselearnhelper.testsm2;

import java.util.Random;

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
		
		menu.add(Menu.NONE, R.id.report_problem_menu_item, Menu.NONE, R.string.report_problem);
		
		MenuShorterHelper.onCreateOptionsMenu(menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		// report problem
		if (item.getItemId() == R.id.report_problem_menu_item) {

			// FIXME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			
			return false;
			
		} else {
			return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
		}
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_test_sm2);
		
		// testy !!!!!!
		
		final DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);
		
		final WordTestSM2Manager wordTestSM2Manager = dictionaryManager.getWordTestSM2Manager();		
		
		//wordTestSM2Manager.getCurrentDateStat();
		
		/*
		for (int idx = 0; idx < 10; ++idx) {

			WordTestSM2WordStat nextNewWordStat = wordTestSM2Manager.getNextNewWordStat(20);
			
			Log.d("AAAAAAAA:", "BBBB: " + nextNewWordStat);
			
			if (nextNewWordStat != null) {			
				nextNewWordStat.processRecallResult(2);
			
				wordTestSM2Manager.updateWordStat(nextNewWordStat);
			}
		}

		for (int idx = 0; idx < 10; ++idx) {

			WordTestSM2WordStat nextRepeatWordStat = wordTestSM2Manager.getNextRepeatWordStat();
			
			Log.d("ZZZZZZZ:", "ZZZZZZZ: " + nextRepeatWordStat);
			
			if (nextRepeatWordStat != null) {			
				nextRepeatWordStat.processRecallResult(3);
			
				wordTestSM2Manager.updateWordStat(nextRepeatWordStat);
			}
		}
		*/
		
		/*
		for (int idx = 0; idx < 30; ++idx) {
			WordTestSM2WordStat nextRepeatWordStat = wordTestSM2Manager.getNextWordStat(20);
			
			Log.d("ZZZZZZZ:", "ZZZZZZZ: " + nextRepeatWordStat);
		}
		*/
		
		
		for (int idx = 0; idx < 20; ++idx) {
			Log.d("ZZZZZZZ:", "ZZZZZZZ: " + wordTestSM2Manager.getNextWordSize(20));
			
			WordTestSM2WordStat nextWordStat = wordTestSM2Manager.getNextWordStat(20);
			
			if (nextWordStat == null) {
				break;
			}
			
			Log.d("ZZZZZZZ:", "ZZZZZZZ: " + nextWordStat);
			
			Random random = new Random();
			
			nextWordStat.processRecallResult(random.nextInt(6));
			
			wordTestSM2Manager.updateWordStat(nextWordStat);
		}
		
	}
}
