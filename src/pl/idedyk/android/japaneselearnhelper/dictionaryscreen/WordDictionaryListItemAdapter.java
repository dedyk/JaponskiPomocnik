package pl.idedyk.android.japaneselearnhelper.dictionaryscreen;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionaryscreen.WordDictionaryListItem.ItemType;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class WordDictionaryListItemAdapter extends ArrayAdapter<WordDictionaryListItem> {
	
    private Context context;
    private int layoutResourceId;   
    private List<WordDictionaryListItem> data = null;
	
    public WordDictionaryListItemAdapter(Context context, int layoutResourceId, List<WordDictionaryListItem> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
        WordDictionaryListItemHolder holder = null;
       
        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new WordDictionaryListItemHolder();
            
            holder.wordDictionaryListItemHolderValue = (TextView)convertView.findViewById(R.id.word_dictionary_simpletow_value);
           
            convertView.setTag(holder);
        } else {
            holder = (WordDictionaryListItemHolder)convertView.getTag();
        }
       
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
