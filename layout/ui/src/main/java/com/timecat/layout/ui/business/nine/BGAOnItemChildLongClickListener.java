package com.timecat.layout.ui.business.nine;

import android.view.View;
import android.view.ViewGroup;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/2
 * @description AdapterView和RecyclerView的item中子控件长按事件监听器
 * @usage null
 */
public interface BGAOnItemChildLongClickListener {
    boolean onItemChildLongClick(ViewGroup parent, View childView, int position);
}