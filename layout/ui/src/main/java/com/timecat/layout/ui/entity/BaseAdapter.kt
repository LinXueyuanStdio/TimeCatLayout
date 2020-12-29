package com.timecat.layout.ui.entity

import android.os.Handler
import android.os.Looper
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.timecat.layout.ui.R
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.viewholders.FlexibleViewHolder

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/5/27
 * @description null
 * @usage null
 */
open class BaseAdapter : FlexibleAdapter<BaseItem<*>> {
    constructor(items: MutableList<BaseItem<*>>?) : super(items)
    constructor(items: MutableList<BaseItem<*>>?, listeners: Any?) : super(items, listeners)
    constructor(items: MutableList<BaseItem<*>>?, listeners: Any?, stableIds: Boolean)
            : super(items, listeners, stableIds)

    override fun onCreateBubbleText(position: Int): String {
        var pos = position
        pos -= when {
            pos < scrollableHeaders.size -> {
                return "到顶啦"
            }
            pos >= itemCount - scrollableFooters.size -> {
                return "到底啦"
            }
            else -> {
                scrollableHeaders.size + 1
            }
        }
        return super.onCreateBubbleText(pos)
    }

    open fun updateById(id: String) {
        val baseItem = getById(id)
        baseItem?.let { updateItem(it) }
    }

    fun getById(id: String): BaseItem<*>? {
        for (item in currentItems) {
            if (id == item.id) {
                return item
            }
        }
        return null
    }

    private var isAnimating = false

    private var animationStartOffset = 0

    private val stopAnimationHandler = Handler(Looper.getMainLooper())
    private val stopAnimationRunnable = Runnable { stopAnimation() }

    private val clearAnimationListener = object : RecyclerView.OnScrollListener() {
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            clearAnimation()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mRecyclerView.addOnScrollListener(clearAnimationListener)
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        mRecyclerView.removeOnScrollListener(clearAnimationListener)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun clear() {
        resetAnimation()
        super.clear()
    }

    fun reload() {
        resetAnimation()
        notifyDataSetChanged()
    }

    fun reload(items: List<BaseItem<*>>) {
        resetAnimation()
        updateDataSet(items)
        notifyDataSetChanged()
    }

    fun bindViewHolderAnimation(holder: FlexibleViewHolder) {
        holder.itemView.clearAnimation()
        if (isAnimating) {
            val animation = AnimationUtils.loadAnimation(holder.itemView.context, R.anim.item_on_load)
                .apply { startOffset = animationStartOffset.toLong() }
            animationStartOffset += ANIMATION_STAGGER_MILLIS
            holder.itemView.startAnimation(animation)
            postStopAnimation()
        }
    }

    private fun stopAnimation() {
        stopAnimationHandler.removeCallbacks(stopAnimationRunnable)
        isAnimating = false
        animationStartOffset = 0
    }

    private fun postStopAnimation() {
        stopAnimationHandler.removeCallbacks(stopAnimationRunnable)
        stopAnimationHandler.post(stopAnimationRunnable)
    }

    private fun clearAnimation() {
        stopAnimation()
        recyclerView?.let {
            for (index in 0 until it.childCount) {
                it.getChildAt(index).clearAnimation()
            }
        }
    }

    private fun resetAnimation() {
        clearAnimation()
        isAnimating = isAnimationEnabled
    }

    protected open val isAnimationEnabled: Boolean
        get() = true

    companion object {
        private const val ANIMATION_STAGGER_MILLIS = 20
    }
}