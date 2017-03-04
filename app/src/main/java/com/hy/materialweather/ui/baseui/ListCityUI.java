package com.hy.materialweather.ui.baseui;

import com.hy.materialweather.model.json.HeWeather5;

/**
 * Activity View要实现的接口
 */
public interface ListCityUI extends FinalMessageWhatInt {

    void showMessage(String message);

    void addCity(HeWeather5 heWeather5);

}
