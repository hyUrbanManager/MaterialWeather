package com.hy.materialweather.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.hy.materialweather.R;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.HeWeather5Map;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.model.json.Tmp;
import com.hy.materialweather.presenter.CityManagerPresenter;
import com.hy.materialweather.ui.baseui.CityManagerUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class CityManagerActivity extends MVPActivity<CityManagerUI, CityManagerPresenter>
        implements MVPActivity.MVPHandler.onHandleMessageListener,
        CityManagerUI, AdapterView.OnItemLongClickListener {
    public final String TAG = CityManagerActivity.class.getName() + "类下";

    private MVPHandler mHandler;

    @Override
    protected MVPHandler createHandler() {
        return new MVPHandler(this);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {

        }
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
    List<Map<String, Object>> mapList = new ArrayList<>();

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

        simpleAdapter = new SimpleAdapter(this,
                mapList,
                R.layout.material_simple_list_item,
                new String[]{"icon", "text", "tmp"},
                new int[]{R.id.image, R.id.text, R.id.tmp});
        mListView.setAdapter(simpleAdapter);
        mListView.setOnItemLongClickListener(this);
    }

    @Override
    protected void onStart() {
        super.onStart();

        //从内存中更新list列表
        Iterator<String> iterator = HeWeather5Map.chosenCities.iterator();
        while (iterator.hasNext()) {
            Map<String, Object> map = new HashMap<>();
            String cityName = iterator.next();
            map.put("icon", R.drawable.ic_city);
            map.put("text", cityName);
            HeWeather5 heWeather5 = HeWeather5Map.heWeather5HashMap.get(cityName);
            Tmp tmp =  heWeather5 == null ? null : heWeather5.daily_forecast.get(0).tmp;
            map.put("tmp", tmp == null ? "N/A" : tmp.min + "°C ~ " + tmp.max + "°C");
            mapList.add(map);
        }
        simpleAdapter.notifyDataSetChanged();
    }

    /**
     * 即将退出此Activity的时候保存下状态
     */
    @Override
    protected void onPause() {
        super.onPause();
        //保存到内存数据里，和当前定位的城市
        mPresenter.setLocationCity(HeWeather5Map.locationCity);
        mPresenter.saveCitiesOnSQLite(HeWeather5Map.chosenCities);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //清除listView选项
        mapList.clear();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_city_manager, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.operate) {
            //更具风格不同启动不同的Activity
            Class<?> ac = HeWeather5Map.style == HeWeather5Map.RAW_STYLE ?
                    ListCityActivityRaw.class : ListCityActivityMaterial.class;
            Intent intent = new Intent(CityManagerActivity.this, ac);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void showMessage(String message) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
        //弹出删除选项
        new AlertDialog.Builder(this)
                .setTitle("是否删除该城市")
                .setPositiveButton("是", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //内存中删除，再保存入数据库
                        HeWeather5Map.chosenCities.remove(position);
                        mPresenter.saveCitiesOnSQLite(HeWeather5Map.chosenCities);
                        //更新列表
                        mapList.remove(position);
                        simpleAdapter.notifyDataSetChanged();
                    }
                })
                .setNegativeButton("否", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .create()
                .show();

        return true;
    }
}
