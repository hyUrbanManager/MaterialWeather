package com.hy.materialweather.model.json;

/**
 * 温度
 */
public class Tmp {

    /**
     * 最高温度
     */
    public final String max;

    /**
     * 最低温度
     */
    public final String min;

    public Tmp(String max, String min) {
        this.max = max;
        this.min = min;
    }
}
