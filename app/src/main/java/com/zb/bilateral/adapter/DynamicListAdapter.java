package com.zb.bilateral.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.adapter.ListBaseAdapter;
import com.example.mycommon.view.GlideCircleTransform;
import com.zb.bilateral.util.AppUtil;
import com.example.mycommon.view.GlideRoundImage;
import com.example.mycommon.view.SuperViewHolder;
import com.zb.bilateral.R;
import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.retrofit.ApiStores;


public class DynamicListAdapter extends ListBaseAdapter<ActivityModel> {
    Context context;

    public DynamicListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_dynamic_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final ActivityModel item = mDataList.get(position);

        ImageView activity_img=holder.getView(R.id.activity_img);
        TextView dynamic_title_item=holder.getView(R.id.dynamic_title_item);
        TextView dynamic_time_text=holder.getView(R.id.dynamic_time_text);
        ImageView dynamic_collect_img=holder.getView(R.id.dynamic_collect_img);
        TextView activity_status_text=holder.getView(R.id.activity_status_text);

        dynamic_title_item.setText(item.getTitle());
        dynamic_time_text.setText(AppUtil.getStrTime(item.getStartTime())+"-"+AppUtil.getStrTime(item.getEndTime()));

        if(item.getIsColl()!=null&& "1".equals(item.getIsColl())){
            dynamic_collect_img.setBackgroundResource(R.mipmap.cultrue_collect_true);
        }else{
            dynamic_collect_img.setBackgroundResource(R.mipmap.collect_gray);
        }

        if (item.getStatus() != null && "0".equals(item.getStatus())) {
            activity_status_text.setText("未开始");
        } else if (item.getStatus() != null && "1".equals(item.getStatus())) {
            activity_status_text.setText("进行中");
        } else {
            activity_status_text.setText("已结束");
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mContext));

        Glide.with(mContext)
                .load(ApiStores.IMG_URL_TOP + item.getCover())
                .apply(options)
                .into(activity_img);
    }
}