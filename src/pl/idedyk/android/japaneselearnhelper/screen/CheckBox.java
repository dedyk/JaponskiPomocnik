package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.view.ViewGroup.LayoutParams;

public class CheckBox extends android.widget.CheckBox {
	
	public CheckBox(Context context) {
		super(context);
		
		throw new RuntimeException("Please do not use");
	}
	
	public CheckBox(Context context, String checkBoxText, boolean defaultSelected, int id) {
		
		super(context);
		
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		
		setId(id);
		setText(checkBoxText);
		setTextSize(16.0f);
		setChecked(defaultSelected);		
	}
		
	public String toString() {
		return getText() + " - " + isChecked();
	}
}
