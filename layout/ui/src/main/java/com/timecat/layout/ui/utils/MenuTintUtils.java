package com.timecat.layout.ui.utils;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.timecat.component.identity.Attr;
import com.timecat.layout.ui.R;

import androidx.annotation.ColorInt;
import androidx.core.graphics.drawable.DrawableCompat;

/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/2/1
 * @description null
 * @usage null
 */
public class MenuTintUtils {
    public static void tintAllIcons(Menu menu,  @ColorInt final int color) {
        for (int i = 0; i < menu.size(); i++) {
            final MenuItem item = menu.getItem(i);
            tintMenuItemIcon(item, color);
            tintShareIconIfPresent(item, color);
        }
    }

    public static void tintAllIcons(Menu menu, Context context) {
        int color = Attr.getIconColor(context);
        for (int i = 0; i < menu.size(); i++) {
            final MenuItem item = menu.getItem(i);
            tintMenuItemIcon(item, color);
            tintShareIconIfPresent(item, color);
        }
    }

    public static void tintMenuItemIcon(MenuItem item,  @ColorInt final int color) {
        final Drawable drawable = item.getIcon();
        if (drawable != null) {
            final Drawable wrapped = DrawableCompat.wrap(drawable);
            drawable.mutate();
            DrawableCompat.setTint(wrapped, color);
            item.setIcon(drawable);
        }
    }

    private static void tintShareIconIfPresent(MenuItem item,  @ColorInt int color) {
        if (item.getActionView() != null) {
            final View actionView             = item.getActionView();
            final View expandActivitiesButton = actionView.findViewById(R.id.expand_activities_button);
            if (expandActivitiesButton != null) {
                final ImageView image = (ImageView) expandActivitiesButton.findViewById(R.id.image);
                if (image != null) {
                    final Drawable drawable = image.getDrawable();
                    final Drawable wrapped  = DrawableCompat.wrap(drawable);
                    drawable.mutate();
                    DrawableCompat.setTint(wrapped, color);
                    image.setImageDrawable(drawable);
                }
            }
        }
    }
}
