package pl.idedyk.android.japaneselearnhelper.kanji.hkr;

import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.dictionary.dto.KanjiEntry;
import pl.idedyk.android.japaneselearnhelper.kanji.hkr.KanjiTestOptionsChooseKanjiArrayAdapter.KanjiChooseListItem;

import android.content.Context;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

public class KanjiTestOptionsChooseKanjiArrayAdapter extends ArrayAdapter<KanjiChooseListItem> {
	
	private LayoutInflater inflater;
	
	public KanjiTestOptionsChooseKanjiArrayAdapter(Context context, List<KanjiChooseListItem> groupList) {
		super(context, R.layout.kanji_test_options_kanji_simplerow, R.id.kanji_test_options_kanji_simplerow_textview, groupList);

		inflater = LayoutInflater.from(context);
	}
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		
		KanjiChooseListItem currentKanjiChooseListItem = (KanjiChooseListItem)getItem(position); 

		CheckBox currentKanjiChooseListItemCheckBox = null; 
		TextView currentKanjiChooseListItemTextView = null;

		if (convertView == null) {
			convertView = inflater.inflate(R.layout.kanji_test_options_kanji_simplerow, null);

			currentKanjiChooseListItemTextView = (TextView) convertView.findViewById(R.id.kanji_test_options_kanji_simplerow_textview);
			currentKanjiChooseListItemCheckBox = (CheckBox) convertView.findViewById(R.id.kanji_test_options_kanji_simplerow_checkbox);

			convertView.setTag(new KanjiChooseListItemHolder(currentKanjiChooseListItemTextView, currentKanjiChooseListItemCheckBox));

			currentKanjiChooseListItemCheckBox.setOnClickListener(new View.OnClickListener() {

				public void onClick(View view) {
					CheckBox cb = (CheckBox) view;

					KanjiChooseListItem kanjiChooseListItem = (KanjiChooseListItem) cb.getTag();

					kanjiChooseListItem.setChecked(cb.isChecked());
				}
			});      
		} else {
			KanjiChooseListItemHolder kanjiChooseListItemHolder = (KanjiChooseListItemHolder) convertView.getTag();

			currentKanjiChooseListItemCheckBox = kanjiChooseListItemHolder.getCheckBox();
			currentKanjiChooseListItemTextView = kanjiChooseListItemHolder.getTextView();
		}

		currentKanjiChooseListItemCheckBox.setTag(currentKanjiChooseListItem); 
		
		currentKanjiChooseListItemCheckBox.setChecked(currentKanjiChooseListItem.isChecked());		
		currentKanjiChooseListItemTextView.setText(currentKanjiChooseListItem.getText(), TextView.BufferType.SPANNABLE);

		return convertView;
	}

	private static class KanjiChooseListItemHolder {
		
		private CheckBox checkBox;
		private TextView textView;

		public KanjiChooseListItemHolder(TextView textView, CheckBox checkBox) {
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

	public static class KanjiChooseListItem {

		private KanjiEntry kanjiEntry;
		
		private Spanned text;
		
		private boolean checked;

		public KanjiChooseListItem(KanjiEntry kanjiEntry, Spanned text, boolean checked) {
			this.kanjiEntry = kanjiEntry;
			this.checked = checked;
			this.text = text;
		}

		public KanjiEntry getKanjiEntry() {
			return kanjiEntry;
		}

		public boolean isChecked() {
			return checked;
		}

		public void setKanjiEntry(KanjiEntry kanjiEntry) {
			this.kanjiEntry = kanjiEntry;
		}

		public void setChecked(boolean checked) {
			this.checked = checked;
		}

		public Spanned getText() {
			return text;
		}

		public void setText(Spanned text) {
			this.text = text;
		}
	}
}

