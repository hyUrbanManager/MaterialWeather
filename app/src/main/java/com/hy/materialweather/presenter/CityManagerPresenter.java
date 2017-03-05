package com.hy.materialweather.presenter;

import android.content.Context;

import com.hy.materialweather.basemvpcomponent.BasePresenter;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.CityManagerModelImpl;
import com.hy.materialweather.model.basemodel.CityManagerModel;
import com.hy.materialweather.ui.baseui.CityManagerUI;

/**
 * Created by Administrator on 2017/3/5.
 */

public class CityManagerPresenter extends BasePresenter<CityManagerUI> {

    CityManagerModel model;
    CityManagerUI view;
    MVPActivity.MVPHandler mHandler;

    public CityManagerPresenter(Context context, CityManagerUI view, MVPActivity.MVPHandler mHandler) {
        this.model = new CityManagerModelImpl(context);
        this.view = view;
        this.mHandler = mHandler;
    }
}
