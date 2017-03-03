package com.hy.materialweather.presenter;

import android.content.Context;
import android.util.Log;

import com.hy.materialweather.Utils;
import com.hy.materialweather.basemvpcomponent.BasePresenter;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.WeatherDataModelImpl;
import com.hy.materialweather.model.WeatherRequestPackage;
import com.hy.materialweather.model.basemodel.WeatherDataModel;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.ui.baseui.WeatherUI;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WeatherPresenter extends BasePresenter<WeatherUI> {

    WeatherDataModel model;
    WeatherUI viewInterface;

    MVPActivity.MVPHandler handler;

    /**
     * 构造器，把Handler和View实例都获取到
     *
     * @param context
     * @param viewInterface
     * @param handler
     */
    public WeatherPresenter(Context context, WeatherUI viewInterface, MVPActivity.MVPHandler handler) {
        this.model = new WeatherDataModelImpl(context);
        this.viewInterface = viewInterface;
        this.handler = handler;
    }

    public void weatherReportOnInternet(WeatherRequestPackage requestPackage) {
        //网络获取数据，Callback中对UI更新
        model.weatherInternetService(requestPackage, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.sendMessage(handler, WeatherUI.PASS_STRING, e.getMessage());
                Utils.sendEmptyMessage(handler, WeatherUI.CLOSE_TOAST);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //调用Model来解析数据，变成数据对象
                HeWeather5 heWeather5;
                String result;
                try {
                    result = response.body().string();
                    heWeather5 = model.parseWeahterJson(result);
                } catch (Exception e) {
                    Utils.sendMessage(handler, WeatherUI.PASS_STRING, e.getMessage());
                    Utils.sendEmptyMessage(handler, WeatherUI.CLOSE_TOAST);
                    return;
                }

                //把收到的数据保存在本地数据库


                //发送数据对象给UI界面，UI细致化处理界面数据
                StringBuilder sb = new StringBuilder();
                sb.append(heWeather5.toString());
                sb.append("\n\n" + "成功获取到对象");

                //报异常的语句，解决
                sb.append(heWeather5.basic.city);
                sb.append(heWeather5.daily_forecast.get(0).wind);
                sb.append('\n' + heWeather5.suggestion.sport.txt);

                Log.d("-----------------",sb.toString());

                Utils.sendMessage(handler, WeatherUI.PASS_STRING, sb);
                Utils.sendEmptyMessage(handler, WeatherUI.CLOSE_TOAST);
            }
        });
    }

    public void weatherReportOnSQLite(WeatherRequestPackage requestPackage) {
        //获取本地数据，先展示在UI上

    }

}
