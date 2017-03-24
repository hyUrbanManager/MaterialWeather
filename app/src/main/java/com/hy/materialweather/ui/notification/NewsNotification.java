package com.hy.materialweather.ui.notification;

import android.app.Notification;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.widget.RemoteViews;

import com.hy.materialweather.R;

/**
 * 提示主体
 */
public class NewsNotification {

    /**
     * 创建通知栏通知
     *
     * @param context
     * @return
     */
    public static Notification getNotification(Context context) {

        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.activity_list_city);


        Notification notification = new Notification.Builder(context)
                .setContentTitle("")
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.materialweather)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.materialweather))
                .setCustomContentView(remoteViews)
                .build();



        return notification;
    }

}
