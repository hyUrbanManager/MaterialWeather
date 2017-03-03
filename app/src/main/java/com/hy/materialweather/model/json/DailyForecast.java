package com.hy.materialweather.model.json;

/**
 * 每天的预报
 */
@SuppressWarnings("unchecked")
public class DailyForecast {

    /**
     * 天文数值
     */
    @SuppressWarnings("unchecked")
    public final Astro astro;

    /**
     * 天气描述
     */
    @SuppressWarnings("unchecked")
    public final CondTwo cond;

    /**
     * 预报日期
     */
    public final String date;
    /**
     * 相对湿度（%）
     */
    public final String hum;
    /**
     * 降水量（mm）
     */
    public final String pcpn;
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
    public final Tmp tmp;

    /**
     * 未知
     */
    public final String uv;
    /**
     * 能见度（km）
     */
    public final String vis;

    /**
     * 风力风向
     */
    public final Wind wind;

    public DailyForecast(Astro astro, CondTwo cond, String date, String hum, String pcpn
            , String pop, String pres, Tmp tmp, String uv, String vis, Wind wind) {
        this.astro = astro;
        this.cond = cond;
        this.date = date;
        this.hum = hum;
        this.pcpn = pcpn;
        this.pop = pop;
        this.pres = pres;
        this.tmp = tmp;
        this.uv = uv;
        this.vis = vis;
        this.wind = wind;
    }
}
