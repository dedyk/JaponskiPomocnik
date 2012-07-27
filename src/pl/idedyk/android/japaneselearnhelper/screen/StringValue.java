package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spanned;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StringValue implements IScreenItem {
	
	private CharSequence value;
	
	private float textSize;
	
	private Integer gravity;
	
	private int level;
	
	public StringValue(String value, float textSize, int level) {
		this.value = value;
		this.textSize = textSize;
		this.level = level;
	}

	public StringValue(Spanned value, float textSize, int level) {
		this.value = value;
		this.textSize = textSize;
		this.level = level;
	}
	
	public void setGravity(int gravity) {
		this.gravity = gravity;
	}

	public void generate(Context context, Resources resources, ViewGroup layout) {
		TextView textView = new TextView(context);
		
		if (layout instanceof android.widget.TableRow) {
			android.widget.TableRow.LayoutParams layoutParam = new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT);
			layoutParam.setMargins(0, 0, 0, 0);
			
			textView.setLayoutParams(layoutParam);
		} else if (layout instanceof LinearLayout) {
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			layoutParam.setMargins(20 + level * 20, 5, 0, 0);
			
			textView.setLayoutParams(layoutParam);
		} else {
			throw new RuntimeException();
		}
		
		if (gravity != null) {
			textView.setGravity(gravity);
		}
		
		textView.setTextSize(textSize);
		textView.setText(value);
		
		layout.addView(textView);			
	}
	
	public String toString() {
		return value.toString();
	}
}
