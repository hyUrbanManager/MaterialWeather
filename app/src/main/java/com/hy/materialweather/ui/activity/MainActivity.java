package com.hy.materialweather.ui.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.ListView;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.Poi;
import com.hy.materialweather.AnimUtils;
import com.hy.materialweather.R;
import com.hy.materialweather.Utils;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.BaiduLocation;
import com.hy.materialweather.model.DATA;
import com.hy.materialweather.model.WeatherRequestPackage;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.presenter.WeatherCityPresenter;
import com.hy.materialweather.ui.adapter.MainCardAdapter;
import com.hy.materialweather.ui.baseui.ListCityUI;
import com.hy.materialweather.ui.receiver.NetworkReceiver;
import com.hy.materialweather.ui.view.UpdateView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import static com.hy.materialweather.model.DATA.COMPLETE;
import static com.hy.materialweather.model.DATA.FAIL;
import static com.hy.materialweather.model.DATA.STARTING;
import static com.hy.materialweather.model.DATA.STOP;
import static com.hy.materialweather.model.DATA.locationCity;

/**
 * 可能会有潜在的BUG
 */
public class MainActivity extends MVPActivity<ListCityUI, WeatherCityPresenter>
        implements MVPActivity.MVPHandler.onHandleMessageListener,
        NavigationView.OnNavigationItemSelectedListener, ListCityUI, BDLocationListener {
    public final String TAG = MainActivity.class.getName() + "类下";

    @Override
    protected MVPHandler createHandler() {
        return new MVPHandler(this);
    }

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
            //展示一个城市的信息
            case NOTIFY_CHANGED_ONE_CITY:
                Package p = (Package) msg.obj;

                Log.d(TAG, "查找数据是否出错： " + p.heWeather5);
                Map<String, Object> map = new HashMap<>();
                map.put("city", p.heWeather5.basic.city == null ? "未知" : p.heWeather5.basic.city);
                map.put("tmp", p.heWeather5.now.tmp == null ? "未知" : p.heWeather5.now.tmp);
                map.put("desc", p.heWeather5.now.cond.txt == null ? "未知" : p.heWeather5.now.cond.txt);
                //有的城市没有AQI，修复此bug
                map.put("pm2_5", "pm2.5: " + (p.heWeather5.aqi == null ?
                        " 未知" : p.heWeather5.aqi.city.pm25) + " ug/cm3");
                map.put("cond", DATA.condMap.get(Integer.parseInt(p.heWeather5.now.cond.code == null ?
                        "999" : p.heWeather5.now.cond.code)));
                //设置背景，输入的是天气的代码
                map.put("background", Integer.parseInt(p.heWeather5.now.cond.code == null ?
                        "999" : p.heWeather5.now.cond.code));

                cityDataList.set(p.list_position, map);
                cityAdapter.notifyDataSetChanged();

                receiveCnt++;
                if (receiveCnt >= DATA.chosenCities.size() && receiveCnt != 0) {
                    onReceiveAll();
                }
                break;
            //把ListView清空
            case CLEAR_ADAPTER:
                cityDataList.clear();
                cityAdapter.notifyDataSetChanged();
                break;
            //异步刷新列表数据
            case UPDATE_LIST_VIEW:
                cityAdapter.notifyDataSetChanged();
                break;
            case START_REFRESHING:
                mSwipeRefreshLayout.setRefreshing(true);
                break;
            case STOP_REFRESHING:
                mSwipeRefreshLayout.setRefreshing(false);
                break;
            case RECEIVER_QUIRE_REFRESH:
                refreshCityList(true);
                break;
        }
    }

    @Override
    protected WeatherCityPresenter createPresenterRefHandler() {
        mHandler = createHandler();
        return new WeatherCityPresenter(this, this, mHandler);
    }

    /* View类引用 */
    protected SwipeRefreshLayout mSwipeRefreshLayout;
    protected ListView mListView;
    protected FloatingActionButton fab;
    public Snackbar mSnackbar;
    public UpdateView updateView;
    private DrawerLayout mDrawer;

    /* 广播监听 */
    private NetworkReceiver mReceiver;

    /* 手势监控 */
    private GestureDetector detector;

    /* 数据引用 */
    public List<Map<String, Object>> cityDataList = new ArrayList<>();
    public MainCardAdapter cityAdapter;
    int receiveCnt = 0;
    private StringBuilder locationMessage;

    /**
     * View相关的初始化都在这里
     */
    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("Material 天气");
        setSupportActionBar(toolbar);

        //添加城市
        fab = (FloatingActionButton) findViewById(R.id.fab);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        //打开手势滑动
        mDrawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        detector = new GestureDetector(this, new GestureListener());

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //初始化ListView，和布局Layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.l_primary_dark,
                R.color.l_primary,
                R.color.l_primary_light);

        mListView = (ListView) findViewById(R.id.listView1);
        cityAdapter = new MainCardAdapter(this,
                cityDataList,
                R.layout.citys_cardview2,
                new String[]{"city", "tmp", "desc", "pm2_5", "cond", "background"},
                new int[]{R.id.city, R.id.tmp, R.id.desc, R.id.pm2_5, R.id.cond, R.id.relativeLayout});
        mListView.setDivider(null);
        mListView.setDividerHeight(30);
        mListView.setAdapter(cityAdapter);

        //ListView选项的设置入场动画
        Animation animation1 = AnimationUtils.loadAnimation(this, R.anim.card_list_view_up);
        AnimationSet set = new AnimationSet(false);
        Animation translate = new TranslateAnimation(0, 0,
                Utils.getScreenDisplay(this).heightPixels - 200, 0);

        set.addAnimation(animation1);
        set.addAnimation(translate);
        set.setDuration(800);

        LayoutAnimationController controller = new LayoutAnimationController(set);
        controller.setDelay(0.5f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mListView.setLayoutAnimation(controller);

        mSnackbar = Snackbar.make(fab, "更新数据中", Snackbar.LENGTH_LONG);

        updateView = (UpdateView) findViewById(R.id.updateView);
        updateView.hide();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //初始化View
        initView();

        //初始化全局数据
        DATA.initCondMap();
        //读数据库选择的城市
        DATA.chosenCities = mPresenter.getCitiesOnSQLite();
        //读取显示的风格
        DATA.style = mPresenter.getStyleOnSQLite();
        //获取网络状态

        //动态注册广播接收者
        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        mReceiver = new NetworkReceiver(mHandler);
        registerReceiver(mReceiver, mFilter);

        //设置listView监听
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cityName = (String) cityDataList.get(position).get("city");
                final Intent intent = new Intent(MainActivity.this, ScrollingInfoActivity.class);
                intent.putExtra("city", cityName);
                //设置转场特效，CardView和图片一起带入
                ActivityOptionsCompat compat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        MainActivity.this,
                        view,
                        getString(R.string.transition_image)
                );
                ActivityCompat.startActivity(MainActivity.this, intent, compat.toBundle());
            }
        });

        //设置fab监听添加城市
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //根据风格不同启动不同的Activity
                Class<?> ac = ListCityActivity.class;
                Intent intent = new Intent(MainActivity.this, ac);
