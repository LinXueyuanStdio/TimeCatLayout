package com.timecat.layout.ui.standard.calendar;

import android.content.Context;
import android.view.LayoutInflater;
import android.widget.TextView;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.WeekBar;
import com.timecat.component.identity.Attr;
import com.timecat.identity.skin.SkinEnable;
import com.timecat.layout.ui.R;


/**
 * 自定义英文栏
 * Created by huanghaibin on 2017/11/30.
 */

public class CustomWeekBar extends WeekBar implements SkinEnable {

    private int mPreSelectedIndex;

    public CustomWeekBar(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.view_calendar_english_week_bar, this, true);
    }

    @Override
    protected void onDateSelected(Calendar calendar, boolean isClick) {
        ((TextView) getChildAt(mPreSelectedIndex)).setTextColor(Attr.getPrimaryTextColor(getContext()));
        ((TextView) getChildAt(calendar.getWeek())).setTextColor(Attr.getAccentColor(getContext()));
        mPreSelectedIndex = calendar.getWeek();
    }

    @Override
    public void applySkin() {
        postInvalidate();
    }
}
