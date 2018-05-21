package com.zb.bilateral.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.adapter.ListBaseAdapter;
import com.example.mycommon.view.GlideRoundImage;
import com.example.mycommon.view.SuperViewHolder;
import com.zb.bilateral.R;
import com.zb.bilateral.model.BaseModel;
import com.zb.bilateral.model.CollectModel;
import com.zb.bilateral.retrofit.ApiStores;


public class CollectAdapter extends ListBaseAdapter<CollectModel> {
    Context context;

    public CollectAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_collect_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final CollectModel item = mDataList.get(position);

        ImageView collect_img=holder.getView(R.id.collect_img);
        TextView collect_name=holder.getView(R.id.collect_name);
        TextView collect_year=holder.getView(R.id.collect_year);

       /* LinearLayout.LayoutParams linearParams =(LinearLayout.LayoutParams) collect_img.getLayoutParams();
        linearParams.height = 100+30*(position+1);
        collect_img.setLayoutParams(linearParams);*/

        collect_name.setText(item.getName());
        collect_year.setText(item.getYears());


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