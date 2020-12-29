package com.timecat.layout.ui.standard.materialspinner;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import androidx.annotation.ArrayRes;
import androidx.annotation.AttrRes;
import androidx.annotation.ColorInt;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StyleableRes;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.appcompat.widget.AppCompatTextView;

import com.blankj.utilcode.util.ScreenUtils;
import com.timecat.extend.arms.BaseApplication;
import com.timecat.layout.ui.R;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * 自定义下拉框，使用AppCompatTextView + PopupWindow组合实现
 *
 * @author xuexiang
 * @since 2018/12/7 下午4:24
 */
public class BlockMaterialSpinner extends AppCompatTextView {

    private OnNothingSelectedListener mOnNothingSelectedListener;
    private OnItemSelectedListener mOnItemSelectedListener;
    private OnNoMoreChoiceListener mOnNoMoreChoiceListener;
    private MaterialSpinnerBaseAdapter mAdapter;
    private PopupWindow mPopupWindow;
    private ListView mListView;
    private Drawable mArrowDrawable;
    private boolean mHideArrow;
    private boolean mNothingSelected;
    private int mPopupWindowMaxHeight;
    private int mPopupWindowHeight;
    private int mSelectedIndex;
    private int mBackgroundColor;
    private int mBackgroundSelector;
    private int mArrowColor;
    private int mArrowColorDisabled;
    private int mTextColor;
    private int mEntriesID;
    private Drawable mDropDownBg;
    private boolean mIsInDialog;
    private int mPopAnimStyle;
    private int mDropDownOffset;

    public BlockMaterialSpinner(Context context) {
        this(context, null);
    }

    public BlockMaterialSpinner(Context context, AttributeSet attrs) {
        this(context, attrs, R.attr.BlockMaterialSpinnerStyle);
    }

    public BlockMaterialSpinner(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttrs(context, attrs, defStyleAttr);
    }

