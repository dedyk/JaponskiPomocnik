package pl.idedyk.android.japaneselearnhelper.test;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
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
		
		setContentView(R.layout.word_test_summary);
		
		fillScreen();
	}
	
	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		fillScreen();
	}
	
	private void fillScreen() {
		
		final JapaneseAndroidLearnHelperContext context = JapaneseAndroidLearnHelperApplication.getInstance().getContext();
		final JapaneseAndroidLearnHelperWordTestContext wordTestContext = context.getWordTestContext();
		
		int allAnswersNo = wordTestContext.getWordTestAnswers();
		int correctAnswerNo = wordTestContext.getWordTestCorrectAnswers();
		int incorrectAnswerNo = wordTestContext.getWordTestIncorrentAnswers();
		
		TextView resultValueLabel = (TextView)findViewById(R.id.word_test_summary_result_value_label);
		resultValueLabel.setText("" + ((correctAnswerNo * 100) / allAnswersNo) + " %");
		
		TextView answersNoValueLabel = (TextView)findViewById(R.id.word_test_summary_answers_no_value_label);
		answersNoValueLabel.setText("" + allAnswersNo);
		
		TextView correctAnswersNoValueLabel = (TextView)findViewById(R.id.word_test_summary_correct_answers_no_value_label);
		correctAnswersNoValueLabel.setText("" + correctAnswerNo);
		
		TextView incorrentAnswersNoValueLabel = (TextView)findViewById(R.id.word_test_summary_incorrect_answers_no_value_label);
		incorrentAnswersNoValueLabel.setText("" + incorrectAnswerNo);
	}
}
