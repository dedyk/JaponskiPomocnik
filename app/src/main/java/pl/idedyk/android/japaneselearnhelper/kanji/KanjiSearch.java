package pl.idedyk.android.japaneselearnhelper.kanji;

import pl.idedyk.android.japaneselearnhelper.R;
import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

@SuppressWarnings("deprecation")
public class KanjiSearch extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.kanji_search);

		TabHost tabHost = getTabHost();

		// radical search
		Intent kanjiSearchRadicalIntent = new Intent().setClass(this, KanjiSearchRadical.class);

		TabSpec kanjiSearchRadicalTabSpec = tabHost.newTabSpec("KanjiSearchRadical")
				.setIndicator(getString(R.string.kanji_search_type_radical)).setContent(kanjiSearchRadicalIntent);

		tabHost.addTab(kanjiSearchRadicalTabSpec);

		// kanji search stroke count
		Intent kanjiSearchStrokeCountIntent = new Intent().setClass(this, KanjiSearchStrokeCount.class);

		TabSpec kanjiSearchStrokeCountTabSpec = tabHost.newTabSpec("KanjiSearchStrokeCount")
				.setIndicator(getString(R.string.kanji_search_stroke_count)).setContent(kanjiSearchStrokeCountIntent);

		tabHost.addTab(kanjiSearchStrokeCountTabSpec);

		// kanji search meaning
		Intent kanjiSearchMeaningIntent = new Intent().setClass(this, KanjiSearchMeaning.class);

		TabSpec kanjiSearchMeaningTabSpec = tabHost.newTabSpec("KanjiSearchMeaning")
				.setIndicator(getString(R.string.kanji_search_meaning)).setContent(kanjiSearchMeaningIntent);

		tabHost.addTab(kanjiSearchMeaningTabSpec);

		tabHost.setCurrentTab(0);
	}
}