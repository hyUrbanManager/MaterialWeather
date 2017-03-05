package com.hy.materialweather.presenter;

import android.content.Context;
import android.util.Log;

import com.hy.materialweather.Utils;
import com.hy.materialweather.basemvpcomponent.BasePresenter;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.HeWeather5Map;
import com.hy.materialweather.model.WeatherDataModelImpl;
import com.hy.materialweather.model.WeatherRequestPackage;
import com.hy.materialweather.model.basemodel.WeatherDataModel;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.ui.baseui.ListCityUI;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WeatherCityPresenter extends BasePresenter<ListCityUI> {

    WeatherDataModel model;
    ListCityUI viewInterface;

    MVPActivity.MVPHandler handler;

    /**
     * 构造器，把Handler和View实例都获取到
     *
     * @param context
     * @param viewInterface
     * @param handler
     */
    public WeatherCityPresenter(Context context, ListCityUI viewInterface, MVPActivity.MVPHandler handler) {
        this.model = new WeatherDataModelImpl(context);
        this.viewInterface = viewInterface;
        this.handler = handler;
    }

    public void weatherReportOnInternet(WeatherRequestPackage requestPackage) {
        //网络获取数据，Callback中对UI更新
        model.weatherInternetService(requestPackage, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.sendMessage(handler, ListCityUI.PASS_STRING, e.getMessage());
                Utils.sendEmptyMessage(handler, ListCityUI.CLOSE_TOAST);
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
                    Utils.sendMessage(handler, ListCityUI.PASS_STRING, e.getMessage());
                    Utils.sendEmptyMessage(handler, ListCityUI.CLOSE_TOAST);
                    return;
                }

                //把数据保存到全局Map
                HeWeather5Map.heWeather5HashMap.put(heWeather5.basic.city, heWeather5);

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

                Utils.sendEmptyMessage(handler, ListCityUI.CLOSE_TOAST);

                viewInterface.addCity(heWeather5);
            }
        });
    }

    /**
     * 获取记录，用户要查看的城市
     * @return
     */
    public List<String> getCitiesOnSQLite() {
        return model.getCitiesOnSQLite();
    }

    /**
     * 把list转换成json字符串传入
     * @param list
     */
    public void saveCitiesOnSQLite(List<String> list) {
        model.saveCitiesOnSQLite(list);
    }

}
