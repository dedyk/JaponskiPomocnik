package pl.idedyk.android.japaneselearnhelper.kana;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext;
import pl.idedyk.android.japaneselearnhelper.context.JapaneseAndroidLearnHelperKanaTestContext.RangeTest;
import pl.idedyk.android.japaneselearnhelper.dictionary.KanaHelper;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanaEntry.KanaGroup;
import android.app.Activity;
import android.os.Bundle;

public class KanaTest extends Activity {

	private List<KanaEntry> allKanaEntries;
	
	private Map<KanaGroup, List<KanaEntry>> allKanaEntriesGroupBy;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
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
		
		allKanaEntriesGroupBy = new HashMap<KanaEntry.KanaGroup, List<KanaEntry>>();
		
		for (KanaEntry currentKanaEntry : allKanaEntries) {
			
			KanaGroup currentKanaEntryKanaGroup = currentKanaEntry.getKanaGroup();

			List<KanaEntry> kanaGroupListKanaEntries = allKanaEntriesGroupBy.get(currentKanaEntryKanaGroup);
			
			if (kanaGroupListKanaEntries == null) {
				kanaGroupListKanaEntries = new ArrayList<KanaEntry>();
			}
			
			kanaGroupListKanaEntries.add(currentKanaEntry);
			
			allKanaEntriesGroupBy.put(currentKanaEntryKanaGroup, kanaGroupListKanaEntries);
		}		
	}
}
