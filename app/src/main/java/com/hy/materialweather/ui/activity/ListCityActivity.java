package com.hy.materialweather.ui.activity;

import android.os.Bundle;
import android.os.Message;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Filter;
import android.widget.ListView;
import android.widget.Toast;

import com.hy.materialweather.R;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.HeWeather5Map;
import com.hy.materialweather.model.json.BasicCity;
import com.hy.materialweather.presenter.CityManagerPresenter;
import com.hy.materialweather.ui.adapter.CitiesAdapter;
import com.hy.materialweather.ui.baseui.CityManagerUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ListCityActivity extends MVPActivity<CityManagerUI, CityManagerPresenter>
        implements CityManagerUI, SearchView.OnQueryTextListener{

    private MVPHandler mHandler;

    @Override
    protected MVPHandler createHandler() {
        return new MVPHandler(new MVPHandler.onHandleMessageListener() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case SHOW_TOAST:
                        mToast.show();
                        break;
                    case CLOSE_TOAST:
                        mToast.cancel();
                        break;
                    case NOTIFY_CHANGED:
                        //添加ListView数据
                        Iterator<BasicCity> iterator = HeWeather5Map.basicCities2560.iterator();

                        while (iterator.hasNext()) {
                            BasicCity basicCity = iterator.next();
                            stringList.add(basicCity.cityZh);
                        }

                        citiesAdapter.notifyDataSetChanged();
                        break;
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
    protected Toast mToast;
    protected SearchView mSearchView;
    protected SearchView.SearchAutoComplete mEditView;

    /* 数据引用 */
    CitiesAdapter citiesAdapter;
    List<String> stringList;

    @Override
    protected void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("");
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);

        //返回监听，在setSupportActionBar之后
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListCityActivity.this.finish();
            }
        });

        mListView = (ListView) findViewById(R.id.listView1);
        mListView.setTextFilterEnabled(true);

        mSearchView = (SearchView) findViewById(R.id.searchView1);
        mSearchView.setIconifiedByDefault(false);
        mSearchView.setOnQueryTextListener(this);

        mToast = Toast.makeText(this, "正在加载全国城市，请稍候...", Toast.LENGTH_LONG);
        mToast.setGravity(Gravity.CENTER, 0, 0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_city);
        //初始化View组件
        initView();

        stringList = new ArrayList<>();
        citiesAdapter = new CitiesAdapter(this, stringList);

        mListView.setAdapter(citiesAdapter);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (HeWeather5Map.basicCities2560 == null) {
                    mHandler.sendEmptyMessage(SHOW_TOAST);
                    //解析超长字符串，耗时操作
                    HeWeather5Map.init2560Cities(ListCityActivity.this);
                }

                Log.d(ListCityActivity.class.getName(),"ListView适配器数据的大小：" + stringList.size());
                if(stringList.size() != 2560) {
                    stringList.clear();
                    mHandler.sendEmptyMessage(NOTIFY_CHANGED);
                }
                mHandler.sendEmptyMessage(CLOSE_TOAST);
            }
        }).start();

    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(ListCityActivity.class.getName(), "监听函数执行了");

        //交给过滤器去过滤
        Filter filter;
        filter = citiesAdapter.getFilter();
        filter.filter(newText);

        return true;
    }
}
