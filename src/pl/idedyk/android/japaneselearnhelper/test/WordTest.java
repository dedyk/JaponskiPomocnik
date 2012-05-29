package pl.idedyk.android.japaneselearnhelper.test;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import android.app.Activity;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class WordTest extends Activity {
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_test);
		
		Button testButton = (Button)findViewById(R.id.word_test_button_test);
		
		testButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				// findViewById(R.id.word_test_word_label2).setVisibility(View.VISIBLE);
				// findViewById(R.id.word_test_word_input2).setVisibility(View.VISIBLE);
				
				List<DictionaryEntry> wordDictionaryEntries = DictionaryManager.getInstance().getWordDictionaryEntries();
				
				DictionaryEntry dictionaryEntry = wordDictionaryEntries.get(413);
				
				EditText kanjiInput = (EditText)findViewById(R.id.word_test_kanji_input);
				kanjiInput.setText(dictionaryEntry.getFullKanji());
				kanjiInput.setEnabled(false);
				
				EditText wordInput1 = (EditText)findViewById(R.id.word_test_word_input1); // FIXME 
				wordInput1.setText(dictionaryEntry.getKanaList().get(0)); // FIXME
				
				EditText translateInput = (EditText)findViewById(R.id.word_test_translate_input); // FIXME 
				translateInput.setText("" + dictionaryEntry.getTranslates()); // FIXME
				translateInput.setEnabled(false);
				
				EditText additionalInfoInput = (EditText)findViewById(R.id.word_test_additional_info_input); // FIXME 
				additionalInfoInput.setText("" + dictionaryEntry.getInfo()); // FIXME
				additionalInfoInput.setEnabled(false);
				
				
				
			}
		});
		
		
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}
}
