package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.content.res.Resources;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;
import pl.idedyk.android.japaneselearnhelper.data.entity.UserGroupEntity;

public class UserGroupListItem {

    private UserGroupEntity userGroupEntity;

    //

    private Resources resources;

    public UserGroupListItem(UserGroupEntity userGroupEntity, Resources resources) {
        this.userGroupEntity = userGroupEntity;

        this.resources = resources;
    }

    public ItemType getItemType() {

        switch (userGroupEntity.getType()) {

            case STAR_GROUP:
                return ItemType.STAR_GROUP;

            case USER_GROUP:
                return ItemType.USER_GROUP;

            default:
                throw new RuntimeException("" + userGroupEntity.getType());
        }
    }

    public Spanned getText() {

        switch (userGroupEntity.getType()) {

            case STAR_GROUP:
                return new SpannableString(resources.getString(R.string.user_group_star_group));

            case USER_GROUP:
                return new SpannableString(userGroupEntity.getName());

            default:
                throw new RuntimeException("" + userGroupEntity.getType());
        }
    }

    public UserGroupEntity getUserGroupEntity() {
        return userGroupEntity;
    }

    public static enum ItemType {

        STAR_GROUP(R.layout.user_group_simplerow, 0, android.R.drawable.star_big_on),

        USER_GROUP(R.layout.user_group_simplerow, 0, JapaneseAndroidLearnHelperApplication.getInstance().getThemeType().getUserGroupListIconId());

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
