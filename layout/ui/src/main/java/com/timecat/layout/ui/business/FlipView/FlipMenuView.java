package com.timecat.layout.ui.business.FlipView;


import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Camera;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.IntDef;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2018/4/5
 * @description null
 * @usage null
 */
public class FlipMenuView extends View {

    private static final int STATUS_NONE = 0;
    private static final int STATUS_DOWN = 1;
    private static final int STATUS_UP = 2;

    public static final int TYPE_VERTICLE = 0;
    public static final int TYPE_HORIZONTAL = 1;
    public static final int TYPE_SLIDE = 1 << 1;

    private int mStatus = STATUS_NONE;

    private Paint mPaint;
    private Path mPath;
    private Camera mCamera;
    private Matrix mMatrix;
    //item's flip degree
    private float mDegree;
    //item's shake degree
    private float mItemShakeDegree;
    //current item which is showing
    private int mCurrentItem = 0;
    //everyItem's animation duration
    private int mDuration = 200;
    //total window view's background color
    private int mBackgroundColor = 0x00FFFFFF;
    //separate line's color
    private int mSeparateLineColor = Color.TRANSPARENT;
    //separate line's height
    private int mSeparateLineHeight = dip2px(0.4f);
    //background color area
    private RectF mBgRect;
    //shareItem's width
    private int mItemWidth = dip2px(160);
    //shareItem's height
    private int mItemHeight = dip2px(40);
    //shareItem's left x position
    private int mItemLeft = dip2px(0);
    //shareItem's top y position
    private int mItemTop = dip2px(10);
    //item's corner
    private int mCorner = dip2px(6);
    //triangle's height
    private int mTriangleHeight = dip2px(4);
    //border's margin
    private int mBorderMargin = dip2px(5);
    //the first shown item's top y coordinate
    private int mFirstItemTop;
    //screen width
    private int mScreenWidth;
    //screen height
    private int mScreenHeight;
    //parent view is used to position
    private View mParentView;
    //shareItem lists
    private List<FlipMenuItem> mItemList = new ArrayList<>();
    private List<RectF> mItemRectList = new ArrayList<>();

    private int mAnimType = TYPE_VERTICLE;
    private final List<AnimatorSet> mTotalAnimList = new ArrayList<>();
    private ObjectAnimator mAlphaAnim;

    private OnFlipClickListener onFlipClickListener;

    public FlipMenuView(Context context, Window window, View parentView, Config config) {
        super(context);
        mParentView = parentView;
        mScreenWidth = getResources().getDisplayMetrics().widthPixels;
        mScreenHeight = getResources().getDisplayMetrics().heightPixels;
        mBgRect = new RectF(0, 0, mScreenWidth, mScreenHeight);
        mItemWidth = dip2px(config.mItemWidth);
        mItemHeight = dip2px(config.mItemHeight);
        mItemLeft = dip2px(config.mItemLeft);
        mItemTop = dip2px(config.mItemTop);
        mCorner = dip2px(config.mCorner);
        mTriangleHeight = dip2px(config.mTriangleHeight);
        mBorderMargin = dip2px(config.mBorderMargin);

        addView(window);
        initView();
    }

    private void addView(Window window) {
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        window.addContentView(this, params);
    }

