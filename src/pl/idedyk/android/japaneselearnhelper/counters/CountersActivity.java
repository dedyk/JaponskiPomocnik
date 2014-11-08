package pl.idedyk.android.japaneselearnhelper.counters;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Stack;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.CountersHelper;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.japanese.dictionary.api.dto.CounterEntry;
import pl.idedyk.japanese.dictionary.api.dto.CounterEntry.Entry;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class CountersActivity extends Activity {
	
	private Stack<Integer> backScreenPositionStack = new Stack<Integer>();
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuShorterHelper.onCreateOptionsMenu(menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);

		return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_counters));
		
		setContentView(R.layout.counters);
		
		final ScrollView scrollMainLayout = (ScrollView)findViewById(R.id.counters_main_layout_scroll);
		final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.counters_main_layout);
		
		final List<IScreenItem> report = new ArrayList<IScreenItem>();
		
		final List<TitleItem> titleList = new ArrayList<TitleItem>();
		
		// get all counters
		CountersHelper countersHelper = new CountersHelper();
		
		List<CounterEntry> allCounters = countersHelper.getAllCounters(getResources());
		
		report.add(new TitleItem(getString(R.string.counters_title), 0));
		
		for (CounterEntry currentCounter : allCounters) {
			
			String kanji = currentCounter.getKanji();
			String kana = currentCounter.getKana();
			String romaji = currentCounter.getRomaji();
			
			String description = currentCounter.getDescription();
						
			description = description.substring(0, 1).toUpperCase(Locale.getDefault()) + description.substring(1);
			
			StringBuffer title = new StringBuffer();
			
			title.append(description);
			title.append(":  ");
			
			if (kanji != null) {
				title.append(kanji);
				
				title.append(" - ");
			}
			
			title.append(kana);
			
			title.append(" (");
			title.append(romaji);
			title.append(")");
			
			TitleItem currentTitle = new TitleItem(title.toString(), 1);
			
			titleList.add(currentTitle);
			report.add(currentTitle);
			
			TableLayout tableLayout = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, true);
			
			List<Entry> currentCounterEntries = currentCounter.getEntries();
			
			for (Entry currentCounterEntriesCurrentEntry : currentCounterEntries) {
				
				TableRow tableRow = new TableRow();
				
				if (currentCounterEntriesCurrentEntry != null) {
					StringValue entryKey = new StringValue(currentCounterEntriesCurrentEntry.getKey(), 15.0f, 1);
					StringValue entryKanji = new StringValue(currentCounterEntriesCurrentEntry.getKanji(), 15.0f, 1);
					StringValue entryKana = new StringValue(currentCounterEntriesCurrentEntry.getKana(), 15.0f, 1);
					StringValue entryRomaji = new StringValue(currentCounterEntriesCurrentEntry.getRomaji(), 15.0f, 1);

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
			
			report.add(new StringValue("", 12.0f, 2));
		}
		
		// add index
		int indexPos = 0;
		
		report.add(indexPos, new TitleItem(getString(R.string.counters_index), 0));
		indexPos++;
		
		report.add(indexPos, new StringValue(getString(R.string.counters_index_go), 12.0f, 1));
		indexPos++;
		
		report.add(indexPos, new StringValue("", 6.0f, 2));
		indexPos++;
		
		for (final TitleItem currentTitle : titleList) {
			StringValue titleStringValue = new StringValue(currentTitle.getTitle(), 15.0f, 2);
			
			titleStringValue.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					
					backScreenPositionStack.push(scrollMainLayout.getScrollY());
					
					scrollMainLayout.scrollTo(0, currentTitle.getY() - 3);
				}
			});
			
			report.add(indexPos, titleStringValue);
			
			indexPos++;
		}
		
		report.add(indexPos, new StringValue("", 12.0f, 2));
		
		// fill mail layout
		fillMainLayout(report, mainLayout);
		
		// report problem
		Button reportProblemButton = (Button)findViewById(R.id.counters_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer reportSb = new StringBuffer();
				
				for (IScreenItem currentReportScreenItem : report) {
					reportSb.append(currentReportScreenItem.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.counters_report_problem_email_subject);
				
				String mailBody = getString(R.string.counters_report_problem_email_body,
						reportSb.toString());				
				
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
	}
	
	@Override
	public void onBackPressed() {
		
		if (backScreenPositionStack.isEmpty() == true) {
			super.onBackPressed();
			
			return;
		}
		
		Integer backPostion = backScreenPositionStack.pop();

		final ScrollView scrollMainLayout = (ScrollView)findViewById(R.id.counters_main_layout_scroll);
		
		scrollMainLayout.scrollTo(0, backPostion);
	}

	private void fillMainLayout(List<IScreenItem> generatedDetails, LinearLayout mainLayout) {

		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), mainLayout);
		}
	}
}
