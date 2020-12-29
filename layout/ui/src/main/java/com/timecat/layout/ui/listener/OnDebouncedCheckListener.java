package com.timecat.layout.ui.listener;

import android.widget.CompoundButton;

import java.util.concurrent.TimeUnit;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/5/22
 * @description null
 * @usage null
 */
public abstract class OnDebouncedCheckListener implements CompoundButton.OnCheckedChangeListener {

    private long debounceIntervalInMillis;
    private long previousClickTimestamp;

    public OnDebouncedCheckListener(long debounceIntervalInMillis) {
        this.debounceIntervalInMillis = debounceIntervalInMillis;
    }

    public OnDebouncedCheckListener() {
        this(1000);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

        final long currentClickTimestamp = TimeUnit.NANOSECONDS.toMillis(System.nanoTime());

        if (previousClickTimestamp == 0
                || currentClickTimestamp - previousClickTimestamp >= debounceIntervalInMillis) {

            //update click timestamp
            previousClickTimestamp = currentClickTimestamp;

            this.onDebouncedCheckedChanged(buttonView, isChecked);
        }
    }

    public abstract void onDebouncedCheckedChanged(CompoundButton buttonView, boolean isChecked);
}
