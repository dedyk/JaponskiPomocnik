package pl.idedyk.android.japaneselearnhelper.screen;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;

public class TableLayout implements IScreenItem {

	private List<TableRow> tableRows = new ArrayList<TableRow>();
	
	public void addTableRow(TableRow tableRow) {
		tableRows.add(tableRow);
	}
	
	public void generate(Context context, Resources resources, ViewGroup layout) {
		
		android.widget.TableLayout tableLayout = new android.widget.TableLayout(context);
		
		android.widget.TableLayout.LayoutParams layoutParam = new android.widget.TableLayout.LayoutParams(
				android.widget.TableLayout.LayoutParams.FILL_PARENT, android.widget.TableLayout.LayoutParams.WRAP_CONTENT);
		
		tableLayout.setLayoutParams(layoutParam);
		
		tableLayout.setShrinkAllColumns(true);
		tableLayout.setStretchAllColumns(true);
		
		for (TableRow currentTableRow : tableRows) {
			currentTableRow.generate(context, resources, tableLayout);
		}
		
		layout.addView(tableLayout);		
	}
	
	public String toString() {
		return "";
	}
}
