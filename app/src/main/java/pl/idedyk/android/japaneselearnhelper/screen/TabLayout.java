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
import android.widget.ScrollView;
import android.widget.TabHost;
import android.widget.TabWidget;
import android.widget.TextView;

import androidx.core.widget.NestedScrollView;

import pl.idedyk.android.japaneselearnhelper.JapaneseAndroidLearnHelperApplication;
import pl.idedyk.android.japaneselearnhelper.R;

public class TabLayout implements IScreenItem {



    @Override
    public void generate(Context context, Resources resources, ViewGroup layout) {
        // FM_FIXME: testy !!!!

        HorizontalScrollView tabsHorizontalScrollView = new HorizontalScrollView(context);

        LinearLayout tabsLinearLayout = new LinearLayout(context);
        tabsLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        tabsHorizontalScrollView.addView(tabsLinearLayout);

        // zawartosc
        NestedScrollView contentScrollView = new NestedScrollView(context);
        contentScrollView.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        LinearLayout contentLinearLayout = new LinearLayout(context);
        contentLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLinearLayout.setOrientation(LinearLayout.VERTICAL);

        contentScrollView.addView(contentLinearLayout);

        for (int i = 0; i < 10; ++i) {
            Button button = new Button(context);

            // textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setText("Tab " + i);

            tabsLinearLayout.addView(button);

            final int iAsFinal = i;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    TextView textView = new TextView(context);
                    textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    textView.setText("Tab content: " + iAsFinal);

                    contentLinearLayout.addView(textView);
                }
            });
        }

        layout.addView(tabsHorizontalScrollView);
        layout.addView(contentScrollView);
    }

    @Override
    public int getY() {
        // FM_FIXME: do naprawy
        return 0;
    }
}
