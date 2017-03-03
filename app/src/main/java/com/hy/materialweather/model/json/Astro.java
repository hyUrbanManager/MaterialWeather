package com.hy.materialweather.model.json;

/**
 * 天文数值
 */
public class Astro {

    /**
     * 月升时间
     */
    public final String mr;
    /**
     * 月落时间
     */
    public final String ms;
    /**
     * 日出时间
     */
    public final String sr;
    /**
     * 日落时间
     */
    public final String ss;

    public Astro(String mr, String ms, String sr, String ss) {
        this.mr = mr;
        this.ms = ms;
        this.sr = sr;
        this.ss = ss;
    }
}
