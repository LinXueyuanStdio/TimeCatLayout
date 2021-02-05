package com.timecat.layout.ui.business.setting

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.widget.TextView
import com.timecat.layout.ui.R
import com.timecat.layout.ui.layout.setShakelessClickListener
import com.timecat.layout.ui.standard.textview.HintTextView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/28
 * @description 下一步再继续的设置项
 * @usage 不要在xml里写文案，要在java里写
 */
class NextItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsItem(context, attrs, defStyleAttr) {

    override fun layoutId(): Int {
        return R.layout.view_setting_next_item_layout
    }

    lateinit var hintTextView: HintTextView
    lateinit var textView: TextView
    override fun initView(context: Context, attrs: AttributeSet?) {
        super.initView(context, attrs)

        hintTextView = findViewById(R.id.htv)
        textView = findViewById(R.id.text)

        textView.isClickable = false
        hintTextView.setShowAnimation(true)
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

    fun onNext(onClick: () -> Unit) {
        setShakelessClickListener {
            onClick()
        }
    }
}