package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import com.google.android.flexbox.FlexDirection;
import com.google.android.flexbox.FlexWrap;
import com.google.android.flexbox.FlexboxLayout;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;

public class TabLayout implements IScreenItem {



    @Override
    public void generate(Context context, Resources resources, ViewGroup layout) {
        FlexboxLayout flexboxLayout = new FlexboxLayout(context);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        flexboxLayout.setLayoutParams(layoutParams);

        flexboxLayout.setFlexWrap(FlexWrap.WRAP);
        flexboxLayout.setFlexDirection(FlexDirection.COLUMN);

        //

        for (int i = 0; i < 10; ++i) {
            Button button = new Button(context);

            // textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setText("_XXXXXXXXXXXXXXXXX_" + i);

            flexboxLayout.addView(button);
        }

        layout.addView(flexboxLayout);
    }

    @Override
    public int getY() {
        // FM_FIXME: do naprawy
        return 0;
    }
}
