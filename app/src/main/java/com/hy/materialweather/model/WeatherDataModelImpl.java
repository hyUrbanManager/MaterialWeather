package com.hy.materialweather.model;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;

import com.hy.materialweather.R;
import com.hy.materialweather.model.basemodel.WeatherDataModel;
import com.hy.materialweather.model.json.CityInfo;
import com.hy.materialweather.model.json.GsonUtils;
import com.hy.materialweather.model.json.HeWeather5;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

import static com.hy.materialweather.model.json.GsonUtils.getObjectList;

/**
 *
 */
public class WeatherDataModelImpl implements WeatherDataModel {

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
    final String weatherKey;

    //SQLite对象
    SharedPreferences preferences;

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
        weatherKey = resources.getString(R.string.weatherKey);

        //获取文件，文件名为App的名字
        preferences = context.getSharedPreferences(resources.getString(R.string.app_name)
                , Context.MODE_PRIVATE);
    }

    @Override
    public HeWeather5 parseWeahterJson(String json) {
        //头尾没有'[',']'的话，加入
        json = '[' + json + ']';

        List<CityInfo> city = GsonUtils.getObjectList(json, CityInfo.class);
        if(city != null) {
            return city.get(0).HeWeather5.get(0);
        }
        return  null;
    }

    @Override
    public List<CityInfo> getWeatherFromSQLite(String city) {
        //查找数据
        String json = preferences.getString(city, "null");
        if(json.equals("null")) {
            return null;
        } else {
            //解析Json
            return getObjectList(json, CityInfo.class);
        }
    }

    @Override
    public boolean saveWeatherOnSQLite(List<CityInfo> cityInfo) {
        //获取引用
        SharedPreferences.Editor editor = preferences.edit();

        //存入Json字符串
        String info = GsonUtils.toJson(cityInfo);
        String city = cityInfo.get(0).HeWeather5.get(0).basic.city;
        editor.putString(city, info);

        //提交
        editor.commit();
        return false;
    }

    /**
     * Callback为空的时候，访问网络数据
     *
     * @param requestPackage 请求的数据，封装成包
     * @param callback
     */
    @Override
    public void weatherInternetService(WeatherRequestPackage requestPackage, Callback callback) {
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
        call.enqueue(callback);
    }

}
