package com.zb.bilateral.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.adapter.ListBaseAdapter;
import com.zb.bilateral.util.AppUtil;
import com.example.mycommon.view.GlideRoundImage;
import com.example.mycommon.view.SuperViewHolder;
import com.zb.bilateral.R;
import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.retrofit.ApiStores;


public class MyActivityAdapter extends ListBaseAdapter<ActivityModel> {
    Context context;

    public MyActivityAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_myactivity_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final ActivityModel item = mDataList.get(position);
        ImageView myactivity_img=holder.getView(R.id.myactivity_img);
        ImageView myactivity_status=holder.getView(R.id.myactivity_status);
        TextView myactivity_name=holder.getView(R.id.myactivity_name);
        TextView myactivity_time=holder.getView(R.id.myactivity_time);

        String startTime=item.getStartTime().contains("-")?item.getStartTime():AppUtil.getStrTime(item.getStartTime());
        String endTime=item.getEndTime().contains("-")?item.getEndTime():AppUtil.getStrTime(item.getEndTime());

        myactivity_time.setText(startTime +"-"+endTime);
        myactivity_name.setText(item.getTitle());
        String url=item.getLogo()!=null?item.getLogo():item.getCover();

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mContext));

        Glide.with(mContext)
                .load(ApiStores.IMG_URL_TOP +url)
                .apply(options)
                .into(myactivity_img);

        if(item.getStatus()!=null&& "0".equals(item.getStatus())){
            myactivity_status.setBackgroundResource(R.mipmap.acitvity_start);
        }else if(item.getStatus()!=null&& "1".equals(item.getStatus())){
            myactivity_status.setBackgroundResource(R.mipmap.activity_ing);
        }else if(item.getStatus()!=null&& "2".equals(item.getStatus())){
            myactivity_status.setBackgroundResource(R.mipmap.status_end);
        }
    }
}