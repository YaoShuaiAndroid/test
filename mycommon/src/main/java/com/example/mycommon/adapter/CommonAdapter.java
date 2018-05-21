package com.example.mycommon.adapter;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

public abstract class CommonAdapter<T> extends BaseQuickAdapter<T, BaseViewHolder> {
    boolean isAutoSize = true;

    public CommonAdapter(int layoutResId, List<T> data) {
        super(layoutResId, data);
    }

    public CommonAdapter(int layoutResId, List<T> data, boolean isAutoSize) {
        super(layoutResId, data);
        this.isAutoSize = isAutoSize;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, T item) {
        convertViewItem(baseViewHolder, item);
    }

    public abstract void convertViewItem(BaseViewHolder baseViewHolder, T item);
}
