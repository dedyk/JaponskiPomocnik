package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spanned;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StringValue extends TextView {
		
	private float textSize;
	
	private int level;
	
	private boolean nullMargins = false;
	
	private Integer layoutWeight;
	
	private Integer defaultTextColor;
	
	public StringValue(Context context) {
		super(context);
		
		throw new RuntimeException("Please do not use");
	}

	public StringValue(Context context, ViewGroup layout, String text, float textSize, int level) {
		super(context);
		
		setText(text);
		setTextSize(textSize);
		
		setLayout(layout);
		
		this.level = level;
	}

	public StringValue(Context context, ViewGroup layout, Spanned text, float textSize, int level) {
		super(context);
		
		setText(text);
		setTextSize(textSize);
		
		setLayout(layout);
		
		this.level = level;
	}
	
	private void setLayout(ViewGroup layout) {
		
		if (layout instanceof android.widget.TableRow) {
			android.widget.TableRow.LayoutParams layoutParam = new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT);
			
			if (nullMargins == true) {
				layoutParam.setMargins(0, 0, 0, 0);
			} else {
				layoutParam.setMargins(20 + level * 20, 5, 0, 0);
			}

			if (layoutWeight != null) {
				layoutParam.weight = layoutWeight;
			}
			
			setLayoutParams(layoutParam);
		} else if (layout instanceof LinearLayout) {
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			if (nullMargins == false) {
				layoutParam.setMargins(20 + level * 20, 5, 0, 0);
			}
			
			if (layoutWeight != null) {
				layoutParam.weight = layoutWeight;
			}
			
			setLayoutParams(layoutParam);
		} else {
			throw new RuntimeException();
		}		
	}
		
	public void setGravity(int gravity) {
		this.gravity = gravity;
	}
	
	public void setLayoutWidth(Integer layoutWeight) {
		this.layoutWeight = layoutWeight;
	}
	
	public void setNullMargins(boolean nullMargins) {
		this.nullMargins = nullMargins;
	}

	public void generate(Context context, Resources resources, ViewGroup layout) {
		textView = new TextView(context);
		
		
		textView.setTextSize(textSize);
		textView.setText(value);
		
		defaultTextColor = textView.getTextColors().getDefaultColor();		
		
		layout.addView(textView);			
	}
	
	public String toString() {
		return getText().toString();
	}
	
	public int getBottomPositionOnScreen() {
		
		int[] location = new int[2];
		
		getLocationOnScreen(location);
		
		return location[1] + getHeight();
	}
	
	public Integer getDefaultTextColor() {
		return defaultTextColor;
	}
}
