package pl.idedyk.android.japaneselearnhelper.screen;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.widget.TableLayout.LayoutParams;

public class TableLayout implements IScreenItem {
	
	public static enum LayoutParam {
		
		FillParent_WrapContent(new android.widget.TableLayout.LayoutParams(
				android.widget.TableLayout.LayoutParams.FILL_PARENT, android.widget.TableLayout.LayoutParams.WRAP_CONTENT)),
		
		WrapContent_WrapContent(new android.widget.TableLayout.LayoutParams(
				android.widget.TableLayout.LayoutParams.WRAP_CONTENT, android.widget.TableLayout.LayoutParams.WRAP_CONTENT));
		
		private android.widget.TableLayout.LayoutParams layoutParam;

		private LayoutParam(LayoutParams layoutParam) {
			this.layoutParam = layoutParam;
		}

		public android.widget.TableLayout.LayoutParams getLayoutParam() {
			return layoutParam;
		}
	}
		
	private LayoutParam layoutParam;

	private Boolean shrinkAllColumns;
	private Boolean stretchAllColumns;
	
	private List<TableRow> tableRows = new ArrayList<TableRow>();
	
	public TableLayout(LayoutParam layoutParam, Boolean shrinkAllColumns, Boolean stretchAllColumns) {
		this.layoutParam = layoutParam;
		
		this.shrinkAllColumns = shrinkAllColumns;
		this.stretchAllColumns = stretchAllColumns;
	}
	
	public void addTableRow(TableRow tableRow) {
		tableRows.add(tableRow);
	}
		
	public void generate(Context context, Resources resources, ViewGroup layout) {
		
		android.widget.TableLayout tableLayout = new android.widget.TableLayout(context);
				
		tableLayout.setLayoutParams(layoutParam.getLayoutParam());
		
		if (shrinkAllColumns != null) {
			tableLayout.setShrinkAllColumns(shrinkAllColumns.booleanValue());
		}
		
		if (stretchAllColumns != null) {
			tableLayout.setStretchAllColumns(stretchAllColumns.booleanValue());
		}
		
		for (TableRow currentTableRow : tableRows) {
			currentTableRow.generate(context, resources, tableLayout);
		}
		
		layout.addView(tableLayout);		
	}
	
	public int getY() {
		
		if (tableRows != null && tableRows.size() > 0) {
			return tableRows.get(0).getY();
		}
		
		throw new RuntimeException("tableRows is null");
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		
		for (TableRow currentTableRow : tableRows) {
			sb.append(currentTableRow.toString()).append("\n");
		}
		
		return sb.toString();
	}
}
