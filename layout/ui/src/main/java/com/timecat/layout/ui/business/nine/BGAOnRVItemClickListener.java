package com.timecat.layout.ui.business.nine;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/3
 * @description RecyclerView的item点击事件监听器
 * @usage null
 */
public interface BGAOnRVItemClickListener {
    void onRVItemClick(ViewGroup parent, View itemView, int position);
}