package pl.idedyk.android.japaneselearnhelper.test;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.WordTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperWordTestContext;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.TextView;

public class WordTestSummary extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.word_test_summary);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_word_test_summary));

		fillScreen();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		fillScreen();
	}
	
	@Override
	protected void onRestoreInstanceState(Bundle savedInstanceState) {
	    super.onRestoreInstanceState(savedInstanceState);
	    
	    fillScreen();
	}
	
	private void fillScreen() {
		
		final JapaneseAndroidLearnHelperContext context = JapaneseAndroidLearnHelperApplication.getInstance().getContext();
		final JapaneseAndroidLearnHelperWordTestContext wordTestContext = context.getWordTestContext();
		
		final WordTestConfig wordTestConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(WordTestSummary.this).getWordTestConfig();
		
		WordTestMode wordTestMode = wordTestConfig.getWordTestMode();

		int allAnswersNo = wordTestContext.getWordTestAnswers();
		int correctAnswerNo = wordTestContext.getWordTestCorrectAnswers();
		int incorrectAnswerNo = wordTestContext.getWordTestIncorrentAnswers();
		
		TextView resultValueLabel = (TextView)findViewById(R.id.word_test_summary_result_value_label);
		TextView answersNoValueLabel = (TextView)findViewById(R.id.word_test_summary_answers_no_value_label);
		TextView correctAnswersNoValueLabel = (TextView)findViewById(R.id.word_test_summary_correct_answers_no_value_label);
		TextView incorrentAnswersNoValueLabel = (TextView)findViewById(R.id.word_test_summary_incorrect_answers_no_value_label);
		
		answersNoValueLabel.setText("" + allAnswersNo);
		
		if (wordTestMode == WordTestMode.INPUT) {
			
			resultValueLabel.setText("" + ((correctAnswerNo * 100) / allAnswersNo) + " %");
			
			correctAnswersNoValueLabel.setText("" + correctAnswerNo);
			incorrentAnswersNoValueLabel.setText("" + incorrectAnswerNo);	
			
		} else if (wordTestMode == WordTestMode.OVERVIEW) {
			
			resultValueLabel.setText("-");
			correctAnswersNoValueLabel.setText("-");
			incorrentAnswersNoValueLabel.setText("-");				
			
		} else {
			throw new RuntimeException("Unknown wordTestMode: " + wordTestConfig.getWordTestMode());
		}
	}
}
