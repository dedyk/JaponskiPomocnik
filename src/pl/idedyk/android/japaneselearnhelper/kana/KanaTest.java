package pl.idedyk.android.japaneselearnhelper.kana;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.RangeTest;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.TestMode1;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.TestMode2;
import pl.idedyk.android.japaneselearnhelper.dictionary.KanaHelper;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry.KanaGroup;
import pl.idedyk.android.japaneselearnhelper.problem.ReportProblem;
import pl.idedyk.android.japaneselearnhelper.screen.Button;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.LinearLayout;
import android.widget.Toast;

public class KanaTest extends Activity {
	
	final int max_x = 4;
	final int max_y = 4;

	private StringValue charTest;
	
	private StringValue position;
	
	private Button[][] chooseButtons;
	
	private TitleItem answerTitleItem;
	
	//private EditText inputAnswerEditText;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.kana_test);
		
		generateDatas();
		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.kana_test_main_layout);
		LinearLayout answerLayout = (LinearLayout)findViewById(R.id.kana_test_answer_layout);
		
		final List<IScreenItem> screenMainItems = createMainScreenTest();
		final List<IScreenItem> screenAnswerItems = createAnswerScreenTest();
		
		fillLayout(screenMainItems, mainLayout);
		fillLayout(screenAnswerItems, answerLayout);
		
		fillScreenValue();
		
		android.widget.Button reportProblemButton = (android.widget.Button)findViewById(R.id.kana_test_report_problem_button);
		
		reportProblemButton.setOnClickListener(new OnClickListener() {
			
			public void onClick(View view) {
				
				StringBuffer detailsSb = new StringBuffer();
				
				for (IScreenItem currentScreenItem : screenMainItems) {
					detailsSb.append(currentScreenItem.toString()).append("\n\n");
				}

				for (IScreenItem currentScreenItem : screenAnswerItems) {
					detailsSb.append(currentScreenItem.toString()).append("\n\n");
				}
				
				String chooseEmailClientTitle = getString(R.string.choose_email_client);
				
				String mailSubject = getString(R.string.kana_test_report_problem_email_subject);
				
				String mailBody = getString(R.string.kana_test_report_problem_email_body,
						detailsSb.toString());				
				
				Log.d("AAA", mailBody.toString());
				
				Intent reportProblemIntent = ReportProblem.createReportProblemIntent(mailSubject, mailBody.toString()); 
				
				startActivity(Intent.createChooser(reportProblemIntent, chooseEmailClientTitle));
			}
		});

	}
	
	private void generateDatas() {
		
		JapaneseAndroidLearnHelperKanaTestContext kanaTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanaTestContext();
		
		if (kanaTestContext.isInitialized() == true) {
			return;
		}
		
		List<KanaEntry> allKanaEntries = null;
		
		RangeTest rangeTest = kanaTestContext.getRangeTest();
		
		if (rangeTest == RangeTest.HIRAGANA) {
			allKanaEntries = KanaHelper.getAllHiraganaKanaEntries();
			
		} else if (rangeTest == RangeTest.KATAKANA) {
			allKanaEntries = KanaHelper.getAllKatakanaKanaEntries();
			
		} else if (rangeTest == RangeTest.HIRAGANA_KATAKANA) {
			
			List<KanaEntry> allHiraganaEntries = KanaHelper.getAllHiraganaKanaEntries();
			List<KanaEntry> allKatakanaEntries = KanaHelper.getAllKatakanaKanaEntries();
			
			allKanaEntries = new ArrayList<KanaEntry>();
			
			allKanaEntries.addAll(allHiraganaEntries);
			allKanaEntries.addAll(allKatakanaEntries);
		} else {
			throw new RuntimeException("allKanaEntries");
		}
		
		boolean gojuuon = kanaTestContext.getGojuuon();
		boolean dakutenHandakuten = kanaTestContext.getDakutenHandakuten();
		boolean youon = kanaTestContext.getYouon();
		
		allKanaEntries = filterAllKanaEntries(allKanaEntries, gojuuon, dakutenHandakuten, youon);
		
		Map<String, List<KanaEntry>> allKanaEntriesGroupBy = allKanaEntriesGroupBy(allKanaEntries);
		
		allKanaEntries = randomAllKanaEntries(allKanaEntries);
		
		// set
		kanaTestContext.setAllKanaEntriesIdx(0);
		kanaTestContext.setAllKanaEntries(allKanaEntries);
		kanaTestContext.setAllKanaEntriesGroupBy(allKanaEntriesGroupBy);
		
		kanaTestContext.setInitialized(true);
		
		checkTestStateAndGenerateAnswers();
	}

	private void fillLayout(List<IScreenItem> screenItems, LinearLayout layout) {
		
		for (IScreenItem currentScreenItem : screenItems) {
			currentScreenItem.generate(this, getResources(), layout);			
		}
	}

	private List<KanaEntry> filterAllKanaEntries(List<KanaEntry> allKanaEntries, boolean gojuuon, Boolean dakutenHandakuten, Boolean youon) {
		
		List<KanaEntry> result = new ArrayList<KanaEntry>();
		
		for (KanaEntry currentKanaEntry : allKanaEntries) {
			
			KanaGroup currentKanaEntryKanaGroup = currentKanaEntry.getKanaGroup();
			
			if (currentKanaEntryKanaGroup == KanaGroup.GOJUUON && gojuuon == true) {
				result.add(currentKanaEntry);
				
				continue;
			}
			
			if (currentKanaEntryKanaGroup == KanaGroup.DAKUTEN && dakutenHandakuten == true) {
				result.add(currentKanaEntry);
				
				continue;
			}

			if (currentKanaEntryKanaGroup == KanaGroup.HANDAKUTEN && dakutenHandakuten == true) {
				result.add(currentKanaEntry);
				
				continue;
			}
			
			if (currentKanaEntryKanaGroup == KanaGroup.YOUON && youon == true) {
				result.add(currentKanaEntry);
				
				continue;
			}
		}
		
		return result;
	}
	
	private Map<String, List<KanaEntry>> allKanaEntriesGroupBy(List<KanaEntry> allKanaEntries) {
		
		Map<String, List<KanaEntry>> allKanaEntriesGroupBy = new HashMap<String, List<KanaEntry>>();
		
		for (KanaEntry currentKanaEntry : allKanaEntries) {
			
			String key = getKanaEntryKey(currentKanaEntry);

			List<KanaEntry> kanaGroupListKanaEntries = allKanaEntriesGroupBy.get(key);
			
			if (kanaGroupListKanaEntries == null) {
				kanaGroupListKanaEntries = new ArrayList<KanaEntry>();
			}
			
			kanaGroupListKanaEntries.add(currentKanaEntry);
			
			allKanaEntriesGroupBy.put(key, kanaGroupListKanaEntries);
		}
		
		return allKanaEntriesGroupBy;
	}
	
	private String getKanaEntryKey(KanaEntry kanaEntry) {
		
		KanaGroup kanaGroup = kanaEntry.getKanaGroup();
		
		if (kanaGroup == KanaGroup.HANDAKUTEN) {
			kanaGroup = KanaGroup.DAKUTEN;
		}
		
		return kanaEntry.getKanaType().toString() + "." + kanaGroup.toString();
	}
	
	private List<KanaEntry> randomAllKanaEntries(List<KanaEntry> allKanaEntries) {
		Collections.shuffle(allKanaEntries);
		
		return allKanaEntries;
	}
	
	private List<IScreenItem> createMainScreenTest() {
		
		final List<IScreenItem> result = new ArrayList<IScreenItem>();
		
		result.add(new TitleItem(getString(R.string.kana_test_char), 0));
		
		charTest = new StringValue("", 90.0f, 0);
		charTest.setGravity(Gravity.CENTER);
		charTest.setNullMargins(true);
		
		result.add(charTest);
		
		position = new StringValue("", 12.0f, 0);
		position.setGravity(Gravity.RIGHT);
		position.setNullMargins(true);
		position.setLayoutWidth(1);
		
		result.add(position);
		
		return result;
	}
	
	private List<IScreenItem> createAnswerScreenTest() {

		final JapaneseAndroidLearnHelperKanaTestContext kanaTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanaTestContext();
		
		final List<IScreenItem> result = new ArrayList<IScreenItem>();
		
		final TestMode1 testMode1 = kanaTestContext.getTestMode1();
		
		final boolean untilSuccess = kanaTestContext.getUntilSuccess();

		if (testMode1 == TestMode1.CHOOSE) {
			
			answerTitleItem = new TitleItem(getString(R.string.kana_test_choose), 0);
			
			result.add(answerTitleItem);
			result.add(new StringValue("", 7.0f, 0));
			
			TableLayout chooseTableLayout = new TableLayout(TableLayout.LayoutParam.FillParent_WrapContent);
			
			chooseButtons = new Button[max_x][max_y];
			
			for (int y = 0; y < max_y; ++y) {				
				TableRow currentTableRow = new TableRow();
				
				for (int x = 0; x < max_x; ++x) {
					chooseButtons[x][y] = new Button("");
					
					chooseButtons[x][y].setTextSize(20.0f);
					
					currentTableRow.addScreenItem(chooseButtons[x][y]);
					
					chooseButtons[x][y].setOnClickListener(new View.OnClickListener() {
						public void onClick(View view) {
							
							// check answer
							android.widget.Button choseButton = (android.widget.Button)view;
							
							String userAnswer = choseButton.getText().toString();
							
							checkUserAnswer(kanaTestContext, untilSuccess, userAnswer);
						}
					});
				}
				
				chooseTableLayout.addTableRow(currentTableRow);
			}
			
			result.add(chooseTableLayout);			
		} else if (testMode1 == TestMode1.INPUT) {
			throw new RuntimeException("testMode1 == TestMode1.INPUT");
			
			/*
			answerTitleItem = new TitleItem(getString(R.string.kana_test_input), 0);
			
			result.add(answerTitleItem);
			result.add(new StringValue("", 7.0f, 0));
			
			inputAnswerEditText = new EditText();
			
			result.add(inputAnswerEditText);
			*/
			
		} else {
			throw new RuntimeException("testMode1");
		}
		
		return result;
	}
	
	private void fillScreenValue() {

		final JapaneseAndroidLearnHelperKanaTestContext kanaTestContext = 
				JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanaTestContext();
		
		List<KanaEntry> allKanaEntries = kanaTestContext.getAllKanaEntries();
		int allKanaEntriesIdx = kanaTestContext.getAllKanaEntriesIdx();
		
		charTest.setValue(kanaTestContext.getCharTestValue());
		
		position.setValue(getString(R.string.kana_test_state, (allKanaEntriesIdx + 1), allKanaEntries.size()));
		
		TestMode1 testMode1 = kanaTestContext.getTestMode1();
		
		if (testMode1 == TestMode1.CHOOSE) {
			
			String[][] buttonValues = kanaTestContext.getButtonValues();
						
			for (int y = 0; y < max_y; ++y) {
				for (int x = 0; x < max_x; ++x) {
					chooseButtons[x][y].setText(buttonValues[x][y]);
				}
			}			
			
		} else if (testMode1 == TestMode1.INPUT) {
			
			throw new RuntimeException("testMode1 == TestMode1.INPUT");
			
		} else {
			throw new RuntimeException("testMode1");
		}
	}
	
	private List<KanaEntry> getIncorrectAnswers(KanaEntry correctKanaEntry, int maxSize) {
		
		final JapaneseAndroidLearnHelperKanaTestContext kanaTestContext = 
				JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanaTestContext();
		
		List<KanaEntry> result = new ArrayList<KanaEntry>();
		
		Set<String> alreadyChoosenKanaEntries = new HashSet<String>();
		
		alreadyChoosenKanaEntries.add(correctKanaEntry.getKanaJapanese());		
		
		List<KanaEntry> answers = kanaTestContext.getAllKanaEntriesGroupBy().get(getKanaEntryKey(correctKanaEntry));
		
		Random random = new Random();
				
		while(true) {
						
			int randomAnswersIdx = random.nextInt(answers.size());
			
			KanaEntry answersToCheck = answers.get(randomAnswersIdx);
			
			if (alreadyChoosenKanaEntries.contains(answersToCheck.getKanaJapanese()) == true) {
				continue;
			}
			
			result.add(answersToCheck);
			alreadyChoosenKanaEntries.add(answersToCheck.getKanaJapanese());
			
			if (result.size() >= maxSize) {
				break;
			}
		}
		
		return result;
	}

	private String getButtonValue(TestMode2 testMode2, KanaEntry kanaEntry) {
		
		if (testMode2 == TestMode2.KANA_TO_ROMAJI) {
			return kanaEntry.getKana();
			
		} else if (testMode2 == TestMode2.ROMAJI_TO_KANA) {
			return kanaEntry.getKanaJapanese();
		} else {
			throw new RuntimeException("getButtonValue");
		}
	}
	
	private void checkUserAnswer(final JapaneseAndroidLearnHelperKanaTestContext kanaTestContext, final boolean untilSuccess, String userAnswer) {
		
		final List<KanaEntry> allKanaEntries = kanaTestContext.getAllKanaEntries();
		
		final KanaEntry currentKanaEntryToTest = allKanaEntries.get(kanaTestContext.getAllKanaEntriesIdx());
		
		String correctAnswer = null;

		TestMode2 testMode2 = kanaTestContext.getTestMode2();
		
		if (testMode2 == TestMode2.KANA_TO_ROMAJI) {
			correctAnswer = currentKanaEntryToTest.getKana();
			
		} else if (testMode2 == TestMode2.ROMAJI_TO_KANA) {
			correctAnswer = currentKanaEntryToTest.getKanaJapanese();
		}
		
		if (correctAnswer.equals(userAnswer) == true) {
			
			int positionBottomPositionOnScreen = position.getBottomPositionOnScreen();
			int answerTitleItemTopPositionOnScreen = answerTitleItem.getTopPositionOnScreen();
			
			Toast toast = Toast.makeText(this, getString(R.string.kana_test_correct_answer), Toast.LENGTH_SHORT);
			
			toast.setGravity(Gravity.TOP, 0, ((answerTitleItemTopPositionOnScreen + positionBottomPositionOnScreen) / 2) - 60);
			
			toast.show();
			
			kanaTestContext.incrementCorrectAnswers();
			
			kanaTestContext.setAllKanaEntriesIdx(kanaTestContext.getAllKanaEntriesIdx() + 1);
						
			checkTestStateAndGenerateAnswers();
			fillScreenValue();
			
		} else {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			
			alertDialog.setMessage(getString(R.string.kana_test_incorrect_answer, correctAnswer));
			
			alertDialog.setCancelable(false);
			alertDialog.setButton(getString(R.string.kana_test_incorrect_ok), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					if (untilSuccess == true) {
						allKanaEntries.add(currentKanaEntryToTest);
					}
					
					kanaTestContext.incrementIncorrectAnswers();
					kanaTestContext.setAllKanaEntriesIdx(kanaTestContext.getAllKanaEntriesIdx() + 1);
					
					checkTestStateAndGenerateAnswers();
					fillScreenValue();
				}
			});
			
			alertDialog.show();
		}
	}
	
	private void checkTestStateAndGenerateAnswers() {
		
		final JapaneseAndroidLearnHelperKanaTestContext kanaTestContext = 
				JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanaTestContext();
		
		List<KanaEntry> allKanaEntries = kanaTestContext.getAllKanaEntries();
		int allKanaEntriesIdx = kanaTestContext.getAllKanaEntriesIdx();
		
		if (allKanaEntriesIdx >= allKanaEntries.size()) {
			// test end
			
			Intent intent = new Intent(getApplicationContext(), KanaTestSummary.class);
			
			startActivity(intent);			
			
			finish();
			
			return;
		}
		
		TestMode1 testMode1 = kanaTestContext.getTestMode1();
		
		TestMode2 testMode2 = kanaTestContext.getTestMode2();
		
		KanaEntry currentKanaEntryToTest = allKanaEntries.get(allKanaEntriesIdx);
		
		if (testMode2 == TestMode2.KANA_TO_ROMAJI) {
			kanaTestContext.setCharTestValue(currentKanaEntryToTest.getKanaJapanese());
			
		} else if (testMode2 == TestMode2.ROMAJI_TO_KANA) {
			kanaTestContext.setCharTestValue(currentKanaEntryToTest.getKana());
			
		} else {
			throw new RuntimeException("testMode2");
		}
		
		if (testMode1 == TestMode1.CHOOSE) {
			
			Random random = new Random();
			
			int correctAnswerPos = random.nextInt(max_x * max_y);
			
			List<KanaEntry> incorrectAnswers = getIncorrectAnswers(currentKanaEntryToTest, max_x * max_y);
			
			String[] buttonsValuesLinear = new String[max_x * max_y];
			
			for (int y = 0; y < max_y; ++y) {
				for (int x = 0; x < max_x; ++x) {

					int currentPosIdx = y * max_x + x;

					if (correctAnswerPos == currentPosIdx) {
						buttonsValuesLinear[currentPosIdx] = getButtonValue(testMode2, currentKanaEntryToTest);
					} else {
						buttonsValuesLinear[currentPosIdx] = getButtonValue(testMode2, incorrectAnswers.get(currentPosIdx));
					}
				}
			}
			
			Arrays.sort(buttonsValuesLinear);
			
			String[][] buttonValues = new String[max_x][max_y];
			
			for (int idx = 0; idx < buttonsValuesLinear.length; ++idx) {
				
				int y = idx / max_x;
				int x = idx - y * max_x;
				
				buttonValues[x][y] = buttonsValuesLinear[idx];
			}
			
			kanaTestContext.setButtonValues(buttonValues);
			
		} else if (testMode1 == TestMode1.INPUT) {
			
			throw new RuntimeException("testMode1 == TestMode1.INPUT");
			
		} else {
			throw new RuntimeException("testMode1");
		}		
	}
}
