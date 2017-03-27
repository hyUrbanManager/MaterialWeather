package com.hy.materialweather.ui.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.hy.materialweather.R;
import com.hy.materialweather.uitls.Utils;

/**
 * Alarm 预警碎片
 * 因为不能控制在LinearLayout中的位置，使用View来代替
 */
@Deprecated
public class AlarmFragment extends Fragment {
    private static final String TAG = AlarmFragment.class.getName() + " ";

    //设置好数据，在onCreateView中调用View设置
    private View rootView;
    private CharSequence title;
    private CharSequence txt;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Utils.d(TAG + "onCreateView方法调用");
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.content_info_alarm, container, false);
        }
        TextView titleView = (TextView) rootView.findViewById(R.id.title);
        titleView.setText(title);
        TextView txtView = (TextView) rootView.findViewById(R.id.text);
        txtView.setText(txt);
        return rootView;
    }

    /**
     * 预警标题
     *
     * @param title
     */
    public void setTitle(CharSequence title) {
        this.title = title;
    }

    /**
     * 预警详细内容
     *
     * @param txt
     */
    public void setTxt(CharSequence txt) {
        this.txt = txt;
    }
}