    /**
     * 初始化属性
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.BlockMaterialSpinner, defStyleAttr, 0);
        int defaultColor = getTextColors().getDefaultColor();
        boolean rtl = isRtl();

        try {
            mBackgroundColor = typedArray.getColor(R.styleable.BlockMaterialSpinner_ms_background_color, Color.WHITE);
            mBackgroundSelector = typedArray.getResourceId(R.styleable.BlockMaterialSpinner_ms_background_selector, 0);
            mTextColor = typedArray.getColor(R.styleable.BlockMaterialSpinner_ms_text_color, defaultColor);

            mArrowDrawable = getDrawableAttrRes(getContext(), typedArray, R.styleable.BlockMaterialSpinner_ms_arrow_image);
            mArrowColor = typedArray.getColor(R.styleable.BlockMaterialSpinner_ms_arrow_tint, mTextColor);
            mHideArrow = typedArray.getBoolean(R.styleable.BlockMaterialSpinner_ms_hide_arrow, false);
            mPopupWindowMaxHeight = typedArray.getDimensionPixelSize(R.styleable.BlockMaterialSpinner_ms_dropdown_max_height, 0);
            mPopupWindowHeight = typedArray.getLayoutDimension(R.styleable.BlockMaterialSpinner_ms_dropdown_height, WindowManager.LayoutParams.WRAP_CONTENT);
            mArrowColorDisabled = lighter(mArrowColor, 0.8f);
            mEntriesID = typedArray.getResourceId(R.styleable.BlockMaterialSpinner_ms_entries, 0);
            mDropDownBg = getDrawableAttrRes(getContext(), typedArray, R.styleable.BlockMaterialSpinner_ms_dropdown_bg);
            mIsInDialog = typedArray.getBoolean(R.styleable.BlockMaterialSpinner_ms_in_dialog, false);
            mPopAnimStyle = typedArray.getResourceId(R.styleable.BlockMaterialSpinner_ms_pop_anim_style, -1);

        } finally {
            typedArray.recycle();
        }

        int left, right, bottom, top;
        left = right = bottom = top = resolveDimension(getContext(), R.attr.ms_padding_top_size);
        if (rtl) {
            right = resolveDimension(getContext(), R.attr.ms_padding_left_size);
        } else {
            left = resolveDimension(getContext(), R.attr.ms_padding_left_size);
        }

        mDropDownOffset = resolveDimension(getContext(), R.attr.ms_dropdown_offset);

        setGravity(Gravity.CENTER);
        setClickable(true);
        setPadding(left, top, right, bottom);
        setBackgroundResource(R.drawable.bms_background_selector);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 && rtl) {
            setLayoutDirection(View.LAYOUT_DIRECTION_RTL);
            setTextDirection(View.TEXT_DIRECTION_RTL);
        }

        if (!mHideArrow) {
            if (mArrowDrawable == null) {
                mArrowDrawable = getVectorDrawable(getContext(), R.drawable.ms_ic_arrow_up).mutate();
            }
            mArrowDrawable.setColorFilter(mArrowColor, PorterDuff.Mode.SRC_IN);
            int arrowSize = resolveDimension(getContext(), R.attr.ms_arrow_size);
            mArrowDrawable.setBounds(0, 0, arrowSize, arrowSize);
            if (rtl) {
                setCompoundDrawablesWithIntrinsicBounds(mArrowDrawable, null, null, null);
            } else {
                setCompoundDrawablesWithIntrinsicBounds(null, null, mArrowDrawable, null);
            }
        }

        mListView = new ListView(context);
        mListView.setId(getId());
        mListView.setDivider(null);
        mListView.setItemsCanFocus(true);
        int padding = resolveDimension(getContext(), R.attr.ms_dropdown_offset);
        mListView.setPadding(padding, padding, padding, padding);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position >= mSelectedIndex && position < mAdapter.getCount()) {
                    position++;
                }
                mSelectedIndex = position;
                mNothingSelected = false;
                Object item = mAdapter.get(position);
                mAdapter.notifyItemSelected(position);
                setText(item.toString());
                collapse();
                if (mOnItemSelectedListener != null) {
                    //noinspection unchecked
                    mOnItemSelectedListener.onItemSelected(BlockMaterialSpinner.this, position, id, item);
                }
            }
        });
        if (mEntriesID != 0) {
            setItems(getStringArray(mEntriesID));
        }

        mPopupWindow = new PopupWindow(context);
        mPopupWindow.setContentView(mListView);
        mPopupWindow.setOutsideTouchable(true);
        if (mPopAnimStyle != -1) {
            mPopupWindow.setAnimationStyle(mPopAnimStyle);
        }
        mPopupWindow.setFocusable(true);
        mPopupWindow.setInputMethodMode(PopupWindow.INPUT_METHOD_NOT_NEEDED);
        mPopupWindow.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            mPopupWindow.setElevation(16);
        }

        if (mDropDownBg != null) {
            mPopupWindow.setBackgroundDrawable(mDropDownBg);
        } else {
            mPopupWindow.setBackgroundDrawable(BaseApplication.getContext().getResources().getDrawable(R.drawable.bms_drop_down_bg));
        }
        // default color is white
        if (mBackgroundColor != Color.WHITE) {
            setBackgroundColor(mBackgroundColor);
        } else if (mBackgroundSelector != 0) {
            setBackgroundResource(mBackgroundSelector);
        }
        if (mTextColor != defaultColor) {
            setTextColor(mTextColor);
        }

        mPopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                if (mNothingSelected && mOnNothingSelectedListener != null) {
                    mOnNothingSelectedListener.onNothingSelected(BlockMaterialSpinner.this);
                }
                if (!mHideArrow) {
                    animateArrow(false);
                }
            }
        });
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mPopupWindow.setHeight(calculatePopupWindowHeight());
        if (mAdapter != null) {
            CharSequence currentText = getText();
            String longestItem = currentText.toString();
            for (int i = 0; i < mAdapter.getCount(); i++) {
                String itemText = mAdapter.getItemText(i);
                if (itemText.length() > longestItem.length()) {
                    longestItem = itemText;
                }
            }
            setText(longestItem);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
            setText(currentText);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
        mPopupWindow.setWidth(getMeasuredWidth());
    }

    @Override
    public boolean onTouchEvent(@NonNull MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_UP) {
            if (isEnabled() && isClickable()) {
                if (!mPopupWindow.isShowing()) {
                    expand();
                } else {
                    collapse();
                }
            }
        }
        return super.onTouchEvent(event);
    }

    @Override
    public void setBackgroundColor(int color) {
        mBackgroundColor = color;
        Drawable background = getBackground();
        if (background instanceof StateListDrawable) { // pre-L
            try {
                Method getStateDrawable = StateListDrawable.class.getDeclaredMethod("getStateDrawable", int.class);
                if (!getStateDrawable.isAccessible()) {
                    getStateDrawable.setAccessible(true);
                }
                int[] colors = {darker(color, 0.85f), color};
                for (int i = 0; i < colors.length; i++) {
                    ColorDrawable drawable = (ColorDrawable) getStateDrawable.invoke(background, i);
                    drawable.setColor(colors[i]);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (background != null) { // 21+ (RippleDrawable)
            background.setColorFilter(color, PorterDuff.Mode.SRC_IN);
        }
        mPopupWindow.getBackground().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

    @Override
    public void setTextColor(int color) {
        mTextColor = color;
        super.setTextColor(color);
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("state", super.onSaveInstanceState());
        bundle.putInt("selected_index", mSelectedIndex);
        if (mPopupWindow != null) {
            bundle.putBoolean("is_popup_showing", mPopupWindow.isShowing());
            collapse();
        } else {
            bundle.putBoolean("is_popup_showing", false);
        }
        return bundle;
    }

    @Override
    public void onRestoreInstanceState(Parcelable savedState) {
        if (savedState instanceof Bundle) {
            Bundle bundle = (Bundle) savedState;
            mSelectedIndex = bundle.getInt("selected_index");
            if (mAdapter != null) {
                setText(mAdapter.get(mSelectedIndex).toString());
                mAdapter.notifyItemSelected(mSelectedIndex);
            }
            if (bundle.getBoolean("is_popup_showing")) {
                if (mPopupWindow != null) {
                    // Post the show request into the looper to avoid bad token exception
                    post(new Runnable() {

                        @Override
                        public void run() {
                            expand();
                        }
                    });
                }
            }
            savedState = bundle.getParcelable("state");
        }
        super.onRestoreInstanceState(savedState);
    }

    @Override
    public void setEnabled(boolean enabled) {
        super.setEnabled(enabled);
        if (mArrowDrawable != null) {
            mArrowDrawable.setColorFilter(enabled ? mArrowColor : mArrowColorDisabled, PorterDuff.Mode.SRC_IN);
        }
    }

    /**
     * @return the selected item position
     */
    public int getSelectedIndex() {
        return mSelectedIndex;
    }

