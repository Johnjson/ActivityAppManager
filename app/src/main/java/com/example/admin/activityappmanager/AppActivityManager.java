package com.example.admin.activityappmanager;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;

import java.util.Stack;

/**
 * Created by admin on 17/2/22.
 */
public class AppActivityManager {

    private static Stack<Activity> activityStack;
    private static AppActivityManager instance;

    private AppActivityManager() {
    }

    /**
     * 单一实例
     */
    public static AppActivityManager getAppManager() {
        if (instance == null) {
            instance = new AppActivityManager();
        }

        if (activityStack == null) {
            activityStack = new Stack<Activity>();
        }

        return instance;
    }

    /**
     * 添加Activity到堆栈
     */
    public void addActivity(Activity activity) {
        activityStack.add(activity);
    }

    /**
     * 添加Activity到堆栈
     */
    public void removeActivity(Activity activity) {
        if (activityStack == null) {
            return;
        }
        if (null != activity) {
            activityStack.remove(activity);
        }
    }

    /**
     * 获取当前Activity（堆栈中最后一个压入的）
     */
    public Activity currentActivity() {
        Activity activity = activityStack.lastElement();
        return activity;
    }

    /**
     * 结束当前Activity（堆栈中最后一个压入的）
     */
    public void finishActivity() {
        Activity activity = activityStack.lastElement();
        if (activity != null) {
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activity != null) {
            activityStack.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        Activity curractivity = null;
        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                curractivity = activity;
                break;
            }
        }
        finishActivity(curractivity);
    }
    /**
     * 是否包含指定的activity
     */
    public boolean hasActivity(Class<?> cls) {

        for (Activity activity : activityStack) {
            if (activity.getClass().equals(cls)) {
                return true;
            }
        }
        return false;
    }
    /**
     * 结束指定类名的所有Activity
     */
    public void finishActivityOfClass(Class<?> cls) {
        if (cls == null || activityStack == null || (null != activityStack && activityStack.size() == 0)) {
            return;
        }
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            if (activityStack.get(i).getClass().equals(cls)) {
                finishActivity(activityStack.get(i));
            }
        }

    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        for (int i = 0, size = activityStack.size(); i < size; i++) {
            if (null != activityStack.get(i)) {
                activityStack.get(i).finish();
            }
        }
        activityStack.clear();
    }

    /**
     * 结束所有Activity,除了指定的Activity
     */
    public void finishActivitysExceptAssign(Class<?> cls) {
        if (activityStack == null || cls == null) {
            return;
        }
        for (int i = activityStack.size() - 1; i >= 0; i--) {
            Activity activity = activityStack.get(i);
            if (null != activity && !(activity.getClass().equals(cls))) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 退出应用程序
     */
    public void AppExit(Context context) {
        try {
            finishAllActivity();
            ActivityManager activityMgr = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            activityMgr.restartPackage(context.getPackageName());
            System.exit(0);
        } catch (Exception e) {
        }
    }

    public int getActivityCount() {
        return activityStack.size();
    }
}
