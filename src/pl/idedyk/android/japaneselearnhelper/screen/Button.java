package pl.idedyk.android.japaneselearnhelper.screen;

import pl.idedyk.android.japaneselearnhelper.R;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class Button implements IScreenItem {
	
	private android.widget.Button button;
	
	private String text;
	
	private Float textSize;
	
	private OnClickListener onClickListener;
	
	public Button(String text) {
		this.text = text;
	}
	
	public void setText(String text) {
		this.text = text;
		
		if (button != null) {
			button.setText(text);
		}
	}
	
	public String getText() {
		return text;
	}
	
	public void setTextSize(Float textSize) {
		this.textSize = textSize;

		if (button != null) {
			button.setTextSize(textSize);
		}
	}

	public void generate(Context context, Resources resources, ViewGroup layout) {
		
		LayoutInflater layoutInflater = ((Activity)context).getLayoutInflater();
		
		LinearLayout linearLayoutInflate = (LinearLayout)layoutInflater.inflate(R.layout.button_wrapper_layout, null);
		
		button = (android.widget.Button)linearLayoutInflate.getChildAt(0);
		
		linearLayoutInflate.removeViewAt(0);
				
		if (layout instanceof android.widget.TableRow) {		
			button.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT));
			
		} else if (layout instanceof LinearLayout) {
			button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
		} else {
			throw new RuntimeException(String.valueOf(layout.getClass()));
		}
		
		button.setText(text);
		
		if (textSize != null) {
			button.setTextSize(textSize);
		}
		
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
