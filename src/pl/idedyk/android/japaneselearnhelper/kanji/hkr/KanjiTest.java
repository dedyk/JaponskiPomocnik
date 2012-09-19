package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.KanjiTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanjiTestContext.DictionaryEntryWithRemovedKanji;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class KanjiTest extends Activity {
	
	//private LinearLayout kanjiInfoLinearLayout;
	private TextView kanjiInfoTextView;
	
	private KanjiDrawView drawView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.kanji_test);
		
		//kanjiInfoLinearLayout = (LinearLayout)findViewById(R.id.kanji_test_info_linearlayout);
		kanjiInfoTextView = (TextView)findViewById(R.id.kanji_test_info_textview);
		
		drawView = (KanjiDrawView) findViewById(R.id.kanji_test_recognizer_draw_view);

		Button undoButton = (Button) findViewById(R.id.kanji_test_recognizer_undo_button);

		undoButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				drawView.removeLastStroke();
			}
		});

		Button clearButton = (Button) findViewById(R.id.kanji_test_recognizer_clear_button);

		clearButton.setOnClickListener(new View.OnClickListener() {

			public void onClick(View v) {
				drawView.clear();
			}
		});
		
		setScreen();
	}
	
	private void setScreen() {
		
		JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanjiTestContext();
		
		KanjiTestConfig kanjiTestConfig = ConfigManager.getInstance().getKanjiTestConfig();
		
		int testCurrentPos = kanjiTestContext.getCurrentPos();
		
		// FIXME: check if finish
		
		// set info value
		setInfoValue(kanjiTestContext, kanjiTestConfig, testCurrentPos);
	}
	
	private void setInfoValue(JapaneseAndroidLearnHelperKanjiTestContext kanjiTestContext,
			KanjiTestConfig kanjiTestConfig, int testCurrentPos) {
		
		KanjiTestMode kanjiTestMode = kanjiTestConfig.getKanjiTestMode();
		
		if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_FROM_MEANING) {
			
			List<KanjiEntry> kanjiEntryList = kanjiTestContext.getKanjiEntryList();
			
			KanjiEntry currentTestKanjiEntry = kanjiEntryList.get(testCurrentPos);
			
			List<String> polishTranslates = currentTestKanjiEntry.getPolishTranslates();
			String info = currentTestKanjiEntry.getInfo();
			
			StringBuffer kanjiInfoSb = new StringBuffer();
			
			kanjiInfoSb.append("<b><big>").append(polishTranslates.toString()).append("</big></b>");
			
			if (info != null && info.equals("") == false) {
				kanjiInfoSb.append(" - ").append(info);
			}				
			
			kanjiInfoTextView.setText(Html.fromHtml(getString(R.string.kanji_test_info_meaning, kanjiInfoSb.toString())), TextView.BufferType.SPANNABLE);
			
			
		} else if (kanjiTestMode == KanjiTestMode.DRAW_KANJI_IN_WORD) {
			
			List<DictionaryEntryWithRemovedKanji> dictionaryEntryWithRemovedKanji = kanjiTestContext.getDictionaryEntryWithRemovedKanji();
			
			DictionaryEntryWithRemovedKanji currentDictionaryEntryWithRemovedKanji = dictionaryEntryWithRemovedKanji.get(testCurrentPos);
			
			String kanjiWithRemovedKanji = currentDictionaryEntryWithRemovedKanji.getKanjiWithRemovedKanji();
			DictionaryEntry dictionaryEntry = currentDictionaryEntryWithRemovedKanji.getDictionaryEntry();
			
			List<String> kanaList = dictionaryEntry.getKanaList();
			List<String> translates = dictionaryEntry.getTranslates();
			String info = dictionaryEntry.getInfo();
			
			StringBuffer kanjiInfoSb = new StringBuffer();
			
			kanjiInfoSb.append(getString(R.string.kanji_test_info_kanji_in_word)).append("<br/><br/>");
			
			kanjiInfoSb.append("<b><big>").append(kanjiWithRemovedKanji).append("</big></b>");
			kanjiInfoSb.append(" ").append(kanaList).append(" - ");
			kanjiInfoSb.append(translates);
			
			if (info != null && info.equals("") == false) {
				kanjiInfoSb.append(" - ").append(info);
			}					
			
			kanjiInfoTextView.setText(Html.fromHtml(kanjiInfoSb.toString()), TextView.BufferType.SPANNABLE);
			
		} else {
			throw new RuntimeException("KanjiTestMode kanjiTestMode");			
		}
	}
}
