package com.timecat.layout.ui.standard.colorpicker;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.timecat.layout.ui.R;

import androidx.core.view.ViewCompat;

public class ValueBar extends View {
    private static final boolean ORIENTATION_DEFAULT = true;
    private static final boolean ORIENTATION_HORIZONTAL = true;
    private static final boolean ORIENTATION_VERTICAL = false;
    private static final String STATE_COLOR = "color";
    private static final String STATE_ORIENTATION = "orientation";
    private static final String STATE_PARENT = "parent";
    private static final String STATE_VALUE = "value";
    private int mBarLength;
    private Paint mBarPaint;
    private Paint mBarPointerHaloPaint;
    private int mBarPointerHaloRadius;
    private Paint mBarPointerPaint;
    private int mBarPointerPosition;
    private int mBarPointerRadius;
    private RectF mBarRect;
    private int mBarThickness;
    private int mColor;
    private float[] mHSVColor;
    private boolean mIsMovingPointer;
    private boolean mOrientation;
    private HoloColorPicker mPicker;
    private float mPosToSatFactor;
    private int mPreferredBarLength;
    private float mSatToPosFactor;
    private int oldChangedListenerValue;
    private OnValueChangedListener onValueChangedListener;
    private Shader shader;

    public interface OnValueChangedListener {
        void onValueChanged(int i);
    }

    public void setOnValueChangedListener(OnValueChangedListener listener) {
        this.onValueChangedListener = listener;
    }

    public OnValueChangedListener getOnValueChangedListener() {
        return this.onValueChangedListener;
    }

    public ValueBar(Context context) {
        super(context);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(null, 0);
    }

