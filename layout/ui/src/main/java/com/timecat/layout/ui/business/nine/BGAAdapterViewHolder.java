package com.timecat.layout.ui.business.nine;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/2
 * @description null
 * @usage null
 */

public class BGAAdapterViewHolder {
    protected View mConvertView;
    protected BGAViewHolderHelper mViewHolderHelper;

    private BGAAdapterViewHolder(ViewGroup parent, int layoutId) {
        mConvertView = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        mConvertView.setTag(this);
        mViewHolderHelper = new BGAViewHolderHelper(parent, mConvertView);
    }

    /**
     * 拿到一个可重用的ViewHolder对象
     *
     * @param convertView
     * @param parent
     * @param layoutId
     * @return
     */
    public static BGAAdapterViewHolder dequeueReusableAdapterViewHolder(View convertView, ViewGroup parent, int layoutId) {
        if (convertView == null) {
            return new BGAAdapterViewHolder(parent, layoutId);
        }
        return (BGAAdapterViewHolder) convertView.getTag();
    }

    public BGAViewHolderHelper getViewHolderHelper() {
        return mViewHolderHelper;
    }

    public View getConvertView() {
        return mConvertView;
    }

}