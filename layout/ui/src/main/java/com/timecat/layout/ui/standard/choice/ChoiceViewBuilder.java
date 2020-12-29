package com.timecat.layout.ui.standard.choice;

import android.content.Context;
import android.content.res.ColorStateList;
import android.view.View;

import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.timecat.layout.ui.R;

import java.util.List;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/12/6
 * @description null
 * @usage null
 */
public class ChoiceViewBuilder {
    public static ChipGroup buildSingleChoice(Context context, List<Choice> choiceList) {
        ChipGroup group = new ChipGroup(context);
        group.setSingleSelection(true);
        return loadChoice(context, group, choiceList);
    }

    public static ChipGroup buildMultiChoice(Context context, List<Choice> choiceList) {
        ChipGroup group = new ChipGroup(context);
        group.setSingleSelection(false);
        return loadChoice(context, group, choiceList);
    }

    public static ChipGroup loadChoice(Context context, ChipGroup group, List<Choice> choiceList) {
        for (Choice choice : choiceList) {
            Chip chip = new Chip(context, null, R.style.Widget_MaterialComponents_Chip_Filter);
            chip.setText(choice.text);
            chip.setTextColor(choice.textColor);
            chip.setSelected(choice.check);
            chip.setCheckedIconResource(choice.icon);
            chip.setCheckedIconTint(ColorStateList.valueOf(choice.iconColor));
            chip.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (choice.onClickListener != null) {
                        choice.onClickListener.onClick(chip);
                    }
                }
            });
            chip.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if (choice.onLongClickListener != null) {
                        return choice.onLongClickListener.onLongClick(chip);
                    }
                    return false;
                }
            });
            group.addView(chip);
        }
        return group;
    }
}
