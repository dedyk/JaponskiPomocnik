package pl.idedyk.android.japaneselearnhelper.screen;

import pl.idedyk.android.japaneselearnhelper.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class TitleItem extends TextView {
		
	private String title;
	
	public TitleItem(Context context) {
		super(context);
		
		throw new RuntimeException("Please do not use");
	}
	
	public TitleItem(Context context, Resources resources, String title, int level) {
		super(context);
		
		this.title = title;
		
		setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		setBackgroundColor(resources.getColor(R.color.word_dictionary_details_title_background_color));
		setTextSize(16.0f);
		
		StringBuffer titleSb = new StringBuffer();
		
		for (int levelIdx = 0; levelIdx < level; ++levelIdx) {
			titleSb.append("   ");
		}
		titleSb.append(title);
		
		setText(titleSb);		
	}
	
	public String toString() {
		return " *** " + title;			
	}
	
	public int getTopPositionOnScreen() {
		
		int[] location = new int[2];
		
		getLocationOnScreen(location);
		
		return location[1];
	}

}

