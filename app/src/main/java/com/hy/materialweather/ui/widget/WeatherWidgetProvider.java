package com.hy.materialweather.ui.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.hy.materialweather.R;
import com.hy.materialweather.Utils;
import com.hy.materialweather.model.WeatherDataModelImpl;
import com.hy.materialweather.model.basemodel.WeatherDataModel;
import com.hy.materialweather.ui.activity.MainActivity;

/**
 * 桌面小部件，自动更新天气
 */
public class WeatherWidgetProvider extends AppWidgetProvider {
    public static final String TAG = WeatherWidgetProvider.class.getName() + " ";

    public static final String CLICK_ACTION = "com.hy.materialweather.action.click";

    private WeatherDataModel model;

    public WeatherWidgetProvider() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Utils.d(TAG + "小部件onReceive方法，action为 " + intent.getAction());
        super.onReceive(context, intent);

        if (intent.getAction().equals(CLICK_ACTION)) {
            Utils.d(TAG + "检测到action为点击");
            //打开住Activity
            Intent intent1 = new Intent(context, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 1, intent1, 0);
            RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_normal_style);
            remoteViews.setOnClickPendingIntent(R.id.widgetAll, pendingIntent);
        }

    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        Utils.d(TAG + "桌面小部件onUpdate方法执行");
        RemoteViews remoteViews = new RemoteViews(context.getPackageName(), R.layout.widget_normal_style);
        //创建一个广播
        Intent intent = new Intent(CLICK_ACTION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        remoteViews.setOnClickPendingIntent(R.id.widgetAll, pendingIntent);
        //如果添加了多个实例要经过以下处理
        for (int i = 0; i < appWidgetIds.length; i++) {
            appWidgetManager.updateAppWidget(appWidgetIds[i], remoteViews);
        }
    }


    @Override
    public void onEnabled(Context context) {
        super.onEnabled(context);
        Utils.d(TAG + "小部件被添加了，桌面上存在Material 天气小部件");
        initModel(context);
    }

    /**
     * 初始化Model对象
     *
     * @param context
     */
    private void initModel(Context context) {
        if (model == null) {
            model = new WeatherDataModelImpl(context);
        }
    }

    @Override
    public void onDisabled(Context context) {
        super.onDisabled(context);
        Utils.d(TAG + "小部件被删除完了");
        model = null;

    }


}
