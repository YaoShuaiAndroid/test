package com.zb.bilateral.adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.example.mycommon.adapter.ListBaseAdapter;
import com.example.mycommon.view.GlideRoundImage;
import com.example.mycommon.view.SuperViewHolder;
import com.zb.bilateral.R;
import com.zb.bilateral.model.AppointmentModel;
import com.zb.bilateral.model.MessageModel;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;


public class MessageAdapter extends ListBaseAdapter<MessageModel> {
    Context context;

    public MessageAdapter(Context context) {
        super(context);
        this.context = context;
    }

    @Override
    public int getLayoutId() {
        return R.layout.list_message_item;
    }

    @Override
    public void onBindItemHolder(SuperViewHolder holder, int position) {
        final MessageModel item = mDataList.get(position);

        ImageView message_img=holder.getView(R.id.message_img);
        TextView message_text=holder.getView(R.id.message_text);
        if(item.getIsRead().equals("1")){
            message_img.setBackgroundResource(R.mipmap.main_dot);
        }else{
            message_img.setBackgroundResource(R.mipmap.gray_dot);
        }
        message_text.setText(item.getTitle());


    }
}