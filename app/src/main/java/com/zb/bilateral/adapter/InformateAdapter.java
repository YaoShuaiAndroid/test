package com.zb.bilateral.adapter;

import android.content.Context;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.adapter.ListBaseAdapter;
import com.example.mycommon.view.GlideRoundImage;
import com.example.mycommon.view.SuperViewHolder;
import com.zb.bilateral.R;
import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.retrofit.ApiStores;


public class InformateAdapter extends ListBaseAdapter<ActivityModel> {
    Context context;

    public InformateAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_informate_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final ActivityModel item = mDataList.get(position);

        TextView informate_content=holder.getView(R.id.informate_content);
        TextView informate_title=holder.getView(R.id.informate_title);
        ImageView informate_img=holder.getView(R.id.informate_img);
        TextView informate_like_num=holder.getView(R.id.informate_like_num);

        informate_title.setText(""+item.getTitle());
        informate_content.setText(Html.fromHtml(item.getContent()));
        informate_like_num.setText(""+item.getCollCount());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mContext));

        Glide.with(mContext)
                .load(ApiStores.IMG_URL_TOP + item.getCover())
                .apply(options)
                .into(informate_img);
    }
}