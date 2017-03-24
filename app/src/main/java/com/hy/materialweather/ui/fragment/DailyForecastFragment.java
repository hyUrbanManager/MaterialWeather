package com.hy.materialweather.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hy.materialweather.R;
import com.hy.materialweather.Utils;
import com.hy.materialweather.model.json.DailyForecast;

/**
 * 具体天气信息中的每日天气预报fragment
 */
public class DailyForecastFragment extends Fragment {
    public static final String TAG = DailyForecastFragment.class.getName();

    /* 数据项 */
    private DailyForecast dailyForecast;
    private View root;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.d(TAG + " 创建了Fragment, onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utils.d(TAG + " onCreateView");
        if (root != null) {
            return root;
        }
        root = inflater.inflate(R.layout.content_info_other, container, false);

        TextView mr, ms, sr, ss, hum, pop, pres, vis, cond, dir, sc, spd;
        mr = (TextView) root.findViewById(R.id.moonRise);
        ms = (TextView) root.findViewById(R.id.moonSet);
        sr = (TextView) root.findViewById(R.id.sunRise);
        ss = (TextView) root.findViewById(R.id.sunSet);
        hum = (TextView) root.findViewById(R.id.hum);
        pop = (TextView) root.findViewById(R.id.pop);
        pres = (TextView) root.findViewById(R.id.pres);
        vis = (TextView) root.findViewById(R.id.vis);
        cond = (TextView) root.findViewById(R.id.cond);
        dir = (TextView) root.findViewById(R.id.dir);
        sc = (TextView) root.findViewById(R.id.sc);
        spd = (TextView) root.findViewById(R.id.spd);

        if (dailyForecast != null && dailyForecast.astro != null) {
            mr.setText(dailyForecast.astro.mr == null ? "00:00" : dailyForecast.astro.mr);
            ms.setText(dailyForecast.astro.ms == null ? "00:00" : dailyForecast.astro.ms);
            sr.setText(dailyForecast.astro.sr == null ? "00:00" : dailyForecast.astro.sr);
            ss.setText(dailyForecast.astro.ss == null ? "00:00" : dailyForecast.astro.ss);
        } else {
            mr.setText("00:00");
            ms.setText("00:00");
            sr.setText("00:00");
            ss.setText("00:00");
        }

        if(dailyForecast != null) {
            hum.setText((dailyForecast.hum == null ? "0" : dailyForecast.hum) + "% 相对湿度");
            pop.setText((dailyForecast.pop == null ? "0" : dailyForecast.pop) + "% 降水概率");
            pres.setText((dailyForecast.pres == null ? "0" : dailyForecast.pres) + "hpa 气压");
            vis.setText((dailyForecast.vis == null ? "0" : dailyForecast.vis) + "km 能见度");
        } else {
            hum.setText("0% 相对湿度");
            pop.setText("0% 降水概率");
            pres.setText("0hpa 气压");
            vis.setText("0km 能见度");
        }

        if (dailyForecast != null && dailyForecast.cond != null) {
            cond.setText(dailyForecast.cond.txt_d == null ? "未知" : dailyForecast.cond.txt_d);
            cond.append(" -- ");
            cond.append(dailyForecast.cond.txt_n == null ? "未知" : dailyForecast.cond.txt_n);
        } else {
            cond.setText("未知 -- 未知");
        }

        if (dailyForecast != null && dailyForecast.wind != null) {
            dir.setText(dailyForecast.wind.dir == null ? "未知风向" : dailyForecast.wind.dir);
            sc.setText(dailyForecast.wind.sc == null ? "未知风力" : dailyForecast.wind.sc);
            spd.setText((dailyForecast.wind.spd == null ? "0" : dailyForecast.wind.spd) + "km每小时");
        } else {
            dir.setText("未知风向");
            sc.setText("未知风力");
            spd.setText("0km每小时");
        }
        return root;
    }

    /**
     * 设置数据，填充每个view的数据
     *
     * @param data
     */
    public void setData(DailyForecast data) {
        this.dailyForecast = data;
    }

}
