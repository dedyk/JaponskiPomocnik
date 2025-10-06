package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class TabLayoutItem implements IScreenItem {

    private String name;
    private List<IScreenItem> tabContents;

    public TabLayoutItem(String name) {
        this.name = name;
        this.tabContents = new ArrayList<>();
    }

    public void addToTabContents(IScreenItem screenItem) {
        tabContents.add(screenItem);
    }

    @Override
    public void generate(Context context, Resources resources, ViewGroup layout) {
        for (IScreenItem currentScreenItem : tabContents) {
            currentScreenItem.generate(context, resources, layout);
        }
    }

    public String getName() {
        return name;
    }

    @Override
    public int getY() {
        return tabContents.get(0).getY();
    }

    public List<IScreenItem> getTabContentsList() {
        return tabContents;
    }
}
