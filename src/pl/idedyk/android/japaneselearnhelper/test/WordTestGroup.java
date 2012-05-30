package pl.idedyk.android.japaneselearnhelper.test;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.DictionaryManager;
import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class WordTestGroup extends Activity {

	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.word_test_group);

		DictionaryManager dictionaryManager = DictionaryManager.getInstance();
		
		// create menu list

		ListView groupList = (ListView)findViewById(R.id.word_test_group_list);

		List<GroupItem> groupListItems = new ArrayList<GroupItem>();
		
		int wordGroupsNo = dictionaryManager.getWordGroupsNo();
		
		for (int idx = 0; idx < wordGroupsNo; ++idx) {
			groupListItems.add(new GroupItem(getResources(), idx, false));
		}

		ArrayAdapter<GroupItem> groupListItemsAdapter = new GroupListArrayAdapter(this, groupListItems);

		groupList.setAdapter(groupListItemsAdapter);
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
	}

	private static class GroupListArrayAdapter extends ArrayAdapter<GroupItem> {

		private LayoutInflater inflater;

		public GroupListArrayAdapter(Context context, List<GroupItem> groupList) {
			super(context, R.layout.word_test_group_simplerow, R.id.word_test_group_simplerow_rowtext, groupList);

			inflater = LayoutInflater.from(context) ;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			GroupItem currentGroupItem = (GroupItem)getItem(position); 

			CheckBox currentGroupCheckBox = null; 
			TextView currentGroupTextView = null;

			if (convertView == null ) {
				convertView = inflater.inflate(R.layout.word_test_group_simplerow, null);

				currentGroupTextView = (TextView) convertView.findViewById(R.id.word_test_group_simplerow_rowtext);
				currentGroupCheckBox = (CheckBox) convertView.findViewById(R.id.word_test_group_simplerow_checkbox);

				convertView.setTag(new GroupItemViewHolder(currentGroupTextView, currentGroupCheckBox));

				// If CheckBox is toggled, update the planet it is tagged with.
				currentGroupCheckBox.setOnClickListener(new View.OnClickListener() {
					
					public void onClick(View view) {
						CheckBox cb = (CheckBox) view;
						
						GroupItem groupItem = (GroupItem) cb.getTag();
						
						groupItem.setChecked(cb.isChecked());
					}
				});      
			} else {
				GroupItemViewHolder viewHolder = (GroupItemViewHolder) convertView.getTag();
				
				currentGroupCheckBox = viewHolder.getCheckBox();
				currentGroupTextView = viewHolder.getTextView();
			}

			currentGroupCheckBox.setTag(currentGroupItem); 

			// Display planet data
			currentGroupCheckBox.setChecked(currentGroupItem.isChecked());
			currentGroupTextView.setText(currentGroupItem.getText());      

			return convertView;
		}
	}

	private static class GroupItemViewHolder {
		private CheckBox checkBox;
		private TextView textView;
		
		public GroupItemViewHolder(TextView textView, CheckBox checkBox) {
			this.checkBox = checkBox;
			this.textView = textView;
		}
		
		public CheckBox getCheckBox() {
			return checkBox;
		}
		
		public TextView getTextView() {
			return textView;
		}    
	}
	
	private static class GroupItem {
		
		private Resources resources;
		
		private int groupNo;
		
		private boolean checked;

		public GroupItem(Resources resources, int groupNo, boolean checked) {
			this.groupNo = groupNo;
			this.checked = checked;
			this.resources = resources;
		}

		public int getGroupNo() {
			return groupNo;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}
		
		public String getText() {
			return resources.getString(R.string.word_test_group_no, (groupNo + 1));
		}
	}
}
