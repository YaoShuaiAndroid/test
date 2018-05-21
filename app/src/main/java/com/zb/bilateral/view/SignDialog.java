package com.zb.bilateral.view;

import android.app.Activity;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mycommon.util.AppToast;
import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.R;
import com.zb.bilateral.model.SignModel;

/**
 * Created by yaos on 2017/10/17.
 */

public class SignDialog extends PopupWindow {
    Activity context;
    Handler handler;
    String title;

    private static final int SIGN_COMMIT=4;

    public SignDialog(Activity mContext, final View parent, Handler handler, String title) {
        this.handler = handler;
        context = mContext;
        this.title=title;

        final View view =LayoutInflater.from(mContext)
                .inflate(R.layout.dialog_sign_item, null);

        //添加activity蒙版
        WindowManager.LayoutParams windowLP = context.getWindow().getAttributes();
        windowLP.alpha = 0.5f;
        context.getWindow().setAttributes(windowLP);
        context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

        setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
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
        RelativeLayout sign_cancel_rel= (RelativeLayout) view.findViewById(R.id.sign_cancel_rel);
        TextView sign_activity_name= (TextView) view.findViewById(R.id.sign_activity_name);
        final TextView sign_name= (TextView) view.findViewById(R.id.sign_name);
        final TextView sign_phone= (TextView) view.findViewById(R.id.sign_phone);
        final TextView sign_num= (TextView) view.findViewById(R.id.sign_num);
        final TextView sign_special= (TextView) view.findViewById(R.id.sign_special);
        final CheckBox sign_check_box= (CheckBox) view.findViewById(R.id.sign_check_box);
        TextView sign_commit= (TextView) view.findViewById(R.id.sign_commit);

        sign_activity_name.setText(title);

        sign_cancel_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        sign_commit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name=sign_name.getText().toString();
                String phone=sign_phone.getText().toString();
                String number=sign_num.getText().toString();
                String other=sign_special.getText().toString();

                if(TextUtils.isEmpty(name)){
                    AppToast.makeToast(context,"请输入姓名");
                    return;
                }

                if(!AppUtil.isMobileNO(phone)){
                    AppToast.makeToast(context,"请输入正确的手机号码");
                    return;
                }

                if(TextUtils.isEmpty(number)){
                    AppToast.makeToast(context,"请输入参与人数");
                    return;
                }

                SignModel signModel=new SignModel();
                signModel.setName(name);
                signModel.setNumber(number);
                signModel.setOther(other);
                signModel.setPhone(phone);
                if(sign_check_box.isChecked()){
                    signModel.setIs_self("1");
                }else{
                    signModel.setIs_self("0");
                }

                Message message=new Message();
                message.obj=signModel;
                message.what=SIGN_COMMIT;
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
