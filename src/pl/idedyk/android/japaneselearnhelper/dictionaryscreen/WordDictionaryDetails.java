package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.FuriganaEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.example.ExampleManager;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.GrammaConjugaterManager;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiDetails;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.Image;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.android.japaneselearnhelper.sod.SodActivity;
import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class WordDictionaryDetails extends Activity implements OnInitListener {
	
	private TextToSpeech textToSpeech = null;

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
		
		if (textToSpeech != null) {
			textToSpeech.stop();
			textToSpeech.shutdown();
		}
		
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onRestart() {
		super.onRestart();
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
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
		
		setContentView(R.layout.word_dictionary_details);
		
		DictionaryEntry dictionaryEntry = (DictionaryEntry)getIntent().getSerializableExtra("item");
				
		LinearLayout detailsMainLayout = (LinearLayout)findViewById(R.id.word_dictionary_details_main_layout);
		
		final List<IScreenItem> generatedDetails = generateDetails(dictionaryEntry);
		
		fillDetailsMainLayout(generatedDetails, detailsMainLayout);
		
		Button reportProblemButton = (Button)findViewById(R.id.word_dictionary_details_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer detailsSb = new StringBuffer();
				
				for (IScreenItem currentGeneratedDetails : generatedDetails) {
					detailsSb.append(currentGeneratedDetails.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.word_dictionary_details_report_problem_email_subject);
				
				String mailBody = getString(R.string.word_dictionary_details_report_problem_email_body,
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
		
		if (textToSpeech != null) {
			textToSpeech.stop();
			textToSpeech.shutdown();
		}
		
		textToSpeech = new TextToSpeech(this, this);
	}
	
	public void onInit(int status) {
		
		if (status == TextToSpeech.SUCCESS) {
			
			int result = textToSpeech.setLanguage(Locale.JAPANESE);
			
			if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
				
				if (textToSpeech != null) {
					textToSpeech.stop();
					textToSpeech.shutdown();
				}
				
				textToSpeech = null;
			} else {
				// success
			}
		} else {
			
			if (textToSpeech != null) {
				textToSpeech.stop();
				textToSpeech.shutdown();
			}
			
			textToSpeech = null;
		}
	}

	private List<IScreenItem> generateDetails(DictionaryEntry dictionaryEntry) {
		
		List<IScreenItem> report = new ArrayList<IScreenItem>();
				
		String prefixKana = dictionaryEntry.getPrefixKana();
		String prefixRomaji = dictionaryEntry.getPrefixRomaji();
		
		if (prefixKana != null && prefixKana.length() == 0) {
			prefixKana = null;
		}

		// Kanji		
		report.add(new TitleItem(getString(R.string.word_dictionary_details_kanji_label), 0));
		
		final StringBuffer kanjiSb = new StringBuffer();
		
		boolean addKanjiWrite = false;
		
		if (dictionaryEntry.isKanjiExists() == true) {
			if (prefixKana != null) {
				kanjiSb.append("(").append(prefixKana).append(") ");
			}
			
			kanjiSb.append(dictionaryEntry.getKanji());
			
			addKanjiWrite = true;
		} else {
			kanjiSb.append("-");
			
			addKanjiWrite = false;
		}
		
		// kanji draw on click listener
		OnClickListener kanjiDrawOnClickListener = new OnClickListener() {
			
			public void onClick(View v) {
				List<List<String>> strokePathsForWord = DictionaryManager.getInstance().getStrokePathsForWord(kanjiSb.toString());
				
				StrokePathInfo strokePathInfo = new StrokePathInfo();
				
				strokePathInfo.setStrokePaths(strokePathsForWord);
				
				Intent intent = new Intent(getApplicationContext(), SodActivity.class);
									
				intent.putExtra("strokePathsInfo", strokePathInfo);
				intent.putExtra("annotateStrokes", false);
				
				startActivity(intent);
			}
		};		
		
		// check furigana
		List<FuriganaEntry> furiganaEntries = DictionaryManager.getInstance().getFurigana(dictionaryEntry);
		
		if (furiganaEntries != null && furiganaEntries.size() > 0 && addKanjiWrite == true) {
			
			report.add(new StringValue(getString(R.string.word_dictionary_word_anim), 12.0f, 0));
						
			for (FuriganaEntry currentFuriganaEntry : furiganaEntries) {
				
				TableLayout furiganaTableLayout = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent, true, null);
				
				final int maxPartsInLine = 7;
				
				List<String> furiganaKanaParts = currentFuriganaEntry.getKanaPart();
				List<String> furiganaKanjiParts = currentFuriganaEntry.getKanjiPart();
				
				int maxParts = furiganaKanaParts.size() / maxPartsInLine + (furiganaKanaParts.size() % maxPartsInLine > 0 ? 1 : 0);
				
				for (int currentPart = 0; currentPart < maxParts; ++currentPart) {
					
					TableRow readingRow = new TableRow();
					
					StringValue spacer = new StringValue("", 15.0f, 0);
					spacer.setGravity(Gravity.CENTER);
					spacer.setNullMargins(true);
					
					readingRow.addScreenItem(spacer);
					
					for (int furiganaKanaPartsIdx = currentPart * maxPartsInLine; furiganaKanaPartsIdx < furiganaKanaParts.size() && furiganaKanaPartsIdx < (currentPart + 1) * maxPartsInLine; ++furiganaKanaPartsIdx) {
						
						StringValue currentKanaPartStringValue = new StringValue(furiganaKanaParts.get(furiganaKanaPartsIdx), 15.0f, 0);
						
						currentKanaPartStringValue.setGravity(Gravity.CENTER);
						currentKanaPartStringValue.setNullMargins(true);
						
						currentKanaPartStringValue.setOnClickListener(kanjiDrawOnClickListener);
						
						readingRow.addScreenItem(currentKanaPartStringValue);
					}
					
					furiganaTableLayout.addTableRow(readingRow);
					
					TableRow kanjiRow = new TableRow();
					
					StringValue spacer2 = new StringValue("  ", 25.0f, 0);
					spacer2.setGravity(Gravity.CENTER);
					spacer2.setNullMargins(true);
					
					kanjiRow.addScreenItem(spacer2);
					
					for (int furiganaKanjiPartsIdx = currentPart * maxPartsInLine; furiganaKanjiPartsIdx < furiganaKanjiParts.size() && furiganaKanjiPartsIdx < (currentPart + 1) * maxPartsInLine; ++furiganaKanjiPartsIdx) {
						StringValue currentKanjiPartStringValue = new StringValue(furiganaKanjiParts.get(furiganaKanjiPartsIdx), 35.0f, 0);
						
						currentKanjiPartStringValue.setGravity(Gravity.CENTER);
						currentKanjiPartStringValue.setNullMargins(true);
						
						currentKanjiPartStringValue.setOnClickListener(kanjiDrawOnClickListener);
						
						kanjiRow.addScreenItem(currentKanjiPartStringValue);
					}
					
					furiganaTableLayout.addTableRow(kanjiRow);
				}
								
				report.add(furiganaTableLayout);
				
				Image speakImage = new Image(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off), 0);
				
				speakImage.setOnClickListener(new TTSJapaneseSpeak(null, dictionaryEntry.getKanji()));
				
				report.add(speakImage);
			}
		} else {
			StringValue kanjiStringValue = new StringValue(kanjiSb.toString(), 35.0f, 0);
			
			if (addKanjiWrite == true) {
				report.add(new StringValue(getString(R.string.word_dictionary_word_anim), 12.0f, 0));
								
				kanjiStringValue.setOnClickListener(kanjiDrawOnClickListener);			
			}
			
			report.add(kanjiStringValue);
			
			if (addKanjiWrite == true) {
				Image speakImage = new Image(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off), 0);
				
				speakImage.setOnClickListener(new TTSJapaneseSpeak(dictionaryEntry.getPrefixKana(), dictionaryEntry.getKanji()));
				
				report.add(speakImage);
			}
		}	
				
		// Reading
		report.add(new TitleItem(getString(R.string.word_dictionary_details_reading_label), 0));
		report.add(new StringValue(getString(R.string.word_dictionary_word_anim), 12.0f, 0));
		
		List<String> kanaList = dictionaryEntry.getKanaList();
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		for (int idx = 0; idx < kanaList.size(); ++idx) {
			
			final StringBuffer sb = new StringBuffer();
			
			if (prefixKana != null) {
				sb.append("(").append(prefixKana).append(") ");
			}
			
			sb.append(kanaList.get(idx)).append(" - ");
			
			if (prefixRomaji != null) {
				sb.append("(").append(prefixRomaji).append(") ");
			}
			
			sb.append(romajiList.get(idx));
						
			StringValue readingStringValue = new StringValue(sb.toString(), 20.0f, 0);
			
			readingStringValue.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					List<List<String>> strokePathsForWord = DictionaryManager.getInstance().getStrokePathsForWord(sb.toString());
					
					StrokePathInfo strokePathInfo = new StrokePathInfo();
					
					strokePathInfo.setStrokePaths(strokePathsForWord);
					
					Intent intent = new Intent(getApplicationContext(), SodActivity.class);
										
					intent.putExtra("strokePathsInfo", strokePathInfo);
					intent.putExtra("annotateStrokes", false);
					
					startActivity(intent);					
				}
			});
					
			Image speakImage = new Image(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off), 0);
			
			speakImage.setOnClickListener(new TTSJapaneseSpeak(prefixKana, kanaList.get(idx)));

			report.add(readingStringValue);
			report.add(speakImage);
		}
				
		// Translate
		report.add(new TitleItem(getString(R.string.word_dictionary_details_translate_label), 0));
		
		List<String> translates = dictionaryEntry.getTranslates();
		
		for (int idx = 0; idx < translates.size(); ++idx) {
			report.add(new StringValue(translates.get(idx), 20.0f, 0));
		}
		
		// Additional info
		report.add(new TitleItem(getString(R.string.word_dictionary_details_additional_info_label), 0));
		
		if (isSmTsukiNiKawatteOshiokiYo(kanjiSb.toString()) == true) {
			report.add(createSmTsukiNiKawatteOshiokiYo());
		} else {
			String info = dictionaryEntry.getInfo();
			
			if (info != null && info.length() > 0) {
				report.add(new StringValue(info, 20.0f, 0));
			} else {
				report.add(new StringValue("-", 20.0f, 0));
			}
		}
				
		// Word type
		boolean addableDictionaryEntryTypeInfo = DictionaryEntryType.isAddableDictionaryEntryTypeInfo(dictionaryEntry.getDictionaryEntryType());
		
		if (addableDictionaryEntryTypeInfo == true) {
			report.add(new TitleItem(getString(R.string.word_dictionary_details_part_of_speech), 0));
			
			report.add(new StringValue(dictionaryEntry.getDictionaryEntryType().getName(), 20.0f, 0));			
		}
		
		List<KanjiEntry> knownKanji = null;
		
		if (dictionaryEntry.isKanjiExists() == true) {
			knownKanji = DictionaryManager.getInstance().findKnownKanji(dictionaryEntry.getKanji());
		}
		
		if (knownKanji != null && knownKanji.size() > 0) {
			
			report.add(new StringValue("", 15.0f, 2));
			report.add(new TitleItem(getString(R.string.word_dictionary_known_kanji), 0));
			report.add(new StringValue(getString(R.string.word_dictionary_known_kanji_info), 12.0f, 0));
			
			for (int knownKanjiIdx = 0; knownKanjiIdx < knownKanji.size(); ++knownKanjiIdx) {
				
				final KanjiEntry kanjiEntry = knownKanji.get(knownKanjiIdx);				
				
				OnClickListener kanjiOnClickListener = new OnClickListener() {
					
					public void onClick(View v) {
						
						// show kanji details
						
						Intent intent = new Intent(getApplicationContext(), KanjiDetails.class);
						
						intent.putExtra("item", kanjiEntry);
						
						startActivity(intent);
					}
				};
				
				StringValue knownKanjiStringValue = new StringValue(kanjiEntry.getKanji(), 16.0f, 1);
				StringValue polishTranslateStringValue = new StringValue(kanjiEntry.getPolishTranslates().toString(), 16.0f, 1);
				
				knownKanjiStringValue.setOnClickListener(kanjiOnClickListener);
				polishTranslateStringValue.setOnClickListener(kanjiOnClickListener);
											
				report.add(knownKanjiStringValue);
				report.add(polishTranslateStringValue);
				
				if (knownKanjiIdx != knownKanji.size() - 1) {
					report.add(new StringValue("", 10.0f, 1));
				}

			}
		}
				
		// Conjugater
		List<GrammaFormConjugateGroupTypeElements> grammaFormConjugateGroupTypeElementsList = GrammaConjugaterManager.getGrammaConjufateResult(dictionaryEntry);
		
		if (grammaFormConjugateGroupTypeElementsList != null) {
			report.add(new StringValue("", 15.0f, 2));
			report.add(new TitleItem(getString(R.string.word_dictionary_details_conjugater_label), 0));
			
			for (GrammaFormConjugateGroupTypeElements currentGrammaFormConjugateGroupTypeElements : 
				grammaFormConjugateGroupTypeElementsList) {
				
				report.add(new TitleItem(currentGrammaFormConjugateGroupTypeElements.getGrammaFormConjugateGroupType().getName(), 1));
				
				List<GrammaFormConjugateResult> grammaFormConjugateResults = currentGrammaFormConjugateGroupTypeElements.getGrammaFormConjugateResults();
				
				for (GrammaFormConjugateResult currentGrammaFormConjugateResult : grammaFormConjugateResults) {
					
					if (currentGrammaFormConjugateResult.getResultType().isShow() == true) {
						report.add(new TitleItem(currentGrammaFormConjugateResult.getResultType().getName(), 2));
					}
					
					addGrammaFormConjugateResult(report, currentGrammaFormConjugateResult);
				}
				
				report.add(new StringValue("", 15.0f, 1));
			}
		}	
		
		// Exampler
		List<ExampleGroupTypeElements> exampleGroupTypeElementsList = ExampleManager.getExamples(dictionaryEntry);
		
		if (exampleGroupTypeElementsList != null) {
			
			if (grammaFormConjugateGroupTypeElementsList == null) {
				report.add(new StringValue("", 15.0f, 2));	
			}
			
			report.add(new TitleItem(getString(R.string.word_dictionary_details_exampler_label), 0));
			
			for (ExampleGroupTypeElements currentExampleGroupTypeElements : exampleGroupTypeElementsList) {
				
				report.add(new TitleItem(currentExampleGroupTypeElements.getExampleGroupType().getName(), 1));
				
				String exampleGroupInfo = currentExampleGroupTypeElements.getExampleGroupType().getInfo(); 
				
				if (exampleGroupInfo != null) {
					report.add(new StringValue(exampleGroupInfo, 15.0f, 1));
				}
				
				List<ExampleResult> exampleResults = currentExampleGroupTypeElements.getExampleResults();
				
				for (ExampleResult currentExampleResult : exampleResults) {					
					addExampleResult(report, currentExampleResult);
				}
				
				report.add(new StringValue("", 15.0f, 1));
			}
		}
		
		return report;
	}
	
	private void fillDetailsMainLayout(List<IScreenItem> generatedDetails, LinearLayout detailsMainLayout) {
		
		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), detailsMainLayout);			
		}
	}
	
	private void addGrammaFormConjugateResult(List<IScreenItem> report, GrammaFormConjugateResult grammaFormConjugateResult) {
		
		String grammaFormKanji = grammaFormConjugateResult.getKanji();
		
		String prefixKana = grammaFormConjugateResult.getPrefixKana();
		String prefixRomaji = grammaFormConjugateResult.getPrefixRomaji();
		
		StringBuffer grammaFormKanjiSb = new StringBuffer();
						
		if (grammaFormKanji != null) {
			if (prefixKana != null && prefixKana.equals("") == false) {
				grammaFormKanjiSb.append("(").append(prefixKana).append(") ");
			}
			
			grammaFormKanjiSb.append(grammaFormKanji);
			
			report.add(new StringValue(grammaFormKanjiSb.toString(), 15.0f, 2));
		}
		
		List<String> grammaFormKanaList = grammaFormConjugateResult.getKanaList();
		List<String> grammaFormRomajiList = grammaFormConjugateResult.getRomajiList();
		
		for (int idx = 0; idx < grammaFormKanaList.size(); ++idx) {
			
			StringBuffer sb = new StringBuffer();
			
			if (prefixKana != null && prefixKana.equals("") == false) {
				sb.append("(").append(prefixKana).append(") ");
			}
			
			sb.append(grammaFormKanaList.get(idx));
						
			report.add(new StringValue(sb.toString(), 15.0f, 2));
						
			StringBuffer grammaFormRomajiSb = new StringBuffer();
			
			if (prefixRomaji != null && prefixRomaji.equals("") == false) {
				grammaFormRomajiSb.append("(").append(prefixRomaji).append(") ");
			}
			
			grammaFormRomajiSb.append(grammaFormRomajiList.get(idx));
			
			report.add(new StringValue(grammaFormRomajiSb.toString(), 15.0f, 2));
			
			Image speakImage = new Image(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off), 2);
			
			speakImage.setOnClickListener(new TTSJapaneseSpeak(prefixKana, grammaFormKanaList.get(idx)));
			
			report.add(speakImage);
		}
		
		GrammaFormConjugateResult alternative = grammaFormConjugateResult.getAlternative();
		
		if (alternative != null) {
			report.add(new StringValue("", 5.0f, 1));
			
			addGrammaFormConjugateResult(report, alternative);	
		}		
	}
	
	private void addExampleResult(List<IScreenItem> report, ExampleResult exampleResult) {
		
		String exampleKanji = exampleResult.getKanji();
		
		String prefixKana = exampleResult.getPrefixKana();
		
		String prefixRomaji = exampleResult.getPrefixRomaji();
		
		StringBuffer exampleKanjiSb = new StringBuffer();
		
		if (exampleKanji != null) {
			if (prefixKana != null && prefixKana.equals("") == false) {
				exampleKanjiSb.append("(").append(prefixKana).append(") ");
			}
			
			exampleKanjiSb.append(exampleKanji);
			
			report.add(new StringValue(exampleKanjiSb.toString(), 15.0f, 2));
		}
		
		List<String> exampleKanaList = exampleResult.getKanaList();
		List<String> exampleRomajiList = exampleResult.getRomajiList();
		
		for (int idx = 0; idx < exampleKanaList.size(); ++idx) {
			
			StringBuffer sb = new StringBuffer();
			
			if (prefixKana != null && prefixKana.equals("") == false) {
				sb.append("(").append(prefixKana).append(") ");
			}
			
			sb.append(exampleKanaList.get(idx));
						
			report.add(new StringValue(sb.toString(), 15.0f, 2));
						
			StringBuffer exampleRomajiSb = new StringBuffer();

			if (prefixRomaji != null && prefixRomaji.equals("") == false) {
				exampleRomajiSb.append("(").append(prefixRomaji).append(") ");
			}
			
			exampleRomajiSb.append(exampleRomajiList.get(idx));
			
			report.add(new StringValue(exampleRomajiSb.toString(), 15.0f, 2));
			
			Image speakImage = new Image(getResources().getDrawable(android.R.drawable.ic_lock_silent_mode_off), 2);
			
			speakImage.setOnClickListener(new TTSJapaneseSpeak(prefixKana, exampleKanaList.get(idx)));
			
			report.add(speakImage);
		}
		
		ExampleResult alternative = exampleResult.getAlternative();
		
		if (alternative != null) {
			report.add(new StringValue("", 5.0f, 1));
			
			addExampleResult(report, alternative);	
		}		
	}
	
	// special
	private boolean isSmTsukiNiKawatteOshiokiYo(String value) {
		
		if (value == null) {
			return false;
		}
		
		if (value.equals("月に代わって、お仕置きよ!") == true) {
			return true;
		}
		
		return false;
	}
	
	private StringValue createSmTsukiNiKawatteOshiokiYo() {
		
		StringValue smStringValue = new StringValue(getString(R.string.sm_tsuki_ni_kawatte_oshioki_yo), 2.8f, 0);
		
		smStringValue.setTypeface(Typeface.MONOSPACE);
		smStringValue.setTextColor(Color.BLACK);
		smStringValue.setBackgroundColor(Color.WHITE);
		smStringValue.setGravity(Gravity.CENTER);
		
		return smStringValue;
	}
	
	private class TTSJapaneseSpeak implements OnClickListener {

		private String prefix;
		
		private String kanjiKana;
		
		public TTSJapaneseSpeak(String prefix, String kanjiKana) {
			this.prefix = prefix;
			this.kanjiKana = kanjiKana;
		}
		
		public void onClick(View v) {
			
			StringBuffer text = new StringBuffer();
			
			if (prefix != null) {
				text.append(prefix);
			}
			
			if (kanjiKana != null) {
				text.append(kanjiKana);
			}			
			
			if (textToSpeech != null) {
				textToSpeech.speak(text.toString(), TextToSpeech.QUEUE_FLUSH, null);
			} else {
				AlertDialog alertDialog = new AlertDialog.Builder(WordDictionaryDetails.this).create();
				
				alertDialog.setMessage(getString(R.string.word_dictionary_details_tts_error));
				alertDialog.setCancelable(false);
				
				alertDialog.setButton(getString(R.string.word_dictionary_details_tts_error_ok), new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// noop
					}
				});
				
				alertDialog.show();
			}
		}
	}
}
