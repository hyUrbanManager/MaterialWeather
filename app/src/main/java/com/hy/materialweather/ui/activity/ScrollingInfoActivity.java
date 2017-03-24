package com.hy.materialweather.ui.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hy.materialweather.R;
import com.hy.materialweather.Utils;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.DATA;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.presenter.WeatherInfoPresenter;
import com.hy.materialweather.ui.baseui.CityAllInfoUI;
import com.hy.materialweather.ui.fragment.DailyForecastFragment;
import com.hy.materialweather.ui.view.CHeightViewPager;
import com.hy.materialweather.ui.view.MViewPagerIndicator;

import java.util.ArrayList;
import java.util.List;

public class ScrollingInfoActivity extends MVPActivity<CityAllInfoUI, WeatherInfoPresenter>
        implements MVPActivity.MVPHandler.onHandleMessageListener, CityAllInfoUI {
    private static final String TAG = ScrollingInfoActivity.class.getName() + " ";

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

    /**
     * ViewPage 适配器
     */
    class PagerAdapterDaily extends FragmentPagerAdapter {

        public PagerAdapterDaily(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return listDaily.size();
        }

        @Override
        public Fragment getItem(int position) {
            return listDaily.get(position);
        }
    }

    /* View 引用*/
    private Toolbar toolbar;
    private ImageView imageView;
    private CHeightViewPager viewPager;
    private MViewPagerIndicator indicator;

    private Snackbar mSnackBar;

    /* 数据引用 */
    List<Fragment> listDaily = new ArrayList<>();

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
        fab.setVisibility(View.INVISIBLE);

        imageView = (ImageView) findViewById(R.id.image);

        //初始化ViewPager1
        viewPager = (CHeightViewPager) findViewById(R.id.viewPager);

        indicator = (MViewPagerIndicator) findViewById(R.id.indicator);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrolling_info);
        //初始化View
        initView();

        //获取传递进来的数据
        final Intent intent = getIntent();
        String cityName = intent.getStringExtra("city");

        //查全局Map获得对象
        HeWeather5 heWeather5 = DATA.heWeather5HashMap.get(cityName);
        infoOnCard(heWeather5);

        //初始化daily天气列表fragment，等待装填数据
        FragmentManager manager = getSupportFragmentManager();
        PagerAdapterDaily adapter = new PagerAdapterDaily(manager);
        for (int i = 0; i < 3; i++) {
            DailyForecastFragment fragment = new DailyForecastFragment();
            if(heWeather5 != null && heWeather5.daily_forecast != null) {
                fragment.setData(heWeather5.daily_forecast.get(i));
            }
            listDaily.add(fragment);
        }
        viewPager.setAdapter(adapter);
        viewPager.setCurrentItem(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                Utils.d(TAG + "ViewPager当前是第 " + position + " 页");
                //通知自定义view当前的位置
                if (position >= 0 && position < 3) {
                    indicator.setPage(position);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }


    @Override
    public void infoOnCard(HeWeather5 heWeather5) {
        //设置标题栏
        try {
            toolbar.setTitle((heWeather5.basic.prov == null ? "" : heWeather5.basic.prov)
                    + heWeather5.basic.city + "   "
                    + heWeather5.now.tmp + "\u2103");
        } catch (NullPointerException e) {
            toolbar.setTitle("来自天国的城市");
            showMessage("数据错误，来自天国的城市");
        }
        setSupportActionBar(toolbar);
        //返回监听，在setSupportActionBar之后
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrollingInfoActivity.this.finish();
            }
        });

        setDataOnBody(heWeather5);
    }

    /**
     * 给主体设置数据
     */
    @SuppressWarnings("uncheck")
    private void setDataOnBody(HeWeather5 heWeather5) {
        //顶上图片背景
        try {
            imageView.setImageResource(DATA.convertToRes(Integer.parseInt(heWeather5.now.cond.code)));
        } catch (NullPointerException e) {

        }

        TextView text1, text2, text3;

        //设置经纬度
        text1 = (TextView) findViewById(R.id.jingweidu);
        text2 = (TextView) findViewById(R.id.date);
        try {
            text1.setText("经度：" + heWeather5.basic.lat + "  纬度：" + heWeather5.basic.lon);
            text2.setText("更新时间： ");
            text2.append(heWeather5.basic.update.loc);
        } catch (NullPointerException e) {
            text1.setText("经度：0.00  纬度：0.00");
            text2.setText("更新时间： 1970.1.1 00:00:00");
        }

        //设置温度
        text1 = (TextView) findViewById(R.id.todayTmp);
        text2 = (TextView) findViewById(R.id.tomorrowTmp);
        text3 = (TextView) findViewById(R.id.afterTomorrowTmp);
        try {
            text1.setText(heWeather5.daily_forecast.get(0).tmp.max + "/" + heWeather5.daily_forecast.get(0).tmp.min + "\u2103");
            text2.setText(heWeather5.daily_forecast.get(1).tmp.max + "/" + heWeather5.daily_forecast.get(1).tmp.min + "\u2103");
            text3.setText(heWeather5.daily_forecast.get(2).tmp.max + "/" + heWeather5.daily_forecast.get(2).tmp.min + "\u2103");
        } catch (NullPointerException e) {
            text1.setText("0/0\u2103");
            text2.setText("0/0\u2103");
            text3.setText("0/0\u2103");
        }

        //设置空气
        text1 = (TextView) findViewById(R.id.co);
        text2 = (TextView) findViewById(R.id.no2);
        text3 = (TextView) findViewById(R.id.o3);
        try {
            text1.setText((heWeather5.aqi.city.co != null ? heWeather5.aqi.city.co : 0) + " ug/cm3");
            text2.setText((heWeather5.aqi.city.no2 != null ? heWeather5.aqi.city.no2 : 0) + " ug/cm3");
            text3.setText((heWeather5.aqi.city.o3 != null ? heWeather5.aqi.city.o3 : 0) + " ug/cm3");
        } catch (NullPointerException e) {
            text1.setText("0 ug/cm3");
            text2.setText("0 ug/cm3");
            text3.setText("0 ug/cm3");
        }

        text1 = (TextView) findViewById(R.id.pm10);
        text2 = (TextView) findViewById(R.id.pm2_5);
        text3 = (TextView) findViewById(R.id.so2);
        try {
            text1.setText((heWeather5.aqi.city.pm10 != null ? heWeather5.aqi.city.pm10 : 0) + " ug/cm3");
            text2.setText((heWeather5.aqi.city.pm25 != null ? heWeather5.aqi.city.pm25 : 0) + " ug/cm3");
            text3.setText((heWeather5.aqi.city.so2 != null ? heWeather5.aqi.city.so2 : 0) + " ug/cm3");
        } catch (NullPointerException e) {
            text1.setText("0 ug/cm3");
            text2.setText("0 ug/cm3");
            text3.setText("0 ug/cm3");
        }
        text1 = (TextView) findViewById(R.id.level);
        try {
            text1.setText(heWeather5.aqi.city.qlty);
        } catch (NullPointerException e) {
            text1.setText("未知");
        }

        //日升日落，杂项

        setSuggestion(heWeather5);
    }

    private void setSuggestion(HeWeather5 heWeather5) {
        TextView text1, text2;
        //设置建议指数
        text1 = (TextView) findViewById(R.id.air);
        text2 = (TextView) findViewById(R.id.airDesc);
        try {
            text1.setText("空气指数---");
            text1.append(heWeather5.suggestion.air.brf);
            text2.setText(heWeather5.suggestion.air.txt);
        } catch (NullPointerException e) {
            text1.setText("空气指数---暂无信息");
            text2.setText("暂无信息");
        }
        text1 = (TextView) findViewById(R.id.comf);
        text2 = (TextView) findViewById(R.id.comfDesc);
        try {
            text1.setText("舒适指数---");
            text1.append(heWeather5.suggestion.comf.brf);
            text2.setText(heWeather5.suggestion.comf.txt);
        } catch (NullPointerException e) {
            text1.setText("舒适指数---暂无信息");
            text2.setText("暂无信息");
        }
        text1 = (TextView) findViewById(R.id.cw);
        text2 = (TextView) findViewById(R.id.cwDesc);
        try {
            text1.setText("洗车指数---");
            text1.append(heWeather5.suggestion.cw.brf);
            text2.setText(heWeather5.suggestion.cw.txt);
        } catch (NullPointerException e) {
            text1.setText("洗车指数---暂无信息");
            text2.setText("暂无信息");
        }
        text1 = (TextView) findViewById(R.id.drsg);
        text2 = (TextView) findViewById(R.id.drsgDesc);
        try {
            text1.setText("穿衣指数---");
            text1.append(heWeather5.suggestion.drsg.brf);
            text2.setText(heWeather5.suggestion.drsg.txt);
        } catch (NullPointerException e) {
            text1.setText("穿衣指数---暂无信息");
            text2.setText("暂无信息");
        }
        text1 = (TextView) findViewById(R.id.flu);
        text2 = (TextView) findViewById(R.id.fluDesc);
        try {
            text1.setText("感冒指数---");
            text1.append(heWeather5.suggestion.flu.brf);
            text2.setText(heWeather5.suggestion.flu.txt);
        } catch (NullPointerException e) {
            text1.setText("感冒指数---暂无信息");
            text2.setText("暂无信息");
        }
        text1 = (TextView) findViewById(R.id.sport);
        text2 = (TextView) findViewById(R.id.sportDesc);
        try {
            text1.setText("运动指数---");
            text1.append(heWeather5.suggestion.sport.brf);
            text2.setText(heWeather5.suggestion.sport.txt);
        } catch (NullPointerException e) {
            text1.setText("运动指数---暂无信息");
            text2.setText("暂无信息");
        }
        text1 = (TextView) findViewById(R.id.trav);
        text2 = (TextView) findViewById(R.id.travDesc);
        try {
            text1.setText("旅游指数---");
            text1.append(heWeather5.suggestion.trav.brf);
            text2.setText(heWeather5.suggestion.trav.txt);
        } catch (NullPointerException e) {
            text1.setText("旅游指数---暂无信息");
            text2.setText("暂无信息");
        }
        text1 = (TextView) findViewById(R.id.uv);
        text2 = (TextView) findViewById(R.id.uvDesc);
        try {
            text1.setText("紫外线指数---");
            text1.append(heWeather5.suggestion.uv.brf);
            text2.setText(heWeather5.suggestion.uv.txt);
        } catch (NullPointerException e) {
            text1.setText("紫外线指数---暂无信息");
            text2.setText("暂无信息");
        }
    }

    private void showMessage(String message) {
        mSnackBar = Snackbar.make(getWindow().getDecorView(), message, Snackbar.LENGTH_LONG);
        mSnackBar.show();
    }

}
