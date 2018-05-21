package com.zb.bilateral.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.zb.bilateral.Constants;
import com.zb.bilateral.R;

/**
 * Created by yaos on 2017/10/17.
 */

public class CommitDialog extends PopupWindow {
    Activity context;
    Handler handler;
    String title;

    public CommitDialog(Activity mContext, final View parent, Handler handler, String title) {
        this.handler = handler;
        context = mContext;
        this.title=title;

        final View view =LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_commit_item, null);

        //添加activity蒙版
        WindowManager.LayoutParams windowLP = context.getWindow().getAttributes();
        windowLP.alpha = 0.5f;
        context.getWindow().setAttributes(windowLP);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        setHeight(LinearLayout.LayoutParams.FILL_PARENT);
        setFocusable(true);
        setSoftInputMode(PopupWindow.INPUT_METHOD_NEEDED);
        setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        setContentView(view);

        if (Build.VERSION.SDK_INT == 24) {
            //在computeGravity()这个方法可以看出，在Android7.0系统上，调用PopupWindow.update( )方法会导致PopupWindow的位置出现在界面顶部
            showAtLocation(parent, Gravity.CENTER, 0, 0);
        } else {
            showAtLocation(parent, Gravity.CENTER, 0, 0);
            update();
        }
        //实例化
        init(view);
    }



    public void init(View view) {
        TextView dialog_cancel_text= (TextView) view.findViewById(R.id.dialog_cancel_text);
        TextView next_commit_text= (TextView) view.findViewById(R.id.next_commit_text);
        TextView commit_text= (TextView) view.findViewById(R.id.commit_text);

        commit_text.setText(title);

        dialog_cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        next_commit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.sendEmptyMessage(Constants.COMMIT_DIALOG);

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
