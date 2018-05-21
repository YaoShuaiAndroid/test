package com.example.mycommon.util;

/**
 * Created by pengl on 2015/12/24.
 */

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import java.lang.reflect.Field;
import java.lang.reflect.Method;


/**
 * 状态栏兼容处理类
 */
public class StatusBarCompat {
    private static final int INVALID_VAL = -1;
    private static final int INVALID_PERSON = 2;
    private static final int MUSEUM = 3;
    private static final int IMAGE_DETAIL = 4;
    private static final int COLOR_DEFAULT = Color.parseColor("#867A6E");
    private static final int COLOR_PERSON = Color.parseColor("#867A6E");
    private static final int COLOR_MESEUM = Color.parseColor("#625a5c");
    private static final int COLOR_IMAGEDETAIL = Color.parseColor("#3c3b40");

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public static void compat(Activity activity, int statusColor) {
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (statusColor == INVALID_PERSON) {
                activity.getWindow().setStatusBarColor(COLOR_PERSON);
            }else if (statusColor == MUSEUM) {
                activity.getWindow().setStatusBarColor(COLOR_MESEUM);
            }else if (statusColor == IMAGE_DETAIL) {
                activity.getWindow().setStatusBarColor(COLOR_IMAGEDETAIL);
            }else{
                activity.getWindow().setStatusBarColor(COLOR_DEFAULT);
            }
            return;
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            int color = COLOR_DEFAULT;
            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
            if (statusColor != INVALID_PERSON) {
                color = statusColor;
            }else{
                color = statusColor;
            }
            View statusBarView = new View(activity);
            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                    getStatusBarHeight(activity));
            statusBarView.setBackgroundColor(color);
            contentView.addView(statusBarView, lp);
        }

    }


    public static void compat(Activity activity) {
        compat(activity, COLOR_DEFAULT);
    }


    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    // 设置状态栏透明与字体颜色
    public static void setStatusBarTranslucent(Activity acitivty, boolean isLightStatusBar) {
        Window window = acitivty.getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            window.addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        /*if (isXiaomi()) {
            setXiaomiStatusBar(window, isLightStatusBar);
        } else if (isMeizu()) {
            setMeizuStatusBar(window, isLightStatusBar);
        }*/
    }

    // 是否是小米手机
    public static boolean isXiaomi() {
        return "Xiaomi".equals(Build.MANUFACTURER);
    }

    // 设置小米状态栏
    public static void setXiaomiStatusBar(Window window, boolean isLightStatusBar) {
        Class<? extends Window> clazz = window.getClass();
        try {
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            int darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(window, isLightStatusBar ? darkModeFlag : 0, darkModeFlag);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 是否是魅族手机
    public static boolean isMeizu() {
        try {
            Method method = Build.class.getMethod("hasSmartBar");
            return method != null;
        } catch (NoSuchMethodException e) {
        }
        return false;
    }

    // 设置魅族状态栏
    public static void setMeizuStatusBar(Window window, boolean isLightStatusBar) {
        WindowManager.LayoutParams params = window.getAttributes();
        try {
            Field darkFlag = WindowManager.LayoutParams.class.getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class.getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(params);
            if (isLightStatusBar) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(params, value);
            window.setAttributes(params);
            darkFlag.setAccessible(false);
            meizuFlags.setAccessible(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
//private static final int INVALID_VAL = -1;
//    private static final int COLOR_DEFAULT = Color.parseColor("#20000000");
//    private static final int COLOR_TRANSPARENT = Color.parseColor("#00000000");
//
//    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
//    public static void compat(Activity activity, int statusColor)
//    {
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
//        {
//            if (statusColor != INVALID_VAL)
//            {
//                //activity.getWindow().setStatusBarColor(COLOR_DEFAULT);
//
//                activity.getWindow().setStatusBarColor(statusColor);
//
//            }
//            return;
//        }
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
//        {
//            int color = COLOR_DEFAULT;
//            ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
//            if (statusColor != INVALID_VAL)
//            {
//                color = statusColor;
//            }
//            View statusBarView = new View(activity);
//            ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                    getStatusBarHeight(activity));
//            statusBarView.setBackgroundColor(color);
//            contentView.addView(statusBarView, lp);
//        }
//
//    }
//
//
//    public static void compat(Activity activity)
//    {
//        compat(activity,COLOR_DEFAULT);
//    }
//
//
//    public static int getStatusBarHeight(Context context)
//    {
//        int result = 0;
//        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
//        if (resourceId > 0)
//        {
//            result = context.getResources().getDimensionPixelSize(resourceId);
//        }
//        return result;
//    }
//
//    public static void fillStatusBar(Activity activity,int statusColor){
//        ViewGroup contentView = (ViewGroup) activity.findViewById(android.R.id.content);
//        View statusBarView = new View(activity);
//        ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
//                getStatusBarHeight(activity));
//        statusBarView.setBackgroundColor(statusColor);
//        contentView.addView(statusBarView, lp);
//    }
}
