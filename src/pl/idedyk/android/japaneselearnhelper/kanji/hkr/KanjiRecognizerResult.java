package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiDetails;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItemAdapter;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;

public class KanjiRecognizerResult extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);

		setContentView(R.layout.kanji_recognizer_result);
		
		Object[] kanjiRecognizeResult = (Object[])getIntent().getSerializableExtra("kanjiRecognizeResult");
		
		final ListView kanjiRecognizerResultListView = (ListView)findViewById(R.id.kanji_recognizer_result_list);
		
		final List<KanjiEntryListItem> searchResultList = new ArrayList<KanjiEntryListItem>();
		
		for (Object currentKanjiEntryAsObject : kanjiRecognizeResult) {
			
			KanjiEntry currentKanjiEntry = (KanjiEntry)currentKanjiEntryAsObject;
			
			KanjiDic2Entry kanjiDic2Entry = currentKanjiEntry.getKanjiDic2Entry();
			
			StringBuffer currentKanjiEntryFullText = new StringBuffer();
			
			currentKanjiEntryFullText.append("<big>").append(currentKanjiEntry.getKanji()).append("</big> - ").append(currentKanjiEntry.getPolishTranslates().toString()).append("\n\n");
			
			if (kanjiDic2Entry != null && kanjiDic2Entry.getRadicals() != null) {
				currentKanjiEntryFullText.append(kanjiDic2Entry.getRadicals().toString());	
			}
											
			searchResultList.add(new KanjiEntryListItem(currentKanjiEntry, Html.fromHtml(currentKanjiEntryFullText.toString().replaceAll("\n", "<br/>"))));
		}
		
		final KanjiEntryListItemAdapter searchResultArrayAdapter = new KanjiEntryListItemAdapter(this, 
				R.layout.kanji_entry_simplerow, searchResultList);
		
		kanjiRecognizerResultListView.setAdapter(searchResultArrayAdapter);
		
		kanjiRecognizerResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				KanjiEntryListItem kanjiEntryListItem = searchResultArrayAdapter.getItem(position);
				
				Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);
				
				intent.putExtra("item", kanjiEntryListItem.getKanjiEntry());
				
				startActivity(intent);
				
			}
		});		
	}
}
