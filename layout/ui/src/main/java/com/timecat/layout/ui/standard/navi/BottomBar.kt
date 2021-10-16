package com.timecat.layout.ui.standard.navi

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.os.Parcel
import android.os.Parcelable
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.AnimationUtils
import android.view.animation.Interpolator
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import com.timecat.layout.ui.R
import java.util.*

open class BottomBar @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    LinearLayout(context, attrs, defStyleAttr) {
    private val mInterpolator: Interpolator = AccelerateDecelerateInterpolator()
    var isVisible = true
        private set
    private val mTabs: MutableList<AbstractBottomBarTab> = ArrayList()
    private var horizontalScrollView: HorizontalScrollView? = null
    private var mTabLayout: LinearLayout? = null
    private var mTabParams: LayoutParams? = null
    var currentItemPosition = 0
        private set
    private var mListener: OnTabSelectedListener? = null
    private fun init(context: Context, attrs: AttributeSet?) {
        orientation = HORIZONTAL
        horizontalScrollView = HorizontalScrollView(context)
        mTabLayout = LinearLayout(context)
        mTabLayout!!.orientation = HORIZONTAL
        horizontalScrollView!!.addView(
            mTabLayout, ViewGroup.LayoutParams(
                0,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        )
        val h = resources.getDimension(R.dimen.t_miniPlayBarHeight).toInt()
        mTabParams = LayoutParams(h, h)
    }

    /**
     * tab 不要自己设置点击事件，交给容器来
     *
     * @param tab tab
     * @return 自己
     */
    fun addItem(tab: AbstractBottomBarTab): BottomBar {
        mTabLayout!!.addView(tab, mTabLayout!!.childCount - fixedLastItemCount)
        bindAnimation(tab)
        attachToList(tab)
        return this
    }

    fun addHeadItem(tab: AbstractBottomBarTab): BottomBar {
        addView(tab)
        attachToList(tab)
        return this
    }

    /**
     * 只能调用一次
     *
     * @return 构造者模式
     */
    fun addHorizonSV(): BottomBar {
        val svParams = LayoutParams(
            0,
            LayoutParams.MATCH_PARENT
        )
        svParams.weight = 1f
        addView(horizontalScrollView, svParams)
        return this
    }

    /**
     * 在 mTabs.add(tab); 之后调用
     *
     * @param tab tab
     */
    fun config(tab: AbstractBottomBarTab) {
        tab.setOnClickListener {
            if (mListener == null) return@setOnClickListener
            val pos = tab.tabPosition
            if (currentItemPosition == pos) {
                mListener!!.onTabReselected(currentItemPosition)
            } else {
                mListener!!.onTabSelected(pos, currentItemPosition)
                tab.isSelected = true
                mListener!!.onTabUnselected(currentItemPosition)
                mTabs[currentItemPosition].isSelected = false
                currentItemPosition = pos
            }
        }
        tab.setOnLongClickListener {
            if (mListener == null) return@setOnLongClickListener false
            val pos = tab.tabPosition
            mListener!!.onTabLongSelected(pos)
            true
        }
        tab.tabPosition = mTabs.size - 1
        tab.layoutParams = mTabParams
    }

    fun attachToList(tab: AbstractBottomBarTab): BottomBar {
        mTabs.add(tab)
        config(tab)
        return this
    }

    /**
     * tab 自己负责点击事件，点击后不选中，一般是启动另一个页面的时候使用
     * 如果想作为其他tab，即点击后选中，则请继续调用 attachToList
     * @param tab 加
     * @return 自己
     */
    fun addOrReplaceLastItem(tab: AbstractBottomBarTab): BottomBar {
        tab.layoutParams = mTabParams
        val count = childCount
        if (count > 0 && getChildAt(count - 1) is AbstractBottomBarTab) {
            removeViewAt(count - 1)
        }
        addView(tab)
        bindAnimation(tab)
        return this
    }

    private var fixedLastItemCount = 0

    /**
     * tab 自己负责点击事件
     *
     * @param tab 加
     * @return 自己
     */
    fun addFixedLastItem(tab: AbstractBottomBarTab): BottomBar {
        val h = resources.getDimension(R.dimen.t_miniPlayBarHeight).toInt()
        val tabParams = LayoutParams(h, h)
        tabParams.gravity = Gravity.END or Gravity.CENTER_VERTICAL
        tab.layoutParams = tabParams
        mTabLayout!!.addView(tab)
        fixedLastItemCount++
        return this
    }

    fun clearContainer() {
        resetAnimation()
        mTabLayout!!.removeViews(0, mTabLayout!!.childCount - fixedLastItemCount)
    }

    fun setOnTabSelectedListener(onTabSelectedListener: OnTabSelectedListener?) {
        mListener = onTabSelectedListener
    }

    fun setCurrentItem(position: Int) {
        post {
            getChildAt(position)?.performClick()
        }
    }

    /**
     * 获取 Tab
     */
    fun getItem(index: Int): AbstractBottomBarTab? {
        return if (mTabs.size < index) null else mTabs[index]
    }

    /**
     * 获取 Tab
     */
    val currentItem: AbstractBottomBarTab?
        get() = getItem(currentItemPosition)

    interface OnTabSelectedListener {
        fun onTabSelected(position: Int, prePosition: Int)
        fun onTabUnselected(position: Int)
        fun onTabLongSelected(position: Int)
        fun onTabReselected(position: Int)
    }

    override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()
        return SavedState(superState, currentItemPosition)
    }

