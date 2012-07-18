package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;

public class WordDictionaryDetails extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		
		DictionaryEntry dictionaryEntry = (DictionaryEntry)getIntent().getSerializableExtra("item");
		
		Log.d("Test", "ZZZZZZZZ: " + dictionaryEntry.getTranslates().get(0));
	}
}
