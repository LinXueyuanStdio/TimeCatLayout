package com.timecat.layout.ui.entity;

import com.timecat.layout.ui.standard.tablayout.listener.CustomTabEntity;

public class TabEntity implements CustomTabEntity {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;
    public String subTitle;

    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }

    public TabEntity(String title, String subTitle) {
        this.title = title;
        this.subTitle = subTitle;
    }

    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setSelectedIcon(int selectedIcon) {
        this.selectedIcon = selectedIcon;
    }

    public void setUnSelectedIcon(int unSelectedIcon) {
        this.unSelectedIcon = unSelectedIcon;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }
}