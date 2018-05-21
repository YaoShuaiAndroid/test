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
import com.zb.bilateral.model.CommentModel;
import com.zb.bilateral.retrofit.ApiStores;


public class CultruedetailAdapter extends ListBaseAdapter<CommentModel> {
    Context context;

    public CultruedetailAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_cultrue_detail_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final CommentModel item = mDataList.get(position);

        ImageView comment_img=holder.getView(R.id.comment_img);
        TextView comment_name=holder.getView(R.id.comment_name);
        TextView comment_content=holder.getView(R.id.comment_content);
        TextView comment_date=holder.getView(R.id.comment_date);
        if(item.getNAME()!=null){
            comment_name.setText(item.getNAME());
        }else{
            comment_name.setText(item.getName());
        }

        comment_content.setText(item.getContent());
        comment_date.setText(AppUtil.getStrTime(item.getUpdateDate()));

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(mContext));

        Glide.with(mContext)
                .load(ApiStores.IMG_URL_TOP + item.getPhoto())
                .apply(options)
                .into(comment_img);
    }
}