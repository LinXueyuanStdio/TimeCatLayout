package com.timecat.layout.ui.business.nine;

import android.view.MotionEvent;
import android.view.View;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/3
 * @description RecyclerView的item中子控件触摸事件监听器
 * @usage null
 */
public interface BGAOnRVItemChildTouchListener {
    boolean onRvItemChildTouch(BGARecyclerViewHolder holder, View childView, MotionEvent event);
}