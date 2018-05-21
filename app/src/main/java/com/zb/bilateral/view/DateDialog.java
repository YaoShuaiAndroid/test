package com.zb.bilateral.view;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.Gravity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.TimePicker;

import com.zb.bilateral.util.AppUtil;
import com.zb.bilateral.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by bt on 2017/10/17.
 */

public class DateDialog extends PopupWindow implements DatePicker.OnDateChangedListener,
        TimePicker.OnTimeChangedListener{
    Context context;
    Handler handler;
    private String dateTime;
    String title;
    private String initDateTime;

    private final static int STATUS_DATE=11;

    private DatePicker datePicker;

    public DateDialog(Context mContext, View parent, String initDateTime, Handler handler, String title1) {
        this.handler=handler;
        context=mContext;
        title=title1;
        this.initDateTime=initDateTime;
        dateTime=initDateTime;

        View view = View
                .inflate(mContext, R.layout.common_datetime, null);

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



    public void init(DatePicker datePicker) {
        Calendar calendar = Calendar.getInstance();
        if (!(null == initDateTime || "".equals(initDateTime))) {
            calendar = this.getCalendarByInintData(initDateTime);
        } else {
            initDateTime = calendar.get(Calendar.YEAR) + "-"
                    + calendar.get(Calendar.MONTH) + "-"
                    + calendar.get(Calendar.DAY_OF_MONTH) + "- "
                    + calendar.get(Calendar.HOUR_OF_DAY) + ":"
                    + calendar.get(Calendar.MINUTE);
        }

        datePicker.init(calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH), this);
    }

    public void init(View view) {
        datePicker = (DatePicker) view.findViewById(R.id.datepicker);
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
                message.what=STATUS_DATE;
                message.obj=dateTime;
                handler.sendMessage(message);

                dismiss();
            }
        });
        init(datePicker);

        //onDateChanged(null, 0, 0, 0);
    }

    @Override
    public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
        onDateChanged(null, 0, 0, 0);
    }

    @Override
    public void onDateChanged(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
        // 获得日历实例
        Calendar calendar = Calendar.getInstance();

        calendar.set(datePicker.getYear(), datePicker.getMonth(),
                datePicker.getDayOfMonth());
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

        dateTime = sdf.format(calendar.getTime());
        //ad.setTitle(dateTime);
    }

    /**
     * 实现将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒,并赋值给calendar
     *
     * @param initDateTime
     *            初始日期时间值 字符串型
     * @return Calendar
     */
    private Calendar getCalendarByInintData(String initDateTime) {
        Calendar calendar = Calendar.getInstance();
        // 将初始日期时间2012年07月02日 16:45 拆分成年 月 日 时 分 秒
        String date =initDateTime; // 日期
        if(!date.contains("-")){
            date= AppUtil.getStrTime();
        }
        String yearStr = spliteString(date, "-", "index", "front"); // 年份
        String monthAndDay = spliteString(date, "-", "index", "back"); // 月日

        String monthStr = spliteString(monthAndDay, "-", "index", "front"); // 月
        String dayStr = spliteString(monthAndDay, "-", "index", "back"); // 日
        int currentYear = Integer.valueOf(yearStr.trim()).intValue();
        int currentMonth = Integer.valueOf(monthStr.trim()).intValue() - 1;
        int currentDay = Integer.valueOf(dayStr.trim()).intValue();

        calendar.set(currentYear, currentMonth, currentDay);
        return calendar;
    }

    /**
     * 截取子串
     *
     * @param srcStr
     *            源串
     * @param pattern
     *            匹配模式
     * @param indexOrLast
     * @param frontOrBack
     * @return
     */
    public static String spliteString(String srcStr, String pattern,
                                      String indexOrLast, String frontOrBack) {
        String result = "";
        int loc = -1;
        if ("index".equalsIgnoreCase(indexOrLast)) {
            loc = srcStr.indexOf(pattern); // 取得字符串第一次出现的位置
        } else {
            loc = srcStr.lastIndexOf(pattern); // 最后一个匹配串的位置
        }
        if ("front".equalsIgnoreCase(frontOrBack)) {
            if (loc != -1) {
                result = srcStr.substring(0, loc); // 截取子串
            }
        } else {
            if (loc != -1) {
                result = srcStr.substring(loc + 1, srcStr.length()); // 截取子串
            }
        }
        return result;
    }
}
