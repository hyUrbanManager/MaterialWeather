package com.hy.materialweather.model;

/**
 * 请求包
 */
public class WeatherRequestPackage {

    //请求的类型，即末尾加入什么参数
    public int index;

    //城市指定，可以用各种指定，现在先用String类型
    public String city;

    public WeatherRequestPackage(String city) {
        this(5, city);
    }

    public WeatherRequestPackage(int index, String city) {
        this.index = index;
        this.city = city;
    }
}
