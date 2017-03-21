package com.hy.materialweather.presenter;

import android.content.Context;

import com.hy.materialweather.Utils;
import com.hy.materialweather.basemvpcomponent.BasePresenter;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.DATA;
import com.hy.materialweather.model.WeatherDataModelImpl;
import com.hy.materialweather.model.basemodel.WeatherDataModel;
import com.hy.materialweather.ui.baseui.CityManagerUI;

import java.util.List;

public class CityManagerPresenter extends BasePresenter<CityManagerUI> {

    private WeatherDataModel model;
    private CityManagerUI view;
    private MVPActivity.MVPHandler mHandler;

    public CityManagerPresenter(Context context, CityManagerUI view, MVPActivity.MVPHandler mHandler) {
        this.model = new WeatherDataModelImpl(context);
        this.view = view;
        this.mHandler = mHandler;
    }

    /**
     * 定位到的城市添加进列表里
     *
     * @param cityName
     */
    public void setLocationCity(String cityName) {
        if (cityName != null) {
            //如果已经在列表里，移到第一位，否则添加到第一位
            if (DATA.chosenCities.contains(cityName)) {
                DATA.chosenCities.remove(cityName);
            }
            Utils.d(" 添加定位城市到显示列表");
            DATA.chosenCities.add(0, cityName);
        }
    }

    /**
     * 获取记录，用户要查看的城市
     * @return
     */
    public List<String> getCitiesOnSQLite() {
        return model.getCitiesOnSQLite();
    }

    /**
     * 把list转换成json字符串传入
     * @param list
     */
    public void saveCitiesOnSQLite(List<String> list) {
        model.saveCitiesOnSQLite(list);
    }

}
