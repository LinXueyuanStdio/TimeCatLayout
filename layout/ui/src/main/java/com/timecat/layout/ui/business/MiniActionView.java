package com.timecat.layout.ui.business;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.ColorStateList;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.github.florent37.viewtooltip.ViewTooltip;
import com.github.florent37.viewtooltip.ViewTooltip.Position;
import com.timecat.layout.ui.R;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;


/**
 * @author dlink
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2019/1/25
 * @description null
 * @usage null
 */
public class MiniActionView extends LinearLayout {

    public final static int VIEW_MODE = 0;
    public final static int CREATE_MODE = 1;
    public final static int EDIT_MODE = 2;
    public ImageView iv_full_edit;
    public ImageView iv_copy;
    public ImageView iv_nlp;
    public ImageView iv_translate;
    public ImageView iv_search;
    public ImageView iv_share;
    public ImageView iv_attachment;
    public ImageView iv_reminder;
    public ImageView iv_labels;
    public ImageView iv_speech;
    public ImageView iv_checklist;
    public ImageView iv_more;
    MiniAction miniAction;


    public MiniActionView(Context context) {
        super(context);
        init(context);
    }

    public MiniActionView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public MiniActionView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public MiniActionView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init(context);
    }

    private void init(Context context) {
        setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        inflate(context, R.layout.master_card_todolist_idea_miniaction, this);

        iv_full_edit = findViewById(R.id.iv_full_edit);
        iv_copy = findViewById(R.id.iv_copy);
        iv_nlp = findViewById(R.id.iv_nlp);
        iv_translate = findViewById(R.id.iv_translate);
        iv_search = findViewById(R.id.iv_search);
        iv_share = findViewById(R.id.iv_share);
        iv_attachment = findViewById(R.id.iv_attachment);
        iv_reminder = findViewById(R.id.iv_reminder);
        iv_labels = findViewById(R.id.iv_labels);
        iv_checklist = findViewById(R.id.iv_checklist);
        iv_more = findViewById(R.id.iv_more);
        iv_speech = findViewById(R.id.iv_speech);
    }

    public void tintAllWithColor(@ColorInt int color) {
        iv_full_edit.setImageTintList(ColorStateList.valueOf(color));
        iv_copy.setImageTintList(ColorStateList.valueOf(color));
        iv_nlp.setImageTintList(ColorStateList.valueOf(color));
        iv_translate.setImageTintList(ColorStateList.valueOf(color));
        iv_search.setImageTintList(ColorStateList.valueOf(color));
        iv_share.setImageTintList(ColorStateList.valueOf(color));
        iv_attachment.setImageTintList(ColorStateList.valueOf(color));
        iv_reminder.setImageTintList(ColorStateList.valueOf(color));
        iv_labels.setImageTintList(ColorStateList.valueOf(color));
        iv_checklist.setImageTintList(ColorStateList.valueOf(color));
        iv_more.setImageTintList(ColorStateList.valueOf(color));
        iv_speech.setImageTintList(ColorStateList.valueOf(color));
    }

    public void setAllWithWidthAndHeight(int width, int height) {
        iv_full_edit.getLayoutParams().width = width;
        iv_full_edit.getLayoutParams().height = height;
        iv_copy.getLayoutParams().width = width;
        iv_copy.getLayoutParams().height = height;
        iv_nlp.getLayoutParams().width = width;
        iv_nlp.getLayoutParams().height = height;
        iv_translate.getLayoutParams().width = width;
        iv_translate.getLayoutParams().height = height;
        iv_search.getLayoutParams().width = width;
        iv_search.getLayoutParams().height = height;
        iv_share.getLayoutParams().width = width;
        iv_share.getLayoutParams().height = height;
        iv_attachment.getLayoutParams().width = width;
        iv_attachment.getLayoutParams().height = height;
        iv_reminder.getLayoutParams().width = width;
        iv_reminder.getLayoutParams().height = height;
        iv_labels.getLayoutParams().width = width;
        iv_labels.getLayoutParams().height = height;
        iv_checklist.getLayoutParams().width = width;
        iv_checklist.getLayoutParams().height = height;
        iv_more.getLayoutParams().width = width;
        iv_more.getLayoutParams().height = height;
        iv_speech.getLayoutParams().width = width;
        iv_speech.getLayoutParams().height = height;
        invalidate();
    }

    public void setEnableAll(boolean enableAll) {
        iv_full_edit.setEnabled(enableAll);
        iv_copy.setEnabled(enableAll);
        iv_nlp.setEnabled(enableAll);
        iv_translate.setEnabled(enableAll);
        iv_search.setEnabled(enableAll);
        iv_share.setEnabled(enableAll);
        iv_attachment.setEnabled(enableAll);
        iv_reminder.setEnabled(enableAll);
        iv_labels.setEnabled(enableAll);
        iv_checklist.setEnabled(enableAll);
    }

    public MiniAction getMiniAction() {
        return miniAction;
    }

    public void setMiniAction(@NonNull MiniAction miniAction) {
        this.miniAction = miniAction;
        iv_full_edit.setOnClickListener(miniAction::onFullEdit);
        iv_copy.setOnClickListener(miniAction::onCopy);
        iv_nlp.setOnClickListener(miniAction::onNLP);
        iv_translate.setOnClickListener(miniAction::onTranslate);
        iv_search.setOnClickListener(miniAction::onSearch);
        iv_share.setOnClickListener(miniAction::onShare);
        iv_attachment.setOnClickListener(miniAction::onAttach);
        iv_reminder.setOnClickListener(miniAction::onRemind);
        iv_labels.setOnClickListener(miniAction::onLabels);
        iv_checklist.setOnClickListener(miniAction::onToggleCheckList);
        iv_more.setOnClickListener(miniAction::onMore);
        iv_speech.setOnClickListener(miniAction::onAppendSpeech);

    }

    public void setLongClick() {
        iv_full_edit.setOnLongClickListener(v -> showToolTip(v, "用编辑器打开"));
        iv_copy.setOnLongClickListener(v -> showToolTip(v, "复制"));
        iv_nlp.setOnLongClickListener(v -> showToolTip(v, "自然语言处理（分词）"));
        iv_translate.setOnLongClickListener(v -> showToolTip(v, "翻译"));
        iv_search.setOnLongClickListener(v -> showToolTip(v, "搜索"));
        iv_share.setOnLongClickListener(v -> showToolTip(v, "分享"));
        iv_attachment.setOnLongClickListener(v -> showToolTip(v, "附件"));
        iv_reminder.setOnLongClickListener(v -> showToolTip(v, "提醒"));
        iv_labels.setOnLongClickListener(v -> showToolTip(v, "标签"));
        iv_checklist.setOnLongClickListener(v -> showToolTip(v, "清单/Markdown"));
        iv_more.setOnLongClickListener(v -> showToolTip(v, "更多"));
        iv_speech.setOnLongClickListener(v -> showToolTip(v, "加入语音识别"));
    }

    private boolean showToolTip(View v, String s) {
        ViewTooltip.on(v)
                   .autoHide(true, 1000)
                   .clickToHide(true)
                   .position(Position.BOTTOM)
                   .text(s)
                   .corner(10)
                   .arrowWidth(15)
                   .arrowHeight(15)
                   .show();
        return true;
    }

    public interface MiniAction {

        void onFullEdit(View iv_full_edit);

        void onCopy(View iv_copy);

        void onNLP(View iv_nlp);

        void onTranslate(View iv_translate);

        void onSearch(View iv_search);

        void onShare(View iv_share);

        void onAttach(View iv_attachment);

        void onRemind(View iv_reminder);

        void onLabels(View iv_labels);

        void onToggleCheckList(View iv_checklist);

        void onMore(View iv_more);

        void onAppendSpeech(View iv_speech);
    }
}
