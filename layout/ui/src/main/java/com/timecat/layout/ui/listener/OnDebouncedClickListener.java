package com.timecat.layout.ui.listener;

import android.view.View;

import java.util.concurrent.TimeUnit;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/22
 * @description null
 * @usage null
 */
public abstract class OnDebouncedClickListener implements View.OnClickListener {

    private long debounceIntervalInMillis;
    private long previousClickTimestamp;

    public OnDebouncedClickListener(long debounceIntervalInMillis) {
        this.debounceIntervalInMillis = debounceIntervalInMillis;
    }

    public OnDebouncedClickListener() {
        this(800);
    }

    @Override
    public void onClick(View view) {

        final long currentClickTimestamp = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

        if (previousClickTimestamp == 0
                || currentClickTimestamp - previousClickTimestamp >= debounceIntervalInMillis) {

            //update click timestamp
            previousClickTimestamp = currentClickTimestamp;

            this.onDebouncedClick(view);
        }
    }

    public abstract void onDebouncedClick(View v);
}
