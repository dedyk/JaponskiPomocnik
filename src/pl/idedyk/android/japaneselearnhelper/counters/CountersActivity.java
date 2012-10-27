package pl.idedyk.android.japaneselearnhelper.counters;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.CountersHelper;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.CounterEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.CounterEntry.Entry;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

public class CountersActivity extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.counters);
		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.counters_main_layout);
		
		List<IScreenItem> report = new ArrayList<IScreenItem>();
		
		// get all counters
		CountersHelper countersHelper = new CountersHelper();
		
		List<CounterEntry> allCounters = countersHelper.getAllCounters(getResources());
		
		report.add(new TitleItem(getString(R.string.counters_title), 0));
		
		for (CounterEntry currentCounter : allCounters) {
			
			String kanji = currentCounter.getKanji();
			String kana = currentCounter.getKana();
			String romaji = currentCounter.getRomaji();
			
			String description = currentCounter.getDescription();
						
			description = description.substring(0, 1).toUpperCase() + description.substring(1);
			
			StringBuffer title = new StringBuffer();
			
			title.append(description);
			title.append(": ");
			
			if (kanji != null) {
				title.append(kanji);
				
				title.append(" - ");
			}
			
			title.append(kana);
			
			title.append(" (");
			title.append(romaji);
			title.append(")");
			
			report.add(new TitleItem(title.toString(), 1));
			
			TableLayout tableLayout = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, null, true);
			
			List<Entry> currentCounterEntries = currentCounter.getEntries();
			
			for (Entry currentCounterEntriesCurrentEntry : currentCounterEntries) {
				
				TableRow tableRow = new TableRow();
				
				if (currentCounterEntriesCurrentEntry != null) {
					StringValue entryKey = new StringValue(currentCounterEntriesCurrentEntry.getKey(), 15.0f, 2);
					StringValue entryKanji = new StringValue(currentCounterEntriesCurrentEntry.getKanji(), 15.0f, 2);
					StringValue entryKana = new StringValue(currentCounterEntriesCurrentEntry.getKana(), 15.0f, 2);
					StringValue entryRomaji = new StringValue(currentCounterEntriesCurrentEntry.getRomaji(), 15.0f, 2);

					entryKey.setNullMargins(false);
					entryKanji.setNullMargins(true);
					entryKana.setNullMargins(true);
					entryRomaji.setNullMargins(true);				

					tableRow.addScreenItem(entryKey);
					tableRow.addScreenItem(entryKanji);
					tableRow.addScreenItem(entryKana);
					tableRow.addScreenItem(entryRomaji);
				} else {
					tableRow.addScreenItem(new StringValue("", 3.0f, 2));
				}
				
				tableLayout.addTableRow(tableRow);
			}			
			
			report.add(tableLayout);
			
			List<String> exampleUse = currentCounter.getExampleUse();
			
			if (exampleUse != null && exampleUse.size() > 0) {
				
				report.add(new StringValue("", 10.0f, 2));
				
				report.add(new TitleItem(getString(R.string.counters_examples), 2));
				
				for (String currentExample : exampleUse) {
					report.add(new StringValue(currentExample, 15.0f, 2));
				}
			}
		}
		
		// fill mail layout
		fillMainLayout(report, mainLayout);
	}

	private void fillMainLayout(List<IScreenItem> generatedDetails, LinearLayout mainLayout) {

		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), mainLayout);
		}
	}
}
