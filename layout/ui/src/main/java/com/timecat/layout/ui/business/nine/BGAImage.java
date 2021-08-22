package com.timecat.layout.ui.business.nine;

import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import androidx.annotation.DrawableRes;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/2
 * @description null
 * @usage null
 */
class BGAImage {

    protected static String getPath(String path) {
        if (path == null) {
            path = "";
        }

        if (!path.startsWith("http") && !path.startsWith("file")) {
            path = "file://" + path;
        }
        return path;
    }

    public static void display(ImageView imageView, @DrawableRes int placeholderResId, String path, int width, int height) {
        String finalPath = getPath(path);
        Glide.with(imageView.getContext()).load(finalPath)
             .apply(
                     new RequestOptions()
                             .placeholder(placeholderResId)
                             .error(placeholderResId)
                             .override(width, height)
                             .dontAnimate()
             )
             .into(imageView);
    }

    public static void display(ImageView imageView, @DrawableRes int placeholderResId, String path, int size) {
        display(imageView, placeholderResId, path, size, size);
    }
}
