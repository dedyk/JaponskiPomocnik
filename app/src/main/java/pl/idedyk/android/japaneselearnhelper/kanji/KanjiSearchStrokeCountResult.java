package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem.ItemType;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.utils.WordKanjiDictionaryUtils;
import pl.idedyk.japanese.dictionary.api.dto.KanjiDic2Entry;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class KanjiSearchStrokeCountResult extends Activity {
	
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

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.kanji_search_stroke_count_result);

		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_kanji_search_stroke_count_result));

		final Object[] kanjiStrokeCountResult = (Object[])getIntent().getSerializableExtra("kanjiStrokeCountResult");
		
		final ListView kanjiStrokeCountResultListView = (ListView)findViewById(R.id.kanji_search_stroke_count_result_list);
		
		final List<KanjiEntryListItem> searchResultList = new ArrayList<KanjiEntryListItem>();
		
		for (Object currentKanjiEntryAsObject : kanjiStrokeCountResult) {
			
			KanjiEntry currentKanjiEntry = (KanjiEntry)currentKanjiEntryAsObject;
			
			KanjiDic2Entry kanjiDic2Entry = currentKanjiEntry.getKanjiDic2Entry();

			String currentKanjiEntryFullText = WordKanjiDictionaryUtils.getKanjiFullTextWithMark(currentKanjiEntry);
			String currentKanjiEntryRadicalText = WordKanjiDictionaryUtils.getKanjiRadicalTextWithMark(currentKanjiEntry);

			searchResultList.add(KanjiEntryListItem.createKanjiEntryListItemAsKanjiEntry(currentKanjiEntry,
					Html.fromHtml(currentKanjiEntryFullText.replaceAll("\n", "<br/>")),
					Html.fromHtml(currentKanjiEntryRadicalText)));
		}
		
		Typeface babelStoneHanTypeface = JapaneseAndroidLearnHelperApplication.getInstance().getBabelStoneHanSubset(getAssets());
		
		final KanjiEntryListItemAdapter searchResultArrayAdapter = new KanjiEntryListItemAdapter(this, 
				searchResultList, babelStoneHanTypeface);
		
		kanjiStrokeCountResultListView.setAdapter(searchResultArrayAdapter);
		
		TextView kanjiStrokeCountResultElementsNo = (TextView)findViewById(R.id.kanji_search_stroke_count_result_elements_no);
		
		kanjiStrokeCountResultElementsNo.setText(getString(R.string.kanji_search_stroke_count_result_elements_no, searchResultList.size()));
		
		kanjiStrokeCountResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				KanjiEntryListItem kanjiEntryListItem = (KanjiEntryListItem)searchResultArrayAdapter.getItem(position);
				
				if (kanjiEntryListItem.getItemType() == ItemType.KANJI_ENTRY) {
					
					Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);
					
					intent.putExtra("item", kanjiEntryListItem.getKanjiEntry());
					
					startActivity(intent);
				}
			}
		});

		/*
		Button reportProblemButton = (Button)findViewById(R.id.kanji_search_stroke_count_result_report_problem_button);
		
		reportProblemButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
								
				StringBuffer searchListText = new StringBuffer();
				
				for (int searchResultArrayAdapterIdx = 0; searchResultArrayAdapterIdx < searchResultArrayAdapter.size(); ++searchResultArrayAdapterIdx) {
					searchListText.append(((KanjiEntryListItem)searchResultArrayAdapter.getItem(searchResultArrayAdapterIdx)).getText().toString()).append("\n--\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kanji_search_stroke_count_result_report_problem_email_subject);
				
				String mailBody = getString(R.string.kanji_search_stroke_count_result_report_problem_email_body,
						searchListText.toString());
				
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
}