    public ValueBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(attrs, 0);
    }

    public ValueBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.mBarRect = new RectF();
        this.mHSVColor = new float[3];
        this.mPicker = null;
        init(attrs, defStyle);
    }

    private void init(AttributeSet attrs, int defStyle) {
        TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ColorBars, defStyle, 0);
        Resources b = getContext().getResources();
        this.mBarThickness = a.getDimensionPixelSize(0, b.getDimensionPixelSize(R.dimen.bar_thickness));
        this.mBarLength = a.getDimensionPixelSize(1, b.getDimensionPixelSize(R.dimen.bar_length));
        this.mPreferredBarLength = this.mBarLength;
        this.mBarPointerRadius = a.getDimensionPixelSize(2, b.getDimensionPixelSize(R.dimen.bar_pointer_radius));
        this.mBarPointerHaloRadius = a.getDimensionPixelSize(3, b.getDimensionPixelSize(R.dimen.bar_pointer_halo_radius));
        this.mOrientation = a.getBoolean(4, true);
        a.recycle();
        this.mBarPaint = new Paint(1);
        this.mBarPaint.setShader(this.shader);
        this.mBarPointerPosition = this.mBarPointerHaloRadius;
        this.mBarPointerHaloPaint = new Paint(1);
        this.mBarPointerHaloPaint.setColor(ViewCompat.MEASURED_STATE_MASK);
        this.mBarPointerHaloPaint.setAlpha(80);
        this.mBarPointerPaint = new Paint(1);
        this.mBarPointerPaint.setColor(-8257792);
        this.mPosToSatFactor = 1.0f / ((float) this.mBarLength);
        this.mSatToPosFactor = ((float) this.mBarLength) / 1.0f;
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int measureSpec;
        int length;
        int intrinsicSize = this.mPreferredBarLength + (this.mBarPointerHaloRadius * 2);
        if (this.mOrientation) {
            measureSpec = widthMeasureSpec;
        } else {
            measureSpec = heightMeasureSpec;
        }
        int lengthMode = MeasureSpec.getMode(measureSpec);
        int lengthSize = MeasureSpec.getSize(measureSpec);
        if (lengthMode == 1073741824) {
            length = lengthSize;
        } else if (lengthMode == Integer.MIN_VALUE) {
            length = Math.min(intrinsicSize, lengthSize);
        } else {
            length = intrinsicSize;
        }
        int barPointerHaloRadiusx2 = this.mBarPointerHaloRadius * 2;
        this.mBarLength = length - barPointerHaloRadiusx2;
        if (this.mOrientation) {
            setMeasuredDimension(this.mBarLength + barPointerHaloRadiusx2, barPointerHaloRadiusx2);
        } else {
            setMeasuredDimension(barPointerHaloRadiusx2, this.mBarLength + barPointerHaloRadiusx2);
        }
    }

    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        int x1;
        int y1;
        super.onSizeChanged(w, h, oldw, oldh);
        if (this.mOrientation) {
            x1 = this.mBarLength + this.mBarPointerHaloRadius;
            y1 = this.mBarThickness;
            this.mBarLength = w - (this.mBarPointerHaloRadius * 2);
            this.mBarRect.set((float) this.mBarPointerHaloRadius, (float) (this.mBarPointerHaloRadius - (this.mBarThickness / 2)), (float) (this.mBarLength + this.mBarPointerHaloRadius), (float) (this.mBarPointerHaloRadius + (this.mBarThickness / 2)));
        } else {
            x1 = this.mBarThickness;
            y1 = this.mBarLength + this.mBarPointerHaloRadius;
            this.mBarLength = h - (this.mBarPointerHaloRadius * 2);
            this.mBarRect.set((float) (this.mBarPointerHaloRadius - (this.mBarThickness / 2)), (float) this.mBarPointerHaloRadius, (float) (this.mBarPointerHaloRadius + (this.mBarThickness / 2)), (float) (this.mBarLength + this.mBarPointerHaloRadius));
        }
        if (isInEditMode()) {
            this.shader = new LinearGradient((float) this.mBarPointerHaloRadius, 0.0f, (float) x1, (float) y1, new int[]{-8257792, ViewCompat.MEASURED_STATE_MASK}, null, TileMode.CLAMP);
            Color.colorToHSV(-8257792, this.mHSVColor);
        } else {
            this.shader = new LinearGradient((float) this.mBarPointerHaloRadius, 0.0f, (float) x1, (float) y1, new int[]{Color.HSVToColor(255, this.mHSVColor), ViewCompat.MEASURED_STATE_MASK}, null, TileMode.CLAMP);
        }
        this.mBarPaint.setShader(this.shader);
        this.mPosToSatFactor = 1.0f / ((float) this.mBarLength);
        this.mSatToPosFactor = ((float) this.mBarLength) / 1.0f;
        float[] hsvColor = new float[3];
        Color.colorToHSV(this.mColor, hsvColor);
        if (isInEditMode()) {
            this.mBarPointerPosition = this.mBarPointerHaloRadius;
        } else {
            this.mBarPointerPosition = Math.round((((float) this.mBarLength) - (this.mSatToPosFactor * hsvColor[2])) + ((float) this.mBarPointerHaloRadius));
        }
    }

    protected void onDraw(Canvas canvas) {
        int cX;
        int cY;
        canvas.drawRect(this.mBarRect, this.mBarPaint);
        if (this.mOrientation) {
            cX = this.mBarPointerPosition;
            cY = this.mBarPointerHaloRadius;
        } else {
            cX = this.mBarPointerHaloRadius;
            cY = this.mBarPointerPosition;
        }
        canvas.drawCircle((float) cX, (float) cY, (float) this.mBarPointerHaloRadius, this.mBarPointerHaloPaint);
        canvas.drawCircle((float) cX, (float) cY, (float) this.mBarPointerRadius, this.mBarPointerPaint);
    }

    public boolean onTouchEvent(MotionEvent event) {
        float dimen;
        getParent().requestDisallowInterceptTouchEvent(true);
        if (this.mOrientation) {
            dimen = event.getX();
        } else {
            dimen = event.getY();
        }
        switch (event.getAction()) {
            case 0:
                this.mIsMovingPointer = true;
                if (dimen >= ((float) this.mBarPointerHaloRadius) && dimen <= ((float) (this.mBarPointerHaloRadius + this.mBarLength))) {
                    this.mBarPointerPosition = Math.round(dimen);
                    calculateColor(Math.round(dimen));
                    this.mBarPointerPaint.setColor(this.mColor);
                    invalidate();
                    break;
                }
            case 1:
                this.mIsMovingPointer = false;
                break;
            case 2:
                if (this.mIsMovingPointer) {
                    if (dimen >= ((float) this.mBarPointerHaloRadius) && dimen <= ((float) (this.mBarPointerHaloRadius + this.mBarLength))) {
                        this.mBarPointerPosition = Math.round(dimen);
                        calculateColor(Math.round(dimen));
                        this.mBarPointerPaint.setColor(this.mColor);
                        if (this.mPicker != null) {
                            this.mPicker.setNewCenterColor(this.mColor);
                            this.mPicker.changeOpacityBarColor(this.mColor);
                        }
                        invalidate();
                    } else if (dimen < ((float) this.mBarPointerHaloRadius)) {
                        this.mBarPointerPosition = this.mBarPointerHaloRadius;
                        this.mColor = Color.HSVToColor(this.mHSVColor);
                        this.mBarPointerPaint.setColor(this.mColor);
                        if (this.mPicker != null) {
                            this.mPicker.setNewCenterColor(this.mColor);
                            this.mPicker.changeOpacityBarColor(this.mColor);
                        }
                        invalidate();
                    } else if (dimen > ((float) (this.mBarPointerHaloRadius + this.mBarLength))) {
                        this.mBarPointerPosition = this.mBarPointerHaloRadius + this.mBarLength;
                        this.mColor = ViewCompat.MEASURED_STATE_MASK;
                        this.mBarPointerPaint.setColor(this.mColor);
                        if (this.mPicker != null) {
                            this.mPicker.setNewCenterColor(this.mColor);
                            this.mPicker.changeOpacityBarColor(this.mColor);
                        }
                        invalidate();
                    }
                }
                if (!(this.onValueChangedListener == null || this.oldChangedListenerValue == this.mColor)) {
                    this.onValueChangedListener.onValueChanged(this.mColor);
                    this.oldChangedListenerValue = this.mColor;
                    break;
                }
                break;
        }
        return true;
    }

    public void setColor(int color) {
        int x1;
        int y1;
        if (this.mOrientation) {
            x1 = this.mBarLength + this.mBarPointerHaloRadius;
            y1 = this.mBarThickness;
        } else {
            x1 = this.mBarThickness;
            y1 = this.mBarLength + this.mBarPointerHaloRadius;
        }
        Color.colorToHSV(color, this.mHSVColor);
        this.shader = new LinearGradient((float) this.mBarPointerHaloRadius, 0.0f, (float) x1, (float) y1, new int[]{color, ViewCompat.MEASURED_STATE_MASK}, null, TileMode.CLAMP);
        this.mBarPaint.setShader(this.shader);
        calculateColor(this.mBarPointerPosition);
        this.mBarPointerPaint.setColor(this.mColor);
        if (this.mPicker != null) {
            this.mPicker.setNewCenterColor(this.mColor);
            if (this.mPicker.hasOpacityBar()) {
                this.mPicker.changeOpacityBarColor(this.mColor);
            }
        }
        invalidate();
    }

    public void setValue(float value) {
        this.mBarPointerPosition = Math.round((((float) this.mBarLength) - (this.mSatToPosFactor * value)) + ((float) this.mBarPointerHaloRadius));
        calculateColor(this.mBarPointerPosition);
        this.mBarPointerPaint.setColor(this.mColor);
        if (this.mPicker != null) {
            this.mPicker.setNewCenterColor(this.mColor);
            this.mPicker.changeOpacityBarColor(this.mColor);
        }
        invalidate();
    }

    private void calculateColor(int coord) {
        coord -= this.mBarPointerHaloRadius;
        if (coord < 0) {
            coord = 0;
        } else if (coord > this.mBarLength) {
            coord = this.mBarLength;
        }
        this.mColor = Color.HSVToColor(new float[]{this.mHSVColor[0], this.mHSVColor[1], 1.0f - (this.mPosToSatFactor * ((float) coord))});
    }

    public int getColor() {
        return this.mColor;
    }

    public void setColorPicker(HoloColorPicker picker) {
        this.mPicker = picker;
    }

    protected Parcelable onSaveInstanceState() {
        Parcelable superState = super.onSaveInstanceState();
        Bundle state = new Bundle();
        state.putParcelable(STATE_PARENT, superState);
        state.putFloatArray(STATE_COLOR, this.mHSVColor);
        float[] hsvColor = new float[3];
        Color.colorToHSV(this.mColor, hsvColor);
        state.putFloat(STATE_VALUE, hsvColor[2]);
        return state;
    }

    protected void onRestoreInstanceState(Parcelable state) {
        Bundle savedState = (Bundle) state;
        super.onRestoreInstanceState(savedState.getParcelable(STATE_PARENT));
        setColor(Color.HSVToColor(savedState.getFloatArray(STATE_COLOR)));
        setValue(savedState.getFloat(STATE_VALUE));
    }
}
