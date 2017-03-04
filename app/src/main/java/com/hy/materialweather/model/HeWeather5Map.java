package com.hy.materialweather.model;

import android.content.Context;
import android.util.SparseIntArray;

import com.hy.materialweather.R;
import com.hy.materialweather.model.json.HeWeather5;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局的数据列表
 */
public class HeWeather5Map {

    //对象集合
    public static Map<String, HeWeather5> HE_WEATHER_5_MAP = new HashMap<>();

    //天气状况代码
    public static int[] condIds;
    public static SparseIntArray condMap = new SparseIntArray();

    public static final void initCond(Context context) {

        //TODO add all conds
        condIds = context.getResources().getIntArray(R.array.cond);
        for (int i = 0; i < condIds.length; i++) {
            condMap.append(101, condIds[i]);
        }

    }

    //所有城市集合

}
