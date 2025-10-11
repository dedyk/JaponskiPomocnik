package pl.idedyk.android.japaneselearnhelper.transitiveintransitive;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryDetails;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.japanese.dictionary.api.dto.DictionaryEntry;
import pl.idedyk.japanese.dictionary.api.dto.TransitiveIntransitivePair;
import pl.idedyk.japanese.dictionary.api.dto.TransitiveIntransitivePairWithDictionaryEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary2.jmdict.xsd.JMdict;

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
import android.widget.Toast;

public class TransitiveIntransitivePairsTable extends Activity {
	
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

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.id.rootView, R.layout.transitive_intransitive_pairs_table);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_transitive_intransitive_pairs_table));
		
		DictionaryEntry transitiveVerb = (DictionaryEntry)getIntent().getSerializableExtra("transitive_verb");
		DictionaryEntry intransitiveVerb = (DictionaryEntry)getIntent().getSerializableExtra("intransitive_verb");

		final LinearLayout mainLayout = (LinearLayout)findViewById(R.id.transitive_intransitive_pairs_table_main_layout);
		
		final List<IScreenItem> report = new ArrayList<IScreenItem>();
		
		if (transitiveVerb == null || intransitiveVerb == null) {
			
			// generate index
			try {
				generateIndex(report);

			} catch (DictionaryException e) {

				Toast.makeText(this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();
			}
			
		} else {
			// generate pairs report body
			generatePairsReportBody(report, transitiveVerb, intransitiveVerb);
		}
		
		// fill mail layout
		fillMainLayout(report, mainLayout);

		/*
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
		*/
	}
	
	private void generateIndex(List<IScreenItem> report) throws DictionaryException {
		
		// add index
		int indexPos = 0;
		
		report.add(indexPos, new TitleItem(getString(R.string.transitive_intransitive_pairs_table_index), 0));
		indexPos++;
		
		report.add(indexPos, new StringValue(getString(R.string.transitive_intransitive_pairs_table_index_go), 12.0f, 1));
		indexPos++;
		
		report.add(indexPos, new StringValue("", 6.0f, 2));
		indexPos++;
		
		// dictionary manager
		DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);
		
		List<TransitiveIntransitivePairWithDictionaryEntry> transitiveIntransitivePairsList = dictionaryManager.getTransitiveIntransitivePairsList();

		for (TransitiveIntransitivePairWithDictionaryEntry transitiveIntransitivePair : transitiveIntransitivePairsList) {

			final DictionaryEntry transitivityDictionaryEntry = transitiveIntransitivePair.getTransitiveDictionaryEntry();
			final DictionaryEntry intransitivityDictionaryEntry = transitiveIntransitivePair.getIntransitiveDictionaryEntry();

			// title			
			StringValue titleStringValue = new StringValue(transitivityDictionaryEntry.getKanji() + " [" + transitivityDictionaryEntry.getRomaji() + "] - " 
					+ intransitivityDictionaryEntry.getKanji() + " [" + intransitivityDictionaryEntry.getRomaji() + "]", 15.0f, 2);
			
			titleStringValue.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					
					Intent intent = new Intent(getApplicationContext(), TransitiveIntransitivePairsTable.class);
					
					intent.putExtra("transitive_verb", transitivityDictionaryEntry);
					intent.putExtra("intransitive_verb", intransitivityDictionaryEntry);
					
					startActivity(intent);
				}
			});
			
			report.add(indexPos, titleStringValue);
			
			indexPos++;
		}
		
		report.add(indexPos, new StringValue("", 12.0f, 2));		
	}

	private void generatePairsReportBody(List<IScreenItem> report, DictionaryEntry transitivityDictionaryEntry, DictionaryEntry intransitivityDictionaryEntry) {
				
		// dictionary manager
		DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);
		
		// title
		report.add(new TitleItem(getString(R.string.transitive_intransitive_pairs_table_title), 0));

		// title 2
		TitleItem titleItem = new TitleItem(transitivityDictionaryEntry.getKanji() + " [" + transitivityDictionaryEntry.getRomaji() + "] - " 
				+ intransitivityDictionaryEntry.getKanji() + " [" + intransitivityDictionaryEntry.getRomaji() + "]", 1);
					
		report.add(titleItem);
		
		// transitive verb
		generateVerbBody(report, transitivityDictionaryEntry, getString(R.string.transitive_intransitive_pairs_table_transitive_verb));
		
		// intransitve verb
		generateVerbBody(report, intransitivityDictionaryEntry, getString(R.string.transitive_intransitive_pairs_table_intransitive_verb));
					
		// spacer
		report.add(new StringValue("", 8.0f, 1));
		
	}
	
	private void generateVerbBody(List<IScreenItem> report, final DictionaryEntry dictionaryEntry, String title) {
				
		OnClickListener goToVerbDictionaryEntryDetails = 
				new OnClickListener() {
					
					@Override
					public void onClick(View v) {

						DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(TransitiveIntransitivePairsTable.this);

						// pobieramy Entry
						JMdict.Entry dictionaryEntry2;

						try {
							dictionaryEntry2 = dictionaryManager.getDictionaryEntry2ByOldPolishJapaneseDictionaryId(dictionaryEntry.getId());

						} catch (DictionaryException e) {
							Toast.makeText(TransitiveIntransitivePairsTable.this, getString(R.string.dictionary_exception_common_error_message, e.getMessage()), Toast.LENGTH_LONG).show();

							return;
						}

						Intent intent = new Intent(getApplicationContext(), WordDictionaryDetails.class);
						
						intent.putExtra("item", dictionaryEntry2);
						
						startActivity(intent);
					}
				};
		
		// spacer
		report.add(new StringValue("", 1.5f, 1));
				
		TitleItem verbTitle = new TitleItem(title, 2);
		verbTitle.setOnClickListener(goToVerbDictionaryEntryDetails);
		
		report.add(verbTitle);
		
		// spacer
		report.add(new StringValue("", 0.5f, 1));
		
		TableLayout tableLayout = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, true);
		
		if (dictionaryEntry.isKanjiExists() == true) {
			
			// kanji
			addRowValue(tableLayout, getString(R.string.transitive_intransitive_pairs_table_kanji), dictionaryEntry.getKanji(), goToVerbDictionaryEntryDetails);
		}
		
		// kana
		addRowValue(tableLayout, getString(R.string.transitive_intransitive_pairs_table_kana), dictionaryEntry.getKana(), goToVerbDictionaryEntryDetails);
		
		// romaji
		addRowValue(tableLayout, getString(R.string.transitive_intransitive_pairs_table_romaji), dictionaryEntry.getRomaji(), goToVerbDictionaryEntryDetails);
		
		// translate
		addRowValue(tableLayout, getString(R.string.transitive_intransitive_pairs_table_translate), listStringToString(dictionaryEntry.getTranslates()), goToVerbDictionaryEntryDetails);
		
		report.add(tableLayout);
	}
	
	private void addRowValue(TableLayout tableLayout, String title, String value, OnClickListener goToVerbDictionaryEntryDetails) {
		
		TableRow tableRow = new TableRow();
		
		StringValue titleStringValue = new StringValue(title, 14.0f, 0);
		
		titleStringValue.setMarginTop(0);
		titleStringValue.setMarginLeft(5);
		titleStringValue.setMarginRight(5);
		titleStringValue.setMarginBottom(0);

		titleStringValue.setBackgroundColor(JapaneseAndroidLearnHelperApplication.getInstance().getThemeType().getTitleItemBackgroundColorAsColor());
		
		titleStringValue.setOnClickListener(goToVerbDictionaryEntryDetails);
		
		StringValue valueStringValue = new StringValue(value, 14.0f, 0);
		
		valueStringValue.setOnClickListener(goToVerbDictionaryEntryDetails);
		
		tableRow.addScreenItem(titleStringValue);
		tableRow.addScreenItem(valueStringValue);
		
		tableLayout.addTableRow(tableRow);
	}
	
	private String listStringToString(List<String> listString) {
		
		StringBuffer sb = new StringBuffer();
		
		for (int idx = 0; idx < listString.size(); ++idx) {
			
			sb.append(listString.get(idx));
			
			if (idx != listString.size() - 1) {
				
				sb.append("\n");				
			}
		}
		
		return sb.toString();
	}

	private void fillMainLayout(List<IScreenItem> generatedDetails, LinearLayout mainLayout) {

		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), mainLayout);
		}
	}
}
