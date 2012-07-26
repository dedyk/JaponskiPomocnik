package pl.idedyk.android.japaneselearnhelper;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MainMenuListItemAdapter extends ArrayAdapter<MainMenuItem> {
	
    private Context context;
    private int layoutResourceId;   
    private List<MainMenuItem> data = null;
	
    public MainMenuListItemAdapter(Context context, int layoutResourceId, List<MainMenuItem> data) {
    	
        super(context, layoutResourceId, data);
        
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }
    
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
    	
    	MainMenuListItemHolder holder = null;
       
        if(convertView == null) {
            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(layoutResourceId, parent, false);
           
            holder = new MainMenuListItemHolder();
            
            holder.mainMenuListItemKanjiHolderValue = (TextView)convertView.findViewById(R.id.main_menu_simplerow_kanjitext);
            holder.mainMenuListItemTextHolderValue = (TextView)convertView.findViewById(R.id.main_menu_simplerow_rowtext);
           
            convertView.setTag(holder);
        } else {
            holder = (MainMenuListItemHolder)convertView.getTag();
        }
       
        MainMenuItem currentMainMenuListItem = data.get(position);
                
        holder.mainMenuListItemKanjiHolderValue.setText(currentMainMenuListItem.getKanji());
        holder.mainMenuListItemTextHolderValue.setText(currentMainMenuListItem.getText());
       
        return convertView;
    }
    
    public int size() {
    	return data.size();
    }
    
    static private class MainMenuListItemHolder {
    	TextView mainMenuListItemKanjiHolderValue;
    	TextView mainMenuListItemTextHolderValue;
    }
}
