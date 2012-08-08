package pl.idedyk.android.japaneselearnhelper.kana;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.screen.CheckBox;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.RadioGroup;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class KanaTestOptions extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
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
		
		List<IScreenItem> result = new ArrayList<IScreenItem>();
		
		result.add(new TitleItem(getString(R.string.kana_test_range), 0));
		
		RadioGroup rangeTestRadioGroup = new RadioGroup(this);
		
		rangeTestRadioGroup.addRadioButton(this, getString(R.string.kana_test_range_hiragana), R.id.kana_test_range_hiragana_id, false);
		rangeTestRadioGroup.addRadioButton(this, getString(R.string.kana_test_range_katakana), R.id.kana_test_range_katakana_id, false);
		rangeTestRadioGroup.addRadioButton(this, getString(R.string.kana_test_range_hiragana_katakana), R.id.kana_test_range_hiragana_katakana_id, true);
		
		result.add(rangeTestRadioGroup);
		
		result.add(new TitleItem(getString(R.string.kana_test_mode1), 0));
		
		RadioGroup testMode1RadioGroup = new RadioGroup(this);
		
		testMode1RadioGroup.addRadioButton(this, getString(R.string.kana_test_mode1_choose), R.id.kana_test_mode1_choose_id, true);
		testMode1RadioGroup.addRadioButton(this, getString(R.string.kana_test_mode1_input), R.id.kana_test_mode1_input_id, false);
		
		result.add(testMode1RadioGroup);
		
		result.add(new TitleItem(getString(R.string.kana_test_mode2), 0));
		
		RadioGroup testMode2RadioGroup = new RadioGroup(this);
		
		testMode2RadioGroup.addRadioButton(this, getString(R.string.kana_test_mode2_kana_to_romaji), R.id.kana_test_mode2_kana_to_romaji_id, true);
		
		testMode2RadioGroup.addRadioButton(this, getString(R.string.kana_test_mode2_romaji_to_kana), R.id.kana_test_mode2_romaji_to_kana_id, false);
		
		result.add(testMode2RadioGroup);
		result.add(new StringValue(getString(R.string.kana_test_mode2_romaji_to_kana_info), 12.0f, 2));
		
		result.add(new TitleItem(getString(R.string.kana_test_other), 0));
		
		result.add(new CheckBox(this, getString(R.string.kana_test_until_success), true, R.id.kana_test_until_success_id));
		
		return result;
	}
}
