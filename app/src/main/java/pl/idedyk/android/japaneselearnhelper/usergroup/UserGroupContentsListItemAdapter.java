package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.app.Activity;
import android.content.Context;
import androidx.databinding.DataBindingUtil;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.R;

public class UserGroupContentsListItemAdapter extends BaseAdapter {

    private Context context;

    private List<UserGroupContentsListItem> data = null;

    public UserGroupContentsListItemAdapter(Context context, List<UserGroupContentsListItem> data) {
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

        UserGroupContentsListItem.ItemType[] itemTypeValues = UserGroupContentsListItem.ItemType.values();

        Set<Integer> viewTypeCodeList = new HashSet<>();

        for (UserGroupContentsListItem.ItemType itemType : itemTypeValues) {
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

        UserGroupContentsListItemHolder holder = null;

        //

        int itemViewType = getItemViewType(position);

        if(convertView == null) {

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = DataBindingUtil.inflate(inflater, UserGroupContentsListItem.ItemType.getLayoutResourceId(itemViewType), parent, false).getRoot();

            holder = new UserGroupContentsListItemHolder();

            holder.userGroupContentsListItemHolderValue = (TextView)convertView.findViewById(R.id.user_group_contents_simplerow_value);

            convertView.setTag(holder);

        } else {
            holder = (UserGroupContentsListItemHolder)convertView.getTag();
        }

        //

        UserGroupContentsListItem currentUserGroupContentsListItem = data.get(position);

        holder.userGroupContentsListItemHolderValue.setText(currentUserGroupContentsListItem.getText(), TextView.BufferType.SPANNABLE);

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

        UserGroupContentsListItem currentUserGroupContentsListItem = data.get(position);

        if (currentUserGroupContentsListItem.getItemType() == UserGroupContentsListItem.ItemType.TITLE) {
            return false;

        } else {
            return true;
        }
    }

    static private class UserGroupContentsListItemHolder {
        TextView userGroupContentsListItemHolderValue;
    }
}
