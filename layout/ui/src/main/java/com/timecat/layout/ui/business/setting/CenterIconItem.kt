package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.ImageView
import com.timecat.layout.ui.R
import com.timecat.layout.ui.layout.*
import com.timecat.layout.ui.listener.OnDebouncedClickListener
import com.timecat.layout.ui.standard.textview.HintTextView
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
            layout_width = 96.dp
            layout_height = 96.dp
            centerInParent = true
            isClickable = false
        }
    }

    fun onClick(onClick: (item: CenterIconItem) -> Unit) {
        setShakelessClickListener {
            onClick(this@CenterIconItem)
        }
    }

    fun setImage(url: String?) {
        IconLoader.loadIcon(context, imageView, url)
    }
}