package com.hy.materialweather.presenter;

import com.hy.materialweather.basemvpcomponent.BasePresenter;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.basemodel.WeatherDataModel;
import com.hy.materialweather.ui.baseui.CityAllInfoUI;

/**
 *
 */
public class WeatherInfoPresenter extends BasePresenter<CityAllInfoUI>{

    private WeatherDataModel model;
    private CityAllInfoUI mView;
    private MVPActivity.MVPHandler mHandler;

    public WeatherInfoPresenter(CityAllInfoUI mView, MVPActivity.MVPHandler mHandler) {
        this.mView = mView;
        this.mHandler = mHandler;
    }



}