    /**
     * Set the default spinner item using its index
     *
     * @param position the item's position
     */
    public BlockMaterialSpinner setSelectedIndex(int position) {
        if (mAdapter != null) {
            if (position >= 0 && position <= mAdapter.getCount()) {
                mAdapter.notifyItemSelected(position);
                mSelectedIndex = position;
                setText(mAdapter.get(position).toString());
            } else {
                throw new IllegalArgumentException("Position must be lower than adapter count!");
            }
        }
        return this;
    }

    /**
     * 设置选中的内容
     *
     * @param item 选中的内容
     * @param <T>
     */
    public <T> BlockMaterialSpinner setSelectedItem(@NonNull T item) {
        if (mAdapter != null && item != null) {
            setSelectedIndex(getSpinnerPosition(item, mAdapter.getItems()));
        }
        return this;
    }

    /**
     * 获取选中内容在Spinner中的位置
     *
     * @param item  选中的内容
     * @param items Spinner中选项的集合
     * @return
     */
    public <T> int getSpinnerPosition(T item, List<T> items) {
        if (item != null && items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++) {
                if (item.equals(items.get(i))) {
                    return i;
                }
            }
        }
        return 0;
    }

    /**
     * Register a callback to be invoked when an item in the dropdown is selected.
     *
     * @param onItemSelectedListener The callback that will run
     */
    public BlockMaterialSpinner setOnItemSelectedListener(@Nullable OnItemSelectedListener onItemSelectedListener) {
        mOnItemSelectedListener = onItemSelectedListener;
        return this;
    }

    /**
     * Register a callback to be invoked when the {@link PopupWindow} is shown but the user didn't select an item.
     *
     * @param onNothingSelectedListener the callback that will run
     */
    public BlockMaterialSpinner setOnNothingSelectedListener(@Nullable OnNothingSelectedListener onNothingSelectedListener) {
        mOnNothingSelectedListener = onNothingSelectedListener;
        return this;
    }

    /**
     * 设置无更多选择的监听
     *
     * @param onNoMoreChoiceListener
     * @return
     */
    public BlockMaterialSpinner setOnNoMoreChoiceListener(OnNoMoreChoiceListener onNoMoreChoiceListener) {
        mOnNoMoreChoiceListener = onNoMoreChoiceListener;
        return this;
    }

    /**
     * Set the dropdown items
     *
     * @param items A list of items
     * @param <T>   The item type
     */
    public <T> BlockMaterialSpinner setItems(@NonNull T... items) {
        setItems(Arrays.asList(items));
        return this;
    }

    /**
     * Set the dropdown items
     *
     * @param items A list of items
     * @param <T>   The item type
     */
    public <T> BlockMaterialSpinner setItems(@NonNull List<T> items) {
        mAdapter = new MaterialSpinnerAdapter<>(getContext(), items)
                .setTextColor(mTextColor).setTextSize(getTextSize());
        setAdapterInternal(mAdapter);
        return this;
    }

    /**
     * Get the list of items in the adapter
     *
     * @param <T> The item type
     * @return A list of items or {@code null} if no items are set.
     */
    public <T> List<T> getItems() {
        if (mAdapter == null) {
            return null;
        }
        //noinspection unchecked
        return mAdapter.getItems();
    }

    /**
     * Get the Selected Item in the adapter
     *
     * @param <T> The item type
     * @return A list of items or {@code null} if no items are set.
     */
    public <T> T getSelectedItem() {
        return mAdapter != null ? (T) mAdapter.get(mSelectedIndex) : null;
    }

    /**
     * Set a custom adapter for the dropdown items
     *
     * @param adapter The list adapter
     */
    public BlockMaterialSpinner setAdapter(@NonNull ListAdapter adapter) {
        mAdapter = new MaterialSpinnerAdapterWrapper(getContext(), adapter)
                .setTextColor(mTextColor).setTextSize(getTextSize());
        setAdapterInternal(mAdapter);
        return this;
    }

    /**
     * Set the custom adapter for the dropdown items
     *
     * @param adapter The adapter
     * @param <T>     The type
     */
    public <T> BlockMaterialSpinner setAdapter(MaterialSpinnerAdapter<T> adapter) {
        mAdapter = adapter;
        mAdapter.setTextColor(mTextColor);
        mAdapter.setTextSize(getTextSize());
        setAdapterInternal(adapter);
        return this;
    }

    /**
     * 获取适配器
     *
     * @return
     */
    public MaterialSpinnerBaseAdapter getAdapter() {
        return mAdapter;
    }

    public BlockMaterialSpinner setDropDownBackgroundSelector(@DrawableRes int backgroundSelector) {
        if (mAdapter != null) {
            mAdapter.setBackgroundSelector(backgroundSelector);
        }
        return this;
    }

    private void setAdapterInternal(@NonNull MaterialSpinnerBaseAdapter adapter) {
        mListView.setAdapter(adapter);
        if (mSelectedIndex >= adapter.getCount()) {
            mSelectedIndex = 0;
        }
        if (adapter.getCount() >= 0) {
            setText(adapter.get(mSelectedIndex).toString());
        } else {
            setText("");
        }
    }

    /**
     * @return 是否有内容可以下拉选择
     */
    private boolean hasMoreChoice() {
        return mAdapter != null && mAdapter.getCount() > 0;
    }

    /**
     * Show the dropdown menu
     */
    public void expand() {
        if (!hasMoreChoice()) {
            if (mOnNoMoreChoiceListener != null) {
                mOnNoMoreChoiceListener.OnNoMoreChoice(this);
            }
            return;
        }

        if (!mHideArrow) {
            animateArrow(true);
        }
        mNothingSelected = true;
        showPopupWindow();
    }

    /**
     * Closes the dropdown menu
     */
    public void collapse() {
        if (!mHideArrow) {
            animateArrow(false);
        }
        mPopupWindow.dismiss();
    }

    private void showPopupWindow() {
        if (mIsInDialog) {
            mPopupWindow.showAsDropDown(this);
        } else {
            mPopupWindow.showAsDropDown(this, 0, calculatePopWindowYOffset(this));
        }
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView 呼出window的view
     * @return window显示的左上角的xOff, yOff坐标
     */
    private int[] calculatePopWindowPos(final View anchorView) {
        final int windowPos[] = new int[2];
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight();
        // 计算ListView的高宽
        int listViewHeight = getListViewHeightBasedOnChildren(mListView);
        if (mPopupWindowMaxHeight > 0 && listViewHeight > mPopupWindowMaxHeight) {
            listViewHeight = mPopupWindowMaxHeight;
        }
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] - anchorHeight < listViewHeight);
        if (isNeedShowUp) {
            windowPos[0] = anchorLoc[0];
            windowPos[1] = anchorLoc[1] - listViewHeight - mDropDownOffset;
        } else {
            windowPos[0] = anchorLoc[0];
            windowPos[1] = anchorLoc[1] + anchorHeight;
        }
        return windowPos;
    }

    /**
     * 计算出来的位置，y方向就在anchorView的上面和下面对齐显示，x方向就是与屏幕右边对齐显示
     * 如果anchorView的位置有变化，就可以适当自己额外加入偏移来修正
     *
     * @param anchorView 呼出window的view
     * @return window显示的左上角的xOff, yOff坐标
     */
    private int calculatePopWindowYOffset(final View anchorView) {
        int windowYOffset;
        final int anchorLoc[] = new int[2];
        // 获取锚点View在屏幕上的左上角坐标位置
        anchorView.getLocationOnScreen(anchorLoc);
        final int anchorHeight = anchorView.getHeight();
        // 获取屏幕的高宽
        final int screenHeight = ScreenUtils.getScreenHeight();
        // 计算ListView的高宽
        int listViewHeight = getListViewHeightBasedOnChildren(mListView);
        if (mPopupWindowMaxHeight > 0 && listViewHeight > mPopupWindowMaxHeight) {
            listViewHeight = mPopupWindowMaxHeight;
        }
        // 判断需要向上弹出还是向下弹出显示
        final boolean isNeedShowUp = (screenHeight - anchorLoc[1] < listViewHeight + anchorHeight);
        if (isNeedShowUp) {
            windowYOffset = -(listViewHeight + mDropDownOffset + anchorHeight);
        } else {
            windowYOffset = 0;
        }
        return windowYOffset;
    }

    /**
     * Set the tint color for the dropdown arrow
     *
     * @param color the color value
     */
    public void setArrowColor(@ColorInt int color) {
        mArrowColor = color;
        mArrowColorDisabled = lighter(mArrowColor, 0.8f);
        if (mArrowDrawable != null) {
            mArrowDrawable.setColorFilter(mArrowColor, PorterDuff.Mode.SRC_IN);
        }
    }

    private void animateArrow(boolean shouldRotateUp) {
        int start = shouldRotateUp ? 0 : 10000;
        int end = shouldRotateUp ? 10000 : 0;
        ObjectAnimator animator = ObjectAnimator.ofInt(mArrowDrawable, "level", start, end);
        animator.start();
    }

    /**
     * Set the maximum height of the dropdown menu.
     *
     * @param height the height in pixels
     */
    public void setDropdownMaxHeight(int height) {
        mPopupWindowMaxHeight = height;
        mPopupWindow.setHeight(calculatePopupWindowHeight());
    }

    /**
     * Set the height of the dropdown menu
     *
     * @param height the height in pixels
     */
    public void setDropdownHeight(int height) {
        mPopupWindowHeight = height;
        mPopupWindow.setHeight(calculatePopupWindowHeight());
    }

    private int calculatePopupWindowHeight() {
        if (mAdapter == null) {
            return WindowManager.LayoutParams.WRAP_CONTENT;
        }
        float listViewHeight = mAdapter.getCount() * resolveDimension(getContext(), R.attr.ms_item_height_size);
        if (mPopupWindowMaxHeight > 0 && listViewHeight > mPopupWindowMaxHeight) {
            return mPopupWindowMaxHeight;
        } else if (mPopupWindowHeight != WindowManager.LayoutParams.MATCH_PARENT
                && mPopupWindowHeight != WindowManager.LayoutParams.WRAP_CONTENT
                && mPopupWindowHeight <= listViewHeight) {
            return mPopupWindowHeight;
        }
        return WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * Get the {@link PopupWindow}.
     *
     * @return The {@link PopupWindow} that is displayed when the view has been clicked.
     */
    public PopupWindow getPopupWindow() {
        return mPopupWindow;
    }

    /**
     * Get the {@link ListView} that is used in the dropdown menu
     *
     * @return the ListView shown in the PopupWindow.
     */
    public ListView getListView() {
        return mListView;
    }

    /**
     * Interface definition for a callback to be invoked when an item in this view has been selected.
     *
     * @param <T> Adapter item type
     */
    public interface OnItemSelectedListener<T> {

        /**
         * <p>Callback method to be invoked when an item in this view has been selected. This callback is invoked only when
         * the newly selected position is different from the previously selected position or if there was no selected
         * item.</p>
         *
         * @param view     The {@link BlockMaterialSpinner} view
         * @param position The position of the view in the adapter
         * @param id       The row id of the item that is selected
         * @param item     The selected item
         */
        void onItemSelected(BlockMaterialSpinner view, int position, long id, T item);
    }

    /**
     * Interface definition for a callback to be invoked when the dropdown is dismissed and no item was selected.
     */
    public interface OnNothingSelectedListener {

        /**
         * Callback method to be invoked when the {@link PopupWindow} is dismissed and no item was selected.
         *
         * @param spinner the {@link BlockMaterialSpinner}
         */
        void onNothingSelected(BlockMaterialSpinner spinner);
    }

    /**
     * 无更多选择的监听
     */
    public interface OnNoMoreChoiceListener {
        /**
         * 无更多选择
         *
         * @param spinner
         */
        void OnNoMoreChoice(BlockMaterialSpinner spinner);
    }

    /**
     * Check if layout direction is RTL
     *
     * @return {@code true} if the layout direction is right-to-left
     */
    public static boolean isRtl() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1 &&
                BaseApplication.getContext().getResources().getConfiguration().getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
    }

    /**
     * 获取Drawable属性（兼容VectorDrawable）
     *
     * @param context
     * @param typedArray
     * @param index
     * @return
     */
    public static Drawable getDrawableAttrRes(Context context, TypedArray typedArray, @StyleableRes int index) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return typedArray.getDrawable(index);
        } else {
            int resourceId = typedArray.getResourceId(index, -1);
            if (resourceId != -1) {
                return AppCompatResources.getDrawable(context, resourceId);
            }
        }
        return null;
    }

    public static int resolveDimension(Context context, @AttrRes int attr) {
        return resolveDimension(context, attr, -1);
    }

    public static int resolveDimension(Context context, @AttrRes int attr, int fallback) {
        TypedArray a = context.getTheme().obtainStyledAttributes(new int[]{attr});
        try {
            return a.getDimensionPixelSize(0, fallback);
        } finally {
            a.recycle();
        }
    }

    /**
     * 获取svg资源图片
     *
     * @param context
     * @param resId
     * @return
     */
    public static Drawable getVectorDrawable(Context context, @DrawableRes int resId) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            return context.getDrawable(resId);
        }
        return AppCompatResources.getDrawable(context, resId);
    }

    /**
     * 加深颜色
     *
     * @param color 需要加深的颜色
     */
    public static int darker(int color) {
        return darker(color, 0.8F);
    }

    /**
     * 加深颜色
     *
     * @param color  需要加深的颜色
     * @param factor The factor to darken the color.
     * @return darker version of specified color.
     */
    public static int darker(int color, float factor) {
        return Color.argb(Color.alpha(color), Math.max((int) (Color.red(color) * factor), 0),
                Math.max((int) (Color.green(color) * factor), 0),
                Math.max((int) (Color.blue(color) * factor), 0));
    }

    /**
     * 变浅颜色
     *
     * @param color 需要变浅的颜色
     */
    public static int lighter(int color) {
        return lighter(color, 0.8F);
    }

    /**
     * 变浅颜色
     *
     * @param color  需要变浅的颜色
     * @param factor The factor to lighten the color. 0 will make the color unchanged. 1 will make the
     *               color white.
     * @return lighter version of the specified color.
     */
    public static int lighter(int color, float factor) {
        int red = (int) ((Color.red(color) * (1 - factor) / 255 + factor) * 255);
        int green = (int) ((Color.green(color) * (1 - factor) / 255 + factor) * 255);
        int blue = (int) ((Color.blue(color) * (1 - factor) / 255 + factor) * 255);
        return Color.argb(Color.alpha(color), red, green, blue);
    }

    /**
     * 获取字符串的数组
     *
     * @param resId
     * @return
     */
    public static String[] getStringArray(@ArrayRes int resId) {
        return BaseApplication.getContext().getResources().getStringArray(resId);
    }

    /**
     * get ListView height according to every children
     *
     * @param view
     * @return
     */
    public static int getListViewHeightBasedOnChildren(ListView view) {
        int height = getAbsListViewHeightBasedOnChildren(view);
        ListAdapter adapter;
        int adapterCount;
        if (view != null && (adapter = view.getAdapter()) != null
                && (adapterCount = adapter.getCount()) > 0) {
            height += view.getDividerHeight() * (adapterCount - 1);
        }
        return height;
    }

    /**
     * get AbsListView height according to every children
     *
     * @param view
     * @return
     */
    public static int getAbsListViewHeightBasedOnChildren(AbsListView view) {
        ListAdapter adapter;
        if (view == null || (adapter = view.getAdapter()) == null) {
            return 0;
        }

        int height = 0;
        for (int i = 0; i < adapter.getCount(); i++) {
            View item = adapter.getView(i, null, view);
            if (item instanceof ViewGroup) {
                item.setLayoutParams(new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
            }
            item.measure(0, 0);
            height += item.getMeasuredHeight();
        }
        height += view.getPaddingTop() + view.getPaddingBottom();
        return height;
    }
}
