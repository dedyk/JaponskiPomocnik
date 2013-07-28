package pl.idedyk.android.japaneselearnhelper.transitiveintransitive;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.TransitiveIntransitivePair;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
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

public class TransitiveIntransitivePairsTable extends Activity {
	
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
		
		setContentView(R.layout.transitive_intransitive_pairs_table);
		
		final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.transitive_intransitive_pairs_table_main_layout);
		
		final List<IScreenItem> report = new ArrayList<IScreenItem>();
		
		final List<TitleItem> titleList = new ArrayList<TitleItem>();
		
		report.add(new TitleItem(getString(R.string.transitive_intransitive_pairs_table_title), 0));
		
		// generate pairs report body
		generatePairsReportBody(report, titleList);
		
		// generate index
		generateIndex(report, titleList);
		
		// fill mail layout
		fillMainLayout(report, mainLayout);

		// report problem
		Button reportProblemButton = (Button)findViewById(R.id.transitive_intransitive_pairs_table_report_problem_button);
		
		reportProblemButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer reportSb = new StringBuffer();
				
				for (IScreenItem currentReportScreenItem : report) {
					reportSb.append(currentReportScreenItem.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.transitive_intransitive_pairs_table_report_problem_email_subject);
				
				String mailBody = getString(R.string.transitive_intransitive_pairs_table_report_problem_email_body,
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
	
	private void generateIndex(List<IScreenItem> report, List<TitleItem> titleList) {
		
		// add index
		int indexPos = 0;
		
		report.add(indexPos, new TitleItem(getString(R.string.transitive_intransitive_pairs_table_index), 0));
		indexPos++;
		
		report.add(indexPos, new StringValue(getString(R.string.transitive_intransitive_pairs_table_index_go), 12.0f, 1));
		indexPos++;
		
		report.add(indexPos, new StringValue("", 6.0f, 2));
		indexPos++;
		
		for (final TitleItem currentTitle : titleList) {
			StringValue titleStringValue = new StringValue(currentTitle.getTitle(), 15.0f, 2);
			
			titleStringValue.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					
					final ScrollView scrollMainLayout = (ScrollView)findViewById(R.id.transitive_intransitive_pairs_table_main_layout_scroll);
					
					backScreenPositionStack.push(scrollMainLayout.getScrollY());
										
					scrollMainLayout.scrollTo(0, currentTitle.getY());					
				}
			});
			
			report.add(indexPos, titleStringValue);
			
			indexPos++;
		}
		
		report.add(indexPos, new StringValue("", 12.0f, 2));		
	}

	private void generatePairsReportBody(List<IScreenItem> report, List<TitleItem> titleList) {
		
		// dictionary manager
		DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);
		
		List<TransitiveIntransitivePair> transitiveIntransitivePairsList = dictionaryManager.getTransitiveIntransitivePairsList();

		for (TransitiveIntransitivePair transitiveIntransitivePair : transitiveIntransitivePairsList) {
			
			Integer transitiveId = transitiveIntransitivePair.getTransitiveId();
			Integer intransitiveId = transitiveIntransitivePair.getIntransitiveId();
			
			DictionaryEntry transitivityDictionaryEntry = dictionaryManager.getDictionaryEntryById(transitiveId);
			DictionaryEntry intransitivityDictionaryEntry = dictionaryManager.getDictionaryEntryById(intransitiveId);
			
			TitleItem titleItem = new TitleItem(transitivityDictionaryEntry.getKanji() + " [" + transitivityDictionaryEntry.getRomajiList().get(0) + "] - " 
					+ intransitivityDictionaryEntry.getKanji() + " [" + intransitivityDictionaryEntry.getRomajiList().get(0) + "]", 1);
			
			titleList.add(titleItem);
			
			report.add(titleItem);
			
			// spacer
			report.add(new StringValue("", 8.0f, 1));
		}
	}

	private void fillMainLayout(List<IScreenItem> generatedDetails, LinearLayout mainLayout) {

		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), mainLayout);
		}
	}
	
	@Override
	public void onBackPressed() {
		
		if (backScreenPositionStack.isEmpty() == true) {
			super.onBackPressed();
			
			return;
		}
		
		Integer backPostion = backScreenPositionStack.pop();

		final ScrollView scrollMainLayout = (ScrollView)findViewById(R.id.transitive_intransitive_pairs_table_main_layout_scroll);
		
		scrollMainLayout.scrollTo(0, backPostion);
	}
}
