package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class KanjiEntryListItemAdapter extends ArrayAdapter<KanjiEntryListItem> {
	
    private Context context;
    private int layoutResourceId;   
    private List<KanjiEntryListItem> data = null;
	
    public KanjiEntryListItemAdapter(Context context, int layoutResourceId, List<KanjiEntryListItem> data) {
        super(context, layoutResourceId, data);
        
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	KanjiEntryListItemHolder holder = null;
       
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new KanjiEntryListItemHolder();
            
            holder.kanjiEntryListItemHolderValue = (TextView)convertView.findViewById(R.id.kanji_entry_simpletow_value);
           
            convertView.setTag(holder);
        } else {
            holder = (KanjiEntryListItemHolder)convertView.getTag();
        }
       
        KanjiEntryListItem currentKanjiEntryListItem = data.get(position);
                
        holder.kanjiEntryListItemHolderValue.setText(currentKanjiEntryListItem.getText(), TextView.BufferType.SPANNABLE);
       
        return convertView;
    }
    
    public int size() {
    	return data.size();
    }
    
    static private class KanjiEntryListItemHolder {
    	TextView kanjiEntryListItemHolderValue;    	
    }
}
