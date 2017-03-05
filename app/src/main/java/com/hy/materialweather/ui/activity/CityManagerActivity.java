package com.hy.materialweather.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.hy.materialweather.R;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.presenter.CityManagerPresenter;
import com.hy.materialweather.ui.baseui.CityManagerUI;

public class CityManagerActivity extends MVPActivity<CityManagerUI, CityManagerPresenter>
        implements CityManagerUI {

    private MVPHandler mHandler;
    @Override
    protected MVPHandler createHandler() {
        return new MVPHandler(new MVPHandler.onHandleMessageListener() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {

                }
            }
        });
    }

    @Override
    protected CityManagerPresenter createPresenterRefHandler() {
        mHandler = createHandler();
        return new CityManagerPresenter(this, this, mHandler);
    }

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        //初始化View
        initView();

    }

}
