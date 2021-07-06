package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.AdapterView
import com.google.android.material.textfield.MaterialAutoCompleteTextView
import com.google.android.material.textfield.TextInputLayout
import com.timecat.component.identity.Attr
import com.timecat.layout.ui.R
import com.timecat.layout.ui.standard.materialspinner.SpinnerAdapter

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/16
 * @description null
 * @usage null
 */
class DropdownInputItem<T> @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsItem(context, attrs, defStyleAttr) {
    override fun layoutId(): Int = R.layout.view_setting_dropdown_input_item_layout

    lateinit var inputLayout: TextInputLayout
    lateinit var autoCompleteTextView: MaterialAutoCompleteTextView

    override fun initView(context: Context, attrs: AttributeSet?) {
        super.initView(context, attrs)
        autoCompleteTextView = findViewById(R.id.et_input)
        inputLayout = findViewById(R.id.input_layout_name)
    }

    override fun setupPadding() {
        val pad = resources.getDimension(R.dimen.card_padding_width).toInt()
        setPadding(pad, pad / 2, pad, pad / 2)
    }

    var hint: String
        get() = inputLayout.hint?.toString() ?: ""
        set(value) {
            inputLayout.hint = value
        }

    var adapter: SpinnerAdapter<T> = SpinnerAdapter<T>(context, listOf())
        set(value) {
            autoCompleteTextView.setAdapter(value)
            field = value
        }

    var items: List<T>
        get() = adapter.items ?: listOf()
        set(value) {
            val a = SpinnerAdapter(context, value)
            a.setTextColor(Attr.getPrimaryTextColor(context))
            adapter = a
            autoCompleteTextView.setText(items[0].toString(), false)
            autoCompleteTextView.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                    selectedItem = items[position]
                    onSelect(items[position], position)
                }

                override fun onNothingSelected(parent: AdapterView<*>?) {
                    selectedItem = null
                }
            }
        }
    var selectedItem: T? = null
    var onSelect: (data: T, index: Int) -> Unit = { _, _ -> }
}