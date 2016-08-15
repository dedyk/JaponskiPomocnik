package pl.idedyk.android.japaneselearnhelper.screen;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class Image implements IScreenItem {
	
	private ImageView imageView;
	
	private boolean nullMargins = false;
	
	private Integer layoutWeight;
	
	private Drawable image;
	
	private int level;
	
	private OnClickListener onClickListener;
	
	public Image(Drawable image, int level) {
		this.image = image;
		this.level = level;
	}
	
	public void setNullMargins(boolean nullMargins) {
		this.nullMargins = nullMargins;
	}
		
	public void setLayoutWidth(Integer layoutWeight) {
		this.layoutWeight = layoutWeight;
	}
	
	public void setOnClickListener(OnClickListener onClickListener) {
		this.onClickListener = onClickListener;		
	}

	public void generate(Context context, Resources resources, ViewGroup layout) {
		
		imageView = new ImageView(context);
		
		if (layout instanceof android.widget.TableRow) {
			android.widget.TableRow.LayoutParams layoutParam = new android.widget.TableRow.LayoutParams(android.widget.TableRow.LayoutParams.WRAP_CONTENT, android.widget.TableRow.LayoutParams.WRAP_CONTENT);
			
			if (nullMargins == true) {
				layoutParam.setMargins(0, 0, 0, 0);
			} else {
				layoutParam.setMargins(20 + level * 20, 5, 0, 0);
			}

			if (layoutWeight != null) {
				layoutParam.weight = layoutWeight;
			}
					
			imageView.setLayoutParams(layoutParam);
		} else if (layout instanceof LinearLayout) {
			LinearLayout.LayoutParams layoutParam = new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			
			if (nullMargins == false) {
				layoutParam.setMargins(20 + level * 20, 5, 0, 0);
			}
			
			if (layoutWeight != null) {
				layoutParam.weight = layoutWeight;
			}
			
			imageView.setLayoutParams(layoutParam);
		} else {
			throw new RuntimeException(String.valueOf(layout.getClass()));
		}
		
		imageView.setImageDrawable(image);
		
		imageView.setOnClickListener(onClickListener);

		layout.addView(imageView);
	}
	
	public int getY() {
		
		if (imageView != null) {
			return imageView.getTop();
		}
		
		throw new RuntimeException("imageView is null");
	}

	@Override
	public String toString() {
		return "";
	}
}
