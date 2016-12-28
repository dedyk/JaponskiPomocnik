package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryListItem.ItemType;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class WordDictionaryListItemAdapter extends BaseAdapter { // ArrayAdapter<WordDictionaryListItem> {
	
    private Context context;
    
    private List<WordDictionaryListItem> data = null;
	
    public WordDictionaryListItemAdapter(Context context, List<WordDictionaryListItem> data) {    	
        this.context = context;
        this.data = data;
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

    	WordDictionaryListItemHolder holder = null;

    	//
    	
    	int itemViewType = getItemViewType(position);

    	if(convertView == null) {
    		
    		LayoutInflater inflater = ((Activity)context).getLayoutInflater();
    		convertView = inflater.inflate(ItemType.getLayoutResourceId(itemViewType), parent, false);

    		holder = new WordDictionaryListItemHolder();

    		holder.wordDictionaryListItemHolderValue = (TextView)convertView.findViewById(R.id.word_dictionary_simpletow_value);

    		convertView.setTag(holder);

    	} else {
    		holder = (WordDictionaryListItemHolder)convertView.getTag();
    	}
    	
    	//
    	
    	WordDictionaryListItem currentWordDictionaryListItem = data.get(position);

    	holder.wordDictionaryListItemHolderValue.setText(currentWordDictionaryListItem.getText(), TextView.BufferType.SPANNABLE);
    	
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
    	
    	WordDictionaryListItem wordDictionaryListItem = data.get(position);
    	
    	ItemType itemType = wordDictionaryListItem.getItemType();
    	
    	if (itemType == ItemType.RESULT_ITEM || itemType == ItemType.SUGGESTION_VALUE) {
    		return true;
    		
    	} else {
    		return false;
    		
    	}
    }
    
    static private class WordDictionaryListItemHolder {
    	TextView wordDictionaryListItemHolderValue;    	
    }
}
