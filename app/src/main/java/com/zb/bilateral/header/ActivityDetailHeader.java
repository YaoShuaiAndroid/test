package com.zb.bilateral.header;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.text.Html;
import android.util.AttributeSet;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycommon.util.AppToast;
import com.example.mycommon.view.GlideRoundImage;
import com.zb.bilateral.R;
import com.zb.bilateral.activity.LoginActivity;
import com.zb.bilateral.model.ActivityModel;
import com.zb.bilateral.retrofit.ApiStores;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.view.CommentDialog;
import com.zb.bilateral.view.PromptLoginDialog;


/**
 * Created by yaos on 2017/5/3.
 */
public class ActivityDetailHeader extends RelativeLayout{
    View view;
    Intent intent;
    Context context;
    Handler handler;

    private static final int LIKE=1;
    private static final int SIGN_CANCEL=2;
    private static final int SIGN_COMMIT=3;
    private static final int COLLECT=5;

    private ImageView activity_detail_img,activity_detail_like_img,activity_isColl_img;

    private TextView activity_detail_title,activity_date,activity_address,activity_num,
            activity_detail_num,activity_detail_sign_up;
    WebView activity_detail_content;

    private LinearLayout activity_detail_like_lin;

    private RelativeLayout activity_detail_message_rel,activity_collect_rel;

    public ActivityDetailHeader(Context context,Handler handler) {
        super(context);
        this.context=context;
        this.handler=handler;
        init(context);
    }

    public ActivityDetailHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context=context;
        init(context);
    }

    public ActivityDetailHeader(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context=context;
        init(context);
    }

    public void init(final Context context) {
        view=inflate(context, R.layout.header_activity_detail_item, this);
        activity_detail_img= (ImageView) view.findViewById(R.id.activity_detail_img);
        activity_detail_title= (TextView) view.findViewById(R.id.activity_detail_title);
        activity_date= (TextView) view.findViewById(R.id.activity_date);
        activity_address= (TextView) view.findViewById(R.id.activity_address);
        activity_num= (TextView) view.findViewById(R.id.activity_num);
        activity_detail_content= (WebView) view.findViewById(R.id.activity_detail_content);
        activity_detail_num= (TextView) view.findViewById(R.id.activity_detail_num);
        activity_detail_like_img= (ImageView) view.findViewById(R.id.activity_detail_like_img);
        activity_detail_sign_up= (TextView) view.findViewById(R.id.activity_detail_sign_up);
        activity_detail_like_lin= (LinearLayout) view.findViewById(R.id.activity_detail_like_lin);
        activity_detail_message_rel= (RelativeLayout) view.findViewById(R.id.activity_detail_message_rel);
        activity_collect_rel= (RelativeLayout) view.findViewById(R.id.activity_collect_rel);
        activity_isColl_img= (ImageView) view.findViewById(R.id.activity_isColl_img);

        activity_detail_like_lin.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(LIKE);
                activity_detail_like_lin.setClickable(false);
            }
        });

        activity_detail_sign_up.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if("取消报名".equals(activity_detail_sign_up.getText().toString())){
                    handler.sendEmptyMessage(SIGN_CANCEL);
                    activity_detail_sign_up.setClickable(false);
                }else{
                    handler.sendEmptyMessage(SIGN_COMMIT);
                }
            }
        });

        activity_detail_message_rel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppUtil.getToken(context).equals(ApiStores.TOURIST_TOKEN)){
                    new PromptLoginDialog((Activity) context,activity_detail_img);
                    return;
                }

                new CommentDialog((Activity) context,activity_detail_message_rel,handler);
            }
        });

        activity_collect_rel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(COLLECT);
                activity_collect_rel.setClickable(false);
            }
        });
    }

    public void setData(ActivityModel activityModel){
        activity_detail_title.setText(activityModel.getTitle());
        activity_date.setText("活动时间："+(AppUtil.getStrTime(activityModel.getStartTime()))+"-"
                +AppUtil.getStrTime(activityModel.getEndTime()));
        activity_address.setText("活动地址："+activityModel.getAddress());
        activity_num.setText("人数："+activityModel.getNumLimit());
        activity_detail_content.loadDataWithBaseURL(ApiStores.IMG_URL_TOP,  AppUtil.css(activityModel.getIntroduce()),
                "text/html", "utf-8", null);
        activity_detail_num.setText(""+activityModel.getLikeCount());
        if(activityModel.getIsLike()!=null&& "1".equals(activityModel.getIsLike())){
            activity_detail_like_img.setBackgroundResource(R.mipmap.cultrue_dianzan);
        }else{
            activity_detail_like_img.setBackgroundResource(R.mipmap.like_false);
        }

        if("1".equals(activityModel.getIsColl())){
            activity_isColl_img.setBackgroundResource(R.mipmap.cultrue_collect_true);
        }else{
            activity_isColl_img.setBackgroundResource(R.mipmap.collect_gray);
        }

        RequestOptions options = new RequestOptions()
                .centerCrop()
                .placeholder(R.mipmap.no_img)
                .transform(new GlideRoundImage(context));

        Glide.with(context)
                .load(ApiStores.IMG_URL_TOP + activityModel.getCover())
                .apply(options)
                .into(activity_detail_img);

        if(activityModel.getStatus()!=null&& "0".equals(activityModel.getStatus())){
            activity_detail_sign_up.setVisibility(View.VISIBLE);
            if(activityModel.getIsSignIn()!=null&& "1".equals(activityModel.getIsSignIn())){
                activity_detail_sign_up.setText("取消报名");
            }else{
                activity_detail_sign_up.setText("立即报名");
            }
        }else{
            activity_detail_sign_up.setVisibility(View.GONE);
        }
    }

    public void setCollectAndLike(ActivityModel activityModel){
        if(activityModel.getIsSignIn()!=null&& "1".equals(activityModel.getIsSignIn())){
            activity_detail_sign_up.setText("取消报名");
        }else{
            activity_detail_sign_up.setText("立即报名");
        }

        if("1".equals(activityModel.getIsColl())){
            activity_isColl_img.setBackgroundResource(R.mipmap.cultrue_collect_true);
        }else{
            activity_isColl_img.setBackgroundResource(R.mipmap.collect_gray);
        }

        if("1".equals(activityModel.getIsLike())){
            activity_detail_like_img.setBackgroundResource(R.mipmap.cultrue_dianzan);
        }else{
            activity_detail_like_img.setBackgroundResource(R.mipmap.like_false);
        }
        activity_detail_num.setText(""+activityModel.getLikeCount());

        activity_detail_like_lin.setClickable(true);
        activity_detail_sign_up.setClickable(true);
        activity_collect_rel.setClickable(true);
    }
}