    private void initView() {
        mPath = new Path();
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setTextSize(sp2px(14));
        mCamera = new Camera();
        mMatrix = new Matrix();

        mAlphaAnim = ObjectAnimator.ofFloat(this, "alpha", 1.0f, 0f);
        mAlphaAnim.setDuration(200);
        mAlphaAnim.addListener(new MyAnimListener() {
            @Override
            public void onAnimationEnd(Animator animation) {
                removeView();
                if (onFlipClickListener != null) {
                    onFlipClickListener.dismiss();
                }
            }
        });

        int parentViewWidth = mParentView.getMeasuredWidth();
        int parentViewHeight = mParentView.getMeasuredHeight();
        int[] location = new int[2];
        mParentView.getLocationInWindow(location);
        int parentTop = location[1] - parentViewHeight / 2;
        int parentBottom = location[1] + parentViewHeight / 2;
        int parentMiddleX = location[0] + parentViewWidth / 2;

        if (location[1] < mScreenHeight / 2) {
            mStatus = STATUS_DOWN;
            mItemTop = parentBottom + dip2px(5);
            mFirstItemTop = mItemTop + dip2px(6);
        } else {
            mStatus = STATUS_UP;
            mItemTop = parentTop - dip2px(5);
            mFirstItemTop = mItemTop - dip2px(6);
        }

        if (parentMiddleX + mItemWidth / 2 > mScreenWidth) {
            mItemLeft = mScreenWidth - mItemWidth - mBorderMargin - parentMiddleX;
        } else if (parentMiddleX - mItemWidth / 2 < 0) {
            mItemLeft = mBorderMargin;
        } else {
            mItemLeft = parentMiddleX - mItemWidth / 2;
        }


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        switch (mStatus) {
            case STATUS_NONE:

                break;
            case STATUS_DOWN:
                drawBackground(canvas);
                drawFlipDownItem(canvas);
                break;
            case STATUS_UP:
                drawBackground(canvas);
                drawFlipUpItem(canvas);
                break;
        }

    }

    private void drawBackground(Canvas canvas) {
        mPaint.setColor(mBackgroundColor);
        canvas.drawRect(mBgRect, mPaint);
    }

