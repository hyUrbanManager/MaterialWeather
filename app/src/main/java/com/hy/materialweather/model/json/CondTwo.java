package com.hy.materialweather.model.json;

/**
 * 两位的天气
 */
public class CondTwo {

    /**
     * 白天天气代码
     */
    public final String code_d;
    /**
     * 夜晚天气代码
     */
    public final String code_n;
    /**
     * 白天天气描述
     */
    public final String txt_d;
    /**
     * 夜晚天气描述
     */
    public final String txt_n;

    public CondTwo(String code_d, String code_n, String txt_d, String txt_n) {
        this.code_d = code_d;
        this.code_n = code_n;
        this.txt_d = txt_d;
        this.txt_n = txt_n;
    }
}
