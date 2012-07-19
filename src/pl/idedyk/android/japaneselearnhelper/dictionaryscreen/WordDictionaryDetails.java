package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

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
		
		/*
		TextView kanjiValue = (TextView)findViewById(R.id.word_dictionary_details_kanji_value);
		
		kanjiValue.setText(dictionaryEntry.getFullKanji());
		
		
		TextView kanaValue = (TextView)findViewById(R.id.word_dictionary_details_kana_value);
		
		kanaValue.setText(dictionaryEntry.getKanaList().toString());
		*/
		
		LinearLayout detailsMainLayout = (LinearLayout)findViewById(R.id.word_dictionary_details_main_layout);
		
		List<IWordDictionaryDetailsReportItem> generatedDetails = generateDetails(dictionaryEntry);
		
		fillDetailsMainLayout(generatedDetails, detailsMainLayout);
	}

	private List<IWordDictionaryDetailsReportItem> generateDetails(DictionaryEntry dictionaryEntry) {
		
		List<IWordDictionaryDetailsReportItem> report = new ArrayList<IWordDictionaryDetailsReportItem>();
		
		String prefix = dictionaryEntry.getPrefix();
		
		if (prefix != null && prefix.length() == 0) {
			prefix = null;
		}

		// Kanji		
		report.add(new WordDictionaryDetailsReportTitleItem(getString(R.string.word_dictionary_details_kanji_label)));
		
		StringBuffer kanjiSb = new StringBuffer();
		
		if (dictionaryEntry.isKanjiExists() == true) {
			if (prefix != null) {
				kanjiSb.append("(").append(prefix).append(") ");
			}
			
			kanjiSb.append(dictionaryEntry.getKanji());
		} else {
			kanjiSb.append("-");
		}
		
		report.add(new WordDictionaryDetailsStringValue(kanjiSb.toString(), 35.0f));
		
		// Reading
		report.add(new WordDictionaryDetailsReportTitleItem(getString(R.string.word_dictionary_details_reading_label)));
		
		List<String> kanaList = dictionaryEntry.getKanaList();
		List<String> romajiList = dictionaryEntry.getRomajiList();
		
		for (int idx = 0; idx < kanaList.size(); ++idx) {
			
			StringBuffer sb = new StringBuffer();
			
			if (prefix != null) {
				sb.append("(").append(prefix).append(") ");
			}
			
			sb.append(kanaList.get(idx)).append(" - ").append(romajiList.get(idx));
						
			report.add(new WordDictionaryDetailsStringValue(sb.toString(), 20.0f));
		}
		
		// Translate
		report.add(new WordDictionaryDetailsReportTitleItem(getString(R.string.word_dictionary_details_translate_label)));
		
		List<String> translates = dictionaryEntry.getTranslates();
		
		for (int idx = 0; idx < translates.size(); ++idx) {
			report.add(new WordDictionaryDetailsStringValue(translates.get(idx), 20.0f));
		}
		
		// Additional info
		report.add(new WordDictionaryDetailsReportTitleItem(getString(R.string.word_dictionary_details_additional_info_label)));
		
		String info = dictionaryEntry.getInfo();
		
		if (info != null && info.length() > 0) {
			report.add(new WordDictionaryDetailsStringValue(info, 20.0f));
		} else {
			report.add(new WordDictionaryDetailsStringValue("-", 20.0f));
		}
		
		// FIXME: dodac typ
		
		return report;
	}
	
	private void fillDetailsMainLayout(List<IWordDictionaryDetailsReportItem> generatedDetails, LinearLayout detailsMainLayout) {
		
		for (IWordDictionaryDetailsReportItem currentDetailsReportItem : generatedDetails) {
			
			currentDetailsReportItem.generate(this, getResources(), detailsMainLayout);			
		}
	}
	
	private static interface IWordDictionaryDetailsReportItem {
		public void generate(Context context, Resources resources, LinearLayout layout);
	}
	
	private static class WordDictionaryDetailsReportTitleItem implements IWordDictionaryDetailsReportItem {

		private String title;
		
		public WordDictionaryDetailsReportTitleItem(String title) {
			this.title = title;
		}

		public void generate(Context context, Resources resources, LinearLayout layout) {
			TextView textView = new TextView(context);
			
			textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			textView.setBackgroundColor(resources.getColor(R.color.word_dictionary_details_title_background_color));
			textView.setTextSize(16.0f);
			textView.setText(title);		
			
			layout.addView(textView);
		}
	}
	
	private static class WordDictionaryDetailsStringValue implements IWordDictionaryDetailsReportItem {
		
		private String value;
		
		private float textSize;
		
		public WordDictionaryDetailsStringValue(String value, float textSize) {
			this.value = value;
			this.textSize = textSize;
		}

		public void generate(Context context, Resources resources, LinearLayout layout) {
			TextView textView = new TextView(context);
			
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParam.setMargins(20, 5, 0, 0);
			
			textView.setLayoutParams(layoutParam);
			
			textView.setTextSize(textSize);
			textView.setText(value);
			
			layout.addView(textView);			
		}
	}
}
