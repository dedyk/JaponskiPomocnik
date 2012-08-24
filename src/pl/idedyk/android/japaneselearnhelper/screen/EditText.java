package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class EditText implements IScreenItem {

	private android.widget.EditText editText;
		
	public void generate(Context context, Resources resources, ViewGroup layout) {
		
		editText = new android.widget.EditText(context);
		
		if (layout instanceof android.widget.TableRow) {		
			editText.setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT));
			
		} else if (layout instanceof LinearLayout) {
			editText.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
		} else {
			throw new RuntimeException();
		}
	
		layout.addView(editText);
	}
	
	public String toString() {
		
		String value = "";
		
		if (editText != null) {
			value = editText.getText().toString();
		}
		
		return value;
	}

}
