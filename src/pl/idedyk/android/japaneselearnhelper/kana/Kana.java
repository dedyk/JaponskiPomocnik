package pl.idedyk.android.japaneselearnhelper.kana;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.KanaHelper;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Gravity;
import android.widget.TableLayout;

public class Kana extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_kana);
		
		TableLayout mainTableLayout = (TableLayout)findViewById(R.id.word_kana_main_layout);
		
		List<IScreenItem> screenItems = new ArrayList<IScreenItem>();
		
		Map<String, KanaEntry> kanaCache = KanaHelper.getKanaCache();
		
		generateHiraganaTable(screenItems, kanaCache);
		
		fillMainLayout(screenItems, mainTableLayout);
	}
	
	private void generateHiraganaTable(List<IScreenItem> screenItems, Map<String, KanaEntry> kanaCache) {
		
		screenItems.add(new TitleItem(getString(R.string.word_kana_hiragana_label)));
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow a_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(a_tableRow, kanaCache, "あ");
		addKanaItem(a_tableRow, kanaCache, "い");
		addKanaItem(a_tableRow, kanaCache, "う");
		addKanaItem(a_tableRow, kanaCache, "え");
		addKanaItem(a_tableRow, kanaCache, "お");
		
		screenItems.add(a_tableRow);
		
		pl.idedyk.android.japaneselearnhelper.screen.TableRow k_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(k_tableRow, kanaCache, "か");
		addKanaItem(k_tableRow, kanaCache, "き");
		addKanaItem(k_tableRow, kanaCache, "く");
		addKanaItem(k_tableRow, kanaCache, "け");
		addKanaItem(k_tableRow, kanaCache, "こ");
				
		screenItems.add(k_tableRow);

		pl.idedyk.android.japaneselearnhelper.screen.TableRow s_tableRow = new pl.idedyk.android.japaneselearnhelper.screen.TableRow();
		
		addKanaItem(s_tableRow, kanaCache, "さ");
		addKanaItem(s_tableRow, kanaCache, "し");
		addKanaItem(s_tableRow, kanaCache, "す");
		addKanaItem(s_tableRow, kanaCache, "せ");
		addKanaItem(s_tableRow, kanaCache, "そ");
						
		screenItems.add(s_tableRow);
	}
	
	private void addKanaItem(pl.idedyk.android.japaneselearnhelper.screen.TableRow tableRow, Map<String, KanaEntry> kanaCache, String kana) {
		
		KanaEntry kanaEntry = kanaCache.get(kana);
		
		if (kanaEntry == null) {
			throw new RuntimeException();
		}
		
		Spanned spanned = Html.fromHtml("<b>" + kanaEntry.getKana() + "</b><br/>" + kanaEntry.getKanaJapanese() + "<br/>");
		
		StringValue stringValue = new StringValue(spanned, 20);
		
		stringValue.setGravity(Gravity.CENTER);
		
		tableRow.addScreenItem(stringValue);		
	}
	
	private void fillMainLayout(List<IScreenItem> generatedDetails, TableLayout mainTableLayout) {
		
		for (IScreenItem currentDetailsReportItem : generatedDetails) {
			currentDetailsReportItem.generate(this, getResources(), mainTableLayout);			
		}
	}
}
