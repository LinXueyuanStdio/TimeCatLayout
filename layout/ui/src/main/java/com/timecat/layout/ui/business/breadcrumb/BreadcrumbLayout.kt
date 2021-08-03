package com.timecat.layout.ui.business.breadcrumb

import android.content.Context
import android.content.res.ColorStateList
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import androidx.annotation.AttrRes
import androidx.annotation.StyleRes
import androidx.appcompat.widget.PopupMenu
import androidx.core.view.isVisible
import com.timecat.layout.ui.R
import com.timecat.layout.ui.databinding.LayoutBreadcrumbItemBinding
import com.timecat.layout.ui.layout.setShakelessClickListener

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/16
 * @description 面板屑
 * @usage null
 */
class BreadcrumbLayout : HorizontalScrollView {

    private val tabLayoutHeight = context.getDimensionPixelSize(R.dimen.tab_layout_height)
    private val itemColor =
        ColorStateList(
            arrayOf(intArrayOf(android.R.attr.state_activated), intArrayOf()),
            intArrayOf(
                context.getColorByAttr(android.R.attr.textColorPrimary),
                context.getColorByAttr(android.R.attr.textColorSecondary)
            )
        )
    private val popupContext =
        context.withTheme(context.getResourceIdByAttr(R.attr.actionBarPopupTheme))

    private val itemsLayout: LinearLayout

    private lateinit var listener: Listener
    private lateinit var data: BreadcrumbData

    private var isLayoutDirty = true
    private var isScrollToSelectedItemPending = false
    private var isFirstScroll = true

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet?) : super(
        context, attrs
    )

    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(
        context, attrs, defStyleAttr
    )

    constructor(
        context: Context,
        attrs: AttributeSet?,
        @AttrRes defStyleAttr: Int,
        @StyleRes defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    init {
        isHorizontalScrollBarEnabled = false
        itemsLayout = LinearLayout(context).apply { orientation = LinearLayout.HORIZONTAL }
        itemsLayout.setPaddingRelative(paddingStart, paddingTop, paddingEnd, paddingBottom)
        setPaddingRelative(0, 0, 0, 0)
        addView(itemsLayout, LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT))
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMode = MeasureSpec.getMode(heightMeasureSpec)
        var heightMeasureSpec = heightMeasureSpec
        if (heightMode == MeasureSpec.UNSPECIFIED || heightMode == MeasureSpec.AT_MOST) {
            var height = tabLayoutHeight
            if (heightMode == MeasureSpec.AT_MOST) {
                height = height.coerceAtMost(MeasureSpec.getSize(heightMeasureSpec))
            }
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY)
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
    }

    override fun requestLayout() {
        isLayoutDirty = true

        super.requestLayout()
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        isLayoutDirty = false
        if (isScrollToSelectedItemPending) {
            scrollToSelectedItem()
            isScrollToSelectedItemPending = false
        }
    }

    fun setListener(listener: Listener) {
        this.listener = listener
    }

    fun setData(data: BreadcrumbData) {
        if (this::data.isInitialized && this.data == data) {
            return
        }
        this.data = data
        inflateItemViews()
        bindItemViews()
        scrollToSelectedItem()
    }

    private fun scrollToSelectedItem() {
        if (isLayoutDirty) {
            isScrollToSelectedItemPending = true
            return
        }
        val selectedItemView = itemsLayout.getChildAt(data.selectedIndex)
        val scrollX = if (layoutDirection == View.LAYOUT_DIRECTION_LTR) {
            selectedItemView.left - itemsLayout.paddingStart
        } else {
            selectedItemView.right - width + itemsLayout.paddingStart
        }
        if (!isFirstScroll && isShown) {
            smoothScrollTo(scrollX, 0)
        } else {
            scrollTo(scrollX, 0)
        }
        isFirstScroll = false
    }

    private fun inflateItemViews() {
        // HACK: Remove/add views at the front so that ripple remains correct, as we are potentially
        // collapsing/expanding breadcrumbs at the front.
        for (index in data.paths.size until itemsLayout.childCount) {
            itemsLayout.removeViewAt(0)
        }
        for (index in itemsLayout.childCount until data.paths.size) {
            val binding = LayoutBreadcrumbItemBinding.inflate(LayoutInflater.from(context), itemsLayout, false)
            binding.textText.setTextColor(itemColor)
            binding.textText.isAllCaps = false
            binding.arrowImage.imageTintList = itemColor
            binding.root.tag = binding
            itemsLayout.addView(binding.root, 0)
        }
    }

    private fun bindItemViews() {
        for (index in data.paths.indices) {
            @Suppress("UNCHECKED_CAST")
            val binding = itemsLayout.getChildAt(index).tag as? LayoutBreadcrumbItemBinding ?: return
            binding.textText.text = data.nameProducers[index](binding.textText.context)
            binding.arrowImage.isVisible = index != data.paths.size - 1
            binding.root.isActivated = index == data.selectedIndex
            val path = data.paths[index]
            binding.root.setShakelessClickListener {
                if (data.selectedIndex == index) {
                    scrollToSelectedItem()
                } else {
                    listener.navigateTo(path)
                }
            }
            binding.root.setOnLongClickListener {
                listener.showMenu(popupContext, binding.root, path)
                true
            }
        }
    }

    interface Listener {
        fun navigateTo(path: Path)
        fun copyPath(path: Path) {}
        fun openInNewTask(path: Path) {}
        fun showMenu(context: Context, anchor: View, path: Path) {
            val menu = PopupMenu(context, anchor).apply {
                inflate(R.menu.breadcrumb)
            }
            menu.setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.action_copy_path -> {
                        copyPath(path)
                        true
                    }
                    R.id.action_open_in_new_task -> {
                        openInNewTask(path)
                        true
                    }
                    else -> false
                }
            }
            menu.show()
        }
    }
}