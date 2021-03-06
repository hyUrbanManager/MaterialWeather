package com.hy.materialweather.ui.baseui;

import com.hy.materialweather.model.json.HeWeather5;

/**
 * Activity View要实现的接口
 */
public interface ListCityUI extends FinalMessageWhatInt {

    void showMessage(String message);

    //显示列表在index位置加入城市
    void addCity(HeWeather5 heWeather5, int list_position);

    //接收到了所有的城市信息
    void onReceiveAll();

    //刷新城市列表
    void flashCitiesList();

}
