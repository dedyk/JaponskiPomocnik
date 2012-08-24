package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class Button extends android.widget.Button {
		
	public Button(Context context) {
		super(context);
		
		throw new RuntimeException("Please do not use");
	}
	
	public Button(Context context, ViewGroup layout, String text) {
		super(context);
		
		if (layout instanceof android.widget.TableRow) {		
			setLayoutParams(new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.MATCH_PARENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT));
			
		} else if (layout instanceof LinearLayout) {
			setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			
		} else {
			throw new RuntimeException();
		}
		
		setText(text);
	}

	public String toString() {
		return "***" + getText() + "***";
	}
}
