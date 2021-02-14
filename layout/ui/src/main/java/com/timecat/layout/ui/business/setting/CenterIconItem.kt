package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.widget.ImageView
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.utils.IconLoader

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/28
 * @description 图片的设置项
 * @usage 不要在xml里写文案，要在java里写
 */
class CenterIconItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsItem(context, attrs, defStyleAttr) {

    lateinit var imageView: ImageView
    override fun layoutId(): Int = 0
    override fun initView(context: Context, attrs: AttributeSet?) {
        super.initView(context, attrs)
        imageView = ImageView {
            layout_width = 50
            layout_height = 50
            centerInParent = true
            isClickable = false
        }
    }

    var icon: String = "R.drawable.ic_launcher"
        set(value) {
            IconLoader.loadIcon(context, imageView, value)
            field = value
        }

    fun onClick(onClick: (item: CenterIconItem) -> Unit) {
        setShakelessClickListener {
            onClick(this@CenterIconItem)
        }
    }
}