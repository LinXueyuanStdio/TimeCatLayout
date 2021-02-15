package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.dp
import com.timecat.layout.ui.layout.layout_height
import com.timecat.layout.ui.layout.layout_width
import com.timecat.layout.ui.layout.match_parent
import com.timecat.layout.ui.utils.ViewUtil

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description null
 * @usage null
 */
class DividerItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    init {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        setBackgroundColor(Attr.getBackgroundDarkColor(context))
        alpha = 0.25f
        layout_width = match_parent
        layout_height = 10
    }
}