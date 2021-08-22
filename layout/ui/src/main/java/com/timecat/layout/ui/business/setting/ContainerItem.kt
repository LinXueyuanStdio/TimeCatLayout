package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.widget.LinearLayout
import com.timecat.layout.ui.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description null
 * @usage null
 */
class ContainerItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    init {
        setBackgroundResource(R.color.trans)
        gravity = Gravity.CENTER_HORIZONTAL
        orientation = VERTICAL
    }
}