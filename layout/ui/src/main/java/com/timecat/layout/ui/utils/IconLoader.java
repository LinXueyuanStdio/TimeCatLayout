package com.timecat.layout.ui.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.identity.font.FontAwesome;
import com.timecat.identity.font.FontDrawable;
import com.timecat.component.identity.Attr;
import com.timecat.layout.ui.R;

import java.util.Locale;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-30
 * @description 图标加载器
 * @usage null
 */
public class IconLoader {
    public static void loadIcon(Context context, ImageView iv, @Nullable String icon) {
        loadDefaultRoundIcon(context, iv, icon);
    }

    public static void loadDefaultRoundIcon(Context context, ImageView iv, @Nullable String icon) {
        loadRoundIcon(context, iv, icon, ViewUtil.dp2px(8));
    }

    public static void loadRoundIcon(Context context, ImageView iv, @Nullable String icon, int radius) {
        if (icon == null) return;
        LogUtil.se(icon);
        if (icon.contains("R.drawable")) {
            loadDrawable(context, iv, icon);
        } else if (icon.contains("R.mipmap")) {
            loadMipmap(context, iv, icon);
        } else if (icon.startsWith(AVATAR_SCHEME)) {
            String hash = icon.substring(AVATAR_SCHEME.length());
            String url = String.format(Locale.getDefault(), IMAGE_REQUEST_HASH, hash);
            loadUrlRoundIcon(context, iv, url, radius);
        } else if (icon.startsWith(FONTAWESOME_SCHEME)) {
            String url = icon.substring(FONTAWESOME_SCHEME.length());
            loadFontAwesome(context, iv, url);
        } else if (icon.startsWith("app:")) {
            loadAppIcon(context, iv, icon);
        } else {
            loadUrlRoundIcon(context, iv, icon, radius);
        }
    }

    public static void loadUrlIcon(Context context, ImageView iv, @NonNull String icon) {
        Glide.with(context)
                .load(icon)
                .apply(new RequestOptions().placeholder(R.drawable.ic_launcher))
                .into(iv);
    }

    public static void loadUrlRoundIcon(Context context, ImageView iv, @NonNull String icon, int radius) {
        //icon is url or file
        BorderRoundTransformation t = new BorderRoundTransformation(context, radius,
                0, 0, 0, BorderRoundTransformation.all_corner);
        Glide.with(context)
                .load(icon)
                .apply(new RequestOptions().placeholder(R.drawable.ic_launcher).transform(t))
                .into(iv);
    }

