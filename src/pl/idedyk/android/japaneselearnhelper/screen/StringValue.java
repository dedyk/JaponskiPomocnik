package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.text.Spanned;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;

public class StringValue implements IScreenItem {
	
	private TextView textView;
	
	private CharSequence value;
	
	private float textSize;
	
	private Integer gravity;
	
	private int level;
	
	private boolean nullMargins = false;
	
	private Integer layoutWeight;
	
	private OnClickListener onClickListener;
	
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
	
	public void setValue(CharSequence value) {
		this.value = value;
		
		if (textView != null) {
			textView.setText(this.value);
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
		
		if (layout instanceof android.widget.TableRow) {
			android.widget.TableRow.LayoutParams layoutParam = new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT);
			layoutParam.setMargins(0, 0, 0, 0);

			if (layoutWeight != null) {
				layoutParam.weight = layoutWeight;
			}
			
			textView.setLayoutParams(layoutParam);
		} else if (layout instanceof LinearLayout) {
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			if (nullMargins == false) {
				layoutParam.setMargins(20 + level * 20, 5, 0, 0);
			}
			
			if (layoutWeight != null) {
				layoutParam.weight = layoutWeight;
			}
			
			textView.setLayoutParams(layoutParam);
		} else {
			throw new RuntimeException();
		}
		
		if (gravity != null) {
			textView.setGravity(gravity);
		}
		
		textView.setTextSize(textSize);
		textView.setText(value);
		
		textView.setOnClickListener(onClickListener);
		
		layout.addView(textView);			
	}
	
	public String toString() {
		return value.toString();
	}
	
	public int getBottomPositionOnScreen() {
		if (textView == null) {
			throw new RuntimeException("getBottomPositionOnScreen");
		}
		
		int[] location = new int[2];
		
		textView.getLocationOnScreen(location);
		
		return location[1] + textView.getHeight();
	}
	
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;		
	}
}
