package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.view.ViewGroup;

public interface IScreenItem {
	public void generate(Context context, Resources resources, ViewGroup layout);
	
	public int getY();
	
	public String toString();
}

