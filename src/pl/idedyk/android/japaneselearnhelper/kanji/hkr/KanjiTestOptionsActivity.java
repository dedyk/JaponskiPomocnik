package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiTestConfig;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioButton;

public class KanjiTestOptionsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		final KanjiTestConfig kanjiTestConfig = ConfigManager.getInstance().getKanjiTestConfig();
		
		setContentView(R.layout.kanji_test_options);
		
		final RadioButton testModeDrawKanjiFromMeaningRadioButton = (RadioButton)findViewById(R.id.kanji_test_options_test_mode_draw_kanji_from_meaning);
		final RadioButton testModeDrawKanjiFromInWord = (RadioButton)findViewById(R.id.kanji_test_options_test_mode_draw_kanji_in_word);
		
		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
		
		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			testModeDrawKanjiFromMeaningRadioButton.setChecked(true);
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			testModeDrawKanjiFromInWord.setChecked(true);
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");
		}
		
		final CheckBox untilSuccessCheckBox = (CheckBox)findViewById(R.id.kanji_test_options_until_success);
		
		untilSuccessCheckBox.setChecked(kanjiTestConfig.getUntilSuccess());
		
		// create menu list
		ListView kanjiChooseList = (ListView)findViewById(R.id.kanji_test_options_choose_kanji_list);
		
		final List<KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem> kanjiChooseListItems = new ArrayList<KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem>();
		
		// fill list
		List<KanjiEntry> allKanjis = DictionaryManager.getInstance().getAllKanjis();
		
		Set<String> chosenKanji = kanjiTestConfig.getChosenKanji();
		
		for (KanjiEntry currentKanjiEntry : allKanjis) {
			
			StringBuffer currentKanjiEntryText = new StringBuffer();
			
			currentKanjiEntryText.append("<big>").append(currentKanjiEntry.getKanji()).append("</big> - ").append(currentKanjiEntry.getPolishTranslates().toString()).append("\n\n");
			
			KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem kanjiChooseListItem = 
					new KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem(currentKanjiEntry, 
					Html.fromHtml(currentKanjiEntryText.toString()),
					chosenKanji.contains(currentKanjiEntry.getKanji()));
			
			kanjiChooseListItems.add(kanjiChooseListItem);
		}
		
		ArrayAdapter<KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem> kanjiChooseListItemsAdapter = 
				new KanjiTestOptionsChooseKanjiArrayAdapter(this, kanjiChooseListItems);

		kanjiChooseList.setAdapter(kanjiChooseListItemsAdapter);
		
		Button startTestButton = (Button)findViewById(R.id.kanji_test_options_start_test);
		
		startTestButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				if (testModeDrawKanjiFromMeaningRadioButton.isChecked() == true) {
					kanjiTestConfig.setKanjiTestMode(KanjiTestMode.DRAW_KANJI_FROM_MEANING);
				} else if (testModeDrawKanjiFromInWord.isChecked() == true) {
					kanjiTestConfig.setKanjiTestMode(KanjiTestMode.DRAW_KANJI_IN_WORD);
				} else {
					throw new RuntimeException("KanjiTestMode kanjiTestMode");
				}
				
				kanjiTestConfig.setUntilSuccess(untilSuccessCheckBox.isChecked());
				
				List<String> chosenKanjiList = new ArrayList<String>();
				
				for (KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem currentKanjiChooseListItem : kanjiChooseListItems) {
					
					if (currentKanjiChooseListItem.isChecked() == true) {
						chosenKanjiList.add(currentKanjiChooseListItem.getKanjiEntry().getKanji());
					}
				}
				
				kanjiTestConfig.setChosenKanji(chosenKanjiList);
			}
		});
	}
}
