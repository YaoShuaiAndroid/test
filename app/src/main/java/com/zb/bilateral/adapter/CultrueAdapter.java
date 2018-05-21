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
import com.zb.bilateral.model.CultrueModel;
import com.zb.bilateral.retrofit.ApiStores;


public class CultrueAdapter extends ListBaseAdapter<CultrueModel> {
    Context context;

    public CultrueAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_fragment_cultrue_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final CultrueModel item = mDataList.get(position);

        TextView cultrue_item_collectNum=holder.getView(R.id.cultrue_item_collectNum);
        TextView cultrue_item_content=holder.getView(R.id.cultrue_item_content);
        TextView cultrue_item_title=holder.getView(R.id.cultrue_item_title);
        ImageView cultrue_item_img=holder.getView(R.id.cultrue_item_img);

        cultrue_item_collectNum.setText(""+item.getCollCount());
        cultrue_item_content.setText(item.getDigest());
        cultrue_item_title.setText(item.getTitle());

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mContext));

        Glide.with(mContext)
                .load(ApiStores.IMG_URL_TOP + item.getCover())
                .apply(options)
                .into(cultrue_item_img);
    }
}