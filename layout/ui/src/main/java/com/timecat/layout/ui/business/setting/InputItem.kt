package com.timecat.layout.ui.business.setting

import android.content.Context
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.AttributeSet
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.timecat.layout.ui.R

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/16
 * @description null
 * @usage null
 */
open class InputItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsItem(context, attrs, defStyleAttr) {
    override fun layoutId(): Int = R.layout.view_setting_input_item_layout

    lateinit var inputLayout: TextInputLayout
    lateinit var inputEditText: TextInputEditText
    var tw: TextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
        override fun afterTextChanged(s: Editable) {
            onTextChange(s.toString())
        }
    }

    override fun initView(context: Context, attrs: AttributeSet?) {
        super.initView(context, attrs)
        inputEditText = findViewById(R.id.et_input)
        inputLayout = findViewById(R.id.input_layout_name)
    }

    override fun setupPadding() {
        val pad = resources.getDimension(R.dimen.card_padding_width).toInt()
        setPadding(pad, pad / 2, pad, pad / 2)
    }

    var hint: String
        get() = inputLayout.hint?.toString() ?:""
        set(value) {
            inputLayout.hint = value
        }
    var text: String
        get() = inputEditText.text?.toString() ?: ""
        set(value) {
            inputEditText.removeTextChangedListener(tw)
            inputEditText.setText(value)
            inputEditText.addTextChangedListener(tw)
        }
    var inputType: Int
        get() = inputEditText.inputType
        set(value) {
            inputEditText.inputType = value
        }

    var onTextChange: (value: String?) -> Unit = {}

}