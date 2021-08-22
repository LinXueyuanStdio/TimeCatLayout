package com.timecat.layout.ui.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.graphics.PixelFormat;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Display;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.timecat.extend.arms.BaseApplication;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicInteger;

import androidx.core.graphics.drawable.DrawableCompat;

public class ViewUtil {

    private static final AtomicInteger sNextGeneratedId = new AtomicInteger(1);

    @SuppressLint("NewApi")
    public static int generateViewId() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.JELLY_BEAN_MR1) {
            for (; ; ) {
                final int result = sNextGeneratedId.get();
                // aapt-generated IDs have the high byte nonzero; clamp to the range under that.
                int newValue = result + 1;
                if (newValue > 0x00FFFFFF) {
                    newValue = 1; // Roll over to 1, not 0.
                }
                if (sNextGeneratedId.compareAndSet(result, newValue)) { return result; }
            }
        } else { return View.generateViewId(); }
    }

    public static boolean hasState(int[] states, int state) {
        if (states == null) { return false; }

        for (int state1 : states) { if (state1 == state) { return true; } }

        return false;
    }

    public static void setBackground(View v, Drawable drawable) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) { v.setBackground(drawable); } else { v.setBackgroundDrawable(drawable); }
    }

    public static int dp2px(float dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, BaseApplication.getContext().getResources().getDisplayMetrics());
    }

    public static int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public static float px2dp(float px) {
        return px / BaseApplication.getContext().getResources().getDisplayMetrics().density;
    }

    public static float sp2px(float sp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, BaseApplication.getContext().getResources().getDisplayMetrics());
    }

    public static float px2sp(float px) {
        return px / BaseApplication.getContext().getResources().getDisplayMetrics().scaledDensity;
    }

    /**
     * Convert Dp to Pixel
     */
    public static int dpToPx(float dp, Resources resources) {
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, resources.getDisplayMetrics());
        return (int) px;
    }

    public static int getRelativeTop(View myView) {
        //	    if (myView.getParent() == myView.getRootView())
        if (myView.getId() == android.R.id.content) { return myView.getTop(); } else {
            return myView.getTop() + getRelativeTop((View) myView.getParent());
        }
    }

    public static int getRelativeLeft(View myView) {
        //	    if (myView.getParent() == myView.getRootView())
        if (myView.getId() == android.R.id.content) { return myView.getLeft(); } else {
            return myView.getLeft() + getRelativeLeft((View) myView.getParent());
        }
    }

    public static void hideInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) BaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
    }

    public static void requestInputMethodIfShow(final EditText view) {
        final InputMethodManager imm = (InputMethodManager) BaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm.isActive()) {
            view.requestFocus();
            view.setSelection(view.getText().length(), view.getText().length());
        }
    }

    public static void showInputMethod(View view) {
        InputMethodManager imm = (InputMethodManager) BaseApplication.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
    }

    public static boolean isNavigationBarShow(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            Point realSize = new Point();
            display.getSize(size);
            display.getRealSize(realSize);
            return realSize.y != size.y;
        } else {
            boolean menu = ViewConfiguration.get(activity).hasPermanentMenuKey();
            boolean back = KeyCharacterMap.deviceHasKey(KeyEvent.KEYCODE_BACK);
            return !(menu || back);
        }
    }

    public static int getNavigationBarHeight(Activity activity) {
        if (!isNavigationBarShow(activity)) {
            return 0;
        }
        Resources resources = activity.getResources();
        int resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android");
        //获取NavigationBar的高度
        int height = resources.getDimensionPixelSize(resourceId);
        return height;
    }

    public static int getScreenWidth(Activity activity) {
        DisplayMetrics localDisplayMetrics = new DisplayMetrics();
        ((WindowManager) activity.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getMetrics(localDisplayMetrics);
        return localDisplayMetrics.widthPixels;
    }


    public static int getSceenHeight(Activity activity) {
        return activity.getWindowManager().getDefaultDisplay().getHeight() + getNavigationBarHeight(activity);
    }

    /**
     * 下划线
     *
     * @param textView input
     */
    public static void addButtomLine(TextView textView) {
        textView.getPaint().setFlags(Paint.UNDERLINE_TEXT_FLAG);
    }

    /**
     * 取消设置的的划线
     *
     * @param textView input
     */
    public static void removeLine(TextView textView) {
        textView.getPaint().setFlags(Paint.ANTI_ALIAS_FLAG); // 取消设置的的划线
    }

    /**
     * 设置中划线并加清晰
     *
     * @param textView input
     */
    public static void addClearCenterLine(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG | Paint.ANTI_ALIAS_FLAG); // 设置中划线并加清晰
    }

    /**
     * 中划线
     *
     * @param textView input
     */
    public static void addCenterLine(TextView textView) {
        textView.getPaint().setFlags(Paint.STRIKE_THRU_TEXT_FLAG); // 中划线
    }

    /**
     * 抗锯齿
     *
     * @param textView input
     */
    public static void addjuchiLine(TextView textView) {
        textView.getPaint().setAntiAlias(true);// 抗锯齿
    }

    private static final String TAG = "ViewUtil";
    private static final Rect sOldBounds = new Rect();
    private static final Canvas sCanvas = new Canvas();
    private static final int NUMBER_OF_PALETTE_COLORS = 24;

    static {
        sCanvas.setDrawFilter(new PaintFlagsDrawFilter(Paint.DITHER_FLAG,
                Paint.FILTER_BITMAP_FLAG));
    }

    public static Point getScreenSize(Context context) {
        WindowManager manager = (WindowManager)
                context.getApplicationContext().getSystemService(Context.WINDOW_SERVICE);
        Point point = new Point();
        manager.getDefaultDisplay().getRealSize(point);
        return point;
    }

    public static int getScreenWidth(Context context) {
        return getScreenSize(context).x;
    }

    public static int getScreenHeight(Context context) {
        return getScreenSize(context).y;
    }

    // #ifdef LAVA_EDIT
    // wangxijun. 2016/10/11, NavigationBar
    public static int getNavigationBarHeight(Context context) {
        int height = 0;
        boolean hasNavigationBar = false;
        Resources rs = context.getResources();
        int id = rs.getIdentifier("config_showNavigationBar", "bool", "android");
        if (id > 0) {
            hasNavigationBar = rs.getBoolean(id);
        }
        try {
            Class systemPropertiesClass = Class.forName("android.os.SystemProperties");
            Method m = systemPropertiesClass.getMethod("get", String.class);
            String navBarOverride = (String) m.invoke(systemPropertiesClass, "qemu.hw.mainkeys");
            if ("1".equals(navBarOverride)) {
                hasNavigationBar = false;
            } else if ("0".equals(navBarOverride)) {
                hasNavigationBar = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (hasNavigationBar) {
            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
            height = context.getResources().getDimensionPixelSize(resourceId);
        }
        return height;
    }


    public static Drawable tintDrawable(Drawable drawable, int colors) {
        final Drawable wrappedDrawable = DrawableCompat.wrap(drawable).mutate();
        DrawableCompat.setTint(wrappedDrawable, colors);
        return wrappedDrawable;
    }

    /**
     * Returns a bitmap suitable for the all apps view.
     */
    public static Bitmap createIconBitmap(Drawable icon, int size) {
        if (icon == null) {
            return null;
        }
        synchronized (sCanvas) {
            // 取 drawable 的长宽
            int w = icon.getIntrinsicWidth();
            int h = icon.getIntrinsicHeight();
            // 取 drawable 的颜色格式
            Bitmap.Config config = icon.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                                                           : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            icon.setBounds(0, 0, w, h);
            // 把 drawable 内容画到画布中
            icon.draw(canvas);
            Bitmap scaleBmp = Bitmap.createScaledBitmap(bitmap, size, size, true);
            if (bitmap != null) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
            return scaleBmp;
        }
    }

    public static Bitmap drawable2Bitmap(Drawable drawable) {
        synchronized (sCanvas) {
            // 取 drawable 的长宽
            int w = drawable.getIntrinsicWidth();
            int h = drawable.getIntrinsicHeight();
            // 取 drawable 的颜色格式
            Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                                                                               : Bitmap.Config.RGB_565;
            // 建立对应 bitmap
            Bitmap bitmap = Bitmap.createBitmap(w, h, config);
            // 建立对应 bitmap 的画布
            Canvas canvas = new Canvas(bitmap);
            drawable.setBounds(0, 0, w, h);
            // 把 drawable 内容画到画布中
            drawable.draw(canvas);
            Bitmap scaleBmp = Bitmap.createScaledBitmap(bitmap, w, h, true);
            if (bitmap != null) {
                if (!bitmap.isRecycled()) {
                    bitmap.recycle();
                }
            }
            return scaleBmp;
        }
    }

}
