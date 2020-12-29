package com.timecat.layout.ui.business.nine;

import android.view.ViewGroup;
import android.widget.CompoundButton;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/2
 * @description AdapterView和RecyclerView的item中子控件选中状态变化事件监听器
 * @usage null
 */
public interface BGAOnItemChildCheckedChangeListener {
    void onItemChildCheckedChanged(ViewGroup parent, CompoundButton childView, int position, boolean isChecked);
}