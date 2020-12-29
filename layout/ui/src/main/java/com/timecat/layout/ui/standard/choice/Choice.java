package com.timecat.layout.ui.standard.choice;

import com.google.android.material.chip.Chip;
import com.timecat.layout.ui.R;
import com.timecat.layout.ui.utils.ColorUtils;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/12/6
 * @description null
 * @usage null
 */
public class Choice {
    public String text;
    public int icon;
    public int textColor;
    public int iconColor;
    public boolean check;
    public OnClickListener onClickListener;
    public OnLongClickListener onLongClickListener;

    public interface OnClickListener {
        void onClick(Chip v);
    }

    public interface OnLongClickListener {
        boolean onLongClick(Chip v);
    }

    public Choice(String text, boolean check) {
        this.text = text;
        this.check = check;
        int color = ColorUtils.randomColor();
        this.icon = R.drawable.ic_launcher;
        this.textColor = color;
        this.iconColor = color;
    }

    public Choice(String text, int icon, int textColor, int iconColor, boolean check) {
        this.text = text;
        this.icon = icon;
        this.textColor = textColor;
        this.iconColor = iconColor;
        this.check = check;
    }

    public OnClickListener getOnClickListener() {
        return onClickListener;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public OnLongClickListener getOnLongClickListener() {
        return onLongClickListener;
    }

    public void setOnLongClickListener(OnLongClickListener onLongClickListener) {
        this.onLongClickListener = onLongClickListener;
    }
}
