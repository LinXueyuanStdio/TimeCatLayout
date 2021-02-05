package com.timecat.layout.ui.business.setting

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.timecat.layout.ui.R
import com.timecat.layout.ui.layout.setShakelessClickListener
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
class ImageItem @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AbsItem(context, attrs, defStyleAttr) {

    lateinit var hintTextView: HintTextView
    lateinit var imageView: ImageView
    override fun layoutId(): Int = R.layout.view_setting_image_item_layout
    override fun initView(context: Context, attrs: AttributeSet?) {
        super.initView(context, attrs)
        hintTextView = findViewById(R.id.htv)
        imageView = findViewById(R.id.iv)

        imageView.isClickable = false
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

    fun onClick(onClick: (item: ImageItem) -> Unit) {
        setShakelessClickListener {
            onClick(this@ImageItem)
        }
    }

    fun setImage(url: String?) {
        IconLoader.loadIcon(context, imageView, url)
    }
}