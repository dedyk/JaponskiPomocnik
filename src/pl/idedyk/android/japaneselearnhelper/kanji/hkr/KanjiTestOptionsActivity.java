package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class KanjiTestOptionsActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.kanji_test_options);
		
		// create menu list
		ListView kanjiChooseList = (ListView)findViewById(R.id.kanji_test_options_choose_kanji_list);
		
		final List<KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem> kanjiChooseListItems = new ArrayList<KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem>();
		
		// fill list
		List<KanjiEntry> allKanjis = DictionaryManager.getInstance().getAllKanjis();
		
		for (KanjiEntry currentKanjiEntry : allKanjis) {
			
			StringBuffer currentKanjiEntryText = new StringBuffer();
			
			currentKanjiEntryText.append("<big>").append(currentKanjiEntry.getKanji()).append("</big> - ").append(currentKanjiEntry.getPolishTranslates().toString()).append("\n\n");

			// FIXME checkbox
			KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem kanjiChooseListItem = 
					new KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem(currentKanjiEntry, Html.fromHtml(currentKanjiEntryText.toString()), false);
			
			kanjiChooseListItems.add(kanjiChooseListItem);
		}
		
		ArrayAdapter<KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem> kanjiChooseListItemsAdapter = 
				new KanjiTestOptionsChooseKanjiArrayAdapter(this, kanjiChooseListItems);

		kanjiChooseList.setAdapter(kanjiChooseListItemsAdapter);
	}
}
