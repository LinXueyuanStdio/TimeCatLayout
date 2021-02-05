package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.timecat.layout.ui.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/16
 * @description null
 * @usage null
 */
abstract class AbsItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    init {
        init(context, attrs)
    }

    fun init(context: Context, attrs: AttributeSet?) {
        initView(context, attrs)
    }

    protected abstract fun layoutId(): Int
    protected open fun initView(context: Context, attrs: AttributeSet?) {
        val id = layoutId()
        if (id != 0) {
            LayoutInflater.from(context).inflate(id, this)
        }
        setup(context)
    }

    fun setup(context: Context) {
        setupLayoutParams()
        setupPadding()
        setupBackground(context)
    }

    fun setupBackground(context: Context) {
        val typedArray = context.obtainStyledAttributes(
            intArrayOf(
                R.attr.selectableItemBackground
            )
        )
        val drawable = typedArray.getDrawable(0)
        background = drawable
        typedArray.recycle()
    }

    fun setupLayoutParams() {
        val lp = layoutParams
        if (lp != null) {
            lp.height = resources.getDimension(R.dimen.card_detail_line_height).toInt()
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT
        }
    }

    open fun setupPadding() {
        val pad = resources.getDimension(R.dimen.card_padding_width).toInt()
        setPadding(pad, 0, pad, 0)
    }
}