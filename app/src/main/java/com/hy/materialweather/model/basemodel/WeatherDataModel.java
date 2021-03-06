package com.hy.materialweather.model.basemodel;

import com.hy.materialweather.model.WeatherRequestPackage;
import com.hy.materialweather.model.json.CityInfo;
import com.hy.materialweather.model.json.HeWeather5;

import java.util.List;

import okhttp3.Callback;

/**
 * Created by Administrator on 2017/3/3.
 */

public interface WeatherDataModel {

    //异步访问网络获取数据
    void weatherInternetService(WeatherRequestPackage requestPackage, Callback callback);

    //对json字符串做小修改，然后解析
    HeWeather5 parseWeahterJson(String json);

    //从数据库中取出数据
    List<CityInfo> getWeatherFromSQLite(String city);

    //把城市信息存入数据库
    boolean saveWeatherOnSQLite(List<CityInfo> cityInfo);

    //读取记录，用户要查看哪些城市
    List<String> getCitiesOnSQLite();

    //存入记录，用户要查看哪些城市
    void saveCitiesOnSQLite(List<String> list);

    void getKeyFromMyServer(Callback callback);

    //数据模型里设置ApiKey
    void setKey(String key);

    //返回是否已经获取到key了
    boolean isKeyGet();


}
