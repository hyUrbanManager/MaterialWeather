package com.hy.materialweather.model.json;

/**
 * 建议
 */
public class Suggestion {

    /**
     * 空气指数
     */
    public final Air air;

    public class Air {
        /**
         * 描述
         */
        public final String brf;
        /**
         * 具体描述
         */
        public final String txt;

        public Air(String brf, String txt) {
            this.brf = brf;
            this.txt = txt;
        }
    }

    /**
     * 舒适度指数
     */
    public final Comf comf;

    public class Comf {
        /**
         * 描述
         */
        public final String brf;
        /**
         * 具体描述
         */
        public final String txt;

        public Comf(String brf, String txt) {
            this.brf = brf;
            this.txt = txt;
        }
    }

    /**
     * 洗车指数
     */
    public final Cw cw;

    public class Cw {
        /**
         * 描述
         */
        public final String brf;
        /**
         * 具体描述
         */
        public final String txt;

        public Cw(String brf, String txt) {
            this.brf = brf;
            this.txt = txt;
        }
    }

    /**
     * 穿衣指数
     */
    public final Drsg drsg;

    public class Drsg {
        /**
         * 描述
         */
        public final String brf;
        /**
         * 具体描述
         */
        public final String txt;

        public Drsg(String brf, String txt) {
            this.brf = brf;
            this.txt = txt;
        }
    }

    /**
     * 感冒指数
     */
    public final Flu flu;

    public class Flu {
        /**
         * 描述
         */
        public final String brf;
        /**
         * 具体描述
         */
        public final String txt;

        public Flu(String brf, String txt) {
            this.brf = brf;
            this.txt = txt;
        }
    }

    /**
     * 运动指数
     */
    public final Sport sport;

    public class Sport {
        /**
         * 描述
         */
        public final String brf;
        /**
         * 具体描述
         */
        public final String txt;

        public Sport(String brf, String txt) {
            this.brf = brf;
            this.txt = txt;
        }
    }

    /**
     * 旅游指数
     */
    public final Tray trav;

    public class Tray {
        /**
         * 描述
         */
        public final String brf;
        /**
         * 具体描述
         */
        public final String txt;

        public Tray(String brf, String txt) {
            this.brf = brf;
            this.txt = txt;
        }
    }

    /**
     * 紫外线指数
     */
    public final Uv uv;

    public class Uv {
        /**
         * 描述
         */
        public final String brf;
        /**
         * 具体描述
         */
        public final String txt;

        public Uv(String brf, String txt) {
            this.brf = brf;
            this.txt = txt;
        }
    }

    public Suggestion(Air air, Comf comf, Cw cw, Drsg drsg, Flu flu
            , Sport sport, Tray tray, Uv uv) {
        this.air = air;
        this.comf = comf;
        this.cw = cw;
        this.drsg = drsg;
        this.flu = flu;
        this.sport = sport;
        this.trav = tray;
        this.uv = uv;
    }
}