    private void drawFlipUpItem(Canvas canvas) {
        mItemRectList.clear();

        for (int i = mItemList.size() - 1; i >= 0; i--) {
            canvas.save();
            mCamera.save();

            if (i < mItemList.size() - mCurrentItem - 1) {
                mCamera.restore();
                canvas.restore();
                continue;
            } else {
                if (i == mItemList.size() - mCurrentItem - 1) {
                    if (mAnimType == TYPE_HORIZONTAL) {
                        mCamera.rotateY(mDegree);
                    } else if (mAnimType == TYPE_VERTICLE) {
                        mCamera.rotateX(mDegree);
                    } else if (mAnimType == TYPE_SLIDE) {
                        mCamera.rotateZ(mDegree);
                    }
                } else {
                    if (mAnimType == TYPE_HORIZONTAL) {
                        mCamera.rotateY(mItemShakeDegree / 2);
                    } else if (mAnimType == TYPE_VERTICLE) {
                        mCamera.rotateX(mItemShakeDegree);
                    } else if (mAnimType == TYPE_SLIDE) {
                        mCamera.rotateZ(mItemShakeDegree / 4);
                    }
                }
                mCamera.getMatrix(mMatrix);
                if (mAnimType == TYPE_SLIDE) {
                    mMatrix.preTranslate(-(mItemLeft + mItemWidth / 2), -mItemHeight / 2);
                    mMatrix.postTranslate(mItemLeft + mItemWidth / 2, mItemHeight / 2);
                } else {
                    mMatrix.preTranslate(-(mItemLeft + mItemWidth / 2), -(mItemTop - (mItemList.size() - i - 1) * mItemHeight));
                    mMatrix.postTranslate(mItemLeft + mItemWidth / 2, mItemTop - (mItemList.size() - i - 1) * mItemHeight);
                }

                canvas.concat(mMatrix);
            }

            mPaint.setColor(mItemList.get(i).bgColor);
            if (i == mItemList.size() - 1) {
                mPath.reset();
                int[] location = new int[2];
                mParentView.getLocationInWindow(location);
                int parentMiddleX = location[0] + mParentView.getMeasuredWidth() / 2;
                mPath.moveTo(parentMiddleX, mItemTop);
                mPath.lineTo(parentMiddleX - mTriangleHeight, mFirstItemTop);
                mPath.lineTo(parentMiddleX + mTriangleHeight, mFirstItemTop);
                canvas.drawPath(mPath, mPaint);
            }

            if (i == 0) {
                mPath.reset();
                mPath.moveTo(mItemLeft, mFirstItemTop - (mItemList.size() - 1) * mItemHeight);
                mPath.lineTo(mItemLeft + mItemWidth, mFirstItemTop - (mItemList.size() - 1) * mItemHeight);
                mPath.lineTo(mItemLeft + mItemWidth, mFirstItemTop - (mItemList.size() - 1) * mItemHeight - mItemHeight + mCorner);
                mPath.quadTo(mItemLeft + mItemWidth, mFirstItemTop - (mItemList.size() - 1) * mItemHeight - mItemHeight
                        , mItemLeft + mItemWidth - mCorner, mFirstItemTop - (mItemList.size() - 1) * mItemHeight - mItemHeight);
                mPath.lineTo(mItemLeft + mCorner, mFirstItemTop - (mItemList.size() - 1) * mItemHeight - mItemHeight);
                mPath.quadTo(mItemLeft, mFirstItemTop - (mItemList.size() - 1) * mItemHeight - mItemHeight
                        , mItemLeft, mFirstItemTop - (mItemList.size() - 1) * mItemHeight - mItemHeight + mCorner);
                mPath.lineTo(mItemLeft, mFirstItemTop - (mItemList.size() - 1) * mItemHeight);
                canvas.drawPath(mPath, mPaint);
                mPaint.setColor(mSeparateLineColor);
                canvas.drawLine(mItemLeft, mFirstItemTop - (mItemList.size() - 1) * mItemHeight - mSeparateLineHeight
                        , mItemLeft + mItemWidth, mFirstItemTop - (mItemList.size() - 1) * mItemHeight - mSeparateLineHeight, mPaint);
            } else if (i == mItemList.size() - 1) {
                mPath.reset();
                mPath.moveTo(mItemLeft, mFirstItemTop - (mItemList.size() - i) * mItemHeight);
                mPath.lineTo(mItemLeft + mItemWidth, mFirstItemTop - (mItemList.size() - i) * mItemHeight);
                mPath.lineTo(mItemLeft + mItemWidth, mFirstItemTop - (mItemList.size() - i) * mItemHeight + mItemHeight - mCorner);
                mPath.quadTo(mItemLeft + mItemWidth, mFirstItemTop - (mItemList.size() - i) * mItemHeight + mItemHeight
                        , mItemLeft + mItemWidth - mCorner, mFirstItemTop - (mItemList.size() - i) * mItemHeight + mItemHeight);
                mPath.lineTo(mItemLeft + mCorner, mFirstItemTop - (mItemList.size() - i) * mItemHeight + mItemHeight);
                mPath.quadTo(mItemLeft, mFirstItemTop - (mItemList.size() - i) * mItemHeight + mItemHeight
                        , mItemLeft, mFirstItemTop - (mItemList.size() - i) * mItemHeight + mItemHeight - mCorner);
                mPath.lineTo(mItemLeft, mFirstItemTop - (mItemList.size() - i) * mItemHeight);
                canvas.drawPath(mPath, mPaint);
            } else {
                canvas.drawRect(mItemLeft, mFirstItemTop - mItemHeight - (mItemList.size() - i - 1) * mItemHeight
                        , mItemLeft + mItemWidth, mFirstItemTop - (mItemList.size() - i - 1) * mItemHeight, mPaint);
                mPaint.setColor(mSeparateLineColor);
                canvas.drawLine(mItemLeft, mFirstItemTop - (mItemList.size() - i - 1) * mItemHeight - mSeparateLineHeight
                        , mItemLeft + mItemWidth, mFirstItemTop - (mItemList.size() - i - 1) * mItemHeight - mSeparateLineHeight, mPaint);
            }

            mItemRectList.add(new RectF(mItemLeft, (mFirstItemTop - mItemHeight) - i * mItemHeight, mItemLeft + mItemWidth, mFirstItemTop - i * mItemHeight));

            drawTitle(canvas, i);
            drawIcon(canvas, i);

            mCamera.restore();
            canvas.restore();
        }

    }

