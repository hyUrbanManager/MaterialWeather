package com.hy.materialweather.ui.activity;

import android.content.Context;
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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.hy.materialweather.R;
import com.hy.materialweather.Utils;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.BaiduLocation;
import com.hy.materialweather.model.HeWeather5Map;
import com.hy.materialweather.model.WeatherRequestPackage;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.presenter.WeatherCityPresenter;
import com.hy.materialweather.ui.baseui.ListCityUI;
import com.hy.materialweather.ui.view.UpdateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class MainActivity extends MVPActivity<ListCityUI, WeatherCityPresenter>
        implements NavigationView.OnNavigationItemSelectedListener, ListCityUI, BDLocationListener {
    public final String TAG = MainActivity.class.getName() + "类下";

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
                        if (mSnackbar != null) {
                            mSnackbar.dismiss();
                            mSnackbar = null;
                        }
                        break;
                    case NOTIFY_CHANGED:
                        Package p = (Package) msg.obj;

                        Log.d(TAG, "查找数据是否出错： " + p.heWeather5);
                        Map<String, Object> map = new HashMap<>();
                        map.put("city", p.heWeather5.basic.city == null ? "未知" : p.heWeather5.basic.city);
                        map.put("tmp", p.heWeather5.now.tmp == null ? "未知" : p.heWeather5.now.tmp);
                        map.put("desc", p.heWeather5.now.cond.txt == null ? "未知" : p.heWeather5.now.cond.txt);
                        //有的城市没有AQI，修复此bug
                        map.put("pm2_5", "pm2.5: " + (p.heWeather5.aqi == null ?
                                " 未知" : p.heWeather5.aqi.city.pm25) + " ug/cm3");
                        map.put("cond", HeWeather5Map.condMap.get(Integer.parseInt(p.heWeather5.now.cond.code == null ?
                                "999" : p.heWeather5.now.cond.code)));

                        cityList.set(p.list_position, map);
                        cityAdapter.notifyDataSetChanged();

                        receiveCnt++;
                        if (receiveCnt >= HeWeather5Map.chosenCities.size() && receiveCnt != 0) {
                            onReceiveAll();
                        }
                        break;
                    case CLEAR_ADAPTER:
                        cityList.clear();
                        cityAdapter.notifyDataSetChanged();
                        break;
                    case SORT_LIST:

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
    protected FloatingActionButton fab;
    public Snackbar mSnackbar;
    public UpdateView updateView;

    /* 数据引用 */
    public List<Map<String, Object>> cityList = new ArrayList<>();
    public SimpleAdapter cityAdapter;

    int receiveCnt = 0;

    /**
     * View相关的初始化都在这里
     */
    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        //添加城市
        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListCityActivity.class);
                startActivity(intent);
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

        mSnackbar = Snackbar.make(fab, "更新数据中", Snackbar.LENGTH_LONG);

        updateView = (UpdateView) findViewById(R.id.updateView);
        updateView.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化View
        initView();

        //初始化全局数据
        HeWeather5Map.initCondMap();

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

        //异步获取key
        mPresenter.getKeyFromMyServer();

    }

    BaiduLocation baiduLocation;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart方法调用，查看数据");

        //开启一个线程等待Key拿到
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        Thread.sleep(300);
                        if (mPresenter.isKeyGet()) {
                            //刷新列表
                            flashCitiesList();
                            break;
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

        //百度定位API
        baiduLocation = new BaiduLocation(getApplicationContext(), this);
        baiduLocation.mLocationClient.start();

    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG, "onStop停止方法，清除ListView里面的数据");
        //Activity不可见时，清除掉listView里面的数据
        Utils.sendEmptyMessage(mHandler, CLEAR_ADAPTER);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void showMessage(String message) {
        mSnackbar = Snackbar.make(fab, message, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    /**
     * 把天气信息全部展现在卡片上，异步线程调用
     *
     * @param heWeather5
     */
    @Override
    public void addCity(HeWeather5 heWeather5, int list_position) {
        Utils.sendMessage(mHandler, NOTIFY_CHANGED, new Package(heWeather5, list_position));
    }

    @Override
    public void onReceiveAll() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
    }

    /**
     * 连接网络，或者读取本地数据，显示出列表
     */
    @Override
    public void flashCitiesList() {
        //读到的数据个数，用于调用完成方法
        receiveCnt = 0;

        //读出储存的城市，保存到全局集合中。如果有数据，则为再次启动，更新listView数据
        if (HeWeather5Map.chosenCities.size() == 0) {
            Log.d(TAG, "第一次查看数据为空");
            HeWeather5Map.chosenCities = mPresenter.getCitiesOnSQLite();
        }
        //如果的确是数据库为空
        if (HeWeather5Map.chosenCities.size() == 0) {
            Log.d(TAG, "第二次查看数据为空，SQLite里面没有数据");
            showMessage("快点开右下角菜单选择城市吧");
        } else {
            Log.d(TAG, "数据列表有城市，查看内存数据，没有再提交网络申请 " + cityList.size());

            //分配list空间
            for (int i = 0; i < HeWeather5Map.chosenCities.size(); i++) {
                Map<String, Object> map = new HashMap<>();
                map.put("city", HeWeather5Map.chosenCities.get(i));
                map.put("tmp", "wait");
                map.put("desc", "wait");
                map.put("pm2_5", "wait" + " ug/cm3");
                map.put("cond", HeWeather5Map.condMap.get(999));
                Log.d(TAG, "预显示的城市名： " + map.get("city"));
                cityList.add(map);
            }

            Log.d(TAG, "分配List空间，空间为 " + cityList.size() + " " + HeWeather5Map.chosenCities.size());
            Iterator<String> iterator = HeWeather5Map.chosenCities.iterator();
            int i = 0;
            while (iterator.hasNext()) {
                String cityName = iterator.next();
                if (HeWeather5Map.heWeather5HashMap.containsKey(cityName)) {
                    Log.d(TAG, "获取 " + cityName + " 城市内存中的天气数据");
                    addCity(HeWeather5Map.heWeather5HashMap.get(cityName), i++);
                    receiveCnt++;
                } else {
                    Log.d(TAG, "申请 " + cityName + " 城市的天气数据");
                    mPresenter.weatherReportOnInternet(new WeatherRequestPackage(cityName, i++));
                }
            }
            Utils.sendMessage(mHandler, PASS_STRING, "更新数据中");
        }
    }

    /**
     * 数据打包，线程通信
     */
    private class Package {

        public final HeWeather5 heWeather5;
        public final int list_position;

        private Package(HeWeather5 heWeather5, int list_position) {
            this.heWeather5 = heWeather5;
            this.list_position = list_position;
        }
    }

    protected Snackbar quitSnackBar;

    /**
     * 返回
     */
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            //真的要退出吗？
            if(quitSnackBar == null) {
                quitSnackBar = Snackbar.make(fab, "真的要退出吗", Snackbar.LENGTH_SHORT)
                        .setAction("退出", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                MainActivity.super.onBackPressed();
                                Utils.sendEmptyMessage(mHandler, CLOSE_TOAST);
                                MainActivity.this.finish();
                            }
                        });
                Utils.d(TAG + " 创建退出的SnackBar");
            }
            if(quitSnackBar.isShown()) {
                quitSnackBar.dismiss();
            } else {
                quitSnackBar.show();
            }
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

    /**
     * 启动Activity
     *
     * @param context
     */
    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    @Override
    public void onReceiveLocation(BDLocation location) {

        //获取定位结果
        StringBuffer sb = new StringBuffer(256);

        sb.append("time : ");
        sb.append(location.getTime());    //获取定位时间

        sb.append("\nerror code : ");
        sb.append(location.getLocType());    //获取类型类型

        sb.append("\nlatitude : ");
        sb.append(location.getLatitude());    //获取纬度信息

        sb.append("\nlontitude : ");
        sb.append(location.getLongitude());    //获取经度信息

        sb.append("\nradius : ");
        sb.append(location.getRadius());    //获取定位精准度

        if (location.getLocType() == BDLocation.TypeGpsLocation) {

            // GPS定位结果
            sb.append("\nspeed : ");
            sb.append(location.getSpeed());    // 单位：公里每小时

            sb.append("\nsatellite : ");
            sb.append(location.getSatelliteNumber());    //获取卫星数

            sb.append("\nheight : ");
            sb.append(location.getAltitude());    //获取海拔高度信息，单位米

            sb.append("\ndirection : ");
            sb.append(location.getDirection());    //获取方向信息，单位度

            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\ndescribe : ");
            sb.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

            // 网络定位结果
            sb.append("\naddr : ");
            sb.append(location.getAddrStr());    //获取地址信息

            sb.append("\noperationers : ");
            sb.append(location.getOperators());    //获取运营商信息

            sb.append("\ndescribe : ");
            sb.append("网络定位成功");

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

            // 离线定位结果
            sb.append("\ndescribe : ");
            sb.append("离线定位成功，离线定位结果也是有效的");

        } else if (location.getLocType() == BDLocation.TypeServerError) {

            sb.append("\ndescribe : ");
            sb.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

            sb.append("\ndescribe : ");
            sb.append("网络不同导致定位失败，请检查网络是否通畅");

        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

            sb.append("\ndescribe : ");
            sb.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

        }

        sb.append("\nlocationdescribe : ");
        sb.append(location.getLocationDescribe());    //位置语义化信息

        List<Poi> list = location.getPoiList();    // POI数据
        if (list != null) {
            sb.append("\npoilist size = : ");
            sb.append(list.size());
            for (Poi p : list) {
                sb.append("\npoi= : ");
                sb.append(p.getId() + " " + p.getName() + " " + p.getRank());
            }
        }

        Utils.d(sb.toString());

        //停止搜索
        baiduLocation.mLocationClient.stop();

    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
