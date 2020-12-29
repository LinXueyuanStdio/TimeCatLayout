package com.timecat.layout.ui.standard.navi;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.timecat.layout.ui.R;

public abstract class AbstractBottomBarTab extends FrameLayout {
    protected Context mContext;

    public AbstractBottomBarTab(Context context) {
        this(context, null);
    }

    public AbstractBottomBarTab(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AbstractBottomBarTab(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        mContext = context;
        TypedArray typedArray = context.obtainStyledAttributes(new int[]{R.attr.selectableItemBackgroundBorderless});
        Drawable drawable = typedArray.getDrawable(0);
        setBackground(drawable);
        typedArray.recycle();
    }

    public abstract void setTabPosition(int position);

    public abstract int getTabPosition();

    protected TabBlockItem item;

    public void setItem(TabBlockItem item) {
        this.item = item;
    }

    public TabBlockItem getItem() {
        return item;
    }
}
