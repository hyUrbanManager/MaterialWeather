package com.hy.materialweather.ui.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.hy.materialweather.R;
import com.hy.materialweather.Utils;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.HeWeather5Map;
import com.hy.materialweather.model.WeatherRequestPackage;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.presenter.WeatherCityPresenter;
import com.hy.materialweather.ui.baseui.ListCityUI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends MVPActivity<ListCityUI, WeatherCityPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, ListCityUI {

    //自己创建的Handler
    protected MVPHandler mHandler;

    @Override
    protected MVPHandler createHandler() {
        return new MVPHandler(new MVPHandler.onHandleMessageListener() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case PASS_STRING:
                        showMessage(msg.obj.toString());
                        break;
                    case CLOSE_TOAST:
                        if (mToast != null) {
                            mToast.cancel();
                            mToast = null;
                        }
                        break;
                    case NOTIFY_CHANGED:
                        HeWeather5 heWeather5 = (HeWeather5) msg.obj;
                        Map<String, Object> map = new HashMap<>();
                        map.put("city", heWeather5.basic.city);
                        map.put("tmp", heWeather5.now.tmp);
                        map.put("desc", heWeather5.now.cond.txt);
                        map.put("pm2_5", "pm2.5: " + heWeather5.aqi.city.pm25 + " ug/cm3");
                        map.put("cond", HeWeather5Map.condMap.get(Integer.parseInt(heWeather5.now.cond.code)));
                        cityList.add(map);
                        cityAdapter.notifyDataSetChanged();
                        break;
                }
            }
        });
    }

    @Override
    protected WeatherCityPresenter createPresenterRefHandler() {
        mHandler = createHandler();
        return new WeatherCityPresenter(this, this, mHandler);
    }

    /* View类引用 */
    protected ListView mListView;
    Toast mToast;

    /* 数据引用 */
    List<Map<String, Object>> cityList = new ArrayList<>();
    SimpleAdapter cityAdapter;

    /**
     * View相关的初始化都在这里
     */
    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //初始化ListView，设置首页Adapter的4个要修改的属性
        mListView = (ListView) findViewById(R.id.listView1);
        cityAdapter = new SimpleAdapter(this,
                cityList,
                R.layout.citys_cardview,
                new String[]{"city", "tmp", "desc", "pm2_5", "cond"},
                new int[]{R.id.city, R.id.tmp, R.id.desc, R.id.pm2_5, R.id.cond});
        mListView.setDivider(null);
        mListView.setDividerHeight(30);
        mListView.setAdapter(cityAdapter);

        mToast = Toast.makeText(this, "更新数据中", Toast.LENGTH_LONG);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //初始化View
        initView();

        //初始化全局数据
        HeWeather5Map.initCondMap();

        //读出储存的城市，保存到全局集合中
        HeWeather5Map.chosenCities = mPresenter.getCitiesOnSQLite();
        if (HeWeather5Map.chosenCities == null) {
            Toast.makeText(this, "快点开左上角菜单选择城市吧", Toast.LENGTH_LONG).show();
        } else {
            Iterator<String> iterator = HeWeather5Map.chosenCities.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                mPresenter.weatherReportOnInternet(new WeatherRequestPackage(iterator.next(), i++));
            }
        }

        //设置listView监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = (String) cityList.get(position).get("city");
                Intent intent = new Intent(MainActivity.this, ScrollingInfoActivity.class);
                intent.putExtra("city", cityName);
                startActivity(intent);
            }
        });

        mToast.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    /**
     * 把天气信息全部展现在卡片上
     *
     * @param heWeather5
     */
    @Override
    public void addCity(HeWeather5 heWeather5) {
        Utils.sendMessage(mHandler, NOTIFY_CHANGED, heWeather5);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.heart) {
            //初始化提示框
            AlertDialog mAlertDialog = new AlertDialog.Builder(this)
                    .setTitle(R.string.about_title)
                    .setMessage(R.string.about_message)
                    .setPositiveButton(R.string.about_positive_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(R.string.about_negative_button, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .create();
            mAlertDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_city_manager) {
            //所选城市管理器
            Intent intent = new Intent(MainActivity.this, CityManagerActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_material_show) {

        } else if (id == R.id.nav_raw_data_show) {

        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about) {

        }

        //不关闭drawer
//        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
