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
class ContainerItem : LinearLayout {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr)

    init {
        setBackgroundResource(R.color.trans)
        gravity = Gravity.CENTER_HORIZONTAL
        orientation = VERTICAL
    }
}