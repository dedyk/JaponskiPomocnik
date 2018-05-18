package pl.idedyk.android.japaneselearnhelper.usergroup;

import android.app.Activity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.MenuShorterHelper;
import pl.idedyk.android.japaneselearnhelper.R;

public class UserGroupActivity extends Activity {

    private ListView userGroupListView;

    private UserGroupListItemAdapter userGroupListAdapter;

    private List<UserGroupListItem> userGroupList;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);

        MenuShorterHelper.onCreateOptionsMenu(menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);

        return MenuShorterHelper.onOptionsItemSelected(item, getApplicationContext(), this);
    }

    @Override
    protected void onSaveInstanceState(Bundle bundle) {

        super.onSaveInstanceState(bundle);

    }

    @Override
    protected void onRestoreInstanceState(Bundle bundle) {

        super.onRestoreInstanceState(bundle);


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        JapaneseAndroidLearnHelperApplication.getInstance().logScreen(getString(R.string.logs_user_group));

        setContentView(R.layout.user_group);

        //

        userGroupListView = (ListView)findViewById(R.id.user_group_list);

        userGroupList = new ArrayList<>();
        userGroupListAdapter = new UserGroupListItemAdapter(this, userGroupList);

        userGroupListView.setAdapter(userGroupListAdapter);

        //

        userGroupList.add(new UserGroupListItem(UserGroupListItem.ItemType.STAR_GROUP, Html.fromHtml("SSSSSS")));

        userGroupList.add(new UserGroupListItem(UserGroupListItem.ItemType.USER_GROUP, Html.fromHtml("UUUUU")));

        //

        userGroupListAdapter.notifyDataSetChanged();
    }
}