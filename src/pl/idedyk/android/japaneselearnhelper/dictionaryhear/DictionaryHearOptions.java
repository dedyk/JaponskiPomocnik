package pl.idedyk.android.japaneselearnhelper.dictionaryhear;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import android.app.Activity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.LinearLayout;

public class DictionaryHearOptions extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.dictionary_hear_options);
		
		final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.dictionary_hear_options_main_layout);
		
		// loading word groups
		
		int groupSize = 20;
		
		int wordGroupsNo = DictionaryManager.getInstance().getWordGroupsNo(groupSize);

		for (int currentGroupNo = 0; currentGroupNo < wordGroupsNo; ++currentGroupNo) {
			
			CheckBox currentWordGroupCheckBox = new CheckBox(this);
			
			currentWordGroupCheckBox.setLayoutParams(new LinearLayout.LayoutParams(
					LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));

			currentWordGroupCheckBox.setTextSize(12);
			
			int startPosition = currentGroupNo * groupSize + 1;
			int endPosition = 0;
			
			if (currentGroupNo != wordGroupsNo - 1) {
				endPosition = (currentGroupNo + 1) * groupSize + 1;
			} else {
				List<DictionaryEntry> lastWordsGroup = DictionaryManager.getInstance().getWordsGroup(groupSize, currentGroupNo);
				
				endPosition = startPosition + lastWordsGroup.size();
			}
			
			currentWordGroupCheckBox.setText(getString(R.string.dictionary_hear_options_group_position, (currentGroupNo + 1), 
					startPosition, endPosition));
			
			// FIXME !!!!!!!!!!!!!!!!!!!!!
			//currentKanjiGroupCheckBox.setChecked(chosenKanjiGroup.contains(currentKanjiGroup));
			
			//currentKanjiGroupCheckBox.setTag(kanjiGroups.get(currentKanjiGroup));
			
			//kanjiGroupList.add(currentKanjiGroupCheckBox);
			
			mainLayout.addView(currentWordGroupCheckBox, mainLayout.getChildCount());
			
			
		}
	}
}
