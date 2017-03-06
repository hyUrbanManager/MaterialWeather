package com.hy.materialweather.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hy.materialweather.R;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.HeWeather5Map;
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
    protected TextView title;

    /* 数据引用 */
    SimpleAdapter simpleAdapter;
    List<Map<String, Object>> mapList;

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        title = (TextView) findViewById(R.id.title);
        title.setText("城市管理");
        setSupportActionBar(toolbar);

        //返回监听，在setSupportActionBar之后
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CityManagerActivity.this.finish();
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
        Iterator<String> iterator = HeWeather5Map.chosenCities.iterator();

        while (iterator.hasNext()) {
            Map<String, Object> map = new HashMap<>();
            map.put("icon", R.drawable.ic_city);
            map.put("text", iterator.next());
            mapList.add(map);
        }

        simpleAdapter = new SimpleAdapter(this,
                mapList,
                R.layout.material_simple_list_item,
                new String[]{"icon", "text"},
                new int[]{R.id.image, R.id.text});
        mListView.setAdapter(simpleAdapter);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city_manager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.operate) {
            Intent intent = new Intent(CityManagerActivity.this, ListCityActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }
}
