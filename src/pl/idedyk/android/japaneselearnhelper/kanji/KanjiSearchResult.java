package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiDic2Entry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class KanjiSearchResult extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.kanji_entry_search_result);
		
		final String[] selectedRadicals = (String[])getIntent().getSerializableExtra("search");
		
		final TextView searchValueTextView = (TextView)findViewById(R.id.kanji_entry_search_value);
		
		searchValueTextView.setText(Arrays.toString(selectedRadicals));
		
		final TextView kanjiDictionarySearchElementsNoTextView = (TextView)findViewById(R.id.kanji_entry_elements_no);
		
		kanjiDictionarySearchElementsNoTextView.setText(getString(R.string.kanji_entry_elements_no, "???"));
		
		final DictionaryManager dictionaryManager = DictionaryManager.getInstance();
		
		final ListView searchResultListView = (ListView)findViewById(R.id.kanji_entry_search_result_list);
		
		final List<KanjiEntryListItem> searchResultList = new ArrayList<KanjiEntryListItem>();
		
		final KanjiEntryListItemAdapter searchResultArrayAdapter = new KanjiEntryListItemAdapter(this, 
				R.layout.kanji_entry_simplerow, searchResultList);
		
		searchResultListView.setAdapter(searchResultArrayAdapter);
		
		searchResultListView.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				
				KanjiEntryListItem kanjiEntryListItem = searchResultArrayAdapter.getItem(position);
				
				Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);
				
				intent.putExtra("item", kanjiEntryListItem.getKanjiEntry());
				
				startActivity(intent);
				
			}
		});
		
		final ProgressDialog progressDialog = ProgressDialog.show(this, 
				getString(R.string.kanji_entry_searching1),
				getString(R.string.kanji_entry_searching2));
		
		final Resources resources = getResources();
		
		class FindKanjiAsyncTask extends AsyncTask<Void, Void, List<KanjiEntry>> {
			
			@Override
			protected List<KanjiEntry> doInBackground(Void... params) {							
				return dictionaryManager.findKnownKanjiFromRadicals(selectedRadicals);
			}
			
		    @Override
		    protected void onPostExecute(List<KanjiEntry> foundKanjis) {
		        super.onPostExecute(foundKanjis);
		        
		        kanjiDictionarySearchElementsNoTextView.setText(resources.getString(R.string.kanji_entry_elements_no, String.valueOf(foundKanjis.size())));
		        				
				for (KanjiEntry currentKanjiEntry : foundKanjis) {
					
					KanjiDic2Entry kanjiDic2Entry = currentKanjiEntry.getKanjiDic2Entry();
					
					StringBuffer currentKanjiEntryFullText = new StringBuffer();
					
					currentKanjiEntryFullText.append("<big>").append(currentKanjiEntry.getKanji()).append("</big> - ").append(currentKanjiEntry.getPolishTranslates().toString()).append("\n\n");
					currentKanjiEntryFullText.append(kanjiDic2Entry.getRadicals().toString());
					
					final String fontBegin = "<font color='red'>";
					final String fontEnd = "</font>";
					
					for (String currentRadical : selectedRadicals) {
						
						int idxStart = 0;
						
						while(true) {
							
							int idx1 = currentKanjiEntryFullText.indexOf(currentRadical, idxStart);
							
							if (idx1 == -1) {
								break;
							}
							
							currentKanjiEntryFullText.insert(idx1, fontBegin);
							
							currentKanjiEntryFullText.insert(idx1 + currentRadical.length() + fontBegin.length(), fontEnd);

							idxStart = idx1 + currentRadical.length() + fontBegin.length() + fontEnd.length();
						}
					}
													
					searchResultList.add(new KanjiEntryListItem(currentKanjiEntry, Html.fromHtml(currentKanjiEntryFullText.toString().replaceAll("\n", "<br/>"))));								
				}

				searchResultArrayAdapter.notifyDataSetChanged();
		        
		        progressDialog.dismiss();
		    }
		}
		
		new FindKanjiAsyncTask().execute();
		
		Button reportProblemButton = (Button)findViewById(R.id.kanji_entry_report_problem_button);
		
		reportProblemButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View view) {
								
				StringBuffer searchListText = new StringBuffer();
				
				for (int searchResultArrayAdapterIdx = 0; searchResultArrayAdapterIdx < searchResultArrayAdapter.size(); ++searchResultArrayAdapterIdx) {
					searchListText.append(searchResultArrayAdapter.getItem(searchResultArrayAdapterIdx).getText().toString()).append("\n--\n");
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
	}
}
