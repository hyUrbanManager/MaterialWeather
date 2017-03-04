package com.hy.materialweather.model.json;

/**
 * 警报数据
 */
public class Alarms {

    /**
     * 等级：蓝色
     */
    public final String level;

    /**
     * 状态：预警中
     */
    public final String stat;

    /**
     * 标题
     */
    public final String title;

    /**
     * 具体表述
     */
    public final String txt;

    /**
     * 类型： 大风
     */
    public final String type;

    public Alarms(String level, String stat, String title, String txt, String type) {
        this.level = level;
        this.stat = stat;
        this.title = title;
        this.txt = txt;
        this.type = type;
    }
}
