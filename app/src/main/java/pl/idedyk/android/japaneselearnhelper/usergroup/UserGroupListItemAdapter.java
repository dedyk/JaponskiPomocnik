package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import pl.idedyk.android.japaneselearnhelper.R;

public class UserGroupListItemAdapter extends BaseAdapter {

    private Context context;

    private List<UserGroupListItem> data = null;

    public UserGroupListItemAdapter(Context context, List<UserGroupListItem> data) {
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

        UserGroupListItem.ItemType[] itemTypeValues = UserGroupListItem.ItemType.values();

        Set<Integer> viewTypeCodeList = new HashSet<>();

        for (UserGroupListItem.ItemType itemType : itemTypeValues) {
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

        UserGroupListItemHolder holder = null;

        //

        int itemViewType = getItemViewType(position);

        if(convertView == null) {

            LayoutInflater inflater = ((Activity)context).getLayoutInflater();
            convertView = inflater.inflate(UserGroupListItem.ItemType.getLayoutResourceId(itemViewType), parent, false);

            holder = new UserGroupListItemHolder();

            holder.userGroupListItemHolderValue = (TextView)convertView.findViewById(R.id.user_group_simplerow_value);
            holder.userGroupListItemHolderIcon = (ImageView)convertView.findViewById(R.id.user_group_simplerow_icon);

            convertView.setTag(holder);

        } else {
            holder = (UserGroupListItemHolder)convertView.getTag();
        }

        //

        UserGroupListItem currentUserGroupListItem = data.get(position);

        holder.userGroupListItemHolderValue.setText(currentUserGroupListItem.getText(), TextView.BufferType.SPANNABLE);
        holder.userGroupListItemHolderIcon.setImageResource(currentUserGroupListItem.getItemType().getIconResourceId());

        return convertView;
    }

    static private class UserGroupListItemHolder {
        TextView userGroupListItemHolderValue;
        ImageView userGroupListItemHolderIcon;
    }
}
