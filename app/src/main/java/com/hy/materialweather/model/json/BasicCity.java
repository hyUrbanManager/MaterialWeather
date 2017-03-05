package com.hy.materialweather.model.json;

/**
 * 所有城市基础信息查询用到的类
 */
public class BasicCity {

    //"CN101010100"
    public final String id;
    //beijing
    public final String cityEn;
    //北京
    public final String cityZh;
    //CN
    public final String countryCode;
    //China
    public final String countryEn;
    //中国
    public final String countryZn;
    //beijing
    public final String provinceEn;
    //北京
    public final String provinceZh;
    //beijing
    public final String leaderEn;
    //北京
    public final String leaderZh;
    //39.904
    public final String lat;
    //116.391
    public final String lon;

    public BasicCity(String id, String cityEn, String cityZh, String countryCode, String countryEn,
                     String countryZn, String provinceEn, String provinceZh, String leaderEn,
                     String leaderZh, String lat, String lon) {
        this.id = id;
        this.cityEn = cityEn;
        this.cityZh = cityZh;
        this.countryCode = countryCode;
        this.countryEn = countryEn;
        this.countryZn = countryZn;
        this.provinceEn = provinceEn;
        this.provinceZh = provinceZh;
        this.leaderEn = leaderEn;
        this.leaderZh = leaderZh;
        this.lat = lat;
        this.lon = lon;
    }
}
