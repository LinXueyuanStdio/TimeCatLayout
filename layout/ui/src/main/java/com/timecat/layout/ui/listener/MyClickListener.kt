package com.timecat.layout.ui.listener

import android.view.View

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/22
 * @description null
 * @usage null
 */
class MyClickListener(
    debounced: Long = 800,
    val click: (v: View) -> Unit
) : OnDebouncedClickListener(debounced) {
    override fun onDebouncedClick(v: View) {
        click(v)
    }
}