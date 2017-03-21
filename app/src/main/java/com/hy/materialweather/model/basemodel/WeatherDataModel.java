package com.hy.materialweather.model.basemodel;

import com.hy.materialweather.model.WeatherRequestPackage;
import com.hy.materialweather.model.json.CityInfo;
import com.hy.materialweather.model.json.HeWeather5;

import java.io.IOException;
import java.util.List;

import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/3/3.
 */

public interface WeatherDataModel {

    //异步访问网络获取数据
    void weatherInternetService(WeatherRequestPackage requestPackage, Callback callback);

    //对json字符串做小修改，然后解析
    HeWeather5 parseWeahterJson(String json);

    //从数据库中取出数据
    @Deprecated
    List<CityInfo> getWeatherFromSQLite(String city);

    //把城市信息存入数据库
    @Deprecated
    boolean saveWeatherOnSQLite(List<CityInfo> cityInfo);

    //读取记录，用户要查看哪些城市
    @Deprecated
    List<String> getCitiesOnSQLite();

    //存入记录，用户要查看哪些城市
    @Deprecated
    void saveCitiesOnSQLite(List<String> list);

    //同步方法获取Key
    Response getKeyFromMyServer() throws IOException;

    //数据模型里设置ApiKey
    void setKey(String key);

    //返回是否已经获取到key了
    boolean isKeyGet();

    //读取显示的风格
    @Deprecated
    int getStyleOnSQLite();

    //保存显示的风格
    @Deprecated
    void saveStyleOnSQLite(int style);

    //把要保存的数据保存进SQLite
    void saveOnSQLite();

    //把保存的数据保从SQLite取出
    void getOnSQLite();

}
