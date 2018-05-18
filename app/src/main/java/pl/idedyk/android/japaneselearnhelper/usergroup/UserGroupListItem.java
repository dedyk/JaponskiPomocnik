package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.text.Spanned;

import pl.idedyk.android.japaneselearnhelper.R;

public class UserGroupListItem {

    private ItemType itemType;

    private Spanned text;

    public UserGroupListItem(ItemType itemType, Spanned text) {
        this.itemType = itemType;
        this.text = text;
    }

    public ItemType getItemType() {
        return itemType;
    }

    public Spanned getText() {
        return text;
    }

    public static enum ItemType {

        STAR_GROUP(R.layout.user_group_simplerow, 0, android.R.drawable.star_big_on),

        USER_GROUP(R.layout.user_group_simplerow, 0, R.drawable.user_group_list);

        private int layoutResourceId;

        private int viewTypeId;

        private int iconResourceId;

        // ten sam layoutResourceId musi sie rownac tego samemu viewTypeId !!!
        ItemType(int layoutResourceId, int viewTypeId, int iconResourceId) {
            this.layoutResourceId = layoutResourceId;
            this.viewTypeId = viewTypeId;
            this.iconResourceId = iconResourceId;
        }

        public int getViewTypeId() {
            return viewTypeId;
        }

        public int getIconResourceId() {
            return iconResourceId;
        }

        public static int getLayoutResourceId(int itemViewTypeId) {

            ItemType[] values = values();

            for (ItemType itemType : values) {

                if (itemType.getViewTypeId() == itemViewTypeId) {
                    return itemType.layoutResourceId;
                }
            }

            throw new RuntimeException("Unknown itemViewTypeId: " + itemViewTypeId);
        }
    }
}
