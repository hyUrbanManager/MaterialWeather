package com.hy.materialweather.model.json;

/**
 * 天气预报
 */
public class HourlyForecast {

    /**
     * 天气状况
     */
    public final CondOne cond;

    /**
     * 预报日期
     */
    public final String date;
    /**
     * 相对湿度（%）
     */
    public final String hum;
    /**
     * 降水概率
     */
    public final String pop;
    /**
     * 气压
     */
    public final String pres;
    /**
     * 温度
     */
    public final String tmp;

    /**
     * 风力风向
     */
    public final Wind wind;

    public HourlyForecast(CondOne cond, String date, String hum, String pop
            , String pres, String tmp, Wind wind) {
        this.cond = cond;
        this.date = date;
        this.hum = hum;
        this.pop = pop;
        this.pres = pres;
        this.tmp = tmp;
        this.wind = wind;
    }
}
