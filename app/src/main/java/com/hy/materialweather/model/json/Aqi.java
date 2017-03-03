package com.hy.materialweather.model.json;

/**
 * 指数
 */
public class Aqi {

    public final City city;

    public class City {

        public final String aqi;

        public final String co;

        public final String no2;

        public final String o3;

        public final String pm10;

        public final String pm25;
        /**
         * 共六个级别，分别：优，良，轻度污染，中度污染，重度污染，严重污染
         */
        public final String qlty;

        public final String so2;

        public City(String aqi, String co, String no2, String o3
                , String pm10, String pm25, String qlty, String so2) {
            this.aqi = aqi;
            this.co = co;
            this.no2 = no2;
            this.o3 = o3;
            this.pm10 = pm10;
            this.pm25 = pm25;
            this.qlty = qlty;
            this.so2 = so2;
        }
    }

    public Aqi(City city) {
        this.city = city;
    }
}
