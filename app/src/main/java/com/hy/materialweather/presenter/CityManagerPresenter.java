package com.hy.materialweather.presenter;

import android.content.Context;

import com.hy.materialweather.basemvpcomponent.BasePresenter;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.WeatherDataModelImpl;
import com.hy.materialweather.model.basemodel.WeatherDataModel;
import com.hy.materialweather.ui.baseui.CityManagerUI;

import java.util.List;

public class CityManagerPresenter extends BasePresenter<CityManagerUI> {

    WeatherDataModel model;
    CityManagerUI view;
    MVPActivity.MVPHandler mHandler;

    public CityManagerPresenter(Context context, CityManagerUI view, MVPActivity.MVPHandler mHandler) {
        this.model = new WeatherDataModelImpl(context);
        this.view = view;
        this.mHandler = mHandler;
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
