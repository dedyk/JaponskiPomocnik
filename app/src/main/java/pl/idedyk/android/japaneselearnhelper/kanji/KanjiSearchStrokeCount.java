package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.screen.EditText;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

public class KanjiSearchStrokeCount extends Activity {
	
	private EditText strokeCountFromEditText;
	private EditText strokeCountToEditText;

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

		JapaneseAndroidLearnHelperApplication.getInstance().setContentViewAndTheme(this, R.layout.kanji_search_stroke_count);
		
		JapaneseAndroidLearnHelperApplication.getInstance().logScreen(this, getString(R.string.logs_kanji_search_stroke_count));

		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.kanji_search_stroke_count_main_layout);
		
		final List<IScreenItem> screenItems = generateScreen();
		
		fillMainLayout(screenItems, mainLayout);

		/*
		Button reportProblemButton = (Button)findViewById(R.id.kanji_search_stroke_count_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer detailsSb = new StringBuffer();
				
				for (IScreenItem currentScreenItem : screenItems) {
					detailsSb.append(currentScreenItem.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kanji_search_stroke_count_report_problem_email_subject);
				
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
		*/
		
		Button searchKanjiButton = (Button)findViewById(R.id.kanji_search_stroke_count_kanji_button);
		
		searchKanjiButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				String fromString = strokeCountFromEditText.getCurrentText();
				
				final Integer fromInt = getInt(fromString, 0);
				
				if (fromInt == null) {
					Toast toast = Toast.makeText(KanjiSearchStrokeCount.this, getString(R.string.kanji_search_stroke_count_from_incorrect, fromString), Toast.LENGTH_SHORT);
					
					toast.show();

					return;		
				}
								
				String toString = strokeCountToEditText.getCurrentText();
				
				final Integer toInt = getInt(toString, 99);
				
				if (toInt == null) {
					Toast toast = Toast.makeText(KanjiSearchStrokeCount.this, getString(R.string.kanji_search_stroke_count_to_incorrect, toString), Toast.LENGTH_SHORT);
					
					toast.show();

					return;		
				}
				
				if (fromInt.intValue() > toInt.intValue()) {
					Toast toast = Toast.makeText(KanjiSearchStrokeCount.this, getString(R.string.kanji_search_stroke_count_from_bigger_than_to, toString), Toast.LENGTH_SHORT);
					
					toast.show();

					return;							
				}

				// uruchomienie wyszukiwania
				Intent intent = new Intent(getApplicationContext(), KanjiSearchStrokeCountResult.class);

				intent.putExtra("from", fromInt);
				intent.putExtra("to", toInt);

				startActivity(intent);
			}
			
			private Integer getInt(String textString, int defaultValue) {
				
				if (textString.equals("") == true) {
					return defaultValue;
				}
								
				try {
					return Integer.parseInt(textString);
				} catch (NumberFormatException e) {
					return null;
				}
			}
		});
	}

	public void onBackPressed() {
		super.onBackPressed();

		finish();
	}

	private void fillMainLayout(List<IScreenItem> screenItems, LinearLayout mainLayout) {
		
		for (IScreenItem currentScreenItem : screenItems) {
			currentScreenItem.generate(this, getResources(), mainLayout);			
		}
	}

	private List<IScreenItem> generateScreen() {
		
		List<IScreenItem> result = new ArrayList<IScreenItem>();
		
		result.add(new TitleItem(getString(R.string.kanji_search_stroke_count_title), 0));
		
		result.add(new TitleItem(getString(R.string.kanji_search_stroke_count_from), 1));
		
		strokeCountFromEditText = new EditText("1", 1, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
			
		result.add(strokeCountFromEditText);
		
		result.add(new TitleItem(getString(R.string.kanji_search_stroke_count_to), 1));
		
		strokeCountToEditText = new EditText("25", 1, InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
		
		result.add(strokeCountToEditText);
		
		return result;
	}
}
