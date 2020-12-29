package com.timecat.layout.ui.standard.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Bundle;
import android.os.Parcelable;
import androidx.core.internal.view.SupportMenu;
import androidx.core.view.InputDeviceCompat;
import androidx.core.view.ViewCompat;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.timecat.layout.ui.R;

public class HoloColorPicker extends View {
    private static final int[] COLORS = new int[]{SupportMenu.CATEGORY_MASK, -65281, -16776961, -16711681, -16711936, InputDeviceCompat.SOURCE_ANY, SupportMenu.CATEGORY_MASK};
    private static final String STATE_ANGLE = "angle";
    private static final String STATE_OLD_COLOR = "color";
    private static final String STATE_PARENT = "parent";
    private static final String STATE_SHOW_OLD_COLOR = "showColor";
    private float mAngle;
    private Paint mCenterHaloPaint;
    private int mCenterNewColor;
    private Paint mCenterNewPaint;
    private int mCenterOldColor;
    private Paint mCenterOldPaint;
    private RectF mCenterRectangle;
    private int mColor;
    private int mColorCenterHaloRadius;
    private int mColorCenterRadius;
    private int mColorPointerHaloRadius;
    private int mColorPointerRadius;
    private Paint mColorWheelPaint;
    private int mColorWheelRadius;
    private RectF mColorWheelRectangle;
    private int mColorWheelThickness;
    private float[] mHSV;
    private OpacityBar mOpacityBar;
    private Paint mPointerColor;
    private Paint mPointerHaloPaint;
    private int mPreferredColorCenterHaloRadius;
    private int mPreferredColorCenterRadius;
    private int mPreferredColorWheelRadius;
    private SVBar mSVbar;
    private SaturationBar mSaturationBar;
    private boolean mShowCenterOldColor;
    private float mSlopX;
    private float mSlopY;
    private boolean mTouchAnywhereOnColorWheelEnabled;
    private float mTranslationOffset;
    private boolean mUserIsMovingPointer;
    private ValueBar mValueBar;
    private int oldChangedListenerColor;
    private int oldSelectedListenerColor;
    private OnColorChangedListener onColorChangedListener;
    private OnColorSelectedListener onColorSelectedListener;

