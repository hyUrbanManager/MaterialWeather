package com.hy.materialweather.ui.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.LayoutAnimationController;
import android.widget.AdapterView;
import android.widget.Filter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hy.materialweather.R;
import com.hy.materialweather.uitls.Utils;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.DATA;
import com.hy.materialweather.model.json.BasicCity;
import com.hy.materialweather.presenter.CityManagerPresenter;
import com.hy.materialweather.ui.adapter.CitiesAdapterRaw;
import com.hy.materialweather.ui.baseui.CityManagerUI;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import static com.hy.materialweather.model.DATA.basicCities2560;

public class ListCityActivity extends MVPActivity<CityManagerUI, CityManagerPresenter>
        implements MVPActivity.MVPHandler.onHandleMessageListener,
        CityManagerUI, SearchView.OnQueryTextListener, AdapterView.OnItemClickListener {

    @Override
    protected MVPHandler createHandler() {
        return new MVPHandler(this);
    }

    @Override
    public void handleMessage(Message msg) {
        switch (msg.what) {
            case SHOW_TOAST:
                mToast.show();
                break;
            case CLOSE_TOAST:
                mToast.cancel();
                break;
            case NOTIFY_CHANGED_ONE_CITY:
                //添加ListView数据
                Set<String> set = DATA.basicCities2560.keySet();
                Iterator<String> iterator = set.iterator();

                while (iterator.hasNext()) {
                    String key = iterator.next();
                    BasicCity basicCity = DATA.basicCities2560.get(key);
                    stringList.add(basicCity.cityZh);
                }

                citiesAdapterRaw.notifyDataSetChanged();
                break;
        }
    }

    @Override
    protected CityManagerPresenter createPresenterRefHandler() {
        mHandler = createHandler();
        return new CityManagerPresenter(this, this, mHandler);
    }

    /* view引用 */
    protected GridView mGridView;
    protected Toast mToast;
    protected SearchView mSearchView;

    /* 数据引用 */
    CitiesAdapterRaw citiesAdapterRaw;
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
                onBackPressed();
            }
        });

        mGridView = (GridView) findViewById(R.id.gridView1);
        mGridView.setTextFilterEnabled(true);
        //设置子项入场动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(0.2f, 1.0f);
        AnimationSet set = new AnimationSet(false);
        set.addAnimation(alphaAnimation);
        set.setDuration(200);

        LayoutAnimationController controller = new LayoutAnimationController(set);
        controller.setDelay(0.3f);
        controller.setOrder(LayoutAnimationController.ORDER_NORMAL);
        mGridView.setLayoutAnimation(controller);

        //设置搜索
        mSearchView = (SearchView) findViewById(R.id.searchView1);
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
        citiesAdapterRaw = new CitiesAdapterRaw(this, stringList);

        mGridView.setAdapter(citiesAdapterRaw);
        mGridView.setOnItemClickListener(this);

        new Thread(new Runnable() {
            @Override
            public void run() {
                if (basicCities2560 == null) {
                    mHandler.sendEmptyMessage(SHOW_TOAST);
                    //解析超长字符串，耗时操作
                    DATA.init2560Cities(ListCityActivity.this);
                }

                Log.d(ListCityActivity.class.getName(), "ListView适配器数据的大小：" + stringList.size());
                if (stringList.size() != 2560) {
                    stringList.clear();
                    mHandler.sendEmptyMessage(NOTIFY_CHANGED_ONE_CITY);
                }
                mHandler.sendEmptyMessage(CLOSE_TOAST);
            }
        }).start();

    }

    /**
     * 搜索框响应搜索，列表对应发生变化
     *
     * @param query
     * @return
     */
    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.d(ListCityActivity.class.getName(), "监听函数执行了");
        mGridView.setFilterText(newText);
        //交给过滤器去过滤
        Filter filter;
        filter = citiesAdapterRaw.getFilter();
        filter.filter(newText);

        return true;
    }

    /**
     * 城市点击事件，弹出一个Material AlertDialog，显示城市的详细信息
     *
     * @param parent
     * @param view
     * @param position
     * @param id
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        mGridView.clearTextFilter();

        String key = citiesAdapterRaw.getItem(position);
        final BasicCity basicCity = DATA.basicCities2560.get(key);

        final View showView = LayoutInflater.from(this).inflate(R.layout.alert_dialog_basic_city_info, null);
        ImageView imag = (ImageView) showView.findViewById(R.id.image);
        TextView nameZH = (TextView) showView.findViewById(R.id.cityNameZN);
        TextView nameEH = (TextView) showView.findViewById(R.id.cityNameEN);
        TextView lat = (TextView) showView.findViewById(R.id.lat);
        TextView lon = (TextView) showView.findViewById(R.id.lon);

        nameZH.setText(basicCity.countryZh + " - " + basicCity.provinceZh + " - " + basicCity.cityZh);
        nameEH.setText(basicCity.countryEn + " - " + basicCity.provinceEn + " - " + basicCity.cityEn);
        lat.setText("维度lat: " + basicCity.lat);
        lon.setText("经度lon: " + basicCity.lon);

        int[] src = new int[]{R.drawable.background1, R.drawable.background2, R.drawable.background3,
                R.drawable.background4};
        Random random = new Random(System.currentTimeMillis());
        imag.setImageResource(src[random.nextInt(4)]);

        new AlertDialog.Builder(this)
                .setView(showView)
                .setPositiveButton("添加", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //是否包含该城市
                        if (DATA.chosenCities.contains(basicCity.cityZh)) {
                            showMessage("该城市已被加入，请选择其他城市");
                        } else {
                            DATA.chosenCities.add(basicCity.cityZh);
                            showMessage("添加成功");
                            //存入数据库
                            mPresenter.saveCitiesOnSQLite(DATA.chosenCities);
                            Utils.d("成功添加了一个请求城市");
                        }
                    }
                })
                .create().show();
    }

    @Override
    public void showMessage(String message) {
        Utils.SnackBarTip(getWindow().getDecorView(), message);
    }
}