    override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState
        super.onRestoreInstanceState(ss.superState)
        if (currentItemPosition != ss.position) {
            val view = mTabLayout!!.getChildAt(currentItemPosition)
            if (view != null) {
                view.isSelected = false
            }
            val view2 = mTabLayout!!.getChildAt(ss.position)
            if (view2 != null) {
                view2.isSelected = true
            }
        }
        currentItemPosition = ss.position
    }

    internal class SavedState : BaseSavedState {
        var position: Int

        constructor(source: Parcel) : super(source) {
            position = source.readInt()
        }

        constructor(superState: Parcelable?, position: Int) : super(superState) {
            this.position = position
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeInt(position)
        }

        companion object {
            @JvmField
            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState? {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    //region hide show
    @JvmOverloads
    fun hide(anim: Boolean = true) {
        toggle(false, anim, false)
    }

    @JvmOverloads
    fun show(anim: Boolean = true) {
        toggle(true, anim, false)
    }

    private fun toggle(visible: Boolean, animate: Boolean, force: Boolean) {
        if (isVisible != visible || force) {
            isVisible = visible
            val height = height
            if (height == 0 && !force) {
                val vto = viewTreeObserver
                if (vto.isAlive) {
                    // view树完成测量并且分配空间而绘制过程还没有开始的时候播放动画。
                    vto.addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
                        override fun onPreDraw(): Boolean {
                            val currentVto = viewTreeObserver
                            if (currentVto.isAlive) {
                                currentVto.removeOnPreDrawListener(this)
                            }
                            toggle(visible, animate, true)
                            return true
                        }
                    })
                    return
                }
            }
            val translationY = if (visible) 0 else height
            if (animate) {
                animate().setInterpolator(mInterpolator)
                    .setDuration(TRANSLATE_DURATION_MILLIS.toLong())
                    .translationY(translationY.toFloat())
            } else {
                setTranslationY(translationY.toFloat())
            }
        }
    }
    //endregion

    //region anim add item
    protected open val isAnimationEnabled: Boolean
        get() = true
    private var isAnimating = false

    private var animationStartOffset = 0

    private val stopAnimationHandler = Handler(Looper.getMainLooper())
    private val stopAnimationRunnable = Runnable { stopAnimation() }

    fun bindAnimation(holder: AbstractBottomBarTab) {
        holder.clearAnimation()
        if (isAnimating) {
            val animation = AnimationUtils.loadAnimation(context, R.anim.item_on_load_hori)
                .apply { startOffset = animationStartOffset.toLong() }
            animationStartOffset += ANIMATION_STAGGER_MILLIS
            holder.startAnimation(animation)
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

    private fun clearAnimationOfSubView() {
        stopAnimation()
        mTabLayout?.let {
            for (index in 0 until it.childCount) {
                it.getChildAt(index).clearAnimation()
            }
        }
    }

    private fun resetAnimation() {
        clearAnimationOfSubView()
        isAnimating = isAnimationEnabled
    }

    //endregion

    companion object {
        private const val ANIMATION_STAGGER_MILLIS = 20
        private const val TRANSLATE_DURATION_MILLIS = 200
    }

    init {
        init(context, attrs)
    }
}