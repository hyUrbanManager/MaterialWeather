package com.hy.materialweather.model.json;

/**
 * Json当前的天气状况
 */
public class CondOne {

    //天气状况代码
    public final String code;

    //天气状况描述
    public final String txt;

    public CondOne(String code, String txt) {
        this.code = code;
        this.txt = txt;
    }

    @Override
    public String toString() {
        return "CondOne [code=" + code + ", txt=" + txt + "]";
    }

}
