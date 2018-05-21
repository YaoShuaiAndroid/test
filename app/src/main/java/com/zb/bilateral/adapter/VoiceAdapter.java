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


public class VoiceAdapter extends ListBaseAdapter<MuseumModel> {
    Context context;

    public VoiceAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_voice_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final MuseumModel item = mDataList.get(position);

        ImageView museum_img=holder.getView(R.id.museum_img);
        TextView voice_museum_name=holder.getView(R.id.voice_museum_name);
        TextView voice_museum_address=holder.getView(R.id.voice_museum_address);
        TextView voice_collect_num=holder.getView(R.id.voice_collect_num);

        voice_museum_name.setText(item.getName());
        voice_museum_address.setText(item.getAREA());
        voice_collect_num.setText(item.getCollCount());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mContext));

        Glide.with(mContext)
                .load(ApiStores.IMG_URL_TOP +item.getCover())
                .apply(options)
                .into(museum_img);
    }
}