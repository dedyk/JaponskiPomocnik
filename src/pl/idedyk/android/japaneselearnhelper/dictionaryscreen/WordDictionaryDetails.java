package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class WordDictionaryDetails extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_dictionary_details);
		
		DictionaryEntry dictionaryEntry = (DictionaryEntry)getIntent().getSerializableExtra("item");
		
		
		TextView kanjiValue = (TextView)findViewById(R.id.word_dictionary_details_kanji_value);
		
		kanjiValue.setText(dictionaryEntry.getFullKanji());
		
		
		TextView kanaValue = (TextView)findViewById(R.id.word_dictionary_details_kana_value);
		
		kanaValue.setText(dictionaryEntry.getKanaList().toString());
		
	}

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
}
