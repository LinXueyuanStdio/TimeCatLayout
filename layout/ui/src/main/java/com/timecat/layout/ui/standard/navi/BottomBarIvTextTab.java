package com.timecat.layout.ui.standard.navi;


import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.timecat.component.identity.Attr;
import com.timecat.identity.skin.SkinResource;
import com.timecat.layout.ui.R;
import com.timecat.layout.ui.utils.IconLoader;

public class BottomBarIvTextTab extends AbstractBottomBarTab {
    private ImageView mIcon;
    private TextView mTvTitle;
    private TextView mTvUnreadCount;

    private int mTabPosition = -1;

    public BottomBarIvTextTab(Context context, String icon, CharSequence title) {
        this(context, null, icon, title);
    }

    public BottomBarIvTextTab(Context context, AttributeSet attrs, String icon, CharSequence title) {
        this(context, attrs, 0, icon, title);
    }

    public BottomBarIvTextTab(Context context, AttributeSet attrs, int defStyleAttr, String icon, CharSequence title) {
        super(context, attrs, defStyleAttr);
        init(context, icon, title);
    }

    private void init(Context context, String icon, CharSequence title) {
        LinearLayout lLContainer = new LinearLayout(context);
        lLContainer.setOrientation(LinearLayout.VERTICAL);
        lLContainer.setGravity(Gravity.CENTER);
        LayoutParams paramsContainer = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsContainer.gravity = Gravity.CENTER;
        lLContainer.setLayoutParams(paramsContainer);

        mIcon = new ImageView(context);
        int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources().getDisplayMetrics());
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        IconLoader.loadIcon(getContext(), mIcon, icon);
        mIcon.setLayoutParams(params);
        mIcon.setColorFilter(null);
        lLContainer.addView(mIcon);

        mTvTitle = new TextView(context);
        mTvTitle.setText(title);
        LinearLayout.LayoutParams paramsTv = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramsTv.topMargin = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics());
        mTvTitle.setTextSize(10);
        mTvTitle.setTextColor(Attr.getPrimaryTextColor(getContext()));
        mTvTitle.setLayoutParams(paramsTv);
        lLContainer.addView(mTvTitle);

        addView(lLContainer);

        int min = dip2px(context, 20);
        int padding = dip2px(context, 5);
        mTvUnreadCount = new TextView(context);
        mTvUnreadCount.setBackgroundResource(R.drawable.bg_msg_bubble);
        mTvUnreadCount.setMinWidth(min);
        mTvUnreadCount.setTextColor(Color.WHITE);
        mTvUnreadCount.setPadding(padding, 0, padding, 0);
        mTvUnreadCount.setGravity(Gravity.CENTER);
        LayoutParams tvUnReadParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, min);
        tvUnReadParams.gravity = Gravity.CENTER;
        tvUnReadParams.leftMargin = dip2px(context, 17);
        tvUnReadParams.bottomMargin = dip2px(context, 14);
        mTvUnreadCount.setLayoutParams(tvUnReadParams);
        mTvUnreadCount.setVisibility(GONE);

        setSelected(false);
        addView(mTvUnreadCount);
    }

    @Override
    public void setSelected(boolean selected) {
        if (selected) {
            mIcon.setColorFilter(SkinResource.getColor(mContext, R.color.master_icon_view_special));
        } else {
            mIcon.setColorFilter(SkinResource.getColor(mContext, R.color.master_icon_view));
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

    /**
     * 设置未读数量
     */
    public void setUnreadCount(int num) {
        if (num <= 0) {
            mTvUnreadCount.setText(String.valueOf(0));
            mTvUnreadCount.setVisibility(GONE);
        } else {
            mTvUnreadCount.setVisibility(VISIBLE);
            if (num > 99) {
                mTvUnreadCount.setText("99+");
            } else {
                mTvUnreadCount.setText(String.valueOf(num));
            }
        }
    }

    /**
     * 获取当前未读数量
     */
    public int getUnreadCount() {
        int count = 0;
        if (TextUtils.isEmpty(mTvUnreadCount.getText())) {
            return count;
        }
        if (mTvUnreadCount.getText().toString().equals("99+")) {
            return 99;
        }
        try {
            count = Integer.valueOf(mTvUnreadCount.getText().toString());
        } catch (Exception ignored) {
        }
        return count;
    }

    private int dip2px(Context context, float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }
}
