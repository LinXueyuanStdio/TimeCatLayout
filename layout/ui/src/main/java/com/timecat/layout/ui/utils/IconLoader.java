package com.timecat.layout.ui.utils;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.request.RequestOptions;
import com.timecat.component.commonsdk.utils.override.LogUtil;
import com.timecat.component.identity.Attr;
import com.timecat.identity.font.FontAwesome;
import com.timecat.identity.font.FontDrawable;
import com.timecat.layout.ui.R;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Locale;
import java.util.UUID;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.res.ResourcesCompat;
import timber.log.Timber;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019-12-30
 * @description 图标加载器
 * @usage null
 */
public class IconLoader {

    public static void loadIcon(Context context, LoadIconCallback iv, @Nullable String icon) {
        loadDefaultRoundIcon(context, iv, icon);
    }

    public static void loadDefaultRoundIcon(Context context, LoadIconCallback iv, @Nullable String icon) {
        loadRoundIcon(context, iv, icon, ViewUtil.dp2px(8));
    }

    public static void loadRoundIcon(Context context, LoadIconCallback iv, @Nullable String icon, int radius) {
        if (icon == null) { return; }
        LogUtil.se(icon);
        if (icon.startsWith(BUILDIN_DRAWABLE_SCHEME)) {
            loadDrawable(context, iv, icon);
        } else if (icon.startsWith(BUILDIN_MIPMAP_SCHEME)) {
            loadMipmap(context, iv, icon);
        } else if (icon.startsWith(AVATAR_SCHEME)) {
            String hash = icon.substring(AVATAR_SCHEME.length());
            String url = String.format(Locale.getDefault(), AVATAR_REQUEST_HASH, hash);
            loadUrlRoundIcon(context, iv, url, radius);
        } else if (icon.startsWith(COVER_SCHEME)) {
            String hash = icon.substring(COVER_SCHEME.length());
            String url = String.format(Locale.getDefault(), COVER_REQUEST_HASH, hash);
            loadUrlRoundIcon(context, iv, url, radius);
        } else if (icon.startsWith(FONTAWESOME_SCHEME)) {
            String url = icon.substring(FONTAWESOME_SCHEME.length());
            loadFontAwesome(context, iv, url);
        } else if (icon.startsWith(APP_SCHEME)) {
            loadAppIcon(context, iv, icon);
        } else {
            loadUrlRoundIcon(context, iv, icon, radius);
        }
    }

    public static void loadUrlRoundIcon(Context context, LoadIconCallback iv, @NonNull String icon, int radius) {
        //icon is url or file
        BorderRoundTransformation t = new BorderRoundTransformation(context, radius,
                0, 0, 0, BorderRoundTransformation.all_corner);
        Glide.with(context)
             .load(icon)
             .apply(new RequestOptions().placeholder(R.drawable.ic_launcher).transform(t))
             .into(new SimpleDrawableTarget(iv));
    }

    public static void loadUrlRoundIcon(Context context, LoadIconCallback iv, int icon, int radius) {
        //icon is url or file
        BorderRoundTransformation t = new BorderRoundTransformation(context, radius,
                0, 0, 0, BorderRoundTransformation.all_corner);
        Glide.with(context)
             .load(icon)
             .apply(new RequestOptions().placeholder(R.drawable.ic_launcher).transform(t))
             .into(new SimpleDrawableTarget(iv));
    }

    public static void loadAppIcon(Context context, LoadIconCallback iv, @NonNull String icon) {
        String pkgName = icon.split(":")[1];
        final PackageManager pm = context.getPackageManager();
        ApplicationInfo applicationInfo;
        try {
            applicationInfo = pm.getApplicationInfo(pkgName, 0);
            iv.setImageDrawable(applicationInfo.loadIcon(pm));
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            loadUrlRoundIcon(context, iv, R.drawable.ic_launcher, ViewUtil.dp2px(8));
        }
    }

