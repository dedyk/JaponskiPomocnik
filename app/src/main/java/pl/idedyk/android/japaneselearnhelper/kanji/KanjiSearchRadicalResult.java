package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem.ItemType;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.android.japaneselearnhelper.utils.WordKanjiDictionaryUtils;
import pl.idedyk.japanese.dictionary.api.dto.KanjiDic2Entry;
import pl.idedyk.japanese.dictionary.api.dto.KanjiEntry;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.Display;
import android.view.Gravity;
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

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.kanji_entry_search_radical_result);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_kanji_search_radical_result));

		Typeface babelStoneHanTypeface = JapaneseAndroidLearnHelperApplication.getInstance().getBabelStoneHanSubset(getAssets());
		
		final String[] selectedRadicals = (String[])getIntent().getSerializableExtra("search");

		// konfiguracja zakladek
		TabHost host = (TabHost)findViewById(R.id.kanji_entry_search_radical_tab_host);

		host.setup();

		// Zakladka ogolna
		TabHost.TabSpec generalTab = host.newTabSpec(getString(R.string.kanji_entry_search_radical_generalTab_label));
		generalTab.setContent(R.id.kanji_entry_search_radical_tab_content_tab1);
		generalTab.setIndicator(getString(R.string.kanji_entry_search_radical_generalTab_label));
		host.addTab(generalTab);

		// Zakladka ze szczegolami (lista)
		TabHost.TabSpec detailsTab = host.newTabSpec(getString(R.string.kanji_entry_search_radical_detailsTab_label));
		detailsTab.setContent(R.id.kanji_entry_search_radical_tab_content_tab2);
		detailsTab.setIndicator(getString(R.string.kanji_entry_search_radical_detailsTab_label));
		host.addTab(detailsTab);

		final DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);

		// wypelnianie zawartosci (czesc wspolna) - inicjacja

		final TextView searchValueTextView = (TextView)findViewById(R.id.kanji_entry_search_radical_value);
		
		searchValueTextView.setTypeface(babelStoneHanTypeface);
		searchValueTextView.setText(Arrays.toString(selectedRadicals));
		
		final TextView kanjiDictionarySearchElementsNoTextView = (TextView)findViewById(R.id.kanji_entry_elements_no);
		
		kanjiDictionarySearchElementsNoTextView.setText(getString(R.string.kanji_entry_elements_no, "???"));

		// czesc ogolna - inicjacja
		LinearLayout generalLinearLayout = (LinearLayout) findViewById(R.id.kanji_entry_search_radical_tab_content_tab1);

		List<IScreenItem> generalScreenItemList = new ArrayList<IScreenItem>();

		TableLayout tableLayout = new pl.idedyk.android.japaneselearnhelper.screen.TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, null, true);

		generalScreenItemList.add(tableLayout);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow currentRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

		tableLayout.addTableRow(currentRow);

		//

		int testI = 1;

		//int itemsWidth = 0;

		//int counter = 0;

		int counter2 = 0;


		// result.add(tableLayout);

		// tableRow = new TableRow();


		for (int i = 0; i < 40; ++i) {

			if (i % 10 == 0) {
				StringValue sv = new StringValue(String.valueOf(testI), 25.0f, 0);

				testI++;

				/*
				sv.setMarginLeft(0);
				sv.setMarginRight(10);
				sv.setMarginBottom(0);
				sv.setMarginTop(0);
				*/

				sv.setNullMargins(true);

				sv.setGravity(Gravity.CENTER);

				sv.setBackgroundColor(JapaneseAndroidLearnHelperApplication.getInstance().getThemeType().getTitleItemBackgroundColorAsColor());

				currentRow.addScreenItem(sv);

				//

				// itemsWidth += sv.getWidthOnDisplay(this, getResources(), generalLinearLayout, display);;

				//

				//counter++;

				if (currentRow.getScreenItemSize() > 5) {

					// counter = 0;

					currentRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

					tableLayout.addTableRow(currentRow);

					//itemsWidth = 0;
				}
			}

			counter2++;

			//StringValue stringValue = new StringValue(String.valueOf(counter2) + " 猫" , 25.0f, 0);
			StringValue stringValue = new StringValue("猫" , 25.0f, 0);

			// stringValue.setMarginRight(90);

			/*
			stringValue.setMarginLeft(0);
			stringValue.setMarginRight(10);
			stringValue.setMarginBottom(0);
			stringValue.setMarginTop(0);
			*/

			stringValue.setNullMargins(true);

			stringValue.setGravity(Gravity.CENTER);

			currentRow.addScreenItem(stringValue);

			// itemsWidth += stringValue.getWidthOnDisplay(this, getResources(), generalLinearLayout, display);

			// counter++;

			//if (itemsWidth > display.getWidth() - 80) {
			if (currentRow.getScreenItemSize() > 5) {

				// counter = 0;

				currentRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();

				tableLayout.addTableRow(currentRow);

				//itemsWidth = 0;
			}
		}

		// generalLinearLayout.removeAllViews();

		for (IScreenItem currentScreenItem : generalScreenItemList) {
			currentScreenItem.generate(this, getResources(), generalLinearLayout);
		}


		////////////////

		// czesc szczegolowa - inicjacja
		final ListView searchResultListView = (ListView)findViewById(R.id.kanji_entry_search_radical_result_list);
		
		final List<KanjiEntryListItem> searchResultList = new ArrayList<KanjiEntryListItem>();
		
		final KanjiEntryListItemAdapter searchResultArrayAdapter = new KanjiEntryListItemAdapter(this, 
				searchResultList, babelStoneHanTypeface);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				KanjiEntryListItem kanjiEntryListItem = (KanjiEntryListItem)searchResultArrayAdapter.getItem(position);
				
				if (kanjiEntryListItem.getItemType() == ItemType.KANJI_ENTRY) {
					
					Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);
					
					intent.putExtra("item", kanjiEntryListItem.getKanjiEntry());
					
					startActivity(intent);					
				}
			}
		});
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, 
				getString(R.string.kanji_entry_searching1),
				getString(R.string.kanji_entry_searching2));
		
		final Resources resources = getResources();

		class PrepareAsyncTaskResult {

			private List<KanjiEntry> kanjiEntryList;

			private DictionaryException dictionaryException;

			public PrepareAsyncTaskResult(List<KanjiEntry> kanjiEntryList) {
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

				List<KanjiEntry> foundKanjis = result.kanjiEntryList;

				kanjiDictionarySearchElementsNoTextView.setText(resources.getString(R.string.kanji_entry_elements_no, String.valueOf(foundKanjis.size())));
		        				
				for (KanjiEntry currentKanjiEntry : foundKanjis) {
					
					KanjiDic2Entry kanjiDic2Entry = currentKanjiEntry.getKanjiDic2Entry();

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
