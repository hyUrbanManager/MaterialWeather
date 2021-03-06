package com.hy.materialweather;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.Closeable;
import java.io.IOException;

/**
 * 派生工具
 */
public class Utils implements Closeable {

    public static final String TAG = Utils.class.getName() + " 测试工具类输出信息：";

    @Override
    public void close() throws IOException {

    }

    /**
     * 发送消息，两个方法
     *
     * @param handler
     * @param what
     * @return
     */
    public static final boolean sendEmptyMessage(Handler handler, int what) {
        return sendMessage(handler, what, null);
    }

    public static final boolean sendMessage(Handler handler, int what, Object obj) {
        if (handler != null) {
            Message msg = Message.obtain();
            msg.what = what;
            msg.obj = obj;
            handler.sendMessage(msg);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 显示给用户的提示消息
     *
     * @param message
     */
    public static final void Toast(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    /**
     * 关闭异常
     * @param resource
     */
    public static final void close(Closeable resource) {
        try {
            resource.close();
        } catch (IOException e) {
            e(e.getMessage());
        }
    }

    /**
     * 输出工具
     * @param message
     */
    public static final void d(String message) {
        Log.d(TAG, message);
    }

    /**
     * 输出工具
     * @param message
     */
    public static final void e(String message) {
        Log.e(TAG, message);
    }

    /**
     * 输出工具
     */
    public static final void dCurrentThread() {
        Log.d(TAG, "当前进程：" + Thread.currentThread().getName());
        Log.d(TAG, "当前时间：" + SystemClock.currentThreadTimeMillis());

    }

    /**
     * SnackBar
     * @param decorView
     * @param message
     */
    public static void SnackBarTip(View decorView, String message) {
        Snackbar.make(decorView, message,Snackbar.LENGTH_LONG).show();
    }
}
