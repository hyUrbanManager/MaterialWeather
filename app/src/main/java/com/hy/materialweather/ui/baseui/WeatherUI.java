package com.hy.materialweather.ui.baseui;

import com.hy.materialweather.model.json.HeWeather5;

/**
 * Activity View要实现的接口
 */
public interface WeatherUI {

    //常量标志
    int PASS_STRING = 1;

    int CLOSE_TOAST = 2;

    void showMessage(String message);

    void CityOnCard(HeWeather5 heWeather5);

}
