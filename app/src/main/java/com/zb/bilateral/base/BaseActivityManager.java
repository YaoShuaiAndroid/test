
package com.zb.bilateral.base;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

import java.util.LinkedList;
import java.util.List;

/**
 * Class Note:
 * use {@link LinkedList} to manage your activity stack
 *
 */
public class BaseActivityManager {
    private static final String TAG = BaseActivityManager.class.getSimpleName();

    private static BaseActivityManager instance = null;
    private static List<Activity> activities = new LinkedList<Activity>();

    private BaseActivityManager() {

    }

    public static BaseActivityManager getInstance() {
        if (null == instance) {
            synchronized (BaseActivityManager.class) {
                if (null == instance) {
                    instance = new BaseActivityManager();
                }
            }
        }
        return instance;
    }

    public int size() {
        return activities.size();
    }

    public synchronized Activity getForwardActivity() {
        return size() > 0 ? activities.get(size() - 1) : null;
    }

    public synchronized void addActivity(Activity activity) {
        activities.add(activity);
    }

    public synchronized void removeActivity(Activity activity) {
        if (activities.contains(activity)) {
            activities.remove(activity);
        }
    }

    public synchronized void clear() {
        for (int i = activities.size() - 1; i > -1; i--) {
            Activity activity = activities.get(i);
            removeActivity(activity);
            activity.finish();
            i = activities.size();
        }
    }

    public synchronized void clearTop() {
        for (int i = activities.size() - 2; i > -1; i--) {
            Activity activity = activities.get(i);
            removeActivity(activity);
            activity.finish();
            i = activities.size() - 1;
        }
    }

    public synchronized boolean lookActivityName(String activityName) {
        for (int i = 0; i <activities.size() ; i++) {
            if(activities.get(i).getLocalClassName().contains(activityName)){
                return true;
            }
        }
        return false;
    }

    /**
     * 判断某个界面是否在前台
     *
             * @param activity 要判断的Activity
     * @return 是否在前台显示
     */
    public static boolean isForeground(Activity activity) {
        return isForeground(activity, activity.getClass().getName());
    }

    /**
     * 判断某个界面是否在前台
     *
     * @param context   Context
     * @param className 界面的类名
     * @return 是否在前台显示
     */
    public static boolean isForeground(Context context, String className) {
        if (context == null || TextUtils.isEmpty(className))
            return false;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(1);
        if (list != null && list.size() > 0) {
            ComponentName cpn = list.get(0).topActivity;
            if (className.equals(cpn.getClassName()))
                return true;
        }
        return false;
    }
}