    public static void loadAppIcon(Context context, ImageView iv, @NonNull String icon) {
        String pkgName = icon.split(":")[1];
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = pm.getApplicationInfo(pkgName, 0);
            iv.setImageDrawable(applicationInfo.loadIcon(pm));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            iv.setImageResource(R.drawable.ic_launcher);
        }
    }

    public static void loadMipmap(Context context, ImageView iv, @NonNull String icon) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(icon.replace("R.mipmap.", ""),
                "mipmap", context.getPackageName());
        if (resId > 0) {
            iv.setImageResource(resId);
        } else {
            iv.setImageResource(R.drawable.ic_launcher);
        }
    }

    public static void loadDrawable(Context context, ImageView iv, @NonNull String icon) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(icon.replace("R.drawable.", ""),
                "drawable", context.getPackageName());
        if (resId > 0) {
            Resources.Theme theme = resources.newTheme();
            theme.applyStyle(R.style.ThemeLight, true);
            Drawable d = ResourcesCompat.getDrawable(resources, resId, theme);
            if (d != null) {
                iv.setImageDrawable(d);
            } else {
                iv.setImageResource(R.mipmap.ic_launcher_foreground);
            }
        } else {
            iv.setImageResource(R.mipmap.ic_launcher_foreground);
        }
    }

    public static void loadFontAwesome(Context context, ImageView iv, @NonNull String icon) {
        LogUtil.sd(icon);
        if (icon.startsWith(FONTAWESOME_SOLID)) {
            Typeface tf = FontAwesome.getFontAwesomeSolid(context);
            String url = icon.substring(FONTAWESOME_SOLID.length());
            loadFontAwesomeWithTypeface(context, iv, tf, url);
        } else if (icon.startsWith(FONTAWESOME_BRAND)) {
            Typeface tf = FontAwesome.getFontAwesomeBrand(context);
            String url = icon.substring(FONTAWESOME_BRAND.length());
            loadFontAwesomeWithTypeface(context, iv, tf, url);
        } else if (icon.startsWith(FONTAWESOME_REGULAR)) {
            Typeface tf = FontAwesome.getFontAwesomeRegular(context);
            String url = icon.substring(FONTAWESOME_REGULAR.length());
            loadFontAwesomeWithTypeface(context, iv, tf, url);
        } else {
            Typeface tf = FontAwesome.getFontAwesome(context);
            loadFontAwesomeWithTypeface(context, iv, tf, icon);
        }
    }

    public static void loadFontAwesomeWithTypeface(Context context, ImageView iv, @NonNull Typeface tf, @NonNull String icon) {
        LogUtil.sd(icon);
        int resId = context.getResources().getIdentifier(icon, "string", context.getPackageName());
        if (resId > 0) {
            FontDrawable d = new FontDrawable(context, tf, resId);
            d.setTextColor(Attr.getIconColor(context));
            iv.setImageDrawable(d);
        } else {
            iv.setImageResource(R.mipmap.ic_launcher_foreground);
        }
    }

    public static void load(Context context,
                            RequestOptions requestOptions,
                            String icon,
                            ImageView iv) {
        if (icon == null) return;
        if (icon.contains("R.drawable")) {
            Resources resources = context.getResources();
            String name = icon.replace("R.drawable.", "");
            int resId = resources.getIdentifier(name, "drawable", context.getPackageName());
            Resources.Theme theme = resources.newTheme();
            theme.applyStyle(R.style.ThemeDark, true);
            iv.setImageDrawable(ResourcesCompat.getDrawable(resources, resId, theme));
        } else if (icon.contains("R.mipmap")) {
            Resources resources = context.getResources();
            String name = icon.replace("R.mipmap.", "");
            int resId = resources.getIdentifier(name, "mipmap", context.getPackageName());
            iv.setImageResource(resId);
        } else if (icon.startsWith("app:")) {
            String pkgName = icon.split(":")[1];
            final PackageManager pm = context.getPackageManager();
            ApplicationInfo applicationInfo;
            try {
                applicationInfo = pm.getApplicationInfo(pkgName, 0);
                iv.setImageDrawable(applicationInfo.loadIcon(pm));
            } catch (PackageManager.NameNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            loadGlide(context, requestOptions, icon, iv);
        }
    }

    public static void loadGlide(Context context,
                                 RequestOptions requestOptions,
                                 String icon,
                                 ImageView iv) {
        Glide.with(context)
                .load(icon)
                .apply(requestOptions.placeholder(R.drawable.ic_launcher))
                .thumbnail(placeholder(context, requestOptions))
                .into(iv);
    }

    /**
     * 统一处理占位图
     *
     * @param context        上下文对象
     * @param requestOptions 加载配置
     * @return
     */
    private static RequestBuilder<Drawable> placeholder(Context context, RequestOptions requestOptions) {
        int resId = R.drawable.ic_launcher;
        return Glide.with(context).load(resId).apply(requestOptions);
    }

    //region schema
    public static final String IMAGE_REQUEST_HASH = "http://www.gravatar.com/avatar/%s?s=40&d=identicon";
    public static final String AVATAR_SCHEME = "avatar://";
    public static final String FONTAWESOME_SCHEME = "fontawesome://";
    public static final String FONTAWESOME_BRAND = "brand/";
    public static final String FONTAWESOME_SOLID = "solid/";
    public static final String FONTAWESOME_REGULAR = "regular/";
    //endregion
}
