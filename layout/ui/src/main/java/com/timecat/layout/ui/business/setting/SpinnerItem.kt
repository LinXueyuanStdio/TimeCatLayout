package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.widget.TextView
import com.timecat.layout.ui.R
import com.timecat.layout.ui.standard.materialspinner.BlockMaterialSpinner

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/3
 * @description 单选
 * @usage null
 */
class SpinnerItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsItem(context, attrs, defStyleAttr) {

    override fun layoutId(): Int {
        return R.layout.view_setting_spinner_item_layout
    }

    lateinit var titleTv: TextView
    lateinit var materialSpinner: BlockMaterialSpinner
    override fun initView(context: Context, attrs: AttributeSet?) {
        super.initView(context, attrs)
        titleTv = findViewById(R.id.title)
        materialSpinner = findViewById(R.id.ms)
    }

    override fun setupPadding() {
        val pad = resources.getDimension(R.dimen.card_padding_width).toInt()
        setPadding(pad, pad / 2, pad, pad / 2)
    }

    var title: String
        get() = titleTv.text.toString()
        set(value) {
            titleTv.setText(value)
        }

    fun <T> onItemSelected(items: List<T>, onSelect: (data: T, index: Int) -> Unit) {
        materialSpinner.setItems(items)
        materialSpinner.setOnItemSelectedListener(object :
            BlockMaterialSpinner.OnItemSelectedListener<T> {
            override fun onItemSelected(
                view: BlockMaterialSpinner?,
                position: Int,
                id: Long,
                item: T
            ) {
                onSelect(item, position)
            }
        })
    }
}