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
        // FM_FIXME: testy !!!!!!

        FlexboxLayout linearLayout = new FlexboxLayout(context);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        linearLayout.setLayoutParams(layoutParams);

        linearLayout.setFlexWrap(FlexWrap.WRAP);
        linearLayout.setFlexDirection(FlexDirection.ROW);

        //

        for (int i = 0; i < 10; ++i) {
            //TextView textView = new TextView(context);

            //textView.setVi

            // textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            // textView.setText("_XXXXXXXXXXXXXXXXX_" + i);

            /*
            Button button = new Button(context); // , null, android.R.style.Widget_TabWidget);

            button.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            button.setText("TAB_x_" + i);
            // button.setBackground(android.R.attr.tab);

            */
                    /*
            <Button
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:text="Tab"
            style="@android:style/Widget.TabWidget" />
            */

            // linearLayout.addView(textView);
            // linearLayout.addView(button);
        }

        /*
        com.google.android.material.tabs.TabLayout tabLayout = new com.google.android.material.tabs.TabLayout(context);


        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 3"));

        */

        // layout.addView(linearLayout);

        /*
        TabHost tabHost = new TabHost(context);

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
        tabHost.setLayoutParams(layoutParams);

        tabHost.setup();

        // setting the name of the new tab
        TabHost.TabSpec spec = tabHost.newTabSpec("Tab One");

        // spec.setContent(R.id.tab1);
        spec.setIndicator("Tab One");
        // adding the tab to tabhost
        tabHost.addTab(spec);

        layout.addView(tabHost);
        */

        TabWidget tabWidget = new TabWidget(context, null, android.R.attr.tabWidgetStyle);

        tabWidget.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        for (int i = 0; i < 10; ++i) {
            TextView textView = new TextView(context, null,
                    JapaneseAndroidLearnHelperApplication.getInstance().getThemeType().getStyleId());

            //textView.setBackground();

            // TabHost.TabSpec spec = new TabHost.TabSpec("Tab One");

            // spec.setContent(R.id.tab1);
            // spec.setIndicator("Tab One");


            // textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            textView.setText("_XXXXXXXXXXXXXXXXX_" + i);

            tabWidget.addView(textView);
        }

        layout.addView(tabWidget);

    }

    public View createIndicatorView(Context context, TabWidget tabWidget, String mLabel) {

        /*
        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tabIndicator = inflater.inflate(android.R.layout.tab_indicator_holo,
                tabWidget, // tab widget is the parent
                false); // no inflate params

        final TextView tv = tabIndicator.findViewById(R.id.title);
        tv.setText(mLabel);
         */

        /*
        if (context.getApplicationInfo().targetSdkVersion <= Build.VERSION_CODES.DONUT) {
            // Donut apps get old color scheme
            tabIndicator.setBackgroundResource(android.R.drawable.tab_indicator_v4);
            tv.setTextColor(context.getColorStateList(android.R.color.tab_indicator_text_v4));
        }
         */

        // return tabIndicator;
        return null;
    }

    @Override
    public int getY() {
        // FM_FIXME: do naprawy
        return 0;
    }
}
