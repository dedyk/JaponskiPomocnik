package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem.ItemType;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.utils.WordKanjiDictionaryUtils;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;
import pl.idedyk.japanese.dictionary2.kanjidic2.xsd.KanjiCharacterInfo;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

public class KanjiSearchRadicalResult extends Activity {
	
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

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.kanji_search_radical_result);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_kanji_search_radical_result));

		Typeface babelStoneHanTypeface = JapaneseAndroidLearnHelperApplication.getInstance().getBabelStoneHanSubset(getAssets());
		
		final String[] selectedRadicals = (String[])getIntent().getSerializableExtra("search");

		// konfiguracja zakladek
		TabHost host = (TabHost)findViewById(R.id.kanji_search_radical_tab_host);

		host.setup();

		// Zakladka ogolna
		TabHost.TabSpec generalTab = host.newTabSpec(getString(R.string.kanji_search_radical_generalTab_label));
		generalTab.setContent(R.id.kanji_search_radical_tab_content_tab1);
		generalTab.setIndicator(getString(R.string.kanji_search_radical_generalTab_label));
		host.addTab(generalTab);

		// Zakladka ze szczegolami (lista)
		TabHost.TabSpec detailsTab = host.newTabSpec(getString(R.string.kanji_search_radical_detailsTab_label));
		detailsTab.setContent(R.id.kanji_search_radical_tab_content_tab2);
		detailsTab.setIndicator(getString(R.string.kanji_search_radical_detailsTab_label));
		host.addTab(detailsTab);

		final DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

		// wypelnianie zawartosci (czesc wspolna) - inicjacja

		final TextView searchValueTextView = (TextView)findViewById(R.id.kanji_search_radical_value);
		
		searchValueTextView.setTypeface(babelStoneHanTypeface);
		searchValueTextView.setText(Arrays.toString(selectedRadicals));
		
		final TextView kanjiDictionarySearchElementsNoTextView = (TextView)findViewById(R.id.kanji_entry_elements_no);
		
		kanjiDictionarySearchElementsNoTextView.setText(getString(R.string.kanji_entry_elements_no, "???"));

		// czesc ogolna - inicjacja
		final LinearLayout generalLinearLayout = (LinearLayout) findViewById(R.id.kanji_search_radical_tab_content_tab1);

		// czesc szczegolowa - inicjacja
		final ListView searchResultListView = (ListView)findViewById(R.id.kanji_search_radical_result_list);
		
		final List<KanjiEntryListItem> searchResultList = new ArrayList<KanjiEntryListItem>();
		
		final KanjiEntryListItemAdapter searchResultArrayAdapter = new KanjiEntryListItemAdapter(this, 
				searchResultList, babelStoneHanTypeface);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				KanjiEntryListItem kanjiEntryListItem = (KanjiEntryListItem)searchResultArrayAdapter.getItem(position);
				
				if (kanjiEntryListItem.getItemType() == ItemType.KANJI_ENTRY) {
					Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);

					intent.putExtra("id", kanjiEntryListItem.getKanjiEntry().getId());
					
					startActivity(intent);					
				}
			}
		});
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, 
				getString(R.string.kanji_entry_searching1),
				getString(R.string.kanji_entry_searching2));
		
		final Resources resources = getResources();

		class PrepareAsyncTaskResult {

			private List<KanjiCharacterInfo> kanjiEntryList;

			private DictionaryException dictionaryException;

			public PrepareAsyncTaskResult(List<KanjiCharacterInfo> kanjiEntryList) {
				this.kanjiEntryList = kanjiEntryList;
			}

			public PrepareAsyncTaskResult(DictionaryException dictionaryException) {
				this.dictionaryException = dictionaryException;

				this.kanjiEntryList = new ArrayList<>();
			}
		}

		class FindKanjiAsyncTask extends AsyncTask<Void, Void, PrepareAsyncTaskResult> {
			
			@Override
			protected PrepareAsyncTaskResult doInBackground(Void... params) {
				try {
					return new PrepareAsyncTaskResult(dictionaryManager.findKnownKanjiFromRadicals(selectedRadicals));

				} catch (DictionaryException e) {
					return new PrepareAsyncTaskResult(e);
				}
			}
			
		    @Override
		    protected void onPostExecute(PrepareAsyncTaskResult result) {
		        super.onPostExecute(result);

				if (result.dictionaryException != null) {
					Toast.makeText(KanjiSearchRadicalResult.this, getString(R.string.dictionary_exception_common_error_message, result.dictionaryException.getMessage()), Toast.LENGTH_LONG).show();
				}

				List<KanjiCharacterInfo> foundKanjis = result.kanjiEntryList;

				kanjiDictionarySearchElementsNoTextView.setText(resources.getString(R.string.kanji_entry_elements_no, String.valueOf(foundKanjis.size())));

				// posortowanie po liczbie kresek
				Collections.sort(foundKanjis, new Comparator<KanjiCharacterInfo>() {

					@Override
					public int compare(KanjiCharacterInfo k1, KanjiCharacterInfo k2) {
						Integer k1StrokeNumber = WordKanjiDictionaryUtils.getStrokeNumber(k1, 100);
						Integer k2StrokeNumber = WordKanjiDictionaryUtils.getStrokeNumber(k2, 100);

						return k1StrokeNumber < k2StrokeNumber ? -1 : k1StrokeNumber > k2StrokeNumber ? 1 : 0;
					}
				});

				// wypelnianie czesci ogolnej
				{
					// lista z elementami
					List<IScreenItem> screenItemList = new ArrayList<IScreenItem>();

					KanjiSearchUtils.generateKanjiSearchGeneralResult(KanjiSearchRadicalResult.this, foundKanjis, screenItemList, true);

					// generowanie zawartosci ekranu
					generalLinearLayout.removeAllViews();

					for (IScreenItem currentScreenItem : screenItemList) {
						currentScreenItem.generate(KanjiSearchRadicalResult.this, getResources(), generalLinearLayout);
					}
				}

				// wypelnianie czesci szczegolowej
				for (KanjiCharacterInfo currentKanjiEntry : foundKanjis) {

                    String currentKanjiEntryFullText = WordKanjiDictionaryUtils.getKanjiFullTextWithMark(currentKanjiEntry);
                    String currentKanjiEntryRadicalText = WordKanjiDictionaryUtils.getKanjiRadicalTextWithMark(currentKanjiEntry);

					searchResultList.add(KanjiEntryListItem.createKanjiEntryListItemAsKanjiEntry(currentKanjiEntry,
							Html.fromHtml(currentKanjiEntryFullText.replaceAll("\n", "<br/>")),
							Html.fromHtml(currentKanjiEntryRadicalText)));
				}

				searchResultArrayAdapter.notifyDataSetChanged();
		        
				if (progressDialog != null && progressDialog.isShowing()) {
					progressDialog.dismiss();
				}
		    }
		}
		
		new FindKanjiAsyncTask().execute();

		/*
		Button reportProblemButton = (Button)findViewById(R.id.kanji_entry_report_problem_button);
		
		reportProblemButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {

				StringBuffer searchListText = new StringBuffer();
				
				for (int searchResultArrayAdapterIdx = 0; searchResultArrayAdapterIdx < searchResultArrayAdapter.size(); ++searchResultArrayAdapterIdx) {
					searchListText.append(((KanjiEntryListItem)searchResultArrayAdapter.getItem(searchResultArrayAdapterIdx)).getText().toString()).append("\n--\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kanji_search_result_report_problem_email_subject);
				
				String mailBody = getString(R.string.kanji_search_result_report_problem_email_body,
						searchValueTextView.getText(), searchListText.toString());
				
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
