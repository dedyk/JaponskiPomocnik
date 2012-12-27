package pl.idedyk.android.japaneselearnhelper.test;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.WordTestConfig;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperWordTestContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.Utils;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.utils.ListUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class WordTest extends Activity {
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuShorterHelper.onCreateOptionsMenu(menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_test);
		
		fillScreen();
		
		Button nextButton = (Button)findViewById(R.id.word_test_next_button);
		
		nextButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				final JapaneseAndroidLearnHelperContext context = JapaneseAndroidLearnHelperApplication.getInstance().getContext();
				final JapaneseAndroidLearnHelperWordTestContext wordTestContext = context.getWordTestContext();
				final WordTestConfig wordTestConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(WordTest.this).getWordTestConfig();
				
				final List<DictionaryEntry> wordDictionaryEntries = wordTestContext.getWordsTest();
				int wordsTestIdx = wordTestContext.getWordsTestIdx();
				
				final DictionaryEntry currentWordDictionaryEntry = wordDictionaryEntries.get(wordsTestIdx);
				
				List<String> kanaList = currentWordDictionaryEntry.getKanaList();
				
				// check user answer
				int correctAnswersNo = getCorrectAnswersNo(context);
				
				wordTestContext.addWordTestAnswers(kanaList.size());
				wordTestContext.addWordTestCorrectAnswers(correctAnswersNo);
				wordTestContext.addWordTestIncorrentAnswers(kanaList.size() - correctAnswersNo);
				
				if (correctAnswersNo == kanaList.size()) {
					Toast.makeText(WordTest.this, getString(R.string.word_test_correct), Toast.LENGTH_SHORT).show();
					
					wordTestContext.incrementWordsTestIdx();				
					
					fillScreen();
				} else {					
					AlertDialog alertDialog = new AlertDialog.Builder(WordTest.this).create();
					
					alertDialog.setMessage(getString(R.string.word_test_incorrect) + 
							ListUtil.getListAsString(kanaList, "\n"));
					
					alertDialog.setCancelable(false);
					alertDialog.setButton(getString(R.string.word_test_incorrect_ok), new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
							
							if (wordTestConfig.getUntilSuccess() != null &&
									wordTestConfig.getUntilSuccess().equals(Boolean.TRUE)) {
								
								wordDictionaryEntries.add(currentWordDictionaryEntry);
							}
							
							wordTestContext.incrementWordsTestIdx();				
							
							fillScreen();
						}
					});
					
					alertDialog.show();					
				}				
			}
		});		
	}
	
	private int getCorrectAnswersNo(JapaneseAndroidLearnHelperContext context) {
		
		JapaneseAndroidLearnHelperWordTestContext wordTestContext = context.getWordTestContext();
		
		List<DictionaryEntry> wordDictionaryEntries = wordTestContext.getWordsTest();
		int currentWordsTextIdx = wordTestContext.getWordsTestIdx();
		
		DictionaryEntry currentWordDictionaryEntry = wordDictionaryEntries.get(currentWordsTextIdx);
		List<String> kanaList = currentWordDictionaryEntry.getKanaList();
		
		List<String> kanaListToRemove = new ArrayList<String>(kanaList);
		
		TextViewAndEditText[] textViewAndEditTextForWordAsArray = getTextViewAndEditTextForWordAsArray();
		
		for (int kanaListIdx = 0; kanaListIdx < kanaList.size(); ++kanaListIdx) {
			
			String currentUserAnswer = textViewAndEditTextForWordAsArray[kanaListIdx].editText.getText().toString();
			
			kanaListToRemove.remove(currentUserAnswer);
		}
		
		return kanaList.size() - kanaListToRemove.size();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		fillScreen();
	}
	
	private void fillScreen() {
		JapaneseAndroidLearnHelperContext context = JapaneseAndroidLearnHelperApplication.getInstance().getContext();
		
		final WordTestConfig wordTestConfig = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(WordTest.this).getWordTestConfig();
		
		JapaneseAndroidLearnHelperWordTestContext wordTestContext = context.getWordTestContext();
		
		List<DictionaryEntry> wordDictionaryEntries = wordTestContext.getWordsTest();
		int currentWordsTextIdx = wordTestContext.getWordsTestIdx();
		
		if (currentWordsTextIdx >= wordDictionaryEntries.size()) {
			
			Intent intent = new Intent(getApplicationContext(), WordTestSummary.class);
			
			startActivity(intent);
			
			finish();
			
		} else {
			DictionaryEntry currentWordDictionaryEntry = wordDictionaryEntries.get(currentWordsTextIdx);
			
			String kanji = currentWordDictionaryEntry.getKanji();
			
			TextView kanjiLabel = (TextView)findViewById(R.id.word_test_kanji_label);
			EditText kanjiInput = (EditText)findViewById(R.id.word_test_kanji_input);
			
			if (kanji != null && wordTestConfig.getShowKanji() != null && wordTestConfig.getShowKanji().equals(Boolean.TRUE) == true) {
				
				String kanjiText = "";
				
				String prefixKana = currentWordDictionaryEntry.getPrefixKana();
				
				if (prefixKana != null && prefixKana.equals("") == false) {
					kanjiText += "(" + prefixKana + ") ";
				}
				
				kanjiText += currentWordDictionaryEntry.getKanji();
				
				kanjiInput.setText(kanjiText);
				
				kanjiLabel.setVisibility(View.VISIBLE);
				kanjiInput.setVisibility(View.VISIBLE);
				kanjiInput.setEnabled(false);
			} else {
				kanjiInput.setText("");
				
				kanjiLabel.setVisibility(View.GONE);
				kanjiInput.setVisibility(View.GONE);
				kanjiInput.setEnabled(false);
			}
			
			List<String> kanaList = currentWordDictionaryEntry.getKanaList();
			
			if (kanaList.size() >= Utils.MAX_LIST_SIZE) {
				throw new RuntimeException("Kana list size: " + kanaList);
			}
			
			TextViewAndEditText[] textViewAndEditTextForWordAsArray = getTextViewAndEditTextForWordAsArray();
			
			for (int kanaListIdx = 0; kanaListIdx < textViewAndEditTextForWordAsArray.length; ++kanaListIdx) {
				
				TextViewAndEditText currentTextViewAndEditText = textViewAndEditTextForWordAsArray[kanaListIdx];
				
				String currentKana = null;
				
				if (kanaListIdx < kanaList.size()) {
					currentKana = kanaList.get(kanaListIdx);
				}
				
				if (currentKana != null && kanaListIdx == 0) {
					currentTextViewAndEditText.editText.requestFocus();
				}
				
				if (currentKana != null) {
					currentTextViewAndEditText.textView.setVisibility(View.VISIBLE);
					
					currentTextViewAndEditText.editText.setVisibility(View.VISIBLE);
					currentTextViewAndEditText.editText.setText("");
				} else {
					currentTextViewAndEditText.textView.setVisibility(View.GONE);
					
					currentTextViewAndEditText.editText.setVisibility(View.GONE);
					currentTextViewAndEditText.editText.setText("");
				}
			}
			
			EditText translateInput = (EditText)findViewById(R.id.word_test_translate_input); 
			translateInput.setText(ListUtil.getListAsString(currentWordDictionaryEntry.getTranslates(), "\n"));
			translateInput.setEnabled(false);
			
			TextView additionalInfoLabel = (TextView)findViewById(R.id.word_test_additional_info_label);
			EditText additionalInfoInput = (EditText)findViewById(R.id.word_test_additional_info_input);

			String additionalInfo = currentWordDictionaryEntry.getFullInfo();
			
			if (additionalInfo != null) {
				additionalInfoInput.setText(additionalInfo);
				additionalInfoInput.setEnabled(false);
				
				additionalInfoLabel.setVisibility(View.VISIBLE);
				additionalInfoInput.setVisibility(View.VISIBLE);
			} else {
				additionalInfoInput.setText("");
				additionalInfoInput.setEnabled(false);
				
				additionalInfoLabel.setVisibility(View.GONE);
				additionalInfoInput.setVisibility(View.GONE);			
			}
			
			TextView state = (TextView)findViewById(R.id.word_test_state_info);
			
			Resources resources = getResources();
			
			state.setText(resources.getString(R.string.word_test_state, (currentWordsTextIdx + 1), wordDictionaryEntries.size()));			
		}
	}
	
	private TextViewAndEditText[] getTextViewAndEditTextForWordAsArray() {
		
		TextView wordLabel1 = (TextView)findViewById(R.id.word_test_word_label1);
		EditText wordInput1 = (EditText)findViewById(R.id.word_test_word_input1);

		TextView wordLabel2 = (TextView)findViewById(R.id.word_test_word_label2);
		EditText wordInput2 = (EditText)findViewById(R.id.word_test_word_input2);

		TextView wordLabel3 = (TextView)findViewById(R.id.word_test_word_label3);
		EditText wordInput3 = (EditText)findViewById(R.id.word_test_word_input3);

		TextView wordLabel4 = (TextView)findViewById(R.id.word_test_word_label4);
		EditText wordInput4 = (EditText)findViewById(R.id.word_test_word_input4);

		TextView wordLabel5 = (TextView)findViewById(R.id.word_test_word_label5);
		EditText wordInput5 = (EditText)findViewById(R.id.word_test_word_input5);

		TextView wordLabel6 = (TextView)findViewById(R.id.word_test_word_label6);
		EditText wordInput6 = (EditText)findViewById(R.id.word_test_word_input6);
		
		TextViewAndEditText[] result = new TextViewAndEditText[6];
		
		result[0] = new TextViewAndEditText(wordLabel1, wordInput1);
		result[1] = new TextViewAndEditText(wordLabel2, wordInput2);
		result[2] = new TextViewAndEditText(wordLabel3, wordInput3);
		result[3] = new TextViewAndEditText(wordLabel4, wordInput4);
		result[4] = new TextViewAndEditText(wordLabel5, wordInput5);
		result[5] = new TextViewAndEditText(wordLabel6, wordInput6);
		
		return result;
	}
		
	private static class TextViewAndEditText {
		TextView textView;
		EditText editText;
		
		public TextViewAndEditText(TextView textView, EditText editText) {
			this.textView = textView;
			this.editText = editText;
		}
	}
}