    public static void loadMipmap(Context context, LoadIconCallback iv, @NonNull String icon) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(icon.replace("R.mipmap.", ""),
                "mipmap", context.getPackageName());
        if (resId > 0) {
            loadUrlRoundIcon(context, iv, resId, ViewUtil.dp2px(8));
        } else {
            loadUrlRoundIcon(context, iv, R.drawable.ic_launcher, ViewUtil.dp2px(8));
        }
    }

    public static void loadDrawable(Context context, LoadIconCallback iv, @NonNull String icon) {
        Resources resources = context.getResources();
        int resId = resources.getIdentifier(icon.replace(BUILDIN_DRAWABLE_SCHEME, ""),
                "drawable", context.getPackageName());
        if (resId > 0) {
            Resources.Theme theme = resources.newTheme();
            theme.applyStyle(R.style.ThemeLight, true);
            Drawable d = ResourcesCompat.getDrawable(resources, resId, theme);
            if (d != null) {
                iv.setImageDrawable(d);
            } else {
                loadUrlRoundIcon(context, iv, R.drawable.ic_launcher, ViewUtil.dp2px(8));
            }
        } else {
            loadUrlRoundIcon(context, iv, R.drawable.ic_launcher, ViewUtil.dp2px(8));
        }
    }

    public static void loadFontAwesome(Context context, LoadIconCallback iv, @NonNull String icon) {
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

    public static void loadFontAwesomeWithTypeface(Context context, LoadIconCallback iv, @NonNull Typeface tf, @NonNull String icon) {
        LogUtil.sd(icon);
        int resId = context.getResources().getIdentifier(icon, "string", context.getPackageName());
        if (resId > 0) {
            FontDrawable d = new FontDrawable(context, tf, resId);
            d.setTextSize(24);
            d.setTextColor(Attr.getIconColor(context));
            iv.setImageDrawable(d);
        } else {
            loadUrlRoundIcon(context, iv, R.drawable.ic_launcher, ViewUtil.dp2px(8));
        }
    }

    public static void load(Context context,
            RequestOptions requestOptions,
            String icon,
            LoadIconCallback iv) {
        if (icon == null) { return; }
        if (icon.startsWith(BUILDIN_DRAWABLE_SCHEME)) {
            Resources resources = context.getResources();
            String name = icon.replace(BUILDIN_DRAWABLE_SCHEME, "");
            int resId = resources.getIdentifier(name, "drawable", context.getPackageName());
            Resources.Theme theme = resources.newTheme();
            theme.applyStyle(R.style.ThemeDark, true);
            iv.setImageDrawable(ResourcesCompat.getDrawable(resources, resId, theme));
        } else if (icon.startsWith(BUILDIN_MIPMAP_SCHEME)) {
            Resources resources = context.getResources();
            String name = icon.replace(BUILDIN_MIPMAP_SCHEME, "");
            int resId = resources.getIdentifier(name, "mipmap", context.getPackageName());
            loadUrlRoundIcon(context, iv, resId, ViewUtil.dp2px(8));
        } else if (icon.startsWith(APP_SCHEME)) {
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
            LoadIconCallback iv) {
        Glide.with(context)
             .load(icon)
             .apply(requestOptions.placeholder(R.drawable.ic_launcher))
             .thumbnail(placeholder(context, requestOptions))
             .into(new SimpleDrawableTarget(iv));
    }

    public static void loadIcon(Context context, ImageView iv, @Nullable String icon) {
        loadDefaultRoundIcon(context, iv, icon);
    }

    public static void loadDefaultRoundIcon(Context context, ImageView iv, @Nullable String icon) {
        loadRoundIcon(context, iv, icon, ViewUtil.dp2px(8));
    }

    public static void loadRoundIcon(Context context, ImageView iv, @Nullable String icon, int radius) {
        if (icon == null) { return; }
        LogUtil.se(icon);
        if (icon.startsWith(BUILDIN_DRAWABLE_SCHEME)) {
            loadDrawable(context, iv, icon);
        } else if (icon.startsWith(BUILDIN_MIPMAP_SCHEME)) {
            loadMipmap(context, iv, icon);
        } else if (icon.startsWith(AVATAR_SCHEME)) {
            String hash = icon.substring(AVATAR_SCHEME.length());
            String url = String.format(Locale.getDefault(), AVATAR_REQUEST_HASH, hash);
            loadUrlRoundIcon(context, iv, url, radius);
        } else if (icon.startsWith(COVER_SCHEME)) {
            String hash = icon.substring(COVER_SCHEME.length());
            String url = String.format(Locale.getDefault(), COVER_REQUEST_HASH, hash);
            loadUrlRoundIcon(context, iv, url, radius);
        } else if (icon.startsWith(FONTAWESOME_SCHEME)) {
            String url = icon.substring(FONTAWESOME_SCHEME.length());
            loadFontAwesome(context, iv, url);
        } else if (icon.startsWith(APP_SCHEME)) {
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
        int resId = resources.getIdentifier(icon.replace(BUILDIN_DRAWABLE_SCHEME, ""),
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
        if (icon == null) { return; }
        if (icon.startsWith(BUILDIN_DRAWABLE_SCHEME)) {
            Resources resources = context.getResources();
            String name = icon.replace(BUILDIN_DRAWABLE_SCHEME, "");
            int resId = resources.getIdentifier(name, "drawable", context.getPackageName());
            Resources.Theme theme = resources.newTheme();
            theme.applyStyle(R.style.ThemeDark, true);
            iv.setImageDrawable(ResourcesCompat.getDrawable(resources, resId, theme));
        } else if (icon.startsWith(BUILDIN_MIPMAP_SCHEME)) {
            Resources resources = context.getResources();
            String name = icon.replace(BUILDIN_MIPMAP_SCHEME, "");
            int resId = resources.getIdentifier(name, "mipmap", context.getPackageName());
            iv.setImageResource(resId);
        } else if (icon.startsWith(APP_SCHEME)) {
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

    public static String randomAvatar(String seed) {
        return AVATAR_SCHEME + md5(seed);
    }

    public static String randomCover(String seed) {
        return COVER_SCHEME + md5(seed);
    }

    public static String randomAvatar() {
        return randomAvatar(UUID.randomUUID().toString());
    }

    public static String randomCover() {
        return randomCover(UUID.randomUUID().toString());
    }

    public static String md5(final String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte[] messageDigest = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; ++i) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2) { h = "0" + h; }
                hexString.append(h);
            }
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            Timber.e(e);
        }
        return "";
    }

    //region schema
    //random avatar
    public static final String AVATAR_SCHEME = "avatar://";
    public static final String AVATAR_REQUEST_HASH = "https://picsum.photos/seed/%s/64/64";
    //random cover
    public static final String COVER_SCHEME = "cover://";
    public static final String COVER_REQUEST_HASH = "https://picsum.photos/seed/%s/1600/900";

    //fontawesome
    public static final String FONTAWESOME_SCHEME = "fontawesome://";
    public static final String FONTAWESOME_BRAND = "brand/";
    public static final String FONTAWESOME_SOLID = "solid/";
    public static final String FONTAWESOME_REGULAR = "regular/";

    public static final String APP_SCHEME = "app:";
    public static final String BUILDIN_DRAWABLE_SCHEME = "R.drawable.";
    public static final String BUILDIN_MIPMAP_SCHEME = "R.mipmap.";
    //endregion
}
