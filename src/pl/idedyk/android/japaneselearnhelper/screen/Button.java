package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class Button implements IScreenItem {
	
	private String text;
	
	private OnClickListener onClickListener;
	
	public Button(String text) {
		this.text = text;
	}

	public void generate(Context context, Resources resources, ViewGroup layout) {
		
		android.widget.Button button = new android.widget.Button(context);
		
		button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		button.setText(text);
		
		button.setOnClickListener(onClickListener);
		
		layout.addView(button);		
	}

	public String toString() {
		return "***" + text + "***";
	}

	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;		
	}
}
