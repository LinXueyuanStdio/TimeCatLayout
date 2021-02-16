package com.timecat.layout.ui.drawabe

import android.graphics.Color
import android.graphics.drawable.Drawable
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

fun roundBorderDrawableBuilder(
    colorDefault: Int = COLOR_DEFAULT,
    colorPressed: Int = COLOR_PRESSED
): LayerDrawableBuilder {
    val layer1 = DrawableBuilder()
        .size(200)
        .rectangle()
        .rounded()
        .hairlineBordered()
        .strokeColor(colorDefault)
        .strokeColorPressed(colorPressed)
        .build()
    val layer2 = DrawableBuilder()
        .rectangle()
        .rounded()
        .solidColor(colorDefault)
        .build()
    val layer3 = DrawableBuilder()
        .rectangle()
        .rounded()
        .solidColor(Color.WHITE)
        .ripple()
        .rippleColor(colorDefault)
        .build()
    return LayerDrawableBuilder()
        .add(layer1)
        .add(layer2)
        .inset(10)
        .add(layer3)
        .inset(20)
}

fun roundBorderDrawable(
    colorDefault: Int = COLOR_DEFAULT,
    colorPressed: Int = COLOR_PRESSED
): Drawable = roundBorderDrawableBuilder(colorDefault, colorPressed).build()

fun roundBorderSelector(
    colorDefault: Int = COLOR_DEFAULT,
    colorPressed: Int = COLOR_PRESSED
): Drawable {
    val layer1 = DrawableBuilder()
        .rectangle()
        .cornerRadius(5)
        .hairlineBordered()
        .strokeColor(colorDefault)
        .strokeColorPressed(colorPressed)
        .build()
    val layer2 = DrawableBuilder()
        .rectangle()
        .rounded()
        .cornerRadius(5)
        .solidColor(colorDefault)
        .build()
    val layer3 = DrawableBuilder()
        .rectangle()
        .rounded()
        .cornerRadius(5)
        .solidColor(Color.WHITE)
        .ripple()
        .rippleColor(colorDefault)
        .build()
    val layer4 = DrawableBuilder()
        .rectangle()
        .rounded()
        .hairlineBordered()
        .longDashed()
        .cornerRadius(5)
        .solidColor(Color.WHITE)
        .ripple()
        .rippleColor(colorDefault)
        .build()

    val layer5 = DrawableBuilder()
        .rectangle()
        .cornerRadius(5)
        .hairlineBordered()
        .strokeColor(colorDefault)
        .strokeColorPressed(colorPressed)
        .build()
    val normalState = layer5
    val selectedState = LayerDrawableBuilder()
        .add(layer1)
        .add(layer2)
        .inset(10)
        .add(layer3)
        .inset(20).build()
    val disabledState = LayerDrawableBuilder()
        .add(layer1)
        .add(layer2)
        .inset(10)
        .add(layer4)
        .inset(20).build()
    val pressedState = LayerDrawableBuilder()
        .add(layer1)
        .inset(10)
        .add(layer3)
        .inset(20).build()
    return StateListDrawableBuilder()
        .normal(normalState)
        .pressed(pressedState)
        .selected(selectedState)
        .disabled(disabledState)
        .build()
}
