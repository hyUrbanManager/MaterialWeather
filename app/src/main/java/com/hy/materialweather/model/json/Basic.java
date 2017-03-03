package com.hy.materialweather.model.json;

/**
 * Json天气信息所有的Basic信息
 * Json数据对象构造器统一放最后
 */
public class Basic {

    /**
     * 城市名称
     */
    public final String city;
    /**
     * 国家
     */
    public final String cnty;
    /**
     * 城市ID
     */
    public final String id;
    /**
     * 城市经度
     */
    public final String lat;
    /**
     * 城市纬度
     */
    public final String lon;

    /** 城市所属省份（仅限国内城市）,测试到实际返回的数据没有这一项 */
//    public String prov;

    /**
     * 更新时间
     */
    public final Update update;

    public class Update {

        /**
         * 当地时间
         */
        public final String loc;
        /**
         * UTC时间
         */
        public final String utc;

        @Override
        public String toString() {
            return "update [loc=" + loc + ", utc=" + utc + "]";
        }

        public Update(String loc, String utc) {
            this.loc = loc;
            this.utc = utc;
        }
    }

    @Override
    public String toString() {
        return "Basic [city=" + city + ", cnty=" + cnty + ", id=" + id + ", lon=" + lon + ", update=" + update
                + "]";
    }

    /**
     * 构造器，没什么用，仅为了final不报错
     *
     * @param city
     * @param cnty
     * @param id
     * @param lon
     * @param update
     */
    public Basic(String city, String cnty, String id, String lat, String lon, Update update) {
        this.city = city;
        this.cnty = cnty;
        this.id = id;
        this.lat = lat;
        this.lon = lon;
        this.update = update;
    }
}
