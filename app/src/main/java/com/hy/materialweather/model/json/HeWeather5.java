package com.hy.materialweather.model.json;

import java.util.List;

public class HeWeather5 {

    /**
     * 警报
     */
    public final List<Alarms> alarms;

    /**
     *
     */
    public final Aqi aqi;

    /**
     * 基础信息
     */
    public final Basic basic;

    /**
     * 3天天气预报
     */
    public final List<DailyForecast> daily_forecast;

    /**
     * 小时预报
     */
    public final List<HourlyForecast> hourly_forecast;

    /**
     * 现在
     */
    public final Now now;

    /**
     * 状态
     */
    public final String status;

    /**
     * 生活指数建议
     */
    public final Suggestion suggestion;

    public HeWeather5(List<Alarms> alarms, Aqi aqi, Basic basic, List<DailyForecast> daily_forecast,
                      List<HourlyForecast> hourly_forecast, Now now, String status, Suggestion suggestion) {
        this.alarms = alarms;
        this.aqi = aqi;
        this.basic = basic;
        this.daily_forecast = daily_forecast;
        this.hourly_forecast = hourly_forecast;
        this.now = now;
        this.status = status;
        this.suggestion = suggestion;
    }

}