package com.zb.bilateral.view;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.R;

/**
 * Created by yaos on 2017/10/17.
 */

public class HallNoDialog extends PopupWindow {
    Activity context;
    Handler handler;

    private final static int EDIT_NO= 4;

    public HallNoDialog(Activity mContext, final View parent, Handler handler ) {
        this.handler = handler;
        context = mContext;

        final View view =LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_hall_no_item, null);

        setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        setHeight(LinearLayout.LayoutParams.FILL_PARENT);
        BitmapDrawable bitmapDrawable=new BitmapDrawable(String.valueOf(Color.parseColor("#90000000")));
        setBackgroundDrawable(bitmapDrawable);
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
        final EditText hall_no_text= (EditText) view.findViewById(R.id.hall_no_text);

        dialog_cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        next_commit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=hall_no_text.getText().toString();

                if(TextUtils.isEmpty(text)){
                    AppToast.makeToast(context,"请输入编号");
                }

                Message message=new Message();
                message.what=EDIT_NO;
                message.obj=text;
                handler.sendMessage(message);

                dismiss();
            }
        });

    }

}
