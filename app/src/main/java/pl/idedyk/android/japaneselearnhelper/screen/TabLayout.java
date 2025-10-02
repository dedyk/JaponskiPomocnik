package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;

import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.List;

public class TabLayout implements IScreenItem {

    private int activeTab = -1;
    private List<TabLayoutItem> tabLayoutItems = new ArrayList<>();

    // przyciski
    private LinearLayout tabsLinearLayout;

    // tab z zawartoscia
    private LinearLayout contentLinearLayout;

    public void addTab(TabLayoutItem tabLayoutItem) {
        tabLayoutItems.add(tabLayoutItem);

        activeTab = 0;
    }

    @Override
    public void generate(Context context, Resources resources, ViewGroup layout) {
        // FM_FIXME: testy !!!!

        // tworzenie przewijaka pionowego z nazwami tabow
        HorizontalScrollView tabsHorizontalScrollView = new HorizontalScrollView(context);

        tabsLinearLayout = new LinearLayout(context);
        tabsLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        tabsHorizontalScrollView.addView(tabsLinearLayout);

        for (int tabNo = 0; tabNo < tabLayoutItems.size(); ++tabNo) {
            TabLayoutItem tabLayoutItem = tabLayoutItems.get(tabNo);

            // dodajemy guzik do wyboru zawartosci
            Button button = new Button(context);

            button.setText(tabLayoutItem.getName());

            if (tabLayoutItems.size() > 1) { // jezeli jest tylko jeden element to nie generuj guzika
                tabsLinearLayout.addView(button);
            }

            // akcja do zmiany tab-a
            final int tabNoAsFinal = tabNo;
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activeTab = tabNoAsFinal;

                    generateTabContent(context, resources);
                }
            });
        }

        // generowanie zawartosci (tylko aktywny tab)
        NestedScrollView contentScrollView = new NestedScrollView(context);
        contentScrollView.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        contentLinearLayout = new LinearLayout(context);
        contentLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        contentLinearLayout.setOrientation(LinearLayout.VERTICAL);

        contentScrollView.addView(contentLinearLayout);

        // generowanie zawartosci tabulatora
        generateTabContent(context, resources);

        // dodanie elementow do ekranu
        layout.addView(tabsHorizontalScrollView);
        layout.addView(contentScrollView);
    }

    private void generateTabContent(Context context, Resources resources) {
        if (tabLayoutItems.size() == 0) {
            return;
        }

        // pobranie aktywnej zakladki
        TabLayoutItem tabLayoutItem = tabLayoutItems.get(activeTab);

        // generowanie zawartosci
        contentLinearLayout.removeAllViews();

        tabLayoutItem.generate(context, resources, contentLinearLayout);
    }

    @Override
    public int getY() {
        return tabsLinearLayout.getTop();
    }
}
