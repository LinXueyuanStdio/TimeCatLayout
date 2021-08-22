package com.timecat.layout.ui.core;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.coorchice.library.ImageEngine;
import com.coorchice.library.image_engine.Engine;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/8/22
 * @description SuperTextView 的图片加载器
 * @usage null
 */
public class GlideEngine implements Engine {

    private Context context;

    public GlideEngine(Context context) {
        this.context = context;
    }

    @Override
    public void load(String url, final ImageEngine.Callback callback) {
        Glide.with(context).load(url).into(new SimpleTarget<Drawable>() {
            @Override
            public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                // 主要是通过callback返回Drawable对象给SuperTextView
                callback.onCompleted(resource);
            }
        });
    }
}