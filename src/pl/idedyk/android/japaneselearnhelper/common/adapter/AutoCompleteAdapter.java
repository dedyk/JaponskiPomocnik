package pl.idedyk.android.japaneselearnhelper.common.adapter;

import java.util.ArrayList;
import java.util.List;

import android.R;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient;
import pl.idedyk.android.japaneselearnhelper.serverclient.ServerClient.AutoCompleteSuggestionType;

public class AutoCompleteAdapter extends BaseAdapter implements Filterable {

	private Context context;
	
	private List<String> resultList = new ArrayList<String>();
	
	@SuppressWarnings("unused")
	private AutoCompleteSuggestionType autoCompleteSuggestionType;
	
	public AutoCompleteAdapter(Context context, AutoCompleteSuggestionType autoCompleteSuggestionType) {
		this.context = context;
		this.autoCompleteSuggestionType = autoCompleteSuggestionType;
	}
	
    @Override
    public int getCount() {
        return resultList.size();
    }

    @Override
    public String getItem(int index) {
        return resultList.get(index);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
        if (convertView == null) {
        	
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            
            convertView = inflater.inflate(pl.idedyk.android.japaneselearnhelper.R.layout.word_dictionary_autocomplete_dropdown_item, parent, false);
        }
        
        ((TextView) convertView.findViewById(android.R.id.text1)).setText(getItem(position));
        
        return convertView;
    }
    
    @Override
    public Filter getFilter() {
    	
        Filter filter = new Filter() {
            
        	@Override
            protected FilterResults performFiltering(CharSequence constraint) {
                
        		FilterResults filterResults = new FilterResults();
                
        		if (constraint != null) {
        			     
					PackageInfo packageInfo = null;
			        
			        try {
			        	packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
			        	
			        } catch (NameNotFoundException e) {        	
			        }
					
					ServerClient serverClient = new ServerClient();
										
					List<String> autoComplete = serverClient.getAutoComplete(packageInfo, constraint.toString(), autoCompleteSuggestionType);
										
                    filterResults.values = autoComplete;
                    filterResults.count = autoComplete.size();
                }
                
        		return filterResults;
            }

            @SuppressWarnings("unchecked")
			@Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
            	
                if (results != null && results.count > 0) {
                    
                	resultList = (List<String>) results.values;
                    
                	notifyDataSetChanged();
                	
                } else {
                	
                    notifyDataSetInvalidated();
                }
            }};
            
        return filter;
    }
}
