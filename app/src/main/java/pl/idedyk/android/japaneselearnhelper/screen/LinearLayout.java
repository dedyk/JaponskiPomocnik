package pl.idedyk.android.japaneselearnhelper.screen;

import android.animation.LayoutTransition;
import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ScrollView;

import java.util.ArrayList;
import java.util.List;

public class LinearLayout implements IScreenItem {

    private android.widget.LinearLayout linearLayout;

    private List<IScreenItem> screenItemList = new ArrayList<>();

    private Integer visibility;

    @Override
    public void generate(Context context, Resources resources, ViewGroup layout) {
        linearLayout = new android.widget.LinearLayout(context);

        linearLayout.setLayoutParams(new ScrollView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(android.widget.LinearLayout.VERTICAL);

        if (visibility != null) {
            linearLayout.setVisibility(visibility);
        }

        for (IScreenItem screenItem : screenItemList) {
            screenItem.generate(context, resources, linearLayout);
        }

        // dodanie elementow do ekranu
        layout.addView(linearLayout);
    }

    @Override
    public int getY() {
        return linearLayout.getTop();
    }

    public void addScreenItem(IScreenItem screenItem) {
        screenItemList.add(screenItem);
    }

    public void setVisibility(int visibility) {
        this.visibility = visibility;
    }

    public void regenerate(Context context, Resources resources, ViewGroup layout) {
        linearLayout.removeAllViews();

        generate(context, resources, layout);
    }
}