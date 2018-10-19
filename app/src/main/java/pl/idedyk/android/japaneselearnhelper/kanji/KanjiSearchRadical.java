package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManagerCommon;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.japanese.dictionary.api.dto.RadicalInfo;
import pl.idedyk.japanese.dictionary.api.exception.DictionaryException;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class KanjiSearchRadical extends Activity {
	
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

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.kanji_search_radical);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_search_radical));

		List<RadicalInfo> radicalList = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(KanjiSearchRadical.this).getRadicalList();
		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.kanji_search_radical_main_layout);
		
		final Set<String> selectedRadicals = new TreeSet<String>();
		
		final List<StringValue> radicalStringValueList = new ArrayList<StringValue>();
		
		final List<IScreenItem> screenItems = generateScreen(radicalList, selectedRadicals, radicalStringValueList);
		
		fillMainLayout(screenItems, mainLayout);
		
		Button reportProblemButton = (Button)findViewById(R.id.kanji_search_radical_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer detailsSb = new StringBuffer();
				
				for (IScreenItem currentScreenItem : screenItems) {
					detailsSb.append(currentScreenItem.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kanji_search_radicals_report_problem_email_subject);
				
				String mailBody = getString(R.string.kanji_search_report_problem_email_body,
						detailsSb.toString());
				
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
		
		Button searchKanjiButton = (Button)findViewById(R.id.kanji_search_radical_kanji_button);
		
		searchKanjiButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				if (selectedRadicals.size() == 0) {
					
					Toast toast = Toast.makeText(KanjiSearchRadical.this, getString(R.string.kanji_entry_search_button_empty_radicals), Toast.LENGTH_SHORT);
					
					toast.show();

					return;					
				}
				
				String[] selectedRadicalsArray = new String[selectedRadicals.size()];
				
				selectedRadicals.toArray(selectedRadicalsArray);
				
				Arrays.sort(selectedRadicalsArray);
				
				Intent intent = new Intent(getApplicationContext(), KanjiSearchRadicalResult.class);
				
				intent.putExtra("search", selectedRadicalsArray);
				
				startActivity(intent);
			}
		});
	}
	
	private void fillMainLayout(List<IScreenItem> screenItems, LinearLayout mainLayout) {
		
		for (IScreenItem currentScreenItem : screenItems) {
			currentScreenItem.generate(this, getResources(), mainLayout);			
		}
	}

	private List<IScreenItem> generateScreen(List<RadicalInfo> radicalList, final Set<String> selectedRadicals, final List<StringValue> radicalStringValueList) {
		
		List<IScreenItem> result = new ArrayList<IScreenItem>();
		
		result.add(new TitleItem(getString(R.string.kanji_search_radical_title), 0));
		
		int lastStrokeCount = -1;
		
		TableLayout tableLayout = null;
		TableRow tableRow = null;
		
		Typeface babelStoneHanTypeface = JapaneseAndroidLearnHelperApplication.getInstance().getBabelStoneHanSubset(getAssets());
			
		for (RadicalInfo currentRadicalInfo : radicalList) {
			
			int strokeCount = currentRadicalInfo.getStrokeCount();
			
			if (strokeCount != lastStrokeCount) {
								
				result.add(new TitleItem(String.valueOf(strokeCount), 1));
				
				tableLayout = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, null, true);
				result.add(tableLayout);
				
				tableRow = new TableRow();
				
				tableLayout.addTableRow(tableRow);
			}
			
			if (tableRow.getScreenItemSize() >= 6) {
				tableRow = new TableRow();
				
				tableLayout.addTableRow(tableRow);				
			}
			
			StringValue radicalStringValue = new StringValue(currentRadicalInfo.getRadical(), 25.0f, 1);
			
			radicalStringValue.setTypeface(babelStoneHanTypeface);
			
			if (tableRow.getScreenItemSize() == 0) {
				radicalStringValue.setNullMargins(false);
			} else {
				radicalStringValue.setNullMargins(true);
			}
			
			radicalStringValue.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {

					TextView textView = (TextView)view;
					
					int currentColor = textView.getTextColors().getDefaultColor();
					
					if (currentColor == Color.DKGRAY) {
						return;
					}

					String radical = textView.getText().toString();

					if (selectedRadicals.contains(radical) == false) {
						selectedRadicals.add(radical);
					} else {
						selectedRadicals.remove(radical);
					}
					
					updateRadicalState(selectedRadicals, radicalStringValueList);
				}
			});
			
			tableRow.addScreenItem(radicalStringValue);
			
			radicalStringValueList.add(radicalStringValue);
			
			lastStrokeCount = strokeCount;
		}
		
		result.add(new StringValue("", 12.0f, 0));
		
		updateRadicalState(selectedRadicals, radicalStringValueList);
				
		return result;
	}

	protected void updateRadicalState(final Set<String> selectedRadicals, final List<StringValue> radicalStringValueList) {
		final ProgressDialog progressDialog = ProgressDialog.show(this, 
				getString(R.string.kanji_entry_searching1),
				getString(R.string.kanji_entry_searching2));

		class PrepareAsyncTaskResult {

			private Set<String> findAllAvailableRadicalsResult;

			private DictionaryException dictionaryException;

			public PrepareAsyncTaskResult(Set<String> findAllAvailableRadicalsResult) {
				this.findAllAvailableRadicalsResult = findAllAvailableRadicalsResult;
			}

			public PrepareAsyncTaskResult(DictionaryException dictionaryException) {
				this.dictionaryException = dictionaryException;

				this.findAllAvailableRadicalsResult = new HashSet<>();
			}
		}

		class FindKanjiAsyncTask extends AsyncTask<Void, Void, PrepareAsyncTaskResult> {
			
			@Override
			protected PrepareAsyncTaskResult doInBackground(Void... params) {
				
				String[] selectedRadicalsArray = new String[selectedRadicals.size()];
				
				selectedRadicals.toArray(selectedRadicalsArray);

				DictionaryManagerCommon dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(KanjiSearchRadical.this);

				try {
					return new PrepareAsyncTaskResult(dictionaryManager.findAllAvailableRadicals(selectedRadicalsArray));

				} catch (DictionaryException e) {
					return new PrepareAsyncTaskResult(e);
				}
			}
			
		    @Override
		    protected void onPostExecute(PrepareAsyncTaskResult result) {

		        super.onPostExecute(result);

				if (result.dictionaryException != null) {
					Toast.makeText(KanjiSearchRadical.this, getString(R.string.dictionary_exception_common_error_message, result.dictionaryException.getMessage()), Toast.LENGTH_LONG).show();
				}

				Set<String> allAvailableRadicals = result.findAllAvailableRadicalsResult;

				for (StringValue currentRadicalStringValue : radicalStringValueList) {
					
		        	String currentRadicalStringValueValue = currentRadicalStringValue.getValue();
		        	
		        	if (selectedRadicals.contains(currentRadicalStringValueValue) == true) {
		        		currentRadicalStringValue.setTextColor(Color.RED);
		        	} else if (allAvailableRadicals.contains(currentRadicalStringValueValue) == true) {
		        		currentRadicalStringValue.setTextColor(currentRadicalStringValue.getDefaultTextColor());
		        	} else {
		        		currentRadicalStringValue.setTextColor(Color.DKGRAY);
		        	}
				}
		        
		        if (progressDialog != null && progressDialog.isShowing()) {
		        	progressDialog.dismiss();
		        }
		    }
		}
		
		new FindKanjiAsyncTask().execute();
	}
}
