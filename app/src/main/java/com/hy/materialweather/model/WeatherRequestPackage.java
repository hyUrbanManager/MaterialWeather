package com.hy.materialweather.model;

/**
 * 请求包
 */
public class WeatherRequestPackage {

    //请求的类型，即末尾加入什么参数
    public int index;

    //城市指定，可以用各种指定，现在先用String类型
    public String city;

    //显示的位置
    public int list_position;

    public WeatherRequestPackage(String city, int list_position) {
        this(5, city, list_position);
    }

    public WeatherRequestPackage(int index, String city, int list_position) {
        this.index = index;
        this.city = city;
        this.list_position = list_position;
    }
}
