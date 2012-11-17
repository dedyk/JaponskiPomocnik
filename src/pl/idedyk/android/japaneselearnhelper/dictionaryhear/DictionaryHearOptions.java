package pl.idedyk.android.japaneselearnhelper.dictionaryhear;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.DictionaryHearConfig;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

public class DictionaryHearOptions extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dictionary_hear_options);
		
		final DictionaryHearConfig dictionaryHearConfig = ConfigManager.getInstance().getDictionaryHearConfig();
		
		final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.dictionary_hear_options_main_layout);
		
		// get repeat number
		final EditText repeatNumberEditText = (EditText)findViewById(R.id.dictionary_hear_options_repeat_number_edit_text);
		
		repeatNumberEditText.setText(String.valueOf(dictionaryHearConfig.getRepeatNumber()));
		
		final CheckBox randomCheckBox = (CheckBox)findViewById(R.id.dictionary_hear_options_random);
		
		randomCheckBox.setChecked(dictionaryHearConfig.getRandom());
		
		// loading word groups
		final List<CheckBox> wordGroupCheckBoxList = new ArrayList<CheckBox>();
		
		final int groupSize = 20;
		
		int wordGroupsNo = DictionaryManager.getInstance().getWordGroupsNo(groupSize);
		
		Set<Integer> chosenWordGroups = dictionaryHearConfig.getChosenWordGroups();

		for (int currentGroupNo = 0; currentGroupNo < wordGroupsNo; ++currentGroupNo) {
			
			CheckBox currentWordGroupCheckBox = new CheckBox(this);
			
			currentWordGroupCheckBox.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

			currentWordGroupCheckBox.setTextSize(12);
			
			int startPosition = currentGroupNo * groupSize + 1;
			int endPosition = 0;
			
			if (currentGroupNo != wordGroupsNo - 1) {
				endPosition = (currentGroupNo + 1) * groupSize;
			} else {
				List<DictionaryEntry> lastWordsGroup = DictionaryManager.getInstance().getWordsGroup(groupSize, currentGroupNo);
				
				endPosition = startPosition + lastWordsGroup.size();
			}
			
			currentWordGroupCheckBox.setText(getString(R.string.dictionary_hear_options_group_position, (currentGroupNo + 1), 
					startPosition, endPosition));
			
			if (chosenWordGroups != null && chosenWordGroups.contains(Integer.valueOf(currentGroupNo))) {
				currentWordGroupCheckBox.setChecked(true);
			}
						
			wordGroupCheckBoxList.add(currentWordGroupCheckBox);
			
			mainLayout.addView(currentWordGroupCheckBox, mainLayout.getChildCount());
		}
		
		// start action
		Button startButton = (Button)findViewById(R.id.dictionary_hear_start);
		
		startButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				String repeatNumberString = repeatNumberEditText.getText().toString();
				
				boolean repeatNumberError = false;
				
				int repeatNumber = -1;
				
				if (repeatNumberString == null) {
					repeatNumberError = true;
				} else {
					
					try {
						repeatNumber = Integer.parseInt(repeatNumberString);
					} catch (NumberFormatException e) {
						repeatNumberError = true;
					}
				}
				
				if (repeatNumberError == false && repeatNumber <= 0) {
					repeatNumberError = true;
				}
				
				if (repeatNumberError == true) {
					
					Toast toast = Toast.makeText(DictionaryHearOptions.this, getString(R.string.dictionary_hear_options_repeat_number_invalid), Toast.LENGTH_SHORT);

					toast.show();

					return;					
				}
				
				dictionaryHearConfig.setRepeatNumber(repeatNumber);
				
				boolean random = randomCheckBox.isChecked();
				
				dictionaryHearConfig.setRandom(random);
				
				List<DictionaryEntry> chosenAllDictionaryEntryList = new ArrayList<DictionaryEntry>();
				List<Integer> chosenWordGroupsNumberList = new ArrayList<Integer>();
				
				for (int wordGroupCheckBoxListIdx = 0; wordGroupCheckBoxListIdx < wordGroupCheckBoxList.size(); ++wordGroupCheckBoxListIdx) {
					
					CheckBox currentWordGroupCheckBox = wordGroupCheckBoxList.get(wordGroupCheckBoxListIdx);
					
					if (currentWordGroupCheckBox.isChecked() == true) {
						
						List<DictionaryEntry> currentWordsGroupDictionaryEntryList = DictionaryManager.getInstance().getWordsGroup(groupSize, wordGroupCheckBoxListIdx);
						
						for (int repeatIdx = 0; repeatIdx < repeatNumber; ++repeatIdx) {
							chosenAllDictionaryEntryList.addAll(currentWordsGroupDictionaryEntryList);
						}
						
						chosenWordGroupsNumberList.add(wordGroupCheckBoxListIdx);
					}
				}
				
				if (chosenAllDictionaryEntryList.size() == 0) {
					
					Toast toast = Toast.makeText(DictionaryHearOptions.this, getString(R.string.dictionary_hear_options_word_group_no_chosen), Toast.LENGTH_SHORT);

					toast.show();

					return;
				}
				
				dictionaryHearConfig.setChosenWordGroups(chosenWordGroupsNumberList);
				
				if (random == true) {
					Collections.shuffle(chosenAllDictionaryEntryList);
				}
				
				
			}
		});
	}
}
