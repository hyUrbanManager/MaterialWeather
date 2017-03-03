package com.hy.materialweather.model.json;

/**
 * 风力风向
 */
public class Wind {

    /**
     * 风向（360度）
     */
    public final String deg;
    /**
     * 风向
     */
    public final String dir;
    /**
     * 风力
     */
    public final String sc;
    /**
     * 风速（kmph）
     */
    public final String spd;

    public Wind(String deg, String dir, String sc, String spd) {
        this.deg = deg;
        this.dir = dir;
        this.sc = sc;
        this.spd = spd;
    }

    @Override
    public String toString() {
        return "Wind [deg=" + deg + ", dir=" + dir + ", sc=" + sc + ", spd=" + spd + "]";
    }

}
