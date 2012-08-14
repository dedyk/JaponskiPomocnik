package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
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

public class KanjiDetails extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.kanji_details);
		
		KanjiEntry kanjiEntry = (KanjiEntry)getIntent().getSerializableExtra("item");
		
		LinearLayout detailsMainLayout = (LinearLayout)findViewById(R.id.kanji_details_main_layout);
		
		final List<IScreenItem> generatedDetails = generateDetails(kanjiEntry);
		
		fillDetailsMainLayout(generatedDetails, detailsMainLayout);
		
		Button reportProblemButton = (Button)findViewById(R.id.kanji_details_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer detailsSb = new StringBuffer();
				
				for (IScreenItem currentGeneratedDetails : generatedDetails) {
					detailsSb.append(currentGeneratedDetails.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kanji_details_report_problem_email_subject);
				
				String mailBody = getString(R.string.kanji_details_report_problem_email_body,
						detailsSb.toString());				
								
				Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString()); 
				
				startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));
			}
		});
	}
	
	private void fillDetailsMainLayout(List<IScreenItem> generatedDetails, LinearLayout detailsMainLayout) {
		
		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), detailsMainLayout);			
		}
	}

	private List<IScreenItem> generateDetails(KanjiEntry kanjiEntry) {
		
		List<IScreenItem> report = new ArrayList<IScreenItem>();

		// Kanji		
		report.add(new TitleItem(getString(R.string.kanji_details_kanji_label), 0));
		
		report.add(new StringValue(kanjiEntry.getKanji(), 35.0f, 0));
		
		// Stroke count
		report.add(new TitleItem(getString(R.string.kanji_details_stroke_count_label), 0));
		
		report.add(new StringValue(String.valueOf(kanjiEntry.getKanjiDic2Entry().getStrokeCount()), 20.0f, 0));
		
		// Radicals
		report.add(new TitleItem(getString(R.string.kanji_details_radicals), 0));
		
		List<String> radicals = kanjiEntry.getKanjiDic2Entry().getRadicals();
		
		for (String currentRadical : radicals) {
			report.add(new StringValue(currentRadical, 20.0f, 0));
		}
		
		// Kun reading
		report.add(new TitleItem(getString(R.string.kanji_details_kun_reading), 0));
		
		List<String> kunReading = kanjiEntry.getKanjiDic2Entry().getKunReading();
		
		for (String currentKun : kunReading) {
			report.add(new StringValue(currentKun, 20.0f, 0));
		}
		
		// On reading
		report.add(new TitleItem(getString(R.string.kanji_details_on_reading), 0));
		
		List<String> onReading = kanjiEntry.getKanjiDic2Entry().getOnReading();
		
		for (String currentOn : onReading) {
			report.add(new StringValue(currentOn, 20.0f, 0));
		}
		
		// Translate
		report.add(new TitleItem(getString(R.string.kanji_details_translate_label), 0));
		
		List<String> translates = kanjiEntry.getPolishTranslates();
		
		for (int idx = 0; idx < translates.size(); ++idx) {
			report.add(new StringValue(translates.get(idx), 20.0f, 0));
		}
		
		// Additional info
		report.add(new TitleItem(getString(R.string.kanji_details_additional_info_label), 0));
		
		String info = kanjiEntry.getInfo();
		
		if (info != null && info.length() > 0) {
			report.add(new StringValue(info, 20.0f, 0));
		} else {
			report.add(new StringValue("-", 20.0f, 0));
		}
		

		
		
		
		return report;
	}
}
