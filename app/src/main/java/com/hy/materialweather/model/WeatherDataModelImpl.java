package com.hy.materialweather.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.util.Log;

import com.hy.materialweather.R;
import com.hy.materialweather.Utils;
import com.hy.materialweather.model.basemodel.WeatherDataModel;
import com.hy.materialweather.model.json.CityInfo;
import com.hy.materialweather.model.json.GsonUtils;
import com.hy.materialweather.model.json.HeWeather5;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import static android.R.id.list;
import static com.hy.materialweather.Utils.d;
import static com.hy.materialweather.model.json.GsonUtils.getObjectList;

/**
 *
 */
public class WeatherDataModelImpl implements WeatherDataModel {
    public final String TAG = WeatherDataModelImpl.class.getName() + "类下";

    public static final String NO_KEY = "no key";

    //OkHttp连接类
    public static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .build();
    Call call;

    //获取字符串资源
    final String[] UrlAppend;
    final String[] params;
    final String rawUrl;
    private String weatherKey = NO_KEY;
    final String cities_manager;
    final String style_key;
    final String myServerAdreess;

    //SQLite对象
    private final SharedPreferences mPreferences;

    /**
     * 传入Activity对象获取Context进而获取到字符串资源
     *
     * @param context
     */
    public WeatherDataModelImpl(Context context) {
        Resources resources = context.getResources();
        UrlAppend = resources.getStringArray(R.array.weatherUrlAppend);
        params = resources.getStringArray(R.array.weatherUrlPostParams);
        rawUrl = resources.getString(R.string.weatherUrl);

        cities_manager = resources.getString(R.string.cities_manager);
        style_key = resources.getString(R.string.style_key);

        myServerAdreess = resources.getString(R.string.myserveraddress);

        //获取文件，文件名为App的名字
        mPreferences = context.getSharedPreferences(resources.getString(R.string.app_name)
                , Context.MODE_PRIVATE);
    }

    @Override
    public HeWeather5 parseWeahterJson(String json) {
        //头尾没有'[',']'的话，加入
        json = '[' + json + ']';

        List<CityInfo> city = GsonUtils.getObjectList(json, CityInfo.class);
        if (city != null) {
            return city.get(0).HeWeather5.get(0);
        }
        return null;
    }

    @Override
    public List<CityInfo> getWeatherFromSQLite(String city) {
        //查找数据
        String json = mPreferences.getString(city, "null");
        if (json.equals("null")) {
            return new ArrayList<>();
        } else {
            //解析Json
            return getObjectList(json, CityInfo.class);
        }
    }

    @Override
    public boolean saveWeatherOnSQLite(List<CityInfo> cityInfo) {
        //获取引用
        SharedPreferences.Editor editor = mPreferences.edit();

        //存入Json字符串
        String info = GsonUtils.toJson(cityInfo);
        String city = cityInfo.get(0).HeWeather5.get(0).basic.city;
        editor.putString(city, info);

        //提交
        editor.commit();
        return false;
    }

    /**
     * 获取记录，用户要查看的城市
     *
     * @return
     */
    @Override
    public List<String> getCitiesOnSQLite() {
        //把json字符串读出来
        List<String> list;
        String json = mPreferences.getString(cities_manager, null);
        Log.d(TAG, "解析城市列表，List<String>类型，解析到的Json数据是： " + json);
        if (json != null) {
            list = GsonUtils.parseJsonArrayWithGson(json, String.class);
            return list;
        }
        return new ArrayList<>();
    }

    /**
     * 把list转换成json字符串传入
     *
     * @param list
     */
    @Override
    public void saveCitiesOnSQLite(List<String> list) {
        //获取引用
        SharedPreferences.Editor editor = mPreferences.edit();
        //存入Json字符串
        String info = GsonUtils.toJson(list);
        editor.putString(cities_manager, info);

        //提交
        editor.commit();
    }


    /**
     * 异步访问天气数据，访问网络数据
     *
     * @param requestPackage 请求的数据，封装成包
     * @param callback
     */
    @Override
    public void weatherInternetService(WeatherRequestPackage requestPackage, Callback callback) {
        //没有获取到key的时候返回
        if (weatherKey.equals(NO_KEY)) {
            return;
        }

        //获取Url
        String url = rawUrl + UrlAppend[requestPackage.index];

        //加入Post参数
        RequestBody formBody = new FormBody.Builder()
                .add(params[0], requestPackage.city)
                .add(params[1], weatherKey)
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        call = client.newCall(request);
        try {
            call.enqueue(callback);
        } catch (IllegalStateException e) {
            d(TAG + " 发生了重复递交申请" + e.getMessage());
        }
    }

    /**
     * 同步从服务器获取我的api key
     * 耗时方法。
     *
     * @return
     * @throws IOException
     */
    public Response getKeyFromMyServer() throws IOException {
        //获取Url
        String url = myServerAdreess;

        //加入Post参数
        RequestBody formBody = new FormBody.Builder()
                .add("user", "h")
                .add("password", "huangye")
                .build();

        Request request = new Request.Builder()
                .post(formBody)
                .url(url)
                .build();

        call = client.newCall(request);
        return call.execute();
    }

    /**
     * 数据模型里设置ApiKey
     */
    public void setKey(String key) {
        this.weatherKey = key;
    }

    /**
     * 返回是否已经获取到key了
     *
     * @return
     */
    public boolean isKeyGet() {
        return !(this.weatherKey == NO_KEY);
    }

    /**
     * 读取显示的风格，没有保存则返回Raw
     *
     * @return
     */
    @Override
    public int getStyleOnSQLite() {
        //把json字符串读出来
        List<String> list;
        int style = mPreferences.getInt(style_key, 0);
        Utils.d(TAG + "读取到的显示风格为： " + style);
        return style;
    }

    /**
     * 存入显示的风格
     *
     * @param style
     */
    @Override
    public void saveStyleOnSQLite(int style) {
        //获取引用
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(style_key, style);

        //提交
        editor.commit();
    }

    /**
     * locationCity,chosenCities style 存入其中
     */
    @Override
    public void saveOnSQLite() {
//获取引用
        SharedPreferences.Editor editor = mPreferences.edit();
        //存入Json字符串
        String info = GsonUtils.toJson(list);
        editor.putString(cities_manager, info);

        //提交
        editor.commit();
    }

    @Override
    public void getOnSQLite() {
//把json字符串读出来
//        List<String> list;
//        String json = mPreferences.getString(cities_manager, null);
//        Log.d(TAG, "解析城市列表，List<String>类型，解析到的Json数据是： " + json);
//        if (json != null) {
//            list = GsonUtils.parseJsonArrayWithGson(json, String.class);
//            return list;
//        }
//        return null;
    }
}
