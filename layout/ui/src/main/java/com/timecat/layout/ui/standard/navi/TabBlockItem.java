package com.timecat.layout.ui.standard.navi;

import android.content.Context;

import org.jetbrains.annotations.NotNull;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-30
 * @description null
 * @usage null
 */
public class TabBlockItem {
    public Long id;
    public String title;
    public String imagePath;
    public String fragmentRouterPath;
    public String actionRouterPath;

    public TabBlockItem() {
    }

    public TabBlockItem(Long id, String title, String imagePath,
                        String fragmentRouterPath, String actionRouterPath) {
        this.id = id;
        this.title = title;
        this.imagePath = imagePath;
        this.fragmentRouterPath = fragmentRouterPath;
        this.actionRouterPath = actionRouterPath;
    }

    public BottomBarIvTextTab createTabView(Context context) {
        BottomBarIvTextTab item =new BottomBarIvTextTab(context, imagePath, title);
        item.setItem(this);
        return item;
    }

    public String getKey() {
        return id + title;
    }

    @NotNull
    @Override
    public String toString() {
        return "TabBlockItem{" +
                ", title='" + title + '\'' +
                ", fragmentRouterPath='" + fragmentRouterPath + '\'' +
                ", actionRouterPath='" + actionRouterPath + '\'' +
                '}';
    }
}
