package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.RadioButton;

public class RadioGroup implements IScreenItem {

	private android.widget.RadioGroup radioGroup = null;
	
	public RadioGroup(Context context) {
		
		radioGroup = new android.widget.RadioGroup(context);
		
		radioGroup.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}	
	
	public void addRadioButton(Context context, String radioButtonText, int radioButtonId, boolean defaultSelected) {
		
		RadioButton radioButton = generateRadioButton(context, radioButtonId, radioButtonText);
		
		radioGroup.addView(radioButton);
		
		if (defaultSelected == true) {
			radioGroup.check(radioButtonId);
		}
	}
	
	public void generate(Context context, Resources resources, ViewGroup layout) {		
		layout.addView(radioGroup);
	}
	
	private RadioButton generateRadioButton(Context context, int radioButtonId, String radioButtonText) {
		
		RadioButton radioButton = new RadioButton(context);
		
		radioButton.setId(radioButtonId);
		radioButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		radioButton.setText(radioButtonText);
		radioButton.setTextSize(12.0f);
		
		return radioButton;
	}
	
	public int getCheckedRadioButtonId() {
		return radioGroup.getCheckedRadioButtonId();
	}
	
	public String toString() {
		
		StringBuffer sb = new StringBuffer(" *** \n\n");
		
		int childCount = radioGroup.getChildCount();
		
		for (int idx = 0; idx < childCount; ++idx) {
			
			RadioButton currentRadioButton = (RadioButton)radioGroup.getChildAt(idx);
			
			sb.append(currentRadioButton.getText().toString() + " - " + currentRadioButton.isChecked() + "\n");
		}
		
		return sb.toString();
	}
}
