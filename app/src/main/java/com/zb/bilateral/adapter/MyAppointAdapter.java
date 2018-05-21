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
import com.zb.bilateral.model.AppointmentModel;
import com.zb.bilateral.retrofit.ApiStores;


public class MyAppointAdapter extends ListBaseAdapter<AppointmentModel> {
    Context context;

    public MyAppointAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_myappoint_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final AppointmentModel item = mDataList.get(position);

        ImageView myappiont_item_img=holder.getView(R.id.myappiont_item_img);
        ImageView myappiont_item_status=holder.getView(R.id.myappiont_item_status);
        TextView myappiont_item_name=holder.getView(R.id.myappiont_item_name);
        TextView myappiont_item_date=holder.getView(R.id.myappiont_item_date);

        myappiont_item_date.setText(AppUtil.getStrTime(item.getCreateDate()));
        myappiont_item_name.setText(item.getName());


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mContext));

        Glide.with(mContext)
                .load(ApiStores.IMG_URL_TOP + item.getLogo())
                .apply(options)
                .into(myappiont_item_img);

        if(item.getStatus()!=null&& "0".equals(item.getStatus())){
            myappiont_item_status.setBackgroundResource(R.mipmap.status_chuli);
        }else if(item.getStatus()!=null&& "1".equals(item.getStatus())){
            myappiont_item_status.setBackgroundResource(R.mipmap.status_anpai);
        }else if(item.getStatus()!=null&& "2".equals(item.getStatus())){
            myappiont_item_status.setBackgroundResource(R.mipmap.status_jiedai);
        }
    }
}