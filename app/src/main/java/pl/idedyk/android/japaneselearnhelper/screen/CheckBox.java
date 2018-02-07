package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

public class CheckBox implements IScreenItem {
	
	private android.widget.CheckBox checkBox;
	
	public CheckBox(Context context, String checkBoxText, boolean defaultSelected, int id) {
		
		checkBox = new android.widget.CheckBox(context);
		
		checkBox.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		checkBox.setId(id);
		checkBox.setText(checkBoxText);
		checkBox.setTextSize(12.0f);
		checkBox.setChecked(defaultSelected);		
	}
	
	public boolean isChecked() {
		return checkBox.isChecked();
	}
	
	public void setOnClickListener(OnClickListener onClickListener) {
		checkBox.setOnClickListener(onClickListener);
	}

	public void generate(Context context, Resources resources, ViewGroup layout) {
		layout.addView(checkBox);
	}
	
	public int getY() {
		
		if (checkBox != null) {
			return checkBox.getTop();
		}
		
		throw new RuntimeException("checkBox is null");
	}
	
	public String toString() {
		return checkBox.getText() + " - " + checkBox.isChecked();
	}

	public void setEnabled(boolean enabled) {
		checkBox.setEnabled(enabled);		
	}
}
