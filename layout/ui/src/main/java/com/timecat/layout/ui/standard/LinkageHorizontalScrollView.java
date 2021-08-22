package com.timecat.layout.ui.standard;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.HorizontalScrollView;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/10/15
 * @description null
 * @usage null
 */
public class LinkageHorizontalScrollView extends HorizontalScrollView {
    public LinkageHorizontalScrollView(Context context) {
        super(context);
    }

    public LinkageHorizontalScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LinkageHorizontalScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    OnLinkageListener onLinkageListener;

    public void setOnLinkageListener(OnLinkageListener onLinkageListener) {
        this.onLinkageListener = onLinkageListener;
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);

        if (onLinkageListener != null) {
            onLinkageListener.onLinkageScroll(this, l, t, oldl, oldt);
        }
    }

    public interface OnLinkageListener {
        void onLinkageScroll(HorizontalScrollView scrollView, int l, int t, int oldl, int oldt);
    }
}
