package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.example.ExampleManager;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.example.dto.ExampleResult;
import pl.idedyk.android.japaneselearnhelper.gramma.GrammaConjugaterManager;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateGroupTypeElements;
import pl.idedyk.android.japaneselearnhelper.gramma.dto.GrammaFormConjugateResult;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
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
								
				Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString()); 
				
				startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));
			}
		});
	}

	private List<IScreenItem> generateDetails(DictionaryEntry dictionaryEntry) {
		
		List<IScreenItem> report = new ArrayList<IScreenItem>();
		
		String prefix = dictionaryEntry.getPrefix();
		
		if (prefix != null && prefix.length() == 0) {
			prefix = null;
		}

		// Kanji		
		report.add(new TitleItem(getString(R.string.word_dictionary_details_kanji_label), 0));
		
		StringBuffer kanjiSb = new StringBuffer();
		
		if (dictionaryEntry.isKanjiExists() == true) {
			if (prefix != null) {
				kanjiSb.append("(").append(prefix).append(") ");
			}
			
			kanjiSb.append(dictionaryEntry.getKanji());
		} else {
			kanjiSb.append("-");
		}
		
		report.add(new StringValue(kanjiSb.toString(), 35.0f, 0));
		
		// Reading
		report.add(new TitleItem(getString(R.string.word_dictionary_details_reading_label), 0));
		
		List<String> kanaList = dictionaryEntry.getKanaList();
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		for (int idx = 0; idx < kanaList.size(); ++idx) {
			
			StringBuffer sb = new StringBuffer();
			
			if (prefix != null) {
				sb.append("(").append(prefix).append(") ");
			}
			
			sb.append(kanaList.get(idx)).append(" - ").append(romajiList.get(idx));
						
			report.add(new StringValue(sb.toString(), 20.0f, 0));
		}
		
		// Translate
		report.add(new TitleItem(getString(R.string.word_dictionary_details_translate_label), 0));
		
		List<String> translates = dictionaryEntry.getTranslates();
		
		for (int idx = 0; idx < translates.size(); ++idx) {
			report.add(new StringValue(translates.get(idx), 20.0f, 0));
		}
		
		// Additional info
		report.add(new TitleItem(getString(R.string.word_dictionary_details_additional_info_label), 0));
		
		String info = dictionaryEntry.getInfo();
		
		if (info != null && info.length() > 0) {
			report.add(new StringValue(info, 20.0f, 0));
		} else {
			report.add(new StringValue("-", 20.0f, 0));
		}
		
		// Word type
		boolean addableDictionaryEntryTypeInfo = dictionaryEntry.isAddableDictionaryEntryTypeInfo();
		
		if (addableDictionaryEntryTypeInfo == true) {
			report.add(new TitleItem(getString(R.string.word_dictionary_details_part_of_speech), 0));
			
			report.add(new StringValue(dictionaryEntry.getDictionaryEntryType().getName(), 20.0f, 0));			
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
					
					addGrammaFormConjugateResult(report, prefix, currentGrammaFormConjugateResult);
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
				
				List<ExampleResult> exampleResults = currentExampleGroupTypeElements.getExampleResults();
				
				for (ExampleResult currentExampleResult : exampleResults) {					
					addExampleResult(report, prefix, currentExampleResult);
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
	
	private void addGrammaFormConjugateResult(List<IScreenItem> report, String prefix, GrammaFormConjugateResult grammaFormConjugateResult) {
		
		String grammaFormKanji = grammaFormConjugateResult.getKanji();
		
		StringBuffer grammaFormKanjiSb = new StringBuffer();
		
		if (grammaFormKanji != null) {
			if (prefix != null) {
				grammaFormKanjiSb.append("(").append(prefix).append(") ");
			}
			
			grammaFormKanjiSb.append(grammaFormKanji);
			
			report.add(new StringValue(grammaFormKanjiSb.toString(), 15.0f, 2));
		}
		
		List<String> grammaFormKanaList = grammaFormConjugateResult.getKanaList();
		List<String> grammaFormRomajiList = grammaFormConjugateResult.getRomajiList();
		
		for (int idx = 0; idx < grammaFormKanaList.size(); ++idx) {
			
			StringBuffer sb = new StringBuffer();
			
			if (prefix != null) {
				sb.append("(").append(prefix).append(") ");
			}
			
			sb.append(grammaFormKanaList.get(idx));
						
			report.add(new StringValue(sb.toString(), 15.0f, 2));
			report.add(new StringValue(grammaFormRomajiList.get(idx), 15.0f, 2));
		}
		
		GrammaFormConjugateResult alternative = grammaFormConjugateResult.getAlternative();
		
		if (alternative != null) {
			report.add(new StringValue("", 5.0f, 1));
			
			addGrammaFormConjugateResult(report, prefix, alternative);	
		}		
	}
	
	private void addExampleResult(List<IScreenItem> report, String prefix, ExampleResult exampleResult) {
		
		String exampleKanji = exampleResult.getKanji();
		
		StringBuffer exampleKanjiSb = new StringBuffer();
		
		if (exampleKanji != null) {
			if (prefix != null && exampleResult.isCanAddPrefix() == true) {
				exampleKanjiSb.append("(").append(prefix).append(") ");
			}
			
			exampleKanjiSb.append(exampleKanji);
			
			report.add(new StringValue(exampleKanjiSb.toString(), 15.0f, 2));
		}
		
		List<String> exampleKanaList = exampleResult.getKanaList();
		List<String> exampleRomajiList = exampleResult.getRomajiList();
		
		for (int idx = 0; idx < exampleKanaList.size(); ++idx) {
			
			StringBuffer sb = new StringBuffer();
			
			if (prefix != null && exampleResult.isCanAddPrefix() == true) {
				sb.append("(").append(prefix).append(") ");
			}
			
			sb.append(exampleKanaList.get(idx));
						
			report.add(new StringValue(sb.toString(), 15.0f, 2));
			report.add(new StringValue(exampleRomajiList.get(idx), 15.0f, 2));
		}
		
		ExampleResult alternative = exampleResult.getAlternative();
		
		if (alternative != null) {
			report.add(new StringValue("", 5.0f, 1));
			
			addExampleResult(report, prefix, alternative);	
		}		
	}
}
