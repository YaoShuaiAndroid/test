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
import com.zb.bilateral.model.MuseumModel;
import com.zb.bilateral.retrofit.ApiStores;


public class MyCollectMuseumAdapter extends ListBaseAdapter<MuseumModel> {
    Context context;

    public MyCollectMuseumAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_mycollect_museum_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final MuseumModel item = mDataList.get(position);

        ImageView collect_img=holder.getView(R.id.mycollect_img);
        TextView mycollect_name=holder.getView(R.id.mycollect_name);

        mycollect_name.setText(item.getName());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mContext));

        Glide.with(mContext)
                .load(ApiStores.IMG_URL_TOP + item.getCover())
                .apply(options)
                .into(collect_img);
    }
}