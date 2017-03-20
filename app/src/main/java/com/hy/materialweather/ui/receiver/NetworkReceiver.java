package com.hy.materialweather.ui.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.telephony.TelephonyManager;

import com.hy.materialweather.Utils;
import com.hy.materialweather.model.HeWeather5Map;
import com.hy.materialweather.ui.baseui.ListCityUI;

/**
 * Created by Administrator on 2017/3/20.
 */

public class NetworkReceiver extends BroadcastReceiver{
    public static final String TAG = NetworkReceiver.class.getName();

    public final Handler mHandler;

    public NetworkReceiver(Handler handler) {
        this.mHandler = handler;
    }

    @Override
    public void onReceive(final Context context, Intent intent) {
        String action = intent.getAction();
        if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
            Utils.d(TAG + " 网络状态发生了改变");
            //开启线程刷新网络状态，并保存到Methods类中的networkType属性中
            new Thread(new Runnable() {
                @Override
                public void run() {
                    //保存当前的网络类型
                    int temp = HeWeather5Map.networkType;
                    HeWeather5Map.networkType = getAPNType(context);
                    if(temp == 0 && HeWeather5Map.networkType != 0) {
                        Utils.sendEmptyMessage(mHandler, ListCityUI.RECEIVER_QUIRE_REFRESH);
                        Utils.d(TAG + " 变成有网");
                    } else if(temp != 0 && HeWeather5Map.networkType == 0){
                        Utils.sendMessage(mHandler, ListCityUI.PASS_STRING, "你已经进入了没有网络的世界");
                        Utils.sendEmptyMessage(mHandler, ListCityUI.STOP_REFRESHING);
                        Utils.d(TAG + " 变成没网");
                    }
                }
            }).start();
        }
    }

    /**
     * 获取当前的网络状态 ：没有网络0：WIFI网络1：3G网络2：2G网络3
     *
     * @param context
     * @return
     */
    private static int getAPNType(Context context) {
        int netType = 0;
        ConnectivityManager connMgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo == null) {
            return netType;
        }
        int nType = networkInfo.getType();
        if (nType == ConnectivityManager.TYPE_WIFI) {
            netType = 1;// wifi
        } else if (nType == ConnectivityManager.TYPE_MOBILE) {
            int nSubType = networkInfo.getSubtype();
            TelephonyManager mTelephony = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            if (nSubType == TelephonyManager.NETWORK_TYPE_UMTS
                    && !mTelephony.isNetworkRoaming()) {
                netType = 2;// 3G
            } else {
                netType = 3;// 2G
            }
        }
        return netType;
    }
}
