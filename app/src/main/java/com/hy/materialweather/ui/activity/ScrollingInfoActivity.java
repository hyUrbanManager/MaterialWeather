package com.hy.materialweather.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.hy.materialweather.R;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.DATA;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.presenter.WeatherInfoPresenter;
import com.hy.materialweather.ui.baseui.CityAllInfoUI;

public class ScrollingInfoActivity extends MVPActivity<CityAllInfoUI, WeatherInfoPresenter>
        implements MVPActivity.MVPHandler.onHandleMessageListener, CityAllInfoUI {

    private MVPHandler mHandler;

    @Override
    protected MVPHandler createHandler() {
        return new MVPHandler(this);
    }

    @Override
    public void handleMessage(Message msg) {

    }

    @Override
    protected WeatherInfoPresenter createPresenterRefHandler() {
        mHandler = createHandler();
        return new WeatherInfoPresenter(this, mHandler);
    }

    /* View 引用*/
    private Toolbar toolbar;
    private ImageView imageView;

    private Snackbar mSnackBar;

    /* 数据引用 */

    @Override
    protected void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageView = (ImageView) findViewById(R.id.image);

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

        //查全局Map获得对象
        HeWeather5 heWeather5 = DATA.heWeather5HashMap.get(cityName);
        infoOnCard(heWeather5);

        if(heWeather5 == null) {
            showMessage("诗句错误");
        } else {
            //加载数据
            setDataOnBody(heWeather5);
        }



    }

    @Override
    public void infoOnCard(HeWeather5 heWeather5) {

        //设置标题栏
        try {
            toolbar.setTitle((heWeather5.basic.prov == null ? "" : heWeather5.basic.prov) + heWeather5.basic.city);
        } catch (NullPointerException e) {
            toolbar.setTitle("未知城市");
        }
        setSupportActionBar(toolbar);
        //返回监听，在setSupportActionBar之后
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollingInfoActivity.this.finish();
            }
        });
    }

    /**
     * 给主体设置数据
     */
    private void setDataOnBody(HeWeather5 heWeather5) {
        //顶上图片背景
        imageView.setImageResource(DATA.convertToRes(Integer.parseInt(heWeather5.now.cond.code)));

    }

    private void showMessage(String message) {
        mSnackBar = Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }

}
