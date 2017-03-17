package com.hy.materialweather.presenter;

import android.content.Context;

import com.hy.materialweather.Utils;
import com.hy.materialweather.basemvpcomponent.BasePresenter;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.HeWeather5Map;
import com.hy.materialweather.model.WeatherDataModelImpl;
import com.hy.materialweather.model.WeatherRequestPackage;
import com.hy.materialweather.model.basemodel.WeatherDataModel;
import com.hy.materialweather.model.json.Basic;
import com.hy.materialweather.model.json.CondOne;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.model.json.Now;
import com.hy.materialweather.ui.baseui.ListCityUI;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WeatherCityPresenter extends BasePresenter<ListCityUI> {

    protected WeatherDataModel model;
    protected ListCityUI viewInterface;

    protected MVPActivity.MVPHandler handler;

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

    /**
     * 从网络中获取数据
     *
     * @param requestPackage
     */
    public void weatherReportOnInternet(final WeatherRequestPackage requestPackage) {
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

                //Key不正确
                if (heWeather5.status.equals("invalid key")) {
                    //和MainActivity的Handle耦合，相应比一些不设置为Null
                    HeWeather5 h = new HeWeather5(null, null,
                            new Basic("封闭KEY", null, null, null, null, null, null),
                            null, null,
                            new Now(new CondOne(null, null), null, null, null, null, null, null, null), null, null);
                    viewInterface.addCity(h, requestPackage.list_position);
                    return;

                } else {

                    //把数据保存到全局Map，当前获取到的实时信息
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

                    Utils.sendEmptyMessage(handler, ListCityUI.CLOSE_TOAST);

                    viewInterface.addCity(heWeather5, requestPackage.list_position);
                }
            }
        });
    }

    /**
     * 连接个人服务器获取我的key
     *
     * @return
     */
    public void getKeyFromMyServer() {
        model.getKeyFromMyServer(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.sendMessage(handler, ListCityUI.PASS_STRING, e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                if (result.equals("password error")) {
                    //密码错误
                    Utils.sendMessage(handler, ListCityUI.PASS_STRING, "服务器获取密码失败 " + result);
                    Utils.d("获取密码失败");
                } else {
                    model.setKey(result);
                    Utils.d("获取密码成功 " + result);
                }
            }
        });
    }

    /**
     * Api key有没有获取到
     *
     * @return
     */
    public boolean isKeyGet() {
        return model.isKeyGet();
    }

    /**
     * 获取记录，用户要查看的城市
     *
     * @return
     */
    public List<String> getCitiesOnSQLite() {
        return model.getCitiesOnSQLite();
    }

    /**
     * 把list转换成json字符串传入
     *
     * @param list
     */
    public void saveCitiesOnSQLite(List<String> list) {
        model.saveCitiesOnSQLite(list);
    }

}
