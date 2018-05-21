package com.zb.bilateral.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.zb.bilateral.R;

/**
 * Created by yaos on 2017/8/2.
 */

public class PlatformAndMuseumDialog extends PopupWindow {
    Activity context;
    Handler handler;

    private static final int SELECT_PLATFORM=1;
    private static final int SELECT_MUSUEM=2;

    public PlatformAndMuseumDialog(Activity mContext, View parent, Handler handler) {
        this.handler=handler;
        context=mContext;

        View view = View
                .inflate(mContext, R.layout.list_platform_museum_item, null);
        // 设置popWindow的显示和消失动画
        setAnimationStyle(R.style.anim_menu_bottombar);

        //添加activity蒙版
        WindowManager.LayoutParams windowLP = context.getWindow().getAttributes();
        windowLP.alpha = 0.5f;
        context.getWindow().setAttributes(windowLP);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        setFocusable(true);
        setOutsideTouchable(true);
        setClippingEnabled(false);

        // 实例化一个ColorDrawable颜色为半透明
       // ColorDrawable dw = new ColorDrawable(Color.parseColor("#90000000"));
        //setBackgroundDrawable(dw);
        setBackgroundDrawable(new BitmapDrawable());
        setContentView(view);
        if (Build.VERSION.SDK_INT == 24) {
            //在computeGravity()这个方法可以看出，在Android7.0系统上，调用PopupWindow.update( )方法会导致PopupWindow的位置出现在界面顶部
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
        } else {
            showAtLocation(parent, Gravity.BOTTOM, 0, 0);
            update();
        }
        //实例化
        init(view);
    }

    public void init(View view) {
        TextView dialog_dismiss= (TextView) view.findViewById(R.id.dialog_dismiss);
        TextView platform_select= (TextView) view.findViewById(R.id.platform_select);
        TextView museum_select= (TextView) view.findViewById(R.id.museum_select);

        dialog_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        platform_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(SELECT_PLATFORM);
                dismiss();
            }
        });

        museum_select.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(SELECT_MUSUEM);
                dismiss();
            }
        });
    }

    @Override
    public void dismiss() {
        super.dismiss();
        //取消掉activity蒙版
        WindowManager.LayoutParams windowLP = context.getWindow().getAttributes();
        windowLP.alpha = 1f;
        context.getWindow().setAttributes(windowLP);
    }
}
