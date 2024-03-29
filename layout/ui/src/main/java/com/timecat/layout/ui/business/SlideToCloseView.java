package com.timecat.layout.ui.business;

import android.content.Context;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Scroller;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/5/11
 * @description 滑动关闭闹钟
 * @usage null
 */
public class SlideToCloseView extends RelativeLayout {

    /**
     * smooth scroll time
     */
    private static final int DURATION = 600;

    private static final int MIN_FLING_VELOCITY = 400;
    private Context mContext;
    private Scroller mScroller;

    /**
     * 滑动解锁最小速率
     */
    private int mMinimumVelocity;

    /**
     * 屏幕宽度
     */
    private int mScreenWidth = 0;

    /**
     * 最后一次移动X轴的位置
     */
    private int mLastX = 0;

    /**
     * 按下时X轴的位置
     */
    private int mLastDownX = 0;

    /**
     * 标识是否解锁
     */
    private boolean mCloseFlag = false;

    /**
     * 速率跟踪
     */
    private VelocityTracker mVelocityTracker;

    /**
     * 滑动解锁成功Listener
     */
    private SlidingTipListener mSlidingTipListener;

    public void setSlidingTipListener(SlidingTipListener slidingTipListener) {
        mSlidingTipListener = slidingTipListener;
    }

    public SlideToCloseView(Context context) {
        super(context);
    }

    public SlideToCloseView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public SlideToCloseView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    private void init() {
        WindowManager windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics displayMetrics = new DisplayMetrics();
        windowManager.getDefaultDisplay().getMetrics(displayMetrics);
        mScreenWidth = displayMetrics.widthPixels;
        mScroller = new Scroller(mContext);
        mMinimumVelocity = (int) (MIN_FLING_VELOCITY * getResources().getDisplayMetrics().density);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        boolean intercepted = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                intercepted = false;
                mLastDownX = (int) event.getX();
                //                LogUtil.i(LOG_TAG, "mLastDownX= " + mLastDownX);
                break;
            case MotionEvent.ACTION_MOVE:
                // <!-- Base "touch slop" value used by ViewConfiguration as a
                // <dimen name="config_viewConfigurationTouchSlop">8dp</dimen>
                intercepted = Math.abs(event.getX() - mLastDownX) >= ViewConfiguration.get(
                        getContext()).getScaledTouchSlop();
                break;
            case MotionEvent.ACTION_UP:
                intercepted = false;
                break;
        }
        return intercepted;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        acquireVelocityTracker(event);
        int x = (int) event.getX();
        //        LogUtil.i(LOG_TAG, "x= " + x);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (!mScroller.isFinished()) {
                    mScroller.abortAnimation();
                }
                //                mLastX = x;
                //                LogUtil.i(LOG_TAG, "mLastX(ACTION_DOWN)= " + mLastX);
                break;
            case MotionEvent.ACTION_MOVE:
                // 每次滑动的距离
                int scrollX = x - mLastX;
                // 总共滑动的距离
                int deltaX = x - mLastDownX;
                // 右滑有效
                if (deltaX > 0) {
                    scrollBy(-scrollX, 0);
                }
                break;
            case MotionEvent.ACTION_UP:
                final VelocityTracker velocityTracker = mVelocityTracker;
                velocityTracker.computeCurrentVelocity(1000);
                int xVelocity = (int) mVelocityTracker.getXVelocity();
                //                LogUtil.i(LOG_TAG, "xVelocity= " + xVelocity);
                releaseVelocityTracker();
                // 总共滑动的距离
                deltaX = x - mLastDownX;
                // 当总共滑动距离超过半屏时/超过最低解锁速率时解锁
                if (deltaX > 0) {
                    if ((Math.abs(deltaX) > mScreenWidth / 2) || xVelocity > mMinimumVelocity) {
                        smoothScrollTo(getScrollX(), -mScreenWidth, DURATION);
                        mCloseFlag = true;

                    } else { // 向右滑动未超过半个屏幕宽的时候 开启向左弹动动画
                        smoothScrollTo(getScrollX(), -getScrollX(), DURATION);
                    }
                } else {// 回滚到原位置
                    smoothScrollTo(getScrollX(), -getScrollX(), DURATION);
                }
                break;
        }
        mLastX = x;
        //        LogUtil.i(LOG_TAG, "mLastX= " + mLastX);
        return true;
    }

    /**
     * @param event 向VelocityTracker添加MotionEvent
     * @see android.view.VelocityTracker#obtain()
     * @see android.view.VelocityTracker#addMovement(MotionEvent)
     */
    private void acquireVelocityTracker(MotionEvent event) {
        if (null == mVelocityTracker) {
            mVelocityTracker = VelocityTracker.obtain();
        }
        mVelocityTracker.addMovement(event);
    }

    /**
     * 释放VelocityTracker
     *
     * @see android.view.VelocityTracker#clear()
     * @see android.view.VelocityTracker#recycle()
     */
    private void releaseVelocityTracker() {
        if (null != mVelocityTracker) {
            mVelocityTracker.clear();
            mVelocityTracker.recycle();
            mVelocityTracker = null;
        }
    }

    /**
     * 滚动解锁
     *
     * @param startX   起始位置的X坐标点
     * @param deltaX   结束时的X坐标点
     * @param duration 动画时长
     */
    private void smoothScrollTo(int startX, int deltaX, int duration) {
        mScroller.startScroll(startX, 0, deltaX, 0, duration);
        invalidate();
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            // 更新界面
            postInvalidate();
        } else if (mCloseFlag) {
            mSlidingTipListener.onSlidFinish();
        }
    }

    /**
     * 滑动解锁Listener
     */
    public interface SlidingTipListener {
        void onSlidFinish();
    }
}
