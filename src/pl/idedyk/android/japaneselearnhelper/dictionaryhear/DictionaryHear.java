package pl.idedyk.android.japaneselearnhelper.dictionaryhear;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperDictionaryHearContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.android.japaneselearnhelper.tts.TtsConnector;
import pl.idedyk.android.japaneselearnhelper.tts.TtsLanguage;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class DictionaryHear extends Activity {
	
	private SpeakAsyncTask speakAsyncTask;
	
	private LinearLayout mainLayout = null;
	
	private TableLayout dictionaryEntryTableLayout = null;
	
	private StringValue dictionaryEntryTableLayout$kanji = null;
	private StringValue dictionaryEntryTableLayout$reading = null;
	private StringValue dictionaryEntryTableLayout$translate = null;
	private StringValue dictionaryEntryTableLayout$additionalInfo = null;
	private StringValue dictionaryEntryTableLayout$wordType = null;
	
	private StringValue stateStringValue = null;
	
	private Button startStopButton = null;
	
	private TtsConnector japanaeseTtsConnector = null;
	private TtsConnector polishTtsConnector = null;

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuShorterHelper.onCreateOptionsMenu(menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		stop();

		return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);

		setContentView(R.layout.dictionary_hear);
		
		mainLayout = (LinearLayout)findViewById(R.id.dictionary_hear_main_layout);
		
		startStopButton = (Button)findViewById(R.id.dictionary_hear_start_stop_button);
		
		List<IScreenItem> screenItemList = generateMainLayout(mainLayout);
		
		fillMainLayout(screenItemList, mainLayout);
		
		// set current dictionary entry
		JapaneseAndroidLearnHelperDictionaryHearContext dictionaryHearContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getDictionaryHearContext();
		
		List<DictionaryEntry> dictionaryEntryList = dictionaryHearContext.getDictionaryEntryList();
		int dictionaryEntryListIdx = dictionaryHearContext.getDictionaryEntryListIdx();
		
		if (dictionaryEntryListIdx >= dictionaryEntryList.size()) {
			dictionaryEntryListIdx = dictionaryEntryList.size() - 1;
		}
		
		setDictionaryEntry(dictionaryEntryList.get(dictionaryEntryListIdx), dictionaryEntryListIdx, dictionaryEntryList.size());		
		
		if (japanaeseTtsConnector != null) {
			japanaeseTtsConnector.stop();
		}
		
		if (polishTtsConnector != null) {
			polishTtsConnector.stop();
		}
		
		japanaeseTtsConnector = new TtsConnector(this, TtsLanguage.JAPANESE);
		polishTtsConnector = new TtsConnector(this, TtsLanguage.POLISH);
		
		// start stop action
		startStopButton.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				
				CharSequence startStopButtonText = startStopButton.getText();
				
				if (startStopButtonText.equals(getString(R.string.dictionary_hear_start)) == true) {
					start();
				} else {
					stop();
				}
			}
		});
		
		// report problem action
		Button reportProblemButton = (Button)findViewById(R.id.dictionary_hear_report_problem_button);

		reportProblemButton.setOnClickListener(new OnClickListener() {

			public void onClick(View view) {
				
				stop();

				String chooseEmailClientTitle = getString(R.string.choose_email_client);

				String mailSubject = getString(R.string.dictionary_hear_report_problem_email_subject);

				String mailBody = getString(R.string.dictionary_hear_report_problem_email_body);

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
	
	@Override
	protected void onDestroy() {
				
		if (speakAsyncTask != null) {
			speakAsyncTask.cancel(false);
			
			speakAsyncTask = null;
		}
		
		if (japanaeseTtsConnector != null) {
			japanaeseTtsConnector.stop();
		}
		
		if (polishTtsConnector != null) {
			polishTtsConnector.stop();
		}
		
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		
		if (speakAsyncTask != null) {
			speakAsyncTask.cancel(false);
			
			speakAsyncTask = null;
		}
		
		super.onPause();
	}

	private void fillMainLayout(List<IScreenItem> screenItemList, LinearLayout mainLayout) {
		
		for (IScreenItem currentReportItem : screenItemList) {
			currentReportItem.generate(this, getResources(), mainLayout);			
		}
	}
	
	private List<IScreenItem> generateMainLayout(LinearLayout mainLayout) {
		
		List<IScreenItem> screenItemList = new ArrayList<IScreenItem>();
		
		// Word		
		screenItemList.add(new TitleItem(getString(R.string.dictionary_hear_word), 0));

		dictionaryEntryTableLayout = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, true);
		
		// kanji
		TableRow kanjiTableRow = new TableRow();
		
		kanjiTableRow.addScreenItem(new StringValue(getString(R.string.dictionary_hear_word_kanji_label), 15.0f, 0));
		
		dictionaryEntryTableLayout$kanji = new StringValue("", 25.0f, 0);
		kanjiTableRow.addScreenItem(dictionaryEntryTableLayout$kanji);
		
		dictionaryEntryTableLayout.addTableRow(kanjiTableRow);
		
		// reading
		TableRow readingTableRow = new TableRow();
		
		readingTableRow.addScreenItem(new StringValue(getString(R.string.dictionary_hear_word_reading_label), 15.0f, 0));
		
		dictionaryEntryTableLayout$reading = new StringValue("", 20.0f, 0);
		readingTableRow.addScreenItem(dictionaryEntryTableLayout$reading);
		
		dictionaryEntryTableLayout.addTableRow(readingTableRow);
		
		// translate
		TableRow translateTableRow = new TableRow();
		
		translateTableRow.addScreenItem(new StringValue(getString(R.string.dictionary_hear_word_translate_label), 15.0f, 0));
		
		dictionaryEntryTableLayout$translate = new StringValue("", 20.0f, 0);
		translateTableRow.addScreenItem(dictionaryEntryTableLayout$translate);
		
		dictionaryEntryTableLayout.addTableRow(translateTableRow);
		
		// additional info
		TableRow additionalInfoTableRow = new TableRow();
		
		additionalInfoTableRow.addScreenItem(new StringValue(getString(R.string.dictionary_hear_word_additional_info_label), 15.0f, 0));
		
		dictionaryEntryTableLayout$additionalInfo = new StringValue("", 20.0f, 0);
		additionalInfoTableRow.addScreenItem(dictionaryEntryTableLayout$additionalInfo);
		
		dictionaryEntryTableLayout.addTableRow(additionalInfoTableRow);
		
		// word type
		TableRow wordTypeTableRow = new TableRow();
		
		wordTypeTableRow.addScreenItem(new StringValue(getString(R.string.dictionary_hear_word_part_of_speech), 15.0f, 0));
		
		dictionaryEntryTableLayout$wordType = new StringValue("", 20.0f, 0);
		wordTypeTableRow.addScreenItem(dictionaryEntryTableLayout$wordType);
		
		dictionaryEntryTableLayout.addTableRow(wordTypeTableRow);		
		
		screenItemList.add(dictionaryEntryTableLayout);
		
		JapaneseAndroidLearnHelperDictionaryHearContext dictionaryHearContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getDictionaryHearContext();
		
		stateStringValue = new StringValue(getString(R.string.dictionary_hear_state, 1, dictionaryHearContext.getDictionaryEntryList().size()), 12.0f, 0);
		
		stateStringValue.setGravity(Gravity.RIGHT);
		stateStringValue.setNullMargins(true);
		stateStringValue.setLayoutWidth(1);
		
		screenItemList.add(stateStringValue);
		
		return screenItemList;
	}
	
	private void start() {
		
		JapaneseAndroidLearnHelperDictionaryHearContext dictionaryHearContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getDictionaryHearContext();
		
		List<DictionaryEntry> dictionaryEntryList = dictionaryHearContext.getDictionaryEntryList();
		int dictionaryEntryListIdx = dictionaryHearContext.getDictionaryEntryListIdx();
		
		if (dictionaryEntryListIdx >= dictionaryEntryList.size() - 1) {
			dictionaryHearContext.setDictionaryEntryListIdx(0);
		}			
		
		speakAsyncTask = new SpeakAsyncTask();
		
		speakAsyncTask.execute();
	}
	
	private void stop() {
		
		if (speakAsyncTask != null) {
			speakAsyncTask.cancel(false);
		}
	}
	
	private void stopPost() {		
		startStopButton.setText(getString(R.string.dictionary_hear_start));
	}
	
	private void setDictionaryEntry(DictionaryEntry dictionaryEntry, int currentPos, int maxPos) {
		
		String prefixKana = dictionaryEntry.getPrefixKana();
		
		if (prefixKana != null && prefixKana.length() == 0) {
			prefixKana = null;
		}
		
		String prefixRomaji = dictionaryEntry.getPrefixRomaji();
		
		if (prefixRomaji != null && prefixRomaji.length() == 0) {
			prefixRomaji = null;
		}
		
		// kanji
		final StringBuffer kanjiSb = new StringBuffer();
				
		if (dictionaryEntry.isKanjiExists() == true) {
			if (prefixKana != null) {
				kanjiSb.append("(").append(prefixKana).append(") ");
			}
			
			kanjiSb.append(dictionaryEntry.getKanji());
		} else {
			kanjiSb.append("-");
		}

		dictionaryEntryTableLayout$kanji.setText(kanjiSb.toString());
		
		// reading
		List<String> romajiList = dictionaryEntry.getRomajiList();
		List<String> kanaList = dictionaryEntry.getKanaList();
		
		StringBuffer readingSb = new StringBuffer();
		
		for (int idx = 0; idx < kanaList.size(); ++idx) {
						
			if (prefixKana != null) {
				readingSb.append("(").append(prefixKana).append(") ");
			}
			
			readingSb.append(kanaList.get(idx)).append(" - ");
			
			if (prefixRomaji != null) {
				readingSb.append("(").append(prefixRomaji).append(") ");
			}
			
			readingSb.append(romajiList.get(idx));
			
			if (idx != kanaList.size() - 1) {
				readingSb.append("\n");
			}
		}
		
		dictionaryEntryTableLayout$reading.setText(readingSb.toString());
			
		// translate
		List<String> translates = dictionaryEntry.getTranslates();
		
		StringBuffer translateSb = new StringBuffer();
		
		for (int idx = 0; idx < translates.size(); ++idx) {
			translateSb.append(translates.get(idx));
			
			if (idx != translates.size() - 1) {
				translateSb.append("\n");
			}
		}
		
		dictionaryEntryTableLayout$translate.setText(translateSb.toString());
		
		// additional info
		String info = dictionaryEntry.getInfo();
		
		if (info != null && info.length() > 0) {
			dictionaryEntryTableLayout$additionalInfo.setText(info);
		} else {
			dictionaryEntryTableLayout$additionalInfo.setText("-");
		}
		
		// word type
		boolean addableDictionaryEntryTypeInfo = DictionaryEntryType.isAddableDictionaryEntryTypeInfo(dictionaryEntry.getDictionaryEntryType());
		
		if (addableDictionaryEntryTypeInfo == true) {
			dictionaryEntryTableLayout$wordType.setText(dictionaryEntry.getDictionaryEntryType().getName());
		} else {
			dictionaryEntryTableLayout$wordType.setText("");
		}
		
		stateStringValue.setText(getString(R.string.dictionary_hear_state, currentPos + 1, maxPos));
	}
	
	class SpeakAsyncTaskStatus {
		
		private DictionaryEntry dictionaryEntry;
		
		private int currentPos;
		private int maxPos;
		
		private Boolean cancel;

		private SpeakAsyncTaskStatus(DictionaryEntry dictionaryEntry, int currentPos, int maxPos) {
			this.dictionaryEntry = dictionaryEntry;
			
			this.currentPos = currentPos;
			this.maxPos = maxPos;
		}

		private SpeakAsyncTaskStatus(Boolean cancel) {
			this.cancel = cancel;
		}	
	}
	
	class SpeakAsyncTask extends AsyncTask<Void, SpeakAsyncTaskStatus, Void> {
				
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			startStopButton.setText(getString(R.string.dictionary_hear_stop));
		}
		
		@Override
		protected Void doInBackground(Void... params) {
			
			JapaneseAndroidLearnHelperDictionaryHearContext dictionaryHearContext = 
					JapaneseAndroidLearnHelperApplication.getInstance().getContext().getDictionaryHearContext();
			
			List<DictionaryEntry> dictionaryEntryList = dictionaryHearContext.getDictionaryEntryList();
			
			for (int dictionaryEntryListIdx = dictionaryHearContext.getDictionaryEntryListIdx();
					dictionaryEntryListIdx < dictionaryEntryList.size(); ++dictionaryEntryListIdx) {
				
				if (isCancelled() == true) {
					break;
				}
				
				DictionaryEntry currentDictionaryEntry = dictionaryEntryList.get(dictionaryEntryListIdx);
				
				publishProgress(new SpeakAsyncTaskStatus(currentDictionaryEntry, dictionaryEntryListIdx, dictionaryEntryList.size()));
				
				dictionaryHearContext.setDictionaryEntryListIdx(dictionaryEntryListIdx);
				
				List<String> kanaList = currentDictionaryEntry.getKanaList();
				
				StringBuffer japaneseTextSb = new StringBuffer();
				
				for (int kanaListIdx = 0; kanaListIdx < kanaList.size(); ++kanaListIdx) {
					japaneseTextSb.append(kanaList.get(kanaListIdx));
					
					if (kanaListIdx != kanaList.size() - 1) {
						japaneseTextSb.append("、");
					}
				}
				
				List<String> translates = currentDictionaryEntry.getTranslates();
				
				StringBuffer polishTranslateSb = new StringBuffer();
				
				for (int idx = 0; idx < translates.size(); ++idx) {
					polishTranslateSb.append(translates.get(idx));
					
					if (idx != translates.size() - 1) {
						polishTranslateSb.append(", ");
					} else {
						polishTranslateSb.append(". ");
					}
				}
				
				String info = currentDictionaryEntry.getInfo();

				if (info != null && info.length() > 0) {					
					polishTranslateSb.append(info).append(", ");
				}				
				
				japanaeseTtsConnector.speakAndWait(japaneseTextSb.toString());
				polishTtsConnector.speakAndWait(polishTranslateSb.toString());
			}
			
			return null;
		}

		@Override
		protected void onProgressUpdate(SpeakAsyncTaskStatus... values) {
			super.onProgressUpdate(values);
					
			DictionaryEntry dictionaryEntry = values[0].dictionaryEntry;
			
			if (dictionaryEntry != null) {
				setDictionaryEntry(dictionaryEntry, values[0].currentPos, values[0].maxPos);
			}
			
			Boolean cancel = values[0].cancel;
			
			if (cancel != null) {
				stopPost();
			}
		}

		@Override
		protected void onPostExecute(Void result) {
			stopPost();
		}

		@Override
		protected void onCancelled() {
			stopPost();
		}
	}
}