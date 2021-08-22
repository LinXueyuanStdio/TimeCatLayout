package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
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
class HeadItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatTextView(context, attrs, defStyleAttr) {
    init {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        initView(context, attrs)
    }

    private fun initView(context: Context, attrs: AttributeSet?) {
        val dp_small = ViewUtil.dp2px(context, 10f)
        //        int dp_medium = ViewUtil.dp2px(context, 20f);
        setPadding(dp_small, dp_small, dp_small, dp_small)
        setTextColor(Attr.getSecondaryTextColor(context))
    }
}