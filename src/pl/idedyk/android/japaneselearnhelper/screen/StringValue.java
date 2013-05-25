package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.Spanned;
import android.view.View.OnTouchListener;
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
	
	private Integer marginLeft = null;
	private Integer marginTop = null;
	private Integer marginRight = null;
	private Integer marginBottom = null;
	
	private Integer layoutWeight;
	
	private OnClickListener onClickListener;
	
	private OnTouchListener onTouchListener;
	
	private Integer textColor;
	
	private Integer defaultTextColor;
	
	private Typeface typeface;
	
	private Integer backgroundColor;

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
	
	public String getValue() {
		return this.value.toString();
	}
	
	public void setGravity(int gravity) {
		this.gravity = gravity;
	}
	
	public void setLayoutWidth(Integer layoutWeight) {
		this.layoutWeight = layoutWeight;
	}
	
	public void setNullMargins(boolean nullMargins) {
		
		if (nullMargins == true) {		
			this.marginLeft = 0;
			this.marginTop = 0;
			this.marginRight = 0;
			this.marginBottom = 0;
			
		} else {
			this.marginLeft = null;
			this.marginTop = null;
			this.marginRight = null;
			this.marginBottom = null;			
		}
	}
	
	public void setTypeface(Typeface typeface) {
		this.typeface = typeface;
	}
	
	public void setBackgroundColor(Integer backgroundColor) {
		this.backgroundColor = backgroundColor;
	}

	public void generate(Context context, Resources resources, ViewGroup layout) {
		textView = new TextView(context);
		
		if (layout instanceof android.widget.TableRow) {
			android.widget.TableRow.LayoutParams layoutParam = new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT);
			
			if (marginLeft != null || marginTop != null || marginRight != null || marginBottom != null) {
				layoutParam.setMargins(marginLeft != null ? marginLeft : 0, marginTop != null ? marginTop : 0,
						marginRight != null ? marginRight : 0, marginRight != null ? marginRight : 0);
			} else {
				layoutParam.setMargins(20 + level * 20, 5, 0, 0);
			}

			if (layoutWeight != null) {
				layoutParam.weight = layoutWeight;
			}
			
			if (gravity != null) {
				layoutParam.gravity = gravity;
			}
			
			textView.setLayoutParams(layoutParam);
		} else if (layout instanceof LinearLayout) {
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
			
			if (marginLeft == null && marginTop == null && marginRight == null && marginBottom == null) {
				layoutParam.setMargins(20 + level * 20, 5, 0, 0);
			}
			
			if (layoutWeight != null) {
				layoutParam.weight = layoutWeight;
			}
			
			textView.setLayoutParams(layoutParam);
		} else {
			throw new RuntimeException(String.valueOf(layout.getClass()));
		}
		
		if (gravity != null) {
			textView.setGravity(gravity);
		}
		
		textView.setTextSize(textSize);
		textView.setText(value);
		
		defaultTextColor = textView.getTextColors().getDefaultColor();
		
		if (textColor != null) {
			textView.setTextColor(textColor.intValue());
		}
		
		if (typeface != null) {
			textView.setTypeface(typeface);
		}
		
		if (backgroundColor != null) {
			textView.setBackgroundColor(backgroundColor);
		}
		
		textView.setOnClickListener(onClickListener);
		textView.setOnTouchListener(onTouchListener);
				
		layout.addView(textView);			
	}
	
	public void setMarginLeft(Integer marginLeft) {
		this.marginLeft = marginLeft;
	}

	public void setMarginTop(Integer marginTop) {
		this.marginTop = marginTop;
	}

	public void setMarginRight(Integer marginRight) {
		this.marginRight = marginRight;
	}

	public void setMarginBottom(Integer marginBottom) {
		this.marginBottom = marginBottom;
	}

	public String toString() {
		if (value != null) {
			return value.toString();
		} else {
			return null;
		}
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

	public void setOnTouchListener(OnTouchListener onTouchListener) {
		this.onTouchListener = onTouchListener;
	}
	
	public void setText(String text) {
		this.value = text;
		
		if (textView != null) {
			textView.setText(text);
		}
	}
	
	public void setTextColor(Integer textColor) {
		this.textColor = textColor;
		
		if (textView != null) {
			textView.setTextColor(textColor.intValue());
		}
	}
	
	public int getY() {
		
		if (textView != null) {
			return textView.getTop();
		}
		
		throw new RuntimeException("textview is null");
	}

	public Integer getDefaultTextColor() {
		return defaultTextColor;
	}
}
