package pl.idedyk.android.japaneselearnhelper.screen;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class GroupScreenItem implements IScreenItem {

	private List<IScreenItem> screenItems = new ArrayList<IScreenItem>();
	
	private int level;
	
	private OnClickListener onClickListener;
	
	public GroupScreenItem(int level) {
		this.level = level;
	}

	public void add(IScreenItem screenItem) {
		screenItems.add(screenItem);
	}
	
	public void generate(Context context, Resources resources, ViewGroup layout) {
		
		android.widget.LinearLayout linearLayout = new android.widget.TableLayout(context);
		
		LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		
		layoutParam.setMargins(20 + level * 20, 5, 0, 0);
		
		linearLayout.setLayoutParams(layoutParam);
				
		for (IScreenItem currentScreenItem : screenItems) {
			currentScreenItem.generate(context, resources, linearLayout);
		}
		
		linearLayout.setOrientation(android.widget.LinearLayout.VERTICAL);
		
		linearLayout.setOnClickListener(onClickListener);
		
		layout.addView(linearLayout);
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (IScreenItem currentScreenItem : screenItems) {
			sb.append(currentScreenItem.toString()).append("\n");
		}
		
		return sb.toString();
	}
	
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;		
	}
}
