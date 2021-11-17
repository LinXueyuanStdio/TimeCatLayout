package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.google.android.material.chip.Chip
import com.timecat.layout.ui.R
import com.timecat.layout.ui.layout.*

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/10/23
 * @description null
 * @usage null
 */
class FilterSortItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    var filterView: Chip
    var sortView: Chip
    var sortTypeView: Chip

    init {
        layout_width = match_parent
        layout_height = 24
        gravity = gravity_center_vertical

        filterView = Chip(context).apply {
            setChipIconResource(R.drawable.ic_filter)
            text = "筛选"
            setShakelessClickListener {
                showFilterSelector()
            }
        }.also {
            addView(it)
        }
        sortTypeView = Chip(context).apply {
            setCloseIconResource(R.drawable.ic_arrow_drop_down)
            isCloseIconVisible = true
            text = "名字顺序"
            weight = 1.0f
            setShakelessClickListener {
                showSortTypeSelector()
            }
        }.also {
            addView(it)
        }
        sortView = Chip(context).apply {
            setChipIconResource(R.drawable.ic_sort_swap)
            text = "正序"
            setShakelessClickListener {
                isAsc = !isAsc
                onAscChanged(isAsc)
            }
        }.also {
            addView(it)
        }
    }

    var filterText: String = "筛选"
        set(value) {
            filterView.text = value
            field = value
        }
    var sortTypeText: String = "名字顺序"
        set(value) {
            sortTypeView.text = value
            field = value
        }


    var sortText: String = "正序"
        set(value) {
            sortView.text = value
            field = value
        }

    var isAsc: Boolean = true
        set(value) {
            field = value
            if (value) {
                sortText = "正序"
            } else {
                sortText = "倒序"
            }
        }
    var onAscChanged: (isAsc: Boolean) -> Unit = {}
    var showSortTypeSelector: () -> Unit = {}
    var showFilterSelector: () -> Unit = {}
}