package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import androidx.appcompat.widget.AppCompatTextView
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.utils.ViewUtil

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/29
 * @description 头部标题
 * @usage null
 */
class HeadItem : AppCompatTextView {
    constructor(context: Context) : super(context) {
        initView(context, null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initView(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        gravity = Gravity.CENTER_HORIZONTAL or Gravity.BOTTOM
        val dp_small = ViewUtil.dp2px(context, 10f)
        //        int dp_medium = ViewUtil.dp2px(context, 20f);
        setPadding(dp_small, dp_small, dp_small, dp_small)
        setTextColor(Attr.getSecondaryTextColor(context))
    }
}