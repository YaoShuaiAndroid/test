package com.zb.bilateral.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.adapter.ListBaseAdapter;
import com.example.mycommon.view.GlideRoundImage;
import com.example.mycommon.view.SuperViewHolder;
import com.zb.bilateral.R;
import com.zb.bilateral.model.ShowModel;
import com.zb.bilateral.retrofit.ApiStores;


public class ShowListAdapter extends ListBaseAdapter<ShowModel> {
    Context context;

    public ShowListAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_show_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final ShowModel item = mDataList.get(position);

        ImageView show_collect_img=holder.getView(R.id.show_collect_img);
        TextView show_name=holder.getView(R.id.show_name);
        ImageView show_img=holder.getView(R.id.show_img);

        show_name.setText(item.getTitle());

        if(item.getIsColl()!=null&& "1".equals(item.getIsColl())){
            show_collect_img.setBackgroundResource(R.mipmap.cultrue_collect_true);
        }else{
            show_collect_img.setBackgroundResource(R.mipmap.collect_gray);
        }


        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mContext));

        Glide.with(mContext)
                .load(ApiStores.IMG_URL_TOP + item.getCover())
                .apply(options)
                .into(show_img);
    }
}