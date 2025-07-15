package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiDetails;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItemAdapter;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem.ItemType;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiSearchUtils;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.utils.WordKanjiDictionaryUtils;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
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

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.id.rootView, R.layout.kanji_recognizer_result);

		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_kanji_recognizer_result));

		final Object[] kanjiRecognizeResult = (Object[])getIntent().getSerializableExtra("kanjiRecognizeResult");
		final String kanjiRecognizeResultStrokes = getIntent().getStringExtra("kanjiRecognizeResultStrokes");

		// konfiguracja zakladek
		TabHost host = (TabHost)findViewById(R.id.kanji_recognizer_tab_host);

		host.setup();

		// Zakladka ogolna
		TabHost.TabSpec generalTab = host.newTabSpec(getString(R.string.kanji_recognizer_generalTab_label));
		generalTab.setContent(R.id.kanji_recognizer_tab_content_tab1);
		generalTab.setIndicator(getString(R.string.kanji_recognizer_generalTab_label));
		host.addTab(generalTab);

		// Zakladka ze szczegolami (lista)
		TabHost.TabSpec detailsTab = host.newTabSpec(getString(R.string.kanji_recognizer_detailsTab_label));
		detailsTab.setContent(R.id.kanji_recognizer_tab_content_tab2);
		detailsTab.setIndicator(getString(R.string.kanji_recognizer_detailsTab_label));
		host.addTab(detailsTab);

		//

		// czesc ogolna - inicjacja
		final LinearLayout generalLinearLayout = (LinearLayout) findViewById(R.id.kanji_recognizer_tab_content_tab1);

		// czesc szczegolowa - inicjacja
		final ListView kanjiRecognizerResultListView = (ListView)findViewById(R.id.kanji_recognizer_result_list);

		// wypelnianie czesci ogolnej
		{
			// lista z elementami
			List<IScreenItem> screenItemList = new ArrayList<IScreenItem>();

			List<KanjiCharacterInfo> kanjiEntryList = new ArrayList<KanjiCharacterInfo>();

			for (Object currentKanjiEntryAsObject : kanjiRecognizeResult) {

				KanjiCharacterInfo currentKanjiEntry = (KanjiCharacterInfo) currentKanjiEntryAsObject;

				kanjiEntryList.add(currentKanjiEntry);
			}

			KanjiSearchUtils.generateKanjiSearchGeneralResult(KanjiRecognizerResult.this, kanjiEntryList, screenItemList, false);

			// generowanie zawartosci ekranu
			generalLinearLayout.removeAllViews();

			for (IScreenItem currentScreenItem : screenItemList) {
				currentScreenItem.generate(KanjiRecognizerResult.this, getResources(), generalLinearLayout);
			}
		}

		// wypelnianie czesci szczegolowej
		final List<KanjiEntryListItem> searchResultList = new ArrayList<KanjiEntryListItem>();
		
		for (Object currentKanjiEntryAsObject : kanjiRecognizeResult) {

			KanjiCharacterInfo currentKanjiEntry = (KanjiCharacterInfo)currentKanjiEntryAsObject;
			
			String currentKanjiEntryFullText = WordKanjiDictionaryUtils.getKanjiFullTextWithMark(currentKanjiEntry);
			String currentKanjiEntryRadicalText = WordKanjiDictionaryUtils.getKanjiRadicalTextWithMark(currentKanjiEntry);

			searchResultList.add(KanjiEntryListItem.createKanjiEntryListItemAsKanjiEntry(currentKanjiEntry,
					Html.fromHtml(currentKanjiEntryFullText.replaceAll("\n", "<br/>")),
					Html.fromHtml(currentKanjiEntryRadicalText)));
		}
		
		Typeface babelStoneHanTypeface = JapaneseAndroidLearnHelperApplication.getInstance().getBabelStoneHanSubset(getAssets());
		
		final KanjiEntryListItemAdapter searchResultArrayAdapter = new KanjiEntryListItemAdapter(this, 
				searchResultList, babelStoneHanTypeface);
		
		kanjiRecognizerResultListView.setAdapter(searchResultArrayAdapter);
		
		TextView kanjiRecognizerResultElementsNo = (TextView)findViewById(R.id.kanji_recognizer_result_elements_no);
		
		kanjiRecognizerResultElementsNo.setText(getString(R.string.kanji_recognizer_result_elements_no, String.valueOf(searchResultList.size())));

		kanjiRecognizerResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				KanjiEntryListItem kanjiEntryListItem = (KanjiEntryListItem)searchResultArrayAdapter.getItem(position);
				
				if (kanjiEntryListItem.getItemType() == ItemType.KANJI_ENTRY) {
					Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);

					intent.putExtra("id", kanjiEntryListItem.getKanjiEntry().getId());
					
					startActivity(intent);					
				}				
			}
		});

		/*
		Button reportProblemButton = (Button)findViewById(R.id.kanji_recognizer_result_report_problem_button);
		
		reportProblemButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
								
				StringBuffer searchListText = new StringBuffer();
				
				for (int searchResultArrayAdapterIdx = 0; searchResultArrayAdapterIdx < searchResultArrayAdapter.size(); ++searchResultArrayAdapterIdx) {
					searchListText.append(((KanjiEntryListItem)searchResultArrayAdapter.getItem(searchResultArrayAdapterIdx)).getText().toString()).append("\n--\n");
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
		*/
	}
}
