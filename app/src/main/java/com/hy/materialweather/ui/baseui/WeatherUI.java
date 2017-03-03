package com.hy.materialweather.ui.baseui;

/**
 * Activity View要实现的接口
 */
public interface WeatherUI {

    //常量标志
    int PASS_STRING = 1;

    int CLOSE_TOAST = 2;

    void showMessage(String message);

}
