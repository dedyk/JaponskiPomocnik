package pl.idedyk.android.japaneselearnhelper.kanji;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.kanji.KanjiEntryListItem.ItemType;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class KanjiEntryListItemAdapter extends BaseAdapter { // extends ArrayAdapter<KanjiEntryListItem> {
	
    private Context context;
        
    private List<KanjiEntryListItem> data = null;
	
    private Typeface radicalTypeface;
    
    public KanjiEntryListItemAdapter(Context context, List<KanjiEntryListItem> data, Typeface radicalTypeface) {
        
        this.context = context;
        this.data = data;
        this.radicalTypeface = radicalTypeface;
    }
    
    @Override
    public int getCount() {
    	return data.size();
    }

    @Override
    public long getItemId(int position) {
    	return position;
    }

    @Override
    public Object getItem(int position) {
    	return data.get(position);
    }

    @Override
    public int getViewTypeCount() {

    	ItemType[] itemTypeValues = ItemType.values();

    	Set<Integer> viewTypeCodeList = new HashSet<Integer>();

    	for (ItemType itemType : itemTypeValues) {
    		viewTypeCodeList.add(itemType.getViewTypeId());
    	}

    	return viewTypeCodeList.size();
    }

    @Override
    public int getItemViewType(int position) {
    	return data.get(position).getItemType().getViewTypeId();
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	KanjiEntryListItemHolder holder = null;
    	
    	int itemViewType = getItemViewType(position);
       
        if (convertView == null) {
        	
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(ItemType.getLayoutResourceId(itemViewType), parent, false);
           
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

            holder.kanjiEntryListItemHolderRadicalValue.setVisibility(View.VISIBLE);
            
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

    	if (itemType == ItemType.KANJI_ENTRY || itemType == ItemType.SUGGESTION_VALUE || itemType == ItemType.SHOW_HISTORY_VALUE) {
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
