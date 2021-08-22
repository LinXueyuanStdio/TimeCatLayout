package com.timecat.layout.ui.standard.viewpaper;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    private boolean enabled = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onTouchEvent(event);
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        if (this.enabled) {
            return super.onInterceptTouchEvent(event);
        }
        return false;
    }

    public CustomViewPager setViewPagerEnabled(boolean enabled) {
        this.enabled = enabled;
        return this;
    }


    public CustomViewPager setCustomOffscreenPageLimit(int position) {
        setOffscreenPageLimit(position);
        return this;
    }


    public CustomViewPager setCustomAdapter(FragmentStatePagerAdapter adapter) {
        setAdapter(adapter);
        return this;
    }


    public CustomViewPager setCustomOnPageChangeListener(OnPageChangeListener onPageChangeListener) {
        setOnPageChangeListener(onPageChangeListener);
        return this;
    }


    public CustomViewPager setCustomCurrentItem(int position) {
        setCurrentItem(position);
        return this;
    }


    /**
     * 设置viewpager的参数
     *
     * @param offscreenPageLimit
     * @param enable
     * @param adapter
     * @param onPageChangeListener
     * @param currentItem
     */
    public void setCustomViewPagerParam(int offscreenPageLimit, boolean enable, FragmentStatePagerAdapter adapter, ViewPager.OnPageChangeListener onPageChangeListener, int currentItem) {
        this.setCustomOffscreenPageLimit(offscreenPageLimit)
            .setViewPagerEnabled(enable)
            .setCustomAdapter(adapter)
            .setCustomOnPageChangeListener(onPageChangeListener)
            .setCustomCurrentItem(currentItem);
    }


}