    private void drawFlipDownItem(Canvas canvas) {

        mItemRectList.clear();
        for (int i = 0; i < mItemList.size(); i++) {
            canvas.save();
            mCamera.save();

            if (i > mCurrentItem) {
                mCamera.restore();
                canvas.restore();
                continue;
            } else {
                if (i == mCurrentItem) {
                    if (mAnimType == TYPE_HORIZONTAL) {
                        mCamera.rotateY(mDegree);
                    } else if (mAnimType == TYPE_VERTICLE) {
                        mCamera.rotateX(mDegree);
                    } else if (mAnimType == TYPE_SLIDE) {
                        mCamera.rotateZ(mDegree);
                    }
                } else {
                    if (mAnimType == TYPE_HORIZONTAL) {
                        mCamera.rotateY(mItemShakeDegree / 2);
                    } else if (mAnimType == TYPE_VERTICLE) {
                        mCamera.rotateX(mItemShakeDegree);
                    } else if (mAnimType == TYPE_SLIDE) {
                        mCamera.rotateZ(mItemShakeDegree);
                    }
                }
                mCamera.getMatrix(mMatrix);
                if (mAnimType == TYPE_SLIDE) {
                    mMatrix.preTranslate(-(mItemLeft + mItemWidth / 2), -mItemHeight / 2);
                    mMatrix.postTranslate(mItemLeft + mItemWidth / 2, mItemHeight / 2);
                } else {
                    mMatrix.preTranslate(-(mItemLeft + mItemWidth / 2), -(mItemTop + i * mItemHeight));
                    mMatrix.postTranslate(mItemLeft + mItemWidth / 2, mItemTop + i * mItemHeight);
                }
                canvas.concat(mMatrix);
            }

            mPaint.setColor(mItemList.get(i).bgColor);
            if (i == 0) {
                mPath.reset();
                int[] location = new int[2];
                mParentView.getLocationInWindow(location);
                int parentMiddleX = location[0] + mParentView.getMeasuredWidth() / 2;
                mPath.moveTo(parentMiddleX, mItemTop);
                mPath.lineTo(parentMiddleX - mTriangleHeight, mFirstItemTop);
                mPath.lineTo(parentMiddleX + mTriangleHeight, mFirstItemTop);
                canvas.drawPath(mPath, mPaint);
            }

            if (i == 0) {
                mPath.reset();
                mPath.moveTo(mItemLeft, mFirstItemTop + mItemHeight);
                mPath.lineTo(mItemLeft + mItemWidth, mFirstItemTop + mItemHeight);
                mPath.lineTo(mItemLeft + mItemWidth, mFirstItemTop + mCorner);
                mPath.quadTo(mItemLeft + mItemWidth, mFirstItemTop, mItemLeft + mItemWidth - mCorner, mFirstItemTop);
                mPath.lineTo(mItemLeft + mCorner, mFirstItemTop);
                mPath.quadTo(mItemLeft, mFirstItemTop, mItemLeft, mFirstItemTop + mCorner);
                mPath.lineTo(mItemLeft, mFirstItemTop + mItemHeight);
                canvas.drawPath(mPath, mPaint);
            } else if (i == mItemList.size() - 1) {
                mPath.reset();
                mPath.moveTo(mItemLeft, mFirstItemTop + i * mItemHeight);
                mPath.lineTo(mItemLeft + mItemWidth, mFirstItemTop + i * mItemHeight);
                mPath.lineTo(mItemLeft + mItemWidth, mFirstItemTop + (i + 1) * mItemHeight - mCorner);
                mPath.quadTo(mItemLeft + mItemWidth, mFirstItemTop + (i + 1) * mItemHeight, mItemLeft + mItemWidth - mCorner, mFirstItemTop + (i + 1) * mItemHeight);
                mPath.lineTo(mItemLeft + mCorner, mFirstItemTop + (i + 1) * mItemHeight);
                mPath.quadTo(mItemLeft, mFirstItemTop + (i + 1) * mItemHeight, mItemLeft, mFirstItemTop + (i + 1) * mItemHeight - mCorner);
                mPath.lineTo(mItemLeft, mFirstItemTop + i * mItemHeight);
                canvas.drawPath(mPath, mPaint);
            } else {
                canvas.drawRect(mItemLeft, mFirstItemTop + i * mItemHeight, mItemLeft + mItemWidth, mFirstItemTop + mItemHeight + i * mItemHeight, mPaint);
            }

            mItemRectList.add(new RectF(mItemLeft, mFirstItemTop + i * mItemHeight, mItemLeft + mItemWidth, mFirstItemTop + mItemHeight + i * mItemHeight));

            drawTitle(canvas, i);
            drawIcon(canvas, i);

            mCamera.restore();
            canvas.restore();
        }
    }

