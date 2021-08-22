package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.google.android.material.slider.Slider
import com.timecat.layout.ui.R
import com.timecat.layout.ui.standard.textview.HintTextView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/28
 * @description 下一步再继续的设置项
 * @usage 不要在xml里写文案，要在java里写
 */
class SliderItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsItem(context, attrs, defStyleAttr) {

    override fun layoutId(): Int {
        return R.layout.view_setting_slide_item_layout
    }

    lateinit var hintTextView: HintTextView
    lateinit var textView: TextView
    lateinit var slider: Slider

    override fun initView(context: Context, attrs: AttributeSet?) {
        super.initView(context, attrs)

        hintTextView = findViewById(R.id.htv)
        textView = findViewById(R.id.text)
        slider = findViewById(R.id.slider)

        textView.isClickable = false
        hintTextView.setShowAnimation(true)
    }

    var valueFrom
        get() = slider.valueFrom
        set(value) {
            slider.valueFrom = value
        }

    var valueTo
        get() = slider.valueTo
        set(value) {
            slider.valueTo = value
        }

    var stepSize
        get() = slider.stepSize
        set(value) {
            slider.stepSize = value
        }

    var value
        get() = slider.value
        set(value) {
            slider.value = when {
                value < valueFrom -> valueFrom
                value > valueTo -> valueTo
                else -> value
            }
        }

    var title: String?
        get() = hintTextView.msg
        set(value) {
            hintTextView.msg = value
        }
    var hint: String?
        get() = hintTextView.hint
        set(value) {
            hintTextView.hint = value
            hintTextView.isShowHint = value != null
        }
    var text: CharSequence?
        get() = textView.text
        set(value) {
            textView.text = value
        }

    fun onSlide(onChange: (value: Float) -> Unit) {
        slider.addOnChangeListener { _, value, _ -> onChange(value) }
    }
}