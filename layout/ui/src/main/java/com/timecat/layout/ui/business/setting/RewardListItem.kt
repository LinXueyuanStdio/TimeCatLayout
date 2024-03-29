package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.widget.HorizontalScrollView
import com.timecat.layout.ui.layout.LinearLayout
import com.timecat.layout.ui.layout.layout_height
import com.timecat.layout.ui.layout.layout_width
import com.timecat.layout.ui.layout.wrap_content

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/28
 * @description
 * |-----------|-|-----------|-|-----------|-|-----------|
 * |   icon    | |   icon    | |   icon    | |   icon    |
 * |-----------| |-----------| |-----------| |-----------|
 * |   count   | |   count   | |   count   | |   count   |
 * |-----------| |-----------| |-----------| |-----------|
 * |   title   | |   title   | |   title   | |   title   |
 * |-----------|-|-----------|-|-----------|-|-----------|
 * @usage 不要在xml里写文案，要在java里写
 */
class RewardListItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : HorizontalScrollView(context, attrs, defStyleAttr) {

    var container: android.widget.LinearLayout = LinearLayout {
        layout_width = wrap_content
        layout_height = wrap_content
        orientation = android.widget.LinearLayout.HORIZONTAL
    }

    var rewards: List<RewardItem.Reward> = listOf()
        set(value) {
            container.removeAllViews()
            for (i in value) {
                val item = RewardItem(context)
                item.reward = i
                item.onClick(onClick)
                container.addView(item)
            }
            field = value
        }
    var onClick: (item: RewardItem) -> Unit = {}
}