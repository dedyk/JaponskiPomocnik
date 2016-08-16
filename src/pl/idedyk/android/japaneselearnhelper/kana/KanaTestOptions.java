package pl.idedyk.android.japaneselearnhelper.kana;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanaTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.RangeTest;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.TestMode2;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.CheckBox;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.RadioGroup;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class KanaTestOptions extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_kana_test_options));
		
		setContentView(R.layout.kana_test_options);
		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.kana_test_options_main_layout);
		
		final List<IScreenItem> screenItems = generateOptionsScreen();
		
		fillMainLayout(screenItems, mainLayout);
	}

	private void fillMainLayout(List<IScreenItem> screenItems, LinearLayout mainLayout) {
		
		for (IScreenItem currentScreenItem : screenItems) {
			currentScreenItem.generate(this, getResources(), mainLayout);			
		}
	}
	
	private List<IScreenItem> generateOptionsScreen() {
		
		final JapaneseAndroidLearnHelperKanaTestContext kanaTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanaTestContext();
		
		final KanaTestConfig kanaTestConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(this).getKanaTestConfig();
		
		final List<IScreenItem> result = new ArrayList<IScreenItem>();
		
		result.add(new TitleItem(getString(R.string.kana_test_mode2), 0));
		
		final RadioGroup testMode2RadioGroup = new RadioGroup(this);
		
		TestMode2 testMode2 = kanaTestConfig.getTestMode2();
		
		testMode2RadioGroup.addRadioButton(this, getString(R.string.kana_test_mode2_kana_to_romaji), R.id.kana_test_mode2_kana_to_romaji_id, (testMode2 == null ? true : (testMode2 == TestMode2.KANA_TO_ROMAJI ? true : false)));
		
		testMode2RadioGroup.addRadioButton(this, getString(R.string.kana_test_mode2_romaji_to_kana), R.id.kana_test_mode2_romaji_to_kana_id, (testMode2 == TestMode2.ROMAJI_TO_KANA ? true : false));
		
		result.add(testMode2RadioGroup);
		//result.add(new StringValue(getString(R.string.kana_test_mode2_romaji_to_kana_info), 12.0f, 2));
		
		result.add(new TitleItem(getString(R.string.kana_test_range), 0));
		
		final RadioGroup rangeTestRadioGroup = new RadioGroup(this);
		
		RangeTest rangeTest = kanaTestConfig.getRangeTest();
		
		rangeTestRadioGroup.addRadioButton(this, getString(R.string.kana_test_range_hiragana), R.id.kana_test_range_hiragana_id, (rangeTest == RangeTest.HIRAGANA ? true : false));
		rangeTestRadioGroup.addRadioButton(this, getString(R.string.kana_test_range_katakana), R.id.kana_test_range_katakana_id, (rangeTest == RangeTest.KATAKANA ? true : false));
		rangeTestRadioGroup.addRadioButton(this, getString(R.string.kana_test_range_hiragana_katakana), R.id.kana_test_range_hiragana_katakana_id, (rangeTest == null ? true : (rangeTest == RangeTest.HIRAGANA_KATAKANA ? true : false)));
		
		result.add(rangeTestRadioGroup);
		
		/*
		result.add(new TitleItem(getString(R.string.kana_test_mode1), 0));
		
		final RadioGroup testMode1RadioGroup = new RadioGroup(this);
		
		TestMode1 testMode1 = kanaTestContext.getTestMode1();
		
		testMode1RadioGroup.addRadioButton(this, getString(R.string.kana_test_mode1_choose), R.id.kana_test_mode1_choose_id, (testMode1 == null ? true : (testMode1 == TestMode1.CHOOSE ? true : false)));
		testMode1RadioGroup.addRadioButton(this, getString(R.string.kana_test_mode1_input), R.id.kana_test_mode1_input_id, (testMode1 == TestMode1.INPUT ? true : false));
		
		result.add(testMode1RadioGroup);
		*/
		
		result.add(new TitleItem(getString(R.string.kana_test_char_range), 0));
		
		Boolean gojuuon = kanaTestConfig.getGojuuon();
		
		final CheckBox gojuuonCheckBox = new CheckBox(this, getString(R.string.kana_test_char_range_gojuuon), (gojuuon == null ? true : gojuuon.booleanValue()), R.id.kana_test_char_range_gojuuon);
		result.add(gojuuonCheckBox);
		
		Boolean dakutenHandakuten = kanaTestConfig.getDakutenHandakuten();
		
		final CheckBox dakutenHandakutenCheckBox = new CheckBox(this, getString(R.string.kana_test_char_range_dakuten_handakuten), (dakutenHandakuten == null ? true : dakutenHandakuten.booleanValue()), R.id.kana_test_char_range_dakuten_handakuten);
		result.add(dakutenHandakutenCheckBox);
		
		Boolean youon = kanaTestConfig.getYouon();
		
		final CheckBox youonCheckBox = new CheckBox(this, getString(R.string.kana_test_char_range_youon), (youon == null ? true : youon.booleanValue()), R.id.kana_test_char_range_youon);
		result.add(youonCheckBox);
				
		result.add(new TitleItem(getString(R.string.kana_test_other), 0));
		
		Boolean untilSuccess = kanaTestConfig.getUntilSuccess();
		
		final CheckBox untilSuccessCheckBox = new CheckBox(this, getString(R.string.kana_test_until_success), (untilSuccess == null ? true : untilSuccess.booleanValue()), R.id.kana_test_until_success_id);
		
		result.add(untilSuccessCheckBox);
		
		Boolean untilSuccessNewWordLimit = kanaTestConfig.getUntilSuccessNewWordLimit();

		final CheckBox untilSuccessNewWordLimitCheckBox = new CheckBox(this, getString(R.string.kana_test_until_success_new_word_limit), (untilSuccessNewWordLimit == null ? true : untilSuccessNewWordLimit.booleanValue()), R.id.kana_test_until_success_new_word_limit_id);
		
		result.add(untilSuccessNewWordLimitCheckBox);
		
		setUntilSuccessNewWordLimitCheckBoxEnabled(untilSuccessCheckBox, untilSuccessNewWordLimitCheckBox);
		
		// actions
		untilSuccessCheckBox. setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				setUntilSuccessNewWordLimitCheckBoxEnabled(untilSuccessCheckBox, untilSuccessNewWordLimitCheckBox);				
			}
		});
		
		pl.idedyk.android.japaneselearnhelper.screen.Button startButton = new pl.idedyk.android.japaneselearnhelper.screen.Button(
				getString(R.string.kana_test_options_startTest));
		
		startButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				kanaTestContext.resetTest();
				
				int rangeTestRadioGroupCheckedRadioButtonId = rangeTestRadioGroup.getCheckedRadioButtonId();
				
				if (rangeTestRadioGroupCheckedRadioButtonId == R.id.kana_test_range_hiragana_id) {
					kanaTestConfig.setRangeTest(JapaneseAndroidLearnHelperKanaTestContext.RangeTest.HIRAGANA);
				} else if (rangeTestRadioGroupCheckedRadioButtonId == R.id.kana_test_range_katakana_id) {
					kanaTestConfig.setRangeTest(JapaneseAndroidLearnHelperKanaTestContext.RangeTest.KATAKANA);
				} else if (rangeTestRadioGroupCheckedRadioButtonId == R.id.kana_test_range_hiragana_katakana_id) {
					kanaTestConfig.setRangeTest(JapaneseAndroidLearnHelperKanaTestContext.RangeTest.HIRAGANA_KATAKANA);
				} else {
					throw new RuntimeException("rangeTestRadioGroupCheckedRadioButtonId: " + rangeTestRadioGroupCheckedRadioButtonId);
				}
				
				/*
				int testMode1RadioGroupCheckedRadioButtonId = testMode1RadioGroup.getCheckedRadioButtonId();
				
				if (testMode1RadioGroupCheckedRadioButtonId == R.id.kana_test_mode1_choose_id) {
					kanaTestContext.setTestMode1(JapaneseAndroidLearnHelperKanaTestContext.TestMode1.CHOOSE);
				} else if (testMode1RadioGroupCheckedRadioButtonId == R.id.kana_test_mode1_input_id) {
					kanaTestContext.setTestMode1(JapaneseAndroidLearnHelperKanaTestContext.TestMode1.INPUT);
				} else {
					throw new RuntimeException("testMode1RadioGroupCheckedRadioButtonId: " + testMode1RadioGroupCheckedRadioButtonId);
				}
				*/
				
				kanaTestConfig.setTestMode1(JapaneseAndroidLearnHelperKanaTestContext.TestMode1.CHOOSE);
				
				int testMode2RadioGroupCheckedRadioButtonId = testMode2RadioGroup.getCheckedRadioButtonId();
				
				if (testMode2RadioGroupCheckedRadioButtonId == R.id.kana_test_mode2_kana_to_romaji_id) {
					kanaTestConfig.setTestMode2(JapaneseAndroidLearnHelperKanaTestContext.TestMode2.KANA_TO_ROMAJI);
				} else if (testMode2RadioGroupCheckedRadioButtonId == R.id.kana_test_mode2_romaji_to_kana_id) {
					kanaTestConfig.setTestMode2(JapaneseAndroidLearnHelperKanaTestContext.TestMode2.ROMAJI_TO_KANA);
				} else {
					throw new RuntimeException("testMode2RadioGroupCheckedRadioButtonId: " + testMode2RadioGroupCheckedRadioButtonId);
				}
				
				kanaTestConfig.setUntilSuccess(untilSuccessCheckBox.isChecked());
				
				kanaTestConfig.setUntilSuccessNewWordLimit(untilSuccessNewWordLimitCheckBox.isChecked());
				
				if (gojuuonCheckBox.isChecked() == false && dakutenHandakutenCheckBox.isChecked() == false && 
						youonCheckBox.isChecked() == false) {
					
					Toast toast = Toast.makeText(KanaTestOptions.this, getString(R.string.kana_test_char_no_range), Toast.LENGTH_SHORT);
					
					toast.show();
					
					return;
				}
				
				kanaTestConfig.setGojuuon(gojuuonCheckBox.isChecked());
				kanaTestConfig.setDakutenHandakuten(dakutenHandakutenCheckBox.isChecked());
				kanaTestConfig.setYouon(youonCheckBox.isChecked());
				
				kanaTestContext.setInitialized(false);
				
				Intent intent = new Intent(getApplicationContext(), KanaTest.class);
				
				startActivity(intent);
				
				finish();
			}
		});
		
		result.add(new StringValue("", 12.0f, 0));
		
		result.add(startButton);
		
		Button reportProblemButton = (Button)findViewById(R.id.kana_test_options_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer detailsSb = new StringBuffer();
				
				for (IScreenItem currentScreenItem : result) {
					detailsSb.append(currentScreenItem.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kana_test_options_report_problem_email_subject);
				
				String mailBody = getString(R.string.kana_test_options_report_problem_email_body,
						detailsSb.toString());
				
		        String versionName = "";
		        int versionCode = 0;
		        
		        try {
		        	PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
		        	
		            versionName = packageInfo.versionName;
		            versionCode = packageInfo.versionCode;

		        } catch (NameNotFoundException e) {        	
		        }
								
				Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString(), versionName, versionCode); 
				
				startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));
			}
		});
		
		return result;
	}
	
	private void setUntilSuccessNewWordLimitCheckBoxEnabled(CheckBox untilSuccessCheckBox, CheckBox untilSuccessNewWordLimitCheckBox) {
		
		if (untilSuccessCheckBox.isChecked() == true) {
			untilSuccessNewWordLimitCheckBox.setEnabled(true);
		} else {
			untilSuccessNewWordLimitCheckBox.setEnabled(false);
		}
	}
}
