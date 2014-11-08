package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiDetails;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItemAdapter;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
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

public class KanjiRecognizerResult extends Activity {
	
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

		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_kanji_recognizer_result));
		
		setContentView(R.layout.kanji_recognizer_result);
		
		final Object[] kanjiRecognizeResult = (Object[])getIntent().getSerializableExtra("kanjiRecognizeResult");
		final String kanjiRecognizeResultStrokes = getIntent().getStringExtra("kanjiRecognizeResultStrokes");
		
		final ListView kanjiRecognizerResultListView = (ListView)findViewById(R.id.kanji_recognizer_result_list);
		
		final List<KanjiEntryListItem> searchResultList = new ArrayList<KanjiEntryListItem>();
		
		for (Object currentKanjiEntryAsObject : kanjiRecognizeResult) {
			
			KanjiEntry currentKanjiEntry = (KanjiEntry)currentKanjiEntryAsObject;
			
			KanjiDic2Entry kanjiDic2Entry = currentKanjiEntry.getKanjiDic2Entry();
			
			StringBuffer currentKanjiEntryFullText = new StringBuffer();
			StringBuffer currentKanjiEntryRadicalText = new StringBuffer();
			
			currentKanjiEntryFullText.append("<big>").append(currentKanjiEntry.getKanji()).append("</big> - ").append(currentKanjiEntry.getPolishTranslates().toString()).append("\n");
						
			if (kanjiDic2Entry != null && kanjiDic2Entry.getRadicals() != null && kanjiDic2Entry.getRadicals().size() > 0) {
				currentKanjiEntryRadicalText.append(kanjiDic2Entry.getRadicals().toString());	
			}
											
			searchResultList.add(new KanjiEntryListItem(currentKanjiEntry, 
					Html.fromHtml(currentKanjiEntryFullText.toString().replaceAll("\n", "<br/>")),
					Html.fromHtml(currentKanjiEntryRadicalText.toString())));
		}
		
		Typeface babelStoneHanTypeface = JapaneseAndroidLearnHelperApplication.getInstance().getBabelStoneHanSubset(getAssets());
		
		final KanjiEntryListItemAdapter searchResultArrayAdapter = new KanjiEntryListItemAdapter(this, 
				R.layout.kanji_entry_simplerow, searchResultList, babelStoneHanTypeface);
		
		kanjiRecognizerResultListView.setAdapter(searchResultArrayAdapter);
		
		TextView kanjiRecognizerResultElementsNo = (TextView)findViewById(R.id.kanji_recognizer_result_elements_no);
		
		kanjiRecognizerResultElementsNo.setText(getString(R.string.kanji_recognizer_result_elements_no, searchResultList.size()));
		
		kanjiRecognizerResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				KanjiEntryListItem kanjiEntryListItem = searchResultArrayAdapter.getItem(position);
				
				Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);
				
				intent.putExtra("item", kanjiEntryListItem.getKanjiEntry());
				
				startActivity(intent);
				
			}
		});
		
		Button reportProblemButton = (Button)findViewById(R.id.kanji_recognizer_result_report_problem_button);
		
		reportProblemButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
								
				StringBuffer searchListText = new StringBuffer();
				
				for (int searchResultArrayAdapterIdx = 0; searchResultArrayAdapterIdx < searchResultArrayAdapter.size(); ++searchResultArrayAdapterIdx) {
					searchListText.append(searchResultArrayAdapter.getItem(searchResultArrayAdapterIdx).getText().toString()).append("\n--\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kanji_recognizer_result_report_problem_email_subject);
				
				String mailBody = getString(R.string.kanji_recognizer_result_report_problem_email_body,
						searchListText.toString(), kanjiRecognizeResultStrokes);
				
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
}