    private void drawTitle(Canvas canvas, int position) {
        FlipMenuItem shareItem = mItemList.get(position);
        mPaint.setColor(shareItem.titleColor);
        if (mStatus == STATUS_DOWN) {
            canvas.drawText(shareItem.title, mItemLeft + dip2px(8)
                    , mFirstItemTop + getTextHeight(shareItem.title, mPaint) / 2 + mItemHeight / 2 + position * mItemHeight, mPaint);
        } else if (mStatus == STATUS_UP) {
            canvas.drawText(shareItem.title, mItemLeft + dip2px(8)
                    , mFirstItemTop + getTextHeight(shareItem.title, mPaint) / 2 - (mItemHeight / 2 + (mItemList.size() - position - 1) * mItemHeight), mPaint);
        }
    }

    private void drawIcon(Canvas canvas, int position) {
        FlipMenuItem shareItem = mItemList.get(position);
        if (shareItem.icon != null) {
            float left = mItemLeft + mItemWidth - mItemHeight / 2 - dip2px(6);
            if (mStatus == STATUS_DOWN) {
                float top = mFirstItemTop + mItemHeight / 4 + position * mItemHeight;
                canvas.drawBitmap(shareItem.icon, null, new RectF(left, top, left + mItemHeight / 2, top + mItemHeight / 2), mPaint);
            } else if (mStatus == STATUS_UP) {
                float top = mFirstItemTop - (mItemHeight / 4 + (mItemList.size() - position - 1) * mItemHeight);
                canvas.drawBitmap(shareItem.icon, null, new RectF(left - mItemHeight / 2, top - mItemHeight / 2, left, top), mPaint);
            }

        }
    }

