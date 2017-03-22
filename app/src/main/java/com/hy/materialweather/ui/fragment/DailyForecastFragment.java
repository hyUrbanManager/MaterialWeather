package com.hy.materialweather.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hy.materialweather.R;
import com.hy.materialweather.Utils;

/**
 * 具体天气信息中的每日天气预报fragment
 */
public class DailyForecastFragment extends Fragment{
    public static final String TAG = DailyForecastFragment.class.getName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Utils.d(TAG + " 创建了Fragment, onCreate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utils.d(TAG + " onCreateView");
        return inflater.inflate(R.layout.content_info_other, container, false);
    }


}
