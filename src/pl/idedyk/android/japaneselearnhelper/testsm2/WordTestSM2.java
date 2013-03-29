package pl.idedyk.android.japaneselearnhelper.testsm2;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.config.ConfigManager.WordTestSM2Config;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.Utils;
import pl.idedyk.android.japaneselearnhelper.dictionary.WordTestSM2Manager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.DictionaryEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.WordTestSM2WordStat;
import pl.idedyk.android.japaneselearnhelper.utils.ListUtil;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class WordTestSM2 extends Activity {
	
	private DictionaryManager dictionaryManager = null;	
	private WordTestSM2Manager wordTestSM2Manager = null;
	
	private WordTestSM2WordStat currentNextWordStat = null;
	private DictionaryEntry currentWordDictionaryEntry = null;
	
	private boolean inputWasCorrectAnswer = false;
	
	private TextViewAndEditText[] textViewAndEditTextForWordAsArray;	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		menu.add(Menu.NONE, R.id.report_problem_menu_item, Menu.NONE, R.string.report_problem);
		
		MenuShorterHelper.onCreateOptionsMenu(menu);
		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		
		// report problem
		if (item.getItemId() == R.id.report_problem_menu_item) {

			// FIXME !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
			
			
			return false;
			
		} else {
			return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
		}
	}
	
	@Override
	public void onBackPressed() {
		
		DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
			
		    @Override
		    public void onClick(DialogInterface dialog, int which) {
		        switch (which){
		        case DialogInterface.BUTTON_POSITIVE:
		        	
		        	finish();
		        	
		            break;

		        case DialogInterface.BUTTON_NEGATIVE:
		        	
		        	// noop
		        	
		            break;
		        }
		    }
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		builder.setMessage(getString(R.string.word_test_sm2_quit_question)).setPositiveButton(getString(R.string.word_test_sm2_quit_question_yes), dialogClickListener)
		    .setNegativeButton(getString(R.string.word_test_sm2_quit_question_no), dialogClickListener).show();
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		
		dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);
		wordTestSM2Manager = dictionaryManager.getWordTestSM2Manager();
		
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
        
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.word_test_sm2);
		
		fillScreen();
		
		Button nextButton = (Button)findViewById(R.id.word_test_sm2_next_button);
		
		nextButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				checkUserAnswer();
			}
		});

		
		// testy !!!!!!
		
		// final DictionaryManager dictionaryManager = JapaneseAndroidLearnHelperApplication.getInstance().getDictionaryManager(this);
		
		// final WordTestSM2Manager wordTestSM2Manager = dictionaryManager.getWordTestSM2Manager();		
		
		//wordTestSM2Manager.getCurrentDateStat();
		
		/*
		for (int idx = 0; idx < 10; ++idx) {

			WordTestSM2WordStat nextNewWordStat = wordTestSM2Manager.getNextNewWordStat(20);
			
			Log.d("AAAAAAAA:", "BBBB: " + nextNewWordStat);
			
			if (nextNewWordStat != null) {			
				nextNewWordStat.processRecallResult(2);
			
				wordTestSM2Manager.updateWordStat(nextNewWordStat);
			}
		}

		for (int idx = 0; idx < 10; ++idx) {

			WordTestSM2WordStat nextRepeatWordStat = wordTestSM2Manager.getNextRepeatWordStat();
			
			Log.d("ZZZZZZZ:", "ZZZZZZZ: " + nextRepeatWordStat);
			
			if (nextRepeatWordStat != null) {			
				nextRepeatWordStat.processRecallResult(3);
			
				wordTestSM2Manager.updateWordStat(nextRepeatWordStat);
			}
		}
		*/
		
		/*
		for (int idx = 0; idx < 30; ++idx) {
			WordTestSM2WordStat nextRepeatWordStat = wordTestSM2Manager.getNextWordStat(20);
			
			Log.d("ZZZZZZZ:", "ZZZZZZZ: " + nextRepeatWordStat);
		}
		*/
		
		/*
		for (int idx = 0; idx < 20; ++idx) {
			Log.d("ZZZZZZZ:", "ZZZZZZZ: " + wordTestSM2Manager.getNextWordSize(20));
			
			WordTestSM2WordStat nextWordStat = wordTestSM2Manager.getNextWordStat(20);
			
			if (nextWordStat == null) {
				break;
			}
			
			Log.d("ZZZZZZZ:", "ZZZZZZZ: " + nextWordStat);
			
			Random random = new Random();
			
			nextWordStat.processRecallResult(random.nextInt(6));
			
			wordTestSM2Manager.updateWordStat(nextWordStat);
		}
		*/		
	}
	
	private void checkUserAnswer() {
		
		final WordTestSM2Config wordTestSM2Config = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(WordTestSM2.this).getWordTestSM2Config();
		
		WordTestSM2Mode wordTestSM2Mode = wordTestSM2Config.getWordTestSM2Mode();
		
		if (wordTestSM2Mode == WordTestSM2Mode.INPUT) {
			
			// check user answer
			int correctAnswersNo = getCorrectAnswersNo();
			
			List<String> kanaList = currentWordDictionaryEntry.getKanaList();
						
			if (correctAnswersNo == kanaList.size()) {
				Toast toast = Toast.makeText(WordTestSM2.this, getString(R.string.word_test_sm2_correct), Toast.LENGTH_SHORT);
				
				toast.setGravity(Gravity.TOP, 0, 110);
				
				toast.show();
				
				showFullAnswer();
				
				inputWasCorrectAnswer = true;
				
				showSM2Buttons();
				
			} else {		
								
				Toast toast = Toast.makeText(WordTestSM2.this, getString(R.string.word_test_sm2_incorrect), Toast.LENGTH_SHORT);
				
				toast.setGravity(Gravity.TOP, 0, 110);
				
				toast.show();
				
				showFullAnswer();
				
				inputWasCorrectAnswer = false;
				
				showSM2Buttons();
			}
			
		} else if (wordTestSM2Mode == WordTestSM2Mode.CHOOSE) {
			
			showFullAnswer();
			
			showSM2Buttons();
			
		} else {
			throw new RuntimeException("Unknown wordTestSM2Mode: " + wordTestSM2Config.getWordTestSM2Mode());
		}	
	}
	
	private void showFullAnswer() {
		
		// show kanji
		String kanji = currentWordDictionaryEntry.getKanji();
							
		if (kanji != null) {	
			
			TextView kanjiLabel = (TextView)findViewById(R.id.word_test_sm2_kanji_label);
			EditText kanjiPrefix = (EditText)findViewById(R.id.word_test_sm2_kanji_prefix);
			EditText kanjiInput = (EditText)findViewById(R.id.word_test_sm2_kanji_input);
			
			kanjiLabel.setVisibility(View.VISIBLE);
			kanjiPrefix.setVisibility(View.VISIBLE);
			kanjiInput.setVisibility(View.VISIBLE);
		}
		
		List<String> kanaList = currentWordDictionaryEntry.getKanaList();
		
		// show kana
		for (int kanaListIdx = 0; kanaListIdx < textViewAndEditTextForWordAsArray.length; ++kanaListIdx) {
			
			TextViewAndEditText currentTextViewAndEditText = textViewAndEditTextForWordAsArray[kanaListIdx];
			
			String currentKana = null;
			
			if (kanaListIdx < kanaList.size()) {
				currentKana = kanaList.get(kanaListIdx);
			}
			
			if (currentKana != null) {
									
				currentTextViewAndEditText.editPrefix.setVisibility(View.VISIBLE);
				currentTextViewAndEditText.textView.setVisibility(View.VISIBLE);
				currentTextViewAndEditText.editText.setVisibility(View.VISIBLE);
				
				currentTextViewAndEditText.editText.setFocusable(false);
				currentTextViewAndEditText.editText.setText(currentKana);
				currentTextViewAndEditText.editText.setEnabled(false);
			}
		}
		
		// show translate
		TextView translateLabel = (TextView)findViewById(R.id.word_test_sm2_translate_label);
		EditText translateInput = (EditText)findViewById(R.id.word_test_sm2_translate_input); 

		translateLabel.setVisibility(View.VISIBLE);
		translateInput.setVisibility(View.VISIBLE);
		
		// show additional info
		TextView additionalInfoLabel = (TextView)findViewById(R.id.word_test_sm2_additional_info_label);
		EditText additionalInfoInput = (EditText)findViewById(R.id.word_test_sm2_additional_info_input);
		
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
	}
	
	private int getCorrectAnswersNo() {
				
		List<String> kanaList = currentWordDictionaryEntry.getKanaList();
		
		List<String> kanaListToRemove = new ArrayList<String>(kanaList);
				
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
		
		final WordTestSM2Config wordTestSM2Config = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(WordTestSM2.this).getWordTestSM2Config();
		
		TextView titleTextView = (TextView)findViewById(R.id.word_test_sm2_title);
		
		Button nextButton = (Button)findViewById(R.id.word_test_sm2_next_button);
		
		TableRow sm2SpacerTableRow = (TableRow)findViewById(R.id.word_test_sm2_spacer);
		TableRow sm2IncorrectButtonsTableRow = (TableRow)findViewById(R.id.word_test_sm2_incorrect_sm2_buttons);
		TableRow sm2CorrectButtonsTableRow = (TableRow)findViewById(R.id.word_test_sm2_correct_sm2_buttons);
		
		nextButton.setVisibility(View.VISIBLE);
		
		sm2SpacerTableRow.setVisibility(View.GONE);
		sm2IncorrectButtonsTableRow.setVisibility(View.GONE);
		sm2CorrectButtonsTableRow.setVisibility(View.GONE);		
		
		if (wordTestSM2Config.getWordTestSM2Mode() == WordTestSM2Mode.INPUT) {
			
			titleTextView.setText(getResources().getString(R.string.word_test_sm2_view_input_label));
			
		} else if (wordTestSM2Config.getWordTestSM2Mode() == WordTestSM2Mode.CHOOSE) {
			
			titleTextView.setText(getResources().getString(R.string.word_test_sm2_view_choose_think_label));
			
		} else {
			throw new RuntimeException("Unknown wordTestSM2Mode: " + wordTestSM2Config.getWordTestSM2Mode());
		}
		
		currentNextWordStat = wordTestSM2Manager.getNextWordStat(wordTestSM2Config.getMaxNewWords());
		
		if (currentNextWordStat == null) {
						
			AlertDialog alertDialog = new AlertDialog.Builder(WordTestSM2.this).create();
			
			alertDialog.setMessage(getString(R.string.word_test_sm2_no_more_words));
			
			alertDialog.setCancelable(false);
			alertDialog.setButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					finish();
				}
			});
			
			alertDialog.show();
			
		} else {
			
			currentWordDictionaryEntry = dictionaryManager.getDictionaryEntryById(currentNextWordStat.getId());
						
			String kanji = currentWordDictionaryEntry.getKanji();
			String prefixKana = currentWordDictionaryEntry.getPrefixKana();
			
			TextView kanjiLabel = (TextView)findViewById(R.id.word_test_sm2_kanji_label);
			EditText kanjiPrefix = (EditText)findViewById(R.id.word_test_sm2_kanji_prefix);
			EditText kanjiInput = (EditText)findViewById(R.id.word_test_sm2_kanji_input);
			
			if (kanji != null) {
				kanjiInput.setText(kanji);
				kanjiPrefix.setText(prefixKana);
			} else {
				kanjiInput.setText("");
				kanjiPrefix.setText("");
			}
			
			if (kanji != null && wordTestSM2Config.getShowKanji() != null && wordTestSM2Config.getShowKanji().equals(Boolean.TRUE) == true) {
				
				kanjiLabel.setVisibility(View.VISIBLE);
				kanjiPrefix.setVisibility(View.VISIBLE);
				kanjiInput.setVisibility(View.VISIBLE);
				
				kanjiInput.setEnabled(false);
			} else {				
				kanjiLabel.setVisibility(View.GONE);
				kanjiPrefix.setVisibility(View.GONE);
				kanjiInput.setVisibility(View.GONE);
				
				kanjiInput.setEnabled(false);
			}
			
			List<String> kanaList = currentWordDictionaryEntry.getKanaList();
			
			if (kanaList.size() >= Utils.MAX_LIST_SIZE) {
				throw new RuntimeException("Kana list size: " + kanaList);
			}
						
			createTextViewAndEditTextForWordAsArray(kanaList.size() - 1);
			
			for (int kanaListIdx = 0; kanaListIdx < textViewAndEditTextForWordAsArray.length; ++kanaListIdx) {
				
				TextViewAndEditText currentTextViewAndEditText = textViewAndEditTextForWordAsArray[kanaListIdx];
				
				String currentKana = null;
				
				if (kanaListIdx < kanaList.size()) {
					currentKana = kanaList.get(kanaListIdx);
				}
				
				if (currentKana != null) {
										
					if (wordTestSM2Config.getWordTestSM2Mode() == WordTestSM2Mode.INPUT || (wordTestSM2Config.getShowKana() != null && wordTestSM2Config.getShowKana().equals(Boolean.TRUE) == true)) {
						
						currentTextViewAndEditText.editPrefix.setVisibility(View.VISIBLE);
						currentTextViewAndEditText.textView.setVisibility(View.VISIBLE);
						currentTextViewAndEditText.editText.setVisibility(View.VISIBLE);
						
					} else {
						currentTextViewAndEditText.editPrefix.setVisibility(View.GONE);
						currentTextViewAndEditText.textView.setVisibility(View.GONE);
						currentTextViewAndEditText.editText.setVisibility(View.GONE);
					}
					
					currentTextViewAndEditText.editPrefix.setText(prefixKana);
					
					if (wordTestSM2Config.getWordTestSM2Mode() == WordTestSM2Mode.INPUT) {
						
						currentTextViewAndEditText.editPrefix.setFocusable(true);
						currentTextViewAndEditText.textView.setFocusable(true);
						currentTextViewAndEditText.editText.setFocusable(true);
						
						currentTextViewAndEditText.editText.setText("");
						
						currentTextViewAndEditText.editText.setEnabled(true);
						
						if (kanaListIdx == 0) {
							currentTextViewAndEditText.editText.requestFocus(); 
						}
						
					} else if (wordTestSM2Config.getWordTestSM2Mode() == WordTestSM2Mode.CHOOSE) {
						
						currentTextViewAndEditText.editPrefix.setFocusable(false);
						currentTextViewAndEditText.textView.setFocusable(false);
						currentTextViewAndEditText.editText.setFocusable(false);
						
						currentTextViewAndEditText.editText.setText(currentKana);
						
						currentTextViewAndEditText.editText.setEnabled(false);						
					} else {
						throw new RuntimeException("Unknown wordTestSM2Mode: " + wordTestSM2Config.getWordTestSM2Mode());
					}					
					
				} else {
					currentTextViewAndEditText.textView.setVisibility(View.GONE);
					
					currentTextViewAndEditText.editPrefix.setVisibility(View.GONE);
					currentTextViewAndEditText.editText.setVisibility(View.GONE);
					currentTextViewAndEditText.editText.setText("");
				}
			}
			
			TextView translateLabel = (TextView)findViewById(R.id.word_test_sm2_translate_label);
			EditText translateInput = (EditText)findViewById(R.id.word_test_sm2_translate_input); 
			
			translateInput.setText(ListUtil.getListAsString(currentWordDictionaryEntry.getTranslates(), "\n"));
			translateInput.setEnabled(false);
			
			if (wordTestSM2Config.getShowTranslate() != null && wordTestSM2Config.getShowTranslate().equals(Boolean.TRUE) == true) {
				translateLabel.setVisibility(View.VISIBLE);
				translateInput.setVisibility(View.VISIBLE);
			} else {
				translateLabel.setVisibility(View.GONE);
				translateInput.setVisibility(View.GONE);
			}
			
			TextView additionalInfoLabel = (TextView)findViewById(R.id.word_test_sm2_additional_info_label);
			EditText additionalInfoInput = (EditText)findViewById(R.id.word_test_sm2_additional_info_input);

			String additionalInfo = currentWordDictionaryEntry.getFullInfo();
			
			if (additionalInfo != null && wordTestSM2Config.getShowAdditionalInfo() != null && wordTestSM2Config.getShowAdditionalInfo().equals(Boolean.TRUE) == true) {
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
			
			TextView state = (TextView)findViewById(R.id.word_test_sm2_state_info);			
			
			Resources resources = getResources();
			
			state.setText(resources.getString(R.string.word_test_sm2_state, wordTestSM2Manager.getNextWordSize(wordTestSM2Config.getMaxNewWords())));
		}
	}
	
	private void showSM2Buttons() {
		
		Button nextButton = (Button)findViewById(R.id.word_test_sm2_next_button);
		
		TableRow sm2SpacerTableRow = (TableRow)findViewById(R.id.word_test_sm2_spacer);
		TableRow sm2IncorrectButtonsTableRow = (TableRow)findViewById(R.id.word_test_sm2_incorrect_sm2_buttons);
		TableRow sm2CorrectButtonsTableRow = (TableRow)findViewById(R.id.word_test_sm2_correct_sm2_buttons);
		
		TextView titleTextView = (TextView)findViewById(R.id.word_test_sm2_title);
		
		nextButton.setVisibility(View.GONE);
		sm2SpacerTableRow.setVisibility(View.VISIBLE);
				
		final WordTestSM2Config wordTestSM2Config = JapaneseAndroidLearnHelperApplication.getInstance().getConfigManager(WordTestSM2.this).getWordTestSM2Config();
		
		WordTestSM2Mode wordTestSM2Mode = wordTestSM2Config.getWordTestSM2Mode();
		
		if (wordTestSM2Mode == WordTestSM2Mode.INPUT) {
			
			if (inputWasCorrectAnswer == false) {
				
				titleTextView.setText(getString(R.string.word_test_sm2_view_input_incorrect_label));
				
				sm2IncorrectButtonsTableRow.setVisibility(View.VISIBLE);
				sm2CorrectButtonsTableRow.setVisibility(View.GONE);
				
			} else {
				
				titleTextView.setText(getString(R.string.word_test_sm2_view_input_correct_label));
				
				sm2IncorrectButtonsTableRow.setVisibility(View.GONE);
				sm2CorrectButtonsTableRow.setVisibility(View.VISIBLE);
			}
			
		} else if (wordTestSM2Mode == WordTestSM2Mode.CHOOSE) {

			titleTextView.setText(getString(R.string.word_test_sm2_view_choose_think_result_label));
			
			sm2IncorrectButtonsTableRow.setVisibility(View.VISIBLE);
			sm2CorrectButtonsTableRow.setVisibility(View.VISIBLE);
			
		} else {
			throw new RuntimeException("Unknown wordTestSM2Mode: " + wordTestSM2Config.getWordTestSM2Mode());
		}		
	}
	
	private void createTextViewAndEditTextForWordAsArray(final int lastAnswerIdx) {
		
		TextView wordLabel1 = (TextView)findViewById(R.id.word_test_sm2_word_label1);
		EditText wordPrefix1 = (EditText)findViewById(R.id.word_test_sm2_word_prefix1);
		EditText wordInput1 = (EditText)findViewById(R.id.word_test_sm2_word_input1);
		
		TextView wordLabel2 = (TextView)findViewById(R.id.word_test_sm2_word_label2);
		EditText wordPrefix2 = (EditText)findViewById(R.id.word_test_sm2_word_prefix2);
		EditText wordInput2 = (EditText)findViewById(R.id.word_test_sm2_word_input2);

		TextView wordLabel3 = (TextView)findViewById(R.id.word_test_sm2_word_label3);
		EditText wordPrefix3 = (EditText)findViewById(R.id.word_test_sm2_word_prefix3);
		EditText wordInput3 = (EditText)findViewById(R.id.word_test_sm2_word_input3);

		TextView wordLabel4 = (TextView)findViewById(R.id.word_test_sm2_word_label4);
		EditText wordPrefix4 = (EditText)findViewById(R.id.word_test_sm2_word_prefix4);
		EditText wordInput4 = (EditText)findViewById(R.id.word_test_sm2_word_input4);

		TextView wordLabel5 = (TextView)findViewById(R.id.word_test_sm2_word_label5);
		EditText wordPrefix5 = (EditText)findViewById(R.id.word_test_sm2_word_prefix5);
		EditText wordInput5 = (EditText)findViewById(R.id.word_test_sm2_word_input5);

		TextView wordLabel6 = (TextView)findViewById(R.id.word_test_sm2_word_label6);
		EditText wordPrefix6 = (EditText)findViewById(R.id.word_test_sm2_word_prefix6);
		EditText wordInput6 = (EditText)findViewById(R.id.word_test_sm2_word_input6);
		
		textViewAndEditTextForWordAsArray = new TextViewAndEditText[Utils.MAX_LIST_SIZE];
		
		textViewAndEditTextForWordAsArray[0] = new TextViewAndEditText(wordLabel1, wordPrefix1, wordInput1);
		textViewAndEditTextForWordAsArray[1] = new TextViewAndEditText(wordLabel2, wordPrefix2, wordInput2);
		textViewAndEditTextForWordAsArray[2] = new TextViewAndEditText(wordLabel3, wordPrefix3, wordInput3);
		textViewAndEditTextForWordAsArray[3] = new TextViewAndEditText(wordLabel4, wordPrefix4, wordInput4);
		textViewAndEditTextForWordAsArray[4] = new TextViewAndEditText(wordLabel5, wordPrefix5, wordInput5);
		textViewAndEditTextForWordAsArray[5] = new TextViewAndEditText(wordLabel6, wordPrefix6, wordInput6);
		
		for (int idx = 0; idx < textViewAndEditTextForWordAsArray.length; ++idx) {
			
			final EditText currentEditText = textViewAndEditTextForWordAsArray[idx].editText;
			
			if (idx == lastAnswerIdx) {
				
				currentEditText.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View v) {
						
						String currentEditTextText = currentEditText.getText().toString();
						
						if (currentEditTextText != null && currentEditTextText.equals("") == false) {
							checkUserAnswer();
						}
					}
				});	
				
			} else {
				currentEditText.setOnClickListener(null);
			}
		}
	}
		
	private static class TextViewAndEditText {
		TextView textView;
		
		EditText editPrefix;
		EditText editText;
		
		public TextViewAndEditText(TextView textView, EditText editPrefix, EditText editText) {
			this.textView = textView;
			
			this.editPrefix = editPrefix;
			this.editText = editText;
		}
	}
}
