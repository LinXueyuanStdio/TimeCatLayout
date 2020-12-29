package com.timecat.layout.ui.standard.navi;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;

import com.timecat.layout.ui.R;

public class BottomBarTab extends AbstractBottomBarTab {
    private ImageView mIcon;
    private int mTabPosition = -1;

    public BottomBarTab(Context context, @DrawableRes int icon) {
        this(context, null, icon);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int icon) {
        this(context, attrs, 0, icon);
    }

    public BottomBarTab(Context context, AttributeSet attrs, int defStyleAttr, int icon) {
        super(context, attrs, defStyleAttr);
        init(context, icon);
    }

    private void init(Context context, int icon) {

        mIcon = new ImageView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 27, getResources().getDisplayMetrics());
        LayoutParams params = new LayoutParams(size, size);
        params.gravity = Gravity.CENTER;
        mIcon.setImageResource(icon);
        mIcon.setLayoutParams(params);
        mIcon.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        mIcon.setColorFilter(null);
        setSelected(false);
        addView(mIcon);
    }

    @Override
    public void setSelected(boolean selected) {
        super.setSelected(selected);
        if (selected) {
            mIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.master_icon_view_special));
        } else {
            mIcon.setColorFilter(ContextCompat.getColor(mContext, R.color.master_icon_view));
        }
    }

    public void setTabPosition(int position) {
        mTabPosition = position;
        if (position == 0) {
            setSelected(true);
        }
    }

    public int getTabPosition() {
        return mTabPosition;
    }

}
