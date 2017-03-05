package com.hy.materialweather.model;

import android.content.Context;
import android.util.SparseIntArray;

import com.hy.materialweather.R;
import com.hy.materialweather.model.json.BasicCity;
import com.hy.materialweather.model.json.GsonUtils;
import com.hy.materialweather.model.json.HeWeather5;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 全局的数据列表
 */
public class HeWeather5Map {

    //当前网络已经获取过的对象集合
    public static Map<String, HeWeather5> heWeather5HashMap = new HashMap<>();

    //存在本地的节点,2560个城市，选择城市的时候再从String中读取 String大小，572K
    public static List<BasicCity> basicCities2560 = null;

    //设置要求查看的城市
    public static Set<String> citiesChosen = new HashSet<>();

    //天气状况代码
    public static SparseIntArray condMap = new SparseIntArray();

    /**
     * 初始化天气代码和图片的对应表
     */
    public static final void initCondMap() {

        //读取入所有的列表
        condMap.append(100,R.mipmap.w100);
        condMap.append(101,R.mipmap.w101);
        condMap.append(102,R.mipmap.w102);
        condMap.append(103,R.mipmap.w103);
        condMap.append(104,R.mipmap.w104);
        condMap.append(200,R.mipmap.w200);
        condMap.append(201,R.mipmap.w201);
        condMap.append(202,R.mipmap.w202);
        condMap.append(203,R.mipmap.w203);
        condMap.append(204,R.mipmap.w204);
        condMap.append(205,R.mipmap.w205);
        condMap.append(206,R.mipmap.w206);
        condMap.append(207,R.mipmap.w207);
        condMap.append(208,R.mipmap.w208);
        condMap.append(209,R.mipmap.w209);
        condMap.append(210,R.mipmap.w210);
        condMap.append(211,R.mipmap.w211);
        condMap.append(212,R.mipmap.w212);
        condMap.append(213,R.mipmap.w213);
        condMap.append(300,R.mipmap.w300);
        condMap.append(301,R.mipmap.w301);
        condMap.append(302,R.mipmap.w302);
        condMap.append(303,R.mipmap.w303);
        condMap.append(304,R.mipmap.w304);
        condMap.append(305,R.mipmap.w305);
        condMap.append(306,R.mipmap.w306);
        condMap.append(307,R.mipmap.w307);
        condMap.append(308,R.mipmap.w308);
        condMap.append(309,R.mipmap.w309);
        condMap.append(310,R.mipmap.w310);
        condMap.append(311,R.mipmap.w311);
        condMap.append(312,R.mipmap.w312);
        condMap.append(313,R.mipmap.w313);
        condMap.append(400,R.mipmap.w400);
        condMap.append(401,R.mipmap.w401);
        condMap.append(402,R.mipmap.w402);
        condMap.append(403,R.mipmap.w403);
        condMap.append(404,R.mipmap.w404);
        condMap.append(405,R.mipmap.w405);
        condMap.append(406,R.mipmap.w406);
        condMap.append(407,R.mipmap.w407);
        condMap.append(500,R.mipmap.w500);
        condMap.append(501,R.mipmap.w501);
        condMap.append(502,R.mipmap.w502);
        condMap.append(503,R.mipmap.w503);
        condMap.append(504,R.mipmap.w504);
        condMap.append(507,R.mipmap.w507);
        condMap.append(508,R.mipmap.w508);
        condMap.append(900,R.mipmap.w900);
        condMap.append(901,R.mipmap.w901);
        condMap.append(999,R.mipmap.w999);

    }

    /**
     * 2560城市List初始化，全局对象，占500k内存
     */
    public static final void init2560Cities(Context context) {
        basicCities2560 = GsonUtils.getObjectList(context.getString(R.string.cities_json), BasicCity.class);
    }

}
