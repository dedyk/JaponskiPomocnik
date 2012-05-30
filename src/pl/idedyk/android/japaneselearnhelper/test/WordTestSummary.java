package pl.idedyk.android.japaneselearnhelper.test;

import pl.idedyk.android.japaneselearnhelper.R;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;

public class WordTestSummary extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_test_summary);
		
		fillScreen();
		
		
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		fillScreen();
	}
	
	private void fillScreen() {
		
		
	}
}