//                ActivityOptionsCompat compat = ActivityOptionsCompat.makeScaleUpAnimation(view,
//                        view.getWidth() / 2, view.getHeight() / 2, 0, 0);
//                ActivityCompat.startActivity(MainActivity.this, intent, compat.toBundle());
                AnimUtils.startActivity(MainActivity.this, intent, view,
                        R.color.l_accent);
            }
        });

        //设置下拉刷新
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Utils.d(TAG + " 下拉刷新");
                refreshCityList(true);
                mSwipeRefreshLayout.setRefreshing(true);
            }
        });

        //百度定位API
        baiduLocation = new BaiduLocation(getApplicationContext(), this);
        startLocation();

        //刷新数据列表
        refreshCityList(false);
        mSwipeRefreshLayout.setRefreshing(true);
    }

    private BaiduLocation baiduLocation;

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart方法调用，查看数据");
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
        Log.d(TAG, "onStop停止方法");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //把chosenCities保存
        mPresenter.saveCitiesOnSQLite(DATA.chosenCities);
        //网络监听器取消
        unregisterReceiver(mReceiver);
    }

    @Override
    public void showMessage(String message) {
        mSnackbar = Snackbar.make(fab, message, Snackbar.LENGTH_SHORT);
        mSnackbar.show();
    }

    /**
     * 把一个城市的信息展现在卡片上，异步线程调用
     *
     * @param heWeather5
     */
    @Override
    public void addOneCity(HeWeather5 heWeather5, int list_position) {
        Utils.sendMessage(mHandler, NOTIFY_CHANGED_ONE_CITY, new Package(heWeather5, list_position));
    }

    /**
     * UI线程中调用
     */
    @Override
    public void onReceiveAll() {
        if (mSnackbar != null) {
            mSnackbar.dismiss();
        }
        //全部收到，停止刷新。
        receiveCnt = 0;
        mSwipeRefreshLayout.setRefreshing(false);
        Utils.d(TAG + " 停止刷新列表动画");
    }

    /**
     * 连接网络，或者读取本地数据，显示出列表。可以执行在其他线程
     */
    @Override
    public void refreshCityList(boolean isAllReconnect) {

        //如果没网，直接提示失败，慎重

        //读到的数据个数，用于调用完成方法
        receiveCnt = 0;
        final List<String> mCityNameList = new ArrayList<>();
        final List<Integer> mCityListCount = new ArrayList<>();

        //如果数据库为空
        if (DATA.chosenCities.size() == 0) {
            Log.d(TAG, "第二次查看数据为空，SQLite里面没有数据");
            //查看定位的城市是否有数据，加入
            if (DATA.locationCity != null) {
                DATA.chosenCities.add(0, locationCity);
            } else {
                Utils.sendEmptyMessage(mHandler, CLEAR_ADAPTER);
                showMessage("快点开右下角菜单选择城市吧");
                Utils.sendEmptyMessage(mHandler, STOP_REFRESHING);
                return;
            }
        }
        Log.d(TAG, "数据列表有城市，查看内存数据，没有再提交网络申请 " + cityDataList.size());

        memoryDataOnListView(); //先把内存中信息显示

        Log.d(TAG, "分配List空间，空间为 " + cityDataList.size() + " " + DATA.chosenCities.size());

        //遍历选择的城市，加入数据，从网络或者从本地。location 城市分开处理
        Iterator<String> iterator = DATA.chosenCities.iterator();
        int i = 0;
        if (isAllReconnect) {
            Utils.d(TAG + "强制刷新");
            while (iterator.hasNext()) {
                //要申请网络的保存下来
                mCityNameList.add(iterator.next());
                mCityListCount.add(i++);
            }
        } else {
            Utils.d(TAG + "非强制刷新");
            while (iterator.hasNext()) {
                String cityName = iterator.next();
                if (DATA.heWeather5HashMap.containsKey(cityName)) {
                    Log.d(TAG, "获取 " + cityName + " 城市内存中的天气数据");
                    //前面内存处理，无须在add
//                        addOneCity(DATA.heWeather5HashMap.get(cityName), i++);
                    receiveCnt++;
                } else {
                    //要申请网络的保存下来
                    mCityNameList.add(cityName);
                    mCityListCount.add(i++);
                }
            }
        }

        //开启一个线程申请网络
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < mCityNameList.size(); i++) {
                    Log.d(TAG, "异步方法申请 " + mCityNameList.get(i) + " 城市的天气数据，等待回调添加到UI界面");
                    mPresenter.weatherReportOnInternet(new WeatherRequestPackage(mCityNameList.get(i), mCityListCount.get(i)));
                }
            }
        }).start();

    }

    @Override
    public void loadBackground() {
//        Glide.with(this).load()
    }

    /**
     * 把内存中的数据显示在ListView上
     * 异步或同步线程中运行
     */
    private void memoryDataOnListView() {
        //分配list空间，内存中有数据显示内存数据，否则显示默认未知的信息
        cityDataList.clear();
        for (int i = 0; i < DATA.chosenCities.size(); i++) {
            Map<String, Object> map = new HashMap<>();
            String cityName = DATA.chosenCities.get((i));
            //查找内存中是否有记录
            map.put("city", cityName);
            if (DATA.heWeather5HashMap.containsKey(cityName)) {
                HeWeather5 heWeather5 = DATA.heWeather5HashMap.get(cityName);
                map.put("city", heWeather5.basic.city == null ? "未知" : heWeather5.basic.city);
                map.put("tmp", heWeather5.now.tmp == null ? "未知" : heWeather5.now.tmp);
                map.put("desc", heWeather5.now.cond.txt == null ? "未知" : heWeather5.now.cond.txt);
                //有的城市没有AQI，修复此bug
                map.put("pm2_5", "pm2.5: " + (heWeather5.aqi == null ?
                        " 未知" : heWeather5.aqi.city.pm25) + " ug/cm3");
                map.put("cond", DATA.condMap.get(Integer.parseInt(heWeather5.now.cond.code == null ?
                        "999" : heWeather5.now.cond.code)));
                //设置背景，输入的是天气的代码
                map.put("background", Integer.parseInt(heWeather5.now.cond.code == null ?
                        "999" : heWeather5.now.cond.code));
            } else {
                map.put("tmp", "wait");
                map.put("desc", "wait");
                map.put("pm2_5", "wait" + " ug/cm3");
                map.put("cond", DATA.condMap.get(999));
                //设置背景，输入的是天气的代码
                map.put("background", 100);
            }
            Log.d(TAG, "预显示的城市名： " + map.get("city"));
            cityDataList.add(map);
        }
        //列表先显示未知
        if (Looper.myLooper() == Looper.getMainLooper()) {
            cityAdapter.notifyDataSetChanged();
        } else {
            Utils.sendEmptyMessage(mHandler, UPDATE_LIST_VIEW);
        }
        Utils.d(TAG + " 默认数据装载完毕，全部设置为未知信息");
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
        } else if (mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(false);
        } else {
            //真的要退出吗？
            if (quitSnackBar == null) {
                quitSnackBar = Snackbar.make(fab, "真的要退出吗", Snackbar.LENGTH_SHORT)
                        .setAction("退出", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                Utils.sendEmptyMessage(mHandler, CLOSE_TOAST);
                                MainActivity.this.finish();
                            }
                        });
                Utils.d(TAG + " 创建退出的SnackBar");
            }
            if (quitSnackBar.isShown()) {
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
            //所选城市管理器，根据风格启动不同的Activity
            mDrawer.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Class<?> activityClass = CityManagerActivity.class;
                    Intent intent = new Intent(MainActivity.this, activityClass);
                    startActivity(intent);
                }
            }, 200);
        } else if (id == R.id.location_message) {
            mDrawer.closeDrawer(GravityCompat.START);
            final AlertDialog dialog = new AlertDialog.Builder(
                    MainActivity.this)
                    .setMessage(locationMessage != null ? locationMessage.toString() : "什么也没有")
                    .setPositiveButton("重新获取位置", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            startLocation();
                        }
                    })
                    .create();
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    dialog.show();
                    //设置窗体
                    WindowManager.LayoutParams layoutParams = dialog.getWindow().getAttributes();
                    layoutParams.alpha = 0.8f;
                    dialog.getWindow().setAttributes(layoutParams);
                }
            }, 200);
        } else if (id == R.id.nav_setting) {

        } else if (id == R.id.nav_about) {
            //Lib界面
            mDrawer.closeDrawer(GravityCompat.START);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(MainActivity.this, AboutActivity.class);
                    startActivity(intent);
                }
            }, 200);
        }
        return true;
    }

    private class GestureListener implements GestureDetector.OnGestureListener {

        @Override
        public boolean onDown(MotionEvent e) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent e) {
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            return false;
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent e) {
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            float x = e2.getX() - e1.getX();
            float y = e2.getY() - e1.getY();
            Utils.d(TAG + x + "  " + y);
            if (Math.abs(x) < Math.abs(y)) {
                return true;
            }
            if (x > 0) {
                mDrawer.openDrawer(GravityCompat.START);
            } else if (x < 0) {
                mDrawer.closeDrawer(GravityCompat.START);
            }
            return true;
        }
    }

    /**
     * 添加手势监听
     *
     * @param event
     * @return
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
//        return detector.onTouchEvent(event);
    }

    /**
     * 启动Activity
     *
     * @param context
     */
    public static void launch(Context context) {
        context.startActivity(new Intent(context, MainActivity.class));
    }

    /**
     * 启动搜索
     */
    private void startLocation() {
        DATA.locationState = STOP;
        //防止意外
        try {
            baiduLocation.mLocationClient.start();
            DATA.locationState = STARTING;
        } catch (Exception e) {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                showMessage(e.getMessage());
            } else {
                Utils.sendMessage(mHandler, PASS_STRING, e.getMessage());
            }
            DATA.locationState = FAIL;
        }
    }

    @Override
    public void onReceiveLocation(BDLocation location) {
        //清空上次数据
        locationMessage = new StringBuilder();

        //获取定位结果
//        locationMessage.append("time : ");
//        locationMessage.append(location.getTime());    //获取定位时间

//        locationMessage.append("\nerror code : ");
//        locationMessage.append(location.getLocType());    //获取类型类型

        locationMessage.append("\n纬度 : ");
        locationMessage.append(location.getLatitude());    //获取纬度信息

        locationMessage.append("\n经度 : ");
        locationMessage.append(location.getLongitude());    //获取经度信息

//        locationMessage.append("\nradius : ");
//        locationMessage.append(location.getRadius());    //获取定位精准度

        if (location.getLocType() == BDLocation.TypeGpsLocation) {

            // GPS定位结果
            locationMessage.append("\n移速 : ");
            locationMessage.append(location.getSpeed());    // 单位：公里每小时

            locationMessage.append("\n连接到卫星数 : ");
            locationMessage.append(location.getSatelliteNumber());    //获取卫星数

            locationMessage.append("\n海拔 : ");
            locationMessage.append(location.getAltitude());    //获取海拔高度信息，单位米

            locationMessage.append("\n方向 : ");
            locationMessage.append(location.getDirection());    //获取方向信息，单位度

            locationMessage.append("\n地址 : ");
            locationMessage.append(location.getAddrStr());    //获取地址信息

            locationMessage.append("\n描述 : ");
            locationMessage.append("gps定位成功");

        } else if (location.getLocType() == BDLocation.TypeNetWorkLocation) {

            // 网络定位结果
            locationMessage.append("\n地址 : ");
            locationMessage.append(location.getAddrStr());    //获取地址信息

            locationMessage.append("\n运营商 : ");
            locationMessage.append(location.getOperators());    //获取运营商信息

            locationMessage.append("\n描述 : ");
            locationMessage.append("网络定位成功");

        } else if (location.getLocType() == BDLocation.TypeOffLineLocation) {

            // 离线定位结果
            locationMessage.append("\n描述 : ");
            locationMessage.append("离线定位成功，离线定位结果也是有效的");

        } else if (location.getLocType() == BDLocation.TypeServerError) {

            locationMessage.append("\n描述 : ");
            locationMessage.append("服务端网络定位失败，可以反馈IMEI号和大体定位时间到loc-bugs@baidu.com，会有人追查原因");

        } else if (location.getLocType() == BDLocation.TypeNetWorkException) {

            locationMessage.append("\n描述 : ");
            locationMessage.append("网络不同导致定位失败，请检查网络是否通畅");

        } else if (location.getLocType() == BDLocation.TypeCriteriaException) {

            locationMessage.append("\n描述 : ");
            locationMessage.append("无法获取有效定位依据导致定位失败，一般是由于手机的原因，处于飞行模式下一般会造成这种结果，可以试着重启手机");

        }

        locationMessage.append("\n地理位置 : ");
        locationMessage.append(location.getLocationDescribe());    //位置语义化信息

        List<Poi> list = location.getPoiList();    // POI数据
        if (list != null) {
//            locationMessage.append("\npoilist size = : ");
//            locationMessage.append(list.size());
            for (Poi p : list) {
//                locationMessage.append("\npoi= : ");
//                locationMessage.append(p.getId() + " " + p.getName() + " " + p.getRank());
                locationMessage.append("\n" + p.getName());
            }
        }

        Utils.d(TAG + locationMessage.toString());

        //停止搜索
        baiduLocation.mLocationClient.stop();
        //把定位到的城市加入列表
        String cityName = location.getAddress().city;
        if (cityName.charAt(cityName.length() - 1) == '市') {
            cityName = cityName.substring(0, cityName.length() - 1);
        }
        DATA.locationCity = cityName;
        Utils.d(TAG + " 定位到的城市是：" + cityName);
        DATA.locationState = COMPLETE;
    }

    @Override
    public void onConnectHotSpotMessage(String s, int i) {

    }
}
