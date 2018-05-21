package com.zb.bilateral.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.TextView;

import com.example.mycommon.adapter.ListBaseAdapter;
import com.example.mycommon.view.SuperViewHolder;
import com.zb.bilateral.R;
import com.zb.bilateral.model.ActivityModel;

/**
 * Created by yaos on 2018/1/27.
 */

public class AnnouncementAdapter extends ListBaseAdapter<ActivityModel> {
    Context context;

    public AnnouncementAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_announcement_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final ActivityModel item = mDataList.get(position);

        TextView announcement_title=holder.getView(R.id.announcement_title);
        TextView announcement_content=holder.getView(R.id.announcement_content);
        TextView announcement_like_num=holder.getView(R.id.announcement_like_num);

        announcement_title.setText(item.getTitle());
        announcement_content.setText(Html.fromHtml(item.getContent()));
        announcement_like_num.setText(item.getCollCount());
    }
}