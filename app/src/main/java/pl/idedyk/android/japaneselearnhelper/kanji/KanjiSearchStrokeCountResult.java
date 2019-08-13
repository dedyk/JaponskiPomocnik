package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem.ItemType;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiRecognizerResult;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.utils.WordKanjiDictionaryUtils;
import pl.idedyk.japanese.dictionary.api.dictionary.dto.FindKanjiResult;
import pl.idedyk.japanese.dictionary.api.dto.KanjiDic2Entry;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

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

		TextView kanjiStrokeCountResultElementsNo = (TextView) findViewById(R.id.kanji_search_stroke_count_result_elements_no);

		kanjiStrokeCountResultElementsNo.setText(getString(R.string.kanji_search_stroke_count_result_elements_no, 0));

		// konfiguracja zakladek
		TabHost host = (TabHost)findViewById(R.id.kanji_search_stroke_count_tab_host);

		host.setup();

		// Zakladka ogolna
		TabHost.TabSpec generalTab = host.newTabSpec(getString(R.string.kanji_search_stroke_count_generalTab_label));
		generalTab.setContent(R.id.kanji_search_stroke_count_tab_content_tab1);
		generalTab.setIndicator(getString(R.string.kanji_search_stroke_count_generalTab_label));
		host.addTab(generalTab);

		// Zakladka ze szczegolami (lista)
		TabHost.TabSpec detailsTab = host.newTabSpec(getString(R.string.kanji_search_stroke_count_detailsTab_label));
		detailsTab.setContent(R.id.kanji_search_stroke_count_tab_content_tab2);
		detailsTab.setIndicator(getString(R.string.kanji_search_stroke_count_detailsTab_label));
		host.addTab(detailsTab);

		//

		final int fromInt = getIntent().getIntExtra("from", 1);
		final int toInt = getIntent().getIntExtra("to", 1);

		// pokazanie kreciola
		final ProgressDialog progressDialog = ProgressDialog.show(KanjiSearchStrokeCountResult.this,
				getString(R.string.kanji_entry_searching1),
				getString(R.string.kanji_entry_searching2));

		class PrepareAsyncTaskResult {

			private FindKanjiResult findKanjiResult;

			private DictionaryException dictionaryException;

			public PrepareAsyncTaskResult(FindKanjiResult findKanjiResult) {
				this.findKanjiResult = findKanjiResult;
			}

			public PrepareAsyncTaskResult(DictionaryException dictionaryException) {
				this.dictionaryException = dictionaryException;

				this.findKanjiResult = new FindKanjiResult();

				this.findKanjiResult.setResult(new ArrayList<KanjiEntry>());
			}
		}

		class FindKanjiAsyncTask extends AsyncTask<Void, Void, PrepareAsyncTaskResult> {

			@Override
			protected PrepareAsyncTaskResult doInBackground(Void... params) {

				try {
					return new PrepareAsyncTaskResult(JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(KanjiSearchStrokeCountResult.this).
							findKanjisFromStrokeCount(fromInt, toInt));

				} catch (DictionaryException e) {
					return new PrepareAsyncTaskResult(e);
				}
			}

			@Override
			protected void onPostExecute(PrepareAsyncTaskResult result) {
				super.onPostExecute(result);

				if (result.dictionaryException != null) {
					Toast.makeText(KanjiSearchStrokeCountResult.this, getString(R.string.dictionary_exception_common_error_message, result.dictionaryException.getMessage()), Toast.LENGTH_LONG).show();
				}

				FindKanjiResult foundKanjis = result.findKanjiResult;

				// wypelnienie ekranu zawartoscia
				fillScreen(foundKanjis);

                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }

				if (foundKanjis.isMoreElemetsExists() == true) {
					Toast toast = Toast.makeText(KanjiSearchStrokeCountResult.this, getString(R.string.kanji_search_stroke_count_result_limited), Toast.LENGTH_SHORT);

					toast.show();
				}
			}
		}

		new FindKanjiAsyncTask().execute();

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

	private void fillScreen(FindKanjiResult findKanjiResult) {

		List<KanjiEntry> kanjiEntryList = new ArrayList<KanjiEntry>();

		for (KanjiEntry currentKanjiEntry : findKanjiResult.result) {
			kanjiEntryList.add(currentKanjiEntry);
		}

		// posortowanie wyniku po liczbie kresek
		Collections.sort(kanjiEntryList, new Comparator<KanjiEntry>() {

			@Override
			public int compare(KanjiEntry k1, KanjiEntry k2) {

				KanjiDic2Entry k1Dic2Entry = k1.getKanjiDic2Entry();
				KanjiDic2Entry k2Dic2Entry = k2.getKanjiDic2Entry();

				if (k1Dic2Entry == null) {
					return -1;
				}

				if (k2Dic2Entry == null) {
					return 1;
				}

				return k1Dic2Entry.getStrokeCount() < k2Dic2Entry.getStrokeCount() ? -1 : k1Dic2Entry.getStrokeCount() > k2Dic2Entry.getStrokeCount() ? 1 : 0;
			}
		});

		// czesc ogolna - inicjacja
		final LinearLayout generalLinearLayout = (LinearLayout) findViewById(R.id.kanji_search_stroke_count_tab_content_tab1);

		// czesc szczegolowa - inicjacja
		final ListView kanjiStrokeCountResultListView = (ListView)findViewById(R.id.kanji_search_stroke_count_result_list);

		// wypelnianie czesci ogolnej
		{
			// lista z elementami
			List<IScreenItem> screenItemList = new ArrayList<IScreenItem>();

			KanjiSearchUtils.generateKanjiSearchGeneralResult(KanjiSearchStrokeCountResult.this, kanjiEntryList, screenItemList, true);

			// generowanie zawartosci ekranu
			generalLinearLayout.removeAllViews();

			for (IScreenItem currentScreenItem : screenItemList) {
				currentScreenItem.generate(KanjiSearchStrokeCountResult.this, getResources(), generalLinearLayout);
			}
		}

		// wypelnianie czesci szczegolowej
		final List<KanjiEntryListItem> searchResultList = new ArrayList<KanjiEntryListItem>();

		for (KanjiEntry currentKanjiEntry : kanjiEntryList) {

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
	}
}
