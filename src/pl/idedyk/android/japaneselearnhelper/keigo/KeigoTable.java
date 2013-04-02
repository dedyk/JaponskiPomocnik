package pl.idedyk.android.japaneselearnhelper.keigo;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.KeigoHelper;
import pl.idedyk.android.japaneselearnhelper.dictionary.KeigoHelper.KeigoEntry;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.android.japaneselearnhelper.testsm2.WordTestSM2Options;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class KeigoTable extends Activity {

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
		
		setContentView(R.layout.keigo_table);
		
		final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.keigo_table_main_layout);
		
		final List<IScreenItem> report = new ArrayList<IScreenItem>();
		
		// get keigo helper
		KeigoHelper keigoHelper = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this).getKeigoHelper();
		
		report.add(new TitleItem(getString(R.string.keigo_title), 0));
		
		addKeigoHighEntryList(report, keigoHelper);
		
		// fill mail layout
		fillMainLayout(report, mainLayout);

		// report problem
		Button reportProblemButton = (Button)findViewById(R.id.keigo_table_report_problem_button);
		
		reportProblemButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer reportSb = new StringBuffer();
				
				for (IScreenItem currentReportScreenItem : report) {
					reportSb.append(currentReportScreenItem.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.keigo_table_report_problem_email_subject);
				
				String mailBody = getString(R.string.keigo_table_report_problem_email_body,
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
	
	private void addKeigoHighEntryList(List<IScreenItem> report, KeigoHelper keigoHelper) {
		
		// get keigo high entry list
		List<KeigoEntry> keigoHighEntryList = keigoHelper.getKeigoHighEntryList();
		
		report.add(new TitleItem(getString(R.string.keigo_high_entry_list_title), 1));
		
		TableLayout tableLayout = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, true);
		
		TableRow titleTableRow = new TableRow();
		
		StringValue titleTableRowVerb = new StringValue(getString(R.string.keigo_high_entry_list_verb), 13.5f, 1);
		StringValue titleTableRowKeigoVerb = new StringValue(getString(R.string.keigo_high_entry_list_keigo_verb), 13.5f, 1);
		StringValue titleTableRowKeigoVerbMasu = new StringValue(getString(R.string.keigo_high_entry_list_keigo_verb_masu), 13.5f, 1);
		
		titleTableRowVerb.setMarginLeft(5);
		titleTableRowVerb.setMarginTop(0);
		titleTableRowVerb.setMarginRight(0);
		titleTableRowVerb.setMarginBottom(0);

		titleTableRowKeigoVerb.setMarginLeft(2);
		titleTableRowKeigoVerb.setMarginTop(0);
		titleTableRowKeigoVerb.setMarginRight(2);
		titleTableRowKeigoVerb.setMarginBottom(0);

		titleTableRowKeigoVerbMasu.setMarginLeft(0);
		titleTableRowKeigoVerbMasu.setMarginTop(0);
		titleTableRowKeigoVerbMasu.setMarginRight(5);
		titleTableRowKeigoVerbMasu.setMarginBottom(0);
				
		titleTableRow.addScreenItem(titleTableRowVerb);
		titleTableRow.addScreenItem(titleTableRowKeigoVerb);
		titleTableRow.addScreenItem(titleTableRowKeigoVerbMasu);
		
		tableLayout.addTableRow(titleTableRow);
		
		for (KeigoEntry currentKeigoEntry : keigoHighEntryList) {
			
			TableRow keigoEntryTableRow = new TableRow();
			
			String keigoEntryKanji = currentKeigoEntry.getKanji();
			String keigoEntryKana = currentKeigoEntry.getKana();
			
			String keigoEntryKeigoKanji = currentKeigoEntry.getKeigoKanji(true);
			String keigoEntryKeigoKana = currentKeigoEntry.getKeigoKana(true);
			
			String keigoEntryKeigoKanjiMasu = currentKeigoEntry.getKeigoLongFormWithoutMasuKanji();
			String keigoEntryKeigoKanaMasu = currentKeigoEntry.getKeigoLongFormWithoutMasuKana();
			
			String verb = null;
			
			if (keigoEntryKanji != null) {
				verb = keigoEntryKanji + " (" + keigoEntryKana + ")";
				
			} else {
				verb = keigoEntryKana;
			}
			
			if (verb.equals("ている") == true) {
				verb = "~" + verb;
			}
			
			StringValue verbStringValue = new StringValue(verb, 13.5f, 1);
			
			verbStringValue.setMarginLeft(5);
			verbStringValue.setMarginTop(0);
			verbStringValue.setMarginRight(0);
			verbStringValue.setMarginBottom(0);
			
			keigoEntryTableRow.addScreenItem(verbStringValue);
			
			String keigoVerb = null;
			
			if (keigoEntryKeigoKanji != null) {
				keigoVerb = keigoEntryKeigoKanji + " (" + keigoEntryKeigoKana + ")";
				
			} else {
				keigoVerb = keigoEntryKeigoKana;
			}

			StringValue keigoVerbStringValue = new StringValue(keigoVerb, 13.5f, 1);
			
			keigoVerbStringValue.setMarginLeft(2);
			keigoVerbStringValue.setMarginTop(0);
			keigoVerbStringValue.setMarginRight(2);
			keigoVerbStringValue.setMarginBottom(0);
			
			keigoEntryTableRow.addScreenItem(keigoVerbStringValue);
			
			String keigoVerbMasu = null;
			
			if (keigoEntryKeigoKanjiMasu != null) {
				keigoVerbMasu = keigoEntryKeigoKanjiMasu + "ます";
				
			} else if (keigoEntryKeigoKanaMasu != null) {
				keigoVerbMasu = keigoEntryKeigoKanaMasu + "ます";
			}

			if (keigoVerbMasu != null) {

				StringValue keigoEntryKeigoKanjiMasuStringValue = new StringValue(keigoVerbMasu + "ます", 13.5f, 1);
				
				keigoVerbStringValue.setMarginLeft(0);
				keigoVerbStringValue.setMarginTop(0);
				keigoVerbStringValue.setMarginRight(5);
				keigoVerbStringValue.setMarginBottom(0);
				
				keigoEntryTableRow.addScreenItem(keigoEntryKeigoKanjiMasuStringValue);
			}			
			
			tableLayout.addTableRow(keigoEntryTableRow);
			
			TableRow spacerTableRow = new TableRow();			
			spacerTableRow.addScreenItem(new StringValue("", 8.0f, 1));
			tableLayout.addTableRow(spacerTableRow);
		}
				
		report.add(tableLayout);
		
		StringValue keigoVerbMaseInfo = new StringValue(getString(R.string.keigo_high_entry_list_keigo_verb_masu_info), 13.0f, 1);
		
		keigoVerbMaseInfo.setMarginLeft(5);
		keigoVerbMaseInfo.setMarginTop(0);
		keigoVerbMaseInfo.setMarginRight(0);
		keigoVerbMaseInfo.setMarginBottom(0);
		
		report.add(new StringValue("", 12.0f, 2));
		report.add(keigoVerbMaseInfo);
	}

	private void fillMainLayout(List<IScreenItem> generatedDetails, LinearLayout mainLayout) {

		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), mainLayout);
		}
	}
}
