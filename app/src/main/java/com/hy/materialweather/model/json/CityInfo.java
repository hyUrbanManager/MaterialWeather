package com.hy.materialweather.model.json;

import java.util.List;

/**
 * 一个城市的所有信息，用来保存到本地和取出，接口的集合接口获取到的数据
 */
public class CityInfo {

    public final List<HeWeather5> HeWeather5;

    public CityInfo(List<HeWeather5> heweather5) {
        HeWeather5 = heweather5;
    }

}
