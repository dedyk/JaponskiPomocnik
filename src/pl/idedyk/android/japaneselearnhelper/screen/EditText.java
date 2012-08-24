package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class EditText extends android.widget.EditText {
	
	public EditText(Context context) {
		super(context);
		
		throw new RuntimeException("Please do not use");
	}
	
	public EditText(Context context, ViewGroup layout) {
		super(context);
		
		if (layout instanceof android.widget.TableRow) {		
			setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT));
			
		} else if (layout instanceof LinearLayout) {
			setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
		} else {
			throw new RuntimeException();
		}
	}
	
	public String toString() {
		
		String value = getText().toString();
				
		return value;
	}

}
