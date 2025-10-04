package pl.idedyk.android.japaneselearnhelper.screen;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;

import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.ScrollView;

import androidx.core.widget.NestedScrollView;

import java.util.ArrayList;
import java.util.List;

import pl.idedyk.android.japaneselearnhelper.R;

public class TabLayout implements IScreenItem {

    private int activeTab = -1;
    private List<TabLayoutItem> tabLayoutItems = new ArrayList<>();

    private boolean addBorder = false;
    private Integer contentsHeight = null;

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
        /*
        NestedScrollView contentScrollView = new NestedScrollView(context);

        if (contentsHeight == null) {
            contentScrollView.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            contentScrollView.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, contentsHeight));
        }
        contentScrollView.setPadding(5, 5, 5, 5);

        if (addBorder == true) {
            contentScrollView.setBackgroundResource(R.drawable.border);
        }
        */

        contentLinearLayout = new LinearLayout(context);

        if (contentsHeight == null) {
            contentLinearLayout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            contentLinearLayout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, contentsHeight));
        }

        contentLinearLayout.setOrientation(LinearLayout.VERTICAL);

        // dodanie animacji do zmiany zawartosci;
        LayoutTransition layoutTransition = new LayoutTransition();

        layoutTransition.setStartDelay(LayoutTransition.APPEARING, 200);
        layoutTransition.setDuration(LayoutTransition.APPEARING, 500);

        Animation appearingAnim = AnimationUtils.loadAnimation(context, android.R.anim.fade_in);

        contentLinearLayout.setLayoutTransition(layoutTransition);
        contentLinearLayout.setAnimation(appearingAnim);

        // dodanie zawartosci do przewijaka
        // contentScrollView.addView(contentLinearLayout);

        // generowanie zawartosci tabulatora
        generateTabContent(context, resources);

        // dodanie elementow do ekranu
        layout.addView(tabsHorizontalScrollView);
        layout.addView(contentLinearLayout);
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

    public void setAddBorder(boolean addBorder) {
        this.addBorder = addBorder;
    }

    public void setContentsHeight(Integer contentsHeight) {
        this.contentsHeight = contentsHeight;
    }
}
