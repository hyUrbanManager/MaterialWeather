package com.hy.materialweather.model;

import com.hy.materialweather.model.json.HeWeather5;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.hy.materialweather.model.DATA.MATERIAL_STYLE;

/**
 * 保存在SQLite上的数据
 */
public class SaveSQLiteData {

    //当前定位的城市
    public String locationCity;

    //设置要求查看的城市
    public List<String> chosenCities = new ArrayList<>();

    //当前网络已经获取过的对象集合
    public Map<String, HeWeather5> heWeather5HashMap = new HashMap<>();

    //当前显示的风格
    public int style = MATERIAL_STYLE;

}
