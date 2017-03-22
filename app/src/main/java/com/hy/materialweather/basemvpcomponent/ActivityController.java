package com.hy.materialweather.basemvpcomponent;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Activity管理者
 * 创建一个自己的Activity任务栈
 * 方便结束掉所有Activity
 *
 * @author hy
 *
 */
public class ActivityController {

    public static final List<Activity> list = new ArrayList<>();

    /**
     * 把某个Activity加入集合管理
     *
     * @param activity
     */
    public static void addActivity(Activity activity) {
        list.add(activity);
    }

    /**
     * 把某个Activity出集合管理
     *
     * @param activity
     */
    public static void removeActivity(Activity activity) {
        list.remove(activity);
    }

    /**
     * 把所有Activity都结束掉
     */
    public static void finishAll() {
        for(Activity activity : list) {
            if(activity.isFinishing()) {
                activity.finish();
            }
        }
    }

}
