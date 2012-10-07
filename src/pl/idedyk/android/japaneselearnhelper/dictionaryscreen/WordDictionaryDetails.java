package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntryType;
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
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import pl.idedyk.android.japaneselearnhelper.sod.SodActivity;
import pl.idedyk.android.japaneselearnhelper.sod.dto.StrokePathInfo;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class WordDictionaryDetails extends Activity {

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	@Override
	protected void onDestroy() {
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
		
		StringValue kanjiStringValue = new StringValue(kanjiSb.toString(), 35.0f, 0);
		
		if (addKanjiWrite == true) {
			report.add(new StringValue(getString(R.string.word_dictionary_word_anim), 12.0f, 0));
			
			kanjiStringValue.setOnClickListener(new OnClickListener() {
				
				public void onClick(View v) {
					List<List<String>> strokePathsForWord = DictionaryManager.getInstance().getStrokePathsForWord(kanjiSb.toString());
					
					StrokePathInfo strokePathInfo = new StrokePathInfo();
					
					strokePathInfo.setStrokePaths(strokePathsForWord);
					
					Intent intent = new Intent(getApplicationContext(), SodActivity.class);
										
					intent.putExtra("strokePathsInfo", strokePathInfo);
					intent.putExtra("annotateStrokes", false);
					
					startActivity(intent);
				}
			});			
		}
				
		report.add(kanjiStringValue);
		
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
			
			report.add(readingStringValue);
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
					
					addGrammaFormConjugateResult(report, prefixKana, prefixRomaji, currentGrammaFormConjugateResult);
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
					addExampleResult(report, prefixKana, prefixRomaji, currentExampleResult);
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
	
	private void addGrammaFormConjugateResult(List<IScreenItem> report, String prefixKana, String prefixRomaji, GrammaFormConjugateResult grammaFormConjugateResult) {
		
		String grammaFormKanji = grammaFormConjugateResult.getKanji();
		
		StringBuffer grammaFormKanjiSb = new StringBuffer();
		
		if (grammaFormKanji != null) {
			if (prefixKana != null) {
				grammaFormKanjiSb.append("(").append(prefixKana).append(") ");
			}
			
			grammaFormKanjiSb.append(grammaFormKanji);
			
			report.add(new StringValue(grammaFormKanjiSb.toString(), 15.0f, 2));
		}
		
		List<String> grammaFormKanaList = grammaFormConjugateResult.getKanaList();
		List<String> grammaFormRomajiList = grammaFormConjugateResult.getRomajiList();
		
		for (int idx = 0; idx < grammaFormKanaList.size(); ++idx) {
			
			StringBuffer sb = new StringBuffer();
			
			if (prefixKana != null) {
				sb.append("(").append(prefixKana).append(") ");
			}
			
			sb.append(grammaFormKanaList.get(idx));
						
			report.add(new StringValue(sb.toString(), 15.0f, 2));
			
			StringBuffer grammaFormRomajiSb = new StringBuffer();
			
			if (prefixRomaji != null) {
				grammaFormRomajiSb.append("(").append(prefixRomaji).append(") ");
			}
			
			grammaFormRomajiSb.append(grammaFormRomajiList.get(idx));
			
			report.add(new StringValue(grammaFormRomajiSb.toString(), 15.0f, 2));
		}
		
		GrammaFormConjugateResult alternative = grammaFormConjugateResult.getAlternative();
		
		if (alternative != null) {
			report.add(new StringValue("", 5.0f, 1));
			
			addGrammaFormConjugateResult(report, prefixKana, prefixRomaji, alternative);	
		}		
	}
	
	private void addExampleResult(List<IScreenItem> report, String prefixKana, String prefixRomaji, ExampleResult exampleResult) {
		
		String exampleKanji = exampleResult.getKanji();
		
		StringBuffer exampleKanjiSb = new StringBuffer();
		
		if (exampleKanji != null) {
			if (prefixKana != null && exampleResult.isCanAddPrefix() == true) {
				exampleKanjiSb.append("(").append(prefixKana).append(") ");
			}
			
			exampleKanjiSb.append(exampleKanji);
			
			report.add(new StringValue(exampleKanjiSb.toString(), 15.0f, 2));
		}
		
		List<String> exampleKanaList = exampleResult.getKanaList();
		List<String> exampleRomajiList = exampleResult.getRomajiList();
		
		for (int idx = 0; idx < exampleKanaList.size(); ++idx) {
			
			StringBuffer sb = new StringBuffer();
			
			if (prefixKana != null && exampleResult.isCanAddPrefix() == true) {
				sb.append("(").append(prefixKana).append(") ");
			}
			
			sb.append(exampleKanaList.get(idx));
						
			report.add(new StringValue(sb.toString(), 15.0f, 2));
			
			StringBuffer exampleRomajiSb = new StringBuffer();

			if (prefixRomaji != null && exampleResult.isCanAddPrefix() == true) {
				exampleRomajiSb.append("(").append(prefixRomaji).append(") ");
			}
			
			exampleRomajiSb.append(exampleRomajiList.get(idx));
			
			report.add(new StringValue(exampleRomajiSb.toString(), 15.0f, 2));
		}
		
		ExampleResult alternative = exampleResult.getAlternative();
		
		if (alternative != null) {
			report.add(new StringValue("", 5.0f, 1));
			
			addExampleResult(report, prefixKana, prefixRomaji, alternative);	
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
}
