package pl.idedyk.android.japaneselearnhelper.screen;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;

public class TableRow implements IScreenItem {
	
	private android.widget.TableRow tableRow = null;
	
	private List<IScreenItem> screenItems = new ArrayList<IScreenItem>();
	
	public void addScreenItem(IScreenItem item) {
		screenItems.add(item);
	}
	
	public int getScreenItemSize() {
		return screenItems.size();
	}

	public void generate(Context context, Resources resources, ViewGroup layout) {
		
		if (screenItems.size() > 0) {
			tableRow = new android.widget.TableRow(context);
						
			for (IScreenItem currentScreenItem : screenItems) {
				currentScreenItem.generate(context, resources, tableRow);
			}
			
			layout.addView(tableRow);			
		}
	}
	
	public int getY() {
		
		if (tableRow != null) {
			return tableRow.getTop();
		}
		
		throw new RuntimeException("tableRow is null");
	}
	
	public String toString() {
		
		StringBuffer sb = new StringBuffer();
		
		if (screenItems.size() > 0) {
			for (IScreenItem currentScreenItem : screenItems) {
				sb.append(currentScreenItem.toString().replaceAll("\n", " ")).append("; ");
			}
		}
		
		return sb.toString();
	}
}
