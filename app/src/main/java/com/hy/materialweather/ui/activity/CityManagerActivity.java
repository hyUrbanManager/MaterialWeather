package com.hy.materialweather.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.hy.materialweather.R;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.HeWeather5Map;
import com.hy.materialweather.model.json.BasicCity;
import com.hy.materialweather.presenter.CityManagerPresenter;
import com.hy.materialweather.ui.baseui.CityManagerUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

    /* view引用 */
    protected ListView mListView;

    /* 数据引用 */
    SimpleAdapter simpleAdapter;
    List<Map<String, Object>> mapList;

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

        mListView = (ListView) findViewById(R.id.listView1);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_manager);
        //初始化View
        initView();

        mapList = new ArrayList<>();
//        Iterator<String> iterator = HeWeather5Map.chosenCities.iterator();

        if (HeWeather5Map.basicCities2560 == null) {
            HeWeather5Map.init2560Cities(this);
        }
        Iterator<BasicCity> iterator = HeWeather5Map.basicCities2560.iterator();

        while (iterator.hasNext()) {
            Map<String, Object> map = new HashMap<>();
            map.put("icon", R.drawable.ic_city);
            BasicCity basicCity = iterator.next();
            map.put("text", basicCity.provinceZh + " " + basicCity.cityZh);
//            map.put("text", iterator.next());
            mapList.add(map);
        }

        simpleAdapter = new SimpleAdapter(this,
                mapList,
                android.R.layout.activity_list_item,
                new String[]{"icon", "text"},
                new int[]{android.R.id.icon, android.R.id.text1});
        mListView.setAdapter(simpleAdapter);


    }
}