    public void startAnim() {

        if (mItemList.size() == 0) {
            throw new RuntimeException("At least set one shareItem");
        }

        mTotalAnimList.clear();

        for (int i = 0; i < mItemList.size(); i++) {
            final Collection<Animator> animList = new ArrayList<>();

            ValueAnimator itemShakeAnim;
            switch (mStatus) {
                case STATUS_DOWN:
                    itemShakeAnim = ValueAnimator.ofFloat(0, 3, 0);
                    break;
                case STATUS_UP:
                    itemShakeAnim = ValueAnimator.ofFloat(0, -3, 0);
                    break;
                default:
                    itemShakeAnim = ValueAnimator.ofFloat(0);
                    break;
            }
            itemShakeAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mItemShakeDegree = (float) animation.getAnimatedValue();
                }
            });

            ValueAnimator itemFlipAnim = ValueAnimator.ofFloat(-90, 8, 0);
            final int index = i;
            itemFlipAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    mDegree = (float) animation.getAnimatedValue();
                    mCurrentItem = index;
                    invalidate();
                }
            });
            animList.add(itemFlipAnim);
            animList.add(itemShakeAnim);
            AnimatorSet set = new AnimatorSet();
            set.setDuration(mDuration);
            set.setInterpolator(new LinearInterpolator());
            set.playTogether(animList);
            mTotalAnimList.add(set);
            set.addListener(new MyAnimListener() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    if (index + 1 < mItemList.size()) {
                        mTotalAnimList.get(index + 1).start();
                    }
                }
            });

        }

        mTotalAnimList.get(0).start();

    }

    public void setFlipMenuItemList(final List<FlipMenuItem> list) {
        mItemList.clear();
        List<FlipMenuItem> items = list;
        for (FlipMenuItem item : items) {
            if (!TextUtils.isEmpty(item.title)) {
                item.title = updateTitle(item.title, item.icon != null);
            } else {
                item.title = "";
            }

            mItemList.add(item);
        }
    }

    private String updateTitle(String title, boolean hasIcon) {
        int textLength = title.length();
        String suffix = "";
        while (getTextWidth(title.substring(0, textLength) + "...", mPaint) > mItemWidth - dip2px(10) - (hasIcon ? (dip2px(6) + mItemHeight / 2) : 0)) {
            textLength--;
            suffix = "...";
        }
        return title.substring(0, textLength) + suffix;
    }

    public void setItemDuration(int mils) {
        mDuration = mils;
    }

    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
    }

    public void setSeparateLineColor(int separateLineColor) {
        mSeparateLineColor = separateLineColor;
    }

    private void dismiss() {
        for (Animator animator : mTotalAnimList) {
            animator.cancel();
        }

        if (!mAlphaAnim.isRunning()) {
            mAlphaAnim.start();
        }
    }

    private void removeView() {
        ViewGroup vg = (ViewGroup) this.getParent();
        if (vg != null) {
            vg.removeView(this);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                return true;
            case MotionEvent.ACTION_UP:
                for (int i = 0; i < mItemRectList.size(); i++) {
                    if (onFlipClickListener != null && isPointInRect(new PointF(event.getX(), event.getY()), mItemRectList.get(i))) {
                        onFlipClickListener.onItemClick(i);
                    }
                }
                dismiss();
                return true;
        }
        return true;
    }

    private boolean isPointInRect(PointF pointF, RectF targetRect) {
        if (pointF.x < targetRect.left) {
            return false;
        }
        if (pointF.x > targetRect.right) {
            return false;
        }
        if (pointF.y < targetRect.top) {
            return false;
        }
        if (pointF.y > targetRect.bottom) {
            return false;
        }
        return true;
    }

    private int dip2px(float dipValue) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    private int sp2px(float spValue) {
        final float fontScale = getContext().getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    private float getTextHeight(String text, Paint paint) {
        Rect rect = new Rect();
        paint.getTextBounds(text, 0, text.length(), rect);
        return rect.height() / 1.1f;
    }

    private float getTextWidth(String text, Paint paint) {
        return paint.measureText(text);
    }

    @IntDef(flag = true, value = {TYPE_VERTICLE, TYPE_HORIZONTAL, TYPE_SLIDE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface AnimType {}

    public void setAnimType(@AnimType int animType) {
        mAnimType = animType;
    }

    public void setOnFlipClickListener(OnFlipClickListener onFlipClickListener) {
        this.onFlipClickListener = onFlipClickListener;
    }

    public interface OnFlipClickListener {
        void onItemClick(int position);

        void dismiss();
    }

    abstract class MyAnimListener implements Animator.AnimatorListener {
        @Override
        public void onAnimationStart(Animator animation) {

        }

        @Override
        public void onAnimationCancel(Animator animation) {

        }

        @Override
        public void onAnimationRepeat(Animator animation) {

        }
    }

    static class Config {
        public int mItemWidth = 160;
        //shareItem's height
        public int mItemHeight = 40;
        //shareItem's left x position
        public int mItemLeft = 0;
        //shareItem's top y position
        public int mItemTop = 10;
        //item's corner
        public int mCorner = 6;
        //triangle's height
        public int mTriangleHeight = 4;
        //border's margin
        public int mBorderMargin = 5;

        public Config(int mItemWidth, int mItemHeight, int mItemLeft, int mItemTop, int mCorner, int mTriangleHeight, int mBorderMargin) {
            this.mItemWidth = mItemWidth;
            this.mItemHeight = mItemHeight;
            this.mItemLeft = mItemLeft;
            this.mItemTop = mItemTop;
            this.mCorner = mCorner;
            this.mTriangleHeight = mTriangleHeight;
            this.mBorderMargin = mBorderMargin;
        }
    }

    public static class Builder {

        private Activity mActivity;
        private View mParentView;
        private List<FlipMenuItem> mFlipMenuItemList = new ArrayList<>();
        private int mMilliSecond = 300;
        private int mBgColor = 0x00FFFFFF;
        private int mAnimType = FlipMenuView.TYPE_VERTICLE;
        private int mSeparateLineColor = Color.TRANSPARENT;

        private int mItemWidth = 160;
        //shareItem's height
        private int mItemHeight = 50;
        //shareItem's left x position
        private int mItemLeft = 0;
        //shareItem's top y position
        private int mItemTop = 10;
        //item's corner
        private int mCorner = 6;
        //triangle's height
        private int mTriangleHeight = 4;
        //border's margin
        private int mBorderMargin = 5;

        public Builder(Activity activity, View parentView) {
            mActivity = activity;
            mParentView = parentView;
        }

        public Builder addItem(FlipMenuItem shareItem) {
            mFlipMenuItemList.add(shareItem);
            return this;
        }

        public Builder addItems(List<FlipMenuItem> list) {
            mFlipMenuItemList.addAll(list);
            return this;
        }

        public Builder setItemDuration(int mils) {
            mMilliSecond = mils;
            return this;
        }

        public Builder setAnimType(@AnimType int animType) {
            mAnimType = animType;
            return this;
        }

        public Builder setBackgroundColor(int color) {
            mBgColor = color;
            return this;
        }

        public Builder setSeparateLineColor(int color) {
            mSeparateLineColor = color;
            return this;
        }

        public Builder setmItemWidth(int mItemWidth) {
            this.mItemWidth = mItemWidth;
            return this;
        }

        public Builder setmItemHeight(int mItemHeight) {
            this.mItemHeight = mItemHeight;
            return this;
        }

        public Builder setmItemLeft(int mItemLeft) {
            this.mItemLeft = mItemLeft;
            return this;
        }

        public Builder setmItemTop(int mItemTop) {
            this.mItemTop = mItemTop;
            return this;
        }

        public Builder setmCorner(int mCorner) {
            this.mCorner = mCorner;
            return this;
        }

        public Builder setmTriangleHeight(int mTriangleHeight) {
            this.mTriangleHeight = mTriangleHeight;
            return this;
        }

        public Builder setmBorderMargin(int mBorderMargin) {
            this.mBorderMargin = mBorderMargin;
            return this;
        }

        public FlipMenuView create() {
            Config config = new Config(mItemWidth, mItemHeight, mItemLeft, mItemTop, mCorner, mTriangleHeight, mBorderMargin);
            FlipMenuView flipShare = new FlipMenuView(mActivity.getBaseContext(), mActivity.getWindow(), mParentView, config);
            flipShare.setFlipMenuItemList(mFlipMenuItemList);
            flipShare.setItemDuration(mMilliSecond);
            flipShare.setBackgroundColor(mBgColor);
            flipShare.setAnimType(mAnimType);
            flipShare.setSeparateLineColor(mSeparateLineColor);
            flipShare.startAnim();
            return flipShare;
        }

    }

}