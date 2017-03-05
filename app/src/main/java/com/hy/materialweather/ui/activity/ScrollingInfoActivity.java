package com.hy.materialweather.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.hy.materialweather.R;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.HeWeather5Map;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.presenter.WeatherInfoPresenter;
import com.hy.materialweather.ui.baseui.CityAllInfoUI;

public class ScrollingInfoActivity extends MVPActivity<CityAllInfoUI, WeatherInfoPresenter>
        implements CityAllInfoUI {

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
    protected WeatherInfoPresenter createPresenterRefHandler() {
        mHandler = createHandler();
        return new WeatherInfoPresenter(this, mHandler);
    }

    /* View 引用*/
    private Toolbar toolbar;
    private TextView title;

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        title = (TextView) findViewById(R.id.title);

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
        setContentView(R.layout.activity_scrolling_info);
        //初始化View
        initView();

        //获取传递进来的数据
        Intent intent = getIntent();
        String cityName = intent.getStringExtra("city");
        Toast.makeText(this, cityName, Toast.LENGTH_LONG).show();

        //查全局Map获得对象
        HeWeather5 heWeather5 = HeWeather5Map.heWeather5HashMap.get(cityName);
        infoOnCard(heWeather5);

//        title.setText((heWeather5.basic.prov == null ? "" : heWeather5.basic.prov) + heWeather5.basic.city);
    }

    //TODO 这一块有bug
    @Override
    public void infoOnCard(HeWeather5 heWeather5) {

        //设置标题栏
        toolbar.setTitle((heWeather5.basic.prov == null ? "" : heWeather5.basic.prov) + heWeather5.basic.city);
        setSupportActionBar(toolbar);


    }


}
