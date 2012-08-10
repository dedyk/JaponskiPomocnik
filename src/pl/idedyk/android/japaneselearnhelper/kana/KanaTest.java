package pl.idedyk.android.japaneselearnhelper.kana;

import java.util.ArrayList;
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
import pl.idedyk.android.japaneselearnhelper.screen.Button;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

public class KanaTest extends Activity {
	
	final int max_x = 4;
	final int max_y = 3;

	private List<KanaEntry> allKanaEntries;
	
	private Map<String, List<KanaEntry>> allKanaEntriesGroupBy;
	
	private StringValue charTest;
	
	private StringValue position;
	
	private Button[][] chooseButtons;
	
	private int allKanaEntriesIdx = 0;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.kana_test);
		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.kana_test_main_layout);
		LinearLayout answerLayout = (LinearLayout)findViewById(R.id.kana_test_answer_layout);
		
		JapaneseAndroidLearnHelperKanaTestContext kanaTestContext = JapaneseAndroidLearnHelperApplication.getInstance().getContext().getKanaTestContext();
		
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
		
		filterAllKanaEntries();
		allKanaEntriesGroupBy();
		randomAllKanaEntries();
		
		List<IScreenItem> screenMainItems = createMainScreenTest(kanaTestContext);
		List<IScreenItem> screenAnswerItems = createAnswerScreenTest(kanaTestContext);
		
		fillLayout(screenMainItems, mainLayout);
		fillLayout(screenAnswerItems, answerLayout);
		
		fillScreenValue(kanaTestContext);
	}

	private void fillLayout(List<IScreenItem> screenItems, LinearLayout layout) {
		
		for (IScreenItem currentScreenItem : screenItems) {
			currentScreenItem.generate(this, getResources(), layout);			
		}
	}

	private void filterAllKanaEntries() {
		
		List<KanaEntry> result = new ArrayList<KanaEntry>();
		
		for (KanaEntry currentKanaEntry : allKanaEntries) {
			
			KanaGroup currentKanaEntryKanaGroup = currentKanaEntry.getKanaGroup();
			
			if (currentKanaEntryKanaGroup == KanaGroup.OTHER) {
				continue;
			}
			
			result.add(currentKanaEntry);
		}
		
		allKanaEntries = result;
	}
	
	private void allKanaEntriesGroupBy() {
		
		allKanaEntriesGroupBy = new HashMap<String, List<KanaEntry>>();
		
		for (KanaEntry currentKanaEntry : allKanaEntries) {
			
			String key = getKanaEntryKey(currentKanaEntry);

			List<KanaEntry> kanaGroupListKanaEntries = allKanaEntriesGroupBy.get(key);
			
			if (kanaGroupListKanaEntries == null) {
				kanaGroupListKanaEntries = new ArrayList<KanaEntry>();
			}
			
			kanaGroupListKanaEntries.add(currentKanaEntry);
			
			allKanaEntriesGroupBy.put(key, kanaGroupListKanaEntries);
		}		
	}
	
	private String getKanaEntryKey(KanaEntry kanaEntry) {
		return kanaEntry.getKanaType().toString() + "." + kanaEntry.getKanaGroup().toString();
	}
	
	private void randomAllKanaEntries() {
		Collections.shuffle(allKanaEntries);
	}
	
	private List<IScreenItem> createMainScreenTest(JapaneseAndroidLearnHelperKanaTestContext kanaTestContext) {
		
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
	
	private List<IScreenItem> createAnswerScreenTest(final JapaneseAndroidLearnHelperKanaTestContext kanaTestContext) {

		final List<IScreenItem> result = new ArrayList<IScreenItem>();
		
		final TestMode1 testMode1 = kanaTestContext.getTestMode1();
		
		final boolean untilSuccess = kanaTestContext.isUntilSuccess();

		if (testMode1 == TestMode1.CHOOSE) {
			result.add(new TitleItem(getString(R.string.kana_test_choose), 0));
			result.add(new StringValue("", 7.0f, 0));
			
			TableLayout chooseTableLayout = new TableLayout();
			
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
			
			// FIXME !!!
			
			
		} else {
			throw new RuntimeException("testMode1");
		}
		
		return result;
	}
	
	private void fillScreenValue(JapaneseAndroidLearnHelperKanaTestContext kanaTestContext) {
		
		if (allKanaEntriesIdx >= allKanaEntries.size()) {
			// test end
			
			finish();
			
			return;
		}
		
		TestMode1 testMode1 = kanaTestContext.getTestMode1();
		
		TestMode2 testMode2 = kanaTestContext.getTestMode2();
		
		KanaEntry currentKanaEntryToTest = allKanaEntries.get(allKanaEntriesIdx);
		
		if (testMode2 == TestMode2.KANA_TO_ROMAJI) {
			charTest.setValue(currentKanaEntryToTest.getKanaJapanese());
			
		} else if (testMode2 == TestMode2.ROMAJI_TO_KANA) {
			charTest.setValue(currentKanaEntryToTest.getKana());
			
		} else {
			throw new RuntimeException("testMode2");
		}
		
		position.setValue(getString(R.string.kana_test_state, (allKanaEntriesIdx + 1), allKanaEntries.size()));

		if (testMode1 == TestMode1.CHOOSE) {
			
			Random random = new Random();
			
			int correctAnswerPos = random.nextInt(max_x * max_y);
			
			List<KanaEntry> incorrectAnswers = getIncorrectAnswers(currentKanaEntryToTest, max_x * max_y);
			
			for (int y = 0; y < max_y; ++y) {
				for (int x = 0; x < max_x; ++x) {

					int currentPosIdx = y * max_x + x;

					if (correctAnswerPos == currentPosIdx) {
						setButtonValue(testMode2, x, y, currentKanaEntryToTest);
					} else {
						setButtonValue(testMode2, x, y, incorrectAnswers.get(currentPosIdx));
					}
				}
			}			
			
		} else if (testMode1 == TestMode1.INPUT) {
			
			// FIXME !!!
			
			
		} else {
			throw new RuntimeException("testMode1");
		}
	}
	
	private List<KanaEntry> getIncorrectAnswers(KanaEntry correctKanaEntry, int maxSize) {
		
		List<KanaEntry> result = new ArrayList<KanaEntry>();
		
		Set<String> alreadyChoosenKanaEntries = new HashSet<String>();
		
		alreadyChoosenKanaEntries.add(correctKanaEntry.getKanaJapanese());		
		
		List<KanaEntry> answers = allKanaEntriesGroupBy.get(getKanaEntryKey(correctKanaEntry));
		
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

	private void setButtonValue(TestMode2 testMode2, int x, int y, KanaEntry kanaEntry) {
		
		if (testMode2 == TestMode2.KANA_TO_ROMAJI) {
			chooseButtons[x][y].setText(kanaEntry.getKana());
			
		} else if (testMode2 == TestMode2.ROMAJI_TO_KANA) {
			chooseButtons[x][y].setText(kanaEntry.getKanaJapanese());
		}
	}
	
	private void checkUserAnswer(final JapaneseAndroidLearnHelperKanaTestContext kanaTestContext, final boolean untilSuccess, String userAnswer) {
		
		TestMode2 testMode2 = kanaTestContext.getTestMode2();
		
		final KanaEntry currentKanaEntryToTest = allKanaEntries.get(allKanaEntriesIdx);
		
		String correctAnswer = null;
		
		if (testMode2 == TestMode2.KANA_TO_ROMAJI) {
			correctAnswer = currentKanaEntryToTest.getKana();
			
		} else if (testMode2 == TestMode2.ROMAJI_TO_KANA) {
			correctAnswer = currentKanaEntryToTest.getKanaJapanese();
		}
		
		if (correctAnswer.equals(userAnswer) == true) {
			Toast.makeText(this, getString(R.string.kana_test_correct_answer), Toast.LENGTH_SHORT).show();
			
			allKanaEntriesIdx++;
			
			fillScreenValue(kanaTestContext);
			
		} else {
			AlertDialog alertDialog = new AlertDialog.Builder(this).create();
			
			alertDialog.setMessage(getString(R.string.kana_test_incorrect_answer, correctAnswer));
			
			alertDialog.setCancelable(false);
			alertDialog.setButton(getString(R.string.kana_test_incorrect_ok), new DialogInterface.OnClickListener() {

				public void onClick(DialogInterface dialog, int which) {
					
					if (untilSuccess == true) {
						allKanaEntries.add(currentKanaEntryToTest);
					}
					
					allKanaEntriesIdx++;
					
					fillScreenValue(kanaTestContext);
				}
			});
			
			alertDialog.show();
		}
	}
}
