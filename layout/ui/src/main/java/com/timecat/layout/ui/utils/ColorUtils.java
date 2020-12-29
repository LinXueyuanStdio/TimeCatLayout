package com.timecat.layout.ui.utils;

import android.graphics.Color;

import androidx.annotation.ColorInt;
import androidx.annotation.IntRange;

import java.util.Random;

public abstract class ColorUtils {

    public static int getAndroidTestColor(int index) {
        int palette[] = {
                Color.parseColor("#D32F2F"), //  0 red
                Color.parseColor("#E64A19"), //  1 orange
                Color.parseColor("#F9A825"), //  2 yellow
                Color.parseColor("#AFB42B"), //  3 light green
                Color.parseColor("#388E3C"), //  4 dark green
                Color.parseColor("#00897B"), //  5 teal
                Color.parseColor("#00ACC1"), //  6 cyan
                Color.parseColor("#039BE5"), //  7 blue
                Color.parseColor("#5E35B1"), //  8 deep purple
                Color.parseColor("#8E24AA"), //  9 purple
                Color.parseColor("#D81B60"), // 10 pink
                Color.parseColor("#303030"), // 11 dark grey
                Color.parseColor("#aaaaaa")  // 12 light grey
        };

        return palette[index];
    }

    public static int mixColors(int color1, int color2, float amount) {
        final byte ALPHA_CHANNEL = 24;
        final byte RED_CHANNEL = 16;
        final byte GREEN_CHANNEL = 8;
        final byte BLUE_CHANNEL = 0;

        final float inverseAmount = 1.0f - amount;

        int a = ((int) (((float) (color1 >> ALPHA_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> ALPHA_CHANNEL & 0xff) *
                        inverseAmount))) & 0xff;
        int r = ((int) (((float) (color1 >> RED_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> RED_CHANNEL & 0xff) *
                        inverseAmount))) & 0xff;
        int g = ((int) (((float) (color1 >> GREEN_CHANNEL & 0xff) * amount) +
                ((float) (color2 >> GREEN_CHANNEL & 0xff) *
                        inverseAmount))) & 0xff;
        int b = ((int) (((float) (color1 & 0xff) * amount) +
                ((float) (color2 & 0xff) * inverseAmount))) & 0xff;

        return a << ALPHA_CHANNEL | r << RED_CHANNEL | g << GREEN_CHANNEL |
                b << BLUE_CHANNEL;
    }

    public static int setAlpha(int color, float newAlpha) {
        int intAlpha = (int) (newAlpha * 255);
        return Color.argb(intAlpha, Color.red(color), Color.green(color),
                Color.blue(color));
    }

    public static int setMinValue(int color, float newValue) {
        float hsv[] = new float[3];
        Color.colorToHSV(color, hsv);
        hsv[2] = Math.max(hsv[2], newValue);
        return Color.HSVToColor(hsv);
    }

    /**
     * 改变color值的透明度
     *
     * @param RGBValues rgb颜色值
     * @param alpha     透明度 0-255
     * @return the alpha color
     */
    public static int getAlphaColor(@ColorInt int RGBValues, @IntRange(from = 0, to = 255) int alpha) {
        int red = RGBValues >> 16 & 0xFF;
        int green = RGBValues >> 8 & 0xFF;
        int blue = RGBValues & 0xFF;
        red = (int) Math.floor(red * (1 - 0.1));
        green = (int) Math.floor(green * (1 - 0.1));
        blue = (int) Math.floor(blue * (1 - 0.1));
        red = red < 0 ? 0 : red;
        green = green < 0 ? 0 : green;
        blue = blue < 0 ? 0 : blue;
        return Color.argb(alpha, red, green, blue);

    }

    public static int randomColor() {
        return Color.HSVToColor(new float[]{(float) new Random().nextInt(360), 0.5f, 0.9f});
    }
}