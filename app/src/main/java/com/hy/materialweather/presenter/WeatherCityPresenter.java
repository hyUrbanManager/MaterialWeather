package com.hy.materialweather.presenter;

import android.content.Context;

import com.hy.materialweather.Utils;
import com.hy.materialweather.basemvpcomponent.BasePresenter;
import com.hy.materialweather.basemvpcomponent.MVPActivity;
import com.hy.materialweather.model.HeWeather5Map;
import com.hy.materialweather.model.WeatherDataModelImpl;
import com.hy.materialweather.model.WeatherRequestPackage;
import com.hy.materialweather.model.basemodel.WeatherDataModel;
import com.hy.materialweather.model.json.Basic;
import com.hy.materialweather.model.json.CondOne;
import com.hy.materialweather.model.json.HeWeather5;
import com.hy.materialweather.model.json.Now;
import com.hy.materialweather.ui.baseui.ListCityUI;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class WeatherCityPresenter extends BasePresenter<ListCityUI> {
    public static final String TAG = WeatherCityPresenter.class.getName() + "类";

    protected WeatherDataModel model;
    protected ListCityUI viewInterface;

    protected MVPActivity.MVPHandler mHandler;

    /**
     * 构造器，把Handler和View实例都获取到
     *
     * @param context
     * @param viewInterface
     * @param mHandler
     */
    public WeatherCityPresenter(Context context, ListCityUI viewInterface, MVPActivity.MVPHandler mHandler) {
        this.model = new WeatherDataModelImpl(context);
        this.viewInterface = viewInterface;
        this.mHandler = mHandler;
    }

    /**
     * 从网络中获取数据
     *
     * @param requestPackage
     */
    public void weatherReportOnInternet(final WeatherRequestPackage requestPackage) {
        //首先检查Key，同步耗时方法
        if (!HeWeather5Map.isKeyCorrect) {
            //异步方法获取key，再异步方法递交请求
            getKeyFromMyServer();
        }
        //网络获取数据，Callback中对UI更新
        model.weatherInternetService(requestPackage, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Utils.sendMessage(mHandler, ListCityUI.PASS_STRING, e.getMessage());
                Utils.sendEmptyMessage(mHandler, ListCityUI.CLOSE_TOAST);
                Utils.sendEmptyMessage(mHandler, ListCityUI.STOP_REFRESHING);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                handlerHeWeather5Response(response, requestPackage);
            }
        });

    }

    /**
     * 连接个人服务器获取我的key
     *
     * @return
     */
    public void getKeyFromMyServer() {
        Utils.d(TAG + " 执行了一次获取Key");
        try {
            Response response = model.getKeyFromMyServer();
            String result = response.body().string();
            if (result.equals("password error")) {
                //密码错误
                Utils.sendMessage(mHandler, ListCityUI.PASS_STRING, "服务器获取key失败\n" + result);
                Utils.d("获取key错误");
                HeWeather5Map.isKeyCorrect = false;
            } else {
                model.setKey(result);
                Utils.d("获取key成功 " + result);
                //首先设定为有效
                HeWeather5Map.isKeyCorrect = true;
            }
        } catch (IOException e) {
            Utils.d(TAG + " 获取key失败" + e.getMessage());
            HeWeather5Map.isKeyCorrect = false;
            Utils.sendMessage(mHandler, ListCityUI.PASS_STRING, "网络出错\n" + e.getMessage());
            Utils.sendEmptyMessage(mHandler, ListCityUI.STOP_REFRESHING);
        }
    }

    /**
     * Http回调时，处理回调的数据
     *
     * @param response
     * @param requestPackage
     */
    void handlerHeWeather5Response(Response response, WeatherRequestPackage requestPackage) {
        //调用Model来解析数据，变成数据对象
        HeWeather5 heWeather5;
        String result;
        try {
            result = response.body().string();
            heWeather5 = model.parseWeahterJson(result);
        } catch (Exception e) {
            Utils.sendMessage(mHandler, ListCityUI.PASS_STRING, e.getMessage());
            Utils.sendEmptyMessage(mHandler, ListCityUI.CLOSE_TOAST);
            return;
        }

        //Key无效
        if (heWeather5.status.equals("invalid key")) {
            //和MainActivity的Handle耦合，相应比一些不设置为Null
            HeWeather5 h = new HeWeather5(null, null,
                    new Basic("封闭KEY", null, null, null, null, null, null),
                    null, null,
                    new Now(new CondOne(null, null), null, null, null, null, null, null, null), null, null);
            viewInterface.addOneCity(h, requestPackage.list_position);
            HeWeather5Map.isKeyCorrect = false;
            return;

        } else {
            //Key有效
            HeWeather5Map.isKeyCorrect = true;

            //把数据保存到全局Map，当前获取到的实时信息
            HeWeather5Map.heWeather5HashMap.put(heWeather5.basic.city, heWeather5);

            //把收到的数据保存在本地数据库


            //发送数据对象给UI界面，UI细致化处理界面数据
            StringBuilder sb = new StringBuilder();
            sb.append(heWeather5.toString());
            sb.append("\n\n" + "成功获取到对象");

            //报异常的语句，解决
            sb.append(heWeather5.basic.city);
            sb.append(heWeather5.daily_forecast.get(0).wind);
            sb.append('\n' + heWeather5.suggestion.sport.txt);

            Utils.sendEmptyMessage(mHandler, ListCityUI.CLOSE_TOAST);

            viewInterface.addOneCity(heWeather5, requestPackage.list_position);
        }
    }

    /**
     * Api key有没有获取到
     *
     * @return
     */
    public boolean isKeyGet() {
        return model.isKeyGet();
    }

    /**
     * 获取记录，用户要查看的城市
     *
     * @return
     */
    public List<String> getCitiesOnSQLite() {
        return model.getCitiesOnSQLite();
    }

    /**
     * 读取显示的风格，没有保存则返回Raw
     *
     * @return
     */
    public int getStyleOnSQLite() {
        return model.getStyleOnSQLite();
    }

    /**
     * 存入显示的风格
     *
     * @param style
     */
    public void saveStyleOnSQLite(int style) {
        model.saveStyleOnSQLite(style);
    }

    /**
     * 把list转换成json字符串传入
     *
     * @param list
     */
    public void saveCitiesOnSQLite(List<String> list) {
        model.saveCitiesOnSQLite(list);
    }

}
