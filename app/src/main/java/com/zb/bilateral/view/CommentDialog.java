package com.zb.bilateral.view;

import android.app.Activity;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.R;

/**
 * Created by yaos on 2017/8/2.
 */

public class CommentDialog extends PopupWindow {
    Activity context;
    Handler handler;

    private static final int COMMENT_TEXT=0;

    public CommentDialog(Activity mContext, View parent, Handler handler) {
        this.handler=handler;
        context=mContext;

        View view = View
                .inflate(mContext, R.layout.dialog_comment_item, null);
        // 设置popWindow的显示和消失动画
        setAnimationStyle(R.style.anim_menu_bottombar);

        //添加activity蒙版
        WindowManager.LayoutParams windowLP = context.getWindow().getAttributes();
        windowLP.alpha = 0.5f;
        context.getWindow().setAttributes(windowLP);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        context.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        setHeight(LinearLayout.LayoutParams.FILL_PARENT);
        setFocusable(true);
        setOutsideTouchable(true);

        // 实例化一个ColorDrawable颜色为半透明
        setBackgroundDrawable(new BitmapDrawable());
        setContentView(view);


        if (Build.VERSION.SDK_INT == 24) {
            //在computeGravity()这个方法可以看出，在Android7.0系统上，调用PopupWindow.update( )方法会导致PopupWindow的位置出现在界面顶部
            showAtLocation(parent, Gravity.TOP, 0, 0);
        } else {
            showAtLocation(parent, Gravity.TOP, 0, 0);
            update();
        }
        //实例化
        init(view);
    }

    public void init(View view) {
        TextView comment_input= (TextView) view.findViewById(R.id.comment_input);
        final TextView comment_text= (TextView) view.findViewById(R.id.comment_text);
        RelativeLayout comment_rel= (RelativeLayout) view.findViewById(R.id.comment_rel);

        comment_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        comment_input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text=comment_text.getText().toString();

                if(TextUtils.isEmpty(text)){
                    AppToast.makeToast(context,"请输入评论信息");
                    return;
                }

                Message message=new Message();
                message.obj=text;
                message.what=COMMENT_TEXT;
                handler.sendMessage(message);

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
