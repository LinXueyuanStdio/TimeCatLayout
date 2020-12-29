package com.timecat.layout.ui.standard.tablayout.listener;

import androidx.annotation.DrawableRes;

public interface CustomTabEntity {
    String getTabTitle();

    @DrawableRes
    int getTabSelectedIcon();

    @DrawableRes
    int getTabUnselectedIcon();

    String getSubTitle();
}