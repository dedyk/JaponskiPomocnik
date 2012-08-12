package pl.idedyk.android.japaneselearnhelper.kana;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class KanaTestSummary extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.kana_test_summary);
		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.kana_test_summary_main_layout);
		
		List<IScreenItem> screenItems = generateScreen();
		
		fillMainLayout(screenItems, mainLayout);
	}
	
	private void fillMainLayout(List<IScreenItem> screenItems, LinearLayout mainLayout) {
		
		for (IScreenItem currentScreenItem : screenItems) {
			currentScreenItem.generate(this, getResources(), mainLayout);			
		}
	}
	
	private List<IScreenItem> generateScreen() {
		
		final JapaneseAndroidLearnHelperKanaTestContext kanaTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanaTestContext();
		
		final List<IScreenItem> result = new ArrayList<IScreenItem>();
		
		int allAnswersNo = kanaTestContext.getAllKanaEntries().size();
		int correctAnswerNo = kanaTestContext.getCorrectAnswers();
		int incorrectAnswerNo = kanaTestContext.getIncorrectAnswers();
		
		result.add(new TitleItem(getString(R.string.kana_test_result), 0));
		
		StringValue resultStringValue = new StringValue("" + ((correctAnswerNo * 100) / allAnswersNo) + " %", 16.0f, 1);
		result.add(resultStringValue);
		
		result.add(new TitleItem(getString(R.string.kana_test_answers_no), 0));
		
		StringValue allAnswersNoStringValue = new StringValue("" + allAnswersNo, 16.0f, 1);
		result.add(allAnswersNoStringValue);
		
		result.add(new TitleItem(getString(R.string.kana_test_correct_answers_no), 0));
		
		StringValue allCorrectAnswersNoStringValue = new StringValue("" + correctAnswerNo, 16.0f, 1);
		result.add(allCorrectAnswersNoStringValue);
		
		result.add(new TitleItem(getString(R.string.kana_test_incorrect_answers_no), 0));
		
		StringValue allIncorrectAnswersNoStringValue = new StringValue("" + incorrectAnswerNo, 16.0f, 1);
		result.add(allIncorrectAnswersNoStringValue);
		
		return result;
	}
}
