package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.widget.RadioButton;

public class RadioGroup extends android.widget.RadioGroup {
	
	public RadioGroup(Context context) {
		super(context);
		
		setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
	}	
	
	public void addRadioButton(Context context, String radioButtonText, int radioButtonId, boolean defaultSelected) {
		
		RadioButton radioButton = generateRadioButton(context, radioButtonId, radioButtonText);
		
		addView(radioButton);
		
		if (defaultSelected == true) {
			check(radioButtonId);
		}
	}
	
	private RadioButton generateRadioButton(Context context, int radioButtonId, String radioButtonText) {
		
		RadioButton radioButton = new RadioButton(context);
		
		radioButton.setId(radioButtonId);
		radioButton.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
		radioButton.setText(radioButtonText);
		radioButton.setTextSize(16.0f);
		
		return radioButton;
	}
		
	public String toString() {
		
		StringBuffer sb = new StringBuffer(" *** \n\n");
		
		int childCount = getChildCount();
		
		for (int idx = 0; idx < childCount; ++idx) {
			
			RadioButton currentRadioButton = (RadioButton)getChildAt(idx);
			
			sb.append(currentRadioButton.getText().toString() + " - " + currentRadioButton.isChecked() + "\n");
		}
		
		return sb.toString();
	}
}
