package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.SwitchCompat
import com.timecat.layout.ui.R
import com.timecat.layout.ui.listener.OnDebouncedClickListener
import com.timecat.layout.ui.standard.textview.HintTextView

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/28
 * @description 开关设置项
 * @usage 不要在xml里写文案，要在java里写
 */
class SwitchItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsItem(context, attrs, defStyleAttr) {

    override fun layoutId(): Int {
        return R.layout.view_setting_switch_item_layout
    }

    lateinit var hintTextView: HintTextView
    lateinit var switchCompat: SwitchCompat

    override fun initView(context: Context, attrs: AttributeSet?) {
        super.initView(context, attrs)

        hintTextView = findViewById(R.id.htv)
        switchCompat = findViewById(R.id.switcher)

        switchCompat.isClickable = false
        hintTextView.setShowAnimation(true)

        setOnClickListener(object : OnDebouncedClickListener() {
            override fun onDebouncedClick(v: View) {
                switchCompat.isChecked = !switchCompat.isChecked
            }
        })
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
    var isChecked: Boolean
        get() = switchCompat.isChecked
        set(value) {
            switchCompat.isChecked = value
        }
    var onCheckChange: (isChecked: Boolean) -> Unit = {}
    var initCheck: Boolean
        get() = switchCompat.isChecked
        set(value) {
            switchCompat.setOnCheckedChangeListener(null)
            switchCompat.isChecked = value
            switchCompat.setOnCheckedChangeListener { buttonView, isChecked ->
                onCheckChange(isChecked)
            }
        }
}