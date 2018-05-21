package com.zb.bilateral.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zb.bilateral.R;

/**
 * Created by bt on 2017/10/17.
 */

public class TimeDialog extends PopupWindow implements DatePicker.OnDateChangedListener,
        TimePicker.OnTimeChangedListener{
    Context context;
    Handler handler;
    private String dateTime;
    String title;
    private String initDateTime;

    private final static int STATUS_TIME=12;

    private TimePicker timepicker;

    public TimeDialog(Context mContext, View parent, String initDateTime, Handler handler, String title1) {
        this.handler=handler;
        context=mContext;
        title=title1;
        this.initDateTime=initDateTime;
        dateTime=initDateTime;
        View view = View
                .inflate(mContext, R.layout.dialog_time_item, null);

        setWidth(LinearLayout.LayoutParams.FILL_PARENT);
        setHeight(LinearLayout.LayoutParams.FILL_PARENT);
        setBackgroundDrawable(new BitmapDrawable());
        setFocusable(true);
        setOutsideTouchable(true);
        setClippingEnabled(false);
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

    public void init(TimePicker datePicker) {
        if(TextUtils.isEmpty(initDateTime)&&initDateTime.contains(":")){
            int hour=Integer.parseInt(initDateTime.substring(0,initDateTime.indexOf(":")));
            int min=Integer.parseInt(initDateTime.substring(initDateTime.indexOf(":")+1,initDateTime.length()));
            datePicker.setCurrentHour(hour);
           datePicker.setCurrentMinute( min);
        }
    }

    public void init(View view) {
        timepicker = (TimePicker) view.findViewById(R.id.timepicker);
        TextView datetime_cancel_text= (TextView) view.findViewById(R.id.datetime_cancel_text);
        TextView dete_text1= (TextView) view.findViewById(R.id.dete_text);
        dete_text1.setText(title);

        datetime_cancel_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        TextView datetime_commit_text= (TextView) view.findViewById(R.id.datetime_commit_text);
        datetime_commit_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message=new Message();
                message.what=STATUS_TIME;
                message.obj=dateTime;
                handler.sendMessage(message);

                dismiss();
            }
        });
        init(timepicker);

        //onTimeChanged(null, 0, 0);
        timepicker.setOnTimeChangedListener(this);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        // 获得日历实例
        dateTime = hourOfDay+":"+minute;
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
    }
}
