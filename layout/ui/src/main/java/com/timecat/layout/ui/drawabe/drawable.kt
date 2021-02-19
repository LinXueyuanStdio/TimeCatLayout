package com.timecat.layout.ui.drawabe

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.Drawable
import com.timecat.layout.ui.R
import com.timecat.layout.ui.layout.dp
import top.defaults.drawabletoolbox.DrawableBuilder
import top.defaults.drawabletoolbox.LayerDrawableBuilder
import top.defaults.drawabletoolbox.StateListDrawableBuilder

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2021/2/15
 * @description null
 * @usage null
 */

const val COLOR_DEFAULT = 0xFFBA68C8.toInt()
const val COLOR_DEFAULT_DARK = 0xFF9C27B0.toInt()
const val COLOR_PRESSED = 0xFFF44336.toInt()
val rect_border_ripple = DrawableBuilder()
    .rectangle()
    .hairlineBordered()
    .strokeColor(COLOR_DEFAULT)
    .strokeColorPressed(COLOR_PRESSED)
    .ripple()
    .build()

fun roundRectSolidDrawableBuilder(solidColor: Int) = DrawableBuilder()
    .rectangle()
    .rounded()
    .cornerRadius(5.dp)
    .solidColor(solidColor)

fun roundRectStrokeDrawableBuilder(strokeColor: Int) = DrawableBuilder()
    .rectangle()
    .rounded()
    .cornerRadius(5.dp)
    .strokeColor(strokeColor)

fun roundRectDashedDrawableBuilder(strokeColor: Int) = DrawableBuilder()
    .rectangle()
    .rounded()
    .hairlineBordered()
    .cornerRadius(5.dp)
    .dashed()
    .strokeColor(strokeColor)

fun roundRectSelector(
    colorDefault: Int = COLOR_DEFAULT
): Drawable {
    val layer5 = roundRectDashedDrawableBuilder(colorDefault).build()
    val normalState = layer5
    val selectedState = roundRectSolidDrawableBuilder(colorDefault).build()
    val disabledState = roundRectDashedDrawableBuilder(Color.GRAY).build()
    val pressedState = LayerDrawableBuilder()
        .add(layer5)
        .inset(10).build()
    return StateListDrawableBuilder()
        .normal(normalState)
        .pressed(pressedState)
        .selected(selectedState)
        .disabled(disabledState)
        .build()
}

fun selectableItemBackground(context: Context): Drawable? {
    val typedArray = context.obtainStyledAttributes(
        intArrayOf(R.attr.selectableItemBackground)
    )
    val drawable = typedArray.getDrawable(0)
    typedArray.recycle()
    return drawable
}