    public interface OnColorChangedListener {
        void onColorChanged(int i);
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int i);
    }

    public HoloColorPicker(Context context) {
        super(context);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        this.mSVbar = null;
        this.mOpacityBar = null;
        this.mSaturationBar = null;
        this.mTouchAnywhereOnColorWheelEnabled = true;
        this.mValueBar = null;
        init(null, 0);
    }

    public HoloColorPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        this.mSVbar = null;
        this.mOpacityBar = null;
        this.mSaturationBar = null;
        this.mTouchAnywhereOnColorWheelEnabled = true;
        this.mValueBar = null;
        init(attrs, 0);
    }

    public HoloColorPicker(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mColorWheelRectangle = new RectF();
        this.mCenterRectangle = new RectF();
        this.mUserIsMovingPointer = false;
        this.mHSV = new float[3];
        this.mSVbar = null;
        this.mOpacityBar = null;
        this.mSaturationBar = null;
        this.mTouchAnywhereOnColorWheelEnabled = true;
        this.mValueBar = null;
        init(attrs, defStyle);
    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.onColorChangedListener = listener;
    }

    public OnColorChangedListener getOnColorChangedListener() {
        return this.onColorChangedListener;
    }

    public void setOnColorSelectedListener(OnColorSelectedListener listener) {
        this.onColorSelectedListener = listener;
    }

    public OnColorSelectedListener getOnColorSelectedListener() {
        return this.onColorSelectedListener;
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.HoloColorPicker, defStyle, 0);
        Resources b = getContext().getResources();
        this.mColorWheelThickness = a.getDimensionPixelSize(R.styleable.HoloColorPicker_color_wheel_thickness, b.getDimensionPixelSize(R.dimen.color_wheel_thickness));
        this.mColorWheelRadius = a.getDimensionPixelSize(R.styleable.HoloColorPicker_color_wheel_radius, b.getDimensionPixelSize(R.dimen.color_wheel_radius));
        this.mPreferredColorWheelRadius = this.mColorWheelRadius;
        this.mColorCenterRadius = a.getDimensionPixelSize(R.styleable.HoloColorPicker_color_center_radius, b.getDimensionPixelSize(R.dimen.color_center_radius));
        this.mPreferredColorCenterRadius = this.mColorCenterRadius;
        this.mColorCenterHaloRadius = a.getDimensionPixelSize(R.styleable.HoloColorPicker_color_center_halo_radius, b.getDimensionPixelSize(R.dimen.color_center_halo_radius));
        this.mPreferredColorCenterHaloRadius = this.mColorCenterHaloRadius;
        this.mColorPointerRadius = a.getDimensionPixelSize(R.styleable.HoloColorPicker_color_pointer_radius, b.getDimensionPixelSize(R.dimen.color_pointer_radius));
        this.mColorPointerHaloRadius = a.getDimensionPixelSize(R.styleable.HoloColorPicker_color_pointer_halo_radius, b.getDimensionPixelSize(R.dimen.color_pointer_halo_radius));
        a.recycle();
        this.mAngle = -1.5707964f;
        Shader s = new SweepGradient(0.0f, 0.0f, COLORS, null);
        this.mColorWheelPaint = new Paint(1);
        this.mColorWheelPaint.setShader(s);
        this.mColorWheelPaint.setStyle(Style.STROKE);
        this.mColorWheelPaint.setStrokeWidth((float) this.mColorWheelThickness);
        this.mPointerHaloPaint = new Paint(1);
        this.mPointerHaloPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mPointerHaloPaint.setAlpha(80);
        this.mPointerColor = new Paint(1);
        this.mPointerColor.setColor(calculateColor(this.mAngle));
        this.mCenterNewPaint = new Paint(1);
        this.mCenterNewPaint.setColor(calculateColor(this.mAngle));
        this.mCenterNewPaint.setStyle(Style.FILL);
        this.mCenterOldPaint = new Paint(1);
        this.mCenterOldPaint.setColor(calculateColor(this.mAngle));
        this.mCenterOldPaint.setStyle(Style.FILL);
        this.mCenterHaloPaint = new Paint(1);
        this.mCenterHaloPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mCenterHaloPaint.setAlpha(0);
        this.mCenterNewColor = calculateColor(this.mAngle);
        this.mCenterOldColor = calculateColor(this.mAngle);
        this.mShowCenterOldColor = true;
    }

    protected void onDraw(Canvas canvas) {
        canvas.translate(this.mTranslationOffset, this.mTranslationOffset);
        canvas.drawOval(this.mColorWheelRectangle, this.mColorWheelPaint);
        float[] pointerPosition = calculatePointerPosition(this.mAngle);
        canvas.drawCircle(pointerPosition[0], pointerPosition[1], (float) this.mColorPointerHaloRadius, this.mPointerHaloPaint);
        canvas.drawCircle(pointerPosition[0], pointerPosition[1], (float) this.mColorPointerRadius, this.mPointerColor);
        canvas.drawCircle(0.0f, 0.0f, (float) this.mColorCenterHaloRadius, this.mCenterHaloPaint);
        if (this.mShowCenterOldColor) {
            canvas.drawArc(this.mCenterRectangle, 90.0f, 180.0f, true, this.mCenterOldPaint);
            canvas.drawArc(this.mCenterRectangle, 270.0f, 180.0f, true, this.mCenterNewPaint);
            return;
        }
        canvas.drawArc(this.mCenterRectangle, 0.0f, 360.0f, true, this.mCenterNewPaint);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width;
        int height;
        int intrinsicSize = (this.mPreferredColorWheelRadius + this.mColorPointerHaloRadius) * 2;
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        if (widthMode == 1073741824) {
            width = widthSize;
        } else if (widthMode == Integer.MIN_VALUE) {
            width = Math.min(intrinsicSize, widthSize);
        } else {
            width = intrinsicSize;
        }
        if (heightMode == 1073741824) {
            height = heightSize;
        } else if (heightMode == Integer.MIN_VALUE) {
            height = Math.min(intrinsicSize, heightSize);
        } else {
            height = intrinsicSize;
        }
        int min = Math.min(width, height);
        setMeasuredDimension(min, min);
        this.mTranslationOffset = ((float) min) * 0.5f;
        this.mColorWheelRadius = ((min / 2) - this.mColorWheelThickness) - this.mColorPointerHaloRadius;
        this.mColorWheelRectangle.set((float) (-this.mColorWheelRadius), (float) (-this.mColorWheelRadius), (float) this.mColorWheelRadius, (float) this.mColorWheelRadius);
        this.mColorCenterRadius = (int) (((float) this.mPreferredColorCenterRadius) * (((float) this.mColorWheelRadius) / ((float) this.mPreferredColorWheelRadius)));
        this.mColorCenterHaloRadius = (int) (((float) this.mPreferredColorCenterHaloRadius) * (((float) this.mColorWheelRadius) / ((float) this.mPreferredColorWheelRadius)));
        this.mCenterRectangle.set((float) (-this.mColorCenterRadius), (float) (-this.mColorCenterRadius), (float) this.mColorCenterRadius, (float) this.mColorCenterRadius);
    }

    private int ave(int s, int d, float p) {
        return Math.round(((float) (d - s)) * p) + s;
    }

    private int calculateColor(float angle) {
        float unit = (float) (((double) angle) / 6.283185307179586d);
        if (unit < 0.0f) {
            unit += 1.0f;
        }
        if (unit <= 0.0f) {
            this.mColor = COLORS[0];
            return COLORS[0];
        } else if (unit >= 1.0f) {
            this.mColor = COLORS[COLORS.length - 1];
            return COLORS[COLORS.length - 1];
        } else {
            float p = unit * ((float) (COLORS.length - 1));
            int i = (int) p;
            p -= (float) i;
            int c0 = COLORS[i];
            int c1 = COLORS[i + 1];
            int a = ave(Color.alpha(c0), Color.alpha(c1), p);
            int r = ave(Color.red(c0), Color.red(c1), p);
            int g = ave(Color.green(c0), Color.green(c1), p);
            int b = ave(Color.blue(c0), Color.blue(c1), p);
            this.mColor = Color.argb(a, r, g, b);
            return Color.argb(a, r, g, b);
        }
    }

    public int getColor() {
        return this.mCenterNewColor;
    }

    public void setColor(int color) {
        this.mAngle = colorToAngle(color);
        this.mPointerColor.setColor(calculateColor(this.mAngle));
        if (this.mOpacityBar != null) {
            this.mOpacityBar.setColor(this.mColor);
            this.mOpacityBar.setOpacity(Color.alpha(color));
        }
        if (this.mSVbar != null) {
            Color.colorToHSV(color, this.mHSV);
            this.mSVbar.setColor(this.mColor);
            if (this.mHSV[1] < this.mHSV[2]) {
                this.mSVbar.setSaturation(this.mHSV[1]);
            } else if (this.mHSV[1] > this.mHSV[2]) {
                this.mSVbar.setValue(this.mHSV[2]);
            }
        }
        if (this.mSaturationBar != null) {
            Color.colorToHSV(color, this.mHSV);
            this.mSaturationBar.setColor(this.mColor);
            this.mSaturationBar.setSaturation(this.mHSV[1]);
        }
        if (this.mValueBar != null && this.mSaturationBar == null) {
            Color.colorToHSV(color, this.mHSV);
            this.mValueBar.setColor(this.mColor);
            this.mValueBar.setValue(this.mHSV[2]);
        } else if (this.mValueBar != null) {
            Color.colorToHSV(color, this.mHSV);
            this.mValueBar.setValue(this.mHSV[2]);
        }
        setNewCenterColor(color);
    }

    private float colorToAngle(int color) {
        float[] colors = new float[3];
        Color.colorToHSV(color, colors);
        return (float) Math.toRadians((double) (-colors[0]));
    }

    public boolean onTouchEvent(MotionEvent event) {
        getParent().requestDisallowInterceptTouchEvent(true);
        float x = event.getX() - this.mTranslationOffset;
        float y = event.getY() - this.mTranslationOffset;
        switch (event.getAction()) {
            case 0:
                float[] pointerPosition = calculatePointerPosition(this.mAngle);
                if (x < pointerPosition[0] - ((float) this.mColorPointerHaloRadius) || x > pointerPosition[0] + ((float) this.mColorPointerHaloRadius) || y < pointerPosition[1] - ((float) this.mColorPointerHaloRadius) || y > pointerPosition[1] + ((float) this.mColorPointerHaloRadius)) {
                    if (x < ((float) (-this.mColorCenterRadius)) || x > ((float) this.mColorCenterRadius) || y < ((float) (-this.mColorCenterRadius)) || y > ((float) this.mColorCenterRadius) || !this.mShowCenterOldColor) {
                        if (Math.sqrt((double) ((x * x) + (y * y))) <= ((double) (this.mColorWheelRadius + this.mColorPointerHaloRadius)) && Math.sqrt((double) ((x * x) + (y * y))) >= ((double) (this.mColorWheelRadius - this.mColorPointerHaloRadius)) && this.mTouchAnywhereOnColorWheelEnabled) {
                            this.mUserIsMovingPointer = true;
                            invalidate();
                            break;
                        }
                        getParent().requestDisallowInterceptTouchEvent(false);
                        return false;
                    }
                    this.mCenterHaloPaint.setAlpha(80);
                    setColor(getOldCenterColor());
                    invalidate();
                    break;
                }
                this.mSlopX = x - pointerPosition[0];
                this.mSlopY = y - pointerPosition[1];
                this.mUserIsMovingPointer = true;
                invalidate();
                break;
            case 1:
                this.mUserIsMovingPointer = false;
                this.mCenterHaloPaint.setAlpha(0);
                if (!(this.onColorSelectedListener == null || this.mCenterNewColor == this.oldSelectedListenerColor)) {
                    this.onColorSelectedListener.onColorSelected(this.mCenterNewColor);
                    this.oldSelectedListenerColor = this.mCenterNewColor;
                }
                invalidate();
                break;
            case 2:
                if (this.mUserIsMovingPointer) {
                    this.mAngle = (float) Math.atan2((double) (y - this.mSlopY), (double) (x - this.mSlopX));
                    this.mPointerColor.setColor(calculateColor(this.mAngle));
                    int calculateColor = calculateColor(this.mAngle);
                    this.mCenterNewColor = calculateColor;
                    setNewCenterColor(calculateColor);
                    if (this.mOpacityBar != null) {
                        this.mOpacityBar.setColor(this.mColor);
                    }
                    if (this.mValueBar != null) {
                        this.mValueBar.setColor(this.mColor);
                    }
                    if (this.mSaturationBar != null) {
                        this.mSaturationBar.setColor(this.mColor);
                    }
                    if (this.mSVbar != null) {
                        this.mSVbar.setColor(this.mColor);
                    }
                    invalidate();
                    break;
                }
                getParent().requestDisallowInterceptTouchEvent(false);
                return false;
            case 3:
                if (!(this.onColorSelectedListener == null || this.mCenterNewColor == this.oldSelectedListenerColor)) {
                    this.onColorSelectedListener.onColorSelected(this.mCenterNewColor);
                    this.oldSelectedListenerColor = this.mCenterNewColor;
                    break;
                }
        }
        return true;
    }

    private float[] calculatePointerPosition(float angle) {
        float x = (float) (((double) this.mColorWheelRadius) * Math.cos((double) angle));
        float y = (float) (((double) this.mColorWheelRadius) * Math.sin((double) angle));
        return new float[]{x, y};
    }

    public void addSVBar(SVBar bar) {
        this.mSVbar = bar;
        this.mSVbar.setColorPicker(this);
        this.mSVbar.setColor(this.mColor);
    }

    public void addOpacityBar(OpacityBar bar) {
        this.mOpacityBar = bar;
        this.mOpacityBar.setColorPicker(this);
        this.mOpacityBar.setColor(this.mColor);
    }

    public void addSaturationBar(SaturationBar bar) {
        this.mSaturationBar = bar;
        this.mSaturationBar.setColorPicker(this);
        this.mSaturationBar.setColor(this.mColor);
    }

    public void addValueBar(ValueBar bar) {
        this.mValueBar = bar;
        this.mValueBar.setColorPicker(this);
        this.mValueBar.setColor(this.mColor);
    }

    public void setNewCenterColor(int color) {
        this.mCenterNewColor = color;
        this.mCenterNewPaint.setColor(color);
        if (this.mCenterOldColor == 0) {
            this.mCenterOldColor = color;
            this.mCenterOldPaint.setColor(color);
        }
        if (!(this.onColorChangedListener == null || color == this.oldChangedListenerColor)) {
            this.onColorChangedListener.onColorChanged(color);
            this.oldChangedListenerColor = color;
        }
        invalidate();
    }

    public void setOldCenterColor(int color) {
        this.mCenterOldColor = color;
        this.mCenterOldPaint.setColor(color);
        invalidate();
    }

    public int getOldCenterColor() {
        return this.mCenterOldColor;
    }

    public void setShowOldCenterColor(boolean show) {
        this.mShowCenterOldColor = show;
        invalidate();
    }

    public boolean getShowOldCenterColor() {
        return this.mShowCenterOldColor;
    }

    public void changeOpacityBarColor(int color) {
        if (this.mOpacityBar != null) {
            this.mOpacityBar.setColor(color);
        }
    }

    public void changeSaturationBarColor(int color) {
        if (this.mSaturationBar != null) {
            this.mSaturationBar.setColor(color);
        }
    }

    public void changeValueBarColor(int color) {
        if (this.mValueBar != null) {
            this.mValueBar.setColor(color);
        }
    }

    public boolean hasOpacityBar() {
        return this.mOpacityBar != null;
    }

    public boolean hasValueBar() {
        return this.mValueBar != null;
    }

    public boolean hasSaturationBar() {
        return this.mSaturationBar != null;
    }

    public boolean hasSVBar() {
        return this.mSVbar != null;
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle state = new Bundle();
        state.putParcelable(STATE_PARENT, superState);
        state.putFloat(STATE_ANGLE, this.mAngle);
        state.putInt(STATE_OLD_COLOR, this.mCenterOldColor);
        state.putBoolean(STATE_SHOW_OLD_COLOR, this.mShowCenterOldColor);
        return state;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        Bundle savedState = (Bundle) state;
        super.onRestoreInstanceState(savedState.getParcelable(STATE_PARENT));
        this.mAngle = savedState.getFloat(STATE_ANGLE);
        setOldCenterColor(savedState.getInt(STATE_OLD_COLOR));
        this.mShowCenterOldColor = savedState.getBoolean(STATE_SHOW_OLD_COLOR);
        int currentColor = calculateColor(this.mAngle);
        this.mPointerColor.setColor(currentColor);
        setNewCenterColor(currentColor);
    }

    public void setTouchAnywhereOnColorWheelEnabled(boolean TouchAnywhereOnColorWheelEnabled) {
        this.mTouchAnywhereOnColorWheelEnabled = TouchAnywhereOnColorWheelEnabled;
    }

    public boolean getTouchAnywhereOnColorWheel() {
        return this.mTouchAnywhereOnColorWheelEnabled;
    }
}
