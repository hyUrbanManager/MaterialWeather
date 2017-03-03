package com.hy.materialweather.model.json;

/**
 * Json类，得到信息
 */
public class Now implements DJson {

    /**
     * 天气状况
     */
    public final CondOne cond;

    /**
     * 体感温度
     */
    public final String fl;
    /**
     * 相对湿度（%）
     */
    public final String hum;
    /**
     * 降水量（mm）
     */
    public final String pcpn;
    /**
     * 气压
     */
    public final String pres;
    /**
     * 温度
     */
    public final String tmp;
    /**
     * 能见度（km）
     */
    public final String vis;

    /**
     * 风向情况
     */
    public final Wind wind;

    public Now(CondOne cond, String fl, String hum, String pcpn, String pres, String tmp
            , String vis, Wind wind) {
        this.cond = cond;
        this.fl = fl;
        this.hum = hum;
        this.pcpn = pcpn;
        this.pres = pres;
        this.tmp = tmp;
        this.vis = vis;
        this.wind = wind;
    }

}