package com.hy.materialweather.ui.baseui;

import com.hy.materialweather.model.json.HeWeather5;

/**
 * 详情界面Activity实现的抽象接口
 */
public interface CityAllInfoUI extends FinalMessageWhatInt{

    void infoOnCard(HeWeather5 heWeather5);

}
