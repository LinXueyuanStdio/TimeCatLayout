package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import com.timecat.layout.ui.layout.*
import it.sephiroth.android.library.numberpicker.NumberPicker
import it.sephiroth.android.library.numberpicker.doOnProgressChanged

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/5/22
 * @description null
 * @usage null
 */
class CounterItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    var picker: NumberPicker

    init {
        orientation = HORIZONTAL
        layout_width = wrap_content
        layout_height = wrap_content
        padding = 4
        margin = 4
        picker = NumberPicker(context).apply {
            layout_width = wrap_content
            layout_height = wrap_content
            layout_gravity = gravity_center
            gravity = gravity_center
            doOnProgressChanged { numberPicker, progress, formUser ->
                onCount(progress)
            }
        }.also {
            addView(it)
        }
    }

    var stepSize: Int
        get() = picker.stepSize
        set(value) {
            picker.stepSize = value
        }

    var value: Int
        get() = picker.progress
        set(value) {
            picker.progress = value
        }

    var min: Int
        get() = picker.minValue
        set(value) {
            picker.minValue = value
        }

    var max: Int
        get() = picker.maxValue
        set(value) {
            picker.maxValue = value
        }

    var onCount: (Int) -> Unit = {}
}