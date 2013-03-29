package pl.idedyk.android.japaneselearnhelper.testsm2;

import java.util.Random;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.WordTestSM2Manager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.WordTestSM2WordStat;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;

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
	public void onBackPressed() {
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	
		        	finish();
		        	
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		        	
		        	// noop
		        	
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage(getString(R.string.word_test_sm2_quit_question)).setPositiveButton(getString(R.string.word_test_sm2_quit_question_yes), dialogClickListener)
		    .setNegativeButton(getString(R.string.word_test_sm2_quit_question_no), dialogClickListener).show();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_test_sm2);
		
		fillScreen();
		
		Button nextButton = (Button)findViewById(R.id.word_test_sm2_next_button);
		
		nextButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				//checkUserAnswer();
				
				int fixme = 1;
			}
		});

		
		// testy !!!!!!
		
		// final DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);
		
		// final WordTestSM2Manager wordTestSM2Manager = dictionaryManager.getWordTestSM2Manager();		
		
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
		
		/*
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
		*/		
	}

	private void fillScreen() {
		
		
	}
}
