package pl.idedyk.android.japaneselearnhelper.screen;

import pl.idedyk.android.japaneselearnhelper.R;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.TextView;

public class TitleItem implements IScreenItem {

	private String title;
	
	public TitleItem(String title) {
		this.title = title;
	}

	public void generate(Context context, Resources resources, ViewGroup layout) {
		TextView textView = new TextView(context);
		
		textView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		textView.setBackgroundColor(resources.getColor(R.color.word_dictionary_details_title_background_color));
		textView.setTextSize(16.0f);
		textView.setText(title);		
		
		layout.addView(textView);
	}
	
	public String toString() {
		return " *** " + title;			
	}
}

