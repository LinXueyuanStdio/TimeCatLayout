package com.timecat.layout.ui.business.setting

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.utils.IconLoader

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/28
 * @description
 * |-----------|
 * |   icon    |
 * |-----------|
 * |   count   |
 * |-----------|
 * |   title   |
 * |-----------|
 * @usage 不要在xml里写文案，要在java里写
 */
class RewardItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var imageView: ImageView
    var countView: TextView
    var nameView: TextView

    init {
        orientation = VERTICAL
        layout_width = wrap_content
        layout_height = wrap_content
        padding = 4
        margin = 4
        imageView = ImageView {
            layout_width = 36
            layout_height = 36
            margin = 2
            layout_gravity = gravity_center
            isClickable = false
        }
        countView = TextView {
            layout_width = wrap_content
            layout_height = wrap_content
            layout_gravity = gravity_center
            isClickable = false
            margin_top = 4
            margin_bottom = 4
            setTextColor(Attr.getSecondaryTextColor(context))
            setTextSize(12f)
        }
        nameView = TextView {
            layout_width = wrap_content
            layout_height = wrap_content
            margin = 2
            layout_gravity = gravity_center
            isClickable = false
            setTextColor(Attr.getSecondaryTextColor(context))
            setTextSize(12f)
            maxLines = 2
            ellipsize = TextUtils.TruncateAt.MIDDLE
        }
    }

    var name: String
        get() = nameView.text.toString()
        set(value) {
            nameView.text = value
        }
    var icon: String = "R.drawable.ic_launcher"
        set(value) {
            IconLoader.loadIcon(context, imageView, value)
            field = value
        }
    var count: Long
        get() = countView.text.toString().toLong()
        set(value) {
            countView.text = "$value"
        }

    fun onClick(onClick: (item: RewardItem) -> Unit) {
        setShakelessClickListener {
            onClick(this@RewardItem)
        }
    }

    var uuid: String = ""

    var reward: Reward
        get() = Reward(uuid, icon, count, name)
        set(value) {
            uuid = value.uuid
            icon = value.icon
            count = value.count
            name = value.name
        }

    data class Reward(var uuid: String, var icon: String, var count: Long, var name: String)
}