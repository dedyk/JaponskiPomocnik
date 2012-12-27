package pl.idedyk.android.japaneselearnhelper.test;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperWordTestContext;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.utils.ListUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TableRow.LayoutParams;
import android.widget.TextView;
import android.widget.Toast;

public class WordTest extends Activity {
	
	private TextViewAndEditText[] textViewAndEditTextForWordAsArray;
	
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
				
				final List<DictionaryEntry> wordDictionaryEntries = wordTestContext.getWordsTest();
				int wordsTestIdx = wordTestContext.getWordsTestIdx();
				
				final DictionaryEntry currentWordDictionaryEntry = wordDictionaryEntries.get(wordsTestIdx);
				
				List<String> fullKanaList = currentWordDictionaryEntry.getFullKanaList();
				
				// check user answer
				int correctAnswersNo = getCorrectAnswersNo(context);
				
				wordTestContext.addWordTestAnswers(fullKanaList.size());
				wordTestContext.addWordTestCorrectAnswers(correctAnswersNo);
				wordTestContext.addWordTestIncorrentAnswers(fullKanaList.size() - correctAnswersNo);
				
				if (correctAnswersNo == fullKanaList.size()) {
					Toast.makeText(WordTest.this, getString(R.string.word_test_correct), Toast.LENGTH_SHORT).show();
					
					wordTestContext.incrementWordsTestIdx();				
					
					fillScreen();
				} else {					
					AlertDialog alertDialog = new AlertDialog.Builder(WordTest.this).create();
					
					alertDialog.setMessage(getString(R.string.word_test_incorrect) + 
							ListUtil.getListAsString(fullKanaList, "\n"));
					
					alertDialog.setCancelable(false);
					alertDialog.setButton(getString(R.string.word_test_incorrect_ok), new DialogInterface.OnClickListener() {

						public void onClick(DialogInterface dialog, int which) {
														
							//wordDictionaryEntries.add(currentWordDictionaryEntry);
							
							// FIXME !!!!!!!!!!!!!!!!!!!!!
							
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
		List<String> fullKanaList = currentWordDictionaryEntry.getFullKanaList();
		
		List<String> fullKanaListToRemove = new ArrayList<String>(fullKanaList);
		
		for (int kanaListIdx = 0; kanaListIdx < fullKanaList.size(); ++kanaListIdx) {
			
			String currentUserAnswer = textViewAndEditTextForWordAsArray[kanaListIdx].editText.getText().toString();
			
			fullKanaListToRemove.remove(currentUserAnswer);
		}
		
		return fullKanaList.size() - fullKanaListToRemove.size();
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		
		fillScreen();
	}
	
	private void fillScreen() {
		JapaneseAndroidLearnHelperContext context = JapaneseAndroidLearnHelperApplication.getInstance().getContext();
		
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
			
			if (kanji != null) {
				kanjiInput.setText(currentWordDictionaryEntry.getFullKanji());
				
				kanjiLabel.setVisibility(View.VISIBLE);
				kanjiInput.setVisibility(View.VISIBLE);
				kanjiInput.setEnabled(false);
			} else {
				kanjiInput.setText("");
				
				kanjiLabel.setVisibility(View.GONE);
				kanjiInput.setVisibility(View.GONE);
				kanjiInput.setEnabled(false);
			}
			
			List<String> kanaList = currentWordDictionaryEntry.getFullKanaList();
						
			createTextViewAndEditTextForWordAsArray(kanaList);
			
			TableLayout mainLayout = (TableLayout)findViewById(R.id.word_test_mail_layout);
			
			int kanjiRowPosition = -1;
			
			while(true) {
				
				boolean repeat = false;
				
				for (int mainLayoutChildIdx = 0; mainLayoutChildIdx < mainLayout.getChildCount(); ++mainLayoutChildIdx) {
					
					View currentChildView = mainLayout.getChildAt(mainLayoutChildIdx);
					
					if (currentChildView.getId() == View.NO_ID) {
						mainLayout.removeViewAt(mainLayoutChildIdx);
						
						repeat = true;
						
						break;
					}
				}
				
				if (repeat == false) {
					break;
				}
			}
			
			for (int mainLayoutChildIdx = 0; mainLayoutChildIdx < mainLayout.getChildCount(); ++mainLayoutChildIdx) {
				
				View currentChildView = mainLayout.getChildAt(mainLayoutChildIdx);
				
				if (currentChildView.getId() == R.id.word_test_kanji_row) {
					kanjiRowPosition = mainLayoutChildIdx;
					
					break;
				}
			}
			
			if (kanjiRowPosition == -1) {
				throw new RuntimeException("Can't find kanji row");
			}
			
			for (int textViewAndEditTextForWordAsArrayIdx = 0; textViewAndEditTextForWordAsArrayIdx < textViewAndEditTextForWordAsArray.length; 
					textViewAndEditTextForWordAsArrayIdx++) {
				
				TableRow tableRow = new TableRow(this);
				
				TableLayout.LayoutParams tableRowLayouytParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.FILL_PARENT, 
						TableLayout.LayoutParams.WRAP_CONTENT, 1.0f);
				
				tableRow.setLayoutParams(tableRowLayouytParams);
				
				tableRow.addView(textViewAndEditTextForWordAsArray[textViewAndEditTextForWordAsArrayIdx].textView);
				//tableRow.addView(textViewAndEditTextForWordAsArray[textViewAndEditTextForWordAsArrayIdx].editText);
				
				mainLayout.addView(tableRow, kanjiRowPosition + textViewAndEditTextForWordAsArrayIdx + 1);
			}
			
			for (int kanaListIdx = 0; kanaListIdx < textViewAndEditTextForWordAsArray.length; ++kanaListIdx) {
				
				TextViewAndEditText currentTextViewAndEditText = textViewAndEditTextForWordAsArray[kanaListIdx];
				
				String currentKana = null;
				
				if (kanaListIdx < kanaList.size()) {
					currentKana = kanaList.get(kanaListIdx);
				}
				
				if (currentKana != null && kanaListIdx == 0) {
					currentTextViewAndEditText.editText.requestFocus();
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
	
	private void createTextViewAndEditTextForWordAsArray(List<String> kanaList) {
		
		textViewAndEditTextForWordAsArray = new TextViewAndEditText[kanaList.size()];
		
		for (int idx = 0; idx < kanaList.size(); ++idx) {
			
			// text view
			TextView textView = new TextView(this);
			
			TableLayout.LayoutParams textLayoutParams = new TableLayout.LayoutParams(
					TableLayout.LayoutParams.FILL_PARENT, TableLayout.LayoutParams.MATCH_PARENT, 1.0f);
			
			//textLayoutParams.bottomMargin = 5;
			//textLayoutParams.weight = 1.0f;
			
			textView.setLayoutParams(textLayoutParams);
			
			textView.setBackgroundColor(getResources().getColor(R.color.title_background));
			textView.setGravity(Gravity.CENTER_VERTICAL);
			textView.setSingleLine(false);
			textView.setText(getString(R.string.word_test_word_label));
			
			// edit text
			EditText editText = new EditText(this);
			
			TableLayout.LayoutParams editLayoutParams = new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 
					TableLayout.LayoutParams.WRAP_CONTENT);
			
			//editText.setLayoutParams(editLayoutParams);

			editText.setEms(10);
			
			editText.setInputType(InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
			
			textViewAndEditTextForWordAsArray[idx] = new TextViewAndEditText(textView, editText);
		}
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
