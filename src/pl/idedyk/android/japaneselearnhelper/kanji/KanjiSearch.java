package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.RadicalInfo;
import pl.idedyk.android.japaneselearnhelper.screen.IScreenItem;
import pl.idedyk.android.japaneselearnhelper.screen.StringValue;
import pl.idedyk.android.japaneselearnhelper.screen.TableLayout;
import pl.idedyk.android.japaneselearnhelper.screen.TableRow;
import pl.idedyk.android.japaneselearnhelper.screen.TitleItem;
import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class KanjiSearch extends Activity {
	
	@Override
	public void onCreate(Bundle savedInstanceState) {	
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.kanji_search);
		
		List<RadicalInfo> radicalList = DictionaryManager.getInstance().getRadicalList();
		
		LinearLayout mainLayout = (LinearLayout)findViewById(R.id.kanji_search_main_layout);
		
		List<IScreenItem> screenItems = generateScreen(radicalList);
		
		fillMainLayout(screenItems, mainLayout);
	}
	
	private void fillMainLayout(List<IScreenItem> screenItems, LinearLayout mainLayout) {
		
		for (IScreenItem currentScreenItem : screenItems) {
			currentScreenItem.generate(this, getResources(), mainLayout);			
		}
	}

	private List<IScreenItem> generateScreen(List<RadicalInfo> radicalList) {
		
		final Set<String> selectedRadicals = new HashSet<String>();
		
		List<IScreenItem> result = new ArrayList<IScreenItem>();
		
		result.add(new TitleItem(getString(R.string.kanji_search_title), 0));
		
		int lastStrokeCount = -1;
		
		TableLayout tableLayout = null;
		TableRow tableRow = null;
			
		for (RadicalInfo currentRadicalInfo : radicalList) {
			
			int strokeCount = currentRadicalInfo.getStrokeCount();
			
			if (strokeCount != lastStrokeCount) {
								
				result.add(new TitleItem(String.valueOf(strokeCount), 1));
				
				tableLayout = new TableLayout(TableLayout.LayoutParam.WrapContent_WrapContent);
				result.add(tableLayout);
				
				tableRow = new TableRow();
				
				tableLayout.addTableRow(tableRow);
			}
			
			if (tableRow.getScreenItemSize() >= 6) {
				tableRow = new TableRow();
				
				tableLayout.addTableRow(tableRow);				
			}
			
			StringValue radicalStringValue = new StringValue(currentRadicalInfo.getRadical(), 25.0f, 1);
			
			if (tableRow.getScreenItemSize() == 0) {
				radicalStringValue.setNullMargins(false);
			} else {
				radicalStringValue.setNullMargins(true);
			}
			
			radicalStringValue.setOnClickListener(new View.OnClickListener() {
				
				private int defaultTextColor;
				
				public void onClick(View view) {
					
					TextView textView = (TextView)view;
					
					String radical = textView.getText().toString();
					
					if (selectedRadicals.contains(radical) == false) {
						
						defaultTextColor = textView.getTextColors().getDefaultColor();
						
						selectedRadicals.add(radical);
						
						textView.setTextColor(Color.RED);
					} else {
						selectedRadicals.remove(radical);
						
						textView.setTextColor(defaultTextColor);
					}
				}
			});
			
			tableRow.addScreenItem(radicalStringValue);
			
			lastStrokeCount = strokeCount;
		}
		
		result.add(new StringValue("", 12.0f, 0));
				
		return result;
	}
}
