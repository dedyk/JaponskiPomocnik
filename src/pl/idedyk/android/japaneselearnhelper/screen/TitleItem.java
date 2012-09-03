package pl.idedyk.android.japaneselearnhelper.screen;

import pl.idedyk.android.japaneselearnhelper.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class TitleItem implements IScreenItem {
	
	private TextView textView;
	
	private String title;
	
	private int level;
	
	public TitleItem(String title, int level) {
		this.title = title;
		this.level = level;
	}

	public void generate(Context context, Resources resources, ViewGroup layout) {
		textView = new TextView(context);
		
		textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		textView.setBackgroundColor(resources.getColor(R.color.title_background));
		textView.setTextSize(16.0f);
		
		StringBuffer titleSb = new StringBuffer();
		
		for (int levelIdx = 0; levelIdx < level; ++levelIdx) {
			titleSb.append("   ");
		}
		titleSb.append(title);
		
		textView.setText(titleSb);	
		
		layout.addView(textView);
	}
	
	public String toString() {
		return " *** " + title;			
	}
	
	public int getTopPositionOnScreen() {
		if (textView == null) {
			throw new RuntimeException("getTopPositionOnScreen");
		}
		
		int[] location = new int[2];
		
		textView.getLocationOnScreen(location);
		
		return location[1];
	}

}

