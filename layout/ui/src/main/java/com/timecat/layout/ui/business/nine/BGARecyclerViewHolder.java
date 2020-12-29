package com.timecat.layout.ui.business.nine;

import android.content.Context;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * @author 林学渊
 * @email linxy59@mail2.sysu.edu.cn
 * @date 2020/7/2
 * @description null
 * @usage null
 */

public class BGARecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener {
    protected Context mContext;
    protected BGAOnRVItemClickListener mOnRVItemClickListener;
    protected BGAOnRVItemLongClickListener mOnRVItemLongClickListener;
    protected BGAViewHolderHelper mViewHolderHelper;
    protected RecyclerView mRecyclerView;
    protected BGARecyclerViewAdapter mRecyclerViewAdapter;

    public BGARecyclerViewHolder(BGARecyclerViewAdapter recyclerViewAdapter, RecyclerView recyclerView, View itemView, BGAOnRVItemClickListener onRVItemClickListener, BGAOnRVItemLongClickListener onRVItemLongClickListener) {
        super(itemView);
        mRecyclerViewAdapter = recyclerViewAdapter;
        mRecyclerView = recyclerView;
        mContext = mRecyclerView.getContext();
        mOnRVItemClickListener = onRVItemClickListener;
        mOnRVItemLongClickListener = onRVItemLongClickListener;
        itemView.setOnClickListener(new BGAOnNoDoubleClickListener() {
            @Override
            public void onNoDoubleClick(View v) {
                if (v.getId() == BGARecyclerViewHolder.this.itemView.getId() && null != mOnRVItemClickListener) {
                    mOnRVItemClickListener.onRVItemClick(mRecyclerView, v, getAdapterPositionWrapper());
                }
            }
        });
        itemView.setOnLongClickListener(this);

        mViewHolderHelper = new BGAViewHolderHelper(mRecyclerView, this);
    }

    public BGAViewHolderHelper getViewHolderHelper() {
        return mViewHolderHelper;
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getId() == this.itemView.getId() && null != mOnRVItemLongClickListener) {
            return mOnRVItemLongClickListener.onRVItemLongClick(mRecyclerView, v, getAdapterPositionWrapper());
        }
        return false;
    }

    public int getAdapterPositionWrapper() {
        if (mRecyclerViewAdapter.getHeadersCount() > 0) {
            return getAdapterPosition() - mRecyclerViewAdapter.getHeadersCount();
        } else {
            return getAdapterPosition();
        }
    }
}