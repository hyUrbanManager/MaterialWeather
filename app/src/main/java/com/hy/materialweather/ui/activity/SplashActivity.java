package com.hy.materialweather.ui.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;

import com.hy.materialweather.Utils;

import java.lang.ref.WeakReference;

public class SplashActivity extends AppCompatActivity {
    public static final String TAG = SplashActivity.class.getName();
    private SwitchHandler mHandler = new SwitchHandler(this);
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //activity切换的淡入淡出效果
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
        super.onCreate(savedInstanceState);
        mHandler.sendEmptyMessage(1);
        Utils.d(TAG + " 启动MainActivity");
    }

    private static class SwitchHandler extends Handler {
        private WeakReference<SplashActivity> mWeakReference;

        SwitchHandler(SplashActivity activity) {
            mWeakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            SplashActivity activity = mWeakReference.get();
            if (activity != null) {
                MainActivity.launch(activity);
                activity.finish();
            }
        }
    }
}
