package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem.ItemType;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class KanjiEntryListItemAdapter extends ArrayAdapter<KanjiEntryListItem> {
	
    private Context context;
    
    private int layoutResourceId;   
    
    private List<KanjiEntryListItem> data = null;
	
    private Typeface radicalTypeface;
    
    public KanjiEntryListItemAdapter(Context context, int layoutResourceId, List<KanjiEntryListItem> data, Typeface radicalTypeface) {
        super(context, layoutResourceId, data);
        
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
        this.radicalTypeface = radicalTypeface;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	KanjiEntryListItemHolder holder = null;
       
        if (convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new KanjiEntryListItemHolder();
            
            holder.kanjiEntryListItemHolderValue = (TextView)convertView.findViewById(R.id.kanji_entry_simpletow_value);
            holder.kanjiEntryListItemHolderRadicalValue = (TextView)convertView.findViewById(R.id.kanji_entry_simpletow_radical_value);
           
            convertView.setTag(holder);
        } else {
            holder = (KanjiEntryListItemHolder)convertView.getTag();
        }
       
        KanjiEntryListItem currentKanjiEntryListItem = data.get(position);
        
        ItemType itemType = currentKanjiEntryListItem.getItemType();
                
        holder.kanjiEntryListItemHolderValue.setText(currentKanjiEntryListItem.getText(), TextView.BufferType.SPANNABLE);
        
        if (itemType == ItemType.KANJI_ENTRY) {
        	
            holder.kanjiEntryListItemHolderRadicalValue.setText(currentKanjiEntryListItem.getRadicalText(), TextView.BufferType.SPANNABLE);
            holder.kanjiEntryListItemHolderRadicalValue.setTypeface(radicalTypeface);

        } else {        	
        	holder.kanjiEntryListItemHolderRadicalValue.setVisibility(View.GONE);        	
        }        
        
        return convertView;
    }
    
    public int size() {
    	return data.size();
    }
    
    @Override
    public boolean isEnabled(int position) {

    	if (position < 0 || position >= data.size()) {
    		return false;
    	}

    	KanjiEntryListItem kanjiEntryListItem = data.get(position);

    	ItemType itemType = kanjiEntryListItem.getItemType();

    	if (itemType == ItemType.KANJI_ENTRY || itemType == ItemType.SUGGESTION_VALUE) {
    		return true;

    	} else {
    		return false;

    	}
    }
    
    static private class KanjiEntryListItemHolder {
    	TextView kanjiEntryListItemHolderValue;    	
    	TextView kanjiEntryListItemHolderRadicalValue;
    }
}
