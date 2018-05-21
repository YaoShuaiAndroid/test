package com.zb.bilateral.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycommon.adapter.CommonAdapter;
import com.zb.bilateral.Constants;
import com.zb.bilateral.R;
import com.zb.bilateral.model.FloorModel;
import com.zb.bilateral.model.ShowRoomModel;

import java.util.List;


/**
 * Created by yaos on 2017/10/17.
 */

public class SetMoreDialog extends PopupWindow {
    private Activity context;

    private Handler handler;

    private boolean isShowShare;

    LinearLayout more_main_activity, more_museum_activity, more_show_activity, more_person_activity,
            share_activity;

    public SetMoreDialog(Activity mContext, final View parent, Handler handler,boolean isShowShare) {
        this.handler = handler;
        context = mContext;
        this.isShowShare=isShowShare;

        final View view = LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_set_more_item, null);

        // 设置popWindow的显示和消失动画
        setAnimationStyle(R.style.anim_menu_topbar);

        setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(view);


        if (Build.VERSION.SDK_INT == 24) {
            //在computeGravity()这个方法可以看出，在Android7.0系统上，调用PopupWindow.update( )方法会导致PopupWindow的位置出现在界面顶部
            showAtLocation(parent, Gravity.RIGHT|Gravity.TOP, 0,0);
        } else {
            showAtLocation(parent, Gravity.RIGHT|Gravity.TOP, 0,0);
            update();
        }
        //实例化
        init(view);
    }

    public void init(View view) {
        more_main_activity = (LinearLayout) view.findViewById(R.id.more_main_activity);
        more_museum_activity = (LinearLayout) view.findViewById(R.id.more_museum_activity);
        more_show_activity = (LinearLayout) view.findViewById(R.id.more_show_activity);
        more_person_activity = (LinearLayout) view.findViewById(R.id.more_person_activity);
        share_activity= (LinearLayout) view.findViewById(R.id.share_activity);
        if(isShowShare){
            share_activity.setVisibility(View.VISIBLE);
        }

        more_main_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(Constants.MAIN_ACTIVITY);
                dismiss();
            }
        });

        more_museum_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(Constants.MUSEUM_ACTIVITY);
                dismiss();
            }
        });

        more_show_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(Constants.SHOW_ACTIVITY);
                dismiss();
            }
        });
        more_person_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(Constants.PERSON_ACTIVITY);
                dismiss();
            }
        });

        share_activity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(Constants.SHARE_ACTIVITY);
                dismiss();
            }
        });
    }
}
